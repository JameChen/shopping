package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.bean.PackageReceivedBean;
import com.nahuo.quicksale.R;

/**
 * Created by jame on 2018/11/8.
 */

public class PackageColorSizeAdapter extends BaseQuickAdapter<PackageReceivedBean.ListBean.ProductsBean, BaseViewHolder> {
    Context context;
    private Color_Size_Click color_size_click;

    public void setColor_size_click(Color_Size_Click color_size_click) {
        this.color_size_click = color_size_click;
    }

    public PackageColorSizeAdapter(Context context) {
        super(R.layout.item_package_color_size, null);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final PackageReceivedBean.ListBean.ProductsBean item) {
        if (item != null) {
            helper.setText(R.id.tv_color_size, item.getColor() + "/" + item.getSize() + "/" + item.getQty());
            TextView tv_right_received = helper.getView(R.id.tv_right_received);
            if (item.isIsReceived()) {
                tv_right_received.setBackground(null);
                tv_right_received.setTextColor(ContextCompat.getColor(context,R.color.textColorPrimary));
                tv_right_received.setText("已收货");
                tv_right_received.setEnabled(false);
                tv_right_received.setClickable(false);
            } else {
                tv_right_received.setBackgroundResource(R.drawable.order_button_red_bg);
                tv_right_received.setTextColor(ContextCompat.getColor(context,R.color.white));
                tv_right_received.setText("确认收货");
                tv_right_received.setEnabled(true);
                tv_right_received.setClickable(true);
            }
            tv_right_received.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (color_size_click!=null){
                        color_size_click.onClick(item.getOrderProductID());
                    }

                }
            });
        }
    }
    public interface Color_Size_Click{
         void  onClick(String OrderProductID);
    }
}
