package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Querystar {


    /**
     * data : {"result":[{"staff_name":"张三","staff_info":"这是一个优秀的员工","staff_id":"10","staff_photo_file_id":"asdfad","staff_state":"3"},{"staff_name":"张三","staff_info":"这是一个优秀的员工","staff_id":"11","staff_photo_file_id":"asdfad","staff_state":"3"},{"staff_name":"张三","staff_info":"这是一个优秀的员工","staff_id":"12","staff_photo_file_id":"asdfad","staff_state":"3"},{"staff_name":"张三","staff_info":"这是一个优秀的员工","staff_id":"13","staff_photo_file_id":"asdfad","staff_state":"3"},{"staff_name":"张三","staff_info":"这是一个优秀的员工","staff_id":"14","staff_photo_file_id":"asdfad","staff_state":"3"},{"staff_name":"lisi","staff_info":"aaadad","staff_id":"16","staff_photo_file_id":"asdfadfadf","staff_state":"3"},{"staff_name":"lisi","staff_info":"aaadad","staff_id":"17","staff_photo_file_id":"asdfadfadf","staff_state":"3"},{"staff_name":"lisi","staff_info":"aaadad","staff_id":"19","staff_photo_file_id":"asdfadfadf","staff_state":"3"},{"staff_name":"aaaaa","staff_info":"aaaaaa11111","staff_id":"4","staff_photo_file_id":"1341341","staff_state":"3"},{"staff_name":"张三","staff_info":"这是一个优秀的员工","staff_id":"8","staff_photo_file_id":"asdfad","staff_state":"3"},{"staff_name":"张三","staff_info":"这是一个优秀的员工","staff_id":"9","staff_photo_file_id":"asdfad","staff_state":"3"}]}
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
             * staff_name : 张三
             * staff_info : 这是一个优秀的员工
             * staff_id : 10
             * staff_photo_file_id : asdfad
             * staff_state : 3
             */

            private String staff_name;
            private String staff_info;
            private String staff_id;
            private String staff_photo_file_id;
            private String staff_state;

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

            public String getStaff_state() {
                return staff_state;
            }

            public void setStaff_state(String staff_state) {
                this.staff_state = staff_state;
            }
        }
    }
}
