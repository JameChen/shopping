package com.nahuo.library.controls;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import android.app.Dialog;
import android.content.Context;

import android.view.Gravity;

import android.widget.TextView;

import com.nahuo.library.R;


public class OkDialog extends Dialog {

	private Context mContext;
	private ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor();

	public OkDialog(Context context) {
		super(context, R.style.LoadingDialog);
		InitDialog(context);
	}

	public OkDialog(Context context, int theme) {
		super(context, R.style.LoadingDialog);
		InitDialog(context);
	}

	public void InitDialog(Context context) {
		mContext = context;
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.okdialog);

		getWindow().getAttributes().gravity = Gravity.CENTER;

		;
	}

	/**
	 * 设置提示信息
	 * 
	 * @param message
	 *            提示信息
	 */
	public void setMessage(String message) {
		TextView tvMsg = (TextView) findViewById(R.id.loadingdialog_message);
		if (tvMsg != null) {
			tvMsg.setText(message);
		}
	}

	/**
	 * 弹出等待提示框
	 * */
	public void start() {
		start("ok");
	}

	/**
	 * 弹出等待提示框
	 * 
	 * @param message
	 *            提示信息
	 * */
	public void start(String message) {
		setMessage(message);

		show();

	}

	/**
	 * 关闭等待提示框
	 * */
	public void stop(int m) {

		// 创建自动关闭任务
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		};
		// 新建调度任务
		executor.schedule(runner, m, TimeUnit.MILLISECONDS);
	}
}
