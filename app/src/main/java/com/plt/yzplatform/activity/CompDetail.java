package com.plt.yzplatform.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CompAppraise;
import com.plt.yzplatform.entity.CompDetailBean;
import com.plt.yzplatform.entity.WeixiuBean;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.CircleImageView;
import com.plt.yzplatform.view.indicator.IndicatorAdapter;
import com.plt.yzplatform.view.indicator.TrackIndicatorView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class CompDetail extends BaseActivity {

    @BindView(R.id.staffsView)
    TrackIndicatorView staffsView;
    @BindView(R.id.tabsView)
    TrackIndicatorView tabsView;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
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
    @BindView(R.id.num_tv)
    TextView numTv;
    @BindView(R.id.loc_tv)
    TextView locTv;
    @BindView(R.id.image_dh)
    ImageView imageDh;
    @BindView(R.id.phone)
    RelativeLayout phone;
    @BindView(R.id.evaluate)
    RelativeLayout evaluate;
    @BindView(R.id.collect)
    LinearLayout collect;
    @BindView(R.id.kefu)
    LinearLayout kefu;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.corrdinLayout)
    CoordinatorLayout corrdinLayout;
    @BindView(R.id.recyclerView_foot_more)
    ClassicsFooter recyclerViewFootMore;
    private ViewGroup viewGroup;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    private Map<String, String> map = new HashMap<>();
    private IndicatorAdapter<View> staffAdapter;//明星员工
    private List<CompDetailBean.DataBean.ResultBean.StaffListBean> staffList = new ArrayList<>();//staffs
    private List<String> images = new ArrayList<>();//banner

    private IndicatorAdapter<View> tabAdapter;//服务项目导航栏Adapter
    //维修保养 钣金喷漆 电瓶  洗车 美容
    private List<CompDetailBean.DataBean.ResultBean.ServerTypeListBean> serverList = new ArrayList<>();

    //维修
    private CommonRecyclerAdapter wxAdapter;
    private List<WeixiuBean.DataBean.ResultBean> weixiuList = new ArrayList<>();
    //维修中孩子RecyclerView
    private RecyclerView recyclerViewWXChild;
    private CommonRecyclerAdapter wxChildAdapter;
    //喷漆
    private CommonRecyclerAdapter pqAdapter;
    private List<WeixiuBean.DataBean.ResultBean> pqList = new ArrayList<>();
    //喷漆中孩子RecyclerView
    private RecyclerView recyclerViewPQChild;
    private CommonRecyclerAdapter pqChildAdapter;
    //电瓶
    private CommonRecyclerAdapter dpAdapter;
    private List<WeixiuBean.DataBean.ResultBean> dpList = new ArrayList<>();
    //孩子RecyclerView
    private RecyclerView recyclerViewDPChild;
    private CommonRecyclerAdapter dpChildAdapter;
    //洗车
    private CommonRecyclerAdapter xcAdapter;
    private List<WeixiuBean.DataBean.ResultBean> xcList = new ArrayList<>();
    //孩子RecyclerView
    private RecyclerView recyclerViewXCChild;
    private CommonRecyclerAdapter xcChildAdapter;
    //美容
    private CommonRecyclerAdapter mrAdapter;
    private List<WeixiuBean.DataBean.ResultBean> mrList = new ArrayList<>();
    //孩子RecyclerView
    private RecyclerView recyclerViewMRChild;
    private CommonRecyclerAdapter mrChildAdapter;

    //评价
    private List<CompAppraise.DataBean.ResultBean> compAppraiseList = new ArrayList<>();
    private CommonRecyclerAdapter appraiseAdapter;

    private String comp_id = "25";//商家Id
    private String comp_phone = "";
    private Double comp_lon,comp_lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        getData();
        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            if(bundle!=null) {
                comp_id = bundle.getString("comp_id");
                map.put("comp_id",comp_id);
                OKhttptils.post(CompDetail.this, Config.BROWSECOMP, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        Log.i("aaaaa", "添加企业浏览记录: " + response);
                    }

                    @Override
                    public void fail(String response) {
                        Toast.makeText(CompDetail.this, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.i("com_id", "comp_id=" + comp_id);
            }
        }

        //设置不加载更多
//        smartRefreshLayout.setEnableLoadmore(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("商家详情");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(CompDetail.this);
            }
        });
    }

    private void initView() {
        viewGroup = findViewById(R.id.head_title);
        staffAdapter = new IndicatorAdapter<View>() {
            @Override
            public int getCount() {
                return staffList.size();
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                View view = LayoutInflater.from(CompDetail.this).inflate(R.layout.item_staff, null);
                TextView name = view.findViewById(R.id.name_tv);
                name.setText(staffList.get(position).getStaff_name());
                TextView des = view.findViewById(R.id.staffDes_tv);
                des.setText(staffList.get(position).getStaff_info());
                CircleImageView imageView = view.findViewById(R.id.circleImage);
                getPic(CompDetail.this, staffList.get(position).getStaff_photo_file_id(), imageView);
                return view;
            }
        };
//        staffsView.setAdapter(staffAdapter);

        tabAdapter = new IndicatorAdapter<View>() {
            @Override
            public int getCount() {
                return serverList.size() + 1;
            }

            @Override
            public View getView(int position, ViewGroup parent) {
                View view = LayoutInflater.from(CompDetail.this).inflate(R.layout.item_tab, null);
                TextView tv = view.findViewById(R.id.title);
                if (position == serverList.size()) {
                    tv.setText("评价");
                } else {
                    tv.setText(serverList.get(position).getServerDesc());
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
                Log.i("highLightIndicator", "position=" + position);
                // 切换Adapter
                if (position == serverList.size()) {
                    recyclerView.setAdapter(appraiseAdapter);
                } else {
                    if (serverList.get(position).getServerDesc().contains("维修")) {
                        recyclerView.setAdapter(wxAdapter);
                    } else if (serverList.get(position).getServerDesc().contains("喷漆")) {
                        recyclerView.setAdapter(pqAdapter);
                    }else if (serverList.get(position).getServerDesc().contains("电瓶")) {
                        recyclerView.setAdapter(dpAdapter);
                    }else if (serverList.get(position).getServerDesc().contains("洗车")) {
                        recyclerView.setAdapter(xcAdapter);
                    }else if (serverList.get(position).getServerDesc().contains("美容")) {
                        recyclerView.setAdapter(mrAdapter);
                    }
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
//        tabsView.setAdapter(tabAdapter);

        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
                Log.i("banner", "path=" + (String) path);
//                Picasso.with(CompDetail.this).load("http://pic.58pic.com/58pic/14/54/14/09E58PICUpb_1024.jpg").into(imageView);
                getPic(CompDetail.this, (String) path, imageView);
            }
        });

    }

    private void initData() {
        // 维修
        wxAdapter = new CommonRecyclerAdapter(this, weixiuList, R.layout.item_wx) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                WeixiuBean.DataBean.ResultBean weixiuBean = weixiuList.get(position);
                TextView wxName = holder.getView(R.id.byName_tv);
                wxName.setText(weixiuBean.getDict_desc());
                TextView wxMoney = holder.getView(R.id.money_tv);
                wxMoney.setText("￥" + (int) weixiuBean.getProd_reduced_price());
                TextView wxTotalMoney = holder.getView(R.id.tolPrice_tv);
                wxTotalMoney.setText("总价￥" + (int) weixiuBean.getProd_price());
                TextView wxyhlMoney = holder.getView(R.id.yhPrice_tv);
                wxyhlMoney.setText("优惠价￥" + (int) weixiuBean.getProd_reduced_price());
                recyclerViewWXChild = holder.getView(R.id.recyclerView_wx_child);
                // 处理滑动冲突
//                recyclerViewWXChild.setFocusableInTouchMode(false); //设置不需要焦点
//                recyclerViewWXChild.requestFocus(); //设置焦点不需要
                recyclerViewWXChild.setNestedScrollingEnabled(false);//不滑动
                recyclerViewWXChild.setLayoutManager(new LinearLayoutManager(CompDetail.this));
                final List<WeixiuBean.DataBean.ResultBean.ProItemListBean> itemList = weixiuBean.getProItemList();
                wxChildAdapter = new CommonRecyclerAdapter(CompDetail.this, itemList, R.layout.item_wx_childs) {
                    @Override
                    public void convert(ViewHolder holder, Object item, int position) {
                        TextView wxChildName = holder.getView(R.id.wxName1_tv);
                        wxChildName.setText(itemList.get(position).getItem_name());
                        TextView childPrice = holder.getView(R.id.price1_tv);
                        childPrice.setText((int) itemList.get(position).getItem_price() + "");
                    }
                };
                recyclerViewWXChild.setAdapter(wxChildAdapter);
            }
        };
        //喷漆
        pqAdapter = new CommonRecyclerAdapter(this, pqList, R.layout.item_wx) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                WeixiuBean.DataBean.ResultBean weixiuBean = pqList.get(position);
                TextView wxName = holder.getView(R.id.byName_tv);
                wxName.setText(weixiuBean.getDict_desc());
                TextView wxMoney = holder.getView(R.id.money_tv);
                wxMoney.setText("￥" + (int) weixiuBean.getProd_reduced_price());
                TextView wxTotalMoney = holder.getView(R.id.tolPrice_tv);
                wxTotalMoney.setText("总价￥" + (int) weixiuBean.getProd_price());
                TextView wxyhlMoney = holder.getView(R.id.yhPrice_tv);
                wxyhlMoney.setText("优惠价￥" + (int) weixiuBean.getProd_reduced_price());
                recyclerViewPQChild = holder.getView(R.id.recyclerView_wx_child);
                // 处理滑动冲突
