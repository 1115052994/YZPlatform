package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 */

public class QueryCarRecord {


    /**
     * data : {"result":[{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"},{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"},{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"},{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"}],"pageCount":1,"pageTotal":4}
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
         * result : [{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"},{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"},{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"},{"log_id":"1bd9328a3d93453f910ed7ad98f35371","log_to":"cf8f19d2b36e43709dde5d60c2e07a0b","log_type":"compbrow","log_from":"cf8f19d2b36e43709dde5d60c2e07a0b","auth_comp_name":"槐荫名家公司","log_date":"2018-05-07 21:52:15","auth_comp_img_head_file_id":"808fea921d184f7592a511adcc74bdbe"}]
         * pageCount : 1
         * pageTotal : 4
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
             * log_id : 1bd9328a3d93453f910ed7ad98f35371
             * log_to : cf8f19d2b36e43709dde5d60c2e07a0b
             * log_type : compbrow
             * log_from : cf8f19d2b36e43709dde5d60c2e07a0b
             * auth_comp_name : 槐荫名家公司
             * log_date : 2018-05-07 21:52:15
             * auth_comp_img_head_file_id : 808fea921d184f7592a511adcc74bdbe
             */

            private String log_id;
            private String log_to;
            private String log_type;
            private String log_from;
            private String auth_comp_name;
            private String log_date;
            private String auth_comp_img_head_file_id;

            public String getLog_id() {
                return log_id;
            }

            public void setLog_id(String log_id) {
                this.log_id = log_id;
            }

            public String getLog_to() {
                return log_to;
            }

            public void setLog_to(String log_to) {
                this.log_to = log_to;
            }

            public String getLog_type() {
                return log_type;
            }

            public void setLog_type(String log_type) {
                this.log_type = log_type;
            }

            public String getLog_from() {
                return log_from;
            }

            public void setLog_from(String log_from) {
                this.log_from = log_from;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public String getLog_date() {
                return log_date;
            }

            public void setLog_date(String log_date) {
                this.log_date = log_date;
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
