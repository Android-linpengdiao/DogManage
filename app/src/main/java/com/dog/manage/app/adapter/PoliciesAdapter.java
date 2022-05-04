package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemPoliciesBinding;
import com.dog.manage.app.model.PoliciesBean;

public class PoliciesAdapter extends BaseRecyclerAdapter<PoliciesBean, ItemPoliciesBinding> {


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public PoliciesAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_policies;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemPoliciesBinding binding, PoliciesBean dataBean, int position) {
        binding.titleView.setText(dataBean.getNoticeTitle());
        binding.timeView.setText(dataBean.getCreateTime());
        GlideLoader.LoderImage(mContext, dataBean.getImageUrl(), binding.coverView, 5);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
