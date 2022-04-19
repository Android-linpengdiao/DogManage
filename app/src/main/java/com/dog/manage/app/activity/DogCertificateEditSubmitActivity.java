package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditSubmitBinding;

public class DogCertificateEditSubmitActivity extends BaseActivity {

    private ActivityDogCertificateEditSubmitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_submit);
        addActivity(this);
        binding.thirdStepView.setSelected(true);

    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        openActivity(UpdateDogOwnerInfoActivity.class, bundle);
    }
}