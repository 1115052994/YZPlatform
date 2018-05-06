package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.adapter.CommonRecyclerAdapter;
import com.xtzhangbinbin.jpq.adapter.MultiTypeSupport;
import com.xtzhangbinbin.jpq.adapter.ViewHolder;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.BuyCarBean;
import com.xtzhangbinbin.jpq.entity.SubscribeTag;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.CarTagLayout;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySubscribe extends BaseActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    // 订阅列表
    private List<SubscribeTag.DataBean.ResultBean> recyclerList = new ArrayList<>();
    private CommonRecyclerAdapter recyclerAdapter;
    private int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribe);
        ButterKnife.bind(this);
        initView();
        getData();
        size = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("我的订阅");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(MySubscribe.this);
            }
        });
    }


    private void initView(){
        // 订阅列表Adapter
        recyclerAdapter = new CommonRecyclerAdapter(this, recyclerList, new MultiTypeSupport() {
            @Override
            public int getLayoutId(Object item, int position) {
                return R.layout.item_subscribe_car;
            }
        }) {
            @Override
            public void convert(final ViewHolder holder, Object item, final int position) {
                // 添加头部
                final SubscribeTag.DataBean.ResultBean resultBean = recyclerList.get(position);
                holder.getView(R.id.tv_more).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 查看更多
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bean",resultBean);
                        JumpUtil.newInstance().jumpLeft(MySubscribe.this,MySubscribeMore.class,bundle);
                    }
                });
                Class clazz = resultBean.getClass();
                final Field[] fields = clazz.getDeclaredFields();
                final CarTagLayout cartagLayout = holder.getView(R.id.cartag_layout);
                cartagLayout.removeAllViews();
                cartagLayout.setOnItemClickListener(new CarTagLayout.OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        // 删除订阅标签
                        TextView tv = (TextView) view;
                        String text =tv.getText().toString();
                        Log.i("carTagClick",text);
                        for (Field field:fields) {
                            field.setAccessible(true);
                            try {
                                String fieldValue = field.get(resultBean)+"";
                                String fieldName = field.getName();
                                if (fieldName.contains("name")){
                                    if (!"".equals(fieldValue)&&(text.contains(fieldValue)||text.equals(fieldValue))){
                                        // 匹配
                                        Log.i("carTagClick",fieldName+"---"+fieldValue);
                                        field.set(resultBean,"");
                                        String startName = fieldName.split("_")[0];
                                        for (Field f:fields) {
                                            f.setAccessible(true);
                                            String name = f.getName();
                                            if (name.contains(startName)&&!name.contains("name")){
                                                if (startName.equals(name.split("_")[1])) {
                                                    //修改Name对应的Id
                                                    Log.i("carTagClick", name + "---" + f.get(resultBean));
                                                    if (cartagLayout.getChildCount() == 1) {
                                                        final OrdinaryDialog dialog = OrdinaryDialog.newInstance(MySubscribe.this);
                                                        dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                                                            @Override
                                                            public void onYesClick() {
                                                                // 删除dingyue
                                                                delSubsrcibe(resultBean,null);
                                                                dialog.dismiss();
                                                            }
                                                        }).setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                                                            @Override
                                                            public void onNoClick() {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                        dialog.setMessage1("删除订阅").setMessage2("确定删除此条订阅吗？").show();
                                                    }else{
                                                        f.set(resultBean, "");
                                                        cartagLayout.removeView(view);
                                                        // 修改订阅信息
                                                        updateSubscribe(resultBean);
                                                    }
                                                    return;
                                                }
                                            }
                                        }
                                        return;
                                    }
                                }else{
                                    // 车龄 里程  排量

                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                // 添加订阅标签
                for (Field field:fields){
                    field.setAccessible(true);
                    try {
                    Log.i("convert===",field.getName()+"-----"+field.get(resultBean));
                        if(field.get(resultBean)!=null&&!"".equals(field.get(resultBean)+"")&&!"serialVersionUID".equals(field.getName())){
                            if (field.getName().contains("name")) {
                                View ly = LayoutInflater.from(MySubscribe.this).inflate(R.layout.item_cartag, null);
                                TextView tag = ly.findViewById(R.id.tv_tag);
                                if (tag.getParent() != null) {
                                    ViewGroup group = (ViewGroup) tag.getParent();
                                    group.removeView(tag);
                                }
                                cartagLayout.addView(tag);
                                tag.setText(field.get(resultBean) + "");
                            }else{
                                // 车龄 里程  排量
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                // 二手车列表
                final List<BuyCarBean.DataBean.ResultBean> recyclerCarList = new ArrayList<>();
                RecyclerView recyclerViewCar = holder.getView(R.id.recyclerView_car);
                CommonRecyclerAdapter  recyclerAdapterCar = new CommonRecyclerAdapter(MySubscribe.this, recyclerCarList, R.layout.item_car_buy) {
                    @Override
                    public void convert(ViewHolder holder, Object item, int position) {
                        TextView tvCarsDes = holder.getView(R.id.tv_cardes);
                        tvCarsDes.setText(recyclerCarList.get(position).getCar_name());
                        TextView tvPrice = holder.getView(R.id.tv_price);
                        DecimalFormat df = new DecimalFormat("#.00");
                        double price = recyclerCarList.get(position).getCar_price()/10000;
                        df.format(price);
                        tvPrice.setText("￥"+price+"万");
                        TextView tvYearAndMiles = holder.getView(R.id.tv_year_miles);
                        tvYearAndMiles.setText(recyclerCarList.get(position).getCar_sign_date().split("-")[0]+"年/"+recyclerCarList.get(position).getCar_mileage()+"万公里");
                        ImageView carImage = holder.getView(R.id.image_car);
                        if(recyclerCarList.get(position).getCar_1_icon_file_id()!=null) {
                            OKhttptils.getPic(MySubscribe.this,recyclerCarList.get(position).getCar_1_icon_file_id(),carImage);
                        }
                    }
                };
                recyclerViewCar.setLayoutManager(new LinearLayoutManager(MySubscribe.this));
                recyclerViewCar.setAdapter(recyclerAdapterCar);
                recyclerAdapterCar.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        JumpUtil.newInstance().jumpLeft(MySubscribe.this, CarDetailsActivity.class,recyclerCarList.get(position).getCar_id()+"");
                    }
                });
                // 获取二手车列表
                getCars(recyclerAdapterCar,recyclerCarList,recyclerList.get(position));

                // 删除订阅
                holder.getView(R.id.tv_del).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final OrdinaryDialog dialog = OrdinaryDialog.newInstance(MySubscribe.this);
                        dialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                delSubsrcibe(recyclerList.get(position),null);
                                dialog.dismiss();
                            }
                        }).setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                dialog.dismiss();
                            }
                        });
                        dialog.setMessage1("删除订阅").setMessage2("确定删除此条订阅吗？").show();
                    }
                });
                // 编辑
                holder.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("bean",resultBean);
                        bundle.putString("String","subscribe");
                        JumpUtil.newInstance().jumpLeft(MySubscribe.this,AdvanceSX.class,bundle);
                        AdvanceSX.setOnDataBackListener(new AdvanceSX.DataBackListener() {
                            @Override
                            public void backData(Map<String, String> map) {
                                if (map == null){
                                    return;
                                }
                                //先删除原来记录
                                delSubsrcibe(resultBean,map);
                                //更新(时间先后问题)
                                //getCityId(map);
                            }
                        });
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @OnClick(R.id.subscribe)
    public void onViewClicked() {
        if (size>=5) {
            // 最多添加5个
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("String","subscribe");
        JumpUtil.newInstance().jumpLeft(this,AdvanceSX.class,bundle);
        AdvanceSX.setOnDataBackListener(new AdvanceSX.DataBackListener() {
            @Override
            public void backData(Map<String, String> map) {
                if (map == null){
                    return;
                }
                getCityId(map);
            }
        });
    }

    private void getData(){
        // 查询订阅
        querySubscribe();
    }

    //通过城市名获取城市Id
    public void getCityId(final Map<String, String> subscribe) {
        if ("".equals(subscribe.get("city"))){
            subscribe.put("cityId", "");
            addSubscribe(subscribe);
        }else {
            Map<String, String> map = new HashMap<>();
            map.put("cityName", subscribe.get("city"));
            OKhttptils.post(this, Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject data = object.getJSONObject("data");
                        JSONObject result = data.getJSONObject("result");
                        String cityId = result.getString("city_id");
                        subscribe.put("cityId", cityId);
                        addSubscribe(subscribe);
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

    // 添加订阅
    private void addSubscribe(final Map<String, String> subscribe){
        Map<String, String> map = new HashMap<>();
        map.put("car_subs_city", "".equals( subscribe.get("cityId"))?"":subscribe.get("cityId"));
        map.put("car_brand", ",".equals(subscribe.get("brand"))?"":subscribe.get("brand").split(",")[1]);// 品牌
        map.put("car_model", ",".equals(subscribe.get("carname"))?"":subscribe.get("carname").split(",")[1]);// 车名
        String[] cl= subscribe.get("cl").split(",");
        // 车龄(0,6特殊处理)
        map.put("car_age_start", "0".equals(cl[0])?"":cl[0]);
        map.put("car_age_end", "6".equals(cl[1])?"":cl[1]);
        map.put("car_letout", "".equals(subscribe.get("pfbz"))?"":subscribe.get("pfbz").split(",")[1]);//paifangbiaozhun
        map.put("car_type", "".equals(subscribe.get("cx"))?"":subscribe.get("cx").split(",")[1]);//车辆类型（SUV、MPV、皮卡）
        map.put("car_gearbox", "".equals(subscribe.get("bsx"))?"":subscribe.get("bsx").split(",")[1]);
        map.put("car_seating", "".equals(subscribe.get("zws"))?"":subscribe.get("zws").split(",")[1]);
        map.put("car_fuel_type", "".equals(subscribe.get("rylx"))?"":subscribe.get("rylx").split(",")[1]);
        String[] lc= subscribe.get("lc").split(",");
        // 里程(0,15特殊处理)
        map.put("car_mileage_start","0".equals(lc[0])?"":lc[0]);
        map.put("car_mileage_end","15".equals(lc[1])?"":lc[1]);
        String[] pl= subscribe.get("pl").split(",");
        // 排量(0.0,5.0特殊处理)
        map.put("car_emissions_start","0.0".equals(pl[0])?"":pl[0]);// 排量
        map.put("car_emissions_end","5.0".equals(pl[1])?"":pl[1]);
        //Log.i("addSubscribe",map.toString());
        OKhttptils.post(this, Config.SUBSCRIPTIONFILTRATE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))){
                        ToastUtil.show(MySubscribe.this,"订阅成功");
                        // 订阅成功刷新订阅列表数据
                        getData();
                    }else{
                        ToastUtil.show(MySubscribe.this,"请选择订阅条件");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void fail(String response) {
                ToastUtil.show(MySubscribe.this,"请选择订阅条件");
            }
        });
    }

    // 查询订阅
    private void querySubscribe(){
        Map<String,String> map = new HashMap<>();//SELECTUSERSUBSCRIPTIONFILTRATE selectCarByItem
        OKhttptils.post(this, Config.SELECTUSERSUBSCRIPTIONFILTRATE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("Subscribe",response);
                try {
                    JSONObject object = new JSONObject(response);
                    Gson gson = new Gson();
                    SubscribeTag subscribeTag = gson.fromJson(response,SubscribeTag.class);
                    List<SubscribeTag.DataBean.ResultBean> resultBeanList = subscribeTag.getData().getResult();
                    size = resultBeanList.size();
                    recyclerList.clear();
                    for (SubscribeTag.DataBean.ResultBean bean:resultBeanList){
                        recyclerList.add(bean);
                    }
                    recyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                Log.i("Subscribe","fail="+response);
            }
        });
    }

    // 删除订阅
    private void delSubsrcibe(final SubscribeTag.DataBean.ResultBean bean, final Map<String, String> map1){
        recyclerList.remove(bean);
        recyclerAdapter.notifyDataSetChanged();

        final Map<String,String> map = new HashMap<>();
        map.put("car_subs_id",bean.getCar_subs_id());
        OKhttptils.post(this, Config.DELETESUBSCRIPTIONFILTRATE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if ("1".equals(status)){
                        if (map1 == null) {
                            ToastUtil.show(MySubscribe.this, "删除订阅成功");
                        }else {
                            // 刷新订阅列表
                            Log.i("delsubscribe",map1.toString());
                            getCityId(map1);
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

    // 修改订阅
    private void updateSubscribe(SubscribeTag.DataBean.ResultBean bean){
        Map<String, String> map = new HashMap<>();
        map.put("car_subs_city", bean.getCity());
        map.put("car_brand", bean.getCar_brand());// 品牌
        map.put("car_model", bean.getCar_model());// 车名
        map.put("car_age_start", bean.getCar_age_start());
        map.put("car_age_end",bean.getCar_age_end());
        map.put("car_letout", bean.getCar_letout());//paifangbiaozhun
        map.put("car_type", bean.getCar_type());//车辆类型（SUV、MPV、皮卡）
        map.put("car_gearbox", bean.getCar_gearbox());
        map.put("car_seating", bean.getCar_seating());
        map.put("car_fuel_type", bean.getCar_fuel_type());
        map.put("car_mileage_start",bean.getCar_mileage_start());
        map.put("car_mileage_end",bean.getCar_mileage_end());
        map.put("car_emissions_start",bean.getCar_emissions_start());// 排量
        map.put("car_emissions_end",bean.getCar_mileage_end());
        map.put("car_subs_id",bean.getCar_subs_id());
        Log.i("updateSubscribe---",map.toString());
        OKhttptils.post(MySubscribe.this, Config.UPDATESUBSCRIPTIONFILTRATE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.i("updateSubscribe",response);
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))){
                        ToastUtil.show(MySubscribe.this,"修改订阅成功");
                        // 更新订阅列表
                         getData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                Log.i("updateSubscribe","fail="+response);
            }
        });
    }

    // 查询二手车
    private void getCars(final CommonRecyclerAdapter adapter, final List<BuyCarBean.DataBean.ResultBean> carList,final SubscribeTag.DataBean.ResultBean bean){
//        Log.i("getCarList","cityId="+bean.getCity());
//        Log.i("getCarList","sxLcStart="+bean.getCar_mileage_start());
//        Log.i("getCarList","sxLcEnd="+bean.getCar_mileage_end());
//        Log.i("getCarList","sxClStart="+bean.getCar_age_start());
//        Log.i("getCarList","sxClEnd="+bean.getCar_age_end());
//        Log.i("getCarList","sxPfbz="+bean.getCar_letout());
//        Log.i("getCarList","sxCx="+bean.getCar_type());
//        Log.i("getCarList","sxBsx="+bean.getCar_gearbox());
//        Log.i("getCarList","sxZws="+bean.getCar_seating());
//        Log.i("getCarList","brand="+bean.getCar_brand());
//        Log.i("getCarList","carname="+bean.getCar_model());
//        Log.i("getCarList","sxRylx="+bean.getCar_fuel_type());
//        Log.i("getCarList","sxPlStart="+bean.getCar_emissions_start());
//        Log.i("getCarList","sxPlEnd="+bean.getCar_emissions_end());

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
        map.put("pageIndex","1");
        map.put("pageSize", "3");
        map.put("car_name", "");//模糊搜索匹配
        map.put("car_fuel_type", bean.getCar_fuel_type());
        map.put("car_brand",bean.getCar_brand());
        map.put("car_emissions_start",bean.getCar_emissions_start());
        map.put("car_emissions_end",bean.getCar_emissions_end());
        map.put("histroy_word","");
        //Log.i("getCarList=======",map.toString());selectCarByItem SEARCHCAR
        //car_brand  car_emissions_start  car_emissions_end  histroy_word
        OKhttptils.post(MySubscribe.this, Config.SELECTCARBYITEM, map, new OKhttptils.HttpCallBack() {
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
                        carList.clear();
                        List<BuyCarBean.DataBean.ResultBean> list = buyCarBean.getData().getResult();
                        if (list.size() == 0){
                            //ToastUtil.show(MySubscribe.this,"未查询到相关车系");
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
