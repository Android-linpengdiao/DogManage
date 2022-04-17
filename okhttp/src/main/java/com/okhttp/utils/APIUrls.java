package com.okhttp.utils;

public class APIUrls {

//    public final static String URL_DOMAIN = "http://39.107.247.82:19000/";

    public final static String URL_DOMAIN = "http://39.107.247.82:19000/";
    public final static String URL_DOMAIN_WEB = "http://39.107.247.82:19000";

    public final static String user_agreement = URL_DOMAIN + "user_agreement.html";
    public final static String privacy_policy = URL_DOMAIN + "privacy_policy.html";

    public final static String URL_UPLOAD_FILES = URL_DOMAIN + "storage/handle";
    public final static String URL_createSecurityToken = URL_DOMAIN + "storage/createSecurityToken";


    // 分享链接
    public final static String URL_shareUserInfo = URL_DOMAIN + "share_view/html/shareDownload/userInfo.html?id=%s";
    public final static String URL_shareApp = URL_DOMAIN + "share_view/html/shareDownload/shareApp.html";
    public final static String URL_shareComment = URL_DOMAIN + "share_view/html/shareDownload/comment.html?id=%s";
    public final static String URL_shareRoom = URL_DOMAIN + "share_view/html/shareDownload/shareRoom.html?id=%s";
    public final static String URL_shareClub = URL_DOMAIN + "share_view/html/shareDownload/shareClub.html?id=%s";


    //用户信息
    public final static String loginCheckReg = URL_DOMAIN + "login/checkReg";
    public final static String registerCheckCode = URL_DOMAIN + "register/checkCode";
    public final static String createAuthCodeOnReg = URL_DOMAIN + "register/createAuthCodeOnReg";
    public final static String registerByPhoneAndPassword = URL_DOMAIN + "register/registerByPhoneAndPassword";
    public final static String login = URL_DOMAIN + "login/login";
    public final static String createAuthCodeOnLogin = URL_DOMAIN + "login/createAuthCodeOnLogin";
    public final static String loginOrRegisterForCode = URL_DOMAIN + "login/loginOrRegisterForCode";
    public final static String url_loginForCode = URL_DOMAIN + "login/loginForCode";
    public final static String url_createForgotAuthCode = URL_DOMAIN + "web/user/createForgotAuthCode";
    public final static String url_updatePasswordAndLogin = URL_DOMAIN + "web/user/updatePasswordAndLogin";
    public final static String url_updatePassword = URL_DOMAIN + "web/user/updatePassword";
    public final static String url_updatePasswordByOld = URL_DOMAIN + "web/user/updatePasswordByOld";
    public final static String url_updateUser = URL_DOMAIN + "web/user/updateUser";
    public final static String url_androidMobileQuery = URL_DOMAIN + "login/androidMobileQuery";
    public final static String url_checkName = URL_DOMAIN + "web/user/checkName";
    public final static String createAuthCodeOnLoginOut = URL_DOMAIN + "web/user/createAuthCodeOnLoginOut";
    public final static String loginOrRegisterByOneKey = URL_DOMAIN + "login/loginOrRegisterByOneKey";
    //注销账号
    public final static String logout = URL_DOMAIN + "web/user/logout";

    // ==================================== 练琴帮 =============================================

    /**
     * 首页
     */
    //首页频道信息
    public final static String channel_home = URL_DOMAIN + "channel/home";
    //频道详情
    public final static String channel_load = URL_DOMAIN + "channel/load";
    //频道作品分页列表
    public final static String channel_getChannelPager = URL_DOMAIN + "channel/getChannelPager";
    //获取趣味测评列表
    public final static String interesttest_getAllList = URL_DOMAIN + "interesttest/getAllList";
    //获取全部图片位置的轮播图
    public final static String getBanner = URL_DOMAIN + "web/banner/getBannerList";


