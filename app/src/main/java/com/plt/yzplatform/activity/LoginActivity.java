package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.MyCountDownTimer;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.StringUtil;
import com.plt.yzplatform.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "登录";
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_code)
    EditText loginCode;
    @BindView(R.id.login_getCode)
    Button loginGetCode;
    private MyCountDownTimer countDownTimer;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        ifLogin();
    }

    private void ifLogin() {
        /* 查询本地存储token 如没有，则登录 */
        if (Prefs.with(getApplicationContext()).read("user_token").isEmpty()) {

        } else {
            //自动登录
            JumpUtil.newInstance().jumpRight(this, MainActivity.class);
        }
    }

    @OnClick({R.id.login_getCode, R.id.login_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_getCode:
                getCode();
                break;
            case R.id.login_button:
                login();
                break;
        }
    }

    /* 登录 */
    private void login() {
        final String phone = loginPhone.getText().toString().trim();
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            OKhttptils.post(this, Config.LOGIN, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    Log.i(TAG, "success: " + response);
                    try {

                        JSONObject object = new JSONObject(response);
                            String data = object.getString("data");
                            JSONObject obj = new JSONObject(data);
                            String user_token = obj.getString("user_token");
                            Prefs.with(getApplicationContext()).write("user_token", user_token);
                            Prefs.with(getApplicationContext()).write("user_phone", phone);
                            JumpUtil.newInstance().jumpRight(LoginActivity.this, MainActivity.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    Log.e(TAG, "fail: " + response );
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastUtil.show(LoginActivity.this,jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            ToastUtil.show(LoginActivity.this, "手机号错误");
        }

    }

    /* 获取验证码 */
    private void getCode() {
        String phone = loginPhone.getText().toString().trim();
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            countDownTimer = new MyCountDownTimer(loginGetCode, 60000, 1000);
            countDownTimer.start();
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            OKhttptils.post(LoginActivity.this, Config.GETCODE, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("data");
                            JSONObject object = new JSONObject(data);
                            String result = object.getString("result");
                            JSONObject o = new JSONObject(result);
                            code = o.getString("code");
                            Log.e(TAG, "onResponse验证码: " + code);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        ToastUtil.show(LoginActivity.this,jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            ToastUtil.show(LoginActivity.this, "手机号错误");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(LoginActivity.this);
            }
        });
        setTitleVisibility(View.INVISIBLE);
        setRightText("注册");
        setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().jumpRight(LoginActivity.this, RegisterActivity.class);
            }
        });
    }

}
