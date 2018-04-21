package com.plt.yzplatform.entity;

import java.util.List;

public class CompAppraise {

    /**
     * data : {"result":[{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"},{"log_date":"2018-04-17 15:18:47","pers_head_file_id":"16d361ac779649deb219d78c0e266f12","log_4":"我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。","log_3":"","pers_phone":"15621571552"}]}
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
             * log_date : 2018-04-17 15:18:47
             * pers_head_file_id : 16d361ac779649deb219d78c0e266f12
             * log_4 : 我刚开店的时候，卖过假货，收到的全是好评，而且评价内容都是超30个字的精美好评，但都是一次性的，这些买家都没有再回头来购买第二次。
             * log_3 :
             * pers_phone : 15621571552
             */

            private String log_date;
            private String pers_head_file_id;
            private String log_4;
            private String log_3;
            private String log_2;

            public String getLog_2() {
                return log_2;
            }

            public void setLog_2(String log_2) {
                this.log_2 = log_2;
            }

            private String pers_phone;

            public String getLog_date() {
                return log_date;
            }

            public void setLog_date(String log_date) {
                this.log_date = log_date;
            }

            public String getPers_head_file_id() {
                return pers_head_file_id;
            }

            public void setPers_head_file_id(String pers_head_file_id) {
                this.pers_head_file_id = pers_head_file_id;
            }

            public String getLog_4() {
                return log_4;
            }

            public void setLog_4(String log_4) {
                this.log_4 = log_4;
            }

            public String getLog_3() {
                return log_3;
            }

            public void setLog_3(String log_3) {
                this.log_3 = log_3;
            }

            public String getPers_phone() {
                return pers_phone;
            }

            public void setPers_phone(String pers_phone) {
                this.pers_phone = pers_phone;
            }
        }
    }
}
