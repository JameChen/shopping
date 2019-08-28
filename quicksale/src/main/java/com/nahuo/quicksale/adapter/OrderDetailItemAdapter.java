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

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailItemAdapter extends BaseAdapter{

    private List<OrderItemModel> models ;
    private Context mContext;
    public int qsid;

    public OrderDetailItemAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public OrderItemModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void refresh(List<OrderItemModel> models){
        if(this.models!=null)
            this.models.clear() ; 
        this.models = models;//DelBaseAdapter.getModels(models) ;
        notifyDataSetChanged() ; 
    }
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null ;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_order_detail_product, null) ;
            holder = new ViewHolder() ; 
            convertView.setTag(holder) ;
            holder.icon = (ImageView)convertView.findViewById(R.id.img_order_detail_icon) ; 
            holder.name = (TextView)convertView.findViewById(R.id.txt_order_detail_name) ; 
            holder.info = (ListView)convertView.findViewById(R.id.txt_order_detail_info) ;
            holder.money = (TextView)convertView.findViewById(R.id.txt_order_detail_money) ;
            holder.summary = (TextView)convertView.findViewById(R.id.txt_order_detail_summary) ;
        }
        else{
            holder = (ViewHolder)convertView.getTag()  ;
        }
        final OrderItemModel model = getItem(position) ;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, model.getAgentItemID());
                //修改去掉qid
               // intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, qsid);
                mContext.startActivity(intent);
            }
        });
        String imageUrl = model.getCover() ; 
        imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_ITEM_SIZE); 
        Picasso.with(parent.getContext()).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon) ;
        holder.name.setText(model.getName()) ;
        OrderDetailItemProductAdapter adapter = new OrderDetailItemProductAdapter();
        adapter.productDatas = model.getProducts() ;
        holder.info.setAdapter(adapter);
        holder.money.setText(parent.getResources().getString(R.string.rmb_x, model.getPrice()));
        if (model.getSummary()!=null && model.getSummary().length() > 0) {
            holder.summary.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.summary.setVisibility(View.GONE);
        }
        holder.summary.setText(model.getSummary());
        return convertView;
    }

    static class ViewHolder{
        ImageView icon ; 
        TextView name , money, summary;
        ListView info;
    }

    static class ViewHolder1{
        TextView info , summary,deleted;
    }

    public class OrderDetailItemProductAdapter extends BaseAdapter{

        public List<Product> productDatas ;
        @Override
        public int getCount() {
            return productDatas == null ? 0 : productDatas.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder1 holder = null ;
            if(convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_order_detail_product_color_size, null) ;
                holder = new ViewHolder1() ;
                convertView.setTag(holder) ;
                holder.info = (TextView)convertView.findViewById(R.id.info) ;
                holder.summary = (TextView)convertView.findViewById(R.id.summary) ;
                holder.deleted = (TextView)convertView.findViewById(R.id.deleted);
            }
            else{
                holder = (ViewHolder1)convertView.getTag()  ;
            }
            Product pd = getItem(position) ;
            holder.info.setText(pd.Color+" / "+pd.Size+" / "+pd.Qty) ;
            holder.summary.setText(pd.Summary);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Product getItem(int position) {
            return productDatas.get(position);
        }
    }
}
