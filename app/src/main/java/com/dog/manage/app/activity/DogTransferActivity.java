package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.manager.DialogManager;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogTransferBinding;

import java.util.Arrays;
import java.util.List;

/**
 * 犬只过户
 */
public class DogTransferActivity extends BaseActivity {

    private ActivityDogTransferBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_transfer);
        addActivity(this);
    }

    private List<String> list = Arrays.asList(
            "萨摩耶", "柯基",
            "泰迪", "哈士奇");

    public void onClickDogCertificate(View view) {
        DialogManager.getInstance().showRankDialog(DogTransferActivity.this,
                list, list.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()),
                new DialogManager.OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        String content = (String) object;
                        binding.dogCertificateView.binding.itemContent.setText(content);
                    }
                });
    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_transfer);
        openActivity(SubmitSuccessActivity.class, bundle);
    }
}