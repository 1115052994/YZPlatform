package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.xtzhangbinbin.jpq.entity.CarParams;
import com.xtzhangbinbin.jpq.entity.CarPhotos;
import com.xtzhangbinbin.jpq.entity.CarProductDetail;
import com.xtzhangbinbin.jpq.entity.ChassisNumber;
import com.xtzhangbinbin.jpq.entity.Enterprise;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.DateUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.NetUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.PhotoUtils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.ExpandableGridView;
import com.xtzhangbinbin.jpq.view.MyGridView;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

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

public class UpdateCarActivity extends BaseActivity {

    private static final String TAG = "修改二手车信息";
    @BindView(R.id.mCarNumb)
    EditText mCarNumb;
    @BindView(R.id.mPhoto)
    TextView mPhoto;
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
    @BindView(R.id.gv_bsx)
    ExpandableGridView gvBsx;
    @BindView(R.id.gv_pfbz)
    ExpandableGridView gvPfbz;
    @BindView(R.id.seekbar)
    RangeSeekbar seekbar;
    @BindView(R.id.gv_zws)
    ExpandableGridView gvZws;
    @BindView(R.id.gv_rllx)
    ExpandableGridView gvRllx;
    @BindView(R.id.car_resume)
    EditText carResume;
    @BindView(R.id.grid_wai)
    MyGridView gridWai;
    @BindView(R.id.grid_nei)
    MyGridView gridNei;
    @BindView(R.id.mSave)
    TextView mSave;
    @BindView(R.id.grid_fadong)
    MyGridView gridFadong;
    @BindView(R.id.grid_more)
    MyGridView gridMore;


    private HashMap<String, String> map = new HashMap<>();
    private HashMap<String, String> map2 = new HashMap<>();
    private List<HashMap<String, String>> arrwai = new ArrayList<>();
    private List<HashMap<String, String>> arrnei = new ArrayList<>();
    private List<HashMap<String, String>> arrFadong = new ArrayList<>();
    private List<HashMap<String, String>> arrDetail = new ArrayList<>();

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
    private String car_id;
    private List<String> file_id = new ArrayList<>();//24张原图id
    private List<String> sfile_id = new ArrayList<>();//24张缩略图id
    private List<CarPhotos> photoList = new ArrayList<>();

    private ArrayList<String> arr = new ArrayList<>();//24个名称
    private List<String> string_id = new ArrayList<>();

