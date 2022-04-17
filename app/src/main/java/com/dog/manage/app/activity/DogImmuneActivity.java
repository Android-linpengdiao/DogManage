package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogImmuneBinding;

/**
 * 免疫证办理
 */
public class DogImmuneActivity extends BaseActivity {

    private ActivityDogImmuneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_immune);
        addActivity(this);
    }

    public void onClickConfirm(View view) {
        openActivity(DogImmuneEditDogOwnerActivity.class);
    }
}