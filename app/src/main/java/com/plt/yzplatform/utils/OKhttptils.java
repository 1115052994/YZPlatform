package com.plt.yzplatform.utils;

import android.app.Activity;

import com.plt.yzplatform.activity.LoginActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;

public class OKhttptils {
    public static void post(final Activity context, String url, Map<String, String> params, final HttpCallBack callBack){
        if (NetUtil.isNetAvailable(context)) {
            OkHttpUtils.post()
                    .url(url)
                    .addHeader("user_token", Prefs.with(context).read("user_token"))
                    //.addParams("car_name", s)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            //失败
                            callBack.fail(e.getMessage());
                        }

                        @Override
                        public void onResponse(String response, int id) {
//                            Log.d("onResponse", "onResponse" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                switch (status) {
                                    case "9999":
                                        //登陆token过期
                                        ToastUtil.show(context,"Token过期请重新登陆");
                                        JumpUtil.newInstance().jumpLeft(context, LoginActivity.class);
                                        break;
                                    case "1":
                                        //成功
                                        callBack.success(response);
                                        break;
                                    case "0":
                                        //失败
                                        callBack.fail(response);
                                        break;
                                    case "103":
                                        callBack.fail(response);
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtil.noNetAvailable(context);
        }
    }

    public interface HttpCallBack{
        void success(String response);
        void fail(String response);
    }
}
