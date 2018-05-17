package com.xtzhangbinbin.jpq.activity;
/**
 * @Author jiangjiang
 * @CreateTime 2017/6/26
 * .欢迎界面（实现第一次启动引导界面，后面则自动进入主界面）
 */

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;


public class Welcome extends AppCompatActivity implements AMapLocationListener {

    @BindView(R.id.image_bg)
    ImageView imageBg;

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private RxPermissions rxPermission;
    private String TAG = "MainActivity";
    private String lat = "",lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        //启动一个延迟线程
//        getData();
        /* 默认地址  北京*/
        String city = Prefs.with(this).read("city");
        Log.d("aaaaaa", "onCreate: "+city);
        if (city==null||"".equals(city)) {
            // 定位
            initLoc();
        }else{
            // 获取城市ID
            String cityid = Prefs.with(this).read("cityId");
            lat = Prefs.with(this).read("lat");
            lon = Prefs.with(this).read("lon");
            if (cityid!=null&&!"".equals(cityid)) {
                startAPP();
                Welcome.this.finish();
            }else{
                getCityId(city);
            }
        }
    }

//    public void close(){
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startAPP();
//                Welcome.this.finish();
//            }
//        }, 2000);
//    }

//    private void getData() {
//        Map<String, String> map = new HashMap<>();
//        OKhttptils.post(this, Config.SELECTBOOTPAGE, map, new OKhttptils.HttpCallBack() {
//            @Override
//            public void success(String response) {
//                try {
//                    JSONObject object = new JSONObject(response);
//                    String status = object.getString("status");
//                    if ("1".equals(status)) {
//                        JSONObject data = object.getJSONObject("data");
//                        String fieldId = data.getString("result");
//                        OKhttptils.getPic(Welcome.this, fieldId, imageBg);
//                        // 开始启动
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                startAPP();
//                                Welcome.this.finish();
//                            }
//                        }, 2000);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void fail(String response) {
//
//            }
//        });
//    }

    private void startAPP() {

        SharedPreferences preferences = getSharedPreferences("count", 0); // 存在则打开它，否则创建新的Preferences
        int count = preferences.getInt("count", 0); // 取出数据
        /**
         *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
         */
        Log.d("aaaaaa", "startAPP: "+count);
        if (count == 0) {
            startActivity(new Intent(Welcome.this, Guide.class));
        } else {
            Intent intent2 = new Intent();
            intent2.setClass(Welcome.this, MainActivity.class);
            startActivity(intent2);
        }
        finish();

        //实例化Editor对象
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", 1); // 存入数据
        //提交修改
        editor.commit();
    }

    /* 地图定位 */
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
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
        rxPermission = new RxPermissions(this);
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
                            Toast.makeText(Welcome.this, "您没有授权定位权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
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
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
                    isFirstLoc = false;
                }
                city = amapLocation.getCity().split("市")[0];
                lon = String.valueOf(amapLocation.getLongitude());
                lat = String.valueOf(amapLocation.getLatitude());
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(this, "定位失败", Toast.LENGTH_LONG).show();
                /* 定位失败 获取默认城市 */
                city = "北京";
                lon = "116.38";
                lat = "39.9";
            }
            /* 将定位地址 存储在本地  */
            Prefs.with(this).remove("city");
            Prefs.with(this).remove("lat");
            Prefs.with(this).remove("lon");
            Prefs.with(this).write("city", city);
            Prefs.with(this).write("lat", lat);
            Prefs.with(this).write("lon", lon);
            // 查询城市Id
            getCityId(city);
        }
    }
    //通过城市名获取城市Id
    public void getCityId(final String cityName) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(this, Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    String cityId = result.getString("city_id");
                    /**
                     * 存储城市Id
                     */
                    Prefs.with(Welcome.this).remove("cityId");
                    Prefs.with(Welcome.this).write("cityId",cityId);
                    // 开启App
                    startAPP();
                    Welcome.this.finish();
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
