package com.plt.yzplatform.entity;

import java.util.List;

public class HotCity {

    /**
     * data : {"result":["长沙","常州","成都","广州","杭州","合肥","临沂","临夏","宁波","青岛"],"pageCount":2,"pageTotal":12}
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
         * result : ["长沙","常州","成都","广州","杭州","合肥","临沂","临夏","宁波","青岛"]
         * pageCount : 2
         * pageTotal : 12
         */

        private int pageCount;
        private int pageTotal;
        private List<String> result;

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

        public List<String> getResult() {
            return result;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }
    }
}
