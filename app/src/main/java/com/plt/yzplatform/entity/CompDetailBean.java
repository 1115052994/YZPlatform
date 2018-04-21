package com.plt.yzplatform.entity;

import java.util.List;

public class CompDetailBean {

    /**
     * data : {"result":{"bannerList":[{"adve_location":"04","adve_turn_type":"内部url","adve_name":"新出炉广告","adve_state":"1","adve_turn_url":"123","adve_oper_user_id":"6","adve_id":"17","adve_file_id":"85de4bbb685f40daad7c46fa783308aa","adve_turn_android_url":"123","adve_order":2,"adve_turn_ios_url":"123","adve_oper_date":"2018-04-17 20:56:54"}],"serverTypeList":[{"serverDesc":"维修保养","serverId":"YZcompcfwlxwxbywxby"},{"serverDesc":"钣金喷漆","serverId":"YZcompcfwlxwxbybjpq"},{"serverDesc":"更换电瓶","serverId":"YZcompcfwlxwxbyghdc"},{"serverDesc":"洗车","serverId":"YZcompcfwlxxcxc"},{"serverDesc":"product_beauty","serverId":"YZcompcfwlxxcmr"}],"comp_id":"24","staffList":[{"staff_name":"苍老师","staff_info":"sdfsfs","staff_id":"5","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"},{"staff_name":"波多老师","staff_info":"sdfsfs","staff_id":"6","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"},{"staff_name":"小泽老师","staff_info":"sdfsfs","staff_id":"7","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"}],"info":{"auth_comp_addr":"缎库胡同15号","auth_comp_service_type":"YZcompcfwlxwxby,YZcompcfwlxxc","auth_comp_lon":116.999618,"auth_comp_name":"嘿嘿","auth_comp_lat":36.694417,"comp_visit_count":1,"auth_audit_state":"2","comp_eval_level":5,"comp_id":"24","comp_order_count":0,"auth_comp_img_head_file_id":"dc7fcc03883c46babae332ac42ed01d3"}}}
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
         * result : {"bannerList":[{"adve_location":"04","adve_turn_type":"内部url","adve_name":"新出炉广告","adve_state":"1","adve_turn_url":"123","adve_oper_user_id":"6","adve_id":"17","adve_file_id":"85de4bbb685f40daad7c46fa783308aa","adve_turn_android_url":"123","adve_order":2,"adve_turn_ios_url":"123","adve_oper_date":"2018-04-17 20:56:54"}],"serverTypeList":[{"serverDesc":"维修保养","serverId":"YZcompcfwlxwxbywxby"},{"serverDesc":"钣金喷漆","serverId":"YZcompcfwlxwxbybjpq"},{"serverDesc":"更换电瓶","serverId":"YZcompcfwlxwxbyghdc"},{"serverDesc":"洗车","serverId":"YZcompcfwlxxcxc"},{"serverDesc":"product_beauty","serverId":"YZcompcfwlxxcmr"}],"comp_id":"24","staffList":[{"staff_name":"苍老师","staff_info":"sdfsfs","staff_id":"5","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"},{"staff_name":"波多老师","staff_info":"sdfsfs","staff_id":"6","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"},{"staff_name":"小泽老师","staff_info":"sdfsfs","staff_id":"7","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"}],"info":{"auth_comp_addr":"缎库胡同15号","auth_comp_service_type":"YZcompcfwlxwxby,YZcompcfwlxxc","auth_comp_lon":116.999618,"auth_comp_name":"嘿嘿","auth_comp_lat":36.694417,"comp_visit_count":1,"auth_audit_state":"2","comp_eval_level":5,"comp_id":"24","comp_order_count":0,"auth_comp_img_head_file_id":"dc7fcc03883c46babae332ac42ed01d3"}}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * bannerList : [{"adve_location":"04","adve_turn_type":"内部url","adve_name":"新出炉广告","adve_state":"1","adve_turn_url":"123","adve_oper_user_id":"6","adve_id":"17","adve_file_id":"85de4bbb685f40daad7c46fa783308aa","adve_turn_android_url":"123","adve_order":2,"adve_turn_ios_url":"123","adve_oper_date":"2018-04-17 20:56:54"}]
             * serverTypeList : [{"serverDesc":"维修保养","serverId":"YZcompcfwlxwxbywxby"},{"serverDesc":"钣金喷漆","serverId":"YZcompcfwlxwxbybjpq"},{"serverDesc":"更换电瓶","serverId":"YZcompcfwlxwxbyghdc"},{"serverDesc":"洗车","serverId":"YZcompcfwlxxcxc"},{"serverDesc":"product_beauty","serverId":"YZcompcfwlxxcmr"}]
             * comp_id : 24
             * staffList : [{"staff_name":"苍老师","staff_info":"sdfsfs","staff_id":"5","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"},{"staff_name":"波多老师","staff_info":"sdfsfs","staff_id":"6","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"},{"staff_name":"小泽老师","staff_info":"sdfsfs","staff_id":"7","staff_photo_file_id":"dc7fcc03883c46babae332ac42ed01d3"}]
             * info : {"auth_comp_addr":"缎库胡同15号","auth_comp_service_type":"YZcompcfwlxwxby,YZcompcfwlxxc","auth_comp_lon":116.999618,"auth_comp_name":"嘿嘿","auth_comp_lat":36.694417,"comp_visit_count":1,"auth_audit_state":"2","comp_eval_level":5,"comp_id":"24","comp_order_count":0,"auth_comp_img_head_file_id":"dc7fcc03883c46babae332ac42ed01d3"}
             */

