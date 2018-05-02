package com.plt.yzplatform.entity;

/**
 * Created by Administrator on 2018/5/1 0001.
 */

public class OrderQRCode {
    /**
     * data : {"result":{"order_comp_prod_id":"58","order_date":"2018-04-30 14:48:46","order_qrcode":"{\"order_pers_id\":\"38\",\"order_comp_user_id\":\"33\",\"order_comp_id\":\"33\",\"qrcode_type\":\"orderpay\",\"order_pers_user_id\":\"38\",\"order_token_code\":\"684152\",\"order_price\":655,\"order_id\":\"eb829b8507094dbab6fec4f8edfd4b55\"}","prod_service_name":"汽车美容","order_token_code":"684152","auth_comp_name":"测试服务商名称","order_price":655}}
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
         * result : {"order_comp_prod_id":"58","order_date":"2018-04-30 14:48:46","order_qrcode":"{\"order_pers_id\":\"38\",\"order_comp_user_id\":\"33\",\"order_comp_id\":\"33\",\"qrcode_type\":\"orderpay\",\"order_pers_user_id\":\"38\",\"order_token_code\":\"684152\",\"order_price\":655,\"order_id\":\"eb829b8507094dbab6fec4f8edfd4b55\"}","prod_service_name":"汽车美容","order_token_code":"684152","auth_comp_name":"测试服务商名称","order_price":655}
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
             * order_date : 2018-04-30 14:48:46
             * order_qrcode : {"order_pers_id":"38","order_comp_user_id":"33","order_comp_id":"33","qrcode_type":"orderpay","order_pers_user_id":"38","order_token_code":"684152","order_price":655,"order_id":"eb829b8507094dbab6fec4f8edfd4b55"}
             * prod_service_name : 汽车美容
             * order_token_code : 684152
             * auth_comp_name : 测试服务商名称
             * order_price : 655
             */

            private String order_comp_prod_id;
            private String order_date;
            private String order_qrcode;
            private String prod_service_name;
            private String order_token_code;
            private String auth_comp_name;
            private int order_price;

            public String getOrder_comp_prod_id() {
                return order_comp_prod_id;
            }

            public void setOrder_comp_prod_id(String order_comp_prod_id) {
                this.order_comp_prod_id = order_comp_prod_id;
            }

            public String getOrder_date() {
                return order_date;
            }

            public void setOrder_date(String order_date) {
                this.order_date = order_date;
            }

            public String getOrder_qrcode() {
                return order_qrcode;
            }

            public void setOrder_qrcode(String order_qrcode) {
                this.order_qrcode = order_qrcode;
            }

            public String getProd_service_name() {
                return prod_service_name;
            }

            public void setProd_service_name(String prod_service_name) {
                this.prod_service_name = prod_service_name;
            }

            public String getOrder_token_code() {
                return order_token_code;
            }

            public void setOrder_token_code(String order_token_code) {
                this.order_token_code = order_token_code;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public int getOrder_price() {
                return order_price;
            }

            public void setOrder_price(int order_price) {
                this.order_price = order_price;
            }
        }
    }
}
