package com.dog.manage.app.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.BaseApplication;
import com.base.BaseData;
import com.base.Constants;
import com.base.MessageBus;
import com.base.UserInfo;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.FileUtils;
import com.base.utils.MsgCache;
import com.base.utils.PermissionUtils;
import com.base.utils.StatusBarUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.Callback;
import com.dog.manage.app.R;
//import com.lianqinbang.Callback;
//import com.lianqinbang.MyApplication;
//import com.lianqinbang.R;
//import com.lianqinbang.adapter.CommentAdapter;
//import com.lianqinbang.databinding.ViewCommentDialogBinding;
//import com.lianqinbang.databinding.ViewShareDialogBinding;
//import com.lianqinbang.model.BannerBean;
//import com.lianqinbang.model.CommentBean;
//import com.lianqinbang.model.CreationBean;
//import com.lianqinbang.model.ResultBean;
//import com.lianqinbang.share.Config;
//import com.lianqinbang.share.qq.TencentHelper;
//import com.lianqinbang.share.weibo.WbManager;
//import com.lianqinbang.share.wx.WXManager;
//import com.lianqinbang.view.BaseBottomSheetDialog;
//import com.liulishuo.filedownloader.BaseDownloadTask;
//import com.liulishuo.filedownloader.FileDownloadListener;
//import com.liulishuo.filedownloader.FileDownloader;
//import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.okhttp.Pager;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;
//import com.okhttp.utils.APIUrls;
//import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
//import com.sina.weibo.sdk.WbSdk;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.share.WbShareCallback;
//import com.sina.weibo.sdk.share.WbShareHandler;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;
//import com.xw.banner.Banner;
//import com.xw.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class BaseActivity extends AppCompatActivity {

    public String TAG = this.getClass().getName();

//    private static boolean wxRegister = false;
//    protected static IUiListener uiListener;
//    protected WbShareHandler shareHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDarkTheme(false);

        initShare();

    }

    public void onClickBack(View view) {
        finish();
    }

    private void initShare() {
//        if (!wxRegister) {
//            wxRegister = true;
//            myRegister();
//        }
//        if (uiListener == null) {
//            uiListener = new IUiListener() {
//                @Override
//                public void onComplete(Object o) {
//                    shareSuccess();
//                }
//
//                @Override
//                public void onError(UiError uiError) {
//                    ToastUtils.showShort(BaseActivity.this, "分享失败");
//                }
//
//                @Override
//                public void onCancel() {
//                    ToastUtils.showShort(BaseActivity.this, "取消分享");
//                }
//            };
//        }
//        WbSdk.install(this, new AuthInfo(this,
//                com.lianqinbang.share.weibo.Constants.APP_KEY,
//                com.lianqinbang.share.weibo.Constants.REDIRECT_URL,
//                com.lianqinbang.share.weibo.Constants.SCOPE));
//        shareHandler = new WbShareHandler(this);
//        shareHandler.registerApp();
//        WbManager.ssoHandler = new SsoHandler(this);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    public <T extends ViewDataBinding> T getViewData(int layoutId) {
        T binding = DataBindingUtil.setContentView(this, layoutId);
//        if (findViewById(R.id.titleView) != null) {
//            setTypeface(findViewById(R.id.titleView));
//        }
        return binding;
    }

    public void setStatusBarDarkTheme(boolean dark) {
        if (!StatusBarUtil.setStatusBarDarkTheme(this, dark)) {
            StatusBarUtil.setStatusBarColor(this, dark ? R.color.black : R.color.white);
        }
    }

    @SuppressLint("NewApi")
    public void setStatusBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (findViewById(R.id.status_bar) != null) {
                findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
                findViewById(R.id.status_bar).getLayoutParams().height = CommonUtil.getStatusBarHeight(getApplication());
            }
        }
    }

    public void setTypeface(TextView textView) {
        Typeface typeface = BaseApplication.getInstance().getTypeface();
        textView.setTypeface(typeface);
    }

    public boolean checkUserRank(Context context) {
        return checkUserRank(context, false);
    }

    public boolean checkUserRank(Context context, boolean login) {
        UserInfo user = getUserInfo();
        //游客模式
        if (user == null || TextUtils.isEmpty(user.getToken())) {
            if (login) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            return false;
        }
        return true;
    }

    public void updateUserInfo() {
        updateUserInfo(null);
    }

    public void updateUserInfo(Callback callback) {
        SendRequest.userLoad(getUserInfo().getToken(), getUserInfo().getId(),
                new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (callback != null)
                            callback.onError();
                    }

                    @Override
                    public void onResponse(ResultClient<UserInfo> response, int id) {
                        if (response.isSuccess()) {
                            response.getData().setToken(getUserInfo().getToken());
                            setUserInfo(response.getData());
                        }
                        if (callback != null)
                            callback.onResponse(response.isSuccess(), id);
                    }
                });
    }

    public void setUserInfo(UserInfo userInfo) {
        MsgCache.get(this).put(Constants.USER_INFO, userInfo);
    }

    public UserInfo getUserInfo() {
        try {
            UserInfo userinfo = (UserInfo) MsgCache.get(this).getAsObject(Constants.USER_INFO);
            if (!CommonUtil.isBlank(userinfo)) {
                return userinfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new UserInfo();
        }
        return new UserInfo();
    }

    public void getVisitor() {
        getVisitor(null);
    }

    public void getVisitor(Callback callback) {
        SendRequest.getVisitor(new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (callback != null) {
                    callback.onError();
                }

            }

            @Override
            public void onResponse(ResultClient<UserInfo> response, int id) {
                if (response.isSuccess() && response.getData() != null) {
                    BaseApplication.getInstance().setUserInfo(response.getData());
                    MessageBus.Builder builder = new MessageBus.Builder();
                    MessageBus messageBus = builder
                            .codeType(MessageBus.msgId_updateUser)
                            .build();
                    EventBus.getDefault().post(messageBus);
                    if (callback != null) {
                        callback.onResponse(true, id);
                    }
                }
            }
        });
    }


    public void openActivity(Class<?> mClass) {
        openActivity(mClass, null);
    }

    public void openActivity(Class<?> mClass, int requestCode) {
        Intent intent = new Intent(this, mClass);
        startActivityForResult(intent, requestCode);
    }

    public void openActivity(Class<?> mClass, Bundle mBundle) {
        Intent intent = new Intent(this, mClass);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivity(intent);
    }

    public void openActivity(Class<?> mClass, Bundle mBundle, int requestCode) {
        Intent intent = new Intent(this, mClass);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        startActivityForResult(intent, requestCode);
    }

    private static List<Activity> activityStack = new ArrayList<Activity>();

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    public static void finishActivity(Class mClass) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && null != mClass
                    && mClass.getSimpleName().equals(activityStack.get(i).getClass().getSimpleName())) {
                activityStack.get(i).finish();
            }
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public boolean checkPermissions(String type, int code) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isAllGranted = PermissionUtils.checkPermissionAllGranted(this, type);
            if (!isAllGranted) {
                PermissionUtils.requestPermissions(this, type, code);
                return false;
            }
        }
        return true;
    }
}
