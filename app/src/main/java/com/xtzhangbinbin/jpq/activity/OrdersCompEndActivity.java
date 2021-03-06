package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.OrdersView;
import com.xtzhangbinbin.jpq.entity.ShowProductDetaile;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class OrdersCompEndActivity extends BaseActivity {

    private ShowProductDetaile product;
    @BindView(R.id.orders_comp_end_price)
    TextView orders_comp_end_price;
    @BindView(R.id.orders_comp_end_service_name)
    TextView orders_comp_end_service_name;
    @BindView(R.id.orders_comp_end_order_id)
    TextView orders_comp_end_order_id;
    @BindView(R.id.orders_comp_end_date)
    TextView orders_comp_end_date;
    @BindView(R.id.orders_to_view)
    Button orders_to_view;

    private OrdersView orders;
    private String order_id = "92a00c10a8444bfea1abf5b95b8f9a16";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_comp_end);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initData();
        init();
    }

    public void initData(){
        if(null != getIntent().getStringExtra("order_id")){
            order_id = getIntent().getStringExtra("order_id");
        }

        if(null != order_id){
            Map<String, String> map = new HashMap<>();
            map.put("order_id", order_id);
            OKhttptils.post(this, Config.ORDERS_GET_BYCODE, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Gson gson = GsonFactory.create();
                    orders = gson.fromJson(response, OrdersView.class);
                    //根据订单信息构建出商品对象，以便于信息展示和下一步的支付操作
                    if (null != orders) {
                        orders_comp_end_price.setText("金额：￥" + String.valueOf(orders.getData().getResult().getProd_reduced_price()));
                        orders_comp_end_service_name.setText(orders.getData().getResult().getProd_service_name());
                        orders_comp_end_order_id.setText("订单号:" + orders.getData().getResult().getOrder_number());
                        orders_comp_end_date.setText("消费时间：" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    }

                }

                @Override
                public void fail(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastUtil.show(OrdersCompEndActivity.this, jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
//
    public void init(){
        orders_to_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id", order_id);
                JumpUtil.newInstance().jumpRight(OrdersCompEndActivity.this, OrdersPersonalViewActivity.class, bundle);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("完成消费");
    }

}
