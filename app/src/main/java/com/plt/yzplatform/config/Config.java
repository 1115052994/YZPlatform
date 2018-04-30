package com.plt.yzplatform.config;

/**
 * 通用配置文件，用于配置整个系统使用的资源
 */

public class Config {
    //全局应用名称
    public final static String APP_NAME = "yzplatform";
    //接口连接地址
    public static final String BASE_URL = "http://192.168.1.222/";

    public static final String URL = "http://192.168.1.222/";


    public static final String S = "mymvc?mvc_id=";

    public static final String M = "http://192.168.1.222/";

    //用于拼接图片地址url
    public static final String Y = "myres?id=";


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

    //根据图片id获取图片(Picasso)
    public static final String GET_Pic= URL + Y ;

    //联想搜索商户
    public static final String GETSEARCHCOMP = URL + S + "getSearchComp";

    //联想搜索Car
    public static final String GETSEARCHCAR = URL + S + "getSearchCar";

    //热搜
    public static final String GETSEARCHHOTWORD = URL + S + "getSearchHotWord";

    //首页车服获取banner和icon
    public static final String GET_SERVICE_IMG = URL + S + "getCompIcon";

    //商家详情
    public static final String GETCOMPDETAIL = URL + S + "getCompDetail";

    //服务详情
    public static final String GETPRODUCTITEMBYSERVERTYPE = URL + S + "getProductItemByServerType";

    //查看企业评价
    public static final String QUERYCOMPEVALUATE = URL + S + "queryCompeValuate";

    //评价获取信息
    public static final String COMMENTORCOMPNAME = URL + S + "commentOrCompName";

    //车商评价
    public static final String ACCRETIONEVALUATE = URL + S + "accretionEvaluate";

    // 服务商评价
    public static final String ACCRETIONEVALUATESERVICECOMP = URL + S +"accretionEvaluateServiceComp";

    //车服列表-获取列表（智能排序）
    public static final String GET_CAR_SERVICE = URL + S + "getPassCompList";

    //企业保存明星员工
    public static final String SAVESTAR = M + S + "saveStaff";

    //企业更新明星员工
    public static final String UPDATASTAR = M + S + "updateStaff";

    //消费者点击查看商家的明星员工
    public static final String SELECTCOMPSTAR = M + S + "selectCompStaff";

    //商家查看自己通过的明星员工
    public static final String SELECTSTAR = M + S + "selectStaff";

    //商家删除明星员工
    public static final String DELETESTAR = M + S + "deleteStaff";

    //查询二手车收藏
    public static final String CHECKCAR = M + S + "checkCar";

    //统计平台有多少人申请卖车
    public static final String SELLCOUNT = M + S + "sellCount";

    //个人预约卖车
    public static final String SAVESELLER = M + S + "saveSeller";

    //查询服务商收藏
    public static final String CHECKCOMP = M + S + "checkComp";

    //添加二手车信息
    public static final String ACCRETIONCAR = M + S + "accretionCar";

    //添加二手车信息
    public static final String QUERYCARSTYLE = M + S + "queryCarStyle";

    //上传图片获取车架号
    public static final String PARSEVIN = M + S + "parseVIN";

    //删除收藏
    public static final String REMOVECOLL = M + S + "removeColl";

    //添加二手车收藏
    public static final String ACCRETIONCOLLCAR = M + S + "accretionCollCar";

    //添加二手车图片
    public static final String ACCRETIONPICTURECAR = M + S + "accretionPictureCar";

    //添加二手车收藏
    public static final String ACCRETIONCOLLCOMP = M + S + "accretionCollComp ";


    //二手车浏览记录
    public static final String BROWSECARCOOKIES = M + S + "browseCarCookies";

    //企业浏览记录
    public static final String BROWSECOMPCOOKIES = M + S + "browseCompCookies";

    //删除浏览记录
    public static final String BROWSEDLOG = M + S + "browseDLog";

    //添加浏览企业记录
    public static final String BROWSECOMP = M + S + "browseComp";

    //添加浏览二手车记录
    public static final String CARVISIT = M + S + "car_visit";

    //查看企业评价
    public static final String QUERYCOMPVALUATE = M + S + "queryCompeValuate";

