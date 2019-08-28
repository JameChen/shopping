package com.nahuo.library.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.R;
import com.nahuo.library.helper.DisplayUtil;
/**
* Description:一个单行item：默认显示左边文字与右边icon，
* 2014-7-4上午10:27:40
* -------------------------
* |左文字 右文字 > |
* -------------------------
*/
public class SingleLineItem extends FrameLayout {
    
    private TextView mLeftText;
    private TextView mRightText;
    private ImageView mRightIcon;
    private View mUnderLine;
    private EditText mRightEt;

    public SingleLineItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.single_line_item, this, true);
        
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SingleLineItem);
        //属性值
        CharSequence leftTextValue = ta.getText(R.styleable.SingleLineItem_left_text);
        CharSequence rightTextValue = ta.getText(R.styleable.SingleLineItem_right_text);
        Drawable rightIconDrawable = ta.getDrawable(R.styleable.SingleLineItem_right_icon);
        //默认隐藏右边文字
        boolean showRightText = ta.getBoolean(R.styleable.SingleLineItem_show_right_text, false);
        boolean showRightEt = ta.getBoolean(R.styleable.SingleLineItem_show_right_edittext, false);
        //默认显示右边icon
        boolean showRightIcon = ta.getBoolean(R.styleable.SingleLineItem_show_right_icon, true);
        //默认显示下划线
        boolean showUnderLine = ta.getBoolean(R.styleable.SingleLineItem_show_underline, true);
        //文字大小:默认 12sp
//        float textSize = ta.getDimension(R.styleable.SingleLineItem_text_size, 12);
        //左边文字颜色
        int leftTextColorId = ta.getResourceId(R.styleable.SingleLineItem_left_text_color, -1);
        //右边文字颜色
        int rightTextColorId = ta.getResourceId(R.styleable.SingleLineItem_right_text_color, -1);
        
        
        int rightTextMaxWidth = ta.getInteger(R.styleable.SingleLineItem_right_text_max_width, -1);
        int rightEtMaxWidth = ta.getInteger(R.styleable.SingleLineItem_right_et_max_width, -1);
		
        mLeftText = (TextView) findViewById(R.id.left_text);
        mRightIcon = (ImageView) findViewById(R.id.right_icon);
        mRightText = (TextView) findViewById(R.id.right_text);
        mUnderLine = findViewById(R.id.under_line);
        mRightEt = (EditText) findViewById(R.id.right_edittext);
        
        if(null != leftTextValue){
            mLeftText.setText(leftTextValue);
        }
        if (null!=rightTextValue){
            mRightText.setText(rightTextValue);
        }
        if(null != rightIconDrawable){
            mRightIcon.setImageDrawable(rightIconDrawable);
        }
//        //left text
//        mLeftText.setTextSize(textSize);
//        if(leftTextColorId != -1){
//            setLeftTextColor(leftTextColorId);
//        }
        
        
        //right text
//        mRightText.setTextSize(textSize);
        if(rightTextColorId != -1){
            setRightTextColorId(rightTextColorId);
        }
        if(rightTextMaxWidth != -1){
            mRightText.setMaxWidth(DisplayUtil.dip2px(context, rightTextMaxWidth));
            mRightText.setEllipsize(TruncateAt.END);
            mRightText.setSingleLine(true);
        }
        
        if(rightEtMaxWidth != -1){
            mRightEt.setMaxWidth(DisplayUtil.dip2px(context, rightEtMaxWidth));
            mRightEt.setEllipsize(TruncateAt.END);
            mRightEt.setSingleLine(true);
        }
        mRightText.setVisibility(showRightText ? View.VISIBLE : View.GONE);
        mRightEt.setVisibility(showRightEt ? View.VISIBLE : View.GONE);
        mRightIcon.setVisibility(showRightIcon ? View.VISIBLE : View.GONE);
        mUnderLine.setVisibility(showUnderLine ? View.VISIBLE : View.GONE);
        
        
        ta.recycle();
    }
    public EditText getRightEt(){
        return mRightEt;
    }
    public void setRightEditText(int resId){
        mRightEt.setText(resId);
    }
    public void setRightEditText(String text){
        mRightEt.setText(text);
    }
    public void setRightText(int resId){
        mRightText.setText(resId);
    }
    public void setRightText(String text){
        mRightText.setText(text);
    }
    
	public void setRightText(android.text.Spanned text) {
		mRightText.setText(text);
	}
    
    public void setRightTextColorId(int resid){
        mRightText.setTextColor(getResources().getColor(resid));
    }
    
    public void setLeftText(CharSequence text){
        mLeftText.setText(text);
    }
    public void setLeftText(int resId){
        mLeftText.setText(resId);
    }
    public void setLeftText(String text){
        mLeftText.setText(text);
    }
    public void setLeftTextColor(int resid){
        mLeftText.setTextColor(getResources().getColor(resid));
    }
    
    public void setRightIcon(Drawable drawable){
        mRightIcon.setImageDrawable(drawable);
    }
  
    public String getLeftText(){
        return mLeftText.getText().toString();
    }
    
    public String getRightText(){
        return mRightText.getText().toString();
    }
    
    
}