package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.CarDetaile;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.DateUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.JudgeNestedScrollView;
import com.xtzhangbinbin.jpq.view.OnlineOrderDialog;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

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
import io.reactivex.functions.Consumer;

/* 二手车 - 车辆详情 */
public class CarDetailsActivity extends BaseActivity {

    private static final String TAG = "车辆详情";
    @BindView(R.id.mBanner)
    Banner mBanner;
    @BindView(R.id.mCarName)
    TextView mCarName;
    @BindView(R.id.mPrice)
    TextView mPrice;
    @BindView(R.id.mGPS)
    TextView mGPS;
    @BindView(R.id.mAd)
    ImageView mAd;
    @BindView(R.id.mName)
    TextView mName;
    @BindView(R.id.mGrade)
    RatingBar mGrade;
    @BindView(R.id.mOnSale)
    TextView mOnSale;
    @BindView(R.id.mSold)
    TextView mSold;
    @BindView(R.id.mSee)
    TextView mSee;
    @BindView(R.id.mDetails)
    LinearLayout mDetails;
    @BindView(R.id.mKm)
    TextView mKm;
    @BindView(R.id.mLong)
    TextView mLong;
    @BindView(R.id.mWhen)
    TextView mWhen;
    @BindView(R.id.mStandard)
    TextView mStandard;
    @BindView(R.id.mTime)
    TextView mTime;
    @BindView(R.id.mCc)
    TextView mCc;
    @BindView(R.id.mSpeed)
    TextView mSpeed;
    @BindView(R.id.mIntroduce)
    TextView mIntroduce;
    @BindView(R.id.mRecycler)
    RecyclerView mRecycler;
    @BindView(R.id.mScroll)
    JudgeNestedScrollView mScroll;
    @BindView(R.id.mRefresh)
    SmartRefreshLayout mRefresh;
    @BindView(R.id.mOrder)
    RelativeLayout mOrder;
    @BindView(R.id.mEvaluate)
    RelativeLayout mEvaluate;
    @BindView(R.id.coll_Icon)
    ImageView collIcon;
    @BindView(R.id.mCollect)
    LinearLayout mCollect;
    @BindView(R.id.mSale)
    ImageView mSale;
    //    @BindView(R.id.ll_banner)
//    LinearLayout llBanner;
    @BindView(R.id.mLoan)
    ImageView mLoan;
    @BindView(R.id.mDeploy)
    LinearLayout mDeploy;
    @BindView(R.id.recyclerView_foot_more)
    ClassicsFooter recyclerViewFootMore;
    @BindView(R.id.tv_coll)
    TextView tvColl;
    @BindView(R.id.mKefu)
    LinearLayout mKefu;
    @BindView(R.id.tab_bottom)
    RelativeLayout tabBottom;
    private Activity currtActivity = this;
    private String car_id;//二手车id
    private List<String> images = new ArrayList<>();
    private double comp_lon;
    private double comp_lat;
    private String comp_id;

    private boolean isSale = false;

    private String carName;
    private String price;
    private String order_time;

    private int page = 0;

    private CarDetaile.DataBean.ResultBean.CarDetailBean carDetailBean;//各种信息
    private List<CarDetaile.DataBean.ResultBean.CarDetailBean.TopImgListBean> topImgListBeans = new ArrayList<>();//顶部广告图
    private List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> carImgInfoListBeans = new ArrayList<>();//底部24张图

