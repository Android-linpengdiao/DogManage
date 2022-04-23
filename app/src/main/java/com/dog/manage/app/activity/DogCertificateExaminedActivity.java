package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.base.manager.DialogManager;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateExaminedBinding;

import java.util.Arrays;
import java.util.List;

/**
 * 犬证年审
 */
public class DogCertificateExaminedActivity extends BaseActivity {

    private ActivityDogCertificateExaminedBinding binding;
    private List<String> dogList = Arrays.asList("萨摩耶", "柯基", "泰迪", "哈士奇");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_examined);
        addActivity(this);

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogCertificateExaminedActivity.this, dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()), new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        String content = (String) object;
                        binding.dogCertificateView.binding.itemContent.setText(content);
                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
            }
        });
    }


    public void onClickConfirm(View view) {
        openActivity(DogCertificateExaminedSubmitActivity.class);
    }
}