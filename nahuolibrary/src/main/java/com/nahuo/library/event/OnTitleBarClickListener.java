package com.nahuo.library.event;

import android.view.MotionEvent;
import android.view.View;

/**
 * 定义监听标题栏按钮点击的接口
 * */
public interface OnTitleBarClickListener {

	void OnBackButtonClick(View view, MotionEvent event);

	void OnLeftMenuButtonClick(View view, MotionEvent event);

	void OnRightButtonClick(View view, MotionEvent event);

	void OnRithtButtonMoreClick(View view, MotionEvent event);
}
