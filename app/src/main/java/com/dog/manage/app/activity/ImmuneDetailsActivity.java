package com.dog.manage.app.activity;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityImmuneDetailsBinding;

public class ImmuneDetailsActivity extends BaseActivity {

    private ActivityImmuneDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_immune_details);
        addActivity(this);
    }
}