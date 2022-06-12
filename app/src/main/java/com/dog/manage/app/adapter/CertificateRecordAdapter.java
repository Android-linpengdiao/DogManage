package com.dog.manage.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.base.BaseRecyclerAdapter;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.RecordActivity;
import com.dog.manage.app.databinding.ItemCertificateRecordBinding;
import com.dog.manage.app.model.RecordImmune;

public class CertificateRecordAdapter extends BaseRecyclerAdapter<RecordImmune, ItemCertificateRecordBinding> {

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
    protected void onBindItem(ItemCertificateRecordBinding binding, RecordImmune dataBean, int position) {

        if (type == RecordActivity.type_certificate) {
            //办理状态 0 全部 1：待审核 2：代缴费 3：审核驳回 4：已办结 5：已过期 6：已注销
            binding.titleView.setText("犬证办理");
            binding.descView.setVisibility(View.GONE);
            binding.bottomView.setVisibility(View.VISIBLE);
            binding.dogOwnerContainer.setVisibility(View.INVISIBLE);
            binding.contentView.setText(dataBean.getDogType() + "-" + dataBean.getDogAge() + "岁3个月");
            binding.createTimeView.setText(dataBean.getCreatedTime());
            binding.contentView.setTextColor(Color.parseColor("#999999"));
            if (dataBean.getLicenceStatus() != null) {
                if (dataBean.getLicenceStatus() == 1) {
                    binding.checkStatusView.setText("审核中");
                    binding.bottomView.setVisibility(View.GONE);

                } else if (dataBean.getLicenceStatus() == 2) {
                    binding.checkStatusView.setText("审核通过");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_g);
                    binding.detailsStatusView.setText("支付费用");

                } else if (dataBean.getLicenceStatus() == 3) {
                    binding.checkStatusView.setText("审核拒绝");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                    binding.detailsStatusView.setText("查看详情");

                } else if (dataBean.getLicenceStatus() == 4) {
                    binding.checkStatusView.setText("已办结");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_gray);
                    binding.detailsStatusView.setText("查看犬证");

                } else {
                    binding.checkStatusView.setText("审核中");
                    binding.bottomView.setVisibility(View.GONE);

                }
            }

        } else if (type == RecordActivity.type_immune) {
            //办理状态 0 默认 1：未接种 2：已接种 3: 即将过期 4: 已过期
            binding.titleView.setText("免疫证办理");
            binding.descView.setVisibility(View.GONE);
            binding.dogOwnerContainer.setVisibility(View.INVISIBLE);
            binding.contentView.setText(dataBean.getDogType() + "-" + dataBean.getDogAge() + "岁3个月");
            binding.contentView.setTextColor(Color.parseColor("#999999"));
            if (dataBean.getLincenceStatus() != null) {
                if (dataBean.getLincenceStatus() == 1) {
                    binding.checkStatusView.setText("已预约");
                    binding.detailsStatusView.setText("查看信息");

                } else if (dataBean.getLincenceStatus() == 2) {
                    binding.checkStatusView.setText("已办结");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_g);
                    binding.detailsStatusView.setText("查看免疫证");

                } else if (dataBean.getLincenceStatus() == 3) {
                    binding.checkStatusView.setText("即将过期");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                    binding.detailsStatusView.setText("办理年审");

                } else if (dataBean.getLincenceStatus() == 4) {
                    binding.checkStatusView.setText("已过期");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                    binding.detailsStatusView.setText("办理年审");

                } else {
                    binding.checkStatusView.setText("已预约");
                    binding.detailsStatusView.setText("查看信息");

                }
            }

        } else if (type == RecordActivity.type_transfer) {
            //办理状态 0 是未审核的 1 成功的 2 是失败的
            binding.titleView.setText("犬只过户");
            binding.contentView.setText(dataBean.getDogType() + "-" + dataBean.getDogAge() + "岁3个月");
            binding.createTimeView.setText(dataBean.getCreatedTime());
            binding.descView.setText("犬证:" + dataBean.getDogLicenceNum());
            binding.oldUserNameView.setText("原犬主:" + dataBean.getOldUserName());
            binding.newUserNameView.setText("新犬主:" + dataBean.getNewUserName());
            if (dataBean.getStatus() != null) {
                if (dataBean.getStatus() == 0) {
                    binding.checkStatusView.setText("审核中");
                    binding.detailsStatusView.setVisibility(View.GONE);

                } else if (dataBean.getStatus() == 1) {
                    binding.checkStatusView.setText("过户成功");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_g);
                    binding.detailsStatusView.setVisibility(View.GONE);

                } else if (dataBean.getStatus() == 2) {
                    binding.checkStatusView.setText("过户失败");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                    binding.detailsStatusView.setVisibility(View.VISIBLE);

                } else {
                    binding.checkStatusView.setText("审核中");
                    binding.detailsStatusView.setVisibility(View.GONE);

                }
            }

        } else if (type == RecordActivity.type_logout) {
            //status 办理状态 null （不传） 全部 0 未办理 1 已办理 2 驳回
            binding.titleView.setText("犬只注销");
            binding.bottomView.setVisibility(View.GONE);
            binding.contentView.setTextColor(Color.parseColor("#999999"));
            binding.contentView.setText(dataBean.getDogType() + "-" + dataBean.getDogAge() + "岁3个月");
            binding.createTimeView.setText(dataBean.getCreatedTime());
            binding.descView.setText("犬证:" + dataBean.getDogLicenceNum());
            if (dataBean.getStatus() != null) {
                if (dataBean.getStatus() == 0) {
                    binding.checkStatusView.setText("审核中");
                    binding.detailsStatusView.setVisibility(View.GONE);

                } else if (dataBean.getStatus() == 1) {
                    binding.checkStatusView.setText("审核通过");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_g);
                    binding.detailsStatusView.setVisibility(View.GONE);

                } else if (dataBean.getStatus() == 2) {
                    binding.checkStatusView.setText("审核拒绝");
                    binding.checkStatusView.setBackgroundResource(R.drawable.tag_r);
                    binding.detailsStatusView.setVisibility(View.VISIBLE);

                } else {
                    binding.checkStatusView.setText("审核中");
                    binding.detailsStatusView.setVisibility(View.GONE);

                }
            }

        }

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null)
                    if (type == RecordActivity.type_certificate) {
                        onClickListener.onClick(view, dataBean);

                    } else if (type == RecordActivity.type_immune) {
                        onClickListener.onClick(view, dataBean);

                    } else if (type == RecordActivity.type_transfer) {
                        onClickListener.onClick(view, dataBean);

                    }else if (type == RecordActivity.type_logout) {
                        onClickListener.onClick(view, dataBean);

                    } else {
                        onClickListener.onClick(view, position);
                    }
            }
        });
    }
}
