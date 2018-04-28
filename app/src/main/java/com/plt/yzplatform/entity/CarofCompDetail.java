package com.plt.yzplatform.entity;

// FIXME generate failure  field _$Status7
// FIXME generate failure  field _$Message68
// FIXME generate failure  field _$Data17
/**
 * Created by Administrator on 2018/4/27.
 */

public class CarofCompDetail {

    /**
     * data : {"result":{"comp_lat":36.694417,"on_sale_count":1002,"auth_comp_addr":"缎库胡同15号","comp_lon":116.999618,"sale_count":0,"auth_comp_name":"山东派乐特网络科技有限公司","comp_visit_count":737,"comp_eval_level":4,"auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420"}}
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
         * result : {"comp_lat":36.694417,"on_sale_count":1002,"auth_comp_addr":"缎库胡同15号","comp_lon":116.999618,"sale_count":0,"auth_comp_name":"山东派乐特网络科技有限公司","comp_visit_count":737,"comp_eval_level":4,"auth_comp_img_head_file_id":"f17e89777dc64ba899db620d4c3d8420"}
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
             * comp_lat : 36.694417
             * on_sale_count : 1002
             * auth_comp_addr : 缎库胡同15号
             * comp_lon : 116.999618
             * sale_count : 0
             * auth_comp_name : 山东派乐特网络科技有限公司
             * comp_visit_count : 737
             * comp_eval_level : 4
             * auth_comp_img_head_file_id : f17e89777dc64ba899db620d4c3d8420
             */

            private double comp_lat;
            private int on_sale_count;
            private String auth_comp_addr;
            private double comp_lon;
            private int sale_count;
            private String auth_comp_name;
            private int comp_visit_count;
            private int comp_eval_level;
            private String auth_comp_img_head_file_id;

            public double getComp_lat() {
                return comp_lat;
            }

            public void setComp_lat(double comp_lat) {
                this.comp_lat = comp_lat;
            }

            public int getOn_sale_count() {
                return on_sale_count;
            }

            public void setOn_sale_count(int on_sale_count) {
                this.on_sale_count = on_sale_count;
            }

            public String getAuth_comp_addr() {
                return auth_comp_addr;
            }

            public void setAuth_comp_addr(String auth_comp_addr) {
                this.auth_comp_addr = auth_comp_addr;
            }

            public double getComp_lon() {
                return comp_lon;
            }

            public void setComp_lon(double comp_lon) {
                this.comp_lon = comp_lon;
            }

            public int getSale_count() {
                return sale_count;
            }

            public void setSale_count(int sale_count) {
                this.sale_count = sale_count;
            }

            public String getAuth_comp_name() {
                return auth_comp_name;
            }

            public void setAuth_comp_name(String auth_comp_name) {
                this.auth_comp_name = auth_comp_name;
            }

            public int getComp_visit_count() {
                return comp_visit_count;
            }

            public void setComp_visit_count(int comp_visit_count) {
                this.comp_visit_count = comp_visit_count;
            }

            public int getComp_eval_level() {
                return comp_eval_level;
            }

            public void setComp_eval_level(int comp_eval_level) {
                this.comp_eval_level = comp_eval_level;
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
