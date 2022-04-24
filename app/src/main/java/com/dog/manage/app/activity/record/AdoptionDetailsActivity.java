package com.dog.manage.app.activity.record;

import android.os.Bundle;
import android.view.View;

import com.base.utils.GlideLoader;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.activity.DogDetailsActivity;
import com.dog.manage.app.activity.PayActivity;
import com.dog.manage.app.databinding.ActivityAdoptionDetailsBinding;

/**
 * 领养记录详情
 */
public class AdoptionDetailsActivity extends BaseActivity {

    private ActivityAdoptionDetailsBinding binding;
    private int type;//1-待支付 2-完成领养 3-审核拒绝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_adoption_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        binding.auditStatusView.setText(type == 1 ? "待支付" : type == 2 ? "完成领养" : type == 3 ? "审核拒绝" : "审核中");
        binding.moneyView.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
        binding.payTypeView.setVisibility(type == 2 ? View.VISIBLE : View.GONE);
        binding.auditReasonView.setVisibility(type == 3 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(type == 1 || type == 3 ? View.VISIBLE : View.GONE);
        binding.confirmView.setText(type == 1 ? "在线支付" : type == 3 ? "重新提交" : "在线支付");
        binding.hintView.setVisibility(type == 1 ? View.VISIBLE : View.GONE);

        GlideLoader.LoderImage(this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView, 8);

        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(DogDetailsActivity.class);
            }
        });

    }

    public void onClickConfirm(View view) {
        if (type == 1) {
            openActivity(PayActivity.class);
        }
    }
}