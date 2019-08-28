package com.nahuo.PopupWindow;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nahuo.bean.StallsAllListBean;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;

import java.util.List;

import static com.nahuo.quicksale.R.id.listview;

/**
 * Created by jame on 2017/6/2.
 */

public class MarketMenu extends PopupWindow {
    Activity mContext;
    View rootView;
    ListView lv;
    private static MarketMenu menu = null;
    List<StallsAllListBean.MarketListBean> data;
    MarketMenu.OnMarketMenuClickItem mListener;
    MAdapter mAdapter;
    int maxWidth = 0;
    private float alpha = 0.75f;
    public static MarketMenu getInstance(Activity context, List<StallsAllListBean.MarketListBean> data) {
        if (menu == null) {
            synchronized (MarketMenu.class) {
                if (menu == null) {
                    menu = new MarketMenu(context, data);
                }
            }
        }
        return menu;
    }

    MarketMenu(Activity mContext, List<StallsAllListBean.MarketListBean> data) {
        this.mContext = mContext;
        this.data = data;
        initViews();
    }

    /**
     * @description 显示菜单栏
     */
    public void show() {

        int[] location = new int[2];
        //获取屏幕信息
        int screenWidth = DisplayUtil.getScreenWidth();
        int screenHeigh = DisplayUtil.getScreenHeight();
        location[0] = screenWidth;
        location[1] = screenHeigh;
        //lv.getMeasuredWidth()
        this.setWidth(maxWidth);
        //this.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setContentView(rootView);
        this.setFocusable(true);
        //0xb0000000
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(R.color.white));
        this.setBackgroundDrawable(dw);
        int x = DisplayUtil.dip2px(mContext, 10);
        int y = DisplayUtil.dip2px(mContext, 54)
                + getStatusHeight();
        showAtLocation(mContext.getWindow().getDecorView(),
                Gravity.LEFT | Gravity.TOP, 0, y);
  /*      showAtLocation(view, Gravity.NO_GRAVITY, location[0],
                view.getTop());*/
        //setBackgroundAlpha(1f, alpha, 240);
        lv.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.top_menu_appear));

    }
    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                mContext.getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    public int getStatusHeight() {
//        Rect rectangle= new Rect();
//        Window window= mContext.getWindow();
//        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
//        int statusBarHeight= rectangle.top;
        Resources resources = mContext.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen",
                "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
        // return statusBarHeight;
    }

    private void initViews() {
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.item_market_menu, null);
        lv = (ListView) rootView.findViewById(listview);
        mAdapter = new MAdapter(mContext, data);
        lv.setAdapter(mAdapter);
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                View view = lv.getAdapter().getView(i, null, lv);
                view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                if (view.getMeasuredWidth() > maxWidth) {
                    maxWidth = view.getMeasuredWidth();
                }
            }
        }
       // lv.getLayoutParams().width=maxWidth;
       // lv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }

    @Override
    public void dismiss() {
//        if (lv.isShown()) {
//            Animation anim = AnimationUtils.loadAnimation(mContext,
//                    R.anim.top_menu_disappear);
//            new Handler().postDelayed(new Runnable() {
//                public void run() {
//                    lv.clearAnimation();
//                    lv.setVisibility(View.GONE);
//                    dismiss();
//                }
//            }, anim.getDuration());
//            lv.startAnimation(anim);
//        } else {
//            super.dismiss();
//        }
        super.dismiss();
        menu = null;
    }

    public class MAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;
        private Context mContext;
        private List<StallsAllListBean.MarketListBean> list;

        public MAdapter(Context context, List<StallsAllListBean.MarketListBean> list) {
            mContext = context;
            mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.list = list;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_market_list_menu,
                        parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final StallsAllListBean.MarketListBean item = list.get(position);
            if (!TextUtils.isEmpty(item.getName()))
                holder.tv_name.setText(item.getName());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMarketMenuClick(item);
                    if (menu != null) {
                        menu.dismiss();
                        menu = null;
                    }
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    private final static class ViewHolder {
        private TextView tv_name;

    }

    public interface OnMarketMenuClickItem {
        void onMarketMenuClick(StallsAllListBean.MarketListBean stallsBean);
    }

    public void setListener(MarketMenu.OnMarketMenuClickItem listener) {
        mListener = listener;
    }
}
