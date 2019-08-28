//package com.nahuo.quicksale.im;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//
//import com.hyphenate.cloud.CloudOperationCallback;
//import com.hyphenate.util.PathUtil;
//import com.nahuo.quicksale.BaseActivity;
//import com.nahuo.quicksale.R;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ShowBigImage extends BaseActivity {
//
//  private ProgressDialog pd;
//  private ImageView image;
//  private int default_res = R.drawable.default_avatar;
//  // flag to indicate if need to delete image on server after download
//  private boolean deleteAfterDownload;
//  private boolean showAvator;
//  private String localFilePath;
//  private String username;
//  private Bitmap bitmap;
//  private boolean isDownloaded;
//  private ProgressBar loadLocalPb;
//
//  @SuppressLint("NewApi")
//  @Override
//  protected void onCreate(Bundle savedInstanceState) {
//
//    super.onCreate(savedInstanceState);
//    setTheme(R.style.AppFullScreenTheme);
//    setContentView(R.layout.activity_show_big_image);
//    image = (ImageView) findViewById(R.id.show_bigimage);
//    loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
//
//    default_res = getIntent().getIntExtra("default_image", R.drawable.default_avatar);
//    showAvator = getIntent().getBooleanExtra("showAvator", false);
//    username = getIntent().getStringExtra("username");
//    deleteAfterDownload = getIntent().getBooleanExtra("delete", false);
//
//    Uri uri = getIntent().getParcelableExtra("uri");
//    String remotepath = getIntent().getExtras().getString("remotepath");
//    String secret = getIntent().getExtras().getString("secret");
//    System.err.println("show big image uri:" + uri + " remotepath:" + remotepath);
//
//    // 本地存在，直接显示本地的图片
//    if (uri != null && new File(uri.getPath()).exists()) {
//      System.err.println("showbigimage file exists. directly show it");
//      DisplayMetrics metrics = new DisplayMetrics();
//      getWindowManager().getDefaultDisplay().getMetrics(metrics);
//      // int screenWidth = metrics.widthPixels;
//      // int screenHeight =metrics.heightPixels;
//      bitmap = ImageCache.getInstance().get(uri.getPath());
//      if (bitmap == null) {
//        LoadLocalBigImgTask task =null;
////            new LoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb,
////                ImageUtils.SCALE_IMAGE_WIDTH, ImageUtils.SCALE_IMAGE_HEIGHT);
//        if (android.os.Build.VERSION.SDK_INT > 10) {
//          task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        } else {
//          task.execute();
//        }
//      } else {
//        image.setImageBitmap(bitmap);
//      }
//      // }
//    } else if (remotepath != null) { // 去服务器下载图片
//      System.err.println("download remote image");
//      Map<String, String> maps = new HashMap<String, String>();
//      //String accessToken = EMChatManager.getInstance().getAccessToken();
//      //maps.put("Authorization", "Bearer " + accessToken);
//      if (!TextUtils.isEmpty(secret)) {
//        maps.put("share-secret", secret);
//      }
//      maps.put("Accept", "application/octet-stream");
//      downloadImage(remotepath, maps);
//    } else {
//      image.setImageResource(default_res);
//    }
//
//    image.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        finish();
//      }
//    });
//  }
//
//  /**
//   * 下载图片
//   *
//   * @param remoteFilePath
//   */
//  private void downloadImage(final String remoteFilePath, final Map<String, String> headers) {
//    pd = new ProgressDialog(this);
//    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//    pd.setCanceledOnTouchOutside(false);
//    pd.setMessage("下载图片: 0%");
//    pd.show();
//    if (!showAvator) {
//      if (remoteFilePath.contains("/"))
//        localFilePath =
//            PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
//                + remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
//      else
//        localFilePath =
//            PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteFilePath;
//    } else {
//      if (remoteFilePath.contains("/"))
//        localFilePath =
//            PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"
//                + remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
//      else
//        localFilePath =
//            PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteFilePath;
//
//    }
////    final HttpFileManager httpFileMgr =
////        new HttpFileManager(this, EMChatConfig.getInstance().getStorageUrl());
//    final CloudOperationCallback callback = new CloudOperationCallback() {
//      public void onSuccess(String resultMsg) {
//
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//            DisplayMetrics metrics = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            int screenWidth = metrics.widthPixels;
//            int screenHeight = metrics.heightPixels;
//
//            bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
//            if (bitmap == null) {
//              image.setImageResource(default_res);
//            } else {
//              image.setImageBitmap(bitmap);
//              ImageCache.getInstance().put(localFilePath, bitmap);
//              isDownloaded = true;
//
//            }
//            if (pd != null) {
//              pd.dismiss();
//            }
//          }
//        });
//      }
//
//      public void onError(String msg) {
//        Log.e("###", "offline file transfer error:" + msg);
//        File file = new File(localFilePath);
//        if (file.exists()) {
//          file.delete();
//        }
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//            pd.dismiss();
//            image.setImageResource(default_res);
//          }
//        });
//      }
//
//      public void onProgress(final int progress) {
//        Log.d("ease", "Progress: " + progress);
//        runOnUiThread(new Runnable() {
//          @Override
//          public void run() {
//            pd.setMessage("下载图片: " + progress + "%");
//          }
//        });
//      }
//    };
//
//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//
//     /*   String downloadDirPath1 = SDCardHelper.getSDCardRootDirectory() + "/weipu/Chat/";
//        httpFileMgr.downloadFile(remoteFilePath, downloadDirPath1 + "1.jpg",
//            EMChatConfig.getInstance().APPKEY, headers, callback);*/
//
////        httpFileMgr.downloadFile(remoteFilePath, localFilePath, EMChatConfig.getInstance().APPKEY,
////            headers, callback);
//      }
//    }).start();
//
//  }
//
//  @Override
//  public void onBackPressed() {
//
//    if (isDownloaded) setResult(RESULT_OK);
//    finish();
//  }
//
//  class LoadLocalBigImgTask extends AsyncTask<Void, Void, Bitmap> {
//
//    private ProgressBar pb;
//    private ImageView photoView;
//    private String path;
//    private int width;
//    private int height;
//    private Context context;
//
//    public LoadLocalBigImgTask(Context context, String path, ImageView photoView, ProgressBar pb,
//        int width, int height) {
//      this.context = context;
//      this.path = path;
//      this.photoView = photoView;
//      this.pb = pb;
//      this.width = width;
//      this.height = height;
//    }
//
//    @Override
//    protected void onPreExecute() {
//      super.onPreExecute();
//      int degree = ImageUtils.readPictureDegree(path);
//      if (degree != 0) {
//        pb.setVisibility(View.VISIBLE);
//        photoView.setVisibility(View.INVISIBLE);
//      } else {
//        pb.setVisibility(View.INVISIBLE);
//        photoView.setVisibility(View.VISIBLE);
//      }
//
//    }
//
//    @Override
//    protected Bitmap doInBackground(Void... params) {
//      Bitmap bitmap = ImageUtils.decodeScaleImage(path, width, height);
//      return bitmap;
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap result) {
//      super.onPostExecute(result);
//      pb.setVisibility(View.INVISIBLE);
//      photoView.setVisibility(View.VISIBLE);
//      if (result != null)
//        ImageCache.getInstance().put(path, result);
//      else
//        result =
//            BitmapFactory.decodeResource(context.getResources(), R.drawable.signin_local_gallry);
//      photoView.setImageBitmap(result);
//    }
//  }
//}
