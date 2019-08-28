package com.nahuo.quicksale.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nahuo.library.controls.NoScrollListView;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.ShipItem;
import com.nahuo.quicksale.oldermodel.ShipItem.Product;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShipItemAdapter extends BaseAdapter {

    private Context                        mContext;

    private Map<Integer, ColorSizeAdapter> mMapColorSizeAdapter = new HashMap<Integer, ShipItemAdapter.ColorSizeAdapter>();

    private List<ShipItem>                 mPickingOrders       = new ArrayList<ShipItem>();

    public ShipItemAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mPickingOrders.size();
    }

    @Override
    public ShipItem getItem(int position) {
        return mPickingOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_ship_item, null);
            holder.ivIcon = (ImageView)convertView.findViewById(android.R.id.icon);
            holder.tvName = (TextView)convertView.findViewById(android.R.id.title);
            holder.lvColorSize = (NoScrollListView)convertView.findViewById(android.R.id.list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ShipItem shipItem = getItem(position);
        holder.tvName.setText(shipItem.Name);
        String url = ImageUrlExtends.getImageUrl(shipItem.Cover, Const.LIST_COVER_SIZE);
        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.ivIcon);
        int key = position;
        if (mMapColorSizeAdapter.containsKey(key)) {
            holder.lvColorSize.setAdapter(mMapColorSizeAdapter.get(key));
        } else {
            ColorSizeAdapter adapter = new ColorSizeAdapter(shipItem.Products);
            mMapColorSizeAdapter.put(key, adapter);
            holder.lvColorSize.setAdapter(adapter);
        }

        return convertView;
    }

    private class ColorSizeAdapter extends BaseAdapter {

        private List<Product> mProducts = new ArrayList<ShipItem.Product>();

        public ColorSizeAdapter(List<ShipItem.Product> products) {
            this.mProducts = products;
        }

        @Override
        public int getCount() {
            return mProducts.size();
        }

        @Override
        public Product getItem(int position) {
            return mProducts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.lvitem_ship_item_color_size, null);
                holder.tvColor = (TextView)convertView.findViewById(R.id.tv_color);
                holder.tvSize = (TextView)convertView.findViewById(R.id.tv_size);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Product product = getItem(position);
            holder.tvSize.setText(product.getSize());
            holder.tvColor.setText(product.ColorName);
            return convertView;
        }
    }
    private final static class ViewHolder {
        public ImageView        ivIcon;
        public TextView         tvName;
        public NoScrollListView lvColorSize;

        public TextView         tvColor;
        public TextView         tvSize;

    }

    public void setPickOrders(List<ShipItem> pickingOrders) {
        this.mPickingOrders = pickingOrders;
    }
}
