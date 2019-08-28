package com.nahuo.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nahuo.quicksale.R;

/**
 * Created by jame on 2017/4/12.
 */

public class PdMenu extends Dialog {
    private Activity mActivity;
    private static PdMenu dialog = null;
    int h, w;
    View mRootView;
    TextView tv;

    public static PdMenu getInstance(Activity mActivity) {
        if (dialog == null) {
            synchronized (PdMenu.class) {
                if (dialog == null) {
                    dialog = new PdMenu(mActivity);
                }
            }
        }
        return dialog;
    }

    public PdMenu(Activity mActivity) {
        super(mActivity, R.style.popDialog);
        this.mActivity = mActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {

        h = mActivity.getResources().getDisplayMetrics().heightPixels;
        w = mActivity.getResources().getDisplayMetrics().widthPixels;
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.pd_dialog, null);
        tv = (TextView) mRootView.findViewById(R.id.toast_content_tv);
        setContentView(mRootView);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dialog != null)
            dialog = null;
    }

    public void dShow(String title) {
        if (mActivity == null||mActivity.isFinishing())
            return;
//        OnKeyListener keylistener = new OnKeyListener() {
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//
//                }
//                return false;
//            }
//        };
//        setOnKeyListener(keylistener);
        if (tv != null) {
            if (!TextUtils.isEmpty(title))
                tv.setText(title);
        }
        if (dialog!=null)
        dialog.show();
    }
//    public void show() {
////        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
////        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
////
//////        this.setWidth(w * 4 / 5);
//////        this.setHeight(h * 2 / 3);
////        this.setContentView(mRootView);
////        this.setFocusable(false);
////        this.setOutsideTouchable(false);
//////        ColorDrawable dw = new ColorDrawable(0xb0000000);
//////        this.setBackgroundDrawable(dw);
////        setBackgroundDrawable(new BitmapDrawable());
////        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
////        mContentViewBg.setVisibility(View.VISIBLE);
////        mContentViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_appear));
//
//    }


}
