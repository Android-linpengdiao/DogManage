package com.dog.manage.app.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.LogUtil;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityUpdateDogOwnerInfoBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.HandleInfo;
import com.dog.manage.app.utils.UploadFileManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.ProgressListener;
import com.obs.services.model.ProgressStatus;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 信息变更
 */
public class UpdateDogOwnerInfoActivity extends BaseActivity {

    private ActivityUpdateDogOwnerInfoBinding binding;
    public static final int type_details = 0;//犬主信息
    public static final int type_submit = 1;//提交

    private int type = 0;
    private Dog dogDetail = null;
    private int newAddressId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_dog_owner_info);
        addActivity(this);

        setTypeface(binding.acceptUnitsHintView);
        setTypeface(binding.detailedAddressHintView);
        setTypeface(binding.houseHintView);

        type = getIntent().getIntExtra("type", 0);

        dogDetail = (Dog) getIntent().getSerializableExtra("DogDetail");
        newAddressId = getIntent().getIntExtra("newAddressId", 0);
        if (dogDetail != null) {
            if (type == type_details) {
                binding.firstStepView.setSelected(true);
                binding.addressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity(AreaSelectActivity.class, request_City);
                    }
                });

            } else if (type == type_submit) {
                binding.secondStepView.setSelected(true);

                binding.confirmView.setText("提交审核");
                binding.submitContainer.setVisibility(View.VISIBLE);
                binding.dogOwnerContainer.setVisibility(View.GONE);

                getHandleInfo();
            }
        }

    }

    private HandleInfo handleInfo;

    private void getHandleInfo() {
        /**
         * dogId
         * integer
         * 犬只id
         * addressId
         * integer
         * 地址id
         */
        Map<String, String> map = new HashMap<>();
        map.put("dogId", String.valueOf(dogDetail.getDogId()));
        map.put("addressId", String.valueOf(newAddressId));
        SendRequest.getHandleInfo(map, new GenericsCallback<ResultClient<HandleInfo>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<HandleInfo> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    handleInfo = response.getData();
                    binding.handleUnitAddressView.setText(response.getData().getHandleUnitAddress());
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    private String address = null;
    private String detailedAddress = null;
    private String houseNum = null;
    private String housePhoto = null;
    private String addressArea = null;

    public void onClickConfirm(View view) {
        if (type == type_details) {

            if (CommonUtil.isBlank(address)) {
                ToastUtils.showShort(getApplicationContext(), "请选择居住地址");
                return;
            }

            detailedAddress = binding.detailedAddressView.getText().toString();
            if (CommonUtil.isBlank(detailedAddress)) {
                ToastUtils.showShort(getApplicationContext(), "请输入详细地址");
                return;
            }

            houseNum = binding.houseNumberView.binding.itemEdit.getText().toString();
            if (CommonUtil.isBlank(houseNum)) {
                ToastUtils.showShort(getApplicationContext(), "请输入房本编号");
                return;
            }

            if (CommonUtil.isBlank(housePhoto)) {
                ToastUtils.showShort(getApplicationContext(), "请上传房产证或房屋租赁合同");
                return;
            }

            if (dogDetail == null) {
                ToastUtils.showShort(getApplicationContext(), "提交失败,犬只信息有误");
                return;
            }

            /**
             * lincenceId
             * integer
             * 犬证编号
             * address
             * string
             * 居住地址
             * detailedAddress
             * string
             * 详细地址
             * houseNum
             * string
             * 房本编号
             * housePhoto
             * string
             * 房产证或租赁合同照片
             * addressArea
             * string
             * 所在区域
             */
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("lincenceId", String.valueOf(dogDetail.getLincenceId()));
            paramsMap.put("address", address);
            paramsMap.put("detailedAddress", detailedAddress);
            paramsMap.put("houseNum", houseNum);
            paramsMap.put("housePhoto", housePhoto);
            if (addressArea != null)
                paramsMap.put("addressArea", addressArea);

            SendRequest.saveCancelAddress(paramsMap, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {

                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                    LoadingManager.showLoadingDialog(UpdateDogOwnerInfoActivity.this);
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    LoadingManager.hideLoadingDialog(UpdateDogOwnerInfoActivity.this);
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                }

                @Override
                public void onResponse(BaseData response, int id) {
                    if (response.isSuccess()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", type_submit);
                        bundle.putInt("newAddressId", 0);
                        bundle.putSerializable("DogDetail", dogDetail);
                        openActivity(UpdateDogOwnerInfoActivity.class, bundle);

                    } else {
                        ToastUtils.showShort(getApplicationContext(), response.getMsg());

                    }

                }
            });

        } else if (type == type_submit) {

            if (handleInfo == null) {
                ToastUtils.showShort(getApplicationContext(), "提交失败,受理单位信息有误");
                return;
            }
            if (dogDetail == null) {
                ToastUtils.showShort(getApplicationContext(), "提交失败,犬只信息有误");
                return;
            }

            /**
             * lincenceId
             * integer
             * 犬只id
             * newAddressId
             * integer
             * 新地址id
             * acceptUnit
             * string
             * 受理单位
             * unitId
             * integer
             */
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("lincenceId", String.valueOf(dogDetail.getLincenceId()));
            paramsMap.put("newAddressId", String.valueOf(newAddressId));
            paramsMap.put("acceptUnit", handleInfo.getHandleUnitAddress());
            paramsMap.put("unitId", String.valueOf(handleInfo.getHandleUnitId()));
            paramsMap.put("housePhoto", housePhoto);
            if (addressArea != null)
                paramsMap.put("addressArea", addressArea);
            SendRequest.approveCancelAddress(paramsMap, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(BaseData response, int id) {
                    if (response.isSuccess()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", SubmitSuccessActivity.type_update);
                        openActivity(SubmitSuccessActivity.class, bundle);

                        finishActivity(DogManageWorkflowActivity.class);
                        finishActivity(UpdateDogCertificateActivity.class);
                        finish();

                    } else {
                        ToastUtils.showShort(getApplicationContext(), response.getMsg());
                    }

                }
            });

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
                        address = data.getStringExtra("cityName");
                        binding.addressView.binding.itemContent.setText(address);
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
                                .ignoreBy(500)// 忽略不压缩图片的大小
                                .setTargetDir(FileUtils.getMediaPath())// 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        if (requestCode == request_HouseProprietaryCertificate) {
                                            housePhoto = file.getAbsolutePath();
                                            GlideLoader.LoderImage(UpdateDogOwnerInfoActivity.this, file.getAbsolutePath(), binding.houseProprietaryCertificateView, 6);

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    uploadFile(file.getAbsolutePath());
                                                }
                                            }).start();
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
        Log.i(TAG, "uploadFile: " + filePath);
        PutObjectRequest request = new PutObjectRequest();
        request.setBucketName(Config.huaweiBucketName);
        request.setObjectKey(Config.huaweiObjectKey);
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


//        ObsClient obsClient = null;
//        try {
//            // 创建ObsClient实例
//            obsClient = new ObsClient(Config.huaweiCloudAccessKey, Config.huaweiCloudSecretKey, Config.huaweiCloudEndPoint);
//
//            //创建桶
//            //obsClient.createBucket("bucketname");
//
//            // localfile为待上传的本地文件路径，需要指定到具体的文件名
//            obsClient.putObject(Config.huaweiBucketName, Config.huaweiObjectKey, new File(filePath));
//            // localfile2 为待上传的本地文件路径，需要指定到具体的文件名
////            PutObjectRequest request = new PutObjectRequest();
////            request.setBucketName(Config.huaweiBucketName);
////            request.setObjectKey(Config.huaweiObjectKey+"_"+System.currentTimeMillis());
////            request.setFile(new File(filePath));
////            request.setProgressListener(new ProgressListener() {
////                @Override
////                public void progressChanged(ProgressStatus status) {
////                    // 获取上传平均速率
////                    Log.i("PutObject", "AverageSpeed:" + status.getAverageSpeed());
////                    // 获取上传进度百分比
////                    Log.i("PutObject", "TransferPercentage:" + status.getTransferPercentage());
////                }
////            });
////            obsClient.putObject(request);
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