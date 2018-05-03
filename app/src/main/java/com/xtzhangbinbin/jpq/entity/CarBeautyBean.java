package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CarBeautyBean {

    /**
     * data : {"result":[{"prod_oper_date":"2018-04-22 15:31:12","prod_service_name":"水电费水电费","prod_service_type_item":"似懂非懂师德师风","prod_reduced_price":455,"prod_is_publish":"0","prod_price":5555,"comp_id":"31","prod_id":"51"},{"prod_oper_date":"2018-04-23 10:58:53","prod_service_name":"哟哟哟","prod_service_type_item":"汽修保养","prod_reduced_price":699,"prod_is_publish":"0","prod_price":1328.88,"comp_id":"31","prod_id":"60"},{"prod_oper_date":"2018-05-02 14:38:19","prod_service_name":"很方便洗车","prod_service_type_item":"洗车","prod_reduced_price":268,"prod_is_publish":"0","prod_price":735,"comp_id":"31","prod_id":"64"}]}
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
             * prod_oper_date : 2018-04-22 15:31:12
             * prod_service_name : 水电费水电费
             * prod_service_type_item : 似懂非懂师德师风
             * prod_reduced_price : 455.0
             * prod_is_publish : 0
             * prod_price : 5555.0
             * comp_id : 31
             * prod_id : 51
             */

            private String prod_oper_date;
            private String prod_service_name;
            private String prod_service_type_item;
            private double prod_reduced_price;
            private String prod_is_publish;
            private double prod_price;
            private String comp_id;
            private String prod_id;

            public String getProd_oper_date() {
                return prod_oper_date;
            }

            public void setProd_oper_date(String prod_oper_date) {
                this.prod_oper_date = prod_oper_date;
            }

            public String getProd_service_name() {
                return prod_service_name;
            }

            public void setProd_service_name(String prod_service_name) {
                this.prod_service_name = prod_service_name;
            }

            public String getProd_service_type_item() {
                return prod_service_type_item;
            }

            public void setProd_service_type_item(String prod_service_type_item) {
                this.prod_service_type_item = prod_service_type_item;
            }

            public double getProd_reduced_price() {
                return prod_reduced_price;
            }

            public void setProd_reduced_price(double prod_reduced_price) {
                this.prod_reduced_price = prod_reduced_price;
            }

            public String getProd_is_publish() {
                return prod_is_publish;
            }

            public void setProd_is_publish(String prod_is_publish) {
                this.prod_is_publish = prod_is_publish;
            }

            public double getProd_price() {
                return prod_price;
            }

            public void setProd_price(double prod_price) {
                this.prod_price = prod_price;
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
