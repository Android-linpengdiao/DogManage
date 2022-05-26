package com.dog.manage.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.RecordActivity;
import com.dog.manage.app.databinding.ActivityDogCertificateExaminedBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.PoliciesBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 犬证年审
 */
public class DogCertificateExaminedActivity extends BaseActivity {

    private ActivityDogCertificateExaminedBinding binding;
    private List<Dog> dogList = Arrays.asList(
            new Dog(1, "萨摩耶", "000000000"),
            new Dog(1, "柯基", "000000000"),
            new Dog(1, "泰迪", "000000000"),
            new Dog(1, "哈士奇", "000000000"),
            new Dog(0, "纸质凭证", null));
    private final int request_DogCertificate = 100;
    private final int request_ImmuneCertificate = 200;

    private String dogCertificate = null;
    private String immuneCertificate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_examined);
        addActivity(this);

        binding.titleView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", RecordActivity.type_certificate);
                openActivity(RecordActivity.class, bundle);
            }
        });

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogCertificateExaminedActivity.this,
                        dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()),
                        new OnClickListener() {
                            @Override
                            public void onClick(View view, Object object) {
                                Dog dataBean = (Dog) object;
                                binding.dogCertificateView.binding.itemContent.setText(dataBean.getDogType());
                                if (dataBean.getDogId() == 0) {
                                    binding.dianZhiContainer.setVisibility(View.GONE);
                                    binding.zhiZhiContainer.setVisibility(View.VISIBLE);
                                } else {
                                    binding.dianZhiContainer.setVisibility(View.VISIBLE);
                                    binding.zhiZhiContainer.setVisibility(View.GONE);
                                    getDogLicenseDetail(dataBean.getLincenceId());
                                }
                            }

                            @Override
                            public void onLongClick(View view, Object object) {

                            }
                        });
            }
        });

        binding.spreadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.dogOwnerContainer.setVisibility(binding.dogOwnerContainer.isShown() ? View.GONE : View.VISIBLE);
            }
        });


        binding.paperDogCertificateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions(PermissionUtils.STORAGE, request_DogCertificate)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
                    bundle.putInt("maxNumber", 1);
                    openActivity(MediaSelectActivity.class, bundle, request_DogCertificate);
                }
            }
        });
        binding.paperImmuneCertificateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissions(PermissionUtils.STORAGE, request_ImmuneCertificate)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
                    bundle.putInt("maxNumber", 1);
                    openActivity(MediaSelectActivity.class, bundle, request_ImmuneCertificate);
                }
            }
        });

        getDogImmuneList();

    }

    /**
     * 获取犬证列表
     */
    private void getDogImmuneList() {
        SendRequest.getDogLicenceList(new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(ResultClient<List<Dog>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    if (response.getData().size() > 0) {
                        dogList = response.getData();
                        getDogLicenseDetail(dogList.get(0).getLincenceId());
                    }

                }
            }
        });
    }

    /**
     * 犬证年审-选择我的犬只详情
     */
    private void getDogLicenseDetail(int lincenceId) {
        SendRequest.getDogLicenseDetail(lincenceId, new GenericsCallback<ResultClient<PoliciesBean>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<PoliciesBean> response, int id) {
                if (response.isSuccess() && response.getData() != null) {

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }


    public void onClickConfirm(View view) {
        if (binding.dogCertificateView.binding.itemContent.getText().toString().trim().equals("纸质凭证")) {
            if (CommonUtil.isBlank(dogCertificate)) {
                ToastUtils.showShort(getApplicationContext(), "请上传纸质犬证");
                return;
            }
            if (CommonUtil.isBlank(immuneCertificate)) {
                ToastUtils.showShort(getApplicationContext(), "请上传纸质免疫证明");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_examined);
            openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
        } else {
            Map<String, String> paramsMap = new HashMap<>();
            Bundle bundle = new Bundle();
            bundle.putString("paramsJson", GsonUtils.toJson(paramsMap));
            openActivity(DogCertificateExaminedSubmitActivity.class, bundle);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_DogCertificate:
                    compressImage(data, request_DogCertificate);

                    break;
                case request_ImmuneCertificate:
                    compressImage(data, request_ImmuneCertificate);

                    break;
            }
        }
    }

    private void compressImage(Intent data, int requestCode) {
        try {
            if (data != null) {
                String imageJson = data.getStringExtra("imageJson");
                if (!TextUtils.isEmpty(imageJson)) {
                    Gson gson = new Gson();
                    List<MediaFile> imageList = gson.fromJson(imageJson, new TypeToken<List<MediaFile>>() {
                    }.getType());
                    if (imageList != null && imageList.size() > 0) {
                        String path = imageList.get(0).getPath();
                        Luban.with(this)
                                .load(path)// 传人要压缩的图片列表
                                .ignoreBy(500)// 忽略不压缩图片的大小
                                .setTargetDir(FileUtils.getMediaPath())// 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        if (requestCode == request_DogCertificate) {
                                            dogCertificate = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateExaminedActivity.this, file.getAbsolutePath(), binding.paperDogCertificateView, 6);

                                        } else if (requestCode == request_ImmuneCertificate) {
                                            immuneCertificate = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateExaminedActivity.this, file.getAbsolutePath(), binding.paperImmuneCertificateView, 6);

                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                    }
                                }).launch();//启动压缩
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
}