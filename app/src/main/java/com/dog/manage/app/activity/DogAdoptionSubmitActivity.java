package com.dog.manage.app.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.base.BaseData;
import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.utils.LogUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogAdoptionSubmitBinding;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.model.DogUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class DogAdoptionSubmitActivity extends BaseActivity {

    private ActivityDogAdoptionSubmitBinding binding;
    private DogDetail dogDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_adoption_submit);
        addActivity(this);


        dogDetail = (DogDetail) getIntent().getSerializableExtra("dogDetail");

        if (dogDetail != null) {
            binding.dogNameView.setText((!CommonUtil.isBlank(dogDetail.getDogName()) ? dogDetail.getDogName() : "--")
                    + "|"
                    + (!CommonUtil.isBlank(dogDetail.getDogColor()) ? dogDetail.getDogColor() : "--")
                    + "|"
                    + CommonUtil.getDogAge(dogDetail.getDogAge()));
            binding.leaveCenterView.setText(dogDetail.getLeaveCenter());
            binding.centerAddressView.setText(dogDetail.getCenterAddress());
            try {
                List<String> dogPhotos = new Gson().fromJson(dogDetail.getDogPhoto(), new TypeToken<List<String>>() {
                }.getType());
                if (dogPhotos != null && dogPhotos.size() > 0)
                    GlideLoader.LoderImage(this, dogPhotos.get(0), binding.coverView);
            } catch (Exception e) {
                e.getMessage();
            }

        }
        binding.selectedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.selectedView.setSelected(!binding.selectedView.isSelected());
            }
        });


    }

    /**
     * addressId
     * integer
     * 地址id
     * leaveId
     * integer
     * 领养id
     *
     * @param view
     */
    public void onClickConfirm(View view) {
        if (!binding.selectedView.isSelected()) {
            ToastUtils.showShort(getApplicationContext(), "请同意领养承诺书");
            return;
        }
        if (dogDetail == null) {
            ToastUtils.showShort(getApplicationContext(), "提交申请失败");
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("addressId", String.valueOf(dogDetail.getAddressId()));
        paramsMap.put("leaveId", String.valueOf(dogDetail.getLeaveId()));
        SendRequest.saveLeaveDog(paramsMap, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(BaseData response, int id) {
                if (response.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", SubmitSuccessActivity.type_adoption);
                    openActivity(SubmitSuccessActivity.class, bundle);

                    finishActivity(DogManageWorkflowActivity.class);
                    finishActivity(DogAdoptionActivity.class);
                    finishActivity(DogDetailsThemeActivity.class);
                    finishActivity(DogCertificateEditDogOwnerActivity.class);
                    finish();

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });

    }
}