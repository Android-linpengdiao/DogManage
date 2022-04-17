package com.dog.manage.app.activity.record;


import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.adapter.MainPagerAdapter;
import com.dog.manage.app.databinding.ActivityAdoptionRecordBinding;
import com.dog.manage.app.fragment.AdoptionRecordFragment;

/**
 * 领养记录
 */
public class AdoptionRecordActivity extends BaseActivity {

    private ActivityAdoptionRecordBinding binding;

    public static final int type_certificate = 1;//犬证办理记录
    public static final int type_immune = 2;//免疫证办理记录
    public static final int type_transfer = 3;//处罚记录
    public static final int type_adoption = 4;//领养记录
    public static final int type_logout = 5;//注销记录
    public static final int type_punish = 6;//处罚记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_adoption_record);
        addActivity(this);

        int type = getIntent().getIntExtra("type", 0);


        if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理记录");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理记录");

        } else if (type == type_transfer) {
            binding.titleView.binding.itemTitle.setText("处罚记录");

        } else if (type == type_adoption) {
            binding.titleView.binding.itemTitle.setText("领养记录");

        } else if (type == type_logout) {
            binding.titleView.binding.itemTitle.setText("注销记录");

        } else if (type == type_punish) {
            binding.titleView.binding.itemTitle.setText("处罚记录详情");

        }

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment("全部", AdoptionRecordFragment.getInstance(type, 0));
        mainPagerAdapter.addFragment("审核中", AdoptionRecordFragment.getInstance(type, 1));
        mainPagerAdapter.addFragment("审核通过", AdoptionRecordFragment.getInstance(type, 2));
        mainPagerAdapter.addFragment("审核拒绝", AdoptionRecordFragment.getInstance(type, 3));
        mainPagerAdapter.addFragment("已办结", AdoptionRecordFragment.getInstance(type, 3));
        mainPagerAdapter.addFragment("即将开始", AdoptionRecordFragment.getInstance(type, 3));

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