package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityCertificateDetailsBinding;

public class CertificateDetailsActivity extends BaseActivity {

    private ActivityCertificateDetailsBinding binding;
    private int type;//1-审核通过 2-审核拒绝 3-已办结

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_certificate_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        binding.auditStatusView.setText(type == 1 ? "审核通过" : type == 2 ? "审核拒绝" : type == 3 ? "已办结" : "审核中");
        binding.payTypeView.setVisibility(type == 3 ? View.VISIBLE : View.GONE);
        binding.auditReasonView.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        binding.confirmView.setText(type == 1 ? "在线支付" : type == 2 ? "犬证年审" : type == 3 ? "查看犬证" : "在线支付");
        binding.confirmView.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
        binding.confirmView.setBackgroundResource(type == 2 ? R.drawable.button_red : R.drawable.button_theme);

    }

    public void onClickConfirm(View view) {
        openActivity(PayActivity.class);
    }
}