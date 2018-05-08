package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class WeizhangResultBean {

    /**
     * retCode : 000000
     * appid : ZTCL201802021050
     * requestId : 507c8904420c4eca8120ab9fa0ad9dae
     * province : 山东
     * lsNum : 鲁A871ZD
     * carType : 02
     * status : 2
     * lists : [{"date":"2018-01-13 10:26:09","addr":"经七路与纬四路路口西向东","content":"机动车违规使用专用车道","legalnum":"","grade":"0","money":"100"},{"date":"2017-09-10 17:14:10","addr":"山大南路（山东大学）","content":"违反禁令标志指示","legalnum":"","grade":"3","money":"200"}]
     * msg : 查询成功
     */

    private String retCode;
    private String appid;
    private String requestId;
    private String province;
    private String lsNum;
    private String carType;
    private String status;
    private String msg;
    private List<ListsBean> lists;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLsNum() {
        return lsNum;
    }

    public void setLsNum(String lsNum) {
        this.lsNum = lsNum;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * date : 2018-01-13 10:26:09
         * addr : 经七路与纬四路路口西向东
         * content : 机动车违规使用专用车道
         * legalnum :
         * grade : 0
         * money : 100
         */

        private String date;
        private String addr;
        private String content;
        private String legalnum;
        private String grade;
        private String money;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLegalnum() {
            return legalnum;
        }

        public void setLegalnum(String legalnum) {
            this.legalnum = legalnum;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
