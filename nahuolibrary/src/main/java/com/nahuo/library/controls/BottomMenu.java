package com.nahuo.library.controls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nahuo.library.R;

/**
 * @author JorsonWong
 * @description 底部弹出菜单
 * @created 2015-3-19
 */
public class BottomMenu extends PopupWindow implements OnItemClickListener,
        View.OnClickListener {

    private Activity mActivity;
    private View mRootView;
    private GridView mGridView;
    private OnItemClickListener mMenuItemClickListener;
    private LinearLayout mGridViewBg;
    private String[] mItemTexts;

    private int mSelectedItemPosition = -1;

    public BottomMenu(Activity activity) {
        super();
        this.mActivity = activity;
        initViews();
    }

    public BottomMenu(Activity activity, AttributeSet atrr) {
        super();
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.bottom_menu,
                null);
        mRootView.setOnClickListener(this);
        mGridView = (GridView) mRootView.findViewById(android.R.id.list);
        mGridViewBg = (LinearLayout) mRootView
                .findViewById(android.R.id.content);

        mGridView.setOnItemClickListener(this);

    }

    public BottomMenu setItems(String... items) {
        this.mItemTexts = items;
        return this;
    }

    private BottomMenu create() {
//        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
//        for (int i = 0; i < mItemTexts.length; i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("text", mItemTexts[i]);
//            maps.add(map);
//        }
//        String[] from = new String[]{"text"};
//        int[] to = new int[]{android.R.id.title};
//        SimpleAdapter adapter = new SimpleAdapter(mActivity, maps,
//                R.layout.bottom_menu_item, from, to);
        MAdapter adapter = new MAdapter(mActivity, mItemTexts);

        mGridView.setAdapter(adapter);

        return this;
    }

    public void setSelectedItemPosition(int position) {
        this.mSelectedItemPosition = position;
    }

    /**
     * @description 显示菜单栏
     * @created 2015年3月20日 上午11:22:23
     * @author JorsonWong
     */
    public void show(View view) {

        create();

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(location[1] - getStatusHeight());

        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]
                - getHeight());
        mGridViewBg.setVisibility(View.VISIBLE);
        mGridViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity,
                R.anim.bottom_menu_appear));


    }

    public int getStatusHeight() {
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        return statusHeight;
    }

    /**
     * @description item click callback
     * @created 2015年3月20日 上午11:21:15
     * @author JorsonWong
     */
    public void setOnMenuItemClickListener(OnItemClickListener listener) {
        this.mMenuItemClickListener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
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
            Animation anim = AnimationUtils.loadAnimation(mActivity,
                    R.anim.bottom_menu_disappear);
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


    private final static class ViewHolder {
        private TextView textView;
    }


    public class MAdapter extends ArrayAdapter<String> {

        private LayoutInflater mInflater = null;
        private Context mContext;

        public MAdapter(Context context, String[] items) {
            super(context, 0, items);
            mContext = context;
            mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.bottom_menu_item,
                        parent, false);
                holder.textView = (TextView) convertView.findViewById(android.R.id.title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setBackgroundResource(mSelectedItemPosition == position ? R.color.bottom_menu_item_p : R.drawable.selector_bottom_menu_item);
            final String text = getItem(position);
            holder.textView.setText(text);
            return convertView;
        }
    }

}
