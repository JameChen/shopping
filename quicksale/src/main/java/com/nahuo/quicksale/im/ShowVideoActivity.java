//package com.nahuo.quicksale.im;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//
//import com.easemob.chat.EMChatConfig;
//import com.easemob.cloud.CloudOperationCallback;
//import com.easemob.cloud.HttpFileManager;
//import com.easemob.util.PathUtil;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.ViewHub;
//import com.nahuo.quicksale.activity.VideoActivity1;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 展示视频内容
// *
// * @author Administrator
// *
// */
//public class ShowVideoActivity extends EaseBaseActivity {
//
//	private RelativeLayout loadingLayout;
//	private ProgressBar progressBar;
//	private String localFilePath;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
////		requestWindowFeature(Window.FEATURE_NO_TITLE);
////		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.activity_show_video);
//		loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
//		progressBar = (ProgressBar) findViewById(R.id.progressBar);
//		localFilePath = getIntent().getStringExtra("localpath");
//		String remotepath = getIntent().getStringExtra("remotepath");
//		String secret = getIntent().getStringExtra("secret");
//		System.err.println("show video view file:" + localFilePath
//				+ " remotepath:" + remotepath + " secret:" + secret);
////		final Uri destinationUri;
////		String authority = this.getPackageName() + ".provider";
//		if (localFilePath != null && new File(localFilePath).exists()) {
////			File dir=new File(localFilePath);
////			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////				//通过FileProvider创建一个content类型的Uri
////				destinationUri = FileProvider.getUriForFile(this, authority, dir);
////			} else {
////				destinationUri = Uri.fromFile(dir);
////			}
////			Intent intent = new Intent(Intent.ACTION_VIEW);
////			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////			intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
////			intent.setDataAndType(destinationUri,
////					"video/mp4");
////			startActivity(intent);
//            Intent intent = new Intent(this, VideoActivity1.class);
//            intent.putExtra(VideoActivity1.VIEDEO_URL_EXTR, localFilePath);
//            intent.putExtra(VideoActivity1.VIEDEO_ISHOW_DOWN_EXTR, false);
//            startActivity(intent);
//			finish();
//		} else if (!TextUtils.isEmpty(remotepath) && !remotepath.equals("null")) {
//			System.err.println("download remote video file");
//			Map<String, String> maps = new HashMap<String, String>();
//			if (!TextUtils.isEmpty(secret)) {
//				maps.put("share-secret", secret);
//			}
//			downloadVideo(remotepath, maps);
//		} else {
//			ViewHub.showLongToast(this,"视频无效");
//		}
//	}
//
//	public String getLocalFilePath(String remoteUrl) {
//		String localPath;
//		if (remoteUrl.contains("/")) {
//			localPath = PathUtil.getInstance().getVideoPath().getAbsolutePath()
//					+ "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1)
//					+ ".mp4";
//		} else {
//			localPath = PathUtil.getInstance().getVideoPath().getAbsolutePath()
//					+ "/" + remoteUrl + ".mp4";
//		}
//		return localPath;
//	}
//
//	/**
//	 * 播放本地视频
//	 *
//	 * @param localPath
//	 *            视频路径
//	 */
//	private void showLocalVideo(String localPath) {
//		Intent intent = new Intent(this, VideoActivity1.class);
//		intent.putExtra(VideoActivity1.VIEDEO_URL_EXTR, localPath);
//		intent.putExtra(VideoActivity1.VIEDEO_ISHOW_DOWN_EXTR, false);
//		startActivity(intent);
//		finish();
////		Intent intent = new Intent(Intent.ACTION_VIEW);
////		intent.setDataAndType(Uri.fromFile(new File(localPath)), "video/mp4");
////		startActivity(intent);
////		finish();
//	}
//
//	/**
//	 * 下载视频文件
//	 */
//	private void downloadVideo(final String remoteUrl,
//			final Map<String, String> header) {
//
//		if (TextUtils.isEmpty(localFilePath)) {
//			localFilePath = getLocalFilePath(remoteUrl);
//		}
//		if (new File(localFilePath).exists()) {
//			showLocalVideo(localFilePath);
//			return;
//		}
//		loadingLayout.setVisibility(View.VISIBLE);
//		final HttpFileManager httpFileMgr = new HttpFileManager(this,
//				EMChatConfig.getInstance().getStorageUrl());
//		final CloudOperationCallback callback = new CloudOperationCallback() {
//
//			@Override
//			public void onSuccess(String result) {
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						loadingLayout.setVisibility(View.GONE);
//						progressBar.setProgress(0);
//						showLocalVideo(localFilePath);
//					}
//				});
//			}
//
//			@Override
//			public void onProgress(final int progress) {
//				Log.d("ease", "video progress:" + progress);
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						progressBar.setProgress(progress);
//					}
//				});
//
//			}
//
//			@Override
//			public void onError(String msg) {
//				Log.e("###", "offline file transfer error:" + msg);
//				File file = new File(localFilePath);
//				if (file.exists()) {
//					file.delete();
//				}
//			}
//		};
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				httpFileMgr.downloadFile(remoteUrl, localFilePath, header,
//						callback);
//			}
//		}).start();
//
//	}
//
//	@Override
//	public void onBackPressed() {
//		finish();
//	}
//
//}
