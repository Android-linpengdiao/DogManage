package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityCertificateDetailsBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.DogLicenceDetail;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class CertificateDetailsActivity extends BaseActivity {

    private ActivityCertificateDetailsBinding binding;
    private int lincenceId;
    private DogLicenceDetail dogLicenceDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_certificate_details);
        addActivity(this);

        lincenceId = getIntent().getIntExtra("lincenceId", 0);

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogLicenceDetail == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", dogLicenceDetail.getDogId());
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogLicenceDetail == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", dogLicenceDetail.getDogId());
                openActivity(DogInfoActivity.class, bundle);
            }
        });

        getDogLicenceDetail();

    }

    public void onClickConfirm(View view) {
        if (dogLicenceDetail == null) {
            return;
        }
        //办理状态 0 全部 1：待审核 2：代缴费 3：审核驳回 4：已办结 5：已过期 6：已注销
        Integer licenceStatus = dogLicenceDetail.getLicenceStatus();
        if (licenceStatus == 2) {
            Bundle bundle = new Bundle();
            bundle.putInt("orderId", dogLicenceDetail.getOrderId());
            bundle.putInt("licenceId", dogLicenceDetail.getLincenceId());
            bundle.putString("price", dogLicenceDetail.getPrice() + "");
            openActivity(PayActivity.class, bundle);

        } else if (licenceStatus == 3) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_certificate);
            openActivity(DogCertificateEditDogOwnerActivity.class, bundle);

        } else if (licenceStatus == 4) {
            Bundle bundle = new Bundle();
            bundle.putInt("type", MyDogCertificateOrImmuneActivity.type_certificate);
            openActivity(MyDogCertificateOrImmuneActivity.class, bundle);

        } else {

        }
    }

    /**
     * 获取个人犬只免疫列表
     */
    private void getDogLicenceDetail() {
        SendRequest.getDogLicenceDetail(lincenceId, new GenericsCallback<ResultClient<DogLicenceDetail>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(CertificateDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(CertificateDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<DogLicenceDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());

                } else {
                    ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取信息失败");
                }
            }
        });
    }

    private void initView(DogLicenceDetail data) {
        binding.container.setVisibility(View.VISIBLE);
        binding.bottomView.setVisibility(View.VISIBLE);
        dogLicenceDetail = data;
        //办理状态 0 全部 1：待审核 2：代缴费 3：审核驳回 4：已办结 5：已过期 6：已注销
        Integer licenceStatus = data.getLicenceStatus();
        binding.auditStatusView.setText(licenceStatus == 1 ? "审核中" : licenceStatus == 2 ? "审核通过" : licenceStatus == 3 ? "审核拒绝" : licenceStatus == 4 ? "已办结" : licenceStatus == 5 ? "已过期" : licenceStatus == 6 ? "已注销" : "审核中");
        binding.payTypeView.setVisibility(licenceStatus == 4 && data.getAdoptStatus() != 1 ? View.VISIBLE : View.GONE);
        binding.priceView.setVisibility(data.getAdoptStatus() == 1 ? View.GONE : View.VISIBLE);
        binding.auditReasonView.setVisibility(licenceStatus == 3 ? View.VISIBLE : View.GONE);
        binding.confirmView.setText(licenceStatus == 2 ? "在线支付" : licenceStatus == 3 ? "重新提交" : licenceStatus == 4 ? "查看犬证" : licenceStatus == 5 ? "犬证年审" : "在线支付");
        binding.confirmView.setVisibility(licenceStatus == 1 ? View.GONE : View.VISIBLE);
        binding.confirmView.setBackgroundResource(licenceStatus == 5 ? R.drawable.button_red : R.drawable.button_theme);

        binding.contentView.setText(data.getDogType() + "-" + CommonUtil.getDogAge(data.getDogAge()));
        binding.createTimeView.setText(data.getCreatedTime());
        binding.dogOwnerInfoView.binding.itemContent.setText(data.getUserName());
        binding.dogDetailsView.binding.itemContent.setText(data.getDogType());
        binding.acceptUnitView.setText(data.getAcceptUnit());
        binding.priceView.binding.itemContent.setText("￥" + data.getPrice());
        binding.auditReasonView.binding.itemDesc.setText(data.getRejectionReason());
        binding.payTypeView.binding.itemContent.setText(data.getPayType() == 1 ? "微信" : "支付宝");
    }
}