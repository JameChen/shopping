package com.nahuo.quicksale.controls;

import com.nahuo.quicksale.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WidgetSettingItem extends RelativeLayout {

    private View mRootView;
    private ImageView mIconLeft, mIconRight;
    private TextView  mTvLeft, mTvRight;
    private View      mLineBottom;
    private Drawable  mLeftIconDw, mRightIconDw;
    private String    mLeftText, mRightText;
    private boolean   mShowBottomLine;
    private Drawable  mRightTextIcon;

    public WidgetSettingItem(Context context) {
        super(context);
        initView();
    }

    public WidgetSettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        readAttrs(attrs);
    }

    private void initView() {
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_setting_item, this, true);
        mIconLeft = (ImageView)mRootView.findViewById(R.id.iv_left_icon);
        mTvLeft = (TextView)mRootView.findViewById(R.id.tv_left_text);
        mTvRight = (TextView)mRootView.findViewById(R.id.tv_right_text);
        mIconRight = (ImageView)mRootView.findViewById(R.id.iv_right_icon);
        mLineBottom = mRootView.findViewById(R.id.line);
    }

    public void setLeftIcon(Drawable icon) {
        mIconLeft.setVisibility(icon == null ? View.GONE : View.VISIBLE);
        mIconLeft.setImageDrawable(icon);
    }

    public void setRightIcon(Drawable icon) {
        mIconRight.setImageDrawable(icon);
    }

    public void setLeftText(CharSequence text) {
        mTvLeft.setText(text);
    }

    public void setRightText(CharSequence text) {
        mTvRight.setText(text);
    }

    public void setBottomLine(boolean show) {
        mLineBottom.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setRightTextIcon(Drawable icon) {
        mTvRight.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
    }

    private void readAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.WidgetSettingItem);
        mLeftIconDw = ta.getDrawable(R.styleable.WidgetSettingItem_wsiLeftIcon);
        mRightIconDw = ta.getDrawable(R.styleable.WidgetSettingItem_wsiRightIcon);
        mLeftText = ta.getString(R.styleable.WidgetSettingItem_wsiLeftText);
        mRightText = ta.getString(R.styleable.WidgetSettingItem_wsiRightText);
        mRightTextIcon = ta.getDrawable(R.styleable.WidgetSettingItem_wsiRightTextIcon);

        mShowBottomLine = ta.getBoolean(R.styleable.WidgetSettingItem_wsiShowbottomLine, true);
        ta.recycle();

        setLeftIcon(mLeftIconDw);
        if (mLeftText != null)
            setLeftText(mLeftText);
        if (mRightIconDw != null)
            setRightIcon(mRightIconDw);
        if (mRightText != null)
            setRightText(mRightText);

        if (mRightTextIcon != null)
            setRightTextIcon(mRightTextIcon);

        setBottomLine(mShowBottomLine);

    }
}
