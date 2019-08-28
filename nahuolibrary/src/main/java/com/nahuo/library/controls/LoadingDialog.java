package com.nahuo.library.controls;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.R;
import com.nahuo.library.controls.pulltorefresh.CircleLoadingDrawable;

public class LoadingDialog extends Dialog {

    private Context mContext;
    private ImageView imageView;
    private CircleLoadingDrawable mCircleLoadingDrawable;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);
        InitDialog(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, R.style.LoadingDialog);
        InitDialog(context);
    }

    public void InitDialog(Context context) {
        mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.loadingdialog);
        getWindow().getAttributes().gravity = Gravity.CENTER;

    }
    private static LoadingDialog loadingDialog;
    public static LoadingDialog getInstance(Context context){
        if (loadingDialog==null){
            loadingDialog=new LoadingDialog(context);
        }
        return  loadingDialog;
    }
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        try {
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                R.anim.loading);
//        animation.setInterpolator(new LinearInterpolator());// 匀速旋转
        imageView = (ImageView) findViewById(R.id.loadingdialog_loadingicon);
//        imageView.setAnimation(animation);
//        animation.start();
//        mCircleLoadingDrawable = new CircleLoadingDrawable(mContext.getApplicationContext());
//        imageView.setImageDrawable(mCircleLoadingDrawable);
      //  mCircleLoadingDrawable.start();
    }

    /**
     * 设置提示信息
     *
     * @param message 提示信息
     */
    public void setMessage(String message) {
        TextView tvMsg = (TextView) findViewById(R.id.loadingdialog_message);
        if (tvMsg != null) {
            tvMsg.setText(message);
        }
    }
    /**
     * 设置提示信息
     *
     * @param message 提示信息
     */
    public void setMessage(int message) {
        TextView tvMsg = (TextView) findViewById(R.id.loadingdialog_message);
        if (tvMsg != null) {
            tvMsg.setText(message);
        }
    }
    /**
     * 弹出等待提示框
     */
    public void start() {
        start("");
    }

    /**
     * 弹出等待提示框
     *
     * @param message 提示信息
     */
    public void start(String message) {
        try {
            setMessage(message);
            show();
        } catch (Exception e) {
            //activity != null && !activity.isFinshing
            e.printStackTrace();
        }
    }
    /**
     * 弹出等待提示框
     *
     * @param message 提示信息
     */
    public void start(int message) {
        try {
            setMessage(message);
            show();
        } catch (Exception e) {
            //activity != null && !activity.isFinshing
            e.printStackTrace();
        }
    }

    /**
     * 关闭等待提示框
     */
    public void stop() {
        if (imageView != null && imageView.getAnimation() != null) {
            imageView.clearAnimation();
        }
        if (mCircleLoadingDrawable != null) {
            mCircleLoadingDrawable.stop();
        }
        try {
            dismiss();
            loadingDialog=null;
        } catch (Exception e) {
            //activity != null && !activity.isFinshing
            e.printStackTrace();
        }

    }

}
