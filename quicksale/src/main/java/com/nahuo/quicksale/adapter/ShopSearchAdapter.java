package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShopSearchAdapter extends BaseAdapter implements
        OnClickListener {
    public List<ShopInfoModel> mDataList = new ArrayList<>();
    public Activity mContext;
    private String mFromClass;

    public ShopSearchAdapter(Activity Context) {
        mContext = Context;
    }

    public ShopSearchAdapter(List<ShopInfoModel> dataList,
                             Activity Context) {
        mDataList = dataList;
        mContext = Context;
    }

    public void setData(List<ShopInfoModel> data) {
        this.mDataList = data;
    }

    public  List<ShopInfoModel> getData(){
        return  mDataList;
    }


    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewSearchHolder holder = null;
        View view = convertView;
        if (mDataList.size() > 0) {
            int userID = mDataList.get(position).getUserID();
            String ShopName = mDataList.get(position).getShopName();
            String UserName = mDataList.get(position).getUserName();
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.layout_shop_searchitem, parent, false);
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        ViewSearchHolder holder = (ViewSearchHolder) v.getTag();
//                        int position = holder.position;
//                        ShopInfoModel item = mDataList.get(position);
//                        int userId = item.getUserID();
//                        Intent userInfoIntent = new Intent(mContext, UserInfoActivity.class);
//                        userInfoIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, userId);
//                        mContext.startActivity(userInfoIntent);
//                        if (item.getUserID() == SpManager.getUserId(mContext)) {
//                            mContext.finish();
//                        }
                    }
                });
                holder = new ViewSearchHolder();

                holder.shopName = (TextView) view
                        .findViewById(R.id.layout_shop_search_item_shopname);
                holder.userName = (TextView) view
                        .findViewById(R.id.layout_shop_search_item_username);
                holder.imgcover = (ImageView) view
                        .findViewById(R.id.layout_shop_search_item_shoplogo);
                view.setTag(holder);
            } else {
                holder = (ViewSearchHolder) view.getTag();
            }

            holder.shopName.setText(ShopName);
            holder.userName.setText(UserName);
            holder.position = position;
            Picasso.with(mContext).load(Const.getShopLogo(userID)).placeholder(R.drawable.empty_photo).into(holder.imgcover);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public String getFromClass() {
        return mFromClass;
    }

    public void setFromClass(String fromClass) {
        mFromClass = fromClass;
    }

    public class ViewSearchHolder {
        int position;
        TextView userName;
        TextView shopName;
        ImageView imgcover;
    }

}
