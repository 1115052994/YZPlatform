package com.plt.yzplatform.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaygoo.widget.RangeSeekbar;
import com.plt.yzplatform.AppraiseInterface;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.BSXCarMoreAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarParams;
import com.plt.yzplatform.entity.ChassisNumber;
import com.plt.yzplatform.entity.Enterprise;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.DateUtil;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.PhotoUtils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.ExpandableGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class AddCarProduct extends BaseActivity {
    private static final String TAG = "添加汽车产品";
    @BindView(R.id.gv_bsx)
    ExpandableGridView gvBsx;
    @BindView(R.id.gv_pfbz)
    ExpandableGridView gvPfbz;
    @BindView(R.id.gv_zws)
    ExpandableGridView gvZws;
    @BindView(R.id.gv_rllx)
    ExpandableGridView gvRllx;
    @BindView(R.id.car_resume)
    EditText carResume;
    @BindView(R.id.seekbar)
    RangeSeekbar seekbar;
    @BindView(R.id.mCarNumb)
    EditText mCarNumb;
    @BindView(R.id.mBrand)
    TextView mBrand;
    @BindView(R.id.mCarType)
    TextView mCarType;
    @BindView(R.id.mKm)
    EditText mKm;
    @BindView(R.id.mPrice)
    EditText mPrice;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.mPhoto)
    TextView mPhoto;
    @BindView(R.id.mNext)
    TextView mNext;


    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, String> map2 = new HashMap<>();
    private String car_vin;  //车架号     已赋值
    private String car_brand;  //品牌id      已赋值
    private String car_model; //车型        已赋值
    private String car_mileage;  //车显里程   已赋值
    private String car_price;  //车辆售价     已赋值
    private String car_time;  //上牌时间    已赋值
    private String car_gearbox; //变速箱     已赋值
    private String car_letout;  //排放标准   已赋值
    private String car_seating; //座位数   已赋值
    private String car_fuel_type; //燃料类型   已赋值
    private String car_resum;  //车辆简历    已赋值
    private String car_place_city; //所在城市   已赋值
    private String car_displacement = "1.0"; //车排量  已赋值
    private String car_name;  //车辆名称     已賦值

    private List<String> car_brand_list = new ArrayList<>();//品牌id

    //变速箱
    private List<String> carBsxList = new ArrayList<>();
    private List<String> carBsxIds = new ArrayList<>();
    private BSXCarMoreAdapter bsxAdapter;
    //排行标准
    private List<String> carPfbzList = new ArrayList<>();
    private List<String> carPfbzIds = new ArrayList<>();
    private BSXCarMoreAdapter pfbzAdapter;
    //座位数
    private List<String> carZwsList = new ArrayList<>();
    private List<String> carZwsIds = new ArrayList<>();
    private BSXCarMoreAdapter zwsAdapter;
    //燃料类型
    private List<String> carRllxList = new ArrayList<>();
    private List<String> carRllxIds = new ArrayList<>();
    private BSXCarMoreAdapter rllxAdapter;

    private DecimalFormat df = new DecimalFormat("#0.0");


    // 保留选中position
    private int bsxPosition = 0;
    private int pfbzPosition = 0;
    private int zwsPosition = 0;
    private int rllxPosition = 0;

    // 保留上次选中状态
    private View bsx = null;
    private View pfbz = null;
    private View zws = null;
    private View rllx = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_product);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        getData();
        onCall();
        getLocation();
    }

    /* 获取数据 -变速箱、排放标准、座位数、燃料类型、 */
    private void getData() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(this, Config.GETCARPARAMDICT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                CarParams carParams = gson.fromJson(response, CarParams.class);
                CarParams.DataBean.ResultBean resultBean = carParams.getData().getResult();
                carBsxList.clear();
                carBsxIds.clear();

                carPfbzList.clear();
                carPfbzIds.clear();

                carZwsList.clear();
                carZwsIds.clear();

                carRllxList.clear();
                carRllxIds.clear();

                List<CarParams.DataBean.ResultBean.DctListBean> dictList = resultBean.getDctList();
                for (CarParams.DataBean.ResultBean.DctListBean bean : dictList) {
                    List<CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean> childList = bean.getChildParamList();
                    switch (bean.getParentParamName()) {
                        case "变速箱":
                            for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                if (childBean.getParamName().equals("不限")) {

                                } else {
                                    carBsxList.add(childBean.getParamName());
                                    carBsxIds.add(childBean.getParamId());
                                }
                            }
                            bsxAdapter.notifyDataSetChanged();
                            break;

                        case "排放标准":
                            for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                if (childBean.getParamName().equals("不限")) {

                                } else {
                                    carPfbzList.add(childBean.getParamName());
                                    carPfbzIds.add(childBean.getParamId());
                                }
                            }
                            pfbzAdapter.notifyDataSetChanged();
                            break;

                        case "座位数":
                            for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                if (childBean.getParamName().equals("不限")) {

                                } else {
                                    carZwsList.add(childBean.getParamName());
                                    carZwsIds.add(childBean.getParamId());
                                }
                            }
                            zwsAdapter.notifyDataSetChanged();
                            break;

                        case "燃油类型":
                            for (CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean : childList) {
                                if (childBean.getParamName().equals("不限")) {

                                } else {
                                    carRllxIds.add(childBean.getParamId());
                                    carRllxList.add(childBean.getParamName());
                                }
                            }
                            rllxAdapter.notifyDataSetChanged();
                            break;
                    }
                }
            }

            @Override
            public void fail(String response) {
                Gson gson = GsonFactory.create();
                CarParams carParams = gson.fromJson(response, CarParams.class);
                ToastUtil.show(AddCarProduct.this, carParams.getMessage());
            }
        });
    }

    /* 获取二手车所在城市 */
    private void getLocation() {
        Log.d(TAG, "getLocation: " + Prefs.with(getApplicationContext()).read("user_token"));
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(AddCarProduct.this, Config.GETCOMP_INFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                Enterprise.DataBean.ResultBean result = enterprise.getData().getResult();
                car_place_city = result.getAuth_comp_city();          //市
            }

            @Override
            public void fail(String response) {
                Log.d("aaaa", "fail: " + response);
                Gson gson = GsonFactory.create();
                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                ToastUtil.show(AddCarProduct.this, enterprise.getMessage());
            }
        });
        seekbar.setValue(1f);
        seekbar.setOnRangeChangedListener(new RangeSeekbar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekbar rangeSeekbar, float min, float max, boolean isFromUser) {
                if (isFromUser) {
//                    car_displacement = (Math.round(min * 10) / 10) + "";
                    car_displacement = df.format(min);
                    seekbar.setLeftProgress(df.format(min));
                }
            }
        });


    }

    /* eventbus获取数据 */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void qwe(String b) {
        if (!b.isEmpty()) {
            //车型名+车型id + 品牌名+品牌id
            String[] split = b.split(",");
            car_name = split[2] + split[0];

            mCarType.setText(split[0]);

            mBrand.setText(split[2]);

            car_model = split[1];

            car_brand = split[3];
            Log.d("qwe", "qwe: " + b);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /* 品牌选择、上牌时间、相机点击事件 */
    public void onCall() {
        //品牌选择
        mBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inten(ModelsChoose.class);
            }
        });

        //上牌时间
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePicker picker = new DatePicker(AddCarProduct.this);
                picker.setCanceledOnTouchOutside(true);
                picker.setUseWeight(true);
                picker.setTopPadding(ConvertUtils.toPx(AddCarProduct.this, 10));
                picker.setRangeStart(1950, 01, 01);
                picker.setRangeEnd(DateUtil.getYEAR(), DateUtil.getMONTH(), DateUtil.getDAY());
                picker.setResetWhileWheel(false);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        car_time = year + "-" + month + "-" + day;
                        mTime.setText(car_time);

                    }
                });
                picker.show();
            }
        });

        //相机
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddCarProduct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(AddCarProduct.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                    startActivityForResult(intent, 0);
                }
            }
        });

    }

    public void inten(Class clas) {
        Intent intent = new Intent(this, clas);
        intent.putExtra("name", "1");
        startActivityForResult(intent, 0);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限成功，可以拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                startActivityForResult(intent, 0);
            } else {
                Toast.makeText(this, "请在应用管理中打开“相机”访问权限！", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0) { // 如果返回数据

            Bitmap data1 = (Bitmap) data.getExtras().get("data");
            String base64 = PhotoUtils.bitmapToBase64(data1);

            map.clear();

            if (NetUtil.isNetAvailable(this)) {
                map.put("imgBase64", base64);
                OKhttptils.post(this, Config.PARSEVIN, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Gson gson = GsonFactory.create();
                        ChassisNumber chassisNumber = gson.fromJson(response, ChassisNumber.class);
                        String vin = chassisNumber.getData().getResult().getVin();
                        mCarNumb.setText(vin);
                    }

                    @Override
                    public void fail(String response) {
                        Log.d("bbbbbb", "fail: " + response);
                        Gson gson = GsonFactory.create();
                        ChassisNumber chassisNumber = gson.fromJson(response, ChassisNumber.class);
                        ToastUtil.show(AddCarProduct.this, chassisNumber.getMessage());
                    }
                });
            }

        }


    }

    public void initView() {
        bsxAdapter = new BSXCarMoreAdapter(this, carBsxList);
        bsxAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (bsx != null && bsx != view) {
                    // 取消之前选中
                    bsx.setSelected(false);
                    //设置字体颜色
                    TextView tv = bsx.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#ff9696"));
                bsx = view;
                TextView text_type1 = view.findViewById(R.id.tv_type);
//                car_gearbox = text_type1.getText().toString().trim();
                car_gearbox = carBsxIds.get(position);
                bsxPosition = position;
            }
        });
        gvBsx.setAdapter(bsxAdapter);

        pfbzAdapter = new BSXCarMoreAdapter(this, carPfbzList);
        pfbzAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (pfbz != null) {
                    // 取消之前选中
                    pfbz.setSelected(false);
                    //设置字体颜色
                    TextView tv = pfbz.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#ff9696"));
                pfbz = view;
                TextView text_type1 = view.findViewById(R.id.tv_type);
//                car_letout = text_type1.getText().toString().trim();
                car_letout = carPfbzIds.get(position);
                pfbzPosition = position;
            }
        });
        gvPfbz.setAdapter(pfbzAdapter);

        zwsAdapter = new BSXCarMoreAdapter(this, carZwsList);
        zwsAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (zws != null) {
                    // 取消之前选中
                    zws.setSelected(false);
                    //设置字体颜色
                    TextView tv = zws.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#ff9696"));
                zws = view;
                TextView text_type1 = view.findViewById(R.id.tv_type);
//                car_seating = text_type1.getText().toString().trim();
                car_seating = carZwsIds.get(position);
                zwsPosition = position;
            }
        });
        gvZws.setAdapter(zwsAdapter);

        rllxAdapter = new BSXCarMoreAdapter(this, carRllxList);
        rllxAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (rllx != null) {
                    // 取消之前选中
                    rllx.setSelected(false);
                    //设置字体颜色
                    TextView tv = rllx.findViewById(R.id.tv_type);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.tv_type);
                tv.setTextColor(Color.parseColor("#ff9696"));
                rllx = view;
                TextView text_type1 = view.findViewById(R.id.tv_type);
