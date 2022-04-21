package com.dog.manage.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemMediaFileBinding;
import com.dog.manage.app.media.MediaFile;
import com.dog.manage.app.media.MediaUtils;

public class MediaFileAdapter extends BaseRecyclerAdapter<MediaFile, ItemMediaFileBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MediaFileAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_media_file;
    }


    @Override
    protected void onBindItem(ItemMediaFileBinding binding, MediaFile dataBean, int position) {
        GlideLoader.LoderMediaImage(mContext, dataBean.getPath(), binding.coverView);
        binding.selectView.setSelected(dataBean.getStatus() == 0 ? false : true);
        binding.durationView.setText(CommonUtil.FormatMiss(dataBean.getDuration()/1000));
        binding.durationView.setVisibility(dataBean.getType() == MediaFile.VIDEO ? View.VISIBLE : View.GONE);
        binding.selectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBean.setStatus(dataBean.getStatus() == 0 ? 1 : 0);
                binding.selectView.setSelected(dataBean.getStatus() == 0 ? false : true);
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
