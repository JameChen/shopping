package com.nahuo.quicksale.util;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.nahuo.Dialog.PdMenu;
import com.nahuo.quicksale.activity.VideoActivity1;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.MediaStoreUtils;

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

public class PicOrVideoDownloadService {
    private static String TAG = "DownloadService";
    //  public static final String PINHUP_ROOT_DIRECTORY = "/mnt/sdcard/pinhuo/pihuo_save";
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    private static ExecutorService SINGLE_TASK_EXECUTOR = null;
    private static ExecutorService LIMITED_TASK_EXECUTOR = null;
    private static final ExecutorService FULL_TASK_EXECUTOR = null;
    private static final ExecutorService DEFAULT_TASK_EXECUTOR;
    private static Object lock = new Object();
    ArrayList<Uri> imageList = new ArrayList();
    List<Boolean> videosLoadList = new ArrayList<>();
    PdMenu pdMenu = null;
    private int type;//1,下载图片和视频 2下载图片3下载视频
    public static int TYPE_PIC_VIDEO = 1;
    public static int TYPE_PIC = 2;
    public static int TYPE_VIDEO = 3;

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

    // 图片下载链接集合
    private List<String> listURL;
    // 下载个数
    private int size = 0, vSize = 0;
    Activity activity;
    // 视频下载链接集合
    private List<String> videosListURL;
    public static PicOrVideoDownloadService instace;
    String Dcim_Path;

    // 下载完成回调接口
    public interface DownloadStateListener {
        void onFinish();

        void onFailed();

        void onUpdate(int pSize, int pTotalCount, int vSize, int vTotalCount, int type);
    }

    public static PicOrVideoDownloadService getInstace(Activity activity, String Dcim_Path, List<String> listURL, List<String> videosListURL,
                                                       DownloadStateListener listener) {
        instace = new PicOrVideoDownloadService(activity, Dcim_Path, listURL, videosListURL, listener);
        return instace;
    }

