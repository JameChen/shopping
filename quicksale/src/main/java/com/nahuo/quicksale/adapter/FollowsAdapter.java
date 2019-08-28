package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.bean.FollowsBean;
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.squareup.picasso.Picasso;

/**
 * Created by jame on 2017/6/22.
 */

public class FollowsAdapter extends MyBaseAdapter<FollowsBean> implements View.OnClickListener ,View.OnLongClickListener{
    Activity activity;
    RemoveListener mListener;
    public void setListener(FollowsAdapter.RemoveListener listener) {
        mListener = listener;
    }

    public FollowsAdapter(Activity context) {
        super(context);
        this.activity=context;
    }

    @Override
    public void onClick(View v) {

        try {
            FollowsBean bean = (FollowsBean) v.getTag(R.id.item);
            PinHuoModel model1 = new PinHuoModel();
            model1.setShopID(bean.getShopID());
            model1.setQsID(bean.getQsID());
            model1.setUrl(bean.getUrl());
            model1.setName(bean.getShopName());
            model1.setActivityType("");
            FollowsBean.VisitResultBean visitResultBean = bean.getVisitResult();
            if (visitResultBean != null) {
                PinHuoModel.VisitResultModel visitResultModel = new PinHuoModel.VisitResultModel();
                visitResultModel.setCanVisit(visitResultBean.isCanVisit());
                visitResultModel.setMessage(visitResultBean.getMessage());
                visitResultModel.setResultType(visitResultBean.getResultType());
                model1.setVisitResult(visitResultModel);
            }
            PinHuoModel.OpenStatuBean openStatuBean = new PinHuoModel.OpenStatuBean();
            openStatuBean.setStatu(bean.getStatu());
            if (bean.getStatu().equals("开拼中")||bean.getStatu().equals("预告")){
                model1.IsStart=true;
                model1.setActivityType("拼货");
            }else {
                model1.IsStart=false;
            }
            model1.setOpenStatu(openStatuBean);
            if (model1.QsID != 0) {
                ViewUtil.gotoMarketChangci(mContext, model1);
            } else {
                ViewHub.showShortToast(mContext, "没有拼货场次");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void removeFollow(FollowsBean bean){
        if (!ListUtils.isEmpty(mdata)){
            for (FollowsBean xbean:mdata) {
                if (xbean.getShopID()==bean.getShopID()){
                    mdata.remove(xbean);
                    break;
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_market, parent, false);
            holder = new ViewHolder();
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_right = (TextView) convertView.findViewById(R.id.tv_right);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            FollowsBean bean = mdata.get(position);
            String shopLogo = Const.getStallLogo(bean.getShopID());
            Picasso.with(mContext).load(shopLogo).placeholder(R.drawable.empty_photo).into(holder.iv_pic);
            String shopName = bean.getShopName();
            String stallName = bean.getStallName();
            String content = bean.getContent();
            String statu = bean.getStatu();
            if (!TextUtils.isEmpty(shopName)) {
                holder.tv_title.setText(shopName);
            }
            if (!TextUtils.isEmpty(stallName)) {
                holder.tv_content.setText(stallName);
            }
            if (statu.equals("预告")) {
                holder.tv_right.setBackgroundResource(R.drawable.bg_rectangle_green_m);
            } else if (statu.equals("开拼中")) {
                holder.tv_right.setBackgroundResource(R.drawable.bg_rectangle_red_m);

            } else if (statu.equals("已结束")) {
                holder.tv_right.setBackgroundResource(R.drawable.bg_rectangle_gray_m);
            }
            if (!TextUtils.isEmpty(content)) {
                holder.tv_right.setVisibility(View.VISIBLE);
                holder.tv_right.setText(content);
            }else {
                holder.tv_right.setVisibility(View.GONE);

            }
            convertView.setTag(R.id.item, bean);
            convertView.setOnClickListener(this);
            convertView.setOnLongClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean onLongClick(final View v) {
        ViewHub.showLightPopDialog(activity, "温馨提示", "您确定要取消关注吗？",activity.getString(android.R.string.cancel),
                activity.getString(android.R.string.ok), new LightPopDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        FollowsBean bean = (FollowsBean) v.getTag(R.id.item);
                        if (mListener!=null)
                            mListener.onRemoveFollowLongClick(bean);
                    }
                });
        return false;
    }
    public interface RemoveListener {
        void onRemoveFollowLongClick(FollowsBean item);
    }
    private static class ViewHolder {
        ImageView iv_pic;
        TextView tv_title, tv_content, tv_right;
    }

}
