package com.plt.yzplatform.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.plt.yzplatform.BuildConfig;
import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.fragment.main.CarBuyFragment;
import com.plt.yzplatform.fragment.main.CarMoneyFragment;
import com.plt.yzplatform.fragment.main.CarServiceFragment;
import com.plt.yzplatform.fragment.main.LifeFragment;
import com.plt.yzplatform.fragment.main.MainFragment;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

    private static final String TAG = "主页" ;
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
        // initLoc();
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


    @OnClick({R.id.mLocation, R.id.mSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLocation:
                JumpUtil.newInstance().jumpRight(MainActivity.this,CityActivity.class);
                break;
            case R.id.mSearch:
                JumpUtil.newInstance().jumpRight(this,SearchActivity.class);
                break;
        }
    }

    /* 初始化界面 */
    private void initView() {
        tabBottom.setOnCheckedChangeListener(this);
        fragmentList = getFragments();
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        fragment = fragmentList.get(2);
        Bundle bundle = new Bundle();
        bundle.putString("selected_city",selected_city);
        fragment.setArguments(bundle);
        transaction.replace(R.id.view_pager, fragment);
        transaction.commit();
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
//                fragment = fragmentList.get(4);
//                transaction.replace(R.id.view_pager, fragment);
                JumpUtil.newInstance().jumpRight(MainActivity.this, ShowProductDetailActivity.class);
                break;
        }
        transaction.commit();
    }

    /* 地图定位 */
    private void initLoc() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
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
                            Toast.makeText(MainActivity.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /* 定位回调函数 */
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
    public void onLocationChanged(AMapLocation amapLocation) {
        //str.substring(0,str.length()-1) 去掉字符串最后一个字符
        if (amapLocation != null) {
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
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
//                 /* 将定位地址 存储在本地  */
//                Prefs.with(getApplicationContext()).write("定位城市", amapLocation.getCity().substring(0, amapLocation.getCity().length() - 1));
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
//                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    ToastUtil.show(MainActivity.this, "已定位到当前城市");
                    mLocation.setText(amapLocation.getCity().substring(0, amapLocation.getCity().length() - 1));
                    isFirstLoc = false;
                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                /* 定位失败 获取默认城市 */
                mLocation.setText("北京");
            }
        }
    }

}
