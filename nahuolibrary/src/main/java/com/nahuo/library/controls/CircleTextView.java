package com.nahuo.library.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.library.R;

/**
 * Description:圆形的text view,属性有textSize, textColor, background
 * 2014-7-23 上午10:36:37
 * @author ZZB
 */
public class CircleTextView extends RelativeLayout {
    public TextView getmCircleText() {
        return mCircleText;
    }

    private TextView mCircleText;

    private View mContentView;

    public CircleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.circle_textview, this, true);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.circle_textview);

        int textColorResId = ta.getResourceId(R.styleable.circle_textview_textColor,
                android.R.color.white);
        float textSize = ta.getInt(R.styleable.circle_textview_textSize, 12);
        int backgroundResId = ta.getResourceId(R.styleable.circle_textview_circlebackground,
                R.color.pin_huo_red);
//        int radius = ta.getInteger(R.styleable.circle_textview_radius, 20);
        
        //设置文字属性
        mCircleText = (TextView) findViewById(R.id.text);
        mCircleText.setTextColor(getResources().getColor(textColorResId));
        mCircleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
//        mCircleText.setGravity()
        //设置圆形背景
        mContentView = (View) mCircleText.getParent();
//        GradientDrawable p = (GradientDrawable) mContentView.getBackground();
//        p.setColor(getResources().getColor(backgroundResId));
        setBackground(backgroundResId);
        
        //设置圆的半径
        setRadius();
        ta.recycle();

    }

    public CircleTextView(Context context){
        super(context);
        mCircleText = (TextView) findViewById(R.id.text);
        mContentView = (View) mCircleText.getParent();
    }
    
    public void setText(String text){
        mCircleText.setText(text);
    }
    public void setText(int resid){
        String text = getResources().getText(resid).toString();
        mCircleText.setText(text);
    }
    public void setTextSize(float size){
        mCircleText.setTextSize(size);
    }
    public void setTextColor(int resid){
        mCircleText.setTextColor(getResources().getColor(resid));
    }
    public void setBackground(int resid){
        GradientDrawable p = (GradientDrawable) mContentView.getBackground();
        p.setColor(getResources().getColor(resid));
    }
    /**
     * Description:设置半径
     * 2014-7-23 上午10:47:03
     * @author ZZB
     */
    public void setRadius(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        mContentView.setLayoutParams(params);
    }

}
