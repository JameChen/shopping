package com.nahuo.quicksale.activity;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.MediaStoreUtils;
import com.nahuo.quicksale.customview.MyJCVideoPlayerStandard;
import com.nahuo.quicksale.customview.RoundProgressBarWidthNumber;
import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayer;
import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayerStandard;

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

import static com.nahuo.quicksale.util.DownloadService.IO_BUFFER_SIZE;

public class VideoActivity1 extends BaseAppCompatActivity implements View.OnClickListener {
    private static String TAG = "VideoActivity1";
    private String url = "";
    public static String VIEDEO_URL_EXTR = "viedeo_url_extr";
    public static String VIEDEO_ISHOW_DOWN_EXTR = "viedeo_down_isshow_extr";
    private MyJCVideoPlayerStandard jcVideoPlayerStandard;
    private ImageView btn_download;
    //  public static final String PINHUP_ROOT_DIRECTORY="/mnt/sdcard/pinhuo/pihuo_video_save";
    public static final String PINHUP_ROOT_DIRECTORY = "/pinhuo/pinhuo_video_save";
    private static final String CACHE_FILENAME_PREFIX = "cache_";
    // 下载目录
    private String downloadPath;
    private View layout_dialog;
    private RoundProgressBarWidthNumber progressbar;
    private String Dcim_Path;
    private boolean canDownLoadPicAndVideo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video1);
        JCVideoPlayer.JC_TYPE=JCVideoPlayer.JC_DEFAUT_TYPE;
        url = getIntent().getStringExtra(VIEDEO_URL_EXTR);
        canDownLoadPicAndVideo=getIntent().getBooleanExtra(VIEDEO_ISHOW_DOWN_EXTR,false);
        Dcim_Path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        jcVideoPlayerStandard = (MyJCVideoPlayerStandard) findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(url
                , JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "天天拼货团");
        // JCVideoPlayer.startFullscreen(this,JCVideoPlayer.class,url,"天天拼货团");
        //jcVideoPlayerStandard.thumbImageView.setImage("");
        findViewById(R.id.video_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    // JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    // JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

            }
        });
        btn_download = (ImageView) findViewById(R.id.btn_download);
        layout_dialog = findViewById(R.id.layout_dialog);
        layout_dialog.setOnClickListener(this);
        progressbar = (RoundProgressBarWidthNumber) findViewById(R.id.progressbar);
        btn_download.setOnClickListener(this);
        if (canDownLoadPicAndVideo){
            btn_download.setVisibility(View.VISIBLE);
        }else {
            btn_download.setVisibility(View.GONE);
        }
        jcVideoPlayerStandard.startVideo();
    }

    @Override
    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.clearSavedProgress(this, url);
    }

    public static String createFilePath(File cacheDir, String key) {
        try {
            return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX
                    + URLEncoder.encode(key.replace("*", ""), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            Log.e(TAG, "createFilePath - " + e);
        }

        return null;
    }

    /**
     * 下载视频
     *
     * @param urlString
     * @return
     */
    private File downloadVideo(String urlString) {
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
            //listener.onFailed();

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
    String fileName = "";
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                if (!TextUtils.isEmpty(url)) {
                    try {
                        fileName = url.substring(url.lastIndexOf("/"), url.length());
                    } catch (Exception e) {
                        fileName = "/"+System.currentTimeMillis() + ".mp4";
                    }
                    File downloadDirectory = new File(Dcim_Path+PINHUP_ROOT_DIRECTORY);
                    if (!downloadDirectory.exists()) {
                        downloadDirectory.mkdirs();
                    }
                    File cacheFile = new File(Dcim_Path+PINHUP_ROOT_DIRECTORY+fileName);
                    if (cacheFile.exists()) {
                        ViewHub.showShortToast(this, "视频已经下载在本地");
                    } else {
                        new DownLoadVideoTask(url).execute(url);
                    }

                } else {
                    ViewHub.showShortToast(this, "视频地址不能为空");
                }
                break;
        }
    }

    class DownLoadVideoTask extends AsyncTask<String, Double, Boolean> {
        String urlString;

        DownLoadVideoTask(String urlString) {
            this.urlString = urlString;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout_dialog.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Log.i(TAG, "doInBackground params[0]=" + params[0]);
            HttpURLConnection conn = null;
            try {
                URL url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("Connection", "Keep-Alive");
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } //获取文件长度
            long videoTotalLength = conn.getContentLength();
            String fileName = "";
            try {
                fileName = url.substring(url.lastIndexOf("/"), url.length());
            } catch (Exception e) {
                fileName = "/"+System.currentTimeMillis() + ".mp4";
            }
            File file = new File(Dcim_Path+PINHUP_ROOT_DIRECTORY+fileName);
            try { //创建File对象，文件路径为/sdcard/test.mp4
                InputStream in = conn.getInputStream();
                if (in == null)
                    return false;
                //如果文件已经存在，并且第二个参数为true，则向原先的文件追加数据，否则重新写。
                FileOutputStream os = new FileOutputStream(file, false);
                byte[] buffer = new byte[IO_BUFFER_SIZE];
                int len = 0;
                int current = 0;
                while ((len = in.read(buffer)) != -1) {
                    current += len;
                    //更新进度
                    publishProgress((double) current,
                            (double) videoTotalLength);
                    os.write(buffer, 0, len);
                }
                os.close();
                in.close();
            } catch (Exception e) {
                if (file.exists())
                    file.delete();
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Double... values) {
            //设置ProgressBar进度
            progressbar.setProgress((int) (values[0] / values[1] * 100));
        }

        @Override
        protected void onPostExecute(Boolean result) {
            layout_dialog.setVisibility(View.GONE);
            if (result) {
                Log.i(TAG, "下载文件成功");
                MediaStoreUtils.scanImageFile(VideoActivity1.this, Dcim_Path+PINHUP_ROOT_DIRECTORY+fileName);
                ViewHub.showLongToast(VideoActivity1.this, "下载视频成功" );
            } else {
                Log.i(TAG, "下载文件失败");
                ViewHub.showShortToast(VideoActivity1.this, "下载失败");
            }
        }


    }
}
