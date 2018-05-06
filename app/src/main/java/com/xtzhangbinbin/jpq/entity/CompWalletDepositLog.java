package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class CompWalletDepositLog {

    /**
     * data : {"result":[{"wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"card_alipay_id":"58699885","wallet_apply_pay_flag":"0","card_type":"alipay","wallet_apply_date":"2018-05-05 11:02:18","wallet_apply_audit_state":"1","card_wechat_id":"123456789","card_bc_name":"测试名","card_bc_type":"你好，人民银行","card_bc_addr":"测试地址","card_bc_no":"445566665544","wallet_apply_audit_msg":"这是审核备注消息"},{"card_wechat_id":"123456789","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":120,"wallet_apply_pay_flag":"0","card_type":"wechat","wallet_apply_date":"2018-05-05 11:01:59","wallet_apply_audit_state":"1"},{"card_wechat_id":"123456789","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"wallet_apply_pay_flag":"0","card_type":"wechat","wallet_apply_date":"2018-05-05 10:50:17","wallet_apply_audit_state":"1"},{"card_bc_name":"测试名","card_bc_type":"你好，人民银行","card_bc_addr":"测试地址","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"wallet_apply_pay_flag":"0","card_bc_no":"445566665544","card_type":"bankcard","wallet_apply_date":"2018-05-05 10:30:56","wallet_apply_audit_state":"1"},{"card_wechat_id":"123456789","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"wallet_apply_pay_flag":"0","card_type":"wechat","wallet_apply_date":"2018-05-05 10:23:42","wallet_apply_audit_msg":"这是审核备注消息","wallet_apply_audit_state":"1"},{"wallet_apply_audit_state_cn":"审核中","wallet_apply_money":500,"card_alipay_id":"58699885","wallet_apply_pay_flag":"0","card_type":"alipay","wallet_apply_date":"2018-05-05 10:23:22","wallet_apply_audit_state":"1"}],"pageCount":1,"pageTotal":6}
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
         * result : [{"wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"card_alipay_id":"58699885","wallet_apply_pay_flag":"0","card_type":"alipay","wallet_apply_date":"2018-05-05 11:02:18","wallet_apply_audit_state":"1"},{"card_wechat_id":"123456789","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":120,"wallet_apply_pay_flag":"0","card_type":"wechat","wallet_apply_date":"2018-05-05 11:01:59","wallet_apply_audit_state":"1"},{"card_wechat_id":"123456789","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"wallet_apply_pay_flag":"0","card_type":"wechat","wallet_apply_date":"2018-05-05 10:50:17","wallet_apply_audit_state":"1"},{"card_bc_name":"测试名","card_bc_type":"你好，人民银行","card_bc_addr":"测试地址","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"wallet_apply_pay_flag":"0","card_bc_no":"445566665544","card_type":"bankcard","wallet_apply_date":"2018-05-05 10:30:56","wallet_apply_audit_state":"1"},{"card_wechat_id":"123456789","wallet_apply_audit_state_cn":"审核中","wallet_apply_money":100,"wallet_apply_pay_flag":"0","card_type":"wechat","wallet_apply_date":"2018-05-05 10:23:42","wallet_apply_audit_msg":"这是审核备注消息","wallet_apply_audit_state":"1"},{"wallet_apply_audit_state_cn":"审核中","wallet_apply_money":500,"card_alipay_id":"58699885","wallet_apply_pay_flag":"0","card_type":"alipay","wallet_apply_date":"2018-05-05 10:23:22","wallet_apply_audit_state":"1"}]
         * pageCount : 1
         * pageTotal : 6
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
             * wallet_apply_audit_state_cn : 审核中
             * wallet_apply_money : 100.0
             * card_alipay_id : 58699885
             * wallet_apply_pay_flag : 0
             * card_type : alipay
             * wallet_apply_date : 2018-05-05 11:02:18
             * wallet_apply_audit_state : 1
             * card_wechat_id : 123456789
             * card_bc_name : 测试名
             * card_bc_type : 你好，人民银行
             * card_bc_addr : 测试地址
             * card_bc_no : 445566665544
             * wallet_apply_audit_msg : 这是审核备注消息
             */

            private String wallet_apply_audit_state_cn;
            private double wallet_apply_money;
            private String card_alipay_id;
            private String wallet_apply_pay_flag;
            private String card_type;
            private String wallet_apply_date;
            private String wallet_apply_audit_state;
            private String card_wechat_id;
            private String card_bc_name;
            private String card_bc_type;
            private String card_bc_addr;
            private String card_bc_no;
            private String wallet_apply_audit_msg;

            public String getWallet_apply_audit_state_cn() {
                return wallet_apply_audit_state_cn;
            }

            public void setWallet_apply_audit_state_cn(String wallet_apply_audit_state_cn) {
                this.wallet_apply_audit_state_cn = wallet_apply_audit_state_cn;
            }

            public double getWallet_apply_money() {
                return wallet_apply_money;
            }

            public void setWallet_apply_money(double wallet_apply_money) {
                this.wallet_apply_money = wallet_apply_money;
            }

            public String getCard_alipay_id() {
                return card_alipay_id;
            }

            public void setCard_alipay_id(String card_alipay_id) {
                this.card_alipay_id = card_alipay_id;
            }

            public String getWallet_apply_pay_flag() {
                return wallet_apply_pay_flag;
            }

            public void setWallet_apply_pay_flag(String wallet_apply_pay_flag) {
                this.wallet_apply_pay_flag = wallet_apply_pay_flag;
            }

            public String getCard_type() {
                return card_type;
            }

            public void setCard_type(String card_type) {
                this.card_type = card_type;
            }

            public String getWallet_apply_date() {
                return wallet_apply_date;
            }

            public void setWallet_apply_date(String wallet_apply_date) {
                this.wallet_apply_date = wallet_apply_date;
            }

            public String getWallet_apply_audit_state() {
                return wallet_apply_audit_state;
            }

            public void setWallet_apply_audit_state(String wallet_apply_audit_state) {
                this.wallet_apply_audit_state = wallet_apply_audit_state;
            }

            public String getCard_wechat_id() {
                return card_wechat_id;
            }

            public void setCard_wechat_id(String card_wechat_id) {
                this.card_wechat_id = card_wechat_id;
            }

            public String getCard_bc_name() {
                return card_bc_name;
            }

            public void setCard_bc_name(String card_bc_name) {
                this.card_bc_name = card_bc_name;
            }

            public String getCard_bc_type() {
                return card_bc_type;
            }

            public void setCard_bc_type(String card_bc_type) {
                this.card_bc_type = card_bc_type;
            }

            public String getCard_bc_addr() {
                return card_bc_addr;
            }

            public void setCard_bc_addr(String card_bc_addr) {
                this.card_bc_addr = card_bc_addr;
            }

            public String getCard_bc_no() {
                return card_bc_no;
            }

            public void setCard_bc_no(String card_bc_no) {
                this.card_bc_no = card_bc_no;
            }

            public String getWallet_apply_audit_msg() {
                return wallet_apply_audit_msg;
            }

            public void setWallet_apply_audit_msg(String wallet_apply_audit_msg) {
                this.wallet_apply_audit_msg = wallet_apply_audit_msg;
            }
        }
    }
}
