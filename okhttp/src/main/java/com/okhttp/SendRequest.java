package com.okhttp;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.base.utils.CommonUtil;
import com.okhttp.callbacks.Callback;
import com.okhttp.utils.APIUrls;
import com.okhttp.utils.OkHttpUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendRequest {
    private static String TAG = "SendRequest";

    /**
     * 检查是否注册
     *
     * @param phone
     * @param call
     */
    public static void loginCheckReg(String phone, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().params(map).url(APIUrls.loginCheckReg).build().execute(call);

    }

    /**
     * 检查验证码是否正确
     * <p>
     * phone 手机号
     * code 验证码
     * phoneModel 手机型号
     */
    public static void registerCheckCode(String phone, String code, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("phoneModel", Build.MODEL);
        OkHttpUtils.post().params(map).url(APIUrls.registerCheckCode).build().execute(call);

    }

    /**
     * 发送注册验证码
     */
    public static void createAuthCodeOnReg(String phone, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().params(map).url(APIUrls.createAuthCodeOnReg).build().execute(call);

    }

    /**
     * 手机号密码注册
     * <p>
     * plat 非必传
     * platVersion  非必传
     * phone  手机号
     * code 短信验证码
     * password 密码
     * againPassword 第二次输入密码
     * phoneModel 手机型号
     * sex 性别 1男 2女
     * birthdate 生日
     * name 昵称
     * icon 头像
     */
    public static void registerByPhoneAndPassword(String phone, String code, String password,
                                                  int sex, String birthdate, String name, String icon, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("password", password);
        map.put("againPassword", password);
        map.put("phoneModel", Build.MODEL);
        map.put("sex", String.valueOf(sex));
        map.put("birthdate", birthdate);
        map.put("name", name);
        map.put("icon", icon);
        OkHttpUtils.post().params(map).url(APIUrls.registerByPhoneAndPassword).build().execute(call);

    }

    /**
     * 密码登录
     */
    public static void login(String phone, String password, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        OkHttpUtils.post().params(map).url(APIUrls.login).build().execute(call);

    }

    /**
     * 一键登录
     *
     * @param accessToken
     * @param call
     */
    public static void loginOrRegisterByOneKey(String accessToken, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("accessToken", accessToken);
        OkHttpUtils.post().params(map).url(APIUrls.loginOrRegisterByOneKey).build().execute(call);

    }


    /**
     * 更新用户信息语音
     *
     * @param token
     * @param language
     * @param call
     */
    public static void updateUserLanguage(String token, String language, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("language", language);
        OkHttpUtils.post().params(map).url(APIUrls.updateUserLanguage).build().execute(call);

    }

//    /**
//     * 更新用户信息
//     *
//     * @param token
//     * @param key
//     * @param value
//     * @param call
//     */
//    public static void updateUserInfo(String token, String key, String value, Callback call) {
//        Map<String, String> map = new HashMap<>();
//        map.put("token", token);
//        map.put(key, value);
//        OkHttpUtils.post().params(map).url(APIUrls.updateUserInfo).build().execute(call);
//
//    }

    /**
     * 更新用户信息
     *
     * @param token
     * @param call
     */
    public static void updateUserInfo(String token, Map<String, String> keyValues, Callback call) {
        keyValues.put("token", token);
        OkHttpUtils.post().params(keyValues).url(APIUrls.updateUserInfo).build().execute(call);

    }


    // ==================================== 练琴帮 =============================================

    /**
     * 犬证办理
     *
     * @param token
     * @param call
     */
    public static void DogCertificate(String token,Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.channel_home).build().execute(call);

    }

    /**
     * 免疫证办理
     *
     * @param token
     * @param call
     */
    public static void DogImmuneHospital(String token,Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.channel_home).build().execute(call);

    }

    /**
     * 犬证年审
     *
     * @param token
     * @param call
     */
    public static void CertificateExamined(String token,Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.channel_home).build().execute(call);

    }

    /**
     * 犬只过户
     * @param token
     * @param map
     * @param call
     */
    public static void DogTransfer(String token,Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.channel_home).build().execute(call);

    }

    /**
     * 犬只注销
     * @param token
     * @param map
     * @param call
     */
    public static void DogLogout(String token,Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.channel_home).build().execute(call);

    }

    /**
     * 信息变更
     * @param token
     * @param map
     * @param call
     */
    public static void UpdateDogInfo(String token,Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.channel_home).build().execute(call);

    }

    /**
     * 频道详情
     *
     * @param token
     * @param id
     * @param call
     */
    public static void channel_load(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.channel_load).build().execute(call);

    }

    /**
     * 频道作品分页列表
     *
     * @param token
     * @param channelId
     * @param cursor
     * @param call
     */
    public static void channel_getChannelPager(String token, int channelId, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("channelId", String.valueOf(channelId));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.channel_getChannelPager).build().execute(call);

    }

    /**
     * 获取趣味测评列表
     *
     * @param token
     * @param call
     */
    public static void interesttest_getAllList(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.interesttest_getAllList).build().execute(call);

    }

    /**
     * 获取全部图片位置的轮播图
     *
     * @param token
     * @param bannerType 类型 1-首页 2-考级专区 3-比赛专区
     * @param call
     */
    public static void getBanner(String token, int bannerType, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("bannerType", String.valueOf(bannerType));
        OkHttpUtils.post().params(map).url(APIUrls.getBanner).build().execute(call);

    }

    /**
     * 正在练习的曲谱
     *
     * @param token
     * @param call
     */
    public static void songset_getLastTestSongSet(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.songset_getLastTestSongSet).build().execute(call);

    }

    /**
     * 曲谱分页列表
     *
     * @param token
     * @param cursor
     * @param call
     */
    public static void songset_getPager(String token, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.songset_getPager).build().execute(call);

    }

    /**
     * 搜索页热门教材
     *
     * @param token
     * @param call
     */
    public static void songset_getHotList(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("size", "6");
        OkHttpUtils.post().params(map).url(APIUrls.songset_getHotList).build().execute(call);

    }

    /**
     * 搜索教材
     *
     * @param token
     * @param content
     * @param cursor
     * @param call
     */
    public static void songset_getSearchPager(String token, String content, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("content", content);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.songset_getSearchPager).build().execute(call);

    }

    /**
     * 搜索用户
     *
     * @param token
     * @param content
     * @param cursor
     * @param call
     */
    public static void searchWebUser(String token, String content, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("content", content);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.searchWebUser).build().execute(call);

    }

    /**
     * 曲谱详情
     *
     * @param token
     * @param id
     * @param call
     */
    public static void songset_load(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.songset_load).build().execute(call);

    }

    /**
     * 曲详情
     *
     * @param token
     * @param id
     * @param call
     */
    public static void song_load(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        map.put("hideSvg", String.valueOf(1));
        OkHttpUtils.post().params(map).url(APIUrls.song_load).build().execute(call);

    }

    /**
     * 搜索页热门曲目
     *
     * @param token
     * @param call
     */
    public static void song_getHotList(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("size", "6");
        OkHttpUtils.post().params(map).url(APIUrls.song_getHotList).build().execute(call);

    }

    /**
     * 搜索音乐
     *
     * @param token
     * @param content
     * @param cursor
     * @param call
     */
    public static void song_getSearchPager(String token, String content, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("content", content);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.song_getSearchPager).build().execute(call);

    }


    /**
     * @param token
     * @param type       类型 1-音频 2-视频
     * @param url        视频/音频URL
     * @param duration   时长
     * @param base64data Base64数据
     * @param setid      谱集ID
     * @param sid        曲目ID
     * @param call
     */
    public static void songtest_submit(String token, int type, String url, int duration, String base64data, int setid, int sid, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("url", url);
        map.put("duration", String.valueOf(duration));
        map.put("setid", String.valueOf(setid));
        map.put("sid", String.valueOf(sid));
        map.put("base64data", base64data);
        OkHttpUtils.post().params(map).url(APIUrls.songtest_submit).build().execute(call);

    }

    public static void songtest_getRecordPager(String token, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.songtest_getRecordPager).build().execute(call);

    }

    /**
     * 测评记录设置隐私
     *
     * @param token
     * @param stidsJson
     * @param show
     * @param call
     */
    public static void songtest_updateSongTestShow(String token, String stidsJson, int show, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("stidsJson", stidsJson);
        map.put("show", String.valueOf(show));
        OkHttpUtils.post().params(map).url(APIUrls.songtest_updateSongTestShow).build().execute(call);

    }

    /**
     * 榜单弹奏分页列表
     *
     * @param token
     * @param cursor
     * @param call
     */
    public static void songtest_getRankPager(String token, int sid, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("sid", String.valueOf(sid));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.songtest_getRankPager).build().execute(call);

    }

    /**
     * 用户曲目测评记录分页列表
     *
     * @param token
     * @param sid
     * @param cursor
     * @param call
     */
    public static void songtest_getSongRecordPager(String token, int uid, int sid, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", String.valueOf(uid));
        map.put("sid", String.valueOf(sid));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.songtest_getSongRecordPager).build().execute(call);

    }

    /**
     * 练习曲目目录列表
     *
     * @param token
     * @param call
     */
    public static void songtestcatalog_getCatalogs(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.songtestcatalog_getCatalogs).build().execute(call);

    }

    /**
     * 练习曲目目录列表
     *
     * @param token
     * @param uid
     * @param sort  1-级数 2-活跃度 3-次数
     * @param asc   0-从高到低 1-从低到高
     * @param call
     */
    public static void songtestcatalog_getCatalogs(String token, int uid, int sort, int asc, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", String.valueOf(uid));
        map.put("sort", String.valueOf(sort));
        map.put("asc", String.valueOf(asc));
        OkHttpUtils.post().params(map).url(APIUrls.songtestcatalog_getCatalogs).build().execute(call);

    }


    /**
     * 作品详情
     *
     * @param token
     * @param id
     * @param call
     */
    public static void creation_load(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.creation_load).build().execute(call);

    }

    /**
     * 搜索内容
     *
     * @param token
     * @param content
     * @param cursor
     * @param call
     */
    public static void creation_getSearchPager(String token, String content, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("content", content);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.creation_getSearchPager).build().execute(call);

    }

    /**
     * 我的发布分页列表
     *
     * @param token
     * @param uid
     * @param cursor
     * @param call
     */
    public static void creation_getUserPager(String token, int uid, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", String.valueOf(uid));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.creation_getUserPager).build().execute(call);

    }

    /**
     * type  关注类型 1用户
     * typeId 第三方ID
     */
    public static void focusFans(String token, int type, int typeId, String url, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        OkHttpUtils.post().params(map).url(url).build().execute(call);
    }

    /**
     * @param token
     * @param type
     * @param cursor
     * @param url
     * @param call
     */
    public static void getFocusFansPager(String token, int type, String cursor, String url, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(url).build().execute(call);
    }

    /**
     * 收藏
     *
     * @param token
     * @param type   type 11:内容 12:教材 13:乐曲 14:测评
     * @param typeId
     * @param url
     * @param call
     */
    public static void favorite(String token, int uid, int type, int typeId, String url, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        map.put("id", String.valueOf(typeId));
        OkHttpUtils.post().params(map).url(url).build().execute(call);
    }

    /**
     * 收藏
     *
     * @param token
     * @param type
     * @param typeId
     * @param call
     */
    public static void favorite_add(String token, int type, int typeId, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        OkHttpUtils.post().params(map).url(APIUrls.favorite_add).build().execute(call);
    }

    /**
     * 取消收藏
     *
     * @param token
     * @param call
     */
    public static void favorite_delete(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.favorite_delete).build().execute(call);
    }

    /**
     * @param token
     * @param type   type 11:内容 12:教材 13:乐曲 14:测评
     * @param cursor
     * @param call
     */
    public static void favorite_getPager(String token, int type, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.favorite_getPager).build().execute(call);

    }

    /**
     * 点赞
     *
     * @param token
     * @param type   type 1-评论 8-作品
     * @param typeId
     * @param url
     * @param call
     */
    public static void support(String token, int uid, int type, int typeId, String url, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        OkHttpUtils.post().params(map).url(url).build().execute(call);
    }

    public static void support_add(String token, int type, int typeId, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        OkHttpUtils.post().params(map).url(APIUrls.support_add).build().execute(call);
    }

    public static void support_delete(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.support_delete).build().execute(call);
    }

    /**
     * 点赞分页列表
     *
     * @param token
     * @param cursor
     * @param url
     * @param call
     */
    public static void getSupportPager(String token, String cursor, String url, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(url).build().execute(call);

    }

    /**
     * 获取消息分页
     *
     * @param token
     * @param cursor
     * @param call
     */
    public static void systemMsg_getPager(String token, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.systemMsg_getPager).build().execute(call);

    }

    /**
     * 获取未读消息数
     *
     * @param token
     * @param call
     */
    public static void getUnReadNum(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.getUnReadNum).build().execute(call);

    }

    /**
     * 全部已读
     *
     * @param token
     * @param call
     */
    public static void readAllSystemMsg(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.readAllSystemMsg).build().execute(call);

    }


    /**
     * web端_分页获取具体类型id下的所有一级评论信息及子评论
     *
     * @param token
     * @param type   1:评论的 9:作品
     * @param typeId 具体的类型下面对应的ID
     * @param cursor
     * @param call
     */
    public static void comment_pager(String token, int type, int typeId, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.comment_pager).build().execute(call);

    }

    /**
     * @param token
     * @param type    1:评论的 9:作品
     * @param typeId
     * @param content
     * @param call
     */
    public static void comment_add(String token, int type, int typeId, String content, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        map.put("content", content);
        OkHttpUtils.post().params(map).url(APIUrls.comment_add).build().execute(call);

    }

    /**
     * 申请老师口令码
     *
     * @param token
     * @param name
     * @param phone
     * @param provinceId
     * @param cityId
     * @param call
     */
    public static void commandrequest_addTeacher(String token, String name, String phone, String provinceId, String cityId, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("name", name);
        map.put("phone", phone);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        OkHttpUtils.post().params(map).url(APIUrls.commandrequest_addTeacher).build().execute(call);

    }

    /**
     * 申请机构口令码
     *
     * @param token
     * @param name
     * @param phone
     * @param license
     * @param call
     */
    public static void commandrequest_addOrganization(String token, String name, String phone, String license, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("name", name);
        map.put("phone", phone);
        map.put("license", license);
        OkHttpUtils.post().params(map).url(APIUrls.commandrequest_addOrganization).build().execute(call);

    }

    /**
     * 绑定机构
     *
     * @param token
     * @param command
     * @param call
     */
    public static void userpool_bangOrganizationCommand(String token, String command, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("command", command);
        OkHttpUtils.post().params(map).url(APIUrls.userpool_bangOrganizationCommand).build().execute(call);

    }

    public static void userpool_unBangOrganization(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.userpool_unBangOrganization).build().execute(call);

    }


    /**
     * 绑定老师
     *
     * @param token
     * @param command
     * @param call
     */
    public static void userpool_bangTeacherCommand(String token, String command, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("command", command);
        OkHttpUtils.post().params(map).url(APIUrls.userpool_bangTeacherCommand).build().execute(call);

    }

    public static void userpool_unBangTeacher(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.userpool_unBangTeacher).build().execute(call);

    }

    /**
     * 我的-同学名单
     *
     * @param token
     * @param call
     */
    public static void userpool_getStudents(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.userpool_getStudents).build().execute(call);

    }

    public static void songtest_getWeekStudentSongTests(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.songtest_getWeekStudentSongTests).build().execute(call);

    }

    /**
     * 本周测评列表 - 学生
     *
     * @param token
     * @param uid
     * @param sort  1-级数 2-活跃度 3-次数
     * @param asc   0-从高到低 1-从低到高
     * @param call
     */
    public static void songteststudentinfo_getWeekInfos(String token, int uid, int sort, int asc, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", String.valueOf(uid));
        map.put("sort", String.valueOf(sort));
        map.put("asc", String.valueOf(asc));
        OkHttpUtils.post().params(map).url(APIUrls.songteststudentinfo_getWeekInfos).build().execute(call);

    }

    /**
     * 累计测评列表 - 学生
     *
     * @param token
     * @param uid
     * @param sort  1-级数 2-活跃度 3-次数
     * @param asc   0-从高到低 1-从低到高
     * @param call
     */
    public static void songteststudentinfo_getAllInfos(String token, int uid, int sort, int asc, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", String.valueOf(uid));
        map.put("sort", String.valueOf(sort));
        map.put("asc", String.valueOf(asc));
        OkHttpUtils.post().params(map).url(APIUrls.songteststudentinfo_getAllInfos).build().execute(call);

    }


    /**
     * 本周测评列表 - 老师
     *
     * @param token
     * @param uid
     * @param sort  1-级数 2-活跃度 3-次数
     * @param asc   0-从高到低 1-从低到高
     * @param call
     */
    public static void songTestTeacherInfo_getWeekInfos(String token, int uid, int sort, int asc, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", String.valueOf(uid));
        map.put("sort", String.valueOf(sort));
        map.put("asc", String.valueOf(asc));
        OkHttpUtils.post().params(map).url(APIUrls.songtestteacherinfo_getWeekInfos).build().execute(call);

    }

    /**
     * 累计测评列表 - 老师
     *
     * @param token
     * @param uid
     * @param sort  1-级数 2-活跃度 3-次数
     * @param asc   0-从高到低 1-从低到高
     * @param call
     */
    public static void songTestTeacherInfo_getAllInfos(String token, int uid, int sort, int asc, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("uid", String.valueOf(uid));
        map.put("sort", String.valueOf(sort));
        map.put("asc", String.valueOf(asc));
        OkHttpUtils.post().params(map).url(APIUrls.songtestteacherinfo_getAllInfos).build().execute(call);

    }

    /**
     * 获取信息
     *
     * @param token
     * @param call
     */
    public static void invitecommand_getInfo(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.invitecommand_getInfo).build().execute(call);

    }

    /**
     * 获取邀请的用户
     *
     * @param token
     * @param content
     * @param call
     */
    public static void getInviteUser(String token, String content, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("content", content);
        OkHttpUtils.post().params(map).url(APIUrls.invitecommand_getInviteUser).build().execute(call);

    }

    /**
     * 记录邀请信息
     *
     * @param token
     * @param inviteUid
     * @param call
     */
    public static void invitecommand_record(String token, int inviteUid, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("inviteUid", String.valueOf(inviteUid));
        OkHttpUtils.post().params(map).url(APIUrls.invitecommand_record).build().execute(call);

    }

    /**
     * @param token
     * @param month
     * @param rewardId
     * @param call
     */
    public static void viporder_createOrder(String token, int month, int rewardId, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("platform", String.valueOf(2));
        map.put("month", String.valueOf(month));
        map.put("rewardId", String.valueOf(rewardId));
        OkHttpUtils.post().params(map).url(APIUrls.viporder_createOrder).build().execute(call);

    }

    public static void orderCreateAliOrderAboutVip(String token, int vipoid, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("vipoid", String.valueOf(vipoid));
        OkHttpUtils.post().params(map).url(APIUrls.orderCreateAliOrderAboutVip).build().execute(call);

    }

    public static void orderCreateWxOrderAboutVip(String token, int vipoid, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("vipoid", String.valueOf(vipoid));
        OkHttpUtils.post().params(map).url(APIUrls.orderCreateWxOrderAboutVip).build().execute(call);

    }

    public static void orderTestSuccess(String token, long vipoid, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("oid", String.valueOf(vipoid));
        OkHttpUtils.post().params(map).url(APIUrls.orderTestSuccess).build().execute(call);

    }


    // ==================================== 练琴帮 =============================================


    /**
     * 反馈
     *
     * @param token
     * @param phone
     * @param content
     * @param imgsJson
     * @param call
     */
    public static void suggestionAdd(String token, String phone, String content, String imgsJson, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("phone", phone);
        map.put("content", content);
        map.put("imgsJson", imgsJson);
        Log.i(TAG, "suggestionAdd: " + map.toString());
        OkHttpUtils.post().params(map).url(APIUrls.suggestionAdd).build().execute(call);

    }

    /**
     * 帮助
     *
     * @param call
     */
    public static void helpCenter(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.helpCenter).build().execute(call);

    }

    /**
     * 发送用户注销证码
     */
    public static void createAuthCodeOnLoginOut(String token, String phone, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("phone", phone);
        OkHttpUtils.post().params(map).url(APIUrls.createAuthCodeOnLoginOut).build().execute(call);

    }

    /**
     * 用户注销
     *
     * @param token
     * @param call
     */
    public static void logout(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.logout).build().execute(call);

    }

    /**
     * platform 平台 1IOS 2android
     * moneyType 货币种类 1RMB 2USD
     * month 月数
     */
    public static void createOrder(String token, String platform, String moneyType, String month, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("platform", platform);
        map.put("moneyType", moneyType);
        map.put("month", month);
        OkHttpUtils.post().params(map).url(APIUrls.createOrder).build().execute(call);

    }


    public static void user_getRecommendList(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.user_getRecommendList).build().execute(call);

    }


    /**
     * =============================================================================================
     */

    /**
     * 发送登录验证码
     */
    public static void createAuthCodeOnLogin(String phone, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().params(map).url(APIUrls.createAuthCodeOnLogin).build().execute(call);

    }

    /**
     * 登陆-注册  (快捷登录)
     */
    public static void loginOrRegisterForCode(String phone, String code, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        OkHttpUtils.post().params(map).url(APIUrls.loginOrRegisterForCode).build().execute(call);

    }

    public static void createUpdatePhone(String token, String phone, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("phone", phone);
        OkHttpUtils.post().params(map).url(APIUrls.createUpdatePhone).build().execute(call);

    }

    public static void updatePhoneByPhone(String token, String phone, String code, String oldPhone, String oldCode, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("phone", phone);
        map.put("code", code);
        map.put("oldPhone", oldPhone);
        map.put("oldCode", oldCode);
        OkHttpUtils.post().params(map).url(APIUrls.updatePhoneByPhone).build().execute(call);

    }


    /**
     * 搜索好友
     */
    public static void searchFriends(String token, String key, Callback call) {
        Log.i(TAG, "searchFriends: " + key);
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("key", key);
        OkHttpUtils.post().params(map).url(APIUrls.searchFriends).build().execute(call);

    }

    public static void androidMobileQuery(String phone, String mobileToken, String clientIp, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("mobileToken", mobileToken);
        map.put("clientIp", clientIp);
        OkHttpUtils.post().params(map).url(APIUrls.url_androidMobileQuery).build().execute(call);

    }

    public static void checkName(String token, String name, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("name", name);
        OkHttpUtils.post().params(map).url(APIUrls.url_checkName).build().execute(call);

    }

    /**
     * 免密登录，没注册需要先注册
     */
    public static void loginForCode(String phone, String code, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        OkHttpUtils.post().params(map).url(APIUrls.url_loginForCode).build().execute(call);

    }

    /**
     * 忘记密码 时获取验证码
     */
    public static void createForgotAuthCode(String phone, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        OkHttpUtils.post().params(map).url(APIUrls.url_createForgotAuthCode).build().execute(call);

    }

    /**
     * 修改密码
     */
    public static void updatePasswordAndLogin(String phone, String code, String password, String againPassword, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        map.put("password", password);
        map.put("againPassword", againPassword);
        OkHttpUtils.post().params(map).url(APIUrls.url_updatePasswordAndLogin).build().execute(call);

    }

    public static void updatePassword(String token, String password, String againPassword, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("password", password);
        map.put("againPassword", againPassword);
        OkHttpUtils.post().params(map).url(APIUrls.url_updatePassword).build().execute(call);

    }

    public static void updatePasswordByOld(String token, String password, String againNewPassword, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("newPassword", password);
        map.put("againNewPassword", againNewPassword);
        OkHttpUtils.post().params(map).url(APIUrls.url_updatePasswordByOld).build().execute(call);

    }

    /**
     * 完善信息
     */
    public static void updateUser(String token, int id, String password, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        map.put("password", password);
        OkHttpUtils.post().params(map).url(APIUrls.url_updateUser).build().execute(call);

    }

    /**
     *
     */
    public static void loginForAuth(String openId, int type, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("key", openId);
        map.put("type", String.valueOf(type));
        OkHttpUtils.post().params(map).url(APIUrls.loginForAuth).build().execute(call);

    }


    public static void getVisitor(Callback call) {
        Map<String, String> map = new HashMap<>();
        OkHttpUtils.post().params(map).url(APIUrls.getVisitor).build().execute(call);

    }


    public static void getDesc(Callback call) {
        Map<String, String> map = new HashMap<>();
        OkHttpUtils.post().params(map).url(APIUrls.getDesc).build().execute(call);

    }

    public static void getUserInfo(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.getUserInfo).build().execute(call);

    }

    public static void userLoad(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.userLoad).build().execute(call);

    }

    /**
     * web端_分页获取评论
     */
    public static void commentPager(String token, int type, int typeId, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        OkHttpUtils.post().params(map).url(APIUrls.comment_pager).build().execute(call);

    }

    /**
     * 获取原评论下的子评论
     */
    public static void loadAllCommentPager(String token, String type, String typeId, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("typeId", String.valueOf(typeId));
        map.put("size", "10");
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.comment_LOADALL).build().execute(call);

    }

    public static void webFavoriteSupportPager(String token, int type, String nextCursor, String url, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("size", "20");
        map.put("cursor", nextCursor);
        OkHttpUtils.post().params(map).url(url).build().execute(call);

    }

    public static void timeline_getFocusPager(String token, String size, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("size", size);
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.timeline_getFocusPager).build().execute(call);
    }

    public static void timeline_load(String token, String id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", id);
        OkHttpUtils.post().params(map).url(APIUrls.timeline_load).build().execute(call);
    }

    public static void getHotTags(String token, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.getHotTags).build().execute(call);
    }

    public static void order_aliOrderPayParam(String token, String oid, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("oid", oid);
        OkHttpUtils.post().params(map).url(APIUrls.order_aliOrderPayParam).build().execute(call);
    }

    public static void order_wxOrderPayParam(String token, String oid, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("oid", oid);
        OkHttpUtils.post().params(map).url(APIUrls.order_wxOrderPayParam).build().execute(call);
    }

    /**
     * 版本更新
     */
    public static void getVersion(String version, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("versionNumber", version);
        map.put("type", "1");
        OkHttpUtils.get().params(map).url(APIUrls.url_getVersion).build().execute(call);
    }
}
