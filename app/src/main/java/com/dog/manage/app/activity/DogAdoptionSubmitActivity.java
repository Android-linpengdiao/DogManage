package com.dog.manage.app.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.base.utils.GlideLoader;
import com.base.utils.LogUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogAdoptionSubmitBinding;
import com.dog.manage.app.model.DogUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class DogAdoptionSubmitActivity extends BaseActivity {

    private ActivityDogAdoptionSubmitBinding binding;
    private Map<String, String> paramsMap = new HashMap<>();
    private int leaveId = 0;
    private int addressId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_adoption_submit);
        addActivity(this);


        leaveId = getIntent().getIntExtra("leaveId", 0);
        addressId = getIntent().getIntExtra("addressId", 0);

//        String paramsJson = getIntent().getStringExtra("paramsJson");
//        if (!TextUtils.isEmpty(paramsJson)) {
//            Gson gson = new Gson();
//            paramsMap = gson.fromJson(paramsJson, new TypeToken<Map<String, Object>>() {
//            }.getType());
//        }

//        binding.dogNameView.setText(dogDetail.getDogName() + "|" + dogDetail.getDogColor() + "|" + dogDetail.getDogAge() + "岁3个月");
//        binding.leaveCenterView.setText(dogDetail.getLeaveCenter());
        
        GlideLoader.LoderImage(this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView);

    }

    public void onClickConfirm(View view) {
        SendRequest.CertificateExamined(getUserInfo().getAuthorization(), paramsMap,
                new GenericsCallback<ResultClient<DogUser>>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<DogUser> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", SubmitSuccessActivity.type_adoption);
                            openActivity(SubmitSuccessActivity.class, bundle);

                            finishActivity(DogManageWorkflowActivity.class);
                            finishActivity(DogAdoptionActivity.class);
                            finishActivity(DogDetailsThemeActivity.class);
                            finishActivity(DogCertificateEditDogOwnerActivity.class);
                            finish();

                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });
    }
}