//                car_fuel_type = text_type1.getText().toString().trim();
                car_fuel_type = carRllxIds.get(position);
                rllxPosition = position;
            }
        });
        gvRllx.setAdapter(rllxAdapter);
//        //下一步
//        mNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                car_number = mCarNumb.getText().toString().trim();
//                car_resum = carResume.getText().toString().trim();
//                car_mileage = mKm.getText().toString().trim();
//                car_price = mPrice.getText().toString().trim();
//                car_name = mBrand.getText().toString().trim();
////                car_model = mCarType.getText().toString().trim();
//                Log.d("oneror", "onClick: " + "车架号" + car_number + "----品牌" + car_brand + "----车型" + car_model + "----车显里程" + car_mileage + "----车辆售价" + car_price + "----上牌时间" + car_time + "----车辆简历" + car_resum);
//                if (!car_number.isEmpty() && !car_resum.isEmpty() && !car_mileage.isEmpty() && !car_price.isEmpty() && !car_name.isEmpty() && !car_model.isEmpty()) {
//                    //已完善信息
////                    postMeages(car_number, car_brand, car_model, car_mileage, car_price, car_time, car_gearbox, car_letout, car_seating, car_fuel_type, car_resum, car_name, car_displacement, car_place_city);
//                } else {
//                    final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(AddCarProduct.this)
//                            .setMessage1("温馨提示").setMessage2(" 请完善信息").setConfirm("确定").showDialog();
//                    ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
//                        @Override
//                        public void onNoClick() {
//                            ordinaryDialog.dismiss();
//                        }
//                    });
//                    ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
//                        @Override
//                        public void onYesClick() {
//
//                            ordinaryDialog.dismiss();
//                        }
//                    });
//                }
//            }
//        });
    }


