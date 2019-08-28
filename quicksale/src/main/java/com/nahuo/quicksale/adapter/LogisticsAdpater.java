package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.bean.LogisticsBean;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.List;

/**
 * Created by jame on 2018/3/8.
 */

public class LogisticsAdpater extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    Context mContext;
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    public LogisticsAdpater(List<MultiItemEntity> data, Context mContext) {
        super(data);
        this.mContext = mContext;
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0_order_detail);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1_order_detail_logistics);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final LogisticsBean.OrdersParentBean parent = (LogisticsBean.OrdersParentBean) item;
                helper.setGone(R.id.line_top, false);
                if (parent.isExpanded()) {
                    helper.setImageResource(R.id.icon_expand, R.drawable.oder_shang);
                } else {
                    helper.setImageResource(R.id.icon_expand, R.drawable.order_xia);
                }
                helper.setText(R.id.tv_tittle, parent.name);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (parent.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final LogisticsBean.OrdersBean sub = (LogisticsBean.OrdersBean) item;
                if (sub != null) {
                    if (sub.isShowTop) {
                        helper.setGone(R.id.line_top, true);
                    } else {
                        helper.setGone(R.id.line_top, false);
                    }
//                    if (sub.getItemPostFee()>0){
//                        helper.setGone(R.id.tv_post_fee, true);
//                        helper.setText(R.id.tv_post_fee, "分摊运费¥ " + sub.getItemPostFee());
//                    }else {
//                        helper.setGone(R.id.tv_post_fee, false);
//                    }
                    helper.setGone(R.id.tv_post_fee, true);
                    helper.setText(R.id.tv_post_fee, "分摊运费¥ " + sub.getItemPostFee());
                    helper.setText(R.id.tv_order_code, "商品单号：" + sub.getCode());
                    helper.getView(R.id.tv_copy_code).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.addNewToClipboard(mContext, sub.getCode());
                        }
                    });
                    ImageView imageView = helper.getView(R.id.iv_icon);
                    String path = ImageUrlExtends.getImageUrl(sub.getCover(), Const.LIST_COVER_SIZE);
                    GlideUtls.glidePic(mContext, path, imageView);
                    helper.setText(R.id.tv_order_title, sub.getTitle());
                    helper.setText(R.id.tv_price, "¥" + sub.getPrice());
                    helper.setText(R.id.tv_count, "共" + sub.getTotalQty() + "件");
//                    if (TextUtils.isEmpty(sub.getSummary())) {
//                        helper.setVisible(R.id.tv_summary, false);
//                    } else {
//                        helper.setVisible(R.id.tv_summary, true);
//                    }
                    //  helper.setText(R.id.tv_summary, sub.getSummary());
                    if (!ListUtils.isEmpty(sub.getProducts())) {
                        helper.setVisible(R.id.layout_color_size, true);
                        helper.setVisible(R.id.tv_right_received, true);
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        for (int i = 0; i < sub.getProducts().size(); i++) {
                            LogisticsBean.OrdersBean.ProductsBean productsBean = sub.getProducts().get(i);
                            if (i == 0) {
                                sb.append("<font color=\"#424242\">" + productsBean.getColor() + "/" + productsBean.getSize() + "/"
                                        + productsBean.getQty()+"</font>");
                            } else {
                                sb.append("<br>" + "<font color=\"#424242\">" + productsBean.getColor() + "/" + productsBean.getSize() + "/"
                                        + productsBean.getQty()+"</font>");
                            }
                            if (i == 0) {
                                if (productsBean.isReceived()) {
                                    sb2.append("<font color=\"#747474\">已收货");
                                }else {
                                    sb2.append("");
                                }
                            }else {
                                if (productsBean.isReceived()) {
                                    sb2.append("<br><font color=\"#747474\">已收货");
                                }else {
                                    sb2.append("<br>");
                                }
                            }
                        }
                        helper.setText(R.id.tv_color_size, Html.fromHtml(sb.toString()));
                        helper.setText(R.id.tv_right_received, Html.fromHtml(sb2.toString()));
                    } else {
                        helper.setGone(R.id.tv_right_received,false);
                        helper.setGone(R.id.layout_color_size, false);
                    }
//                    LinearLayout layout_address_buttons = helper.getView(R.id.layout_address_buttons);
//                    if (ListUtils.isEmpty(sub.getButtons())) {
//                        helper.setGone(R.id.layout_buttons, false);
//                    } else {
//                        helper.setGone(R.id.layout_buttons, true);
//                        addOrderDetailButton(layout_address_buttons, sub.getButtons(), sub);
//                    }
                    helper.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                                intent.putExtra(ItemDetailsActivity.EXTRA_ID, sub.getItemID());
                                // intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                                mContext.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }
}
