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

    /**
     * 分享
     *
     * @return
     */
//    public ViewShareDialogBinding shareView(Activity activity, String shareUrl, String shareTitle, String shareSummary, String thumbnailUrl) {
//        View contentView = View.inflate(this, R.layout.view_share_dialog, null);
//        ViewShareDialogBinding shareBinding = DataBindingUtil.bind(contentView);
//        BaseBottomSheetDialog shareBottomSheetDialog = new BaseBottomSheetDialog(this) {
//            @Override
//            protected View initContentView() {
//                return shareBinding.getRoot();
//            }
//        };
//        shareBottomSheetDialog.show();
//
//
//        shareUrl = APIUrls.URL_DOMAIN + "share_view/invite.html?uid=";
//        shareTitle = "新的好友邀请！";
//        shareSummary = "我一起领养专属宠物体验全新的萌宠社交吧～";
//        thumbnailUrl = null;
//
//        String finalShareUrl = shareUrl;
//        String finalShareTitle = shareTitle;
//        String finalShareSummary = shareSummary;
//        String finalThumbnailUrl = thumbnailUrl;
//        shareBinding.shareWx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // scene 0代表好友   1代表朋友圈
//                WXManager.send(activity, 0, finalShareUrl, finalShareTitle, finalShareSummary, finalThumbnailUrl);
//            }
//        });
//        shareBinding.shareWxMoment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // scene 0代表好友   1代表朋友圈
//                WXManager.send(activity, 1, finalShareUrl, finalShareTitle, finalShareSummary, finalThumbnailUrl);
//            }
//        });
//        shareBinding.shareQQ.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TencentHelper.shareToQQ(activity, finalShareUrl, finalShareTitle, finalShareSummary, finalThumbnailUrl, uiListener);
//            }
//        });
//        shareBinding.shareQzone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TencentHelper.shareToQzone(activity, finalShareUrl, finalShareTitle, finalShareSummary, finalThumbnailUrl, uiListener);
//            }
//        });
//        shareBinding.shareWeiBo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                WbManager.sendMultiMessage(shareHandler, finalShareTitle, finalShareSummary, finalThumbnailUrl, finalShareUrl);
//            }
//        });
//        shareBinding.sharePicture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FileUtils.shareMultiImg(activity, "分享图片", "/storage/emulated/0/DCIM/Camera/IMG_20220216_182347.jpg");
////                shareMultiImg(activity, "分享图片", "/storage/emulated/0/练琴帮/1643017039586127.jpeg");
//
//            }
//        });
//
//
//        shareBinding.cancelView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                shareBottomSheetDialog.cancel();
//            }
//        });
//        return shareBinding;
//    }
//
//    private static MyBroadcastReceiver receiver;
//
//    private void myRegister() {
//        if (receiver == null) {
//            receiver = new MyBroadcastReceiver();
//        }
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(receiver, new IntentFilter(Config.wechat_share_success));
//    }
//
//    private void unRegister() {
//        if (receiver != null) {
//            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(receiver);
//            receiver = null;
//        }
//    }
//
//    private class MyBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (Config.wechat_share_success.equals(action)) {
//                shareSuccess();
//            }
//        }
//    }
//
//    @Override
//    public void onWbShareSuccess() {
//        shareSuccess();
//    }
//
//    @Override
//    public void onWbShareCancel() {
//        ToastUtils.showShort(this, "取消分享");
//
//    }
//
//    @Override
//    public void onWbShareFail() {
//        ToastUtils.showShort(this, "分享失败");
//
//    }
//
//    //分享成功
//    private void shareSuccess() {
//        ToastUtils.showShort(this, "分享成功");
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        shareHandler.doResultIntent(data, this);
//        if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE ||
//                requestCode == com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE) {
//            Tencent.onActivityResultData(requestCode, resultCode, data, uiListener);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    /**
     * 关注
     *
     * @param type     关注类型 1用户
     * @param typeId
     * @param url
     * @param callback
     */
    public void focusFans(int type, int typeId, String url, Callback callback) {
        if (!checkUserRank(this, true)) {
            return;
        }
        SendRequest.focusFans(getUserInfo().getToken(), type, typeId, url,
                new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (callback != null)
                            callback.onError();
                    }

                    @Override
                    public void onResponse(BaseData response, int id) {
                        if (callback != null)
                            callback.onResponse(response.isSuccess(), id);

                    }
                });
    }

    /**
     * 收藏
     *
     * @param type     type 11:内容 12:教材 13:乐曲 14:测评
     * @param typeId
     * @param callback
     */
