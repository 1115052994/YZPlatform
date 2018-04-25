package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/21.
 */

public class QueryCollectServer {

    /**
     * data : {"result":[{"coll_id":"5","auth_comp_addr":"缎库胡同15号","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","auth_comp_phone":"4444","comp_id":"24","auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420"},{"auth_comp_addr":"缎库胡同15号","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","auth_comp_phone":"4444","coll_id":"8","comp_id":"24","auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420"},{"auth_comp_addr":"缎库胡同15号","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","auth_comp_phone":"4444","coll_id":"9","comp_id":"24","auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420"}],"pageCount":1,"pageTotal":3}
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
         * result : [{"coll_id":"5"},{"auth_comp_addr":"缎库胡同15号","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","auth_comp_phone":"4444","coll_id":"8","comp_id":"24","auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420"},{"auth_comp_addr":"缎库胡同15号","auth_id":"60","auth_comp_name":"山东派乐特网络科技有限公司","auth_comp_phone":"4444","coll_id":"9","comp_id":"24","auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420"}]
         * pageCount : 1
         * pageTotal : 3
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
             * coll_id : 5
             * auth_comp_addr : 缎库胡同15号
             * auth_id : 60
             * auth_comp_name : 山东派乐特网络科技有限公司
             * auth_comp_phone : 4444
             * comp_id : 24
             * auth_comp_img_head_file_id : f17e89777dc64ba899db620d4c3d8420
             */

            private String coll_id;
            private String auth_comp_addr;
            private String auth_id;
            private String auth_comp_name;
            private String auth_comp_phone;
            private String comp_id;
            private String auth_comp_img_head_file_id;

            public String getColl_id() {
                return coll_id;
            }

            public void setColl_id(String coll_id) {
                this.coll_id = coll_id;
            }

            public String getAuth_comp_addr() {
                return auth_comp_addr;
            }

            public void setAuth_comp_addr(String auth_comp_addr) {
                this.auth_comp_addr = auth_comp_addr;
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

            public String getAuth_comp_phone() {
                return auth_comp_phone;
            }

            public void setAuth_comp_phone(String auth_comp_phone) {
                this.auth_comp_phone = auth_comp_phone;
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
        }
    }
}