    public PicOrVideoDownloadService(Activity activity, String Dcim_Path, List<String> listURL, List<String> videosListURL,
                                     DownloadStateListener listener) {
        this.activity = activity;
        this.listURL = listURL;
        this.videosListURL = videosListURL;
        this.listener = listener;
        this.Dcim_Path = Dcim_Path;
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
        imageList.clear();
        videosLoadList.clear();
        size = 0;
        vSize = 0;
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
        if (!ListUtils.isEmpty(listURL) && !ListUtils.isEmpty(videosListURL)) {
            //下载图片和视频
            type = TYPE_PIC_VIDEO;
            for (final String url : listURL) {
                //捕获线程池拒绝执行异常
                try {
                    DownLoadExecutor.execute(new Runnable() {

                        @Override
                        public void run() {
                            File file = downloadBitmap(url);
                            if (file != null) {
                                imageList.add(Uri.fromFile(file));
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
        } else if (!ListUtils.isEmpty(listURL) && ListUtils.isEmpty(videosListURL)) {
            //下载图片
            type = TYPE_PIC;
            for (final String url : listURL) {
                //捕获线程池拒绝执行异常
                try {
                    DownLoadExecutor.execute(new Runnable() {

                        @Override
                        public void run() {
                            File file = downloadBitmap(url);
                            if (file != null) {
                                imageList.add(Uri.fromFile(file));
                            }
                            statDownloadPicsNum();
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
        } else if (ListUtils.isEmpty(listURL) && !ListUtils.isEmpty(videosListURL)) {
            //下载视频
            type = TYPE_VIDEO;
            for (final String xurl : videosListURL) {
                //捕获线程池拒绝执行异常
                try {
                    DownLoadExecutor.execute(new Runnable() {

                        @Override
                        public void run() {
                            boolean isDload = dowlnVideos(xurl);
                            if (isDload) {
                                videosLoadList.add(isDload);
                            }
                            synchronized (lock) {
                                vSize++;
                                listener.onUpdate(size, listURL.size(), vSize, videosListURL.size(), type);
                                if (vSize >= videosListURL.size()) {
                                    listener.onFinish();
                                    DownLoadExecutor.shutdown();
                                }
                            }
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


    }

    /**
     * 统计下载个数
     */
    private void statDownloadPicsNum() {
        synchronized (lock) {
            size++;
//            Message message = Message.obtain();
//            message.arg1 = size;
//            message.what = 0;
//            handler.sendMessage(message);
            listener.onUpdate(size, listURL.size(), 0, 0, type);
            if (size >= listURL.size()) {
                // 释放资源
                //  DEFAULT_TASK_EXECUTOR.shutdownNow();
                // 如果下载成功的个数与列表中 url个数一致，说明下载成功
                listener.onFinish(); // 下载成功回调
                DownLoadExecutor.shutdown();
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
        String fileName = "";
        // 图片命名方式
        try {
            fileName = urlString.substring(urlString.lastIndexOf("/"), urlString.lastIndexOf("!"));
        } catch (Exception e) {
            fileName = "/" + System.currentTimeMillis() + ".jpg";
        }
        String path = Dcim_Path + "/" + Const.IMAGES_CASH_PATH + fileName;
        File cacheFile = new File(path);
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
            MediaStoreUtils.scanImageFile(activity, path);
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
            listener.onUpdate(size, listURL.size(), 0, videosListURL.size(), type);
            if (size >= listURL.size()) {
                // 释放资源
                //  DEFAULT_TASK_EXECUTOR.shutdownNow();
                // 如果下载成功的个数与列表中 url个数一致，说明下载成功
                // listener.onFinish(imageList); // 下载成功回调
                //  DownLoadExecutor.shutdown();
                //去下载视频
                for (final String xurl : videosListURL) {
                    //捕获线程池拒绝执行异常
                    try {
                        DownLoadExecutor.execute(new Runnable() {

                            @Override
                            public void run() {
                                boolean isDload = dowlnVideos(xurl);
                                if (isDload) {
                                    videosLoadList.add(isDload);
                                }
                                synchronized (lock) {
                                    vSize++;
                                    listener.onUpdate(size, listURL.size(), vSize, videosListURL.size(), type);
                                    if (vSize >= videosListURL.size()) {
                                        listener.onFinish();
                                        DownLoadExecutor.shutdown();
                                    }
                                }
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
        }
    }

    private boolean dowlnVideos(String xurl) {
        boolean flag = false;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(xurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Connection", "Keep-Alive");
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            listener.onFailed();
        } //获取文件长度
        long videoTotalLength = conn.getContentLength();
        String fileName = "";
        try {
            fileName = xurl.substring(xurl.lastIndexOf("/"), xurl.length());
        } catch (Exception e) {
            fileName = "/" + System.currentTimeMillis() + ".mp4";
        }
        String path = Dcim_Path + VideoActivity1.PINHUP_ROOT_DIRECTORY + fileName;
        File file = new File(path);
        try { //创建File对象，文件路径为/sdcard/test.mp4
            InputStream in = conn.getInputStream();
            if (in == null) {
                flag = false;
                listener.onFailed();
            }
            //如果文件已经存在，并且第二个参数为true，则向原先的文件追加数据，否则重新写。
            FileOutputStream os = new FileOutputStream(file, false);
            byte[] buffer = new byte[IO_BUFFER_SIZE];
            int len = 0;
            int current = 0;
            while ((len = in.read(buffer)) != -1) {
                current += len;
                //更新进度
                os.write(buffer, 0, len);
            }
            MediaStoreUtils.scanImageFile(activity, path);
            os.close();
            in.close();
        } catch (Exception e) {
            if (file.exists())
                file.delete();
            e.printStackTrace();
            flag = false;
            listener.onFailed();
        }
        return flag;
    }

}