package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class CompWalletDetail {


    /**
     * data : {"result":[{"wechat_id":"","wallet_log_type":"zc","alipay_id":"58699885","card_bc_no":"","wallet_log_date":"2018-05-05 11:02:18","wallet_log_money":-100,"card_type":"alipay"},{"wechat_id":"123456789","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 11:01:59","wallet_log_money":-120,"card_type":"wechat"},{"wechat_id":"123456789","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:50:17","wallet_log_money":-100,"card_type":"wechat"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"","card_bc_no":"445566665544","wallet_log_date":"2018-05-05 10:30:56","wallet_log_money":-100,"card_type":"bankcard"},{"wechat_id":"123456789","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:23:42","wallet_log_money":-100,"card_type":"wechat"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"58699885","card_bc_no":"","wallet_log_date":"2018-05-05 10:23:22","wallet_log_money":-500,"card_type":"alipay"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:17:51","wallet_log_money":-500},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:05:03","wallet_log_money":-100},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:32:33","wallet_log_money":200},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:30:55","wallet_log_money":200}],"pageCount":3,"pageTotal":21}
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
         * result : [{"wechat_id":"","wallet_log_type":"zc","alipay_id":"58699885","card_bc_no":"","wallet_log_date":"2018-05-05 11:02:18","wallet_log_money":-100,"card_type":"alipay"},{"wechat_id":"123456789","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 11:01:59","wallet_log_money":-120,"card_type":"wechat"},{"wechat_id":"123456789","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:50:17","wallet_log_money":-100,"card_type":"wechat"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"","card_bc_no":"445566665544","wallet_log_date":"2018-05-05 10:30:56","wallet_log_money":-100,"card_type":"bankcard"},{"wechat_id":"123456789","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:23:42","wallet_log_money":-100,"card_type":"wechat"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"58699885","card_bc_no":"","wallet_log_date":"2018-05-05 10:23:22","wallet_log_money":-500,"card_type":"alipay"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:17:51","wallet_log_money":-500},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-05-05 10:05:03","wallet_log_money":-100},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:32:33","wallet_log_money":200},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:30:55","wallet_log_money":200}]
         * pageCount : 3
         * pageTotal : 21
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
             * wechat_id :
             * wallet_log_type : zc
             * alipay_id : 58699885
             * card_bc_no :
             * wallet_log_date : 2018-05-05 11:02:18
             * wallet_log_money : -100.0
             * card_type : alipay
             */

            private String wechat_id;
            private String wallet_log_type;
            private String alipay_id;
            private String card_bc_no;
            private String wallet_log_date;
            private double wallet_log_money;
            private String card_type;

            public String getWechat_id() {
                return wechat_id;
            }

            public void setWechat_id(String wechat_id) {
                this.wechat_id = wechat_id;
            }

            public String getWallet_log_type() {
                return wallet_log_type;
            }

            public void setWallet_log_type(String wallet_log_type) {
                this.wallet_log_type = wallet_log_type;
            }

            public String getAlipay_id() {
                return alipay_id;
            }

            public void setAlipay_id(String alipay_id) {
                this.alipay_id = alipay_id;
            }

            public String getCard_bc_no() {
                return card_bc_no;
            }

            public void setCard_bc_no(String card_bc_no) {
                this.card_bc_no = card_bc_no;
            }

            public String getWallet_log_date() {
                return wallet_log_date;
            }

            public void setWallet_log_date(String wallet_log_date) {
                this.wallet_log_date = wallet_log_date;
            }

            public double getWallet_log_money() {
                return wallet_log_money;
            }

            public void setWallet_log_money(double wallet_log_money) {
                this.wallet_log_money = wallet_log_money;
            }

            public String getCard_type() {
                return card_type;
            }

            public void setCard_type(String card_type) {
                this.card_type = card_type;
            }
        }
    }
}
