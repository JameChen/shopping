package com.nahuo.library.event;

/**
 * 定义监听标题栏按钮点击的接口
 * */
public interface OnListViewItemClickListener {

	void OnItemDown(float x,float y);
	void OnItemUp(float x,float y);
}
