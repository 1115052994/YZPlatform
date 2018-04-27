package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.view.View;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.utils.JumpUtil;

public class CarPhoto extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_photo);



    }
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("车辆照片");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(CarPhoto.this);
            }
        });
    }
}
