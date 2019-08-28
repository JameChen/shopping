package com.nahuo.quicksale.hyphenate.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.util.EMLog;
import com.nahuo.quicksale.activity.VideoActivity1;

import java.io.File;

/**
 * show the video
 * 
 */
public class EaseShowVideoActivity extends BaseAppCompatActicity {
	private static final String TAG = "ShowVideoActivity";
	
	private RelativeLayout loadingLayout;
	private ProgressBar progressBar;
	private String localFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(com.hyphenate.easeui.R.layout.ease_showvideo_activity);
		loadingLayout = (RelativeLayout) findViewById(com.hyphenate.easeui.R.id.loading_layout);
		progressBar = (ProgressBar) findViewById(com.hyphenate.easeui.R.id.progressBar);

		final EMMessage message = getIntent().getParcelableExtra("msg");
		if (!(message.getBody() instanceof EMVideoMessageBody)) {
			Toast.makeText(EaseShowVideoActivity.this, "Unsupported message body", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		EMVideoMessageBody messageBody = (EMVideoMessageBody)message.getBody();

		localFilePath = messageBody.getLocalUrl();

		if (localFilePath != null && new File(localFilePath).exists()) {
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			Uri uri = EaseCompat.getUriForFile(this, new File(localFilePath));
//			intent.setDataAndType(uri, "video/mp4");
//			// 注意添加该flag,用于Android7.0以上设备获取相册文件权限.
//			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//			startActivity(intent);
//			finish();
			Intent intent = new Intent(this, VideoActivity1.class);
            intent.putExtra(VideoActivity1.VIEDEO_URL_EXTR, localFilePath);
            intent.putExtra(VideoActivity1.VIEDEO_ISHOW_DOWN_EXTR, false);
            startActivity(intent);
			finish();
		} else {
			EMLog.d(TAG, "download remote video file");
			downloadVideo(message);
		}
	}

	/**
	 * show local video
	 * @param localPath -- local path of the video file
	 */
	private void showLocalVideo(String localPath){
		Intent intent = new Intent(this, VideoActivity1.class);
		intent.putExtra(VideoActivity1.VIEDEO_URL_EXTR, localPath);
		intent.putExtra(VideoActivity1.VIEDEO_ISHOW_DOWN_EXTR, false);
		startActivity(intent);
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setDataAndType(EaseCompat.getUriForFile(this, new File(localPath)),
//				"video/mp4");
//		startActivity(intent);
		finish();
	}

	/**
	 * download video file
	 */
	private void downloadVideo(EMMessage message) {
		loadingLayout.setVisibility(View.VISIBLE);
		message.setMessageStatusCallback(new EMCallBack() {
			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						loadingLayout.setVisibility(View.GONE);
						progressBar.setProgress(0);
						showLocalVideo(localFilePath);
					}
				});
			}

			@Override
			public void onProgress(final int progress,String status) {
				Log.d("ease", "video progress:" + progress);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressBar.setProgress(progress);
					}
				});

			}

			@Override
			public void onError(int error, String msg) {
				Log.e("###", "offline file transfer error:" + msg);
				File file = new File(localFilePath);
				if (file.exists()) {
					file.delete();
				}
			}
		});
		EMClient.getInstance().chatManager().downloadAttachment(message);
	}

	@Override
	public void onBackPressed() {
		finish();
	}
 

}
