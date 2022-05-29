package com.dog.manage.app.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.utils.CommonUtil;
import com.base.utils.GsonUtils;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.MessageDetailsActivity;
import com.dog.manage.app.adapter.AreaSelectAdapter;
import com.dog.manage.app.area.CityData;
import com.dog.manage.app.area.CityManager;
import com.dog.manage.app.databinding.ActivityAreaSelectBinding;

import java.util.List;

/**
 * 请选择省市区
 */
public class AreaSelectActivity extends BaseActivity {

    private ActivityAreaSelectBinding binding;
    private AreaSelectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_area_select);
        addActivity(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 1),
                Color.parseColor("#FAFAFA"));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new AreaSelectAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);

        List<CityData> cities = CityManager.getInstance().getCityDataList();
        if (cities.size() > 0) {
            for (CityData cityData : cities) {
                if (cityData.getName().equals("北京市")) {
//                    adapter.refreshData(cityData.getChildren().get(0).getChildren());
                    break;
                }
            }
        }

        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                CityData.FirstChildrenBean.SecondChildrenBean dataBean = (CityData.FirstChildrenBean.SecondChildrenBean) object;
                Intent intent = new Intent();
                intent.putExtra("cityName", dataBean.getName());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });
    }
}