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
