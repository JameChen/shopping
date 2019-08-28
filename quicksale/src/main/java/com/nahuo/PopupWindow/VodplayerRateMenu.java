package com.nahuo.PopupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.CouponModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALAN on 2017/4/14 0014.
 */
public class VodplayerRateMenu extends PopupWindow implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = VodplayerRateMenu.class.getSimpleName();
    private View mRootView;
    private Activity mActivity;
    private ListView mListView;
    private List<Float> list = new ArrayList<>();
    private MAdapter adapter;
    private LinearLayout mListViewBg;

    private View headView;

    private LinearLayout sumLinearlayout;

    //新的被选择的优惠券，旧的优惠券
    private CouponModel select;

    public VodplayerRateMenu(Activity mActivity) {
        super();
        this.mActivity = mActivity;
        if (this.list == null)
            this.list = new ArrayList<>();
        this.list.clear();
        this.list.add(1.0F);
        this.list.add(1.25F);
        this.list.add(1.5F);
        this.list.add(2.0F);
        initViews();
    }

    public CouponModel getSelect() {
        return select;
    }

    public void setSelect(CouponModel select) {
        this.select = select;
    }

    private View mContent;

    private void initViews() {

        mRootView = mActivity.getLayoutInflater().inflate(R.layout.vod_player_rate_pop,
                null);
        mContent = mRootView.findViewById(R.id.content);
        //获取自身的长宽高
        mListView = (ListView) mRootView.findViewById(R.id.lv_rate);
        adapter = new MAdapter(mActivity, list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
//        mRootView.findViewById(R.id.coupon_bottom_menu_confirm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(onCouponClickItem!=null){
//                    onCouponClickItem.onCouponClick(getSelect());
//                    dismiss();
//                }
//            }
//        });
//        mRootView.findViewById(R.id.coupon_cross).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//             dismiss();
//            }
//        });
//        mListViewBg = (LinearLayout) mRootView
//                .findViewById(android.R.id.content);

        // mContent.setOnClickListener(this);
        mListView.setOnItemClickListener(this);

        setOutsideTouchable(false);
        setFocusable(false);
        mRootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
                return false;
            }
        });
        mRootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int top = getmContent().getTop();
                int bottom = getmContent().getBottom();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < top || y > bottom) {
                        // dismiss();
                    }
                }
                return true;
            }
        });
    }


    public View getmContent() {
        return mContent;
    }

//    public VodplayerRateMenu setData(List<CouponModel> data) {
//        this.list = data;
//        if(adapter!=null){
//            adapter.notifyDataSetChanged();
//        }
//        return this;
//    }

    private VodplayerRateMenu create() {

        return this;
    }

    @Override
    public void onClick(View v) {
        //dismiss();
    }


    /**
     * @description 显示菜单栏
     * @created 上午11:22:23
     * @author ALAN
     */
    public void show() {

        create();

//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
        //获取屏幕信息
//        DisplayMetrics dm = new DisplayMetrics();
//        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenWidth = dm.widthPixels;
//        int screenHeigh = dm.heightPixels;

//        location[0]=screenWidth;
//        location[1]=screenHeigh;
        this.setWidth(ScreenUtils.dip2px(mActivity, 60));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
//        mRootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int measuredWidth = mRootView.getMeasuredWidth();
//        int measuredHeight = mRootView.getMeasuredHeight();
//        showAtLocation(view, Gravity.NO_GRAVITY,
//                location[0], location[1] - view.getHeight());
        showAtLocation(mActivity.getWindow().getDecorView(),
                Gravity.BOTTOM | Gravity.RIGHT, DisplayUtil.dip2px(mActivity, 30), DisplayUtil.dip2px(mActivity,50));
//        mListViewBg.setVisibility(View.VISIBLE);
//        mListViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity,
//                com.nahuo.library.R.anim.bottom_menu_appear));


    }

    public int getStatusHeight() {
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        return statusHeight;
    }


    public class MAdapter extends BaseAdapter {

        private LayoutInflater mInflater = null;
        private Context mContext;
        private List<Float> list;

        public MAdapter(Context context, List<Float> list) {
            mContext = context;
            mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.list = list;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_vod_rate,
                        parent, false);
                holder.tv_info = (TextView) convertView.findViewById(R.id.tv_info);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Float item = list.get(position);
            holder.tv_info.setText(item.toString() + "X");
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    changeSelect(item);
//                }
//            });
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
        private TextView tv_info;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Float item = list.get(position);
        if (vodClickItem != null)
            vodClickItem.onRateClick(item);
        dismiss();
    }

    private VodClickItem vodClickItem;

    public void setVodClickItem(VodClickItem vodClickItem) {
        this.vodClickItem = vodClickItem;
    }

    //选择优惠券接口
    public interface VodClickItem {
        void onRateClick(Float rate);
    }

}
