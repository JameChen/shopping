package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ShipLogActivity;
import com.nahuo.quicksale.activity.LogisticsActivity;
import com.nahuo.quicksale.activity.PackageReceivedActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.BGQDModel;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;
import com.nahuo.quicksale.orderdetail.GetBuyOrderActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BGQDLogAdapter extends MyBaseAdapter<BGQDModel> {

    private BGQDLogAdapter vThis = this;
    Activity activity;
    public BGQDLogAdapter(Activity context) {
        super(context);
        activity=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final BGQDModel item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_bgqd_log, parent, false);
            holder = new ViewHolder();
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
            holder.txt3 = (TextView) convertView.findViewById(R.id.txt3);
            holder.txt4 = (TextView) convertView.findViewById(R.id.txt4);
            holder.btn = (TextView) convertView.findViewById(R.id.btnGoWL);
            holder.imgUp = (ImageView) convertView.findViewById(R.id.btnUp);
            holder.info = (ListView) convertView.findViewById(R.id.list);
            holder.btnGoOrderGood = (TextView) convertView.findViewById(R.id.btnGoOrderGood);
            holder.btnGoDetail = (TextView) convertView.findViewById(R.id.btnGoDetail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (item.isReceivedButton()) {
            holder.btnGoOrderGood.setVisibility(View.VISIBLE);
        } else {
            holder.btnGoOrderGood.setVisibility(View.GONE);
        }
        holder.txt1.setText("日期：" + item.getShipTime());
        if (item.getPackageTotalQty() > 0) {
            holder.txt2.setText("共：" + item.getPackageTotalQty() + "件，运费：" + item.getPostFee());
        } else {
            holder.txt2.setText("运费：" + item.getPostFee());
        }
        holder.txt3.setText("物流：" + item.getExpressName());
        holder.txt4.setText("单号：" + item.getExpressCode());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getItems().size() > 0) {
                    Intent shipIntent = new Intent(mContext, ShipLogActivity.class);
                    shipIntent.putExtra("name", item.getExpressName());
                    shipIntent.putExtra("code", item.getExpressCode());
                    mContext.startActivity(shipIntent);
                }
            }
        });
        holder.btnGoOrderGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getItems().size() > 0) {
                    Intent intent = new Intent(activity, PackageReceivedActivity.class);
                    intent.putExtra(PackageReceivedActivity.EXTRA_SHIPID, item.getShipID());
                    activity.startActivity(intent);
                }
            }
        });
        holder.btnGoDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getItems().size() > 0) {
                    Intent intent = new Intent(mContext, LogisticsActivity.class);
                    intent.putExtra(LogisticsActivity.ETRA_SHIPID, item.getShipID());
                    intent.putExtra(LogisticsActivity.ETRA_ORDER_ID, 0);
                    mContext.startActivity(intent);
                }
            }
        });
        holder.imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShowDetail = !item.isShowDetail;
                vThis.notifyDataSetChanged();
            }
        });
        if (item.isShowDetail) {
            holder.info.setVisibility(View.VISIBLE);
            holder.imgUp.setImageResource(R.drawable.up_icon);
        } else {
            holder.info.setVisibility(View.GONE);
            holder.imgUp.setImageResource(R.drawable.down_icon);
        }
        BGQDDetailAdapter adapter = new BGQDDetailAdapter();
        adapter.productDatas = item.getItems();
        holder.info.setAdapter(adapter);

        return convertView;
    }

    private static final class ViewHolder {
        TextView txt1, txt2, txt3, txt4, btn, btnGoOrderGood, btnGoDetail;
        ImageView imgUp;
        ListView info;
    }


    static class ViewHolder1 {
        TextView name, detail, txt_order_price, txt_order_count, tv_summary, txt_order_detail_order_goods;
        ImageView icon;
    }

    public class BGQDDetailAdapter extends BaseAdapter {

        public List<BGQDModel.ItemsBean> productDatas;

        @Override
        public int getCount() {
            return productDatas == null ? 0 : productDatas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder1 holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_bgqd_detail_log, null);
                holder = new ViewHolder1();
                convertView.setTag(holder);
                holder.icon = (ImageView) convertView.findViewById(R.id.img_order_detail_icon);
                holder.name = (TextView) convertView.findViewById(R.id.txt_order_detail_name);
                holder.detail = (TextView) convertView.findViewById(R.id.txt_order_detail_detail);
                holder.txt_order_count = (TextView) convertView.findViewById(R.id.txt_order_count);
                holder.txt_order_price = (TextView) convertView.findViewById(R.id.txt_order_price);
                holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
                holder.txt_order_detail_order_goods = (TextView) convertView.findViewById(R.id.txt_order_detail_order_goods);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }
            final BGQDModel.ItemsBean pd = getItem(position);

            holder.name.setText(pd.getName());
//            if (pd.getItemPostFee()>0) {
//                holder.tv_summary.setVisibility(View.VISIBLE);
//                holder.tv_summary.setText("共分摊运费¥ "+pd.getItemPostFee()+"");
//            }else {
//                holder.tv_summary.setVisibility(View.INVISIBLE);
//            }
            holder.tv_summary.setText("共分摊运费¥ " + pd.getItemPostFee() + "");
            String imageUrl = pd.getCover();
            imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_ITEM_SIZE);
            Picasso.with(parent.getContext()).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon);
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mContext, GetBuyOrderActivity.class);
                    it.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, pd.getOrderID());
                    mContext.startActivity(it);
                }
            });
            int count = 0;
            if (ListUtils.isEmpty(pd.getProducts())) {
                holder.detail.setText("");
                holder.txt_order_detail_order_goods.setText("");
            } else {
                StringBuffer info = new StringBuffer();
                StringBuffer info2 = new StringBuffer();
                info.append("");
                info2.append("");
                for (int i = 0; i < pd.getProducts().size(); i++) {
                    BGQDModel.ItemsBean.ProductsBean p = pd.getProducts().get(i);
                    info.append(p.getColor());
                    info.append(" / ");
                    info.append(p.getSize());
                    // info.append(" / ");
                    //info.append(p.getQty()) ;
                    count += p.getQty();
                    if (p.isReceived()) {
                        info2.append("已收货");
                    } else {
                        info2.append("");
                    }
                    if (i < pd.getProducts().size() - 1) {
                        info.append("\n");
                        info2.append("\n");
                    }
                }
                holder.detail.setText(info.toString());
                holder.txt_order_detail_order_goods.setText(info2.toString());
            }
            holder.txt_order_price.setText("¥ " + pd.getPrice());
            holder.txt_order_count.setText("共" + count + "件");


            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public BGQDModel.ItemsBean getItem(int position) {
            return productDatas.get(position);
        }
    }
}
