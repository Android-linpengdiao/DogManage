package com.dog.manage.app.activity;


import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.base.utils.LogUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityPoliciesDetailsBinding;
import com.dog.manage.app.model.PoliciesBean;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import okhttp3.Call;

/**
 * 政法法规
 */
public class PoliciesDetailsActivity extends BaseActivity {

    private ActivityPoliciesDetailsBinding binding;
    private PoliciesBean dataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_policies_details);
        addActivity(this);
        dataBean = (PoliciesBean) getIntent().getSerializableExtra("dataBean");
        if (dataBean!=null){
            loadData(dataBean.getNoticeId());

        }
    }

    public void loadData(Integer noticeId) {
        SendRequest.getNoticeById(noticeId,
                new GenericsCallback<ResultClient<PoliciesBean>>(new JsonGenericsSerializator()) {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(ResultClient<PoliciesBean> response, int id) {
                        if (response.isSuccess() && response.getData() != null) {
                            binding.noticeTitleView.setText(dataBean.getNoticeTitle());
                            binding.noticeTimeView.setText(dataBean.getCreateTime());
                            initWebView(dataBean.getNoticeContent());
                        } else {
                            ToastUtils.showShort(getApplicationContext(), response.getMessage());
                        }
                    }
                });

    }

    private void initWebView(String noticeContent) {
        WebSettings wvSettings = binding.webView.getSettings();
        // 是否阻止网络图像
        wvSettings.setBlockNetworkImage(false);
        // 是否阻止网络请求
        wvSettings.setBlockNetworkLoads(false);
        // 是否加载JS
        wvSettings.setJavaScriptEnabled(true);
        wvSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //覆盖方式启动缓存
        wvSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 使用广泛视窗
        wvSettings.setUseWideViewPort(false);
        wvSettings.setLoadWithOverviewMode(true);
        wvSettings.setDomStorageEnabled(true);
        //是否支持缩放
        wvSettings.setBuiltInZoomControls(false);
        wvSettings.setSupportZoom(false);
        //不显示缩放按钮
        wvSettings.setDisplayZoomControls(false);
        wvSettings.setAllowFileAccess(true);
        wvSettings.setDatabaseEnabled(true);
        //缓存相关
        wvSettings.setAppCacheEnabled(true);
        wvSettings.setDomStorageEnabled(true);
        wvSettings.setDatabaseEnabled(true);
        //去掉右侧导航条
        binding.webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.setHorizontalScrollBarEnabled(false);//水平不显示
        binding.webView.setVerticalScrollBarEnabled(false); //垂直不显示
        binding.webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                webView.loadUrl("javascript:getContent('" + noticeContent + "')");
                super.onPageFinished(webView, s);
            }
        });
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.addJavascriptInterface(new myJavascriptInterface(), "injectedObject");

        String url = "file:///android_asset/template.html";
        binding.webView.loadUrl(url);
    }

    public class myJavascriptInterface {

        @JavascriptInterface
        public void getHeight(int pageHeight) {
            LogUtil.i(TAG, "getHeight: pageHeight = " + pageHeight);

        }

    }
}