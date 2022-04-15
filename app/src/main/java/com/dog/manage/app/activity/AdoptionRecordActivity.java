package com.dog.manage.app.activity;


import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.dog.manage.app.R;
import com.dog.manage.app.adapter.MainPagerAdapter;
import com.dog.manage.app.databinding.ActivityAdoptionRecordBinding;
import com.dog.manage.app.fragment.AdoptionRecordFragment;

public class AdoptionRecordActivity extends BaseActivity {

    private ActivityAdoptionRecordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_adoption_record);
        addActivity(this);


        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment("内容", AdoptionRecordFragment.getInstance(0));
        mainPagerAdapter.addFragment("教材", AdoptionRecordFragment.getInstance(1));
        mainPagerAdapter.addFragment("乐曲", AdoptionRecordFragment.getInstance(2));
        mainPagerAdapter.addFragment("测评", AdoptionRecordFragment.getInstance(3));

        binding.viewPager.setAdapter(mainPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(4);
        binding.viewPager.setCurrentItem(0);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}