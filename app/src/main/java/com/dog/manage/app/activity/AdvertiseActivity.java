package com.dog.manage.app.activity;


import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.base.utils.CommonUtil;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.DogAdapter;
import com.dog.manage.app.databinding.ActivityAdvertiseBinding;
import com.dog.manage.app.view.GridItemDecoration;

import java.util.Arrays;

public class AdvertiseActivity extends BaseActivity {

    private ActivityAdvertiseBinding binding;
    private DogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_advertise);
        addActivity(this);

        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 12));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new DogAdapter(getApplication());
        adapter.setType(1);
        binding.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                openActivity(DogDetailsActivity.class);

            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
        adapter.refreshData(Arrays.asList(
                "犬证办理",
                "免疫证办理",
                "犬证年审",
                "犬只过户",
                "犬只领养",
                "犬只注销",
                "信息変更",
                "办理流程",
                "政策法规"));
    }
}