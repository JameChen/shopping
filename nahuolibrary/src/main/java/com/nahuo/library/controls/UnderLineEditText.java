package com.nahuo.library.controls;

import com.nahuo.library.R;
import com.nahuo.library.helper.DisplayUtil;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Description:只有下划线的EditText 2014-7-8上午10:08:22
 */
@Deprecated //过期，使用样式替换，见wp项目的style:edittext_blue
public class UnderLineEditText extends RelativeLayout {

    private Context mContext;
    private EditText mEditText;
    private View mleftView;
    private View mBottomView;
    private View mRightView;
    private View mContentView;

    public UnderLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.underline_edittext, this,
                true);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.UnderlineEditText);
        // 属性值
        CharSequence hint = ta.getText(R.styleable.UnderlineEditText_hint);
        int underLineColorId = ta.getResourceId(
                R.styleable.UnderlineEditText_underline_color,
                R.color.darkgreen);
        int bgResid = ta.getResourceId(
                R.styleable.UnderlineEditText_underbackground,
                android.R.color.transparent);
        float textSize = ta.getDimension(
                R.styleable.UnderlineEditText_text_size, -1.0f);
        float paddingLeft = ta.getDimension(
                R.styleable.UnderlineEditText_paddingLeft, 0);
        float paddingRight = ta.getDimension(
                R.styleable.UnderlineEditText_paddingRight, 0);
        float paddingTop = ta.getDimension(
                R.styleable.UnderlineEditText_paddingTop, 0);
        float paddingBottom = ta.getDimension(
                R.styleable.UnderlineEditText_paddingBottom, 0);
        int inputType = ta.getInt(R.styleable.UnderlineEditText_inputType, -1);
        boolean editable = ta.getBoolean(R.styleable.UnderlineEditText_editable, true);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mleftView = findViewById(R.id.left_view);
        mBottomView = findViewById(R.id.bottom_view);
        mRightView = findViewById(R.id.right_view);
        mContentView = findViewById(R.id.layout_base);

        if (hint != null) {
            mEditText.setHint(hint);
        }

        if (textSize != -1.0f) {
            mEditText.setTextSize(textSize);
        }

        if (inputType != -1) {
            setInputType(inputType);
        }
        setUnderLineColor(underLineColorId);
        mContentView.setBackgroundResource(bgResid);

        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        
        if(!editable){//不可点击
            mEditText.setKeyListener(null);
        }
        ta.recycle();
    }

    public UnderLineEditText(Context context) {
        this(context, "", -1);
    }

    public UnderLineEditText(Context context, String hint,
            int underlineColorRedid) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.underline_edittext, this,
                true);
        mEditText = (EditText) findViewById(R.id.edit_text);
        mleftView = findViewById(R.id.left_view);
        mBottomView = findViewById(R.id.bottom_view);
        mRightView = findViewById(R.id.right_view);
        mContentView = findViewById(R.id.layout_base);
        if (hint != null) {
            mEditText.setHint(hint);
        }
        if (underlineColorRedid > 0) {
            setUnderLineColor(underlineColorRedid);
        }

    }

    public UnderLineEditText(Context context, int hintResid,
            int underlineColorRedid) {
        this(context, context.getString(hintResid), underlineColorRedid);
    }

    public UnderLineEditText(Context context, int hintResid) {
        this(context, context.getString(hintResid), -1);
    }

    public UnderLineEditText(Context context, String hint) {
        this(context, hint, -1);
    }

    /**
     * @param inputType
     *            @see {@link EditorInfo#inputType}
     *            eg:EditorInfo.TYPE_CLASS_NUMBER
     * @time 2014-8-12 下午4:44:15
     */
    public void setInputType(int inputType) {
        switch (inputType) {
        case EditorInfo.TYPE_NUMBER_FLAG_DECIMAL:
            mEditText.setRawInputType(Configuration.KEYBOARD_12KEY);
            break;
        default:
            mEditText.setInputType(inputType);
            break;
        }

    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }

    public void setHint(int resid) {
        mEditText.setHint(resid);
    }

    public void setUnderLineColor(int resid) {
        mleftView.setBackgroundResource(resid);
        mBottomView.setBackgroundResource(resid);
        mRightView.setBackgroundResource(resid);
    }

    public void setBackgroundResource(int resid) {
        mContentView.setBackgroundResource(resid);
    }

    public String getText() {
        return mEditText.getText().toString();
    }

    public void setText(String text) {
        mEditText.setText(text);
        mEditText.setSelection(text.length());
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mEditText.addTextChangedListener(watcher);
    }

    public void setPadding(float paddingLeft, float paddingTop,
            float paddingRight, float paddingBottom) {
        mContentView.setPadding(DisplayUtil.dip2px(mContext, paddingLeft),
                DisplayUtil.dip2px(mContext, paddingTop),
                DisplayUtil.dip2px(mContext, paddingRight),
                DisplayUtil.dip2px(mContext, paddingBottom));
    }

    // 设置为数字
    public void setNumeric(boolean f) {
        if (f) {
            mEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        }
    }

    // 设置输入框的最大的位数
    public void setMaxLength(int len)
    {
        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(len) });
    }

    public void setError(CharSequence error) {
        mEditText.setError(Html.fromHtml("<font color='black'>" + error
                + "</font>"));
    }

    public void setError(CharSequence error, Drawable icon) {
        mEditText
                .setError(
                        Html.fromHtml("<font color='black'>" + error
                                + "</font>"), icon);
    }
    
    public IBinder getWindowToken(){
        return mEditText.getWindowToken();
    }

}
