package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by glp on 2018/4/22.
 * 描述：
 */

public class ShowProductDetaile {

    /**
     * data : {"result":{"prod_oper_date":"2018-04-23 10:59:26","prod_service_name":"哟哟哟","service_type_item_name_detail":"美容","prod_service_type_item":"YZcompcfwlxxcmr","prod_reduced_price":699.99,"prod_is_publish":"0","prod_price":1328.88,"productItemList":[{"item_id":"144","item_price":366,"item_oper_date":"2018-04-23 10:59:26","item_name":"更换前刹车片(4片)","comp_id":"33","prod_id":"61"},{"item_id":"145","item_price":888,"item_oper_date":"2018-04-23 10:59:26","item_name":"新年美容套餐","comp_id":"33","prod_id":"61"},{"item_id":"146","item_price":69,"item_oper_date":"2018-04-23 10:59:26","item_name":"局部沥青清洗","comp_id":"33","prod_id":"61"},{"item_id":"147","item_price":5.88,"item_oper_date":"2018-04-23 10:59:26","item_name":"发动机内部清洗","comp_id":"33","prod_id":"61"}],"comp_id":"33","prod_id":"61"}}
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
         * result : {"prod_oper_date":"2018-04-23 10:59:26","prod_service_name":"哟哟哟","service_type_item_name_detail":"美容","prod_service_type_item":"YZcompcfwlxxcmr","prod_reduced_price":699.99,"prod_is_publish":"0","prod_price":1328.88,"productItemList":[{"item_id":"144","item_price":366,"item_oper_date":"2018-04-23 10:59:26","item_name":"更换前刹车片(4片)","comp_id":"33","prod_id":"61"},{"item_id":"145","item_price":888,"item_oper_date":"2018-04-23 10:59:26","item_name":"新年美容套餐","comp_id":"33","prod_id":"61"},{"item_id":"146","item_price":69,"item_oper_date":"2018-04-23 10:59:26","item_name":"局部沥青清洗","comp_id":"33","prod_id":"61"},{"item_id":"147","item_price":5.88,"item_oper_date":"2018-04-23 10:59:26","item_name":"发动机内部清洗","comp_id":"33","prod_id":"61"}],"comp_id":"33","prod_id":"61"}
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
             * prod_oper_date : 2018-04-23 10:59:26
             * prod_service_name : 哟哟哟
             * service_type_item_name_detail : 美容
             * prod_service_type_item : YZcompcfwlxxcmr
             * prod_reduced_price : 699.99
             * prod_is_publish : 0
             * prod_price : 1328.88
             * productItemList : [{"item_id":"144","item_price":366,"item_oper_date":"2018-04-23 10:59:26","item_name":"更换前刹车片(4片)","comp_id":"33","prod_id":"61"},{"item_id":"145","item_price":888,"item_oper_date":"2018-04-23 10:59:26","item_name":"新年美容套餐","comp_id":"33","prod_id":"61"},{"item_id":"146","item_price":69,"item_oper_date":"2018-04-23 10:59:26","item_name":"局部沥青清洗","comp_id":"33","prod_id":"61"},{"item_id":"147","item_price":5.88,"item_oper_date":"2018-04-23 10:59:26","item_name":"发动机内部清洗","comp_id":"33","prod_id":"61"}]
             * comp_id : 33
             * prod_id : 61
             */

            private String prod_oper_date;
            private String prod_service_name;
            private String service_type_item_name_detail;
            private String prod_service_type_item;
            private double prod_reduced_price;
            private String prod_is_publish;
            private double prod_price;
            private String comp_id;
            private String prod_id;
            private List<ProductItemListBean> productItemList;

            public String getProd_oper_date() {
                return prod_oper_date;
            }

            public void setProd_oper_date(String prod_oper_date) {
                this.prod_oper_date = prod_oper_date;
            }

            public String getProd_service_name() {
                return prod_service_name;
            }

            public void setProd_service_name(String prod_service_name) {
                this.prod_service_name = prod_service_name;
            }

            public String getService_type_item_name_detail() {
                return service_type_item_name_detail;
            }

            public void setService_type_item_name_detail(String service_type_item_name_detail) {
                this.service_type_item_name_detail = service_type_item_name_detail;
            }

            public String getProd_service_type_item() {
                return prod_service_type_item;
            }

            public void setProd_service_type_item(String prod_service_type_item) {
                this.prod_service_type_item = prod_service_type_item;
            }

            public double getProd_reduced_price() {
                return prod_reduced_price;
            }

            public void setProd_reduced_price(double prod_reduced_price) {
                this.prod_reduced_price = prod_reduced_price;
            }

            public String getProd_is_publish() {
                return prod_is_publish;
            }

            public void setProd_is_publish(String prod_is_publish) {
                this.prod_is_publish = prod_is_publish;
            }

            public double getProd_price() {
                return prod_price;
            }

            public void setProd_price(double prod_price) {
                this.prod_price = prod_price;
            }

            public String getComp_id() {
                return comp_id;
            }

            public void setComp_id(String comp_id) {
                this.comp_id = comp_id;
            }

            public String getProd_id() {
                return prod_id;
            }

            public void setProd_id(String prod_id) {
                this.prod_id = prod_id;
            }

            public List<ProductItemListBean> getProductItemList() {
                return productItemList;
            }

            public void setProductItemList(List<ProductItemListBean> productItemList) {
                this.productItemList = productItemList;
            }

            public static class ProductItemListBean {
                /**
                 * item_id : 144
                 * item_price : 366.0
                 * item_oper_date : 2018-04-23 10:59:26
                 * item_name : 更换前刹车片(4片)
                 * comp_id : 33
                 * prod_id : 61
                 */

                private String item_id;
                private double item_price;
                private String item_oper_date;
                private String item_name;
                private String comp_id;
                private String prod_id;

                public String getItem_id() {
                    return item_id;
                }

                public void setItem_id(String item_id) {
                    this.item_id = item_id;
                }

                public double getItem_price() {
                    return item_price;
                }

                public void setItem_price(double item_price) {
                    this.item_price = item_price;
                }

                public String getItem_oper_date() {
                    return item_oper_date;
                }

                public void setItem_oper_date(String item_oper_date) {
                    this.item_oper_date = item_oper_date;
                }

                public String getItem_name() {
                    return item_name;
                }

                public void setItem_name(String item_name) {
                    this.item_name = item_name;
                }

                public String getComp_id() {
                    return comp_id;
                }

                public void setComp_id(String comp_id) {
                    this.comp_id = comp_id;
                }

                public String getProd_id() {
                    return prod_id;
                }

                public void setProd_id(String prod_id) {
                    this.prod_id = prod_id;
                }

                @Override
                public String toString() {
                    return "{" +
                            "\"item_name\""+ "=" +'\"' + item_name + '\"' +
                            ",\"item_price\"" + "=" + item_price +
                            '}';
                }
            }
        }
    }
}
