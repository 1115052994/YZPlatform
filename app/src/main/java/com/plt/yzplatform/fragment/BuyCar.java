package com.plt.yzplatform.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.AppraiseInterface;
import com.plt.yzplatform.R;
import com.plt.yzplatform.activity.AdvanceSX;
import com.plt.yzplatform.activity.CarBrandSearch;
import com.plt.yzplatform.adapter.AppraiseGVAdapter;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.BuyCarBean;
import com.plt.yzplatform.entity.CarParams;
import com.plt.yzplatform.utils.CommonUtils;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.view.CarTagLayout;
import com.plt.yzplatform.view.ExpandableGridView;
import com.plt.yzplatform.view.SeekBarPressure;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

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
    CarTagLayout cartagLayout;
    @BindView(R.id.car_ly)
    RelativeLayout carLy;


    // 轮播图
    private List<String> bannersImage = new ArrayList<>();

    private List<BuyCarBean.DataBean.ResultBean> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;

    // 城市
    private String cityName = "济南";
    private String cityId = "";

    //高级筛选
    //车型cx  变速箱bsx   排放标准pfbz   燃油类型rylx   座位数(zwsparamName,paramId）
    // 车龄cl 里程lc 排量pl(start,end)
    private String sxCx = "";//不限全是Id
    private String sxBsx = "";
    private String sxPfbz = "";
    private String sxRylx = "";
    private String sxZws = "";
    private String sxClStart = "",sxClEnd="";
    private String sxLcStart = "",sxLcEnd ="";
    private String sxPlStart = "",sxPlEnd = "";
    // 价格
    private String startPrice = "0",endPrice="";
    // 品牌车系
    /**
     * carMap1.put("tv_carbrand", "A");
     carMap1.put("image_carbrand", "");
     carMap1.put("id_carbrand", "");
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_buy, container, false);
        unbinder = ButterKnife.bind(this, view);
        iniData();
        initView();
        getData();
        return view;
    }

    private void initView() {
        //隐藏筛选tag布局
        carLy.setVisibility(View.GONE);

        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //此处可以自行选择，我直接用的Picasso
//                Picasso.with(CompDetail.this).load("http://pic.58pic.com/58pic/14/54/14/09E58PICUpb_1024.jpg").into(imageView);
                OKhttptils.getPic(getActivity(), (String) path, imageView);
            }
        });


        recyclerAdapter = new CommonRecyclerAdapter(getActivity(), recyclerList, R.layout.item_car_buy) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
//                TextView tvCarsDes = holder.getView(R.id.tv_cardes);
//                tvCarsDes.setText(recyclerList.get(position).getCar_name());
//                TextView tvPrice = holder.getView(R.id.tv_price);
//                tvPrice.setText("￥"+recyclerList.get(position).getCar_price()/10000.0+"万");
//                TextView tvYearAndMiles = holder.getView(R.id.tv_year_miles);
//                tvYearAndMiles.setText(recyclerList.get(position).getCar_sign_date().split("_")[0]+"年/"+recyclerList.get(position).getCar_mileage()+"公里");
//                ImageView carImage = holder.getView(R.id.image_car);
//                OKhttptils.getPic(getActivity(),recyclerList.get(position).getCar_1_icon_file_id(),carImage);
            }
        };
        recyclerViewCar.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCar.setAdapter(recyclerAdapter);

    }

    private void iniData() {
        rankList.add("智能排序");
        rankList.add("价格最高");
        rankList.add("价格最低");
        rankList.add("车龄最短");
        rankList.add("里程最少");
        rankList.add("最新发布");
        jgList.add("不限价格");
        jgList.add("5万以下");
        jgList.add("5万-10万");
        jgList.add("10万-15万");
        jgList.add("15万-20万");
        jgList.add("20万-30万");
        jgList.add("30万-50万");
        jgList.add("50万以上");
    }

    @Override
    public void onStop() {
        super.onStop();
//        banner.stopAutoPlay();
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
                tvPp.setTextColor(Color.parseColor("#ff9696"));
                imagePp.setImageResource(R.drawable.b_g);
                JumpUtil.newInstance().jumpLeft(getActivity(), CarBrandSearch.class);
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
                JumpUtil.newInstance().jumpLeft(getActivity(), AdvanceSX.class);
                AdvanceSX.setOnDataBackListener(new AdvanceSX.DataBackListener() {
                    @Override
                    public void backData(Map<String, String> map) {
                        Log.i("databack",map.toString());
                        cartagLayout.removeAllViews();
                        //显示搜索选项   重新搜索  返回数据（
                        //车型cx  变速箱bsx   排放标准pfbz   燃油类型rylx   座位数(zwsparamName,paramId）
                        // 车龄cl 里程lc 排量pl(start,end)
                        if(!"".equals(map.get("cx"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("cx").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxCx = map.get("cx").split(",")[1];
                        }
                        if(!"".equals(map.get("bsx"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("bsx").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxBsx = map.get("bsx").split(",")[1];
                        }
                        if(!"".equals(map.get("pfbz"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("pfbz").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxPfbz = map.get("pfbz").split(",")[1];
                        }
                        if(!"".equals(map.get("rylx"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("rylx").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxRylx = map.get("rylx").split(",")[1];
                        }
                        if(!"".equals(map.get("zws"))) {
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                            TextView tv = view.findViewById(R.id.tv_tag);
                            tv.setText(map.get("zws").split(",")[0]);
                            if (tv.getParent() != null) {
                                ViewGroup group = (ViewGroup) tv.getParent();
                                group.removeAllViews();
                            }
                            cartagLayout.addView(tv);
                            sxZws = map.get("zws").split(",")[1];
                        }
                        // 车龄0-6
                        View view_cl = LayoutInflater.from(getContext()).inflate(R.layout.item_cartag, null);
                        TextView tv_cl = view_cl.findViewById(R.id.tv_tag);
                        if (tv_cl.getParent() != null) {
                            ViewGroup group = (ViewGroup) tv_cl.getParent();
                            group.removeAllViews();
                        }
                        String cl[] = map.get("cl").split(",");
                        if ("0".equals(cl[0])){
                            if (!"6".equals(cl[1])){
                                //<6
                                tv_cl.setText(cl[1]+"年以内");
                                cartagLayout.addView(tv_cl);
                            }else {
                                // 车龄不限
                            }
                        }else{
                            if ("6".equals(cl[1])){
                                //>0
                                tv_cl.setText(cl[0]+"年以上");
                                cartagLayout.addView(tv_cl);
                            }else {
                                tv_cl.setText(cl[0]+"年-"+cl[1]+"年");
                                cartagLayout.addView(tv_cl);
                            }
                        }
                        // 里程0-15
                        String lc[] = map.get("lc").split(",");
                        Log.i("shaixuan",lc[0]+"---"+lc[1]);
                        if ("0.0".equals(lc[0])){
                            if (!"15".equals(lc[1])){
                                //<15
                            }else {
                                // 不限
                            }
                        }else{
                            if ("15".equals(lc[1])){
                                //>0
                            }else {

                            }
                        }
                        // 排量0-5.0
                        String pl[] = map.get("pl").split(",");
                        Log.i("shaixuan",pl[0]+"---"+pl[1]);
                        if ("0".equals(pl[0])){
                            if (!"5.0".equals(pl[1])){
                                //<5.0
                            }else {
                                // 不限
                            }
                        }else{
                            if ("5.0".equals(pl[1])){
                                //>0
                            }else {

                            }
                        }
                        // 显示搜索筛选信息
                        carLy.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.ly_cz:
                // 重置
                tvCz.setTextColor(Color.parseColor("#ff9696"));
                //tvCz.setTextColor(Color.parseColor("#333333"));
                break;
        }
    }


    private PopupWindow popupWindow;
    private List<String> rankList = new ArrayList<>();
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
                tvZn.setTextColor(Color.parseColor("#333333"));
                imageZn.setImageResource(R.drawable.b_a);
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
                    if (progressHigh == 6)
                        tvPrice.setText("不限价格");
                    else
                        tvPrice.setText((int)progressHigh + "0万以下");
                } else {
                    if (progressHigh == 6)
                        tvPrice.setText((int)progressLow + "0万以上");
                    else
                        tvPrice.setText((int)progressLow + "0万-" + (int)progressHigh + "0万");
                }
            }
        });

        ExpandableGridView priceGv = contentView.findViewById(R.id.price_gv);
        jgAdapter = new AppraiseGVAdapter(getActivity(), jgList);
        jgAdapter.setOnItemClickListener(new AppraiseInterface() {
            @Override
            public void onClick(View view, int position) {

            }
        });
        priceGv.setAdapter(jgAdapter);
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
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(line1, 0, 0);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.verticalMargin = CommonUtils.dip2px(getActivity(), 30);
        lp.alpha = 0.9f;
        getActivity().getWindow().setAttributes(lp);
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

     // 搜索Car列表(添加筛选条件)
    public void getCarList() {
        Map<String, String> map = new HashMap<>();
        map.put("city", cityId);
        map.put("car_mileage_start", "");
        map.put("car_mileage_end", "");
        map.put("car_age_start", "");
        map.put("car_age_end", "");
        map.put("car_letout", "");
        map.put("car_type", "");//车辆类型（SUV、MPV、皮卡）
        map.put("car_gearbox", "");
        map.put("car_seating", "");
        map.put("car_price_start", "");
        map.put("car_price_end", "");
        map.put("car_train", "");
        map.put("car_train_item", "");
        map.put("sort", "");//排序方式（智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage）
        map.put("pageIndex", "");
        map.put("pageSize", "");
        map.put("car_name", "");
        map.put("car_fuel_type", "");
        OKhttptils.post(getActivity(), Config.SEARCHCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        BuyCarBean buyCarBean = gson.fromJson(response, BuyCarBean.class);
                        recyclerList.clear();
                        List<BuyCarBean.DataBean.ResultBean> list = buyCarBean.getData().getResult();
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

    // 获得car各种信息字典以及banner
    public void getCarParams() {
        Map<String,String> map =new HashMap<>();
        OKhttptils.post(getActivity(), Config.GETCARPARAMDICT, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("response===",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                       Gson gson = new Gson();
                       CarParams carParams = gson.fromJson(response, CarParams.class);
                        CarParams.DataBean.ResultBean resultBean = carParams.getData().getResult();
                        //banner
                        List<CarParams.DataBean.ResultBean.BannerListBean> bannerListBeans = resultBean.getBannerList();
                        bannersImage.clear();
                        for (CarParams.DataBean.ResultBean.BannerListBean bean:bannerListBeans){
                            bannersImage.add(bean.getAdve_file_id());
                        }
                        banner.setImages(bannersImage);
                        banner.start();
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
}
