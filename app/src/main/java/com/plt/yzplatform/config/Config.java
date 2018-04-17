package com.plt.yzplatform.config;

/**
 *
 * 通用配置文件，用于配置整个系统使用的资源
 */

public class Config {
    //全局应用名称
    public final static String APP_NAME = "yzplatform";
    //接口连接地址
    public static final String BASE_URL = "http://192.168.1.222/";

    public static final String URL = "http://192.168.1.119/";

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

    //获取企业服务类型
    public static final String GETSERVERTYPE = URL + S + "serverType";

    //提交企业认证信息
    public static final String SUBMIT_COMPANY = URL + S + "submitCompAuthInfo";

    //查询首字母表示的所有城市
    public static final String QUERYHEADCITY = URL + S +"queryHeadCity";

    //查询热门城市
    public static final String QUERYHOTCITY = URL + S +"queryHotCity";

    //模糊查询城市
    public static final String QUERYCITY = URL + S +"queryCity";
}
