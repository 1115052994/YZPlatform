package com.plt.yzplatform.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarDetaile;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
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
    NestedScrollView mScroll;
    @BindView(R.id.mRefresh)
    SmartRefreshLayout mRefresh;
    @BindView(R.id.mOrder)
    RelativeLayout mOrder;
    @BindView(R.id.mEvaluate)
    RelativeLayout mEvaluate;
    @BindView(R.id.coll_Icon)
    ImageView collIcon;
    private Activity currtActivity = this;
    private String car_id;//二手车id
    private List<String> images = new ArrayList<>();
    private String comp_lon;
    private String comp_lat;
    private String comp_id;

    private String carName;
    private String price;

    private int page = 1;

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
                    Log.i(TAG, "handleMessage页数: " + page);
                    if (page > 5){

                    }else {
                        for (int i = page * 4; i < page * 4 + 4; i++) {
                            if (i<carImgInfoListBeans.size()) {
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
        //禁止刷新
        mRefresh.setEnableRefresh(false);
        car_id = "5630";
        getData();
        getAd();
        initView();
    }

    /* 底部24张图 */
    private void initAdapter() {
        final List<CarDetaile.DataBean.ResultBean.CarDetailBean.CarImgInfoListBean> beanList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
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
        comp_lat = String.valueOf(carDetailBean.getComp_lat());
        comp_lon = String.valueOf(carDetailBean.getComp_lon());
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
                /**
                 * {
                 "data": {
                 "result": {
                 "carDetail": {
                 "topImgList": [
                 {
                 "imgName": "正45度",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "1",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "正面",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "2",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "侧面",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "4",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "背面",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "6",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "中控区",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "11",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 }
                 ],
                 "car_emissions": "YZcarscreenpl1.0",
                 "car_letout": "YZcarscreenpfbzsan",
                 "car_comp_id": "24",
                 "comp_visit_count": 714,
                 "car_id": 5,
                 "carImgInfoList": [
                 {
                 "imgName": "正45度",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "1",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "正面",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "2",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "前大灯",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "3",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "侧面",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "4",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "后车灯",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "5",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "背面",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "6",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "钥匙",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "7",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "左前车门",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "8",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "车门操控区",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "9",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "前座椅",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "10",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "中控区",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "11",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "方向盘",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "12",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "仪表盘",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "13",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "显示屏",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "14",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "档位",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "15",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "内车顶",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "16",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "后座椅",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "17",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "后备箱",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "18",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "发动机舱",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "19",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "底盘",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "20",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "车轮",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "21",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "灯光控制",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "22",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 },
                 {
                 "imgName": "雨刷器",
                 "img": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "detail": "23",
                 "IconImg": "a9e59108aefc44e6957c0a49a1a2ebda"
                 }
                 ],
                 "car_24_file_id": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "car_vin": "",
                 "car_audit_state": "2",
                 "letout": "国三及以上",
                 "car_name": "这是APP测试数据，别再给我动了！！！！ 速腾 \t\r\n2017款 1.6L 手动舒适型",
                 "car_brand": "YZescxgescppcxdlxlcnkcxmy_58",
                 "gearbox": "自动",
                 "car_desc": "舒服",
                 "car_oper_date": "2018-04-23 09:30:24",
                 "car_model": "YZescxgescppcxdlxlcnkcxmy_58_11_2118",
                 "car_deal_state": "ysj",
                 "car_24_desc": "24",
                 "car_place_city": "jinan",
                 "auth_comp_addr": "缎库胡同15号",
                 "car_type": "YZcarscreencarstypelxjc",
                 "sale_count": 0,
                 "car_gearbox": "YZcarscreenbsxzd",
                 "auth_comp_name": "山东派乐特网络科技有限公司",
                 "car_comp_user_id": "24",
                 "car_sign_date": "2017-02-13",
                 "car_fuel_type": "YZcarscreenoilqy",
                 "car_visit_count": 12,
                 "car_price": 120,
                 "emissions": "1.0",
                 "on_sale_count": 6,
                 "car_seating": "YZcarscreenseatfour",
                 "car_24_icon_file_id": "a9e59108aefc44e6957c0a49a1a2ebda",
                 "car_mileage": 6
                 }
                 }
                 },
                 "message": "",
                 "status": "1"
                 }
                 * */

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
                Log.d(TAG, "success个数: " +carImgInfoListBeans.size());
                Message message2 = new Message();
                message2.what = 003;
                message2.obj = carImgInfoListBeans;
                handler.sendMessage(message2);
            }

            @Override
            public void fail(String response) {
                Log.i(TAG, "fail: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(currtActivity, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* 设置banner */
    private void initBanner() {
        mBanner.setIndicatorGravity(BannerConfig.LEFT);
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                getPic(currtActivity, (String) path, imageView);
            }
        });
        Log.e(TAG, "initBanner广告图: " +topImgListBeans.size() );
        for (int i = 0; i < topImgListBeans.size(); i++) {
            String file_id = topImgListBeans.get(i).getIconImg();
            images.add(file_id);
        }
        mBanner.setImages(images);
        mBanner.start();
    }

    private void initView() {
//        //text设置首尾双引号
//        String s = "11这是一段话话话话话话阿萨德离开静安寺的离开家啊山东iu群文件饿哦IQ物流金额卡萨丁见风使舵风口浪尖阿尔偶皮阿尔欧安尔碘；范加德里克；放假11";
//        SpannableString spannableString = new SpannableString(s);
//        Drawable drawable1 = getResources().getDrawable(R.drawable.b_a_e);
//        Drawable drawable2 = getResources().getDrawable(R.drawable.b_a_f);
//        drawable1.setBounds(0, 0, 18, 18);
//        drawable2.setBounds(0, 0, 18, 18);
//        ImageSpan imageSpan1 = new ImageSpan(drawable1);
//        ImageSpan imageSpan2 = new ImageSpan(drawable2);
//        spannableString.setSpan(imageSpan1, 0, 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(imageSpan2, s.length() - 2, s.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        mIntroduce.setText(spannableString);
    }

    @OnClick({R.id.mLoan, R.id.mSale, R.id.mGPS, R.id.mDetails, R.id.mDeploy, R.id.mCollect, R.id.mKefu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLoan:
                //车辆贷款
                break;
            case R.id.mSale:
                //订阅降价信息
                break;
            case R.id.mGPS:
                //导航
                break;
            case R.id.mDetails:
                //公司详情
                break;
            case R.id.mDeploy:
                //查看详细参数配置
                break;
            case R.id.mCollect:
                break;
            case R.id.mKefu:
                break;
        }
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
                    bundle.putString("file_id", id);
                    /* 跳转到大图界面 传下标和大图id */
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
            public LinearLayout mDetail;

            public ViewHolder(View rootView, boolean isItem) {
                super(rootView);
                if (isItem) {
                    this.rootView = rootView;
                    this.mPic = (ImageView) rootView.findViewById(R.id.mPic);
                    this.mType = (TextView) rootView.findViewById(R.id.mType);
                    this.mContent = (TextView) rootView.findViewById(R.id.mContent);
                    this.mDetail = (LinearLayout) rootView.findViewById(R.id.mDetail);
                }
            }

        }
    }
}
