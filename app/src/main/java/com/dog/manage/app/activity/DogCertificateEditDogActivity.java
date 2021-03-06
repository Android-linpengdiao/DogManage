package com.dog.manage.app.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.dog.manage.app.Config;
import com.dog.manage.app.DogDialogManager;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.model.PetType;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class DogCertificateEditDogActivity extends BaseActivity {

    private ActivityDogCertificateEditDogBinding binding;

    public static final int type_userInfo = 0;//我的信息
    public static final int type_certificate = 1;//犬证办理
    public static final int type_immune = 2;//免疫证办理
    public static final int type_examined = 3;//犬证年审
    private int type = 0;
    private int addressId = 0;
    private Map<String, String> paramsMap = new HashMap<>();
    private Dog dog = new Dog();

    private List<Dog> dogList = new ArrayList<>();
    private List<String> dogColors = Arrays.asList("金黄色", "黑色", "棕色");

    private final int request_petType = 1100;
    private final int request_petArchive = 1200;

    private String paperLicence = null;
    private String paperImmuneLicence = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_certificate_edit_dog);
        addActivity(this);
        type = getIntent().getIntExtra("type", 0);
        addressId = getIntent().getIntExtra("addressId", 0);

        String paramsJson = getIntent().getStringExtra("paramsJson");
        if (!TextUtils.isEmpty(paramsJson)) {
            Gson gson = new Gson();
            paramsMap = gson.fromJson(paramsJson, new TypeToken<Map<String, Object>>() {
            }.getType());
        }

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

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("年审办理");
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交年审");
            paperLicence = getIntent().getStringExtra("paperLicence");
            paperImmuneLicence = getIntent().getStringExtra("paperImmuneLicence");
            Log.i(TAG, "onCreate: paperLicence = " + paperLicence);
            Log.i(TAG, "onCreate: paperImmuneLicence = " + paperImmuneLicence);

        }

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogList.size() == 0) {
                    ToastUtils.showShort(getApplicationContext(), "暂无犬只");
                    return;
                }
                onClickDogCertificate(DogCertificateEditDogActivity.this, type,
                        dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()),
                        new OnClickListener() {
                            @Override
                            public void onClick(View view, Object object) {
                                dog = (Dog) object;
                                binding.dogCertificateView.binding.itemContent.setText(dog.getDogType());
                                Log.i(TAG, "onClick: getId = " + dog.getId());
                                Log.i(TAG, "onClick: getDogId = " + dog.getDogId());
                                Log.i(TAG, "onClick: type = " + type);
                                if (type == type_certificate) {
                                    if (dog.getDogId() != 0) {
                                        if (dog.getType() != null && dog.getType() == 2) {
                                            getAdoptNumDetail(dog.getIdNum());
                                        } else {
                                            getDogById(dog.getDogId());
                                        }
                                    }

                                } else if (type == type_immune) {
                                    if (dog.getDogId() != 0)
                                        getDogById(dog.getDogId());

                                }
                            }

                            @Override
                            public void onLongClick(View view, Object object) {

                            }
                        });
            }
        });
        binding.dogColorView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DogDialogManager.getInstance().showDogColorDialog(DogCertificateEditDogActivity.this, dogColors,
                        new DogDialogManager.OnClickListener() {
                            @Override
                            public void onClick(View view, Object object) {
                                String color = (String) object;
                                dog.setDogColor(color);
                                binding.dogColorView.binding.itemContent.setText(dog.getDogColor());
                            }
                        });
            }
        });
        binding.radioGroupSterilization.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonSterilization1:
                        binding.sterilizationProveContainer.setVisibility(View.VISIBLE);

                        break;
                    case R.id.radioButtonSterilization0:
                        binding.sterilizationProveContainer.setVisibility(View.GONE);

                        break;
                    default:
                        break;
                }
            }
        });
        //犬只品种
        binding.petTypeView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dog.setDogType("金毛");
//                binding.petTypeView.binding.itemContent.setText(dog.getDogType());

                if (CommonUtil.isBlank(centerFace)) {
                    ToastUtils.showShort(getApplicationContext(), "请上传正面照片");
                    return;
                }
                petTypeUrl(centerFace);
            }
        });
        //鼻纹信息
        binding.createPetArchivesView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dog.setNoseprint("23325059-b2c1-11eb-1Vu7hqwN6afc");
