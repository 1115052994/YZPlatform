package com.xtzhangbinbin.jpq.entity;

import java.util.List;

public class HotCar {

    /**
     * data : {"result":[{"hotword_name":"宝马","hotword_id":"3"},{"hotword_name":"大众","hotword_id":"1"},{"hotword_name":"丰田","hotword_id":"7"},{"hotword_name":"奔驰","hotword_id":"5"}]}
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
             * hotword_name : 宝马
             * hotword_id : 3
             */

            private String hotword_name;
            private String hotword_id;

            public String getHotword_name() {
                return hotword_name;
            }

            public void setHotword_name(String hotword_name) {
                this.hotword_name = hotword_name;
            }

            public String getHotword_id() {
                return hotword_id;
            }

            public void setHotword_id(String hotword_id) {
                this.hotword_id = hotword_id;
            }
        }
    }
}
