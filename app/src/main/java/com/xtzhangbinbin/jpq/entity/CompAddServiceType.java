package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by glp on 2018/4/21.
 * 描述：
 */

public class CompAddServiceType {

    /**
     * data : {"result":[{"suppostList":["玻璃维修","补胎(冷补)","底盘装甲","发动机内部清洗","更换大灯灯泡","更换电瓶","更换防冻液","更换火花塞","更换后刹车盘(2片)","更换机油、机滤","更换减震器","更换空气滤清器","更换空调滤清器","更换空调滤清器(外置)","更换空调制冷剂","更换PM2.5滤芯","更换汽车尾灯","更换前刹车片(4片)","更换燃油滤清器(外置)","更换燃油滤清器(内置)","更换前刹车盘(2片)","更换后刹车片(4片)","更换刹车油","更换手动变速箱油","更换雨刮片(2只)","更换自动变速箱油","更换助力转向油","节气门清洗","进气系统清洗","空调管路清洗","轮胎安装(18寸以上)","轮胎安装(防爆胎)","轮胎安装(普通)","轮胎换位","喷油嘴清洗","全车安全检测","燃油系统养护","刹车系统养护","四轮定位","上门换轮胎","三元催化清洗服务"],"typeName":"维修保养"},{"suppostList":["超宝安全出行礼包","超宝进口保养礼包","超宝镀晶大礼包","超值美容","臭氧消毒","发动机外部镀膜","光触媒消毒","精细洗车","局部沥青清洗","局部抛光","空调除臭","轮毂镀膜","内饰镀膜","内饰清洗","普通洗车","全车玻璃雨膜","全车打蜡","全车镀晶","全车镀膜","全车封釉","全车抛光","贴膜","洗车次卡套餐","新年美容套餐","雨刮水添加","蒸汽桑拿"],"typeName":"洗车美容"},{"suppostList":["后保险杠","后车尾","前保险杠","前车盖","全车喷漆"],"typeName":"钣金维修"}]}
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
             * suppostList : ["玻璃维修","补胎(冷补)","底盘装甲","发动机内部清洗","更换大灯灯泡","更换电瓶","更换防冻液","更换火花塞","更换后刹车盘(2片)","更换机油、机滤","更换减震器","更换空气滤清器","更换空调滤清器","更换空调滤清器(外置)","更换空调制冷剂","更换PM2.5滤芯","更换汽车尾灯","更换前刹车片(4片)","更换燃油滤清器(外置)","更换燃油滤清器(内置)","更换前刹车盘(2片)","更换后刹车片(4片)","更换刹车油","更换手动变速箱油","更换雨刮片(2只)","更换自动变速箱油","更换助力转向油","节气门清洗","进气系统清洗","空调管路清洗","轮胎安装(18寸以上)","轮胎安装(防爆胎)","轮胎安装(普通)","轮胎换位","喷油嘴清洗","全车安全检测","燃油系统养护","刹车系统养护","四轮定位","上门换轮胎","三元催化清洗服务"]
             * typeName : 维修保养
             */

            private String typeName;
            private List<String> suppostList;

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public List<String> getSuppostList() {
                return suppostList;
            }

            public void setSuppostList(List<String> suppostList) {
                this.suppostList = suppostList;
            }
        }
    }
}
