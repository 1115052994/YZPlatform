package com.xtzhangbinbin.jpq.config;

/**
 * 通用配置文件，用于配置整个系统使用的资源
 */

public class Config {
    //全局应用名称
    public final static String APP_NAME = "yzplatform";
    //接口连接地址
    public static final String BASE_URL = "http://192.168.1.223/";

    public static final String URL = "http://192.168.1.223/";
    //微信APPID
    public static final String WECHAT_APP_ID = "wx5486a0497a18e7d4";

    public static final String S = "mymvc?mvc_id=";

    public static final String M = "http://192.168.1.223/";

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
    public static final String GET_USERINFO = BASE_URL + S + "getUserInfor";
    public static final String GET_USERTOKEN = BASE_URL + S + "getUserInfor";

    //通过城市名获取城市id
    public static final String GET_CITY_ID = URL + S + "get_cityIdByCityName";

    //获取个人设置信息
    public static final String GET_PERS_INFO = URL + S + "getPersInfo";

    //修改个人设置
    public static final String UPDATE_PERS_INFO = URL + S + "updatePersInfo";

    //根据图片id获取base64
    public static final String GET_BASE64 = URL + S + "getFile";

    //联想搜索商户
    public static final String GETSEARCHCOMP = URL + S +"getSearchComp";

    //根据图片id获取图片(Picasso)
    public static final String GET_Pic= URL + Y ;

    //联想搜索Car
    public static final String GETSEARCHCAR = URL + S +"getSearchCar";

    //热搜
    public static final String GETSEARCHHOTWORD = URL + S +"getSearchHotWord";

    //首页车服获取banner和icon
    public static final String GET_SERVICE_IMG = URL + S + "getCompIcon";

    //商家详情
    public static final String GETCOMPDETAIL = URL + S + "getCompDetail";

    //服务详情
    public static final String GETPRODUCTITEMBYSERVERTYPE = URL + S + "getProductItemByServerType";

    //查看企业评价
    public static final String QUERYCOMPEVALUATE = URL + S + "queryCompeValuate";

    //车商评价获取信息
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

    //ETC查询
    public static final String SELECTETC = M + S + "selectEtc";

    //添加二手车图片
    public static final String ACCRETIONPICTURECAR = M + S + "accretionPictureCar";

    //获取登录企业的服务细项类型（添加服务产品用）
    public static final String SERVERITEMTYPE = M + S + "serverItemType";

    //查询所有道路救援
    public static final String SELECTROADSIDE = M + S + "selectRoadSide";

    //金融信息录入
    public static final String INSERTINFO = M + S + "insertInfo";

    //车金融数据
    public static final String SELECTJRTOAPP = M + S + "selectJrToApp";

    //车信贷
    public static final String QUERYALLTWO = M + S + "queryAllTwo";

    //贷款明细
    public static final String QUERYALLTHIRDAPP= M + S + "queryAllThirdApp";

    //通过城市名称得到城市Id
    public static final String GETCITYIDBYCITYNAME= M + S + "get_cityIdByCityName";

    //查询保险超市所有产品
    public static final String QUERYALLINSURANCE = M + S + "queryAllInsurance";

    //添加二手车图片
    public static final String COMPCAR = M + S + "compCar";

    //商家查看二手车产品信息
    public static final String SHOW_CAR_PRODUCT = URL + S +"singleCar";

    //商家修改二手车产品信息
    public static final String UPDATE_CAR_PRODUCT = URL + S + "revampCar";

    //产品列表二手车之后的接口
    public static final String COMPPRODUCT = M + S + "compProduct";

    //修改产品上架
    public static final String REVAMPPUBLISH = M + S + "revampPublish";

    //修改产品下架
    public static final String REVAMPSOLDOUT = M + S + "revampSoldOut";

    //修改二手车状态
    public static final String CHANGESTATE = M + S + "changeState";

    //删除产品
    public static final String CANCELPRODUCT = M + S + "cancelProduct";

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

    //商家产品 - 查看车品牌
    public static final String SHOW_CAR_BRAND = URL + S + "getBrandNode";


    //模糊查询car
    public static final String SEARCHCAR = URL + S + "searchCar";

    // 查询Car信息
    public static final String GETCARPARAMDICT = URL + S + "getCarParamDict";

    // (热门车辆/模糊搜索品牌)
    public static final String GETCARBRANDDICT = URL + S + "getCarBrandDict";

    //二手车详情
    public static final String CAR_DETAIL = URL + S + "car_detail";

    //查询单个二手车信息（二手车编号）
    public static final String SINGLECAR = URL + S + "singleCar";

    //二手车详情 - 获取广告
    public static final String CAR_DETAIL_AD = URL + S + "selectAdveToApp";
    public static final String GETCARTRIANBYCARBRAND = URL + S +"getCarTrianByCarBrand";
    public static final String GETHOTCARBRANDDICT = URL + S +"getHotCarBrandDict";
    public static final String GETCARBRANDDICTBYINITIALS = URL + S +"getCarBrandDictByInitials";


