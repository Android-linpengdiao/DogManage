package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.base.manager.DialogManager;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateExaminedBinding;

import java.util.Arrays;
import java.util.List;

/**
 * 犬证年审
 */
public class DogCertificateExaminedActivity extends BaseActivity {

    private ActivityDogCertificateExaminedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_examined);
        addActivity(this);
        binding.dogCertificateTextView.setText(list.get(0));
    }

    private List<String> list = Arrays.asList(
            "电子犬证-萨摩耶", "电子犬证-柯基",
            "电子犬证-泰迪", "电子犬证-哈士奇");

    public void onClickDogCertificate(View view) {
        DialogManager.getInstance().showRankDialog(DogCertificateExaminedActivity.this,
                list, list.indexOf(binding.dogCertificateTextView.getText().toString()),
                new DialogManager.OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        String content = (String) object;
                        binding.dogCertificateTextView.setText(content);
                    }
                });
    }

    public void onClickConfirm(View view) {
        openActivity(DogCertificateExaminedSubmitActivity.class);
    }
}