//    public void favorite(boolean add, int type, int typeId, Callback callback) {
//        if (!checkUserRank(this, true)) {
//            return;
//        }
//        if (add) {
//            SendRequest.favorite_add(getUserInfo().getToken(), type, typeId,
//                    new GenericsCallback<ResultClient<ResultBean>>(new JsonGenericsSerializator()) {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            Log.i(TAG, "onError: " + e.getMessage());
//                            if (callback != null)
//                                callback.onError();
//                        }
//
//                        @Override
//                        public void onResponse(ResultClient<ResultBean> response, int id) {
//                            if (response.isSuccess()) {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), response.getData().getId());
//
//                            } else {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), 0);
//                                ToastUtils.showShort(getApplicationContext(), response.getMsg());
//
//                            }
//
//                        }
//                    });
//        } else {
//            SendRequest.favorite_delete(getUserInfo().getToken(), typeId,
//                    new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            Log.i(TAG, "onError: " + e.getMessage());
//                            if (callback != null)
//                                callback.onError();
//                        }
//
//                        @Override
//                        public void onResponse(BaseData response, int id) {
//                            if (response.isSuccess()) {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), 0);
//
//                            } else {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), typeId);
//                                ToastUtils.showShort(getApplicationContext(), response.getMsg());
//
//                            }
//
//                        }
//                    });
//        }
//    }

    /**
     * 点赞
     *
     * @param type     type 1-评论 8-作品
     * @param typeId
     * @param callback
     */
//    public void support(boolean add, int type, int typeId, Callback callback) {
//        if (!checkUserRank(this, true)) {
//            return;
//        }
//        if (add) {
//            SendRequest.support_add(getUserInfo().getToken(), type, typeId,
//                    new GenericsCallback<ResultClient<ResultBean>>(new JsonGenericsSerializator()) {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            if (callback != null)
//                                callback.onError();
//                        }
//
//                        @Override
//                        public void onResponse(ResultClient<ResultBean> response, int id) {
//                            if (response.isSuccess()) {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), response.getData().getId());
//
//                            } else {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), 0);
//                                ToastUtils.showShort(getApplicationContext(), response.getMsg());
//
//                            }
//
//                        }
//                    });
//        } else {
//            SendRequest.support_delete(getUserInfo().getToken(), typeId,
//                    new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            if (callback != null)
//                                callback.onError();
//                        }
//
//                        @Override
//                        public void onResponse(BaseData response, int id) {
//                            if (response.isSuccess()) {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), typeId);
//
//                            } else {
//                                if (callback != null)
//                                    callback.onResponse(response.isSuccess(), 0);
//                                ToastUtils.showShort(getApplicationContext(), response.getMsg());
//
//                            }
//
//                        }
//                    });
//        }
//    }

    /**
     * 评论列表
     *
     * @return
     */
