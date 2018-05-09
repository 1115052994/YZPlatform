package com.xtzhangbinbin.jpq.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.BuyCarBean;
import com.xtzhangbinbin.jpq.entity.CarofCompDetail;
import com.xtzhangbinbin.jpq.entity.CompAppraise;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.CircleImageView;
import com.xtzhangbinbin.jpq.view.indicator.IndicatorAdapter;
import com.xtzhangbinbin.jpq.view.indicator.TrackIndicatorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
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
import io.reactivex.functions.Consumer;
@SuppressWarnings("all")
public class CarCompDetail extends BaseActivity {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.comp_name)
    TextView compName;
    @BindView(R.id.star1)
    CheckBox star1;
    @BindView(R.id.star2)
    CheckBox star2;
    @BindView(R.id.star3)
    CheckBox star3;
    @BindView(R.id.star4)
    CheckBox star4;
    @BindView(R.id.star5)
    CheckBox star5;
    @BindView(R.id.loc_tv)
    TextView locTv;
    @BindView(R.id.tabsView)
    TrackIndicatorView tabsView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_onsale)
    TextView tv_onsale;
    @BindView(R.id.tv_sale)
    TextView tv_sale;
    @BindView(R.id.tv_buyNum)
    TextView tv_buyNum;

    private List<String> images = new ArrayList<>();//banner

    //车辆信息 /评价
    private IndicatorAdapter<View> tabAdapter;
    //车辆信息
    private CommonRecyclerAdapter carAdapter;
    private List<BuyCarBean.DataBean.ResultBean> carList = new ArrayList<>();
    //评价
    private CommonRecyclerAdapter appraiseAdapter;
    private List<CompAppraise.DataBean.ResultBean> compAppraiseList = new ArrayList<>();

    private String phone = "";//商家phone
    private String comp_id = "";//商家Id
    private double lat,lon;

    private int pageIndex = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_comp_detail);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        if (intent!=null){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                comp_id = bundle.getString("comp_id");
                Log.i("comp_id===",comp_id);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("商家详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CarCompDetail.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @OnClick({R.id.image_dialog, R.id.image_dh, R.id.tv_appraise})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image_dialog:
                call(phone);
                break;
            case R.id.image_dh:
                ToastUtil.show(this, "正在进入导航请稍等...");
                Bundle bundle = new Bundle();
                bundle.putString("comp_lon", lon + "");
                bundle.putString("comp_lat", lat + "");
                //JumpUtil.newInstance().jumpLeft(this, Map_navigation.class, bundle);
                navigation(lat,lon);
                break;
            case R.id.tv_appraise :
                Bundle loc = new Bundle();
                loc.putString("comp_id", comp_id);
                loc.putString("type", "car");
                JumpUtil.newInstance().jumpRight(this, AppraiseActivity.class, loc);
                break;
        }
    }

    private void initView(){
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
                getPic(CarCompDetail.this, (String) path, imageView);
            }
        });
//        banner.setImages(images);
//        banner.start();

        // 切换tab
        tabAdapter = new IndicatorAdapter<View>() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                View view = LayoutInflater.from(CarCompDetail.this).inflate(R.layout.item_tab, null);
                TextView tv = view.findViewById(R.id.title);
                if (position != 0) {
                    tv.setText("评价");
                } else {
                    tv.setText("车辆信息");
                }
                return view;
            }

            @Override
            public void highLightIndicator(View view, int position) {
                super.highLightIndicator(view, position);
                TextView tv = view.findViewById(R.id.title);
                tv.setTextColor(Color.parseColor("#ff7979"));
                LinearLayout bg = view.findViewById(R.id.bg);
                bg.setVisibility(View.VISIBLE);
                if (position == 0){
                    //车辆信息
                    recyclerView.setAdapter(carAdapter);
                }else{
                    //评价
                    recyclerView.setAdapter(appraiseAdapter);
                }
            }

            @Override
            public void restoreIndicator(View view) {
                super.restoreIndicator(view);
                LinearLayout bg = view.findViewById(R.id.bg);
                bg.setVisibility(View.INVISIBLE);
                TextView tv = view.findViewById(R.id.title);
                tv.setTextColor(Color.parseColor("#333333"));
            }
        };
        tabsView.setAdapter(tabAdapter);

        //评价
        appraiseAdapter = new CommonRecyclerAdapter(this, compAppraiseList, R.layout.item_compappraise) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                CircleImageView circleImageView = holder.getView(R.id.circle_Name);
                if(compAppraiseList.get(position).getPers_head_file_id()!=null) {
                    //getPic(CompDetail.this, compAppraiseList.get(position).getPers_head_file_id(), circleImageView);
                    OKhttptils.getPic(CarCompDetail.this,compAppraiseList.get(position).getPers_head_file_id(), circleImageView);
                }
                TextView phone = holder.getView(R.id.tv_phone);
                phone.setText(compAppraiseList.get(position).getPers_phone());
                TextView time = holder.getView(R.id.tv_appraiseTime);
                time.setText(compAppraiseList.get(position).getLog_date());
                TextView content = holder.getView(R.id.tv_appraise);
                content.setText(compAppraiseList.get(position).getLog_4());
                int level = (int) Double.parseDouble(compAppraiseList.get(position).getLog_2());
                ImageView star1 = holder.getView(R.id.star1);
                ImageView star2 = holder.getView(R.id.star2);
                ImageView star3 = holder.getView(R.id.star3);
                ImageView star4 = holder.getView(R.id.star4);
                ImageView star5 = holder.getView(R.id.star5);
                switch (level) {
                    case 5:
                        star5.setImageResource(R.drawable.pj_pinkstar);
                    case 4:
                        star4.setImageResource(R.drawable.pj_pinkstar);
                    case 3:
                        star3.setImageResource(R.drawable.pj_pinkstar);
                    case 2:
                        star2.setImageResource(R.drawable.pj_pinkstar);
                    case 1:
                        star1.setImageResource(R.drawable.pj_pinkstar);
                        break;
                }
            }
        };

        // 二手车列表
        carAdapter = new CommonRecyclerAdapter(this, carList, R.layout.item_car_buy) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                TextView tvCarsDes = holder.getView(R.id.tv_cardes);
                tvCarsDes.setText(carList.get(position).getCar_name());
                TextView tvPrice = holder.getView(R.id.tv_price);
                DecimalFormat df = new DecimalFormat("#.00");
                double price = carList.get(position).getCar_price()/10000;
                String strPrice = df.format(price);
                tvPrice.setText("￥"+price+"万");
                TextView tvYearAndMiles = holder.getView(R.id.tv_year_miles);
                tvYearAndMiles.setText(carList.get(position).getCar_sign_date().split("-")[0]+"年/"+carList.get(position).getCar_mileage()+"公里");
                ImageView carImage = holder.getView(R.id.image_car);
                if(carList.get(position).getCar_1_icon_file_id()!=null) {
                    OKhttptils.getPic(CarCompDetail.this,carList.get(position).getCar_1_icon_file_id(),carImage);
                }
            }
        };
        // 设置点击事件
        carAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                JumpUtil.newInstance().jumpLeft(CarCompDetail.this,CarDetailsActivity.class,carList.get(position).getCar_id()+"");
            }
        });
        // recyclerView设置manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(carAdapter);

