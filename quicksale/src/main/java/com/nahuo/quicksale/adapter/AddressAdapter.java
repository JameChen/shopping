package com.nahuo.quicksale.adapter;

import java.util.Iterator;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.Address;

/**
 * @description 地址adapter
 * @created 2014-8-27 上午11:48:26
 * @author ZZB
 */

public class AddressAdapter extends MyBaseAdapter<Address> {
    private int selectedPos = -1;

    public AddressAdapter(Context context) {
        super(context);
    }

    public Address getDefault() {
        for (Address add : mdata) {
            if (add.isDefault()) {
                return add;
            }
        }
        return null;
    }

    /**
     * @description 添加地址
     * @created 2014-8-29 下午2:53:58
     * @author ZZB
     */
    public void add(Address add) {
        mdata.add(add);
        notifyDataSetChanged();
    }

    /**
     * @description 设置默认显示
     * @created 2014-8-29 下午2:39:31
     * @author ZZB
     */
    public void setDefault(Address address) {

        for (Address add : mdata) {
            add.setDefault(add.getId() == address.getId() ? true : false);
        }
        notifyDataSetChanged();

    }

    /**
     * @description 移除地址
     * @created 2014-8-29 下午12:04:42
     * @author ZZB
     */
    public void remove(int id) {
        if (mdata == null) {
            return;
        }
        Iterator<Address> it = mdata.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                it.remove();
                selectedPos = -1;
                notifyDataSetChanged();
                return;
            }
        }
    }

    /**
     * @description 更新地址
     * @created 2014-8-29 下午2:38:39
     * @author ZZB
     */
    public void update(Address address) {
        Iterator<Address> it = mdata.iterator();
        boolean found = false;
        int counter = 0;
        while (it.hasNext()) {
            if (it.next().getId() == address.getId()) {
                found = true;
                it.remove();
                break;
            }
            counter++;
        }
        if (found) {
            mdata.add(counter, address);
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Address item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_address, parent, false);
            holder = new ViewHolder();
            holder.tvProvince = (TextView)convertView.findViewById(R.id.tv_province_city);
            holder.tvNamePost = (TextView)convertView.findViewById(R.id.tv_name_postcode);
            holder.tvDetail = (TextView)convertView.findViewById(R.id.tv_detail_address);
            holder.tvPhone = (TextView)convertView.findViewById(R.id.tv_phone);
            holder.tvDefault = (TextView)convertView.findViewById(R.id.tv_default);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        convertView.setBackgroundResource(selectedPos == position ? R.drawable.bg_rectangle_red : R.color.transparent);
        holder.tvDefault.setVisibility(item.isDefault() ? View.VISIBLE : View.GONE);
        holder.tvProvince.setText(String.format(Locale.CHINA, "%s %s %s", item.getProvince() == null ? "" : item
                .getProvince().getName(), item.getCity().getName(), item.getArea().getName()));
        holder.tvDetail.setText(item.getDetailAddress());
        // holder.tvNamePost.setText(String.format(Locale.CHINA, "%s %s", item.getUserName(),
        // item.getPostCode()));
        holder.tvNamePost.setText(String.format(Locale.CHINA, "%s", item.getUserName()));
        holder.tvPhone.setText(item.getPhone());
        return convertView;
    }

    /**
     * 存放列表项控件句柄
     */
    private static class ViewHolder {
        public TextView tvProvince;
        public TextView tvDetail;
        public TextView tvNamePost;
        public TextView tvPhone;
        public TextView tvDefault;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

}
