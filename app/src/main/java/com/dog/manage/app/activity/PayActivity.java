package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.Callback;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityPayBinding;
import com.dog.manage.app.model.VipOrderBean;
import com.dog.manage.app.utils.PayManager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Request;

public class PayActivity extends BaseActivity {

    private ActivityPayBinding binding;
    private int licenceId;
    private int payType = 0;
    private int price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_pay);
        addActivity(this);

        licenceId = getIntent().getIntExtra("licenceId", 0);
        price = getIntent().getIntExtra("price", 0);
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
        SendRequest.payment(licenceId, payType, new GenericsCallback<ResultClient<Boolean>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(PayActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(PayActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<Boolean> response, int id) {
                if (response.isSuccess()) {
                    finishActivity(CertificateDetailsActivity.class);
                    finish();

                } else {
                    ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "支付失败");

                }
            }
        });
    }

    private int month = 1;

    private void createOrder(Integer id) {
        SendRequest.viporder_createOrder(getUserInfo().getAuthorization(), month, id,
                new GenericsCallback<ResultClient<VipOrderBean>>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<VipOrderBean> response, int id) {
                        if (response.isSuccess()) {
                            orderCreateOrderAboutVip(response.getData().getId());
                        } else {
                            ToastUtils.showShort(PayActivity.this, response.getMessage());
                        }

                    }
                });
    }

    private void orderCreateOrderAboutVip(int vipoid) {
        if (true) {
            SendRequest.orderCreateAliOrderAboutVip(getUserInfo().getAuthorization(), vipoid,
                    new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(BaseData response, int id) {
                            if (response.isSuccess()) {
                                long oid = ((long) Double.parseDouble(response.getData().toString()));


//                                orderTestSuccess(oid);


                                SendRequest.order_aliOrderPayParam(getUserInfo().getAuthorization(), Long.toString(oid),
                                        new GenericsCallback<String>(new JsonGenericsSerializator()) {

                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                try {
                                                    JSONObject object = new JSONObject(response);
                                                    if (!object.isNull("success")) {
                                                        boolean success = object.getBoolean("success");
                                                        if (success) {
                                                            if (!object.isNull("msg")) {
                                                                String msg = object.getString("msg");
                                                                PayManager.aliPay(PayActivity.this, msg, new PayManager.PayListener() {
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
                                                        }
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        });

                            } else {
                                ToastUtils.showShort(PayActivity.this, response.getMessage());

                            }
                        }
                    });
        } else {
            SendRequest.orderCreateWxOrderAboutVip(getUserInfo().getAuthorization(), vipoid,
                    new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(BaseData response, int id) {
                            if (response.isSuccess()) {
                                long oid = ((long) Double.parseDouble(response.getData().toString()));
                                SendRequest.order_wxOrderPayParam(getUserInfo().getAuthorization(), Long.toString(oid),
                                        new GenericsCallback<String>(new JsonGenericsSerializator()) {

                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                            }

                                            @Override
                                            public void onResponse(String response, int id) {
                                                try {
                                                    JSONObject object = new JSONObject(response);
                                                    if (!object.isNull("success")) {
                                                        boolean success = object.getBoolean("success");
                                                        if (success) {
                                                            if (!object.isNull("data")) {
                                                                JSONObject dataJson = object.getJSONObject("data");
                                                                String appId = dataJson.getString("appid");
                                                                String partnerId = dataJson.getString("partnerid");
                                                                String prepayId = dataJson.getString("prepayid");
                                                                String nonceStr = dataJson.getString("noncestr");
                                                                String timeStamp = dataJson.getString("timestamp");
                                                                String sign = dataJson.getString("sign");
                                                                PayManager.WeChatPay(PayActivity.this, appId, partnerId, prepayId, nonceStr, timeStamp, sign);
                                                            }
                                                        }
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });

                            } else {
                                ToastUtils.showShort(PayActivity.this, response.getMessage());

                            }
                        }
                    });
        }

    }

    private void paySuccess() {
        ToastUtils.showShort(PayActivity.this, "支付成功");
        updateUserInfo(new Callback() {
            @Override
            public void onError() {

            }

            @Override
            public void onResponse(boolean success, int id) {

            }
        });
    }

    private void payFail(String content) {
        ToastUtils.showShort(PayActivity.this, content);
    }

}