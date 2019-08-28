package com.nahuo.library.controls;

import com.nahuo.library.R;
import com.nahuo.library.helper.FunctionHelper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

public class AlertDialogEx extends Dialog {
	private Context mcontext;

	public AlertDialogEx(Context context, View view) {
		super(context, R.style.Alteringdialog);

		mcontext = context;
		InitDialog(mcontext, view);
	}

	public void InitDialog(Context context, View view) {

		setCancelable(false);
		setCanceledOnTouchOutside(false);
		setContentView(view);
		getWindow().getAttributes().gravity = Gravity.CENTER;

		getWindow().getAttributes().width = (int) (FunctionHelper
				.getScreenWidth((Activity) context) * 0.9);
		;
	}

	public void Start() {
		show();
	}

	public void Stop() {
		dismiss();
	}
}
