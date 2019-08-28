package com.nahuo.quicksale.adapter;

import java.util.List;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.DialogEditNumber;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.orderdetail.DelBaseAdapter;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.Product;
import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PickingBillAdapter extends BaseAdapter{

    private List<OrderItemModel> models ; 
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
        this.models = DelBaseAdapter.getModels(models) ; 
        notifyDataSetChanged() ; 
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ; 
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_agent_product   , null) ;
            holder = new ViewHolder() ; 
            convertView.setTag(holder) ;
            holder.icon = (ImageView)convertView.findViewById(R.id.img_order_detail_icon) ; 
            holder.name = (TextView)convertView.findViewById(R.id.txt_order_detail_name) ; 
            holder.info = (TextView)convertView.findViewById(R.id.txt_order_detail_info) ; 
            holder.money = (TextView)convertView.findViewById(R.id.txt_order_detail_money) ; 
            holder.getMoney = (TextView)convertView.findViewById(R.id.txt_order_detail_get_money) ; 
            holder.btnNum = (Button)convertView.findViewById(R.id.et_sendgoods_number);
            holder.btnDelete = (Button)convertView.findViewById(R.id.txt_sendgoods_del);
        }
        else{
            holder = (ViewHolder)convertView.getTag()  ;
        }
        OrderItemModel model = getItem(position) ; 
        String imageUrl = model.getCover() ; 
        imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_ITEM_SIZE); 
        Picasso.with(parent.getContext()).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon) ;
        holder.name.setText(model.getName()) ;
        StringBuffer info = new StringBuffer() ; 
        List<Product> products =  model.getProducts() ; 
        if(products!=null){
            for(Product p : products){
                info.append(p.Color) ;
                info.append("/") ; 
                info.append(p.Size);
                info.append("/");
                if(p.EnableModify) {
                    holder.btnNum.setTag(p);
                    holder.btnNum.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Product p = (Product)v.getTag() ;
                            new DialogEditNumber(v.getContext(), p.ID, p.Qty).show() ;
                        }
                    });
                    holder.btnDelete.setVisibility(View.VISIBLE);
                    holder.btnNum.setVisibility(View.VISIBLE);
                }else {
                    holder.btnDelete.setVisibility(View.GONE);
                    holder.btnNum.setVisibility(View.GONE);
                    info.append(p.Qty) ;
                }
                
            }
        }
        holder.info.setText(info) ; 
        holder.money.setText(parent.getResources().getString(R.string.rmb_x , model.RetailPrice));
        holder.getMoney.setText(parent.getResources().getString(R.string.order_get_money1 , model.getPrice())) ;
        return convertView;
    }

    static class ViewHolder{
        ImageView icon ; 
        TextView name , info , money , getMoney; 
        Button btnNum, btnDelete;
    }
}
