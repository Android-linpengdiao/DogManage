package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.AdoptionRecordActivity;
import com.dog.manage.app.activity.record.PunishRecordActivity;
import com.dog.manage.app.databinding.ActivityUserHomeBinding;

public class UserHomeActivity extends BaseActivity {

    private ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_user_home);
        addActivity(this);
    }

    public void onClickUserInfo(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_userInfo);
        openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

    }

    public void onClickMyDogImmune(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", MyDogCertificateOrImmuneActivity.type_immune);
        openActivity(MyDogCertificateOrImmuneActivity.class, bundle);

    }

    public void onClickMyDogCertificate(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", MyDogCertificateOrImmuneActivity.type_certificate);
        openActivity(MyDogCertificateOrImmuneActivity.class, bundle);

    }

    public void onClickCertificate(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", AdoptionRecordActivity.type_certificate);
        openActivity(AdoptionRecordActivity.class, bundle);

    }

    public void onClickImmune(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", AdoptionRecordActivity.type_immune);
        openActivity(AdoptionRecordActivity.class, bundle);

    }

    public void onClickTransfer(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", AdoptionRecordActivity.type_transfer);
        openActivity(AdoptionRecordActivity.class, bundle);

    }

    public void onClickAdoption(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", AdoptionRecordActivity.type_adoption);
        openActivity(AdoptionRecordActivity.class, bundle);

    }

    public void onClickLogout(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", AdoptionRecordActivity.type_logout);
        openActivity(AdoptionRecordActivity.class, bundle);

    }

    public void onClickPunish(View view) {
        openActivity(PunishRecordActivity.class);

    }

    public void onClickSetting(View view) {
        openActivity(SettingsActivity.class);
    }
}