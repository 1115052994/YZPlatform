package com.xtzhangbinbin.jpq;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;


import com.xtzhangbinbin.jpq.utils.LogUtil;
import com.xtzhangbinbin.jpq.utils.Prefs;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class YizhongApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        initXg();
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
}
