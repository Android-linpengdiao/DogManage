package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityImmuneDetailsBinding;

public class ImmuneDetailsActivity extends BaseActivity {

    private ActivityImmuneDetailsBinding binding;
    private int type;//1-已办结 2-即将过期 3-已过期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_immune_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        binding.auditStatusView.setText(type == 1 ? "已办结" : type == 2 ? "即将过期" : type == 3 ? "已过期" : "已预约");
        binding.hintView.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
        binding.confirmView.setText(type == 1 ? "查看免疫证":"立即办理");

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(DogDetailsActivity.class);
            }
        });

    }
}