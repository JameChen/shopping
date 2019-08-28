package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;

/**
 * Created by 诚 on 2015/9/21.
 */
public class NewHotPostAdapter extends BaseHotAdapter{


    public NewHotPostAdapter(Context context)
    {
        mContext = context;
    }

    @Override
    protected void doChildView(View commonView, TopicInfoModel model) {
        ((TextView)commonView).setText(model.getSummary()) ;
    }

    @Override
    protected View inflaterView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_hotpost, null);
    }

}
