package com.nahuo.quicksale.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import com.nahuo.quicksale.R;

/**
 * @description 商品规格的按钮
 * @created 2014-10-21 下午4:12:00
 * @author ZZB
 */
public class ItemSpecButton extends RadioButton {

    private static final int MIN_WIDTH = 20;
    private static final int MIN_HEIGHT = 20;

    public ItemSpecButton(Context context, String text){
        super(context);
        sharedConstructor(context);
        setText(text);
    }
    public ItemSpecButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructor(context);
//        setMinWidth(DisplayUtil.dip2px(context, MIN_WIDTH));
//        setMinHeight(DisplayUtil.dip2px(context, MIN_HEIGHT));
    }

    private void sharedConstructor(Context context){
        setBackgroundResource(R.drawable.bg_item_spect_button);
        setButtonDrawable(android.R.color.transparent);
        setGravity(Gravity.CENTER);
        
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        setTextColor(checked ? getResources().getColor(R.color.white) : getResources().getColor(R.color.lightgray));
        
    }
    

}
