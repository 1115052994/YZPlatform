package com.xtzhangbinbin.jpq.entity;

import java.util.List;

public class AppraiseBean {

    /**
     * data : {"result":{"service":[{"dict_id":"YZpjpjqyflfwdw","dict_desc":"服务到位"},{"dict_id":"YZpjpjqyflaqzy","dict_desc":"安全专业"},{"dict_id":"YZpjpjqyflxlfcg","dict_desc":"效率非常高"},{"dict_id":"YZpjpjqyflfywz","dict_desc":"风雨无阻"},{"dict_id":"YZpjpjqyflshjs","dict_desc":"送货即时"},{"dict_id":"YZpjpjqyflwxybz","dict_desc":"维修有保障"}],"comp_name":{"comp_name":"山东派乐特网络科技有限公司"}}}
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
         * result : {"service":[{"dict_id":"YZpjpjqyflfwdw","dict_desc":"服务到位"},{"dict_id":"YZpjpjqyflaqzy","dict_desc":"安全专业"},{"dict_id":"YZpjpjqyflxlfcg","dict_desc":"效率非常高"},{"dict_id":"YZpjpjqyflfywz","dict_desc":"风雨无阻"},{"dict_id":"YZpjpjqyflshjs","dict_desc":"送货即时"},{"dict_id":"YZpjpjqyflwxybz","dict_desc":"维修有保障"}],"comp_name":{"comp_name":"山东派乐特网络科技有限公司"}}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            /**
             * service : [{"dict_id":"YZpjpjqyflfwdw","dict_desc":"服务到位"},{"dict_id":"YZpjpjqyflaqzy","dict_desc":"安全专业"},{"dict_id":"YZpjpjqyflxlfcg","dict_desc":"效率非常高"},{"dict_id":"YZpjpjqyflfywz","dict_desc":"风雨无阻"},{"dict_id":"YZpjpjqyflshjs","dict_desc":"送货即时"},{"dict_id":"YZpjpjqyflwxybz","dict_desc":"维修有保障"}]
             * comp_name : {"comp_name":"山东派乐特网络科技有限公司"}
             */

            private CompNameBean comp_name;
            private List<ServiceBean> service;

            public CompNameBean getComp_name() {
                return comp_name;
            }

            public void setComp_name(CompNameBean comp_name) {
                this.comp_name = comp_name;
            }

            public List<ServiceBean> getService() {
                return service;
            }

            public void setService(List<ServiceBean> service) {
                this.service = service;
            }

            public static class CompNameBean {
                /**
                 * comp_name : 山东派乐特网络科技有限公司
                 */

                private String comp_name;

                public String getComp_name() {
                    return comp_name;
                }

                public void setComp_name(String comp_name) {
                    this.comp_name = comp_name;
                }
            }

            public static class ServiceBean {
                /**
                 * dict_id : YZpjpjqyflfwdw
                 * dict_desc : 服务到位
                 */

                private String dict_id;
                private String dict_desc;

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
            }
        }
    }
}
