package com.dog.manage.app.activity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.GsonUtils;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.MainPagerAdapter;
import com.dog.manage.app.databinding.ActivityMyDogCertificateOrImmuneBinding;
import com.dog.manage.app.fragment.MyDogCertificateOrImmuneFragment;
import com.dog.manage.app.model.Dog;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 我的犬证、免疫证明
 */
public class MyDogCertificateOrImmuneActivity extends BaseActivity {

    private ActivityMyDogCertificateOrImmuneBinding binding;
    public static final int type_certificate = 1;//犬证
    public static final int type_immune = 2;//免疫证
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_my_dog_certificate_or_immune);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);
        binding.titleView.binding.itemTitle.setText(type == type_certificate ? "我的犬证" : "免疫证明");

        if (type == type_certificate) {
            getMyLicenceList();

        } else if (type == type_immune) {
            getDogImmuneList();

        }


        initTabLayout();

    }

    private void initTabLayout() {
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

    /**
     * 我的-我的犬证
     */
    private void getMyLicenceList() {
        SendRequest.getMyLicenceList(new GenericsCallback<ResultClient<List<BaseData>>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(MyDogCertificateOrImmuneActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(MyDogCertificateOrImmuneActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<List<BaseData>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    /**
     * 获取个人犬只免疫列表
     */
    private void getDogImmuneList() {
        SendRequest.getDogImmuneList(new GenericsCallback<ResultClient<List<Dog>>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(MyDogCertificateOrImmuneActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(MyDogCertificateOrImmuneActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<List<Dog>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }
}