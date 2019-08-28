package com.nahuo.live.xiaozhibo.adpater;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.live.xiaozhibo.common.utils.GlideUtls;
import com.nahuo.live.xiaozhibo.model.GoodsBean;
import com.nahuo.quicksale.R;

/**
 * Created by jame on 2019/5/15.
 */

public class GoodsAdapter extends BaseQuickAdapter<GoodsBean.GoodsListBean, BaseViewHolder> {
    private Context context;
    public static int Type_Live = 1;
    public static int Type_Play = 2;
    private int type = Type_Live;
    private TryOnItemOnClick tryOnItemOnClick;

    public void setTryOnItemOnClick(TryOnItemOnClick tryOnItemOnClick) {
        this.tryOnItemOnClick = tryOnItemOnClick;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GoodsAdapter(Context context) {
        super(R.layout.item_goods);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsBean.GoodsListBean item) {
        if (item != null) {
            if (item.getSort() > 0) {
                helper.setGone(R.id.tv_sort, true);
            } else {
                helper.setGone(R.id.tv_sort, false);
            }
            helper.setText(R.id.tv_sort, item.getSort() + "");
            helper.setText(R.id.tv_tittle, item.getName() + "");
            helper.setText(R.id.tv_price, "¥" + item.getPrice() + "");
            helper.setText(R.id.tv_sort, item.getSort() + "");
            TextView tvTryOn = helper.getView(R.id.tv_try_on);
            ImageView iv_pic = helper.getView(R.id.iv_pic);
            ImageView iv_shop_cart = helper.getView(R.id.iv_shop_cart);
            GlideUtls.loadRoundedCorners(context,R.drawable.empty_photo, ImageUrlExtends.getImageUrl(item.getCover()) ,iv_pic);
            if (type == Type_Live) {
                tvTryOn.setVisibility(View.VISIBLE);
            } else {
                tvTryOn.setVisibility(View.GONE);
            }
            int StatuID=item.getStatuID();
            if (StatuID==1){
                iv_shop_cart.setVisibility(View.VISIBLE);
            }else {
                iv_shop_cart.setVisibility(View.INVISIBLE);
            }
            if (item.isTryOn()) {
                helper.setGone(R.id.tv_try_msg,true);
//                tvTryOn.setText("取消试穿");
//                tvTryOn.setTextColor(ContextCompat.getColor(context, R.color.txt_black));
//                tvTryOn.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_live_rectangle_black));
            } else {
                helper.setGone(R.id.tv_try_msg,false);
//                tvTryOn.setText("试穿一下");
//                tvTryOn.setTextColor(ContextCompat.getColor(context, R.color.white));
//                tvTryOn.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_live_shape_red));
            }
            iv_shop_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tryOnItemOnClick != null)
                        tryOnItemOnClick.OnBuyClick(item, helper.getAdapterPosition(), item.isTryOn());
                }
            });
            tvTryOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tryOnItemOnClick != null)
                        tryOnItemOnClick.OnClick(item, helper.getAdapterPosition(), item.isTryOn());
                }
            });
        }
    }

    public interface TryOnItemOnClick {
        void OnClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn);
        void OnBuyClick(GoodsBean.GoodsListBean item, int position, boolean isTryOn);
    }
}
