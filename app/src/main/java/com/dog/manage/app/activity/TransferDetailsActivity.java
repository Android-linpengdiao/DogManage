package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityTransferDetailsBinding;
import com.dog.manage.app.model.DogLicenceDetail;
import com.dog.manage.app.model.RecordImmune;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;
import okhttp3.Request;

public class TransferDetailsActivity extends BaseActivity {

    private ActivityTransferDetailsBinding binding;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_transfer_details);
        addActivity(this);

        id = getIntent().getIntExtra("id", 0);
        getTransferDog();

    }

    /**
     * 获取个人犬只免疫列表
     */
    private void getTransferDog() {
        SendRequest.getTransferDog(id, new GenericsCallback<ResultClient<RecordImmune>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(TransferDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(TransferDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<RecordImmune> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    private void initView(RecordImmune dataBean) {
        //status   办理状态 0 是未审核的 1 成功的 2 是失败的
        int status = dataBean.getStatus();
        binding.auditStatusView.setText(status == 1 ? "过户成功" : status == 2 ? "过户失败" : "审核中");
        binding.auditReasonView.setVisibility(status == 2 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(status == 2 ? View.VISIBLE : View.GONE);

        binding.contentView.setText(dataBean.getDogType() + "-" + dataBean.getDogAge() + "岁3个月");
        binding.createTimeView.setText(dataBean.getApplyTime());
        binding.descView.setText("犬证:" + dataBean.getDogLicenceNum());
        binding.oldUserNameView.setText("原犬主:" + dataBean.getOldUserName());
        binding.newUserNameView.setText("新犬主:" + dataBean.getNewUserName());

        binding.userNameView.binding.itemContent.setText(dataBean.getNewUserName());
        binding.userPhoneView.binding.itemContent.setText(dataBean.getNewUserPhone());
    }
}