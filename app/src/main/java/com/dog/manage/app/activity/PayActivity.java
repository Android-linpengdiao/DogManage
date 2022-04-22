package com.dog.manage.app.activity;


import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityPayBinding;

public class PayActivity extends BaseActivity {

    private ActivityPayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_pay);
        addActivity(this);
    }
}