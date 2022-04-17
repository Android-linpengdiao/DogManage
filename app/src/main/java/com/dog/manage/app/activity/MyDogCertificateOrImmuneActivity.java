package com.dog.manage.app.activity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.adapter.MainPagerAdapter;
import com.dog.manage.app.databinding.ActivityMyDogCertificateOrImmuneBinding;
import com.dog.manage.app.fragment.MyDogCertificateOrImmuneFragment;

/**
 * 我的犬证、免疫证明
 */
public class MyDogCertificateOrImmuneActivity extends BaseActivity {

    private ActivityMyDogCertificateOrImmuneBinding binding;
    public static final int type_certificate = 1;//犬证
    public static final int type_immune = 2;//免疫证

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_my_dog_certificate_or_immune);
        addActivity(this);

        int type = getIntent().getIntExtra("type", 0);
        binding.titleView.binding.itemTitle.setText(type == type_certificate ? "我的犬证" : "免疫证明");

        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        mainPagerAdapter.addFragment("萨摩耶", MyDogCertificateOrImmuneFragment.getInstance(type, 0));
        mainPagerAdapter.addFragment("柯基", MyDogCertificateOrImmuneFragment.getInstance(type, 1));
        mainPagerAdapter.addFragment("泰迪", MyDogCertificateOrImmuneFragment.getInstance(type, 2));
        mainPagerAdapter.addFragment("哈士奇", MyDogCertificateOrImmuneFragment.getInstance(type, 3));

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