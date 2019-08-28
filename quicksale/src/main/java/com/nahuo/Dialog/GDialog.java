package com.nahuo.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.customview.PinHuoTextView;


public class GDialog extends Dialog implements View.OnClickListener {

    private Activity mActivity;
    private View mRootView;
    private LinearLayout mContentViewBg;
    private TextView mTvTitle;PinHuoTextView mTvMessage;
    private Button mBtnCancel, mBtnOK;
    private PopDialogListener mPositivePopDialogListener;
    private PopDialogListener mNegativePopDialogListener;
    private LinearLayout mContentView;
    private boolean isShowDismiss = true;
    static GDialog dialog;
    public static final int BUTTON_POSITIVIE = 1;
    public static final int BUTTON_NEGATIVE = 0;
    String content = "", left = "", right;

    public  enum  DialogType {
        D_PAY, D_FINISH
    }

    public void setIsShowDismiss(boolean showDismiss) {
        isShowDismiss = showDismiss;
        mBtnCancel.setVisibility(isShowDismiss ? View.VISIBLE : View.GONE);
    }

    public static GDialog getInstance(Activity activity) {
        if (dialog == null) {
            dialog = new GDialog(activity);
        }
        return dialog;
    }

    public GDialog(Activity activity) {
        super(activity, R.style.popDialog);
        this.mActivity = activity;
        initViews();
    }

    public View getmRootView() {
        return mRootView;
    }


    private void initViews() {
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.light_popwindow_dialogx, null);
        mContentViewBg = (LinearLayout) mRootView.findViewById(R.id.contentView);
        mTvTitle = (TextView) mContentViewBg.findViewById(R.id.tv_title);
        mContentView = (LinearLayout) mRootView.findViewById(R.id.ll_content);
        mTvMessage = (PinHuoTextView) mContentViewBg.findViewById(R.id.tv_message);
        mTvMessage.setLetterSpacing(2);
        mBtnCancel = (Button) mContentViewBg.findViewById(R.id.btn_cancle);
        mBtnOK = (Button) mContentViewBg.findViewById(R.id.btn_ok);
        mBtnCancel.setOnClickListener(this);
        mBtnOK.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        OnKeyListener keylistener = new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        this.setContentView(mRootView);
        setOnKeyListener(keylistener);
        setCancelable(true);

//        mRootView.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = mContentViewBg.getTop();
//                int bottom = mContentViewBg.getBottom();
//
//                int y = (int)event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height || y > bottom) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });

       /* this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);*/

//        this.setFocusable(true);
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        this.setBackgroundDrawable(dw);
//        setAnimationStyle(R.style.LightPopDialogAnim);
    }

    public void showDialog() {
//        if (!TextUtils.isEmpty(content)) {
//            if (tv_title != null)
//                tv_title.setText(content);
//        }   if (mTvMessage!=null)
        if (mTvMessage!=null)
            mTvMessage.setText(content);
        if (mBtnOK!=null)
            mBtnOK.setText(left);
        if (mBtnCancel!=null)
            mBtnCancel.setText(right);
        if (dialog!=null)
        dialog.show();
    }
//    public void show() {
//        DisplayMetrics dm = new DisplayMetrics();
//        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        // int screenWidth = dm.widthPixels;
//        int screenHeight = dm.heightPixels;
//        int top = mContentViewBg.getTop();
//        int bottom = mContentViewBg.getBottom();
//        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, screenHeight / 2 - (bottom - top) / 2);
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            if (mPositivePopDialogListener != null) {
                mPositivePopDialogListener.onPopDialogButtonClick(BUTTON_POSITIVIE,type);
            }
            dialog = null;
            dismiss();
        } else if (id == R.id.btn_cancle) {
            if (mPositivePopDialogListener != null) {
                mPositivePopDialogListener.onPopDialogButtonClick(BUTTON_NEGATIVE,type);
            }
            dialog = null;
            dismiss();
        }
    }

    DialogType type;

    public GDialog setDialogType (DialogType type) {
        this.type = type;
        return this;
    }

    public GDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public GDialog setLeftStr(String left) {
        this.left = left;
        return this;
    }

    public GDialog setRightStr(String right) {
        this.right = right;
        return this;
    }

    public GDialog setIcon(int resId) {
        mTvTitle.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    public GDialog setIcon(Drawable icon) {
        mTvTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        return this;
    }

    public GDialog setMessage(CharSequence message) {
        mTvMessage.setText(message);
        return this;
    }

    public GDialog setMessage(int resId) {
        mTvMessage.setText(resId);
        return this;
    }

    public GDialog setNegative(CharSequence text, PopDialogListener listener) {
        mBtnCancel.setText(text);
        mNegativePopDialogListener = listener;
        return this;
    }

    public GDialog setNegative(int resId, PopDialogListener listener) {
        mBtnCancel.setText(resId);
        mNegativePopDialogListener = listener;
        return this;
    }

    public GDialog setPositive(PopDialogListener listener) {
        mPositivePopDialogListener = listener;
        return this;
    }


    public void addContentView(View child) {
        mContentView.addView(child);
    }

    public interface PopDialogListener {
        public void onPopDialogButtonClick(int ok_cancel, DialogType type);
    }

}
