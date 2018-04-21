package com.plt.yzplatform.config;

/**
 *
 * 通用配置文件，用于配置整个系统使用的资源
 */

public class Config {
    //全局应用名称
    public final static String APP_NAME = "yzplatform";
    //接口连接地址
    public static final String BASE_URL = "http://192.168.2.223/";

    public static final String URL = "http://192.168.2.223/";

    public static final String S = "mymvc?mvc_id=";

    //获取验证码
    public static final String GETCODE = BASE_URL + S + "sendCode";

    //注册
    public static final String REG = BASE_URL + S + "reg";

    //校验推荐码是否正确
    public static final String VERIFY_CODE = BASE_URL + S + "verifyCode";

    //获取用户注册协议
    public static final String GET_REGISTER_SERVICE = URL + S + "getUserRegProtocol";

    //登陆
    public static final String LOGIN = BASE_URL + S + "login";

    //上传图片base64
    public static final String UPLOADFILE = URL + S + "uploadFile";

    //根据文件id获取图片
    public static final String GETFILE = URL + S + "getFile";

    //获取企业认证信息
    public static final String GETCOMP_INFO = URL + S + "getCompInfo";

    //获取企业服务类型
    public static final String GETSERVERTYPE = URL + S + "serverType";

    //提交企业认证信息
    public static final String SUBMIT_COMPANY = URL + S + "submitCompAuthInfo";

    //查询首字母表示的所有城市
    public static final String QUERYHEADCITY = URL + S + "queryHeadCity";

    //查询热门城市
    public static final String QUERYHOTCITY = URL + S + "queryHotCity";

    //模糊查询城市
    public static final String QUERYCITY = URL + S + "queryCity";

    //获取用户信息
    public static final String GET_USERTOKEN = BASE_URL + S + "getUserInfor";

    //通过城市名获取城市id
    public static final String GET_CITY_ID = URL + S + "get_cityIdByCityName";

    //获取个人设置信息
    public static final String GET_PERS_INFO = URL + S + "getPersInfo";

    //修改个人设置
    public static final String UPDATE_PERS_INFO = URL + S + "updatePersInfo";

    //根据图片id获取base64
    public static final String GET_BASE64 = URL + S + "getFile";

    //模糊搜索商户
    public static final String GETSEARCHCOMP = URL + S +"getSearchComp";

    //热搜
    public static final String GETSEARCHHOTWORD = URL + S +"getSearchHotWord";

    //首页车服获取banner和icon
    public static final String GET_SERVICE_IMG = URL + S + "getCompIcon";

    //商家详情
    public static final String GETCOMPDETAIL = URL + S + "getCompDetail";

    //服务详情
    public static final String GETPRODUCTITEMBYSERVERTYPE = URL + S + "getProductItemByServerType";

    //查看企业评价
    public static final String QUERYCOMPEVALUATE = URL + S +"queryCompeValuate";

    //车商评价获取信息
    public static final String COMMENTORCOMPNAME = URL + S +"commentOrCompName";

    //车商评价
    public static final String ACCRETIONEVALUATE = URL + S +"accretionEvaluate";
    //车服列表-获取列表（智能排序）
    public static final String GET_CAR_SERVICE = URL + S + "getPassCompList";

    //企业保存明星员工
    public static final String SAVESTAR = M +S+ "saveStaff";

    //企业更新明星员工
    public static final String UPDATASTAR = M + S + "updateStaff";

    //消费者点击查看商家的明星员工
    public static final String SELECTCOMPSTAR = M + S + "selectCompStaff";

    //商家查看自己通过的明星员工
    public static final String SELECTSTAR = M + S + "selectStaff";

    //商家删除明星员工
    public static final String DELETESTAR = M + S + "deleteStaff";


}