//                binding.createPetArchivesView.binding.itemContent.setText("已完成采集");

                if (checkPermissions(PermissionUtils.CAMERA, 100)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", CameraActivity.type_petArchives);
                    openActivity(CameraActivity.class, bundle, request_petArchive);
                }
            }
        });

        binding.dogAgeView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDogAge();
            }
        });

        if (type == type_certificate) {
            getDogImmuneList();

        } else if (type == type_immune) {
            getDogLicenceList();

        }

        if (CommonUtil.isBlank(getUserInfo().getAccessToken())) {
            getAccessToken(this);
        }

    }

    /**
     * 宠物品种识别
     *
     * @param imageUrl
     */
    private void petTypeUrl(String imageUrl) {
        SendRequest.petTypeUrl(getUserInfo().getAccessToken(), imageUrl,
                new GenericsCallback<PetType>(new JsonGenericsSerializator()) {

                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        LoadingManager.showLoadingDialog(DogCertificateEditDogActivity.this);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        LoadingManager.hideLoadingDialog(DogCertificateEditDogActivity.this);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort(getApplicationContext(), "犬只品种获取失败");
                    }

                    @Override
                    public void onResponse(PetType response, int id) {
                        if (response.getStatus() == 200) {
                            if (response.getData() != null &&
                                    response.getData().getPet() != null &&
                                    response.getData().getPet().size() > 0 &&
                                    response.getData().getPet().get(0).getIdentification() != null &&
                                    response.getData().getPet().get(0).getIdentification().size() > 0) {
                                String english_name = response.getData().getPet().get(0).getIdentification().get(0).getEnglish_name();
                                String chinese_name = response.getData().getPet().get(0).getIdentification().get(0).getChinese_name();
                                Double confidence = response.getData().getPet().get(0).getIdentification().get(0).getConfidence();

                                dog.setDogType(chinese_name);
                                binding.petTypeView.binding.itemContent.setText(dog.getDogType());
                                verificationDog(dog.getDogType());

                            } else {
                                ToastUtils.showShort(getApplicationContext(), "犬只品种获取失败");
                            }

                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }

                    }
                });
    }

    /**
     * 犬只年龄
     */
    private void getDogAge() {
//        List<String> optionsItems = new ArrayList<>();
//        for (int i = 1; i <= 100; i++) {
//            optionsItems.add(i + "个月");
//        }
//        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                dog.setDogAge(options1 + 1);
//                binding.dogAgeView.binding.itemContent.setText(optionsItems.get(options1));
//            }
//        })
//                .setTitleText("选择犬只年龄")
//                .setContentTextSize(16)
//                .setCancelText("取消")//取消按钮文字
//                .setSubmitText("确定")//确认按钮文字
//                .setSubCalSize(16)//滚轮文字大小
//                .setContentTextSize(18)//滚轮文字大小
//                .setTitleSize(18)//标题文字大小
//                .setTitleText("选择时间")//标题文字
//                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
//                .setTitleColor(Color.BLACK)//标题文字颜色
//                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
//                .setCancelColor(Color.BLACK)//取消按钮文字颜色
//                .setTitleBgColor(getResources().getColor(R.color.white))//标题背景颜色 Night mode
//                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .isDialog(false)//是否显示为对话框样式
//                .setLineSpacingMultiplier(2.0f)//设置间距倍数,但是只能在1.0-4.0f之间
//                .build();
//
//        pickerView.setPicker(optionsItems);
//        pickerView.show();

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //正确设置方式 原因：注意事项有说明
        SimpleDateFormat yearSimpleDateFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthSimpleDateFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("dd");
        Date date = new Date(System.currentTimeMillis());
        startDate.set(2010, 11, 31);
        endDate.set(Integer.parseInt(yearSimpleDateFormat.format(date)), Integer.parseInt(monthSimpleDateFormat.format(date)) - 1, Integer.parseInt(dateSimpleDateFormat.format(date)));

        TimePickerView timePickerView = new TimePickerBuilder(DogCertificateEditDogActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd");
                binding.dogAgeView.binding.itemContent.setText(sdr.format(date));
                dog.setDogAge(sdr.format(date));

            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setSubCalSize(16)//滚轮文字大小
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择时间")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLACK)//确定按钮文字颜色
                .setCancelColor(Color.BLACK)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.white))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.white))//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("", "", "", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setLineSpacingMultiplier(2.6f)//设置间距倍数,但是只能在1.0-4.0f之间
                .build();
        timePickerView.show();
    }

    /**
     * 是否不可养犬
     *
     * @param dogType
     */
    private void verificationDog(String dogType) {
        SendRequest.verificationDog(dogType, new GenericsCallback<ResultClient<Boolean>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(ResultClient<Boolean> response, int id) {
                if (response.isSuccess() && response.getData()) {
                    binding.petTypeView.binding.itemContent.setText(dog.getDogType() + "（可养犬）");
                } else {
                    binding.petTypeView.binding.itemContent.setText(dog.getDogType() + "（不可养犬）");
                }
            }
        });
    }

    /**
     * 办理过疫苗的。没有办理狗证的
     */
    private void getDogImmuneList() {
        SendRequest.getDogImmuneList(new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(ResultClient<List<Dog>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    if (response.getData().size() > 0) {
                        dogList = response.getData();
                    }
                    Dog newDog = new Dog();
                    newDog.setDogId(0);
                    newDog.setDogType("添加新犬只");
                    dogList.add(newDog);
                    dog = dogList.get(0);
//                    binding.dogCertificateView.binding.itemContent.setText(dog.getDogType());
//                    if (dog.getDogId() != 0) {
//                        intiView();
//                    }

                }
            }
        });
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
                    }
                    Dog newDog = new Dog();
                    newDog.setDogId(0);
                    newDog.setDogType("添加新犬只");
                    dogList.add(newDog);
                    dog = dogList.get(0);
