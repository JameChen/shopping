//package com.nahuo.wp.adapter;
//
//import java.util.List;
//
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.R;
//import com.nahuo.wp.orderdetail.DelBaseAdapter;
//import com.nahuo.wp.orderdetail.model.OrderItemModel;
//import com.nahuo.wp.orderdetail.model.Product;
//import com.squareup.picasso.Picasso;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//public class SendGoodsOrderAdapter extends DelBaseAdapter{
//
//    private List<OrderItemModel> models ; 
//    private LayoutInflater mInflater ; 
//    @Override
//    public int getCount() {
//        return models == null ? 0 : models.size();
//    }
//
//    @Override
//    public OrderItemModel getItem(int position) {
//        return models.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//    public void refresh(List<OrderItemModel> models){
//        if(this.models!=null)
//            this.models.clear() ; 
//        this.models = models ; 
//        notifyDataSetChanged() ; 
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null ; 
//        if(convertView == null){
//            if(mInflater == null)
//            {
//                mInflater = LayoutInflater.from(parent.getContext()) ; 
//            }
//            convertView = mInflater.inflate(R.layout.lvitem_sendgoods_product   , null) ;
//            holder = new ViewHolder() ; 
//            convertView.setTag(holder) ;
//            holder.icon = (ImageView)convertView.findViewById(R.id.img_order_detail_icon) ; 
//            holder.name = (TextView)convertView.findViewById(R.id.txt_order_detail_name) ; 
//            holder.ll = (LinearLayout)convertView.findViewById(R.id.ll_ss_product_parent);
//        }
//        else{
//            holder = (ViewHolder)convertView.getTag()  ;
//        }
//        OrderItemModel model = getItem(position) ; 
//        String imageUrl = model.getCover() ; 
//        imageUrl = ImageUrlExtends.getImageUrl(imageUrl, 3); 
//        Picasso.with(parent.getContext()).load(imageUrl).placeholder(R.drawable.empty_photo).into(holder.icon) ;
//        holder.name.setText(model.getName()) ;
//        List<Product> products =  model.getProducts(); 
//        holder.ll.removeAllViews() ; 
//        if(products!=null){
//            for(Product p : products){
//                View child = mInflater.inflate(R.layout.common_sendgoods_product_item   , null) ;
//                TextView infoView = (TextView)child.findViewById(R.id.txt_order_detail_info) ; 
//                TextView moneyView = (TextView)child.findViewById(R.id.txt_order_detail_money) ; 
//                moneyView.setText(parent.getResources().getString(R.string.rmb_x , model.getPrice()));
//                if(p.IsDeleted){
//                    infoView.setText(p.Color+"/"+p.Size+"/"+p.Qty) ;
//                    View editParent = child.findViewById(R.id.ll_editnumber) ; 
//                    editParent.setVisibility(View.INVISIBLE) ; 
//                }
//                else{
//                    infoView.setText(p.Color+"/"+p.Size+"/") ;
//                    TextView editNumber = (TextView)child.findViewById(R.id.et_sendgoods_number) ; 
//                    editNumber.setText(String.valueOf(p.Qty)) ; 
//                    editNumber.setOnEditorActionListener(eal) ;
//                    View del = child.findViewById(R.id.txt_sendgoods_del);
//                    Log.e("test" , "del = "+del) ;
//                    del.setOnClickListener(delProductItemListener) ;
//                }
//                holder.ll.addView(child) ; 
//            }
//        }
//        return convertView;
//    }
//    static class ViewHolder{
//        ImageView icon ; 
//        TextView name ; 
//        LinearLayout ll ;
//    }
//}
