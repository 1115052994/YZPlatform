package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class Querystar {


    /**
     * data : {"result":[{"staff_name":"fff","staff_info":"fff","staff_id":"124","staff_photo_file_id":"1b75ebf825e24dca92eeda565765ffba","staff_state":"2"},{"staff_name":"123456","staff_info":"123456","staff_id":"128","staff_photo_file_id":"11459e25181545069e8ff6a57161bb27","staff_state":"3"},{"staff_name":"456789","staff_info":"456789","staff_id":"129","staff_photo_file_id":"98aeab43f7ec475f99f3731a2679ea1f","staff_state":"3"}]}
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
             * staff_name : fff
             * staff_info : fff
             * staff_id : 124
             * staff_photo_file_id : 1b75ebf825e24dca92eeda565765ffba
             * staff_state : 2
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
