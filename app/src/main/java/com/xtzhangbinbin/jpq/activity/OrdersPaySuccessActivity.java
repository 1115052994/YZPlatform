package com.xtzhangbinbin.jpq.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.entity.ShowProductDetaile;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.Prefs;

import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.widget.Button;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class OrdersPaySuccessActivity extends BaseActivity {

    private ShowProductDetaile product;
    @BindView(R.id.orders_pay_success_price)
    TextView price;
    @BindView(R.id.orders_pay_success_style)
    TextView payStyle;
    @BindView(R.id.orders_pay_success_tolist)
    Button tolist;
    @BindView(R.id.orders_pay_success_tocomp)
    Button tocomp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_pay_success);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initData();
        init();
    }

    public void initData(){
        price.setText("￥" + new DecimalFormat("#0.00").format(getIntent().getDoubleExtra("price", 0)));
        payStyle.setText("支付方式：" + (getIntent().getStringExtra("payStyle")));
        //清空缓存中的数据
        Prefs.with(getApplicationContext()).remove("wechat_order_price");
        Prefs.with(getApplicationContext()).remove("wechat_order_id");
    }

    public void init(){
        tolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().jumpRight(OrdersPaySuccessActivity.this, OrdersPersonalActivity.class);
                OrdersPaySuccessActivity.this.finish();
            }
        });
        tocomp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrdersPaySuccessActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("支付完成");
    }

}
