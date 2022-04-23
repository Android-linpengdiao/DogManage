package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemDongManageWorkflowBinding;

public class DogManageWorkflowAdapter extends BaseRecyclerAdapter<String, ItemDongManageWorkflowBinding> {


    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private String searchContent;

    public void setContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public DogManageWorkflowAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_dong_manage_workflow;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemDongManageWorkflowBinding binding, String dataBean, int position) {
        binding.titleView.setText((position + 1) + "." + dataBean);
        binding.getRoot().setBackgroundResource(position % 3 == 0 ? R.drawable.bk_03 : position % 3 == 1 ? R.drawable.bk_02 : R.drawable.bk_01);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
