package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.fragment.main.CarBuyFragment;
import com.xtzhangbinbin.jpq.fragment.main.CarMoneyFragment;
import com.xtzhangbinbin.jpq.fragment.main.CarServiceFragment;
import com.xtzhangbinbin.jpq.fragment.main.LifeFragment;
import com.xtzhangbinbin.jpq.fragment.main.MainFragment;
import com.xtzhangbinbin.jpq.upgrade.UpgradeUtil;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

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

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener , MainFragment.Fragmentwsby,AMapLocationListener {

    @BindView(R.id.view_pager)
    FrameLayout viewPager;
    @BindView(R.id.main)
    RadioButton main;
    @BindView(R.id.buy)
    RadioButton buy;
    @BindView(R.id.service)
    RadioButton service;
    @BindView(R.id.money)
    RadioButton money;
    @BindView(R.id.life)
    RadioButton life;
    @BindView(R.id.tab_bottom)
    RadioGroup tabBottom;

    private String selected_city;
    private String dict_id;

    private FragmentManager fm;
    private Fragment fragment;
    private FragmentTransaction transaction;
    private List<Fragment> fragmentList = new ArrayList<>();

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        rxPermission = new RxPermissions(this);
        // 申请应用高危险权限
        requestPermission();
        initView();
        //应用升级
        upgrade();
        //搜索车辆回调
        Intent intent = getIntent();
        if (intent!=null){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                String searchData = bundle.getString("String","");
                if (!"".equals(searchData)){
                    if (searchData.contains(",")) {
                        String[] data = searchData.split(",");
                        switchToCarBuyFromSearch(data[0],data[1],data[2],data[3]);
                    }
                }
            }
        }
        /* 默认地址  北京*/
        lon = "116.38";
        lat = "39.9";
        Prefs.with(this).remove("city");
        Prefs.with(this).remove("lat");
        Prefs.with(this).remove("lon");
        Prefs.with(this).write("city", "北京");
        Prefs.with(this).write("lat", lat);
        Prefs.with(this).write("lon", lon);
        // 查询城市Id
        getCityId("北京");
        // 定位
        initLoc();
    }



    public void switchTocar(String type){
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        fragment = fragmentList.get(1);
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        transaction.replace(R.id.view_pager, fragment);
        buy.setChecked(true);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // Manifest.permission.ACCESS_COARSE_LOCATION,
    private void requestPermission() {
        rxPermission
                .requestEach(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE
                        )
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });
    }

    /* 初始化界面 */
    private void initView() {
        tabBottom.setOnCheckedChangeListener(this);
        fragmentList = getFragments();
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        fragment = fragmentList.get(0);
        transaction.replace(R.id.view_pager, fragment);
        transaction.commit();
        main.setChecked(true);
    }



    /* 退出程序 */
    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    ToastUtil.show(this, "再按一次退出程序！");
                    firstTime = secondTime;
                    return true;
                } else {
                    ActivityUtil.finishAll();
                    System.exit(0);
                }
            }
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /* 添加viewpager */
    public List<Fragment> getFragments() {
        fragmentList.add(new MainFragment());
        fragmentList.add(new CarBuyFragment());
        fragmentList.add(new CarServiceFragment());
        fragmentList.add(new CarMoneyFragment());
        fragmentList.add(new LifeFragment());
        return fragmentList;
    }

    /* 切换fragment */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        switch (i) {
            case R.id.main:
                fragment = fragmentList.get(0);
                transaction.replace(R.id.view_pager, fragment);
                break;
            case R.id.buy:
                fragment = fragmentList.get(1);
                transaction.replace(R.id.view_pager, fragment);
                break;
            case R.id.service:
                fragment = fragmentList.get(2);
                Bundle bundle = new Bundle();
                bundle.putString("selected_city",selected_city);
                fragment.setArguments(bundle);
                transaction.replace(R.id.view_pager, fragment);
                break;
            case R.id.money:
                fragment = fragmentList.get(3);
                transaction.replace(R.id.view_pager, fragment);
                break;
            case R.id.life:
                fragment = fragmentList.get(4);
                transaction.replace(R.id.view_pager, fragment);
                break;
        }
        transaction.commitAllowingStateLoss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            Bundle bundle = data.getExtras();
            switch (requestCode){
                case 01:
                    selected_city = bundle.getString("selected_city");
                    fragment = fragmentList.get(2);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("selected_city",selected_city);
                    fragment.setArguments(bundle1);
                    transaction.replace(R.id.view_pager, fragment);
                    Log.i(TAG, "onActivityResult: " + selected_city );
                    break;
            }
        }
    }

    /**
     * 应用升级
     */
    public void upgrade(){
        UpgradeUtil.getInstance(this).upgrade(main);
    }

    // 切换到 买车
    public void switchToCarBuy(String str){
        buy.setChecked(true);
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        fragment = fragmentList.get(1);
        Bundle bundle = new Bundle();
        bundle.putString("type","carbuy");
        switch (str) {
            case "cmzy":
                bundle.putString("param","cmzy");
                break;
            case "rmsx":
                bundle.putString("param","rmsx");
                break;
            case "zxc":
                bundle.putString("param","zxc");
                break;
            case "pprm":
                bundle.putString("param","pprm");
                break;
        }
        fragment.setArguments(bundle);
        transaction.replace(R.id.view_pager, fragment);
        transaction.commit();
    }
    public void switchToCarSell(String type){
        buy.setChecked(true);
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        fragment = fragmentList.get(1);
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        transaction.replace(R.id.view_pager, fragment);
    }
    // 切换到 车生活
    public void switchToLife(){
        life.setChecked(true);
    }

    // 搜索 切换到 买车
    public void switchToCarBuyFromSearch(String brand,String brandId,String carName,String carId){
        Log.i("BuyFromSearch",brand+"---"+brandId+"---"+carName+"---"+carId);
        buy.setChecked(true);
        fragment = fragmentList.get(1);
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("type","searchcar");
        bundle.putString("brand",brand);
        bundle.putString("brandId",brandId);
        bundle.putString("carName",carName);
        bundle.putString("carId",carId);
        fragment.setArguments(bundle);
        transaction.replace(R.id.view_pager, fragment);
        transaction.commitAllowingStateLoss();
    }


    /* 维修保养回调函数 */
    @Override
    public void pross(String s) {
        if (s != null){
            main.setChecked(false);
            service.setChecked(true);
            dict_id = s;
            transaction = fm.beginTransaction();
            fragment = fragmentList.get(2);

            Bundle bundle = new Bundle();
            bundle.putString("dict_id",dict_id);
            Log.d(TAG, "pross啦啦啦: " + dict_id );
            fragment.setArguments(bundle);
            transaction.replace(R.id.view_pager, fragment);
            transaction.commit();
        }
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
                            Toast.makeText(MainActivity.this, "您没有授权定位权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
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
                    Prefs.with(MainActivity.this).remove("cityId");
                    Prefs.with(MainActivity.this).write("cityId",cityId);
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
