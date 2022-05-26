package com.dog.manage.app.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.base.view.OnClickListener;
import com.base.view.RecycleViewDivider;
import com.dog.manage.app.R;
import com.dog.manage.app.adapter.DogCertificateAdapter;
import com.dog.manage.app.databinding.ActivityUpdateDogCertificateBinding;
import com.dog.manage.app.model.Dog;

import java.util.Arrays;

/**
 * 信息变更
 */
public class UpdateDogCertificateActivity extends BaseActivity {

    private ActivityUpdateDogCertificateBinding binding;
    private DogCertificateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_update_dog_certificate);
        addActivity(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        RecycleViewDivider divider = new RecycleViewDivider(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                CommonUtil.dip2px(getApplicationContext(), 8),
                Color.parseColor("#FFFFFF"));
        binding.recyclerView.addItemDecoration(divider);

        binding.recyclerView.setNestedScrollingEnabled(false);
        adapter = new DogCertificateAdapter(getApplicationContext());
        binding.recyclerView.setAdapter(adapter);
        adapter.refreshData(Arrays.asList("", "", "", "", "", "", "", "", ""));

    }

    public void onClickConfirm(View view) {
        if (adapter.getSelect() < 0) {
            ToastUtils.showShort(getApplicationContext(), "选择1个要变更的犬证");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("type", UpdateDogOwnerInfoActivity.type_details);
        bundle.putSerializable("DogDetail", new Dog(0,0,"哈士奇"));
        openActivity(UpdateDogOwnerInfoActivity.class, bundle);
    }
}