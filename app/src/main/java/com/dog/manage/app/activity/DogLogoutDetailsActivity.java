package com.dog.manage.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.LogUtil;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogLogoutDetailsBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
 * 注销记录详情
 */
public class DogLogoutDetailsActivity extends BaseActivity {

    private ActivityDogLogoutDetailsBinding binding;
    public static final int type_submit = 0;//0-提交注销
    public static final int type_details = 1;//1-注销详情
    private int type = 0;
    private int auditType = 0;//1-审核通过 2-审核拒绝

    private List<String> dogList = Arrays.asList("萨摩耶", "柯基", "泰迪", "哈士奇");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_logout_details);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        auditType = getIntent().getIntExtra("auditType", 0);
        binding.auditStatusView.setText(auditType == 1 ? "审核通过" : auditType == 2 ? "审核拒绝" : "审核中");

        if (type == type_submit) {
            initSubmitView();

        } else if (type == type_details) {
            initDetailsView();

        }

    }

    private void initDetailsView() {
        binding.dogInfoView.setVisibility(View.VISIBLE);
        binding.acceptUnitsHintView.setVisibility(View.VISIBLE);
        binding.auditReasonView.setVisibility(auditType == 2 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(auditType == 2 ? View.VISIBLE : View.GONE);

        binding.radioButton1.setTextColor(getResources().getColor(R.color.black));
        binding.radioButton1.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        binding.radioButton1.setPadding(0, 0, 0, 0);
        binding.radioButton1.setBackgroundColor(getResources().getColor(R.color.transparent));
        binding.radioButton0.setVisibility(View.GONE);

        binding.descriptionView.setEnabled(false);
        binding.descriptionView.setText("丢失了");
        GlideLoader.LoderImage(DogLogoutDetailsActivity.this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.pictureView1, 6);
        GlideLoader.LoderImage(DogLogoutDetailsActivity.this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.pictureView2, 6);
        GlideLoader.LoderImage(DogLogoutDetailsActivity.this, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.pictureView3, 6);


    }

    private void initSubmitView() {
        binding.titleView.binding.itemTitle.setText("犬只注销");
        binding.descriptionHintView.setText("简要说明(非必填)");
        binding.dogCertificateView.setItemArrowVisible(true);
        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogLogoutDetailsActivity.this, null, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()), new OnClickListener() {
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
        binding.radioGroupView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        binding.pictureHintView.setText("上传照片(非必填)");
                        binding.descriptionHintView.setText("简要说明");
                        binding.pictureContainer2.setVisibility(View.INVISIBLE);
                        binding.pictureContainer3.setVisibility(View.INVISIBLE);

//                        picture1 = null;
//                        picture2 = null;
//                        picture3 = null;
//                        GlideLoader.LoderUploadImage(DogLogoutDetailsActivity.this, null, binding.pictureView1, 6);
//                        GlideLoader.LoderUploadImage(DogLogoutDetailsActivity.this, null, binding.pictureView2, 6);
//                        GlideLoader.LoderUploadImage(DogLogoutDetailsActivity.this, null, binding.pictureView3, 6);


                        break;
                    case R.id.radioButton0:
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

    private int dogCertificate = -1;
    private String picture1 = null;
    private String picture2 = null;
    private String picture3 = null;
    private String description = null;

    public void onClickConfirm(View view) {

        Map<String, String> paramsMap = new HashMap<>();
        if (dogCertificate < 0) {
            ToastUtils.showShort(getApplicationContext(), "请选择犬只");
            return;
        }
        paramsMap.put("dogCertificate", String.valueOf(dogCertificate));
        int checkedRadioButtonId = binding.radioGroupView.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radioButton0) {//犬只死亡
            if (CommonUtil.isBlank(picture1) || CommonUtil.isBlank(picture2) || CommonUtil.isBlank(picture3)) {
                ToastUtils.showShort(getApplicationContext(), "请上传无害化处理过程图片3张");
                return;
            }
            paramsMap.put("picture1", String.valueOf(picture1));
            paramsMap.put("picture2", String.valueOf(picture2));
            paramsMap.put("picture3", String.valueOf(picture3));

        } else if (checkedRadioButtonId == R.id.radioButton1) {//犬只丢失
            description = binding.descriptionView.getText().toString();
            if (CommonUtil.isBlank(description)) {
                ToastUtils.showShort(getApplicationContext(), "请输入简要说明");
                return;
            }
            paramsMap.put("description", String.valueOf(description));

        }

        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_logout);
        openActivity(SubmitSuccessActivity.class, bundle);

        finishActivity(DogManageWorkflowActivity.class);
        finish();

        if (LogUtil.isDebug) {
            return;
        }
        SendRequest.DogLogout(getUserInfo().getAuthorization(), paramsMap, new GenericsCallback(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {

            }
        });
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
                                            picture1 = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogLogoutDetailsActivity.this, file.getAbsolutePath(), binding.pictureView1, 6);

                                        } else if (requestCode == request_Image2) {
                                            picture2 = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogLogoutDetailsActivity.this, file.getAbsolutePath(), binding.pictureView2, 6);

                                        } else if (requestCode == request_Image3) {
                                            picture3 = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogLogoutDetailsActivity.this, file.getAbsolutePath(), binding.pictureView3, 6);

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