    private CarProductDetail.DataBean.ResultBean resultBean;
    private Grid_Updata adapter;


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
    private List<CarParams.DataBean.ResultBean.DctListBean> dctListBeans = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    dctListBeans = (List<CarParams.DataBean.ResultBean.DctListBean>) msg.obj;
                    for (CarParams.DataBean.ResultBean.DctListBean bean : dctListBeans) {
                        List<CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean> childList = bean.getChildParamList();
                        CarParams.DataBean.ResultBean.DctListBean.ChildParamListBean childBean;
                        switch (bean.getParentParamName()) {
                            case "变速箱":
                                for (int i = 0; i < childList.size(); i++) {
                                    childBean = childList.get(i);
                                    if (childBean.getParamName().equals("不限")) {

                                    } else {
                                        carBsxList.add(childBean.getParamName());
                                        carBsxIds.add(childBean.getParamId());
                                    }
                                }

                                break;

                            case "排放标准":
                                for (int i = 0; i < childList.size(); i++) {
                                    childBean = childList.get(i);
                                    if (childBean.getParamName().equals("不限")) {

                                    } else {
                                        carPfbzList.add(childBean.getParamName());
                                        carPfbzIds.add(childBean.getParamId());
                                    }
                                }
                                break;

                            case "座位数":
                                for (int i = 0; i < childList.size(); i++) {
                                    childBean = childList.get(i);
                                    if (childBean.getParamName().equals("不限")) {

                                    } else {
                                        carZwsList.add(childBean.getParamName());
                                        carZwsIds.add(childBean.getParamId());
                                    }
                                }
                                break;

                            case "燃油类型":
                                for (int i = 0; i < childList.size(); i++) {
                                    childBean = childList.get(i);
                                    if (childBean.getParamName().equals("不限")) {

                                    } else {
                                        carRllxIds.add(childBean.getParamId());
                                        carRllxList.add(childBean.getParamName());
                                    }
                                }
                                break;
                        }
                    }
                    getDetail();
                    break;
                case 002:
                    photoList.clear();
                    arrwai.clear();
                    arrnei.clear();
                    arrFadong.clear();
                    arrDetail.clear();
                    sfile_id.clear();
                    resultBean = (CarProductDetail.DataBean.ResultBean) msg.obj;
                    /* 24张原图id */
                    CarPhotos carPhoto = new CarPhotos();
                    carPhoto.setCar_1_file_id(resultBean.getCar_1_file_id());
                    carPhoto.setCar_2_file_id(resultBean.getCar_2_file_id());
                    carPhoto.setCar_3_file_id(resultBean.getCar_3_file_id());
                    carPhoto.setCar_4_file_id(resultBean.getCar_4_file_id());
                    carPhoto.setCar_5_file_id(resultBean.getCar_5_file_id());
                    carPhoto.setCar_6_file_id(resultBean.getCar_6_file_id());
                    carPhoto.setCar_7_file_id(resultBean.getCar_7_file_id());
                    carPhoto.setCar_8_file_id(resultBean.getCar_8_file_id());
                    carPhoto.setCar_9_file_id(resultBean.getCar_9_file_id());
                    carPhoto.setCar_10_file_id(resultBean.getCar_10_file_id());
                    carPhoto.setCar_11_file_id(resultBean.getCar_11_file_id());
                    carPhoto.setCar_12_file_id(resultBean.getCar_12_file_id());
                    carPhoto.setCar_13_file_id(resultBean.getCar_13_file_id());
                    carPhoto.setCar_14_file_id(resultBean.getCar_14_file_id());
                    carPhoto.setCar_15_file_id(resultBean.getCar_15_file_id());
                    carPhoto.setCar_16_file_id(resultBean.getCar_16_file_id());
                    carPhoto.setCar_17_file_id(resultBean.getCar_17_file_id());
                    carPhoto.setCar_18_file_id(resultBean.getCar_18_file_id());
                    carPhoto.setCar_19_file_id(resultBean.getCar_19_file_id());
                    carPhoto.setCar_20_file_id(resultBean.getCar_20_file_id());
                    carPhoto.setCar_21_file_id(resultBean.getCar_21_file_id());
                    carPhoto.setCar_22_file_id(resultBean.getCar_22_file_id());
                    carPhoto.setCar_23_file_id(resultBean.getCar_23_file_id());
                    carPhoto.setCar_24_file_id(resultBean.getCar_24_file_id());
                    //图描述
                    carPhoto.setCar_1_desc(resultBean.getCar_1_desc());
                    carPhoto.setCar_2_desc(resultBean.getCar_2_desc());
                    carPhoto.setCar_3_desc(resultBean.getCar_3_desc());
                    carPhoto.setCar_4_desc(resultBean.getCar_4_desc());
                    carPhoto.setCar_5_desc(resultBean.getCar_5_desc());
                    carPhoto.setCar_6_desc(resultBean.getCar_6_desc());
                    carPhoto.setCar_7_desc(resultBean.getCar_7_desc());
                    carPhoto.setCar_8_desc(resultBean.getCar_8_desc());
                    carPhoto.setCar_9_desc(resultBean.getCar_9_desc());
                    carPhoto.setCar_10_desc(resultBean.getCar_10_desc());
                    carPhoto.setCar_11_desc(resultBean.getCar_11_desc());
                    carPhoto.setCar_12_desc(resultBean.getCar_12_desc());
                    carPhoto.setCar_13_desc(resultBean.getCar_13_desc());
                    carPhoto.setCar_14_desc(resultBean.getCar_14_desc());
                    carPhoto.setCar_15_desc(resultBean.getCar_15_desc());
                    carPhoto.setCar_16_desc(resultBean.getCar_16_desc());
                    carPhoto.setCar_17_desc(resultBean.getCar_17_desc());
                    carPhoto.setCar_18_desc(resultBean.getCar_18_desc());
                    carPhoto.setCar_19_desc(resultBean.getCar_19_desc());
                    carPhoto.setCar_20_desc(resultBean.getCar_20_desc());
                    carPhoto.setCar_21_desc(resultBean.getCar_21_desc());
                    carPhoto.setCar_22_desc(resultBean.getCar_22_desc());
                    carPhoto.setCar_23_desc(resultBean.getCar_23_desc());
                    carPhoto.setCar_24_desc(resultBean.getCar_24_desc());
                    carPhoto.setCar_id(resultBean.getCar_id());
                    photoList.add(carPhoto);

