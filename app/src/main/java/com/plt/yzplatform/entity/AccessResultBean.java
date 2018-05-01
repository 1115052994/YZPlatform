package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/1.
 */

public class AccessResultBean {

    /**
     * data : {"result":{"est_price_area":[{"area":"东北地区","areaid":"1","price":2.56},{"area":"东部沿海地区","areaid":"2","price":2.45},{"area":"南部沿海地区","areaid":"3","price":2.49},{"area":"黄河中游地区","areaid":"4","price":2.85},{"area":"长江中游地区","areaid":"5","price":2.58},{"area":"西南地区","areaid":"6","price":2.75},{"area":"西北地区","areaid":"7","price":2.59},{"area":"北部沿海地区","areaid":"9","price":2.45}],"est_price_result":[2.85,2.62,3.05],"est_price_year_result":[{"year":2017,"price":3.23}]}}
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
         * result : {"est_price_area":[{"area":"东北地区","areaid":"1","price":2.56},{"area":"东部沿海地区","areaid":"2","price":2.45},{"area":"南部沿海地区","areaid":"3","price":2.49},{"area":"黄河中游地区","areaid":"4","price":2.85},{"area":"长江中游地区","areaid":"5","price":2.58},{"area":"西南地区","areaid":"6","price":2.75},{"area":"西北地区","areaid":"7","price":2.59},{"area":"北部沿海地区","areaid":"9","price":2.45}],"est_price_result":[2.85,2.62,3.05],"est_price_year_result":[{"year":2017,"price":3.23}]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private List<EstPriceAreaBean> est_price_area;
            private List<Double> est_price_result;
            private List<EstPriceYearResultBean> est_price_year_result;

            public List<EstPriceAreaBean> getEst_price_area() {
                return est_price_area;
            }

            public void setEst_price_area(List<EstPriceAreaBean> est_price_area) {
                this.est_price_area = est_price_area;
            }

            public List<Double> getEst_price_result() {
                return est_price_result;
            }

            public void setEst_price_result(List<Double> est_price_result) {
                this.est_price_result = est_price_result;
            }

            public List<EstPriceYearResultBean> getEst_price_year_result() {
                return est_price_year_result;
            }

            public void setEst_price_year_result(List<EstPriceYearResultBean> est_price_year_result) {
                this.est_price_year_result = est_price_year_result;
            }

            public static class EstPriceAreaBean {
                /**
                 * area : 东北地区
                 * areaid : 1
                 * price : 2.56
                 */

                private String area;
                private String areaid;
                private double price;

                public String getArea() {
                    return area;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public String getAreaid() {
                    return areaid;
                }

                public void setAreaid(String areaid) {
                    this.areaid = areaid;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }
            }

            public static class EstPriceYearResultBean {
                /**
                 * year : 2017
                 * price : 3.23
                 */

                private int year;
                private double price;

                public int getYear() {
                    return year;
                }

                public void setYear(int year) {
                    this.year = year;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }
            }
        }
    }
}
