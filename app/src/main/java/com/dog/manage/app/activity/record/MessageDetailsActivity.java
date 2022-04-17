package com.dog.manage.app.activity.record;


import android.os.Bundle;

import com.dog.manage.app.R;
import com.dog.manage.app.activity.BaseActivity;
import com.dog.manage.app.databinding.ActivityMessageDetailsBinding;

public class MessageDetailsActivity extends BaseActivity {

    private ActivityMessageDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_message_details);
        addActivity(this);
    }
}