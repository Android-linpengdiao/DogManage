package com.okhttp;

import android.util.Log;

import com.base.BaseApplication;
import com.okhttp.callbacks.Callback;
import com.okhttp.utils.APIUrls;
import com.okhttp.utils.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SendRequest {

    private static String TAG = "SendRequest";

    /**
     * APP登录
     * 设备类型 1 ios 2 安卓
     */
    public static void userLogin(String phone, String code, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("loginPhone", phone);
        map.put("code", code);
        map.put("tremType", "2");
        OkHttpUtils.post().params(map).url(APIUrls.userLogin).build().execute(call);

    }

    /**
     * 发送短信验证
     *
     * @param loginPhone
     * @param call
     */
    public static void sendMessageUser(String loginPhone, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("loginPhone", loginPhone);
        OkHttpUtils.post().params(map).url(APIUrls.sendMessageUser).build().execute(call);

    }

    public static void userLoad(String token, int id, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("id", String.valueOf(id));
        OkHttpUtils.post().params(map).url(APIUrls.userLoad).build().execute(call);

    }

    /**
     * 政策法规
     *
     * @param pageNum
     * @param pageSize
     * @param call
     */
    public static void noticeList(int pageNum, int pageSize, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        OkHttpUtils.post().params(map).url(APIUrls.noticeList).build().execute(call);

    }

    /**
     * 政策法规详情
     *
     * @param noticeId
     * @param call
     */
    public static void getNoticeById(int noticeId, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("noticeId", String.valueOf(noticeId));
        OkHttpUtils.post().params(map).url(APIUrls.getNoticeById).build().execute(call);

    }

    /**
     * 获取banner图片列表
     *
     * @param pageNum
     * @param pageSize
     * @param call
     */
    public static void bannerInfoList(int pageNum, int pageSize, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        OkHttpUtils.post().params(map).url(APIUrls.bannerInfoList).build().execute(call);

    }

    public static void getForbiddenById(Callback call) {
        OkHttpUtils.post().url(APIUrls.getForbiddenById).build().execute(call);

    }

    /**
     * 验证用户是否填写 个人信息
     *
     * @param call
     */
    public static void checkingDogUser(Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).url(APIUrls.checkingDogUser).build().execute(call);

    }

    /**
     * 获取犬主信息
     *
     * @param call
     */
    public static void getDogUser(Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).url(APIUrls.getDogUser).build().execute(call);

    }

    /**
     * 保存犬主信息
     *
     * @param map
     * @param call
     */
    public static void editDogUser(Map<String, String> map, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.editDogUser).build().execute(call);

    }

    /**
     * 保存犬只
     *
     * @param map
     * @param call
     */
    public static void savaDog(Map<String, String> map, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.savaDog).build().execute(call);

    }

    /**
     * 验证犬只是否可养
     *
     * @param dogType
     * @param call
     */
    public static void verificationDog(String dogType, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("dogType", dogType);
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.verificationDog).build().execute(call);

    }

    /**
     * 获取价格、手里单位信息接口
     *
     * @param map
     * @param call
     */
    public static void getHandleInfo(Map<String, String> map, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getHandleInfo).build().execute(call);

    }

    /**
     * 犬证提交审核
     *
     * @param map
     * @param call
     */
    public static void approveDogLicence(Map<String, String> map, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.approveDogLicence).build().execute(call);

    }


    /**
     * 根据坐标获取宠物医院信息
     *
     * @param coordinate 坐标 经纬度 2342342,12312
     * @param call
     */
    public static void getHospitalPosition(String coordinate, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("coordinate", coordinate);
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getHospitalPosition).build().execute(call);

    }

    /**
     * 免疫证 提交免疫证办理
     *
     * @param dogId    犬只id
     * @param unitId   医院id
     * @param unitName 医院名称
     * @param call
     */
    public static void saveImmune(int dogId, int unitId, String unitName, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("dogId", String.valueOf(dogId));
        map.put("unitId", String.valueOf(unitId));
        map.put("unitName", unitName);
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.saveImmune).build().execute(call);

    }


    /**
     * 犬证年审-选择我的犬只详情
     *
     * @param call
     * @paramlincenceId 犬证id
     */
    public static void getDogLicenseDetail(int lincenceId, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        Map<String, String> map = new HashMap<>();
        map.put("lincenceId", String.valueOf(lincenceId));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getDogLicenseDetail).build().execute(call);

    }


    // ==================================== 犬证年审 =============================================


    /**
     * 获取犬只信息列表
     *
     * @param call
     */
    public static void getDogList(Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).url(APIUrls.getDogList).build().execute(call);

    }


    // ==================================== 我的 =============================================


    /**
     * 获取犬证列表
     *
     * @param call
     */
    public static void getDogLicenceList(Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).url(APIUrls.getDogLicenceList).build().execute(call);

    }

    /**
     * 获取个人犬只免疫列表
     *
     * @param call
     */
    public static void getDogImmuneList(Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.post().headers(headers).url(APIUrls.getDogImmuneList).build().execute(call);

    }


    /**
     * 犬证提交审核
     *
     * @param lincenceStatus 办理状态 0 全部 1：待审核 2：代缴费 3：审核驳回 4：已办结 5：已过期 6：已注销
     * @param call
     */
    public static void getUserDogLicence(int lincenceStatus, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("lincenceStatus", String.valueOf(lincenceStatus));

        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getUserDogLicence).build().execute(call);

    }

    /**
     * 犬证办理记录详情
     *
     * @param lincenceId
     * @param call
     */
    public static void getDogLicenceDetail(int lincenceId, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("lincenceId", String.valueOf(lincenceId));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getDogLicenceDetail).build().execute(call);

    }

    /**
     * 犬证 获取犬主信息
     *
     * @param userId 用户id
     * @param dogId  犬只id
     * @param call
     */
    public static void getUserById(int userId, int dogId, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(userId));
        map.put("dogId", String.valueOf(dogId));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getUserById).build().execute(call);

    }

    /**
     * 犬证 获取犬只详情信息
     *
     * @param dogId 犬只id
     * @param call
     */
    public static void getDogById(int dogId, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("dogId", String.valueOf(dogId));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getDogById).build().execute(call);

    }


    /**
     * 免疫证办理记录列表
     *
     * @param licenceStatus 办理状态 0 默认 1：未接种 2：已接种 3: 即将过期 4: 已过期
     * @param call
     */
    public static void getDogImmuneStatusList(int licenceStatus, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("licenceStatus", String.valueOf(licenceStatus));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getDogImmuneStatusList).build().execute(call);

    }

    /**
     * 犬证办理记录详情
     *
     * @param immuneId 免疫证主键id
     * @param call
     */
    public static void getDogImmuneApproveDetail(int immuneId, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("immuneId", String.valueOf(immuneId));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getDogImmuneApproveDetail).build().execute(call);

    }

    /**
     * 犬只领养-留检犬只信息分页列表
     *
     * @param pageNum
     * @param pageSize
     * @param call
     */
    public static void getLeaveDogPageList(int pageNum, int pageSize, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getLeaveDogPageList).build().execute(call);

    }

    /**
     * 犬只领养-犬只详情
     *
     * @param leaveId
     * @param call
     */
    public static void getLeaveDogDetail(int leaveId, Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());

        Map<String, String> map = new HashMap<>();
        map.put("leaveId", String.valueOf(leaveId));
        OkHttpUtils.post().headers(headers).params(map).url(APIUrls.getLeaveDogDetail).build().execute(call);

    }


    // ==================================== 练琴帮 =============================================


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


    /*====================================    华为云     ==========================================*/

    /**
     * 获取鉴权Token
     *
     * @param call
     */
    public static void getObsFormInfo(Callback call) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", BaseApplication.getInstance().getUserInfo().getAuthorization());
        OkHttpUtils.get().headers(headers).url(APIUrls.getObsFormInfo).build().execute(call);

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
        OkHttpUtils.pet().addFile("file", filename, new File(filePath)).params(map).url(APIUrls.createPetArchives).build().execute(call);

    }

    /**
     * 宠物品种识别
     * <p>
     * file	file	图片文件需要使用post multipart/form-data的方式上传；支持PNG、JPG、JPEG、BMP格式；图片大小最大限制2M；
     * imageUrl	string	图片的URL地址；支持PNG、JPG、JPEG、BMP格式；优先使用该参数
     * imageBase64	string	图片base64编码；支持PNG、JPG、JPEG、BMP格式；
     * 如果同时传入file、imageUrl、imageBase64，本API使用顺序为imageUrl优先，imageBase64最低
     * petType 宠物种类：0为狗，1为猫
     *
     * @param token
     * @param filePath
     * @param call
     */
    public static void petType(String token, String filePath, Callback call) {
        Map<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("petType", String.valueOf(0));
        String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
        OkHttpUtils.pet().addFile("file", filename, new File(filePath)).params(map).url(APIUrls.petType).build().execute(call);

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
