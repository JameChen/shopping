package com.nahuo.library.controls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.nahuo.library.R;
import com.nahuo.library.helper.DisplayUtil;

public class AutoCompleteEditTextView extends AutoCompleteTextView {

    private final String TAG = "ClearEditText";
    private Drawable mIcon;
    private Drawable mClear;
    private Rect mBounds;
    private String title;
    // private String leftText;
    private OnEditTextIconClickListener onEditTextIconClickListener;


    public void setOnEditTextIconClickListener(
            OnEditTextIconClickListener listener) {
        onEditTextIconClickListener = listener;
    }

    public AutoCompleteEditTextView(Context context) {
        super(context);
        this.initEditText(context, null);
    }

    public AutoCompleteEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initEditText(context, attrs);
    }

    /**
     * 初始化edittext 控件
     * */
    @SuppressLint("Recycle")
    private void initEditText(Context context, AttributeSet attrs) {
        try {

            // 获取Title
            if (attrs != null) {
                TypedArray typedArray = context.obtainStyledAttributes(attrs,
                        R.styleable.EditTextEx);
                this.title = typedArray.getString(R.styleable.EditTextEx_ex_title);
                // this.leftText =
                // typedArray.getString(R.styleable.EditTextEx_leftText);
            }

            setEditTextDrawable();
            // 对文本内容改变进行监听
            addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable paramEditable) {
                }

                public void beforeTextChanged(CharSequence paramCharSequence,
                        int paramInt1, int paramInt2, int paramInt3) {
                }

                public void onTextChanged(CharSequence paramCharSequence,
                        int paramInt1, int paramInt2, int paramInt3) {
                    setEditTextDrawable();
                }
            });
            // 文本框失去焦点时不显示删除图标
            setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    setEditTextDrawable();
                }
            });
        } catch (Exception ex) {
            Log.e(TAG, "初始化异常：" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 设置标题
     * */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // 根据屏幕密度，将sp转换为px
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float fontScale = dm.scaledDensity;
        float textSize = DisplayUtil.sp2px(14, fontScale);
        // 设置字体样式
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(Color.BLACK);
        // 使用FontMetrics对象，根据字体大小计算字体的高度
        FontMetrics fontMetrics = paint.getFontMetrics();
        double fontHeight = Math.ceil(fontMetrics.descent - fontMetrics.top);
        // 计算文字在文本框的Y坐标，保证文字始终垂直居中
        float y = (float) ((getHeight() + fontHeight / 2) / 2);
        // 绘制文本
        float x = 15;
        canvas.drawText(this.title == null ? "" : this.title, x, y, paint);
        super.onDraw(canvas);
    }

    /**
     * 控制图片的显示
     * */
    private void setEditTextDrawable() {
        if (getText().toString().length() > 0 && this.isFocused()) {
            setCompoundDrawables(this.mIcon, null, this.mClear, null);
        } else {
            setCompoundDrawables(this.mIcon, null, null, null);
        }
    }

    public void setEditTextDrawableLeft(Drawable drawable) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());
        }
        if (getText().toString().length() > 0 && this.isFocused()) {
            setCompoundDrawables(drawable, null, this.mClear, null);
        } else {
            setCompoundDrawables(drawable, null, null, null);
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        this.mIcon = null;
        this.mClear = null;
        this.mBounds = null;
    }

    /**
     * 添加触摸事件
     * */
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        int i = (int) paramMotionEvent.getX();
        int width = this.getWidth();
        if (i < (width / 2f)) {
            if ((this.mIcon != null)
                    && (paramMotionEvent.getAction() == MotionEvent.ACTION_UP)) {
                Rect rect = this.mIcon.getBounds();
                if (i > 0 && i <= rect.width()) {
                    if (onEditTextIconClickListener != null) {
                        onEditTextIconClickListener.onIconClick(this,
                                paramMotionEvent);
                    }
                    paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
                }
            }
        } else if (i > (width / 2f)) {
            if ((this.mClear != null)
                    && (paramMotionEvent.getAction() == MotionEvent.ACTION_UP)) {
                this.mBounds = this.mClear.getBounds();
                if (i > width - this.mBounds.width() * 1.5) {
                    if (this.isFocused())
                        setText("");
                    paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
                }
            }
        }
        return super.onTouchEvent(paramMotionEvent);
    }

    /**
     * 设置显示的图片资源
     * */
    public void setCompoundDrawables(Drawable paramDrawable1,
            Drawable paramDrawable2, Drawable paramDrawable3,
            Drawable paramDrawable4) {
        if (paramDrawable1 != null)
            this.mIcon = paramDrawable1;
        if (paramDrawable3 != null)
            this.mClear = paramDrawable3;
        super.setCompoundDrawables(paramDrawable1, paramDrawable2,
                paramDrawable3, paramDrawable4);
    }

    public interface OnEditTextIconClickListener {
        void onIconClick(View view, MotionEvent event);
    }
}
