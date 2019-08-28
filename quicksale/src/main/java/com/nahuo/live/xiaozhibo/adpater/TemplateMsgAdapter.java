package com.nahuo.live.xiaozhibo.adpater;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.nahuo.quicksale.R;

/**
 * Created by jame on 2019/5/13.
 */

public class TemplateMsgAdapter extends BaseQuickAdapter<String,BaseViewHolder> {
    private Context context;
    public TemplateMsgAdapter(Context context) {
        super(R.layout.item_txt);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (item!=null){
            helper.setText(R.id.tv_txt,item.toString());
        }
    }
}
