package com.dog.manage.app.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.DogManageWorkflowAdapter;
import com.dog.manage.app.databinding.ActivityMainDogManageWorkflowBinding;

import java.util.Arrays;

public class MainDogManageWorkflowActivity extends BaseActivity {

    private ActivityMainDogManageWorkflowBinding binding;
    private DogManageWorkflowAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_main_dog_manage_workflow);
        addActivity(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
//        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
//                LinearLayoutManager.VERTICAL,
//                CommonUtil.dip2px(getApplicationContext(), 8),
//                Color.parseColor("#FAFAFA"));
//        binding.recyclerView.addItemDecoration(divider);
        adapter = new DogManageWorkflowAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);
        adapter.refreshData(Arrays.asList(
                "犬证办理流程",
                "免疫证办理流程",
                "犬证年审流程",
                "犬只过户流程",
                "犬只领养流程",
                "犬只注销流程",
                "信息变更流程"));
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {

            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

    }

}