//                recyclerViewWXChild.setFocusableInTouchMode(false); //设置不需要焦点
//                recyclerViewWXChild.requestFocus(); //设置焦点不需要
                recyclerViewPQChild.setNestedScrollingEnabled(false);//不滑动
                recyclerViewPQChild.setLayoutManager(new LinearLayoutManager(CompDetail.this));
                final List<WeixiuBean.DataBean.ResultBean.ProItemListBean> itemList = weixiuBean.getProItemList();
                pqChildAdapter = new CommonRecyclerAdapter(CompDetail.this, itemList, R.layout.item_wx_childs) {
                    @Override
                    public void convert(ViewHolder holder, Object item, int position) {
                        TextView wxChildName = holder.getView(R.id.wxName1_tv);
                        wxChildName.setText(itemList.get(position).getItem_name());
                        TextView childPrice = holder.getView(R.id.price1_tv);
                        childPrice.setText((int) itemList.get(position).getItem_price() + "");
                    }
                };
                recyclerViewPQChild.setAdapter(pqChildAdapter);
            }
        };
        //电瓶
        dpAdapter = new CommonRecyclerAdapter(this, dpList, R.layout.item_wx) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                WeixiuBean.DataBean.ResultBean weixiuBean = dpList.get(position);
                TextView wxName = holder.getView(R.id.byName_tv);
                wxName.setText(weixiuBean.getDict_desc());
                TextView wxMoney = holder.getView(R.id.money_tv);
                wxMoney.setText("￥" + (int) weixiuBean.getProd_reduced_price());
                TextView wxTotalMoney = holder.getView(R.id.tolPrice_tv);
                wxTotalMoney.setText("总价￥" + (int) weixiuBean.getProd_price());
                TextView wxyhlMoney = holder.getView(R.id.yhPrice_tv);
                wxyhlMoney.setText("优惠价￥" + (int) weixiuBean.getProd_reduced_price());
                recyclerViewDPChild = holder.getView(R.id.recyclerView_wx_child);
                // 处理滑动冲突
