package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityTransferDetailsBinding;

public class TransferDetailsActivity extends BaseActivity {

    private ActivityTransferDetailsBinding binding;
    private int type;//2-过户失败

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_transfer_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        binding.auditStatusView.setText(type == 1 ? "过户成功" : type == 2 ? "过户失败" : "审核中");
        binding.auditReasonView.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(type == 2 ? View.VISIBLE : View.GONE);


    }
}