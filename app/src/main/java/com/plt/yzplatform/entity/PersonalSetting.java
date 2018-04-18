package com.plt.yzplatform.entity;

/**
 * Created by glp on 2018/4/18.
 */

public class PersonalSetting {

    /**
     * data : {"result":{"user_id":"27","pers_head_file_id":"1a3219e7058d4e7eba31610a380a24bd","pers_id":"27","pers_nickname":"一一一","pers_phone":"17685416555","pers_sex":"女"}}
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
         * result : {"user_id":"27","pers_head_file_id":"1a3219e7058d4e7eba31610a380a24bd","pers_id":"27","pers_nickname":"一一一","pers_phone":"17685416555","pers_sex":"女"}
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
             * user_id : 27
             * pers_head_file_id : 1a3219e7058d4e7eba31610a380a24bd
             * pers_id : 27
             * pers_nickname : 一一一
             * pers_phone : 17685416555
             * pers_sex : 女
             */

            private String user_id;
            private String pers_head_file_id = "";
            private String pers_id;
            private String pers_nickname = "";
            private String pers_phone = "";
            private String pers_sex = "男";

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getPers_head_file_id() {
                return pers_head_file_id;
            }

            public void setPers_head_file_id(String pers_head_file_id) {
                this.pers_head_file_id = pers_head_file_id;
            }

            public String getPers_id() {
                return pers_id;
            }

            public void setPers_id(String pers_id) {
                this.pers_id = pers_id;
            }

            public String getPers_nickname() {
                return pers_nickname;
            }

            public void setPers_nickname(String pers_nickname) {
                this.pers_nickname = pers_nickname;
            }

            public String getPers_phone() {
                return pers_phone;
            }

            public void setPers_phone(String pers_phone) {
                this.pers_phone = pers_phone;
            }

            public String getPers_sex() {
                return pers_sex;
            }

            public void setPers_sex(String pers_sex) {
                this.pers_sex = pers_sex;
            }
        }
    }
}
