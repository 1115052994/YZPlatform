package com.plt.yzplatform.utils;

import android.util.Log;

import com.plt.yzplatform.config.Config;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public class LogUtil {
    public static void w(Object str){
        Log.w(Config.APP_NAME, str.toString());
    }

    public static void e(Object str){
        Log.e(Config.APP_NAME, str.toString());
    }

    public static void d(Object str){
        Log.d(Config.APP_NAME, str.toString());
    }

    public static void i(Object str){
        Log.i(Config.APP_NAME, str.toString());
    }

    public static void v(Object str){
        Log.v(Config.APP_NAME, str.toString());
    }
}
