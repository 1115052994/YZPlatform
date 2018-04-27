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
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plt.yzplatform.AppraiseInterface;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.BSXCarMoreAdapter;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.ChassisNumber;
import com.plt.yzplatform.entity.Enterprise;
import com.plt.yzplatform.entity.QueryCarList;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.PhotoUtils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.ExpandableGridView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

public class AddCarProduct extends BaseActivity {
    private static final String TAG =  "添加汽车产品";
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
    @BindView(R.id.but_text)
    TextView butText;
    @BindView(R.id.car_cjh)
    EditText carCjh;
    @BindView(R.id.car_lc)
    EditText carLc;
    @BindView(R.id.car_sj)
    EditText carSj;
    @BindView(R.id.AddCarProduct_)
    LinearLayout AddCarProduct;
    @BindView(R.id.brand_choice)
    LinearLayout brandChoice;
    @BindView(R.id.models_choice)
    LinearLayout modelsChoice;
    @BindView(R.id.time_choose)
    LinearLayout timeChoose;
    @BindView(R.id.car_pp)
    TextView carPp;
    @BindView(R.id.car_cx)
    TextView carCx;
    @BindView(R.id.car_sp)
    TextView carSp;
    private boolean bs=false;
    private boolean pf=false;
    private boolean zw=false;
    private boolean rl=false;

    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, Object> map2 = new HashMap<>();
    private String car_number;  //车架号     已赋值
    private String car_brand;  //品牌id      已赋值
    private String car_models; //车型        已赋值
    private double car_mileage;  //车显里程   已赋值
    private double car_price;  //车辆售价     已赋值
    private Date car_time;  //上牌时间    已赋值
    private String car_gearbox; //变速箱     已赋值
    private String car_letout;  //排放标准   已赋值
    private String car_seating; //座位数   已赋值
    private String car_fuel_type; //燃料类型   已赋值
    private String car_resum;  //车辆简历    已赋值


    //变速箱
    private List<String> carBsxList = new ArrayList<>();
    private BSXCarMoreAdapter bsxAdapter;
    //排行标准
    private List<String> carPfbzList = new ArrayList<>();
    private BSXCarMoreAdapter pfbzAdapter;
    //座位数
    private List<String> carZwsList = new ArrayList<>();
    private BSXCarMoreAdapter zwsAdapter;
    //燃料类型
    private List<String> carRllxList = new ArrayList<>();
    private BSXCarMoreAdapter rllxAdapter;


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
        carBsxList.add("手动");
        carBsxList.add("自动");
        carPfbzList.add("国三");
        carPfbzList.add("国四");
        carPfbzList.add("国五");
        carZwsList.add("2座");
        carZwsList.add("4座");
        carZwsList.add("5座");
        carZwsList.add("7座");
        carZwsList.add("7座以上");
        carRllxList.add("汽油");
        carRllxList.add("柴油");
        carRllxList.add("电动");
        carRllxList.add("电动混合");
        initView();
        onCall();
        Intent intent = getIntent();
        String series_itim = intent.getStringExtra("series_itim");
        String tv_carbrand = intent.getStringExtra("tv_carbrand");
        //获取车型
        if(series_itim!=null){
            Log.d("aaaaa", "aaa: "+series_itim);
            carCx.setText(series_itim+" "+tv_carbrand);
            car_models=series_itim;
        }
        //得到当前位置
        getLocation();
    }

    private void getLocation() {
        Log.d("aaaa", "getLocation: ");
        Log.d(TAG, "getLocation: "+ Prefs.with(getApplicationContext()).read("user_token"));
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(AddCarProduct.this, Config.GETCOMP_INFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                Enterprise.DataBean.ResultBean result = enterprise.getData().getResult();
                String auth_comp_city = result.getAuth_comp_city();          //市
                String auth_comp_province = result.getAuth_comp_province();  //省
                Log.d("aaaa", "success: "+auth_comp_city+auth_comp_province);

            }

            @Override
            public void fail(String response) {
                Gson gson = GsonFactory.create();
                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                Log.d("aaaa", "fail: "+response);
                ToastUtil.show(com.plt.yzplatform.activity.AddCarProduct.this,enterprise.getMessage());
            }
        });
