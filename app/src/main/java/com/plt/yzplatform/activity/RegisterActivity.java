package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.ActivityUtil;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.LogUtil;
import com.plt.yzplatform.utils.MyCountDownTimer;
import com.plt.yzplatform.utils.OKhttptils;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.StringUtil;
import com.plt.yzplatform.utils.ToastUtil;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "注册";
    @BindView(R.id.register_phone)
    EditText registerPhone;
    @BindView(R.id.register_code)
    EditText registerCode;
    @BindView(R.id.register_getCode)
    Button registerGetCode;
    @BindView(R.id.invite_code)
    EditText inviteCode;
    @BindView(R.id.personal)
    RadioButton personal;
    @BindView(R.id.enterprise)
    RadioButton enterprise;
    @BindView(R.id.user_Type)
    RadioGroup userType;
    private MyCountDownTimer countDownTimer;
    private String code;
    private String user_type = "pers";
    private String xg_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ActivityUtil.addActivity(this);
        initListener();
    }


    private void initListener() {
        userType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.personal:
                        user_type = "pers";
                        break;
                    case R.id.enterprise:
                        user_type = "comp";
                        break;
                }
            }
        });
    }

    @OnClick({R.id.register_getCode, R.id.register, R.id.service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_getCode:
                getCode();
                break;
            case R.id.register:
                register();
                break;
            case R.id.service:
                service();
                break;
        }
    }

    /* 查看服务协议 */
    private void service() {
        JumpUtil.newInstance().jumpRight(this, ServiceActivity.class);
    }

    /* 注册 */
    private void register() {
        Log.d(TAG, "register注册用户类型: " + user_type);
        String phone = registerPhone.getText().toString().trim();
        String signCode = registerCode.getText().toString();
        String invi_code = inviteCode.getText().toString().trim();
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
//            if (signCode.equals(code)) {
//                if (phone.equals(Prefs.with(getApplicationContext()).read("验证码手机号"))) {
            Log.i(TAG, "register推荐码: " + invi_code);
            if (invi_code.isEmpty()) {
                //无邀请码注册
                Map<String, String> map = new HashMap<>();
                map.put("phone", phone);
                map.put("user_type", user_type);
                map.put("invi_code", invi_code);
                map.put("token_code", registerCode.getText().toString());
                map.put("xg_token", Prefs.with(getApplicationContext()).read("xinge_token_code"));
                map.put("client_type", "android");
                register2(map);

            } else {
                //校验邀请码是否正确
                final Map<String, String> map2 = new HashMap<>();
                map2.put("phone", phone);
                map2.put("user_type", user_type);
                map2.put("invi_code", invi_code);
                map2.put("token_code", registerCode.getText().toString());
                map2.put("xg_token", Prefs.with(getApplicationContext()).read("xinge_token_code"));
                map2.put("client_type", "android");
                OKhttptils.post(RegisterActivity.this, Config.VERIFY_CODE, map2, new OKhttptils.HttpCallBack() {
                    @Override
                    public void success(String response) {
                        register2(map2);
                    }

                    @Override
                    public void fail(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ToastUtil.show(RegisterActivity.this, jsonObject.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
//                } else {
//                    ToastUtil.show(RegisterActivity.this, "注册手机号与获取验证码手机号不一致");
//                }
//            } else {
//                ToastUtil.show(RegisterActivity.this, "验证码填写错误");
//            }
        } else {
            ToastUtil.show(RegisterActivity.this, "手机号格式不正确");
        }
    }

    /* 无邀请码注册 */
    private void register2(Map<String, String> m) {
        OKhttptils.post(RegisterActivity.this, Config.REG, m, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(null != jsonObject){
                        Prefs.with(getApplicationContext()).write("user_token", jsonObject.getJSONObject("data").getString("result"));
                        Prefs.with(getApplicationContext()).write("user_phone", registerPhone.getText().toString());
                        JumpUtil.newInstance().jumpRight(RegisterActivity.this, MainActivity.class);
                    }
//                    String data = jsonObject.getString("data");
//                    JSONObject object = new JSONObject(data);
//                    String flag = object.getString("flag");
//                    String user_token = object.getString("user_token");
//                    Prefs.with(getApplicationContext()).write("user_token", jsonObject.getString("data"));
//                    Prefs.with(getApplicationContext()).write("user_phone", registerPhone.getText().toString());
//                    if ("true".equals(flag)) {
//                        login();
//                        JumpUtil.newInstance().jumpRight(RegisterActivity.this, MainActivity.class);
//                    } else {
//                        ToastUtil.show(RegisterActivity.this, object.getString("message"));
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                Log.i(TAG, "fail: " + response);
                /**
                 * {"message":"手机已经注册过！","status":"0"}
                 * */
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    ToastUtil.show(RegisterActivity.this, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                ToastUtil.noNAR(RegisterActivity.this);

            }
        });
    }

    /*  获取验证码*/
    private void getCode() {
        String phone = registerPhone.getText().toString().trim();
        Prefs.with(getApplicationContext()).write("验证码手机号", phone);
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            countDownTimer = new MyCountDownTimer(registerGetCode, 60000, 1000);
            countDownTimer.start();
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            OKhttptils.post(RegisterActivity.this, Config.GETCODE, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    //此处由服务器端校验验证码，所以不再返回验证码信息，无任何返回。
                    //如果再加返回，找小梁，请他吃糖
//                    try {
//                        Log.w("test", "checkcode:" + response);
//                        JSONObject jsonObject = new JSONObject(response);
//                        String data = jsonObject.getString("data");
//                        JSONObject object = new JSONObject(data);
//                        String result = object.getString("result");
//                        JSONObject o = new JSONObject(result);
//                        code = o.getString("code");
//                        Log.e(TAG, "onResponse验证码: " + code);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }

                @Override
                public void fail(String response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response);
//                        ToastUtil.show(RegisterActivity.this, jsonObject.getString("message"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        } else {
            ToastUtil.show(RegisterActivity.this, "手机号错误");
        }
    }

    /* 登录 */
    private void login() {
        String phone = registerPhone.getText().toString().trim();
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            Map<String, String> map = new HashMap<>();
            map.put("phone", phone);
            OKhttptils.post(RegisterActivity.this, Config.LOGIN, map, new OKhttptils.HttpCallBack() {
                @Override
                public void success(String response) {
                    try {

                        JSONObject object = new JSONObject(response);
                        String data = object.getString("data");
                        JSONObject obj = new JSONObject(data);
                        String user_token = obj.getString("user_token");
                        Prefs.with(getApplicationContext()).write("user_token", user_token);
                        JumpUtil.newInstance().jumpRight(RegisterActivity.this, EnterpriseActivity.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void fail(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        ToastUtil.show(RegisterActivity.this, jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            ToastUtil.show(RegisterActivity.this, "手机号错误");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        setRightText("登录");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(RegisterActivity.this);
            }
        });
        setTitleVisibility(View.INVISIBLE);
        setRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().jumpRight(RegisterActivity.this, LoginActivity.class);
            }
        });
    }


}
