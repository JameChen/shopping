package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.squareup.picasso.Picasso;

/**
 * Created by jame on 2017/9/15.
 */

public class RelatedGoodsAdapter extends MyBaseAdapter<ShopItemModel.RelatedGoodsBean> {
Activity activity;
    public RelatedGoodsAdapter(Activity context) {
        super(context);
        activity=context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final ShopItemModel.RelatedGoodsBean item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_related_goods, parent, false);

            holder = new ViewHolder();
            holder.rl_iv=(RelativeLayout)convertView.findViewById(R.id.rl_iv);
            holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
            holder.tv_price = (TextView)convertView.findViewById(R.id.tv_price);

            holder.iv_cover = (ImageView)convertView.findViewById(R.id.iv_cover);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        //int size = mdata.size();
//        int length = 150;
//        DisplayMetrics dm = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//       // int gridviewWidth = (int) (size * (length + 4) * density);
//        int itemWidth = (int) (length * density);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemWidth);
//        holder.rl_iv.setLayoutParams(lp);
        String url = ImageUrlExtends.getImageUrl(item.getCover(), 11);
        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(holder.iv_cover);
        if (!TextUtils.isEmpty(item.getPrice()+"")){
            holder.tv_price.setText("¥"+item.getPrice()+"");
        }
        if (!TextUtils.isEmpty(item.getName())){
            holder.tv_content.setText(item.getName());
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopItemModel.RelatedGoodsBean item = mdata.get(position);
                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
                // intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    private static class ViewHolder {
        public TextView tv_content;
        public TextView  tv_price;
        public ImageView iv_cover;
        public RelativeLayout rl_iv;
    }

}
