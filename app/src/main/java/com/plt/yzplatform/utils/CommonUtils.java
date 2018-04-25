package com.plt.yzplatform.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.TypedValue;

public class CommonUtils {
    public static int dip2px(Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, context.getResources().getDisplayMetrics());
    }
    public static String getDeviceId(Context context){
        String imei = "";
//        try {
//            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            imei = telephonyManager.getDeviceId();
//        } catch (Exception e) {
//            imei = "";
//        }
        return imei;
    }

    /* 获取状态栏高度 */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
