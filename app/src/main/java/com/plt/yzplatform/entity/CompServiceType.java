package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by glp on 2018/4/21.
 * 描述：
 */

public class CompServiceType {


    /**
     * data : {"result":[{"serverLinkType":"inner","serverDesc":"洗车","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcxc"},{"serverLinkType":"inner","serverDesc":"美容","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcmr"}]}
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
            private String serverImgPath;
            private String serverUrl;
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

            public String getServerImgPath() {
                return serverImgPath;
            }

            public void setServerImgPath(String serverImgPath) {
                this.serverImgPath = serverImgPath;
            }

            public Object getServerUrl() {
                return serverUrl;
            }

            public void setServerUrl(String serverUrl) {
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
