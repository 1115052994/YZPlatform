package com.xtzhangbinbin.jpq.activity;

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
import com.jaygoo.widget.RangeSeekbar;
import com.xtzhangbinbin.jpq.AppraiseInterface;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.BSXCarMoreAdapter;
import com.xtzhangbinbin.jpq.adapter.Grid_Updata;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.ChassisNumber;
import com.xtzhangbinbin.jpq.entity.Enterprise;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.view.ExpandableGridView;
import com.xtzhangbinbin.jpq.view.MyGridView;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class UpdateCar extends BaseActivity {

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
    @BindView(R.id.seekbar)
    RangeSeekbar seekbar;
    @BindView(R.id.grid_wai)
    MyGridView gridWai;
    @BindView(R.id.grid_nei)
    MyGridView gridNei;


    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, String> map2 = new HashMap<>();
    private ArrayList<HashMap<String, String>> arrwai = new ArrayList<>();
    private ArrayList<HashMap<String, String>> arrnei = new ArrayList<>();
    private String car_number;  //车架号     已赋值
    private String car_brand;  //品牌id      已赋值
    private String car_models; //车型        已赋值
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

    private DecimalFormat df = new DecimalFormat("0.0");


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
        setContentView(R.layout.activity_update_car);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate: ");
        EventBus.getDefault().register(this);
        Log.d(TAG, "onCreate: ");
        String carid = getIntent().getStringExtra("carid");

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
        //得到当前位置
        getLocation();

        gridWai.setAdapter(new Grid_Updata(arrwai, UpdateCar.this));
        gridNei.setAdapter(new Grid_Updata(arrnei, UpdateCar.this));

    }

    private void getLocation() {
        Log.d(TAG, "getLocation: " + Prefs.with(getApplicationContext()).read("user_token"));
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(UpdateCar.this, Config.GETCOMP_INFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                Gson gson = GsonFactory.create();
                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                Enterprise.DataBean.ResultBean result = enterprise.getData().getResult();
                car_place_city = result.getAuth_comp_city();          //市
//                String auth_comp_province = result.getAuth_comp_province();  //省
//                Log.d("aaaa", "success: "+car_place_city+auth_comp_province);

            }

            @Override
            public void fail(String response) {
//                Gson gson = GsonFactory.create();
//                Enterprise enterprise = gson.fromJson(response, Enterprise.class);
                Log.d("aaaa", "fail: " + response);
//                ToastUtil.show(com.xtzhangbinbin.jpq.activity.AddCarProduct.this, enterprise.getMessage());
            }
        });
        seekbar.setValue(1f);
        seekbar.setOnRangeChangedListener(new RangeSeekbar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekbar rangeSeekbar, float min, float max, boolean isFromUser) {
                if (isFromUser) {
//                    tv2.setText(""+(float)Math.round(min*10)/10);
                    car_displacement = (Math.round(min * 10) / 10) + "";
                    seekbar.setLeftProgress(df.format(min));
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }


    //这个会自动接收值
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void qwe(String b) {
        if (b != null) {
            carCx.setText(b);
            String[] split = b.split("  ");
            car_models = split[0];
            Log.d("qwe", "qwe: " + split.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void onCall() {
        //品牌选择
        brandChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inten(ModelsChoose.class);
            }
        });
//        //车型选择
//        modelsChoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                inten(ModelsChoose.class);
//            }
//        });
        //上牌时间
        timeChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();//获取当前时间
                final DatePicker picker = new DatePicker(UpdateCar.this);
                picker.setCanceledOnTouchOutside(true);
                picker.setUseWeight(true);
                picker.setTopPadding(ConvertUtils.toPx(UpdateCar.this, 10));
                picker.setRangeStart(c.get(Calendar.YEAR), 1, 1);
                picker.setRangeEnd(1770, 12, 1);
                picker.setResetWhileWheel(false);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        carSp.setText(year + "年" + month + "月" + day + "日");

//                        try {
//                            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
//                            car_time = bartDateFormat.parse(year + "年" + month + "月" + day + "日");
                        car_time = year + month + day;
//                            Log.d("aaaaa", "onDatePicked: " + car_time);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }

                    }
                });
                picker.show();
            }
        });

        //相机
        AddCarProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateCar.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(UpdateCar.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                } else {
                    //有权限，直接拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                    startActivityForResult(intent, 0);
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
        intent.putExtra("name","2");
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
            Log.d("bbbbbb", "onActivityResult: " + base64);
            map.clear();
            if (NetUtil.isNetAvailable(this)) {
                map.put("imgBase64", base64);
                Log.d("bbbbbb", "onActivityResult: ");
                OKhttptils.post(this, Config.PARSEVIN, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Gson gson = GsonFactory.create();
                        ChassisNumber chassisNumber = gson.fromJson(response, ChassisNumber.class);
                        String vin = chassisNumber.getData().getResult().getVin();
                        carCjh.setText(vin);

                    }

                    @Override
                    public void fail(String response) {
                        Log.d("bbbbbb", "fail: " + response);
                        Toast.makeText(UpdateCar.this, "获取车架号失败", Toast.LENGTH_SHORT).show();
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
        }


    }

    public void initView() {
        BSXCarMoreAdapter bsxAdapter = new BSXCarMoreAdapter(this, carBsxList);
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
                car_gearbox = text_type1.getText().toString().trim();
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
                car_letout = text_type1.getText().toString().trim();
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
                car_seating = text_type1.getText().toString().trim();
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
                car_fuel_type = text_type1.getText().toString().trim();
                rllxPosition = position;
            }
        });
        gvRllx.setAdapter(rllxAdapter);


        //下一步保存
        butText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car_number = carCjh.getText().toString().trim();
                car_resum = carResume.getText().toString().trim();
                car_mileage = carLc.getText().toString().trim();
                car_price = carSj.getText().toString().trim();
                car_name = carPp.getText().toString().trim();
                car_models = carCx.getText().toString().trim();
                if (!carCjh.getText().toString().trim().isEmpty() && !carSj.getText().toString().trim().isEmpty() && !carCx.getText().toString().trim().isEmpty() && !carPp.getText().toString().trim().isEmpty() && !carLc.getText().toString().trim().isEmpty() && !carSj.getText().toString().trim().isEmpty() && car_time != null && !carResume.getText().toString().trim().isEmpty()) {
                    //已完善信息
                    postMeages(car_number, car_brand, car_models, car_mileage, car_price, car_time, car_gearbox, car_letout, car_seating, car_fuel_type, car_resum, car_name, car_displacement, car_place_city);
                    Log.d("oneror", "onClick: " + "车架号" + car_number + "----品牌id" + car_brand + "----车型" + car_models + "----车显里程" + car_mileage + "----车辆售价" + car_price + "----上牌时间" + car_time + "----车辆简历" + car_resum);
                } else {
                    final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(UpdateCar.this)
                            .setMessage1("温馨提示").setMessage2(" 请完善信息").setConfirm("确定").showDialog();
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
//                Log.d("oneror", "onClick: " + "车架号" + car_number + "----品牌id" + car_brand + "----车型" +
//                        car_models + "----车显里程" + car_mileage + "----车辆售价" + car_price + "----上牌时间" + car_time +
//                        "----变速箱" + car_gearbox + "----排放标准" + car_letout + "----座位数" + car_seating + "----燃料类型" +
//                        car_fuel_type + "----车辆简历" + car_resum+"----車排量"+car_displacement+"----車名稱"+car_name+"----所在城市"+car_place_city);
            }
        });
    }


    public void postMeages(String car_number, String car_brand, String car_model, String car_mileage, String car_price, String car_time, String car_gearbox, String car_letout, String car_seating, String car_fuel_type, String car_resum, String car_name, String car_emissions, String car_place_city) {
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
//            post(AddCarProduct.this,Config.QUERYCARSTYLE,map2);
            OKhttptils.post(this, Config.ACCRETIONCAR, map2, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("oneror", "添加二手车信息: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String result = data.getString("result");
                        Intent intent = new Intent(UpdateCar.this, CarPhotoActivity.class);
                        intent.putExtra("result", result);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void fail(String response) {
                    Toast.makeText(UpdateCar.this, "添加失敗", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle("修改产品信息");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(UpdateCar.this);
            }
        });
    }
}
