package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.OrderActivityModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @description 订单管理
 * @author pj
 */
public class OrderActivityAdapter extends BaseAdapter {

    public Context mContext;
    public List<OrderActivityModel> mList;

    // 构造函数
    public OrderActivityAdapter(Context Context, List<OrderActivityModel> dataList) {
        mContext = Context;
        mList = dataList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public OrderActivityModel getItem(int arg0) {
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
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.lvitem_order_activity, arg2, false);
                holder = new ViewHolder();

                holder.title = (TextView) view
                        .findViewById(R.id.item_title);
                holder.cover = (ImageView) view
                        .findViewById(R.id.item_cover);
                holder.text1 = (TextView) view
                        .findViewById(R.id.item_text_1);
                holder.text2 = (TextView) view
                        .findViewById(R.id.item_text_2);
                holder.isclose = (TextView) view
                        .findViewById(R.id.item_text_is_close);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            OrderActivityModel item = mList.get(arg0);

            String url = ImageUrlExtends.getImageUrl(Const.getShopLogo(item.getSellerUserID()), 10);
            Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.cover);
            holder.title.setText(item.getName());
            holder.text1.setText("供货商："+item.getSellerUserName());
            holder.text2.setText("拼了"+item.getTotalProdCount()+"件   ¥"+item.getTotalAmount());
            holder.isclose.setText(item.getStatu());
        }

        return view;
    }

    public class ViewHolder {
        public int position;
        ImageView cover;
        TextView title;
        TextView text1;
        TextView text2;
        TextView isclose;
    }


}