package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/2 0002.
 */

public class OrdersCompInfo {

    /**
     * data : {"result":[{"order_date":"2018-05-02 15:26:37","prod_service_name":"很方便洗车","order_state_comp":"wxf","prod_service_type_item":"YZcompcfwlxxcxc","prod_price":735,"pers_head_file_id":"mmmmmm14","order_price":268,"order_id":"4a6c35605cb747db9fe78bc93c7f92ac","pers_phone":"17685416555"},{"order_date":"2018-04-30 14:58:38","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"5b7574aa1b9d4462b5faedfc3205a2dd","pers_phone":"17685416555"},{"order_date":"2018-04-30 15:00:27","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"92a00c10a8444bfea1abf5b95b8f9a16","pers_phone":"17685416555"},{"order_date":"2018-04-30 14:59:44","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"b0eec5070d27438d84915a6c8b289667","pers_phone":"17685416555"},{"order_date":"2018-04-30 14:56:46","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"bd5e29871a27443a84955b2add3450d4","pers_phone":"17685416555"},{"order_date":"2018-05-02 15:26:32","prod_service_name":"很方便洗车","order_state_comp":"qxdd","prod_service_type_item":"YZcompcfwlxxcxc","prod_price":735,"pers_head_file_id":"mmmmmm14","order_price":268,"order_id":"dcf45853d8ed43f7a597976db3a75e4b","pers_phone":"17685416555"},{"order_date":"2018-04-30 15:00:35","prod_service_name":"汽车美容","order_state_comp":"qxdd","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"e6bcba35023b4a0c8550277537d5456d","pers_phone":"17685416555"},{"order_date":"2018-05-02 15:23:20","prod_service_name":"很方便洗车","order_state_comp":"qxdd","prod_service_type_item":"YZcompcfwlxxcxc","prod_price":735,"pers_head_file_id":"mmmmmm14","order_price":268,"order_id":"fe8f174002a94c6b8b7f9a86a4fd08d8","pers_phone":"17685416555"}],"pageCount":1,"pageTotal":8}
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
         * result : [{"order_date":"2018-05-02 15:26:37","prod_service_name":"很方便洗车","order_state_comp":"wxf","prod_service_type_item":"YZcompcfwlxxcxc","prod_price":735,"pers_head_file_id":"mmmmmm14","order_price":268,"order_id":"4a6c35605cb747db9fe78bc93c7f92ac","pers_phone":"17685416555"},{"order_date":"2018-04-30 14:58:38","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"5b7574aa1b9d4462b5faedfc3205a2dd","pers_phone":"17685416555"},{"order_date":"2018-04-30 15:00:27","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"92a00c10a8444bfea1abf5b95b8f9a16","pers_phone":"17685416555"},{"order_date":"2018-04-30 14:59:44","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"b0eec5070d27438d84915a6c8b289667","pers_phone":"17685416555"},{"order_date":"2018-04-30 14:56:46","prod_service_name":"汽车美容","order_state_comp":"wxf","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"bd5e29871a27443a84955b2add3450d4","pers_phone":"17685416555"},{"order_date":"2018-05-02 15:26:32","prod_service_name":"很方便洗车","order_state_comp":"qxdd","prod_service_type_item":"YZcompcfwlxxcxc","prod_price":735,"pers_head_file_id":"mmmmmm14","order_price":268,"order_id":"dcf45853d8ed43f7a597976db3a75e4b","pers_phone":"17685416555"},{"order_date":"2018-04-30 15:00:35","prod_service_name":"汽车美容","order_state_comp":"qxdd","prod_service_type_item":"美容","prod_price":1254,"pers_head_file_id":"mmmmmm14","order_price":655,"order_id":"e6bcba35023b4a0c8550277537d5456d","pers_phone":"17685416555"},{"order_date":"2018-05-02 15:23:20","prod_service_name":"很方便洗车","order_state_comp":"qxdd","prod_service_type_item":"YZcompcfwlxxcxc","prod_price":735,"pers_head_file_id":"mmmmmm14","order_price":268,"order_id":"fe8f174002a94c6b8b7f9a86a4fd08d8","pers_phone":"17685416555"}]
         * pageCount : 1
         * pageTotal : 8
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
             * order_date : 2018-05-02 15:26:37
             * prod_service_name : 很方便洗车
             * order_state_comp : wxf
             * prod_service_type_item : YZcompcfwlxxcxc
             * prod_price : 735.0
             * pers_head_file_id : mmmmmm14
             * order_price : 268.0
             * order_id : 4a6c35605cb747db9fe78bc93c7f92ac
             * pers_phone : 17685416555
             */

            private String order_date;
            private String prod_service_name;
            private String order_state_comp;
            private String prod_service_type_item;
            private double prod_price;
            private String pers_head_file_id;
            private double order_price;
            private String order_id;
            private String pers_phone;

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

            public String getOrder_state_comp() {
                return order_state_comp;
            }

            public void setOrder_state_comp(String order_state_comp) {
                this.order_state_comp = order_state_comp;
            }

            public String getProd_service_type_item() {
                return prod_service_type_item;
            }

            public void setProd_service_type_item(String prod_service_type_item) {
                this.prod_service_type_item = prod_service_type_item;
            }

            public double getProd_price() {
                return prod_price;
            }

            public void setProd_price(double prod_price) {
                this.prod_price = prod_price;
            }

            public String getPers_head_file_id() {
                return pers_head_file_id;
            }

            public void setPers_head_file_id(String pers_head_file_id) {
                this.pers_head_file_id = pers_head_file_id;
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

            public String getPers_phone() {
                return pers_phone;
            }

            public void setPers_phone(String pers_phone) {
                this.pers_phone = pers_phone;
            }
        }
    }
}
