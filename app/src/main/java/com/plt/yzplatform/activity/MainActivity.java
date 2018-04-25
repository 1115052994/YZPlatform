package com.plt.yzplatform.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.ViewPagerAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.fragment.main.CarBuyFragment;
import com.plt.yzplatform.fragment.main.CarMoneyFragment;
import com.plt.yzplatform.fragment.main.CarServiceFragment;
import com.plt.yzplatform.fragment.main.LifeFragment;
import com.plt.yzplatform.fragment.main.MainFragment;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, AMapLocationListener {

    @BindView(R.id.mLocation)
    TextView mLocation;
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

    private ViewPagerAdapter mAdapter;
    private FragmentManager fm;
    private Fragment fragment;
    private FragmentTransaction transaction;
    private List<Fragment> fragmentList = new ArrayList<>();

    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initView();
         /* 获取定位权限  并且进行定位 */
        checkLocationPermission();
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
                transaction.replace(R.id.view_pager, fragment);
                break;
            case R.id.money:
                fragment = fragmentList.get(3);
                transaction.replace(R.id.view_pager, fragment);
                break;
            case R.id.life:
//                fragment = fragmentList.get(4);
//                transaction.replace(R.id.view_pager, fragment);
                JumpUtil.newInstance().jumpRight(MainActivity.this,ShowProductDetailActivity.class);
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
        //启动定位
        mLocationClient.startLocation();
    }

    /* 定位回调函数 */
    @Override
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
//                getDefaultCity();
            }
        }
    }

    /* 检查是否有定位权限 */
    private void checkLocationPermission() {
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY", "没有权限");
            requestPermission(LOCATION_PERMISSION_CODE);
        } else {
            initLoc();
            //已经获得权限，则执行定位请求。
            Toast.makeText(MainActivity.this, "已获取定位权限", Toast.LENGTH_SHORT).show();
//            mLocationClient.startLocation();

        }
    }

    /* 检查是否有存储的读写权限 */
    private void checkStoragePermission() {
        // 检查权限的方法: ContextCompat.checkSelfPermission()两个参数分别是Context和权限名.
        // 返回PERMISSION_GRANTED是有权限，PERMISSION_DENIED没有权限
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有权限，向系统申请该权限。
            Log.i("MY", "没有权限");
            requestPermission(STORAGE_PERMISSION_CODE);
        } else {
            //同组的权限，只要有一个已经授权，则系统会自动授权同一组的所有权限，比如WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE
            Toast.makeText(MainActivity.this, "已获取存储的读写权限", Toast.LENGTH_SHORT).show();
        }
    }

    /* 请求权限 */
    private void requestPermission(int permissioncode) {
        String permission = getPermissionString(permissioncode);
//        if (!IsEmptyOrNullString(permission)) {
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    permission)) {
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                if (permissioncode == LOCATION_PERMISSION_CODE) {
//                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.location_description_title,
//                            R.string.location_description_why_we_need_the_permission,
//                            permissioncode);
//                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
//                } else if (permissioncode == STORAGE_PERMISSION_CODE) {
//                    DialogFragment newFragment = HintDialogFragment.newInstance(R.string.storage_description_title,
//                            R.string.storage_description_why_we_need_the_permission,
//                            permissioncode);
//                    newFragment.show(getFragmentManager(), HintDialogFragment.class.getSimpleName());
//                }
//
//
//            } else {
                Log.i("MY", "返回false 不需要解释为啥要权限，可能是第一次请求，也可能是勾选了不再询问");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission}, permissioncode);
//            }
//        }
    }

    /* 判断字符串是否为空 */
    public static boolean IsEmptyOrNullString(String s) {
        return (s == null) || (s.trim().length() == 0);
    }

    /* 获取权限名 */
    private String getPermissionString(int requestCode) {
        String permission = "";
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                permission = Manifest.permission.ACCESS_FINE_LOCATION;
                break;
            case STORAGE_PERMISSION_CODE:
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
        }
        return permission;
    }
}
