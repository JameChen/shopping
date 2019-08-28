package com.nahuo.quicksale.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.orderdetail.DelBaseAdapter;
import com.nahuo.quicksale.orderdetail.model.OrderShipInfoModel;
import com.nahuo.quicksale.orderdetail.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailShipInfoAdapter extends BaseAdapter{

    private List<OrderShipInfoModel> models ;
    public void setDatas( List<OrderShipInfoModel> _datas) {
        models = _datas;
    }
    @Override
    public int getCount() {
        return models == null ? 0 : models.size();
    }

    @Override
    public OrderShipInfoModel getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ; 
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lvitem_order_detail_shipinfo, null) ;
            holder = new ViewHolder() ; 
            convertView.setTag(holder) ;
            holder.name = (TextView)convertView.findViewById(R.id.ship_info_express_name) ;
            holder.detailinfo = (TextView)convertView.findViewById(R.id.ship_info_express_info) ;
            holder.info = (TextView)convertView.findViewById(R.id.ship_info_product_info) ;
            holder.ctime = (TextView)convertView.findViewById(R.id.ship_info_createtime) ;
        }
        else{
            holder = (ViewHolder)convertView.getTag()  ;
        }
        OrderShipInfoModel model = getItem(position) ;
        holder.name.setText(model.getExpressName()+"   快递单号："+model.getExpressCode());
        holder.detailinfo.setText(Html.fromHtml(model.getExpressInfo()));
        holder.ctime.setText(model.getCreateTime());
        List<Product> products =  model.getShipProds() ;
        StringBuffer info = new StringBuffer() ;
        for (Product p: products) {
            info.append(p.Color) ;
            info.append("   ") ;
            info.append(p.Size);
            info.append("   ");
            info.append(p.Qty) ;
            info.append("\n") ;
        }
        holder.info.setText(info.toString()) ;
        return convertView;
    }

    static class ViewHolder{
        TextView ctime , name , info, detailinfo;
    }
}
