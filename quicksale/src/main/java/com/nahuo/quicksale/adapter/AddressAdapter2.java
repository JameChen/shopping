package com.nahuo.quicksale.adapter;

import java.util.Iterator;
import java.util.Locale;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.Address;

/**
 * @description
 * @created 2015年4月8日 下午3:28:23
 * @author JorsonWong
 */
public class AddressAdapter2 extends MyBaseAdapter<Address> {

    private int                    selectedPos = -1;
    private IAddressEdtDelListener mAddressEdtDelListener;

    public AddressAdapter2(Context context) {
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

    public void add(Address add) {
        mdata.add(add);
        notifyDataSetChanged();
    }

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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Address item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_activity_address2_list_item, parent, false);
            holder = new ViewHolder();

            holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            holder.tvPhone = (TextView)convertView.findViewById(R.id.tvPhone);

            holder.tvAddress = (TextView)convertView.findViewById(R.id.tvAddress);
            holder.tvAddressDetail = (TextView)convertView.findViewById(R.id.tvAddressDetail);
            holder.tvDefaultSign=(TextView) convertView.findViewById(R.id.tvDefaultSign);

            holder.imgDel = (ImageView)convertView.findViewById(android.R.id.icon2);
            holder.imgEdt = (ImageView)convertView.findViewById(android.R.id.icon1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        convertView.setBackgroundResource(selectedPos == position ? R.drawable.bg_rectangle_red : R.color.transparent);
        // holder.tvDefault.setVisibility(item.isDefault() ? View.VISIBLE : View.GONE);
        String provinve = item.getProvince() == null ? "" : item.getProvince().getName();
        String city = item.getCity() == null ? "" : item.getCity().getName();
        String area = item.getArea() == null ? "" : item.getArea().getName();
        String detail = item.getDetailAddress() == null ? "" : item.getDetailAddress();

        holder.tvName.setText(item.getUserName());
        holder.tvPhone.setText(item.getPhone());
        holder.tvAddress.setText(String.format(Locale.CHINA, "%s %s %s", provinve, city, area));
        holder.tvAddressDetail.setText(detail);

        if(item.isDefault()){
            holder.tvDefaultSign.setVisibility(View.VISIBLE);
        }else{
            holder.tvDefaultSign.setVisibility(View.GONE);
        }

        holder.imgEdt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressEdtDelListener != null) {
                    mAddressEdtDelListener.edit(position, item);
                }
            }
        });
        holder.imgDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddressEdtDelListener != null) {
                    mAddressEdtDelListener.delete(position, item);
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        public TextView  tvAddressDetail;
        public TextView  tvPhone;
        public TextView  tvName;
        public TextView  tvAddress;
        public TextView  tvDefaultSign;
        public ImageView imgDel;
        public ImageView imgEdt;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setAddressEdtDelListener(IAddressEdtDelListener listener) {
        mAddressEdtDelListener = listener;
    }

    public interface IAddressEdtDelListener {

        public void edit(int position, Address address);

        public void delete(int position, Address address);

    }

}
