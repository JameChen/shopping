package com.nahuo.library.controls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nahuo.library.R;
import com.nahuo.library.controls.NoScrollGridView;
import com.nahuo.library.helper.DisplayUtil;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

/**
 * @description 分享的小菜单
 * @created 2015年3月19日 下午4:55:52
 * @author JorsonWong
 */
public class ShareMenu extends PopupWindow implements OnItemClickListener {

    private Activity            mActivity;
    private View                mRootView;
    private NoScrollGridView    mGridView;
    private OnItemClickListener mOnItemClickListener;
    private boolean             isAnchorOnScreenLeft;

    private static int          ITEM_WIDTH      = 100;                                     // 按钮宽度，单位dip
    private static int          SPACING         = 5;                                       // 控件与popwindow间隔，单位dip
    private int[]               mAnchorLocation = new int[2];
    // private String[] mItems;
    // private int[] mIconsRes;
    private List<ShareMenuItem> menuItems       = new ArrayList<ShareMenu.ShareMenuItem>();

    public ShareMenu(Activity activity) {
        super();
        this.mActivity = activity;
        initViews();
    }

    public ShareMenu(Activity activity, AttributeSet attributeSet) {
        super();
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.share_menu, null);
        mGridView = (NoScrollGridView)mRootView.findViewById(android.R.id.list);
        mGridView.setOnItemClickListener(this);
        mRootView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int[] location = new int[2];
                mRootView.getLocationOnScreen(location);
                // int y = (int)event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismiss();
                }
                return true;
            }
        });
    }

    // public ShareMenu setItems(String... items) {
    // this.mItems = items;
    // return this;
    // }
    //
    // public ShareMenu setIconsResId(int... resids) {
    // this.mIconsRes = resids;
    // return this;
    // }

    /**
     * 添加menu item
     * 
     * @param item
     * @return
     */
    public ShareMenu addMenuItem(ShareMenuItem item) {
        menuItems.add(item);
        return this;
    }

    private ShareMenu create() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (ShareMenuItem item : menuItems) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", item.text);
            map.put("icon", item.iconResId);
            maps.add(map);
        }
        String[] from = new String[] {"text", "icon"};
        int[] to = new int[] {android.R.id.title, android.R.id.icon};
        SimpleAdapter adapter = new SimpleAdapter(mActivity, maps, R.layout.share_menu_item, from, to);
        mGridView.setAdapter(adapter);

        return this;
    }

    // private void setItemData(String[] items) {
    // List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
    // for (int i = 0; i < items.length; i++) {
    // Map<String, Object> map = new HashMap<String, Object>();
    // map.put("text", items[i]);
    // maps.add(map);
    // }
    // String[] from = new String[] { "text" };
    // int[] to = new int[] { android.R.id.title };
    // SimpleAdapter adapter = new SimpleAdapter(mActivity, maps,
    // R.layout.share_menu_item, from, to);
    // mGridView.setAdapter(adapter);
    // }
    // private void setItemData(String[] items, int[] icons) {
    // List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
    // for (int i = 0; i < items.length; i++) {
    // Map<String, Object> map = new HashMap<String, Object>();
    // map.put("text", items[i]);
    // map.put("icon", icons[i]);
    // maps.add(map);
    // }
    // String[] from = new String[] { "text", "icon" };
    // int[] to = new int[] { android.R.id.title, android.R.id.icon };
    // SimpleAdapter adapter = new SimpleAdapter(mActivity, maps,
    // R.layout.share_menu_item, from, to);
    // mGridView.setAdapter(adapter);
    // }

    /**
     * @description 显示popwindow,带图标
     * @created 2015年3月20日 上午11:06:35
     * @author JorsonWong
     */
    public void show(View v) {
        initPopWindow(v);
        // setItemData(items, iconRes);
        create();

        int spacing = DisplayUtil.dip2px(mActivity, SPACING);
        showAtLocation(v, Gravity.NO_GRAVITY, isAnchorOnScreenLeft ? mAnchorLocation[0] + v.getWidth() + spacing
                : mAnchorLocation[0] - getWidth() - spacing, mAnchorLocation[1]);

    }

    /**
     * @description 显示popwindow
     * @created 2015年3月20日 上午11:06:35
     * @author JorsonWong
     */
    // public void show(View v, String[] items) {
    // initPopWindow(v, items.length);
    // setItemData(items);
    // int spacing = DisplayUtil.dip2px(mActivity, SPACING);
    // showAtLocation(v, Gravity.NO_GRAVITY,
    // isAnchorOnScreenLeft ? mAnchorLocation[0] + v.getWidth()
    // + spacing : mAnchorLocation[0] - getWidth() - spacing,
    // mAnchorLocation[1]);
    // }

    /**
     * @description 初始化popwindow
     * @created 2015年3月20日 上午11:15:40
     * @author JorsonWong
     */
    private void initPopWindow(View v) {
        int maxWidth;
        Display display = mActivity.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        v.getLocationOnScreen(mAnchorLocation);
        if ((2 * mAnchorLocation[0] + v.getWidth()) / 2 < width / 2) {
            maxWidth = width - (mAnchorLocation[0] + v.getWidth());
            isAnchorOnScreenLeft = true;
        } else {
            maxWidth = mAnchorLocation[0];
        }
        int spacing = DisplayUtil.dip2px(mActivity, SPACING);
        maxWidth = maxWidth - spacing;
        int itemWidth = DisplayUtil.dip2px(mActivity, ITEM_WIDTH);
        int columns = maxWidth / itemWidth;
        if (columns > menuItems.size()) {// 自动计算列数
            columns = menuItems.size();
        }
        if (columns > 2) {// 最多一行2列
            columns = 2;
        }
        mGridView.setNumColumns(columns);
        this.setWidth(itemWidth * columns);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setContentView(mRootView);
        this.setFocusable(true);
        setAnimationStyle(isAnchorOnScreenLeft ? R.style.PopupAnimationInLeftOutLeft
                : R.style.PopupAnimationInRightOutRight);

    }

    /**
     * @description item click callback
     * @created 2015年3月20日 上午11:21:15
     * @author JorsonWong
     */
    public ShareMenu setMenuItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(parent, view, position, id);
        }
        dismiss();
    }

    public static class ShareMenuItem {
        public int    iconResId = 0;
        public String text;

        /**
         * 
         * @param text :menu item text
         */
        public ShareMenuItem(String text) {
            this.text = text;
        }

        /**
         * 
         * @param text :menu item text
         * @param iconResId :menu item 图标
         */
        public ShareMenuItem(String text, int iconResId) {
            this.text = text;
            this.iconResId = iconResId;
        }
    }

}
