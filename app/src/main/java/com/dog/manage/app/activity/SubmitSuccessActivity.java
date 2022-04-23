package com.dog.manage.app.activity;

import android.os.Bundle;
import android.view.View;

import com.dog.manage.app.R;
import com.dog.manage.app.activity.record.AdoptionRecordActivity;
import com.dog.manage.app.databinding.ActivitySubmitSuccessBinding;

public class SubmitSuccessActivity extends BaseActivity {
    private ActivitySubmitSuccessBinding binding;

    public static final int type_certificate = 0;//犬证办理
    public static final int type_logout = 1;//犬只注销
    public static final int type_update = 2;//信息变更
    public static final int type_transfer = 3;//信息变更
    public static final int type_immune = 4;//免疫证办理
    public static final int type_examined = 5;//犬证年审
    public static final int type_adoption = 6;//犬只领养
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_submit_success);
        addActivity(this);

        type = getIntent().getIntExtra("type", 0);


        if (type == type_certificate) {
            binding.titleView.binding.itemTitle.setText("犬证办理");

        } else if (type == type_logout) {
            binding.titleView.binding.itemTitle.setText("犬只注销");

        } else if (type == type_update) {
            binding.titleView.binding.itemTitle.setText("信息变更");

        } else if (type == type_transfer) {
            binding.titleView.binding.itemTitle.setText("犬只过户");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("犬证年审");

        } else if (type == type_adoption) {
            binding.titleView.binding.itemTitle.setText("犬只领养");

        }


    }

    public void onClickBack(View view) {
        openActivity(MainActivity.class);
    }

    public void onClickMain(View view) {
        openActivity(MainActivity.class);
    }

    public void onClickRecord(View view) {
        Bundle bundle = new Bundle();
        if (type == type_certificate) {
            bundle.putInt("type", AdoptionRecordActivity.type_certificate);
            openActivity(AdoptionRecordActivity.class, bundle);

        } else if (type == type_logout) {
            bundle.putInt("type", AdoptionRecordActivity.type_logout);
            openActivity(AdoptionRecordActivity.class, bundle);

        } else if (type == type_update) {
            binding.titleView.binding.itemTitle.setText("信息变更");

        } else if (type == type_transfer) {
            bundle.putInt("type", AdoptionRecordActivity.type_transfer);
            openActivity(AdoptionRecordActivity.class, bundle);

        } else if (type == type_immune) {
            bundle.putInt("type", AdoptionRecordActivity.type_immune);
            openActivity(AdoptionRecordActivity.class, bundle);

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("犬证年审");

        } else if (type == type_adoption) {
            bundle.putInt("type", AdoptionRecordActivity.type_adoption);
            openActivity(AdoptionRecordActivity.class, bundle);

        }
    }
}