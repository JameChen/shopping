package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.nahuo.bean.LogisticsBean;
import com.nahuo.quicksale.R;

import java.util.List;

/**
 * Created by jame on 2018/3/8.
 */

public class ExpressInfoAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    Context mContext;
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;

    public ExpressInfoAdapter(List<MultiItemEntity> data, Context mContext) {
        super(data);
        this.mContext = mContext;
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0_order_detail_express);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1_order_detail_express);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final LogisticsBean.ExpressInfoParentListBean parent = (LogisticsBean.ExpressInfoParentListBean) item;
                if (parent.isExpanded()) {
                    helper.setImageResource(R.id.icon_expand, R.drawable.arrow_shang);
                } else {
                    helper.setImageResource(R.id.icon_expand, R.drawable.arrow_xia);
                }
                helper.getView(R.id.tv_look_logistics).setOnClickListener(new View.OnClickListener() {
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
                if (parent != null) {
                    if (parent.getExpressInfoListSize() > 1) {
                        helper.setGone(R.id.tv_look_logistics, true);
                        helper.setGone(R.id.icon_expand, true);
                    } else {
                        helper.setGone(R.id.tv_look_logistics, false);
                        helper.setGone(R.id.icon_expand, false);
                    }
                    LogisticsBean.ExpressInfoListBean expressInfoListBean = parent.getFirstInfoListBean();
                    if (expressInfoListBean != null) {
                        if (TextUtils.isEmpty(expressInfoListBean.getContent())) {
                            helper.setGone(R.id.tv_content, false);
                            helper.setGone(R.id.tv_time, false);
                        } else {
                            helper.setGone(R.id.tv_content, true);
                            helper.setGone(R.id.tv_time, true);
                            helper.setText(R.id.tv_content, expressInfoListBean.getContent());
                            helper.setText(R.id.tv_time, expressInfoListBean.getTime());
                        }

                    } else {
                        helper.setGone(R.id.tv_content, false);
                        helper.setGone(R.id.tv_time, false);
                    }
                }
                break;
            case TYPE_LEVEL_1:
                LogisticsBean.ExpressInfoListBean expressInfoListBean = (LogisticsBean.ExpressInfoListBean) item;
                if (expressInfoListBean != null) {
                    if (TextUtils.isEmpty(expressInfoListBean.getContent())) {
                        helper.setGone(R.id.tv_content, false);
                        helper.setGone(R.id.tv_time, false);
                    } else {
                        helper.setGone(R.id.tv_content, true);
                        helper.setGone(R.id.tv_time, true);
                        helper.setText(R.id.tv_content, expressInfoListBean.getContent());
                        helper.setText(R.id.tv_time, expressInfoListBean.getTime());
                    }

                } else {
                    helper.setGone(R.id.tv_content, false);
                    helper.setGone(R.id.tv_time, false);
                }
                break;

        }
    }
}
