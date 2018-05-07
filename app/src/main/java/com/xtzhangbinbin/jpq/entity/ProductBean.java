package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ProductBean {

    /**
     * data : {"result":[{"serverLinkType":"inner","serverDesc":"洗车","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcxc"},{"serverLinkType":"inner","serverDesc":"美容","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcmr"},{"serverLinkType":"inner","serverDesc":"维修保养","serverImgPath":"/res/service_icon/1.jpg","serverUrl":"/service_url/123","serverId":"YZcompcfwlxwxbywxby"},{"serverLinkType":"inner","serverDesc":"钣金喷漆","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxwxbybjpq"},{"serverLinkType":"inner","serverDesc":"更换电瓶","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxwxbyghdc"}]}
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
             * serverLinkType : inner
             * serverDesc : 洗车
             * serverImgPath : null
             * serverUrl : null
             * serverId : YZcompcfwlxxcxc
             */

            private String serverLinkType;
            private String serverDesc;
            private Object serverImgPath;
            private Object serverUrl;
            private String serverId;

            public String getServerLinkType() {
                return serverLinkType;
            }

            public void setServerLinkType(String serverLinkType) {
                this.serverLinkType = serverLinkType;
            }

            public String getServerDesc() {
                return serverDesc;
            }

            public void setServerDesc(String serverDesc) {
                this.serverDesc = serverDesc;
            }

            public Object getServerImgPath() {
                return serverImgPath;
            }

            public void setServerImgPath(Object serverImgPath) {
                this.serverImgPath = serverImgPath;
            }

            public Object getServerUrl() {
                return serverUrl;
            }

            public void setServerUrl(Object serverUrl) {
                this.serverUrl = serverUrl;
            }

            public String getServerId() {
                return serverId;
            }

            public void setServerId(String serverId) {
                this.serverId = serverId;
            }
        }
    }
}
