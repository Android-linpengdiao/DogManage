package com.dog.manage.app.activity.record;


import android.graphics.Color;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.adapter.MainPagerAdapter;
import com.dog.manage.app.databinding.ActivityRecordBinding;
import com.dog.manage.app.fragment.RecordFragment;
import com.dog.manage.app.tab.TopTabLayout;

/**
 * 记录
 */
public class RecordActivity extends BaseActivity {

    private ActivityRecordBinding binding;

    public static final int type_certificate = 1;//犬证办理记录
    public static final int type_immune = 2;//免疫证办理记录
    public static final int type_transfer = 3;//过户记录
    public static final int type_adoption = 4;//领养记录
    public static final int type_logout = 5;//注销记录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_record);
        addActivity(this);

        int type = getIntent().getIntExtra("type", 0);

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment("全部", RecordFragment.getInstance(type, 0));

        if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理记录");
            mainPagerAdapter.addFragment("审核中", RecordFragment.getInstance(type, 1));
            mainPagerAdapter.addFragment("审核通过", RecordFragment.getInstance(type, 2));
            mainPagerAdapter.addFragment("审核拒绝", RecordFragment.getInstance(type, 3));
            mainPagerAdapter.addFragment("已办结", RecordFragment.getInstance(type, 3));

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理记录");
            mainPagerAdapter.addFragment("已预约", RecordFragment.getInstance(type, 1));
            mainPagerAdapter.addFragment("已办结", RecordFragment.getInstance(type, 2));
            mainPagerAdapter.addFragment("即将过期", RecordFragment.getInstance(type, 3));
            mainPagerAdapter.addFragment("已过期", RecordFragment.getInstance(type, 3));

        } else if (type == type_transfer) {
            binding.titleView.binding.itemTitle.setText("过户记录");
            binding.tabLayout.setTabMode(TopTabLayout.MODE_FIXED);
            mainPagerAdapter.addFragment("审核中", RecordFragment.getInstance(type, 1));
            mainPagerAdapter.addFragment("过户成功", RecordFragment.getInstance(type, 2));
            mainPagerAdapter.addFragment("过户失败", RecordFragment.getInstance(type, 3));

        } else if (type == type_adoption) {
            binding.titleView.binding.itemTitle.setText("领养记录");
            binding.tabLayout.changedTableUI(R.drawable.tab_indicator_l, Color.parseColor("#4DACEF"), Color.parseColor("#999999"));
            mainPagerAdapter.addFragment("待审核", RecordFragment.getInstance(type, 1));
            mainPagerAdapter.addFragment("待支付", RecordFragment.getInstance(type, 2));
            mainPagerAdapter.addFragment("完成领养", RecordFragment.getInstance(type, 3));
            mainPagerAdapter.addFragment("审核拒绝", RecordFragment.getInstance(type, 3));

        } else if (type == type_logout) {
            binding.titleView.binding.itemTitle.setText("注销记录");
            binding.tabLayout.setTabMode(TopTabLayout.MODE_FIXED);
            mainPagerAdapter.addFragment("审核中", RecordFragment.getInstance(type, 1));
            mainPagerAdapter.addFragment("审核通过", RecordFragment.getInstance(type, 2));
            mainPagerAdapter.addFragment("审核拒绝", RecordFragment.getInstance(type, 3));

        }

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