package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.controls.NumPlusMinusDialog;
import com.nahuo.quicksale.controls.NumPlusMinusDialog.NumPlusMinusDialogListener;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.countdownutils.CountDownTimers;
import com.nahuo.quicksale.oldermodel.ShopCart.ShopcartItem;
import com.nahuo.quicksale.oldermodel.ShopCart.ShopcartShop;
import com.nahuo.quicksale.util.AKUtil;
import com.nahuo.quicksale.util.ObjectUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShopcartAdapter extends BaseExpandableListAdapter {

    private Activity mContext;
    private LayoutInflater mLayoutInflater;
    private List<ShopcartShop> mShopCarts = new ArrayList<ShopcartShop>();
    private TotalPriceChangedListener mTotalPriceChangedListener;
    private IGoodItemOnClickListener mIGoodItemOnClickListener;
    private CountDownTask mCountDownTask;
    private static final String TAG = ShopcartAdapter.class.getSimpleName();

    public void setCountDownTask(CountDownTask countDownTask) {
        if (!ObjectUtils.equals(mCountDownTask, countDownTask)) {
            mCountDownTask = countDownTask;
            notifyDataSetChanged();
        }
    }

    /**
     * 取消定时
     *
     * @author James Chen
     * @create time in 2017/3/27 16:36
     */
    private void cancelCountDown(View convertView) {
        if (mCountDownTask != null) {
            mCountDownTask.cancel(convertView);
        }
    }

    /**
     * @author James Chen
     * 计时器
     * @create time in 2017/3/27 16:30
     */
    private void startCountDown(final ViewHolder holder, final ShopcartShop item, View convertView) {
        // long isStartDuration = item.getStartMillis() - System.currentTimeMillis();
//        long isEndDuration = item.getEndMillis() - System.currentTimeMillis();
//        long pihuoTime = isEndDuration + SystemClock.elapsedRealtime();
        long pihuoTime =item.getEndMillis() -item.getStartMillis();
        if (pihuoTime > 0) {
            holder.lladd.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.GONE);
            if (mCountDownTask != null) {
                mCountDownTask.until(convertView, pihuoTime,
                        100, new CountDownTimers.OnCountDownListener() {
                            @Override
                            public void onTick(View view, long millisUntilFinished) {
                                //d拼货计时开始
                                long isEndDuration = millisUntilFinished;
                                int hour = (int) (isEndDuration / TimeUtils.HOUR_MILLIS);
                                int min = (int) ((isEndDuration - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
                                int sec = (int) ((isEndDuration - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
                                int milli = (int) (isEndDuration - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS - sec * TimeUtils.SECOND_MILLIS);
                                if (hour < 24) {//小于一天
                                    holder.mTvFDay.setVisibility(View.GONE);
                                    holder.mTvFDayDesc.setVisibility(View.GONE);
                                } else {
                                    holder.mTvFDay.setVisibility(View.VISIBLE);
                                    holder.mTvFDayDesc.setVisibility(View.VISIBLE);
                                    holder.mTvFDay.setText((hour / 24) + "");
                                }
                                int hour_show = hour % 24;
                                holder.mTvFHH.setText((hour_show / 10) + "");
                                holder.mTvFH.setText((hour_show % 10) + "");
                                holder.mTvFMM.setText((min / 10) + "");
                                holder.mTvFM.setText((min % 10) + "");
                                holder.mTvFSS.setText((sec / 10) + "");
                                holder.mTvFS.setText((sec % 10) + "");
                                holder.mTvFF.setText(milli / 100 + "");
                            }

                            @Override
                            public void onFinish(View view) {
                                //拼货结束
//                                holder.ll_top1.setVisibility(View.GONE);
//                                holder.lladd.setVisibility(View.GONE);
                                setOverData(item.getEndMillis());
                                // holder.mTvfOveredTips.setVisibility(View.VISIBLE);
                                //  holder.mTvfOveredTips.setText(TimeUtils.millisToTimestamp(item.getEndMillis(), "MM月dd日 HH:mm:ss") + " 拼货结束");

                            }
                        });
            }

        } else {
            holder.lladd.setVisibility(View.GONE);
            holder.line.setVisibility(View.VISIBLE);
        }


    }

    public ShopcartAdapter(Activity context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);

    }

    View invalid_shop_clear;

    public void setTextView(View invalid_shop_clear) {
        this.invalid_shop_clear = invalid_shop_clear;
    }

    public void setShopCarts(List<ShopcartShop> shopCarts) {
        this.mShopCarts = shopCarts;
    }

    public List<ShopcartShop> getShopCarts() {
        return mShopCarts;
    }

    @Override
    public int getGroupCount() {
        return mShopCarts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mShopCarts.get(groupPosition).getShopcartItems().size();
    }

    @Override
    public ShopcartShop getGroup(int groupPosition) {
        return mShopCarts.get(groupPosition);
    }

    @Override
    public ShopcartItem getChild(int groupPosition, int childPosition) {
        return mShopCarts.get(groupPosition).getShopcartItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 1000 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_shopcart_order_group_item, null);
            holder.lladd = convertView.findViewById(R.id.lladd);
            holder.line = convertView.findViewById(R.id.line);
            holder.ll_top1 = convertView.findViewById(R.id.ll_top1);
            holder.mTvFHH = (TextView) convertView.findViewById(R.id.tv_hh);
            holder.mTvFH = (TextView) convertView.findViewById(R.id.tv_h);
            holder.mTvFMM = (TextView) convertView.findViewById(R.id.tv_mm);
            holder.mTvFM = (TextView) convertView.findViewById(R.id.tv_m);
            holder.mTvFSS = (TextView) convertView.findViewById(R.id.tv_ss);
            holder.mTvFS = (TextView) convertView.findViewById(R.id.tv_s);
            holder.mTvFF = (TextView) convertView.findViewById(R.id.tv_f);
            holder.mTvFDay = (TextView) convertView.findViewById(R.id.tv_day);
            holder.mTvFDayDesc = (TextView) convertView.findViewById(R.id.tv_day_desc);
            holder.mTvfOveredTips = (TextView) convertView.findViewById(R.id.tv_overed_tips);
            holder.checkBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
            holder.icon = (ImageView) convertView.findViewById(android.R.id.icon);
            holder.tvTitle = (TextView) convertView.findViewById(android.R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopcartShop group = getGroup(groupPosition);

        holder.tvTitle.setText(group.Name);
        //long isEndDuration = group.getEndMillis() - System.currentTimeMillis();
        //long pihuoTime = isEndDuration + SystemClock.elapsedRealtime();
        long pihuoTime =group.getEndMillis() -group.getStartMillis();
//        Log.e("yu","isEndDuration==="+isEndDuration);
//        Log.e("yu","pihuoTime==="+pihuoTime);
//        Log.e("yu","groupPosition==="+groupPosition);
        if (groupPosition == 0) {
            if (pihuoTime > 0 && group.isStart()) {
                startCountDown(holder, group, convertView);
            } else {
                holder.lladd.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
                cancelCountDown(convertView);
            }

        } else {
            long my_time = mShopCarts.get(groupPosition).getEndMillis();
            long last_time = mShopCarts.get(groupPosition - 1).getEndMillis();
            if (my_time != last_time) {
                if (pihuoTime > 0 && group.isStart()) {
                    startCountDown(holder, group, convertView);
                } else {
                    holder.lladd.setVisibility(View.GONE);
                    holder.line.setVisibility(View.VISIBLE);
                    cancelCountDown(convertView);
                }
            } else {
                holder.lladd.setVisibility(View.GONE);
                holder.line.setVisibility(View.VISIBLE);
                cancelCountDown(convertView);
            }
        }

        final List<ShopcartItem> itmes = group.getShopcartItems();

        boolean checked = true;
        for (ShopcartItem item : itmes) {
            if (!item.isSelect) {
                checked = false;
                break;
            }
        }
        holder.checkBox.setChecked(checked);
        holder.checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                for (ShopcartItem item : itmes) {
                    item.isSelect = cb.isChecked();
                }
                notifyDataSetChanged();
                if (mTotalPriceChangedListener != null) {
                    mTotalPriceChangedListener.totalPriceChanged(getTotalPrice());
                }
            }
        });
        holder.icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent it = new Intent(mContext, UserInfoActivity.class);
//                it.putExtra(UserInfoActivity.EXTRA_USER_ID, group.UserID);
//                mContext.startActivity(it);
            }
        });
        holder.tvTitle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                Intent it = new Intent(mContext, UserInfoActivity.class);
