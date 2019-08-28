package com.nahuo.library.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.library.R;
import com.nahuo.library.event.OnTitleBarClickListener;
import com.nahuo.library.helper.FunctionHelper;

public class NoHuoLibTitleBar extends RelativeLayout {

	private RelativeLayout vThis;
	private Button btnBack;
	public ImageButton imgBtnLeftMenu;
	private Button btnRight;
	public ImageButton imgBtnRight;
	private ImageButton imgBtnRightMore;
	public TextView tvTitle;

	public NoHuoLibTitleBar(Context context) {
		super(context);
		InitTitleBar(context, null);
	}

	public NoHuoLibTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		InitTitleBar(context, attrs);
	}

	private void InitTitleBar(Context context, AttributeSet attrs) {
		vThis = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.titlebar, this);

		btnBack = (Button) vThis.findViewById(R.id.titlebar_btnBack);
		imgBtnLeftMenu = (ImageButton) vThis
				.findViewById(R.id.titlebar_imgBtnMenu);
		btnRight = (Button) vThis.findViewById(R.id.titlebar_btnRigth);
		imgBtnRight = (ImageButton) vThis
				.findViewById(R.id.titlebar_imgBtnRight);
		tvTitle = (TextView) vThis.findViewById(R.id.titlebar_tvTitle);
		imgBtnRightMore = (ImageButton) vThis
				.findViewById(R.id.titlebar_imgBtnRightmore);

		// 获取自定义属性
		String title = "";
		String rightButtonText = "";
		Drawable rightButtonImage = null;
		boolean isImgButton = false;
		boolean showBackButton = false;
		boolean showLeftMenuButton = false;
		boolean showRightButton = false;

		Drawable showRigthMoreimage = null;
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.NoHuoLibTitleBar);
			title = a.getString(R.styleable.NoHuoLibTitleBar_Title);
			rightButtonText = a.getString(R.styleable.NoHuoLibTitleBar_RightButtonText);
			rightButtonImage = a
					.getDrawable(R.styleable.NoHuoLibTitleBar_RightButtonImage);
			isImgButton = a.getBoolean(R.styleable.NoHuoLibTitleBar_IsImgButton, false);
			showBackButton = a.getBoolean(R.styleable.NoHuoLibTitleBar_ShowBackButton,
					false);
			showLeftMenuButton = a.getBoolean(
					R.styleable.NoHuoLibTitleBar_ShowLeftMenuButton, false);
			showRightButton = a.getBoolean(
					R.styleable.NoHuoLibTitleBar_ShowRightButton, false);

			showRigthMoreimage = a
					.getDrawable(R.styleable.NoHuoLibTitleBar_showRigthMoreimage);
		}
		// 标题
		tvTitle.setText(title);

		tvTitle.setTextColor(getResources().getColor(R.color.white));

		// 返回
		int back_visibility = showBackButton == true ? VISIBLE : GONE;
		btnBack.setVisibility(back_visibility);
		btnBack.setOnClickListener(onClickListener);

		// 菜单
		int leftmenu_visibility = showLeftMenuButton == true ? VISIBLE : GONE;
		imgBtnLeftMenu.setVisibility(leftmenu_visibility);
		imgBtnLeftMenu.setOnClickListener(onClickListener);

		// 右边按钮
		int rightbutton_visibility;
		if (isImgButton) {
			btnRight.setVisibility(GONE);
			imgBtnRight.setVisibility(VISIBLE);
			imgBtnRight.setImageDrawable(rightButtonImage);
			imgBtnRight.setOnClickListener(onClickListener);

			if (showRigthMoreimage != null) {
				imgBtnRightMore.setVisibility(VISIBLE);
				imgBtnRightMore.setImageDrawable(showRigthMoreimage);
				imgBtnRightMore.setOnClickListener(onClickListener);
			}
		} else {
			imgBtnRight.setVisibility(GONE);
			imgBtnRightMore.setVisibility(GONE);

			rightbutton_visibility = showRightButton == true ? VISIBLE : GONE;
			btnRight.setVisibility(rightbutton_visibility);
			btnRight.setText(rightButtonText);
			btnRight.setOnClickListener(onClickListener);
		}

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();

			// case R.id.titlebar_btnBack: // 返回按钮
			// if (onTitleBarClickListener != null)
			// onTitleBarClickListener.OnBackButtonClick(vThis, null);
			// // FunctionHelper.simulateKey(KeyEvent.KEYCODE_BACK);
			// Activity activity = (Activity) getContext();
			// activity.finish();
			// break;
			// case R.id.titlebar_imgBtnMenu: // 菜单按钮
			// if (onTitleBarClickListener != null)
			// onTitleBarClickListener.OnLeftMenuButtonClick(vThis, null);
			// break;
			// case R.id.titlebar_btnRigth: // 标题后边的按钮
			// case R.id.titlebar_imgBtnRight:
			// if (onTitleBarClickListener != null)
			// onTitleBarClickListener.OnRightButtonClick(vThis, null);
			// break;
			if (id == R.id.titlebar_imgBtnRight) {
				if (onTitleBarClickListener != null)
					onTitleBarClickListener.OnRightButtonClick(vThis, null);
			} else if (id == R.id.titlebar_btnBack) {
				if (onTitleBarClickListener != null)
					onTitleBarClickListener.OnBackButtonClick(vThis, null);
				FunctionHelper.simulateKey(KeyEvent.KEYCODE_BACK);
				// Activity activity = (Activity) getContext();
				// activity.finish();
			} else if (id == R.id.titlebar_imgBtnRightmore) {
				if (onTitleBarClickListener != null)

					onTitleBarClickListener.OnRithtButtonMoreClick(vThis, null);

			}

		}
	};

	private OnTitleBarClickListener onTitleBarClickListener;

	public void setOnTitleBarClickListener(OnTitleBarClickListener listener) {
		this.onTitleBarClickListener = listener;
	}

	public OnTitleBarClickListener getOnTitleBarClickListener() {
		return this.onTitleBarClickListener;
	}
}
