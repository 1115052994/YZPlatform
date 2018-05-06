package com.xtzhangbinbin.jpq.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.DateUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

    private String cityId = "";
    private String carYear = "",carMonth = "";
    private String carId = "";

    private MyProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_car);
        ButterKnife.bind(this);
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
                        getCityId(city);
                    }
                });
                break;
            case R.id.brand:
                JumpUtil.newInstance().jumpLeft(this,CarAccessBrandSearch.class);
                CarAccessModeSearch.setDataBackListener(new CarAccessModeSearch.DataBackListener() {
                    @Override
                    public void backData(String id, String name) {
                        carId = id;
                        tvEditbrand.setText(name);
                        tvEditbrand.setTextColor(Color.parseColor("#333333"));
                    }
                });
                break;
            case R.id.time:
                onYearMonthDayTimePicker();
                break;
            case R.id.access:
                accessCar();
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
                carYear = year;
                carMonth = month;
            }
        });
        picker.show();
    }


    //通过城市名获取城市Id
    public void getCityId(String cityName) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(this, Config.GETJHCITYID, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONArray array = data.getJSONArray("result");
                    JSONObject object1 = array.getJSONObject(0);
                    cityId = object1.getString("juheId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void fail(String response) {

            }
        });
    }


    private void accessCar(){
        dialog = MyProgressDialog.createDialog(this);
        dialog.setMessage("正在加载中,请稍候");
        dialog.show();
        if ("".equals(cityId)||"".equals(carId)||"".equals(carYear)||"".equals(carMonth)||"".equals(tvEditlc.getText().toString().trim())){
            ToastUtil.show(this,"请填写完整内容");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("city", cityId);
        map.put("car", carId);
        map.put("useddate", carYear);
        map.put("useddateMonth", carMonth);
        map.put("mileage", tvEditlc.getText().toString().trim());
        map.put("price","");
        OKhttptils.post(this, Config.GETCARASSESS, map, new OKhttptils.HttpCallBack() {
            @Override
            public String success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if ("1".equals(object.getString("status"))){
                        Bundle bundle = new Bundle();
                        bundle.putString("price",response);
                        bundle.putString("name",tvEditbrand.getText().toString().trim());
                        bundle.putString("car","上牌时间:"+tvEdittime.getText().toString().trim()+"   "+tvEditcity.getText().toString().trim()+"   "+tvEditlc.getText().toString().trim()+"万公里");
                        JumpUtil.newInstance().jumpLeft(AccessCar.this,AccessResult.class,bundle);
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
