package com.plt.yzplatform.entity;

/**
 * Created by glp on 2018/4/17.
 */

public class Enterprise {

    /**
     * data : {"result":{"auth_comp_lon":116.993253,"auth_comp_service_type":"YZcompcfwlxwxby, YZcompcfwlxxc, YZcompcfwlxjx","auth_comp_name":"山东派乐特网络科技有限公司","auth_comp_province":"shandong","auth_audit_state":"1","comp_id":"21","auth_comp_addr":"山东省济南市天桥区汽车厂东路与济泺路十字路口西南角中凡·鲁鼎广场","auth_comp_lat":36.687885,"auth_comp_phone":"13288888888","auth_comp_city":"jinan","auth_comp_linkman":"高总","auth_comp_file_id":"f453b93184fa40f58edffafa6766f1c9","auth_comp_img_head_file_id":"8df0e2fea313481d99c8aca460bfe145"}}
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
         * result : {"auth_comp_lon":116.993253,"auth_comp_service_type":"YZcompcfwlxwxby, YZcompcfwlxxc, YZcompcfwlxjx","auth_comp_name":"山东派乐特网络科技有限公司","auth_comp_province":"shandong","auth_audit_state":"1","comp_id":"21","auth_comp_addr":"山东省济南市天桥区汽车厂东路与济泺路十字路口西南角中凡·鲁鼎广场","auth_comp_lat":36.687885,"auth_comp_phone":"13288888888","auth_comp_city":"jinan","auth_comp_linkman":"高总","auth_comp_file_id":"f453b93184fa40f58edffafa6766f1c9","auth_comp_img_head_file_id":"8df0e2fea313481d99c8aca460bfe145"}
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
             * auth_comp_lon : 116.993253
             * auth_comp_service_type : YZcompcfwlxwxby, YZcompcfwlxxc, YZcompcfwlxjx
             * auth_comp_name : 山东派乐特网络科技有限公司
             * auth_comp_province : shandong
             * auth_audit_state : 1
             * comp_id : 21
             * auth_comp_addr : 山东省济南市天桥区汽车厂东路与济泺路十字路口西南角中凡·鲁鼎广场
             * auth_comp_lat : 36.687885
             * auth_comp_phone : 13288888888
             * auth_comp_city : jinan
             * auth_comp_linkman : 高总
             * auth_comp_file_id : f453b93184fa40f58edffafa6766f1c9
             * auth_comp_img_head_file_id : 8df0e2fea313481d99c8aca460bfe145
             */

            private double auth_comp_lon;
            private String auth_comp_service_type;
            private String auth_comp_name;
            private String auth_comp_province;
            private String auth_audit_state;
            private String comp_id;
            private String auth_comp_addr;
            private double auth_comp_lat;
            private String auth_comp_phone;
            private String auth_comp_city;
            private String auth_comp_linkman;
            private String auth_comp_file_id;
            private String auth_comp_img_head_file_id;

            public double getAuth_comp_lon() {
                return auth_comp_lon;
            }

            public void setAuth_comp_lon(double auth_comp_lon) {
                this.auth_comp_lon = auth_comp_lon;
            }

            public String getAuth_comp_service_type() {
                return auth_comp_service_type;
            }

            public void setAuth_comp_service_type(String auth_comp_service_type) {
                this.auth_comp_service_type = auth_comp_service_type;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public String getAuth_comp_province() {
                return auth_comp_province;
            }

            public void setAuth_comp_province(String auth_comp_province) {
                this.auth_comp_province = auth_comp_province;
            }

            public String getAuth_audit_state() {
                return auth_audit_state;
            }

            public void setAuth_audit_state(String auth_audit_state) {
                this.auth_audit_state = auth_audit_state;
            }

            public String getComp_id() {
                return comp_id;
            }

            public void setComp_id(String comp_id) {
                this.comp_id = comp_id;
            }

            public String getAuth_comp_addr() {
                return auth_comp_addr;
            }

            public void setAuth_comp_addr(String auth_comp_addr) {
                this.auth_comp_addr = auth_comp_addr;
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

            public String getAuth_comp_city() {
                return auth_comp_city;
            }

            public void setAuth_comp_city(String auth_comp_city) {
                this.auth_comp_city = auth_comp_city;
            }

            public String getAuth_comp_linkman() {
                return auth_comp_linkman;
            }

            public void setAuth_comp_linkman(String auth_comp_linkman) {
                this.auth_comp_linkman = auth_comp_linkman;
            }

            public String getAuth_comp_file_id() {
                return auth_comp_file_id;
            }

            public void setAuth_comp_file_id(String auth_comp_file_id) {
                this.auth_comp_file_id = auth_comp_file_id;
            }

            public String getAuth_comp_img_head_file_id() {
                return auth_comp_img_head_file_id;
            }

            public void setAuth_comp_img_head_file_id(String auth_comp_img_head_file_id) {
                this.auth_comp_img_head_file_id = auth_comp_img_head_file_id;
            }
        }
    }
}
