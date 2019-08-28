package com.nahuo.quicksale.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;

/**
 * @description 添加悬浮层
 * @created 2015-1-16 上午10:01:31
 * @author ZZB
 */
public class ShowcaseBuilder {

    private List<ShowcaseItem> items;
    private Activity mActivity;
    private WindowManager mWindowManager;
    private int mCurIndex;
    private int mItemSize;
    //是否强制显示，如果不强制显示，每个图片，对应每个用户id只显示一次
    private boolean mForceShow = false;
    private boolean mActivityIsNull = false;

    public static ShowcaseBuilder with(Activity activity) {
        return new ShowcaseBuilder(activity);
    }

    private ShowcaseBuilder(Activity activity) {
        items = new ArrayList<ShowcaseBuilder.ShowcaseItem>();
        mActivity = activity;
        if(activity == null){//FIXME 有时activity会为空
            mActivityIsNull = true;
        }else{
            mWindowManager = mActivity.getWindowManager();
        }
    }

    public ShowcaseBuilder addItem(ShowcaseItem item) {
        items.add(item);
        return this;
    }
    public void build(boolean forceShow){
        mForceShow = forceShow;
        build();
    }
    public void build() {
        final WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,//TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.RGBA_8888);

        mItemSize = items.size();
        mCurIndex = 0;
        show(windowParams, items.get(mCurIndex));

    }
    /**
     * @description 判断是否需要显示
     * @created 2015-1-16 上午11:34:08
     * @author ZZB
     */
    private boolean needToShow(){
        if(mForceShow){
            return true;
        }
        if(mActivityIsNull){
            return false;
        }
        String showcaseUserIds = SpManager.getShowcaseUserIds(mActivity);
        int userId = SpManager.getUserId(mActivity);
        ShowcaseItem curItem = items.get(mCurIndex);
        String jsonKey = String.valueOf(curItem.imgResId);
        try {
            JSONObject jObj = TextUtils.isEmpty(showcaseUserIds) ? new JSONObject() : new JSONObject(showcaseUserIds);
            String userIds = jObj.has(jsonKey) ? jObj.getString(jsonKey) : "";
            if(StringUtils.contains(userIds, userId + "", ",")){
                return false;
            }else{
                userIds = userIds + "," + userId;
                jObj.put(jsonKey, userIds);
                SpManager.setShowcaseUserIds(mActivity, jObj.toString());
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }
    private void show(final WindowManager.LayoutParams windowParams, final ShowcaseItem item) {
        if(!needToShow()){
            return;
        }
        final View anchorView = item.anchorView;
        final int extraXInDp = item.extraXInDp;
        final int extraYInDp = item.extraYInDp;
        final int imgResId = item.imgResId;
        anchorView.post(new Runnable() {
            @Override
            public void run() {
                windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                windowParams.x = 0;
                windowParams.y = 0;
                final View bg = new View(mActivity);
                bg.setBackgroundColor(mActivity.getResources().getColor(R.color.black));
                bg.getBackground().setAlpha(200);
                mWindowManager.addView(bg, windowParams);

                windowParams.width = item.imgWidth > 0 ? item.imgWidth : WindowManager.LayoutParams.WRAP_CONTENT;
                windowParams.height = item.imgHeight > 0 ? item.imgHeight : WindowManager.LayoutParams.WRAP_CONTENT;
                int[] location = new int[2];
                anchorView.getLocationOnScreen(location);
//                anchorView.getLocationInWindow(location);
                windowParams.gravity = Gravity.TOP;
                windowParams.x = location[0] + DisplayUtil.dip2px(mActivity, extraXInDp);
                windowParams.y = location[1] + DisplayUtil.dip2px(mActivity, extraYInDp);
                final ImageView iv = new ImageView(mActivity);
                iv.setImageResource(imgResId);
                mWindowManager.addView(iv, windowParams);

                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWindowManager.removeViewImmediate(iv);
                        mWindowManager.removeViewImmediate(bg);
                        onDismiss(windowParams);
                    }
                });
                bg.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mWindowManager.removeViewImmediate(iv);
                        mWindowManager.removeViewImmediate(bg);
                        onDismiss(windowParams);
                    }
                });

            }
        });
    }
    
    private void onDismiss(WindowManager.LayoutParams windowParams) {
        mCurIndex++;
        if (mCurIndex < mItemSize) {
            show(windowParams, items.get(mCurIndex));
        }
    }

    public static class ShowcaseItem {
        public View anchorView;
        public int imgResId;
        public int extraXInDp;
        public int extraYInDp;
        public int imgWidth;
        public int imgHeight;

        public ShowcaseItem(View anchorView, int imgResId, int extraXInDp, int extraYInDp) {
            super();
            this.anchorView = anchorView;
            this.imgResId = imgResId;
            this.extraXInDp = extraXInDp;
            this.extraYInDp = extraYInDp;
        }

    }

}
