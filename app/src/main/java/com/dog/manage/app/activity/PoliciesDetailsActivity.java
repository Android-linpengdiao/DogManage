package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import com.base.BaseApplication;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.PoliciesAdapter;
import com.dog.manage.app.databinding.ActivityPoliciesDetailsBinding;
import com.dog.manage.app.model.PoliciesBean;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;

public class PoliciesDetailsActivity extends BaseActivity {

    private ActivityPoliciesDetailsBinding binding;
    private PoliciesBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_policies_details);
        addActivity(this);
        dataBean = (PoliciesBean) getIntent().getSerializableExtra("dataBean");
        if (dataBean!=null){
            loadData(dataBean.getNoticeId());

        }
    }

    public void loadData(Integer noticeId) {
        SendRequest.getNoticeById(noticeId,
                new GenericsCallback<ResultClient<PoliciesBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(ResultClient<PoliciesBean> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            binding.noticeTitleView.setText(dataBean.getNoticeTitle());
                            binding.noticeTimeView.setText(dataBean.getCreateTime());
                            binding.noticeContentView.setText(dataBean.getNoticeTitle());
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });

    }
}