package com.xtzhangbinbin.jpq.entity;

/**
 * Created by Administrator on 2018/5/7.
 */

public class EventBasBean {
    private String indexFragment;
    private String indexBotton;

    public EventBasBean(String indexFragment, String indexBotton) {
        this.indexFragment = indexFragment;
        this.indexBotton = indexBotton;
    }

    public String getIndexFragment() {
        return indexFragment;
    }

    public String getIndexBotton() {
        return indexBotton;
    }
}
