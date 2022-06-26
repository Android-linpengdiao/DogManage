package com.dog.manage.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.LogUtil;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.Config;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogLogoutDetailsBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.model.RecordImmune;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
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

    private List<Dog> dogList = new ArrayList<>();

    private Dog dogDetail;
    private RecordImmune recordImmune;

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

        recordImmune = (RecordImmune) getIntent().getSerializableExtra("dataBean");
        if (recordImmune != null) {
            //status 办理状态 null （不传） 全部 0 未办理 1 已办理 2 驳回
            binding.auditStatusView.setText(recordImmune.getStatus() == 1 ? "审核通过" : recordImmune.getStatus() == 2 ? "审核拒绝" : "审核中");
            binding.contentView.setText(recordImmune.getDogType() + "-编号：" + recordImmune.getDogLicenceNum()
                    + "（" + (recordImmune.getCancelType() == 1 ? "犬只死亡" : "犬只丢失") + "）");
            binding.createTimeView.setText(recordImmune.getCreatedTime());

            binding.dogCertificateView.binding.itemTitle.setText("选择犬只");
            binding.dogCertificateView.binding.itemContent.setText(recordImmune.getDogType() + "（犬证编号：" + recordImmune.getDogLicenceNum() + "）");

            binding.auditReasonView.setVisibility(recordImmune.getStatus() == 2 ? View.VISIBLE : View.GONE);
            binding.confirmView.setVisibility(recordImmune.getStatus() == 2 ? View.VISIBLE : View.GONE);

            //cancelType 办理类型 1 死亡 2 丢失
            if (recordImmune.getCancelType() == 1) {
                binding.radioButton1.setVisibility(View.GONE);
            } else if (recordImmune.getCancelType() == 2) {
                binding.radioButton1.setTextColor(getResources().getColor(R.color.black));
                binding.radioButton1.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                binding.radioButton1.setPadding(0, 0, 0, 0);
                binding.radioButton1.setBackgroundColor(getResources().getColor(R.color.transparent));
                binding.radioButton0.setVisibility(View.GONE);
            }

            if (!CommonUtil.isBlank(recordImmune.getCancelReason())) {
                binding.descriptionView.setEnabled(false);
                binding.descriptionView.setText(recordImmune.getCancelReason());
            } else {
                binding.descriptionContainer.setVisibility(View.GONE);
            }

            binding.acceptUnitView.setText(recordImmune.getAcceptUnit());

            try {
                binding.pictureHintView.setText(recordImmune.getCancelType() == 1 ? "无害化处理过程图片" : "犬只照片");
                //犬只照片
                List<String> idPhotos = new Gson().fromJson(recordImmune.getCancelImageUrl(), new TypeToken<List<String>>() {
                }.getType());
                if (idPhotos.size() > 0) {
                    GlideLoader.LoderImage(DogLogoutDetailsActivity.this, idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.pictureView1, 6);
                } else {
                    binding.pictureContainer.setVisibility(View.GONE);
                }
                if (idPhotos.size() > 1) {
                    GlideLoader.LoderImage(DogLogoutDetailsActivity.this, idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.pictureView2, 6);
                } else {
                    binding.pictureContainer2.setVisibility(View.INVISIBLE);
                    binding.pictureContainer3.setVisibility(View.INVISIBLE);
                }
                if (idPhotos.size() > 2) {
                    GlideLoader.LoderImage(DogLogoutDetailsActivity.this, idPhotos.size() > 2 ? idPhotos.get(2) : "", binding.pictureView3, 6);
                } else {
                    binding.pictureContainer3.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.getMessage();
            }

        }

    }


    //=====================================     提交   ==============================================

    private void initSubmitView() {

//        picture1 = "http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/828adcb2-c53c-446f-ae04-4f1f653a7561.webp";
//        picture2 = "http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/828adcb2-c53c-446f-ae04-4f1f653a7561.webp";
//        picture3 = "http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/828adcb2-c53c-446f-ae04-4f1f653a7561.webp";
//        GlideLoader.LoderImage(DogLogoutDetailsActivity.this, "http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/828adcb2-c53c-446f-ae04-4f1f653a7561.webp", binding.pictureView1, 6);
//        GlideLoader.LoderImage(DogLogoutDetailsActivity.this, "http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/828adcb2-c53c-446f-ae04-4f1f653a7561.webp", binding.pictureView2, 6);
//        GlideLoader.LoderImage(DogLogoutDetailsActivity.this, "http://dogmanage.file.obs.cn-north-4.myhuaweicloud.com/828adcb2-c53c-446f-ae04-4f1f653a7561.webp", binding.pictureView3, 6);


        binding.titleView.binding.itemTitle.setText("犬只注销");
        binding.descriptionHintView.setText("简要说明(非必填)");
        binding.dogCertificateView.setItemArrowVisible(true);
        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogList.size() == 0) {
                    ToastUtils.showShort(getApplicationContext(), "暂无犬只");
                    return;
                }
                onClickDogCertificate(DogLogoutDetailsActivity.this,
                        dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()),
                        new OnClickListener() {
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
                        dogDetail = dogList.get(0);
                        binding.dogCertificateView.binding.itemContent.setText(dogDetail.getDogType());
                    }

                }
            }
        });
    }

    /**
     * dogLicenceId
     * integer
     * 犬证id
     * dogLicenceNum
     * string
     * 犬证编号
     * cancelReason
     * string
     * 注销理由
     * cancelType
     * integer
     * 办理类型 1 死亡 2 丢失
     * cancelImageUrl
     * string
     * 图片地址，与之前多张格式一致
     */

    private String picture1 = null;
    private String picture2 = null;
    private String picture3 = null;

    public void onClickConfirm(View view) {

        Map<String, String> paramsMap = new HashMap<>();
        if (dogDetail == null) {
            ToastUtils.showShort(getApplicationContext(), "请选择犬只");
            return;
        }

        paramsMap.put("dogLicenceId", String.valueOf(dogDetail.getLincenceId()));
        paramsMap.put("dogLicenceNum", String.valueOf(dogDetail.getIdNum()));

        int checkedRadioButtonId = binding.radioGroupView.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.radioButton0) {//犬只死亡
            if (CommonUtil.isBlank(picture1) || CommonUtil.isBlank(picture2) || CommonUtil.isBlank(picture3)) {
                ToastUtils.showShort(getApplicationContext(), "请上传无害化处理过程图片3张");
                return;
            }
            paramsMap.put("cancelImageUrl", GsonUtils.toJson(Arrays.asList(picture1, picture2, picture3)));

            String description = binding.descriptionView.getText().toString();
            if (!CommonUtil.isBlank(description)) {
                paramsMap.put("cancelReason", description);
            }
            paramsMap.put("cancelType", String.valueOf(1));

        } else if (checkedRadioButtonId == R.id.radioButton1) {//犬只丢失

            paramsMap.put("cancelImageUrl", GsonUtils.toJson(Arrays.asList(picture1)));

            String description = binding.descriptionView.getText().toString();
            if (CommonUtil.isBlank(description)) {
                ToastUtils.showShort(getApplicationContext(), "请输入简要说明");
                return;
            }
            paramsMap.put("cancelReason", description);
            paramsMap.put("cancelType", String.valueOf(2));

        }

        SendRequest.saveCancelDogInfo(paramsMap, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogLogoutDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogLogoutDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(BaseData response, int id) {
                if (response.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", SubmitSuccessActivity.type_logout);
                    openActivity(SubmitSuccessActivity.class, bundle);
                    finishActivity(DogManageWorkflowActivity.class);
                    finish();

                } else {
                    ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "提交失败");

                }
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
        String url = "http://" + Config.huaweiBucketName + "." + Config.huaweiCloudEndPoint + "/" + fileName;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (requestCode == request_Image1) {
                    picture1 = url;
                    GlideLoader.LoderImage(DogLogoutDetailsActivity.this, picture1, binding.pictureView1, 6);

                } else if (requestCode == request_Image2) {
                    picture2 = url;
                    GlideLoader.LoderImage(DogLogoutDetailsActivity.this, picture2, binding.pictureView2, 6);

                } else if (requestCode == request_Image3) {
                    picture3 = url;
                    GlideLoader.LoderImage(DogLogoutDetailsActivity.this, picture3, binding.pictureView3, 6);

                }

            }
        });


    }

}