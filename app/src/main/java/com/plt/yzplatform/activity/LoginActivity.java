package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.MyCountDownTimer;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.StringUtil;
import com.plt.yzplatform.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "登录" ;
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
        ifLogin();
    }

    private void ifLogin() {
        /* 查询本地存储token 如没有，则登录 */
        if (Prefs.with(getApplicationContext()).read("user_token").isEmpty()) {

        } else {
            //自动登录
            JumpUtil.newInstance().jumpRight(this,CityActivity.class);
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
        String phone = loginPhone.getText().toString().trim();
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            if (NetUtil.isNetAvailable(this)) {
                OkHttpUtils.post()
                        .url(Config.LOGIN)
                        .addParams("phone", phone)
                        .addHeader("user_token","")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.noNAR(LoginActivity.this);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.e(TAG, "onResponse: " + response );
                                /**
                                 * {
                                 "message": "账号不存在",
                                 "status": "0"
                                 }
                                 */

                                /**
                                 *{
                                 "data": {
                                 "user_token": "96730A47BBCD8F345203CFAB9A2CA83AFBBA8AAA6CF39FB4C43C77884BCF7698F0F8976573622E870DE2352FD1908EADDDFB735DA5E3A77DE3C6E2520B61D7F6"
                                 },
                                 "message": "",
                                 "status": "1"
                                 }
                                 */
                                try {

                                    JSONObject object = new JSONObject(response);
                                    if ("1".equals(object.getString("status"))){
                                        String data = object.getString("data");
                                        JSONObject obj = new JSONObject(data);
                                        String user_token = obj.getString("user_token");
                                        Prefs.with(getApplicationContext()).write("user_token",user_token);
                                        JumpUtil.newInstance().jumpRight(LoginActivity.this,CityActivity.class);
                                    }else {
                                        ToastUtil.show(LoginActivity.this,object.getString("message"));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                ToastUtil.noNAR(LoginActivity.this);
            }
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
            if (NetUtil.isNetAvailable(this)) {
                OkHttpUtils.post()
                        .url(Config.GETCODE)
                        .addHeader("user_token", "")
                        .addParams("phone", phone)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.noNAR(LoginActivity.this);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Log.d(TAG, "onResponse: " + response);
                                /**
                                 *{
                                 "data": {
                                 "result": {
                                 "code": "242638",
                                 "body": {
                                 "code": "0",
                                 "msg": "SUCCESS",
                                 "smUuid": "13594_1_0_18366135023_1_pQRA1Ya_1"
                                 }
                                 }
                                 },
                                 "message": "",
                                 "status": "1"
                                 }
                                 */
                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    if ("1".equals(jsonObject.getString("status"))) {
                                        String data = jsonObject.getString("data");
                                        JSONObject object = new JSONObject(data);
                                        String result = object.getString("result");
                                        JSONObject o = new JSONObject(result);
                                        code = o.getString("code");
                                        Log.e(TAG, "onResponse验证码: " + code);
                                        ToastUtil.show(LoginActivity.this, "验证码获取成功");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                ToastUtil.noNetAvailable(LoginActivity.this);
            }
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
