package com.dog.manage.app.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.LocationBean;
import com.base.utils.CommonUtil;
import com.base.utils.MapNavigationUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.MessageDetailsActivity;
import com.dog.manage.app.adapter.DogImmuneHospitalAdapter;
import com.dog.manage.app.adapter.MessageAdapter;
import com.dog.manage.app.databinding.ActivityDogImmuneHospitalBinding;

import java.util.Arrays;
import java.util.List;

/**
 * 犬只选择免疫医院
 */
public class DogImmuneHospitalActivity extends BaseActivity {

    private ActivityDogImmuneHospitalBinding binding;
    private DogImmuneHospitalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_dog_immune_hospital);
        addActivity(this);
        binding.thirdStepView.setSelected(true);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 1),
                Color.parseColor("#FAFAFA"));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new DogImmuneHospitalAdapter(getApplicationContext());
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
                switch (view.getId()) {
                    case R.id.navigateView:
                        openMapNavigate();

                        break;
                }
            }

            @Override
            public void onLongClick(View view, Object object) {

            }
        });


    }

    private String name = "北京";
    private double latitude = 39.993743;
    private double longitude = 116.472995;

    private void openMapNavigate() {
        List<String> hasMap = new MapNavigationUtil().hasMap(DogImmuneHospitalActivity.this);
        if (hasMap.size() > 0) {
            MapNavigationUtil.showChooseMap(DogImmuneHospitalActivity.this, new LocationBean(name, latitude, longitude));
        } else {
            ToastUtils.showLong(DogImmuneHospitalActivity.this, "未找到地图APP，请下载安装百度高德或者腾讯的地图APP");
        }
    }

    public void onClickConfirm(View view) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", SubmitSuccessActivity.type_immune);
        openActivity(SubmitSuccessActivity.class, bundle);
    }
}