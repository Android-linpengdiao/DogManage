package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateExaminedSubmitBinding;

public class DogCertificateExaminedSubmitActivity extends BaseActivity {

    private ActivityDogCertificateExaminedSubmitBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_examined_submit);
        addActivity(this);

        setTypeface(binding.acceptUnitsHintView);
    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_examined);
        openActivity(SubmitSuccessActivity.class, bundle);
    }
}