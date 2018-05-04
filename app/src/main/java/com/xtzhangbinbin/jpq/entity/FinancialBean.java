package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class FinancialBean {

    /**
     * data : {"result":[{"jr_visit_count":10029,"jr_turn_url":"https://www.czbx18.com/cbwMobile/","jr_img_url":"bac01fe4f68246c498df01524da3567a","jr_tags_cn":"金额大,利息低,效率高","jr_id":"28","jr_name":"车险","jr_tags":"jed,lxd,xlg","jr_desc":"全面保障您的爱车和出行安全"},{"jr_visit_count":10027,"jr_turn_url":" https://b.pingan.com.cn/station/","jr_img_url":"da32d7eb30774eb6ac1e2c76e2a3d188","jr_tags_cn":"金额大,效率高","jr_id":"31","jr_name":"平安银行新一贷","jr_tags":"jed,xlg","jr_desc":"最高可贷50万，有房贷·有保单·有公积金账户即可申请"},{"jr_visit_count":10027,"jr_turn_url":"https://www.czbx18.com/cbwMobile/","jr_img_url":"5433b7e1474c43439688ad19b2af8dc7","jr_tags_cn":"金额大","jr_id":"32","jr_name":"保险服务","jr_tags":"jed","jr_desc":"平安保险综合超市，只有你想不到的，没有保不到的"},{"jr_visit_count":10027,"jr_turn_url":"https://creditcard.cmbc.com.cn/wsv2","jr_img_url":"20f62f03df7c4fada7cff32d04dc3fd1","jr_tags_cn":"利息低","jr_id":"34","jr_name":"车主卡","jr_tags":"lxd","jr_desc":"车主卡信用卡是银行面向有车一族推出的信用卡产品"}]}
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
             * jr_visit_count : 10029
             * jr_turn_url : https://www.czbx18.com/cbwMobile/
             * jr_img_url : bac01fe4f68246c498df01524da3567a
             * jr_tags_cn : 金额大,利息低,效率高
             * jr_id : 28
             * jr_name : 车险
             * jr_tags : jed,lxd,xlg
             * jr_desc : 全面保障您的爱车和出行安全
             */

            private int jr_visit_count;
            private String jr_turn_url;
            private String jr_img_url;
            private String jr_tags_cn;
            private String jr_id;
            private String jr_name;
            private String jr_tags;
            private String jr_desc;

            public int getJr_visit_count() {
                return jr_visit_count;
            }

            public void setJr_visit_count(int jr_visit_count) {
                this.jr_visit_count = jr_visit_count;
            }

            public String getJr_turn_url() {
                return jr_turn_url;
            }

            public void setJr_turn_url(String jr_turn_url) {
                this.jr_turn_url = jr_turn_url;
            }

            public String getJr_img_url() {
                return jr_img_url;
            }

            public void setJr_img_url(String jr_img_url) {
                this.jr_img_url = jr_img_url;
            }

            public String getJr_tags_cn() {
                return jr_tags_cn;
            }

            public void setJr_tags_cn(String jr_tags_cn) {
                this.jr_tags_cn = jr_tags_cn;
            }

            public String getJr_id() {
                return jr_id;
            }

            public void setJr_id(String jr_id) {
                this.jr_id = jr_id;
            }

            public String getJr_name() {
                return jr_name;
            }

            public void setJr_name(String jr_name) {
                this.jr_name = jr_name;
            }

            public String getJr_tags() {
                return jr_tags;
            }

            public void setJr_tags(String jr_tags) {
                this.jr_tags = jr_tags;
            }

            public String getJr_desc() {
                return jr_desc;
            }

            public void setJr_desc(String jr_desc) {
                this.jr_desc = jr_desc;
            }
        }
    }
}
