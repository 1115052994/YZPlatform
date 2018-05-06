package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.Enterprise;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MessageEntry extends BaseActivity implements AMapLocationListener {

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.phone2)
    EditText phone2;
    @BindView(R.id.idcard)
    EditText idcard;
    @BindView(R.id.baocun)
    Button baocun;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    private String auth_comp_city;
    //定位需要的声明
    private AMapLocationClient mLocationClient = null;//定位发起端
    private AMapLocationClientOption mLocationOption = null;//定位参数
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    private RxPermissions rxPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_entry);
        ButterKnife.bind(this);
        initLoc();
        Intent intent = getIntent();
        final String ZX_dict_id = intent.getStringExtra("dict_id");
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()==true) {
                    baocun.setEnabled(true);
                    baocun.setBackground(getResources().getDrawable(R.drawable.button_baocun));
                }else {
                    baocun.setEnabled(false);
                    baocun.setBackground(getResources().getDrawable(R.drawable.button_baocun2));
                }
            }
        });
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() && !phone.getText().toString().isEmpty() && !phone2.getText().toString().isEmpty() && !idcard.getText().toString().isEmpty()) {
                    if (auth_comp_city != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            String time = formatter.format(curDate);//获取当前时间
                            getData(ZX_dict_id,name.getText().toString(),phone.getText().toString(),phone2.getText().toString(),idcard.getText().toString(),auth_comp_city,time);
                            baocun.setBackground(getResources().getDrawable(R.drawable.button_baocun));
                    }else {
                        final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(MessageEntry.this).setMessage1("温馨提示").setMessage2("  当前城市获取失败").setConfirm("确定").setCancel("取消").showDialog();
                        ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                ordinaryDialog.dismiss();
                            }
                        });
                        ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                ordinaryDialog.dismiss();
                            }
                        });
                    }
                } else {
                    final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(MessageEntry.this).setMessage1("温馨提示").setMessage2("  请完善信息").setConfirm("确定").setCancel("取消").showDialog();
                    ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            ordinaryDialog.dismiss();
                        }
                    });
                    ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            ordinaryDialog.dismiss();
                        }
                    });
                }
            }
        });


    }

    //数据请求
    private void getData(String ZX_dict_id, String zx_name, String zx_phone, String zx_tel, String zx_card_no, String zx_city, String zx_create_date) {
        Map<String, String> map = new HashMap<>();
        map.put("ZX_dict_id", ZX_dict_id);
        map.put("zx_name", zx_name);
        map.put("zx_phone", zx_phone);
        map.put("zx_tel", zx_tel);
        map.put("zx_card_no", zx_card_no);
        map.put("zx_city", zx_city);
        map.put("zx_create_date", zx_create_date);
        OKhttptils.post(MessageEntry.this, Config.INSERTINFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(MessageEntry.this).setMessage1("温馨提示").setMessage2("  您的信息已经提交成功!").showDialog();
                ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ordinaryDialog.dismiss();
                    }
                });
                ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        finish();
                        ordinaryDialog.dismiss();
                    }
                });
                return response;
            }

            @Override
            public void fail(String response) {
                Log.d("aaaa", "fail: " + response);
            }
        });

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
                            Toast.makeText(MessageEntry.this, "您没有授权定位权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
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
//                amapLocation.getLatitude();//获取纬度
//                amapLocation.getLongitude();//获取经度
//                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
//                amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                amapLocation.getCountry();//国家信息
//                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                //locPlace.setText(amapLocation.getCity().split("市")[0]);
//                amapLocation.getDistrict();//城区信息
//                amapLocation.getStreet();//街道信息
//                amapLocation.getStreetNum();//街道门牌号信息
//                amapLocation.getCityCode();//城市编码
//                amapLocation.getAdCode();//地区编码
                /* 将定位地址 存储在本地  */
                Prefs.with(this).write("定位城市", amapLocation.getCity().substring(0, amapLocation.getCity().length() - 1));
                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
//                if (isFirstLoc) {
//                    //获取定位信息
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
////                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
////                    ToastUtil.show(getActivity(), "已定位到当前城市");
//                    isFirstLoc = false;
//                }
                city = amapLocation.getCity().split("市")[0];
                if(city!=null){//通过名称获取城市id
                    Map<String, String> map = new HashMap<>();
                    map.put("cityName",city);
                    OKhttptils.post(MessageEntry.this, Config.GETCITYIDBYCITYNAME, map, new
                            OKhttptils.HttpCallBack() {
                                @Override
                                public String success(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        JSONObject result = data.getJSONObject("result");
                                        auth_comp_city = result.getString("city_id");
                                        android.util.Log.d("城市id", "onResponse获取数据: " + auth_comp_city);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    return response;
                                }

                                @Override
                                public void fail(String response) {
                                    android.util.Log.d("aaaa", "fail: " + response);
                                }
                            });
                }
                android.util.Log.d("城市", "onLocationChanged: "+city);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                android.util.Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
                Toast.makeText(this, "定位失败", Toast.LENGTH_LONG).show();
                /* 定位失败 获取默认城市 */
                city = "北京";
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle("信息录入");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(MessageEntry.this);
            }
        });
    }
}