            private String comp_id;
            private InfoBean info;
            private List<BannerListBean> bannerList;
            private List<ServerTypeListBean> serverTypeList;
            private List<StaffListBean> staffList;

            public String getComp_id() {
                return comp_id;
            }

            public void setComp_id(String comp_id) {
                this.comp_id = comp_id;
            }

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public List<BannerListBean> getBannerList() {
                return bannerList;
            }

            public void setBannerList(List<BannerListBean> bannerList) {
                this.bannerList = bannerList;
            }

            public List<ServerTypeListBean> getServerTypeList() {
                return serverTypeList;
            }

            public void setServerTypeList(List<ServerTypeListBean> serverTypeList) {
                this.serverTypeList = serverTypeList;
            }

            public List<StaffListBean> getStaffList() {
                return staffList;
            }

            public void setStaffList(List<StaffListBean> staffList) {
                this.staffList = staffList;
            }

            public static class InfoBean {
                /**
                 * auth_comp_addr : 缎库胡同15号
                 * auth_comp_service_type : YZcompcfwlxwxby,YZcompcfwlxxc
                 * auth_comp_lon : 116.999618
                 * auth_comp_name : 嘿嘿
                 * auth_comp_lat : 36.694417
                 * comp_visit_count : 1
                 * auth_audit_state : 2
                 * comp_eval_level : 5
                 * comp_id : 24
                 * comp_order_count : 0
                 * auth_comp_img_head_file_id : dc7fcc03883c46babae332ac42ed01d3
                 */

                private String auth_comp_addr;
                private String auth_comp_service_type;
                private double auth_comp_lon;
                private String auth_comp_name;
                private double auth_comp_lat;
                private int comp_visit_count;
                private String auth_audit_state;
                private int comp_eval_level;
                private String comp_id;
                private int comp_order_count;
                private String auth_comp_img_head_file_id;

                public String getAuth_comp_addr() {
                    return auth_comp_addr;
                }

                public void setAuth_comp_addr(String auth_comp_addr) {
                    this.auth_comp_addr = auth_comp_addr;
                }

                public String getAuth_comp_service_type() {
                    return auth_comp_service_type;
                }

                public void setAuth_comp_service_type(String auth_comp_service_type) {
                    this.auth_comp_service_type = auth_comp_service_type;
                }

