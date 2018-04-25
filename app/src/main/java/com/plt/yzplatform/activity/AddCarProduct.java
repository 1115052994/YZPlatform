package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.view.View;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.utils.JumpUtil;

public class AddCarProduct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_product);
    }





















    @Override
    protected void onStart() {
        super.onStart();
        setTitle("添加产品");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(AddCarProduct.this);
            }
        });
    }
}