//                recyclerViewWXChild.setFocusableInTouchMode(false); //设置不需要焦点
//                recyclerViewWXChild.requestFocus(); //设置焦点不需要
                recyclerViewDPChild.setNestedScrollingEnabled(false);//不滑动
                recyclerViewDPChild.setLayoutManager(new LinearLayoutManager(CompDetail.this));
                final List<WeixiuBean.DataBean.ResultBean.ProItemListBean> itemList = weixiuBean.getProItemList();
                dpChildAdapter = new CommonRecyclerAdapter(CompDetail.this, itemList, R.layout.item_wx_childs) {
                    @Override
                    public void convert(ViewHolder holder, Object item, int position) {
                        TextView wxChildName = holder.getView(R.id.wxName1_tv);
                        wxChildName.setText(itemList.get(position).getItem_name());
                        TextView childPrice = holder.getView(R.id.price1_tv);
                        childPrice.setText((int) itemList.get(position).getItem_price() + "");
                    }
                };
                recyclerViewDPChild.setAdapter(dpChildAdapter);
            }
        };
        //洗车
        xcAdapter = new CommonRecyclerAdapter(this, xcList, R.layout.item_wx) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                WeixiuBean.DataBean.ResultBean weixiuBean = xcList.get(position);
                TextView wxName = holder.getView(R.id.byName_tv);
                wxName.setText(weixiuBean.getDict_desc());
                TextView wxMoney = holder.getView(R.id.money_tv);
                wxMoney.setText("￥" + (int) weixiuBean.getProd_reduced_price());
                TextView wxTotalMoney = holder.getView(R.id.tolPrice_tv);
                wxTotalMoney.setText("总价￥" + (int) weixiuBean.getProd_price());
                TextView wxyhlMoney = holder.getView(R.id.yhPrice_tv);
                wxyhlMoney.setText("优惠价￥" + (int) weixiuBean.getProd_reduced_price());
                recyclerViewXCChild = holder.getView(R.id.recyclerView_wx_child);
                // 处理滑动冲突
