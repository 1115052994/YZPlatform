package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public class AccessModeBean {

    /**
     * data : {"result":[{"mvalue":"30014087","mid":"YZescxgescjzpgcpcxcxpassenger_20003893000113930014087","mname":"AC Schnitzer M3(进口) 2015款 ACS3 sport"}]}
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
             * mvalue : 30014087
             * mid : YZescxgescjzpgcpcxcxpassenger_20003893000113930014087
             * mname : AC Schnitzer M3(进口) 2015款 ACS3 sport
             */

            private String mvalue;
            private String mid;
            private String mname;

            public String getMvalue() {
                return mvalue;
            }

            public void setMvalue(String mvalue) {
                this.mvalue = mvalue;
            }

            public String getMid() {
                return mid;
            }

            public void setMid(String mid) {
                this.mid = mid;
            }

            public String getMname() {
                return mname;
            }

            public void setMname(String mname) {
                this.mname = mname;
            }
        }
    }
}
