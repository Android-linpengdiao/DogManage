package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemAdoptionRecordBinding;
import com.dog.manage.app.databinding.ItemDongCertificateBinding;
import com.dog.manage.app.model.LicenceBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DogCertificateAdapter extends BaseRecyclerAdapter<LicenceBean, ItemDongCertificateBinding> {

    private int select = 0;
    private OnClickListener onClickListener;

    public int getSelect() {
        return select;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public DogCertificateAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_dong_certificate;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemDongCertificateBinding binding, LicenceBean licenceBean, int position) {

        binding.idNumView.setText(licenceBean.getIdNum());
        binding.dogTypeView.setText(licenceBean.getDogType());
        binding.dogColorView.setText(licenceBean.getDogColor());
        binding.dogGenderView.setText(licenceBean.getDogGender() == 0 ? "雌性" : "雄性");
        binding.orgNameView.setText(licenceBean.getOrgName());
        binding.awardTimeView.setText(licenceBean.getAwardTime());
        binding.detailedAddressView.setText(licenceBean.getDetailedAddress());
        try {
            List<String> dogPhotos = new Gson().fromJson(licenceBean.getDogPhoto(), new TypeToken<List<String>>() {
            }.getType());
            if (dogPhotos != null && dogPhotos.size() > 0)
                GlideLoader.LoderImage(mContext, dogPhotos.get(0), binding.certificateCoverView, 5);
        } catch (Exception e) {
            e.getMessage();
        }

        binding.selectedView.setSelected(select == licenceBean.getLincenceId() ? true : false);
        binding.selectedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = licenceBean.getLincenceId();
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, licenceBean);
            }
        });
    }
}
