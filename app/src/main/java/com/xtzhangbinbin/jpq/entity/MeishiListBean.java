package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/4.
 */

public class MeishiListBean {

    /**
     * data : {"result":[{"zsh_avg_price":1,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C0%2C640%2C640%3Bw%3D470%3Bq%3D99%3Bc%3Dnuomi%2C95%2C95/sign=a0ec57f9ba119313d30ca5f0580820e7/9358d109b3de9c8222f760b46481800a19d8437f.jpg","zsh_name":"优朵滋","zsh_addr":"合肥市经开区幸福大街10栋202号门面店","zsh_phone":"18655138028|18656952128","zsh_level":0,"zsh_id":"039f368488464c54a91d76bddc14904e","zsh_eval_count":0},{"zsh_avg_price":120,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C2%2C470%2C285%3Bw%3D470%3Bq%3D79/sign=b0634e630a2442a7ba41a7e5ec738179/0df431adcbef76096504ed7f2bdda3cc7dd99ec6.jpg","zsh_name":"钻客蛋糕(南七店)","zsh_addr":"合肥市蜀山区金寨路与黄山路交口西50米路南（科技大学对面黄金广场）","zsh_phone":"4006591520|055164421992","zsh_level":0,"zsh_id":"06979d3cfc2a4d39a25721c04bb612ad","zsh_eval_count":0},{"zsh_avg_price":24,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C93%2C470%2C285%3Bw%3D470%3Bq%3D99/sign=62407ea05182b2b7b3d063840c9de7d6/5d6034a85edf8db108179ac00123dd54564e745d.jpg","zsh_name":"瑞可爷爷的店(名邦广场店)","zsh_addr":"合肥市肥西县上派镇上派镇金寨路与站前路交口名邦广场内三楼318号(中影名邦影城)","zsh_phone":"13695609537","zsh_level":4.7,"zsh_id":"074ca8b1ed4d480ab009b9bc41efe87b","zsh_eval_count":285},{"zsh_avg_price":200,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D68%2C57%2C505%2C336%3Bw%3D470%3Bq%3D99%3Bc%3Dnuomi%2C95%2C95/sign=857383b230dbb6fd3114bf6634129334/5366d0160924ab184382d67e3cfae6cd7a890be6.jpg","zsh_name":"天使宝贝(宿松路店)","zsh_addr":"合肥市包河区宿松路与绩溪路交口西北角安徽国际商务中心A座底商","zsh_phone":"4008004667","zsh_level":4.4,"zsh_id":"12da2d9a5d2d48f3bc473728bbd43155","zsh_eval_count":19},{"zsh_avg_price":80,"zsh_img":"https://ss0.bdstatic.com/7LsWdDW5_xN3otebn9fN2DJv/bainuo/wh%3D690%2C460%3Bq%3D90%3Bc%3Dnuomi%2C95%2C95/sign=71e27b1c58da81cb4eb38bcb6b56fc22/e4dde71190ef76c67b6368c39416fdfaae5167a1.jpg","zsh_name":"优麦祺蛋糕(港汇广场店)","zsh_addr":"合肥市蜀山区潜山路与望江路交叉口东50米","zsh_phone":"13329042816|18226650582","zsh_level":4.8,"zsh_id":"16d06398caef48959d35915e53ffdd7e","zsh_eval_count":5},{"zsh_avg_price":128,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C0%2C702%2C507%3Bw%3D640%3Bq%3D84/sign=d508d1bb24738bd4d06ee8719cbbabe6/a71ea8d3fd1f413498b6ab182f1f95cad0c85ed6.jpg","zsh_name":"格蓝蛋糕","zsh_addr":"合肥市包河区宁国南路与南二环交口望湖城14号门面","zsh_phone":"15309693736","zsh_level":4.8,"zsh_id":"199f805c0753460aa201dca146071866","zsh_eval_count":26},{"zsh_avg_price":80,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C17%2C800%2C486%3Bw%3D470%3Bq%3D90/sign=87f71b33cb1b9d169e88c021ceee98be/32fa828ba61ea8d30c2efde8920a304e241f58ca.jpg","zsh_name":"米苏烘焙(安徽大学店)","zsh_addr":"合肥市蜀山区肥西路与龙河路交口安徽大学北门","zsh_phone":"15715655922","zsh_level":4.8,"zsh_id":"1b207320297a4771a67da19f2481e6c7","zsh_eval_count":21},{"zsh_avg_price":49,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/nuomi/pic/item/c2cec3fdfc039245051c25558e94a4c27d1e253b.jpg","zsh_name":"四唯蛋糕店(宁国路店)","zsh_addr":"合肥市包河区宁国路与九华山路贵美人旁边","zsh_phone":"15855133997","zsh_level":0,"zsh_id":"1d51382ad483434ca6efe189e6b48919","zsh_eval_count":0},{"zsh_avg_price":11,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/nuomi/pic/item/79f0f736afc3793149921e72e2c4b74542a911f6.jpg","zsh_name":"麦香村(合肥一店)","zsh_addr":"合肥市长丰县长合路与长丰路交口北20米","zsh_phone":"055166682856","zsh_level":4.9,"zsh_id":"1f3143ce71c84ee785b537b72e1b0dab","zsh_eval_count":9},{"zsh_avg_price":35,"zsh_img":"https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D97%2C16%2C572%2C381%3Bw%3D470%3Bq%3D89%3Bc%3Dnuomi%2C95%2C95/sign=0505ffe16b2762d09471feff9dd53fd6/f3d3572c11dfa9ec26fa16d56ad0f703908fc118.jpg","zsh_name":"巴莉甜甜(桐江小区店)","zsh_addr":"合肥市包河区桐城南路与望江东路交口向北50米路东侧桐江小区1幢106-206","zsh_phone":"4008875657|055163446036|055164655200","zsh_level":4.6,"zsh_id":"22467ff12c9c43909e429cdad1fde018","zsh_eval_count":13}]}
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
             * zsh_avg_price : 1
             * zsh_img : https://gss0.baidu.com/-4o3dSag_xI4khGko9WTAnF6hhy/bainuo/crop%3D0%2C0%2C640%2C640%3Bw%3D470%3Bq%3D99%3Bc%3Dnuomi%2C95%2C95/sign=a0ec57f9ba119313d30ca5f0580820e7/9358d109b3de9c8222f760b46481800a19d8437f.jpg
             * zsh_name : 优朵滋
             * zsh_addr : 合肥市经开区幸福大街10栋202号门面店
             * zsh_phone : 18655138028|18656952128
             * zsh_level : 0
             * zsh_id : 039f368488464c54a91d76bddc14904e
             * zsh_eval_count : 0
             */

