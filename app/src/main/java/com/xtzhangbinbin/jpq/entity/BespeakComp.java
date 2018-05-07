package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class BespeakComp {

    /**
     * data : {"result":[{"car_name":"巴博斯","car_1_file_id":"mmmmmm33","online_phone":"17685416555","online_date":"2017-12-27 15:08:57","online_id":"f8ffa78cb42b4267b6922f2f9f1b0e42"},{"car_name":"赛麟","car_1_file_id":"mmmmmm55","online_phone":"17685416555","online_date":"2017-12-26 08:06:22","online_id":"c8c38f82b12544fdab2e6570c9cda435"},{"car_name":"思铭","car_1_file_id":"mmmmmm36","online_phone":"17685416555","online_date":"2017-12-15 04:24:54","online_id":"50e1498112f541cf899bc005d8f656f7"}],"pageCount":334,"pageTotal":1000}
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
         * result : [{"car_name":"巴博斯","car_1_file_id":"mmmmmm33","online_phone":"17685416555","online_date":"2017-12-27 15:08:57","online_id":"f8ffa78cb42b4267b6922f2f9f1b0e42"},{"car_name":"赛麟","car_1_file_id":"mmmmmm55","online_phone":"17685416555","online_date":"2017-12-26 08:06:22","online_id":"c8c38f82b12544fdab2e6570c9cda435"},{"car_name":"思铭","car_1_file_id":"mmmmmm36","online_phone":"17685416555","online_date":"2017-12-15 04:24:54","online_id":"50e1498112f541cf899bc005d8f656f7"}]
         * pageCount : 334
         * pageTotal : 1000
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
             * car_name : 巴博斯
             * car_1_file_id : mmmmmm33
             * online_phone : 17685416555
             * online_date : 2017-12-27 15:08:57
             * online_id : f8ffa78cb42b4267b6922f2f9f1b0e42
             */

            private String car_name;
            private String car_1_file_id;
            private String online_phone;
            private String online_date;
            private String online_id;

            public String getCar_name() {
                return car_name;
            }

            public void setCar_name(String car_name) {
                this.car_name = car_name;
            }

            public String getCar_1_file_id() {
                return car_1_file_id;
            }

            public void setCar_1_file_id(String car_1_file_id) {
                this.car_1_file_id = car_1_file_id;
            }

            public String getOnline_phone() {
                return online_phone;
            }

            public void setOnline_phone(String online_phone) {
                this.online_phone = online_phone;
            }

            public String getOnline_date() {
                return online_date;
            }

            public void setOnline_date(String online_date) {
                this.online_date = online_date;
            }

            public String getOnline_id() {
                return online_id;
            }

            public void setOnline_id(String online_id) {
                this.online_id = online_id;
            }
        }
    }
}
