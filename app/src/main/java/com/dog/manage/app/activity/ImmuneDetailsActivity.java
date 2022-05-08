package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityImmuneDetailsBinding;
import com.dog.manage.app.model.Dog;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class ImmuneDetailsActivity extends BaseActivity {

    private ActivityImmuneDetailsBinding binding;
    private int type;//1-已办结 2-即将过期 3-已过期
    private int immuneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_immune_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        immuneId = getIntent().getIntExtra("immuneId", 0);

        binding.auditStatusView.setText(type == 1 ? "已办结" : type == 2 ? "即将过期" : type == 3 ? "已过期" : "已预约");
        binding.hintView.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
        binding.confirmView.setText(type == 1 ? "查看免疫证":"立即办理");

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(DogDetailsActivity.class);
            }
        });

        getDogImmuneApproveDetail();

    }

    /**
     * 获取个人犬只免疫列表
     */
    private void getDogImmuneApproveDetail() {
        SendRequest.getDogImmuneApproveDetail(immuneId, new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(ImmuneDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(ImmuneDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<List<Dog>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }
}