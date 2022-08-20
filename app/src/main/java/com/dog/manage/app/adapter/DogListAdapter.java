package com.dog.manage.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.databinding.ItemTextBinding;
import com.base.view.OnClickListener;
import com.dog.manage.app.model.Dog;

public class DogListAdapter extends BaseRecyclerAdapter<Dog, ItemTextBinding> {

    private int type = 0;//1-犬证办理
    private int done = 0;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public DogListAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return com.base.R.layout.item_text;
    }

    @Override
    protected void onBindItem(ItemTextBinding binding, Dog dataBean, int position) {

        if (type == 1) {
            if (dataBean.getType() != null && dataBean.getType() == 2) {
                binding.titleView.setText(dataBean.getDogType() + (dataBean.getIdNum() != null ? "  " + "领养编号" + ":" + dataBean.getIdNum() : ""));
            } else {
                binding.titleView.setText(dataBean.getDogType());
            }
        } else {
            binding.titleView.setText(dataBean.getDogType());
        }

//        if (type == 1) {
//            if (dataBean.getType() != null && dataBean.getType() == 2) {
//                binding.titleView.setText(dataBean.getDogType() + (dataBean.getDogNum() != null ? "  " + "领养编号" + ":" + dataBean.getDogNum() : ""));
//            } else {
//                binding.titleView.setText(dataBean.getDogType() + (dataBean.getIdNum() != null ? "  " + "免疫证号" + ":" + dataBean.getIdNum() : ""));
//            }
//        } else {
//            binding.titleView.setText(dataBean.getDogType() + (dataBean.getIdNum() != null ? "  " + "犬证号" + ":" + dataBean.getIdNum() : ""));
//        }

//        binding.titleView.setText(dataBean.getDogType() + (dataBean.getIdNum() != null ? "  " + (type == 1 ? "免疫证号" : "犬证号") + "-" + dataBean.getIdNum() : ""));
        binding.titleView.setTypeface(position == done ? Typeface.defaultFromStyle(Typeface.BOLD) : Typeface.defaultFromStyle(Typeface.NORMAL));
//        binding.doneView.setVisibility(position == done ? View.VISIBLE : View.GONE);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done = position;
                notifyDataSetChanged();
                if (onClickListener != null) {
                    onClickListener.onClick(view, dataBean);
                }
            }
        });
    }
}
