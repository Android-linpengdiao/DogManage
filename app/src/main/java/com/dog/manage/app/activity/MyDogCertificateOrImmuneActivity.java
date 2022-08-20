package com.dog.manage.app.activity;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.base.BaseData;
import com.base.manager.LoadingManager;
import com.base.utils.GsonUtils;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.CustomPagerAdapter;
import com.dog.manage.app.adapter.MainPagerAdapter;
import com.dog.manage.app.databinding.ActivityMyDogCertificateOrImmuneBinding;
import com.dog.manage.app.fragment.MyDogCertificateOrImmuneFragment;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.ImmuneBean;
import com.dog.manage.app.model.LicenceBean;
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

    }

    /**
     * 我的-我的犬证
     */
    private void getMyLicenceList() {
        SendRequest.getMyLicenceList(new GenericsCallback<ResultClient<List<LicenceBean>>>(new JsonGenericsSerializator()) {

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
            public void onResponse(ResultClient<List<LicenceBean>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    if (response.getData().size() > 0) {
                        initLicenceTabLayout(response.getData());
                    } else {
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.emptyView.setText("暂无犬证～");
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }

    /**
     * 获取个人犬只免疫列表
     */
    private void getDogImmuneList() {
        SendRequest.getDogImmuneList(new GenericsCallback<ResultClient<List<ImmuneBean>>>(new JsonGenericsSerializator()) {

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
            public void onResponse(ResultClient<List<ImmuneBean>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    if (response.getData().size() > 0) {
                        initImmuneTabLayout(response.getData());

                    } else {
                        binding.emptyView.setVisibility(View.GONE);
                        binding.emptyView.setText("暂无免疫证～");

                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }

    private void initLicenceTabLayout(List<LicenceBean> data) {
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        for (LicenceBean licenceBean : data) {
            customPagerAdapter.addFragment(licenceBean.getDogType(), MyDogCertificateOrImmuneFragment.getInstanceLicence(type, licenceBean));

        }

//        mainPagerAdapter.addFragment("萨摩耶", MyDogCertificateOrImmuneFragment.getInstance(type, 0));
//        mainPagerAdapter.addFragment("柯基", MyDogCertificateOrImmuneFragment.getInstance(type, 1));
//        mainPagerAdapter.addFragment("泰迪", MyDogCertificateOrImmuneFragment.getInstance(type, 2));
//        mainPagerAdapter.addFragment("哈士奇", MyDogCertificateOrImmuneFragment.getInstance(type, 3));

        binding.viewPager.setAdapter(customPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(10);
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

    private void initImmuneTabLayout(List<ImmuneBean> data) {
        CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        for (ImmuneBean immuneBean : data) {
            if (immuneBean.getType() != 2 ) {
                customPagerAdapter.addFragment(immuneBean.getDogType(), MyDogCertificateOrImmuneFragment.getInstanceImmune(type, immuneBean));
            }
        }
        binding.viewPager.setAdapter(customPagerAdapter);
        binding.viewPager.setOffscreenPageLimit(10);
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