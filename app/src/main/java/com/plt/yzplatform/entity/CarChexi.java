package com.plt.yzplatform.entity;

import java.util.List;

public class CarChexi {

    /**
     * data : {"result":{"trainList":[{"trainId":"YZescxgescppcxdlxlcnkcxmy_1_8","trainImg":null,"trainName":"一汽-大众","carTrianerMapList":[{"brandImg":"","brandName":"宝来/宝来经典","brandId":"YZescxgescppcxdlxlcnkcxmy_1_8_15","carNum":0,"fristChar":""}]},{"trainId":"YZescxgescppcxdlxlcnkcxmy_1_50","trainImg":null,"trainName":"大众(进口)","carTrianerMapList":[{"brandImg":"e2f733dff3a04e179b806c64549e7a13","brandName":"甲壳虫","brandId":"YZescxgescppcxdlxlcnkcxmy_1_50_210","carNum":0,"fristChar":""}]},{"trainId":"YZescxgescppcxdlxlcnkcxmy_1_58","trainImg":null,"trainName":"上汽大众","carTrianerMapList":[{"brandImg":"e95eb5dc33234c3c84b7d6bac1cde3f9","brandName":"高尔","brandId":"YZescxgescppcxdlxlcnkcxmy_1_58_144","carNum":0,"fristChar":""}]}]}}
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
         * result : {"trainList":[{"trainId":"YZescxgescppcxdlxlcnkcxmy_1_8","trainImg":null,"trainName":"一汽-大众","carTrianerMapList":[{"brandImg":"","brandName":"宝来/宝来经典","brandId":"YZescxgescppcxdlxlcnkcxmy_1_8_15","carNum":0,"fristChar":""}]},{"trainId":"YZescxgescppcxdlxlcnkcxmy_1_50","trainImg":null,"trainName":"大众(进口)","carTrianerMapList":[{"brandImg":"e2f733dff3a04e179b806c64549e7a13","brandName":"甲壳虫","brandId":"YZescxgescppcxdlxlcnkcxmy_1_50_210","carNum":0,"fristChar":""}]},{"trainId":"YZescxgescppcxdlxlcnkcxmy_1_58","trainImg":null,"trainName":"上汽大众","carTrianerMapList":[{"brandImg":"e95eb5dc33234c3c84b7d6bac1cde3f9","brandName":"高尔","brandId":"YZescxgescppcxdlxlcnkcxmy_1_58_144","carNum":0,"fristChar":""}]}]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private List<TrainListBean> trainList;

            public List<TrainListBean> getTrainList() {
                return trainList;
            }

            public void setTrainList(List<TrainListBean> trainList) {
                this.trainList = trainList;
            }

            public static class TrainListBean {
                /**
                 * trainId : YZescxgescppcxdlxlcnkcxmy_1_8
                 * trainImg : null
                 * trainName : 一汽-大众
                 * carTrianerMapList : [{"brandImg":"","brandName":"宝来/宝来经典","brandId":"YZescxgescppcxdlxlcnkcxmy_1_8_15","carNum":0,"fristChar":""}]
                 */

                private String trainId;
                private Object trainImg;
                private String trainName;
                private List<CarTrianerMapListBean> carTrianerMapList;

                public String getTrainId() {
                    return trainId;
                }

                public void setTrainId(String trainId) {
                    this.trainId = trainId;
                }

                public Object getTrainImg() {
                    return trainImg;
                }

                public void setTrainImg(Object trainImg) {
                    this.trainImg = trainImg;
                }

                public String getTrainName() {
                    return trainName;
                }

                public void setTrainName(String trainName) {
                    this.trainName = trainName;
                }

                public List<CarTrianerMapListBean> getCarTrianerMapList() {
                    return carTrianerMapList;
                }

                public void setCarTrianerMapList(List<CarTrianerMapListBean> carTrianerMapList) {
                    this.carTrianerMapList = carTrianerMapList;
                }

                public static class CarTrianerMapListBean {
                    /**
                     * brandImg :
                     * brandName : 宝来/宝来经典
                     * brandId : YZescxgescppcxdlxlcnkcxmy_1_8_15
                     * carNum : 0
                     * fristChar :
                     */

                    private String brandImg;
                    private String brandName;
                    private String brandId;
                    private int carNum;
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

                    public int getCarNum() {
                        return carNum;
                    }

                    public void setCarNum(int carNum) {
                        this.carNum = carNum;
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
}
