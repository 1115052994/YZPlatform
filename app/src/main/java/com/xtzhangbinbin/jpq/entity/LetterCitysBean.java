package com.xtzhangbinbin.jpq.entity;

import java.util.List;

public class LetterCitysBean {

    /**
     * data : {"result":{"A":["阿坝","阿克苏","阿拉尔","阿拉善盟","阿勒泰","阿里","安康","安庆","鞍山","安顺","安阳"],"B":["白城","百色","白山","白银","蚌埠","保定","宝鸡","保山","包头","巴彦淖尔","巴音郭楞州","巴中","北海","北京","本溪","毕节","滨州","博尔塔拉州","亳州"],"C":["沧州","常德","昌都","昌吉","常州","巢湖","朝阳","潮州","承德","成都","赤峰","池州","楚雄","滁州","郴州","崇左"],"D":["大理","大连","丹东","大庆","大同","大兴安岭","达州","德宏","德阳","德州","定西","迪庆","东莞","东营","儋州"],"E":["鄂尔多斯","恩施","鄂州"],"F":["防城港","佛山","抚顺","阜新","阜阳","福州","抚州"],"G":["赣州","甘南","甘孜","广安","广元","广州","贵港","桂林","贵阳","果洛","固原"],"H":["哈尔滨","海北","海东","海口","海南","海西","哈密","邯郸","杭州","汉中","鹤壁","河池","合肥","鹤岗","黑河","衡水","衡阳","和田","河源","菏泽","贺州","红河","淮安","淮北","怀化","淮南","黄冈","黄南","黄山","黄石","呼和浩特","惠州","葫芦岛","呼伦贝尔","湖州"],"I":[],"J":["佳木斯","吉安","江门","焦作","嘉兴","嘉峪关","揭阳","吉林","济南","金昌","晋城","景德镇","荆门","荆州","金华","济宁","晋中","锦州","九江","酒泉","鸡西"],"K":["开封","喀什","克拉玛依","克孜勒苏州","昆明"],"L":["来宾","莱芜","廊坊","兰州","拉萨","乐山","凉山","连云港","聊城","辽阳","辽源","丽江","临沧","临汾","临沂","临夏","林芝","丽水","六安","六盘水","柳州","陇南","龙岩","漯河","洛阳","泸州","娄底","吕梁"],"M":["马鞍山","茂名","眉山","梅州","绵阳","牡丹江"],"N":["南昌","南充","南京","南宁","南平","南通","南阳","那曲","内江","宁波","宁德","怒江"],"O":[],"P":["盘锦","攀枝花","平顶山","平凉","萍乡","普洱","莆田","濮阳"],"Q":["黔东南","黔南","黔西南","青岛","庆阳","清远","秦皇岛","钦州","齐齐哈尔","七台河","泉州","曲靖","衢州"],"R":["日喀则","日照"],"S":["省直辖县级行政区划","三沙","绥化","三门峡","三明","三亚","上海","商洛","商丘","上饶","山南","汕头","汕尾","韶关","绍兴","邵阳","沈阳","深圳","石河子","石家庄","十堰","石嘴山","双鸭山","四平","松原","随州","宿迁","朔州","苏州","宿州","遂宁"],"U":[],"V":[],"W":["潍坊","威海","渭南","文山","温州","乌海","武汉","芜湖","五家渠","乌兰察布","乌鲁木齐","武威","无锡","梧州","吴忠市"],"X":["西安","襄樊","湘潭","湘西","咸宁","咸阳","孝感","锡林郭勒盟","兴安盟","邢台","西宁","新乡","新余","信阳","西双版纳州","忻州","宣城","许昌","徐州"],"Y":["雅安","延安","延边","盐城","阳江","阳泉","扬州","烟台","宜宾","宜昌","伊春","宜春","伊犁","银川","营口市 ","鹰潭","益阳","永州","岳阳","玉林","榆林","运城","云浮","玉树","玉溪"],"Z":["长春","长沙","长治","重庆","昭通","枣庄","张家界","张家口","张掖","漳州","湛江","肇庆","郑州","镇江","中山","周口","舟山","珠海","驻马店","株洲","淄博","自贡","资阳","遵义"]}}
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
         * result : {"A":["阿坝","阿克苏","阿拉尔","阿拉善盟","阿勒泰","阿里","安康","安庆","鞍山","安顺","安阳"],"B":["白城","百色","白山","白银","蚌埠","保定","宝鸡","保山","包头","巴彦淖尔","巴音郭楞州","巴中","北海","北京","本溪","毕节","滨州","博尔塔拉州","亳州"],"C":["沧州","常德","昌都","昌吉","常州","巢湖","朝阳","潮州","承德","成都","赤峰","池州","楚雄","滁州","郴州","崇左"],"D":["大理","大连","丹东","大庆","大同","大兴安岭","达州","德宏","德阳","德州","定西","迪庆","东莞","东营","儋州"],"E":["鄂尔多斯","恩施","鄂州"],"F":["防城港","佛山","抚顺","阜新","阜阳","福州","抚州"],"G":["赣州","甘南","甘孜","广安","广元","广州","贵港","桂林","贵阳","果洛","固原"],"H":["哈尔滨","海北","海东","海口","海南","海西","哈密","邯郸","杭州","汉中","鹤壁","河池","合肥","鹤岗","黑河","衡水","衡阳","和田","河源","菏泽","贺州","红河","淮安","淮北","怀化","淮南","黄冈","黄南","黄山","黄石","呼和浩特","惠州","葫芦岛","呼伦贝尔","湖州"],"I":[],"J":["佳木斯","吉安","江门","焦作","嘉兴","嘉峪关","揭阳","吉林","济南","金昌","晋城","景德镇","荆门","荆州","金华","济宁","晋中","锦州","九江","酒泉","鸡西"],"K":["开封","喀什","克拉玛依","克孜勒苏州","昆明"],"L":["来宾","莱芜","廊坊","兰州","拉萨","乐山","凉山","连云港","聊城","辽阳","辽源","丽江","临沧","临汾","临沂","临夏","林芝","丽水","六安","六盘水","柳州","陇南","龙岩","漯河","洛阳","泸州","娄底","吕梁"],"M":["马鞍山","茂名","眉山","梅州","绵阳","牡丹江"],"N":["南昌","南充","南京","南宁","南平","南通","南阳","那曲","内江","宁波","宁德","怒江"],"O":[],"P":["盘锦","攀枝花","平顶山","平凉","萍乡","普洱","莆田","濮阳"],"Q":["黔东南","黔南","黔西南","青岛","庆阳","清远","秦皇岛","钦州","齐齐哈尔","七台河","泉州","曲靖","衢州"],"R":["日喀则","日照"],"S":["省直辖县级行政区划","三沙","绥化","三门峡","三明","三亚","上海","商洛","商丘","上饶","山南","汕头","汕尾","韶关","绍兴","邵阳","沈阳","深圳","石河子","石家庄","十堰","石嘴山","双鸭山","四平","松原","随州","宿迁","朔州","苏州","宿州","遂宁"],"U":[],"V":[],"W":["潍坊","威海","渭南","文山","温州","乌海","武汉","芜湖","五家渠","乌兰察布","乌鲁木齐","武威","无锡","梧州","吴忠市"],"X":["西安","襄樊","湘潭","湘西","咸宁","咸阳","孝感","锡林郭勒盟","兴安盟","邢台","西宁","新乡","新余","信阳","西双版纳州","忻州","宣城","许昌","徐州"],"Y":["雅安","延安","延边","盐城","阳江","阳泉","扬州","烟台","宜宾","宜昌","伊春","宜春","伊犁","银川","营口市 ","鹰潭","益阳","永州","岳阳","玉林","榆林","运城","云浮","玉树","玉溪"],"Z":["长春","长沙","长治","重庆","昭通","枣庄","张家界","张家口","张掖","漳州","湛江","肇庆","郑州","镇江","中山","周口","舟山","珠海","驻马店","株洲","淄博","自贡","资阳","遵义"]}
         */

