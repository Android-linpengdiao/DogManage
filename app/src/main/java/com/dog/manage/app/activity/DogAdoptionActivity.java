package com.dog.manage.app.activity;

import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.view.OnClickListener;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.AdoptionDetailsActivity;
import com.dog.manage.app.adapter.DogAdapter;
import com.dog.manage.app.databinding.ActivityDogAdoptionBinding;
import com.dog.manage.app.view.GridItemDecoration;

import java.util.Arrays;

public class DogAdoptionActivity extends BaseActivity {

    private ActivityDogAdoptionBinding binding;
    private DogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_adoption);
        addActivity(this);

        binding.firstStepView.setSelected(true);
        binding.firstStepView.setText("①选择犬只");
        binding.secondStepView.setText("②犬主信息");
        binding.thirdStepView.setText("③提交审核");

        GridItemDecoration.Builder builder = new GridItemDecoration.Builder(this);
        builder.color(R.color.transparent);
        builder.size(CommonUtil.dip2px(this, 12));
        binding.recyclerView.addItemDecoration(new GridItemDecoration(builder));
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new DogAdapter(getApplication());
        adapter.setType(0);
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
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""));


    }

}