package com.xtzhangbinbin.jpq.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.AppraiseInterface;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.activity.AdvanceSX;
import com.xtzhangbinbin.jpq.activity.CarBrandSearch;
import com.xtzhangbinbin.jpq.activity.CarDetailsActivity;
import com.xtzhangbinbin.jpq.adapter.AppraiseGVAdapter;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.BuyCarBean;
import com.xtzhangbinbin.jpq.entity.CarParams;
import com.xtzhangbinbin.jpq.utils.CommonUtils;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.CarTagLayout;
import com.xtzhangbinbin.jpq.view.ExpandableGridView;
import com.xtzhangbinbin.jpq.view.SeekBarPressure;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
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
import butterknife.Unbinder;

public class BuyCar extends Fragment {

    @BindView(R.id.ly_zn)
    RelativeLayout lyZn;
    @BindView(R.id.ly_pp)
    RelativeLayout lyPp;
    @BindView(R.id.ly_jg)
    RelativeLayout lyJg;
    @BindView(R.id.ly_sx)
    RelativeLayout lySx;
    @BindView(R.id.ly_cz)
    RelativeLayout lyCz;
    Unbinder unbinder;
    @BindView(R.id.tv_zn)
    TextView tvZn;
    @BindView(R.id.image_zn)
    ImageView imageZn;
    @BindView(R.id.tv_pp)
    TextView tvPp;
    @BindView(R.id.image_pp)
    ImageView imagePp;
    @BindView(R.id.tv_jg)
    TextView tvJg;
    @BindView(R.id.image_jg)
    ImageView imageJg;
    @BindView(R.id.tv_sx)
    TextView tvSx;
    @BindView(R.id.image_sx)
    ImageView imageSx;
    @BindView(R.id.tv_cz)
    TextView tvCz;
    @BindView(R.id.image_cz)
    ImageView imageCz;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recyclerView_car)
    RecyclerView recyclerViewCar;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.cartag_layout)
    CarTagLayout cartagLayout;//流布局
    @BindView(R.id.car_ly)
    RelativeLayout carLy;//流布局(根布局)
    @BindView(R.id.tv_dy)
    TextView tvDy;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.corrdinLayout)
    CoordinatorLayout corrdinLayout;
    @BindView(R.id.recyclerView_foot_more)
    ClassicsFooter recyclerViewFootMore;

    // 轮播图
    private List<String> bannersImage = new ArrayList<>();

    private List<BuyCarBean.DataBean.ResultBean> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;

    // 城市
    private String cityName = "济南";
    private String cityId = "";

    //高级筛选
    //车型cx  变速箱bsx   排放标准pfbz   燃油类型rylx   座位数(zwsparamName,paramId）
    private String sxCx = "", sxCxName = "";//不限(全是Id)
    private String sxBsx = "", sxBsxName = "";
    private String sxPfbz = "", sxPfbzName = "";
    private String sxRylx = "", sxRylxName = "";
    private String sxZws = "", sxZwsName = "";
    // 车龄cl 里程lc 排量pl(star,end)
    private String sxClStart = "", sxClEnd = "";
    private String sxLcStart = "", sxLcEnd = "";
    private String sxPlStart = "", sxPlEnd = "";

    // 价格
    private String startPrice = "", endPrice = "";
    private View selectPrice = null;

    // 品牌车系
    /**
     * carMap1.put("tv_carbrand", "A");
     * carMap1.put("image_carbrand", "");
     * carMap1.put("id_carbrand", "");
     */
    private Map<String, String> selectedBrand = new HashMap<>();
    private Map<String, String> selectedCarName = new HashMap<>();

    // 排序方式（智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage）
    private List<String> rankList = new ArrayList<>();
    private List<String> rankId = new ArrayList<>();
    private String selectedRank = "";
    private String selectedRankId = "";

    // 分页
    private int pageIndex = 1;
    private int pageTotal = 1;

    // 记录tag view
    private TextView brandView, carView, priceView;

    private MyBroadcast smsBroadCastReceiver;

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_car_buy, container, false);
        }
        unbinder = ButterKnife.bind(this, view);
        iniData();
        initView();
        getData();
        // 广播
        smsBroadCastReceiver = new MyBroadcast ();
        //实例化过滤器并设置要过滤的广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("city");
        //注册广播
        getContext().registerReceiver(smsBroadCastReceiver, intentFilter);
        return view;
    }


    private void initView() {
        //隐藏筛选tag布局
        carLy.setVisibility(View.GONE);
        // 订阅按钮
        tvDy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加订阅
                addSubscribe();
            }
        });
        cartagLayout.setOnItemClickListener(new CarTagLayout.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                // 删除筛选标签
                TextView textView = (TextView) view;
                String name = textView.getText().toString();
                Log.i("tagClick", position + "---" + name);
                if (name.equals(sxBsxName)) {
                    sxBsx = "";
                    sxBsxName = "";
                } else if (name.equals(sxPfbzName)) {
                    sxPfbz = "";
                    sxPfbzName = "";
                } else if (name.equals(sxRylxName)) {
                    sxRylx = "";
                    sxRylxName = "";
                } else if (name.equals(sxZwsName)) {
                    sxZws = "";
                    sxZwsName = "";
                } else if (name.equals(sxCxName)) {
                    sxCx = "";
                    sxCxName = "";
                }
                // 车龄cl 里程lc 排量pl(star,end)
                //private String sxClStart = "",sxClEnd="";
                //private String sxLcStart = "",sxLcEnd ="";
                //private String sxPlStart = "",sxPlEnd = "";
                else if (name.contains("年")) {
                    sxClStart = "";
                    sxClEnd = "";
                } else if (name.contains("里程")) {
                    sxLcStart = "";
                    sxLcEnd = "";
                } else if (name.contains("排量")) {
                    sxPlStart = "";
                    sxPlEnd = "";
                }
                // 品牌
                else if (name.equals(selectedBrand.get("tv_carbrand"))) {
                    selectedBrand.clear();
                    selectedBrand.put("tv_carbrand", "");
                    selectedBrand.put("id_carbrand", "");
                    selectedBrand.put("image_carbrand", "");
                    brandView = null;
                    selectedCarName.clear();
                    selectedCarName.put("tv_carbrand", "");
                    selectedCarName.put("id_carbrand", "");
                    selectedCarName.put("image_carbrand", "");
                    carView = null;
                } else if (name.equals(selectedCarName.get("tv_carbrand"))) {
                    selectedCarName.clear();
                    selectedCarName.put("tv_carbrand", "");
                    selectedCarName.put("id_carbrand", "");
                    selectedCarName.put("image_carbrand", "");
                    if (carView != null) {
                        cartagLayout.removeView(carView);
                        carView = null;
                    }
                }
                // 价格
                else if (name.contains(startPrice) || name.contains(endPrice)) {
                    startPrice = "";
                    endPrice = "";
                    priceView = null;
                }
                cartagLayout.removeView(view);
                if (cartagLayout.getChildCount() == 0) {
                    carLy.setVisibility(View.GONE);
                }
                // 重新筛选
                getCarList();
            }
        });

        // banner
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
//                Picasso.with(CompDetail.this).load("http://pic.58pic.com/58pic/14/54/14/09E58PICUpb_1024.jpg").into(imageView);
                OKhttptils.getPic(getActivity(), (String) path, imageView);
            }
        });

        // 二手车列表
        recyclerAdapter = new CommonRecyclerAdapter(getActivity(), recyclerList, R.layout.item_car_buy) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                TextView tvCarsDes = holder.getView(R.id.tv_cardes);
                tvCarsDes.setText(recyclerList.get(position).getCar_name());
                TextView tvPrice = holder.getView(R.id.tv_price);
                DecimalFormat df = new DecimalFormat("#.00");
                double price = recyclerList.get(position).getCar_price() / 10000;
                df.format(price);
                tvPrice.setText("￥" + price + "万");
                TextView tvYearAndMiles = holder.getView(R.id.tv_year_miles);
                tvYearAndMiles.setText(recyclerList.get(position).getCar_sign_date().split("-")[0] + "年/" + recyclerList.get(position).getCar_mileage() + "万公里");
                ImageView carImage = holder.getView(R.id.image_car);
                if (recyclerList.get(position).getCar_1_icon_file_id() != null) {
                    OKhttptils.getPic(getActivity(), recyclerList.get(position).getCar_1_icon_file_id(), carImage);
                }
            }
        };
        recyclerViewCar.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCar.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                JumpUtil.newInstance().jumpLeft(getActivity(), CarDetailsActivity.class, recyclerList.get(position).getCar_id() + "");
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getCarListLoadMore(refreshlayout);
            }
        });
    }

    private void iniData() {
        //智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage
        rankList.clear();
        rankId.clear();
        rankList.add("智能排序");
        rankId.add("intelligence");
        rankList.add("价格最高");
        rankId.add("price_down");
        rankList.add("价格最低");
        rankId.add("price_up");
        rankList.add("车龄最短");
        rankId.add("car_age");
        rankList.add("最新发布");
        rankId.add("sale_time");
        rankList.add("里程最少");
        rankId.add("car_mileage");
        // 价格
        jgList.clear();
        jgList.add("不限价格");
        jgList.add("5万以下");
        jgList.add("5万-10万");
        jgList.add("10万-15万");
        jgList.add("15万-20万");
        jgList.add("20万-30万");
        jgList.add("30万-50万");
        jgList.add("50万以上");

        // 品牌车系
        selectedBrand.put("tv_carbrand", "");
        selectedBrand.put("image_carbrand", "");
        selectedBrand.put("id_carbrand", "");
        selectedCarName.put("tv_carbrand", "");
        selectedCarName.put("image_carbrand", "");
        selectedCarName.put("id_carbrand", "");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ly_zn, R.id.ly_pp, R.id.ly_jg, R.id.ly_sx, R.id.ly_cz})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ly_zn:
                // 智能排序
                tvZn.setTextColor(Color.parseColor("#ff9696"));
                imageZn.setImageResource(R.drawable.b_g);
                rzPopupWindow();
                break;
            case R.id.ly_pp:
                // 品牌
                /**
                 * carMap1.put("tv_carbrand", "A");
                 carMap1.put("image_carbrand", "");
                 carMap1.put("id_carbrand", "");
                 */
                tvPp.setTextColor(Color.parseColor("#ff9696"));
                imagePp.setImageResource(R.drawable.b_g);
                JumpUtil.newInstance().jumpLeft(getActivity(), CarBrandSearch.class);
                CarBrandSearch.setOnDataBackListener(new CarBrandSearch.DataBackListener() {
                    @Override
                    public void backData(Map<String, String> brand, Map<String, String> carName) {
                        tvPp.setTextColor(Color.parseColor("#333333"));
                        imagePp.setImageResource(R.drawable.b_a);
                        if (brand == null) {
                            selectedBrand.put("tv_carbrand", "");
                            selectedBrand.put("id_carbrand", "");
                            selectedCarName.put("tv_carbrand", "");
                            selectedCarName.put("id_carbrand", "");
                        } else {
                            selectedBrand = brand;
                            selectedCarName = carName;
                            Log.i("carbrand", selectedBrand.toString() + "---" + selectedCarName.toString());
                            //----------加入tag布局--------------
                            if (!"".equals(selectedBrand.get("tv_carbrand"))) {
                                if (brandView == null) {
                                    View ly = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                                    brandView = ly.findViewById(R.id.tv_tag);
                                    if (brandView.getParent() != null) {
                                        ViewGroup group = (ViewGroup) brandView.getParent();
                                        group.removeAllViews();
                                    }
                                    cartagLayout.addView(brandView);
                                    carLy.setVisibility(View.VISIBLE);
                                }
                                brandView.setText(selectedBrand.get("tv_carbrand"));
                            }
                            if (!"".equals(selectedCarName.get("tv_carbrand"))) {
                                if (carView == null) {
                                    View ly = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                                    carView = ly.findViewById(R.id.tv_tag);
                                    if (carView.getParent() != null) {
                                        ViewGroup group = (ViewGroup) carView.getParent();
                                        group.removeAllViews();
                                    }
                                    cartagLayout.addView(carView);
                                    carLy.setVisibility(View.VISIBLE);
                                }
                                carView.setText(selectedCarName.get("tv_carbrand"));
                            }

                            // 重新筛选
                            getCarList();
                        }
                    }
                });
                break;
            case R.id.ly_jg:
                // 价格
                tvJg.setTextColor(Color.parseColor("#ff9696"));
                imageJg.setImageResource(R.drawable.b_g);
                jgPopupWindow();
                break;
            case R.id.ly_sx:
                // 筛选
                tvSx.setTextColor(Color.parseColor("#ff9696"));
                imageSx.setImageResource(R.drawable.b_g);
                JumpUtil.newInstance().jumpLeft(getActivity(), AdvanceSX.class, "");
                AdvanceSX.setOnDataBackListener(new AdvanceSX.DataBackListener() {
                    @Override
                    public void backData(Map<String, String> map) {
                        tvSx.setTextColor(Color.parseColor("#333333"));
                        imageSx.setImageResource(R.drawable.b_a);
                        if (map == null) {
                            return;
                        }
                        Log.i("shaixuan", map.toString());
                        // 显示搜索筛选信息
                        //if(cartagLayout.isInLayout()) {
                        cartagLayout.removeAllViews();
                        if (priceView != null)
                            cartagLayout.addView(priceView);
                        if (brandView != null)
                            cartagLayout.addView(brandView);
                        if (carView != null)
                            cartagLayout.addView(carView);
                        //}
                        carLy.setVisibility(View.VISIBLE);
                        //显示搜索选项   重新搜索  返回数据（
                        //车型cx  变速箱bsx   排放标准pfbz   燃油类型rylx   座位数(zws paramName,paramId）
                        // 车龄cl 里程lc 排量pl(star,end)
                        if (!"".equals(map.get("cx"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("cx").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxCxName = map.get("cx").split(",")[0];
                            sxCx = map.get("cx").split(",")[1];
                        }
                        if (!"".equals(map.get("bsx"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("bsx").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxBsxName = map.get("bsx").split(",")[0];
                            sxBsx = map.get("bsx").split(",")[1];
                        }
                        if (!"".equals(map.get("pfbz"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("pfbz").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxBsxName = map.get("bsx").split(",")[0];
                            sxPfbz = map.get("pfbz").split(",")[1];
                        }
                        if (!"".equals(map.get("rylx"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("rylx").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxRylxName = map.get("rylx").split(",")[0];
                            sxRylx = map.get("rylx").split(",")[1];
                        }
                        if (!"".equals(map.get("zws"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("zws").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxZwsName = map.get("zws").split(",")[0];
                            sxZws = map.get("zws").split(",")[1];
                        }
                        // 车龄0-6
                        /*
                        private String sxClStart = "",sxClEnd="";
                        private String sxLcStart = "",sxLcEnd ="";
                        private String sxPlStart = "",sxPlEnd = "";
                         */
                        View view_cl = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                        TextView tv_cl = view_cl.findViewById(R.id.tv_tag);
                        if (tv_cl.getParent() != null) {
                            ViewGroup group = (ViewGroup) tv_cl.getParent();
                            group.removeAllViews();
                        }
                        String cl[] = map.get("cl").split(",");
                        if ("0".equals(cl[0])) {
                            if (!"6".equals(cl[1])) {
                                //<6
                                tv_cl.setText(cl[1] + "年以内");
                                cartagLayout.addView(tv_cl);
                                sxClStart = "";
                                sxClEnd = cl[1];
                            } else {
                                // 车龄不限
                                sxClStart = "";
                                sxClEnd = "";
                            }
                        } else {
                            if ("6".equals(cl[1])) {
                                //>0
                                tv_cl.setText(cl[0] + "年以上");
                                cartagLayout.addView(tv_cl);
                                sxClStart = cl[0];
                                sxClEnd = "";
                            } else {
                                tv_cl.setText(cl[0] + "-" + cl[1] + "年");
                                cartagLayout.addView(tv_cl);
                                sxClStart = cl[0];
                                sxClEnd = cl[1];
                            }
                        }
                        // 里程0-15
                        View view_lc = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                        TextView tv_lc = view_lc.findViewById(R.id.tv_tag);
                        if (tv_lc.getParent() != null) {
                            ViewGroup group = (ViewGroup) tv_lc.getParent();
                            group.removeAllViews();
                        }
                        String lc[] = map.get("lc").split(",");
                        if ("0".equals(lc[0])) {
                            if (!"15".equals(lc[1])) {
                                //<15
                                tv_lc.setText(lc[1] + "万里程以内");
                                cartagLayout.addView(tv_lc);
                                sxLcStart = "";
                                sxLcEnd = lc[1];
                            } else {
                                // 不限
                                sxLcStart = "";
                                sxLcEnd = "";
                            }
                        } else {
                            if ("15".equals(lc[1])) {
                                //>0
                                tv_lc.setText(lc[0] + "万里程以上");
                                cartagLayout.addView(tv_lc);
                                sxLcStart = lc[0];
                                sxLcEnd = "";
                            } else {
                                tv_lc.setText(lc[0] + "-" + lc[1] + "万里程");
                                cartagLayout.addView(tv_lc);
                                sxLcStart = lc[0];
                                sxLcEnd = lc[1];
                            }
                        }
                        // 排量0-5.0
                        View view_pl = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                        TextView tv_pl = view_pl.findViewById(R.id.tv_tag);
                        if (tv_pl.getParent() != null) {
                            ViewGroup group = (ViewGroup) tv_pl.getParent();
                            group.removeAllViews();
                        }
                        String pl[] = map.get("pl").split(",");
                        if ("0.0".equals(pl[0])) {
                            if (!"5.0".equals(pl[1])) {
                                //<5.0
                                tv_pl.setText(pl[1] + "排量以内");
                                cartagLayout.addView(tv_pl);
                                sxPlStart = "";
                                sxPlEnd = pl[1];
                            } else {
                                // 不限
                                sxPlStart = "";
                                sxPlEnd = "";
                            }
                        } else {
                            if ("5.0".equals(pl[1])) {
                                //>0
                                tv_pl.setText(pl[0] + "排量以上");
                                cartagLayout.addView(tv_pl);
                                sxPlStart = pl[0];
                                sxPlEnd = "";
                            } else {
                                tv_pl.setText(pl[0] + "-" + pl[1] + "排量");
                                cartagLayout.addView(tv_pl);
                                sxPlStart = pl[0];
                                sxPlEnd = pl[1];
                            }
                        }
                        // 如果没有筛选条件xianshi
                        Log.i("databack", "count===" + cartagLayout.getChildCount());
                        if (cartagLayout.getChildCount() == 0) {
                            carLy.setVisibility(View.GONE);
                        } else {
                            // 重新筛选
                            getCarList();
                        }
                    }
                });
                break;
            case R.id.ly_cz:
                // 重置请求参数
                resetParams();
                tvCz.setTextColor(Color.parseColor("#ff9696"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvCz.setTextColor(Color.parseColor("#333333"));
                    }
                }, 500);
                // 重新筛选
                getCarList();
                break;
        }
    }


    private PopupWindow popupWindow;

    /* 创建popupwindow */
    private void rzPopupWindow() {
//        if (popupWindow == null) {
        View contentView = getLayoutInflater().inflate(R.layout.popupwindow, null);
        RecyclerView recyclerView = contentView.findViewById(R.id.recyclerView_popup);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommonRecyclerAdapter adapter = new CommonRecyclerAdapter(getContext(), rankList, R.layout.car_mennu) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                TextView tv = holder.getView(R.id.tv_rankName);
                tv.setText((String) item);
            }
        };
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.trans_a)));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams wlp = getActivity().getWindow().getAttributes();
                wlp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlp);
                if (!"".equals(selectedRank))
                    tvZn.setText(selectedRank);
                else
                    tvZn.setText(rankList.get(0));
                tvZn.setTextColor(Color.parseColor("#333333"));
                imageZn.setImageResource(R.drawable.b_a);
                // 获得排序方式（智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage）
                Log.i("rank===", selectedRank + "---" + selectedRankId);
                // 重新筛选
                getCarList();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(line1, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.verticalMargin = CommonUtils.dip2px(getActivity(), 30);
        lp.alpha = 0.9f;
        getActivity().getWindow().setAttributes(lp);
        //设置点击监听
        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                selectedRank = rankList.get(position);
                selectedRankId = rankId.get(position);
                popupWindow.dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
//        }
    }

    private List<String> jgList = new ArrayList<>();
    private AppraiseGVAdapter jgAdapter;

    /* 创建popupwindow */
    private void jgPopupWindow() {
        View contentView = getLayoutInflater().inflate(R.layout.popup_priceselect, null);
        final TextView tvPrice = contentView.findViewById(R.id.tv_zdyprice);
        SeekBarPressure seekBarPressures = contentView.findViewById(R.id.seekBar);
        Button okButton = contentView.findViewById(R.id.OKbutton);
        //显示最大值
        seekBarPressures.setMaxSize(6);
        //设置分几块区域
        seekBarPressures.setMaxCount(7);
        seekBarPressures.isInside(true);
        seekBarPressures.setOnSeekBarChangeListener(new SeekBarPressure.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBarPressure seekBar, double progressLow, double progressHigh) {
                //tvPrice.setText("低：" + (progressLow == 6 ? "上限" : progressLow) + "高：" + (progressHigh == 60 ? "上限" : progressHigh));
                if (progressLow == 0) {
                    if (progressHigh == 6) {
                        tvPrice.setText("不限价格");
                        startPrice = "";
                        endPrice = "";
                    } else {
                        tvPrice.setText((int) progressHigh + "0万以下");
                        endPrice = (int) progressHigh * 10 + "";
                        startPrice = "";
                    }
                } else {
                    if (progressHigh == 6) {
                        tvPrice.setText((int) progressLow * 10 + "0万以上");
                        startPrice = (int) progressLow * 10 + "";
                        endPrice = "";
                    } else {
                        tvPrice.setText((int) progressLow + "0万-" + (int) progressHigh + "0万");
                        startPrice = (int) progressLow * 10 + "";
                        endPrice = (int) progressHigh * 10 + "";
                    }
                }
            }
        });

        // 创建popupwindow
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.trans_a)));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams wlp = getActivity().getWindow().getAttributes();
                wlp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(wlp);
                tvJg.setTextColor(Color.parseColor("#333333"));
                imageJg.setImageResource(R.drawable.b_a);
                // 获得选择的价格，数据重新筛选
                Log.i("price===", startPrice + "---" + endPrice);
                // 加入tag布局
                View ly = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                if (!"".equals(startPrice) || !"".equals(endPrice)) {
                    if (priceView == null) {
                        priceView = ly.findViewById(R.id.tv_tag);
                        if (priceView.getParent() != null) {
                            ViewGroup group = (ViewGroup) priceView.getParent();
                            group.removeAllViews();
                        }
                        cartagLayout.addView(priceView);
                    }
                    if ("".equals(startPrice)) {
                        if (!"".equals(endPrice))
                            priceView.setText(endPrice + "万以下");
                    } else {
                        if ("".equals(endPrice))
                            priceView.setText(startPrice + "万以上");
                        else
                            priceView.setText(startPrice + "-" + endPrice + "万");
                    }
                    carLy.setVisibility(View.VISIBLE);
                }
                // 重新筛选
                getCarList();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(line1, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.verticalMargin = CommonUtils.dip2px(getActivity(), 30);
        lp.alpha = 0.9f;
        getActivity().getWindow().setAttributes(lp);

        // 价格选择
        ExpandableGridView priceGv = contentView.findViewById(R.id.price_gv);
        jgAdapter = new AppraiseGVAdapter(getActivity(), jgList);
        jgAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {
//                jgList.add("不限价格");
//                jgList.add("5万以下");
//                jgList.add("5万-10万");
//                jgList.add("10万-15万");
//                jgList.add("15万-20万");
//                jgList.add("20万-30万");
//                jgList.add("30万-50万");
//                jgList.add("50万以上");
                switch (position) {
                    case 0:
                        // 不限
                        break;
                    case 1:
                        startPrice = "";
                        endPrice = 5 + "";
                        break;
                    case 2:
                        startPrice = 5 + "";
                        endPrice = 10 + "";
                        break;
                    case 3:
                        startPrice = 10 + "";
                        endPrice = 15 + "";
                        break;
                    case 4:
                        startPrice = 15 + "";
                        endPrice = 20 + "";
                        break;
                    case 5:
                        startPrice = 20 + "";
                        endPrice = 30 + "";
                        break;
                    case 6:
                        startPrice = 30 + "";
                        endPrice = 50 + "";
                        break;
                    case 7:
                        startPrice = 50 + "";
                        endPrice = "";
                        break;
                }
                if (selectPrice != null) {
                    selectPrice.setSelected(false);
                    TextView tv = selectPrice.findViewById(R.id.tv_appraise);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
                TextView tv = view.findViewById(R.id.tv_appraise);
//                if(!view.isSelected()) {
                view.setSelected(true);
                tv.setTextColor(Color.parseColor("#ff9696"));
//                }else{
//                    view.setSelected(false);
//                    tv.setTextColor(Color.parseColor("#333333"));
//                }
                selectPrice = view;

                popupWindow.dismiss();
            }
        });

        // 确定按钮监听
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        priceGv.setAdapter(jgAdapter);
    }

    public void getData() {
        //通过城市名获取城市Id
        getCityId();
        // 获得car各种信息字典以及banner
        getCarParams();
    }

    //通过城市名获取城市Id
    public void getCityId() {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(getActivity(), Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                /**
                 * {
                 "data": {
                 "result": {
                 "province_id": "shandong",
                 "city_id": "jinan"
                 }
                 },
                 "message": "",
                 "status": "1"
                 }
                 */
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    cityId = result.getString("city_id");
//                    Log.i("city_Id",cityId);
                    getCarList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {

            }
        });
    }


    // 获得car各种信息字典以及banner
    public void getCarParams() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(getActivity(), Config.GETCARPARAMDICT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("response===", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        CarParams carParams = gson.fromJson(response, CarParams.class);
                        CarParams.DataBean.ResultBean resultBean = carParams.getData().getResult();
                        //banner
                        List<CarParams.DataBean.ResultBean.BannerListBean> bannerListBeans = resultBean.getBannerList();
                        bannersImage.clear();
                        for (CarParams.DataBean.ResultBean.BannerListBean bean : bannerListBeans) {
                            bannersImage.add(bean.getAdve_file_id());
                        }
                        if (banner !=null) {
                            banner.setImages(bannersImage);
                            banner.start();
                        }
                        //carParams
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


    // 搜索Car列表(添加筛选条件)
    public void getCarList() {
        pageIndex = 1;
        String startPrice1 = "", endPrice1 = "";
        String brand = "", carname = "";
        Log.i("getCarList", "index=" + pageIndex);
        Log.i("getCarList", "total=" + pageTotal);
        Log.i("getCarList", "cityId=" + cityId);
        Log.i("getCarList", "sxLcStart=" + sxLcStart);
        Log.i("getCarList", "sxLcEnd=" + sxLcEnd);
        Log.i("getCarList", "sxClStart=" + sxClStart);
        Log.i("getCarList", "sxClEnd=" + sxClEnd);
        Log.i("getCarList", "sxPfbz=" + sxPfbz);
        Log.i("getCarList", "sxCx=" + sxCx);
        Log.i("getCarList", "sxBsx=" + sxBsx);
        Log.i("getCarList", "sxZws=" + sxZws);
        if (!"".equals(startPrice))
            startPrice1 = (int) Double.parseDouble(startPrice) * 10000 + "";
        if (!"".equals(endPrice))
            endPrice1 = (int) Double.parseDouble(endPrice) * 10000 + "";
        carname = selectedCarName.get("id_carbrand") == null ? "" : selectedCarName.get("id_carbrand");
        brand = selectedBrand.get("id_carbrand") == null ? "" : selectedBrand.get("id_carbrand");
        Log.i("getCarList", "startPrice=" + startPrice);
        Log.i("getCarList", "endPrice=" + endPrice);
        Log.i("getCarList", "brand=" + brand);
        Log.i("getCarList", "selectedRankId=" + selectedRankId);
        Log.i("getCarList", "sxRylx=" + sxRylx);
        Log.i("getCarList", "carname=" + carname);
        Log.i("getCarList", "sxPlStart=" + sxPlStart);
        Log.i("getCarList", "sxPlEnd=" + sxPlEnd);

        Map<String, String> map = new HashMap<>();
        map.put("city", cityId);
        map.put("car_mileage_start", sxLcStart);
        map.put("car_mileage_end", sxLcEnd);
        map.put("car_age_start", sxClStart);
        map.put("car_age_end", sxClEnd);
        map.put("car_letout", sxPfbz);
        map.put("car_type", sxCx);//车辆类型（SUV、MPV、皮卡）
        map.put("car_gearbox", sxBsx);
        map.put("car_seating", sxZws);
        map.put("car_price_start", startPrice1);//元为单位
        map.put("car_price_end", endPrice1);//元为单位
        //map.put("car_train", "");//车系大
        map.put("car_train_item", carname);//车系小
        map.put("sort", selectedRankId);//排序方式（智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage）
        map.put("pageIndex", "" + pageIndex++);
        map.put("pageSize", "20");
        map.put("car_name", "");//模糊搜索匹配
        map.put("car_fuel_type", sxRylx);
        map.put("car_brand", brand);
        map.put("car_emissions_start", sxPlStart);
        map.put("car_emissions_end", sxPlEnd);
        map.put("histroy_word", "");
        //car_brand  car_emissions_start  car_emissions_end  histroy_word
        OKhttptils.post(getActivity(), Config.SEARCHCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("BuyCarresponse", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        BuyCarBean buyCarBean = gson.fromJson(response, BuyCarBean.class);
                        //pageIndex = buyCarBean.getData().getPageCount();
                        pageTotal = buyCarBean.getData().getPageTotal();
                        recyclerList.clear();
                        List<BuyCarBean.DataBean.ResultBean> list = buyCarBean.getData().getResult();
                        if (list.size() == 0) {
                            ToastUtil.show(getActivity(), "未查询到相关车系");
                        }
                        for (BuyCarBean.DataBean.ResultBean bean : list
                                ) {
                            recyclerList.add(bean);
                        }
                        recyclerAdapter.notifyDataSetChanged();
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

    // 加载更多
    private void getCarListLoadMore(final RefreshLayout refreshlayout) {
        String startPrice1 = "", endPrice1 = "";
        String brand = "", carname = "";
//        if (pageIndex == pageTotal){
//            ToastUtil.show(getActivity(),"没有更多");
//            refreshlayout.finishLoadmore(200);
//            return;
//        }
        Log.i("getCarList", "index=" + pageIndex);
        Log.i("getCarList", "total=" + pageTotal);
        Log.i("getCarList", "cityId=" + cityId);
        Log.i("getCarList", "sxLcStart=" + sxLcStart);
        Log.i("getCarList", "sxLcEnd=" + sxLcEnd);
        Log.i("getCarList", "sxClStart=" + sxClStart);
        Log.i("getCarList", "sxClEnd=" + sxClEnd);
        Log.i("getCarList", "sxPfbz=" + sxPfbz);
        Log.i("getCarList", "sxCx=" + sxCx);
        Log.i("getCarList", "sxBsx=" + sxBsx);
        Log.i("getCarList", "sxZws=" + sxZws);
        if (!"".equals(startPrice))
            startPrice1 = (int) Double.parseDouble(startPrice) * 10000 + "";
        if (!"".equals(endPrice))
            endPrice1 = (int) Double.parseDouble(endPrice) * 10000 + "";
        carname = selectedCarName.get("id_carbrand") == null ? "" : selectedCarName.get("id_carbrand");
        brand = selectedBrand.get("id_carbrand") == null ? "" : selectedBrand.get("id_carbrand");
        Log.i("getCarList", "startPrice=" + startPrice);
        Log.i("getCarList", "endPrice=" + endPrice);
        Log.i("getCarList", "brand=" + brand);
        Log.i("getCarList", "selectedRankId=" + selectedRankId);
        Log.i("getCarList", "sxRylx=" + sxRylx);
        Log.i("getCarList", "carname=" + carname);
        Log.i("getCarList", "sxPlStart=" + sxPlStart);
        Log.i("getCarList", "sxPlEnd=" + sxPlEnd);

        Map<String, String> map = new HashMap<>();
        map.put("city", cityId);
        map.put("car_mileage_start", sxLcStart);
        map.put("car_mileage_end", sxLcEnd);
        map.put("car_age_start", sxClStart);
        map.put("car_age_end", sxClEnd);
        map.put("car_letout", sxPfbz);
        map.put("car_type", sxCx);//车辆类型（SUV、MPV、皮卡）
        map.put("car_gearbox", sxBsx);
        map.put("car_seating", sxZws);
        map.put("car_price_start", startPrice1);//元为单位
        map.put("car_price_end", endPrice1);//元为单位
        //map.put("car_train", "");//车系大
        map.put("car_train_item", carname);//车系小
        map.put("sort", selectedRankId);//排序方式（智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage）
        map.put("pageIndex", "" + pageIndex++);
        map.put("pageSize", "20");
        map.put("car_name", "");//模糊搜索匹配
        map.put("car_fuel_type", sxRylx);
        map.put("car_brand", brand);
        map.put("car_emissions_start", sxPlStart);
        map.put("car_emissions_end", sxPlEnd);
        map.put("histroy_word", "");
        //car_brand  car_emissions_start  car_emissions_end  histroy_word
        OKhttptils.post(getActivity(), Config.SEARCHCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("BuyCarresponse", response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        BuyCarBean buyCarBean = gson.fromJson(response, BuyCarBean.class);
//                        pageTotal = buyCarBean.getData().getPageTotal();
//                        pageIndex = buyCarBean.getData().getPageCount();
                        List<BuyCarBean.DataBean.ResultBean> list = buyCarBean.getData().getResult();
                        if (list.size() == 0) {
                            ToastUtil.show(getActivity(), "没有更多了!");
                        }
                        for (BuyCarBean.DataBean.ResultBean bean : list
                                ) {
                            recyclerList.add(bean);
                        }
                        recyclerAdapter.notifyDataSetChanged();
                        refreshlayout.finishLoadmore(100);
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

    // 重置请求参数
    private void resetParams() {
        //车型cx  变速箱bsx   排放标准pfbz   燃油类型rylx   座位数(zwsparamName,paramId）
        sxCx = "";
        sxCxName = "";
        sxBsx = "";
        sxBsxName = "";
        sxPfbz = "";
        sxPfbzName = "";
        sxRylx = "";
        sxRylxName = "";
        sxZws = "";
        sxZwsName = "";
        // 车龄cl 里程lc 排量pl(start,end)
        sxClStart = "";
        sxClEnd = "";
        sxLcStart = "";
        sxLcEnd = "";
        sxPlStart = "";
        sxPlEnd = "";

        // 价格
        startPrice = "";
        endPrice = "";

        // 品牌车系
        /**
         * carMap1.put("tv_carbrand", "A");
         carMap1.put("image_carbrand", "");
         carMap1.put("id_carbrand", "");
         */
        selectedBrand.clear();
        selectedBrand.put("tv_carbrand", "");
        selectedBrand.put("image_carbrand", "");
        selectedBrand.put("id_carbrand", "");
        selectedCarName.clear();
        selectedCarName.put("tv_carbrand", "");
        selectedCarName.put("image_carbrand", "");
        selectedCarName.put("id_carbrand", "");

        // 排序方式（智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage）
        selectedRank = "";
        selectedRankId = "";

        // 清空liu布局
        cartagLayout.removeAllViews();
        carLy.setVisibility(View.GONE);

        //tvCz.setTextColor(Color.parseColor("#333333"));
        tvZn.setText(rankList.get(0));

        priceView = null;
        brandView = null;
        carView = null;
    }

    // 添加订阅
    private void addSubscribe() {
        Map<String, String> map = new HashMap<>();
        map.put("car_subs_city", cityId);
        map.put("car_brand", selectedBrand.get("id_carbrand"));// 品牌
        map.put("car_model", selectedCarName.get("id_carbrand"));// 车名
        // 车龄(0,6特殊处理)
        map.put("car_age_start", sxClStart);
        map.put("car_age_end", sxClEnd);
        map.put("car_letout", sxPfbz);//paifangbiaozhun
        map.put("car_type", sxCx);//车辆类型（SUV、MPV、皮卡）
        map.put("car_gearbox", sxBsx);
        map.put("car_seating", sxZws);
        map.put("car_fuel_type", sxRylx);
        // 里程(0,15特殊处理)
        map.put("car_mileage_start", sxLcStart);
        map.put("car_mileage_end", sxLcEnd);
        // 排量(0.0,5.0特殊处理)
        map.put("car_emissions_start", sxPlStart);// 排量
        map.put("car_emissions_end", sxPlEnd);
        Log.i("addSubscribe", map.toString());
        OKhttptils.post(getActivity(), Config.SUBSCRIPTIONFILTRATE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        ToastUtil.show(getActivity(), "订阅成功");
                        // 订阅成功刷新订阅列表数据
                        //getData();
                    } else {
                        ToastUtil.show(getActivity(), "请选择订阅条件");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                ToastUtil.show(getActivity(), "请选择订阅条件");
            }
        });
    }



    class MyBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String city = intent.getStringExtra("city");
            if (city!=null&&!"".equals(city)){
                cityName = city;
                getCityId();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(smsBroadCastReceiver);
    }
}
