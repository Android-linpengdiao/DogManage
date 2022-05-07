package com.okhttp.utils;

public class APIUrls {

    public final static String URL_DOMAIN = "http://124.70.40.92:9204/";

    public final static String protocol = URL_DOMAIN + "protocol.html";
    public final static String service = URL_DOMAIN + "protocol.html";


    //APP登录
    public final static String userLogin = URL_DOMAIN + "user/toLogin";
    //发送短信验证
    public final static String sendMessageUser = URL_DOMAIN + "user/sendMessageUser";
    //获得游客身份
    public final static String getVisitor = URL_DOMAIN + "web/user/getVisitor";
    //获取用户信息
    public final static String userLoad = URL_DOMAIN + "web/user/load";

    // ==================================== 犬只管理 =============================================

    public final static String testUrl = URL_DOMAIN + "testUrl";

    //政策法规
    public final static String noticeList = URL_DOMAIN + "notice/list";
    //政策法规详情
    public final static String getNoticeById = URL_DOMAIN + "notice/getNoticeById";
    //获取banner图片列表
    public final static String bannerInfoList = URL_DOMAIN + "bannerInfo/list";
    //禁养犬宣传接口
    public final static String getForbiddenById = URL_DOMAIN + "forbidden/getForbiddenById";

    /**
     * 办理犬证
     */
    //验证用户是否填写 个人信息
    public final static String checkingDogUser = URL_DOMAIN + "user/checkingDogUser";
    //获取犬主信息
    public final static String getDogUser = URL_DOMAIN + "user/getDogUser";
    //保存犬主信息
    public final static String editDogUser = URL_DOMAIN + "user/editDogUser";
    //保存犬只
    public final static String savaDog = URL_DOMAIN + "user/dog/savaDog";
    //验证犬只是否可养
    public final static String verificationDog = URL_DOMAIN + "user/dog/verificationDog";
    //获取价格、手里单位信息接口
    public final static String getHandleInfo = URL_DOMAIN + "user/dog/getHandleInfo";
    //犬证提交审核
    public final static String approveDogLicence = URL_DOMAIN + "user/dog/approveDogLicence";


    /**
     * 犬只免疫办理
     */
    //根据坐标获取宠物医院信息
    public final static String getHospitalPosition = URL_DOMAIN + "user/dog/getHospitalPosition";
    //免疫证 提交免疫证办理
    public final static String saveImmune = URL_DOMAIN + "user/dog/saveImmune";


    //获取犬只信息列表
    public final static String getDogList = URL_DOMAIN + "user/dog/getDogList";


    /**
     * 我的
     */
    //获取犬证列表
    public final static String getDogLicenceList = URL_DOMAIN + "user/dog/getDogLicenceList";
    //获取个人犬只免疫列表
    public final static String getDogImmuneList = URL_DOMAIN + "/user/dog/getDogImmuneList";
    //犬证办理记录
    public final static String getUserDogLicence = URL_DOMAIN + "user/dog/getUserDogLincence";
    //犬证办理记录详情
    public final static String getDogLicenceDetail = URL_DOMAIN + "user/dog/getDogLincenceDetail";
    //犬证 获取犬主信息
    public final static String getUserById = URL_DOMAIN + "user/dog/getUserById";
    //犬证 获取犬只详情信息
    public final static String getDogById = URL_DOMAIN + "user/dog/getDogById";


    /**
     * 悦保
     */
    public final static String getAccessToken = "https://ai.ybinsure.com/s/api/getAccessToken";
    public final static String createPetArchives = "https://ai.ybinsure.com/s/api/createPetArchives";
    public final static String petType = "https://ai.ybinsure.com/s/api/petType";

    /**
     * 获取OBS信息
     */
    public final static String getObsFormInfo = URL_DOMAIN + "obs/getObsFormInfo";

    /**
     * 支付
     */
    public final static String viporder_createOrder = URL_DOMAIN + "viporder/createOrder";//记录邀请信息
    //支付宝_VIP订单_生成支付订单
    public final static String orderCreateAliOrderAboutVip = URL_DOMAIN + "order/createAliOrderAboutVip";
    //微信_VIP订单_生成支付订单
    public final static String orderCreateWxOrderAboutVip = URL_DOMAIN + "order/createWxOrderAboutVip";
    public final static String order_aliOrderPayParam = URL_DOMAIN + "order/aliOrderPayParam";//支付包支付 oid
    public final static String order_wxOrderPayParam = URL_DOMAIN + "order/wxOrderPayParam";//微信支付 oid

    // ==================================== 犬只管理 =============================================

}
