package com.dog.manage.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.base.utils.GlideLoader;
import com.base.utils.TimeUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.DogCertificateEditDogOwnerActivity;
import com.dog.manage.app.activity.DogCertificateExaminedActivity;
import com.dog.manage.app.activity.DogDetailsActivity;
import com.dog.manage.app.activity.DogInfoActivity;
import com.dog.manage.app.databinding.FragmentMyDogCertificateOrImmuneBinding;
import com.dog.manage.app.model.LicenceBean;

/**
 * 我的犬证、免疫证明
 */
public class MyDogCertificateOrImmuneFragment extends BaseFragment {

    private FragmentMyDogCertificateOrImmuneBinding binding;
    public static final int type_certificate = 1;//犬证
    public static final int type_immune = 2;//免疫证
    private int type;
    private int id;

    public static MyDogCertificateOrImmuneFragment getInstanceLicence(int type, LicenceBean licenceBean) {
        MyDogCertificateOrImmuneFragment fragment = new MyDogCertificateOrImmuneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putSerializable("dataBean", licenceBean);
        fragment.setArguments(bundle);
        return fragment;

    }

    public static MyDogCertificateOrImmuneFragment getInstance(int type, int id) {
        MyDogCertificateOrImmuneFragment fragment = new MyDogCertificateOrImmuneFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putInt("id", id);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_dog_certificate_or_immune, container, false);

        if (getArguments() != null) {
            type = getArguments().getInt("type");
//            id = getArguments().getInt("id");

            binding.certificateContainer.setVisibility(type == type_certificate ? View.VISIBLE : View.GONE);
            binding.immuneContainer.setVisibility(type == type_immune ? View.VISIBLE : View.GONE);

            if (type == type_certificate) {
                LicenceBean licenceBean = (LicenceBean) getArguments().getSerializable("dataBean");
                if (licenceBean != null) {
                    certificateView(licenceBean);

                }

            } else if (type == type_immune) {
                GlideLoader.LoderImage(getActivity(), "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView, 5);


            }


        }
        return binding.getRoot();
    }

    private void certificateView(LicenceBean licenceBean) {
        binding.idNumView.setText(licenceBean.getIdNum());
        binding.dogTypeView.setText(licenceBean.getDogType());
        binding.dogColorView.setText(licenceBean.getDogColor());
        binding.dogGenderView.setText(licenceBean.getDogGender() == 0 ? "雌性" : "雄性");
        binding.orgNameView.setText(licenceBean.getOrgName());
        binding.awardTimeView.setText(licenceBean.getAwardTime());
        binding.detailedAddressView.setText(licenceBean.getDetailedAddress());
        GlideLoader.LoaderDogCover(getActivity(), "", binding.certificateCoverView, 5);

        binding.dogOwnerInfoView.binding.itemContent.setText(licenceBean.getOrgName());
        binding.dogDetailsView.binding.itemContent.setText(licenceBean.getDogType());

        long examinedTime = 365 - (System.currentTimeMillis() - TimeUtils.getTimeExamined(licenceBean.getAwardTime())) / (24 * 60 * 60 * 1000);
        binding.examinedTimeView.setText((examinedTime > 0 ? "距离年审还有" : "距离年审已过") + Math.abs(examinedTime) + "天");
        binding.examinedTimeView.setTextColor(examinedTime > 0 ? Color.parseColor("#273154") : Color.parseColor("#FF2020"));

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", licenceBean.getDogId());
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", licenceBean.getDogId());
                openActivity(DogInfoActivity.class, bundle);
            }
        });
        binding.examinedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(DogCertificateExaminedActivity.class);
            }
        });
    }
}
