package com.dog.manage.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.databinding.ItemTextBinding;
import com.base.view.OnClickListener;
import com.dog.manage.app.model.Dog;

public class DogColorListAdapter extends BaseRecyclerAdapter<String, ItemTextBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public DogColorListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return com.base.R.layout.item_text;
    }


    @Override
    protected void onBindItem(ItemTextBinding binding, String dataBean, int position) {
        binding.titleView.setText(dataBean);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
                if (onClickListener != null) {
                    onClickListener.onClick(view, dataBean);
                }
            }
        });
    }
}
