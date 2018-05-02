package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public class AccessSearchBrandBean {

    /**
     * data : {"result":[{"pinyin":"D","bname":"大迪","bvalue":"2000430","bid":"YZescxgescjzpgcpcxcxpassenger_2000430"},{"pinyin":"D","bname":"大发","bvalue":"2000431","bid":"YZescxgescjzpgcpcxcxpassenger_2000431"},{"pinyin":"D","bname":"大通","bvalue":"2000432","bid":"YZescxgescjzpgcpcxcxpassenger_2000432"},{"pinyin":"D","bname":"大众","bvalue":"2000433","bid":"YZescxgescjzpgcpcxcxpassenger_2000433"}]}
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
             * pinyin : D
             * bname : 大迪
             * bvalue : 2000430
             * bid : YZescxgescjzpgcpcxcxpassenger_2000430
             */

            private String pinyin;
            private String bname;
            private String bvalue;
            private String bid;

            public String getPinyin() {
                return pinyin;
            }

            public void setPinyin(String pinyin) {
                this.pinyin = pinyin;
            }

            public String getBname() {
                return bname;
            }

            public void setBname(String bname) {
                this.bname = bname;
            }

            public String getBvalue() {
                return bvalue;
            }

            public void setBvalue(String bvalue) {
                this.bvalue = bvalue;
            }

            public String getBid() {
                return bid;
            }

            public void setBid(String bid) {
                this.bid = bid;
            }
        }
    }
}
