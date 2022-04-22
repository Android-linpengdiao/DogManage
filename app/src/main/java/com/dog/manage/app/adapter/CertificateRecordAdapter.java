package com.dog.manage.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.AdoptionRecordActivity;
import com.dog.manage.app.databinding.ItemAdoptionRecordBinding;
import com.dog.manage.app.databinding.ItemCertificateRecordBinding;

public class CertificateRecordAdapter extends BaseRecyclerAdapter<String, ItemCertificateRecordBinding> {

    private int type;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CertificateRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_certificate_record;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected void onBindItem(ItemCertificateRecordBinding binding, String dataBean, int position) {

        if (type == AdoptionRecordActivity.type_certificate) {
            binding.bottomView.setVisibility(View.VISIBLE);
            binding.dogOwnerContainer.setVisibility(View.INVISIBLE);
            binding.contentView.setText("萨摩耶-2岁3个月");
            binding.contentView.setTextColor(Color.parseColor("#999999"));
            if (position == 1) {
                binding.checkStatusView.setText("审核通过");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_g);
                binding.detailsStatusView.setText("支付费用");

            } else if (position == 2) {
                binding.checkStatusView.setText("审核拒绝");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                binding.detailsStatusView.setText("查看详情");

            } else if (position == 3) {
                binding.checkStatusView.setText("已办结");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_gray);
                binding.detailsStatusView.setText("查看犬证");

            } else {
                binding.checkStatusView.setText("审核中");
                binding.bottomView.setVisibility(View.GONE);

            }

        } else if (type == AdoptionRecordActivity.type_immune) {
            binding.dogOwnerContainer.setVisibility(View.INVISIBLE);
            binding.contentView.setText("萨摩耶-2岁3个月");
            binding.contentView.setTextColor(Color.parseColor("#999999"));
            if (position == 1) {
                binding.checkStatusView.setText("已办结");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_g);
                binding.detailsStatusView.setText("查看免疫证");

            } else if (position == 2) {
                binding.checkStatusView.setText("即将过期");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                binding.detailsStatusView.setText("办理年审");

            } else if (position == 3) {
                binding.checkStatusView.setText("已过期");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                binding.detailsStatusView.setText("办理年审");

            } else {
                binding.checkStatusView.setText("已预约");
                binding.detailsStatusView.setText("查看信息");

            }

        } else if (type == AdoptionRecordActivity.type_transfer) {
            if (position == 1) {
                binding.checkStatusView.setText("过户成功");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_g);
                binding.detailsStatusView.setVisibility(View.GONE);

            } else if (position == 2) {
                binding.checkStatusView.setText("过户失败");
                binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                binding.detailsStatusView.setVisibility(View.VISIBLE);

            } else {
                binding.checkStatusView.setText("审核中");
                binding.detailsStatusView.setVisibility(View.GONE);

            }

        } else if (type == AdoptionRecordActivity.type_logout) {


        }

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    onClickListener.onClick(view, position);
            }
        });
    }
}
