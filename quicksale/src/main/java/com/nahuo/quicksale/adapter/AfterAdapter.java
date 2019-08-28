package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.bean.AfterBean;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.List;

/**
 * Created by jame on 2018/3/13.
 */

public class AfterAdapter extends BaseQuickAdapter<AfterBean, BaseViewHolder> {
    Activity activity;

    public AfterAdapter(@Nullable List<AfterBean> data, Activity activity) {
        super(R.layout.layout_item_after);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, AfterBean item) {
        if (item != null) {
            helper.setText(R.id.tv_order_code, "商品单号   "+item.getOrderCode());
            helper.setText(R.id.tv_order_status, item.getResultTypeName());
            helper.setText(R.id.tv_order_title, item.getName());
            helper.setText(R.id.tv_price, "¥" + item.getPrice());
            helper.setText(R.id.tv_count, "共登记" + item.getQty() + "件");
//            String refing = item.getRefundingMoney();
//            String refed = item.getRefundedMoney();
            String msg = item.getOrderText();
//            if (!TextUtils.isEmpty(refing) && !TextUtils.isEmpty(refed)) {
//                msg = "退款中¥" + refing + "，" + "已退款" + refed;
//            } else if (TextUtils.isEmpty(refing) && !TextUtils.isEmpty(refed)) {
//                msg = "已退款" + refed;
//            } else if (!TextUtils.isEmpty(refing) && TextUtils.isEmpty(refed)) {
//                msg = "退款中¥" + refing;
//            }
            if (helper.getAdapterPosition() > 0) {
                helper.setGone(R.id.line, true);
            } else {
                helper.setGone(R.id.line, false);
            }
            helper.setText(R.id.tv_order_summary, msg);
            ImageView imageView = helper.getView(R.id.iv_simple_pic);
            String imageUrl1 = ImageUrlExtends.getImageUrl(item.getCover(), Const.LIST_COVER_SIZE);
            GlideUtls.glidePic(activity, imageUrl1, imageView);
            helper.setText(R.id.tv_order_title, item.getName());
            //  imageUrl1 = ImageUrlExtends.getImageUrl(item.get, Const.LIST_COVER_SIZE);
        }
    }
}
