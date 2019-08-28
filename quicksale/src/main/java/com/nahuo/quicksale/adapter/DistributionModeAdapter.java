package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.ShipSettingModel;

/**
 * Created by jame on 2017/12/29.
 */

public class DistributionModeAdapter extends MyBaseAdapter<ShipSettingModel> {
    public DistributionModeAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_distribution, parent, false);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.name);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.tv_description = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShipSettingModel bean = mdata.get(position);
        if (bean != null) {
            holder.tv_name.setText(bean.getName());
            holder.tv_description.setText(bean.getDescription());
            if (bean.isDefault()) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShipSettingModel bean = mdata.get(position);
                    setSindleCheck(bean);
                }
            });

        }

        return convertView;
    }

    public ShipSettingModel getCheck() {
        ShipSettingModel bean = null;
        if (!ListUtils.isEmpty(mdata)) {
            for (ShipSettingModel xbean : mdata) {
                if (xbean.isDefault()) {
                    bean=xbean;
                    break;
                }
            }
        }
        return bean;
    }

    public void setSindleCheck(ShipSettingModel bean) {
        if (!ListUtils.isEmpty(mdata)) {
            for (ShipSettingModel xbean : mdata) {
                if (xbean.getID() == bean.getID()) {
                    xbean.setIsDefault(true);
                } else {
                    xbean.setIsDefault(false);
                }
            }
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        TextView tv_name, tv_description;
        CheckBox checkbox;
    }
}
