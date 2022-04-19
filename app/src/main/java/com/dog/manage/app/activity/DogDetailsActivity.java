package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.base.utils.GlideLoader;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogDetailsBinding;

public class DogDetailsActivity extends BaseActivity {

    private ActivityDogDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_details);
        addActivity(this);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(layoutParams);

        GlideLoader.LoderRoundedImage(this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView, 16);

    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_adoption);
        openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

    }
}