//        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                getCarListLoadMore(refreshlayout);
//            }
//        });
    }

    public void getData() {
        //请求商家信息
        getCompDetail();
        // 请求评价信息
        getAppraise();
        // 请求二手车信息
        getCar();
    }

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
                            CarCompDetail.this.startActivity(intent); // 激活Activity组件
                        } else {
                            // 未获取权限
                            Toast.makeText(CarCompDetail.this, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if(Build.VERSION.SDK_INT >= 9){
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                            } else if(Build.VERSION.SDK_INT <= 8){
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                                intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                            }
                            startActivity(intent);
                        }
                    }
                });
    }

    //请求评价信息
    private void getAppraise() {
        pageIndex = 1;
        Map<String, String> map = new HashMap<>();
        map.put("pageSize","100");
        map.put("pageIndex",pageIndex+"");
        map.put("type","cs");//(cs 车商，fws 服务商)
        map.put("comp_id",comp_id);
        OKhttptils.post(this, Config.QUERYCOMPEVALUATE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("appraise===",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        try {
                            String data = object.getString("data");
                            Gson gson = new Gson();
                            CompAppraise compAppraise = gson.fromJson(response, CompAppraise.class);
                            compAppraiseList.clear();
                            for (CompAppraise.DataBean.ResultBean bean : compAppraise.getData().getResult()) {
                                compAppraiseList.add(bean);
                            }
                            appraiseAdapter.notifyDataSetChanged();
//                                recyclerView.setAdapter(appraiseAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    // 获得商家信息
    private void getCompDetail(){
        Map<String, String> map = new HashMap<>();
        map.put("pageSize","100");
        map.put("pageIndex","1");
        map.put("comp_id",comp_id);
        OKhttptils.post(this, Config.FINDCOMPBYCOMPID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("response=",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        Gson gson = new Gson();
                        CarofCompDetail carofCompDetail = gson.fromJson(response, CarofCompDetail.class);
                        CarofCompDetail.DataBean.ResultBean resultBean = carofCompDetail.getData().getResult();
                        compName.setText(resultBean.getAuth_comp_name());
                        tv_onsale.setText("在售车"+resultBean.getOn_sale_count()+"");
                        tv_sale.setText("已售车"+resultBean.getSale_count()+"");
                        tv_buyNum.setText("浏览人数"+resultBean.getComp_visit_count()+"");
                        locTv.setText(resultBean.getAuth_comp_addr());
                        lat = resultBean.getComp_lat();
                        lon = resultBean.getComp_lon();
                        phone = resultBean.getAuth_comp_tel();
                        images.clear();
                        images.add(resultBean.getAuth_comp_img_head_file_id());
                        banner.setImages(images);
                        banner.start();
                        switch (resultBean.getComp_eval_level()) {
                            case 5:
                                star5.setChecked(true);
                            case 4:
                                star4.setChecked(true);
                            case 3:
                                star3.setChecked(true);
                            case 2:
                                star2.setChecked(true);
                            case 1:
                                star1.setChecked(true);
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    private void getCar(){
        Map<String, String> map = new HashMap<>();
        map.put("pageSize","200");
        map.put("pageIndex","1");
        map.put("comp_id",comp_id);
        OKhttptils.post(this, Config.FINDCOMPCARBYCOMPID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("response=",response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)) {
                        Gson gson = new Gson();
                        BuyCarBean carBean = gson.fromJson(response,BuyCarBean.class);
                        carList.clear();
                        List<BuyCarBean.DataBean.ResultBean> list = carBean.getData().getResult();
                        //if (list.size() == 0){
                            //ToastUtil.show(getActivity(),"未查询到相关车系");
                        //}
                        for (BuyCarBean.DataBean.ResultBean bean : list
                                ) {
                            carList.add(bean);
                        }
                        carAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
