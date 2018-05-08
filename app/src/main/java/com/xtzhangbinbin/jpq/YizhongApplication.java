package com.xtzhangbinbin.jpq;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;


import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.xtzhangbinbin.jpq.activity.MainActivity;
import com.xtzhangbinbin.jpq.utils.JumpUtil;
import com.xtzhangbinbin.jpq.utils.LogUtil;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.util.Collections;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class YizhongApplication extends Application implements Thread.UncaughtExceptionHandler{
    @Override
    public void onCreate() {
        super.onCreate();
        initXg();
        initPicasso();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }

    public void initXg(){
        //如果打开推送，进行注册
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
//                LogUtil.w("注册成功，设备token为：" + data);
                Prefs.with(getApplicationContext()).write("xinge_token_code", data.toString());
            }
            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.w("注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
        //将信鸽token加入系统缓存
        if(null == Prefs.with(getApplicationContext()).read("xinge_token_code")){
            Prefs.with(getApplicationContext()).write("xinge_token_code", XGPushConfig.getToken(this));
        }

        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setNotificationLargeIcon(R.drawable.icon_default);
        XGPushManager.setPushNotificationBuilder(this, 1, build);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        JumpUtil.newInstance().jumpLeft(this, MainActivity.class);
    }

    /**
     * 初始化Picasso加载https图片
     */
    public void initPicasso(){
        OkHttpClient client = new OkHttpClient().newBuilder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();

        final Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();

        Picasso.setSingletonInstance(picasso);
    }
}
