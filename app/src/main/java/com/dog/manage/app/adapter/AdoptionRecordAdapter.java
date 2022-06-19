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
import com.dog.manage.app.model.RecordAdoption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class AdoptionRecordAdapter extends BaseRecyclerAdapter<RecordAdoption, ItemAdoptionRecordBinding> {


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
    protected void onBindItem(ItemAdoptionRecordBinding binding, RecordAdoption dataBean, int position) {
        binding.dogNameView.setText(dataBean.getDogName() + "|" + dataBean.getDogColor() + "|" + dataBean.getDogAge() + "岁");
        binding.acceptUnitView.setText(dataBean.getAcceptUnit());
        binding.acceptUnitAddressView.setText(dataBean.getAcceptUnitAddress());
        try {
            List<String> dogPhotos = new Gson().fromJson(dataBean.getDogPhoto(), new TypeToken<List<String>>() {
            }.getType());
            if (dogPhotos != null && dogPhotos.size() > 0)
                GlideLoader.LoderImage(mContext, dogPhotos.get(0), binding.coverView, 8);
        } catch (Exception e) {
            e.getMessage();
        }
        binding.bottomView.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        //办理状态 1 待审核 0 待支付 2 领养完成 3 拒绝 4 全部
        if (dataBean.getStatus() != null) {
            if (dataBean.getStatus() == 1) {
                binding.statusView.setBackgroundResource(R.drawable.tag_b);
                binding.statusView.setText("待审核");
                binding.bottomView.setVisibility(View.GONE);

            } else if (dataBean.getStatus() == 0) {
                binding.statusView.setBackgroundResource(R.drawable.tag_l);
                binding.statusView.setText("待支付");
                binding.detailsStatusView.setText("支付费用");
                binding.bottomView.setVisibility(View.VISIBLE);

            } else if (dataBean.getStatus() == 2) {
                binding.statusView.setBackgroundResource(R.drawable.tag_y);
                binding.statusView.setText("完成领养");
                binding.detailsStatusView.setText("查看信息");
                binding.bottomView.setVisibility(View.VISIBLE);

            } else if (dataBean.getStatus() == 3) {
                binding.statusView.setBackgroundResource(R.drawable.tag_r);
                binding.statusView.setText("审核拒绝");
                binding.detailsStatusView.setText("查看信息");
                binding.bottomView.setVisibility(View.VISIBLE);

            } else {
                binding.statusView.setBackgroundResource(R.drawable.tag_b);
                binding.statusView.setText("待审核");
                binding.bottomView.setVisibility(View.GONE);

            }
        }
        binding.detailsStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