                    /* 车辆24张图片 */
                    addCarName();
                    sfile_id.add(resultBean.getCar_1_icon_file_id());
                    sfile_id.add(resultBean.getCar_2_icon_file_id());
                    sfile_id.add(resultBean.getCar_3_icon_file_id());
                    sfile_id.add(resultBean.getCar_4_icon_file_id());
                    sfile_id.add(resultBean.getCar_5_icon_file_id());
                    sfile_id.add(resultBean.getCar_6_icon_file_id());
                    sfile_id.add(resultBean.getCar_7_icon_file_id());
                    sfile_id.add(resultBean.getCar_8_icon_file_id());
                    sfile_id.add(resultBean.getCar_9_icon_file_id());
                    sfile_id.add(resultBean.getCar_10_icon_file_id());
                    sfile_id.add(resultBean.getCar_11_icon_file_id());
                    sfile_id.add(resultBean.getCar_12_icon_file_id());
                    sfile_id.add(resultBean.getCar_13_icon_file_id());
                    sfile_id.add(resultBean.getCar_14_icon_file_id());
                    sfile_id.add(resultBean.getCar_15_icon_file_id());
                    sfile_id.add(resultBean.getCar_16_icon_file_id());
                    sfile_id.add(resultBean.getCar_17_icon_file_id());
                    sfile_id.add(resultBean.getCar_18_icon_file_id());
                    sfile_id.add(resultBean.getCar_19_icon_file_id());
                    sfile_id.add(resultBean.getCar_20_icon_file_id());
                    sfile_id.add(resultBean.getCar_21_icon_file_id());
                    sfile_id.add(resultBean.getCar_22_icon_file_id());
                    sfile_id.add(resultBean.getCar_23_icon_file_id());
                    sfile_id.add(resultBean.getCar_24_icon_file_id());


                    HashMap<String, String> map1 = new HashMap<>();
                    HashMap<String, String> map2 = new HashMap<>();
                    HashMap<String, String> map3 = new HashMap<>();
                    HashMap<String, String> map4 = new HashMap<>();
                    HashMap<String, String> map5 = new HashMap<>();
                    HashMap<String, String> map6 = new HashMap<>();

                    map1.put(arr.get(0), resultBean.getCar_1_icon_file_id());
                    map2.put(arr.get(1), resultBean.getCar_2_icon_file_id());
                    map3.put(arr.get(2), resultBean.getCar_3_icon_file_id());
                    map4.put(arr.get(3), resultBean.getCar_4_icon_file_id());
                    map5.put(arr.get(4), resultBean.getCar_5_icon_file_id());
                    map6.put(arr.get(5), resultBean.getCar_6_icon_file_id());
                    arrwai.add(map1);
                    arrwai.add(map2);
                    arrwai.add(map3);
                    arrwai.add(map4);
                    arrwai.add(map5);
                    arrwai.add(map6);

                    adapter = new Grid_Updata(arrwai, UpdateCarActivity.this, 0, photoList);
                    gridWai.setAdapter(adapter);


                    HashMap<String, String> map7 = new HashMap<>();
                    HashMap<String, String> map8 = new HashMap<>();
                    HashMap<String, String> map9 = new HashMap<>();
                    HashMap<String, String> map10 = new HashMap<>();
                    HashMap<String, String> map11 = new HashMap<>();
                    HashMap<String, String> map12 = new HashMap<>();
                    HashMap<String, String> map13 = new HashMap<>();
                    HashMap<String, String> map14 = new HashMap<>();
                    HashMap<String, String> map15 = new HashMap<>();
                    HashMap<String, String> map16 = new HashMap<>();
                    HashMap<String, String> map17 = new HashMap<>();
                    HashMap<String, String> map18 = new HashMap<>();

