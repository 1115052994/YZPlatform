package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class MeishiCompDetail {

    /**
     * data : {"result":[{"zsh_item_name":"金满福","zsh_city":"jinan","zsh_item_sales_price":"99","zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C330%2C717%2C435%3Bw%3D470%3Bq%3D89/sign=a7a7dec14a4a20a425516687ad62b411/9825bc315c6034a8ba6e3e52ce13495408237651.jpg","zsh_item_count":"8","zsh_ext":"","zsh_item_desc":"蛋糕2选1！节假日通用，请至少提前24小时预约，配送自提均可，需预约！","zsh_item_id":"52dca3c9c07a43718b3cf45a6d7d105d","zsh_type":"dg","zsh_eval_count":8,"zsh_item_ext":"","zsh_avg_price":30.5,"zsh_name":"金满福(济钢店)","zsh_addr":"济南市历城区新村中路与工业北路交叉口往南500米","zsh_phone":"13589041234","zsh_level":4.9,"zsh_item_price":"89","zsh_id":"0cef17b7af4e409582dd30936ae9def0"},{"zsh_item_name":"金满福","zsh_city":"jinan","zsh_item_sales_price":"89","zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C330%2C717%2C435%3Bw%3D470%3Bq%3D89/sign=a7a7dec14a4a20a425516687ad62b411/9825bc315c6034a8ba6e3e52ce13495408237651.jpg","zsh_item_count":"86","zsh_ext":"","zsh_item_desc":"10英寸水果蛋糕1份！精选食材，新鲜水果，奶香醇厚，果香四溢！","zsh_item_id":"794cd10b44eb4a1f9a62833a624f96d2","zsh_type":"dg","zsh_eval_count":8,"zsh_item_ext":"","zsh_avg_price":30.5,"zsh_name":"金满福(济钢店)","zsh_addr":"济南市历城区新村中路与工业北路交叉口往南500米","zsh_phone":"13589041234","zsh_level":4.9,"zsh_item_price":"79","zsh_id":"0cef17b7af4e409582dd30936ae9def0"}]}
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
             * zsh_item_name : 金满福
             * zsh_city : jinan
             * zsh_item_sales_price : 99
             * zsh_img : https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C330%2C717%2C435%3Bw%3D470%3Bq%3D89/sign=a7a7dec14a4a20a425516687ad62b411/9825bc315c6034a8ba6e3e52ce13495408237651.jpg
             * zsh_item_count : 8
             * zsh_ext :
             * zsh_item_desc : 蛋糕2选1！节假日通用，请至少提前24小时预约，配送自提均可，需预约！
             * zsh_item_id : 52dca3c9c07a43718b3cf45a6d7d105d
             * zsh_type : dg
             * zsh_eval_count : 8
             * zsh_item_ext :
             * zsh_avg_price : 30.5
             * zsh_name : 金满福(济钢店)
             * zsh_addr : 济南市历城区新村中路与工业北路交叉口往南500米
             * zsh_phone : 13589041234
             * zsh_level : 4.9
             * zsh_item_price : 89
             * zsh_id : 0cef17b7af4e409582dd30936ae9def0
             */

            private String zsh_item_name;
            private String zsh_city;
            private String zsh_item_sales_price;
            private String zsh_img;
            private String zsh_item_count;
            private String zsh_ext;
            private String zsh_item_desc;
            private String zsh_item_id;
            private String zsh_type;
            private int zsh_eval_count;
            private String zsh_item_ext;
            private double zsh_avg_price;
            private String zsh_name;
            private String zsh_addr;
            private String zsh_phone;
            private double zsh_level;
            private String zsh_item_price;
            private String zsh_id;

            public String getZsh_item_name() {
                return zsh_item_name;
            }

            public void setZsh_item_name(String zsh_item_name) {
                this.zsh_item_name = zsh_item_name;
            }

            public String getZsh_city() {
                return zsh_city;
            }

            public void setZsh_city(String zsh_city) {
                this.zsh_city = zsh_city;
            }

            public String getZsh_item_sales_price() {
                return zsh_item_sales_price;
            }

            public void setZsh_item_sales_price(String zsh_item_sales_price) {
                this.zsh_item_sales_price = zsh_item_sales_price;
            }

            public String getZsh_img() {
                return zsh_img;
            }

            public void setZsh_img(String zsh_img) {
                this.zsh_img = zsh_img;
            }

            public String getZsh_item_count() {
                return zsh_item_count;
            }

            public void setZsh_item_count(String zsh_item_count) {
                this.zsh_item_count = zsh_item_count;
            }

            public String getZsh_ext() {
                return zsh_ext;
            }

            public void setZsh_ext(String zsh_ext) {
                this.zsh_ext = zsh_ext;
            }

            public String getZsh_item_desc() {
                return zsh_item_desc;
            }

            public void setZsh_item_desc(String zsh_item_desc) {
                this.zsh_item_desc = zsh_item_desc;
            }

            public String getZsh_item_id() {
                return zsh_item_id;
            }

            public void setZsh_item_id(String zsh_item_id) {
                this.zsh_item_id = zsh_item_id;
            }

            public String getZsh_type() {
                return zsh_type;
            }

            public void setZsh_type(String zsh_type) {
                this.zsh_type = zsh_type;
            }

            public int getZsh_eval_count() {
                return zsh_eval_count;
            }

            public void setZsh_eval_count(int zsh_eval_count) {
                this.zsh_eval_count = zsh_eval_count;
            }

            public String getZsh_item_ext() {
                return zsh_item_ext;
            }

            public void setZsh_item_ext(String zsh_item_ext) {
                this.zsh_item_ext = zsh_item_ext;
            }

            public double getZsh_avg_price() {
                return zsh_avg_price;
            }

            public void setZsh_avg_price(double zsh_avg_price) {
                this.zsh_avg_price = zsh_avg_price;
            }

            public String getZsh_name() {
                return zsh_name;
            }

            public void setZsh_name(String zsh_name) {
                this.zsh_name = zsh_name;
            }

            public String getZsh_addr() {
                return zsh_addr;
            }

            public void setZsh_addr(String zsh_addr) {
                this.zsh_addr = zsh_addr;
            }

            public String getZsh_phone() {
                return zsh_phone;
            }

            public void setZsh_phone(String zsh_phone) {
                this.zsh_phone = zsh_phone;
            }

            public double getZsh_level() {
                return zsh_level;
            }

            public void setZsh_level(double zsh_level) {
                this.zsh_level = zsh_level;
            }

            public String getZsh_item_price() {
                return zsh_item_price;
            }

            public void setZsh_item_price(String zsh_item_price) {
                this.zsh_item_price = zsh_item_price;
            }

            public String getZsh_id() {
                return zsh_id;
            }

            public void setZsh_id(String zsh_id) {
                this.zsh_id = zsh_id;
            }
        }
    }
}