    /**
     * 李岩使用接口start
     */
    //获取订单中的产品详情
    public static  final String ORDERS_SHOW_PRODUCT_BYID = URL + S + "examineProduct";
    //获取支付宝支付时所需的参数加签名的组合
    public static  final String ORDERS_ALIPAY_SIGNINFO = URL + S + "getAlipaySignInfo";
    //创建订单
    public static  final String ORDERS_CREATE = URL + S + "placeAnOrderComp";
    //确认支付订单
    public static  final String ORDERS_PAY_SUCCESS = URL + S + "settlementWarrant";
    //支付流水
    public static  final String ORDERS_PAY_LOG = URL + S + "payLog";
    //查询个人用户订单信息
    public static  final String ORDERS_GET_LIST = URL + S + "orderListPers";
    //根据订单号查询订单明细
    public static  final String ORDERS_GET_BYCODE= URL + S + "checkSingleOrder";
    //获取微信支付调起支付单
    public static  final String ORDERS_WECCHAT_CREATE= URL + S + "startWechatPay";
    //取消订单
    public static  final String ORDERS_CANCEL= URL + S + "deactivePaymentMethod";
    //获取二维码
    public static  final String ORDERS_QUERY_QRCODE = URL + S + "queryOrderQRCode";
    //使用二维码确认消费
    public static final String ORDERS_QRCODE_OPERATION = URL + S +  "operation";
    //使用验证码确认消费
    public static final String ORDERS_TOKEN_OPERATION = URL + S +  "updateOrderConsumption";
    //查询企业用户订单信息
    public static  final String ORDERS_GET_COMP_LIST = URL + S + "orderListComp";
    //钱包消费明细查询
    public static  final String COMP_WALLET_OPTION_DETAIL = URL + S + "consumptionDetail";
    //钱包余额查询
    public static  final String COMP_WALLET_BALANCE= URL + S + "walletBalance";
    //钱包综合查询
    public static  final String COMP_WALLET_INTEGRATED_QUERY= URL + S + "integratedQuery";
    //钱包查询帐户信息
    public static  final String COMP_WALLET_QUERY_ACCOUNT= URL + S + "balanceOrTransactionAccount";
    //绑定帐户发送验证码
    public static  final String COMP_WALLET_SEND_CHECKCODE= URL + S + "sendNoteCodeNoPhone";
    //绑定微信帐号
    public static  final String COMP_WALLET_BIND_WECHAT= URL + S + "bindingWechat";
    //绑定微信帐号
    public static  final String COMP_WALLET_BIND_ALIPAY= URL + S + "bindingAlipay";
    //绑定银行卡号
    public static  final String COMP_WALLET_BIND_BANK= URL + S + "bindingBankcard";
    //申请提现
    public static  final String COMP_WALLET_GET_CASH_SUBMIT = URL + S + "destoonFinanCecash";
    //提现列表
    public static  final String COMP_WALLET_DEPOST_LOG = URL + S + "withdrawDepositLog";
    //判断是否需要升级
    public static  final String UPTRADE_CHECK_VERSION = URL + S + "judgeVersion";
    //获取版本最新信息
    public static  final String UPTRADE_GET_VERSIONINFO = URL + S + "singleNewestVersion";
    //商家预约列表
    public static  final String COMP_BESPEAK_LIST = URL + S + "subscribeList";

    /**
     * 李岩使用接口end
     */

    //二手车详情 - 是否收藏   coll_content_id (car_id)
    public static final String CAR_DETAIL_COLLECT = URL + S + "whetherCollCar";

    //二手车详情 - 添加收藏   coll_content_id  (car_id)
    public static final String CAR_DETAIL_ADD_COLLECT = URL + S + "accretionCollCar";

    //二手车详情 - 订阅降价通知
    public static final String CAR_DETAIL_ADD_SALE = URL + S + "subscriptionCar";

    //降价提醒  -删除降价订阅
    public static final String DELETESUBSCRIPTION = URL + S + "deleteSubscription";


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

    // 估价Brand
    public static final String GETBRAND = URL + S + "getBrand";

    // 估价chexi
    public static final String GETJUHECARTRIAN = URL + S + "getJuheCarTrian";

    //估价年款
    public static final String GETJUHEMODEL = URL + S +"getJuheModel";

    // 估价城市
    public static final String GETJHCITYID = URL + S +"getJhCityId";

    // 估价
    public static final String GETCARASSESS = URL + S +"getCarAssess";

    // 估价模糊查询品牌
    public static final String GETBRANDBYBRANDNAME = URL + S +"getBrandByBrandName";

    // 首页banner
    public static final String SELECTADVEMAIN = URL + S +"selectAdveMain";

    // 京东商品分类
    public static final String SELECTJDSP = URL + S +"selectJdSp";

    //更换手机号 - 检验手机号是否是当前登录的手机号
    public static final String CHECK_PHONE = URL + S + "checkoutPhone";

    //更换手机号 - 校验旧手机号和验证码
    public static final String CHECK_OLD = URL + S + "checkoutToken_code";

    //更换手机号 - 新手机号
    public static final String CHECK_NEW = URL + S + "modificationpPhone";

    // 订阅查询车
    public static final String SELECTCARBYITEM = URL + S +"selectCarByItem";

    // 众生活banner
    public static final String SELECTZSHCENTER = URL + S +"selectZshCenter";

    // 众生活
    public static final String SELECTTYPE = URL + S +"selectType";

    // 查询美食项目
    public static final String GETUSUALLY = URL + S +"getUsually";

    // 查询众生活列表
    public static final String GETPAGEINFO = URL + S +"getPageInfo";

    // 众生活商家xiangqing
    public static final String GETBUSDETAILS = URL + S +"getBusDetails";

    // 违章查询City
    public static final String GETCITY = URL + S +"getCity";

    // 违章查询
    public static final String GETVIOLMSG = URL + S +"getViolMsg";

    // 违章查询图片识别
    public static final String GETDRIVERINFO = URL + S +"getDriverInfo";

    // 违章是否可以拍照
    public static final String GETPARA = URL + S +"getPara";

    // 启动页面
    public static final String SELECTBOOTPAGE = URL + S +"selectBootPage";

    // 启动轮播
    public static final String SELECTCAROUSEL = URL + S +"selectCarousel";
}
