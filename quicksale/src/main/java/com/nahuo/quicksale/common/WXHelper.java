package com.nahuo.quicksale.common;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.ImageTools;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.api.OtherAPI;
import com.nahuo.quicksale.exceptions.CatchedException;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信特殊方法类，专业弄微信
 */
public class WXHelper {
    public static final String TAG = "WXHelper";
    private Context mContext;
    private LoadingDialog mLoadingDialog;
    // 中途取消的标志
    private boolean mCancled;
    public static final int MSG_SHARE_EXCEPTION = 69;
    public static String downloadDirPath = SDCardHelper.getSDCardRootDirectory() + "/ttpht/share_download";
    private boolean isMyItem = false;

    public void ShareToWXTimeLine(Context context, String title, List<String> imgs, final String itemUrl,
                                  boolean _isMyItem) {
        isMyItem = _isMyItem;
        mLoadingDialog = new LoadingDialog(context);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mCancled = true;
            }
        });
        mContext = context;

        mHandler.sendEmptyMessage(0);
        DownloadThread thread = new DownloadThread(mContext, title, imgs);
        thread.setDownloadImgListener(new DownloadImgListener() {

            @Override
            public void onLoadedAllImg(Context context, String title, ArrayList<Uri> filePaths) {
                if (mCancled) {
                    return;
                }
                mHandler.sendEmptyMessage(99);

                if (isMyItem) {
                    mHandler.sendEmptyMessage(101);
                    Uri itemQrCodeUri = null;
                    if (filePaths != null) {
                        itemQrCodeUri = QrCodeHelper.genItemQrcode(context, itemUrl, filePaths.get(0).getPath(),
                                "长按图片识别二维码即可购买");
                    } else {
                        filePaths = new ArrayList<Uri>();
                        itemQrCodeUri = QrCodeHelper.genItemQrcode(context, itemUrl, R.mipmap.app_logo,
                                "长按图片识别二维码即可购买");
                    }
                    if (itemQrCodeUri != null) {
                        if (filePaths.size() > 8) {
                            filePaths.set(8, itemQrCodeUri);
                        } else {
                            filePaths.add(itemQrCodeUri);
                        }
                    }
                    new GetShortUrl(context, filePaths, title, itemUrl).execute((Void) null);
                } else {
                    showWXActivity(context, title, filePaths);
                }
            }
        });
        thread.start();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    mLoadingDialog.start("开始下载款式图片");
                    break;
                }
                case MSG_SHARE_EXCEPTION: {
                    Toast.makeText(mContext, "分享异常", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 68: {
                    Builder builder = LightAlertDialog.Builder.create(mContext);
                    builder.setTitle("提示").setMessage("请先安装微信").setPositiveButton("OK", null);
                    builder.show();
                    break;
                }
                case 99: {
                    mLoadingDialog.setMessage("下载完毕，开始分享");
                    break;
                }
                case 100: {
                    mLoadingDialog.stop();
                    break;
                }
                case 101: {
                    mLoadingDialog.setMessage("正在生成链接二维码");
                    break;
                }
                case 102: {
                    mLoadingDialog.setMessage("正在生成链接二维码");
                    // mLoadingDialog.setMessage("正在生成短链接");
                    break;
                }
                default: {
                    mLoadingDialog.setMessage(String.format("正在下载第%s张照片\n取消下载请点击手机返回键",
                            msg.what));
                    break;
                }
            }
        }
    };

    private class GetShortUrl extends AsyncTask<Object, Void, String> {

        String title;
        ArrayList<Uri> filePaths;
        Context context;
        String url;

        public GetShortUrl(Context _context, ArrayList<Uri> _filePaths, String _title, String _url) {
            title = _title;
            filePaths = _filePaths;
            context = _context;
            url = _url;
        }

        @Override
        protected void onPreExecute() {
            mHandler.sendEmptyMessage(102);
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                return OtherAPI.getShortUrl(context, url);
            } catch (Exception e) {
                return url;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                try {

                    JSONArray ja = new JSONArray(result);
                    if (ja.length() > 0) {
                        JSONObject jo = (JSONObject) ja.get(0);
                        if (jo.has("url_short")) {
                            url = jo.getString("url_short");
                        }
                    }
                } catch (Exception e) {
                }
                title += " " + url;

                showWXActivity(context, title, filePaths);
            } catch (Exception e) {
                mHandler.sendEmptyMessage(68);
                e.printStackTrace();
            }
        }
    }

    private void showWXActivity(Context context, String title, ArrayList<Uri> filePaths) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        intent.putExtra("Kdescription", title);
        if (!ListUtils.isEmpty(filePaths)) {
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, filePaths);
        }


        mHandler.sendEmptyMessage(100);
        context.startActivity(intent);
    }

    // 下载网络图片的线程
    public class DownloadThread extends Thread {

        private int nowFileIndex;
        private Context mContext;
        private String mTitle;
        private List<String> imgUrls;
        private ArrayList<Uri> shareImgUrls;

        public DownloadThread(Context context, String title, List<String> imgs) {
            mContext = context;
            mTitle = title;
            nowFileIndex = 0;
            shareImgUrls = new ArrayList<Uri>();
            imgUrls = imgs;
        }

        @Override
        public void run() {
            String bucket = "share_download";
            int bucket_id = 111111;
            File file = new File(downloadDirPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            for (int i = 0; i < imgUrls.size(); i++) {
                try {
                    if (mCancled) {
                        return;
                    }
                    mHandler.sendEmptyMessage(i + 1);
                    String[] spUrl = imgUrls.get(i).toString().split("/");
                    String downloadFileName = spUrl[spUrl.length - 1];
                    File imgFile = new File(downloadDirPath, downloadFileName);
                    if (imgFile.exists()) {
                        shareImgUrls.add(Uri.fromFile(imgFile));
                    } else {
                        // 准备读取数据
                        URL url = new URL(imgUrls.get(i));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setConnectTimeout(20 * 1000);
                        InputStream inStream = conn.getInputStream();

                        // 读流
                        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = inStream.read(buffer)) != -1) {
                            outSteam.write(buffer, 0, len);
                        }
                        outSteam.close();
                        inStream.close();

                        // 保存到本地
                        byte[] data = outSteam.toByteArray();
                        if (imgFile.createNewFile()) {
                            FileOutputStream outStream = new FileOutputStream(imgFile);
                            outStream.write(data);
                            shareImgUrls.add(Uri.fromFile(imgFile));
                            ImageTools.saveImageExternal(mContext, imgFile.getPath(), data.length, downloadFileName,
                                    bucket, bucket_id, 0, 0);
                        }
                    }
                    shareToWX();
                } catch (FileNotFoundException e) {
                    CrashReport.postCatchedException(new CatchedException(e));
                    shareToWX();
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_SHARE_EXCEPTION);
                    mLoadingDialog.dismiss();
                    CrashReport.postCatchedException(new CatchedException(e));
                    return;
                    // shareToWX();
                }
            }

        }

        /**
         * 单击事件监听器
         */
        private DownloadImgListener mListener = null;

        public void setDownloadImgListener(DownloadImgListener listener) {
            mListener = listener;
        }

        public void shareToWX() {
            nowFileIndex++;
            if (imgUrls != null && nowFileIndex == imgUrls.size()) {
                if (mListener != null) {
                    mListener.onLoadedAllImg(mContext, mTitle, shareImgUrls);
                }
            }
        }
    }

    public interface DownloadImgListener {
        void onLoadedAllImg(Context context, String title, ArrayList<Uri> filePaths);
    }
}