package com.plt.yzplatform.entity;

import java.util.List;

public class SearchCompBean {

    /**
     * data : {"result":[{"auth_comp_service_type":"YZcompcfwlxjx,YZcompcfwlxfwjg,YZcompcfwlxqcgz,YZcompcfwlxjcz","auth_id":"72","auth_comp_name":"山东派乐特网络科技有限公司","comp_id":"21"},{"auth_comp_service_type":"YZcompcfwlxwxby,YZcompcfwlxxc","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","comp_id":"24"}]}
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
             * auth_comp_service_type : YZcompcfwlxjx,YZcompcfwlxfwjg,YZcompcfwlxqcgz,YZcompcfwlxjcz
             * auth_id : 72
             * auth_comp_name : 山东派乐特网络科技有限公司
             * comp_id : 21
             */

            private String auth_comp_service_type;
            private String auth_id;
            private String auth_comp_name;
            private String comp_id;

            public String getAuth_comp_service_type() {
                return auth_comp_service_type;
            }

            public void setAuth_comp_service_type(String auth_comp_service_type) {
                this.auth_comp_service_type = auth_comp_service_type;
            }

            public String getAuth_id() {
                return auth_id;
            }

            public void setAuth_id(String auth_id) {
                this.auth_id = auth_id;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public String getComp_id() {
                return comp_id;
            }

            public void setComp_id(String comp_id) {
                this.comp_id = comp_id;
            }
        }
    }
}
