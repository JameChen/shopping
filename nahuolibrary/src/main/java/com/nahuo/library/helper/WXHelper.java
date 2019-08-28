package com.nahuo.library.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;

/**
 * 微信特殊方法类，专业弄微信
 * 
 */
public class WXHelper {
//    public static final String TAG = "WXHelper";
//    private Context mContext;
//    private LoadingDialog mLoadingDialog;
//    // 中途取消的标志
//    private boolean mCancled;
//    public static final int MSG_NO_NETWORK = 69;
//
//    public void ShareToWXTimeLine(Context context, String title,
//            List<String> imgs) {
//        mLoadingDialog = new LoadingDialog(context);
//        mLoadingDialog.setCancelable(true);
//        mLoadingDialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                mCancled = true;
//            }
//        });
//        mContext = context;
//
//        mHandler.sendEmptyMessage(0);
//        DownloadThread thread = new DownloadThread(mContext, title, imgs);
//        thread.setDownloadImgListener(new DownloadImgListener() {
//
//            @Override
//            public void onLoadedAllImg(Context context, String title,
//                    ArrayList<Uri> filePaths) {
//                if (mCancled) {
//                    return;
//                }
//                mHandler.sendEmptyMessage(99);
//                try {
//                    Intent intent = new Intent();
//                    ComponentName comp = new ComponentName("com.tencent.mm",
//                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//                    intent.setComponent(comp);
//                    intent.setAction("android.intent.action.SEND_MULTIPLE");
//                    intent.setType("image/*");
//
//                    intent.putExtra("Kdescription", title);
//
//                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
//                            filePaths);
//
//                    mHandler.sendEmptyMessage(100);
//                    context.startActivity(intent);
//                } catch (Exception e) {
//                    mHandler.sendEmptyMessage(68);
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        thread.start();
//
//    }
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//            case 0: {
//                mLoadingDialog.start("开始下载款式图片");
//                break;
//            }
//            case MSG_NO_NETWORK:{
//                Toast.makeText(mContext, "网络不可用，无法分享", Toast.LENGTH_SHORT).show();
//                break;
//            }
//            case 68: {
//                Builder builder = LightAlertDialog.Builder.create(mContext);
//                builder.setTitle("提示").setMessage("请先安装微信").setPositiveButton("OK", null);
//                builder.show();
//                break;
//            }
//            case 99: {
//                mLoadingDialog.setMessage("下载完毕，开始分享");
//                break;
//            }
//            case 100: {
//                mLoadingDialog.stop();
//                break;
//            }
//            default:
//            {
//                mLoadingDialog.setMessage(String
//                        .format("正在下载第%s张照片\n取消下载请点击手机返回键",
//                                msg.what));
//                break;
//            }
//            }
//        }
//    };
//
//    // 下载网络图片的线程
//    public class DownloadThread extends Thread {
//
//        private int nowFileIndex;
//        private Context mContext;
//        private String mTitle;
//        private List<String> imgUrls;
//        private ArrayList<Uri> shareImgUrls;
//
//        public DownloadThread(Context context, String title, List<String> imgs) {
//            mContext = context;
//            mTitle = title;
//            nowFileIndex = 0;
//            shareImgUrls = new ArrayList<Uri>();
//            imgUrls = imgs;
//        }
//
//        @Override
//        public void run() {
//            String bucket = "share_download";
//            int bucket_id = 111111;
//            String downloadDirPath = SDCardHelper.getSDCardRootDirectory()
//                    + "/weipu/share_download";
//            String downloadDirPath1 = SDCardHelper.getSDCardRootDirectory()
//                    + "/weipu";
//            File file = new File(downloadDirPath1);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//            File file1 = new File(downloadDirPath);
//            if (!file1.exists()) {
//                file1.mkdir();
//            }
//
//            for (int i = 0; i < imgUrls.size(); i++) {
//                try {
//                    if (mCancled) {
//                        return;
//                    }
//                    mHandler.sendEmptyMessage(i + 1);
//                    String[] spUrl = imgUrls.get(i).toString().split("/");
//                    String downloadFileName = spUrl[spUrl.length - 1];
//                    File imgFile = new File(downloadDirPath, downloadFileName);
//                    if (imgFile.exists()) {
//                        shareImgUrls.add(Uri.fromFile(imgFile));
//                    } else {
//                        // 准备读取数据
//                        URL url = new URL(imgUrls.get(i));
//                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                        conn.setRequestMethod("GET");
//                        conn.setConnectTimeout(20 * 1000);
//                        InputStream inStream = conn.getInputStream();
//
//                        // 读流
//                        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
//                        byte[] buffer = new byte[1024];
//                        int len = -1;
//                        while ((len = inStream.read(buffer)) != -1) {
//                            outSteam.write(buffer, 0, len);
//                        }
//                        outSteam.close();
//                        inStream.close();
//
//                        // 保存到本地
//                        byte[] data = outSteam.toByteArray();
//                        if (imgFile.createNewFile()) {
//                            FileOutputStream outStream = new FileOutputStream(imgFile);
//                            outStream.write(data);
//                            shareImgUrls.add(Uri.fromFile(imgFile));
//                            ImageTools.saveImageExternal(mContext, imgFile.getPath(), data.length,
//                                    downloadFileName, bucket, bucket_id, 0, 0);
//                        }
//                    }
//                    shareToWX();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    mHandler.sendEmptyMessage(MSG_NO_NETWORK);
//                    mLoadingDialog.dismiss();
//                    return;
////                    shareToWX();
//                }
//            }
//
//        }
//
//        /**
//         * 单击事件监听器
//         */
//        private DownloadImgListener mListener = null;
//
//        public void setDownloadImgListener(DownloadImgListener listener) {
//            mListener = listener;
//        }
//
//        public void shareToWX() {
//            nowFileIndex++;
//            if (nowFileIndex == imgUrls.size()) {
//                if (mListener != null) {
//                    mListener.onLoadedAllImg(mContext, mTitle, shareImgUrls);
//                }
//            }
//        }
//    };
//
//    public interface DownloadImgListener {
//        void onLoadedAllImg(Context context, String title,
//                ArrayList<Uri> filePaths);
//    }
}