    //曲谱分页列表
    public final static String songset_getPager = URL_DOMAIN + "songset/getPager";
    //正在练习的曲谱
    public final static String songset_getLastTestSongSet = URL_DOMAIN + "songset/getLastTestSongSet";
    //曲谱详情
    public final static String songset_load = URL_DOMAIN + "songset/load";
    //搜索页热门教材
    public final static String songset_getHotList = URL_DOMAIN + "songset/getHotList";
    //搜索教材
    public final static String songset_getSearchPager = URL_DOMAIN + "songset/getSearchPager";


    //曲详情
    public final static String song_load = URL_DOMAIN + "song/load";
    //搜索页热门曲目
    public final static String song_getHotList = URL_DOMAIN + "song/getHotList";
    //搜索音乐
    public final static String song_getSearchPager = URL_DOMAIN + "song/getSearchPager";


    //提交数据
    public final static String songtest_submit = URL_DOMAIN + "songtest/submit";
    //用户测评记录
    public final static String songtest_getRecordPager = URL_DOMAIN + "songtest/getRecordPager";
    //测评记录设置隐私
    public final static String songtest_updateSongTestShow = URL_DOMAIN + "songtest/updateSongTestShow";
    //榜单弹奏分页列表
    public final static String songtest_getRankPager = URL_DOMAIN + "songtest/getRankPager";
    //用户曲目测评记录分页列表
    public final static String songtest_getSongRecordPager = URL_DOMAIN + "songtest/getSongRecordPager";
    public final static String songtest_getWeekStudentSongTests = URL_DOMAIN + "songtest/getWeekStudentSongTests";
    //练习曲目目录列表
    public final static String songtestcatalog_getCatalogs = URL_DOMAIN + "songtestcatalog/getCatalogs";


    //作品详情
    public final static String creation_load = URL_DOMAIN + "creation/load";
    //搜索内容
    public final static String creation_getSearchPager = URL_DOMAIN + "creation/getSearchPager";
    //我的发布分页列表
    public final static String creation_getUserPager = URL_DOMAIN + "creation/getUserPager";


    public final static String focusfansAdd = URL_DOMAIN + "web/focusfans/add";
    public final static String focusfansDel = URL_DOMAIN + "web/focusfans/del";
    //获取我的关注列表
    public final static String getFocusPager = URL_DOMAIN + "web/focusfans/getFocusPager";
    //获取粉丝列表
    public final static String getFansPager = URL_DOMAIN + "web/focusfans/getFansPager";
    //收藏
    public final static String favorite_add = URL_DOMAIN + "web/favorite/add";//查看他人的信息
    //取消收藏
    public final static String favorite_delete = URL_DOMAIN + "web/favorite/delete";
    //我的收藏分页列表
    public final static String favorite_getPager = URL_DOMAIN + "web/favorite/getPager";
    public final static String support_add = URL_DOMAIN + "web/support/add";//点赞
    public final static String support_delete = URL_DOMAIN + "web/support/delete";//取消点赞
    public final static String support_getPager = URL_DOMAIN + "web/support/getPager";//我的点赞分页列表
    public final static String support_getObtainPager = URL_DOMAIN + "web/support/getObtainPager";//我的获赞分页列表

    public final static String systemMsg_getPager = URL_DOMAIN + "systemMsg/getPager";//获取消息分页
    public final static String getUnReadNum = URL_DOMAIN + "systemMsg/getUnReadNum";//获取未读消息数
    public final static String readAllSystemMsg = URL_DOMAIN + "systemMsg/readAllSystemMsg";//全部已读

    //web端_分页获取具体类型id下的所有一级评论信息及子评论
    public final static String comment_pager = URL_DOMAIN + "web/comment/pager";
    public final static String comment_add = URL_DOMAIN + "web/comment/add";


    public final static String searchWebUser = URL_DOMAIN + "web/user/searchWebUser";//搜索用户

