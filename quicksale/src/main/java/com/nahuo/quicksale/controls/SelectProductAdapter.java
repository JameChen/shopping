package com.nahuo.quicksale.controls;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.customview.CancelDialog;
import com.nahuo.quicksale.oldermodel.ProductModel;

import java.util.List;

public class SelectProductAdapter extends BaseAdapter {
    private static final String TAG = SelectProductAdapter.class.getSimpleName();
    public Context mContext;
    public List<ProductModel> mList;
    private OnChangeQtyListener mListener;

    public SelectProductAdapter(Context Context, List<ProductModel> dataList) {
        mContext = Context;
        mList = dataList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ProductModel getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        final ViewHolder holder;
        View view = arg1;
        if (mList.size() > 0) {
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_select_size_color_detail, arg2, false);

                holder.color = (TextView) view
                        .findViewById(R.id.item_select_size_color_detail_color);
                holder.size = (TextView) view
                        .findViewById(R.id.item_select_size_color_detail_size);
                holder.txtReduce = (TextView) view
                        .findViewById(R.id.item_select_size_color_detail_qty_reduce);
                holder.txtQty = (TextView) view
                        .findViewById(R.id.item_select_size_color_detail_qty);
                holder.txtAdd = (TextView) view
                        .findViewById(R.id.item_select_size_color_detail_qty_add);
                holder.item_select_size_des = (TextView) view.findViewById(R.id.item_select_size_des);
                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }
            ProductModel item = mList.get(arg0);

            holder.color.setText(item.getColor());
            holder.size.setText(item.getSize());
            holder.txtQty.setText(item.getStock() + "");
            if (item.getMaxStock()<5){
                holder.item_select_size_des.setVisibility(View.VISIBLE);
                holder.item_select_size_des.setText("(仅剩"+item.getMaxStock() + "件)");
            }else {
                holder.item_select_size_des.setVisibility(View.GONE);
            }
            if (holder.txtReduce != null) {
                holder.txtReduce.setTag(arg0);
                holder.txtReduce.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView cb = (TextView) v;
                        int selectPosition = (int) cb.getTag();
                        ProductModel selectItem = mList.get(selectPosition);
                        qtyWork(true, selectItem);
                    }
                });
                holder.txtAdd.setTag(arg0);
                holder.txtAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView cb = (TextView) v;
                        int selectPosition = (int) cb.getTag();
                        ProductModel selectItem = mList.get(selectPosition);
                        if (Integer.parseInt(holder.txtQty.getText().toString().trim()) + 1 <= selectItem.getMaxStock()) {
                            qtyWork(false, selectItem);
                        } else {
                            Log.v(TAG, "--库存不足");
                            new CancelDialog(mContext, "库存不足").show();
                        }
                    }
                });
                if (item.getStock() > 0) {
                    holder.txtReduce.setTextColor(mContext.getResources().getColor(R.color.base_red));
                } else {
                    holder.txtReduce.setTextColor(mContext.getResources().getColor(R.color.click_color));
                }
            }
        }

        return view;
    }

    private void qtyWork(boolean reduce, ProductModel selectItem) {
        //reduce false+1
        int stock = selectItem.getStock();
        if (reduce) {
            stock = stock - 1;
            selectItem.setStock(stock);
        } else {
            stock = stock + 1;
            selectItem.setStock(stock);
        }
        if (mListener != null) {
            mListener.OnQtyChange(reduce, stock);
        }

        notifyDataSetChanged();
    }

    public class ViewHolder {
        public int position;
        TextView color;
        TextView size;
        TextView txtReduce;
        TextView txtQty;
        TextView txtAdd, item_select_size_des;
    }

    public static interface OnChangeQtyListener {
        public void OnQtyChange(boolean reduce, int stock);
    }

    public void setListener(OnChangeQtyListener listener) {
        mListener = listener;
    }
}
