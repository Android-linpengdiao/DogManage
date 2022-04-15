package com.dog.manage.app.activity;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityAdoptionDetailsBinding;

/**
 * 领养记录详情
 */
public class AdoptionDetailsActivity extends BaseActivity {

    private ActivityAdoptionDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_adoption_details);
        addActivity(this);
    }
}