//        if (NetUtil.isNetAvailable(this)) {
//            OkHttpUtils.get()
//                    .url(Config.GETCOMP_INFO)
//                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            ToastUtil.noNAR(EnterpriseActivity.this);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            Log.d(TAG, "onResponse获取数据: " + response);
//                            Gson gson = GsonFactory.create();
//                            Enterprise enterprise = gson.fromJson(response, Enterprise.class);
//                            if ("1".equals(enterprise.getStatus())) {
//                                Enterprise.DataBean dataBean = enterprise.getData();
//                                if (null == dataBean.getResult()) {
//                                    //如果为空 只加载服务类型
//                                    getService1();
//                                    uploading.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            Log.w(TAG, "onClick城市城市！！！: " + shi  );
//                                            getCityId(shi.substring(0,shi.length()-1));
//                                            uploading1();
//                                        }
//                                    });
//                                } else {
//                                    //获取到数据 填充数据
//                                    Enterprise.DataBean.ResultBean resultBean = enterprise.getData().getResult();
//                                    //审核状态
//                                    if ("1".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("审核拒绝");
//                                        setEdit(compAddress, true);
//                                        setEdit(getLocation, true);
//                                        setEdit(compAddress, true);
//                                        setEdit(business, true);
//                                        setEdit(linkName, true);
//                                        setEdit(linkPhone, true);
//                                        setEdit(headPic, true);
//                                        uploading.setVisibility(View.VISIBLE);
//                                    } else if ("2".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("审核通过");
//                                        uploading.setVisibility(View.VISIBLE);
//                                    } else if ("3".equals(resultBean.getAuth_audit_state())) {
//                                        setRightText("正在审核");
//                                        setEdit(compAddress, true);
//                                        setEdit(getLocation, true);
//                                        setEdit(compAddress, true);
//                                        setEdit(business, true);
//                                        setEdit(linkName, true);
//                                        setEdit(linkPhone, true);
//                                        setEdit(headPic, true);
//                                        uploading.setVisibility(View.GONE);
//                                    }
//                                    //赋值
//                                    compName.setText(resultBean.getAuth_comp_name());
//                                    getLocation.setText(resultBean.getAuth_comp_lon() + "," + resultBean.getAuth_comp_lat());
//                                    compAddress.setText(resultBean.getAuth_comp_addr());
//                                    String yingye = resultBean.getAuth_comp_file_id();
//                                    Prefs.with(getApplicationContext()).write("营业执照",yingye);
//                                    business.setText("已上传");
//                                    linkName.setText(resultBean.getAuth_comp_linkman());
//                                    linkPhone.setText(resultBean.getAuth_comp_phone());
//                                    String mentou = resultBean.getAuth_comp_img_head_file_id();
//                                    Prefs.with(getApplicationContext()).write("门头照",mentou);
//                                    headPic.setText("已上传");
//                                    updateType = resultBean.getAuth_comp_service_type();
//                                    lon = String.valueOf(resultBean.getAuth_comp_lon());
//                                    lat = String.valueOf(resultBean.getAuth_comp_lat());
//                                    province_id = resultBean.getAuth_comp_province();
//                                    city_id = resultBean.getAuth_comp_city();
//
//                                    String as [] = updateType.split(", ");
//                                    for (int i = 0; i < as.length ; i++) {
//                                        type_id.add(as[i]);
//                                    }
//
//                                    getService2();
//                                    uploading.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            uploading2();
//                                        }
//                                    });
//                                }
//                            }
//                        }
//                    });
//        } else {
//            ToastUtil.noNetAvailable(this);
//        }
    }


    public void onCall() {
        //品牌选择
        brandChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inten(BrandChoice.class);
            }
        });
        //车型选择
        modelsChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inten(ModelsChoose.class);
            }
        });
        //上牌时间
        timeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();//获取当前时间
                final DatePicker picker = new DatePicker(AddCarProduct.this);
                picker.setCanceledOnTouchOutside(true);
                picker.setUseWeight(true);
                picker.setTopPadding(ConvertUtils.toPx(AddCarProduct.this, 10));
                picker.setRangeStart(c.get(Calendar.YEAR), 1, 1);
                picker.setRangeEnd(1770, 12, 1);
                picker.setResetWhileWheel(false);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        carSp.setText(year+"年"+month+"月"+day+"日");

                        try {
                            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                            car_time = bartDateFormat.parse(year+"年"+month+"月"+day+"日");
                            Log.d("aaaaa", "onDatePicked: "+car_time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });
                picker.show();
            }
        });
        //下一步
        butText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_resum = carResume.getText().toString().trim();
                if(!carLc.getText().toString().trim().isEmpty()){
                    car_mileage = Double.parseDouble(carLc.getText().toString().trim());
                }
                if(!carSj.getText().toString().trim().isEmpty()){
                    car_price=Double.parseDouble(carSj.getText().toString().trim());
                }
                if(bs&&pf&&zw&&rl&&carCjh!=null&&carCx!=null&&carPp!=null&&carLc!=null&&carSj!=null&&carSp!=null&&carResume!=null){
                    //已选择
                }
                Log.d("aaaaa", "onClick: "+"车架号"+car_number+"----品牌id"+car_brand+"----车型"+
                        car_models+"----车显里程"+car_mileage+"----车辆售价"+car_price+"----上牌时间"+car_time+
                        "----变速箱"+car_gearbox+"----排放标准"+car_letout+"----座位数"+car_seating+"----燃料类型"+
                        car_fuel_type+"----车辆简历"+car_resum);
