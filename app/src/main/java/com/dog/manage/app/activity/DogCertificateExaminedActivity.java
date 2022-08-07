package com.dog.manage.app.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.RecordActivity;
import com.dog.manage.app.databinding.ActivityDogCertificateExaminedBinding;
import com.base.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogLicenseDetail;
import com.dog.manage.app.utils.UploadFileManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 犬证年审
 */
public class DogCertificateExaminedActivity extends BaseActivity {

    private ActivityDogCertificateExaminedBinding binding;
    private List<Dog> dogList = new ArrayList<>();
    private final int request_DogCertificate = 100;
    private final int request_ImmuneCertificate = 200;

    private String dogCertificate = null;
    private String immuneCertificate = null;
    private Dog dogDetail;

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
                                Log.i(TAG, "onClick: " + object);
                                dogDetail = (Dog) object;
                                binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());
                                if (dogDetail.getLincenceId() == 0) {
                                    binding.dianZhiContainer.setVisibility(View.GONE);
                                    binding.zhiZhiContainer.setVisibility(View.VISIBLE);
                                } else {
                                    binding.dianZhiContainer.setVisibility(View.VISIBLE);
                                    binding.zhiZhiContainer.setVisibility(View.GONE);
                                    getDogLicenseDetail(dogDetail.getLincenceId());
                                }

                            }

                            @Override
                            public void onLongClick(View view, Object object) {

                            }
                        });
            }
        });

        binding.dogUserTopView.setOnClickListener(new View.OnClickListener() {
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

        getDogLicenceList();

    }

    /**
     * 获取犬证列表
     */
    private void getDogLicenceList() {
        SendRequest.getDogLicenceList(new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(ResultClient<List<Dog>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    if (response.getData().size() > 0) {
                        dogList = response.getData();
                    }
                    Dog newDog = new Dog();
                    newDog.setLincenceId(0);
                    newDog.setDogType("纸质犬证-新建年审");
                    dogList.add(newDog);

                    dogDetail = dogList.get(0);
                    binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());
                    if (dogDetail.getLincenceId() != 0) {
                        getDogLicenseDetail(dogDetail.getLincenceId());
                    } else {
                        initView(null);
                    }

                }
            }
        });
    }

    /**
     * 犬证年审-选择我的犬只详情
     */
    private void getDogLicenseDetail(int lincenceId) {
        SendRequest.getDogLicenseDetail(lincenceId,
                new GenericsCallback<ResultClient<DogLicenseDetail>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        initView(null);
                    }

                    @Override
                    public void onResponse(ResultClient<DogLicenseDetail> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            annualStatus(lincenceId);
                            initView(response.getData());

                        } else {
                            initView(null);
                            ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");

                        }
                    }
                });
    }

    private void annualStatus(int lincenceId) {
        SendRequest.annualStatus(lincenceId,
                new GenericsCallback<ResultClient<DogLicenseDetail>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(ResultClient<DogLicenseDetail> response, int id) {
                        if (response.isSuccess()) {
                            binding.confirmView.setEnabled(true);
                            binding.confirmView.setTextColor(Color.parseColor("#ffffff"));
                            binding.confirmView.setBackgroundResource(R.drawable.button_theme);
                        } else {
                            binding.confirmView.setEnabled(false);
                            binding.confirmView.setTextColor(Color.parseColor("#ffffff"));
                            binding.confirmView.setBackgroundResource(R.drawable.button_gray);
                        }
                    }
                });
    }

    private void initView(DogLicenseDetail data) {

        if (data != null) {
            binding.dianZhiContainer.setVisibility(View.VISIBLE);
            binding.zhiZhiContainer.setVisibility(View.GONE);
            //北京市养犬登记证
            if (data.getDetailResultRespVo() != null) {
                binding.idNumView.setText(data.getDetailResultRespVo().getIdNum());
                binding.dogTypeView.setText(data.getDetailResultRespVo().getDogType());
                binding.dogColorView.setText(data.getDetailResultRespVo().getDogColor());
                binding.dogGenderView.setText(data.getDetailResultRespVo().getDogGender() == 0 ? "雌性" : "雄性");
                binding.orgNameView.setText(data.getDetailResultRespVo().getOrgName());
                binding.detailedAddressView.setText(data.getDetailResultRespVo().getDetailedAddress());
                binding.awardTimeView.setText(data.getDetailResultRespVo().getAwardTime());
                GlideLoader.LoderImage(DogCertificateExaminedActivity.this, data.getDetailResultRespVo().getImmunePhoto(), binding.certificateCoverView, 5);

            } else {
                binding.idNumView.setText("");
                binding.dogTypeView.setText("");
                binding.dogColorView.setText("");
                binding.dogGenderView.setText("");
                binding.orgNameView.setText("");
                binding.detailedAddressView.setText("");
                binding.awardTimeView.setText("");
            }

            //犬主详细信息
            if (data.getDogLicenseUserDetailVo() != null) {
                binding.userNameView.setText(data.getDogLicenseUserDetailVo().getUserName());
                binding.userIdNumView.setText(data.getDogLicenseUserDetailVo().getIdNum());
                binding.contactPhoneNumView.setText(data.getDogLicenseUserDetailVo().getContactPhoneNum());
                binding.userDetailedAddressView.setText(data.getDogLicenseUserDetailVo().getDetailedAddress());
            } else {
                binding.userNameView.setText("");
                binding.userIdNumView.setText("");
                binding.contactPhoneNumView.setText("");
                binding.userDetailedAddressView.setText("");
            }

            //犬证年审信息
            if (data.getBizLicenseApproveDetailVo() != null) {
                binding.expireTimeView.setText(data.getBizLicenseApproveDetailVo().getExpireTime());
                binding.acceptUnitView.setText(data.getBizLicenseApproveDetailVo().getAcceptUnit());
                binding.approveUserNameView.setText(data.getBizLicenseApproveDetailVo().getApproveUserName());
            } else {
                binding.expireTimeView.setText("");
                binding.acceptUnitView.setText("");
                binding.approveUserNameView.setText("");
            }
        } else {
            binding.dianZhiContainer.setVisibility(View.GONE);

            binding.idNumView.setText("");
            binding.dogTypeView.setText("");
            binding.dogColorView.setText("");
            binding.dogGenderView.setText("");
            binding.orgNameView.setText("");
            binding.detailedAddressView.setText("");
            binding.awardTimeView.setText("");

            binding.userNameView.setText("");
            binding.userIdNumView.setText("");
            binding.contactPhoneNumView.setText("");
            binding.userDetailedAddressView.setText("");

            binding.expireTimeView.setText("");
            binding.acceptUnitView.setText("");
            binding.approveUserNameView.setText("");
        }
    }


    public void onClickConfirm(View view) {
        if (dogDetail == null) {
            ToastUtils.showShort(getApplicationContext(), "信息有误");
            return;
        }
        if (dogDetail.getLincenceId() == 0) {
            if (CommonUtil.isBlank(dogCertificate)) {
                ToastUtils.showShort(getApplicationContext(), "请上传纸质犬证");
                return;
            }
            if (CommonUtil.isBlank(immuneCertificate)) {
                ToastUtils.showShort(getApplicationContext(), "请上传纸质免疫证明");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("paperLicence", dogCertificate);
            bundle.putString("paperImmuneLicence", immuneCertificate);
            bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_examined);
            openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("dataBean", dogDetail);
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
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                uploadFile(requestCode, file.getAbsolutePath());
                                            }
                                        }).start();

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

    /**
     * 华为云 上传文件
     *
     * @param requestCode
     * @param filePath
     */
    private void uploadFile(int requestCode, String filePath) {
        try {

            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(Config.huaweiBucketName);
            request.setObjectKey(fileName);
            request.setFile(new File(filePath));
            request.setProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressStatus status) {
                    // 获取上传平均速率
                    Log.i(TAG, "uploadFile: AverageSpeed:" + status.getAverageSpeed());
                    // 获取上传进度百分比
                    Log.i(TAG, "uploadFile: TransferPercentage:" + status.getTransferPercentage());
                }
            });
            //每上传1MB数据反馈上传进度
            request.setProgressInterval(1024 * 1024L);
            PutObjectResult result = UploadFileManager.getInstance().getObsClient().putObject(request);
            Log.i(TAG, "uploadFile: getObjectUrl = " + result.getObjectUrl());
            String url = "http://" + Config.huaweiBucketName + "." + Config.huaweiCloudEndPoint + "/" + fileName;
            Log.i(TAG, "uploadFile: url = " + url);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (requestCode == request_DogCertificate) {
                        dogCertificate = url;
                        GlideLoader.LoderImage(DogCertificateExaminedActivity.this, dogCertificate, binding.paperDogCertificateView, 6);

                    } else if (requestCode == request_ImmuneCertificate) {
                        immuneCertificate = url;
                        GlideLoader.LoderImage(DogCertificateExaminedActivity.this, immuneCertificate, binding.paperImmuneCertificateView, 6);

                    }

                }
            });

        } catch (Exception e) {
            ToastUtils.showShort(getApplication(), "上传失败");
        }


    }
}