//                recyclerViewWXChild.setFocusableInTouchMode(false); //设置不需要焦点
//                recyclerViewWXChild.requestFocus(); //设置焦点不需要
                recyclerViewXCChild.setNestedScrollingEnabled(false);//不滑动
                recyclerViewXCChild.setLayoutManager(new LinearLayoutManager(CompDetail.this));
                final List<WeixiuBean.DataBean.ResultBean.ProItemListBean> itemList = weixiuBean.getProItemList();
                xcChildAdapter = new CommonRecyclerAdapter(CompDetail.this, itemList, R.layout.item_wx_childs) {
                    @Override
                    public void convert(ViewHolder holder, Object item, int position) {
                        TextView wxChildName = holder.getView(R.id.wxName1_tv);
                        wxChildName.setText(itemList.get(position).getItem_name());
                        TextView childPrice = holder.getView(R.id.price1_tv);
                        childPrice.setText((int) itemList.get(position).getItem_price() + "");
                    }
                };
                recyclerViewXCChild.setAdapter(xcChildAdapter);
            }
        };
        //美容
        mrAdapter = new CommonRecyclerAdapter(this, mrList, R.layout.item_wx) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                WeixiuBean.DataBean.ResultBean weixiuBean = mrList.get(position);
                TextView wxName = holder.getView(R.id.byName_tv);
                wxName.setText(weixiuBean.getDict_desc());
                TextView wxMoney = holder.getView(R.id.money_tv);
                wxMoney.setText("￥" + (int) weixiuBean.getProd_reduced_price());
                TextView wxTotalMoney = holder.getView(R.id.tolPrice_tv);
                wxTotalMoney.setText("总价￥" + (int) weixiuBean.getProd_price());
                TextView wxyhlMoney = holder.getView(R.id.yhPrice_tv);
                wxyhlMoney.setText("优惠价￥" + (int) weixiuBean.getProd_reduced_price());
                recyclerViewMRChild = holder.getView(R.id.recyclerView_wx_child);
                // 处理滑动冲突
