package com.nahuo.quicksale.controls;

import com.nahuo.library.controls.EditTextEx;
import com.nahuo.quicksale.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchEditText extends EditText {

    private CharSequence mHint;
    private Drawable     mSearchIcon;
    private int          mGravity;
    private Drawable     mCleanIcon;
    private View         mRootView;
    private ImageView    mIvSearch;
    private TextView     mTvHint;
    private EditTextEx   mEditText;

    public SearchEditText(Context context) {
        super(context);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        readAttrs(attrs);
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setHighlightColor(Color.parseColor("#9B9B9F"));
        setTextSize(TypedValue.COMPLEX_UNIT_PX, sp2px(getContext(), 16));
        setBackgroundResource(R.drawable.bg_search_edittext);

        this.mHint = getHint();

        setHintWithIcon(mHint);

    }

    private void readAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchEdit);
//        this.mHint = typedArray.getString(R.styleable.SearchEdit_HintText);
        this.mSearchIcon = typedArray.getDrawable(R.styleable.SearchEdit_SearchIcon);
        mGravity = typedArray.getInt(R.styleable.SearchEdit_SearchIconGravity, 0);
        this.mCleanIcon = typedArray.getDrawable(R.styleable.SearchEdit_CleanIcon);

        typedArray.recycle();

        setHintWithIcon(mHint);

    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5f);
    }

    public void setHintWithIcon(CharSequence text) {
        Drawable drawable = getResources().getDrawable(R.drawable.find);
        drawable.setBounds(0, 0, (int)(0.5 * drawable.getIntrinsicWidth()), (int)(0.5 * drawable.getIntrinsicHeight()));
        // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString spannable = new SpannableString("[search]" + text);
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spannable.setSpan(span, 0, "[search]".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        setHint(spannable);
    }

}
