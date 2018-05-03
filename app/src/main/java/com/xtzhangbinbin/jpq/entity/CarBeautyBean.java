package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class CarBeautyBean {


    /**
     * data : {"result":[{"prod_service_name":"很方便洗车","prod_reduced_price":268,"prod_is_publish":"0","prod_price":735,"unused":1,"consume":0,"prod_id":"64","ordersum":3},{"prod_service_name":"哟哟哟","prod_reduced_price":699,"prod_is_publish":"0","prod_price":1328.88,"unused":0,"consume":0,"prod_id":"60","ordersum":0},{"prod_service_name":"水电费水电费","prod_reduced_price":455,"prod_is_publish":"0","prod_price":5555,"unused":0,"consume":0,"prod_id":"51","ordersum":0}],"pageCount":1,"pageTotal":3}
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
         * result : [{"prod_service_name":"很方便洗车","prod_reduced_price":268,"prod_is_publish":"0","prod_price":735,"unused":1,"consume":0,"prod_id":"64","ordersum":3},{"prod_service_name":"哟哟哟","prod_reduced_price":699,"prod_is_publish":"0","prod_price":1328.88,"unused":0,"consume":0,"prod_id":"60","ordersum":0},{"prod_service_name":"水电费水电费","prod_reduced_price":455,"prod_is_publish":"0","prod_price":5555,"unused":0,"consume":0,"prod_id":"51","ordersum":0}]
         * pageCount : 1
         * pageTotal : 3
         */

        private int pageCount;
        private int pageTotal;
        private List<ResultBean> result;

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(int pageTotal) {
            this.pageTotal = pageTotal;
        }

        public List<ResultBean> getResult() {
            return result;
        }

        public void setResult(List<ResultBean> result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * prod_service_name : 很方便洗车
             * prod_reduced_price : 268.0
             * prod_is_publish : 0
             * prod_price : 735.0
             * unused : 1
             * consume : 0
             * prod_id : 64
             * ordersum : 3
             */

            private String prod_service_name;
            private double prod_reduced_price;
            private String prod_is_publish;
            private double prod_price;
            private int unused;
            private int consume;
            private String prod_id;
            private int ordersum;

            public String getProd_service_name() {
                return prod_service_name;
            }

            public void setProd_service_name(String prod_service_name) {
                this.prod_service_name = prod_service_name;
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

            public int getUnused() {
                return unused;
            }

            public void setUnused(int unused) {
                this.unused = unused;
            }

            public int getConsume() {
                return consume;
            }

            public void setConsume(int consume) {
                this.consume = consume;
            }

            public String getProd_id() {
                return prod_id;
            }

            public void setProd_id(String prod_id) {
                this.prod_id = prod_id;
            }

            public int getOrdersum() {
                return ordersum;
            }

            public void setOrdersum(int ordersum) {
                this.ordersum = ordersum;
            }
        }
    }
}
