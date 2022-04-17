package com.dog.manage.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.utils.CommonUtil;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.MessageDetailsActivity;
import com.dog.manage.app.activity.record.PunishDetailsActivity;
import com.dog.manage.app.adapter.MessageAdapter;
import com.dog.manage.app.databinding.ActivityMessageBinding;

import java.util.Arrays;

public class MessageActivity extends BaseActivity {

    private ActivityMessageBinding binding;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_message);
        addActivity(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 1),
                Color.parseColor("#FAFAFA"));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new MessageAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);
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
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                openActivity(MessageDetailsActivity.class);
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

    }
}