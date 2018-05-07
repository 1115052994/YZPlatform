package com.xtzhangbinbin.jpq.entity;

import java.io.Serializable;
import java.util.List;

public class BuyCarBean {

    /**
     * data : {"result":[{"car_name":"大众","his_sort_weight":0,"car_place_city":"jinan","car_id":1,"car_1_icon_file_id":"12","car_price":10000000,"car_sign_date":"2018-02-20","car_mileage":222,"age":0},{"car_name":"Fenyr Supersport","car_1_icon_file_id":"12","car_price":10000000,"his_sort_weight":0,"car_sign_date":"2018-02-20","car_mileage":222,"car_place_city":"jinan","car_id":2,"age":0}],"pageCount":1,"pageTotal":2}
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
         * result : [{"car_name":"大众","his_sort_weight":0,"car_place_city":"jinan","car_id":1},{"car_name":"Fenyr Supersport","car_1_icon_file_id":"12","car_price":10000000,"his_sort_weight":0,"car_sign_date":"2018-02-20","car_mileage":222,"car_place_city":"jinan","car_id":2,"age":0}]
         * pageCount : 1
         * pageTotal : 2
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

        public static class ResultBean implements Serializable{
            /*
                "car_name": "斯柯达",
				"car_1_icon_file_id": "834b3281180f4f7a9ed1405777ced0e1",
				"car_price": 135288648,
				"car_sign_date": "2013-04-26",
				"car_mileage": 160763076,
				"car_id": 13961
             */
            /**
             * car_name : 大众
             * his_sort_weight : 0
             * car_place_city : jinan
             * car_id : 1
             * car_1_icon_file_id : 12
             * car_price : 10000000元
             * car_sign_date : 2018-02-20
             * car_mileage : 222
             * age : 0
             */

            private String car_name;
            private int his_sort_weight;
            private String car_place_city;
            private int car_id;
            private String car_1_icon_file_id;
            private int car_price;
            private String car_sign_date;
            private int car_mileage;
            private int age;

            public String getCar_name() {
                return car_name;
            }

            public void setCar_name(String car_name) {
                this.car_name = car_name;
            }

            public int getHis_sort_weight() {
                return his_sort_weight;
            }

            public void setHis_sort_weight(int his_sort_weight) {
                this.his_sort_weight = his_sort_weight;
            }

            public String getCar_place_city() {
                return car_place_city;
            }

            public void setCar_place_city(String car_place_city) {
                this.car_place_city = car_place_city;
            }

            public int getCar_id() {
                return car_id;
            }

            public void setCar_id(int car_id) {
                this.car_id = car_id;
            }

            public String getCar_1_icon_file_id() {
                return car_1_icon_file_id;
            }

            public void setCar_1_icon_file_id(String car_1_icon_file_id) {
                this.car_1_icon_file_id = car_1_icon_file_id;
            }

            public int getCar_price() {
                return car_price;
            }

            public void setCar_price(int car_price) {
                this.car_price = car_price;
            }

            public String getCar_sign_date() {
                return car_sign_date;
            }

            public void setCar_sign_date(String car_sign_date) {
                this.car_sign_date = car_sign_date;
            }

            public int getCar_mileage() {
                return car_mileage;
            }

            public void setCar_mileage(int car_mileage) {
                this.car_mileage = car_mileage;
            }

            public int getAge() {
                return age;
            }

            public void setAge(int age) {
                this.age = age;
            }
        }
    }
}
