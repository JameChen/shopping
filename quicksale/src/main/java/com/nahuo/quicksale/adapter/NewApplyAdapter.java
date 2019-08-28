package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.ApplyItem;
import com.nahuo.quicksale.oldermodel.ApplyItem.HandleStateCode;
import com.squareup.picasso.Picasso;

public class NewApplyAdapter extends MyBaseAdapter<ApplyItem> {

    private Listener mListener;

    public NewApplyAdapter(Context context) {
        super(context);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        public void onAcceptClick(ApplyItem item);

        public void onIgnoreClick(ApplyItem item);
    }

    /**
     * Description:设置 申请条目的处理状态
     * 
     * @param handleStateCode
     * @see {@link HandleStateCode} 2014-7-12 下午6:38:49
     */
    public void setHandleStateCode(int userId, int handleStateCode) {
        if (mdata == null || mdata.size() == 0) {
            return;
        } else {
            for (ApplyItem applyItem : mdata) {
                if (applyItem.getUserId() == userId) {
                    applyItem.setHandleStateCode(handleStateCode);
                    return;
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ApplyItem item = mdata.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_new_apply, parent, false);
            holder = new ViewHolder();

            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.userName = (TextView) convertView.findViewById(R.id.user_name);
            holder.ignoreBtn = convertView.findViewById(R.id.btn_ignore);
            holder.acceptBtn = convertView.findViewById(R.id.btn_accept);
            
            holder.applyResult = (TextView) convertView.findViewById(R.id.apply_result);
            holder.btnLayout = convertView.findViewById(R.id.layout_btns);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(Const.getShopLogo(item.getUserId())).placeholder(R.drawable.empty_photo).into(holder.icon);
        holder.userName.setText(item.getName());
        holder.ignoreBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onIgnoreClick(item);
                }
            }
        });
        holder.acceptBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAcceptClick(item);
                }
            }
        });
        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, UserInfoActivity.class);
//                intent.putExtra(UserInfoActivity.EXTRA_USER_ID, item.getUserId());
//                mContext.startActivity(intent);
            }
        });
        switch (item.getHandleStateCode()) {
        case ApplyItem.HandleStateCode.APPLY:
            holder.btnLayout.setVisibility(View.VISIBLE);
            holder.applyResult.setVisibility(View.GONE);
            break;
        case ApplyItem.HandleStateCode.AGREE:
            holder.btnLayout.setVisibility(View.GONE);
            holder.applyResult.setVisibility(View.VISIBLE);
            holder.applyResult.setText("已同意");
            break;
        case ApplyItem.HandleStateCode.REJECT:
            holder.btnLayout.setVisibility(View.GONE);
            holder.applyResult.setVisibility(View.VISIBLE);
            holder.applyResult.setText("已拒绝");
            break;
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView icon;
        TextView userName;
        View btnLayout;
        View ignoreBtn;
        View acceptBtn;
        TextView applyResult;
    }

}
