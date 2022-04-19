package com.dog.manage.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.base.utils.GlideLoader;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.FragmentMyDogCertificateOrImmuneBinding;

/**
 * 我的犬证、免疫证明
 */
public class MyDogCertificateOrImmuneFragment extends BaseFragment {

    private FragmentMyDogCertificateOrImmuneBinding binding;
    public static final int type_certificate = 1;//犬证
    public static final int type_immune = 2;//免疫证
    private int type;
    private int id;

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
            id = getArguments().getInt("id");

            binding.certificateContainer.setVisibility(type == type_certificate ? View.VISIBLE : View.GONE);
            binding.immuneContainer.setVisibility(type == type_immune ? View.VISIBLE : View.GONE);

            if (type == type_certificate) {
                GlideLoader.LoderImage(getActivity(), "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.certificateCoverView, 5);

            } else if (type == type_immune) {
                GlideLoader.LoderImage(getActivity(), "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView, 5);


            }

        }
        return binding.getRoot();
    }
}
