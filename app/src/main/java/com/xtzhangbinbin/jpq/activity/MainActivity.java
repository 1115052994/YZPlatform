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

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;

import com.xtzhangbinbin.jpq.fragment.main.CarBuyFragment;
import com.xtzhangbinbin.jpq.fragment.main.CarMoneyFragment;
import com.xtzhangbinbin.jpq.fragment.main.CarServiceFragment;
import com.xtzhangbinbin.jpq.fragment.main.LifeFragment;
import com.xtzhangbinbin.jpq.fragment.main.MainFragment;
import com.xtzhangbinbin.jpq.upgrade.UpgradeUtil;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener , MainFragment.Fragmentwsby {

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



}
