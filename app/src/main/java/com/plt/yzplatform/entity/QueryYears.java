package com.plt.yzplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/27.
 */

public class QueryYears {


    /**
     * data : {"result":[{"dict_4":"2017","dict_desc":"2017款 1.5L 自动豪华型"},{"dict_4":"2017","dict_desc":"2017款 1.5L 自动舒适型"},{"dict_4":"2017","dict_desc":"2017款 1.4L 手动时尚型"},{"dict_4":"2017","dict_desc":"2017款 1.4L 手动舒适型"},{"dict_4":"2017","dict_desc":"2017款 1.5L 手动时尚型"},{"dict_4":"2017","dict_desc":"2017款 1.5L 自动时尚型"},{"dict_4":"2017","dict_desc":"2017款 1.5L 手动舒适型"},{"dict_4":"2017","dict_desc":"2017款 1.5L 手动豪华型"},{"dict_4":"2017","dict_desc":"2017款 1.4TSI 自动运动版"},{"dict_4":"2016","dict_desc":"2016款 1.6L 25周年纪念版"},{"dict_4":"2015","dict_desc":"2015款 1.4TSI 自动运动型"},{"dict_4":"2015","dict_desc":"2015款 1.4L 手动时尚型"},{"dict_4":"2015","dict_desc":"2015款 1.4L 手动舒适型"},{"dict_4":"2015","dict_desc":"2015款 1.6L 手动时尚型"},{"dict_4":"2015","dict_desc":"2015款 1.6L 自动时尚型"},{"dict_4":"2015","dict_desc":"2015款 1.6L 手动舒适型"},{"dict_4":"2015","dict_desc":"2015款 1.6L 自动舒适型"},{"dict_4":"2015","dict_desc":"2015款 1.6L 手动豪华型"},{"dict_4":"2015","dict_desc":"2015款 1.6L 自动豪华型"},{"dict_4":"2015","dict_desc":"2015款 质惠版 1.6L 手动时尚型"},{"dict_4":"2015","dict_desc":"2015款 质惠版 1.6L 自动时尚型"},{"dict_4":"2015","dict_desc":"2015款 质惠版 1.6L 手动舒适型"},{"dict_4":"2015","dict_desc":"2015款 质惠版 1.6L 自动舒适型"},{"dict_4":"2015","dict_desc":"2015款 质惠版 1.4TSI 自动运动型"},{"dict_4":"2015","dict_desc":"2015款 质惠版 1.4L 手动时尚型"},{"dict_4":"2015","dict_desc":"2015款 质惠版 1.4L 手动舒适型"},{"dict_4":"2013","dict_desc":"2013款 1.6L 自动豪华型"},{"dict_4":"2013","dict_desc":"2013款 1.4L 手动时尚型"},{"dict_4":"2013","dict_desc":"2013款 1.4L 手动舒适型"},{"dict_4":"2013","dict_desc":"2013款 1.6L 手动时尚型"},{"dict_4":"2013","dict_desc":"2013款 1.6L 手动舒适型"},{"dict_4":"2013","dict_desc":"2013款 1.6L 自动舒适型"},{"dict_4":"2013","dict_desc":"2013款 1.6L 手动豪华型"},{"dict_4":"2013","dict_desc":"2013款 1.6L 自动时尚型"},{"dict_4":"2012","dict_desc":"2012款 1.6L 纪念版"},{"dict_4":"2012","dict_desc":"2012款 1.6L 典藏版"},{"dict_4":"2012","dict_desc":"2012款 1.6L 前卫"},{"dict_4":"2010","dict_desc":"2010款 1.6L 伙伴"},{"dict_4":"2010","dict_desc":"2010款 1.9L 柴油先锋"},{"dict_4":"2010","dict_desc":"2010款 1.6L 前卫"},{"dict_4":"2008","dict_desc":"2008款 CIF 春天型"},{"dict_4":"2008","dict_desc":"2008款 CIF+AT 春天型"},{"dict_4":"2008","dict_desc":"2008款 CIX-P伙伴型"},{"dict_4":"2008","dict_desc":"2008款 GDF-V 1.9L 柴油先锋"},{"dict_4":"2008","dict_desc":"2008款 GIF 前卫型"},{"dict_4":"2008","dict_desc":"2008款 GIF+AT 新前卫"},{"dict_4":"2008","dict_desc":"2008款 CiF-P 伙伴"},{"dict_4":"2008","dict_desc":"2008款 GDF-P 1.9L 柴油先锋"},{"dict_4":"2008","dict_desc":"2008款 GiF 前卫"},{"dict_4":"2008","dict_desc":"2008款 GiF+AT 前卫"},{"dict_4":"2007","dict_desc":"2007款 CIX 伙伴豪华型"},{"dict_4":"2006","dict_desc":"2006款 GIF 前卫百万纪念版"},{"dict_4":"2005","dict_desc":"2005款 CIF 基本型"},{"dict_4":"2005","dict_desc":"2005款 GIF 前卫豪华型"},{"dict_4":"2005","dict_desc":"2005款 CIF 伙伴"},{"dict_4":"2005","dict_desc":"2005款 CIF 舒适型"},{"dict_4":"2004","dict_desc":"2004款 GDF 豪华型"}]}
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
             * dict_4 : 2017
             * dict_desc : 2017款 1.5L 自动豪华型
             */

            private String dict_4;
            private String dict_desc;

            public String getDict_4() {
                return dict_4;
            }

            public void setDict_4(String dict_4) {
                this.dict_4 = dict_4;
            }

            public String getDict_desc() {
                return dict_desc;
            }

            public void setDict_desc(String dict_desc) {
                this.dict_desc = dict_desc;
            }
        }
    }
}
