package com.nahuo.quicksale.controls;

import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.SpManager;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PromptDialog extends PopupWindow implements View.OnClickListener {

    private Activity        mActivity;
    private View            mRootView;
    private TextView        mTvMessage;
    private Button          mBtnOK;
    private OnClickListener mPositiveClickListener;
    private int             screenHeight;
    private int             screenWidth;
    private String          mPrefKeyNoPromptMore;
    private CheckBox        mCbNoPromptAgain;
    private LinearLayout    mDialogContent;

    public static final int BUTTON_POSITIVIE = 1;
    public static final int BUTTON_NEGATIVE  = 0;

    public PromptDialog(Activity activity) {
        super();
        this.mActivity = activity;
        initViews();
    }

    public PromptDialog(Activity activity, AttributeSet atrr) {
        super();
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.prompt_dialog, null);
        mDialogContent = (LinearLayout)mRootView.findViewById(android.R.id.content);
        mTvMessage = (TextView)mRootView.findViewById(R.id.tv_message);
        mCbNoPromptAgain = (CheckBox)mRootView.findViewById(android.R.id.checkbox);
        mBtnOK = (Button)mRootView.findViewById(android.R.id.button1);
        mBtnOK.setOnClickListener(this);

        mRootView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int top = mDialogContent.getTop();
                int bottom = mDialogContent.getBottom();

                int y = (int)event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top || y > bottom) {
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

        // Drawable bg = mActivity.getResources().getDrawable(R.drawable.bg_prompt_dialog);

        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        setAnimationStyle(R.style.LightPopDialogAnim);

    }

    public void show() {
        boolean isNotShowAgain = SpManager.getBoolean(mActivity, mPrefKeyNoPromptMore, false);
        if (!isNotShowAgain) {// 若已勾选不再显示的就不显示
            mCbNoPromptAgain.setVisibility(TextUtils.isEmpty(mPrefKeyNoPromptMore) ? View.GONE : View.VISIBLE);
            showAtLocation(mActivity.getWindow().getDecorView(), Gravity.NO_GRAVITY, (screenWidth - getWidth()) / 2,
                    screenHeight / 2 - DisplayUtil.dip2px(mActivity, 80));
        } else {// 直接回调
            if (mPositiveClickListener != null) {
                mPositiveClickListener.onClick(mBtnOK);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == android.R.id.button1) {
            boolean isChecked = mCbNoPromptAgain.isChecked();
            if (!TextUtils.isEmpty(mPrefKeyNoPromptMore) && isChecked) {
                SpManager.setBoolean(mActivity, mPrefKeyNoPromptMore, isChecked);
            }
            if (mPositiveClickListener != null) {
                mPositiveClickListener.onClick(v);
            }
            dismiss();
        }
    }

    public PromptDialog setMessage(CharSequence message) {
        mTvMessage.setText(message);
        return this;
    }

    public PromptDialog setMessage(int resId) {
        mTvMessage.setText(resId);
        return this;
    }

    public PromptDialog setPositive(CharSequence text, View.OnClickListener listener) {
        mBtnOK.setText(text);
        mPositiveClickListener = listener;
        return this;
    }

    public PromptDialog setPositive(int resId, View.OnClickListener listener) {
        mBtnOK.setText(resId);
        mPositiveClickListener = listener;
        return this;
    }

    public PromptDialog setNoPromptMore(String prefKey) {
        mPrefKeyNoPromptMore = prefKey;
        return this;
    }

}
