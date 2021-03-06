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

    public static final int type_userInfo = 0;//????????????
    public static final int type_certificate = 1;//????????????
    public static final int type_immune = 2;//???????????????
    public static final int type_examined = 3;//????????????
    private int type = 0;
    private int addressId = 0;
    private Map<String, String> paramsMap = new HashMap<>();
    private Dog dog = new Dog();

    private List<Dog> dogList = new ArrayList<>();
    private List<String> dogColors = Arrays.asList("?????????", "??????", "??????");

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
            binding.titleView.binding.itemTitle.setText("????????????");

        } else if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("????????????");
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("???????????????");
            binding.secondStepView.setText("???????????????");
            binding.thirdStepView.setText("???????????????");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("???????????????");
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("???????????????");
            binding.secondStepView.setText("???????????????");
            binding.thirdStepView.setText("???????????????");

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("????????????");
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("???????????????");
            binding.secondStepView.setText("???????????????");
            binding.thirdStepView.setText("???????????????");
            paperLicence = getIntent().getStringExtra("paperLicence");
            paperImmuneLicence = getIntent().getStringExtra("paperImmuneLicence");
            Log.i(TAG, "onCreate: paperLicence = " + paperLicence);
            Log.i(TAG, "onCreate: paperImmuneLicence = " + paperImmuneLicence);

        }

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogList.size() == 0) {
                    ToastUtils.showShort(getApplicationContext(), "????????????");
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
        //????????????
        binding.petTypeView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dog.setDogType("??????");
//                binding.petTypeView.binding.itemContent.setText(dog.getDogType());

                if (CommonUtil.isBlank(centerFace)) {
                    ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                    return;
                }
                petTypeUrl(centerFace);
            }
        });
        //????????????
        binding.createPetArchivesView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dog.setNoseprint("23325059-b2c1-11eb-1Vu7hqwN6afc");