                    map7.put(arr.get(6), resultBean.getCar_7_icon_file_id());
                    map8.put(arr.get(7), resultBean.getCar_8_icon_file_id());
                    map9.put(arr.get(8), resultBean.getCar_9_icon_file_id());
                    map10.put(arr.get(9), resultBean.getCar_10_icon_file_id());
                    map11.put(arr.get(10), resultBean.getCar_11_icon_file_id());
                    map12.put(arr.get(11), resultBean.getCar_12_icon_file_id());
                    map13.put(arr.get(12), resultBean.getCar_13_icon_file_id());
                    map14.put(arr.get(13), resultBean.getCar_14_icon_file_id());
                    map15.put(arr.get(14), resultBean.getCar_15_icon_file_id());
                    map16.put(arr.get(15), resultBean.getCar_16_icon_file_id());
                    map17.put(arr.get(16), resultBean.getCar_17_icon_file_id());
                    map18.put(arr.get(17), resultBean.getCar_18_icon_file_id());

                    arrnei.add(map7);
                    arrnei.add(map8);
                    arrnei.add(map9);
                    arrnei.add(map10);
                    arrnei.add(map11);
                    arrnei.add(map12);
                    arrnei.add(map13);
                    arrnei.add(map14);
                    arrnei.add(map15);
                    arrnei.add(map16);
                    arrnei.add(map17);
                    arrnei.add(map18);
                    gridNei.setAdapter(new Grid_Updata(arrnei, UpdateCarActivity.this, 6, photoList));


                    HashMap<String, String> map19 = new HashMap<>();
                    HashMap<String, String> map20 = new HashMap<>();
                    HashMap<String, String> map21 = new HashMap<>();

                    map19.put(arr.get(18), resultBean.getCar_19_icon_file_id());
                    map20.put(arr.get(19), resultBean.getCar_20_icon_file_id());
                    map21.put(arr.get(20), resultBean.getCar_21_icon_file_id());

                    arrFadong.add(map19);
                    arrFadong.add(map20);
                    arrFadong.add(map21);
                    gridFadong.setAdapter(new Grid_Updata(arrFadong, UpdateCarActivity.this, 18, photoList));


                    HashMap<String, String> map22 = new HashMap<>();
                    HashMap<String, String> map23 = new HashMap<>();
                    HashMap<String, String> map24 = new HashMap<>();

                    map22.put(arr.get(21), resultBean.getCar_22_icon_file_id());
                    map23.put(arr.get(22), resultBean.getCar_23_icon_file_id());
                    map24.put(arr.get(23), resultBean.getCar_24_icon_file_id());
                    arrDetail.add(map22);
                    arrDetail.add(map23);
                    arrDetail.add(map24);
                    gridMore.setAdapter(new Grid_Updata(arrDetail, UpdateCarActivity.this, 21, photoList));
                    break;
            }
            closeDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        car_id = getIntent().getStringExtra("car_id");
        Log.d(TAG, "onCreate车编号: " + car_id);
        dialog = MyProgressDialog.createDialog(this);
        dialog.setMessage("正在加载车辆数据，请稍候！");
        dialog.show();
        photoList.clear();
        arrwai.clear();
        arrnei.clear();
        arrFadong.clear();
        arrDetail.clear();
        sfile_id.clear();
        file_id.clear();
        getData();
        onCall();
        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        photoList.clear();