//                    binding.dogCertificateView.binding.itemContent.setText(dog.getDogType());
//                    if (dog.getDogId() != 0) {
//                        intiView();
//                    }

                }
            }
        });
    }

    /**
     * 犬证 获取犬只详情信息
     */
    private void getDogById(int dogId) {
        Log.i(TAG, "getDogById: " + dogId);
        SendRequest.getDogById(dogId, new GenericsCallback<ResultClient<Dog>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogCertificateEditDogActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogCertificateEditDogActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<Dog> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    intiView(response.getData(), null);
                } else {
                    ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                }
            }
        });
    }

    /**
     * 犬证 获取领养犬只详情信息
     */
    private void getAdoptNumDetail(String adoptNum) {
        Log.i(TAG, "getAdoptNumDetail: " + adoptNum);
        SendRequest.getAdoptNumDetail(adoptNum, new GenericsCallback<ResultClient<Dog>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogCertificateEditDogActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogCertificateEditDogActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<Dog> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    intiView(response.getData(), adoptNum);
                } else {
                    ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                }
            }
        });
    }

    private void intiView(Dog data, String adoptNum) {
        dog = data;

        //领养状态 0 正常 1 领养
        dog.setAdoptStatus(CommonUtil.isBlank(adoptNum) ? 0 : 1);
        dog.setAdoptNum(adoptNum);

        //犬只姓名
        binding.dogNameView.binding.itemEdit.setText(dog.getDogName());
        //犬只颜色
        binding.dogColorView.binding.itemContent.setText(dog.getDogColor());
        //犬只年龄
        binding.dogAgeView.binding.itemContent.setText(CommonUtil.getDogAge(dog.getDogAge()));

        //犬只性别;0:雌性， 1：雄性
        if (dog.getDogGender() == 1) {
            binding.radioButtonMale.setChecked(true);
        }

        //是否绝育;0：否 1：是
        if (dog.getSterilization() == 1) {
            binding.radioButtonSterilization1.setChecked(true);
            binding.sterilizationProveContainer.setVisibility(View.VISIBLE);
            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, dog.getSterilizationProve(), binding.testifyView, 6);
        }

        if (!CommonUtil.isBlank(dog.getDogType())) {
            binding.petTypeView.binding.itemContent.setText(dog.getDogType());
        }

        binding.petTypeView.binding.itemContent.setText(dog.getDogType());//犬只品种
        if (!CommonUtil.isBlank(dog.getNoseprint()))
            binding.createPetArchivesView.binding.itemContent.setText("已完成采集");//鼻纹信息

        try {
            //犬只照片
            List<String> idPhotos = new Gson().fromJson(dog.getDogPhoto(), new TypeToken<List<String>>() {
            }.getType());
            if (idPhotos.size() > 0) {
                GlideLoader.LoderImage(DogCertificateEditDogActivity.this,
                        idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.leftFaceView, 6);
                leftFace = idPhotos.get(0);
            }
            if (idPhotos.size() > 1) {
                GlideLoader.LoderImage(DogCertificateEditDogActivity.this,
                        idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.centerFaceView, 6);
                centerFace = idPhotos.get(1);
            }
            if (idPhotos.size() > 2) {
                GlideLoader.LoderImage(DogCertificateEditDogActivity.this,
                        idPhotos.size() > 2 ? idPhotos.get(2) : "", binding.rightFaceView, 6);
                rightFace = idPhotos.get(2);
            }
        } catch (Exception e) {
            e.getMessage();
        }

    }

    private String leftFace = null;
    private String centerFace = null;
    private String rightFace = null;

    /**
     * dogName
     * string
     * 犬只姓名
     * dogColor
     * string
     * 犬只颜色
     * dogGender
     * integer
     * 犬只性别;0:雌性， 1：雄性
     * sterilization
     * integer
     * 是否绝育;0：否 1：是
     * sterilizationProve
     * string
     * 绝育证明
     * dogAge
     * integer
     * 犬只年龄;记录月份
     * dogPhoto
     * string
     * 犬只照片，多张图片以“，”分开
     * dogType
     * string
     * 犬只品种
     * noseprint
     * string
     * 鼻纹信息
     *
     * @param view
     */
    public void onClickConfirm(View view) {

        Map<String, String> map = new HashMap<>();

        String dogName = binding.dogNameView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogName)) {
            ToastUtils.showShort(getApplicationContext(), "请输入犬昵称");
            return;
        } else {
            dog.setDogName(dogName);
        }

        if (CommonUtil.isBlank(dog.getDogColor())) {
            ToastUtils.showShort(getApplicationContext(), "选择犬只毛色");
            return;
        }

        //0-雌性 1-雄性
        int sexCheckedRadioButtonId = binding.radioGroupSex.getCheckedRadioButtonId();
        if (sexCheckedRadioButtonId == R.id.radioButtonMale) {//雄性
            dog.setDogGender(1);
        } else if (sexCheckedRadioButtonId == R.id.radioButtonFemale) {//雌性
            dog.setDogGender(0);
        }

        //是否绝育;0：否 1：是
        int sterilizationCheckedRadioButtonId = binding.radioGroupSterilization.getCheckedRadioButtonId();
        if (sterilizationCheckedRadioButtonId == R.id.radioButtonSterilization0) {//未绝育
            dog.setSterilization(0);
        } else if (sterilizationCheckedRadioButtonId == R.id.radioButtonSterilization1) {//已绝育
            dog.setSterilization(1);
            if (CommonUtil.isBlank(dog.getSterilizationProve())) {
                ToastUtils.showShort(getApplicationContext(), "请上传绝育证明");
                return;
            }

        }

        if (CommonUtil.isBlank(dog.getDogAge())) {
            ToastUtils.showShort(getApplicationContext(), "选择犬只年龄");
            return;
        }

        if (CommonUtil.isBlank(leftFace)) {
            ToastUtils.showShort(getApplicationContext(), "请上传左侧面照片");
            return;
        }
        if (CommonUtil.isBlank(centerFace)) {
            ToastUtils.showShort(getApplicationContext(), "请上传正面照片");
            return;
        }
        if (CommonUtil.isBlank(rightFace)) {
            ToastUtils.showShort(getApplicationContext(), "请上传右侧面照片");
            return;
        }

        if (CommonUtil.isBlank(dog.getDogType())) {
            ToastUtils.showShort(getApplicationContext(), "识别犬只品种");
            return;
        }
        if (CommonUtil.isBlank(dog.getNoseprint())) {
            ToastUtils.showShort(getApplicationContext(), "采集鼻纹信息");
            return;
        }

        map.put("dogName", dog.getDogName());//犬只姓名
        map.put("dogColor", dog.getDogColor());//犬只颜色
        map.put("dogGender", String.valueOf(dog.getDogGender()));//犬只性别;0:雌性， 1：雄性
        map.put("sterilization", String.valueOf(dog.getSterilization()));//是否绝育;0：否 1：是
        map.put("sterilizationProve", dog.getSterilizationProve());//绝育证明
        map.put("dogAge", String.valueOf(dog.getDogAge()));//犬只年龄;记录月份
        map.put("dogPhoto", GsonUtils.toJson(Arrays.asList(leftFace, centerFace, rightFace)));//犬只照片，多张图片以“，”分开
        map.put("dogType", dog.getDogType());//犬只品种
        map.put("noseprint", dog.getNoseprint());//鼻纹信息

        if (dog.getId() > 0)
            map.put("id", dog.getId() + "");//

        map.put("adoptStatus", dog.getAdoptStatus() + "");//领养状态 0 正常 1 领养
        if (dog.getAdoptNum() != null)
            map.put("adoptNum", dog.getAdoptNum());//领养犬只编号

        if (type == type_examined) {
            map.put("paperLicence", paperLicence);//犬证图片地址
            map.put("paperImmuneLicence", paperImmuneLicence);//免疫证明
            map.put("addressId", addressId + "");//地址id
            map.put("annualStatus", "1");//类型 0 首次 1 年审
            SendRequest.savaPaperDog(map, new GenericsCallback<ResultClient<Dog>>(new JsonGenericsSerializator()) {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(ResultClient<Dog> response, int id) {
                    if (response.isSuccess() && response.getData() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", DogCertificateExaminedSubmitActivity.type_zhiZhi);
                        bundle.putSerializable("dataBean", response.getData());
                        openActivity(DogCertificateExaminedSubmitActivity.class, bundle);

                    } else {
                        ToastUtils.showShort(getApplicationContext(), response.getMsg());
                    }
                }
            });
        } else {
            SendRequest.savaDog(map, new GenericsCallback<ResultClient<Dog>>(new JsonGenericsSerializator()) {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(ResultClient<Dog> response, int id) {
                    if (response.isSuccess() && response.getData() != null) {
                        if (type == type_userInfo) {


                        } else if (type == type_certificate || type == type_examined) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("type", type);
                            bundle.putInt("dogId", response.getData().getDogId());
                            bundle.putInt("addressId", addressId);
                            bundle.putString("dogType", dog.getDogType());
                            bundle.putString("immunePhoto", centerFace);
                            openActivity(DogCertificateEditSubmitActivity.class, bundle);

                        } else if (type == type_immune) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("dogId", response.getData().getDogId());
                            bundle.putInt("addressId", addressId);
                            openActivity(DogImmuneHospitalActivity.class, bundle);

                        }
                    } else {
                        ToastUtils.showShort(getApplicationContext(), response.getMsg());
                    }
                }
            });
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
                case request_petType:
                    if (data != null) {
                        String dogType = data.getStringExtra("dogType");
                        dog.setDogType(dogType);
                        binding.petTypeView.binding.itemContent.setText(dog.getDogType());
                        verificationDog(dog.getDogType());
                    }
                    break;
                case request_petArchive:
                    if (data != null) {
                        String petId = data.getStringExtra("petId");
                        dog.setNoseprint(petId);
                        binding.createPetArchivesView.binding.itemContent.setText("已完成采集");
                    }
                    break;
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