//                binding.createPetArchivesView.binding.itemContent.setText("???????????????");

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
     * ??????????????????
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
                        ToastUtils.showShort(getApplicationContext(), "????????????????????????");
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
                                ToastUtils.showShort(getApplicationContext(), "????????????????????????");
                            }

                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }

                    }
                });
    }

    /**
     * ????????????
     */
    private void getDogAge() {
//        List<String> optionsItems = new ArrayList<>();
//        for (int i = 1; i <= 100; i++) {
//            optionsItems.add(i + "??????");
//        }
//        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
//            @Override
//            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                dog.setDogAge(options1 + 1);
//                binding.dogAgeView.binding.itemContent.setText(optionsItems.get(options1));
//            }
//        })
//                .setTitleText("??????????????????")
//                .setContentTextSize(16)
//                .setCancelText("??????")//??????????????????
//                .setSubmitText("??????")//??????????????????
//                .setSubCalSize(16)//??????????????????
//                .setContentTextSize(18)//??????????????????
//                .setTitleSize(18)//??????????????????
//                .setTitleText("????????????")//????????????
//                .setOutSideCancelable(false)//???????????????????????????????????????????????????????????????
//                .setTitleColor(Color.BLACK)//??????????????????
//                .setSubmitColor(Color.BLACK)//????????????????????????
//                .setCancelColor(Color.BLACK)//????????????????????????
//                .setTitleBgColor(getResources().getColor(R.color.white))//?????????????????? Night mode
//                .setBgColor(getResources().getColor(R.color.white))//?????????????????? Night mode
//                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
//                .isDialog(false)//??????????????????????????????
//                .setLineSpacingMultiplier(2.0f)//??????????????????,???????????????1.0-4.0f??????
//                .build();
//
//        pickerView.setPicker(optionsItems);
//        pickerView.show();

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        //?????????????????? ??????????????????????????????
        SimpleDateFormat yearSimpleDateFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthSimpleDateFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("dd");
        Date date = new Date(System.currentTimeMillis());
        startDate.set(2010, 11, 31);
        endDate.set(Integer.parseInt(yearSimpleDateFormat.format(date)), Integer.parseInt(monthSimpleDateFormat.format(date)) - 1, Integer.parseInt(dateSimpleDateFormat.format(date)));

        TimePickerView timePickerView = new TimePickerBuilder(DogCertificateEditDogActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//??????????????????
                SimpleDateFormat sdr = new SimpleDateFormat("yyyyMMdd");
                binding.dogAgeView.binding.itemContent.setText(sdr.format(date));
                dog.setDogAge(sdr.format(date));

            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// ??????????????????
                .setCancelText("??????")//??????????????????
                .setSubmitText("??????")//??????????????????
                .setSubCalSize(16)//??????????????????
                .setContentTextSize(18)//??????????????????
                .setTitleSize(18)//??????????????????
                .setTitleText("????????????")//????????????
                .setOutSideCancelable(false)//???????????????????????????????????????????????????????????????
                .isCyclic(false)//??????????????????
                .setTitleColor(Color.BLACK)//??????????????????
                .setSubmitColor(Color.BLACK)//????????????????????????
                .setCancelColor(Color.BLACK)//????????????????????????
                .setTitleBgColor(getResources().getColor(R.color.white))//?????????????????? Night mode
                .setBgColor(getResources().getColor(R.color.white))//?????????????????? Night mode
                .setDate(selectedDate)// ?????????????????????????????????????????????*/
                .setRangDate(startDate, endDate)//???????????????????????????
                .setLabel("", "", "", "???", "???", "???")//?????????????????????????????????
                .isCenterLabel(false) //?????????????????????????????????label?????????false?????????item???????????????label???
                .isDialog(false)//??????????????????????????????
                .setLineSpacingMultiplier(2.6f)//??????????????????,???????????????1.0-4.0f??????
                .build();
        timePickerView.show();
    }

    /**
     * ??????????????????
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
                    binding.petTypeView.binding.itemContent.setText(dog.getDogType() + "???????????????");
                } else {
                    binding.petTypeView.binding.itemContent.setText(dog.getDogType() + "??????????????????");
                }
            }
        });
    }

    /**
     * ??????????????????????????????????????????
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
                    newDog.setDogType("???????????????");
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
     * ??????????????????
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
                    newDog.setDogType("???????????????");
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
     * ?????? ????????????????????????
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
                    ToastUtils.showShort(getApplicationContext(), "??????????????????");
                }
            }
        });
    }

    /**
     * ?????? ??????????????????????????????
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
                    ToastUtils.showShort(getApplicationContext(), "??????????????????");
                }
            }
        });
    }

    private void intiView(Dog data, String adoptNum) {
        dog = data;

        //???????????? 0 ?????? 1 ??????
        dog.setAdoptStatus(CommonUtil.isBlank(adoptNum) ? 0 : 1);
        dog.setAdoptNum(adoptNum);

        //????????????
        binding.dogNameView.binding.itemEdit.setText(dog.getDogName());
        //????????????
        binding.dogColorView.binding.itemContent.setText(dog.getDogColor());
        //????????????
        binding.dogAgeView.binding.itemContent.setText(CommonUtil.getDogAge(dog.getDogAge()));

        //????????????;0:????????? 1?????????
        if (dog.getDogGender() == 1) {
            binding.radioButtonMale.setChecked(true);
        }

        //????????????;0?????? 1??????
        if (dog.getSterilization() == 1) {
            binding.radioButtonSterilization1.setChecked(true);
            binding.sterilizationProveContainer.setVisibility(View.VISIBLE);
            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, dog.getSterilizationProve(), binding.testifyView, 6);
        }

        if (!CommonUtil.isBlank(dog.getDogType())) {
            binding.petTypeView.binding.itemContent.setText(dog.getDogType());
        }

        binding.petTypeView.binding.itemContent.setText(dog.getDogType());//????????????
        if (!CommonUtil.isBlank(dog.getNoseprint()))
            binding.createPetArchivesView.binding.itemContent.setText("???????????????");//????????????

        try {
            //????????????
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
     * ????????????
     * dogColor
     * string
     * ????????????
     * dogGender
     * integer
     * ????????????;0:????????? 1?????????
     * sterilization
     * integer
     * ????????????;0?????? 1??????
     * sterilizationProve
     * string
     * ????????????
     * dogAge
     * integer
     * ????????????;????????????
     * dogPhoto
     * string
     * ?????????????????????????????????????????????
     * dogType
     * string
     * ????????????
     * noseprint
     * string
     * ????????????
     *
     * @param view
     */
    public void onClickConfirm(View view) {

        Map<String, String> map = new HashMap<>();

        String dogName = binding.dogNameView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogName)) {
            ToastUtils.showShort(getApplicationContext(), "??????????????????");
            return;
        } else {
            dog.setDogName(dogName);
        }

        if (CommonUtil.isBlank(dog.getDogColor())) {
            ToastUtils.showShort(getApplicationContext(), "??????????????????");
            return;
        }

        //0-?????? 1-??????
        int sexCheckedRadioButtonId = binding.radioGroupSex.getCheckedRadioButtonId();
        if (sexCheckedRadioButtonId == R.id.radioButtonMale) {//??????
            dog.setDogGender(1);
        } else if (sexCheckedRadioButtonId == R.id.radioButtonFemale) {//??????
            dog.setDogGender(0);
        }

        //????????????;0?????? 1??????
        int sterilizationCheckedRadioButtonId = binding.radioGroupSterilization.getCheckedRadioButtonId();
        if (sterilizationCheckedRadioButtonId == R.id.radioButtonSterilization0) {//?????????
            dog.setSterilization(0);
        } else if (sterilizationCheckedRadioButtonId == R.id.radioButtonSterilization1) {//?????????
            dog.setSterilization(1);
            if (CommonUtil.isBlank(dog.getSterilizationProve())) {
                ToastUtils.showShort(getApplicationContext(), "?????????????????????");
                return;
            }

        }

        if (CommonUtil.isBlank(dog.getDogAge())) {
            ToastUtils.showShort(getApplicationContext(), "??????????????????");
            return;
        }

        if (CommonUtil.isBlank(leftFace)) {
            ToastUtils.showShort(getApplicationContext(), "????????????????????????");
            return;
        }
        if (CommonUtil.isBlank(centerFace)) {
            ToastUtils.showShort(getApplicationContext(), "?????????????????????");
            return;
        }
        if (CommonUtil.isBlank(rightFace)) {
            ToastUtils.showShort(getApplicationContext(), "????????????????????????");
            return;
        }

        if (CommonUtil.isBlank(dog.getDogType())) {
            ToastUtils.showShort(getApplicationContext(), "??????????????????");
            return;
        }
        if (CommonUtil.isBlank(dog.getNoseprint())) {
            ToastUtils.showShort(getApplicationContext(), "??????????????????");
            return;
        }

        map.put("dogName", dog.getDogName());//????????????
        map.put("dogColor", dog.getDogColor());//????????????
        map.put("dogGender", String.valueOf(dog.getDogGender()));//????????????;0:????????? 1?????????
        map.put("sterilization", String.valueOf(dog.getSterilization()));//????????????;0?????? 1??????
        map.put("sterilizationProve", dog.getSterilizationProve());//????????????
        map.put("dogAge", String.valueOf(dog.getDogAge()));//????????????;????????????
        map.put("dogPhoto", GsonUtils.toJson(Arrays.asList(leftFace, centerFace, rightFace)));//?????????????????????????????????????????????
        map.put("dogType", dog.getDogType());//????????????
        map.put("noseprint", dog.getNoseprint());//????????????

        if (dog.getId() > 0)
            map.put("id", dog.getId() + "");//

        map.put("adoptStatus", dog.getAdoptStatus() + "");//???????????? 0 ?????? 1 ??????
        if (dog.getAdoptNum() != null)
            map.put("adoptNum", dog.getAdoptNum());//??????????????????

        if (type == type_examined) {
            map.put("paperLicence", paperLicence);//??????????????????
            map.put("paperImmuneLicence", paperImmuneLicence);//????????????
            map.put("addressId", addressId + "");//??????id
            map.put("annualStatus", "1");//?????? 0 ?????? 1 ??????
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
     * ??????????????????
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
     * ?????????????????????
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
     * ??????????????????
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
     * ??????????????????
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
                        binding.createPetArchivesView.binding.itemContent.setText("???????????????");
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
                                .load(path)// ??????????????????????????????
                                .ignoreBy(500)// ??????????????????????????????
                                .setTargetDir(FileUtils.getMediaPath())// ?????????????????????????????????
                                .setCompressListener(new OnCompressListener() { //????????????
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
                                }).launch();//????????????
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * ????????? ????????????
     *
     * @param requestCode
     * @param filePath
     */
    private void uploadFile(int requestCode, String filePath) {
        LoadingManager.showLoadingDialog(DogCertificateEditDogActivity.this, "?????????...");
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
                // ????????????????????????
                Log.i(TAG, "uploadFile: AverageSpeed:" + status.getAverageSpeed());
                // ???????????????????????????
                Log.i(TAG, "uploadFile: TransferPercentage:" + status.getTransferPercentage());
            }
        });
        //?????????1MB????????????????????????
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