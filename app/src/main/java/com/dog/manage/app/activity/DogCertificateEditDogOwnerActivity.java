package com.dog.manage.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.PermissionUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogOwnerBinding;
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
 * 犬证办理
 */
public class DogCertificateEditDogOwnerActivity extends BaseActivity {

    private ActivityDogCertificateEditDogOwnerBinding binding;

    public static final int type_userInfo = 0;//我的信息
    public static final int type_certificate = 1;//犬证办理
    public static final int type_immune = 2;//免疫证办理
    public static final int type_adoption = 3;//犬只领养
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_dog_owner);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);

        if (type == type_userInfo) {
            binding.titleView.binding.itemTitle.setText("我的信息");
            binding.stepContainer.setVisibility(View.GONE);
            binding.confirmView.setText("保存信息");

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交审核");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③选择医院");

        } else if (type == type_adoption) {
            binding.titleView.binding.itemTitle.setText("犬只领养");
            binding.firstStepView.setSelected(true);
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("①选择犬只");
            binding.secondStepView.setText("②犬主信息");
            binding.thirdStepView.setText("③提交审核");
            binding.secondStepView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_10), 0, getResources().getDimensionPixelOffset(R.dimen.dp_10), 0);

        }

        initPersonal();
        binding.radioGroupDogOwner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOrgan:
                        initOrganView();

                        break;
                    case R.id.radioButtonPersonal:
                        initPersonal();

                        break;
                    default:
                        break;
                }
            }
        });


    }

    /**
     * 个人办理
     */
    private void initPersonal() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.VISIBLE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogTypeContainer.setVisibility(View.VISIBLE);
        if (binding.radioGroupDogType.getCheckedRadioButtonId() == R.id.radioButtonOldMan) {
            binding.oldManContainer.setVisibility(View.VISIBLE);
        } else {
            binding.oldManContainer.setVisibility(View.GONE);
        }
        binding.oldManOrDisabledCertificateContainer.setVisibility(View.VISIBLE);
        binding.houseNumberView.setVisibility(View.VISIBLE);
        binding.houseProprietaryCertificateContainer.setVisibility(View.VISIBLE);

        binding.organNameView.setVisibility(View.GONE);
        binding.organCertificateContainer.setVisibility(View.GONE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.GONE);
        binding.facilityContainer.setVisibility(View.GONE);
        binding.managementSystemContainer.setVisibility(View.GONE);

        binding.dogOwnerNameView.binding.itemTitle.setText("犬主姓名");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入犬主姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("身份证号码");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入身份证号码");

        binding.addressView.binding.itemTitle.setText("居住地址");

        binding.radioGroupIDCard.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonHaiWai:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("上传外国护照");

                        break;
                    case R.id.radioButtonGanGao:
                        binding.IDCardBackContainer.setVisibility(View.INVISIBLE);
                        binding.IDCardFrontHintView.setText("上传港澳通信证");

                        break;
                    case R.id.radioButtonIDCard:
                        binding.IDCardBackContainer.setVisibility(View.VISIBLE);
                        binding.IDCardFrontHintView.setText("上传身份证人像面");

                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupDogType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan://陪伴犬
                        Log.i(TAG, "onCheckedChanged: radioButtonOldMan");
                        binding.oldManContainer.setVisibility(View.VISIBLE);
                        binding.oldManOrDisabledCertificateHintView.setText("螺寡老人证明");

                        break;
                    case R.id.radioButtonDisabled://导盲犬/扶助犬
                        Log.i(TAG, "onCheckedChanged: radioButtonDisabled");
                        binding.oldManContainer.setVisibility(View.GONE);
                        binding.oldManOrDisabledCertificateHintView.setText("残疾人证");

                        break;
                    default:
                        break;
                }
            }
        });
        binding.radioGroupOldMan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonOldMan0:
//                        binding.oldManHintView.setVisibility(View.GONE);

                        break;
                    case R.id.radioButtonOldMan1:
//                        binding.oldManHintView.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 单位办理
     */
    private void initOrganView() {

        binding.dogOwnerPersonalCertificateTypeView.setVisibility(View.GONE);
        binding.dogOwnerPersonalCertificateContainer.setVisibility(View.GONE);
        binding.dogTypeContainer.setVisibility(View.GONE);
        binding.oldManContainer.setVisibility(View.GONE);
        binding.oldManOrDisabledCertificateContainer.setVisibility(View.GONE);
        binding.houseNumberView.setVisibility(View.GONE);
        binding.houseProprietaryCertificateContainer.setVisibility(View.GONE);

        binding.organNameView.setVisibility(View.VISIBLE);
        binding.organCertificateContainer.setVisibility(View.VISIBLE);
        binding.dogOwnerOrganCertificateContainer.setVisibility(View.VISIBLE);
        binding.facilityContainer.setVisibility(View.VISIBLE);
        binding.managementSystemContainer.setVisibility(View.VISIBLE);

        binding.dogOwnerNameView.binding.itemTitle.setText("法人姓名");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入法人姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("法人身份证号码");
        binding.dogOwnerNameView.binding.itemContent.setText("请输入法人身份证号码");

        binding.addressView.binding.itemTitle.setText("养犬地址");


    }

    public void onClickConfirm(View view) {
        if (type == type_adoption) {
            openActivity(DogAdoptionSubmitActivity.class);

        }else if (type == type_userInfo) {
            finish();

        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            openActivity(DogCertificateEditDogActivity.class, bundle);

        }
    }

    private final int request_IDCardFront = 100;
    private final int request_IDCardBack = 200;
    private final int request_LegalPersonIDCardFront = 300;
    private final int request_LegalPersonIDCardBack = 400;
    private final int request_BusinessLicense = 500;
    private final int request_OldManOrDisabledCertificate = 600;
    private final int request_HouseProprietaryCertificate = 700;
    private final int request_ManagementSystem = 800;
    private final int request_Facility1 = 900;
    private final int request_Facility2 = 1000;

    /**
     * 个人证件 上传身份证人像面
     *
     * @param view
     */
    public void onClickIDCardFront(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_IDCardFront)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_IDCardFront);
        }
    }

    /**
     * 个人证件 上传身份证国徽面
     *
     * @param view
     */
    public void onClickIDCardBack(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_IDCardBack)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_IDCardBack);
        }
    }

    /**
     * 法人证件 上传身份证人像面
     *
     * @param view
     */
    public void onClickLegalPersonIDCardFront(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_LegalPersonIDCardFront)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_LegalPersonIDCardFront);
        }
    }

    /**
     * 法人证件 上传身份证国徽面
     *
     * @param view
     */
    public void onClickLegalPersonIDCardBack(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_LegalPersonIDCardBack)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_LegalPersonIDCardBack);
        }
    }

    /**
     * 上传单位营业执照
     *
     * @param view
     */
    public void onClickBusinessLicense(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_BusinessLicense)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_BusinessLicense);
        }
    }

    /**
     * 上传残疾人证或鳏寡老人证明
     *
     * @param view
     */
    public void onClickOldManOrDisabledCertificate(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_OldManOrDisabledCertificate)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_OldManOrDisabledCertificate);
        }
    }

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

    /**
     * 上传管理制度文件照片
     *
     * @param view
     */
    public void onClickManagementSystem(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_ManagementSystem)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_ManagementSystem);
        }
    }

    /**
     * 上传养犬设施1图片
     *
     * @param view
     */
    public void onClickFacility1(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_Facility1)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Facility1);
        }
    }

    /**
     * 上传养犬设施2图片
     *
     * @param view
     */
    public void onClickFacility2(View view) {
        if (checkPermissions(PermissionUtils.STORAGE, request_Facility2)) {
            Bundle bundle = new Bundle();
            bundle.putInt("mediaType", MediaUtils.MEDIA_TYPE_PHOTO);
            bundle.putInt("maxNumber", 1);
            openActivity(MediaSelectActivity.class, bundle, request_Facility2);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case request_IDCardFront:
                    compressImage(data, request_IDCardFront);

                    break;
                case request_IDCardBack:
                    compressImage(data, request_IDCardBack);

                    break;
                case request_LegalPersonIDCardFront:
                    compressImage(data, request_LegalPersonIDCardFront);

                    break;
                case request_LegalPersonIDCardBack:
                    compressImage(data, request_LegalPersonIDCardBack);

                    break;
                case request_BusinessLicense:
                    compressImage(data, request_BusinessLicense);

                    break;
                case request_OldManOrDisabledCertificate:
                    compressImage(data, request_OldManOrDisabledCertificate);

                    break;
                case request_HouseProprietaryCertificate:
                    compressImage(data, request_HouseProprietaryCertificate);

                    break;
                case request_ManagementSystem:
                    compressImage(data, request_ManagementSystem);

                    break;
                case request_Facility1:
                    compressImage(data, request_Facility1);

                    break;
                case request_Facility2:
                    compressImage(data, request_Facility2);

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
                                        if (requestCode == request_IDCardFront) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.IDCardFrontView, 8);

                                        } else if (requestCode == request_IDCardBack) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.IDCardBackView, 8);

                                        } else if (requestCode == request_LegalPersonIDCardFront) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.legalPersonIDCardFrontView, 8);

                                        } else if (requestCode == request_LegalPersonIDCardBack) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.legalPersonIDCardBackView, 8);

                                        } else if (requestCode == request_BusinessLicense) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.businessLicenseView, 8);

                                        } else if (requestCode == request_OldManOrDisabledCertificate) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.oldManOrDisabledCertificateView, 8);

                                        } else if (requestCode == request_HouseProprietaryCertificate) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.houseProprietaryCertificateView, 8);

                                        } else if (requestCode == request_ManagementSystem) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.managementSystemView, 8);

                                        } else if (requestCode == request_Facility1) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.facility1View, 8);

                                        } else if (requestCode == request_Facility2) {
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.facility2View, 8);

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