package com.dog.manage.app.activity.record;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.utils.CommonUtil;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.activity.DogManageWorkflowActivity;
import com.dog.manage.app.activity.MainDogManageWorkflowActivity;
import com.dog.manage.app.activity.PoliciesActivity;
import com.dog.manage.app.adapter.FrameItemAdapter;
import com.dog.manage.app.adapter.VideoAdapter;
import com.dog.manage.app.databinding.ActivityPunishDetailsBinding;
import com.dog.manage.app.view.GridItemDecoration;

import java.util.Arrays;

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

        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 12));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(videoAdapter);
        videoAdapter.refreshData(Arrays.asList("", "", "", "", ""));
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