package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.bean.PackageReceivedBean;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.List;

/**
 * Created by jame on 2018/11/7.
 */

public class PackageReceivedAdapter extends BaseQuickAdapter<PackageReceivedBean.ListBean,BaseViewHolder> {
    private List<PackageReceivedBean.ListBean> data;
    private Context context;
    private Color_Size_Click colorSizeClick;

    public void setColorSizeClick(Color_Size_Click colorSizeClick) {
        this.colorSizeClick = colorSizeClick;
    }

    public PackageReceivedAdapter(Context context) {
        super(R.layout.item_pacage_received, null);
        this.context=context;
    }

    public void setMyData(List<PackageReceivedBean.ListBean> data) {
        this.data = data;
        super.setNewData(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PackageReceivedBean.ListBean item) {
        if (item!=null){
            RecyclerView rl_color_size=helper.getView(R.id.rl_color_size);
            rl_color_size.setNestedScrollingEnabled(false);
            if (ListUtils.isEmpty(item.getProducts())){
                helper.setGone(R.id.rl_color_size,false);
            }else {
                helper.setGone(R.id.rl_color_size,true);
                PackageColorSizeAdapter adapter=new PackageColorSizeAdapter(context);
                adapter.setNewData(item.getProducts());
                adapter.setColor_size_click(new PackageColorSizeAdapter.Color_Size_Click() {
                    @Override
                    public void onClick(String OrderProductID) {
                        if (colorSizeClick!=null){
                            colorSizeClick.onClick(OrderProductID);
                        }
                    }
                });
                rl_color_size.setLayoutManager(new LinearLayoutManager(context));
                rl_color_size.setAdapter(adapter);
            }
            if (helper.getAdapterPosition()==0){
                helper.setGone(R.id.line_top,false);
            }else {
                helper.setGone(R.id.line_top,true);
            }
            helper.setText(R.id.tv_order_code,"商品单号："+item.getCode());
            ImageView imageView = helper.getView(R.id.iv_icon);
            String path = ImageUrlExtends.getImageUrl(item.getCover(), Const.LIST_COVER_SIZE);
            GlideUtls.glidePic(mContext, path, imageView);
            helper.setText(R.id.tv_order_title, item.getTitle());
            helper.setText(R.id.tv_price, "¥" + item.getPrice());
            helper.setText(R.id.tv_count, "共" + item.getTotalQty() + "件");
            if (item.getItemPostFee()>0){
                helper.setText(R.id.tv_post_fee, "分摊运费¥ " + item.getItemPostFee());
                helper.setGone(R.id.tv_post_fee,true);
            }else {
                helper.setGone(R.id.tv_post_fee,false);
            }

        }
    }
    public interface Color_Size_Click{
        void  onClick(String OrderProductID);
    }
}
