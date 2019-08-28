package com.nahuo.quicksale.util;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.nahuo.Dialog.PdMenu;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by jame on 2017/6/9.
 */

public class DownloadService {
    private static String TAG = "DownloadService";
    public static final String PINHUP_ROOT_DIRECTORY="/mnt/sdcard/pinhuo/pihuo_save";
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    private static ExecutorService SINGLE_TASK_EXECUTOR = null;
    private static ExecutorService LIMITED_TASK_EXECUTOR = null;
    private static final ExecutorService FULL_TASK_EXECUTOR = null;
    private static final ExecutorService DEFAULT_TASK_EXECUTOR;
    private static Object lock = new Object();
    ArrayList<Uri> imageList = new ArrayList();
    PdMenu pdMenu = null;

    static {
        // SINGLE_TASK_EXECUTOR = (ExecutorService)
        // Executors.newSingleThreadExecutor();
        LIMITED_TASK_EXECUTOR = (ExecutorService) Executors
                .newFixedThreadPool(9);
        // FULL_TASK_EXECUTOR = (ExecutorService)
        // Executors.newCachedThreadPool();
        DEFAULT_TASK_EXECUTOR = LIMITED_TASK_EXECUTOR;
    }

    // 下载状态监听，提供回调
    DownloadStateListener listener;
    // 下载目录
    private String downloadPath;

    // 下载链接集合
    private List<String> listURL;
    // 下载个数
    private int size = 0;
    Activity activity;
    public static DownloadService instace;

    // 下载完成回调接口
    public interface DownloadStateListener {
         void onFinish(ArrayList<Uri> imageList);

         void onFailed();
         void onUpdate(int size,int count);
    }

    public static DownloadService getInstace(Activity activity, String downloadPath, List<String> listURL,
                                             DownloadStateListener listener) {
            instace = new DownloadService(activity, downloadPath, listURL, listener);
        return instace;
    }

    public DownloadService(Activity activity, String downloadPath, List<String> listURL,
                           DownloadStateListener listener) {
        this.downloadPath = downloadPath;
        this.activity = activity;
        this.listURL = listURL;
        this.listener = listener;
    }

    /**
     * 暂未提供设置
     */
    public void setDefaultExecutor() {

    }

    /**
     * 开始下载
     */
    public void startDownload() {
        // 首先检测path是否存在
        File downloadDirectory = new File(downloadPath);
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs();
        }
        imageList.clear();
        size=0;
        for (final String url : listURL) {
            //捕获线程池拒绝执行异常
            try {
                // 线程放入线程池
//                    DEFAULT_TASK_EXECUTOR.execute(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            File file = downloadBitmap(url);
//                            if (file != null) ;
//                            imageList.add(file);
//                        }
//                    });
                DownLoadExecutor.execute(new Runnable() {

                    @Override
                    public void run() {
                        File file = downloadBitmap(url);
                        if (file != null) {
                            imageList.add(FileUriUtil.getWxFileProviderUri(activity,file));
                        }
                        statDownloadNum();
                    }
                });
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
                Log.e(TAG, "thread pool rejected error");
                listener.onFailed();
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailed();
            }

        }


    }

    /**
     * 下载图片
     *
     * @param urlString
     * @return
     */
    private File downloadBitmap(String urlString) {
        String fileName = urlString;
        // 图片命名方式
        final File cacheFile = new File(createFilePath(new File(
                downloadPath), fileName));
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            final InputStream in = new BufferedInputStream(
                    urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(cacheFile),
                    IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            // 每下载成功一个，统计一下图片个数
            return cacheFile;

        } catch (final IOException e) {
            // 有一个下载失败，则表示批量下载没有成功
            Log.e(TAG, "download " + urlString + " error");
            listener.onFailed();

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error in downloadBitmap - " + e);
                }
            }
        }

        return null;
    }

    /**
     * Creates a constant cache file path given a target cache directory and an
     * image key.
     *
     * @param cacheDir
     * @param key
     * @return
     */
    public static String createFilePath(File cacheDir, String key) {
        try {
            // Use URLEncoder to ensure we have a valid filename, a tad hacky
            // but it will do for
            // this example
            return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX
                    + URLEncoder.encode(key.replace("*", ""), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            Log.e(TAG, "createFilePath - " + e);
        }

        return null;
    }

    public void colseDialog() {
        if (pdMenu != null) {
            pdMenu.dismiss();
            pdMenu = null;
        }
    }

    /**
     * 统计下载个数
     */
    private void statDownloadNum() {
        synchronized (lock) {
            size++;
//            Message message = Message.obtain();
//            message.arg1 = size;
//            message.what = 0;
//            handler.sendMessage(message);
            listener.onUpdate(size,listURL.size());
            if (size >= listURL.size()) {
                // 释放资源
                //  DEFAULT_TASK_EXECUTOR.shutdownNow();
                // 如果下载成功的个数与列表中 url个数一致，说明下载成功
                listener.onFinish(imageList); // 下载成功回调
                DownLoadExecutor.shutdown();
            }
        }
    }

}