package com.plt.yzplatform.activity;

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

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
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
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{

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

}
