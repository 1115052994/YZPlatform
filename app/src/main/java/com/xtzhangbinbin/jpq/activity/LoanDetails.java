package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.LoanDetailsAdapter;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.LoanDetailsBean;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class LoanDetails extends BaseActivity{
    @BindView(R.id.browsing_Cardeal_image)
    ImageView browsingCardealImage;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private List<LoanDetailsBean.DataBean.ResultBean> result = new ArrayList<>();
    private LoanDetailsAdapter carCreditAdapter;
    private int pageIndex;
    private int pageTotal;
    private String auth_comp_city;
    private String dict_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        dict_id = intent.getStringExtra("dict_id");//得到id
        //设置线性管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carCreditAdapter = new LoanDetailsAdapter(this, result);
        recyclerView.setAdapter(carCreditAdapter);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageIndex = 1;
                result.clear();
                getData(1,dict_id,auth_comp_city,refreshlayout);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if(pageIndex < pageTotal){
                    getData(++pageIndex,dict_id,auth_comp_city, refreshlayout);
                }else {
                    refreshlayout.finishLoadmore();
                }
            }
        });

        //initLoc();
        // 城市
        String city = Prefs.with(this).read("city");
        if (city!=null&&!"".equals(city)){
            //mLocation.setText(city);
            // 获取城市ID
            String cityid = Prefs.with(this).read("cityId");
            //lat = Double.parseDouble(Prefs.with(this).read("lat"));
            //lon = Double.parseDouble(Prefs.with(this).read("lon"));
            if (cityid!=null&&!"".equals(cityid)) {
                auth_comp_city = cityid;
                getData(1,dict_id,auth_comp_city, null);
            }else{
                getCityId(city);
            }
        }

    }

    //通过城市名获取城市Id
    public void getCityId(String cityName) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(this, Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    auth_comp_city = result.getString("city_id");
                    getData(1,dict_id,auth_comp_city, null);
                    /**
                     * 存储城市Id
                     */
                    Prefs.with(LoanDetails.this).remove("cityId");
                    Prefs.with(LoanDetails.this).write("cityId",auth_comp_city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }
    //数据请求
    private void getData(final int pageIndex,String dict_parent_id,String dict_ext, final RefreshLayout refreshlayout) {
        Map<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("dict_parent_id", dict_parent_id);
        map.put("dict_ext", dict_ext);
        Log.d("aaaaaa", "dict_parent_id "+dict_parent_id);

        OKhttptils.post(this, Config.QUERYALLTHIRDAPP, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                com.tencent.mm.opensdk.utils.Log.d("aaaaa", "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                LoanDetailsBean enterprise = gson.fromJson(response, LoanDetailsBean.class);
                pageTotal=enterprise.getData().getPageTotal();
                result.addAll(enterprise.getData().getResult());
                carCreditAdapter.notifyDataSetChanged();
                //没有信息图片显示
                if (LoanDetails.this.result.size() <= 0) {
                    browsingCardealImage.setVisibility(View.VISIBLE);
                } else {
                    if (refreshlayout != null) {
                        if (pageIndex > pageTotal) {
                            refreshlayout.finishLoadmore(2000);
                        } else {
                            carCreditAdapter.notifyDataSetChanged();
                            refreshlayout.finishRefresh(2000);
                            refreshlayout.finishLoadmore(2000);
                        }
                    } else {
                        carCreditAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void fail(String response) {
                com.tencent.mm.opensdk.utils.Log.d("aaaa", "fail: " + response);
            }
        });

    }
//    /* 地图定位 */
//    private void initLoc() {
//        //初始化定位
//        mLocationClient = new AMapLocationClient(this);
//        //设置定位回调监听
//        mLocationClient.setLocationListener(this);
//        //初始化定位参数
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(true);
//        ////设置定位间隔,单位毫秒,默认为2000ms
//        //mLocationOption.setInterval(2000);
//        //获取一次定位结果：
//        //该方法默认为false。
//        mLocationOption.setOnceLocation(true);
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        // 申请定位权限
//        rxPermission = new RxPermissions(this);
//        rxPermission.request(Manifest.permission.ACCESS_FINE_LOCATION)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean granted) throws Exception {
//                        if (granted) { // 在android 6.0之前会默认返回true
//                            // 已经获取权限
//                            //启动定位
//                            mLocationClient.startLocation();
//                        } else {
//                            // 未获取权限
//                            Toast.makeText(LoanDetails.this, "您没有授权定位权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    /* 定位回调函数 */
//    @Override
//    public void onLocationChanged(AMapLocation amapLocation) {
//        if (amapLocation != null) {
//            String city = "";
//            if (amapLocation.getErrorCode() == 0) {
//                //定位成功回调信息，设置相关消息
//                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
////                amapLocation.getLatitude();//获取纬度
////                amapLocation.getLongitude();//获取经度
////                amapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(amapLocation.getTime());
//                df.format(date);//定位时间
////                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
////                amapLocation.getCountry();//国家信息
////                amapLocation.getProvince();//省信息
//                amapLocation.getCity();//城市信息
//                //locPlace.setText(amapLocation.getCity().split("市")[0]);
////                amapLocation.getDistrict();//城区信息
////                amapLocation.getStreet();//街道信息
////                amapLocation.getStreetNum();//街道门牌号信息
////                amapLocation.getCityCode();//城市编码
////                amapLocation.getAdCode();//地区编码
//                /* 将定位地址 存储在本地  */
//                Prefs.with(this).write("定位城市", amapLocation.getCity().substring(0, amapLocation.getCity().length() - 1));
//                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
////                if (isFirstLoc) {
////                    //获取定位信息
////                    StringBuffer buffer = new StringBuffer();
////                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//////                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
//////                    ToastUtil.show(getActivity(), "已定位到当前城市");
////                    isFirstLoc = false;
////                }
//                city = amapLocation.getCity().split("市")[0];
//                if(city!=null){//通过名称获取城市id
//                    Map<String, String> map = new HashMap<>();
//                    map.put("cityName",city);
//                    OKhttptils.post(LoanDetails.this, Config.GETCITYIDBYCITYNAME, map, new
//                            OKhttptils.HttpCallBack() {
//                                @Override
//                                public void success(String response) {
//
//                                    try {
//                                        JSONObject jsonObject = new JSONObject(response);
//                                        JSONObject data = jsonObject.getJSONObject("data");
//                                        JSONObject result = data.getJSONObject("result");
//                                        auth_comp_city = result.getString("city_id");
//                                        getData(1,dict_id,auth_comp_city, null);
//                                        Log.d("城市id", "onResponse获取数据: " + auth_comp_city);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                }
//
//                                @Override
//                                public void fail(String response) {
//                                    Log.d("aaaa", "fail: " + response);
//                                }
//                            });
//                }
//                Log.d("城市", "onLocationChanged: "+city);
//            } else {
//                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                Log.e("AmapError", "location Error, ErrCode:"
//                        + amapLocation.getErrorCode() + ", errInfo:"
//                        + amapLocation.getErrorInfo());
//                Toast.makeText(this, "定位失败", Toast.LENGTH_LONG).show();
//                /* 定位失败 获取默认城市 */
//                city = "北京";
//            }
//
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle("贷款种类明细");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(LoanDetails.this);
            }
        });
    }

}