    /**
     * 我的
     */
    public final static String commandrequest_addTeacher = URL_DOMAIN + "commandrequest/addTeacher";//申请老师口令码
    public final static String commandrequest_addOrganization = URL_DOMAIN + "commandrequest/addOrganization";//申请机构口令码
    public final static String userpool_bangOrganizationCommand = URL_DOMAIN + "userpool/bangOrganizationCommand";//绑定机构
    public final static String userpool_unBangOrganization = URL_DOMAIN + "userpool/unBangOrganization";//解绑机构
    public final static String userpool_bangTeacherCommand = URL_DOMAIN + "userpool/bangTeacherCommand";//绑定老师
    public final static String userpool_unBangTeacher = URL_DOMAIN + "userpool/unBangTeacher";//解绑老师
    public final static String userpool_getStudents = URL_DOMAIN + "userpool/getStudents";//我的-同学名单

    public final static String songteststudentinfo_getWeekInfos = URL_DOMAIN + "songteststudentinfo/getWeekInfos";//本周测评列表(学生)
    public final static String songteststudentinfo_getAllInfos = URL_DOMAIN + "songteststudentinfo/getAllInfos";//累计测评列表(学生)
    public final static String songtestteacherinfo_getWeekInfos = URL_DOMAIN + "songtestteacherinfo/getAllInfos";//本周测评列表(老师)
    public final static String songtestteacherinfo_getAllInfos = URL_DOMAIN + "songtestteacherinfo/getAllInfos";//累计测评列表(老师)

    public final static String invitecommand_getInfo = URL_DOMAIN + "invitecommand/getInfo";//获取信息
    public final static String invitecommand_getInviteUser = URL_DOMAIN + "invitecommand/getInviteUser";//获取邀请的用户
    public final static String invitecommand_record = URL_DOMAIN + "invitecommand/record";//记录邀请信息

    public final static String viporder_createOrder = URL_DOMAIN + "viporder/createOrder";//记录邀请信息
    //支付宝_VIP订单_生成支付订单
    public final static String orderCreateAliOrderAboutVip = URL_DOMAIN + "order/createAliOrderAboutVip";
    //微信_VIP订单_生成支付订单
    public final static String orderCreateWxOrderAboutVip = URL_DOMAIN + "order/createWxOrderAboutVip";
    public final static String orderTestSuccess = URL_DOMAIN + "order/testSuccess";



    // ==================================== 练琴帮 =============================================

    /**
     * 我的
     */
    public final static String updateUserLanguage = URL_DOMAIN + "user/updateUserLanguage";
    public final static String updateUserInfo = URL_DOMAIN + "web/user/updateUserInfo";
    public final static String suggestionAdd = URL_DOMAIN + "suggestion/add";
    public final static String helpCenter = URL_DOMAIN + "helpCenter/getWebQuestionPager";


    public final static String getSystemMsgNumber = URL_DOMAIN + "systemMsg/getSystemMsgNumber";


    //推荐
    public final static String user_getRecommendList = URL_DOMAIN + "web/user/getRecommendList";
    //话题
    public final static String getInterestChildren = URL_DOMAIN + "interest/getChildren";
    //搜索-俱乐部
    public final static String club_getSearchList = URL_DOMAIN + "club/getSearchList";
    public final static String user_getSearchList = URL_DOMAIN + "web/user/getSearchList";
    public final static String clubroom_getSearchList = URL_DOMAIN + "clubroom/getSearchList";
    public final static String getClubsByInterestId = URL_DOMAIN + "club/getClubsByInterestId";


