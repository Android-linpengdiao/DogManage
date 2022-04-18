package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogBinding;

public class DogCertificateEditDogActivity extends BaseActivity {

    private ActivityDogCertificateEditDogBinding binding;

    public static final int type_userInfo = 0;//我的信息
    public static final int type_certificate = 1;//犬证办理
    public static final int type_immune = 2;//免疫证办理
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_dog);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);

        if (type == type_userInfo) {
            binding.titleView.binding.itemTitle.setText("我的信息");

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");
            binding.thirdStepView.setText("选择医院");

        }

    }

    public void onClickConfirm(View view) {
        if (type == type_userInfo) {


        } else if (type == type_certificate) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            openActivity(DogCertificateEditSubmitActivity.class, bundle);

        } else if (type == type_immune) {
            openActivity(DogImmuneHospitalActivity.class);

        }
    }
}