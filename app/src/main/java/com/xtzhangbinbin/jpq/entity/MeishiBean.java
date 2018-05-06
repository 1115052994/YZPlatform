package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/3.
 */

public class MeishiBean {

    /**
     * data : {"result":[{"dict_id":"YZzshmsdg","dict_desc":"蛋糕","dict_value":"dg","dict_10":"41fb8dc27831416386c85aaf59484029"},{"dict_id":"YZzshmshg","dict_desc":"火锅","dict_value":"hg","dict_10":"c9b27a10ca884c9cafd589180d758dfa"},{"dict_id":"YZzshmshx","dict_desc":"海鲜","dict_value":"hx","dict_10":"1939df9c34614c7d8cf69905a1be1cd5"},{"dict_id":"YZzshmsrhll","dict_desc":"日韩料理","dict_value":"rhll","dict_10":"a3f8d017714442ce96d7872e15c04764"},{"dict_id":"YZzshmssk","dict_desc":"烧烤","dict_value":"sk","dict_10":"2c8ffd601baa4f8ba5de6dc4710bd7a2"},{"dict_id":"YZzshmstp","dict_desc":"甜品","dict_value":"tp","dict_10":"f5dd0665348c4a518703080bb5cbc8fa"},{"dict_id":"YZzshmsxc","dict_desc":"西餐","dict_value":"xc","dict_10":"70c3a562303a4723ae460a9efc9b847e"},{"dict_id":"YZzshmszzc","dict_desc":"自助餐","dict_value":"zzc","dict_10":"b73771606b2f4b048028263a66bc44b9"}]}
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
             * dict_id : YZzshmsdg
             * dict_desc : 蛋糕
             * dict_value : dg
             * dict_10 : 41fb8dc27831416386c85aaf59484029
             */

            private String dict_id;
            private String dict_desc;
            private String dict_value;
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

            public String getDict_10() {
                return dict_10;
            }

            public void setDict_10(String dict_10) {
                this.dict_10 = dict_10;
            }
        }
    }
}
