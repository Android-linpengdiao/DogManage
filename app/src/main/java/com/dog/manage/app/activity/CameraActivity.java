package com.dog.manage.app.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.ImageReader;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.base.UserInfo;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.ToastUtils;
import com.cjt2325.camera.JCameraView;
import com.cjt2325.camera.listener.JCameraListener;
import com.cjt2325.camera.util.FileUtil;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityCameraBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.model.PetArchives;
import com.dog.manage.app.model.PetType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.callbacks.StringCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class CameraActivity extends BaseActivity{

    private ActivityCameraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_camera);
        setStatusBarDarkTheme(true);
        addActivity(this);

        if (CommonUtil.isBlank(getUserInfo().getAccessToken())) {
            getAccessToken();
        }

        initCamera();

    }

    private void initCamera() {
        binding.cameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);
        //CameraView监听
        binding.cameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i(TAG, "captureSuccess: bitmap = " + bitmap.getWidth());
                String path = FileUtil.saveBitmap("JCamera", bitmap);
            }

            @Override
            public void recordSuccess(final String videoPath, final Bitmap firstFrame) {


            }
        });
    }


    private void getAccessToken() {
        SendRequest.getAccessToken(Config.yueBaoAccessKey, Config.yueBaoSecretKey,
                new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        try {
                            JSONObject object = new JSONObject(response);
                            int status = object.optInt("status");
                            if (status == 200) {
                                String accessToken = object.optJSONObject("data").optString("access_token");
                                UserInfo userInfo = getUserInfo();
                                userInfo.setAccessToken(accessToken);
                                setUserInfo(userInfo);

                            } else if (status == 401) {
                                ToastUtils.showShort(CameraActivity.this, object.optString("message"));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void onClickConfirm(View view) {
//        if (checkPermissions(PermissionUtils.STORAGE, request_Phone)) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
//            bundle.putInt("maxNumber", 1);
//            openActivity(MediaSelectActivity.class, bundle, request_Phone);
//        }
    }

    private final int request_Phone = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_Phone:
                    compressImage(data);

                    break;
            }
        }
    }

    private void compressImage(Intent data) {
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
//                                        createPetArchives(file.getAbsolutePath());
//                                        petType(file.getAbsolutePath());

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
     * 宠物狗面部+鼻纹建档
     *
     * @param filePath
     */
    private void createPetArchives(String filePath) {
        SendRequest.createPetArchives(getUserInfo().getAccessToken(), filePath,
                new GenericsCallback<PetArchives>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PetArchives response, int id) {
                        if (response.getStatus() == 200 && response.getData() != null) {

                        } else {
                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
                        }

                    }
                });
    }

    /**
     * 宠物品种识别
     *
     * @param filePath
     */
    private void petType(String filePath) {
        SendRequest.petType(getUserInfo().getAccessToken(), filePath,
                new GenericsCallback<PetType>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(PetType response, int id) {
                        if (response.getStatus() == 200) {

                        } else {
                            ToastUtils.showShort(CameraActivity.this, response.getMessage());
                        }

                    }
                });
    }
}