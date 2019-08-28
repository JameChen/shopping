package com.nahuo.quicksale.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.base.BaseActivty;

public class VideoActivity extends BaseActivty implements View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, View.OnTouchListener {

    /**
     * View播放
     */
    private VideoView videoView;

    /**
     * 加载预览进度条
     */
    private ProgressBar progressBar;

    /**
     * 设置view播放控制条
     */
    private MediaController mediaController;

    /**
     * 标记当视频暂停时播放位置
     */
    private int intPositionWhenPause = -1;

    /**
     * 设置窗口模式下的videoview的宽度
     */
    private int videoWidth;

    /**
     * 设置窗口模式下videoview的高度
     */
    private int videoHeight;
    private String url = "";
    public static String VIEDEO_URL_EXTR = "viedeo_url_extr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initVideoView();
    }

    /**
     * 初始化videoview播放
     */
    public void initVideoView() {
        //rl_video = (RelativeLayout) findViewById(R.id.rl_video);
        //初始化进度条
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //初始化VideoView
        videoView = (VideoView) findViewById(R.id.videoView);
        //初始化videoview控制条
        mediaController = new MediaController(this);
        //设置videoview的控制条
        videoView.setMediaController(mediaController);
        //设置显示控制条
        mediaController.show(0);
        //设置播放完成以后监听
        videoView.setOnCompletionListener(this);
        //设置发生错误监听，如果不设置videoview会向用户提示发生错误
        videoView.setOnErrorListener(this);
        //设置在视频文件在加载完毕以后的回调函数
        videoView.setOnPreparedListener(this);
        //设置videoView的点击监听
        videoView.setOnTouchListener(this);
        url = getIntent().getStringExtra(VIEDEO_URL_EXTR);
        Uri uri = Uri.parse(url);
        videoView.setVideoURI(uri);
        //设置为模式播放
        setVideoViewLayoutParams(2);
        findViewById(R.id.video_finish).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //启动视频播放
        videoView.start();
        //设置获取焦点
        videoView.setFocusable(true);
    }

    /**
     * 设置videiview的全屏和窗口模式
     *
     * @param paramsType 标识 1为全屏模式 2为窗口模式
     */
    public void setVideoViewLayoutParams(int paramsType) {

        if (1 == paramsType) {
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoView.setLayoutParams(LayoutParams);
        } else {
            //动态获取宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
            videoHeight = DisplayMetrics.heightPixels - 50;
            videoWidth = DisplayMetrics.widthPixels - 50;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            videoView.setLayoutParams(LayoutParams);
        }

    }

    /**
     * 设置videiview的全屏和窗口模式
     *
     * @param paramsType 标识 1为全屏模式 2为窗口模式
     */
    public void setVideoViewLayoutParams1(int paramsType) {

        if (1 == paramsType) {
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            videoView.setLayoutParams(LayoutParams);
        } else {
            //动态获取宽高
            DisplayMetrics DisplayMetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
            videoHeight = DisplayMetrics.heightPixels - 50;
            videoWidth = DisplayMetrics.widthPixels;
            RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
            LayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            videoView.setLayoutParams(LayoutParams);
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e("text", "发生未知错误");
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e("text", "媒体服务器死机");
                break;
            default:
                Log.e("text", "onError+" + what);
                break;
        }
        switch (extra) {
            case MediaPlayer.MEDIA_ERROR_IO:
                //io读写错误
                Log.e("text", "文件或网络相关的IO操作错误");
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                //文件格式不支持
                Log.e("text", "比特流编码标准或文件不符合相关规范");
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                //一些操作需要太长时间来完成,通常超过3 - 5秒。
                Log.e("text", "操作超时");
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                //比特流编码标准或文件符合相关规范,但媒体框架不支持该功能
                Log.e("text", "比特流编码标准或文件符合相关规范,但媒体框架不支持该功能");
                break;
            default:
                Log.e("text", "onError+" + extra);
                break;
        }
        //如果未指定回调函数， 或回调函数返回假，VideoView 会通知用户发生了错误。
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //如果文件加载成功,隐藏加载进度条
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_finish:
                finish();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * 页面暂停效果处理
     */
    @Override
    public void onPause() {
        super.onPause();
//        //如果当前页面暂定则保存当前播放位置，并暂停
//        intPositionWhenPause = videoView.getCurrentPosition();
//        //停止回放视频文件
        videoView.stopPlayback();
    }

    /**
     * 页面从暂停中恢复
     */
    @Override
    public void onResume() {
        super.onResume();
        //跳转到暂停时保存的位置
//        if (intPositionWhenPause >= 0) {
//            videoView.seekTo(intPositionWhenPause);
//            //初始播放位置
//            intPositionWhenPause = -1;
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != videoView) {
            videoView = null;
        }
    }

}
