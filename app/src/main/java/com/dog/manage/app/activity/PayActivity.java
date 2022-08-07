package com.dog.manage.app.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.AdoptionDetailsActivity;
import com.dog.manage.app.databinding.ActivityPayBinding;
import com.dog.manage.app.model.WXOrderPayParam;
import com.dog.manage.app.utils.PayManager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class PayActivity extends BaseActivity {

    private ActivityPayBinding binding;
    private int orderId;
    private int licenceId;
    private int payType = 0;
    private String price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_pay);
        addActivity(this);

        orderId = getIntent().getIntExtra("orderId", 0);
        licenceId = getIntent().getIntExtra("licenceId", 0);
        price = getIntent().getStringExtra("price");
        binding.moneyView.binding.itemContent.setText("￥" + price);

        binding.wxPayView.setSelected(true);

    }

    public void onClickWxPay(View view) {
        binding.wxPayView.setSelected(true);
        binding.aliPayView.setSelected(false);
    }

    public void onClickAliPay(View view) {
        binding.wxPayView.setSelected(false);
        binding.aliPayView.setSelected(true);
    }

    public void onClickConfirm(View view) {
        if (binding.wxPayView.isSelected()) {
            SendRequest.wxPayment(orderId, new GenericsCallback<ResultClient<WXOrderPayParam>>(new JsonGenericsSerializator()) {

                @Override
                public void onError(Call call, Exception e, int id) {
                }

                @Override
                public void onResponse(ResultClient<WXOrderPayParam> response, int id) {
                    if (response.isSuccess() && response.getData() != null && response.getData().getOrderInfo() != null) {
                        String appId = response.getData().getOrderInfo().getAppid();
                        String partnerId = response.getData().getOrderInfo().getPartnerid();
                        String prepayId = response.getData().getOrderInfo().getPrepayid();
                        String nonceStr = response.getData().getOrderInfo().getNoncestr();
                        String timeStamp = response.getData().getOrderInfo().getTimestamp() + "";
                        String sign = response.getData().getOrderInfo().getSign();
                        Log.i(TAG, "onResponse: " + appId);
                        PayManager.WeChatPay(PayActivity.this, appId, partnerId, prepayId, nonceStr, timeStamp, sign);

                    } else {
                        ToastUtils.showShort(PayActivity.this, !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "支付失败");

                    }

                }
            });

        } else if (binding.aliPayView.isSelected()) {
            SendRequest.aliPayment(orderId, new GenericsCallback<String>(new JsonGenericsSerializator()) {

                @Override
                public void onError(Call call, Exception e, int id) {
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (!object.isNull("code")) {
                            int code = object.optInt("code");
                            if (code == 200) {
                                if (!object.isNull("data")) {
                                    JSONObject data = object.optJSONObject("data");
                                    PayManager.aliPay(PayActivity.this, data.optString("orderInfo"), new PayManager.PayListener() {
                                        @Override
                                        public void onSuccess() {
                                            paySuccess();
                                        }

                                        @Override
                                        public void onFail() {
                                            payFail("支付失败");

                                        }

                                        @Override
                                        public void onCancel() {
                                            payFail("取消支付");
                                        }
                                    });
                                }
                            } else {
                                ToastUtils.showShort(PayActivity.this, object.optString("msg"));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private void paySuccess() {
        ToastUtils.showShort(PayActivity.this, "支付成功");
        finishActivity(CertificateDetailsActivity.class);
        finishActivity(AdoptionDetailsActivity.class);
        finish();
    }

    private void payFail(String content) {
        ToastUtils.showShort(PayActivity.this, content);
    }

}