package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityUpdateDogOwnerInfoBinding;

public class UpdateDogOwnerInfoActivity extends BaseActivity {

    private ActivityUpdateDogOwnerInfoBinding binding;
    public static final int type_details = 0;//犬主信息
    public static final int type_submit = 1;//提交

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_dog_owner_info);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);

        if (type == type_details) {


        } else if (type == type_submit) {

            binding.dogOwnerView.setBackgroundColor(getResources().getColor(R.color.transparent));
            binding.dogOwnerView.setTextColor(getResources().getColor(R.color.colorAppTheme));

            binding.submitView.setBackgroundResource(R.drawable.button_theme);
            binding.submitView.setTextColor(getResources().getColor(R.color.white));
            binding.confirmView.setText("提交审核");
            binding.submitContainer.setVisibility(View.VISIBLE);
            binding.dogOwnerContainer.setVisibility(View.GONE);
        }

    }

    public void onClickConfirm(View view) {
        if (type == 0) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            openActivity(UpdateDogOwnerInfoActivity.class, bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("type", SubmitSuccessActivity.type_update);
            openActivity(SubmitSuccessActivity.class, bundle);
        }
    }
}