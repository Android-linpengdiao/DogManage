package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogBinding;

public class DogCertificateEditDogActivity extends BaseActivity {

    private ActivityDogCertificateEditDogBinding binding;
    private int type = 0;//0-犬主信息 ；1-犬只信息 ；2-提交信息 ；

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_dog);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);

    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        openActivity(DogCertificateEditSubmitActivity.class, bundle);
    }
}