//                                        String url = "https://img0.baidu.com/it/u=3282676189,411395798&fm=253&fmt=auto&app=138&f=JPEG?w=564&h=500";
//                                        if (requestCode == request_Testify) {
//                                            dog.setSterilizationProve(url);
//                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, dog.getSterilizationProve(), binding.testifyView, 6);
//
//                                        } else if (requestCode == request_LeftFace) {
//                                            leftFace = url;
//                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, leftFace, binding.leftFaceView, 6);
//
//                                        } else if (requestCode == request_CenterFace) {
//                                            centerFace = url;
//                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, centerFace, binding.centerFaceView, 6);
//
//                                        } else if (requestCode == request_RightFace) {
//                                            rightFace = url;
//                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, rightFace, binding.rightFaceView, 6);
//
//                                        }
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
        LoadingManager.showLoadingDialog(DogCertificateEditDogActivity.this, "上传中...");
        Log.i(TAG, "uploadFile: filePath = " + filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        Log.i(TAG, "uploadFile: fileName = " + fileName);
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
        Log.i(TAG, "uploadFile: getObjectUrl = " + result.getObjectUrl());
        String url = "http://" + Config.huaweiBucketName + "." + Config.huaweiCloudEndPoint + "/" + fileName;
        Log.i(TAG, "uploadFile: url = " + url);
        LoadingManager.hideLoadingDialog(DogCertificateEditDogActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (requestCode == request_Testify) {
                    dog.setSterilizationProve(url);
                    GlideLoader.LoderImage(DogCertificateEditDogActivity.this, dog.getSterilizationProve(), binding.testifyView, 6);

                } else if (requestCode == request_LeftFace) {
                    leftFace = url;
                    GlideLoader.LoderImage(DogCertificateEditDogActivity.this, leftFace, binding.leftFaceView, 6);

                } else if (requestCode == request_CenterFace) {
                    centerFace = url;
                    GlideLoader.LoderImage(DogCertificateEditDogActivity.this, centerFace, binding.centerFaceView, 6);

                } else if (requestCode == request_RightFace) {
                    rightFace = url;
                    GlideLoader.LoderImage(DogCertificateEditDogActivity.this, rightFace, binding.rightFaceView, 6);

                }

            }
        });


    }
}