        private ResultBean result;

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class ResultBean {
            private List<String> A;
            private List<String> B;
            private List<String> C;
            private List<String> D;
            private List<String> E;
            private List<String> F;
            private List<String> G;
            private List<String> H;
            private List<?> I;
            private List<String> J;
            private List<String> K;
            private List<String> L;
            private List<String> M;
            private List<String> N;
            private List<?> O;
            private List<String> P;
            private List<String> Q;
            private List<String> R;
            private List<String> S;
            private List<?> U;
            private List<?> V;
            private List<String> W;
            private List<String> X;
            private List<String> Y;
            private List<String> Z;

            public List<String> getA() {
                return A;
            }

            public void setA(List<String> A) {
                this.A = A;
            }

            public List<String> getB() {
                return B;
            }

            public void setB(List<String> B) {
                this.B = B;
            }

            public List<String> getC() {
                return C;
            }

            public void setC(List<String> C) {
                this.C = C;
            }

            public List<String> getD() {
                return D;
            }

            public void setD(List<String> D) {
                this.D = D;
            }

            public List<String> getE() {
                return E;
            }

            public void setE(List<String> E) {
                this.E = E;
            }

            public List<String> getF() {
                return F;
            }

            public void setF(List<String> F) {
                this.F = F;
            }

            public List<String> getG() {
                return G;
            }

            public void setG(List<String> G) {
                this.G = G;
            }

            public List<String> getH() {
                return H;
            }

            public void setH(List<String> H) {
                this.H = H;
            }

            public List<?> getI() {
                return I;
            }

            public void setI(List<?> I) {
                this.I = I;
            }

            public List<String> getJ() {
                return J;
            }

            public void setJ(List<String> J) {
                this.J = J;
            }

            public List<String> getK() {
                return K;
            }

            public void setK(List<String> K) {
                this.K = K;
            }

            public List<String> getL() {
                return L;
            }

            public void setL(List<String> L) {
                this.L = L;
            }

            public List<String> getM() {
                return M;
            }

            public void setM(List<String> M) {
                this.M = M;
            }

            public List<String> getN() {
                return N;
            }

            public void setN(List<String> N) {
                this.N = N;
            }

            public List<?> getO() {
                return O;
            }

            public void setO(List<?> O) {
                this.O = O;
            }

            public List<String> getP() {
                return P;
            }

            public void setP(List<String> P) {
                this.P = P;
            }

            public List<String> getQ() {
                return Q;
            }

            public void setQ(List<String> Q) {
                this.Q = Q;
            }

            public List<String> getR() {
                return R;
            }

            public void setR(List<String> R) {
                this.R = R;
            }

            public List<String> getS() {
                return S;
            }

            public void setS(List<String> S) {
                this.S = S;
            }

            public List<?> getU() {
                return U;
            }

            public void setU(List<?> U) {
                this.U = U;
            }

            public List<?> getV() {
                return V;
            }

            public void setV(List<?> V) {
                this.V = V;
            }

            public List<String> getW() {
                return W;
            }

            public void setW(List<String> W) {
                this.W = W;
            }

            public List<String> getX() {
                return X;
            }

            public void setX(List<String> X) {
                this.X = X;
            }

            public List<String> getY() {
                return Y;
            }

            public void setY(List<String> Y) {
                this.Y = Y;
            }

            public List<String> getZ() {
                return Z;
            }

            public void setZ(List<String> Z) {
                this.Z = Z;
            }
        }
    }
}
