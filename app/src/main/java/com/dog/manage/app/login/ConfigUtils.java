package com.dog.manage.app.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.utils.CommonUtil;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.tool.ShanYanUIConfig;
import com.dog.manage.app.R;
import com.dog.manage.app.activity.LoginActivity;

public class ConfigUtils {

    //沉浸式竖屏样式
    public static ShanYanUIConfig getCJSConfig(final Context context) {
        /************************************************自定义控件**************************************************************/
        Drawable logBtnImgPath = context.getResources().getDrawable(R.drawable.shanyan_demo_auth_bt);
        Drawable backgruond = context.getResources().getDrawable(R.drawable.shanyan_demo_auth_no_bg);
        Drawable returnBg = context.getResources().getDrawable(R.drawable.arrow_right_right_m);
        //loading自定义加载框
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout view_dialog = (RelativeLayout) inflater.inflate(R.layout.shanyan_demo_dialog_layout, null);
        RelativeLayout.LayoutParams mLayoutParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        view_dialog.setLayoutParams(mLayoutParams3);
        view_dialog.setVisibility(View.GONE);

        //号码栏背景
        LayoutInflater numberinflater = LayoutInflater.from(context);
        RelativeLayout numberLayout = (RelativeLayout) numberinflater.inflate(R.layout.shanyan_demo_phobackground, null);
        RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numberParams.setMargins(0, 0, 0, CommonUtil.dip2px(context, 260));
        numberParams.width = CommonUtil.getScreenWidth(context) - CommonUtil.dip2px(context, 50);
        numberParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        numberParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        numberLayout.setLayoutParams(numberParams);

        Drawable uncheckedImgPath = context.getResources().getDrawable(R.drawable.check_n);
        Drawable checkedImgPath = context.getResources().getDrawable(R.drawable.check_s);

        LayoutInflater inflater1 = LayoutInflater.from(context);
        RelativeLayout relativeLayout = (RelativeLayout) inflater1.inflate(R.layout.shanyan_demo_other_login_item, null);
        RelativeLayout.LayoutParams layoutParamsOther = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsOther.setMargins(0, 0, 0, 0);
        layoutParamsOther.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParamsOther.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeLayout.setLayoutParams(layoutParamsOther);
        otherLogin(context, relativeLayout);

        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                .setActivityTranslateAnim("shanyan_demo_fade_in_anim", "shanyan_dmeo_fade_out_anim")
                //授权页导航栏：
                .setNavColor(Color.parseColor("#ffffff"))  //设置导航栏颜色
                .setNavText("")  //设置导航栏标题文字
                .setLightColor(false)
                .setStatusBarHidden(false)
                .setNavReturnBtnWidth(44)
                .setNavReturnBtnHeight(0)
                .setAuthBGImgPath(backgruond)
                .setLogoHidden(true)   //是否隐藏logo
                .setDialogDimAmount(0f)
//                .setNavReturnImgPath(returnBg)
                .setFullScreen(false)
                .setStatusBarHidden(false)
                .setAuthNavHidden(true)


                //授权页号码栏：
                .setNumberColor(Color.parseColor("#333333"))  //设置手机号码字体颜色
                .setNumFieldOffsetY(303)    //设置号码栏相对于标题栏下边缘y偏移
//                .setNumFieldOffsetBottomY(303)    //设置号码栏相对于标题栏下边缘y偏移
                .setNumberSize(32)
                .setNumFieldHeight(50)
                .setNumFieldOffsetX(0)
                .setNumFieldWidth(CommonUtil.getScreenWidth(context))


                //授权页登录按钮：
                .setLogBtnText("一键登录")  //设置登录按钮文字
                .setLogBtnTextColor(Color.parseColor("#ffffff"))   //设置登录按钮文字颜色
                .setLogBtnImgPath(context.getResources().getDrawable(R.drawable.button_theme))   //设置登录按钮图片
                .setLogBtnTextSize(18)
                .setLogBtnHeight(48)
                .setLogBtnOffsetBottomY(180)
                .setLogBtnWidth(CommonUtil.px2dip(context, CommonUtil.getScreenWidth(context)) - 136)

                //授权页隐私栏：
                .setAppPrivacyOne("用户协议", "http://download.bjshiwuwei.com/protocol.html")  //设置开发者隐私条款1名称和URL(名称，url)
                .setAppPrivacyTwo("隐私政策", "http://download.bjshiwuwei.com/service.html")  //设置开发者隐私条款2名称和URL(名称，url)
                .setAppPrivacyColor(Color.parseColor("#5C5C5C"), Color.parseColor("#333333"))    //	设置隐私条款名称颜色(基础文字颜色，协议文字颜色)
                .setPrivacyText("同意", "和", "、", "、", "并授权获取手机号")
                .setPrivacyOffsetBottomY(20)//设置隐私条款相对于屏幕下边缘y偏
                .setPrivacyState(false)
                .setUncheckedImgPath(uncheckedImgPath)
                .setCheckedImgPath(checkedImgPath)
                .setPrivacyTextSize(12)
                .setPrivacyOffsetX(0)
                .setSloganHidden(true)
                .setShanYanSloganTextColor(Color.parseColor("#ffffff"))

//                .addCustomView(numberLayout, false, false, null)

                .setLoadingView(view_dialog)
                // 添加自定义控件:
                .addCustomView(relativeLayout, false, false, null)
                //标题栏下划线，可以不写
                .build();
        return uiConfig;

    }

    private static void otherLogin(final Context context, RelativeLayout relativeLayout) {
        ImageView back = relativeLayout.findViewById(R.id.back);
        View loginView = relativeLayout.findViewById(R.id.login_view);
        TextView switchLogin = relativeLayout.findViewById(R.id.switch_login);

//        RelativeLayout.LayoutParams loginViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        loginViewParams.setMargins(CommonUtil.dip2px(context, 25), 0, CommonUtil.dip2px(context, 25), CommonUtil.getScreenHeight(context) / 2);
//        loginViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        loginViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        loginView.setLayoutParams(loginViewParams);
//
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneKeyLoginManager.getInstance().finishAuthActivity();

            }
        });
        switchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
}
