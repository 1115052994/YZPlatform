package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4 0004.
 */

public class CompWalletQuery {

    /**
     * data : {"result":{"date":"2018-04","pageCount":3,"pageTotal":13,"data":[{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:32:33","wallet_log_money":200,"card_type":"哟哟哟"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:30:55","wallet_log_money":200,"card_type":"汽车美容"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:27:45","wallet_log_money":200,"card_type":"哟哟哟"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:25:49","wallet_log_money":200,"card_type":"水电费水电费"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"23123","card_bc_no":"","wallet_log_date":"2018-04-25 21:25:24","wallet_log_money":200,"card_type":"alipay"}],"srmoney":0,"zcmoney":0}}
     * message :
     * status : 1
     */

    private DataBeanX data;
    private String message;
    private String status;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
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

    public static class DataBeanX {
        /**
         * result : {"date":"2018-04","pageCount":3,"pageTotal":13,"data":[{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:32:33","wallet_log_money":200,"card_type":"哟哟哟"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:30:55","wallet_log_money":200,"card_type":"汽车美容"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:27:45","wallet_log_money":200,"card_type":"哟哟哟"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:25:49","wallet_log_money":200,"card_type":"水电费水电费"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"23123","card_bc_no":"","wallet_log_date":"2018-04-25 21:25:24","wallet_log_money":200,"card_type":"alipay"}],"srmoney":0,"zcmoney":0}
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
             * date : 2018-04
             * pageCount : 3
             * pageTotal : 13
             * data : [{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:32:33","wallet_log_money":200,"card_type":"哟哟哟"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:30:55","wallet_log_money":200,"card_type":"汽车美容"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:27:45","wallet_log_money":200,"card_type":"哟哟哟"},{"wechat_id":"","wallet_log_type":"sr","alipay_id":"","card_bc_no":"","wallet_log_date":"2018-04-25 21:25:49","wallet_log_money":200,"card_type":"水电费水电费"},{"wechat_id":"","wallet_log_type":"zc","alipay_id":"23123","card_bc_no":"","wallet_log_date":"2018-04-25 21:25:24","wallet_log_money":200,"card_type":"alipay"}]
             * srmoney : 0.0
             * zcmoney : 0.0
             */

            private String date;
            private int pageCount;
            private int pageTotal;
            private double srmoney;
            private double zcmoney;
            private List<DataBean> data;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

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

            public double getSrmoney() {
                return srmoney;
            }

            public void setSrmoney(double srmoney) {
                this.srmoney = srmoney;
            }

            public double getZcmoney() {
                return zcmoney;
            }

            public void setZcmoney(double zcmoney) {
                this.zcmoney = zcmoney;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean {
                /**
                 * wechat_id :
                 * wallet_log_type : sr
                 * alipay_id :
                 * card_bc_no :
                 * wallet_log_date : 2018-04-25 21:32:33
                 * wallet_log_money : 200.0
                 * card_type : 哟哟哟
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
}
