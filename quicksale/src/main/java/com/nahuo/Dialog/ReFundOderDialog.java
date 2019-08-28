package com.nahuo.Dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nahuo.quicksale.R;


public class ReFundOderDialog extends PopupWindow implements View.OnClickListener {

    private Activity          mActivity;
    private View              mRootView;
    private LinearLayout      mContentViewBg;
    private TextView          mTvTitle;
    private TextView mTvMessage;
    private Button            mBtnCancel, mBtnOK;
    private PopDialogListener mPositivePopDialogListener;
    private PopDialogListener mNegativePopDialogListener;
    private LinearLayout      mContentView;

    public static final int   BUTTON_POSITIVIE = 1;
    public static final int   BUTTON_NEGATIVE  = 0;
    public  boolean isHasTittle=true;

    public ReFundOderDialog setHasTittle(boolean hasTittle) {
        isHasTittle = hasTittle;
        return this;
    }

    public ReFundOderDialog(Activity activity) {
        super();
        this.mActivity = activity;
        initViews();
    }

    public ReFundOderDialog(Activity activity, AttributeSet atrr) {
        super();
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.layout_refund_order , null);
        mContentViewBg = (LinearLayout)mRootView.findViewById(R.id.contentView);
        mTvTitle = (TextView)mContentViewBg.findViewById(R.id.tv_title);
        mContentView = (LinearLayout)mRootView.findViewById(R.id.ll_content);
        mTvMessage = (TextView) mContentViewBg.findViewById(R.id.tv_message);
       // mTvMessage.setLetterSpacing(2);
        mBtnCancel = (Button)mContentViewBg.findViewById(R.id.btn_cancle);
        mBtnOK = (Button)mContentViewBg.findViewById(R.id.btn_ok);
        mBtnCancel.setOnClickListener(this);
        mBtnOK.setOnClickListener(this);
        mRootView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mContentViewBg.getTop();
                int bottom = mContentViewBg.getBottom();

                int y = (int)event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height || y > bottom) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        setAnimationStyle(R.style.LightPopDialogAnim);

    }

    public void show() {
        if (isHasTittle){
            if (mTvTitle!=null){
                mTvTitle.setVisibility(View.VISIBLE);
            }
        }else {
            if (mTvTitle!=null){
                mTvTitle.setVisibility(View.GONE);
            }
        }
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        // int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        int top = mContentViewBg.getTop();
        int bottom = mContentViewBg.getBottom();

        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, screenHeight / 2 - (bottom - top) / 2);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            if (mPositivePopDialogListener != null) {
                mPositivePopDialogListener.onPopDialogButtonClick(BUTTON_POSITIVIE,this);
            }
          //  dismiss();
        } else if (id == R.id.btn_cancle) {
            if (mNegativePopDialogListener != null) {
                mNegativePopDialogListener.onPopDialogButtonClick(BUTTON_NEGATIVE,this);
            }
            dismiss();
        }
    }

    public ReFundOderDialog setTitle(CharSequence title) {
        mTvTitle.setText(title);
        return this;
    }

    public ReFundOderDialog setTitle(int resId) {
        mTvTitle.setText(resId);
        return this;
    }

    public ReFundOderDialog setIcon(int resId) {
        mTvTitle.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
        return this;
    }

    public ReFundOderDialog setIcon(Drawable icon) {
        mTvTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        return this;
    }

    public ReFundOderDialog setMessage(Spanned message) {
        mTvMessage.setText(message,TextView.BufferType.SPANNABLE);
        return this;
    }

    public ReFundOderDialog setMessage(int resId) {
        mTvMessage.setText(resId);
        return this;
    }
    public ReFundOderDialog setNegative(CharSequence text, PopDialogListener listener) {
        mBtnCancel.setText(text);
        mNegativePopDialogListener = listener;
        return this;
    }

    public ReFundOderDialog setNegative(int resId, PopDialogListener listener) {
        mBtnCancel.setText(resId);
        mNegativePopDialogListener = listener;
        return this;
    }

    public ReFundOderDialog setPositive(CharSequence text, PopDialogListener listener) {
        mBtnOK.setText(text);
        mPositivePopDialogListener = listener;
        return this;
    }

    public ReFundOderDialog setPositive(int resId, PopDialogListener listener) {
        mBtnOK.setText(resId);
        mPositivePopDialogListener = listener;
        return this;
    }

    public void addContentView(View child) {
        mContentView.addView(child);
    }

    public interface PopDialogListener {
        public void onPopDialogButtonClick(int which, ReFundOderDialog dialog);
    }

}