    /**
     * 三方注册登录
     */
    //第三方授权登录
    public final static String loginForAuth = URL_DOMAIN + "register/loginForAuth";
    //第三方授权绑定手机号验证码
    public final static String bangPhoneCode = URL_DOMAIN + "register/bangPhoneCode";
    //第三方授权绑定手机号
    public final static String bangPhoneByKey = URL_DOMAIN + "register/bangPhoneByKey";
    //注册手机号,绑定第三方
    public final static String bindingAuthkey = URL_DOMAIN + "web/user/bindingAuthKey";
    //解绑定第三方
    public final static String unBindingAuthkey = URL_DOMAIN + "web/user/unBindingAuthKey";
    //一键登录
    public final static String androidMobileQuery = URL_DOMAIN + "login/androidMobileQuery";
    //发送更新手机号验证码
    public final static String createUpdatePhone = URL_DOMAIN + "web/user/createUpdatePhone";
    // 更新手机号接口
    public final static String updatePhoneByPhone = URL_DOMAIN + "web/user/updatePhoneByPhone";
    //更换新手机号
    public final static String updatePhone = URL_DOMAIN + "web/user/updatePhone";
    //
    public final static String createMyPhoneCode = URL_DOMAIN + "web/user/createMyPhoneCode";
    //获得游客身份
    public final static String getVisitor = URL_DOMAIN + "web/user/getVisitor";


    public final static String url_getBannerList = URL_DOMAIN + "web/banner/getBannerList";
    //版本更新
    public final static String url_getVersion = URL_DOMAIN + "web/version/getNewVersionList";
    //新增动态
    public final static String timelineAdd = URL_DOMAIN + "timeline/add";
    //删除动态
    public final static String timelineDelete = URL_DOMAIN + "timeline/delete";

    /**
     * 搜索
     */
    public final static String hotWord = URL_DOMAIN + "hotWord/getList";
    public final static String searchTimelineTagForum = URL_DOMAIN + "search/searchTimelineTagForum";
    public final static String loadsTimeline = URL_DOMAIN + "timeline/loads";
    public final static String searchFriends = URL_DOMAIN + "friends/searchFriends";

    /**
     * 好友
     */
    //好友列表
    public final static String getFriendsList = URL_DOMAIN + "friends/getFriendsList";
    public final static String getIsFriends = URL_DOMAIN + "friends/isFriends";
    public final static String getBlockList = URL_DOMAIN + "friends/getBlockList";
    public final static String blockFriends = URL_DOMAIN + "friends/blockFriends";
    public final static String restoreFriends = URL_DOMAIN + "friends/restoreFriends";

    //支付订单
    public final static String createOrder = URL_DOMAIN + "viporder/createOrder";

    //获得配置信息
    public final static String getListByType = URL_DOMAIN + "dictionary/getListByType";
    //获得一条简介
    public final static String getDesc = URL_DOMAIN + "library/getDesc";
    public final static String getUserInfo = URL_DOMAIN + "web/user/getUserInfo";
    public final static String userLoad = URL_DOMAIN + "web/user/load";

    public final static String comment_LOADALL = URL_DOMAIN + "web/comment/load";
    public final static String comment_getAllList = URL_DOMAIN + "web/comment/getAllList";

    public final static String timeline_getFocusPager = URL_DOMAIN + "timeline/getFocusPager";//获得关注动态
    public final static String timeline_load = URL_DOMAIN + "timeline/load";//动态详情
    public final static String timeline_tagTimeline = URL_DOMAIN + "timeline/tagTimeline";//动态详情
    public final static String getTimelineTid = URL_DOMAIN + "timeline/getTimelineTid";//动态详情

    public final static String getHotTags = URL_DOMAIN + "tag/getHotTags";


    public final static String chatgroup_getAllList = URL_DOMAIN + "chatgroup/getAllList";//获得我的群组
    public final static String getNumber = URL_DOMAIN + "chatgroup/getNumber";
    public final static String addRongCloudToWhiteList = URL_DOMAIN + "friends/addRongCloudToWhiteList";


    public final static String getFriendInfo = URL_DOMAIN + "friends/getFriendInfo";//查看他人的信息


    public final static String order_aliOrderPayParam = URL_DOMAIN + "order/aliOrderPayParam";//支付包支付 oid
    public final static String order_wxOrderPayParam = URL_DOMAIN + "order/wxOrderPayParam";//微信支付 oid


}
