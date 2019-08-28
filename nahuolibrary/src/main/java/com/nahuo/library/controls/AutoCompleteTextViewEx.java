package com.nahuo.library.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.R;
import com.nahuo.library.helper.DisplayUtil;

/**
 * @author JorsonWong
 * @description 输入匹配控件
 * @created 2015年3月20日 下午3:00:42
 */
public class AutoCompleteTextViewEx extends AutoCompleteTextView {

    private final String TAG = "AutoCompleteTextViewEx";
    private Drawable mIcon;
    private Drawable mClear;
    private Rect mBounds;
    private OnEditTextIconClickListener onEditTextIconClickListener;
    private OnSearchLogDeleteListener mOnSearchLogDeleteListener;
    private LogAdapter mAdapter;

    public void setOnEditTextIconClickListener(
            OnEditTextIconClickListener listener) {
        onEditTextIconClickListener = listener;
    }

    public void setOnSearchLogDeleteListener(OnSearchLogDeleteListener listener) {
        mOnSearchLogDeleteListener = listener;
    }

    public AutoCompleteTextViewEx(Context context) {
        super(context);
        this.initEditText(context, null);
    }

    public AutoCompleteTextViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initEditText(context, attrs);
    }

    /**
     * @param data      字符串
     * @param separator 分隔符
     */
    public void populateData(String data, String separator) {
        if (!TextUtils.isEmpty(data) && data.contains(separator)) {
            String[] texts = data.split(separator);
//            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                    getContext(), R.layout.auto_complete_textview_item, texts);
            mAdapter = new LogAdapter(getContext(), texts);
            setAdapter(mAdapter);
        } else {
            mAdapter = new LogAdapter(getContext(), new String[]{});
            setAdapter(mAdapter);
        }
    }

    public Filter getFilter() {
        return mAdapter.getFilter();
    }

    private void initEditText(Context context, AttributeSet attrs) {
        setThreshold(1);
        try {
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

        setDropDownBackgroundResource(R.drawable.bg_pop_auto_complete_textview);
        setDropDownVerticalOffset(3);

    }

    /**
     * 设置标题
     */
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
        super.onDraw(canvas);
    }

    /**
     * 控制图片的显示
     */
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
     */
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
     */
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

    public interface OnSearchLogDeleteListener {
        void onSearchLogDeleted(String text);
    }

    private final static class ViewHolder {
        private TextView textView;
        private ImageView ivDel;
    }


    public class LogAdapter extends ArrayAdapter<String> {

        private LayoutInflater mInflater = null;
        private Context mContext;

        public LogAdapter(Context context, String[] items) {
            super(context, 0, items);
            mContext = context;
            mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.auto_complete_textview_item,
                        parent, false);
                holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
                holder.ivDel = (ImageView) convertView.findViewById(android.R.id.cut);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String text = getItem(position);
            holder.textView.setText(text);
            holder.ivDel.setVisibility(mOnSearchLogDeleteListener != null ? View.VISIBLE : View.GONE);
            holder.ivDel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSearchLogDeleteListener != null) {
                        mOnSearchLogDeleteListener.onSearchLogDeleted(text);
                    }
                }
            });

            return convertView;
        }
    }
}
