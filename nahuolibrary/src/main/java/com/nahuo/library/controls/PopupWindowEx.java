package com.nahuo.library.controls;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.nahuo.library.helper.FunctionHelper;

/**
 * 自定义PopupWindow
 * */
public class PopupWindowEx extends PopupWindow {

  private View mContentView;
  private int mResId;

  public PopupWindowEx(Context context) {
    super(context);
  }

  /**
   * PopupWindow构造函数
   * 
   * @param contentView 整个PopupWindow布局视图
   * @param resId 内容视图资源ID
   * @param width PopupWindow的宽度
   * @param height PopupWindow的高度
   * @param focusable PopupWindow是否可以点击
   * */
  public PopupWindowEx(View contentView, int resId, int width, int height, boolean focusable) {
    super(contentView, width, height, focusable);
    init(contentView, resId);
  }

  private void init(View contentView, int resId) {
    // 内容视图
    this.mContentView = contentView;
    this.mResId = resId;
    // 设置点击窗口外边窗口消失
    this.setOutsideTouchable(true);
    // 设置半透明背景色
    ColorDrawable dw = new ColorDrawable(0x50000000);
    this.setBackgroundDrawable(dw);
    // 设置弹出窗体需要软键盘
    this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
    // 设置软键盘覆盖模式
    this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    // 点击内容视图区域外时窗口消失
    mContentView.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        int height = mContentView.findViewById(mResId).getTop();
  
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
          // 隐藏软键盘
          FunctionHelper.hideSoftInput(v.getWindowToken(), mContentView.getContext());
          // 关闭PopupWindow
          if (y < height) {
            dismiss();
          }
        }
        return true;
      }
    });
  }
}
