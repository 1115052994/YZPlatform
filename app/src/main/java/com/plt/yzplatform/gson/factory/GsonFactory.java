package com.plt.yzplatform.gson.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.plt.yzplatform.gson.converter.StringConverter;

/**
 * Created by glp on 2018/3/15.
 * 防止后台返回值字段为空
 */

public class GsonFactory {
    public static Gson create() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(String.class, new StringConverter());
        Gson gson = gb.create();
        return gson;
    }

//    public static void main(String[] args){
//        String json="{'message':'test'}";
//        Gson gson = create();
//        ReleaseLine releaseLine = gson.fromJson(json,ReleaseLine.class);
//        System.out.println(releaseLine);
//    }
}
