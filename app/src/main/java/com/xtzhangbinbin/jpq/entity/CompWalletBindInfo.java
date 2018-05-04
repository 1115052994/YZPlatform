package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4 0004.
 */

public class CompWalletBindInfo {

    /**
     * data : {"result":{"balance":3718,"data":["wechat","alipay"]}}
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
         * result : {"balance":3718,"data":["wechat","alipay"]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * balance : 3718.0
             * data : ["wechat","alipay"]
             */

            private double balance;
            private List<String> data;

            public double getBalance() {
                return balance;
            }

            public void setBalance(double balance) {
                this.balance = balance;
            }

            public List<String> getData() {
                return data;
            }

            public void setData(List<String> data) {
                this.data = data;
            }
        }
    }
}
