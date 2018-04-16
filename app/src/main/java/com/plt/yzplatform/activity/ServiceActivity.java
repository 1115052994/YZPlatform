package com.plt.yzplatform.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.plt.yzplatform.R;
import com.plt.yzplatform.base.BaseActivity;
import com.plt.yzplatform.config.Config;
import com.plt.yzplatform.utils.JumpUtil;
import com.plt.yzplatform.utils.NetUtil;
import com.plt.yzplatform.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class ServiceActivity extends BaseActivity {

    private static final String TAG = "注册服务协议" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        getData();
    }

    private void getData() {
        if (NetUtil.isNetAvailable(this)){
            OkHttpUtils.post()
                    .url(Config.GET_REGISTER_SERVICE)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtil.noNAR(ServiceActivity.this);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.d(TAG, "onResponse: " + response);
                        }
                    });
        }else {
            ToastUtil.noNetAvailable(this);
        }
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
