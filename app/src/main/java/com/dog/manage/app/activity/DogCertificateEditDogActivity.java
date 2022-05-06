package com.dog.manage.app.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.GlideLoader;
import com.base.utils.GsonUtils;
import com.base.utils.PermissionUtils;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogCertificateEditDogBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaSelectActivity;
import com.dog.manage.app.media.MediaUtils;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
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

public class DogCertificateEditDogActivity extends BaseActivity {

    private ActivityDogCertificateEditDogBinding binding;

    public static final int type_userInfo = 0;//我的信息
    public static final int type_certificate = 1;//犬证办理
    public static final int type_immune = 2;//免疫证办理
    public static final int type_examined = 3;//犬证年审
    private int type = 0;
    private int addressId = 0;
    private Map<String, String> paramsMap = new HashMap<>();

    private List<String> dogList = Arrays.asList("添加新犬只", "萨摩耶", "柯基", "泰迪", "哈士奇");

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
            binding.titleView.binding.itemTitle.setText("犬证办理");
            binding.secondStepView.setSelected(true);
            binding.firstStepView.setText("①犬主信息");
            binding.secondStepView.setText("②犬只信息");
            binding.thirdStepView.setText("③提交审核");

        }

        binding.dogCertificateView.binding.itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickDogCertificate(DogCertificateEditDogActivity.this, dogList, dogList.indexOf(binding.dogCertificateView.binding.itemContent.getText().toString()), new OnClickListener() {
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

        getDogList();

    }

    private void getDogList() {
        SendRequest.getDogList(new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<List<Dog>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {


                }
            }
        });
    }

    private int dogCertificate = 0;//添加新犬只
    private String dogName = null;
    private String dogHair = "addadad";
    private int dogSex = 0;//0-雌性 1-雄性
    private int dogBear = 0;//0-未绝育 1-已绝育
    private String testify = null;
    private int dogAge = 1;
    private String leftFace = "https://img2.baidu.com/it/u=4114155986,1519346958&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=603";
    private String centerFace = "https://img2.baidu.com/it/u=4114155986,1519346958&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=603";
    private String rightFace = "https://img2.baidu.com/it/u=4114155986,1519346958&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=603";
    private String dogType = "柴犬";
    private String noseprint = "23325059-b2c1-11eb-1Vu7hqwN6";

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

        dogName = binding.dogNameView.binding.itemEdit.getText().toString();
        if (CommonUtil.isBlank(dogName)) {
            ToastUtils.showShort(getApplicationContext(), "请输入犬昵称");
            return;
        }

        if (CommonUtil.isBlank(dogHair)) {
            ToastUtils.showShort(getApplicationContext(), "选择犬只毛色");
            return;
        }

        int bearCheckedRadioButtonId = binding.radioGroupBear.getCheckedRadioButtonId();
        if (bearCheckedRadioButtonId == R.id.radioButton0) {//未绝育

        } else if (bearCheckedRadioButtonId == R.id.radioButton1) {//已绝育
            if (CommonUtil.isBlank(testify)) {
                ToastUtils.showShort(getApplicationContext(), "请上传绝育证明");
                return;
            }

        }

        if (dogAge <= 0) {
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

        map.put("dogName", dogName);//犬只姓名
        map.put("dogColor", dogHair);//犬只颜色
        map.put("dogGender", String.valueOf(dogSex));//犬只性别;0:雌性， 1：雄性
        map.put("sterilization", String.valueOf(dogBear));//是否绝育;0：否 1：是
        map.put("sterilizationProve", testify);//绝育证明
        map.put("dogAge", String.valueOf(dogAge));//犬只年龄;记录月份
        map.put("dogPhoto", GsonUtils.toJson(Arrays.asList(leftFace, centerFace,rightFace)));//犬只照片，多张图片以“，”分开
        map.put("dogType", dogType);//犬只品种
        map.put("noseprint", noseprint);//鼻纹信息

//        if (type == type_userInfo) {
//
//
//        } else if (type == type_certificate || type == type_examined) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("type", type);
//            bundle.putString("paramsJson", GsonUtils.toJson(map));
//            openActivity(DogCertificateEditSubmitActivity.class, bundle);
//
//        } else if (type == type_immune) {
//            Bundle bundle = new Bundle();
//            bundle.putString("paramsJson", GsonUtils.toJson(map));
//            openActivity(DogImmuneHospitalActivity.class, bundle);
//
//        }

        //{"msg":"操作成功","code":200,"data":{"dogId":3}}
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
                        bundle.putString("dogType", dogType);
                        openActivity(DogCertificateEditSubmitActivity.class, bundle);

                    } else if (type == type_immune) {
                        Bundle bundle = new Bundle();
                        bundle.putString("paramsJson", GsonUtils.toJson(map));
                        openActivity(DogImmuneHospitalActivity.class, bundle);

                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
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

    /**
     * 犬只品种
     *
     * @param view
     */
    public void onClickPetType(View view) {
        if (checkPermissions(PermissionUtils.CAMERA, 100)) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", CameraActivity.type_petType);
            openActivity(CameraActivity.class, bundle);
        }
    }

    /**
     * 鼻纹信息
     *
     * @param view
     */
    public void onClickCreatePetArchives(View view) {
        if (checkPermissions(PermissionUtils.CAMERA, 100)) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", CameraActivity.type_petArchives);
            openActivity(CameraActivity.class, bundle);
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
                                            testify = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.testifyView, 6);

                                        } else if (requestCode == request_LeftFace) {
                                            leftFace = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.leftFaceView, 6);

                                        } else if (requestCode == request_CenterFace) {
                                            centerFace = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.centerFaceView, 6);

                                        } else if (requestCode == request_RightFace) {
                                            rightFace = file.getAbsolutePath();
                                            GlideLoader.LoderImage(DogCertificateEditDogActivity.this, file.getAbsolutePath(), binding.rightFaceView, 6);

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