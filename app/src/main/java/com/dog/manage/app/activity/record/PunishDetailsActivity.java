package com.dog.manage.app.activity.record;

import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.databinding.ActivityPunishDetailsBinding;

/**
 * 处罚记录详情
 */
public class PunishDetailsActivity extends BaseActivity {

    private ActivityPunishDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_punish_details);
        addActivity(this);

    }
}