package com.okhttp.utils;

public class APIUrls {

    public final static String URL_DOMAIN = "http://39.107.247.82:10000/";

    public final static String protocol = URL_DOMAIN + "protocol.html";
    public final static String service = URL_DOMAIN + "protocol.html";

    public final static String URL_UPLOAD_FILES = URL_DOMAIN + "storage/handle";


    //用户信息
    public final static String login = URL_DOMAIN + "login/login";
    public final static String loginOrRegisterForCode = URL_DOMAIN + "login/loginOrRegisterForCode";
    public final static String createAuthCodeOnLoginOut = URL_DOMAIN + "web/user/createAuthCodeOnLoginOut";
    //发送更新手机号验证码
    public final static String createUpdatePhone = URL_DOMAIN + "web/user/createUpdatePhone";
    //获得游客身份
    public final static String getVisitor = URL_DOMAIN + "web/user/getVisitor";
    //获取用户信息
    public final static String userLoad = URL_DOMAIN + "web/user/load";

    // ==================================== 犬只管理 =============================================

    public final static String testUrl = URL_DOMAIN + "testUrl";

    /**
     * 悦保
     */
    public final static String getAccessToken =   "https://ai.ybinsure.com/s/api/getAccessToken";
    public final static String createPetArchives =   "https://ai.ybinsure.com/s/api/createPetArchives";
    public final static String petType =   "https://ai.ybinsure.com/s/api/petType";

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
