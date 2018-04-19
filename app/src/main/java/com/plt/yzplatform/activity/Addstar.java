package com.plt.yzplatform.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.Prefs;
import com.plt.yzplatform.utils.ToastUtil;
import com.plt.yzplatform.view.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class Addstar extends BaseActivity {
    @BindView(R.id.addition_image)
    CircleImageView additionImage;
    @BindView(R.id.addition_ed_name)
    EditText additionEdName;
    @BindView(R.id.addition_ed_introduce)
    EditText additionEdIntroduce;
    @BindView(R.id.addition_layout)
    LinearLayout additionLayout;
    private String file_id;
    private String text = "示例 精通汽车工作原理以及各种故障排查和解决，有较强的... （最多输入150个字）";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_star);
        ButterKnife.bind(this);
        additionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (additionEdName.getText().toString().length() != 0 && additionEdIntroduce.getText().toString().length() != 0) {
                   PostData();
                } else {
                    Toast.makeText(Addstar.this, "不能为空" + additionEdName.getText().toString() + additionEdIntroduce.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //给字体设置大小
        int end = text.length();
        SpannableString textSpan = new SpannableString(text);
        textSpan.setSpan(new AbsoluteSizeSpan(25), 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new AbsoluteSizeSpan(23), 2, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        additionEdIntroduce.setHint(textSpan);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //给头部修改文字
        setTitle("添加明星员工");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(Addstar.this);
            }
        });
    }
    //将相机图片上传到服务器 并且生成图片id
    @OnClick(R.id.addition_image)
    public void onViewClicked() {
        upDataPicture(this,additionImage,null,"员工头像");
        file_id = Prefs.with(getApplicationContext()).read("员工头像");

    }
    //将数据上传到服务器
    private void PostData() {
        if (NetUtil.isNetAvailable(this)) {
            OkHttpUtils.post()
                    .url(Config.SAVESTAR)
                    .addHeader("user_token", Prefs.with(getApplicationContext()).read("user_token"))
                    .addParams("staff_name",additionEdName.getText().toString())
                    .addParams("staff_photo_file_id",file_id)
                    .addParams("staff_reco","2")
                    .addParams("staff_info",additionEdIntroduce.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(Addstar.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.get("status").toString();
                                if(status.equals("1")){
                                    Intent intent = new Intent();
                                    setResult(1, intent);
                                    finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }
    }
}
