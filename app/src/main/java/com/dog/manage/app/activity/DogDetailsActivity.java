package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.base.manager.LoadingManager;
import com.base.utils.GlideLoader;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityDogDetailsBinding;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.model.DogUser;
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

public class DogDetailsActivity extends BaseActivity {

    private ActivityDogDetailsBinding binding;
    private int dogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_details);
        addActivity(this);

        dogId = getIntent().getIntExtra("dogId", 0);

        intBanner();
        getDogById();
    }

    private void intBanner() {
        binding.banner.setImageLoader(new GlideImageLoader(0));
        binding.banner.isAutoPlay(false);
        binding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.banner.setBannerAnimation(Transformer.Default);
        binding.banner.setIndicatorGravity(BannerConfig.CENTER);
        binding.banner.setDelayTime(5000);
        binding.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                openActivity(AdvertiseActivity.class);
            }
        });
    }

    /**
     * 犬证 获取犬只详情信息
     */
    private void getDogById() {
        SendRequest.getDogById(dogId, new GenericsCallback<ResultClient<DogDetail>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(DogDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(DogDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    DogDetail dogDetail = response.getData();
                    binding.dogNameView.setText(dogDetail.getDogName() + "|" + dogDetail.getDogColor() + "|" + dogDetail.getDogAge() + "岁3个月");
                    binding.idNumView.setText("犬只编号："+dogDetail.getIdNum());
                    binding.dogGenderView.setText("犬只性别："+(dogDetail.getDogGender() == 0 ? "雌性" : "雄性"));
                    binding.sterilizationView.setText("绝育情况："+(dogDetail.getSterilization() == 0 ? "未绝育" : "已绝育"));

                    try {
                        List<String> imageList = new Gson().fromJson(dogDetail.getDogPhoto(), new TypeToken<List<String>>() {
                        }.getType());
                        binding.banner.setImages(imageList).start();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        });
    }
}