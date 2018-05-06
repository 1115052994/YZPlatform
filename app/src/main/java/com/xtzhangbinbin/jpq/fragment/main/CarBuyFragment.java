package com.xtzhangbinbin.jpq.fragment.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.CityActivity;
import com.xtzhangbinbin.jpq.activity.SearchActivity;
import com.xtzhangbinbin.jpq.fragment.BuyCar;
import com.xtzhangbinbin.jpq.fragment.Sellcars;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * Created by glp on 2018/4/18.
 * 描述：车买卖
 */

public class CarBuyFragment extends Fragment implements AMapLocationListener {
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_sell)
    TextView tvSell;
    Unbinder unbinder;
    @BindView(R.id.ly_buy)
    LinearLayout lyBuy;
    @BindView(R.id.ly_sell)
    LinearLayout lySell;
    @BindView(R.id.mLocation)
    TextView mLocation;
    private FragmentManager fm;
    private Fragment buyFragment;
    private Sellcars sellFragment;
    private FragmentTransaction transaction;

    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private RxPermissions rxPermission;

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_car, container, false);
            buyFragment = new BuyCar();
            sellFragment = new Sellcars();
        }
        unbinder = ButterKnife.bind(this, view);
        initView();
        initLoc();
        return view;
    }

    private void initView() {
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.content, buyFragment);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ly_buy, R.id.ly_sell, R.id.mSearch, R.id.mLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_buy:
                lyBuy.setSelected(false);
                lySell.setSelected(false);
                tvSell.setTextColor(Color.parseColor("#ff9696"));
                tvBuy.setTextColor(Color.parseColor("#ffffff"));
                transaction = fm.beginTransaction();
                transaction.replace(R.id.content, buyFragment);
                transaction.commit();
                break;
            case R.id.ly_sell:
                lySell.setSelected(true);
                lyBuy.setSelected(true);
                tvBuy.setTextColor(Color.parseColor("#ff9696"));
                tvSell.setTextColor(Color.parseColor("#ffffff"));
                transaction = fm.beginTransaction();
                transaction.replace(R.id.content, sellFragment);
                transaction.commit();
                break;
            case R.id.mSearch:
                JumpUtil.newInstance().jumpRight(getContext(), SearchActivity.class, "car");
                break;
            case R.id.mLocation:
                JumpUtil.newInstance().jumpRight(getContext(), CityActivity.class);
                CityActivity.setOnBackListener(new CityActivity.BackListener() {
                    @Override
                    public void backData(String city) {
                        mLocation.setText(city);
                        Intent intent = new Intent();
                        intent.setAction("city");
                        intent.putExtra("city",city);
                        getContext().sendBroadcast(intent);
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
            Intent intent = new Intent();
            intent.setAction("city");
            intent.putExtra("city",city);
            getContext().sendBroadcast(intent);
        }
    }


}
