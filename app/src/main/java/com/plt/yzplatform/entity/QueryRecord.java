package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 */

public class QueryRecord {


    /**
     * data : {"result":[{"log_id":"556","log_to":"3","car_name":"马自达阿特兹 2017款 2.0L 蓝天豪华版","log_type":"carbrow","log_from":"superadmin","log_date":"2018-04-24 10:52:14","carphone":"4444","car_1_file_id":"44"}],"pageCount":1,"pageTotal":1}
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
         * result : [{"log_id":"556","log_to":"3","car_name":"马自达阿特兹 2017款 2.0L 蓝天豪华版","log_type":"carbrow","log_from":"superadmin","log_date":"2018-04-24 10:52:14","carphone":"4444","car_1_file_id":"44"}]
         * pageCount : 1
         * pageTotal : 1
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
             * log_id : 556
             * log_to : 3
             * car_name : 马自达阿特兹 2017款 2.0L 蓝天豪华版
             * log_type : carbrow
             * log_from : superadmin
             * log_date : 2018-04-24 10:52:14
             * carphone : 4444
             * car_1_file_id : 44
             */

            private String log_id;
            private String log_to;
            private String car_name;
            private String log_type;
            private String log_from;
            private String log_date;
            private String carphone;
            private String car_1_file_id;

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

            public String getCar_name() {
                return car_name;
            }

            public void setCar_name(String car_name) {
                this.car_name = car_name;
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

            public String getLog_date() {
                return log_date;
            }

            public void setLog_date(String log_date) {
                this.log_date = log_date;
            }

            public String getCarphone() {
                return carphone;
            }

            public void setCarphone(String carphone) {
                this.carphone = carphone;
            }

            public String getCar_1_file_id() {
                return car_1_file_id;
            }

            public void setCar_1_file_id(String car_1_file_id) {
                this.car_1_file_id = car_1_file_id;
            }
        }
    }
}
