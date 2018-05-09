package com.xtzhangbinbin.jpq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xtzhangbinbin.jpq.R;
import com.xtzhangbinbin.jpq.base.BaseActivity;
import com.xtzhangbinbin.jpq.config.Config;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.OKhttptils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceActivity extends BaseActivity {

    private static final String TAG = "注册服务协议";
    @BindView(R.id.content)
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra("Url");
        if (url != null) {
            getData(url);
        } else {
            getData(Config.GET_REGISTER_SERVICE);
        }

    }

    private void getData(String url) {
        Log.d(TAG, "getData: "+url);
        OKhttptils.post(ServiceActivity.this,url, new HashMap<String,String>(), new OKhttptils.HttpCallBack() {
            @Override
            public void success(String response) {
                Log.d(TAG, "onResponse获取数据: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String protocol_content = data.getString("protocol_content");
                    content.setText(Html.fromHtml(protocol_content));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String response) {
                Log.d(TAG, "fail: " + response);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle("用户协议");
        setLeftImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JumpUtil.newInstance().finishRightTrans(ServiceActivity.this);
            }
        });
    }
}
