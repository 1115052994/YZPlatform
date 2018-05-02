package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public class Orders {
    /**
     * data : {"result":[{"order_date":"2018-04-27 16:33:03","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"dc4a58cde1aa47f6a97354dd0a27c0cd"},{"order_date":"2018-04-27 16:02:00","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"63dbb2360eb9485a9a03e8e468050516"},{"order_date":"2018-04-27 15:00:06","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"c626f179b32c4a1e8cc0482f8fadd840"},{"order_date":"2018-04-27 14:48:46","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"aa7ff3963bc64269856f860d23edf814"},{"order_date":"2018-04-27 14:48:13","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"479729b09e104baa983807c8011637c3"}],"pageCount":6,"pageTotal":27}
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
         * result : [{"order_date":"2018-04-27 16:33:03","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"dc4a58cde1aa47f6a97354dd0a27c0cd"},{"order_date":"2018-04-27 16:02:00","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"63dbb2360eb9485a9a03e8e468050516"},{"order_date":"2018-04-27 15:00:06","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"c626f179b32c4a1e8cc0482f8fadd840"},{"order_date":"2018-04-27 14:48:46","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"aa7ff3963bc64269856f860d23edf814"},{"order_date":"2018-04-27 14:48:13","prod_service_name":"汽车美容","auth_comp_name":"测试服务商名称","order_state_pers":"yzfwsy","prod_price":1254,"order_price":1254,"order_id":"479729b09e104baa983807c8011637c3"}]
         * pageCount : 6
         * pageTotal : 27
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
             * order_date : 2018-04-27 16:33:03
             * prod_service_name : 汽车美容
             * auth_comp_name : 测试服务商名称
             * order_state_pers : yzfwsy
             * prod_price : 1254.0
             * order_price : 1254.0
             * order_id : dc4a58cde1aa47f6a97354dd0a27c0cd
             */

            private String order_date;
            private String prod_service_name;
            private String auth_comp_name;
            private String order_state_pers;
            private double prod_price;
            private double order_price;
            private String order_id;

            public String getOrder_date() {
                return order_date;
            }

            public void setOrder_date(String order_date) {
                this.order_date = order_date;
            }

            public String getProd_service_name() {
                return prod_service_name;
            }

            public void setProd_service_name(String prod_service_name) {
                this.prod_service_name = prod_service_name;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public String getOrder_state_pers() {
                return order_state_pers;
            }

            public void setOrder_state_pers(String order_state_pers) {
                this.order_state_pers = order_state_pers;
            }

            public double getProd_price() {
                return prod_price;
            }

            public void setProd_price(double prod_price) {
                this.prod_price = prod_price;
            }

            public double getOrder_price() {
                return order_price;
            }

            public void setOrder_price(double order_price) {
                this.order_price = order_price;
            }

            public String getOrder_id() {
                return order_id;
            }

            public void setOrder_id(String order_id) {
                this.order_id = order_id;
            }
        }
    }
}
