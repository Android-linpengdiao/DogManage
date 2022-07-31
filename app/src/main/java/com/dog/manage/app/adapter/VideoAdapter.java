package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemVideoBinding;

public class VideoAdapter extends BaseRecyclerAdapter<String, ItemVideoBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public VideoAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_video;
    }

    @Override
    protected void onBindItem(ItemVideoBinding binding, String url, int position) {

        GlideLoader.LoderImage(mContext, url, binding.coverView, 6);
//        binding.durationView.setText(CommonUtil.FormatMiss(dataBean.getDuration()/1000));
        binding.playView.setVisibility(url != null && url.endsWith("mp4") ? View.VISIBLE : View.GONE);
        binding.durationView.setVisibility(View.GONE);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
    }
}
