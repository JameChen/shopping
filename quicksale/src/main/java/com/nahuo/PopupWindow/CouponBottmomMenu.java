package com.nahuo.PopupWindow;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.CouponModel;
import com.nahuo.quicksale.util.AKUtil;
import java.util.List;
/**
 * Created by ALAN on 2017/4/14 0014.
 */
public class CouponBottmomMenu extends PopupWindow implements AdapterView.OnItemClickListener,View.OnClickListener{
    private static final String TAG=CouponBottmomMenu.class.getSimpleName();
    private View mRootView;
    private Activity mActivity;
    private ListView mListView;
    private List<CouponModel> list;
    private MAdapter adapter;
    private LinearLayout mListViewBg;

    private View headView;

    private LinearLayout sumLinearlayout;

    //新的被选择的优惠券，旧的优惠券
    private CouponModel select;


    public CouponBottmomMenu(Activity mActivity,List<CouponModel> list) {
        super();
        this.mActivity = mActivity;
        this.list=list;
        initViews();
    }

    public CouponModel getSelect() {
        return select;
    }

    public void setSelect(CouponModel select) {
        this.select = select;
    }
    private View mContent;
    private void initViews(){

        mRootView = mActivity.getLayoutInflater().inflate(R.layout.coupon_bottom_menu,
                null);
        mContent=mRootView.findViewById(android.R.id.content);
        mListView=(ListView) mRootView.findViewById(android.R.id.list);
        mRootView.findViewById(R.id.coupon_bottom_menu_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onCouponClickItem!=null){
                    onCouponClickItem.onCouponClick(getSelect());
                    dismiss();
                }
            }
        });
        mRootView.findViewById(R.id.coupon_cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dismiss();
            }
        });
        mListViewBg = (LinearLayout) mRootView
                .findViewById(android.R.id.content);

        mContent.setOnClickListener(this);
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

                int y = (int)event.getY();
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

    public CouponBottmomMenu setData(List<CouponModel> data) {
        this.list = data;
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
        return this;
    }

    private CouponBottmomMenu create() {
        adapter = new MAdapter(mActivity,list);
        mListView.setAdapter(adapter);
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
    public void show(View view) {

        create();

        int[] location = new int[2];
        //获取屏幕信息
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeigh = dm.heightPixels;
        location[0]=screenWidth;
        location[1]=screenHeigh;
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(location[1] - getStatusHeight());
        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        showAtLocation(view, Gravity.NO_GRAVITY, location[0],
                view.getTop());
        mListViewBg.setVisibility(View.VISIBLE);
        mListViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity,
                com.nahuo.library.R.anim.bottom_menu_appear));


    }

    public int getStatusHeight() {
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        return statusHeight;
    }

    //设置默认
    public void setDefault(CouponModel defaultCoupon){
        for(CouponModel coupon:list){
            if(defaultCoupon==null){
                coupon.setSelect(false);
            }else {
                if (coupon.getID() == defaultCoupon.getID()) {
                    coupon.setSelect(true);
                } else {
                    coupon.setSelect(false);
                }
            }
        }
        setSelect(defaultCoupon);
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    public class MAdapter extends BaseAdapter{

        private LayoutInflater mInflater = null;
        private Context mContext;
        private List<CouponModel> list;

        public MAdapter(Context context,List<CouponModel> list) {
            mContext = context;
            mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.list=list;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.layout_popupwindow_coupon,
                        parent, false);
                holder.doller=(TextView) convertView.findViewById(R.id.tv_coupon_doller);
                holder.money = (TextView) convertView.findViewById(R.id.tv_coupon_left1);
                holder.moneyTitle=(TextView) convertView.findViewById(R.id.tv_coupon_left2);
                holder.name = (TextView) convertView.findViewById(R.id.coupon_name);
                holder.content = (TextView) convertView.findViewById(R.id.coupon_content);
                holder.time = (TextView) convertView.findViewById(R.id.coupon_time);
                holder.radioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final CouponModel item=list.get(position);
            String s=item.getDiscount();
            if(!AKUtil.hasDigit(s)){
                holder.doller.setVisibility(View.GONE);
                holder.money.setText(s);
                holder.moneyTitle.setText(item.getCondition());
            }else{
                holder.doller.setVisibility(View.VISIBLE);
                holder.doller.setText(s.substring(0,1));
                holder.money.setText(s.substring(1,s.length()));
                holder.moneyTitle.setText(item.getCondition());
            }
            holder.name.setText(item.getTitle());
            holder.content.setText(item.getLimitSummary());
            holder.time.setText("有效期:"+AKUtil.format(item.getFromTime())+"至"+AKUtil.format(item.getToTime()));
            holder.radioButton.setChecked(item.isSelect);
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeSelect(item);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeSelect(item);
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
        private TextView doller;
        private TextView money;
        private TextView moneyTitle;
        private TextView name;
        private TextView content;
        private TextView time;
        private RadioButton radioButton;
    }

    @Override
    public void dismiss() {
        if (mListViewBg.isShown()) {
            Animation anim = AnimationUtils.loadAnimation(mActivity,
                    com.nahuo.library.R.anim.bottom_menu_disappear);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mListViewBg.clearAnimation();
                    mListViewBg.setVisibility(View.GONE);
                    dismiss();
                }
            }, anim.getDuration());
            mListViewBg.startAnimation(anim);
        } else {
            super.dismiss();
        }
        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         CouponModel item=list.get(position);
         changeSelect(item);
    }

    private void changeSelect(CouponModel selectItem){
        if(select==null){
            for(CouponModel coupon:list){
                if(coupon.getID()==selectItem.getID()){
                    coupon.setSelect(true);
                    setSelect(selectItem);
                }
            }
            adapter.notifyDataSetChanged();
            return;
        }

        if(selectItem.getID()==select.getID()){
            setSelect(null);
            for(CouponModel coupon:list){
                if(coupon.getID()==selectItem.getID()){
                    coupon.setSelect(false);
                }
            }
            adapter.notifyDataSetChanged();
            return;
        }

        for(CouponModel coupon:list){
                if (coupon.getID() == select.getID()) {
                    coupon.setSelect(false);
                }
                if (coupon.getID() == selectItem.getID()) {
                    coupon.setSelect(true);
                }
        }
        select=selectItem;
        adapter.notifyDataSetChanged();
    }

    //选择优惠券接口
    public interface onCouponClickItem{
        void onCouponClick(CouponModel coupon);
    }
    private onCouponClickItem onCouponClickItem;

    public CouponBottmomMenu.onCouponClickItem getOnCouponClickItem() {
        return onCouponClickItem;
    }

    public void setOnCouponClickItem(CouponBottmomMenu.onCouponClickItem onCouponClickItem) {
        this.onCouponClickItem = onCouponClickItem;
    }
}
