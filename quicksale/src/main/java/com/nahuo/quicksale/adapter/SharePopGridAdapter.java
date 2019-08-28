package com.nahuo.quicksale.adapter;

import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.NahuoShare;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SharePopGridAdapter extends MyBaseAdapter<Integer> {

    public SharePopGridAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv = null;
        if (convertView == null) {
            tv = new TextView(mContext);
            convertView = tv;
            convertView.setTag(tv);
        } else {
            tv = (TextView) convertView.getTag();
        }
        tv.setGravity(Gravity.CENTER);
        tv.setCompoundDrawablePadding(DisplayUtil.dip2px(mContext, 5));
        int platform = mdata.get(position);
        switch (platform) {
        case NahuoShare.PLATFORM_QQ_FRIEND:
            tv.setId(NahuoShare.PLATFORM_QQ_FRIEND);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_qq, 0, 0);
            tv.setText("QQ好友");
            break;
        case NahuoShare.PLATFORM_QZONE:
            tv.setId(NahuoShare.PLATFORM_QZONE);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_qzone, 0, 0);
            tv.setText("QQ空间");
            break;
        case NahuoShare.PLATFORM_WX_CIRCLE:
            tv.setId(NahuoShare.PLATFORM_WX_CIRCLE);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_wx_moment, 0, 0);
            tv.setText("微信朋友圈");
            break;
        case NahuoShare.PLATFORM_WX_FRIEND:
            tv.setId(NahuoShare.PLATFORM_WX_FRIEND);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_wx_frd, 0, 0);
            tv.setText("微信朋友");
            break;
//        case NahuoShare.PLATFORM_SINA_WEIBO:
//            tv.setId(NahuoShare.PLATFORM_SINA_WEIBO);
//            tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_wb, 0, 0);
//            tv.setText("新浪微博");
//            break;
        case NahuoShare.PLATFORM_COPY_LINK: 
            tv.setId(NahuoShare.PLATFORM_COPY_LINK);
            tv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.share_copy_url, 0, 0);
            tv.setText("复制链接");
            break;
        }
        return convertView;
    }

}
