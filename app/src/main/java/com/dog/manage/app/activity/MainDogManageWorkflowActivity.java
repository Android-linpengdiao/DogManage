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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainDogManageWorkflowActivity extends BaseActivity {

    private ActivityMainDogManageWorkflowBinding binding;
    private DogManageWorkflowAdapter adapter;
    private List<String> list = Arrays.asList(
            "犬证办理",
            "免疫证办理",
            "犬证年审",
            "犬只过户",
            "犬只领养",
            "犬只注销",
            "信息变更");

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
        adapter.refreshData(list);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                int position = (int) object;
                if (checkUserRank(getApplicationContext(), true)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", position);
                    bundle.putString("title", list.get(position));
                    openActivity(DogManageWorkflowActivity.class, bundle);
                }
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });

    }

}