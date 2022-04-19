package com.dog.manage.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.base.manager.DialogManager;
import com.base.utils.StatusBarUtil;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityWelcomeBinding;
import com.okhttp.utils.APIUrls;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_welcome);
        StatusBarUtil.setStatusBarColor(this, R.color.black);
        setStatusBarHeight();

        sharedPreferences = getSharedPreferences("sp_data", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("appService", false)) {
            appService();
        } else {
            init();
        }

    }

    private void appService() {
        DialogManager.showServiceDialog(WelcomeActivity.this, new DialogManager.OnClickListener() {
            @Override
            public void onClick(View view, Object object) {
                switch (view.getId()) {
                    case R.id.tv_confirm:
                        sharedPreferences.edit().putBoolean("appService", true).apply();
                        init();

                        break;
                    case R.id.tv_cancel:
                        System.exit(1);

                        break;
                }
            }
        }, new DialogManager.Listener() {
            @Override
            public void onItemLeft() {
                Intent intent = new Intent();
                intent.setData(Uri.parse(APIUrls.user_agreement));
                intent.setAction(Intent.ACTION_VIEW);
//                startActivity(intent);
            }

            @Override
            public void onItemRight() {
                Intent intent = new Intent();
                intent.setData(Uri.parse(APIUrls.privacy_policy));
                intent.setAction(Intent.ACTION_VIEW);
//                startActivity(intent);
            }
        });
    }

    private void init() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(MainActivity.class);
                finish();
            }
        }, 1000);

//        if (CommonUtil.isBlank(getUserInfo()) || CommonUtil.isBlank(getUserInfo().getToken())) {
//            SendRequest.getVisitor(new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//
//                }
//
//                @Override
//                public void onResponse(ResultClient<UserInfo> response, int id) {
//                    if (response.isSuccess() && response.getData() != null) {
//                        BaseApplication.getInstance().setUserInfo(response.getData());
//                        openActivity(MainActivity.class);
//                        finish();
//                    } else {
//                        ToastUtils.showShort(WelcomeActivity.this, response.getMsg());
//                    }
//                }
//            });
//        } else {
//            openActivity(MainActivity.class);
//            finish();
//        }
    }
}