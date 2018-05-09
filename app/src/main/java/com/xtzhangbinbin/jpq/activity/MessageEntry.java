package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.mm.opensdk.utils.Log;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class MessageEntry extends BaseActivity{

    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.phone2)
    EditText phone2;
    @BindView(R.id.idcard)
    EditText idcard;
    @BindView(R.id.baocun)
    Button baocun;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.zx)
    TextView zx;
    private String auth_comp_city;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_entry);
        ButterKnife.bind(this);
        //initLoc();
        // 城市
        String city = Prefs.with(this).read("city");
        if (city!=null&&!"".equals(city)){
            //mLocation.setText(city);
            // 获取城市ID
            String cityid = Prefs.with(this).read("cityId");
            //lat = Double.parseDouble(Prefs.with(this).read("lat"));
            //lon = Double.parseDouble(Prefs.with(this).read("lon"));
            if (cityid!=null&&!"".equals(cityid)) {
                auth_comp_city = cityid;
            }else{
                getCityId(city);
            }
        }

        Intent intent = getIntent();
        final String ZX_dict_id = intent.getStringExtra("dict_id");
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()==true) {
                    baocun.setEnabled(true);
                    baocun.setBackground(getResources().getDrawable(R.drawable.button_baocun));
                }else {
                    baocun.setEnabled(false);
                    baocun.setBackground(getResources().getDrawable(R.drawable.button_baocun2));
                }
            }
        });
        baocun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!name.getText().toString().isEmpty() && !phone.getText().toString().isEmpty() && !phone2.getText().toString().isEmpty() && !idcard.getText().toString().isEmpty()) {
                    if (auth_comp_city != null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
                            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                            String time = formatter.format(curDate);//获取当前时间
                            getData(ZX_dict_id,name.getText().toString(),phone.getText().toString(),phone2.getText().toString(),idcard.getText().toString(),auth_comp_city,time);
                            baocun.setBackground(getResources().getDrawable(R.drawable.button_baocun));
                    }else {
                        final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(MessageEntry.this).setMessage1("温馨提示").setMessage2("  当前城市获取失败").setConfirm("确定").setCancel("取消").showDialog();
                        ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                            @Override
                            public void onNoClick() {
                                ordinaryDialog.dismiss();
                            }
                        });
                        ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                            @Override
                            public void onYesClick() {
                                ordinaryDialog.dismiss();
                            }
                        });
                    }
                } else {
                    final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(MessageEntry.this).setMessage1("温馨提示").setMessage2("  请完善信息").setConfirm("确定").setCancel("取消").showDialog();
                    ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                        @Override
                        public void onNoClick() {
                            ordinaryDialog.dismiss();
                        }
                    });
                    ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                        @Override
                        public void onYesClick() {
                            ordinaryDialog.dismiss();
                        }
                    });
                }
            }
        });


    }

    //数据请求
    private void getData(String ZX_dict_id, String zx_name, String zx_phone, String zx_tel, String zx_card_no, String zx_city, String zx_create_date) {
        Map<String, String> map = new HashMap<>();
        map.put("ZX_dict_id", ZX_dict_id);
        map.put("zx_name", zx_name);
        map.put("zx_phone", zx_phone);
        map.put("zx_tel", zx_tel);
        map.put("zx_card_no", zx_card_no);
        map.put("zx_city", zx_city);
        map.put("zx_create_date", zx_create_date);
        OKhttptils.post(MessageEntry.this, Config.INSERTINFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d("aaaaa", "onResponse获取数据: " + response);
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(MessageEntry.this).setMessage1("温馨提示").setMessage2("  您的信息已经提交成功!").showDialog();
                ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ordinaryDialog.dismiss();
                    }
                });
                ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        finish();
                        ordinaryDialog.dismiss();
                    }
                });

            }

            @Override
            public void fail(String response) {
                Log.d("aaaa", "fail: " + response);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        setTitle("信息录入");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(MessageEntry.this);
            }
        });
    }

    //通过城市名获取城市Id
    public void getCityId(String cityName) {
        Map<String, String> map = new HashMap<>();
        map.put("cityName", cityName);
        OKhttptils.post(this, Config.GET_CITY_ID, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject result = data.getJSONObject("result");
                    auth_comp_city = result.getString("city_id");
                    /**
                     * 存储城市Id
                     */
                    Prefs.with(MessageEntry.this).remove("cityId");
                    Prefs.with(MessageEntry.this).write("cityId",auth_comp_city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {

            }
        });
    }
}
