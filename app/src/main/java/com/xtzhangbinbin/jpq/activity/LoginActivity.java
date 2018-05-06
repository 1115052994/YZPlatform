package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.ActivityUtil;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.MyCountDownTimer;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.StringUtil;
import com.xtzhangbinbin.jpq.utils.ToastUtil;

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

    private String user_type;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 001:
                    getUserInfo();
                    JumpUtil.newInstance().jumpRight(LoginActivity.this, MainActivity.class);
                    break;
            }
        }
    };

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
            JumpUtil.newInstance().jumpRight(this,MainActivity.class);
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
        map.put("token_code", loginCode.getText().toString().trim());
        map.put("xg_token", Prefs.with(getApplicationContext()).read("xinge_token_code"));
        map.put("client_type", "android");
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            if(!loginCode.getText().toString().trim().isEmpty()){
                OKhttptils.post(this, Config.LOGIN, map, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
//                        Log.i(TAG, "success: " + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String data = object.getString("data");
                            JSONObject obj = new JSONObject(data);
                            String user_token = obj.getString("user_token");
                            Prefs.with(getApplicationContext()).write("user_token", user_token);
                            Prefs.with(getApplicationContext()).write("user_phone", phone);
                            Message message = new Message();
                            message.what = 001;
                            handler.sendMessage(message);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void fail(String response) {
                        Log.e(TAG, "fail登录: " + response );
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            ToastUtil.show(LoginActivity.this,jsonObject.getString("message"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
            } else {
                ToastUtil.show(LoginActivity.this, "请填写验证码");
            }
        } else {
            ToastUtil.show(LoginActivity.this, "手机号错误");
        }

    }

    /* 获取用户信息 */
    private void getUserInfo() {
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(LoginActivity.this, Config.GET_USERINFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.e("", "onResponse用户信息: " + response);
                /**
                 * {"data":
                 * {"result":
                 * {"login_name":"17685416552",
                 * "user_type":"comp",
                 * "user_id":"15",
                 * "phone_number":"17685416552",
                 * "user_token":"96730A47BBCD8F345203CFAB9A2CA83A768A3A7A1150AC667DA0330D0B94B04602AACF19E9AB5C98A977CA377CD73B6F8CBAECEE459C76A0EF20E0615C5498E8",
                 * "invi_code":"",
                 * "user_state":"active"}},
                 * "message":"",
                 * "status":"1"}
                 * */
                try {
                    JSONObject object = new JSONObject(response);
                    String data = object.getString("data");
                    JSONObject jsonObject = new JSONObject(data);
                    String result = jsonObject.getString("result");
                    JSONObject obj = new JSONObject(result);
                    user_type = obj.getString("user_type");
                    Prefs.with(getApplicationContext()).write("user_type",user_type);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fail(String response) {
                Log.d(TAG, "fail用户信息: " + response);
//                try {
//                    JSONObject object = new JSONObject(response);
//                    user_type = "";
//                    ToastUtil.show(LoginActivity.this, object.getString("message"));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

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