//    public ViewCommentDialogBinding commentView(int typeId, CommentAdapter adapter) {
//        View contentView = View.inflate(this, R.layout.view_comment_dialog, null);
//        commentBinding = DataBindingUtil.bind(contentView);
//
//        commentAdapter = adapter;
//        commentBinding.recyclerView.setNestedScrollingEnabled(false);
//        commentBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        commentBinding.recyclerView.setAdapter(commentAdapter);
//
//        BaseBottomSheetDialog shareBottomSheetDialog = new BaseBottomSheetDialog(this) {
//            @Override
//            protected View initContentView() {
//                return commentBinding.getRoot();
//            }
//        };
//        shareBottomSheetDialog.show();
//        commentBinding.cancelView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                shareBottomSheetDialog.cancel();
//            }
//        });
//
//
//        commentBinding.refreshLayout.setEnableRefresh(false);
//        commentBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                commentPager(false, typeId);
//            }
//        });
//        commentPager(true, typeId);
//
//
//        return commentBinding;
//    }
//
//    private ViewCommentDialogBinding commentBinding;
//    private CommentAdapter commentAdapter;
//    private Pager<CommentBean> pager = new Pager<>();
//
//    private void commentPager(boolean isRefresh, int typeId) {
//        SendRequest.comment_pager(getUserInfo().getToken(), 9, typeId, pager.getNextCursor(),
//                new GenericsCallback<Pager<CommentBean>>(new JsonGenericsSerializator()) {
//
//                    @Override
//                    public void onAfter(int id) {
//                        super.onAfter(id);
//                        if (!isRefresh) {
//                            commentBinding.refreshLayout.finishLoadMore();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        if (!isRefresh) {
//                            commentBinding.refreshLayout.finishLoadMore(false);
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(Pager<CommentBean> response, int id) {
//                        pager = response;
//                        if (response != null && response.getData() != null) {
//                            if (isRefresh) {
//                                commentAdapter.refreshData(response.getData());
//                            } else {
//                                commentAdapter.loadMoreData(response.getData());
//                            }
//                            if (!response.isHasnext()) {
//                                commentBinding.refreshLayout.setNoMoreData(true);
//                            }
//                            commentBinding.commentTotalNumView.setText("评论 " + pager.getTotalCount());
//                        }
//                    }
//                });
//    }
//
//    public void setBanner(Activity activity, Banner banner, CreationBean creationBean) {
//        List<BannerBean> banners = creationBean.getBanners();
//        List<String> images = new ArrayList<>();
//        if (banners != null && banners.size() > 0) {
//            for (int i = 0; i < banners.size(); i++) {
//                images.add(banners.get(i).getImage());
//            }
//            banner.setOnBannerListener(new OnBannerListener() {
//                @Override
//                public void OnBannerClick(int position) {
//                    if (banners.size() >= position + 1) {
//                        BannerBean dataBean = banners.get(position);
//                        if (dataBean != null && !CommonUtil.isBlank(dataBean.getUrl())) {
//                            //内部原生地址以 native://开头 内部h5地址以 inner://开头 外部浏览器地址以 http://开头
//                            if (dataBean.getUrl().startsWith("native")) {
//                                if (checkUserRank(activity, true)) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putInt("setid", 0);
//                                    bundle.putInt("sid", 0);
//                                    openActivity(EvaluationActivity.class, bundle);
//                                }
//
//                            } else if (dataBean.getUrl().startsWith("inner")) {
//
//
//                            } else if (dataBean.getUrl().startsWith("http")) {
//                                Intent intent = new Intent();
//                                intent.setData(Uri.parse(dataBean.getUrl()));
//                                intent.setAction(Intent.ACTION_VIEW);
//                                startActivity(intent);
//
//                            }
//                        }
//                    }
//                }
//            });
//            banner.setImages(images).start();
//            banner.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void downLoadFile(Activity activity, String url) {
//        if (CommonUtil.isBlank(url)) {
//            return;
//        }
//        File file = new File(FileUtils.getAppPath() + url.substring(url.lastIndexOf("/") + 1));
//        BaseDownloadTask task = FileDownloader.getImpl().create(url)
//                .setPath(file.getPath())
//                .setCallbackProgressTimes(1000)
//                .setListener(new FileDownloadListener() {
//
//                    @Override
//                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                    }
//
//                    @Override
//                    protected void started(BaseDownloadTask task) {
//                        super.started(task);
//                        LoadingManager.showProgress(activity,
//                                String.format(Constants.str_download_wait, " 0%"));
//                        LoadingManager.OnDismissListener(activity, new LoadingManager.OnDismissListener() {
//                            @Override
//                            public void onDismiss() {
//                                final int id = FileDownloadUtils.generateId(url, file.getPath());
//                                FileDownloader.getImpl().pause(id);
//                            }
//                        });
//                    }
//
//                    @Override
//                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
//                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
//                    }
//
//                    @Override
//                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                        int percent = (int) ((double) soFarBytes / (double) totalBytes * 100);
//                        LoadingManager.updateProgress(activity, String.format(Constants.str_download_wait, " " + percent + "%"));
//                    }
//
//                    @Override
//                    protected void completed(BaseDownloadTask task) {
//                        LoadingManager.hideProgress(activity);
//                        ToastUtils.showShort(activity, "文件已保存至" + FileUtils.getAppPath() + " 文件夹");
//
//                    }
//
//                    @Override
//                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                    }
//
//                    @Override
//                    protected void error(BaseDownloadTask task, Throwable e) {
//                        LoadingManager.hideProgress(activity);
//                        ToastUtils.showShort(activity, "下载失败");
//
//                    }
//
//                    @Override
//                    protected void warn(BaseDownloadTask task) {
//                    }
//                });
//        task.start();
//    }

    public boolean checkUserRank(Context context) {
        return checkUserRank(context, false);
    }

    public boolean checkUserRank(Context context, boolean login) {
        UserInfo user = getUserInfo();
        //游客模式
        if (user == null || TextUtils.isEmpty(user.getToken()) || user.getUserRank() == 99) {
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
