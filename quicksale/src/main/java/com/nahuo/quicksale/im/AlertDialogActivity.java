//package com.nahuo.quicksale.im;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.util.EMLog;
//import com.nahuo.quicksale.BaseActivity;
//import com.nahuo.quicksale.R;
//
//import java.io.File;
//
//public class AlertDialogActivity extends BaseActivity {
//	private TextView mTextView;
//	private Button mButton;
//	private int position;
//	private ImageView imageView;
//	private EditText editText;
//	private boolean isEditextShow;
//	private String voicePath;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setTheme(R.style.AppNoTitleTheme);
//		setContentView(R.layout.activity_alert_dialog);
//		mTextView = (TextView) findViewById(R.id.title);
//		mButton = (Button) findViewById(R.id.btn_cancel);
//		imageView = (ImageView) findViewById(R.id.image);
//		editText = (EditText) findViewById(R.id.edit);
//		// 提示内容
//		String msg = getIntent().getStringExtra("msg");
//		// 提示标题
//		String title = getIntent().getStringExtra("title");
//		// voicePath = getIntent().getStringExtra("voicePath");
//		position = getIntent().getIntExtra("position", -1);
//		// 是否显示取消标题
//		boolean isCanceTitle = getIntent().getBooleanExtra("titleIsCancel",
//				false);
//		// 是否显示取消按钮
//		boolean isCanceShow = getIntent().getBooleanExtra("cancel", false);
//		// 是否显示文本编辑框
//		isEditextShow = getIntent().getBooleanExtra("editTextShow", false);
//		// 转发复制的图片的path
//		String path = getIntent().getStringExtra("forwardImage");
//		if (msg != null)
//			((TextView) findViewById(R.id.alert_message)).setText(msg);
//		if (title != null)
//			mTextView.setText(title);
//		if (isCanceTitle) {
//			mTextView.setVisibility(View.GONE);
//		}
//		if (isCanceShow)
//			mButton.setVisibility(View.VISIBLE);
//		if (path != null) {
//			// 优先拿大图，没有去取缩略图
//			if (!new File(path).exists())
//				path = DownloadImageTask.getThumbnailImagePath(path);
//			imageView.setVisibility(View.VISIBLE);
//			((TextView) findViewById(R.id.alert_message))
//					.setVisibility(View.GONE);
//			if (ImageCache.getInstance().get(path) != null) {
//				imageView.setImageBitmap(ImageCache.getInstance().get(path));
//			} else {
//				Bitmap bm = ImageUtils.decodeScaleImage(path, 150, 150);
//				imageView.setImageBitmap(bm);
//				ImageCache.getInstance().put(path, bm);
//			}
//
//		}
//		if (isEditextShow) {
//			editText.setVisibility(View.VISIBLE);
//
//		}
//	}
//
//	public void ok(View view) {
//		setResult(RESULT_OK, new Intent().putExtra("position", position)
//				.putExtra("edittext", editText.getText().toString())
//		/* .putExtra("voicePath", voicePath) */);
//		if (position != -1)
//			ChatActivity.resendPos = position;
//		finish();
//
//	}
//
//	public void cancel(View view) {
//		finish();
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		finish();
//		return true;
//	}
//
//}
//
// class DownloadImageTask extends AsyncTask<EMMessage, Integer, Bitmap>{
//	private DownloadFileCallback callback;
//	Bitmap bitmap = null;
//	public boolean downloadThumbnail = false;
//	EMMessage message;
//	private String remoteDir;
//
//	public DownloadImageTask(String remoteDir, DownloadFileCallback callback){
//		this.callback = callback;
//		this.remoteDir = remoteDir;
//	}
//
//	@Override
//	protected Bitmap doInBackground(EMMessage... params) {
//	    /*
//	    try {
//	        message = params[1];//视频的图片path信息的message
//        } catch (Exception e) {
//            message = params[0];
//        }
//
//
//		String remoteFilePath = message.getFilePath().substring(message.getFilePath().lastIndexOf("/")+1);
//		if(remoteDir != null){
//			remoteFilePath = remoteDir + remoteFilePath;
//		}
//		final String localFilePath;
//		if (downloadThumbnail) {
//		    localFilePath = getThumbnailImagePath(message.getFilePath());
//		    SMTLog.d("###", "localFilePath: "+localFilePath);
//		} else {
//		    localFilePath = message.getFilePath();
//		}
////		final String remoteFilePath = message.getFilePath();
////		final String localFilePath = User.getImagePath() + "/"+ message.getImageName();
//		SMTLog.d("###", "download picture from remote "+ remoteFilePath + " to local:" + localFilePath);
//		final HttpFileManager httpFileMgr = new HttpFileManager(EaseMobUserConfig.getInstance().applicationContext,
//		        EaseMobChatConfig.getInstance().EASEMOB_STORAGE_URL);
//		CloudOperationCallback callback = new CloudOperationCallback() {
//			public void onSuccess() {
//				SMTLog.d("###", "offline file saved to "+ localFilePath);
//				// after download to phone, we will delete the
//				// file on server
//				try {
//					//httpFileMgr.deleteFileInBackground(remoteFilePath, null, null);
//				    bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(localFilePath)));
//					//bitmap = Bitmap.createScaledBitmap(bm, 120, 120, true);
//					//bitmap = Bitmap.createBitmap(bm);
//					//bm.recycle();
//					//bm = null;
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					bitmap = null;
//				}
//			}
//
//			public void onError(String msg) {
//				SMTLog.e("###","offline file transfer error:" + msg);
//				File file = new File(localFilePath);
//				if(file.exists())
//					file.delete();
//			}
//
//			public void onProgress(int progress) {
//				onProgressUpdate(progress);
//			}
//		};
//		if (downloadThumbnail) {
//		    httpFileMgr.downloadThumbnailFile(remoteFilePath, localFilePath, EaseMobUserConfig.getInstance().APPKEY, null, callback);
//		} else {
//		    httpFileMgr.downloadFile(remoteFilePath, localFilePath, EaseMobUserConfig.getInstance().APPKEY, null, callback);
//		}
//		return bitmap;
//		*/
//	    //todo: need to implement
//	    return null;
//	}
//
//	@Override
//	protected void onPostExecute(Bitmap result) {
//		callback.afterDownload(result);
//	}
//
//	@Override
//	protected void onPreExecute() {
//		callback.beforeDownload();
//	}
//
//	@Override
//	protected void onProgressUpdate(Integer... values) {
//		callback.downloadProgress(values[0]);
//	}
//
//
//
//	public interface DownloadFileCallback{
//		void beforeDownload();
//		void downloadProgress(int progress);
//		void afterDownload(Bitmap bitmap);
//	}
//
//
//	public static String getThumbnailImagePath(String imagePath) {
//        String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
//        path += "th" + imagePath.substring(imagePath.lastIndexOf("/")+1, imagePath.length());
//        EMLog.d("msg", "original image path:" + imagePath);
//        EMLog.d("msg", "thum image path:" + path);
//        return path;
//    }
//}