    private CarDetailePicAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 001:
                    carDetailBean = (CarDetaile.DataBean.ResultBean.CarDetailBean) msg.obj;
                    initData();
                    break;
                case 002:
                    topImgListBeans = (List<CarDetaile.DataBean.ResultBean.CarDetailBean.TopImgListBean>) msg.obj;
                    initBanner();
                    break;
                case 003:
                    carImgInfoListBeans = (List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean>) msg.obj;
                    initAdapter();
                    break;
                case 004:
                    page = (int) msg.obj;
                    if (page > 4) {
                    } else {
                        Log.i(TAG, "handleMessage页数: " + page);
                        for (int i = page * 8; i < page * 8 + 8; i++) {
                            if (i < carImgInfoListBeans.size()) {
                                adapter.insert(carImgInfoListBeans.get(i), adapter.getItemCount());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        ButterKnife.bind(this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT/3*2);
//        llBanner.setLayoutParams(params);
        //禁止刷新
        mRefresh.setEnableRefresh(false);
        car_id = "5630";
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("String");
            if (id != null && !"".equals(id)) {
                car_id = id;
            }
        }
        getData();
        getAd();
        getCollect();
        getSale();
        mScroll.setNeedScroll(true);
//        mRecycler.setNestedScrollingEnabled(false);
    }

    /* 订阅降价通知 */
    private void getSale() {

        mSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                map.put("car_id", car_id);
                OKhttptils.post(currtActivity, Config.CAR_DETAIL_IS_SALE, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.e(TAG, "success: " + response);
                        /**
                         * {"data":{"result":""},"message":"","status":"1"}
                         */
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("data");
                            JSONObject object = new JSONObject(data);
                            final String result = object.getString("result");
                            Log.i(TAG, "success: " + result);
                            if (!result.isEmpty()) {
                                ToastUtil.show(currtActivity, "您已订阅过该车降价通知");
                            } else {
                                //未订阅过
                                final OrdinaryDialog dialog = OrdinaryDialog.newInstance(currtActivity).setMessage1("订阅降价通知").setMessage2("确定订阅该车的降价通知吗？").showDialog();
                                dialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                                    @Override
                                    public void onNoClick() {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                                    @Override
                                    public void onYesClick() {
                                        dialog.dismiss();
                                        Map<String, String> map = new HashMap<>();
                                        map.put("car_id", car_id);
                                        OKhttptils.post(currtActivity, Config.CAR_DETAIL_ADD_SALE, map, new OKhttptils.HttpCallBack() {
                                            @Override
                                            public void success(String response) {
                                                Log.i(TAG, "success添加订阅: " + response);
                                                /**
                                                 * {"data":{"result":"1"},"message":"","status":"1"}
                                                 */
                                                try {
                                                    JSONObject jsonObject1 = new JSONObject(response);
                                                    String data = jsonObject1.getString("data");
                                                    JSONObject object1 = new JSONObject(data);
                                                    String result = object1.getString("result");
                                                    ToastUtil.show(currtActivity, "订阅成功");

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void fail(String response) {
                                                Log.i(TAG, "fail添加订阅: " + response);
                                                try {
                                                    JSONObject jsonObject1 = new JSONObject(response);
                                                    ToastUtil.show(currtActivity, jsonObject1.getString("message"));

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void fail(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ToastUtil.show(currtActivity, jsonObject.getString("message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /* 查看是否收藏 */
    private void getCollect() {
        Map<String, String> map = new HashMap<>();
        map.put("coll_content_id", car_id);
        OKhttptils.post(currtActivity, Config.CAR_DETAIL_COLLECT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success是否收藏: " + response);
                /**
                 * {"data":{"result":""},"message":"","status":"1"}
                 */
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String result = object.getString("result");
                    if (result.isEmpty()) {
                        //没有收藏过
                        collIcon.setImageResource(R.drawable.qy_huistar0);
                        mCollect.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Map<String, String> map = new HashMap<>();
                                map.put("coll_content_id", car_id);
                                OKhttptils.post(currtActivity, Config.CAR_DETAIL_ADD_COLLECT, map, new OKhttptils.HttpCallBack() {
                                    @Override
                                    public void success(String response) {
                                        Log.d(TAG, "success添加收藏: " + response);
                                        collIcon.setImageResource(R.drawable.qy_yelowstar1);
                                    }

                                    @Override
                                    public void fail(String response) {
                                        Log.d(TAG, "fail添加收藏: " + response);
                                        try {
                                            JSONObject jsonObject1 = new JSONObject(response);
                                            ToastUtil.show(currtActivity, jsonObject1.getString("message"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        //收藏过
                        collIcon.setImageResource(R.drawable.qy_yelowstar1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                Log.d(TAG, "fail手否收藏: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(currtActivity, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* 拨打客服电话 */
    private RxPermissions rxPermission;

    private void call(final String phone) {
        // 申请定位权限
        rxPermission = new RxPermissions(this);
        rxPermission.request(Manifest.permission.CALL_PHONE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) { // 在android 6.0之前会默认返回true
                            // 已经获取权限
                            Intent intent = new Intent(); // 意图对象：动作 + 数据
                            intent.setAction(Intent.ACTION_CALL); // 设置动作
                            Uri data = Uri.parse("tel:" + phone); // 设置数据
                            intent.setData(data);
                            currtActivity.startActivity(intent); // 激活Activity组件
                        } else {
                            // 未获取权限
                            Toast.makeText(currtActivity, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= 9) {
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                            } else if (Build.VERSION.SDK_INT <= 8) {
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                                intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                            }
                            startActivity(intent);
                        }
                    }
                });

    }

    /* 底部24张图 */
    private void initAdapter() {
        final List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> beanList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            beanList.add(carImgInfoListBeans.get(i));
        }
        adapter = new CarDetailePicAdapter(beanList);
        mRecycler.setLayoutManager(new LinearLayoutManager(currtActivity));
        mRecycler.setAdapter(adapter);

        mRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                Message message = new Message();
                message.what = 004;
                message.obj = page;
                handler.sendMessage(message);
                refreshlayout.finishLoadmore(2000);
            }
        });

    }

    /* 获取广告图片 */
    private void getAd() {
        Map<String, String> map = new HashMap<>();
        map.put("", "");
        OKhttptils.post(currtActivity, Config.CAR_DETAIL_AD, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i(TAG, "success广告: " + response);
                /**
                 * {
                 "data": {
                 "result": "541f384a6d85448d8857f2226030b5b1"
                 },
                 "message": "",
                 "status": "1"
                 }
                 * */
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String data = jsonObject.getString("data");
                    JSONObject object = new JSONObject(data);
                    String file_id = object.getString("result");
                    OKhttptils.getPic(currtActivity, file_id, mAd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                Log.i(TAG, "fail广告: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(currtActivity, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* 展示数据 */
    private void initData() {
        String address = carDetailBean.getAuth_comp_addr();
        mGPS.setText(address);
        carName = carDetailBean.getCar_name();
        mCarName.setText(carName);
        double d = carDetailBean.getCar_price();
        DecimalFormat df = new DecimalFormat("#0.00");
        price = String.valueOf(df.format(d / 10000));
        mPrice.setText(price);
        String onSale = String.valueOf(carDetailBean.getOn_sale_count());
        mOnSale.setText(onSale);
        String sold = String.valueOf(carDetailBean.getSale_count());
        mSold.setText(sold);
        String see = String.valueOf(carDetailBean.getComp_visit_count());
        mSee.setText(see);
        String comp_name = carDetailBean.getAuth_comp_name();
        mName.setText(comp_name);
        int grade = carDetailBean.getComp_eval_level();
        mGrade.setRating(grade);
        comp_lat = carDetailBean.getComp_lat();
        comp_lon = carDetailBean.getComp_lon();
        String km = String.valueOf(carDetailBean.getCar_mileage());
        mKm.setText(km + "万公里");
        String sign_date = carDetailBean.getCar_sign_date();//上牌时间
        mWhen.setText(sign_date + "上牌");
        String year = String.valueOf(carDetailBean.getSign_year());
        String month = String.valueOf(carDetailBean.getSign_month());
        mLong.setText(year + "年" + month + "个月");
        String biaozhun = carDetailBean.getLetout();
        mStandard.setText(biaozhun);
        String time = String.valueOf(carDetailBean.getPutaway_day());
        mTime.setText(time + "天前");
        String cc = carDetailBean.getEmissions();
        mCc.setText(cc + "T");
        String speed = carDetailBean.getGearbox();
        mSpeed.setText(speed + "挡");
        //text设置首尾双引号
        String s = "11" + carDetailBean.getCar_desc() + "11";
        SpannableString spannableString = new SpannableString(s);
        Drawable drawable1 = getResources().getDrawable(R.drawable.b_a_e);
        Drawable drawable2 = getResources().getDrawable(R.drawable.b_a_f);
        drawable1.setBounds(0, 0, 18, 18);
        drawable2.setBounds(0, 0, 18, 18);
        ImageSpan imageSpan1 = new ImageSpan(drawable1);
        ImageSpan imageSpan2 = new ImageSpan(drawable2);
        spannableString.setSpan(imageSpan1, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(imageSpan2, s.length() - 2, s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mIntroduce.setText(spannableString);
    }

    /* 获取数据 */
    private void getData() {
        final Map<String, String> map = new HashMap<>();
        map.put("car_id", car_id);
        OKhttptils.post(currtActivity, Config.CAR_DETAIL, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i(TAG, "success: " + response);

                Gson gson = GsonFactory.create();
                CarDetaile carDetaile = gson.fromJson(response, CarDetaile.class);
                CarDetaile.DataBean.ResultBean.CarDetailBean carDetailBean = carDetaile.getData().getResult().getCarDetail();
                Message message = new Message();
                message.what = 001;
                message.obj = carDetailBean;
                handler.sendMessage(message);

                List<CarDetaile.DataBean.ResultBean.CarDetailBean.TopImgListBean> topImgListBeans = carDetaile.getData().getResult().getCarDetail().getTopImgList();
                Message message1 = new Message();
                message1.what = 002;
                message1.obj = topImgListBeans;
                handler.sendMessage(message1);

                List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> carImgInfoListBeans = carDetaile.getData().getResult().getCarDetail().getCarImgInfoList();
                Log.d(TAG, "success个数: " + carImgInfoListBeans.size());
                Message message2 = new Message();
                message2.what = 003;
                message2.obj = carImgInfoListBeans;
                handler.sendMessage(message2);
            }

            @Override
            public void fail(String response) {
                Log.i(TAG, "fail: " + response);
                if (response == null){
                    ToastUtil.noNAR(currtActivity);
                }
            }
        });
    }

    /* 设置banner */
    private void initBanner() {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (double)(mBanner.getWidth()/3*2));
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (width / 3 * 2));
        Log.i(TAG, "initBanner高度: " + mBanner.getWidth() / 3 * 2);
        Log.i(TAG, "initBanner高度: " + LinearLayout.LayoutParams.MATCH_PARENT);

        mBanner.setLayoutParams(params);
        mBanner.setIndicatorGravity(BannerConfig.LEFT);
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                getPic(currtActivity, (String) path, imageView);
            }
        });
        Log.e(TAG, "initBanner广告图: " + topImgListBeans.size());
        for (int i = 0; i < topImgListBeans.size(); i++) {
            String file_id = topImgListBeans.get(i).getIconImg();
            images.add(file_id);
        }
        mBanner.setImages(images);
        mBanner.start();
    }

    @OnClick({R.id.mLoan, R.id.mGPS, R.id.mDetails, R.id.mDeploy, R.id.mKefu, R.id.mTop,R.id.mOrder, R.id.mEvaluate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLoan:
                //车辆贷款
                /* 未定 */
                break;
            case R.id.mGPS:
                //导航
                navigation(comp_lat, comp_lon);
                break;
            case R.id.mDetails:
                //公司详情
                JumpUtil.newInstance().jumpRight(currtActivity,CarCompDetail.class);
                break;
            case R.id.mDeploy:
                //查看详细参数配置
                Bundle bundle = new Bundle();
                bundle.putString("car_id",car_id);
                JumpUtil.newInstance().jumpRight(currtActivity,CarDetailsKeyActivity.class,bundle);
                break;
            case R.id.mKefu:
                //拨打客服电话
                call("4001198698");
                break;
            case R.id.mTop:
                //回到顶部
                mScroll.scrollTo(0, 0);
                break;
            case R.id.mOrder:
                //在线预约
                showDialog();
                break;
            case R.id.mEvaluate:
                //估价
                break;
        }

    }

    /* 弹出预约dialog */
    private void showDialog() {
        final OnlineOrderDialog dialog = OnlineOrderDialog.newInstance(this).showDialog();
        dialog.setTimePickerListener(new OnlineOrderDialog.onTimePickerListener() {
            @Override
            public void onTimePiker() {
                DatePicker picker = new DatePicker(currtActivity);
                picker.setRangeStart(DateUtil.getNextYear(),DateUtil.getNextMonth(),DateUtil.getNextDay());
                picker.setRangeEnd(2050,12,31);
                picker.setDividerColor(getResources().getColor(R.color.theme_coloer));
                picker.setTextColor(getResources().getColor(R.color.theme_coloer));
                picker.setCancelTextColor(getResources().getColor(R.color.theme_coloer));
                picker.setSubmitTextColor(getResources().getColor(R.color.theme_coloer));
                picker.setTopLineColor(getResources().getColor(R.color.theme_coloer));
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        order_time = year + "-" + month + "-" + day;
                        dialog.setTime(order_time);
                    }
                });
                picker.show();
            }
        });


        dialog.setYesOnclickListener(new OnlineOrderDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (!dialog.getPhone().isEmpty() && StringUtil.isPhoneNum(dialog.getPhone())){
                    if (!dialog.getTime().isEmpty()){
                        Map<String,String> map = new HashMap<>();
                        map.put("car_id",car_id);
                        map.put("online_phone",dialog.getPhone());
                        map.put("online_date",order_time + " 00:00:00");
                        OKhttptils.post(currtActivity, Config.CAR_DETAIL_ONLINE_ORDER, map, new OKhttptils.HttpCallBack() {
                            @Override
                            public void success(String response) {
                                Log.i(TAG, "success在线预约: " + response);
                                /**
                                 * {"data":{},"message":"","status":"1"}
                                 */
                                dialog.dismiss();
                                ToastUtil.show(currtActivity,"预约成功");
                            }

                            @Override
                            public void fail(String response) {
                                Log.d(TAG, "fail在线预约: " + response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    ToastUtil.show(currtActivity,jsonObject.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else {
                        ToastUtil.show(currtActivity,"请选择看车时间");
                    }
                }else {
                    ToastUtil.show(currtActivity,"请填写正确的手机号");
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        setTitle("车辆详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(currtActivity);
            }
        });
    }


    class CarDetailePicAdapter extends RecyclerView.Adapter<CarDetailePicAdapter.ViewHolder> {
        private List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> carImgInfoListBeans;
        private CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean carImgInfoListBean;

        public CarDetailePicAdapter(List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> carImgInfoListBeans) {
            this.carImgInfoListBeans = carImgInfoListBeans;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(currtActivity).inflate(R.layout.item_car_detail_pic, null);
            ViewHolder viewHolder = new ViewHolder(view, true);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            carImgInfoListBean = carImgInfoListBeans.get(position);
            //缩略图id
            String file_id = carImgInfoListBean.getIconImg();
            OKhttptils.getPic(currtActivity, file_id, holder.mPic);
            String type = carImgInfoListBean.getImgName();
            holder.mType.setText(type);

            String content = carImgInfoListBean.getDetail();
            holder.mContent.setText(content);

            //大图id
            final String id = carImgInfoListBean.getImg();
            holder.mDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putString("car_id", car_id);
                    bundle.putString("car_name", carName);
                    bundle.putString("car_price", price);
                    /* 跳转到大图界面 传下标和大图id */
                    JumpUtil.newInstance().jumpRight(currtActivity, CarPhotosActivity.class, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return carImgInfoListBeans.size() == 0 ? 0 : carImgInfoListBeans.size();
        }

        public void insert(CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean resultBean, int position) {
            carImgInfoListBeans.add(position, resultBean);
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public View rootView;
            public ImageView mPic;
            public TextView mType;
            public TextView mContent;
            public RelativeLayout mDetail;

            public ViewHolder(View rootView, boolean isItem) {
                super(rootView);
                if (isItem) {
                    this.rootView = rootView;
                    this.mPic = (ImageView) rootView.findViewById(R.id.mPic);
                    this.mType = (TextView) rootView.findViewById(R.id.mType);
                    this.mContent = (TextView) rootView.findViewById(R.id.mContent);
                    this.mDetail = (RelativeLayout) rootView.findViewById(R.id.mDetail);
                }
            }

        }
    }
}
