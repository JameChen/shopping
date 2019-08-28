package com.nahuo.quicksale;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nahuo.library.controls.CircleTextView;

/**
 * @description
 * @created 2015年4月1日 下午2:03:34
 * @author ALAN
 */
public class AbstractActivity {
    public static final String       EXTRA_TITLE_DATA = "extra_title_data";
    private Activity                 mContext;
    // private TextView tvTitle;
    private TextView                 tvLeft;
    private TextView                 tvRight;
    private CircleTextView circleTextView;
    private BackPressOnClickListener mBackPressOnClickListener;
    private ProgressBar              progress;
    private ImageView                imgSearch;
    private TextView                 tvTitle;
    private OnClickListener          mTvLeftClickListener;
    private boolean mNoTitle;

    public AbstractActivity(boolean noTitle) {
        mNoTitle = noTitle;
    }
    public void createContent(View view, Activity activity) {
        if(mNoTitle) {
            initNoTitleActivity(view, activity);
        }else {
            initTitleActivity(view, activity);
        }
        initView(activity);

    }

    private void initNoTitleActivity(View view, Activity activity) {
      //  activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
       // activity.setContentView(view);
      //  activity.getWindow().setBackgroundDrawable(activity.getResources().getDrawable(R.color.bg_titlebar));
    }
    private void initTitleActivity(View view, Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.my_colorPrimaryDark));
//        }
        //activity.getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
      //  super.setContentView(view);
//        activity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
//        activity.getWindow().setBackgroundDrawable(activity.getResources().getDrawable(R.color.bg_titlebar));
    }

    public void initView(Activity context) {
        mContext = context;
        if(!mNoTitle) {
            mBackPressOnClickListener = new BackPressOnClickListener(context);
            tvTitle = (TextView)context.findViewById(R.id.tvTitleCenter);
            tvLeft = (TextView)context.findViewById(R.id.tvTLeft);
            tvLeft.setOnClickListener(mBackPressOnClickListener);
            tvRight = (TextView)context.findViewById(R.id.tvTRight);
            circleTextView=(CircleTextView) context.findViewById(R.id.circle_car_text);
            imgSearch = (ImageView)context.findViewById(R.id.img_Tsearch);
            progress = (ProgressBar)context.findViewById(R.id.pbgT);
        }
        
    }

    // public void setCenterClickListener(View.OnClickListener listener) {
    // tvTitle.setOnClickListener(listener);
    // }
    public void setLeftText(CharSequence text) {
        tvLeft.setText(text);
        tvLeft.setOnClickListener(mBackPressOnClickListener);

    }

    public void setLeftText(int resId) {
        tvLeft.setText(mContext.getString(resId));
        tvLeft.setOnClickListener(mBackPressOnClickListener);
    }

    public void setTitleText(CharSequence text) {
        tvTitle.setText(text);
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void setTitleText(int resId) {
        tvTitle.setText(resId);
        tvTitle.setVisibility(View.VISIBLE);
    }

    public void setRightText(int resId) {
        tvRight.setText(mContext.getString(resId));
    }

    public void setRightText(CharSequence text) {
        tvRight.setText(text);
    }

    public CircleTextView getCircleTextView(){
        return circleTextView;
    }

    // public void setCenterBackground(int resid) {
    // tvTitle.setBackgroundResource(resid);
    // }

    public void setLeftClickListener(View.OnClickListener listener) {
        this.mTvLeftClickListener = listener;
        tvLeft.setOnClickListener(mTvLeftClickListener);
    }

    public void setRightClickListener(View.OnClickListener listener) {
        tvRight.setOnClickListener(listener);
    }

    public void setSearchClickListener(View.OnClickListener listener) {
        imgSearch.setOnClickListener(listener);
    }

    public void showLeft(boolean isShow) {
        tvLeft.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    public void setLeftIcon(int resid) {
        tvLeft.setCompoundDrawablesWithIntrinsicBounds(resid, 0, 0, 0);
    }

    public void showRight(boolean isShow) {
        tvRight.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    public void setRightIcon(int resid) {
        showRight(true);
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, resid, 0);
    }

    public void showProgress(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    public void showSearch(boolean show) {
        imgSearch.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showBackIcon(boolean show) {
        setLeftIcon(show ? R.drawable.back : 0);
    }

    public void setSearchIcon(int resId) {
        imgSearch.setImageResource(resId);
        showSearch(resId > 0);
    }

    /**
     * 返回事件处理
     */
    private class BackPressOnClickListener implements OnClickListener {
        Activity activity;

        BackPressOnClickListener(Activity context) {
            activity = context;
        }

        @Override
        public void onClick(View v) {
            if (mTvLeftClickListener == null) {
                if (activity instanceof FragmentActivity) {
                    FragmentActivity fragmentActivity = (FragmentActivity)mContext;
                    fragmentActivity.onBackPressed();
                } else {
                    activity.finish();
                }
            } else {
                mTvLeftClickListener.onClick(v);
            }
        }
    }

    public boolean isNoTitle() {
        return mNoTitle;
    }
}
