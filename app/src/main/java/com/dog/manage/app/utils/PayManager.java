package com.dog.manage.app.utils;

import android.app.Activity;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * 支付管理类
 */

public class PayManager {


    /**
     * 支付宝支付
     *
     * @param activity  上下文
     * @param orderInfo 支付信息
     * @param listener  监听
     */
    public static void aliPay(final Activity activity, final String orderInfo, final PayListener listener) {

        if (activity == null) {
            if (listener != null) {
                listener.onFail();
            }
            return;
        }


        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                final Map<String, String> result = alipay.payV2(orderInfo, true);


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String result_ = result.get("result");

                        try {

                            String code_ = result.get("resultStatus");
                            if ("9000".equals(code_)) {

                                JSONObject jsonObject = new JSONObject(result_);

                                JSONObject object = jsonObject.getJSONObject("alipay_trade_app_pay_response");

                                if (!object.isNull("code")) {

                                    String code = object.getString("code");
                                    if ("10000".equals(code)) {

                                        if (listener != null) {
                                            listener.onSuccess();
                                        }

                                    }

                                }

                            } else if ("6001".equals(code_)) {
                                if (listener != null) {
                                    listener.onCancel();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                            if (listener != null) {
                                listener.onFail();
                            }

                        }
                    }
                });
            }
        };


        Thread payThread = new Thread(payRunnable);
        payThread.start();


    }


    /**
     * 微信支付
     *
     * @param activity 上下文
     */
    public static void WeChatPay(Activity activity, String... args) {

        if (null == args) {
            //判断是否为空。丢一个toast，给个提示。比如服务器异常，错误啥的
//            activity.sendBroadcast(new Intent(Constant.we_chat_pay_result).putExtra("code", -1));
            return;
        }


        IWXAPI api = WXAPIFactory.createWXAPI(activity, args[0]);

        if (!isWXAppInstalledAndSupported(api)) {
            //ToastUtil.toastShortMessage("请先安装微信");

            return;
        }


        //data  根据服务器返回的json数据创建的实体类对象
        PayReq req = new PayReq();

        req.appId = args[0];

        req.partnerId = args[1];

        req.prepayId = args[2];

        req.packageValue = "Sign=WXPay";

        req.nonceStr = args[3];

        req.timeStamp = args[4];

        req.sign = args[5];

        api.registerApp(args[0]);

        //发起请求
        api.sendReq(req);

    }


    private static boolean isWXAppInstalledAndSupported(IWXAPI msgApi) {

        return msgApi.isWXAppInstalled();
    }

    public interface PayListener {

        void onSuccess();

        void onFail();

        void onCancel();

    }

}
