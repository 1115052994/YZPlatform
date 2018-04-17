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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
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
            if (signCode.equals(code)) {
                if (phone.equals(Prefs.with(getApplicationContext()).read("验证码手机号"))) {
                    Log.i(TAG, "register推荐码: " + invi_code);
                    if (invi_code.isEmpty()) {
                        //提交注册
                        if (NetUtil.isNetAvailable(this)) {
                            OkHttpUtils.post()
                                    .url(Config.REG)
                                    .addHeader("user_token", "")
                                    .addParams("phone", phone)
                                    .addParams("user_type", user_type)
                                    .addParams("invi_code", invi_code)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            ToastUtil.noNAR(RegisterActivity.this);
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            Log.e(TAG, "onResponse: " + response);
                                            /**
                                             * {"data":{"flag":"true"},"message":"","status":"1"}
                                             */
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                if ("1".equals(jsonObject.getString("status"))) {
                                                    String data = jsonObject.getString("data");
                                                    JSONObject object = new JSONObject(data);
                                                    String flag = object.getString("flag");

                                                    if ("true".equals(flag)) {
                                                        JumpUtil.newInstance().jumpRight(RegisterActivity.this, EnterpriseActivity.class);
                                                    } else {
                                                        ToastUtil.show(RegisterActivity.this, object.getString("message"));
                                                    }

                                                } else {
                                                    ToastUtil.show(RegisterActivity.this, jsonObject.getString("message"));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                        } else {
                            ToastUtil.noNetAvailable(RegisterActivity.this);
                        }


                    } else {
                        //校验邀请码是否正确
                        if (NetUtil.isNetAvailable(this)) {
                            OkHttpUtils.post()
                                    .url(Config.VERIFY_CODE)
                                    .addParams("code", invi_code)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            ToastUtil.noNAR(RegisterActivity.this);
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            Log.d(TAG, "onResponse: " + response);
                                            //如果邀请码错误 则提示  如果邀请码正确 提交注册
                                        }
                                    });
                        } else {
                            ToastUtil.noNetAvailable(this);
                        }
                    }
                } else {
                    ToastUtil.show(RegisterActivity.this, "注册手机号与获取验证码手机号不一致");
                }


            } else {
                ToastUtil.show(RegisterActivity.this, "验证码填写错误");
            }
        } else {
            ToastUtil.show(RegisterActivity.this, "手机号格式不正确");
        }
    }

    /*  获取验证码*/
    private void getCode() {
        String phone = registerPhone.getText().toString().trim();
        Prefs.with(getApplicationContext()).write("验证码手机号", phone);
        if (!phone.isEmpty() && StringUtil.isPhoneNum(phone)) {
            countDownTimer = new MyCountDownTimer(registerGetCode, 60000, 1000);
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
                                ToastUtil.noNAR(RegisterActivity.this);
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
                                        ToastUtil.show(RegisterActivity.this, "验证码获取成功");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                ToastUtil.noNetAvailable(RegisterActivity.this);
            }
        } else {
            ToastUtil.show(RegisterActivity.this, "手机号错误");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setRightText("登陆");
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
                JumpUtil.newInstance().jumpRight(RegisterActivity.this,LoginActivity.class);
            }
        });
    }
}
