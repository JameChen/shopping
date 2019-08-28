package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.TempOrder;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.ArrayList;
import java.util.List;

import static com.nahuo.quicksale.R.id.layout_top;

/**
 * Created by jame on 2018/2/27.
 */

public class ShopItemmAdapter extends MyBaseAdapter<TempOrder.ItemsBean> {
    public ShopItemmAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_new, parent, false);
            holder = new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.layout_top=convertView.findViewById(layout_top);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            holder.tv_color_size=(TextView) convertView.findViewById(R.id.tv_color_size);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TempOrder.ItemsBean itemsBean = mdata.get(position);
        if (position==0){
            holder.layout_top.setVisibility(View.GONE);
        }else {
            holder.layout_top.setVisibility(View.VISIBLE);
        }
        if (itemsBean != null) {
            holder.title.setText(itemsBean.getName());
            holder.price.setText("¥"+itemsBean.getPrice());
            holder.tv_count.setText("共"+itemsBean.getTotalQty()+"件");
            String imageUrl1 = ImageUrlExtends.getImageUrl(itemsBean.getCover(), Const.LIST_COVER_SIZE);
            GlideUtls.glideChang(mContext, imageUrl1, holder.iv_icon);
            if (!ListUtils.isEmpty(itemsBean.getProducts())) {
                List<String> size_color = new ArrayList<>();
                StringBuilder builder = new StringBuilder();
                builder.append("");
                boolean isFirst=false;
                for (int i=0;i< itemsBean.getProducts().size();i++) {
                    TempOrder.ItemsBean.ProductsBean pm=itemsBean.getProducts().get(i);
                    if (pm.getQty() > 0) {
                        if (size_color.contains(pm.getColor())) {
                            builder.append(pm.getSize() + "/" + pm.getQty() + "件，");
                        } else {
                            size_color.add(pm.getColor());
                            if (isFirst){
                                String ss=  StringUtils.deleteEndStr(builder.toString(), "，")+"\n";
                                builder.setLength(0);
                                builder.append(ss);
                            }
                            builder.append(pm.getColor() + "：" + pm.getSize() + "/" + pm.getQty() + "件，");
                            isFirst=true;
                        }
                    }

                }
                String ss = StringUtils.deleteEndStr(builder.toString(), "，");
                holder.tv_color_size.setText(ss);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_icon;
        TextView price, title, tv_count,tv_color_size;
        View layout_top;
    }
}
