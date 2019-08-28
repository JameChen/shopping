package com.nahuo.library.toast.style;

/**
 *    desc   : 默认白色样式实现
 */
public class ToastWhiteStyle extends ToastBlackStyle {

    @Override
    public int getBackgroundColor() {
        return 0XFFEAEAEA;
    }

    @Override
    public int getTextColor() {
        return 0XBB000000;
    }
}