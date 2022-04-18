package com.dog.manage.app.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.base.utils.CommonUtil;
import com.base.utils.ToastUtils;
import com.dog.manage.app.MyClickableSpan;
import com.dog.manage.app.R;
import com.dog.manage.app.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private boolean privacyState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewData(R.layout.activity_login);
        addActivity(this);

        privacyState = getIntent().getBooleanExtra("privacyState",false);
        binding.checkView.setSelected(privacyState);
        binding.checkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.checkView.setSelected(!binding.checkView.isSelected());
            }
        });

        String userText = "隐私条款";
        String yinsiText = "隐私协议";
        String content = "登录代表同意隐私条款和隐私协议";
        SpannableString spannableString = new SpannableString(content);
        // 设置字体颜色
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#489DFA")), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#489DFA")), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体大小
//          spannableString.setSpan(new AbsoluteSizeSpan(CommonUtil.sp2px(mContext, 14)), content.indexOf(stateText), content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置点击
        spannableString.setSpan(new MyClickableSpan(LoginActivity.this,userText), content.indexOf(userText), content.indexOf(userText) + userText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new MyClickableSpan(LoginActivity.this,yinsiText), content.indexOf(yinsiText), content.indexOf(yinsiText) + yinsiText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.privacyView.setText(spannableString);
        binding.privacyView.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件

    }

    public void onClickLogin(View view) {
//        String phone = binding.phoneEditText.getText().toString().trim();
//        if (!CommonUtil.isPhone(getApplicationContext(), phone)) {
//            return;
//        }
//        if (!binding.checkView.isSelected()) {
//            ToastUtils.showShort(getApplicationContext(), "请同意服务条款");
//            return;
//        }
        Bundle bundle = new Bundle();
//        bundle.putString("phone", phone);
        openActivity(MainActivity.class, bundle);

    }

    public void onClickSendCode(View view) {
    }
}