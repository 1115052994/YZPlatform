package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Querystar {

    /**
     * data : {"result":[{"staff_name":"sdfssds","staff_info":"sdfsfs","staff_id":"4","staff_photo_file_id":"sdfssdfs"}]}
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
             * staff_name : sdfssds
             * staff_info : sdfsfs
             * staff_id : 4
             * staff_photo_file_id : sdfssdfs
             */

            private String staff_name;
            private String staff_info;
            private String staff_id;
            private String staff_photo_file_id;

            public String getStaff_name() {
                return staff_name;
            }

            public void setStaff_name(String staff_name) {
                this.staff_name = staff_name;
            }

            public String getStaff_info() {
                return staff_info;
            }

            public void setStaff_info(String staff_info) {
                this.staff_info = staff_info;
            }

            public String getStaff_id() {
                return staff_id;
            }

            public void setStaff_id(String staff_id) {
                this.staff_id = staff_id;
            }

            public String getStaff_photo_file_id() {
                return staff_photo_file_id;
            }

            public void setStaff_photo_file_id(String staff_photo_file_id) {
                this.staff_photo_file_id = staff_photo_file_id;
            }
        }
    }
}
