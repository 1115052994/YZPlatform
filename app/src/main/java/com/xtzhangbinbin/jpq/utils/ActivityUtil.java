package com.xtzhangbinbin.jpq.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: CoolTone
 * 描述: Activity管理类
 */
public class ActivityUtil {

    public static List<Activity> uis = new ArrayList<>();

    public static void addActivity(Activity activity) {
        uis.add(activity);
    }

    public static void removeActivity(Activity activity) {
        uis.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : uis) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static Activity getTopActivity() {
        if (uis.isEmpty()) {
            return null;
        } else {
            return uis.get(uis.size() - 1);
        }
    }
}
