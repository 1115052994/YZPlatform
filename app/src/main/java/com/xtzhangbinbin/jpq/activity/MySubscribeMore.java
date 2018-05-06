package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.BuyCarBean;
import com.xtzhangbinbin.jpq.entity.SubscribeTag;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MySubscribeMore extends BaseActivity {

    @BindView(R.id.recyclerView_car)
    RecyclerView recyclerViewCar;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;

    private List<BuyCarBean.DataBean.ResultBean> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;
    private SubscribeTag.DataBean.ResultBean bean;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribe_more);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        if(intent!=null){
            Bundle bundle = intent.getExtras();
            if (bundle!=null){
                 bean = (SubscribeTag.DataBean.ResultBean) bundle.get("bean");
                getCars(recyclerAdapter,recyclerList,bean,1);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("我的订阅");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(MySubscribeMore.this);
            }
        });
    }
    private void initView() {
        // 二手车列表
        recyclerAdapter = new CommonRecyclerAdapter(this, recyclerList, R.layout.item_car_buy) {
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
                    OKhttptils.getPic(MySubscribeMore.this, recyclerList.get(position).getCar_1_icon_file_id(), carImage);
                }
            }
        };
        recyclerViewCar.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCar.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                JumpUtil.newInstance().jumpLeft(MySubscribeMore.this, CarDetailsActivity.class, recyclerList.get(position).getCar_id() + "");
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getCars(recyclerAdapter,recyclerList,bean,2);
            }
        });
    }

    // 查询二手车
    private void getCars(final CommonRecyclerAdapter adapter, final List<BuyCarBean.DataBean.ResultBean> carList,final SubscribeTag.DataBean.ResultBean bean,int type){
        if (type == 1){
            pageIndex = 1;
        }else{
            pageIndex++;
        }
        Map<String, String> map = new HashMap<>();
        map.put("city", bean.getCity());
        map.put("car_mileage_start", bean.getCar_mileage_start());
        map.put("car_mileage_end", bean.getCar_mileage_end());
        map.put("car_age_start", bean.getCar_age_start());
        map.put("car_age_end", bean.getCar_age_end());
        map.put("car_letout", bean.getCar_letout());
        map.put("car_type", bean.getCar_type());//车辆类型（SUV、MPV、皮卡）
        map.put("car_gearbox", bean.getCar_gearbox());
        map.put("car_seating", bean.getCar_seating());
        map.put("car_price_start", "");//元为单位
        map.put("car_price_end", "");//元为单位
        //map.put("car_train", "");//车系大
        map.put("car_train_item", bean.getCar_model());//车系小
        map.put("sort", "");//排序方式（智能：intelligence， 价格升序：price_up， 价格降序， price_down， 车龄：car_age， 上架时间：sale_time，里程：car_mileage）
        map.put("pageIndex",pageIndex+"");
        map.put("pageSize", "20");
        map.put("car_name", "");//模糊搜索匹配
        map.put("car_fuel_type", bean.getCar_fuel_type());
        map.put("car_brand",bean.getCar_brand());
        map.put("car_emissions_start",bean.getCar_emissions_start());
        map.put("car_emissions_end",bean.getCar_emissions_end());
        map.put("histroy_word","");
        //Log.i("getCarList=======",map.toString());
        //car_brand  car_emissions_start  car_emissions_end  histroy_word
        OKhttptils.post(MySubscribeMore.this, Config.SEARCHCAR, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("Subscribe",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))) {
                        Gson gson = new Gson();
                        BuyCarBean buyCarBean = gson.fromJson(response, BuyCarBean.class);
                        //pageIndex = buyCarBean.getData().getPageCount();
                        //pageTotal = buyCarBean.getData().getPageTotal();
                        if (pageIndex == 1)
                            carList.clear();
                        List<BuyCarBean.DataBean.ResultBean> list = buyCarBean.getData().getResult();
                        if (list.size() == 0){
                            ToastUtil.show(MySubscribeMore.this,"未查询到相关车系");
                        }
                        for (BuyCarBean.DataBean.ResultBean bean : list
                                ) {
                            carList.add(bean);
                        }
                        adapter.notifyDataSetChanged();
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
