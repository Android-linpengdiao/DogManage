package com.dog.manage.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.DogCertificateAdapter;
import com.dog.manage.app.databinding.ActivityUpdateDogCertificateBinding;
import com.dog.manage.app.model.Dog;
import com.dog.manage.app.model.LicenceBean;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 信息变更
 */
public class UpdateDogCertificateActivity extends BaseActivity {

    private ActivityUpdateDogCertificateBinding binding;
    private DogCertificateAdapter adapter;
    private LicenceBean licenceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_dog_certificate);
        addActivity(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 8),
                Color.parseColor("#FFFFFF"));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new DogCertificateAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                licenceBean = (LicenceBean) object;
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

        getMyLicenceList();
    }

    /**
     * 我的-我的犬证
     */
    private void getMyLicenceList() {
        SendRequest.getMyLicenceList(new GenericsCallback<ResultClient<List<LicenceBean>>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(UpdateDogCertificateActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(UpdateDogCertificateActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<List<LicenceBean>> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    if (response.getData().size() > 0) {
                        adapter.refreshData(response.getData());
                    } else {
                        binding.emptyView.setVisibility(View.VISIBLE);
                        binding.emptyView.setText("暂无犬证～");
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }
            }
        });
    }

    public void onClickConfirm(View view) {
        if (licenceBean == null) {
            ToastUtils.showShort(getApplicationContext(), "选择1个要变更的犬证");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", UpdateDogOwnerInfoActivity.type_details);
        bundle.putSerializable("LicenceBean", licenceBean);
        openActivity(UpdateDogOwnerInfoActivity.class, bundle);
    }
}