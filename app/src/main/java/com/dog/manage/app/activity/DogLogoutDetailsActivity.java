package com.dog.manage.app.activity;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogLogoutDetailsBinding;

/**
 * 注销记录详情
 */
public class DogLogoutDetailsActivity extends BaseActivity {

    private ActivityDogLogoutDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_logout_details);
        addActivity(this);

    }
}