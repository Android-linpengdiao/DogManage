package com.dog.manage.app.adapter;

import android.content.Context;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.utils.CommonUtil;
import com.base.utils.GlideLoader;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.DogCertificateEditDogActivity;
import com.dog.manage.app.databinding.ItemDogBinding;
import com.dog.manage.app.databinding.ItemFrameBinding;
import com.dog.manage.app.model.Dog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DogAdapter extends BaseRecyclerAdapter<Dog, ItemDogBinding> {

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
    protected void onBindItem(ItemDogBinding binding, Dog dataBean, int position) {

        binding.titleView.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        binding.contentView.setVisibility(type == 0 ? View.GONE : View.VISIBLE);

        try {
            //犬只照片
            List<String> idPhotos = new Gson().fromJson(dataBean.getDogPhoto(), new TypeToken<List<String>>() {
            }.getType());
            if (idPhotos.size() > 1) {
                GlideLoader.LoderImage(mContext, idPhotos.size() > 1 ? idPhotos.get(1) : "", binding.iconView);
            }
            if (idPhotos.size() > 0) {
                GlideLoader.LoderImage(mContext, idPhotos.size() > 0 ? idPhotos.get(0) : "", binding.iconView);
            }
            if (idPhotos.size() > 2) {
                GlideLoader.LoderImage(mContext, idPhotos.size() > 2 ? idPhotos.get(2) : "", binding.iconView);
            }
        } catch (Exception e) {
            e.getMessage();
        }

        binding.titleView.setText((!CommonUtil.isBlank(dataBean.getDogName()) ? dataBean.getDogName() : "--")
                + "|"
                + (!CommonUtil.isBlank(dataBean.getDogColor()) ? dataBean.getDogColor() : "--")
                + "|"
                + CommonUtil.getDogAge(dataBean.getDogAge()));
        binding.contentView.setText((!CommonUtil.isBlank(dataBean.getDogName()) ? dataBean.getDogName() : "--")
                + "|"
                + (!CommonUtil.isBlank(dataBean.getDogColor()) ? dataBean.getDogColor() : "--")
                + "|"
                + CommonUtil.getDogAge(dataBean.getDogAge()));

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, dataBean);
            }
        });
    }
}