//                recyclerViewWXChild.setFocusableInTouchMode(false); //设置不需要焦点
//                recyclerViewWXChild.requestFocus(); //设置焦点不需要
                recyclerViewMRChild.setNestedScrollingEnabled(false);//不滑动
                recyclerViewMRChild.setLayoutManager(new LinearLayoutManager(CompDetail.this));
                final List<WeixiuBean.DataBean.ResultBean.ProItemListBean> itemList = weixiuBean.getProItemList();
                mrChildAdapter = new CommonRecyclerAdapter(CompDetail.this, itemList, R.layout.item_wx_childs) {
                    @Override
                    public void convert(ViewHolder holder, Object item, int position) {
                        TextView wxChildName = holder.getView(R.id.wxName1_tv);
                        wxChildName.setText(itemList.get(position).getItem_name());
                        TextView childPrice = holder.getView(R.id.price1_tv);
                        childPrice.setText((int) itemList.get(position).getItem_price() + "");
                    }
                };
                recyclerViewMRChild.setAdapter(mrChildAdapter);
            }
        };

        //评价
        appraiseAdapter = new CommonRecyclerAdapter(this, weixiuList, R.layout.item_compappraise) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                CircleImageView circleImageView = holder.getView(R.id.circle_Name);
                getPic(CompDetail.this, compAppraiseList.get(position).getPers_head_file_id(), circleImageView);
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
                Log.i("level", "level=" + level);
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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void getData() {
        if (NetUtil.isNetAvailable(getApplicationContext())) {
            OkHttpUtils.post()
                    .url(Config.GETCOMPDETAIL)
                    .addHeader("user_token", Prefs.with(this).read("user_token"))
                    .addParams("comp_id", comp_id)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(CompDetail.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.w("onResponse", "onResponse:" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("1")) {
                                    Gson gson = new Gson();
                                    CompDetailBean detailBean = gson.fromJson(response, CompDetailBean.class);
                                    CompDetailBean.DataBean.ResultBean resultBean = detailBean.getData().getResult();
                                    //获取banner
                                    List<CompDetailBean.DataBean.ResultBean.BannerListBean> bannerListBeans = resultBean.getBannerList();
                                    for (CompDetailBean.DataBean.ResultBean.BannerListBean bannerBean : bannerListBeans) {
                                        images.add(bannerBean.getAdve_file_id());
                                    }
                                    banner.setImages(images);
                                    banner.start();
                                    //公司名字星级地址
                                    CompDetailBean.DataBean.ResultBean.InfoBean infoBean = resultBean.getInfo();
                                    compName.setText(infoBean.getAuth_comp_name());
                                    locTv.setText(infoBean.getAuth_comp_addr());
                                    numTv.setText(infoBean.getComp_order_count() + "人");
                                    comp_phone = infoBean.getPhone_number();
                                    comp_lon = infoBean.getAuth_comp_lon();
                                    comp_lat = infoBean.getAuth_comp_lat();
                                    switch (infoBean.getComp_eval_level()) {
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
                                    // 明星员工
                                    staffList.clear();
                                    for (CompDetailBean.DataBean.ResultBean.StaffListBean staffListBean : resultBean.getStaffList()) {
                                        staffList.add(staffListBean);
                                    }
                                    staffsView.setmTabVisibleNums(1.5f);
                                    staffsView.setAdapter(staffAdapter);
                                    //tabs服务项目
                                    serverList.clear();
                                    List<CompDetailBean.DataBean.ResultBean.ServerTypeListBean> serverTypeListBean = resultBean.getServerTypeList();
                                    for (CompDetailBean.DataBean.ResultBean.ServerTypeListBean serverBean : serverTypeListBean) {
                                        serverList.add(serverBean);
                                    }
                                    tabsView.setmTabVisibleNums(3.0f);
                                    tabsView.setAdapter(tabAdapter);
                                    //服务项目（维修、洗车、评价等）
                                    getServerItem();
                                } else {
                                    ToastUtil.noNAR(CompDetail.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(CompDetail.this);
        }
    }

    // 服务项目（维修、洗车等）
    private void getServerItem() {
        for (int i = 0; i < serverList.size(); i++) {
            Log.i("serverItemId",serverList.get(i).getServerDesc()+"----"+serverList.get(i).getServerId());
            getServerItem(serverList.get(i).getServerDesc(), serverList.get(i).getServerId(), i);
        }
        // 商家评价
        getAppraise();
    }

    //请求服务项目
    private void getServerItem(final String item, String serverId, final int index) {
        if (NetUtil.isNetAvailable(CompDetail.this)) {
            OkHttpUtils.post()
                    .url(Config.GETPRODUCTITEMBYSERVERTYPE)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("pageSize", "100")
                    .addParams("pageIndex", "1")
                    .addParams("comp_id", comp_id)
                    .addParams("serverId", serverId)
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("onResponseWX", "onResponse" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String data = object.getString("data");
                                Gson gson = new Gson();
                                if (item.contains("维修")) {
                                    WeixiuBean weixiuBean = gson.fromJson(response, WeixiuBean.class);
                                    List<WeixiuBean.DataBean.ResultBean> weixiu = weixiuBean.getData().getResult();
                                    for (WeixiuBean.DataBean.ResultBean bean : weixiu) {
                                        weixiuList.add(bean);
                                    }
                                    wxAdapter.notifyDataSetChanged();
                                    if (index == 0) {
                                        recyclerView.setAdapter(wxAdapter);
                                    }
                                } else if (item.contains("喷漆")) {
                                    WeixiuBean weixiuBean = gson.fromJson(response, WeixiuBean.class);
                                    List<WeixiuBean.DataBean.ResultBean> weixiu = weixiuBean.getData().getResult();
                                    for (WeixiuBean.DataBean.ResultBean bean : weixiu) {
                                        pqList.add(bean);
                                    }
                                    pqAdapter.notifyDataSetChanged();
                                    if (index == 0) {
                                        recyclerView.setAdapter(pqAdapter);
                                    }
                                }else if (item.contains("美容")) {
                                    WeixiuBean weixiuBean = gson.fromJson(response, WeixiuBean.class);
                                    List<WeixiuBean.DataBean.ResultBean> weixiu = weixiuBean.getData().getResult();
                                    for (WeixiuBean.DataBean.ResultBean bean : weixiu) {
                                        mrList.add(bean);
                                    }
                                    mrAdapter.notifyDataSetChanged();
                                    if (index == 0) {
                                        recyclerView.setAdapter(mrAdapter);
                                    }
                                }else if (item.contains("电瓶")) {
                                    WeixiuBean weixiuBean = gson.fromJson(response, WeixiuBean.class);
                                    List<WeixiuBean.DataBean.ResultBean> weixiu = weixiuBean.getData().getResult();
                                    for (WeixiuBean.DataBean.ResultBean bean : weixiu) {
                                        dpList.add(bean);
                                    }
                                    dpAdapter.notifyDataSetChanged();
                                    if (index == 0) {
                                        recyclerView.setAdapter(dpAdapter);
                                    }
                                }else if (item.contains("洗车")) {
                                    WeixiuBean weixiuBean = gson.fromJson(response, WeixiuBean.class);
                                    List<WeixiuBean.DataBean.ResultBean> weixiu = weixiuBean.getData().getResult();
                                    for (WeixiuBean.DataBean.ResultBean bean : weixiu) {
                                        xcList.add(bean);
                                    }
                                    xcAdapter.notifyDataSetChanged();
                                    if (index == 0) {
                                        recyclerView.setAdapter(xcAdapter);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(CompDetail.this);
        }
    }

    //请求评价信息
    private void getAppraise() {
        if (NetUtil.isNetAvailable(CompDetail.this)) {
            OkHttpUtils.post()
                    .url(Config.QUERYCOMPEVALUATE)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("pageSize", "100")
                    .addParams("pageIndex", "1")
                    .addParams("comp_id", comp_id)
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d("onResponseAppraise", "onResponse" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String data = object.getString("data");
                                Gson gson = new Gson();
                                CompAppraise compAppraise = gson.fromJson(response, CompAppraise.class);
                                compAppraiseList.clear();
                                for (CompAppraise.DataBean.ResultBean bean : compAppraise.getData().getResult()) {
                                    compAppraiseList.add(bean);
                                }
                                appraiseAdapter.notifyDataSetChanged();
//                                recyclerView.setAdapter(wxAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(CompDetail.this);
        }
    }

    @OnClick({R.id.image_dh, R.id.phone, R.id.evaluate, R.id.collect, R.id.kefu})
    public void onViewClicked(View view) {
//        final int[] position = new int[2];
//        tabsView.getLocationInWindow(position);
//        final int headHeight = viewGroup.getHeight();
//        final int statusHeight = ScreenUtils.getStatusHeight(CompDetail.this);
//        Log.i("tabViews", "getLocationInWindow:" + position[0] + "," + position[1] + ",headHeight=" + headHeight);
//        offset = -position[1] + headHeight + statusHeight;
        switch (view.getId()) {
            case R.id.image_dh:
                ToastUtil.show(this,"正在进入导航请稍等...");
                Intent intent = new Intent();
                JumpUtil.newInstance().jumpLeft(this,Map_navigation.class);
                break;
            case R.id.phone:
                call(comp_phone);
                ToastUtil.show(this,"打电话");
//                corrdinLayout.animate().translationY(-position[1] + headHeight + statusHeight).setDuration(200).start();
                break;
            case R.id.evaluate:
                Bundle bundle =new Bundle();
                bundle.putString("comp_id",comp_id);
                JumpUtil.newInstance().jumpRight(this,AppraiseActivity.class,bundle);
//                ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(2,0);
//                appBar.animate().translationY(offset).setDuration(200).start();
//                recyclerView.animate().translationY(offset).setDuration(200).start();
                break;
            case R.id.collect:
                break;
            case R.id.kefu:
                call("4001198698");
                break;
        }
    }

    private void call(String phone){
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this,
                    Manifest.permission.CALL_PHONE)) {
                // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                // 弹窗需要解释为何需要该权限，再次请求授权
                Toast.makeText(this, "请授权！", Toast.LENGTH_LONG).show();
                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                this.startActivity(intent);
            }else{
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions((Activity) this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }else {
            // 已经获得授权，可以打电话
            if (TextUtils.isEmpty(phone)) {
                // 提醒用户
                // 注意：在这个匿名内部类中如果用this则表示是View.OnClickListener类的对象，
                // 所以必须用MainActivity.this来指定上下文环境。
                Toast.makeText(this, "号码不能为空！", Toast.LENGTH_SHORT).show();
            } else {
                // 拨号：激活系统的拨号组件
                Intent intent = new Intent(); // 意图对象：动作 + 数据
                intent.setAction(Intent.ACTION_CALL); // 设置动作
                Uri data = Uri.parse("tel:" + phone); // 设置数据
                intent.setData(data);
                this.startActivity(intent); // 激活Activity组件
            }
        }
    }
}
