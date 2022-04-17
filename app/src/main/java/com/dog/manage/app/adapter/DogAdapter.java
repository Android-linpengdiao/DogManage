package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemDogBinding;
import com.dog.manage.app.databinding.ItemFrameBinding;

public class DogAdapter extends BaseRecyclerAdapter<String, ItemDogBinding> {

    private int type = 0;//0-领养 1-宣传
    private OnClickListener onClickListener;

    public void setType(int type) {
        this.type = type;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public DogAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_dog;
    }

    @Override
    protected void onBindItem(ItemDogBinding binding, String dataBean, int position) {

        binding.titleView.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        binding.contentView.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
        GlideLoader.LoderLoadImage(mContext, "", binding.iconView);
        binding.titleView.setText(dataBean);
        binding.contentView.setText(dataBean);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
    }
}
