package com.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.base.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public abstract class BaseBottomSheetDialog extends BottomSheetDialog {

    public Context context;
    private BottomSheetBehavior mDialogBehavior;

    public BaseBottomSheetDialog(Context context) {
        super(context, R.style.BottomSheetDialog);
        this.context = context;
        View rootView = initContentView();
        setContentView(rootView);
        setCanceledOnTouchOutside(true);

        mDialogBehavior = BottomSheetBehavior.from((View) rootView.getParent());
        mDialogBehavior.setPeekHeight(getWindowHeight());
        //dialog滑动监听
        mDialogBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float _slideOffset) {
            }
        });

    }

    public void setPeekHeight(int height) {
        mDialogBehavior.setPeekHeight(height);

    }

    protected abstract View initContentView();

    private int getWindowHeight() {
        Resources res = context.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
}
