package com.xtzhangbinbin.jpq.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * 描述: 跳转页面
 */
public class JumpUtil {

    public static JumpUtil instance = null; // 单例模式

    public static JumpUtil newInstance() {
        if (instance == null) {
            instance = new JumpUtil();
        }
        return instance;
    }

    Intent mIntent = new Intent();

    public void jumpRight(Context context, Class<?> cls) {  // 向左走的页面跳转
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    public void jumpRightFlags(Context context, Class<?> cls) {  // 向左走的页面跳转
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    public void jumpRight(Context context, Class<?> cls, boolean b) {
        mIntent.putExtra("boolean", b);
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    public void jumpRight(Context context, Class<?> cls, String s) {
        mIntent.putExtra("string", s);
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }


    public void jumpRight(Context context, Class<?> cls, Bundle bundle) {
        mIntent.putExtras(bundle);
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    // 向左走的页面跳转
    public void jumpRight(Context context, Class<?> cls, int tag) {
        mIntent.setClass(context, cls);
        ((Activity) context).startActivityForResult(mIntent, tag);
    }

    public void jumpRight(Context context, Class<?> cls, int tag, String s) {
        mIntent.setClass(context, cls);
        mIntent.putExtra("string", s);
        ((Activity) context).startActivityForResult(mIntent, tag);
    }

    public void jumpRight(Context context, Class<?> cls, int tag, Bundle bundle) {
        mIntent.setClass(context, cls);
        mIntent.putExtras(bundle);
        ((Activity) context).startActivityForResult(mIntent, tag);
    }



    // 向右走的页面跳转
    public void jumpLeft(Context context, Class<?> cls) {
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    public void jumpLeft(Context context, Class<?> cls, boolean b) {
        mIntent.putExtra("boolean", b);
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    public void jumpLeft(Context context, Class<?> cls, String s) {
        mIntent.putExtra("String", s);
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    public void jumpLeft(Context context, Class<?> cls, Bundle bundle) {
        mIntent.putExtras(bundle);
        mIntent.setClass(context, cls);
        context.startActivity(mIntent);
    }

    // finish
    public void finishRightTrans(Context context) {
        ((Activity) context).finish();
    }

    // finish
    public void finishRightTrans(Context context, Bundle bundle, int tag) {
        mIntent.putExtras(bundle);
        ((Activity) context).setResult(tag, mIntent);
        ((Activity) context).finish();
    }

    public void finishLeftTrans(Context context) {
        ((Activity) context).finish();
    }
}
