package com.plt.yzplatform.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.utils.DateUtil;
import com.plt.yzplatform.utils.JumpUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;

public class AccessCar extends BaseActivity {

    @BindView(R.id.tv_editcity)
    TextView tvEditcity;
    @BindView(R.id.tv_editbrand)
    TextView tvEditbrand;
    @BindView(R.id.tv_edittime)
    TextView tvEdittime;
    @BindView(R.id.tv_editlc)
    EditText tvEditlc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_car);
        ButterKnife.bind(this);
        Window win = getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("车辆估价");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtil.newInstance().finishRightTrans(AccessCar.this);
            }
        });
    }

    @OnClick({R.id.city, R.id.brand, R.id.time, R.id.access})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.city:
                JumpUtil.newInstance().jumpLeft(this,CityActivity.class);
                CityActivity.setOnBackListener(new CityActivity.BackListener() {
                    @Override
                    public void backData(String city) {
                        tvEditcity.setText(city);
                        tvEditcity.setTextColor(Color.parseColor("#333333"));
                    }
                });
                break;
            case R.id.brand:
                JumpUtil.newInstance().jumpLeft(this,CarBrandSearch.class);
                CarBrandSearch.setOnDataBackListener(new CarBrandSearch.DataBackListener() {
                    @Override
                    public void backData(Map<String, String> brand, Map<String, String> carName) {
                        /**
                         * carMap1.put("tv_carbrand", "A");
                         * carMap1.put("image_carbrand", "");
                         * carMap1.put("id_carbrand", "");
                         */
                    }
                });
                break;
            case R.id.time:
                onYearMonthDayTimePicker();
                break;
            case R.id.access:
                break;
        }
    }

    /* 时间选择器 */
    private void onYearMonthDayTimePicker() {
        DatePicker picker = new DatePicker(this);
        picker.setRangeStart(DateUtil.getYEAR(), DateUtil.getMONTH(), DateUtil.getDAY());
        picker.setRangeEnd(DateUtil.getYEAR()-30, 12, 31);
        picker.setTextColor(0xFF000000);
        picker.setDividerColor(0xFFFFFFFF);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvEdittime.setText(year + "年" + month + "月");
                tvEdittime.setTextColor(Color.parseColor("#333333"));
            }
        });
        picker.show();
    }

    @OnClick(R.id.tv_editlc)
    public void onViewClicked() {
    }
}