//                it.putExtra(UserInfoActivity.EXTRA_USER_ID, group.UserID);
//                mContext.startActivity(it);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_shopcart_child_item, null);
            holder.checkBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
            holder.icon = (ImageView) convertView.findViewById(android.R.id.icon);
            holder.tvTitle = (TextView) convertView.findViewById(android.R.id.title);
            holder.tvColorSize = (TextView) convertView.findViewById(android.R.id.text1);
            holder.tvPrice = (TextView) convertView.findViewById(android.R.id.text2);
            holder.etCount = (EditText) convertView.findViewById(android.R.id.edit);
            holder.circleTextView = (CircleTextView) convertView.findViewById(R.id.shopcart_circletext);
            holder.subImg = (Button) convertView.findViewById(R.id.img_shopcart_sub);
            holder.addImg = (Button) convertView.findViewById(R.id.img_shopcart_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ShopcartItem child = getChild(groupPosition, childPosition);
        holder.tvTitle.setText(child.Name);
        holder.tvColorSize.setText(child.Color + "/" + child.Size);
        holder.etCount.setText(child.Qty + "");
        if (!child.IsAvailable) {
            convertView.setAlpha((float) 0.5);
            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.gray));
        } else {
            convertView.setAlpha((float) 1.0);
            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.base_red));
        }
        String imageUrl = child.Image;
        imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_ITEM_SIZE);
        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon);

        Log.v(TAG, child.IsAvailable + "");
        AKUtil.isShowCircleTextView(child.IsAvailable, holder.circleTextView);

        holder.tvPrice.setText("¥" + Utils.moneyFormat(child.Price));
        if (child.IsAvailable) {
            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        holder.checkBox.setChecked(child.isSelect);
        holder.checkBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                child.isSelect = cb.isChecked();
                notifyDataSetChanged();
                if (mTotalPriceChangedListener != null) {
                    mTotalPriceChangedListener.totalPriceChanged(getTotalPrice());
                }
            }
        });
        //加减（编辑框）添加监听
        setDialogFragment(holder.etCount, child, groupPosition, childPosition);
        setDialogFragment(holder.subImg, child, groupPosition, childPosition);
        setDialogFragment(holder.addImg, child, groupPosition, childPosition);
        holder.icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIGoodItemOnClickListener != null) {
                    mIGoodItemOnClickListener.goodOnClick(child);
                }
            }
        });
        holder.tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIGoodItemOnClickListener != null) {
                    mIGoodItemOnClickListener.goodOnClick(child);
                }
            }
        });

        return convertView;
    }

    private void setDialogFragment(View v, final ShopcartItem child, final int groupPosition, final int childPosition) {
        if (v instanceof Button) {
            v = (Button) v;
        } else if (v instanceof EditText) {
            v = (EditText) v;
        }
        v.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NumPlusMinusDialog dialog = NumPlusMinusDialog.newInstance(child.Qty);
                        dialog.setNumPlusMinusDialogListener(new NumPlusMinusDialogListener() {
                            @Override
                            public void numChanged(int num) {
                                int prefCount = child.Qty;
                                child.Qty = num;
                                notifyDataSetChanged();
                                if (mTotalPriceChangedListener != null) {
                                    mTotalPriceChangedListener.totalPriceChanged(getTotalPrice());
                                    mTotalPriceChangedListener.shopcartItemCountChanged(groupPosition, childPosition, child.Qty, prefCount);
                                }
                            }
                        });
                        dialog.show(mContext.getFragmentManager(), "numDialog");
                    }
                }
        );
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setSelectAll(boolean isChecked) {
        try {
            for (int i = 0; i < getGroupCount(); i++) {
                ShopcartShop group = getGroup(i);
                List<ShopcartItem> items = group.getShopcartItems();
                if (items != null && !items.isEmpty())
                    for (ShopcartItem item : items) {
                        item.isSelect = isChecked;
                    }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mTotalPriceChangedListener != null) {
            mTotalPriceChangedListener.totalPriceChanged(getTotalPrice());
        }
    }

    public void setTotalPriceChangedListener(TotalPriceChangedListener listener) {
        this.mTotalPriceChangedListener = listener;
    }

    public List<ShopcartItem> getSelectedShopcartItems() {
        List<ShopcartItem> its = new ArrayList<ShopcartItem>();
        try {
            for (int i = 0; i < getGroupCount(); i++) {
                ShopcartShop group = getGroup(i);
                List<ShopcartItem> items = group.getShopcartItems();
                if (items != null && !items.isEmpty())
                    for (ShopcartItem item : items) {
                        if (item.isSelect) {
                            its.add(item);
                        }
                    }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return its;
    }

    //倒计时结束数据整理
    public void setOverData(long end_time) {
        try {
            if (!ListUtils.isEmpty(mShopCarts)) {
                List<ShopcartShop> newList = new ArrayList<>();
                for (int i = 0; i < mShopCarts.size(); i++) {
                    if (mShopCarts.get(i).getEndMillis() == end_time) {
                        ShopcartShop bean = mShopCarts.get(i);
                        bean.setStart(false);
                        if (!ListUtils.isEmpty(bean.getShopcartItems())) {
                            for (ShopcartItem child : bean.getShopcartItems()) {
                                child.IsAvailable = false;
                            }
                        }
                        newList.add(bean);
                        mShopCarts.remove(i);
                    }
                }
                mShopCarts.addAll(newList);
                notifyDataSetChanged();
                if (mTotalPriceChangedListener != null) {
                    mTotalPriceChangedListener.totalPriceChanged(getTotalPrice());
                }
                if (invalid_shop_clear!=null)
                    invalid_shop_clear.setVisibility(View.VISIBLE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<ShopcartItem> getLoseShopCart() {
        List<ShopcartItem> its = new ArrayList<ShopcartItem>();
        try {
            for (int i = 0; i < getGroupCount(); i++) {
                ShopcartShop group = getGroup(i);
                List<ShopcartItem> items = group.getShopcartItems();
                if (items != null && !items.isEmpty())
                    for (ShopcartItem item : items) {
                        if (!item.IsAvailable) {
                            its.add(item);
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return its;
    }

    public void deleteSelectedItems() {
        try {
            for (int i = getGroupCount() - 1; i >= 0; i--) {
                ShopcartShop group = getGroup(i);
                List<ShopcartItem> items = group.getShopcartItems();
                if (items != null && !items.isEmpty()) {
                    for (int j = items.size() - 1; j >= 0; j--) {
                        ShopcartItem item = items.get(j);
                        if (item.isSelect) {
                            items.remove(item);
                        }
                    }
                }
                if (group.getShopcartItems().size() <= 0) {
                    mShopCarts.remove(i);
                }
            }
            notifyDataSetChanged();
            if (mTotalPriceChangedListener != null) {
                mTotalPriceChangedListener.totalPriceChanged(getTotalPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //删除失效商品

    public void deleteLoseItems() {
        try {
            for (int i = getGroupCount() - 1; i >= 0; i--) {
                ShopcartShop group = getGroup(i);
                List<ShopcartItem> items = group.getShopcartItems();
                if (items != null && !items.isEmpty()) {
                    for (int j = items.size() - 1; j >= 0; j--) {
                        ShopcartItem item = items.get(j);
                        if (!item.IsAvailable) {
                            items.remove(item);
                        }
                    }
                }
                if (group.getShopcartItems().size() <= 0) {
                    mShopCarts.remove(i);
                }
            }
            notifyDataSetChanged();
            if (mTotalPriceChangedListener != null) {
                mTotalPriceChangedListener.totalPriceChanged(getTotalPrice());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public double getTotalPrice() {
        double price = 0;
        try {
            for (int i = 0; i < getGroupCount(); i++) {
                ShopcartShop group = getGroup(i);
                List<ShopcartItem> items = group.getShopcartItems();
                if (items != null && !items.isEmpty())
                    for (ShopcartItem item : items) {
                        if (item.isSelect && item.IsAvailable) {
                            price = price + (item.Price * item.Qty);
                        }
                    }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }


    public boolean isSelectAll() {
        boolean isSelAll = true;
        for (int i = 0; i < getGroupCount(); i++) {
            ShopcartShop group = getGroup(i);
            List<ShopcartItem> items = group.getShopcartItems();
            if (items != null && !items.isEmpty())
                for (ShopcartItem item : items) {
                    if (!item.isSelect) {
                        isSelAll = false;
                        break;
                    }
                }

        }
        return isSelAll;
    }

    public void setIGoodItemOnClickListener(IGoodItemOnClickListener listener) {
        this.mIGoodItemOnClickListener = listener;
    }

    private final static class ViewHolder {
        private View lladd, ll_top1, line;
        private TextView mTvFHH, mTvFH, mTvFMM, mTvFM, mTvFSS, mTvFS, mTvFF, mTvFDay, mTvFDayDesc, mTvfOveredTips;
        public CheckBox checkBox;
        public ImageView icon;
        public TextView tvTitle;
        public TextView tvColorSize;
        public EditText etCount;
        public TextView tvPrice;

        public Button subImg;
        public Button addImg;
        //圆形
        public CircleTextView circleTextView;
    }

    public interface TotalPriceChangedListener {
        public void totalPriceChanged(double totalPrice);

        public void shopcartItemCountChanged(int groupPs, int childPs, int num, int preCount);
    }

    public interface IGoodItemOnClickListener {
        public void goodOnClick(ShopcartItem item);
    }

}