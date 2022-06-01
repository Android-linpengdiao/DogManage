package com.dog.manage.app.activity;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.RecordActivity;
import com.dog.manage.app.activity.record.PunishRecordActivity;
import com.dog.manage.app.databinding.ActivityUserHomeBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UserHomeActivity extends BaseActivity {

    private ActivityUserHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_user_home);
        addActivity(this);

        if (!CommonUtil.isBlank(getUserInfo().getLoginPhone()) && getUserInfo().getLoginPhone().length() >= 11) {
            String phone = getUserInfo().getLoginPhone();
            binding.userNameView.setText(phone.substring(0, 3) + "****" + phone.substring(7));
        } else {
            binding.userNameView.setText(getUserInfo().getLoginPhone());
        }
    }

    public void onClickHead(View view) {
//        permissionsManager();
    }

    private final int REQUEST_IMAGE = 100;
    private final int REQUEST_CAMERA = 200;
    private final int requestCode = 1100;

    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @AfterPermissionGranted(requestCode)
    private void permissionsManager() {
        if (EasyPermissions.hasPermissions(UserHomeActivity.this, permissions)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, REQUEST_IMAGE);

        } else {
            EasyPermissions.requestPermissions(this, "请同意下面的权限", requestCode, permissions);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    try {
                        if (data != null) {
                            String imageJson = data.getStringExtra("imageJson");
                            if (!TextUtils.isEmpty(imageJson)) {
                                Gson gson = new Gson();
                                List<MediaFile> imageList = gson.fromJson(imageJson, new TypeToken<List<MediaFile>>() {
                                }.getType());
                                if (imageList != null && imageList.size() > 0) {
                                    String path = imageList.get(0).getPath();
                                    GlideLoader.LoderCircleImage(UserHomeActivity.this, path, binding.userIconView);
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
                                                    GlideLoader.LoderCircleImage(UserHomeActivity.this, file.getAbsolutePath(), binding.userIconView);
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

                    break;
                case REQUEST_CAMERA:

                    break;
            }
        }
    }

    public void onClickUserInfo(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_userInfo);
        openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

    }

    public void onClickMyDogImmune(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", MyDogCertificateOrImmuneActivity.type_immune);
        openActivity(MyDogCertificateOrImmuneActivity.class, bundle);

    }

    public void onClickMyDogCertificate(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", MyDogCertificateOrImmuneActivity.type_certificate);
        openActivity(MyDogCertificateOrImmuneActivity.class, bundle);

    }

    public void onClickCertificate(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", RecordActivity.type_certificate);
        openActivity(RecordActivity.class, bundle);

    }

    public void onClickImmune(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", RecordActivity.type_immune);
        openActivity(RecordActivity.class, bundle);

    }

    public void onClickTransfer(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", RecordActivity.type_transfer);
        openActivity(RecordActivity.class, bundle);

    }

    public void onClickAdoption(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", RecordActivity.type_adoption);
        openActivity(RecordActivity.class, bundle);

    }

    public void onClickLogout(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", RecordActivity.type_logout);
        openActivity(RecordActivity.class, bundle);

    }

    public void onClickPunish(View view) {
        openActivity(PunishRecordActivity.class);

    }

    public void onClickSetting(View view) {
        openActivity(SettingsActivity.class);
    }

}