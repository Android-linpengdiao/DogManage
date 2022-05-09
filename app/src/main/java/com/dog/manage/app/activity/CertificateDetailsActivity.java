package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityCertificateDetailsBinding;
import com.dog.manage.app.model.Dog;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class CertificateDetailsActivity extends BaseActivity {

    private ActivityCertificateDetailsBinding binding;
    private int type;//1-审核通过 2-审核拒绝 3-已办结
    private int lincenceId;
    private Dog dog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_certificate_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        lincenceId = getIntent().getIntExtra("lincenceId", 0);

        binding.auditStatusView.setText(type == 1 ? "审核通过" : type == 2 ? "审核拒绝" : type == 3 ? "已办结" : "审核中");
        binding.payTypeView.setVisibility(type == 3 ? View.VISIBLE : View.GONE);
        binding.auditReasonView.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        binding.confirmView.setText(type == 1 ? "在线支付" : type == 2 ? "犬证年审" : type == 3 ? "查看犬证" : "在线支付");
        binding.confirmView.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
        binding.confirmView.setBackgroundResource(type == 2 ? R.drawable.button_red : R.drawable.button_theme);

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dog == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", dog.getDogId());
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dog == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", dog.getDogId());
                openActivity(DogDetailsActivity.class, bundle);
            }
        });

        getDogLicenceDetail();

    }

    public void onClickConfirm(View view) {
        openActivity(PayActivity.class);
    }

    /**
     * 获取个人犬只免疫列表
     */
    private void getDogLicenceDetail() {
        SendRequest.getDogLicenceDetail(lincenceId, new GenericsCallback<ResultClient<Dog>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(CertificateDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(CertificateDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<Dog> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }

    private void initView(Dog data) {
        dog = data;
        binding.contentView.setText(data.getDogType() + "-" + data.getDogAge() + "岁3个月");
        binding.createTimeView.setText(data.getCreatedTime());
        binding.dogOwnerInfoView.binding.itemContent.setText(data.getUserName());
        binding.dogDetailsView.binding.itemContent.setText(data.getDogType());
        binding.acceptUnitView.setText(data.getAcceptUnit());
        binding.priceView.binding.itemContent.setText("￥" + data.getPrice());
    }
}