package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.BrandRecyclerAdapter;
import com.plt.yzplatform.adapter.MultiTypeSupport;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.CarChexiBean;
import com.plt.yzplatform.entity.QueryYears;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.OKhttptils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class CarChoose extends BaseActivity {

    @BindView(R.id.recyclerView_carBrand)
    RecyclerView recyclerViewCarBrand;
    private BrandRecyclerAdapter recyclerAdapter;
    //通过品牌搜车系
    private List<CarChexiBean.DataBean.ResultBean.TrainListBean> searchChexiList = new ArrayList<>();

    // 字母A-Z
    private List<Map<String, String>> recyclerList = new ArrayList<>();

    private String s;
    private String tv_carbrand;
    private String name;

    private String car_brand;
    private String car_brand_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_choose);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        car_brand = bundle.getString("car_brand");
        car_brand_id = bundle.getString("car_brand_id");

        tv_carbrand = bundle.getString("tv_carbrand");
        String id_carbrand = bundle.getString("id_carbrand");
        name = intent.getStringExtra("name");
        String[] split = id_carbrand.split("_");
        s = split[split.length-1];

        initView();
        // 通过字母获得品牌
        getCarBrandDictByLetter();
    }
    private void initView() {
        recyclerAdapter = new BrandRecyclerAdapter(this, recyclerList, new MultiTypeSupport() {
            @Override
            public int getLayoutId(Object item, int position) {
                String letter = recyclerList.get(position).get("id_carbrand");
                if ("letter".equals(letter)) {
                    return R.layout.item_year;
                } else {
                    return R.layout.item_year_model;
                }
            }
        }) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                if ("letter".equals(recyclerList.get(position).get("id_carbrand"))) {
                    // 字母
                    TextView letter = holder.getView(R.id.car_letter);
                    letter.setText(recyclerList.get(position).get("tv_carbrand"));
                } else {
                    // 品牌
                    TextView name = holder.getView(R.id.tv_carbrand);
                    if (recyclerList.get(position).get("tv_carbrand") != null) {
                        name.setText(recyclerList.get(position).get("tv_carbrand"));
                    }

                }
            }
        };
        recyclerAdapter.setOnItemClickListener(new BrandRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = null;
                String istrue = recyclerList.get(position).get("istrue");
                if(istrue==null){
                    // 点击事件
                    Log.i("点击了", position + "---" + recyclerList.get(position).get("tv_carbrand")  +"车型id："+ recyclerList.get(position).get("id_carbrand") + "品牌：" +car_brand + "品牌id：" + car_brand_id);
                    if(name.equals("1")){
                        intent =new Intent(CarChoose.this,AddCarProduct.class);
                    }else {
                        intent =new Intent(CarChoose.this,UpdateCar.class);
                    }
                    EventBus.getDefault().post(recyclerList.get(position).get("tv_carbrand")+"," + recyclerList.get(position).get("id_carbrand") + "," + car_brand + "," + car_brand_id );
                    startActivity(intent);
                    finish();
                }

            }
        });
        recyclerViewCarBrand.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCarBrand.setAdapter(recyclerAdapter);

    }



    // 查询字母品牌
    private void getCarBrandDictByLetter() {
        Map<String, String> map = new HashMap<>();
        map.put("series_item_id",s);
//        map.put("brandName", "");
//                             QUERYCARSTYLE
        OKhttptils.post(this, Config.QUERYCARSTYLE, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                        Gson gson = new Gson();
                        QueryYears queryYears = gson.fromJson(response, QueryYears.class);
                        List<QueryYears.DataBean.ResultBean> result = queryYears.getData().getResult();
                        recyclerList.clear();
                        String letter = "";
                        for (QueryYears.DataBean.ResultBean bean : result) {
                            String firstChar = bean.getDict_4();
                            if (!letter.equals(firstChar)) {
                                Map<String, String> carMap = new HashMap<>();
                                carMap.put("tv_carbrand", bean.getDict_4());
//                                carMap.put("image_carbrand", "");
                                carMap.put("id_carbrand", "letter");
                                carMap.put("istrue","1");
                                recyclerList.add(carMap);
                                letter = firstChar;
                            }
                            Map<String, String> carMap1 = new HashMap<>();
                            carMap1.put("tv_carbrand", bean.getDict_desc());
                            carMap1.put("id_carbrand",bean.getDict_id());

//                            carMap1.put("image_carbrand", bean.getBrandImg());
//                            carMap1.put("id_carbrand", bean.getBrandId());
                            recyclerList.add(carMap1);
                        }
                        recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void fail(String response) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("车型选择");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(CarChoose.this);
            }
        });
    }

}
