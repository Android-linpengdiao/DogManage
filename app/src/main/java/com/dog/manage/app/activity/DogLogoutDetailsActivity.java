package com.dog.manage.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.PermissionUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogLogoutDetailsBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 注销记录详情
 */
public class DogLogoutDetailsActivity extends BaseActivity {

    private ActivityDogLogoutDetailsBinding binding;
    public static final int type_submit = 0;//0-提交注销
    public static final int type_details = 1;//1-注销详情
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_logout_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);

        if (type == type_submit) {
            initSubmitView();

        } else if (type == type_details) {
            initDetailsView();

        }

    }

    private void initDetailsView() {
        binding.dogInfoView.setVisibility(View.VISIBLE);
        binding.acceptUnitsHintView.setVisibility(View.VISIBLE);
        binding.reasonView.setVisibility(View.VISIBLE);
        binding.radioButton1.setTextColor(getResources().getColor(R.color.black));
        binding.radioButton1.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        binding.radioButton1.setPadding(0, 0, 0, 0);
        binding.radioButton1.setBackgroundColor(getResources().getColor(R.color.transparent));
        binding.radioButton2.setVisibility(View.GONE);

    }

    private void initSubmitView() {
        binding.selectView.setItemArrowVisible(true);
        binding.selectView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.radioGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        binding.pictureHintView.setText("上传照片(非必填)");
                        binding.descriptionHintView.setText("简要说明");
                        binding.pictureContainer2.setVisibility(View.INVISIBLE);
                        binding.pictureContainer3.setVisibility(View.INVISIBLE);

                        break;
                    case R.id.radioButton2:
                        binding.pictureHintView.setText("上传无害化处理过程图片3张");
                        binding.descriptionHintView.setText("简要说明(非必填)");
                        binding.pictureContainer2.setVisibility(View.VISIBLE);
                        binding.pictureContainer3.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_logout);
        openActivity(SubmitSuccessActivity.class, bundle);
    }


    private final int request_Image1 = 100;
    private final int request_Image2 = 200;
    private final int request_Image3 = 300;

    /**
     * 上传图片1
     *
     * @param view
     */
    public void onClickImage1(View view) {
        if (type == type_submit && checkPermissions(PermissionUtils.STORAGE, request_Image1)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Image1);
        }
    }

    /**
     * 上传图片2
     *
     * @param view
     */
    public void onClickImage2(View view) {
        if (type == type_submit && checkPermissions(PermissionUtils.STORAGE, request_Image2)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Image2);
        }
    }

    /**
     * 上传图片3
     *
     * @param view
     */
    public void onClickImage3(View view) {
        if (type == type_submit && checkPermissions(PermissionUtils.STORAGE, request_Image3)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Image3);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_Image1:
                    compressImage(data, request_Image1);

                    break;
                case request_Image2:
                    compressImage(data, request_Image2);

                    break;
                case request_Image3:
                    compressImage(data, request_Image3);

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
                                        if (requestCode == request_Image1) {
                                            GlideLoader.LoderImage(DogLogoutDetailsActivity.this, file.getAbsolutePath(), binding.image1View, 8);

                                        } else if (requestCode == request_Image2) {
                                            GlideLoader.LoderImage(DogLogoutDetailsActivity.this, file.getAbsolutePath(), binding.image2View, 8);

                                        } else if (requestCode == request_Image3) {
                                            GlideLoader.LoderImage(DogLogoutDetailsActivity.this, file.getAbsolutePath(), binding.image3View, 8);

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