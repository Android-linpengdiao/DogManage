package com.dog.manage.app.activity;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivitySubmitSuccessBinding;

public class SubmitSuccessActivity extends BaseActivity {
    private ActivitySubmitSuccessBinding binding;

    public static final int type_logout = 0;//犬只注销
    public static final int type_update = 1;//信息更变
    public static final int type_transfer = 2;//信息更变
    public static final int type_immune = 3;//免疫证办理
    public static final int type_examined = 4;//犬证年审

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_submit_success);
        addActivity(this);

        int type = getIntent().getIntExtra("type", 0);


        if (type == type_logout) {
            binding.titleView.binding.itemTitle.setText("犬只注销");

        } else if (type == type_update) {
            binding.titleView.binding.itemTitle.setText("信息更变");

        } else if (type == type_transfer) {
            binding.titleView.binding.itemTitle.setText("犬只过户");

        } else if (type == type_immune) {
            binding.titleView.binding.itemTitle.setText("免疫证办理");

        } else if (type == type_examined) {
            binding.titleView.binding.itemTitle.setText("犬证年审");

        }


    }
}