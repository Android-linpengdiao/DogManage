package com.dog.manage.app.activity;


import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.adapter.PoliciesAdapter;
import com.dog.manage.app.databinding.ActivityPoliciesDetailsBinding;

public class PoliciesDetailsActivity extends BaseActivity {

    private ActivityPoliciesDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_policies_details);
        addActivity(this);
    }
}