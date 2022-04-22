package com.dog.manage.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemAdoptionRecordBinding;

public class AdoptionRecordAdapter extends BaseRecyclerAdapter<String, ItemAdoptionRecordBinding> {


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private String searchContent;

    public void setContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public AdoptionRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_adoption_record;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemAdoptionRecordBinding binding, String dataBean, int position) {
        GlideLoader.LoderImage(mContext, "https://pics7.baidu.com/feed/6c224f4a20a446236fb6db0ac3bf5d040df3d785.jpeg", binding.coverView,8);
        binding.bottomView.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        if (position == 1) {
            binding.statusView.setBackgroundResource(R.drawable.tag_l);
            binding.statusView.setText("待支付");
            binding.detailsStatusView.setText("支付费用");
            binding.bottomView.setVisibility(View.VISIBLE);

        } else if (position == 2) {
            binding.statusView.setBackgroundResource(R.drawable.tag_y);
            binding.statusView.setText("完成领养");
            binding.detailsStatusView.setText("查看信息");
            binding.bottomView.setVisibility(View.VISIBLE);

        } else if (position == 3) {
            binding.statusView.setBackgroundResource(R.drawable.tag_r);
            binding.statusView.setText("审核拒绝");
            binding.detailsStatusView.setText("查看信息");
            binding.bottomView.setVisibility(View.VISIBLE);

        } else {
            binding.statusView.setBackgroundResource(R.drawable.tag_b);
            binding.statusView.setText("待审核");
            binding.bottomView.setVisibility(View.GONE);

        }
        binding.detailsStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
    }
}
