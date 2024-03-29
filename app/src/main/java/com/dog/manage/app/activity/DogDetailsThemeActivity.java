package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogDetailsThemeBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.utils.GlideImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
import com.xw.banner.BannerConfig;
import com.xw.banner.Transformer;
import com.xw.banner.listener.OnBannerListener;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class DogDetailsThemeActivity extends BaseActivity {

    private ActivityDogDetailsThemeBinding binding;
    private int leaveId;
    private DogDetail dogDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_details_theme);
        addActivity(this);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.FILL_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(layoutParams);

        leaveId = getIntent().getIntExtra("leaveId", 0);

        intBanner();
        getDogById();
    }

    private void intBanner() {
//        binding.banner.setImageLoader(new GlideImageLoader(0));
//        binding.banner.isAutoPlay(false);
//        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//        binding.banner.setBannerAnimation(Transformer.Default);
//        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
//        binding.banner.setDelayTime(5000);
//        binding.banner.setOnBannerListener(new OnBannerListener() {
//            @Override
//            public void OnBannerClick(int position) {
//
//            }
//        });
    }

    /**
     * 犬证 获取犬只详情信息
     */
    private void getDogById() {
        SendRequest.getLeaveDogDetail(leaveId, new GenericsCallback<ResultClient<DogDetail>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
//                LoadingManager.showLoadingDialog(DogDetailsThemeActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
//                LoadingManager.hideLoadingDialog(DogDetailsThemeActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    dogDetail = response.getData();
                    binding.dogNameView.setText((!CommonUtil.isBlank(dogDetail.getDogName()) ? dogDetail.getDogName() : "--")
                            + "|"
                            + (!CommonUtil.isBlank(dogDetail.getDogColor()) ? dogDetail.getDogColor() : "--")
                            + "|"
                            + CommonUtil.getDogAge(dogDetail.getDogAge()));
                    binding.leaveCenterView.setText(dogDetail.getLeaveCenter());
                    binding.centerAddressView.setText(dogDetail.getCenterAddress());
                    binding.phoneView.setText(dogDetail.getPhone());
                    binding.idNumView.setText("犬只编号：" + dogDetail.getDogLeaveNum());
                    binding.dogGenderView.setText("犬只性别：" + (dogDetail.getDogGender() == 0 ? "雌性" : "雄性"));
                    binding.dogShapeView.setText("犬只体型：" + (dogDetail.getDogShape() == 0 ? "小型" : "中型"));

                    binding.immuneStatus.setText("免疫情况：已免疫");
                    binding.sterilizationView.setText("绝育情况：" + "已绝育");

//                    if (dogDetail.getImmuneStatus() == 1) {
//                        if (!CommonUtil.isBlank(dogDetail.getImmuneExprie())) {
//                            binding.immuneStatus.setText("免疫情况：已免疫" + "，" + dogDetail.getImmuneExprie() + "到期");
//                        }else {
//                            binding.immuneStatus.setText("免疫情况：已免疫");
//                        }
//                    } else {
//                        binding.immuneStatus.setText("免疫情况：未免疫");
//                    }
//                    binding.sterilizationView.setText("绝育情况：" + (dogDetail.getSterilization() == 0 ? "未绝育" : "已绝育"));

                    try {
                        List<String> imageList = new Gson().fromJson(dogDetail.getDogPhoto(), new TypeToken<List<String>>() {
                        }.getType());
                        if (imageList != null && imageList.size() > 0)
                            GlideLoader.LoderRoundedImage(DogDetailsThemeActivity.this, imageList.get(0), binding.coverView, 15);
//                        binding.banner.setImages(imageList).start();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), "获取信息失败");
                }
            }
        });
    }

    public void onClickCancel(View view) {
        finish();
    }

    public void onClickConfirm(View view) {
        if (dogDetail == null) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("leaveId", leaveId);
        bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_adoption);
        openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

    }
}