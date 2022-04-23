package com.dog.manage.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.PermissionUtils;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityUpdateDogOwnerInfoBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.utils.UploadFileManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UpdateDogOwnerInfoActivity extends BaseActivity {

    private ActivityUpdateDogOwnerInfoBinding binding;
    public static final int type_details = 0;//犬主信息
    public static final int type_submit = 1;//提交

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_dog_owner_info);
        addActivity(this);

        setTypeface(binding.acceptUnitsHintView);
        setTypeface(binding.detailedAddressHintView);
        setTypeface(binding.houseHintView);

        type = getIntent().getIntExtra("type", 0);

        if (type == type_details) {
            binding.firstStepView.setSelected(true);
            binding.addressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openActivity(AreaSelectActivity.class,request_City);
                }
            });

        } else if (type == type_submit) {
            binding.secondStepView.setSelected(true);

            binding.confirmView.setText("提交审核");
            binding.submitContainer.setVisibility(View.VISIBLE);
            binding.dogOwnerContainer.setVisibility(View.GONE);
        }

    }

    public void onClickConfirm(View view) {
        if (type == 0) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            openActivity(UpdateDogOwnerInfoActivity.class, bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("type", SubmitSuccessActivity.type_update);
            openActivity(SubmitSuccessActivity.class, bundle);
        }
    }

    private final int request_HouseProprietaryCertificate = 100;
    private final int request_City = 200;

    /**
     * 上传房产证或房屋租赁合同
     *
     * @param view
     */
    public void onClickHouseProprietaryCertificate(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_HouseProprietaryCertificate)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_HouseProprietaryCertificate);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_HouseProprietaryCertificate:
                    compressImage(data, request_HouseProprietaryCertificate);

                    break;
                case request_City:
                    if (data != null) {
                        String cityName = data.getStringExtra("cityName");
                        binding.addressView.binding.itemContent.setText(cityName);
                    }
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
                                .ignoreBy(100)// 忽略不压缩图片的大小
                                .setTargetDir(FileUtils.getMediaPath())// 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        if (requestCode == request_HouseProprietaryCertificate) {
                                            GlideLoader.LoderImage(UpdateDogOwnerInfoActivity.this, file.getAbsolutePath(), binding.houseProprietaryCertificateView, 8);

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

    /**
     * 华为云 上传文件
     *
     * @param filePath
     */
    private void uploadFile(String filePath) {

        PutObjectRequest request = new PutObjectRequest();
        request.setBucketName("bucketName");
        request.setObjectKey("objectKey");
        request.setFile(new File(filePath));
        request.setProgressListener(new ProgressListener() {
            @Override
            public void progressChanged(ProgressStatus status) {
                // 获取上传平均速率
                Log.i("PutObject", "AverageSpeed:" + status.getAverageSpeed());
                // 获取上传进度百分比
                Log.i("PutObject", "TransferPercentage:" + status.getTransferPercentage());
            }
        });
        //每上传1MB数据反馈上传进度
        request.setProgressInterval(1024 * 1024L);
        UploadFileManager.getInstance().getObsClient().putObject(request);


//        ObsClient obsClient = null;
//        try {
//            // 创建ObsClient实例
//            obsClient = new ObsClient(Config.huaweiCloudAccessKey, Config.huaweiCloudSecretKey, Config.huaweiCloudEndPoint);
//
//            //创建桶
//            //obsClient.createBucket("bucketname");
//
//            // localfile为待上传的本地文件路径，需要指定到具体的文件名
//            obsClient.putObject("bucketname", "objectname", new File("localfile"));
//            // localfile2 为待上传的本地文件路径，需要指定到具体的文件名
//            PutObjectRequest request = new PutObjectRequest();
//            request.setBucketName("bucketname");
//            request.setObjectKey("objectname2");
//            request.setFile(new File("localfile2"));
//            request.setProgressListener(new ProgressListener() {
//                @Override
//                public void progressChanged(ProgressStatus status) {
//                    // 获取上传平均速率
//                    Log.i("PutObject", "AverageSpeed:" + status.getAverageSpeed());
//                    // 获取上传进度百分比
//                    Log.i("PutObject", "TransferPercentage:" + status.getTransferPercentage());
//                }
//            });
//            obsClient.putObject(request);
//
//            // 使用访问OBS
//
//
//        } catch (ObsException e) {
//            Log.e("PutObject", "Response Code: " + e.getResponseCode());
//            Log.e("PutObject", "Error Message: " + e.getErrorMessage());
//            Log.e("PutObject", "Error Code:       " + e.getErrorCode());
//            Log.e("PutObject", "Request ID:      " + e.getErrorRequestId());
//            Log.e("PutObject", "Host ID:           " + e.getErrorHostId());
//        } finally {
//            // 关闭ObsClient实例，如果是全局ObsClient实例，可以不在每个方法调用完成后关闭
//            // ObsClient在调用ObsClient.close方法关闭后不能再次使用
//            if (obsClient != null) {
//                try {
//                    // 关闭obsClient
//                    obsClient.close();
//                } catch (IOException e) {
//                    e.getMessage();
//                }
//            }
//        }

    }
}