package com.xtzhangbinbin.jpq.fragment.main;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CityActivity;
import com.xtzhangbinbin.jpq.activity.ZhongLife;
import com.xtzhangbinbin.jpq.activity.ZhongLifeDetail;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.MeishiListBean;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Created by glp on 2018/4/18.
 * 描述：众生活
 */

public class LifeFragment extends Fragment implements AMapLocationListener {

    @BindView(R.id.mLocation)
    TextView mLocation;
    Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private RxPermissions rxPermission;

    private CommonRecyclerAdapter adapter;
    private List<MeishiListBean.DataBean.ResultBean> list = new ArrayList<>();

    // 轮播图
    private List<String> bannersImage = new ArrayList<>();

    private View view;
    private String cityId = "";
    private String cityName = "";

    /*
    {
	"data": {
		"result": [
			{
				"dict_id": "YZzshjd",
				"dict_desc": "酒店",
				"dict_value": "jd"
			},
			{
				"dict_id": "YZzshms",
				"dict_desc": "美食",
				"dict_value": "ms"
			},
			{
				"dict_id": "YZzshshfu",
				"dict_desc": "生活服务",
				"dict_value": "shfu"
			},
			{
				"dict_id": "YZzshxxyl",
				"dict_desc": "休闲娱乐",
				"dict_value": "xxyl"
			}
		]
	},
	"message": "",
	"status": "1"
}
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_life, container, false);
        }
        initLoc();
        unbinder = ButterKnife.bind(this, view);
        initView();
        getData();
        return view;
    }

    private void initView() {
        // banner
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                OKhttptils.getPic(getActivity(), (String) path, imageView);
            }
        });
//        if (banner != null) {
//            banner.setImages(bannersImage);
//            banner.start();
//        }

        adapter = new CommonRecyclerAdapter(getContext(), list, R.layout.item_zhongshenghuo) {

            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                MeishiListBean.DataBean.ResultBean bean = list.get(position);
                ImageView imageView = holder.getView(R.id.imageView);
//                OKhttptils.getPic(getActivity(),bean.getZsh_img(),imageView);
                Picasso.with(getContext()).load(bean.getZsh_img()).into(imageView);
                TextView tvName = holder.getView(R.id.tv_name);
                tvName.setText(bean.getZsh_name());
                TextView tvPrice = holder.getView(R.id.tv_price);
                tvPrice.setText("人均￥" + bean.getZsh_avg_price() + "元");
                TextView tvLoc = holder.getView(R.id.tv_loc);
                tvLoc.setText(bean.getZsh_addr());
                int level = (int) Double.parseDouble(bean.getZsh_level());
                ImageView star1 = holder.getView(R.id.star1);
                ImageView star2 = holder.getView(R.id.star2);
                ImageView star3 = holder.getView(R.id.star3);
                ImageView star4 = holder.getView(R.id.star4);
                ImageView star5 = holder.getView(R.id.star5);
                switch (level) {
                    case 5:
                        star5.setImageResource(R.drawable.pj_pinkstar);
                    case 4:
                        star4.setImageResource(R.drawable.pj_pinkstar);
                    case 3:
                        star3.setImageResource(R.drawable.pj_pinkstar);
                    case 2:
                        star2.setImageResource(R.drawable.pj_pinkstar);
                    case 1:
                        star1.setImageResource(R.drawable.pj_pinkstar);
                        break;
                }
            }
        };
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("String",list.get(position).getZsh_id());
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLifeDetail.class, bundle);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadmore(false);
    }

    private void getData() {
        getBanner();
    }

    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(getActivity(), Config.SELECTZSHCENTER, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray result = data.getJSONArray("result");
                    bannersImage.clear();
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object1 = result.getJSONObject(i);
                        String fileId = object1.getString("adve_file_id");
                        bannersImage.add(fileId);
                    }
                    if (banner != null) {
                        banner.setImages(bannersImage);
                        banner.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void getPageInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("dict_value", "dg");
        map.put("zsh_city", cityId);
        map.put("pageIndex", "1");
        map.put("pageSize", "20");
        OKhttptils.post(getActivity(), Config.GETPAGEINFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getData===", response);
                Gson gson = new Gson();
                MeishiListBean meishiListBean = gson.fromJson(response, MeishiListBean.class);
                list.clear();
                List<MeishiListBean.DataBean.ResultBean> resultBeanList = meishiListBean.getData().getResult();
                for (MeishiListBean.DataBean.ResultBean bean : resultBeanList) {
                    list.add(bean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    //通过城市名获取城市Id
    public void getCityId(final String cityName) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(getActivity(), Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    cityId = result.getString("city_id");
                    // 通过城市查询列表信息
                    getPageInfo();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    /* 地图定位 */
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        ////设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(2000);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 申请定位权限
        rxPermission = new RxPermissions(getActivity());
        rxPermission.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            //启动定位
                            mLocationClient.startLocation();
                        } else {
                            // 未获取权限
                            Toast.makeText(getActivity(), "您没有授权定位权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* 定位回调函数 */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            String city = "";
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                //locPlace.setText(amapLocation.getCity().split("市")[0]);
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
                /* 将定位地址 存储在本地  */
                Prefs.with(getActivity()).write("定位城市", amapLocation.getCity().substring(0, amapLocation.getCity().length() - 1));
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
//                    ToastUtil.show(getActivity(), "已定位到当前城市");
                    isFirstLoc = false;
                }
                city = amapLocation.getCity().split("市")[0];
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getActivity(), "定位失败", Toast.LENGTH_LONG).show();
                /* 定位失败 获取默认城市 */
                city = "北京";
            }
            mLocation.setText(city);
            cityName = city;
            // 查询城市Id
            getCityId(cityName);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ly_ms, R.id.ly_xxyl, R.id.ly_shfw, R.id.ly_jiudian})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("cityId",cityId);
        switch (view.getId()) {
            case R.id.ly_ms:
                bundle.putString("String","YZzshms");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class,bundle );
                break;
            case R.id.ly_xxyl:
                bundle.putString("String","YZzshxxyl");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, bundle);
                break;
            case R.id.ly_shfw:
                bundle.putString("String","YZzshshfu");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, bundle);
                break;
            case R.id.ly_jiudian:
                bundle.putString("String","YZzshjd");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, bundle);
                break;
        }
    }

    @OnClick(R.id.mLocation)
    public void onViewClicked() {
        // 城市
        JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class);
        CityActivity.setOnBackListener(new CityActivity.BackListener() {
            @Override
            public void backData(String city) {
                mLocation.setText(city);
                cityName = city;
                // 查询城市Id
                getCityId(cityName);
            }
        });
    }
}
