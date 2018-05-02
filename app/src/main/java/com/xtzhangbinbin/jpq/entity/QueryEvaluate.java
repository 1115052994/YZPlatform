package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 */

public class QueryEvaluate {


    /**
     * data : {"result":[{"log_id":"501","log_2":"2.2","log_1":"似乎都爱死","log_date":"2018-04-20 17:16:50","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"送达到撒","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"502","log_2":"4","log_1":"山东派乐特网络科技有限公司","log_date":"2018-04-20 18:03:45","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"好","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"503","log_2":"3","log_1":"山东派乐特网络科技有限公司","log_date":"2018-04-20 18:11:26","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"好","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"504","log_2":"4","log_1":"1","log_date":"2018-04-20 18:51:27","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"1","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"505","log_2":"5","log_1":"阿斯顿发","log_date":"2018-04-20 18:52:10","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"asdfasdfasd","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"509","log_2":"1","log_1":"1","log_date":"2018-04-21 15:52:35","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"1","log_3":"1","pers_nickname":"二二二"},{"log_id":"510","log_2":"5","log_1":"1","log_date":"2018-04-21 15:53:18","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"1","log_3":"1","pers_nickname":"二二二"}],"pageCount":1,"pageTotal":7}
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
         * result : [{"log_id":"501","log_2":"2.2","log_1":"似乎都爱死","log_date":"2018-04-20 17:16:50","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"送达到撒","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"502","log_2":"4","log_1":"山东派乐特网络科技有限公司","log_date":"2018-04-20 18:03:45","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"好","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"503","log_2":"3","log_1":"山东派乐特网络科技有限公司","log_date":"2018-04-20 18:11:26","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"好","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"504","log_2":"4","log_1":"1","log_date":"2018-04-20 18:51:27","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"1","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"505","log_2":"5","log_1":"阿斯顿发","log_date":"2018-04-20 18:52:10","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"asdfasdfasd","log_3":"YZpjpjqyflaqzy,YZpjpjqyflshjs","pers_nickname":"二二二"},{"log_id":"509","log_2":"1","log_1":"1","log_date":"2018-04-21 15:52:35","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"1","log_3":"1","pers_nickname":"二二二"},{"log_id":"510","log_2":"5","log_1":"1","log_date":"2018-04-21 15:53:18","pers_head_file_id":"b07d339d016448c4be5096f1ec7ae0a2","log_4":"1","log_3":"1","pers_nickname":"二二二"}]
         * pageCount : 1
         * pageTotal : 7
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
             * log_id : 501
             * log_2 : 2.2
             * log_1 : 似乎都爱死
             * log_date : 2018-04-20 17:16:50
             * pers_head_file_id : b07d339d016448c4be5096f1ec7ae0a2
             * log_4 : 送达到撒
             * log_3 : YZpjpjqyflaqzy,YZpjpjqyflshjs
             * pers_nickname : 二二二
             */

            private String log_id;
            private String log_2;
            private String log_1;
            private String log_date;
            private String pers_head_file_id;
            private String log_4;
            private String log_3;
            private String pers_nickname;

            public String getLog_id() {
                return log_id;
            }

            public void setLog_id(String log_id) {
                this.log_id = log_id;
            }

            public String getLog_2() {
                return log_2;
            }

            public void setLog_2(String log_2) {
                this.log_2 = log_2;
            }

            public String getLog_1() {
                return log_1;
            }

            public void setLog_1(String log_1) {
                this.log_1 = log_1;
            }

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

            public String getPers_nickname() {
                return pers_nickname;
            }

            public void setPers_nickname(String pers_nickname) {
                this.pers_nickname = pers_nickname;
            }
        }
    }
}
