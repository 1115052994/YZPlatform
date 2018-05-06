package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by glp on 2018/4/17.
 */

public class EtcBean {


    /**
     * data : {"result":[{"phone":"95566","cname":"中国银行","id":"1","url":"http://www.boc.cn/"},{"phone":"95558","cname":"中信银行","id":"2","url":"http://www.citicbank.com/"},{"phone":"95533","cname":"中国建设银行","id":"3","url":"http://www.ccb.com/cn/home/indexv3.html"},{"phone":"95588","cname":"中国工商银行","id":"4","url":"http://www.icbc.com.cn/icbc/"},{"phone":"95577","cname":"华夏银行","id":"5","url":"http://www.hxb.com.cn/index.shtml"},{"phone":"95595","cname":"中国光大银行","id":"6","url":"https://www.cebbank.com/thp/minOpenOnline.do?channelForEcif=05"},{"phone":"95599","cname":"农业银行","id":"8","url":"http://www.abchina.com/cn/"},{"phone":"95580","cname":"邮储银行","id":"9","url":"http://www.psbc.com/cn/index.html"}]}
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
             * phone : 95566
             * cname : 中国银行
             * id : 1
             * url : http://www.boc.cn/
             */

            private String phone;
            private String cname;
            private String id;
            private String url;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getCname() {
                return cname;
            }

            public void setCname(String cname) {
                this.cname = cname;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
