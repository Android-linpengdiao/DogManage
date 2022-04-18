package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ItemFrameBinding;

public class FrameItemAdapter extends BaseRecyclerAdapter<String, ItemFrameBinding> {

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public FrameItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_frame;
    }

    @Override
    protected void onBindItem(ItemFrameBinding binding, String dataBean, int position) {

        GlideLoader.LoderDrawable(mContext,
                position == 0 ? R.drawable.icon_qz :
                        position == 1 ? R.drawable.icon_myz :
                                position == 2 ? R.drawable.icon_qzns :
                                        position == 3 ? R.drawable.icon_qzgh :
                                                position == 4 ? R.drawable.icon_qzly :
                                                        position == 5 ? R.drawable.icon_qzzx :
                                                                position == 6 ? R.drawable.icon_xxbg :
                                                                        position == 7 ? R.drawable.icon_bllc :
                                                                                R.drawable.icon_zcfg, binding.iconView);
        binding.titleView.setText(dataBean);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
    }
}
