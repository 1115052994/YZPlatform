package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class SupermarketBean {


    /**
     * data : {"result":[{"dict_id":"YZyzqcfwzhjcpxxbb","dict_desc":"宝贝太平\u2013少儿综合保障计划","dict_value":"bb","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/078dadc791254df7864ad4cb13c8f172?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxbbye","dict_desc":"宝贝太平\u2013幼儿综合保障计划","dict_value":"bbye","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/34497329dcb5412b94e824305f235c0c?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxggjtyw","dict_desc":"中美大都会水陆公共交通意外伤害保险经典版","dict_value":"ggjtyw","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/fc3baa8ecae54610896a9af80bbdf3bd?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxgryw","dict_desc":"华泰财险个人综合意外保障","dict_value":"gryw","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/fbe10df49bc3435bbf63ecd5c21f0358?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxgrywjt","dict_desc":"华泰财险个人综合意外交通保障","dict_value":"grywjt","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/3924a502e7d94c8981e2d9509f870f33?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxh5","dict_desc":"客户订单查询H5页面地址","dict_value":"h5","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/queryorder?akey=acf4075bbf0b40599a60e6db9527da63"},{"dict_id":"YZyzqcfwzhjcpxxhwlx","dict_desc":"美亚乐悠游海外旅行保障计划","dict_value":"hwlx","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/e87a85755e8e4897a2ad294a3f11283d?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxjcx","dict_desc":"阳光家庭无忧家财险","dict_value":"jcx","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/7f773b3e8edf46a19ea6a0a3073a1fd5?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxjcyw","dict_desc":"中美大都会自驾车意外伤害保险基础版","dict_value":"jcyw","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/c5da0e7d13e7448ca2117d22a7f3f35a?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxjnly","dict_desc":"美亚\u201c畅游神州\u201d境内旅行保障计划","dict_value":"jnly","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/e87a85755e8e4897a2ad294a3f11283d?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"}],"pageCount":2,"pageTotal":16}
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
         * result : [{"dict_id":"YZyzqcfwzhjcpxxbb","dict_desc":"宝贝太平\u2013少儿综合保障计划","dict_value":"bb","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/078dadc791254df7864ad4cb13c8f172?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxbbye","dict_desc":"宝贝太平\u2013幼儿综合保障计划","dict_value":"bbye","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/34497329dcb5412b94e824305f235c0c?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxggjtyw","dict_desc":"中美大都会水陆公共交通意外伤害保险经典版","dict_value":"ggjtyw","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/fc3baa8ecae54610896a9af80bbdf3bd?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxgryw","dict_desc":"华泰财险个人综合意外保障","dict_value":"gryw","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/fbe10df49bc3435bbf63ecd5c21f0358?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxgrywjt","dict_desc":"华泰财险个人综合意外交通保障","dict_value":"grywjt","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/3924a502e7d94c8981e2d9509f870f33?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxh5","dict_desc":"客户订单查询H5页面地址","dict_value":"h5","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/queryorder?akey=acf4075bbf0b40599a60e6db9527da63"},{"dict_id":"YZyzqcfwzhjcpxxhwlx","dict_desc":"美亚乐悠游海外旅行保障计划","dict_value":"hwlx","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/e87a85755e8e4897a2ad294a3f11283d?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxjcx","dict_desc":"阳光家庭无忧家财险","dict_value":"jcx","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/7f773b3e8edf46a19ea6a0a3073a1fd5?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxjcyw","dict_desc":"中美大都会自驾车意外伤害保险基础版","dict_value":"jcyw","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/c5da0e7d13e7448ca2117d22a7f3f35a?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"},{"dict_id":"YZyzqcfwzhjcpxxjnly","dict_desc":"美亚\u201c畅游神州\u201d境内旅行保障计划","dict_value":"jnly","dict_parent_id":"YZyzqcfwzhjcpxx","dict_10":"https://www.700du.cn/m/index.html#/product/e87a85755e8e4897a2ad294a3f11283d?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5"}]
         * pageCount : 2
         * pageTotal : 16
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
             * dict_id : YZyzqcfwzhjcpxxbb
             * dict_desc : 宝贝太平–少儿综合保障计划
             * dict_value : bb
             * dict_parent_id : YZyzqcfwzhjcpxx
             * dict_10 : https://www.700du.cn/m/index.html#/product/078dadc791254df7864ad4cb13c8f172?isShowHead=1&isShowPrice=0&akey=acf4075bbf0b40599a60e6db9527da63&fromType=5
             */

            private String dict_id;
            private String dict_desc;
            private String dict_value;
            private String dict_parent_id;
            private String dict_10;

            public String getDict_id() {
                return dict_id;
            }

            public void setDict_id(String dict_id) {
                this.dict_id = dict_id;
            }

            public String getDict_desc() {
                return dict_desc;
            }

            public void setDict_desc(String dict_desc) {
                this.dict_desc = dict_desc;
            }

            public String getDict_value() {
                return dict_value;
            }

            public void setDict_value(String dict_value) {
                this.dict_value = dict_value;
            }

            public String getDict_parent_id() {
                return dict_parent_id;
            }

            public void setDict_parent_id(String dict_parent_id) {
                this.dict_parent_id = dict_parent_id;
            }

            public String getDict_10() {
                return dict_10;
            }

            public void setDict_10(String dict_10) {
                this.dict_10 = dict_10;
            }
        }
    }
}
