package com.xtzhangbinbin.jpq.entity;

import java.util.List;

/**
 * Created by glp on 2018/5/4.
 * 描述：车品牌
 */

public class CarBrand {

    /**
     * data : {"result":[{"id":"YZescxgescppcxdlxlcnkcxmy_1","value":"1","desc":"大众"},{"id":"YZescxgescppcxdlxlcnkcxmy_3","value":"3","desc":"丰田"},{"id":"YZescxgescppcxdlxlcnkcxmy_8","value":"8","desc":"福特"},{"id":"YZescxgescppcxdlxlcnkcxmy_9","value":"9","desc":"克莱斯勒"},{"id":"YZescxgescppcxdlxlcnkcxmy_10","value":"10","desc":"雷诺"},{"id":"YZescxgescppcxdlxlcnkcxmy_11","value":"11","desc":"菲亚特"},{"id":"YZescxgescppcxdlxlcnkcxmy_12","value":"12","desc":"现代"},{"id":"YZescxgescppcxdlxlcnkcxmy_13","value":"13","desc":"标致"},{"id":"YZescxgescppcxdlxlcnkcxmy_14","value":"14","desc":"本田"},{"id":"YZescxgescppcxdlxlcnkcxmy_15","value":"15","desc":"宝马"},{"id":"YZescxgescppcxdlxlcnkcxmy_19","value":"19","desc":"荣威"},{"id":"YZescxgescppcxdlxlcnkcxmy_20","value":"20","desc":"名爵"},{"id":"YZescxgescppcxdlxlcnkcxmy_22","value":"22","desc":"中华"},{"id":"YZescxgescppcxdlxlcnkcxmy_24","value":"24","desc":"哈飞"},{"id":"YZescxgescppcxdlxlcnkcxmy_25","value":"25","desc":"吉利汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_26","value":"26","desc":"奇瑞"},{"id":"YZescxgescppcxdlxlcnkcxmy_27","value":"27","desc":"北京"},{"id":"YZescxgescppcxdlxlcnkcxmy_32","value":"32","desc":"东风"},{"id":"YZescxgescppcxdlxlcnkcxmy_33","value":"33","desc":"奥迪"},{"id":"YZescxgescppcxdlxlcnkcxmy_34","value":"34","desc":"阿尔法·罗密欧"},{"id":"YZescxgescppcxdlxlcnkcxmy_35","value":"35","desc":"阿斯顿·马丁"},{"id":"YZescxgescppcxdlxlcnkcxmy_36","value":"36","desc":"奔驰"},{"id":"YZescxgescppcxdlxlcnkcxmy_37","value":"37","desc":"布加迪"},{"id":"YZescxgescppcxdlxlcnkcxmy_38","value":"38","desc":"别克"},{"id":"YZescxgescppcxdlxlcnkcxmy_39","value":"39","desc":"宾利"},{"id":"YZescxgescppcxdlxlcnkcxmy_40","value":"40","desc":"保时捷"},{"id":"YZescxgescppcxdlxlcnkcxmy_41","value":"41","desc":"道奇"},{"id":"YZescxgescppcxdlxlcnkcxmy_42","value":"42","desc":"法拉利"},{"id":"YZescxgescppcxdlxlcnkcxmy_43","value":"43","desc":"悍马"},{"id":"YZescxgescppcxdlxlcnkcxmy_44","value":"44","desc":"捷豹"},{"id":"YZescxgescppcxdlxlcnkcxmy_45","value":"45","desc":"smart"},{"id":"YZescxgescppcxdlxlcnkcxmy_46","value":"46","desc":"Jeep"},{"id":"YZescxgescppcxdlxlcnkcxmy_47","value":"47","desc":"凯迪拉克"},{"id":"YZescxgescppcxdlxlcnkcxmy_48","value":"48","desc":"兰博基尼"},{"id":"YZescxgescppcxdlxlcnkcxmy_49","value":"49","desc":"路虎"},{"id":"YZescxgescppcxdlxlcnkcxmy_50","value":"50","desc":"路特斯"},{"id":"YZescxgescppcxdlxlcnkcxmy_51","value":"51","desc":"林肯"},{"id":"YZescxgescppcxdlxlcnkcxmy_52","value":"52","desc":"雷克萨斯"},{"id":"YZescxgescppcxdlxlcnkcxmy_53","value":"53","desc":"铃木"},{"id":"YZescxgescppcxdlxlcnkcxmy_54","value":"54","desc":"劳斯莱斯"},{"id":"YZescxgescppcxdlxlcnkcxmy_55","value":"55","desc":"迈巴赫"},{"id":"YZescxgescppcxdlxlcnkcxmy_56","value":"56","desc":"MINI"},{"id":"YZescxgescppcxdlxlcnkcxmy_57","value":"57","desc":"玛莎拉蒂"},{"id":"YZescxgescppcxdlxlcnkcxmy_58","value":"58","desc":"马自达"},{"id":"YZescxgescppcxdlxlcnkcxmy_59","value":"59","desc":"欧宝"},{"id":"YZescxgescppcxdlxlcnkcxmy_60","value":"60","desc":"讴歌"},{"id":"YZescxgescppcxdlxlcnkcxmy_61","value":"61","desc":"帕加尼"},{"id":"YZescxgescppcxdlxlcnkcxmy_62","value":"62","desc":"起亚"},{"id":"YZescxgescppcxdlxlcnkcxmy_63","value":"63","desc":"日产"},{"id":"YZescxgescppcxdlxlcnkcxmy_64","value":"64","desc":"萨博"},{"id":"YZescxgescppcxdlxlcnkcxmy_65","value":"65","desc":"斯巴鲁"},{"id":"YZescxgescppcxdlxlcnkcxmy_66","value":"66","desc":"世爵"},{"id":"YZescxgescppcxdlxlcnkcxmy_67","value":"67","desc":"斯柯达"},{"id":"YZescxgescppcxdlxlcnkcxmy_68","value":"68","desc":"三菱"},{"id":"YZescxgescppcxdlxlcnkcxmy_69","value":"69","desc":"双龙"},{"id":"YZescxgescppcxdlxlcnkcxmy_70","value":"70","desc":"沃尔沃"},{"id":"YZescxgescppcxdlxlcnkcxmy_71","value":"71","desc":"雪佛兰"},{"id":"YZescxgescppcxdlxlcnkcxmy_72","value":"72","desc":"雪铁龙"},{"id":"YZescxgescppcxdlxlcnkcxmy_73","value":"73","desc":"英菲尼迪"},{"id":"YZescxgescppcxdlxlcnkcxmy_74","value":"74","desc":"中兴"},{"id":"YZescxgescppcxdlxlcnkcxmy_75","value":"75","desc":"比亚迪"},{"id":"YZescxgescppcxdlxlcnkcxmy_76","value":"76","desc":"长安"},{"id":"YZescxgescppcxdlxlcnkcxmy_77","value":"77","desc":"长城"},{"id":"YZescxgescppcxdlxlcnkcxmy_78","value":"78","desc":"猎豹汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_79","value":"79","desc":"北汽昌河"},{"id":"YZescxgescppcxdlxlcnkcxmy_80","value":"80","desc":"力帆汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_81","value":"81","desc":"东南"},{"id":"YZescxgescppcxdlxlcnkcxmy_82","value":"82","desc":"广汽传祺"},{"id":"YZescxgescppcxdlxlcnkcxmy_83","value":"83","desc":"金杯"},{"id":"YZescxgescppcxdlxlcnkcxmy_84","value":"84","desc":"江淮"},{"id":"YZescxgescppcxdlxlcnkcxmy_85","value":"85","desc":"华普"},{"id":"YZescxgescppcxdlxlcnkcxmy_86","value":"86","desc":"海马"},{"id":"YZescxgescppcxdlxlcnkcxmy_87","value":"87","desc":"华泰"},{"id":"YZescxgescppcxdlxlcnkcxmy_88","value":"88","desc":"陆风"},{"id":"YZescxgescppcxdlxlcnkcxmy_89","value":"89","desc":"莲花汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_90","value":"90","desc":"双环"},{"id":"YZescxgescppcxdlxlcnkcxmy_91","value":"91","desc":"红旗"},{"id":"YZescxgescppcxdlxlcnkcxmy_92","value":"92","desc":"大发"},{"id":"YZescxgescppcxdlxlcnkcxmy_93","value":"93","desc":"永源"},{"id":"YZescxgescppcxdlxlcnkcxmy_94","value":"94","desc":"众泰"},{"id":"YZescxgescppcxdlxlcnkcxmy_95","value":"95","desc":"奔腾"},{"id":"YZescxgescppcxdlxlcnkcxmy_96","value":"96","desc":"福田"},{"id":"YZescxgescppcxdlxlcnkcxmy_97","value":"97","desc":"黄海"},{"id":"YZescxgescppcxdlxlcnkcxmy_98","value":"98","desc":"西雅特"},{"id":"YZescxgescppcxdlxlcnkcxmy_99","value":"99","desc":"威兹曼"},{"id":"YZescxgescppcxdlxlcnkcxmy_100","value":"100","desc":"科尼赛克"},{"id":"YZescxgescppcxdlxlcnkcxmy_101","value":"101","desc":"开瑞"},{"id":"YZescxgescppcxdlxlcnkcxmy_102","value":"102","desc":"威麟"},{"id":"YZescxgescppcxdlxlcnkcxmy_103","value":"103","desc":"瑞麒"},{"id":"YZescxgescppcxdlxlcnkcxmy_108","value":"108","desc":"广汽吉奥"},{"id":"YZescxgescppcxdlxlcnkcxmy_109","value":"109","desc":"KTM"},{"id":"YZescxgescppcxdlxlcnkcxmy_110","value":"110","desc":"一汽"},{"id":"YZescxgescppcxdlxlcnkcxmy_111","value":"111","desc":"野马汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_112","value":"112","desc":"GMC"},{"id":"YZescxgescppcxdlxlcnkcxmy_113","value":"113","desc":"东风风神"},{"id":"YZescxgescppcxdlxlcnkcxmy_114","value":"114","desc":"五菱汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_116","value":"116","desc":"光冈"},{"id":"YZescxgescppcxdlxlcnkcxmy_117","value":"117","desc":"AC Schnitzer"},{"id":"YZescxgescppcxdlxlcnkcxmy_118","value":"118","desc":"Lorinser"},{"id":"YZescxgescppcxdlxlcnkcxmy_119","value":"119","desc":"江铃"},{"id":"YZescxgescppcxdlxlcnkcxmy_120","value":"120","desc":"宝骏"},{"id":"YZescxgescppcxdlxlcnkcxmy_122","value":"122","desc":"启辰"},{"id":"YZescxgescppcxdlxlcnkcxmy_124","value":"124","desc":"理念"},{"id":"YZescxgescppcxdlxlcnkcxmy_129","value":"129","desc":"迈凯伦"},{"id":"YZescxgescppcxdlxlcnkcxmy_130","value":"130","desc":"纳智捷"},{"id":"YZescxgescppcxdlxlcnkcxmy_133","value":"133","desc":"特斯拉"},{"id":"YZescxgescppcxdlxlcnkcxmy_140","value":"140","desc":"巴博斯"},{"id":"YZescxgescppcxdlxlcnkcxmy_141","value":"141","desc":"福迪"},{"id":"YZescxgescppcxdlxlcnkcxmy_142","value":"142","desc":"东风小康"},{"id":"YZescxgescppcxdlxlcnkcxmy_143","value":"143","desc":"北汽威旺"},{"id":"YZescxgescppcxdlxlcnkcxmy_144","value":"144","desc":"依维柯"},{"id":"YZescxgescppcxdlxlcnkcxmy_145","value":"145","desc":"金龙"},{"id":"YZescxgescppcxdlxlcnkcxmy_146","value":"146","desc":"欧朗"},{"id":"YZescxgescppcxdlxlcnkcxmy_149","value":"149","desc":"陕汽通家"},{"id":"YZescxgescppcxdlxlcnkcxmy_150","value":"150","desc":"海格"},{"id":"YZescxgescppcxdlxlcnkcxmy_151","value":"151","desc":"九龙"},{"id":"YZescxgescppcxdlxlcnkcxmy_152","value":"152","desc":"观致"},{"id":"YZescxgescppcxdlxlcnkcxmy_154","value":"154","desc":"北汽制造"},{"id":"YZescxgescppcxdlxlcnkcxmy_155","value":"155","desc":"上汽大通"},{"id":"YZescxgescppcxdlxlcnkcxmy_156","value":"156","desc":"卡尔森"},{"id":"YZescxgescppcxdlxlcnkcxmy_161","value":"161","desc":"腾势"},{"id":"YZescxgescppcxdlxlcnkcxmy_162","value":"162","desc":"思铭"},{"id":"YZescxgescppcxdlxlcnkcxmy_163","value":"163","desc":"长安欧尚"},{"id":"YZescxgescppcxdlxlcnkcxmy_164","value":"164","desc":"恒天"},{"id":"YZescxgescppcxdlxlcnkcxmy_165","value":"165","desc":"东风风行"},{"id":"YZescxgescppcxdlxlcnkcxmy_167","value":"167","desc":"五十铃"},{"id":"YZescxgescppcxdlxlcnkcxmy_168","value":"168","desc":"摩根"},{"id":"YZescxgescppcxdlxlcnkcxmy_169","value":"169","desc":"DS"},{"id":"YZescxgescppcxdlxlcnkcxmy_173","value":"173","desc":"北汽绅宝"},{"id":"YZescxgescppcxdlxlcnkcxmy_174","value":"174","desc":"如虎"},{"id":"YZescxgescppcxdlxlcnkcxmy_175","value":"175","desc":"金旅"},{"id":"YZescxgescppcxdlxlcnkcxmy_181","value":"181","desc":"哈弗"},{"id":"YZescxgescppcxdlxlcnkcxmy_182","value":"182","desc":"之诺"},{"id":"YZescxgescppcxdlxlcnkcxmy_184","value":"184","desc":"华骐"},{"id":"YZescxgescppcxdlxlcnkcxmy_185","value":"185","desc":"新凯"},{"id":"YZescxgescppcxdlxlcnkcxmy_187","value":"187","desc":"东风风度"},{"id":"YZescxgescppcxdlxlcnkcxmy_188","value":"188","desc":"Icona"},{"id":"YZescxgescppcxdlxlcnkcxmy_192","value":"192","desc":"潍柴英致"},{"id":"YZescxgescppcxdlxlcnkcxmy_196","value":"196","desc":"成功汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_197","value":"197","desc":"福汽启腾"},{"id":"YZescxgescppcxdlxlcnkcxmy_199","value":"199","desc":"卡威"},{"id":"YZescxgescppcxdlxlcnkcxmy_202","value":"202","desc":"泰卡特"},{"id":"YZescxgescppcxdlxlcnkcxmy_203","value":"203","desc":"北汽幻速"},{"id":"YZescxgescppcxdlxlcnkcxmy_204","value":"204","desc":"陆地方舟"},{"id":"YZescxgescppcxdlxlcnkcxmy_205","value":"205","desc":"赛麟"},{"id":"YZescxgescppcxdlxlcnkcxmy_206","value":"206","desc":"知豆"},{"id":"YZescxgescppcxdlxlcnkcxmy_208","value":"208","desc":"北汽新能源"},{"id":"YZescxgescppcxdlxlcnkcxmy_210","value":"210","desc":"江铃集团轻汽"},{"id":"YZescxgescppcxdlxlcnkcxmy_213","value":"213","desc":"南京金龙"},{"id":"YZescxgescppcxdlxlcnkcxmy_214","value":"214","desc":"凯翼"},{"id":"YZescxgescppcxdlxlcnkcxmy_215","value":"215","desc":"雷丁"},{"id":"YZescxgescppcxdlxlcnkcxmy_219","value":"219","desc":"康迪全球鹰"},{"id":"YZescxgescppcxdlxlcnkcxmy_220","value":"220","desc":"华颂"},{"id":"YZescxgescppcxdlxlcnkcxmy_221","value":"221","desc":"安凯客车"},{"id":"YZescxgescppcxdlxlcnkcxmy_224","value":"224","desc":"卡升"},{"id":"YZescxgescppcxdlxlcnkcxmy_231","value":"231","desc":"宝沃"},{"id":"YZescxgescppcxdlxlcnkcxmy_232","value":"232","desc":"御捷"},{"id":"YZescxgescppcxdlxlcnkcxmy_235","value":"235","desc":"前途"},{"id":"YZescxgescppcxdlxlcnkcxmy_237","value":"237","desc":"华利"},{"id":"YZescxgescppcxdlxlcnkcxmy_238","value":"238","desc":"斯达泰克"},{"id":"YZescxgescppcxdlxlcnkcxmy_241","value":"241","desc":"LOCAL MOTORS"},{"id":"YZescxgescppcxdlxlcnkcxmy_245","value":"245","desc":"华凯"},{"id":"YZescxgescppcxdlxlcnkcxmy_259","value":"259","desc":"东风风光"},{"id":"YZescxgescppcxdlxlcnkcxmy_260","value":"260","desc":"华泰新能源"},{"id":"YZescxgescppcxdlxlcnkcxmy_263","value":"263","desc":"驭胜"},{"id":"YZescxgescppcxdlxlcnkcxmy_267","value":"267","desc":"汉腾汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_269","value":"269","desc":"SWM斯威汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_270","value":"270","desc":"江铃集团新能源"},{"id":"YZescxgescppcxdlxlcnkcxmy_271","value":"271","desc":"比速汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_272","value":"272","desc":"ARCFOX"},{"id":"YZescxgescppcxdlxlcnkcxmy_276","value":"276","desc":"ALPINA"},{"id":"YZescxgescppcxdlxlcnkcxmy_279","value":"279","desc":"领克"},{"id":"YZescxgescppcxdlxlcnkcxmy_280","value":"280","desc":"电咖"},{"id":"YZescxgescppcxdlxlcnkcxmy_282","value":"282","desc":"福田乘用车"},{"id":"YZescxgescppcxdlxlcnkcxmy_283","value":"283","desc":"WEY"},{"id":"YZescxgescppcxdlxlcnkcxmy_284","value":"284","desc":"蔚来"},{"id":"YZescxgescppcxdlxlcnkcxmy_286","value":"286","desc":"云度"},{"id":"YZescxgescppcxdlxlcnkcxmy_289","value":"289","desc":"祺智"},{"id":"YZescxgescppcxdlxlcnkcxmy_294","value":"294","desc":"长安轻型车"},{"id":"YZescxgescppcxdlxlcnkcxmy_296","value":"296","desc":"瑞驰新能源"},{"id":"YZescxgescppcxdlxlcnkcxmy_297","value":"297","desc":"君马汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_298","value":"298","desc":"宇通客车"},{"id":"YZescxgescppcxdlxlcnkcxmy_299","value":"299","desc":"长安跨越"},{"id":"YZescxgescppcxdlxlcnkcxmy_301","value":"301","desc":"北汽道达"},{"id":"YZescxgescppcxdlxlcnkcxmy_304","value":"304","desc":"国金汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_306","value":"306","desc":"鑫源"},{"id":"YZescxgescppcxdlxlcnkcxmy_307","value":"307","desc":"裕路"},{"id":"YZescxgescppcxdlxlcnkcxmy_312","value":"312","desc":"庆铃汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_313","value":"313","desc":"广汽新能源"},{"id":"YZescxgescppcxdlxlcnkcxmy_317","value":"317","desc":"云雀汽车"},{"id":"YZescxgescppcxdlxlcnkcxmy_326","value":"326","desc":"东风瑞泰特"}]}
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
             * id : YZescxgescppcxdlxlcnkcxmy_1
             * value : 1
             * desc : 大众
             */

            private String id;
            private String value;
            private String desc;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
