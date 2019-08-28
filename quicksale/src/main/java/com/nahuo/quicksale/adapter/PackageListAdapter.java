package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.bean.PackageNewListBean;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.CustomerServiceActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;
import com.nahuo.quicksale.orderdetail.GetBuyOrderActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PackageListAdapter extends MyBaseAdapter<PackageNewListBean.PackageListBean> {

    private PackageListAdapter vThis = this;

    public PackageListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final PackageNewListBean.PackageListBean item = mdata.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_package, parent, false);
            holder = new ViewHolder();
            holder.txt1 = (TextView) convertView.findViewById(R.id.txt1);
            holder.txt2 = (TextView) convertView.findViewById(R.id.txt2);
            holder.txt3 = (TextView) convertView.findViewById(R.id.txt3);
            holder.txt4 = (TextView) convertView.findViewById(R.id.txt4);
            holder.btn = (TextView) convertView.findViewById(R.id.btnGoWL);
            holder.imgUp = (ImageView) convertView.findViewById(R.id.btnUp);
            holder.btn_apply_after_sale = (TextView) convertView.findViewById(R.id.btn_apply_after_sale);
            holder.info = (ListView) convertView.findViewById(R.id.list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int btnType = 0;
        btnType = item.getBtnType();
        if (btnType == 0) {
            holder.btn_apply_after_sale.setVisibility(View.GONE);
        } else {
            holder.btn_apply_after_sale.setVisibility(View.VISIBLE);
        }
        holder.btn_apply_after_sale.setText(item.getBtnText()+"");
        holder.txt1.setText("日期：" + item.getShipTime());
        // holder.txt2.setText("运费：" + item.getPostFee());
        holder.txt3.setText("物流：" + item.getExpressName());
        holder.txt4.setText("单号：" + item.getExpressCode());
//        holder.btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (item.getItems().size() > 0) {
//                    Intent shipIntent = new Intent(mContext, ShipLogActivity.class);
//                    shipIntent.putExtra("name", item.getExpressName());
//                    shipIntent.putExtra("code", item.getExpressCode());
//                    mContext.startActivity(shipIntent);
//                }
//            }
//        });
        holder.btn_apply_after_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageNewListBean.PackageListBean item = mdata.get(position);
                int defectiveID = item.getDefectiveID();
                int orderID = item.getOrderID();
                int shippingID = item.getShippingID();
                if (item.getBtnType() == 1) {
                    Intent vendorIntent = new Intent(mContext, CustomerServiceActivity.class);
                    vendorIntent.putExtra(CustomerServiceActivity.EXTRA_TYPE, CustomerServiceActivity.TYPE_AFTER_SALES_APPLY);
                    vendorIntent.putExtra(CustomerServiceActivity.EXTRA_SHIPPINGID, shippingID);
                    vendorIntent.putExtra(CustomerServiceActivity.EXTRA_ORDERID, orderID);
                    mContext.startActivity(vendorIntent);

                } else if (item.getBtnType() == 2) {
                    Intent intent = new Intent(mContext, CustomerServiceActivity.class);
                    intent.putExtra(CustomerServiceActivity.EXTRA_TYPE, CustomerServiceActivity.TYPE_AFTER_SALES_APPLY_DETAIL);
                    intent.putExtra(CustomerServiceActivity.EXTRA_APPLYID, defectiveID);
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
        TextView txt1, txt2, txt3, txt4, btn, btn_apply_after_sale;
        ImageView imgUp;
        ListView info;
    }


    static class ViewHolder1 {
        TextView name, detail;
        ImageView icon;
    }

    public class BGQDDetailAdapter extends BaseAdapter {

        public List<PackageNewListBean.PackageListBean.ItemsBean> productDatas;

        @Override
        public int getCount() {
            return productDatas == null ? 0 : productDatas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder1 holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_bgqd_detail, null);
                holder = new ViewHolder1();
                convertView.setTag(holder);
                holder.icon = (ImageView) convertView.findViewById(R.id.img_order_detail_icon);
                holder.name = (TextView) convertView.findViewById(R.id.txt_order_detail_name);
                holder.detail = (TextView) convertView.findViewById(R.id.txt_order_detail_detail);
            } else {
                holder = (ViewHolder1) convertView.getTag();
            }
            final PackageNewListBean.PackageListBean.ItemsBean pd = getItem(position);

            holder.name.setText(pd.getName());

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

            StringBuffer info = new StringBuffer();
            for (PackageNewListBean.PackageListBean.ItemsBean.ProductsBean p : pd.getProducts()) {
                info.append(p.getColor());
                info.append("   ");
                info.append(p.getSize());
                info.append("   ");
                info.append(p.getQty());
                info.append("\n");
            }
            holder.detail.setText(info.toString());

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public PackageNewListBean.PackageListBean.ItemsBean getItem(int position) {
            return productDatas.get(position);
        }
    }
}
