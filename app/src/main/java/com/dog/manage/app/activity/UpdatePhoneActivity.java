package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityUpdatePhoneBinding;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;

public class UpdatePhoneActivity extends BaseActivity {

    private ActivityUpdatePhoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_phone);
        addActivity(this);
    }

    public void onClickSendCode(View view) {
        String phone = binding.phoneEditText.getText().toString().trim();
        if (CommonUtil.isBlank(phone)) {
            ToastUtils.showShort(getApplicationContext(), "请输入手机号");
            return;
        }
        binding.sendCodeView.setEnabled(false);
        SendRequest.sendMessageUser(phone, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(UpdatePhoneActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(UpdatePhoneActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                binding.sendCodeView.setEnabled(true);
            }

            @Override
            public void onResponse(BaseData response, int id) {
                if (response.isSuccess()) {
                    Observable.intervalRange(1, 60, 1, 1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(final Long time) {
                            binding.sendCodeView.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.sendCodeView.setText("重新发送 (" + (60 - time) + ")");
                                }
                            });
                            if (time == 60) {
                                binding.sendCodeView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.sendCodeView.setEnabled(true);
                                        binding.sendCodeView.setText("获取验证码");
                                    }
                                });
                            }
                        }
                    });
                } else {
                    binding.sendCodeView.setEnabled(true);
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }

            }
        });
    }

    public void onClickConfirm(View view) {
        String phone = binding.phoneEditText.getText().toString().trim();
        String code = binding.codeEditText.getText().toString().trim();
        if (CommonUtil.isBlank(phone)) {
            ToastUtils.showShort(getApplicationContext(), "请输入手机号");
            return;
        }
        if (CommonUtil.isBlank(code)) {
            ToastUtils.showShort(getApplicationContext(), "请输入验证码");
            return;
        }
        SendRequest.editUserPhone(phone, code, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(UpdatePhoneActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(UpdatePhoneActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(BaseData response, int id) {
                if (response.isSuccess()) {
                    finishActivity(AccountActivity.class);
                    finish();
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }

            }
        });
    }
}