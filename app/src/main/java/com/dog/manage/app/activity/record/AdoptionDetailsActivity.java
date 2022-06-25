package com.dog.manage.app.activity.record;

import android.os.Bundle;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.GlideLoader;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.activity.DogCertificateEditDogOwnerActivity;
import com.dog.manage.app.activity.DogDetailsActivity;
import com.dog.manage.app.activity.DogInfoActivity;
import com.dog.manage.app.activity.PayActivity;
import com.dog.manage.app.databinding.ActivityAdoptionDetailsBinding;
import com.dog.manage.app.model.RecordAdoption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 领养记录详情
 */
public class AdoptionDetailsActivity extends BaseActivity {

    private ActivityAdoptionDetailsBinding binding;
    private RecordAdoption recordAdoption;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_adoption_details);
        addActivity(this);

        id = getIntent().getIntExtra("id", 0);
        dogAdoptDetails();

    }

    /**
     * 获取个人犬只免疫列表
     */
    private void dogAdoptDetails() {
        SendRequest.dogAdoptDetails(id, new GenericsCallback<ResultClient<RecordAdoption>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(AdoptionDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(AdoptionDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<RecordAdoption> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    private void initView(RecordAdoption dataBean) {
        binding.container.setVisibility(View.VISIBLE);
        binding.bottomView.setVisibility(View.VISIBLE);
        recordAdoption = dataBean;
        binding.dogNameView.setText(dataBean.getDogName() + "|" + dataBean.getDogColor() + "|" + dataBean.getDogAge() + "岁3个月");
        binding.acceptUnitView.setText(dataBean.getAcceptUnit());
        binding.acceptUnitAddressView.setText(dataBean.getAcceptUnitAddress());
        try {
            List<String> dogPhotos = new Gson().fromJson(dataBean.getDogPhoto(), new TypeToken<List<String>>() {
            }.getType());
            if (dogPhotos != null && dogPhotos.size() > 0)
                GlideLoader.LoderImage(this, dogPhotos.get(0), binding.coverView,8);
        } catch (Exception e) {
            e.getMessage();
        }

        binding.dogOwnerInfoView.binding.itemContent.setText(dataBean.getUserName());
        binding.dogDetailsView.binding.itemContent.setText(dataBean.getDogName());
        binding.auditAcceptUnitView.setText(dataBean.getAcceptUnit());
        binding.priceView.binding.itemContent.setText("￥" + dataBean.getPrice());
        //办理状态 1 待审核 0 待支付 2 领养完成 3 拒绝 4 全部
        if (dataBean.getStatus() != null) {
            if (dataBean.getStatus() == 1) {
                binding.statusView.setText("待审核");

            } else if (dataBean.getStatus() == 0) {
                binding.statusView.setText("待支付");
                binding.confirmView.setText("在线支付");
                binding.priceView.setVisibility(View.VISIBLE);
                binding.hintView.setVisibility(View.VISIBLE);
                binding.confirmView.setVisibility(View.VISIBLE);
                binding.statusView.setText("在线支付");

            } else if (dataBean.getStatus() == 2) {
                binding.statusView.setText("完成领养");
                binding.priceView.setVisibility(View.VISIBLE);
                binding.payTypeView.setVisibility(View.VISIBLE);

            } else if (dataBean.getStatus() == 3) {
                binding.statusView.setText("审核拒绝");
                binding.auditReasonView.setVisibility(View.VISIBLE);
                binding.confirmView.setVisibility(View.VISIBLE);
                binding.auditReasonView.binding.itemDesc.setText("犬只信息有误，无法领养，请选择其他犬只");
                binding.confirmView.setText("重新提交");

            } else {
                binding.statusView.setText("待审核");
                binding.confirmView.setVisibility(View.GONE);

            }
        }

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordAdoption == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", recordAdoption.getLeaveDogId());
                bundle.putInt("useId", recordAdoption.getUseId());
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recordAdoption == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("leaveId", recordAdoption.getLeaveDogId());
                openActivity(DogDetailsActivity.class, bundle);
            }
        });
    }

    public void onClickConfirm(View view) {
        if (recordAdoption != null && recordAdoption.getStatus() != null) {
            if (recordAdoption.getStatus() == 0) {
                Bundle bundle = new Bundle();
                bundle.putInt("orderId", recordAdoption.getOrderId());
                bundle.putInt("leaveId", recordAdoption.getLeaveId());
                bundle.putString("price", recordAdoption.getPrice());
                openActivity(PayActivity.class, bundle);
            }
        }
    }
}