    //全部评价
    public static final String ALLCOMPEVALUATE = M + S + "allCompEvaluate";

    //好评
    public static final String PRAISECOMPEVALUATE = M + S + "praiseCompEvaluate";

    //中评
    public static final String MEDIUMCOMPEVALUATE = M + S + "mediumCompEvaluate";

    //差评
    public static final String POORREVIEWCOMPEVALUATE = M + S + "poorReviewCompEvaluate";

    //评价次数
    public static final String SCOREBYCOMPCOUNT = M + S + "scoreByCompCount";

    //得到评价词典
    public static final String GETVALUE = M + S + "getValue";

    //查看服务商是否收藏过
    public static final String WHETHERCOLLCOMP = URL + S + "whetherCollComp";

    //商家添加产品 - 服务类型
    public static final String GET_COMP_SERVICE_TYPE = URL + S + "serverItemType";

    //商家添加产品 - 添加服务项
    public static final String ADD_COMP_SERVICE_TYPE = URL + S + "findSuppostWord";

    //商家添加产品
    public static final String ADD_PRODUCT = URL + S + "addProDuct";

    //商家产品 - 修改产品
    public static final String UPDATE_PRODUCT = URL + S + "updateProDuct";

    //商家产品 - 查看产品信息
    public static final String SHOW_PRODUCT = URL + S + "showProductDetail";


    //模糊查询car
    public static final String SEARCHCAR = URL + S + "searchCar";

    // 查询Car信息
    public static final String GETCARPARAMDICT = URL + S + "getCarParamDict";

    // (热门车辆/模糊搜索品牌)
    public static final String GETCARBRANDDICT = URL + S + "getCarBrandDict";

    /*
    *getCarTrianByCarBrand  品牌查车系
    getHotCarBrandDict     获取热门
    getCarBrandDictByInitials   字母筛选
     */
    public static final String GETCARTRIANBYCARBRAND = URL + S + "getCarTrianByCarBrand";
    public static final String GETHOTCARBRANDDICT = URL + S + "getHotCarBrandDict";
    public static final String GETCARBRANDDICTBYINITIALS = URL + S + "getCarBrandDictByInitials";

    //二手车详情
    public static final String CAR_DETAIL = URL + S + "car_detail";

    //二手车详情 - 获取广告
    public static final String CAR_DETAIL_AD = URL + S + "selectAdveToApp";

    //二手车详情 - 是否收藏   coll_content_id (car_id)
    public static final String CAR_DETAIL_COLLECT = URL + S + "whetherCollCar";

    //二手车详情 - 添加收藏   coll_content_id  (car_id)
    public static final String CAR_DETAIL_ADD_COLLECT = URL + S + "accretionCollCar";

    //二手车详情 - 订阅降价通知
    public static final String CAR_DETAIL_ADD_SALE = URL + S + "subscriptionCar";

    //二手车详情 - 查看是否订阅降价通知
    public static final String CAR_DETAIL_IS_SALE =  URL + S + "whetherSubscription";

    //二手车详情 - 在线预约
    public static final String CAR_DETAIL_ONLINE_ORDER = URL + S + "accretionSubscribe";

    //二手车详情 - 车辆详参
    public static final String CAR_DETAIL_KEY = URL + S + "queryCarParameter";

    // 二手车详情页查询所在公司信息
    public static final String FINDCOMPBYCOMPID = URL + S +"findCompByCompId";

    //查询车商下的二手车信息
    public static final String FINDCOMPCARBYCOMPID = URL + S +"findCompCarByCompId";

    // 订阅添加
    public static final String SUBSCRIPTIONFILTRATE = URL + S +"subscriptionFiltrate";
    // 订阅修改
    public static final String UPDATESUBSCRIPTIONFILTRATE = URL + S +"updateSubscriptionFiltrate";
    // 订阅查询
    public static final String SELECTUSERSUBSCRIPTIONFILTRATE = URL + S +"selectUserSubscriptionFiltrate";
    // 订阅删除
    public static final String DELETESUBSCRIPTIONFILTRATE = URL + S +"deleteSubscriptionFiltrate";

    //个人中心 - 降价提醒
    public static final String PERS_PRICE_WARN = URL + S + "QuerySubscription";
}
