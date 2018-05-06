package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.MyCountDownTimer;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeNewPhoneActivity extends BaseActivity {

    @BindView(R.id.mPhone)
    EditText mPhone;
    @BindView(R.id.mCode)
    EditText mCode;
    @BindView(R.id.mGetCode)
    Button mGetCode;

    private MyCountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_new_phone);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mGetCode, R.id.mLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mGetCode:
                getCode();
                break;
            case R.id.mLogin:
                login();
                break;
        }
    }

    /* 登录 */
    private void login() {
        String phone = mPhone.getText().toString().trim();
        String code = mCode.getText().toString().trim();
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("token_code",code);
        OKhttptils.post(ChangeNewPhoneActivity.this, Config.CHECK_NEW, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Prefs.with(getApplicationContext()).clear();
                JumpUtil.newInstance().jumpRight(ChangeNewPhoneActivity.this,LoginActivity.class);
            }

            @Override
            public void fail(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    ToastUtil.show(ChangeNewPhoneActivity.this,jsonObject.getString("message"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        });
    }

    /* 获取验证码 */
    private void getCode() {
        String phone = mPhone.getText().toString().trim();
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            countDownTimer = new MyCountDownTimer(mGetCode, 60000, 1000);
            countDownTimer.start();
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            OKhttptils.post(ChangeNewPhoneActivity.this, Config.GETCODE, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {

                }

                @Override
                public void fail(String response) {
//                    Log.d(TAG, "fail获取验证码: " + response);
//                    try {
//
//                        JSONObject jsonObject = new JSONObject(response);
//                        ToastUtil.show(LoginActivity.this,jsonObject.getString("message"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });

        } else {
            ToastUtil.show(ChangeNewPhoneActivity.this, "手机号错误");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("更换手机号");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(ChangeNewPhoneActivity.this);
            }
        });
    }
}
