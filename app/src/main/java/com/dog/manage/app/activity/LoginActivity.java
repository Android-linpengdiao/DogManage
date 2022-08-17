package com.dog.manage.app.activity;


import android.Manifest;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.base.BaseApplication;
import com.base.BaseData;
import com.base.UserInfo;
import com.base.manager.LoadingManager;
import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.dog.manage.app.MyClickableSpan;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityLoginBinding;
import com.dog.manage.app.login.ConfigUtils;
import com.dog.manage.app.utils.AppSigning;
import com.dog.manage.app.utils.DeviceUtils;
import com.okhttp.ResultClient;
import com.okhttp.SendRequest;
import com.okhttp.callbacks.GenericsCallback;
import com.okhttp.sample_okhttp.JsonGenericsSerializator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Request;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private boolean privacyState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_login);
        setStatusBarDarkTheme(true);
        addActivity(this);

        privacyState = getIntent().getBooleanExtra("privacyState", true);
        binding.checkView.setSelected(privacyState);
        binding.checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.checkView.setSelected(!binding.checkView.isSelected());
            }
        });

        String userText = "《用户协议》";
        String yinsiText = "《隐私政策》";
        String content = "登录代表同意《用户协议》《隐私政策》";
        SpannableString spannableString = new SpannableString(content);
        // 设置字体颜色
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#5E44FF")), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#5E44FF")), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体大小
//          spannableString.setSpan(new AbsoluteSizeSpan(CommonUtil.sp2px(mContext, 14)), content.indexOf(stateText), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置点击
        spannableString.setSpan(new MyClickableSpan(LoginActivity.this, userText), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new MyClickableSpan(LoginActivity.this, yinsiText), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.privacyView.setText(spannableString);
        binding.privacyView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件


//        /**
//         * appId	STRING	是	应用的APPID，由控制台创建应用时生成
//         * bundleId	STRING	否	bundleId，IOS必传。iOS项目Bundle Identifier
//         * client	STRING	是	客户端类型，客户端类型，iOS：1，Android：2
//         * device	STRING	是	设备型号
//         * packageName	STRING	否	包名，安卓必传，Android applicationId对应的包名
//         * packageSign	STRING	是	包签名，安卓必传，签名文件MD5值去冒号转小写
//         * randoms	STRING	是	随机串，UUID去除”-”
//         * sign	STRING	是	签名 hmacsha1
//         * version	STRING	是	SDK版本，SDK完整版本号，如2.3.1.0
//         */
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("appId", String.valueOf(""));
//        paramsMap.put("client", String.valueOf(2));
//        paramsMap.put("device", Build.DEVICE);
//        paramsMap.put("packageName", getPackageName());
//        paramsMap.put("packageSign", AppSigning.getMD5(this));
//        paramsMap.put("randoms", String.valueOf(UUID.randomUUID().hashCode()).replace("-",""));
//        paramsMap.put("sign", AppSigning.getSha1(this));
//        paramsMap.put("version", Build.VERSION.RELEASE);
//        Log.i(TAG, "onCreate: paramsMap = " + paramsMap.toString());
//        SendRequest.accountInit(paramsMap, new GenericsCallback(new JsonGenericsSerializator()) {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//
//            }
//
//            @Override
//            public void onResponse(Object response, int id) {
//
//            }
//        });

    }

    public void onClickLogin(View view) {
        String phone = binding.phoneEditText.getText().toString().trim();
        String code = binding.codeEditText.getText().toString().trim();
        if (!CommonUtil.isPhone(getApplicationContext(), phone)) {
            return;
        } else if (CommonUtil.isBlank(code)) {
            ToastUtils.showShort(getApplicationContext(), "请输入短信验证码");
            return;
        }
        if (!binding.checkView.isSelected()) {
            ToastUtils.showShort(getApplicationContext(), "请同意服务条款");
            return;
        }
        String registrationID = JPushInterface.getRegistrationID(this);
        Log.i(TAG, "onClickLogin: registrationID = " + registrationID);
        SendRequest.userLogin(phone, code, registrationID, new GenericsCallback<ResultClient<UserInfo>>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(LoginActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(LoginActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ResultClient<UserInfo> response, int id) {
                if (response.isSuccess()) {
                    if (response.getData() != null) {
                        BaseApplication.getInstance().setUserInfo(response.getData());
                        finishActivity(LoginActivity.class);
                        finish();
                    } else {
                        ToastUtils.showShort(getApplicationContext(), "获取用户信息失败");
                    }
                } else {
                    ToastUtils.showShort(getApplicationContext(), !CommonUtil.isBlank(response.getMsg()) ? response.getMsg() : "获取用户信息失败");
                }
            }
        });

    }

    public void onClickSendCode(View view) {
        String phone = binding.phoneEditText.getText().toString().trim();
        if (!CommonUtil.isPhone(getApplicationContext(), phone)) {
            return;
        }
        binding.sendCodeView.setEnabled(false);
        SendRequest.sendMessageUser(phone, new GenericsCallback<BaseData>(new JsonGenericsSerializator()) {

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                LoadingManager.showLoadingDialog(LoginActivity.this);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                LoadingManager.hideLoadingDialog(LoginActivity.this);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                binding.sendCodeView.setEnabled(true);
            }

            @Override
            public void onResponse(BaseData response, int id) {
                if (response.getCode() == 0) {
                    Observable.intervalRange(1, 60, 1, 1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(final Long time) {
                            binding.sendCodeView.post(new Runnable() {
                                @Override
                                public void run() {
                                    binding.sendCodeView.setText("重新发送 (" + (60 - time) + ")");
                                    binding.sendCodeView.setTextColor(Color.parseColor("#FFFFFF"));
                                }
                            });
                            if (time == 60) {
                                binding.sendCodeView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.sendCodeView.setEnabled(true);
                                        binding.sendCodeView.setText("发送短信");
                                        binding.sendCodeView.setTextColor(Color.parseColor("#FFFFFF"));
                                    }
                                });
                            }
                        }
                    });
                } else {
                    binding.sendCodeView.setEnabled(true);
                    ToastUtils.showShort(getApplicationContext(), response.getMsg());
                }

            }
        });

    }

    public void onClickBackSwitchLogin(View view) {
        permissionsManager();
    }


    private static final int requestCode = 1100;
    private String[] permissions = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @AfterPermissionGranted(requestCode)
    private void permissionsManager() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), permissions)) {
            OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCJSConfig(getApplicationContext()), ConfigUtils.getCJSConfig(getApplicationContext()));
            openLoginActivity(true);

        } else {
            EasyPermissions.requestPermissions(this, "请同意下面的权限", requestCode, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * ***************************** 一键登录 **********************************
     */

    private void openLoginActivity(boolean show) {
        //拉取授权页方法
        OneKeyLoginManager.getInstance().openLoginAuth(false, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                Log.i(TAG, "getOpenLoginAuthStatus: " + result);
                if (1000 == code) {
                    //拉起授权页成功
                } else {
                    //拉起授权页失败
                    try {
                        JSONObject object = new JSONObject(result);
                        if (show) {
                            ToastUtils.showShort(LoginActivity.this, object.optString("innerDesc"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new OneKeyLoginListener() {
            @Override
            public void getOneKeyLoginStatus(int code, String result) {
                if (1011 == code) {

                    return;
                } else if (1000 == code) {
                    OneKeyLoginManager.getInstance().finishAuthActivity();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (!CommonUtil.isBlank(jsonObject.optString("token"))) {
                            Log.i(TAG, "getOneKeyLoginStatus: "+jsonObject.optString("token"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}