            private String zsh_avg_price;
            private String zsh_img;
            private String zsh_name;
            private String zsh_addr;
            private String zsh_phone;
            private String zsh_level;
            private String zsh_id;
            private int zsh_eval_count;

            public String getZsh_avg_price() {
                return zsh_avg_price;
            }

            public void setZsh_avg_price(String zsh_avg_price) {
                this.zsh_avg_price = zsh_avg_price;
            }

            public String getZsh_img() {
                return zsh_img;
            }

            public void setZsh_img(String zsh_img) {
                this.zsh_img = zsh_img;
            }

            public String getZsh_name() {
                return zsh_name;
            }

            public void setZsh_name(String zsh_name) {
                this.zsh_name = zsh_name;
            }

            public String getZsh_addr() {
                return zsh_addr;
            }

            public void setZsh_addr(String zsh_addr) {
                this.zsh_addr = zsh_addr;
            }

            public String getZsh_phone() {
                return zsh_phone;
            }

            public void setZsh_phone(String zsh_phone) {
                this.zsh_phone = zsh_phone;
            }

            public String getZsh_level() {
                return zsh_level;
            }

            public void setZsh_level(String zsh_level) {
                this.zsh_level = zsh_level;
            }

            public String getZsh_id() {
                return zsh_id;
            }

            public void setZsh_id(String zsh_id) {
                this.zsh_id = zsh_id;
            }

            public int getZsh_eval_count() {
                return zsh_eval_count;
            }

            public void setZsh_eval_count(int zsh_eval_count) {
                this.zsh_eval_count = zsh_eval_count;
            }
        }
    }
}
