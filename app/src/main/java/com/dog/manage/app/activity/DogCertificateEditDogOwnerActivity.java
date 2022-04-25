package com.dog.manage.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogOwnerBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static final int type_examined = 3;//犬证年审
    public static final int type_adoption = 4;//犬只领养
    public static final int type_details = 5;//犬主信息
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
            initPersonal();
            loadDate(type);

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交审核");
            initPersonal();

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③选择医院");
            initPersonal();

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("犬证办理");
            binding.firstStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交审核");
            initPersonal();

        } else if (type == type_adoption) {
            binding.titleView.binding.itemTitle.setText("犬只领养");
            binding.firstStepView.setSelected(true);
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("①选择犬只");
            binding.secondStepView.setText("②犬主信息");
            binding.thirdStepView.setText("③提交审核");
            binding.secondStepView.setPadding(getResources().getDimensionPixelOffset(R.dimen.dp_10), 0, getResources().getDimensionPixelOffset(R.dimen.dp_10), 0);
            initPersonal();

        } else if (type == type_details) {
            binding.titleView.binding.itemTitle.setText("犬主信息");
            binding.stepContainer.setVisibility(View.GONE);
            binding.confirmView.setVisibility(View.GONE);

            binding.radioButtonOrgan.setEnabled(false);
            binding.radioButtonPersonal.setEnabled(false);

            binding.radioButtonHaiWai.setEnabled(false);
            binding.radioButtonGanGao.setEnabled(false);
            binding.radioButtonIDCard.setEnabled(false);


            binding.organNameView.binding.itemEdit.setEnabled(false);
            binding.dogOwnerNameView.binding.itemEdit.setEnabled(false);
            binding.dogOwnerIDCardView.binding.itemEdit.setEnabled(false);

            loadDate(type);


        }

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
        binding.addressView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(AreaSelectActivity.class, request_City);
            }
        });


    }

    private void loadDate(int type) {
        //个人办理
        binding.radioButtonOrgan.setVisibility(View.GONE);
        binding.radioButtonHaiWai.setVisibility(View.GONE);
        binding.radioButtonGanGao.setVisibility(View.GONE);
        binding.dogOwnerNameView.binding.itemEdit.setText("张三");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("111111111111111111");
        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.IDCardFrontView, 6);
        GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this,
                "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.IDCardBackView, 6);

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
        binding.dogOwnerNameView.binding.itemEdit.setHint("请输入犬主姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("身份证号码");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("请输入身份证号码");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
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
                        oldManOrDisabledCertificate = null;
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, null, binding.oldManOrDisabledCertificateView, 6);
                        binding.oldManContainer.setVisibility(View.VISIBLE);
                        binding.oldManOrDisabledCertificateHintView.setText("鳏寡老人证明");

                        break;
                    case R.id.radioButtonDisabled://导盲犬/扶助犬
                        oldManOrDisabledCertificate = null;
                        GlideLoader.LoderUploadImage(DogCertificateEditDogOwnerActivity.this, null, binding.oldManOrDisabledCertificateView, 6);
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
        binding.dogOwnerNameView.binding.itemEdit.setHint("请输入法人姓名");
        binding.dogOwnerIDCardView.binding.itemTitle.setText("法人身份证号码");
        binding.dogOwnerIDCardView.binding.itemEdit.setHint("请输入法人身份证号码");

        binding.dogOwnerNameView.binding.itemEdit.setText("");
        binding.dogOwnerIDCardView.binding.itemEdit.setText("");
        binding.addressView.binding.itemTitle.setText("养犬地址");


    }

    //个人
    private int personalIDCardType = 0;
    private String personalIDCardFront = null;
    private String personalIDCardBack = null;
    private String personaHouseNumber = null;
    private String personaHouseProprietaryCertificate = null;

    //单位
    private String organName = null;
    private String businessLicense = null;
    private String legalPersonIDCardFront = null;
    private String legalPersonIDCardBack = null;
    private String legalManagementSystem = null;
    private String legalFacility1 = null;
    private String legalFacility2 = null;


    private String dogOwnerName = null;
    private String dogOwnerIDCard = null;
    private String oldManOrDisabledCertificate = null;
    private String address = null;
    private String detailedAddress = null;

    public void onClickConfirm(View view) {
        if (type == type_userInfo) {
            finish();

        } else if (type == type_certificate || type == type_immune || type == type_examined || type == type_adoption) {

            Map<String, Object> map = new HashMap<>();

            //犬主类型
            int checkedRadioButtonId = binding.radioGroupDogOwner.getCheckedRadioButtonId();
            if (checkedRadioButtonId == R.id.radioButtonOrgan) {
                //单位办理
                int IDCardCheckedRadioButtonId = binding.radioGroupIDCard.getCheckedRadioButtonId();

                organName = binding.organNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(organName)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入单位名称");
                    return;
                }

                if (CommonUtil.isBlank(businessLicense)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传单位营业执照");
                    return;
                }

                dogOwnerName = binding.dogOwnerNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(dogOwnerName)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入法人姓名");
                    return;
                }

                dogOwnerIDCard = binding.dogOwnerIDCardView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(dogOwnerIDCard)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入法人身份证号码");
                    return;
                }

                if (CommonUtil.isBlank(legalPersonIDCardFront)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传身份证人像面");
                    return;
                }

                if (CommonUtil.isBlank(legalPersonIDCardBack)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传身份证国徽面");
                    return;
                }

                if (CommonUtil.isBlank(address)) {
                    ToastUtils.showShort(getApplicationContext(), "请选择养犬地址");
                    return;
                }

                detailedAddress = binding.detailedAddressView.getText().toString();
                if (CommonUtil.isBlank(detailedAddress)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入详细地址");
                    return;
                }

                if (CommonUtil.isBlank(legalManagementSystem)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传管理制度文件照片");
                    return;
                }

                if (CommonUtil.isBlank(legalFacility1)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传养犬设施图片");
                    return;
                }

                if (CommonUtil.isBlank(legalFacility2)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传养犬设施图片");
                    return;
                }

            } else if (checkedRadioButtonId == R.id.radioButtonPersonal) {
                //个人办理
                int IDCardCheckedRadioButtonId = binding.radioGroupIDCard.getCheckedRadioButtonId();
                if (IDCardCheckedRadioButtonId == R.id.radioButtonIDCard) {//身份证
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传身份证人像面");
                        return;
                    }
                    if (CommonUtil.isBlank(personalIDCardBack)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传身份证国徽面");
                        return;
                    }
                } else if (IDCardCheckedRadioButtonId == R.id.radioButtonGanGao) {//港澳通信证
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传港澳通信证");
                        return;
                    }

                } else if (IDCardCheckedRadioButtonId == R.id.radioButtonHaiWai) {//外国护照
                    if (CommonUtil.isBlank(personalIDCardFront)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传外国护照");
                        return;
                    }

                }

                dogOwnerName = binding.dogOwnerNameView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(dogOwnerName)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入犬主姓名");
                    return;
                }

                dogOwnerIDCard = binding.dogOwnerIDCardView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(dogOwnerIDCard)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入身份证号码");
                    return;
                }

                int dogTypeCheckedRadioButtonId = binding.radioGroupDogType.getCheckedRadioButtonId();
                if (dogTypeCheckedRadioButtonId == R.id.radioButtonOldMan) {//陪伴犬

                    //是否为鳏寡老人
                    int oldManCheckedRadioButtonId = binding.radioGroupOldMan.getCheckedRadioButtonId();
                    if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan1) {//是
                        if (CommonUtil.isBlank(oldManOrDisabledCertificate)) {
                            ToastUtils.showShort(getApplicationContext(), "请上传鳏寡老人证明");
                            return;
                        }

                    } else if (oldManCheckedRadioButtonId == R.id.radioButtonOldMan0) {//否


                    }

                } else if (dogTypeCheckedRadioButtonId == R.id.radioButtonDisabled) {//导盲犬/扶助犬
                    if (CommonUtil.isBlank(oldManOrDisabledCertificate)) {
                        ToastUtils.showShort(getApplicationContext(), "请上传残疾人证");
                        return;
                    }

                }

                if (CommonUtil.isBlank(address)) {
                    ToastUtils.showShort(getApplicationContext(), "请选择居住地址");
                    return;
                }

                detailedAddress = binding.detailedAddressView.getText().toString();
                if (CommonUtil.isBlank(detailedAddress)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入详细地址");
                    return;
                }

                personaHouseNumber = binding.houseNumberView.binding.itemEdit.getText().toString();
                if (CommonUtil.isBlank(personaHouseNumber)) {
                    ToastUtils.showShort(getApplicationContext(), "请输入房本编号");
                    return;
                }

                if (CommonUtil.isBlank(personaHouseProprietaryCertificate)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传房产证或房屋租赁合同");
                    return;
                }

            }

            Bundle bundle = new Bundle();
            bundle.putInt("type", type);
            bundle.putString("paramsJson", GsonUtils.toJson(map));
            if (type == type_certificate || type == type_immune || type == type_examined) {
                openActivity(DogCertificateEditDogActivity.class, bundle);

            } else if (type == type_adoption) {
                openActivity(DogAdoptionSubmitActivity.class);


            }

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
    private final int request_City = 1100;

    /**
     * 个人证件 上传身份证人像面
     *
     * @param view
     */
    public void onClickIDCardFront(View view) {
        if (type == type_details) {
            return;
        }
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
        if (type == type_details) {
            return;
        }
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
        if (type == type_details) {
            return;
        }
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
        if (type == type_details) {
            return;
        }
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
        if (type == type_details) {
            return;
        }
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
                case request_City:
                    if (data != null) {
                        String cityName = data.getStringExtra("cityName");
                        address = cityName;
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
                                        if (requestCode == request_IDCardFront) {
                                            personalIDCardFront = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.IDCardFrontView, 6);

                                        } else if (requestCode == request_IDCardBack) {
                                            personalIDCardBack = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.IDCardBackView, 6);

                                        } else if (requestCode == request_LegalPersonIDCardFront) {
                                            legalPersonIDCardFront = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.legalPersonIDCardFrontView, 6);

                                        } else if (requestCode == request_LegalPersonIDCardBack) {
                                            legalPersonIDCardBack = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.legalPersonIDCardBackView, 6);

                                        } else if (requestCode == request_BusinessLicense) {
                                            businessLicense = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.businessLicenseView, 6);

                                        } else if (requestCode == request_OldManOrDisabledCertificate) {
                                            oldManOrDisabledCertificate = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.oldManOrDisabledCertificateView, 6);

                                        } else if (requestCode == request_HouseProprietaryCertificate) {
                                            personaHouseProprietaryCertificate = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.houseProprietaryCertificateView, 6);

                                        } else if (requestCode == request_ManagementSystem) {
                                            legalManagementSystem = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.managementSystemView, 6);

                                        } else if (requestCode == request_Facility1) {
                                            legalFacility1 = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.facility1View, 6);

                                        } else if (requestCode == request_Facility2) {
                                            legalFacility2 = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogOwnerActivity.this, file.getAbsolutePath(), binding.facility2View, 6);

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