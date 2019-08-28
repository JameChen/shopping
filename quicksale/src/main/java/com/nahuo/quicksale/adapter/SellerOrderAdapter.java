package com.nahuo.quicksale.adapter;

import java.util.List;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.orderdetail.DelBaseAdapter;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.Product;
import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SellerOrderAdapter extends DelBaseAdapter{

    private List<OrderItemModel> models ; 
    private LayoutInflater mInflater ; 
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
        this.models = getModels(models) ; 
        notifyDataSetChanged() ; 
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ; 
        if(convertView == null){
            if(mInflater == null)
            {
                mInflater = LayoutInflater.from(parent.getContext()) ; 
            }
            convertView = mInflater.inflate(R.layout.lvitem_sendgoods_product   , null) ;
            holder = new ViewHolder() ; 
            convertView.setTag(holder) ;
            holder.icon = (ImageView)convertView.findViewById(R.id.img_order_detail_icon) ; 
            holder.name = (TextView)convertView.findViewById(R.id.txt_order_detail_name) ; 
            
            holder.editNumber = (TextView)convertView.findViewById(R.id.et_sendgoods_number) ; 
            holder.del = (TextView)convertView.findViewById(R.id.txt_sendgoods_del) ; 
            holder.txtPrice = (TextView)convertView.findViewById(R.id.txt_price) ; 
            holder.txtGetPrice = (TextView)convertView.findViewById(R.id.txt_get_price) ; 
            holder.info = (TextView)convertView.findViewById(R.id.txt_order_detail_info) ; 
            holder.parent = convertView.findViewById(R.id.ll_editnumber) ; 
            
        }
        else{
            holder = (ViewHolder)convertView.getTag()  ;
        }
        OrderItemModel model = getItem(position) ; 
        String imageUrl = model.getCover() ; 
        imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_ITEM_SIZE); 
        Picasso.with(parent.getContext()).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon) ;
        holder.name.setText(model.getName()) ;
        List<Product> products =  model.getProducts() ; 
        StringBuffer info = new StringBuffer() ;
        if(products!=null){
            Product p = products.get(0) ; 
            info.append(p.Color) ;
            info.append("/") ; 
            info.append(p.Size);
            if(p.IsDeleted){
                info.append("/");
                info.append(p.Qty) ;
                if(holder.editNumber.getVisibility()!=View.GONE)
                    holder.editNumber.setVisibility(View.GONE) ; 
                if(holder.parent.getVisibility()!=View.VISIBLE)
                    holder.parent.setVisibility(View.VISIBLE) ; 
                holder.del.setOnClickListener(null) ; 
                holder.del.setText("已删除") ;
                holder.del.setTextColor(parent.getResources().getColor(R.color.orange)) ;
            }
            else{
                if(p.EnableModify){
                    if(holder.editNumber.getVisibility()!=View.VISIBLE)
                        holder.editNumber.setVisibility(View.VISIBLE) ;
                    if(holder.parent.getVisibility()!=View.VISIBLE)
                        holder.parent.setVisibility(View.VISIBLE) ; 
                    holder.editNumber.setText(String.valueOf(p.Qty)) ; 
//                    holder.editNumber.setOnEditorActionListener(eal) ;
                    holder.editNumber.setOnClickListener(editNumberL) ; 
                    holder.editNumber.setTag(p) ;
                    holder.del.setTag(p) ;
                    holder.del.setOnClickListener(delProductItemListener) ;
                    holder.del.setTextColor(parent.getResources().getColor(R.color.blue_edittext_border)) ;
                }
                else{
                    
                    info.append("/");
                    info.append(p.Qty) ;
                    if(holder.parent.getVisibility()!=View.INVISIBLE)
                        holder.parent.setVisibility(View.INVISIBLE) ; 
                }
            }
            holder.info.setText(info) ;
            holder.txtPrice.setText(holder.del.getResources().getString(R.string.rmb_2 , model.getPrice()));
            
            if(model.getParent()!=null){
                holder.txtGetPrice.setText(holder.del.getResources().getString(R.string.order_get_money1 , model.getParent().getPrice()));
            }
            else{
                holder.txtGetPrice.setText(null) ;
            }
        }
        return convertView;
    }
    static class ViewHolder{
        ImageView icon ; 
        TextView name  , info; 
        TextView editNumber , del , txtPrice  , txtGetPrice ; 
        View parent ; 
    }
}
