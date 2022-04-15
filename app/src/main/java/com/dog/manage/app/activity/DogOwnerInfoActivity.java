package com.dog.manage.app.activity;


import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogOwnerInfoBinding;

/**
 * 犬只信息
 */
public class DogOwnerInfoActivity extends BaseActivity {

    private ActivityDogOwnerInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_owner_info);
        addActivity(this);
    }
}