//        arrwai.clear();
//        arrnei.clear();
//        arrFadong.clear();
//        arrDetail.clear();
//        getData();
//        onCall();
//        getLocation();
        getPictures();
    }

    /* 拍照后显示拍照图片 */
    private void getPictures() {
        Map<String, String> map = new HashMap<>();
        map.put("car_id", car_id);
        OKhttptils.post(UpdateCarActivity.this, Config.SHOW_CAR_PRODUCT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Gson gson = GsonFactory.create();
                CarProductDetail productDetail = gson.fromJson(response, CarProductDetail.class);
                CarProductDetail.DataBean.ResultBean resultBean = productDetail.getData().getResult();
                Message message = new Message();
                message.what = 002;
                message.obj = resultBean;
                handler.sendMessage(message);
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    /* 获取数据 */
    private void getDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("car_id", car_id);
        OKhttptils.post(UpdateCarActivity.this, Config.SHOW_CAR_PRODUCT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.e(TAG, "success查看数据: " + response);
                Gson gson = GsonFactory.create();
                CarProductDetail productDetail = gson.fromJson(response, CarProductDetail.class);
                CarProductDetail.DataBean.ResultBean resultBean = productDetail.getData().getResult();
                car_vin = resultBean.getCar_vin();
                mCarNumb.setText(car_vin);

                car_brand = resultBean.getCar_brand();
                mBrand.setText(resultBean.getCar_brand_cn());

                car_model = resultBean.getCar_model();
                mCarType.setText(resultBean.getCar_model_cn());

                car_mileage = String.valueOf(resultBean.getCar_mileage());
                mKm.setText(car_mileage);

                car_price = String.valueOf(resultBean.getCar_price());
                mPrice.setText(car_price);

                car_time = resultBean.getCar_sign_date();
                mTime.setText(car_time);

                car_resum = resultBean.getCar_desc();
                carResume.setText(car_resum);

                /* ==== 变速箱等 */
                car_gearbox = resultBean.getCar_gearbox();//变速箱
                for (int i = 0; i < carBsxIds.size(); i++) {
                    if (carBsxIds.get(i).equals(car_gearbox)) {
                        bsxAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carBsxList, i);
                        bsxPosition = i;
                        Log.d(TAG, "success变速箱: " + resultBean.getCar_gearbox() + i);
                    }
                }
                bsxAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carBsxList, bsxPosition);
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
                        car_gearbox = tv.getText().toString().trim();
                        bsxPosition = position;


                    }
                });
                gvBsx.setAdapter(bsxAdapter);

                car_letout = resultBean.getCar_letout();//排放标准
                for (int i = 0; i < carPfbzIds.size(); i++) {
                    if (car_letout.equals(carPfbzIds.get(i))) {
                        pfbzAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carPfbzList, i);
                        pfbzPosition = i;
                        Log.d(TAG, "success排放标准: " + resultBean.getCar_letout() + i);
                    }
                }
                pfbzAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carPfbzList, pfbzPosition);
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
                        car_letout = tv.getText().toString().trim();
                        pfbzPosition = position;
                    }
                });
                gvPfbz.setAdapter(pfbzAdapter);

                car_displacement = String.valueOf(resultBean.getCar_emissions());//排量
                seekbar.setValue(Float.parseFloat(car_displacement));
                Log.d(TAG, "success排量: " + resultBean.getCar_emissions());

                car_seating = resultBean.getCar_seating();//座位数
                for (int i = 0; i < carZwsIds.size(); i++) {
                    if (car_seating.equals(carZwsIds.get(i))) {
                        zwsAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carBsxList, i);
                        zwsPosition = i;
                        Log.i(TAG, "success座位数: " + resultBean.getCar_seating() + i);
                    }
                }
                zwsAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carZwsList, zwsPosition);
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
                        car_seating = tv.getText().toString().trim();
                        zwsPosition = position;
                    }
                });
                gvZws.setAdapter(zwsAdapter);

                car_fuel_type = resultBean.getCar_fuel_type();//燃料类型
                for (int i = 0; i < carRllxIds.size(); i++) {
                    if (car_fuel_type.equals(carRllxIds.get(i))) {
                        rllxAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carRllxList, i);
                        rllxPosition = i;
                        Log.d(TAG, "success燃料类型: " + resultBean.getCar_fuel_type() + i);
                    }

                }
                rllxAdapter = new BSXCarMoreAdapter(UpdateCarActivity.this, carRllxList, rllxPosition);
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
                        car_fuel_type = tv.getText().toString().trim();
                        rllxPosition = position;
                    }
                });
                gvRllx.setAdapter(rllxAdapter);

                Message message = new Message();
                message.what = 002;
                message.obj = resultBean;
                handler.sendMessage(message);

            }

            @Override
            public void fail(String response) {

            }
        });

    }

    /* 添加24张图片名、名id */
    private void addCarName() {
        arr.add("正45度");
        arr.add("正面");
        arr.add("前大灯");
        arr.add("侧面");
        arr.add("后车灯");
        arr.add("背面");
        arr.add("钥匙");
        arr.add("左前车门");
        arr.add("车门操控区");
        arr.add("前座椅");
        arr.add("中控区");
        arr.add("方向盘");
        arr.add("仪表盘");
        arr.add("显示屏");
        arr.add("档位");
        arr.add("内车顶");
        arr.add("后座椅");
        arr.add("后备箱");
        arr.add("发动机舱");
        arr.add("底盘");
        arr.add("车轮");
        arr.add("灯光控制");
        arr.add("雨刷器");
        arr.add("钥匙孔");
        for (int i = 1; i < 25; i++) {
            string_id.add(String.valueOf(i));
        }
    }


    /* 获取数据 -变速箱、排放标准、座位数、燃料类型 */
    private void getData() {
        final Map<String, String> map = new HashMap<>();
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
                Message message = new Message();
                message.what = 001;
                message.obj = dictList;
                handler.sendMessage(message);

            }

            @Override
            public void fail(String response) {
//                Gson gson = GsonFactory.create();
//                CarParams carParams = gson.fromJson(response, CarParams.class);
//                ToastUtil.show(UpdateCarActivity.this, carParams.getMessage());
            }
        });
    }

    /* 获取定位*/
    private void getLocation() {
        Log.d(TAG, "getLocation: " + Prefs.with(getApplicationContext()).read("user_token"));
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(UpdateCarActivity.this, Config.GETCOMP_INFO, map, new OKhttptils.HttpCallBack() {
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
            }
        });
        seekbar.setValue(1f);
        seekbar.setOnRangeChangedListener(new RangeSeekbar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekbar rangeSeekbar, float min, float max, boolean isFromUser) {
                if (isFromUser) {
                    car_displacement = (Math.round(min * 10) / 10) + "";
                    seekbar.setLeftProgress(df.format(min));
                }
            }
        });


    }


    //这个会自动接收值
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void qwe(String b) {
        if (!b.isEmpty()) {
            //车型名+车型id + 品牌名+品牌id
            String[] split = b.split(",");
            car_name = split[2] + " " + split[4] + " " + split[0];

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


    /* 品牌、车型、拍摄车架码 */
    public void onCall() {
        //品牌选择
        mBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inten(ModelsChoose.class);
            }
        });
        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePicker picker = new DatePicker(UpdateCarActivity.this);
                picker.setCanceledOnTouchOutside(true);
                picker.setUseWeight(true);
                picker.setTopPadding(ConvertUtils.toPx(UpdateCarActivity.this, 10));
                picker.setRangeStart(1950, 01, 01);
                picker.setSelectedItem(2010, 1, 1);
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
        ;

        //相机
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateCarActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(UpdateCarActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
        intent.putExtra("name", "2");
        startActivityForResult(intent, 0);
    }


    /* 权限返回 */
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


    /* 结果返回 */
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
                        mCarNumb.setText(vin);

                    }

                    @Override
                    public void fail(String response) {
                        Log.d("bbbbbb", "fail: " + response);
                        Toast.makeText(UpdateCarActivity.this, "获取车架号失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else if (requestCode == 0 && resultCode == 1) {
            //接收品牌数据
            Bundle bundle = data.getBundleExtra("bundle");
            String tv_carbrand = bundle.getString("tv_carbrand");
            String id_carbrand = bundle.getString("id_carbrand");
            car_brand = id_carbrand;
            mBrand.setText(tv_carbrand);
        }


    }


    /* 配置adapter */
    public void initView() {

        bsxAdapter = new BSXCarMoreAdapter(this, carBsxList, 0);
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

        pfbzAdapter = new BSXCarMoreAdapter(this, carPfbzList, 1);
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

        zwsAdapter = new BSXCarMoreAdapter(this, carZwsList, 2);
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

        rllxAdapter = new BSXCarMoreAdapter(this, carRllxList, 0);
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
            OKhttptils.post(this, Config.ACCRETIONCAR, map2, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i("oneror", "添加二手车信息: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject data = jsonObject.getJSONObject("data");
                        String result = data.getString("result");
                        Bundle bundle = new Bundle();
                        bundle.putString("car_id", result);
                        JumpUtil.newInstance().jumpRight(UpdateCarActivity.this, CarPhotoActivity.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void fail(String response) {
                    Toast.makeText(UpdateCarActivity.this, "添加失敗", Toast.LENGTH_SHORT).show();
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
                JumpUtil.newInstance().finishRightTrans(UpdateCarActivity.this);
            }
        });
    }

    @OnClick(R.id.mSave)
    public void onViewClicked() {
        car_mileage = mKm.getText().toString().trim();
        car_price = mPrice.getText().toString().trim();
        car_time = mTime.getText().toString().trim();
        car_resum = carResume.getText().toString().trim();
        car_vin = mCarNumb.getText().toString().trim();

        car_gearbox = carBsxIds.get(bsxPosition);
        car_fuel_type = carRllxIds.get(rllxPosition);
        car_letout = carPfbzIds.get(pfbzPosition);
        car_seating = carZwsIds.get(zwsPosition);
        car_name = mBrand.getText().toString().trim() + mCarType.getText().toString().trim();

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
//                Log.i(TAG, "onViewClicked车辆所在城市: " + car_place_city);
        Log.w(TAG, "onViewClicked燃料类型: " + car_fuel_type);
        Log.d(TAG, "onViewClicked座位数: " + car_seating);
        Log.e(TAG, "onViewClicked车架号: " + car_vin);

        boolean isT = false;
        Map<String, String> map = new HashMap<>();
        map.put("car_vin", car_vin);
        map.put("car_sign_date", car_time);
        map.put("car_seating", car_seating);
        map.put("car_price", car_price);
        map.put("car_name", car_name);
        map.put("car_model", car_model);
        map.put("car_mileage", car_mileage);
        map.put("car_letout", car_letout);
        map.put("car_id", car_id);
        map.put("car_gearbox", car_gearbox);
        map.put("car_fuel_type", car_fuel_type);
        map.put("car_emissions", car_displacement);
        map.put("car_desc", car_resum);
        map.put("car_brand", car_brand);
        if (!car_vin.isEmpty() && !car_time.isEmpty() && !car_seating.isEmpty() && !car_price.isEmpty() && !car_name.isEmpty() &&
                !car_model.isEmpty() && !car_mileage.isEmpty() && !car_letout.isEmpty() && !car_id.isEmpty() && !car_gearbox.isEmpty() && !car_fuel_type.isEmpty()
                && !car_displacement.isEmpty() && !car_resum.isEmpty() && !car_brand.isEmpty() && !car_brand.isEmpty()) {
            Log.d(TAG, "onViewClicked看看有几个: " + sfile_id.size());

            for (int i = 0; i < sfile_id.size(); i++) {
                if (sfile_id.get(i) == null) {
                    isT = false;
                    break;
                } else {
                    isT = true;
                }
            }
            Log.d(TAG, "onViewClicked哒哒哒: " + isT);
            if (isT) {
                dialog = MyProgressDialog.createDialog(this);
                dialog.setMessage("正在上传数据");
                dialog.show();

                OKhttptils.post(UpdateCarActivity.this, Config.UPDATE_CAR_PRODUCT, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.d(TAG, "success修改: " + response);
                        dialog.dismiss();
                        JumpUtil.newInstance().finishRightTrans(UpdateCarActivity.this);
                    }

                    @Override
                    public void fail(String response) {
                        Log.d(TAG, "fail修改: " + response);
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            ToastUtil.show(UpdateCarActivity.this, jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            } else {
                ToastUtil.show(UpdateCarActivity.this, "请完善信息后保存");
            }

        } else {
            ToastUtil.show(UpdateCarActivity.this, "请完善信息后保存");
        }

//        Log.e(TAG, "看看哪些值不对: " + map.toString() );
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDialog();
    }
}
