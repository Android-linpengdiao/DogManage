package com.dog.manage.app.activity;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityTransferDetailsBinding;

public class TransferDetailsActivity extends BaseActivity {

    private ActivityTransferDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_transfer_details);
        addActivity(this);
    }
}