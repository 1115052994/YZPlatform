package com.plt.yzplatform.entity;

import java.util.List;

public class WeixiuBean {

    /**
     * data : {"result":[{"dict_desc":"维修保养","prod_oper_date":"2018-04-16 12:13:39","prod_service_name":"111","prod_service_type_item":"YZcompcfwlxwxbywxby","prod_reduced_price":111,"prod_price":11,"proItemList":[{"item_id":"1","item_price":11,"item_oper_date":"2018-04-16 20:08:25","item_name":"dfadf","comp_id":"24","prod_id":"1"}],"comp_id":"24","prod_id":"1"},{"dict_desc":"维修保养","prod_oper_date":"2018-04-18 12:13:18","prod_service_name":"asdffff","prod_service_type_item":"YZcompcfwlxwxbywxby","prod_reduced_price":9999999.3,"prod_price":200.2,"proItemList":[{"item_id":"10","item_price":100.1,"item_oper_date":"2018-04-18 12:13:18","item_name":"凯美瑞","comp_id":"24","prod_id":"11"},{"item_id":"9","item_price":100.1,"item_oper_date":"2018-04-18 12:13:17","item_name":"凯美瑞","comp_id":"24","prod_id":"11"}],"comp_id":"24","prod_id":"11"},{"dict_desc":"维修保养","prod_oper_date":"2018-04-18 11:07:13","prod_service_name":"666","prod_service_type_item":"YZcompcfwlxwxbywxby","prod_reduced_price":1.0E7,"prod_price":200.2,"proItemList":[{"item_id":"5","item_price":100.1,"item_oper_date":"2018-04-18 11:07:13","item_name":"凯美瑞","comp_id":"24","prod_id":"9"},{"item_id":"6","item_price":100.1,"item_oper_date":"2018-04-18 11:07:13","item_name":"凯美瑞","comp_id":"24","prod_id":"9"}],"comp_id":"24","prod_id":"9"}]}
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
             * dict_desc : 维修保养
             * prod_oper_date : 2018-04-16 12:13:39
             * prod_service_name : 111
             * prod_service_type_item : YZcompcfwlxwxbywxby
             * prod_reduced_price : 111.0
             * prod_price : 11.0
             * proItemList : [{"item_id":"1","item_price":11,"item_oper_date":"2018-04-16 20:08:25","item_name":"dfadf","comp_id":"24","prod_id":"1"}]
             * comp_id : 24
             * prod_id : 1
             */

            private String dict_desc;
            private String prod_oper_date;
            private String prod_service_name;
            private String prod_service_type_item;
            private double prod_reduced_price;
            private double prod_price;
            private String comp_id;
            private String prod_id;
            private List<ProItemListBean> proItemList;

            public String getDict_desc() {
                return dict_desc;
            }

            public void setDict_desc(String dict_desc) {
                this.dict_desc = dict_desc;
            }

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

            public List<ProItemListBean> getProItemList() {
                return proItemList;
            }

            public void setProItemList(List<ProItemListBean> proItemList) {
                this.proItemList = proItemList;
            }

            public static class ProItemListBean {
                /**
                 * item_id : 1
                 * item_price : 11.0
                 * item_oper_date : 2018-04-16 20:08:25
                 * item_name : dfadf
                 * comp_id : 24
                 * prod_id : 1
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
            }
        }
    }
}
