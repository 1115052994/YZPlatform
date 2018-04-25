package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/22.
 */

public class QueryNum {

    /**
     * data : {"result":[{"star45num":29,"star12num":2,"star3num":2}]}
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
             * star45num : 29
             * star12num : 2
             * star3num : 2
             */

            private int star45num;
            private int star12num;
            private int star3num;

            public int getStar45num() {
                return star45num;
            }

            public void setStar45num(int star45num) {
                this.star45num = star45num;
            }

            public int getStar12num() {
                return star12num;
            }

            public void setStar12num(int star12num) {
                this.star12num = star12num;
            }

            public int getStar3num() {
                return star3num;
            }

            public void setStar3num(int star3num) {
                this.star3num = star3num;
            }
        }
    }
}
