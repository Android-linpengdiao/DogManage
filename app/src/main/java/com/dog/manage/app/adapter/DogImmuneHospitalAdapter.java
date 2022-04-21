package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemDogImmuneHospitalBinding;
import com.dog.manage.app.databinding.ItemMessageBinding;

public class DogImmuneHospitalAdapter extends BaseRecyclerAdapter<String, ItemDogImmuneHospitalBinding> {

    private int mPosition = -1;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private String searchContent;

    public void setContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public DogImmuneHospitalAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_dog_immune_hospital;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemDogImmuneHospitalBinding binding, String dataBean, int position) {

        binding.selectedView.setSelected(mPosition == position ? true : false);
        binding.selectedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPosition = position;
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
        binding.navigateView.setOnClickListener(new View.OnClickListener() {
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