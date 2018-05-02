package com.xtzhangbinbin.jpq.entity;

/**
 * Created by glp on 2018/4/22.
 * 描述：
 */

public class AddServiceTypeList {
    String item_name;
    String item_price;

    public AddServiceTypeList() {
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    @Override
    public String toString() {
        return "{" +
                "\"item_name\""+ "=" +'\"' + item_name + '\"' +
                ",\"item_price\"" + "=" + item_price +
                '}';
    }
}
