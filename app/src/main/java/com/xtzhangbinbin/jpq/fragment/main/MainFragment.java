package com.xtzhangbinbin.jpq.fragment.main;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.AccessCar;
import com.xtzhangbinbin.jpq.activity.CarCredit;
import com.xtzhangbinbin.jpq.activity.CarProduct;
import com.xtzhangbinbin.jpq.activity.CarProductSX;
import com.xtzhangbinbin.jpq.activity.CityActivity;
import com.xtzhangbinbin.jpq.activity.MainActivity;
import com.xtzhangbinbin.jpq.activity.CompanyCenterActivity;
import com.xtzhangbinbin.jpq.activity.LoginActivity;
import com.xtzhangbinbin.jpq.activity.PersonalCenterActivity;
import com.xtzhangbinbin.jpq.activity.PoiAroundSearchActivity;
import com.xtzhangbinbin.jpq.activity.WeizhangQuery;
import com.xtzhangbinbin.jpq.activity.ZhongLife;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.zxing.android.CaptureActivity;
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
 * 描述：主页
 */
public class MainFragment extends Fragment implements AMapLocationListener {
    Unbinder unbinder;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.mLocation)
    TextView mLocation;

    private String user_type;

    // 轮播图
    private List<String> bannersImage = new ArrayList<>();

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private RxPermissions rxPermission;

    private View view;
    private double lat,lon;
    private String cityId ="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_main1, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        getData();
        initView();
        initLoc();
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
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(getActivity(), Config.SELECTADVEMAIN, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("getData===", response);
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
                    if (banner!=null) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.image_scan, R.id.image_man, R.id.ly_etc, R.id.ly_wby, R.id.ly_jyz, R.id.ly_jjjy, R.id.ly_wzcx, R.id.ly_yxc, R.id.ly_xydk, R.id.ly_cyp, R.id.ly_mc, R.id.ly_gj, R.id.image_czcl, R.id.yzfw, R.id.image_xyd, R.id.image_cyp, R.id.image_csh, R.id.image_wxby, R.id.image_xc, R.id.image_qcmr, R.id.image_pprm, R.id.image_rmsx, R.id.image_zxc, R.id.image_cmzy, R.id.iamge_etzy, R.id.iamge_qczd, R.id.iamge_xcjly, R.id.iamge_dcld,R.id.mLocation,R.id.image_ms,R.id.image_xxyl,R.id.image_shfw})
    public void onViewClicked(View view) {
        //ToastUtil.show(getContext(),view.getId());

        //YZcompcfwlxxcxc洗车
        //YZcompcfwlxwxbywxby维修保养
        //YZcompcfwlxxcmr美容
        //YZcompcfwlxwxbybjpq钣金喷漆
        //YZcompcfwlxwxbyghdc更换电瓶
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (view.getId()) {
            case R.id.image_scan:
                if(null != Prefs.with(getContext()).read("user_token")){
                    JumpUtil.newInstance().jumpRight(getContext(), CaptureActivity.class);
                } else {
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }
                break;
            case R.id.image_man:
                user_type = Prefs.with(getContext()).read("user_type");
                Log.d("用户类型", "onViewClicked: " + user_type);
                if (user_type.isEmpty()){
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }else if (user_type.equals("comp")){
                    JumpUtil.newInstance().jumpRight(getContext(), CompanyCenterActivity.class);
                }else if (user_type.equals("pers")){
                    /* 跳转到个人中心 */
                    JumpUtil.newInstance().jumpRight(getContext(), PersonalCenterActivity.class);
                }
                break;
            case R.id.ly_etc:
                break;
            case R.id.ly_wby:
                break;
            case R.id.ly_jyz:
                // 加油站
                Bundle bundle = new Bundle();
                bundle.putDouble("lat",lat);
                bundle.putDouble("lon",lon);
                JumpUtil.newInstance().jumpLeft(getActivity(), PoiAroundSearchActivity.class,bundle);
                break;
            case R.id.ly_jjjy:
                break;
            case R.id.ly_wzcx:
                if(null != Prefs.with(getContext()).read("user_token")){
                    // 违章查询
                    JumpUtil.newInstance().jumpLeft(getActivity(), WeizhangQuery.class);
                } else {
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }
                break;
            case R.id.ly_yxc:
                break;
            case R.id.ly_xydk:
                break;
            case R.id.ly_cyp:
                // 车用品
                JumpUtil.newInstance().jumpLeft(getActivity(), CarProduct.class);
                break;
            case R.id.ly_mc:
                //卖车
                // 卖che
                mainActivity.switchToCarSell("2");
                break;
            case R.id.ly_gj:
                if(null != Prefs.with(getContext()).read("user_token")){
                    // 估价
                    JumpUtil.newInstance().jumpRight(getContext(), AccessCar.class);
                } else {
                    JumpUtil.newInstance().jumpRight(getContext(), LoginActivity.class);
                }
                break;
            case R.id.image_czcl:
                break;
            case R.id.yzfw:
                break;
            case R.id.image_xyd:
                JumpUtil.newInstance().jumpRight(getContext(), CarCredit.class);
                break;
            case R.id.image_cyp:
                // 车用品
                JumpUtil.newInstance().jumpLeft(getActivity(), CarProduct.class);
                break;
            case R.id.image_csh:
                // 车生活
                mainActivity.switchToLife();
                break;
            case R.id.image_wxby:
                break;
            case R.id.image_xc:
                break;
            case R.id.image_qcmr:
                break;
            case R.id.image_pprm:
                // 品牌热卖
                mainActivity.switchToCarBuy("pprm");
                break;
            case R.id.image_rmsx:
                mainActivity.switchToCarBuy("rmsx");
                // 入门首选
                break;
            case R.id.image_zxc:
                mainActivity.switchToCarBuy("zxc");
                // 准新车
                break;
            case R.id.image_cmzy:
                // 车meizhiyou
                mainActivity.switchToCarBuy("cmzy");
                break;
            case R.id.image_ms:
                // meishi
                Bundle ms = new Bundle();
                ms.putString("cityId",cityId);
                ms.putString("String","YZzshms");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class,ms );
                break;
            case R.id.image_xxyl:
                // 休闲
                Bundle xxyl = new Bundle();
                xxyl.putString("cityId",cityId);
                xxyl.putString("String","YZzshxxyl");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, xxyl);
                break;
            case R.id.image_shfw:
                // 生活服务
                Bundle shfw = new Bundle();
                shfw.putString("cityId",cityId);
                shfw.putString("String","YZzshshfu");
                JumpUtil.newInstance().jumpLeft(getActivity(), ZhongLife.class, shfw);
                break;
            case R.id.iamge_etzy:
                // 车用品 儿童座椅
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxetzy");
                break;
            case R.id.iamge_qczd:
                // 汽车坐垫
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxqczd");
                break;
            case R.id.iamge_xcjly:
                // 行车记录仪
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxxcjly");
                break;
            case R.id.iamge_dcld:
                // 倒车雷达
                JumpUtil.newInstance().jumpLeft(getActivity(),CarProductSX.class,"YZcarYPcyplxdcld");
                break;
            case R.id.mLocation:
                // 城市
                JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class);
                CityActivity.setOnBackListener(new CityActivity.BackListener() {
                    @Override
                    public void backData(String city) {
                        mLocation.setText(city);
                        // 获取城市ID
                        getCityId(city);
                    }
                });
                break;
        }
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
                lat = amapLocation.getLatitude();//获取纬度
                lon = amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
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
            // 获取城市ID
            getCityId(city);
            mLocation.setText(city);
        }
    }


    //通过城市名获取城市Id
    public void getCityId(String cityName) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
