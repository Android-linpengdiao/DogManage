package com.dog.manage.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogOwnerBinding;
import com.dog.manage.app.databinding.ActivityDogImmuneEditDogOwnerBinding;

/**
 * 免疫证办理
 */
public class DogImmuneEditDogOwnerActivity extends BaseActivity {

    private ActivityDogImmuneEditDogOwnerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_immune_edit_dog_owner);
        addActivity(this);


    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        openActivity(DogCertificateEditDogActivity.class, bundle);
    }
}