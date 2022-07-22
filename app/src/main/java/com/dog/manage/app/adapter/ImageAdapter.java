package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.DogCertificateEditDogOwnerActivity;
import com.dog.manage.app.databinding.ItemImageBinding;


public class ImageAdapter extends BaseRecyclerAdapter<String, ItemImageBinding> {

    private OnClickListener onClickListener;
    private int type = 0;

    public void setType(int type) {
        this.type = type;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) { //应该在此根viewType 选择不同布局的,但设计图上给的差距不大.就整合成一个布局了
        return R.layout.item_image;
    }

    @Override
    protected void onBindItem(final ItemImageBinding binding, String url, int position) {
        if (url != null && url.equals("add")) {
            binding.updateContainer.setVisibility(View.VISIBLE);
            binding.deleteImageView.setVisibility(View.GONE);
            binding.playView.setVisibility(View.GONE);
            GlideLoader.LoderImage(mContext, null, binding.imageImageView, 6);
        } else {
            binding.updateContainer.setVisibility(View.GONE);
            binding.deleteImageView.setVisibility(View.VISIBLE);
            binding.playView.setVisibility(url != null && url.endsWith("mp4") ? View.VISIBLE : View.GONE);
            GlideLoader.LoderImage(mContext, url, binding.imageImageView, 6);
        }
        if (type == DogCertificateEditDogOwnerActivity.type_details){
            binding.deleteImageView.setVisibility(View.GONE);
        }
        binding.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.remove(position);
                notifyDataSetChanged();
            }
        });
        binding.viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(view, position);
                }
            }
        });

    }
}
