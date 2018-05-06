package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class CreditBean {


    /**
     * data : {"result":[{"dict_id":"YZcarJRjrdkdyd","dict_desc":"抵押贷","dict_value":"diyadai","dict_parent_id":"YZcarJRjrdk","dict_9":"这是一段关于抵押贷的很认真的描述","dict_10":"70bebf73177c4b1cb8afd4eaa538ea60"},{"dict_id":"YZcarJRjrdkdzbd","dict_desc":"电子保贷","dict_value":"dianzibaodai","dict_parent_id":"YZcarJRjrdk","dict_9":"电子保贷的一段非常认真的描述","dict_10":"29c201e6eb2a4070b9e19015babc5335"},{"dict_id":"YZcarJRjrdkxyd","dict_desc":"信用贷","dict_value":"xinyongdai","dict_parent_id":"YZcarJRjrdk","dict_9":"信用贷的一段很认真的描述","dict_10":"479d7ae0ea2247a0b58064edb7255164"},{"dict_id":"YZcarJRjrdkzy","dict_desc":"质押","dict_value":"zhiya","dict_parent_id":"YZcarJRjrdk","dict_9":"质押的一段很敷衍的描述","dict_10":"2bd6b2b247eb4ee4a384317702dfbeb9"}],"pageCount":1,"pageTotal":4}
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
         * result : [{"dict_id":"YZcarJRjrdkdyd","dict_desc":"抵押贷","dict_value":"diyadai","dict_parent_id":"YZcarJRjrdk","dict_9":"这是一段关于抵押贷的很认真的描述","dict_10":"70bebf73177c4b1cb8afd4eaa538ea60"},{"dict_id":"YZcarJRjrdkdzbd","dict_desc":"电子保贷","dict_value":"dianzibaodai","dict_parent_id":"YZcarJRjrdk","dict_9":"电子保贷的一段非常认真的描述","dict_10":"29c201e6eb2a4070b9e19015babc5335"},{"dict_id":"YZcarJRjrdkxyd","dict_desc":"信用贷","dict_value":"xinyongdai","dict_parent_id":"YZcarJRjrdk","dict_9":"信用贷的一段很认真的描述","dict_10":"479d7ae0ea2247a0b58064edb7255164"},{"dict_id":"YZcarJRjrdkzy","dict_desc":"质押","dict_value":"zhiya","dict_parent_id":"YZcarJRjrdk","dict_9":"质押的一段很敷衍的描述","dict_10":"2bd6b2b247eb4ee4a384317702dfbeb9"}]
         * pageCount : 1
         * pageTotal : 4
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
             * dict_id : YZcarJRjrdkdyd
             * dict_desc : 抵押贷
             * dict_value : diyadai
             * dict_parent_id : YZcarJRjrdk
             * dict_9 : 这是一段关于抵押贷的很认真的描述
             * dict_10 : 70bebf73177c4b1cb8afd4eaa538ea60
             */

            private String dict_id;
            private String dict_desc;
            private String dict_value;
            private String dict_parent_id;
            private String dict_9;
            private String dict_10;

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

            public String getDict_10() {
                return dict_10;
            }

            public void setDict_10(String dict_10) {
                this.dict_10 = dict_10;
            }
        }
    }
}
