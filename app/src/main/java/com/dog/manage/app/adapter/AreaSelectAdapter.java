package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.area.CityData;
import com.dog.manage.app.databinding.ItemAreaSelectBinding;

public class AreaSelectAdapter extends BaseRecyclerAdapter<CityData.FirstChildrenBean.SecondChildrenBean, ItemAreaSelectBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int select = 0;

    public int getSelect() {
        return select;
    }

    public AreaSelectAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_area_select;
    }

    @Override
    protected void onBindItem(ItemAreaSelectBinding binding, CityData.FirstChildrenBean.SecondChildrenBean dataBean, int position) {
        binding.titleView.setText(dataBean.getName());
        binding.selectedView.setVisibility(select == position ? View.VISIBLE : View.GONE);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
