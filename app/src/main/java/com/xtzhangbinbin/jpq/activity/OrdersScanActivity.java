package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.zxing.android.CaptureActivity;

import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/5/1 0001.
 */
@SuppressWarnings("all")
public class OrdersScanActivity extends BaseActivity {

    @BindView(R.id.test)
    Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_scan);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().jumpRight(getApplicationContext(), CaptureActivity.class);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("订单详情");
    }

}