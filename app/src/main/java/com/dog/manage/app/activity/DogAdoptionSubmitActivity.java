package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.base.utils.GlideLoader;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogAdoptionSubmitBinding;

public class DogAdoptionSubmitActivity extends BaseActivity {

    private ActivityDogAdoptionSubmitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_adoption_submit);
        addActivity(this);

        GlideLoader.LoderImage(this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView);

    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_adoption);
        openActivity(SubmitSuccessActivity.class, bundle);
    }
}