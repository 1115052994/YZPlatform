package com.plt.yzplatform.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.entity.ShowProductDetaile;
import com.plt.yzplatform.utils.ActivityUtil;
import java.text.DecimalFormat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class OrdersPaySuccessActivity extends BaseActivity {

    private ShowProductDetaile product;
    @BindView(R.id.orders_pay_success_price)
    TextView price;
    @BindView(R.id.orders_pay_success_style)
    TextView payStyle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_pay_success);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initData();
    }

    public void initData(){
        price.setText("￥" + new DecimalFormat("#0.00").format(getIntent().getDoubleExtra("price", 0)));
        payStyle.setText("支付方式：" + (getIntent().getStringExtra("payStyle")));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("支付完成");
    }

}
