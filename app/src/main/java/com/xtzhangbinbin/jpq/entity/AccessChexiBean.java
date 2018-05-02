package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/30.
 */

public class AccessChexiBean {

    /**
     * data : {"result":[{"tvalue":"30001139","tname":"AC Schnitzer M3(进口)","tid":"YZescxgescjzpgcpcxcxpassenger_200038930001139"},{"tvalue":"30001140","tname":"AC Schnitzer X5(进口)","tid":"YZescxgescjzpgcpcxcxpassenger_200038930001140"}]}
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
             * tvalue : 30001139
             * tname : AC Schnitzer M3(进口)
             * tid : YZescxgescjzpgcpcxcxpassenger_200038930001139
             */

            private String tvalue;
            private String tname;
            private String tid;

            public String getTvalue() {
                return tvalue;
            }

            public void setTvalue(String tvalue) {
                this.tvalue = tvalue;
            }

            public String getTname() {
                return tname;
            }

            public void setTname(String tname) {
                this.tname = tname;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }
        }
    }
}
