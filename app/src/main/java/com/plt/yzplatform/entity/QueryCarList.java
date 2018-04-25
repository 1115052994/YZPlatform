package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/21.
 */

public class QueryCarList {

    /**
     * data : {"result":[{"car_name":"马自达阿特兹 2017款 2.0L 蓝天豪华版","car_price":300,"coll_id":"33","car_mileage":99,"car_id":3,"car_1_file_id":"1111","car_phone":"4444"},{"car_name":"马自达阿特兹 2017款 2.0L 蓝天豪华版","car_price":300,"coll_id":"34","car_mileage":99,"car_id":3},{"coll_id":"35"},{"coll_id":"36"}],"pageCount":1,"pageTotal":4}
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
         * result : [{"car_name":"马自达阿特兹 2017款 2.0L 蓝天豪华版","car_price":300,"coll_id":"33","car_mileage":99,"car_id":3,"car_1_file_id":"1111","car_phone":"4444"},{"car_name":"马自达阿特兹 2017款 2.0L 蓝天豪华版","car_price":300,"coll_id":"34","car_mileage":99,"car_id":3},{"coll_id":"35"},{"coll_id":"36"}]
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
             * car_name : 马自达阿特兹 2017款 2.0L 蓝天豪华版
             * car_price : 300
             * coll_id : 33
             * car_mileage : 99
             * car_id : 3
             * car_1_file_id : 1111
             * car_phone : 4444
             */

            private String car_name;
            private int car_price;
            private String coll_id;
            private int car_mileage;
            private int car_id;
            private String car_1_file_id;
            private String car_phone;

            public String getCar_name() {
                return car_name;
            }

            public void setCar_name(String car_name) {
                this.car_name = car_name;
            }

            public int getCar_price() {
                return car_price;
            }

            public void setCar_price(int car_price) {
                this.car_price = car_price;
            }

            public String getColl_id() {
                return coll_id;
            }

            public void setColl_id(String coll_id) {
                this.coll_id = coll_id;
            }

            public int getCar_mileage() {
                return car_mileage;
            }

            public void setCar_mileage(int car_mileage) {
                this.car_mileage = car_mileage;
            }

            public int getCar_id() {
                return car_id;
            }

            public void setCar_id(int car_id) {
                this.car_id = car_id;
            }

            public String getCar_1_file_id() {
                return car_1_file_id;
            }

            public void setCar_1_file_id(String car_1_file_id) {
                this.car_1_file_id = car_1_file_id;
            }

            public String getCar_phone() {
                return car_phone;
            }

            public void setCar_phone(String car_phone) {
                this.car_phone = car_phone;
            }
        }
    }
}
