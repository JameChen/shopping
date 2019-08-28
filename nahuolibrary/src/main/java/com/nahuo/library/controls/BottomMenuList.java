package com.nahuo.library.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nahuo.library.R;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

/**
 * @description底部弹出菜单,list形式，类似于上传头像时选择拍照和从相处选
 * @created 2015年5月13日 上午9:29:26
 * @author JorsonWong
 */
public class BottomMenuList extends PopupWindow implements OnItemClickListener, View.OnClickListener {

    private Activity            mActivity;
    private View                mRootView;
    private ListView            mGridView;
    private OnItemClickListener mMenuItemClickListener;
    private LinearLayout        mGridViewBg;
    private String[]            mItemTexts;

    public BottomMenuList(Activity activity) {
        super();
        this.mActivity = activity;
        initViews();
    }

    public BottomMenuList(Activity activity, AttributeSet atrr) {
        super();
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.bottom_menu_list, null);
        mRootView.setOnClickListener(this);
        mGridView = (ListView)mRootView.findViewById(android.R.id.list);
        mGridViewBg = (LinearLayout)mRootView.findViewById(android.R.id.content);
        mRootView.findViewById(android.R.id.button1).setOnClickListener(this);
        mGridView.setOnItemClickListener(this);

    }

    public BottomMenuList setItems(String... items) {
        this.mItemTexts = items;
        return this;
    }

    private BottomMenuList create() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mItemTexts.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", mItemTexts[i]);
            maps.add(map);
        }
        String[] from = new String[] {"text"};
        int[] to = new int[] {android.R.id.title};
        SimpleAdapter adapter = new SimpleAdapter(mActivity, maps, R.layout.bottom_menu_list_item, from, to);
        mGridView.setAdapter(adapter);

        return this;
    }

    /**
     * @description 显示菜单栏
     * @created 2015年3月20日 上午11:22:23
     * @author JorsonWong
     */
    public void show() {

        create();

        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x50000000);
        this.setBackgroundDrawable(dw);

        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        mGridViewBg.setVisibility(View.VISIBLE);
        mGridViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_appear));
        setAnimationStyle(R.style.LightPopDialogAnim);

    }

    public int getStatusHeight() {
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        return statusHeight;
    }

    /**
     * @description item click callback
     * @created 2015年3月20日 上午11:21:15
     * @author JorsonWong
     */
    public BottomMenuList setOnMenuItemClickListener(OnItemClickListener listener) {
        this.mMenuItemClickListener = listener;
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mMenuItemClickListener != null) {
            mMenuItemClickListener.onItemClick(parent, view, position, id);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void dismiss() {
        if (mGridViewBg.isShown()) {
            Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.bottom_menu_disappear);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mGridViewBg.clearAnimation();
                    mGridViewBg.setVisibility(View.GONE);
                    dismiss();
                }
            }, anim.getDuration());
            mGridViewBg.startAnimation(anim);
        } else {
            super.dismiss();
        }
    }

}
