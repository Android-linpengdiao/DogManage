package com.dog.manage.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.PermissionUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class DogCertificateEditDogActivity extends BaseActivity {

    private ActivityDogCertificateEditDogBinding binding;

    public static final int type_userInfo = 0;//我的信息
    public static final int type_certificate = 1;//犬证办理
    public static final int type_immune = 2;//免疫证办理
    private int type = 0;

    private List<String> dogList = Arrays.asList("添加新犬只","萨摩耶", "柯基", "泰迪", "哈士奇");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_dog);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);

        if (type == type_userInfo) {
            binding.titleView.binding.itemTitle.setText("我的信息");

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理");
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交审核");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③选择医院");

        }

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogCertificateEditDogActivity.this, dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()), new OnClickListener() {
                    @Override
                    public void onClick(View view, Object object) {
                        String content = (String) object;
                        binding.dogCertificateView.binding.itemContent.setText(content);
                    }

                    @Override
                    public void onLongClick(View view, Object object) {

                    }
                });
            }
        });

    }

    public void onClickConfirm(View view) {
        if (type == type_userInfo) {


        } else if (type == type_certificate) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
            openActivity(DogCertificateEditSubmitActivity.class, bundle);

        } else if (type == type_immune) {
            openActivity(DogImmuneHospitalActivity.class);

        }
    }


    private final int request_Testify = 100;
    private final int request_LeftFace = 200;
    private final int request_CenterFace = 300;
    private final int request_RightFace = 400;

    /**
     * 上传绝育证明
     *
     * @param view
     */
    public void onClickTestify(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_Testify)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Testify);
        }
    }

    /**
     * 上传左侧面照片
     *
     * @param view
     */
    public void onClickLeftFace(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_LeftFace)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_LeftFace);
        }
    }

    /**
     * 上传正面照片
     *
     * @param view
     */
    public void onClickCenterFace(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_CenterFace)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_CenterFace);
        }
    }

    /**
     * 上右侧面照片
     *
     * @param view
     */
    public void onClickRightFace(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_RightFace)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_RightFace);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_Testify:
                    compressImage(data, request_Testify);

                    break;
                case request_LeftFace:
                    compressImage(data, request_LeftFace);

                    break;
                case request_CenterFace:
                    compressImage(data, request_CenterFace);

                    break;
                case request_RightFace:
                    compressImage(data, request_RightFace);

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
                                        if (requestCode == request_Testify) {
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.testifyView, 8);

                                        } else if (requestCode == request_LeftFace) {
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.leftFaceView, 8);

                                        } else if (requestCode == request_CenterFace) {
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.centerFaceView, 8);

                                        } else if (requestCode == request_RightFace) {
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.rightFaceView, 8);

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