package com.xtzhangbinbin.jpq.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.entity.PersonalSetting;
import com.xtzhangbinbin.jpq.gson.factory.GsonFactory;
import com.xtzhangbinbin.jpq.utils.DataCleanManager;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.xtzhangbinbin.jpq.utils.ToastUtil;
import com.xtzhangbinbin.jpq.view.CircleImageView;
import com.xtzhangbinbin.jpq.view.OrdinaryDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        Map<String, String> map = new HashMap<>();
        OKhttptils.post(PersonalSettingActivity.this, Config.GET_PERS_INFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "success个人设置: " + response);
                Gson gson = GsonFactory.create();
                PersonalSetting setting = gson.fromJson(response, PersonalSetting.class);
                PersonalSetting.DataBean.ResultBean resultBean = setting.getData().getResult();
                pers_id = resultBean.getPers_id();
                head_file_id = resultBean.getPers_head_file_id();
                if (head_file_id == "") {

                } else {
                    OKhttptils.getPicByHttp(PersonalSettingActivity.this, head_file_id, icon);
                }
                String sSex = resultBean.getPers_sex();
                if (sSex.equals(boy.getText().toString())) {
                    boy.setChecked(true);
                } else {
                    girl.setChecked(true);
                }

                nick.setText(resultBean.getPers_nickname());
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(PersonalSettingActivity.this);
            }
        });
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
        Log.w(TAG, "upLoading头像id！！！: " + head_file_id);
        Log.i(TAG, "upLoading昵称！！！: " + nick.getText().toString().trim());
        Log.e(TAG, "upLoading性别！！！: " + sType);
        Map<String, String> map = new HashMap<>();
        map.put("pers_id", pers_id);
        map.put("pers_head_file_id", head_file_id);
        map.put("pers_nickname", nick.getText().toString().trim());
        map.put("pers_sex", sType);
        OKhttptils.post(PersonalSettingActivity.this, Config.UPDATE_PERS_INFO, map, new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    ToastUtil.show(PersonalSettingActivity.this, "保存成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                ToastUtil.noNAR(PersonalSettingActivity.this);
            }
        });
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
