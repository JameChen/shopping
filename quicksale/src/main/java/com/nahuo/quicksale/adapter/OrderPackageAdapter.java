package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.bean.PackageListBean;
import com.nahuo.bean.PackageListParent;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.LogisticsActivity;
import com.nahuo.quicksale.common.Utils;

import java.util.List;

/**
 * Created by jame on 2018/3/7.
 */

public class OrderPackageAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    Context mContext;
    private int orderId;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public OrderPackageAdapter(List<MultiItemEntity> data, Context mContext) {
        super(data);
        this.mContext = mContext;
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0_order_detail_package);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1_order_detail_package);
    }


    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final PackageListParent parent = (PackageListParent) item;
                if (parent != null)
                    helper.setText(R.id.tv_tittle, parent.getName());
                helper.setGone(R.id.line_top, false);
                //helper.setImageResource(R.id.icon_expand, R.drawable.my_order_get);
                if (parent.isExpanded()) {
                    helper.setImageResource(R.id.icon_expand, R.drawable.pack_shang);
                } else {
                    helper.setImageResource(R.id.icon_expand, R.drawable.pack_xia);
                }
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
                final PackageListBean sub = (PackageListBean) item;
                if (sub != null) {
                    if (sub.isShowTop)
                        helper.setGone(R.id.top_line, true);
                    else
                        helper.setGone(R.id.top_line, false);
                    helper.setText(R.id.tv_order_code, sub.getPosStr() + sub.getCode());
                    helper.setText(R.id.tv_logistics, "物流：" + sub.getName());
                    if (TextUtils.isEmpty(sub.getSummary())) {
                        helper.setGone(R.id.tv_weight, false);
                    } else {
                        helper.setGone(R.id.tv_weight, true);
                    }
                    helper.setText(R.id.tv_weight, sub.getSummary());
                    helper.setText(R.id.tv_create_time, "创建时间："+sub.getShipTime());
                    helper.getView(R.id.tv_copy_code).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(sub.getCode()))
                                ViewHub.showShortToast(mContext, "单号为空");
                            else
                                Utils.addNewToClipboard(mContext, sub.getCode());
                        }
                    });
                    helper.getView(R.id.tv_look_logistics).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(mContext, LogisticsActivity.class);
                                intent.putExtra(LogisticsActivity.ETRA_SHIPID, sub.getShipID());
                                intent.putExtra(LogisticsActivity.ETRA_ORDER_ID, orderId);
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
