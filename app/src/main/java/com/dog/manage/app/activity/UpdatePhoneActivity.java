package com.dog.manage.app.activity;


import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityUpdatePhoneBinding;

public class UpdatePhoneActivity extends BaseActivity {

    private ActivityUpdatePhoneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_phone);
        addActivity(this);
    }
}