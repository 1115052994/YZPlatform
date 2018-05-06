package com.xtzhangbinbin.jpq.entity;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class VersionInfo {
    /**
     * data : {"result":{"app_version_use_count":null,"app_version_url":"http://eflow.56ez.com/myres?type=download&id=8bf50ca39f7b4f3b943c9226e403891b","app_version_id":"2","app_version_date":"2018-05-06 15:28:02","app_version_name":"1.0.1","app_version_file_id":"8bf50ca39f7b4f3b943c9226e403891b","app_version_desc":"这是最新的版本内容","app_version_ext":null,"app_version_state":"1"}}
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
         * result : {"app_version_use_count":null,"app_version_url":"http://eflow.56ez.com/myres?type=download&id=8bf50ca39f7b4f3b943c9226e403891b","app_version_id":"2","app_version_date":"2018-05-06 15:28:02","app_version_name":"1.0.1","app_version_file_id":"8bf50ca39f7b4f3b943c9226e403891b","app_version_desc":"这是最新的版本内容","app_version_ext":null,"app_version_state":"1"}
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
             * app_version_use_count : null
             * app_version_url : http://eflow.56ez.com/myres?type=download&id=8bf50ca39f7b4f3b943c9226e403891b
             * app_version_id : 2
             * app_version_date : 2018-05-06 15:28:02
             * app_version_name : 1.0.1
             * app_version_file_id : 8bf50ca39f7b4f3b943c9226e403891b
             * app_version_desc : 这是最新的版本内容
             * app_version_ext : null
             * app_version_state : 1
             */

            private Object app_version_use_count;
            private String app_version_url;
            private String app_version_id;
            private String app_version_date;
            private String app_version_name;
            private String app_version_file_id;
            private String app_version_desc;
            private Object app_version_ext;
            private String app_version_state;

            public Object getApp_version_use_count() {
                return app_version_use_count;
            }

            public void setApp_version_use_count(Object app_version_use_count) {
                this.app_version_use_count = app_version_use_count;
            }

            public String getApp_version_url() {
                return app_version_url;
            }

            public void setApp_version_url(String app_version_url) {
                this.app_version_url = app_version_url;
            }

            public String getApp_version_id() {
                return app_version_id;
            }

            public void setApp_version_id(String app_version_id) {
                this.app_version_id = app_version_id;
            }

            public String getApp_version_date() {
                return app_version_date;
            }

            public void setApp_version_date(String app_version_date) {
                this.app_version_date = app_version_date;
            }

            public String getApp_version_name() {
                return app_version_name;
            }

            public void setApp_version_name(String app_version_name) {
                this.app_version_name = app_version_name;
            }

            public String getApp_version_file_id() {
                return app_version_file_id;
            }

            public void setApp_version_file_id(String app_version_file_id) {
                this.app_version_file_id = app_version_file_id;
            }

            public String getApp_version_desc() {
                return app_version_desc;
            }

            public void setApp_version_desc(String app_version_desc) {
                this.app_version_desc = app_version_desc;
            }

            public Object getApp_version_ext() {
                return app_version_ext;
            }

            public void setApp_version_ext(Object app_version_ext) {
                this.app_version_ext = app_version_ext;
            }

            public String getApp_version_state() {
                return app_version_state;
            }

            public void setApp_version_state(String app_version_state) {
                this.app_version_state = app_version_state;
            }
        }
    }
}
