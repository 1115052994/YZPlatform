package com.plt.yzplatform.entity;

import android.graphics.Bitmap;

/**
 * Created by glp on 2018/4/19.
 * 描述：
 */

public class Model {
    public String name;
    public Bitmap iconRes;

    public Model() {
    }

    public Model(String name, Bitmap iconRes) {
        this.name = name;
        this.iconRes = iconRes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getIconRes() {
        return iconRes;
    }

    public void setIconRes(Bitmap iconRes) {
        this.iconRes = iconRes;
    }
}
