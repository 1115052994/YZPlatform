package com.plt.yzplatform.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by glp on 2018/4/30.
 * 描述：
 */

public class PriceWarn {

    /**
     * data : {"result":{"2018-04-18":[{"car_name":"奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版","price":10000,"dp_oper_date":"2018-04-18 20:21:03","dp_state":"no","car_sign_date":"2011-10-26","dp_id":"21","car_mileage":800000,"dp_price":"10001","depreciate":"1"},{"car_name":"奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版","price":10000,"dp_oper_date":"2018-04-18 20:21:04","dp_state":"no","car_sign_date":"2011-10-26","dp_id":"23344","car_mileage":800000,"dp_price":"","depreciate":"暂未降价"}],"2018-04-07":[{"car_name":"奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版","price":10000,"dp_oper_date":"2018-04-07 20:21:06","dp_state":"no","car_sign_date":"2011-10-26","dp_id":"2131","car_mileage":800000,"dp_price":"","depreciate":"暂未降价"}]}}
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
         * result : {"2018-04-18":[{"car_name":"奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版","price":10000,"dp_oper_date":"2018-04-18 20:21:03","dp_state":"no","car_sign_date":"2011-10-26","dp_id":"21","car_mileage":800000,"dp_price":"10001","depreciate":"1"},{"car_name":"奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版","price":10000,"dp_oper_date":"2018-04-18 20:21:04","dp_state":"no","car_sign_date":"2011-10-26","dp_id":"23344","car_mileage":800000,"dp_price":"","depreciate":"暂未降价"}],"2018-04-07":[{"car_name":"奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版","price":10000,"dp_oper_date":"2018-04-07 20:21:06","dp_state":"no","car_sign_date":"2011-10-26","dp_id":"2131","car_mileage":800000,"dp_price":"","depreciate":"暂未降价"}]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            @SerializedName("2018-04-18")
            private List<_$20180418Bean> _$20180418;
            @SerializedName("2018-04-07")
            private List<_$20180407Bean> _$20180407;

            public List<_$20180418Bean> get_$20180418() {
                return _$20180418;
            }

            public void set_$20180418(List<_$20180418Bean> _$20180418) {
                this._$20180418 = _$20180418;
            }

            public List<_$20180407Bean> get_$20180407() {
                return _$20180407;
            }

            public void set_$20180407(List<_$20180407Bean> _$20180407) {
                this._$20180407 = _$20180407;
            }

            public static class _$20180418Bean {
                /**
                 * car_name : 奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版
                 * price : 10000
                 * dp_oper_date : 2018-04-18 20:21:03
                 * dp_state : no
                 * car_sign_date : 2011-10-26
                 * dp_id : 21
                 * car_mileage : 800000
                 * dp_price : 10001
                 * depreciate : 1
                 */

                private String car_name;
                private int price;
                private String dp_oper_date;
                private String dp_state;
                private String car_sign_date;
                private String dp_id;
                private int car_mileage;
                private String dp_price;
                private String depreciate;

                public String getCar_name() {
                    return car_name;
                }

                public void setCar_name(String car_name) {
                    this.car_name = car_name;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public String getDp_oper_date() {
                    return dp_oper_date;
                }

                public void setDp_oper_date(String dp_oper_date) {
                    this.dp_oper_date = dp_oper_date;
                }

                public String getDp_state() {
                    return dp_state;
                }

                public void setDp_state(String dp_state) {
                    this.dp_state = dp_state;
                }

                public String getCar_sign_date() {
                    return car_sign_date;
                }

                public void setCar_sign_date(String car_sign_date) {
                    this.car_sign_date = car_sign_date;
                }

                public String getDp_id() {
                    return dp_id;
                }

                public void setDp_id(String dp_id) {
                    this.dp_id = dp_id;
                }

                public int getCar_mileage() {
                    return car_mileage;
                }

                public void setCar_mileage(int car_mileage) {
                    this.car_mileage = car_mileage;
                }

                public String getDp_price() {
                    return dp_price;
                }

                public void setDp_price(String dp_price) {
                    this.dp_price = dp_price;
                }

                public String getDepreciate() {
                    return depreciate;
                }

                public void setDepreciate(String depreciate) {
                    this.depreciate = depreciate;
                }
            }

            public static class _$20180407Bean {
                /**
                 * car_name : 奔驰V级  2017款 V 260 L 自动挡 防弹 氮气弹射 自动驾驶 核反应堆 臻藏版
                 * price : 10000
                 * dp_oper_date : 2018-04-07 20:21:06
                 * dp_state : no
                 * car_sign_date : 2011-10-26
                 * dp_id : 2131
                 * car_mileage : 800000
                 * dp_price :
                 * depreciate : 暂未降价
                 */

                private String car_name;
                private int price;
                private String dp_oper_date;
                private String dp_state;
                private String car_sign_date;
                private String dp_id;
                private int car_mileage;
                private String dp_price;
                private String depreciate;

                public String getCar_name() {
                    return car_name;
                }

                public void setCar_name(String car_name) {
                    this.car_name = car_name;
                }

                public int getPrice() {
                    return price;
                }

                public void setPrice(int price) {
                    this.price = price;
                }

                public String getDp_oper_date() {
                    return dp_oper_date;
                }

                public void setDp_oper_date(String dp_oper_date) {
                    this.dp_oper_date = dp_oper_date;
                }

                public String getDp_state() {
                    return dp_state;
                }

                public void setDp_state(String dp_state) {
                    this.dp_state = dp_state;
                }

                public String getCar_sign_date() {
                    return car_sign_date;
                }

                public void setCar_sign_date(String car_sign_date) {
                    this.car_sign_date = car_sign_date;
                }

                public String getDp_id() {
                    return dp_id;
                }

                public void setDp_id(String dp_id) {
                    this.dp_id = dp_id;
                }

                public int getCar_mileage() {
                    return car_mileage;
                }

                public void setCar_mileage(int car_mileage) {
                    this.car_mileage = car_mileage;
                }

                public String getDp_price() {
                    return dp_price;
                }

                public void setDp_price(String dp_price) {
                    this.dp_price = dp_price;
                }

                public String getDepreciate() {
                    return depreciate;
                }

                public void setDepreciate(String depreciate) {
                    this.depreciate = depreciate;
                }
            }
        }
    }
}
