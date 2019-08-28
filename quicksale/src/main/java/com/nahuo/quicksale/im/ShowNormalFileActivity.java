//package com.nahuo.quicksale.im;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.content.FileProvider;
//import android.text.TextUtils;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.hyphenate.cloud.CloudOperationCallback;
//import com.nahuo.quicksale.R;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class ShowNormalFileActivity extends EaseBaseActivity {
//	private ProgressBar progressBar;
//	private File file;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_show_file);
//		progressBar = (ProgressBar) findViewById(R.id.progressBar);
//
//		final FileMessageBody messageBody = getIntent().getParcelableExtra(
//				"msgbody");
//		file = new File(messageBody.getLocalUrl());
//		// set head map
//		final Map<String, String> maps = new HashMap<String, String>();
//		if (!TextUtils.isEmpty(messageBody.getSecret())) {
//			maps.put("share-secret", messageBody.getSecret());
//		}
//		// 下载文件
//		new Thread(new Runnable() {
//			public void run() {
//				HttpFileManager fileManager = new HttpFileManager(
//						ShowNormalFileActivity.this, EMChatConfig.getInstance()
//								.getStorageUrl());
//				fileManager.downloadFile(messageBody.getRemoteUrl(),
//						messageBody.getLocalUrl(), maps,
//						new CloudOperationCallback() {
//
//							@Override
//							public void onSuccess(String result) {
//								runOnUiThread(new Runnable() {
//									public void run() {
//									openFile(file,
//												ShowNormalFileActivity.this);
//										finish();
//									}
//								});
//							}
//
//							@Override
//							public void onProgress(final int progress) {
//								runOnUiThread(new Runnable() {
//									public void run() {
//										progressBar.setProgress(progress);
//									}
//								});
//							}
//
//							@Override
//							public void onError(final String msg) {
//								runOnUiThread(new Runnable() {
//									public void run() {
//										if (file != null && file.exists()
//												&& file.isFile())
//											file.delete();
//										String str4 = getResources()
//												.getString(
//														R.string.Failed_to_download_file);
//										Toast.makeText(
//												ShowNormalFileActivity.this,
//												str4 + msg, Toast.LENGTH_SHORT)
//												.show();
//										finish();
//									}
//								});
//							}
//						});
//
//			}
//		}).start();
//
//	}
//	public  void openFile(File var0, Activity var1) {
//		Intent var2 = new Intent();
//		var2.addFlags(268435456);
//		var2.setAction("android.intent.action.VIEW");
//		String var3 = getMIMEType(var0);
//		final Uri destinationUri;
//		String authority = this.getPackageName() + ".provider";
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//				//通过FileProvider创建一个content类型的Uri
//				destinationUri = FileProvider.getUriForFile(this, authority, var0);
//			} else {
//				destinationUri = Uri.fromFile(var0);
//			}
//		var2.setDataAndType(destinationUri, var3);
//		try {
//			var1.startActivity(var2);
//		} catch (Exception var5) {
//			var5.printStackTrace();
//			Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
//		}
//
//	}
//}
