package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by glp on 2018/4/20.
 * 描述：
 */

public class CarServiceList {

    /**
     * data : {"result":[{"auth_comp_addr":"缎库胡同15号","auth_comp_service_type":"YZcompcfwlxwxby,YZcompcfwlxxc","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","auth_state":"2","comp_eval_level":5,"comp_id":"24","auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420","apart":889},{"auth_comp_addr":"济泺路与汽车厂东路交叉口(长途汽车总站北200米)","auth_comp_service_type":"YZcompcfwlxxc,YZcompcfwlxjx,YZcompcfwlxwxby","auth_id":"49","auth_comp_name":"北京工期公司","auth_state":"2","comp_eval_level":5,"comp_id":"19","auth_comp_img_head_file_id":"d270e68986f941e19b3ff513e95ff610","apart":116}],"pageCount":1,"pageTotal":2}
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
         * result : [{"auth_comp_addr":"缎库胡同15号","auth_comp_service_type":"YZcompcfwlxwxby,YZcompcfwlxxc","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","auth_state":"2","comp_eval_level":5,"comp_id":"24","auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420","apart":889},{"auth_comp_addr":"济泺路与汽车厂东路交叉口(长途汽车总站北200米)","auth_comp_service_type":"YZcompcfwlxxc,YZcompcfwlxjx,YZcompcfwlxwxby","auth_id":"49","auth_comp_name":"北京工期公司","auth_state":"2","comp_eval_level":5,"comp_id":"19","auth_comp_img_head_file_id":"d270e68986f941e19b3ff513e95ff610","apart":116}]
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
             * auth_comp_addr : 缎库胡同15号
             * auth_comp_service_type : YZcompcfwlxwxby,YZcompcfwlxxc
             * auth_id : 60
             * auth_comp_name : 山东派乐特网络科技有限公司
             * auth_state : 2
             * comp_eval_level : 5
             * comp_id : 24
             * auth_comp_img_head_file_id : f17e89777dc64ba899db620d4c3d8420
             * apart : 889.0
             */

            private String auth_comp_addr = "";
            private String auth_comp_service_type = "";
            private String auth_id = "";
            private String auth_comp_name = "";
            private String auth_state = "";
            private int comp_eval_level = 5;
            private String comp_id = "";
            private String auth_comp_img_head_file_id = "";
            private double apart = 0.0;

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

            public String getAuth_id() {
                return auth_id;
            }

            public void setAuth_id(String auth_id) {
                this.auth_id = auth_id;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public String getAuth_state() {
                return auth_state;
            }

            public void setAuth_state(String auth_state) {
                this.auth_state = auth_state;
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

            public String getAuth_comp_img_head_file_id() {
                return auth_comp_img_head_file_id;
            }

            public void setAuth_comp_img_head_file_id(String auth_comp_img_head_file_id) {
                this.auth_comp_img_head_file_id = auth_comp_img_head_file_id;
            }

            public double getApart() {
                return apart;
            }

            public void setApart(double apart) {
                this.apart = apart;
            }
        }
    }
}
