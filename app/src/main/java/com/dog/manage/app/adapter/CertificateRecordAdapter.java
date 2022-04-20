package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemAdoptionRecordBinding;
import com.dog.manage.app.databinding.ItemCertificateRecordBinding;

public class CertificateRecordAdapter extends BaseRecyclerAdapter<String, ItemCertificateRecordBinding> {


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private String searchContent;

    public void setContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public CertificateRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_certificate_record;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemCertificateRecordBinding binding, String dataBean, int position) {
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
