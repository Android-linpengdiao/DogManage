package com.okhttp;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.base.utils.CommonUtil;
import com.okhttp.callbacks.Callback;
import com.okhttp.utils.APIUrls;
import com.okhttp.utils.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendRequest {
    private static String TAG = "SendRequest";

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

    public static void getVisitor(Callback call) {
        Map<String, String> map = new HashMap<>();
        OkHttpUtils.post().params(map).url(APIUrls.getVisitor).build().execute(call);

    }

    public static void userLoad(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.userLoad).build().execute(call);

    }


    // ==================================== 练琴帮 =============================================

    /**
     * 犬证办理
     *
     * @param token
     * @param call
     */
    public static void DogCertificate(String token, Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.testUrl).build().execute(call);

    }

    /**
     * 免疫证办理
     *
     * @param token
     * @param call
     */
    public static void DogImmuneHospital(String token, Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.testUrl).build().execute(call);

    }

    /**
     * 犬证年审
     *
     * @param token
     * @param call
     */
    public static void CertificateExamined(String token, Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.testUrl).build().execute(call);

    }

    /**
     * 犬只过户
     *
     * @param token
     * @param map
     * @param call
     */
    public static void DogTransfer(String token, Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.testUrl).build().execute(call);

    }

    /**
     * 犬只注销
     *
     * @param token
     * @param map
     * @param call
     */
    public static void DogLogout(String token, Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.testUrl).build().execute(call);

    }

    /**
     * 信息变更
     *
     * @param token
     * @param map
     * @param call
     */
    public static void UpdateDogInfo(String token, Map<String, String> map, Callback call) {
        map.put("token", token);
        OkHttpUtils.post().params(map).url(APIUrls.testUrl).build().execute(call);

    }

    public static void getPager(String token, int type, String cursor, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("type", String.valueOf(type));
        map.put("cursor", cursor);
        OkHttpUtils.post().params(map).url(APIUrls.testUrl).build().execute(call);

    }


    /*====================================    悦保      ==========================================*/

    /**
     * 获取鉴权Token
     *
     * @param accessKey
     * @param accessSecret
     * @param call
     */
    public static void getAccessToken(String accessKey, String accessSecret, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);
        map.put("accessSecret", accessSecret);
        OkHttpUtils.get().params(map).url(APIUrls.getAccessToken).build().execute(call);

    }

    /**
     * 宠物狗面部+鼻纹建档
     * <p>
     * file	file	图片文件需要使用post multipart/form-data的方式上传；支持PNG、JPG、JPEG、BMP格式；图片大小最大限制2M；
     * imageUrl	string	图片的URL地址；支持PNG、JPG、JPEG、BMP格式；优先使用该参数
     * imageBase64	string	图片base64编码；支持PNG、JPG、JPEG、BMP格式；
     * 如果同时传入file、imageUrl、imageBase64，本API使用顺序为imageUrl优先，imageBase64最低
     *
     * @param token
     * @param filePath
     * @param call
     */
    public static void createPetArchives(String token, String filePath, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        OkHttpUtils.post().addFile("file", filename, new File(filePath)).params(map).url(APIUrls.createPetArchives).build().execute(call);

    }

    /**
     * 宠物品种识别
     * <p>
     * file	file	图片文件需要使用post multipart/form-data的方式上传；支持PNG、JPG、JPEG、BMP格式；图片大小最大限制2M；
     * imageUrl	string	图片的URL地址；支持PNG、JPG、JPEG、BMP格式；优先使用该参数
     * imageBase64	string	图片base64编码；支持PNG、JPG、JPEG、BMP格式；
     * 如果同时传入file、imageUrl、imageBase64，本API使用顺序为imageUrl优先，imageBase64最低
     * petType 宠物种类：0为狗，1为猫
     * @param token
     * @param filePath
     * @param call
     */
    public static void petType(String token, String filePath, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("petType", String.valueOf(0));
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        OkHttpUtils.post().addFile("file", filename, new File(filePath)).params(map).url(APIUrls.petType).build().execute(call);

    }

    /*====================================    支付      ==========================================*/

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

}