//              PostMeages(car_number,car_brand,car_models,car_mileage,car_price,car_time,car_gearbox,car_letout,car_seating,car_fuel_type,car_resum);
            }
        });
        //相机
        AddCarProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddCarProduct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(AddCarProduct.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                    startActivityForResult(intent, 1);
                }
            }
        });


        butText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        butText.setBackgroundColor(Color.parseColor("#ff5656"));
                        break;
                    case MotionEvent.ACTION_UP:
                        butText.setBackgroundColor(Color.parseColor("#ff9696"));
                        break;
                }
                return false;
            }
        });
    }

    public void inten(Class clas) {
        Intent intent = new Intent(this, clas);
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
                finish();
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
                Log.d("aaaaa", "onActivityResult: ");
                OKhttptils.post(this, Config.PARSEVIN, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Gson gson = GsonFactory.create();
                        ChassisNumber chassisNumber = gson.fromJson(response, ChassisNumber.class);
                        String vin = chassisNumber.getData().getResult().getVin();
                        car_number = vin;
                        carCjh.setText(vin);
                    }

                    @Override
                    public void fail(String response) {
                        Toast.makeText(AddCarProduct.this, "获取车架号失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }


//            //做缓存存储
//            if (resultCode == Activity.RESULT_OK) {
//                String sdStatus = Environment.getExternalStorageState();
//                if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
//                    Log.i("TestFile", "SD card is not avaiable/writeable right now.");
//                    return;
//                }
//                new DateFormat();
//                String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//                Toast.makeText(this, name, Toast.LENGTH_LONG).show();
//                Bundle bundle = data.getExtras();
//                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
//
//                FileOutputStream b = null;
//                File file = new File("/sdcard/Image26/");
//                file.mkdirs();// 创建文件夹
//                String fileName = "/sdcard/Image/" + name;
//
//                try {
//                    b = new FileOutputStream(fileName);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        b.flush();
//                        b.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                try {
//
//                } catch (Exception e) {
//                    Log.e("error", e.getMessage());
//                }
//
//            }
        } else if (requestCode == 0 && resultCode == 1) {
            //接收品牌数据
            Bundle bundle = data.getBundleExtra("bundle");
            String tv_carbrand = bundle.getString("tv_carbrand");
            String id_carbrand = bundle.getString("id_carbrand");
            car_brand = id_carbrand;
            carPp.setText(tv_carbrand);
            Log.d("aaaaa", "onActivityResult: " + "你选择了" + tv_carbrand + id_carbrand);

        } else if (requestCode == 0 && resultCode == 3) {
            //接收上牌时间数据
            Log.d("aaaaa", "onActivityResult: "+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        }


    }

    public void initView() {
        bsxAdapter = new BSXCarMoreAdapter(this, carBsxList);
        bsxAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
                //设置响应事件
                if (bsx != null) {
                    // 取消之前选中
                    bsx.setSelected(false);
                    //设置字体颜色
                    TextView tv = bsx.findViewById(R.id.text_type1);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.text_type1);
                tv.setTextColor(Color.parseColor("#ff9696"));
                bsx = view;
                TextView text_type1 = view.findViewById(R.id.text_type1);
                car_gearbox = text_type1.getText().toString().trim();
                bsxPosition = position;
                bs=true;
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
                    TextView tv = pfbz.findViewById(R.id.text_type1);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.text_type1);
                tv.setTextColor(Color.parseColor("#ff9696"));
                pfbz = view;
                TextView text_type1 = view.findViewById(R.id.text_type1);
                car_letout=text_type1.getText().toString().trim();
                pfbzPosition = position;
                pf=true;
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
                    TextView tv = zws.findViewById(R.id.text_type1);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.text_type1);
                tv.setTextColor(Color.parseColor("#ff9696"));
                zws = view;
                TextView text_type1 = view.findViewById(R.id.text_type1);
                car_seating = text_type1.getText().toString().trim();
                zwsPosition = position;
                zw=true;
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
                    TextView tv = rllx.findViewById(R.id.text_type1);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                view.setSelected(true);
                //设置字体颜色
                TextView tv = view.findViewById(R.id.text_type1);
                tv.setTextColor(Color.parseColor("#ff9696"));
                rllx = view;
                TextView text_type1 = view.findViewById(R.id.text_type1);
                car_fuel_type = text_type1.getText().toString().trim();
                rllxPosition = position;
                rl=true;
            }
        });
        gvRllx.setAdapter(rllxAdapter);
    }


    public void PostMeages(String car_number, String car_brand, String car_model, double car_mileage, double car_price, Date car_time, String car_gearbox,  String car_letout, String car_seating,String car_fuel_type, String car_resum, String car_name,double car_emissions,String car_place_city) {
        map2.clear();
        if (NetUtil.isNetAvailable(this)) {
            map2.put("car_name", car_name);
            map2.put("car_brand", car_brand);
            map2.put("car_model", car_model);
            map2.put("car_mileage", car_mileage);
            map2.put("car_price", car_price);
            map2.put("car_sign_date", car_time);
            map2.put("car_gearbox", car_gearbox);
            map2.put("car_emissions", car_emissions);
            map2.put("car_letout", car_letout);
            map2.put("car_desc", car_resum);
            map2.put("car_place_city", car_place_city);
            map2.put("car_fuel_type", car_fuel_type);
            map2.put("car_seating", car_seating);
            map2.put("car_vin", car_number);
            OKhttptils.post(this, Config.ACCRETIONCAR, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("aaaaa", "添加二手车信息: " + response);
                    Gson gson = GsonFactory.create();
                    QueryCarList querystar = gson.fromJson(response, QueryCarList.class);
                    List<QueryCarList.DataBean.ResultBean> result = querystar.getData().getResult();
                    // 没有信息图片显示

                }

                @Override
                public void fail(String response) {
                    Toast.makeText(AddCarProduct.this, "查询失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


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
}
