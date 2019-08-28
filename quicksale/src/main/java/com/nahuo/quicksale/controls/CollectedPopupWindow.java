package com.nahuo.quicksale.controls;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nahuo.quicksale.R;

/**
 * 收藏商品后的弹窗
 * Created by ZZB on 2015/9/21.
 */
public class CollectedPopupWindow extends PopupWindow {
    private Activity mActivity;
    private View mContentView;
    private CountDownTimer mTimer;
    private TextView mTvCountDown;
    public CollectedPopupWindow(Activity activity){
        mActivity = activity;
        init(activity);
    }

    private void init(Activity activity) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = inflater.inflate(R.layout.pop_collected, null);
        mTvCountDown = (TextView) mContentView.findViewById(R.id.tv_count_down);
        setContentView(mContentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置半透明背景色
        ColorDrawable dw = new ColorDrawable(0x50000000);
        setBackgroundDrawable(dw);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public CollectedPopupWindow setShowDuration(final int seconds){
        if(mTimer != null){
            mTimer.cancel();
        }

        mTimer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String countDown = mActivity.getString(R.string.collect_count_down, millisUntilFinished / 1000);
                mTvCountDown.setText(Html.fromHtml(countDown));
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(mTimer != null){
            mTimer.cancel();
        }
    }

    public void show(){
        mTimer.start();
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
}
