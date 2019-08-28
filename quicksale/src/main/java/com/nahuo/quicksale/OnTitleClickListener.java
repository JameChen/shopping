package com.nahuo.quicksale;

import android.view.View;

/**
 * @description Title bar click
 * @created 2015年4月1日 下午2:34:46
 * @author JorsonWong
 */
public interface OnTitleClickListener {
    public abstract void onBackClick(View v);

    public abstract void onRightClick(View v);

    public abstract void onSearchClick(View v);
}
