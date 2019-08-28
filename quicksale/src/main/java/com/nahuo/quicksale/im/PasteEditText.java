//package com.nahuo.quicksale.im;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.text.ClipboardManager;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//
//import com.way.ui.emoji.EmojiEditText;
//
///**
// * 自定义的textview，用来处理复制粘贴的消息
// *
// */
//public class PasteEditText extends EmojiEditText {
//	private Context context;
//
//	public PasteEditText(Context context) {
//		super(context);
//		this.context = context;
//	}
//
//	public PasteEditText(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		this.context = context;
//	}
//
//	public PasteEditText(Context context, AttributeSet attrs, int defStyle) {
//		super(context, attrs, defStyle);
//		this.context = context;
//	}
//
//	@SuppressLint("NewApi")
//	@SuppressWarnings("deprecation")
//	@Override
//	public boolean onTextContextMenuItem(int id) {
//		if (id == android.R.id.paste) {
//			ClipboardManager clip = (ClipboardManager) getContext()
//					.getSystemService(Context.CLIPBOARD_SERVICE);
//			String text = clip.getText().toString();
//			if (text.startsWith(ChatActivity.COPY_IMAGE)) {
//				// intent.setDataAndType(Uri.fromFile(new
//				// File("/sdcard/mn1.jpg")), "image/*");
//				text = text.replace(ChatActivity.COPY_IMAGE, "");
//				Intent intent = new Intent(context, AlertDialogActivity.class);
//				intent.putExtra("title", "发送以下图片？");
//				intent.putExtra("forwardImage", text);
//				intent.putExtra("cancel", true);
//				((Activity) context).startActivityForResult(intent,
//						ChatActivity.REQUEST_CODE_COPY_AND_PASTE);
//				// clip.setText("");
//			}
//		}
//		return super.onTextContextMenuItem(id);
//	}
//
//	@Override
//	protected void onTextChanged(CharSequence text, int start,
//			int lengthBefore, int lengthAfter) {
//		if (!TextUtils.isEmpty(text)
//				&& text.toString().startsWith(ChatActivity.COPY_IMAGE)) {
//			setText("");
//		}
//		// else if(!TextUtils.isEmpty(text)){
//		// setText(SmileUtils.getSmiledText(getContext(),
//		// text),BufferType.SPANNABLE);
//		// }
//		super.onTextChanged(text, start, lengthBefore, lengthAfter);
//	}
//
//}
