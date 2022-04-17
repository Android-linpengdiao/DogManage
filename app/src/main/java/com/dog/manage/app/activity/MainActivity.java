package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.FrameItemAdapter;
import com.dog.manage.app.databinding.ActivityMainBinding;
import com.dog.manage.app.utils.GlideImageLoader;
import com.xw.banner.BannerConfig;
import com.xw.banner.Transformer;
import com.xw.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_main);
        addActivity(this);

        intBanner();


        binding.frameItemRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.frameItemRecyclerView.setNestedScrollingEnabled(false);
        FrameItemAdapter frameItemAdapter = new FrameItemAdapter(getApplicationContext());
        binding.frameItemRecyclerView.setAdapter(frameItemAdapter);
        frameItemAdapter.refreshData(Arrays.asList(
                "犬证办理",
                "免疫证办理",
                "犬证年审",
                "犬只过户",
                "犬只领养",
                "犬只注销",
                "信息変更",
                "办理流程",
                "政策法规"));
        frameItemAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                int position = (int) object;
                if (position == 7) {
                    openActivity(MainDogManageWorkflowActivity.class);
                } else if (position == 8) {
                    openActivity(PoliciesActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", position);
                    bundle.putString("title", frameItemAdapter.getList().get(position));
                    openActivity(DogManageWorkflowActivity.class, bundle);
                }

            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

    }

    private void intBanner() {
        binding.mainBanner.setImageLoader(new GlideImageLoader(0));
        binding.mainBanner.isAutoPlay(true);
        binding.mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        binding.mainBanner.setBannerAnimation(Transformer.Default);
        binding.mainBanner.setIndicatorGravity(BannerConfig.CENTER);
        binding.mainBanner.setDelayTime(5000);
        binding.mainBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

            }
        });

        binding.banner.setImageLoader(new GlideImageLoader(8));
        binding.banner.isAutoPlay(true);
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
        List<String> images = new ArrayList<>();
        images.add("https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg");
        images.add("https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg");
        images.add("https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg");

        binding.mainBanner.setImages(images).start();
        binding.banner.setImages(images).start();
    }

    public void onClickMessage(View view) {
        openActivity(MessageActivity.class);
    }

    public void onClickUser(View view) {
        openActivity(UserHomeActivity.class);

    }
}