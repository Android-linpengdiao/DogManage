package com.dog.manage.app.activity;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityCertificateDetailsBinding;

public class CertificateDetailsActivity extends BaseActivity {

    private ActivityCertificateDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_certificate_details);
        addActivity(this);

    }
}