package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemAdoptionRecordBinding;
import com.dog.manage.app.databinding.ItemDongCertificateBinding;

public class DogCertificateAdapter extends BaseRecyclerAdapter<String, ItemDongCertificateBinding> {

    private int select = -1;
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
    protected void onBindItem(ItemDongCertificateBinding binding, String dataBean, int position) {
        GlideLoader.LoderImage(mContext, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.certificateCoverView,8);
        binding.selectedView.setSelected(select == position ? true : false);
        binding.selectedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
