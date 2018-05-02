package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.adapter.CommonRecyclerAdapter;
import com.plt.yzplatform.adapter.ViewHolder;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.entity.AccessResultBean;
import com.plt.yzplatform.utils.JumpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccessResult extends BaseActivity {

    @BindView(R.id.tv_compPrice)
    TextView tvCompPrice;
    @BindView(R.id.tv_perPrice)
    TextView tvPerPrice;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_access)
    TextView tvAccess;
    @BindView(R.id.tv_car)
    TextView tvCar;

    private CommonRecyclerAdapter adapter;
    private List<Map<String,String>> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_result);
        ButterKnife.bind(this);
        initView();
        Intent intent = getIntent();
        if(intent!=null){
            getData(intent.getExtras().getString("price"));
            tvAccess.setText("评价结果："+intent.getExtras().getString("name"));
            tvCar.setText(intent.getExtras().getString("car"));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("估价结果");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(AccessResult.this);
            }
        });
    }

    private void initView(){
        adapter = new CommonRecyclerAdapter(this,list,R.layout.item_access_price) {
            @Override
            public void convert(ViewHolder holder, Object item, int position) {
                TextView tvPrice = holder.getView(R.id.tv_price);
                TextView tvLoc = holder.getView(R.id.tv_loc);
                tvPrice.setText(list.get(position).get("price"));
                tvLoc.setText(list.get(position).get("loc"));
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void getData(String response){
        Gson gson = new Gson();
        AccessResultBean resultBean = gson.fromJson(response, AccessResultBean.class);
        List<AccessResultBean.DataBean.ResultBean.EstPriceAreaBean> priceAreaBeanList = resultBean.getData().getResult().getEst_price_area();
        List<Double> priceResult = resultBean.getData().getResult().getEst_price_result();
        if (priceResult!=null) {
            tvCompPrice.setText(priceResult.get(1) + "万");
            tvPerPrice.setText(priceResult.get(2) + "万");
        }
        list.clear();
        for (AccessResultBean.DataBean.ResultBean.EstPriceAreaBean bean:priceAreaBeanList){
            Map<String,String> map = new HashMap<>();
            map.put("loc",bean.getArea());
            map.put("price",bean.getPrice()+"万");
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }

}
