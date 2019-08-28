package com.nahuo.quicksale.adapter;

import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.bean.Level0Item;
import com.nahuo.bean.Level1Item;
import com.nahuo.bean.Level2Item;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.countdownutils.CountDownTimers;
import com.nahuo.quicksale.util.AKUtil;
import com.nahuo.quicksale.util.GlideUtls;
import com.nahuo.quicksale.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

import static com.nahuo.quicksale.common.StringUtils.deleteEndStr;

public class ShopcartNewAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    private static final String TAG = ShopcartNewAdapter.class.getSimpleName();
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_LEVEL_2 = 2;
    private CountDownTask mCountDownTask;
    private ShopCartOnClickListener mListener;

    public void setmListener(ShopCartOnClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 新购物车适配器
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ShopcartNewAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
        addItemType(TYPE_LEVEL_2, R.layout.item_expandable_lv2);
    }

    List<MultiItemEntity> data;

    public void setListData(List<MultiItemEntity> data) {
        this.data = data;
        super.setNewData(data);
    }

    public List<MultiItemEntity> getListData() {
        return this.data;
    }

    public void deleteSelectedItems() {
        if (!ListUtils.isEmpty(data)) {
            for (int i = data.size() - 1; i >= 0; i--) {
                MultiItemEntity entity = data.get(i);
                if (entity instanceof Level2Item) {
                    Level2Item level2Item = (Level2Item) entity;
                    if (level2Item.isAvailable() && level2Item.isSelect) {
                        data.remove(i);
                    }
                } else if (entity instanceof Level1Item) {
                    Level1Item level1Item = (Level1Item) entity;
                    if (level1Item.isAvailable() && level1Item.isSelect) {
                        data.remove(i);
                    }
                } else if (entity instanceof Level0Item) {
                    Level0Item level0Item = (Level0Item) entity;
                    if (level0Item.isAvailable() && level0Item.isSelect) {
                        data.remove(i);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public void setEditItem(int pos, List<Level2Item.ProductsBean> Products, int total) {
        if (!ListUtils.isEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                MultiItemEntity entity = data.get(i);
                if (entity instanceof Level2Item) {
                    Level2Item level2Item = (Level2Item) entity;
                    if (i == pos) {
                        level2Item.setProducts(Products);
                        level2Item.setTotalQty(total);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    public List<Level2Item> getSelectedShopcartItems() {
        List<Level2Item> loseList = new ArrayList<>();
        if (!ListUtils.isEmpty(data)) {
            for (MultiItemEntity entity : data) {
                if (entity instanceof Level2Item) {
                    Level2Item level2Item = (Level2Item) entity;
                    if (level2Item.isAvailable() && level2Item.isSelect) {
                        loseList.add(level2Item);
                    }
                }
            }
        }
        return loseList;
    }

    public boolean hasSelectedIsNotAvailableItems() {
        boolean flag = false;
        List<Level2Item> loseList = new ArrayList<>();
        if (!ListUtils.isEmpty(data)) {
            for (MultiItemEntity entity : data) {
                if (entity instanceof Level2Item) {
                    Level2Item level2Item = (Level2Item) entity;
                    if (!level2Item.isAvailable() && level2Item.isSelect) {
                        loseList.add(level2Item);
                    }
                }
            }
        }
        if (!ListUtils.isEmpty(loseList)) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public void deleteLoseItems() {
        if (!ListUtils.isEmpty(data)) {
            for (int i = data.size() - 1; i >= 0; i--) {
                MultiItemEntity entity = data.get(i);
                if (entity instanceof Level2Item) {
                    Level2Item level2Item = (Level2Item) entity;
                    if (!level2Item.isAvailable()) {
                        data.remove(i);
                    }
                } else if (entity instanceof Level1Item) {
                    Level1Item level1Item = (Level1Item) entity;
                    if (!level1Item.isAvailable()) {
                        data.remove(i);
                    }
                } else if (entity instanceof Level0Item) {
                    Level0Item level0Item = (Level0Item) entity;
                    if (!level0Item.isAvailable()) {
                        data.remove(i);
                    }
                }
                notifyDataSetChanged();
            }
        }
    }

    public List<Level2Item> getLoseShopCart() {
        List<Level2Item> loseList = new ArrayList<>();
        if (!ListUtils.isEmpty(data)) {
            for (MultiItemEntity entity : data) {
                if (entity instanceof Level2Item) {
                    Level2Item level2Item = (Level2Item) entity;
                    if (!level2Item.isAvailable()) {
                        loseList.add(level2Item);
                    }
                }
            }
        }
        return loseList;
    }

    public void setSelectAll(boolean isCheck) {
        if (!ListUtils.isEmpty(data)) {
            for (MultiItemEntity entity : data) {
                if (entity instanceof Level0Item) {
                    Level0Item level0Item = (Level0Item) entity;
                    level0Item.isSelect = isCheck;
                } else if (entity instanceof Level1Item) {
                    Level1Item level1Item = (Level1Item) entity;
                    level1Item.isSelect = isCheck;
                } else if (entity instanceof Level2Item) {
                    Level2Item level2Item = (Level2Item) entity;
                    level2Item.isSelect = isCheck;
                }
            }
            notifyDataSetChanged();
            if (mListener != null)
                mListener.OnChangeClick(true);
        }

    }

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


    @Override
    protected void convert(final BaseViewHolder holder, MultiItemEntity item) {
        switch (holder.getItemViewType()) {
            case TYPE_LEVEL_0:
                final Level0Item level0Item = (Level0Item) item;
                CheckBox title_checkbox = holder.getView(R.id.title_checkbox);
                //   TextView tv_right = holder.getView(R.id.tv_right);
//                if (level0Item.isAvailable()) {
//                    title_checkbox.setVisibility(View.VISIBLE);
//                    tv_right.setText("");
//                } else {
//                    title_checkbox.setVisibility(View.INVISIBLE);
//                    tv_right.setText("失效商品");
//                    title_checkbox.setText("");
//                }
                title_checkbox.setText(level0Item.getWareHouseName());
                if (level0Item.isSelect) {
                    title_checkbox.setChecked(true);
                } else {
                    title_checkbox.setChecked(false);
                }
                title_checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Level1Item> l1List = level0Item.getSubItems();
                        if (!ListUtils.isEmpty(l1List)) {
                            for (Level1Item item : l1List) {
                                item.isSelect = !level0Item.isSelect;
                                List<Level2Item> level2ItemList = item.getSubItems();
                                if (!ListUtils.isEmpty(level2ItemList)) {
                                    for (Level2Item level2Item : level2ItemList) {
                                        level2Item.isSelect = !level0Item.isSelect;
                                    }
                                }
                            }
                            level0Item.isSelect = !level0Item.isSelect;
                            notifyDataSetChanged();
                            if (mListener != null)
                                mListener.OnChangeClick(level0Item.isAvailable());
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final Level1Item level1Item = (Level1Item) item;
                final TextView mTvFHH = holder.getView(R.id.tv_hh);
                final TextView mTvFH = holder.getView(R.id.tv_h);
                final TextView mTvFMM = holder.getView(R.id.tv_mm);
                final TextView mTvFM = holder.getView(R.id.tv_m);
                final TextView mTvFSS = holder.getView(R.id.tv_ss);
                final TextView mTvFS = holder.getView(R.id.tv_s);
                final TextView mTvFF = holder.getView(R.id.tv_f);
                final TextView mTvFDay = holder.getView(R.id.tv_day);
                final TextView tv_over_mess = holder.getView(R.id.tv_over_mess);
                final TextView tv_day_desc = holder.getView(R.id.tv_day_desc);
                final View layout_time_right = holder.getView(R.id.layout_time_right);
                CheckBox checkBox = holder.getView(R.id.title_checkbox);
                View layout_time = holder.getView(R.id.layout_time);
//                if (level1Item.isSelect){
//                    tv_over_mess.setText("选择");
//                }else {
//                    tv_over_mess.setText("剩下");
//                }
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!level1Item.isAvailable()) {
                            List<Level2Item> level2Items = level1Item.getSubItems();
                            if (!ListUtils.isEmpty(level2Items)) {
                                for (Level2Item level2Item : level2Items) {
                                    level2Item.isSelect = !level1Item.isSelect;
                                }
                                level1Item.isSelect = !level1Item.isSelect;
                                notifyDataSetChanged();
                                if (mListener != null)
                                    mListener.OnChangeClick(false);
                            }
                        }
                    }
                });
                if (!level1Item.isAvailable()) {
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setText("失效商品");
                    layout_time.setVisibility(View.GONE);
                    if (level1Item.isSelect) {
                        checkBox.setChecked(true);
                    } else {
                        checkBox.setChecked(false);
                    }
                    //  tv_over_mess.setText("已截单");
                    // holder.getConvertView().setVisibility(View.GONE);
                    layout_time_right.setVisibility(View.GONE);

                } else {
                    layout_time.setVisibility(View.VISIBLE);
                    checkBox.setVisibility(View.GONE);
                    tv_over_mess.setText("距截单");
                    layout_time_right.setVisibility(View.VISIBLE);
                    //holder.getConvertView().setVisibility(View.VISIBLE);
                    long isEndDuration = level1Item.getEndMillis() - System.currentTimeMillis();
                    long pihuoTime = isEndDuration + SystemClock.elapsedRealtime();
                    if (pihuoTime > 0) {
                        if (mCountDownTask != null) {
                            mCountDownTask.until(holder.getConvertView(), pihuoTime,
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
                                                mTvFDay.setVisibility(View.GONE);
                                                tv_day_desc.setVisibility(View.GONE);
                                            } else {
                                                mTvFDay.setVisibility(View.VISIBLE);
                                                tv_day_desc.setVisibility(View.VISIBLE);
                                                mTvFDay.setText((hour / 24) + "");
                                            }
                                            int hour_show = hour % 24;
                                            mTvFHH.setText((hour_show / 10) + "");
                                            mTvFH.setText((hour_show % 10) + "");
                                            mTvFMM.setText((min / 10) + "");
                                            mTvFM.setText((min % 10) + "");
                                            mTvFSS.setText((sec / 10) + "");
                                            mTvFS.setText((sec % 10) + "");
                                            mTvFF.setText(milli / 100 + "");

                                        }

                                        @Override
                                        public void onFinish(View view) {
                                            //拼货结束
                                            tv_over_mess.setText("本场已截单请下拉刷新");
                                            layout_time_right.setVisibility(View.GONE);
                                        }
                                    });
                        }
                    } else {
                        cancelCountDown(holder.getConvertView());
                    }
                }

                break;
            case TYPE_LEVEL_2:
                final Level2Item level2Item = (Level2Item) item;
                holder.setText(R.id.title, level2Item.getName());
                CheckBox checkbox = holder.getView(R.id.checkbox);
                checkbox.setEnabled(false);
                checkbox.setClickable(false);
                View rl_check = holder.getView(R.id.rl_check);
                ImageView icon = holder.getView(R.id.iv_icon);
                TextView price = holder.getView(R.id.price);
                CircleTextView circleTextView = holder.getView(R.id.shopcart_circletext);
                TextView tv_count = holder.getView(R.id.tv_count);
                TextView tv_color_size = holder.getView(R.id.tv_color_size);
                TextView tv_edit = holder.getView(R.id.tv_edit);
                String path = ImageUrlExtends.getImageUrl(level2Item.getCover(), 14);
                GlideUtls.glidePic(mContext, path, icon);
                price.setText("¥" + level2Item.getPrice());
                tv_count.setText("共" + level2Item.getTotalQty() + "件");
                AKUtil.isShowCircleTextView(level2Item.isAvailable(), circleTextView);
                if (!level2Item.isAvailable()) {
                    holder.getConvertView().setAlpha((float) 0.5);
                    holder.getConvertView().setClickable(false);
                    price.setTextColor(mContext.getResources().getColor(R.color.gray));
                    tv_edit.setEnabled(false);
                    tv_edit.setClickable(false);
                    //  rl_check.setVisibility(View.INVISIBLE);
                } else {
                    holder.getConvertView().setAlpha((float) 1.0);
                    holder.getConvertView().setClickable(true);
                    tv_edit.setEnabled(true);
                    tv_edit.setClickable(true);
                    // rl_check.setVisibility(View.VISIBLE);
                    price.setTextColor(mContext.getResources().getColor(R.color.base_red));
                }
                if (level2Item.isShowTopLine) {
                    holder.setVisible(R.id.layout_top, true);
                } else {
                    holder.setVisible(R.id.layout_top, false);
                }
                if (level2Item.isSelect) {
                    checkbox.setChecked(true);
                } else {
                    checkbox.setChecked(false);
                }
                if (!ListUtils.isEmpty(level2Item.getProducts())) {
                    List<String> size_color = new ArrayList<>();
                    StringBuilder builder = new StringBuilder();
                    builder.append("");
                    for (int i = 0; i < level2Item.getProducts().size(); i++) {
                        Level2Item.ProductsBean pm = level2Item.getProducts().get(i);
                        if (pm.getQty() > 0) {
                            if (size_color.contains(pm.getColor())) {
                                builder.append(pm.getSize() + "/" + pm.getQty() + "件，");
                            } else {
                                size_color.add(pm.getColor());
                                if (i > 0) {
                                    String add = StringUtils.deleteEndStr(builder.toString(), "，");
                                    if (!TextUtils.isEmpty(add)) {
                                        String ss = add + "\n";
                                        builder.setLength(0);
                                        builder.append(ss);
                                    }
                                }
                                builder.append(pm.getColor() + "：" + pm.getSize() + "/" + pm.getQty() + "件，");
                            }
                        }

                    }
                    String ss = deleteEndStr(builder.toString(), "，");
                    tv_color_size.setText(ss);
//
                }
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mContext, ItemDetailsActivity.class);
                        it.putExtra(ItemDetailsActivity.EXTRA_ID, level2Item.getAgentItemID());
                        mContext.startActivity(it);
                    }
                });
                rl_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (level2Item.isAvailable()) {
                            int pos = getParPosition(level2Item);
                            Level0Item level0Item1 = (Level0Item) mData.get(pos);
                            List<Level1Item> l1List = level0Item1.getSubItems();
                            if (!ListUtils.isEmpty(l1List)) {
                                boolean isAllCheck = true;
                                level2Item.isSelect = !level2Item.isSelect;
                                for (Level1Item item : l1List) {
                                    List<Level2Item> level2ItemList = item.getSubItems();
                                    if (!ListUtils.isEmpty(level2ItemList)) {
                                        for (Level2Item level2Item : level2ItemList) {
                                            if (!level2Item.isSelect)
                                                isAllCheck = false;
                                        }
                                        if (isAllCheck) {
                                            item.isSelect = true;
                                        } else {
                                            item.isSelect = false;
                                        }
                                    }
                                }
                                if (isAllCheck) {
                                    level0Item1.isSelect = true;
                                } else {
                                    level0Item1.isSelect = false;
                                }
                            }
                        } else {
                            int pos = getSecendParPosition(level2Item);
                            final Level1Item level1Item1 = (Level1Item) mData.get(pos);
                            List<Level2Item> l1List = level1Item1.getSubItems();
                            if (!ListUtils.isEmpty(l1List)) {
                                boolean isAllCheck = true;
                                level2Item.isSelect = !level2Item.isSelect;
                                for (Level2Item item : l1List) {
                                    if (!item.isSelect)
                                        isAllCheck = false;

                                }
                                if (isAllCheck) {
                                    level1Item1.isSelect = true;
                                } else {
                                    level1Item1.isSelect = false;
                                }
                            }
                        }

                        notifyDataSetChanged();
                        if (mListener != null)
                            mListener.OnChangeClick(level2Item.isAvailable());
                    }
                });
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.OnEditClick(level2Item, holder.getAdapterPosition());
                    }
                });
                break;
        }
    }

    public interface ShopCartOnClickListener {
        void OnEditClick(Level2Item level2Item, int pos);

        void OnChangeClick(boolean IsAvailable);
    }
}