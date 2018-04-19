package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by glp on 2018/4/19.
 * 描述：
 */

public class CarServiceImage {

    /**
     * data : {"result":{"bannerList":[{"adve_location":"03","adve_turn_type":"外部url","adve_name":"12341","adve_state":"1","adve_turn_url":"wqwq","adve_oper_user_id":"6","adve_id":"16","adve_file_id":"1c817c1b748f4dfa82a23c17b90641ca","adve_turn_android_url":"wqwq","adve_order":4,"adve_turn_ios_url":"wqwq","adve_oper_date":"2018-04-17 10:45:12"}],"itemList":[{"serverLinkType":null,"serverDesc":"洗车","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcxc"},{"serverLinkType":null,"serverDesc":"紧急救援","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzyjjjy"},{"serverLinkType":"inner","serverDesc":"维修保养","serverImgPath":"/res/service_icon/1.jpg","serverUrl":"/service_url/123","serverId":"YZcompcfwlxwxbywxby"},{"serverLinkType":null,"serverDesc":"美容","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcmr"},{"serverLinkType":null,"serverDesc":"ETC","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzyetc"},{"serverLinkType":"inner","serverDesc":"钣金喷漆","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxwxbybjpq"},{"serverLinkType":null,"serverDesc":"加油站","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzyjyz"},{"serverLinkType":"inner","serverDesc":"更换电瓶","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxwxbyghdc"},{"serverLinkType":null,"serverDesc":"违章查询","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzywzcx"}]}}
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
         * result : {"bannerList":[{"adve_location":"03","adve_turn_type":"外部url","adve_name":"12341","adve_state":"1","adve_turn_url":"wqwq","adve_oper_user_id":"6","adve_id":"16","adve_file_id":"1c817c1b748f4dfa82a23c17b90641ca","adve_turn_android_url":"wqwq","adve_order":4,"adve_turn_ios_url":"wqwq","adve_oper_date":"2018-04-17 10:45:12"}],"itemList":[{"serverLinkType":null,"serverDesc":"洗车","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcxc"},{"serverLinkType":null,"serverDesc":"紧急救援","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzyjjjy"},{"serverLinkType":"inner","serverDesc":"维修保养","serverImgPath":"/res/service_icon/1.jpg","serverUrl":"/service_url/123","serverId":"YZcompcfwlxwxbywxby"},{"serverLinkType":null,"serverDesc":"美容","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxxcmr"},{"serverLinkType":null,"serverDesc":"ETC","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzyetc"},{"serverLinkType":"inner","serverDesc":"钣金喷漆","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxwxbybjpq"},{"serverLinkType":null,"serverDesc":"加油站","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzyjyz"},{"serverLinkType":"inner","serverDesc":"更换电瓶","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxwxbyghdc"},{"serverLinkType":null,"serverDesc":"违章查询","serverImgPath":null,"serverUrl":null,"serverId":"YZcompcfwlxggzywzcx"}]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private List<BannerListBean> bannerList;
            private List<ItemListBean> itemList;

            public List<BannerListBean> getBannerList() {
                return bannerList;
            }

            public void setBannerList(List<BannerListBean> bannerList) {
                this.bannerList = bannerList;
            }

            public List<ItemListBean> getItemList() {
                return itemList;
            }

            public void setItemList(List<ItemListBean> itemList) {
                this.itemList = itemList;
            }

            public static class BannerListBean {
                /**
                 * adve_location : 03
                 * adve_turn_type : 外部url
                 * adve_name : 12341
                 * adve_state : 1
                 * adve_turn_url : wqwq
                 * adve_oper_user_id : 6
                 * adve_id : 16
                 * adve_file_id : 1c817c1b748f4dfa82a23c17b90641ca
                 * adve_turn_android_url : wqwq
                 * adve_order : 4
                 * adve_turn_ios_url : wqwq
                 * adve_oper_date : 2018-04-17 10:45:12
                 */

                private String adve_location = "";
                private String adve_turn_type = "";
                private String adve_name = "";
                private String adve_state = "";
                private String adve_turn_url = "";
                private String adve_oper_user_id = "";
                private String adve_id = "";
                private String adve_file_id = "";
                private String adve_turn_android_url = "";
                private int adve_order = 0;
                private String adve_turn_ios_url = "";
                private String adve_oper_date = "";

                public String getAdve_location() {
                    return adve_location;
                }

                public void setAdve_location(String adve_location) {
                    this.adve_location = adve_location;
                }

                public String getAdve_turn_type() {
                    return adve_turn_type;
                }

                public void setAdve_turn_type(String adve_turn_type) {
                    this.adve_turn_type = adve_turn_type;
                }

                public String getAdve_name() {
                    return adve_name;
                }

                public void setAdve_name(String adve_name) {
                    this.adve_name = adve_name;
                }

                public String getAdve_state() {
                    return adve_state;
                }

                public void setAdve_state(String adve_state) {
                    this.adve_state = adve_state;
                }

                public String getAdve_turn_url() {
                    return adve_turn_url;
                }

                public void setAdve_turn_url(String adve_turn_url) {
                    this.adve_turn_url = adve_turn_url;
                }

                public String getAdve_oper_user_id() {
                    return adve_oper_user_id;
                }

                public void setAdve_oper_user_id(String adve_oper_user_id) {
                    this.adve_oper_user_id = adve_oper_user_id;
                }

                public String getAdve_id() {
                    return adve_id;
                }

                public void setAdve_id(String adve_id) {
                    this.adve_id = adve_id;
                }

                public String getAdve_file_id() {
                    return adve_file_id;
                }

                public void setAdve_file_id(String adve_file_id) {
                    this.adve_file_id = adve_file_id;
                }

                public String getAdve_turn_android_url() {
                    return adve_turn_android_url;
                }

                public void setAdve_turn_android_url(String adve_turn_android_url) {
                    this.adve_turn_android_url = adve_turn_android_url;
                }

                public int getAdve_order() {
                    return adve_order;
                }

                public void setAdve_order(int adve_order) {
                    this.adve_order = adve_order;
                }

                public String getAdve_turn_ios_url() {
                    return adve_turn_ios_url;
                }

                public void setAdve_turn_ios_url(String adve_turn_ios_url) {
                    this.adve_turn_ios_url = adve_turn_ios_url;
                }

                public String getAdve_oper_date() {
                    return adve_oper_date;
                }

                public void setAdve_oper_date(String adve_oper_date) {
                    this.adve_oper_date = adve_oper_date;
                }
            }

            public static class ItemListBean {
                /**
                 * serverLinkType : null
                 * serverDesc : 洗车
                 * serverImgPath : null
                 * serverUrl : null
                 * serverId : YZcompcfwlxxcxc
                 */

                private String serverLinkType = "";
                private String serverDesc = "";
                private String serverImgPath = "";
                private String serverUrl = "";
                private String serverId = "";

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

                public void setServerImgPath(String serverImgPath) {
                    this.serverImgPath = serverImgPath;
                }

                public String getServerUrl() {
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
}
