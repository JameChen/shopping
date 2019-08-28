package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ColorItemModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class ColorGridItemAdapter extends BaseAdapter {

    private Context                  mContext;
    private List<ColorItemModel>     mList;
    private List<String>             mCheckMap;               // 当前已勾选的颜色，选择颜色时使用
    private OnColorItemClickListener onColorItemClickListener;

    /**
     * 返回临时勾选的项
     * */
    public List<String> getCheckMap() {
        return mCheckMap;
    }

    /**
     * 返回临时勾选的项
     * */
    public List<String> getCheckIDsMap() {
        String checksStr = "";
        List<String> checkIDs = new ArrayList<String>();
        for (String itemStr : mCheckMap) {
            checksStr += "," + itemStr + ",";
        }
        for (ColorItemModel checkItem : mList) {
            if (checksStr.contains("," + checkItem.getColor().getName() + ",")) {
                if (checkItem.getColor().getID() > 0) {
                    checkIDs.add(String.valueOf(checkItem.getColor().getID()));
                }
            }
        }
        return checkIDs;
    }

    public void setOnColorItemClickListener(OnColorItemClickListener listener) {
        onColorItemClickListener = listener;
    }

    @SuppressLint("UseSparseArrays")
    public ColorGridItemAdapter(Context context, List<ColorItemModel> list) {
        mContext = context;
        mList = list;
        mCheckMap = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        mCheckMap.clear();
        for (ColorItemModel colorItem : mList) {
            if (!colorItem.isCheck())
                continue;

            String name = colorItem.getColor().getName();
            if (mCheckMap.contains(name))
                continue;
            mCheckMap.add(name);
        }
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (mList.size() > 0) {
            ViewHolder holder = null;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_pw_color_or_size_items, parent, false);
                holder = new ViewHolder();
                holder.root = view.findViewById(R.id.itemRoot);
                holder.checkBox = (CheckBox)view.findViewById(R.id.specqty_griditem_checkbox);
                holder.tvName = (TextView)view.findViewById(R.id.tv_name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }

            boolean isChecked = false;
            ColorItemModel colorItem = mList.get(position);
            String name = colorItem.getColor().getName();
            // 判断是否选择
            if (mCheckMap.contains(name)) {// colorItem.isCheck() ||
                isChecked = true;
            }

            holder.tvName.setText(name);
            holder.checkBox.setChecked(isChecked);
            // 添加事件
            holder.root.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox ckb = (CheckBox)v.findViewById(R.id.specqty_griditem_checkbox);
                    TextView tvName = (TextView)v.findViewById(R.id.tv_name);
                    String name = tvName.getText().toString().trim();
                    boolean isChecked = ckb.isChecked();
                    Log.i("ColorAdapter", "ckb isChecked:" + isChecked + " name: " + name);
                    ckb.setChecked(!isChecked);

                    if (ckb.isChecked()) {
                        if (!mCheckMap.contains(name))
                            mCheckMap.add(name);
                    } else {
                        if (mCheckMap.contains(name))
                            mCheckMap.remove(name);
                    }
                    if (onColorItemClickListener != null)
                        onColorItemClickListener.onCheckChanged(v, ckb.isChecked());
                }
            });
            // holder.checkBox.setOnClickListener(new OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // CheckBox ckb = (CheckBox)v;
            // String name = ckb.getText().toString().trim();
            // // 判断是否选中，选中时向字典中添加已选中项，否则移除
            // boolean isChecked = ckb.isChecked();
            // if (isChecked) {
            // if (!mCheckMap.contains(name))
            // mCheckMap.add(name);
            // } else {
            // if (mCheckMap.contains(name))
            // mCheckMap.remove(name);
            // }
            // if (onColorItemClickListener != null)
            // onColorItemClickListener.onCheckChanged(v, isChecked);
            // }
            // });
        }
        return view;
    }

    private class ViewHolder {
        View     root;
        CheckBox checkBox;
        TextView tvName;
    }

    public interface OnColorItemClickListener {
        void onCheckChanged(View v, boolean isChecked);
    }
}