                public double getAuth_comp_lon() {
                    return auth_comp_lon;
                }

                public void setAuth_comp_lon(double auth_comp_lon) {
                    this.auth_comp_lon = auth_comp_lon;
                }

                public String getAuth_comp_name() {
                    return auth_comp_name;
                }

                public void setAuth_comp_name(String auth_comp_name) {
                    this.auth_comp_name = auth_comp_name;
                }

                public double getAuth_comp_lat() {
                    return auth_comp_lat;
                }

                public void setAuth_comp_lat(double auth_comp_lat) {
                    this.auth_comp_lat = auth_comp_lat;
                }

                public int getComp_visit_count() {
                    return comp_visit_count;
                }

                public void setComp_visit_count(int comp_visit_count) {
                    this.comp_visit_count = comp_visit_count;
                }

                public String getAuth_audit_state() {
                    return auth_audit_state;
                }

                public void setAuth_audit_state(String auth_audit_state) {
                    this.auth_audit_state = auth_audit_state;
                }

                public int getComp_eval_level() {
                    return comp_eval_level;
                }

                public void setComp_eval_level(int comp_eval_level) {
                    this.comp_eval_level = comp_eval_level;
                }

                public String getComp_id() {
                    return comp_id;
                }

                public void setComp_id(String comp_id) {
                    this.comp_id = comp_id;
                }

                public int getComp_order_count() {
                    return comp_order_count;
                }

                public void setComp_order_count(int comp_order_count) {
                    this.comp_order_count = comp_order_count;
                }

                public String getAuth_comp_img_head_file_id() {
                    return auth_comp_img_head_file_id;
                }

                public void setAuth_comp_img_head_file_id(String auth_comp_img_head_file_id) {
                    this.auth_comp_img_head_file_id = auth_comp_img_head_file_id;
                }
            }

            public static class BannerListBean {
                /**
                 * adve_location : 04
                 * adve_turn_type : 内部url
                 * adve_name : 新出炉广告
                 * adve_state : 1
                 * adve_turn_url : 123
                 * adve_oper_user_id : 6
                 * adve_id : 17
                 * adve_file_id : 85de4bbb685f40daad7c46fa783308aa
                 * adve_turn_android_url : 123
                 * adve_order : 2
                 * adve_turn_ios_url : 123
                 * adve_oper_date : 2018-04-17 20:56:54
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

            public static class ServerTypeListBean {
                /**
                 * serverDesc : 维修保养
                 * serverId : YZcompcfwlxwxbywxby
                 */

                private String serverDesc;
                private String serverId;

                public String getServerDesc() {
                    return serverDesc;
                }

                public void setServerDesc(String serverDesc) {
                    this.serverDesc = serverDesc;
                }

                public String getServerId() {
                    return serverId;
                }

                public void setServerId(String serverId) {
                    this.serverId = serverId;
                }
            }

            public static class StaffListBean {
                /**
                 * staff_name : 苍老师
                 * staff_info : sdfsfs
                 * staff_id : 5
                 * staff_photo_file_id : dc7fcc03883c46babae332ac42ed01d3
                 */

                private String staff_name;
                private String staff_info;
                private String staff_id;
                private String staff_photo_file_id;

                public String getStaff_name() {
                    return staff_name;
                }

                public void setStaff_name(String staff_name) {
                    this.staff_name = staff_name;
                }

                public String getStaff_info() {
                    return staff_info;
                }

                public void setStaff_info(String staff_info) {
                    this.staff_info = staff_info;
                }

                public String getStaff_id() {
                    return staff_id;
                }

                public void setStaff_id(String staff_id) {
                    this.staff_id = staff_id;
                }

                public String getStaff_photo_file_id() {
                    return staff_photo_file_id;
                }

                public void setStaff_photo_file_id(String staff_photo_file_id) {
                    this.staff_photo_file_id = staff_photo_file_id;
                }
            }
        }
    }
}
