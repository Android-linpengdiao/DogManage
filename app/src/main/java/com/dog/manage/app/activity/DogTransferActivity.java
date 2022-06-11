package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogTransferBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.LicenceBean;
import com.dog.manage.app.model.PunishRecord;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 犬只过户
 */
public class DogTransferActivity extends BaseActivity {

    private ActivityDogTransferBinding binding;
    private List<Dog> dogList = new ArrayList<>();
    private Dog dogDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_transfer);
        addActivity(this);

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogTransferActivity.this, dogList,
                        dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()), new OnClickListener() {
                            @Override
                            public void onClick(View view, Object object) {
                                dogDetail = (Dog) object;
                                binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());

                            }

                            @Override
                            public void onLongClick(View view, Object object) {

                            }
                        });
            }
        });

        getMyLicenceList();
    }

    /**
     * 我的-我的犬证
     */
    private void getMyLicenceList() {
        SendRequest.getMyLicenceList(new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogTransferActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogTransferActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<List<Dog>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    dogList = response.getData();
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }

    public void onClickConfirm(View view) {
        if (dogDetail == null) {
            ToastUtils.showShort(getApplicationContext(), "请选择犬只");
            return;
        }

        String dogOwnerName = binding.dogOwnerName.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogOwnerName)) {
            ToastUtils.showShort(getApplicationContext(), "请输入新犬主的姓名");
            return;
        }
        String dogOwnerPhone = binding.dogOwnerPhoneView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogOwnerPhone)) {
            ToastUtils.showShort(getApplicationContext(), "请输入新犬主的手机号码");
            return;
        }

        SendRequest.saveTransferDog(dogDetail.getLincenceId(), dogOwnerName, dogOwnerPhone,
                new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(BaseData response, int id) {
                        if (response.isSuccess()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", SubmitSuccessActivity.type_transfer);
                            openActivity(SubmitSuccessActivity.class, bundle);

                            finishActivity(DogManageWorkflowActivity.class);
                            finish();

                        } else {
                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");

                        }
                    }
                });
    }
}