package com.nahuo.quicksale.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;

import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;

/**
 * @description 商品规格的按钮
 * @created 2014-10-21 下午4:12:00
 * @author ZZB
 */
public class ItemStyleButton extends RadioButton {

    public ItemStyleButton(Context context, String text) {
        super(context);
        sharedConstructor(context);
        setText(text);
    }

    public ItemStyleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructor(context);
//        setMinWidth(DisplayUtil.dip2px(context, 60));
//        setMinHeight(DisplayUtil.dip2px(context, 30));
        //useless
        setMinimumWidth(DisplayUtil.dip2px(context, 160));
        setMinimumHeight(DisplayUtil.dip2px(context, 130));
    }

    private void sharedConstructor(Context context) {
        setBackgroundResource(R.drawable.bg_item_style_btn);
        setButtonDrawable(android.R.color.transparent);
        setGravity(Gravity.CENTER);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        setTextColor(checked ? getResources().getColor(R.color.white) : getResources().getColor(
                R.color.black));
    }

}
