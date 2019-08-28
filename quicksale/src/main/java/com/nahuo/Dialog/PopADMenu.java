package com.nahuo.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.adapter.ImageBannerAdapter;
import com.nahuo.quicksale.common.Constant;
import com.nahuo.quicksale.customview.ViewFlow;
import com.nahuo.quicksale.customview.ViewFlowCircleIndicator;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.task.UpdateTask;
import com.nahuo.service.autoupdate.AppUpdate;

import java.util.List;

import static com.nahuo.quicksale.adapter.ImageBannerAdapter.IMGE_CENTER;

/**
 * Created by jame on 2017/4/12.
 */

public class PopADMenu extends Dialog {
    private Activity fragment;
    private View mRootView;
    private List<BannerAdModel> adList;
    private ViewFlow mViewFlow;
    private ViewFlowCircleIndicator mViewFlowIndic;
    private View iv_del;
    private ImageBannerAdapter bAdapter;
    private View mContentViewBg;
    private int h;
    private int w;
    private Activity mActivity;
    private Context context;
    private static PopADMenu dialog = null;
    private AppUpdate mAppUpdate;

    public static PopADMenu getInstance(Context context, Activity fragment, List<BannerAdModel> list, AppUpdate mAppUpdate) {
        if (dialog == null) {
            synchronized (PopADMenu.class) {
                if (dialog == null) {
                    dialog = new PopADMenu(context, fragment, list, mAppUpdate);
                }
            }
        }
        return dialog;
    }

    public PopADMenu(Context context, Activity fragment, List<BannerAdModel> list, AppUpdate mAppUpdate) {
        super(context, R.style.popDialog);
        this.context = context;
        this.fragment = fragment;
        this.mActivity = fragment;
        this.adList = list;
        this.mAppUpdate = mAppUpdate;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {

        h = mActivity.getResources().getDisplayMetrics().heightPixels;
        w = mActivity.getResources().getDisplayMetrics().widthPixels;
        mRootView = LayoutInflater.from(context).inflate(R.layout.index_pop_ad_menu, null);
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(w, h);
        setContentView(mRootView, layoutParams);
        setCanceledOnTouchOutside(false);
        OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        setOnKeyListener(keylistener);
        setCancelable(true);
        mViewFlow = (ViewFlow) mRootView.findViewById(R.id.viewflow);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h);
        mViewFlow.setLayoutParams(params);
        mViewFlowIndic = (ViewFlowCircleIndicator) mRootView.findViewById(R.id.viewflowindic);
        iv_del = mRootView.findViewById(R.id.iv_del);
        mContentViewBg = mRootView.findViewById(R.id.bannerview);
        mViewFlow.setTimeSpan(Constant.ADVERTISE_TIME);
        mViewFlow.setFlowIndicator(mViewFlowIndic);
        int size = adList == null ? 0 : adList.size();
        mViewFlow.setSelection(size * 1000);
        mViewFlow.setSideBuffer(size);
        bAdapter = new ImageBannerAdapter(mActivity, adList);
        bAdapter.setImge_type(IMGE_CENTER);
        mViewFlow.setAdapter(bAdapter);
        mViewFlow.startAutoFlowTimer();
        iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = null;
                dismiss();
            }
        });
        bAdapter.setOnItemClickListener(new ImageBannerAdapter.BannerOnclickListener() {
            @Override
            public void onItemClick(View v, int positon) {
                //Toast.makeText(mActivity, positon + "", Toast.LENGTH_SHORT).show();
                if (positon >= 0) {
                    if (adList.get(positon).del_flag) {
                        //去强制更新
                        new UpdateTask(context, mAppUpdate, false, false).execute();

                    } else {
                        BannerAdModel selectAd = adList.get(positon);
                        if (fragment instanceof MainNewActivity) {
                            ((MainNewActivity) fragment).gotoBannerJump(selectAd);
                        }
                        dialog = null;
                        dismiss();
                    }


                }
            }
        });
        for (BannerAdModel bean : adList) {
            if (bean.del_flag) {
                iv_del.setVisibility(View.INVISIBLE);
                return;
            }
        }
    }

//    public void show() {
////        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
////        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
////
//////        this.setWidth(w * 4 / 5);
//////        this.setHeight(h * 2 / 3);
////        this.setContentView(mRootView);
////        this.setFocusable(false);
////        this.setOutsideTouchable(false);
//////        ColorDrawable dw = new ColorDrawable(0xb0000000);
//////        this.setBackgroundDrawable(dw);
////        setBackgroundDrawable(new BitmapDrawable());
////        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
////        mContentViewBg.setVisibility(View.VISIBLE);
////        mContentViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_appear));
//
//    }


}
