package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.manager.DialogManager;
import com.base.utils.CommonUtil;
import com.base.utils.GsonUtils;
import com.base.utils.LogUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogTransferBinding;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 犬只过户
 */
public class DogTransferActivity extends BaseActivity {

    private ActivityDogTransferBinding binding;
    private List<String> dogList = Arrays.asList("萨摩耶", "柯基", "泰迪", "哈士奇");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_transfer);
        addActivity(this);

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogTransferActivity.this, dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()), new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        String content = (String) object;
                        dogCertificate = dogList.indexOf(content);
                        binding.dogCertificateView.binding.itemContent.setText(content);
                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
            }
        });
    }

    private int dogCertificate = -1;
    private String dogOwnerName = null;
    private String dogOwnerPhone = null;

    public void onClickConfirm(View view) {
        Map<String, String> paramsMap = new HashMap<>();
        if (dogCertificate < 0) {
            ToastUtils.showShort(getApplicationContext(), "请选择犬只");
            return;
        }

        dogOwnerName = binding.dogOwnerName.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogOwnerName)) {
            ToastUtils.showShort(getApplicationContext(), "请输入新犬主的姓名");
            return;
        }
        dogOwnerPhone = binding.dogOwnerPhoneView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogOwnerPhone)) {
            ToastUtils.showShort(getApplicationContext(), "请输入新犬主的手机号码");
            return;
        }

        paramsMap.put("dogCertificate", String.valueOf(dogCertificate));
        paramsMap.put("dogOwnerName", dogOwnerName);
        paramsMap.put("dogOwnerPhone", dogOwnerPhone);

        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_transfer);
        openActivity(SubmitSuccessActivity.class, bundle);

        finishActivity(DogManageWorkflowActivity.class);
        finish();

        if (LogUtil.isDebug){
            return;
        }
        SendRequest.DogTransfer(getUserInfo().getAuthorization(), paramsMap, new GenericsCallback(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
    }
}