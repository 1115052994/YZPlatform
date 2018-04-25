package com.plt.yzplatform.entity;

import java.util.List;

public class CarParams {

    /**
     * data : {"result":{"bannerList":[{"adve_location":"01","adve_turn_type":"内部url","adve_name":"1","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"32","adve_file_id":"da1be8e281854d68bd3e5fc59771e37d","adve_turn_android_url":"111","adve_order":5,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:51:56"},{"adve_location":"01","adve_turn_type":"内部url","adve_name":"2","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"33","adve_file_id":"59763be6106f4c97b9af298bff6cd07f","adve_turn_android_url":"111","adve_order":4,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:52:18"},{"adve_location":"01","adve_turn_type":"内部url","adve_name":"3","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"34","adve_file_id":"4c83aab6f5ab4e5eb5084352023ec58c","adve_turn_android_url":"111","adve_order":3,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:52:30"},{"adve_location":"01","adve_turn_type":"内部url","adve_name":"4","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"35","adve_file_id":"2657e155de21412ea916bf9d6bc89991","adve_turn_android_url":"111","adve_order":3,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:52:43"}],"dctList":[{"childParamList":[{"img":"d1f4340c727e4fcd86ca50ce6777bcec","paramName":"不限车型","paramId":"YZcarscreencarstypebxcx"},{"img":"49e178d88b7242ec895aa75abdcc9b10","paramName":"两厢轿车","paramId":"YZcarscreencarstypelxjc"},{"img":"498316ceba9b43bcb7bc01ab89c08940","paramName":"三厢轿车","paramId":"YZcarscreencarstypesxjc"},{"img":"9eaa9f1ddeb944689c610d7d0280a603","paramName":"SUV","paramId":"YZcarscreencarstypeSUV"},{"img":"31802b35284540e6b61ce5cd4c5f3cea","paramName":"MPV","paramId":"YZcarscreencarstypeMPV"},{"img":"ff00722ea13948968cc475334dd0c1a8","paramName":"跑车","paramId":"YZcarscreencarstypepao"}],"parentParamName":"车型","parentParamId":"YZcarscreencarstype"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenbsxbx"},{"img":null,"paramName":"自动","paramId":"YZcarscreenbsxzd"},{"img":null,"paramName":"手动","paramId":"YZcarscreenbsxsd"}],"parentParamName":"变速箱","parentParamId":"YZcarscreenbsx"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenpfbzbx"},{"img":null,"paramName":"国三及以上","paramId":"YZcarscreenpfbzsan"},{"img":null,"paramName":"国四及以上","paramId":"YZcarscreenpfbzsi"},{"img":null,"paramName":"国五","paramId":"YZcarscreenpfbzwu"}],"parentParamName":"排放标准","parentParamId":"YZcarscreenpfbz"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenoilbx"},{"img":null,"paramName":"汽油","paramId":"YZcarscreenoilqy"},{"img":null,"paramName":"柴油","paramId":"YZcarscreenoilcy"},{"img":null,"paramName":"电动","paramId":"YZcarscreenoildd"},{"img":null,"paramName":"油电混合","paramId":"YZcarscreenoilydhe"},{"img":null,"paramName":"油气混合","paramId":"YZcarscreenoilyqhe"}],"parentParamName":"燃油类型","parentParamId":"YZcarscreenoil"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenseatbx"},{"img":null,"paramName":"2座","paramId":"YZcarscreenseattwo"},{"img":null,"paramName":"4座","paramId":"YZcarscreenseatfour"},{"img":null,"paramName":"7座","paramId":"YZcarscreenseatseven"},{"img":null,"paramName":"7座以上","paramId":"YZcarscreenseatup"}],"parentParamName":"座位数","parentParamId":"YZcarscreenseat"}]}}
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
         * result : {"bannerList":[{"adve_location":"01","adve_turn_type":"内部url","adve_name":"1","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"32","adve_file_id":"da1be8e281854d68bd3e5fc59771e37d","adve_turn_android_url":"111","adve_order":5,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:51:56"},{"adve_location":"01","adve_turn_type":"内部url","adve_name":"2","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"33","adve_file_id":"59763be6106f4c97b9af298bff6cd07f","adve_turn_android_url":"111","adve_order":4,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:52:18"},{"adve_location":"01","adve_turn_type":"内部url","adve_name":"3","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"34","adve_file_id":"4c83aab6f5ab4e5eb5084352023ec58c","adve_turn_android_url":"111","adve_order":3,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:52:30"},{"adve_location":"01","adve_turn_type":"内部url","adve_name":"4","adve_state":"1","adve_turn_url":"11","adve_oper_user_id":"6","adve_id":"35","adve_file_id":"2657e155de21412ea916bf9d6bc89991","adve_turn_android_url":"111","adve_order":3,"adve_turn_ios_url":"111","adve_oper_date":"2018-04-21 18:52:43"}],"dctList":[{"childParamList":[{"img":"d1f4340c727e4fcd86ca50ce6777bcec","paramName":"不限车型","paramId":"YZcarscreencarstypebxcx"},{"img":"49e178d88b7242ec895aa75abdcc9b10","paramName":"两厢轿车","paramId":"YZcarscreencarstypelxjc"},{"img":"498316ceba9b43bcb7bc01ab89c08940","paramName":"三厢轿车","paramId":"YZcarscreencarstypesxjc"},{"img":"9eaa9f1ddeb944689c610d7d0280a603","paramName":"SUV","paramId":"YZcarscreencarstypeSUV"},{"img":"31802b35284540e6b61ce5cd4c5f3cea","paramName":"MPV","paramId":"YZcarscreencarstypeMPV"},{"img":"ff00722ea13948968cc475334dd0c1a8","paramName":"跑车","paramId":"YZcarscreencarstypepao"}],"parentParamName":"车型","parentParamId":"YZcarscreencarstype"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenbsxbx"},{"img":null,"paramName":"自动","paramId":"YZcarscreenbsxzd"},{"img":null,"paramName":"手动","paramId":"YZcarscreenbsxsd"}],"parentParamName":"变速箱","parentParamId":"YZcarscreenbsx"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenpfbzbx"},{"img":null,"paramName":"国三及以上","paramId":"YZcarscreenpfbzsan"},{"img":null,"paramName":"国四及以上","paramId":"YZcarscreenpfbzsi"},{"img":null,"paramName":"国五","paramId":"YZcarscreenpfbzwu"}],"parentParamName":"排放标准","parentParamId":"YZcarscreenpfbz"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenoilbx"},{"img":null,"paramName":"汽油","paramId":"YZcarscreenoilqy"},{"img":null,"paramName":"柴油","paramId":"YZcarscreenoilcy"},{"img":null,"paramName":"电动","paramId":"YZcarscreenoildd"},{"img":null,"paramName":"油电混合","paramId":"YZcarscreenoilydhe"},{"img":null,"paramName":"油气混合","paramId":"YZcarscreenoilyqhe"}],"parentParamName":"燃油类型","parentParamId":"YZcarscreenoil"},{"childParamList":[{"img":null,"paramName":"不限","paramId":"YZcarscreenseatbx"},{"img":null,"paramName":"2座","paramId":"YZcarscreenseattwo"},{"img":null,"paramName":"4座","paramId":"YZcarscreenseatfour"},{"img":null,"paramName":"7座","paramId":"YZcarscreenseatseven"},{"img":null,"paramName":"7座以上","paramId":"YZcarscreenseatup"}],"parentParamName":"座位数","parentParamId":"YZcarscreenseat"}]}
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
            private List<DctListBean> dctList;

            public List<BannerListBean> getBannerList() {
                return bannerList;
            }

            public void setBannerList(List<BannerListBean> bannerList) {
                this.bannerList = bannerList;
            }

            public List<DctListBean> getDctList() {
                return dctList;
            }

            public void setDctList(List<DctListBean> dctList) {
                this.dctList = dctList;
            }

            public static class BannerListBean {
                /**
                 * adve_location : 01
                 * adve_turn_type : 内部url
                 * adve_name : 1
                 * adve_state : 1
                 * adve_turn_url : 11
                 * adve_oper_user_id : 6
                 * adve_id : 32
                 * adve_file_id : da1be8e281854d68bd3e5fc59771e37d
                 * adve_turn_android_url : 111
                 * adve_order : 5
                 * adve_turn_ios_url : 111
                 * adve_oper_date : 2018-04-21 18:51:56
                 */

