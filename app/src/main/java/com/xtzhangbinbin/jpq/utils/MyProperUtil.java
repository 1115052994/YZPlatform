package com.xtzhangbinbin.jpq.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by glp on 2018/4/30.
 * 描述：读取 assets文件夹下的properties格式文件
 */

public class MyProperUtil {
    private static Properties properties;

    public static Properties getProperties(Context context) {
        Properties pros = new Properties();
        try {
            InputStream in = context.getAssets().open("carkeys.properties");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"GBK"));
            pros.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties = pros;
        return properties;
    }

    public static String getValue(Context context,String key){
        Properties pros = new Properties();
        try {
            InputStream in = context.getAssets().open("carkeys.properties");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,"GBK"));
            pros.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        properties = pros;
        return properties.getProperty(key);
    }
}
