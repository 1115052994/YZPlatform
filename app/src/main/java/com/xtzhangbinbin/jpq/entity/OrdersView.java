package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public class OrdersView {

    /**
     * data : {"result":{"order_comp_prod_id":"58","auth_comp_lon":116.993253,"prod_service_name":"汽车美容","order_state_pers":"yzfwsy","auth_comp_name":"测试服务商名称","order_number":"20180430150027877","prod_reduced_price":655,"prod_price":1254,"comp_eval_level":5,"auth_flag":"1","auth_comp_addr":"山东省济南市天桥区汽车厂东路与济泺路十字路口西南角中凡·鲁鼎广场","order_pers_user_id":"38","auth_comp_lat":36.687885,"auth_comp_phone":"13222222222","order_id":"92a00c10a8444bfea1abf5b95b8f9a16","items":[{"item_id":"135","item_price":588,"item_oper_date":"2018-04-23 10:45:07","item_name":"更换电瓶","comp_id":"33","prod_id":"58"},{"item_id":"136","item_price":666,"item_oper_date":"2018-04-23 10:45:07","item_name":"局部抛光","comp_id":"33","prod_id":"58"}],"auth_comp_img_head_file_id":"f26fab7ee7c641fbbd87d09f59bb008b"}}
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
        /**
         * result : {"order_comp_prod_id":"58","auth_comp_lon":116.993253,"prod_service_name":"汽车美容","order_state_pers":"yzfwsy","auth_comp_name":"测试服务商名称","order_number":"20180430150027877","prod_reduced_price":655,"prod_price":1254,"comp_eval_level":5,"auth_flag":"1","auth_comp_addr":"山东省济南市天桥区汽车厂东路与济泺路十字路口西南角中凡·鲁鼎广场","order_pers_user_id":"38","auth_comp_lat":36.687885,"auth_comp_phone":"13222222222","order_id":"92a00c10a8444bfea1abf5b95b8f9a16","items":[{"item_id":"135","item_price":588,"item_oper_date":"2018-04-23 10:45:07","item_name":"更换电瓶","comp_id":"33","prod_id":"58"},{"item_id":"136","item_price":666,"item_oper_date":"2018-04-23 10:45:07","item_name":"局部抛光","comp_id":"33","prod_id":"58"}],"auth_comp_img_head_file_id":"f26fab7ee7c641fbbd87d09f59bb008b"}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * order_comp_prod_id : 58
             * auth_comp_lon : 116.993253
             * prod_service_name : 汽车美容
             * order_state_pers : yzfwsy
             * auth_comp_name : 测试服务商名称
             * order_number : 20180430150027877
             * prod_reduced_price : 655
             * prod_price : 1254
             * comp_eval_level : 5
             * auth_flag : 1
             * auth_comp_addr : 山东省济南市天桥区汽车厂东路与济泺路十字路口西南角中凡·鲁鼎广场
             * order_pers_user_id : 38
             * auth_comp_lat : 36.687885
             * auth_comp_phone : 13222222222
             * order_id : 92a00c10a8444bfea1abf5b95b8f9a16
             * items : [{"item_id":"135","item_price":588,"item_oper_date":"2018-04-23 10:45:07","item_name":"更换电瓶","comp_id":"33","prod_id":"58"},{"item_id":"136","item_price":666,"item_oper_date":"2018-04-23 10:45:07","item_name":"局部抛光","comp_id":"33","prod_id":"58"}]
             * auth_comp_img_head_file_id : f26fab7ee7c641fbbd87d09f59bb008b
             */

            private String order_comp_prod_id;
            private double auth_comp_lon;
            private String prod_service_name;
            private String order_state_pers;
            private String auth_comp_name;
            private String order_number;
            private int prod_reduced_price;
            private int prod_price;
            private int comp_eval_level;
            private String auth_flag;
            private String auth_comp_addr;
            private String order_pers_user_id;
            private double auth_comp_lat;
            private String auth_comp_phone;
            private String order_id;
            private String auth_comp_img_head_file_id;
            private List<ItemsBean> items;

            public String getOrder_comp_prod_id() {
                return order_comp_prod_id;
            }

            public void setOrder_comp_prod_id(String order_comp_prod_id) {
                this.order_comp_prod_id = order_comp_prod_id;
            }

            public double getAuth_comp_lon() {
                return auth_comp_lon;
            }

            public void setAuth_comp_lon(double auth_comp_lon) {
                this.auth_comp_lon = auth_comp_lon;
            }

            public String getProd_service_name() {
                return prod_service_name;
            }

            public void setProd_service_name(String prod_service_name) {
                this.prod_service_name = prod_service_name;
            }

            public String getOrder_state_pers() {
                return order_state_pers;
            }

            public void setOrder_state_pers(String order_state_pers) {
                this.order_state_pers = order_state_pers;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public int getProd_reduced_price() {
                return prod_reduced_price;
            }

            public void setProd_reduced_price(int prod_reduced_price) {
                this.prod_reduced_price = prod_reduced_price;
            }

            public int getProd_price() {
                return prod_price;
            }

            public void setProd_price(int prod_price) {
                this.prod_price = prod_price;
            }

            public int getComp_eval_level() {
                return comp_eval_level;
            }

            public void setComp_eval_level(int comp_eval_level) {
                this.comp_eval_level = comp_eval_level;
            }

            public String getAuth_flag() {
                return auth_flag;
            }

            public void setAuth_flag(String auth_flag) {
                this.auth_flag = auth_flag;
            }

            public String getAuth_comp_addr() {
                return auth_comp_addr;
            }

            public void setAuth_comp_addr(String auth_comp_addr) {
                this.auth_comp_addr = auth_comp_addr;
            }

            public String getOrder_pers_user_id() {
                return order_pers_user_id;
            }

            public void setOrder_pers_user_id(String order_pers_user_id) {
                this.order_pers_user_id = order_pers_user_id;
            }

            public double getAuth_comp_lat() {
                return auth_comp_lat;
            }

            public void setAuth_comp_lat(double auth_comp_lat) {
                this.auth_comp_lat = auth_comp_lat;
            }

            public String getAuth_comp_phone() {
                return auth_comp_phone;
            }

            public void setAuth_comp_phone(String auth_comp_phone) {
                this.auth_comp_phone = auth_comp_phone;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }

            public String getAuth_comp_img_head_file_id() {
                return auth_comp_img_head_file_id;
            }

            public void setAuth_comp_img_head_file_id(String auth_comp_img_head_file_id) {
                this.auth_comp_img_head_file_id = auth_comp_img_head_file_id;
            }

            public List<ItemsBean> getItems() {
                return items;
            }

            public void setItems(List<ItemsBean> items) {
                this.items = items;
            }

            public static class ItemsBean {
                /**
                 * item_id : 135
                 * item_price : 588
                 * item_oper_date : 2018-04-23 10:45:07
                 * item_name : 更换电瓶
                 * comp_id : 33
                 * prod_id : 58
                 */

                private String item_id;
                private int item_price;
                private String item_oper_date;
                private String item_name;
                private String comp_id;
                private String prod_id;

                public String getItem_id() {
                    return item_id;
                }

                public void setItem_id(String item_id) {
                    this.item_id = item_id;
                }

                public int getItem_price() {
                    return item_price;
                }

                public void setItem_price(int item_price) {
                    this.item_price = item_price;
                }

                public String getItem_oper_date() {
                    return item_oper_date;
                }

                public void setItem_oper_date(String item_oper_date) {
                    this.item_oper_date = item_oper_date;
                }

                public String getItem_name() {
                    return item_name;
                }

                public void setItem_name(String item_name) {
                    this.item_name = item_name;
                }

                public String getComp_id() {
                    return comp_id;
                }

                public void setComp_id(String comp_id) {
                    this.comp_id = comp_id;
                }

                public String getProd_id() {
                    return prod_id;
                }

                public void setProd_id(String prod_id) {
                    this.prod_id = prod_id;
                }
            }
        }
    }
}
