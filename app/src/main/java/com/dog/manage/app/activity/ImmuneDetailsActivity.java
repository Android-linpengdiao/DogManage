package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.base.LocationBean;
import com.base.manager.LoadingManager;
import com.base.utils.MapNavigationUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityImmuneDetailsBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.ImmuneApproveDetail;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class ImmuneDetailsActivity extends BaseActivity {

    private ActivityImmuneDetailsBinding binding;
    private int immuneId;
    private ImmuneApproveDetail immuneApproveDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_immune_details);
        addActivity(this);

        immuneId = getIntent().getIntExtra("immuneId", 0);

        binding.dogOwnerInfoView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (immuneApproveDetail == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", immuneApproveDetail.getDogId());
                bundle.putInt("type", DogCertificateEditDogOwnerActivity.type_details);
                openActivity(DogCertificateEditDogOwnerActivity.class, bundle);
            }
        });
        binding.dogDetailsView.binding.itemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (immuneApproveDetail == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("dogId", immuneApproveDetail.getDogId());
                openActivity(DogDetailsActivity.class, bundle);
            }
        });

        getDogImmuneApproveDetail();

    }

    /**
     * 免疫证办理记录详情
     */
    private void getDogImmuneApproveDetail() {
        SendRequest.getDogImmuneApproveDetail(immuneId, new GenericsCallback<ResultClient<ImmuneApproveDetail>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(ImmuneDetailsActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(ImmuneDetailsActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<ImmuneApproveDetail> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());

                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMessage());
                }
            }
        });
    }

    private void initView(ImmuneApproveDetail data) {
        immuneApproveDetail = data;
        //办理状态 0 默认 1：未接种 2：已接种 3: 即将过期 4: 已过期
        Integer licenceStatus = data.getLincenceStatus();
        binding.auditStatusView.setText(licenceStatus == 1 ? "审核中" : licenceStatus == 2 ? "已接种" : licenceStatus == 3 ? "即将过期" : licenceStatus == 4 ? "已过期" : "审核中");
        binding.hintView.setVisibility(licenceStatus == 1 ? View.VISIBLE : View.GONE);
        binding.confirmView.setVisibility(licenceStatus == 1 ? View.GONE : View.VISIBLE);
        binding.confirmView.setText(licenceStatus == 2 ? "查看免疫证" : "立即办理");
        binding.contentView.setText(data.getDogType() + "-" + data.getDogAge() + "岁3个月");
        binding.createTimeView.setText(data.getCreateTime());

        binding.dogOwnerInfoView.binding.itemContent.setText(data.getUserName());
        binding.dogDetailsView.binding.itemContent.setText(data.getDogType());

        binding.hospitalNameView.setText(data.getHospitalName());
        binding.hospitalAddressView.setText(data.getHospitalAddress());
        binding.hospitalPhoneView.setText(data.getHospitalPhone());
    }

    public void onClickNavigate(View view) {
        LatLng latLng = new LatLng(39.993743, 116.472995);
        List<String> hasMap = new MapNavigationUtil().hasMap(ImmuneDetailsActivity.this);
        if (hasMap.size() > 0) {
            MapNavigationUtil.showChooseMap(ImmuneDetailsActivity.this, new LocationBean(null, latLng.latitude, latLng.longitude));
        } else {
            ToastUtils.showLong(ImmuneDetailsActivity.this, "未找到地图APP，请下载安装高德地图APP");
        }
    }

}