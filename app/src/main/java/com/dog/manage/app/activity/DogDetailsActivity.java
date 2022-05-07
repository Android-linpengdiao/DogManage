package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.base.utils.GlideLoader;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogDetailsBinding;
import com.dog.manage.app.model.DogUser;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;

public class DogDetailsActivity extends BaseActivity {

    private ActivityDogDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_details);
        addActivity(this);

        GlideLoader.LoderImage(this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView);

        getDogById();
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_adoption);
        openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

    }

    /**
     * 犬证 获取犬只详情信息
     */
    private void getDogById() {
        SendRequest.getDogById(0, new GenericsCallback<ResultClient<DogUser>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogUser> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    DogUser dogUser = response.getData();

                }
            }
        });
    }
}