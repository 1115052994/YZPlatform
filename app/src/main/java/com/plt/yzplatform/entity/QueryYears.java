package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */

public class QueryYears {

    /**
     * data : {"result":[{"dict_4":"2017","dict_id":"YZescxgescppcxdlxlcnkcxmy_33_79_2730_2017_25912","dict_desc":"2017款 S3  2.0T Limousine"},{"dict_4":"2017","dict_id":"YZescxgescppcxdlxlcnkcxmy_33_79_2730_2017_25914","dict_desc":"2017款 改款 S3 2.0T Limousine"},{"dict_4":"2015","dict_id":"YZescxgescppcxdlxlcnkcxmy_33_79_2730_2015_15427","dict_desc":"2015款 S3 2.0T Limousine"}]}
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
             * dict_4 : 2017
             * dict_id : YZescxgescppcxdlxlcnkcxmy_33_79_2730_2017_25912
             * dict_desc : 2017款 S3  2.0T Limousine
             */

            private String dict_4;
            private String dict_id;
            private String dict_desc;

            public String getDict_4() {
                return dict_4;
            }

            public void setDict_4(String dict_4) {
                this.dict_4 = dict_4;
            }

            public String getDict_id() {
                return dict_id;
            }

            public void setDict_id(String dict_id) {
                this.dict_id = dict_id;
            }

            public String getDict_desc() {
                return dict_desc;
            }

            public void setDict_desc(String dict_desc) {
                this.dict_desc = dict_desc;
            }
        }
    }
}
