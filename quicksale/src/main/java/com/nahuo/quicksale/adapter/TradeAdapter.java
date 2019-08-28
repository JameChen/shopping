package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.bean.TradeBean;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.List;

/**
 * Created by jame on 2018/8/1.
 */

public class TradeAdapter extends BaseQuickAdapter<TradeBean.ButtonsBean, BaseViewHolder> {

    private Context mContext;
    private List<TradeBean.ButtonsBean> mdata;
    private TradeListener mLister;

    public void setmLister(TradeListener mLister) {
        this.mLister = mLister;
    }

    public void setData(List<TradeBean.ButtonsBean> mdata) {
        this.mdata = mdata;
        super.setNewData(mdata);
    }

    public TradeAdapter(Context context) {
        super(R.layout.item_trade, null);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, TradeBean.ButtonsBean item) {
        if (item != null) {
            helper.setText(R.id.left_text, item.getName());
            helper.setText(R.id.right_text, item.getSummary());
            if (TextUtils.isEmpty(item.getIco())) {
                helper.setGone(R.id.left_icon, false);
            } else {
                helper.setGone(R.id.left_icon, true);
                GlideUtls.glidePic(mContext, ImageUrlExtends.getImageUrl(item.getIco()), (ImageView) helper.getView(R.id.left_icon));
            }
            helper.itemView.setOnClickListener(new MyOnclick(item));
        }
    }

    public class MyOnclick implements View.OnClickListener {
        TradeBean.ButtonsBean item;

        public MyOnclick(TradeBean.ButtonsBean item) {
            this.item = item;
        }

        @Override
        public void onClick(View v) {
            if (mLister != null)
                mLister.gotoDoWath(item);

        }
    }

    public interface TradeListener {
        void gotoDoWath(TradeBean.ButtonsBean item);
    }

}
