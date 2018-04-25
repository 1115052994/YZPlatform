package com.plt.yzplatform.entity;

import java.util.List;

public class CarHotBrand {

    /**
     * data : {"result":{"hotBrandList":[{"brandImg":"709087ec45894b8e92b2739516e70dc4","brandName":"阿斯顿·马丁","brandId":"YZescxgescppcxdlxlcnkcxmy_35","fristChar":"A"},{"brandImg":"56d97567af0e4335b43954ae3d238a78","brandName":"布加迪","brandId":"YZescxgescppcxdlxlcnkcxmy_37","fristChar":"B"},{"brandImg":"84035050ba214d3b8c7478984ed6b6f0","brandName":"宾利","brandId":"YZescxgescppcxdlxlcnkcxmy_39","fristChar":"B"},{"brandImg":"2543d0921e2a4571a1fb368249115414","brandName":"保时捷","brandId":"YZescxgescppcxdlxlcnkcxmy_40","fristChar":"B"},{"brandImg":"e214977b7243424db544e187101b7a20","brandName":"法拉利","brandId":"YZescxgescppcxdlxlcnkcxmy_42","fristChar":"F"},{"brandImg":"1484d35c136948e980fdf62d9aa6017e","brandName":"兰博基尼","brandId":"YZescxgescppcxdlxlcnkcxmy_48","fristChar":"L"},{"brandImg":"d0b092e1ff4d49c1951aa33b9af6db3f","brandName":"劳斯莱斯","brandId":"YZescxgescppcxdlxlcnkcxmy_54","fristChar":"L"},{"brandImg":"1a8f8477d32d4934ae9da3d43d5e37fb","brandName":"迈巴赫","brandId":"YZescxgescppcxdlxlcnkcxmy_55","fristChar":"M"}]}}
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
         * result : {"hotBrandList":[{"brandImg":"709087ec45894b8e92b2739516e70dc4","brandName":"阿斯顿·马丁","brandId":"YZescxgescppcxdlxlcnkcxmy_35","fristChar":"A"},{"brandImg":"56d97567af0e4335b43954ae3d238a78","brandName":"布加迪","brandId":"YZescxgescppcxdlxlcnkcxmy_37","fristChar":"B"},{"brandImg":"84035050ba214d3b8c7478984ed6b6f0","brandName":"宾利","brandId":"YZescxgescppcxdlxlcnkcxmy_39","fristChar":"B"},{"brandImg":"2543d0921e2a4571a1fb368249115414","brandName":"保时捷","brandId":"YZescxgescppcxdlxlcnkcxmy_40","fristChar":"B"},{"brandImg":"e214977b7243424db544e187101b7a20","brandName":"法拉利","brandId":"YZescxgescppcxdlxlcnkcxmy_42","fristChar":"F"},{"brandImg":"1484d35c136948e980fdf62d9aa6017e","brandName":"兰博基尼","brandId":"YZescxgescppcxdlxlcnkcxmy_48","fristChar":"L"},{"brandImg":"d0b092e1ff4d49c1951aa33b9af6db3f","brandName":"劳斯莱斯","brandId":"YZescxgescppcxdlxlcnkcxmy_54","fristChar":"L"},{"brandImg":"1a8f8477d32d4934ae9da3d43d5e37fb","brandName":"迈巴赫","brandId":"YZescxgescppcxdlxlcnkcxmy_55","fristChar":"M"}]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private List<HotBrandListBean> hotBrandList;

            public List<HotBrandListBean> getHotBrandList() {
                return hotBrandList;
            }

            public void setHotBrandList(List<HotBrandListBean> hotBrandList) {
                this.hotBrandList = hotBrandList;
            }

            public static class HotBrandListBean {
                /**
                 * brandImg : 709087ec45894b8e92b2739516e70dc4
                 * brandName : 阿斯顿·马丁
                 * brandId : YZescxgescppcxdlxlcnkcxmy_35
                 * fristChar : A
                 */

                private String brandImg;
                private String brandName;
                private String brandId;
                private String fristChar;

                public String getBrandImg() {
                    return brandImg;
                }

                public void setBrandImg(String brandImg) {
                    this.brandImg = brandImg;
                }

                public String getBrandName() {
                    return brandName;
                }

                public void setBrandName(String brandName) {
                    this.brandName = brandName;
                }

                public String getBrandId() {
                    return brandId;
                }

                public void setBrandId(String brandId) {
                    this.brandId = brandId;
                }

                public String getFristChar() {
                    return fristChar;
                }

                public void setFristChar(String fristChar) {
                    this.fristChar = fristChar;
                }
            }
        }
    }
}
