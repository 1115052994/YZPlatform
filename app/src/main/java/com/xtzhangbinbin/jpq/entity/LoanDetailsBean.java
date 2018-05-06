package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class LoanDetailsBean {


    /**
     * data : {"result":[{"dict_id":"YZcarJRjrdkxydwd","dict_desc":"网贷","dict_value":"wangdai","dict_ext":"baotou,jinan","dict_parent_id":"YZcarJRjrdkxyd","dict_9":"123"},{"dict_id":"YZcarJRjrdkxydxyk","dict_desc":"信用卡","dict_value":"xinyongka","dict_ext":"baotou,jinan","dict_parent_id":"YZcarJRjrdkxyd","dict_9":"1234"}],"pageCount":1,"pageTotal":2}
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
         * result : [{"dict_id":"YZcarJRjrdkxydwd","dict_desc":"网贷","dict_value":"wangdai","dict_ext":"baotou,jinan","dict_parent_id":"YZcarJRjrdkxyd","dict_9":"123"},{"dict_id":"YZcarJRjrdkxydxyk","dict_desc":"信用卡","dict_value":"xinyongka","dict_ext":"baotou,jinan","dict_parent_id":"YZcarJRjrdkxyd","dict_9":"1234"}]
         * pageCount : 1
         * pageTotal : 2
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
             * dict_id : YZcarJRjrdkxydwd
             * dict_desc : 网贷
             * dict_value : wangdai
             * dict_ext : baotou,jinan
             * dict_parent_id : YZcarJRjrdkxyd
             * dict_9 : 123
             */

            private String dict_id;
            private String dict_desc;
            private String dict_value;
            private String dict_ext;
            private String dict_parent_id;
            private String dict_9;

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

            public String getDict_value() {
                return dict_value;
            }

            public void setDict_value(String dict_value) {
                this.dict_value = dict_value;
            }

            public String getDict_ext() {
                return dict_ext;
            }

            public void setDict_ext(String dict_ext) {
                this.dict_ext = dict_ext;
            }

            public String getDict_parent_id() {
                return dict_parent_id;
            }

            public void setDict_parent_id(String dict_parent_id) {
                this.dict_parent_id = dict_parent_id;
            }

            public String getDict_9() {
                return dict_9;
            }

            public void setDict_9(String dict_9) {
                this.dict_9 = dict_9;
            }
        }
    }
}
