package com.dog.manage.app.activity.record;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.activity.DogManageWorkflowActivity;
import com.dog.manage.app.activity.MainDogManageWorkflowActivity;
import com.dog.manage.app.activity.PoliciesActivity;
import com.dog.manage.app.adapter.FrameItemAdapter;
import com.dog.manage.app.adapter.VideoAdapter;
import com.dog.manage.app.databinding.ActivityPunishDetailsBinding;
import com.dog.manage.app.model.DogDetail;
import com.dog.manage.app.model.PoliciesBean;
import com.dog.manage.app.model.PunishRecord;
import com.dog.manage.app.view.GridItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * 处罚记录详情
 */
public class PunishDetailsActivity extends BaseActivity {

    private ActivityPunishDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_punish_details);
        addActivity(this);

        PunishRecord dataBean = (PunishRecord) getIntent().getSerializableExtra("dataBean");
        if (dataBean != null) {
            getIllegalDetails(dataBean);
        }


    }

    private void getIllegalDetails(PunishRecord dataBean) {
        SendRequest.getIllegalDetails(dataBean.getId(), new GenericsCallback<ResultClient<PunishRecord>>(new JsonGenericsSerializator()) {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<PunishRecord> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    initView(response.getData());
                } else {
                    initView(dataBean);
                }
            }
        });
    }

    private void initView(PunishRecord dataBean) {
        binding.illegalTypeView.setText("违法类型: " + "违法类型: " +
                (dataBean.getIllegalTypeId() == 1 ? "犬只伤人" :
                        dataBean.getIllegalTypeId() == 2 ? "犬吠" :
                                dataBean.getIllegalTypeId() == 3 ? "未牵狗绳" :
                                        dataBean.getIllegalTypeId() == 4 ? "其他" : "其他"));
        binding.illegalMeasureView.setText("处罚措施: " + dataBean.getIllegalMeasure());
        binding.illegalTimeView.setText("处罚时间: " + dataBean.getIllegalTime());
        binding.illegalDescribeView.setText(dataBean.getIllegalDescribe());

        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 12));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(videoAdapter);
        videoAdapter.refreshData(Arrays.asList(dataBean.getIllegalFileUrl()));
        videoAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {


            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
    }
}