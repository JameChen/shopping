package com.nahuo.live.xiaozhibo.permission;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hyphenate.easeui.VersionUtls;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

import static com.nahuo.live.demo.roomutil.commondef.BaseRoom.getPlayType;


/**
 * Created by admin on 2018/5/30.
 */

public class FloatingVideoService extends Service {
    public static boolean isStarted = false;
    private FloatingVideoService Vthis = this;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;

    private View displayView;
    private String mPlayUrl = "";
    private boolean IslivePlay;
    public static int changId, play_progress;
    public int mychangId, myplay_progress;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;
    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;
    private TXVodPlayer mTXVodPlayer;
    private TXLivePlayer mTXLivePlayer;
    protected EventBus mEventBus = EventBus.getDefault();

    @Override
    public void onCreate() {
        super.onCreate();
        if (mEventBus != null) {
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        } else {
            mEventBus = EventBus.getDefault();
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        }
        isStarted = true;
        Vthis = this;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {
            layoutParams.type = VersionUtls.getTYPE_APPLICATION_OVERLAY();
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = 250;
        layoutParams.height = 450;
        layoutParams.x = 0;
        layoutParams.y = 500;

        // mediaPlayer = new MediaPlayer();
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.BACKGROUND_ACTION:
                try {
                    if (event.data!=null){
                        int type= (int) event.data;
                        if (type==BWApplication.BACK_APP){
                            if (windowManager != null && displayView != null) {
                                windowManager.addView(displayView, layoutParams);
                                if (mTXVodPlayer!=null)
                                    mTXVodPlayer.resume();
                            }
                        }else if (type==BWApplication.LEAVE_APP){
                            if (windowManager != null && displayView != null) {
                                if (mTXVodPlayer!=null)
                                    mTXVodPlayer.pause();
                                windowManager.removeViewImmediate(displayView);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))
                mEventBus.unregister(this);
        }
        stopPlay(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            mPlayUrl = intent.getExtras().getString(TCConstants.PLAY_URL, "");
            IslivePlay = intent.getExtras().getBoolean(TCConstants.PLAY_TYPE, false);
            mychangId = intent.getExtras().getInt(TCConstants.PLAY_CHANG_ID);
            myplay_progress = intent.getExtras().getInt(TCConstants.PLAY_PROGRESS);
            changId = mychangId;
            play_progress = myplay_progress;
        }
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatingWindow() {
        stopPlay(true);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        displayView = layoutInflater.inflate(R.layout.video_display, null);
        ImageView iv_del = (ImageView) displayView.findViewById(R.id.iv_del);
        TXCloudVideoView mTXCloudVideoView = (TXCloudVideoView) displayView.findViewById(R.id.video_view);
        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        displayView.setOnTouchListener(new FloatingOnTouchListener());
        if (IslivePlay) {
            mTXLivePlayer = new TXLivePlayer(this);
            mTXLivePlayer.setPlayerView(mTXCloudVideoView);
            mTXLivePlayer.setAutoPlay(true);
            int playType = getPlayType(mPlayUrl);
            mTXLivePlayer.startPlay(mPlayUrl, playType);
        } else {
            mTXVodPlayer = new TXVodPlayer(this);
            mTXVodPlayer.setPlayerView(mTXCloudVideoView);
            mTXVodPlayer.setVodListener(new ITXVodPlayListener() {
                @Override
                public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
                    // Log.e("yu",i+"");
                    if (bundle != null) {
                        int progress = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
                        if (i == TXLiveConstants.PLAY_EVT_PLAY_END) {
                            play_progress = 0;
                        } else if (i == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
                            play_progress = progress;
                        } else if (i == TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED) {
                            if (myplay_progress > 0) {
                                mTXVodPlayer.seek(myplay_progress);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mTXVodPlayer.resume();
                                    }
                                }, 400);
                            }else {
                                mTXVodPlayer.resume();
                            }
                        }
                    }
                }

                @Override
                public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

                }
            });

            mTXVodPlayer.setAutoPlay(false);
            mTXVodPlayer.startPlay(mPlayUrl);
            mTXVodPlayer.pause();

        }
        windowManager.addView(displayView, layoutParams);
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXVodPlayer != null) {
            mTXVodPlayer.setVodListener(null);
            mTXVodPlayer.stopPlay(clearLastFrame);
        }
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mTXLivePlayer.setPlayerView(null);
        }
        if (windowManager != null && displayView != null) {
            windowManager.removeViewImmediate(displayView);
        }
    }

    private void initStopPlay(boolean clearLastFrame) {
        if (mTXVodPlayer != null) {
            mTXVodPlayer.setVodListener(null);
            mTXVodPlayer.stopPlay(clearLastFrame);
        }
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mTXLivePlayer.setPlayerView(null);
        }
        if (windowManager != null && displayView != null) {
            windowManager.removeViewImmediate(displayView);
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    xDownInScreen = event.getRawX();
                    yDownInScreen = event.getRawY();
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY();
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    Log.e("yu", "nowX=" + nowX + ">>nowY=" + nowY + ">>movedX=" + movedX + ">>movedY=" + movedY
                            + ">>layoutParams.x=" + layoutParams.x + ">>layoutParams.y =" +
                            layoutParams.y);
                    layoutParams.x = layoutParams.x - movedX;
                    layoutParams.y = layoutParams.y - movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(xDownInScreen - xInScreen) <= ViewConfiguration.get(Vthis).getScaledTouchSlop()
                            && Math.abs(yDownInScreen - yInScreen) <= ViewConfiguration.get(Vthis).getScaledTouchSlop()) {
                        // 点击效果
                        stopSelf();
                        BWApplication.getInstance().clearVActivity();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