                private String adve_location;
                private String adve_turn_type;
                private String adve_name;
                private String adve_state;
                private String adve_turn_url;
                private String adve_oper_user_id;
                private String adve_id;
                private String adve_file_id;
                private String adve_turn_android_url;
                private int adve_order;
                private String adve_turn_ios_url;
                private String adve_oper_date;

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

            public static class DctListBean {
                /**
                 * childParamList : [{"img":"d1f4340c727e4fcd86ca50ce6777bcec","paramName":"不限车型","paramId":"YZcarscreencarstypebxcx"},{"img":"49e178d88b7242ec895aa75abdcc9b10","paramName":"两厢轿车","paramId":"YZcarscreencarstypelxjc"},{"img":"498316ceba9b43bcb7bc01ab89c08940","paramName":"三厢轿车","paramId":"YZcarscreencarstypesxjc"},{"img":"9eaa9f1ddeb944689c610d7d0280a603","paramName":"SUV","paramId":"YZcarscreencarstypeSUV"},{"img":"31802b35284540e6b61ce5cd4c5f3cea","paramName":"MPV","paramId":"YZcarscreencarstypeMPV"},{"img":"ff00722ea13948968cc475334dd0c1a8","paramName":"跑车","paramId":"YZcarscreencarstypepao"}]
                 * parentParamName : 车型
                 * parentParamId : YZcarscreencarstype
                 */

                private String parentParamName;
                private String parentParamId;
                private List<ChildParamListBean> childParamList;

                public String getParentParamName() {
                    return parentParamName;
                }

                public void setParentParamName(String parentParamName) {
                    this.parentParamName = parentParamName;
                }

                public String getParentParamId() {
                    return parentParamId;
                }

                public void setParentParamId(String parentParamId) {
                    this.parentParamId = parentParamId;
                }

                public List<ChildParamListBean> getChildParamList() {
                    return childParamList;
                }

                public void setChildParamList(List<ChildParamListBean> childParamList) {
                    this.childParamList = childParamList;
                }

                public static class ChildParamListBean {
                    /**
                     * img : d1f4340c727e4fcd86ca50ce6777bcec
                     * paramName : 不限车型
                     * paramId : YZcarscreencarstypebxcx
                     */

                    private String img;
                    private String paramName;
                    private String paramId;

                    public String getImg() {
                        return img;
                    }

                    public void setImg(String img) {
                        this.img = img;
                    }

                    public String getParamName() {
                        return paramName;
                    }

                    public void setParamName(String paramName) {
                        this.paramName = paramName;
                    }

                    public String getParamId() {
                        return paramId;
                    }

                    public void setParamId(String paramId) {
                        this.paramId = paramId;
                    }
                }
            }
        }
    }
}
