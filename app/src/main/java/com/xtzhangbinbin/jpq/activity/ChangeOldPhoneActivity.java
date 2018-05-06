package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.MyCountDownTimer;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/* 更改手机号 - 原手机号 */
public class ChangeOldPhoneActivity extends BaseActivity {

    private static final String TAG = "更换旧手机号" ;
    @BindView(R.id.mPhone)
    EditText mPhone;
    @BindView(R.id.mCode)
    EditText mCode;
    @BindView(R.id.mGetCode)
    Button mGetCode;

    private MyCountDownTimer countDownTimer;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_old_phone);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.mGetCode, R.id.mNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mGetCode:
                getCode();
                break;
            case R.id.mNext:
                next();
                break;
        }
    }

    /* 验证手机号+验证码 */
    private void next() {
        final String phone = mPhone.getText().toString().trim();
        code = mCode.getText().toString().trim();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("token_code", code);
        OKhttptils.post(ChangeOldPhoneActivity.this, Config.CHECK_OLD, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                JumpUtil.newInstance().jumpRight(ChangeOldPhoneActivity.this, ChangeNewPhoneActivity.class);
            }

            @Override
            public void fail(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(ChangeOldPhoneActivity.this, jsonObject.getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* 获取验证码 */
    private void getCode() {
        final String phone = mPhone.getText().toString().trim();
            /* 检验手机号是否是当前登录手机号 */
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            Map<String, String> m = new HashMap<>();
            m.put("phone", phone);
            OKhttptils.post(ChangeOldPhoneActivity.this, Config.CHECK_PHONE, m, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {

                    countDownTimer = new MyCountDownTimer(mGetCode, 60000, 1000);
                    countDownTimer.start();

                    Map<String, String> map = new HashMap<>();
                    map.put("phone", phone);
                    OKhttptils.post(ChangeOldPhoneActivity.this, Config.GETCODE, map, new OKhttptils.HttpCallBack() {
                        @Override
                        public void success(String response) {

                        }

                        @Override
                        public void fail(String response) {

                        }
                    });

                }

                @Override
                public void fail(String response) {
                    Log.d(TAG, "fail旧手机号: " + response);
//                            try {
//                                JSONObject jsonObject = new JSONObject(response);
//                                ToastUtil.show(ChangeOldPhoneActivity.this,jsonObject.getString("message"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                }
            });

        } else {
            ToastUtil.show(ChangeOldPhoneActivity.this, "手机号错误");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("更换手机号");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(ChangeOldPhoneActivity.this);
            }
        });
    }
}