//    public void postMeages(String car_number, String car_brand, String car_model, String car_mileage, String car_price, String car_time, String car_gearbox, String car_letout, String car_seating, String car_fuel_type, String car_resum, String car_name, String car_emissions, String car_place_city) {
//        map2.clear();
//        if (NetUtil.isNetAvailable(this)) {
//            map2.put("car_name", car_name);
//            map2.put("car_brand", car_brand);
//            map2.put("car_model", car_model);
//            map2.put("car_mileage", car_mileage);
//            map2.put("car_price", car_price);
//            map2.put("car_sign_date", car_time);
//            map2.put("car_gearbox", car_gearbox);
//            map2.put("car_emissions", car_emissions);
//            map2.put("car_letout", car_letout);
//            map2.put("car_desc", car_resum);
//            map2.put("car_place_city", car_place_city);
//            map2.put("car_fuel_type", car_fuel_type);
//            map2.put("car_seating", car_seating);
//            map2.put("car_vin", car_number);
////            post(AddCarProduct.this,Config.QUERYCARSTYLE,map2);
//            OKhttptils.post(this, Config.ACCRETIONCAR, map2, new OKhttptils.HttpCallBack() {
//                @Override
//                public void success(String response) {
//                    Log.i("oneror", "添加二手车信息: " + response);
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        JSONObject data = jsonObject.getJSONObject("data");
//                        String result = data.getString("result");
//                        Intent intent = new Intent(AddCarProduct.this, CarPhotoActivity.class);
//                        intent.putExtra("result", result);
//                        startActivity(intent);
//                        finish();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void fail(String response) {
//                    try {
//                        JSONObject object = new JSONObject(response);
//                        ToastUtil.show(AddCarProduct.this, object.getString("message"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    }\\

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("添加产品");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(AddCarProduct.this);
            }
        });
    }

    @OnClick(R.id.mNext)
    public void onViewClicked() {
        car_mileage = mKm.getText().toString().trim();
        car_price = mPrice.getText().toString().trim();
        car_time = mTime.getText().toString().trim();
        car_resum = carResume.getText().toString().trim();
        car_vin = mCarNumb.getText().toString().trim();

        Log.d(TAG, "onViewClicked车辆名称: " + car_name);
        Log.e(TAG, "onViewClicked车品牌id: " + car_brand);
        Log.i(TAG, "onViewClicked车型id: " + car_model);
        Log.w(TAG, "onViewClicked车里程: " + car_mileage);
        Log.d(TAG, "onViewClicked车售价: " + car_price);
        Log.e(TAG, "onViewClicked上牌时间: " + car_time);
        Log.i(TAG, "onViewClicked变速箱类型: " + car_gearbox);
        Log.w(TAG, "onViewClicked车排量: " + car_displacement);
        Log.d(TAG, "onViewClicked排放标准: " + car_letout);
        Log.e(TAG, "onViewClicked车辆介绍: " + car_resum);
        Log.i(TAG, "onViewClicked车辆所在城市: " + car_place_city);
        Log.w(TAG, "onViewClicked燃料类型: " + car_fuel_type);
        Log.d(TAG, "onViewClicked座位数: " + car_seating);
        Log.e(TAG, "onViewClicked车架号: " + car_vin);

        Map<String, String> map = new HashMap<>();
        map.put("car_name", car_name);
        map.put("car_brand", car_brand);
        map.put("car_model", car_model);
        map.put("car_mileage", car_mileage);
        map.put("car_price", car_price);
        map.put("car_sign_date", car_time + " 00:00:00");
        map.put("car_gearbox", car_gearbox);
        map.put("car_emissions", car_displacement);
        map.put("car_letout", car_letout);
        map.put("car_desc", car_resum);
        map.put("car_place_city", car_place_city);
        map.put("car_fuel_type", car_fuel_type);
        map.put("car_seating", car_seating);
        map.put("car_vin", car_vin);
        OKhttptils.post(AddCarProduct.this, Config.ACCRETIONCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success: " + response);
                /**
                 * {"data":{"result":"17492"},"message":"","status":"1"}
                 * */
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String result = object.getString("result");
                    if (result.isEmpty()) {
                    } else {
                        //上传成功  result为car_id;
                        Bundle bundle = new Bundle();
                        bundle.putString("car_id",result);
                        JumpUtil.newInstance().jumpRight(AddCarProduct.this,CarPhotoActivity.class,bundle);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ToastUtil.show(AddCarProduct.this, object.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
