package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class SearchCarBeanNew {

    /**
     * data : {"result":[{"item_id":"YZescxgescppcxdlxlcnkcxmy_1_50_700","item_name":"大众CC","brand_name":"大众","brand_id":"YZescxgescppcxdlxlcnkcxmy_1"},{"item_id":"YZescxgescppcxdlxlcnkcxmy_1_8_905","item_name":"一汽-大众CC","brand_name":"大众","brand_id":"YZescxgescppcxdlxlcnkcxmy_1"}]}
     * message :
     * status : 1
     */

    private DataBean data;
    private String message;
    private String status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class DataBean {
        private List<ResultBean> result;

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * item_id : YZescxgescppcxdlxlcnkcxmy_1_50_700
             * item_name : 大众CC
             * brand_name : 大众
             * brand_id : YZescxgescppcxdlxlcnkcxmy_1
             */

            private String item_id;
            private String item_name;
            private String brand_name;
            private String brand_id;

            public String getItem_id() {
                return item_id;
            }

            public void setItem_id(String item_id) {
                this.item_id = item_id;
            }

            public String getItem_name() {
                return item_name;
            }

            public void setItem_name(String item_name) {
                this.item_name = item_name;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }
        }
    }
}
