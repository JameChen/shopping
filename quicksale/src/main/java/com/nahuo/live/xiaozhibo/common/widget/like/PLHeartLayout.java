/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nahuo.live.xiaozhibo.common.widget.like;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.nahuo.quicksale.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 飘心动画界面布局类
 * 通过动画控制每个心形界面的显示
 * TCPathAnimator 控制显示路径
 * TCHeartView 单个心形界面
 */
public class PLHeartLayout extends RelativeLayout {

    private TCAbstractPathAnimator mAnimator;
    private int defStyleAttr = 0;

    private int textHight;
    private int dHeight;
    private int dWidth;
    private int initX;
    private int pointx;
    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE){
           // Log.e("yu" ,"可见");
            //开始某些任务
        }
        else if(visibility == INVISIBLE || visibility == GONE){
           // Log.e("yu" ,"不可见");
            clearAnimation();
            //停止某些任务
        }
    }
    public PLHeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        findViewById(context);
        initHeartDrawable();
        init(attrs, defStyleAttr);
        goToLikes();
    }

    private void findViewById(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_live_periscope, this);
        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.h_01);
        dHeight = sp2px(getContext(), 30);
        dWidth = sp2px(getContext(), 30);
        textHight = sp2px(getContext(), 20) + dHeight / 2;

        pointx = dWidth;//随机上浮方向的x坐标

        //bitmap.recycle();
    }

    private static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    Timer timer;

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeartLayout, defStyleAttr, 0);

        //todo:获取确切值
        initX = sp2px(getContext(), 30);
//        if (pointx <= initX && pointx >= 0) {
//            pointx -= 10;
//        } else if (pointx >= -initX && pointx <= 0) {
//            pointx += 10;
//        } else pointx = initX;

        mAnimator = new TCPathAnimator(
                TCAbstractPathAnimator.Config.fromTypeArray(a, initX, textHight, pointx, dWidth, dHeight));
        a.recycle();


    }


    private void  goToLikes(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // (1) 使用handler发送消息
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        }, 0, 400);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                addFavor();
            }
        }
    };

    public void clearAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();
    }

    public void resourceLoad() {
        mHearts = new Bitmap[drawableIds.length];
        mHeartsDrawable = new BitmapDrawable[drawableIds.length];
        for (int i = 0; i < drawableIds.length; i++) {
            mHearts[i] = BitmapFactory.decodeResource(getResources(), drawableIds[i]);
            mHeartsDrawable[i] = new BitmapDrawable(getResources(), mHearts[i]);
        }
    }

    private static int[] drawableIds = new int[]{R.drawable.h_01, R.drawable.h_02, R.drawable.h_03,
            R.drawable.h_04, R.drawable.h_05, R.drawable.h_06, R.drawable.h_07, R.drawable.h_08, R.drawable.h_09,
            R.drawable.h_10, R.drawable.h_11};
    private Random mRandom = new Random();
    private static Drawable[] sDrawables;
    private Bitmap[] mHearts;
    private BitmapDrawable[] mHeartsDrawable;

    private void initHeartDrawable() {
        int size = drawableIds.length;
        sDrawables = new Drawable[size];
        for (int i = 0; i < size; i++) {
            sDrawables[i] = getResources().getDrawable(drawableIds[i]);
        }
        resourceLoad();
    }

    public void addFavor() {
        TCHeartView heartView = new TCHeartView(getContext());
        heartView.setDrawable(mHeartsDrawable[mRandom.nextInt(10)]);
//        heartView.setImageDrawable(sDrawables[random.nextInt(8)]);
        //  init(attrs, defStyleAttr);
        mAnimator.start(heartView, this);
    }

}
