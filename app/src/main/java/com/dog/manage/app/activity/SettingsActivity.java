package com.dog.manage.app.activity;


import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.base.Constants;
import com.base.manager.DialogManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileSizeUtil;
import com.base.utils.FileUtils;
import com.base.utils.MsgCache;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivitySettingsBinding;

public class SettingsActivity extends BaseActivity {

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_settings);
        addActivity(this);
    }

    public void onClickClear(View view) {
        DialogManager.showConfirmDialog(this, "确定清理缓存？", new DialogManager.Listener() {

            @Override
            public void onItemLeft() {

            }

            @Override
            public void onItemRight() {
                FileUtils.clearFile();
                double fileSize = FileSizeUtil.getFileOrFilesSize(FileUtils.getPath(), 3);
//                binding.tvFileSize.setText(fileSize + "MB");
                ToastUtils.showShort(SettingsActivity.this, "已完成清理");
            }
        });
    }

    public void onClickLogout(View view) {
        DialogManager.showConfirmDialog(SettingsActivity.this, "确定要退出登录？", new DialogManager.Listener() {
            @Override
            public void onItemLeft() {

            }

            @Override
            public void onItemRight() {
                MsgCache.get(SettingsActivity.this).remove(Constants.USER_INFO);
                openActivity(MainActivity.class);
                finish();

            }
        });
    }

    public void onClickAbout(View view) {
        openActivity(AboutActivity.class);
    }

    public void onClickAccount(View view) {
        openActivity(AccountActivity.class);
    }

    public void onClickSystemNotice(View view) {
        int settingHint;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settingHint = R.string.str_setting_hint_26;
        } else {
            settingHint = R.string.str_setting_hint;
        }
        DialogManager.showConfirmDialog(SettingsActivity.this, "开始消息推送通知", getString(settingHint), "取消", "设置", new DialogManager.Listener() {

            @Override
            public void onItemLeft() {

            }

            @Override
            public void onItemRight() {
                CommonUtil.toSetting(getApplication());
            }
        });
    }
}