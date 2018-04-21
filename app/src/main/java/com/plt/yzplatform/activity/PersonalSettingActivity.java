package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.entity.PersonalSetting;
import com.plt.yzplatform.gson.factory.GsonFactory;
import com.plt.yzplatform.utils.DataCleanManager;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.CircleImageView;
import com.plt.yzplatform.view.OrdinaryDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/* 个人设置 */
public class PersonalSettingActivity extends BaseActivity {

    private static final String TAG = "个人设置";
    @BindView(R.id.icon)
    CircleImageView icon;
    @BindView(R.id.boy)
    RadioButton boy;
    @BindView(R.id.girl)
    RadioButton girl;
    @BindView(R.id.group)
    RadioGroup group;
    @BindView(R.id.nick)
    EditText nick;
    @BindView(R.id.clean)
    TextView clean;

    private String head_file_id;
    private String sType;
    private String sNike;
    private String pers_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        ButterKnife.bind(this);
        try {
            String s = DataCleanManager.getTotalCacheSize(this);
            clean.setText("有" + s + "缓存可以清除");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sType = boy.getText().toString().trim();
        /* 获取数据 */
        getData();
        initView();
    }

    /* 监听 */
    private void initView() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.boy:
                        sType = boy.getText().toString().trim();
                        break;
                    case R.id.girl:
                        sType = girl.getText().toString().trim();
                        break;
                }
            }
        });
    }

    /* 获取数据 */
    private void getData() {
        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.GET_PERS_INFO)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(PersonalSettingActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e(TAG, "onResponse: " + response);
                            /**
                             * {"data":{"result":{"user_id":"27","pers_id":"27"}},"message":"","status":"1"}
                             */
                            Gson gson = GsonFactory.create();
                            PersonalSetting setting = gson.fromJson(response,PersonalSetting.class);
                            if ("1".equals(setting.getStatus())){
                                PersonalSetting.DataBean.ResultBean resultBean = setting.getData().getResult();
                                pers_id = resultBean.getPers_id();
                                head_file_id = resultBean.getPers_head_file_id();
                                if (head_file_id == ""){

                                }else {
                                    getPic(PersonalSettingActivity.this,head_file_id,icon);
                                }
                                String sSex = resultBean.getPers_sex();
                                if (sSex.equals(boy.getText().toString())){
                                    boy.setChecked(true);
                                }else {
                                    girl.setChecked(true);
                                }

                                nick.setText(resultBean.getPers_nickname());

                            }else {
                                ToastUtil.show(PersonalSettingActivity.this,setting.getMessage());
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(this);
        }
    }

    @OnClick({R.id.icon, R.id.clean, R.id.save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon:
                upDataPicture(this, icon, null, "用户头像");
                break;
            case R.id.clean:
                final OrdinaryDialog ordinaryDialog = OrdinaryDialog.newInstance(this).setMessage1("清除缓存").setMessage2("清除后图片等多媒体消息需要重新下载查看，确定清除？").showDialog();
                ordinaryDialog.setNoOnclickListener(new OrdinaryDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        ordinaryDialog.dismiss();
                    }
                });
                ordinaryDialog.setYesOnclickListener(new OrdinaryDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        DataCleanManager.clearAllCache(PersonalSettingActivity.this);
                        ordinaryDialog.dismiss();
                        try {
                            String s = DataCleanManager.getTotalCacheSize(PersonalSettingActivity.this);
                            clean.setText("有" + s + "缓存可以清除");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.save:
                upLoading();
                break;
        }
    }

    /* 上传数据 */
    private void upLoading() {
        head_file_id = Prefs.with(getApplicationContext()).read("用户头像");
        Log.d(TAG, "upLoading个人id！！！: " + pers_id);
        Log.w(TAG, "upLoading头像id！！！: " +head_file_id );
        Log.i(TAG, "upLoading昵称！！！: "+nick.getText().toString().trim());
        Log.e(TAG, "upLoading性别！！！: " + sType );
        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.UPDATE_PERS_INFO)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("pers_id", pers_id)
                    .addParams("pers_head_file_id", head_file_id)
                    .addParams("pers_nickname", nick.getText().toString().trim())
                    .addParams("pers_sex", sType)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(PersonalSettingActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.i(TAG, "onResponse: " + response);
                            /**
                             * {"data":{},"message":"","status":"1"}
                             */
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if ("1".equals(jsonObject.getString("status"))){
                                    ToastUtil.show(PersonalSettingActivity.this,"保存成功");
                                }else {
                                    ToastUtil.show(PersonalSettingActivity.this,jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        } else {
            ToastUtil.noNetAvailable(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("个人设置");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(PersonalSettingActivity.this);
            }
        });
    }
}
