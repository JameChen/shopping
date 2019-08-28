package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.bean.StallsAllListBean;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.squareup.picasso.Picasso;

/**
 * Created by jame on 2017/6/20.
 */

public class MarketsAdapter extends MyBaseAdapter<StallsAllListBean.ShopListBean> implements View.OnClickListener {
    public MarketsAdapter(Context context) {
        super(context);
    }

    @Override
    public void onClick(View v) {
        try {
            StallsAllListBean.ShopListBean bean = (StallsAllListBean.ShopListBean) v.getTag(R.id.item);
            PinHuoModel model1 = new PinHuoModel();
            model1.setShopID(bean.getShopID());
            model1.setQsID(bean.getQsID());
            model1.setUrl(bean.getUrl());
            model1.setName(bean.getShopName());
            model1.setActivityType("");
            StallsAllListBean.ShopListBean.VisitResultBean visitResultBean = bean.getVisitResult();
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
            // long isStartDuration = model1.getStartMillis() - System.currentTimeMillis();
//        if (model1.isStart) {
//            ViewUtil.gotoChangci(mContext, model1);
//        } else {
            //if (isStartDuration > 0) {//预告
            if (model1.QsID != 0) {
                ViewUtil.gotoMarketChangci(mContext, model1);
            } else {
                ViewHub.showShortToast(mContext, "没有拼货场次");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//                else {
//                    if (model1.Url.indexOf("/xiaozu/topic/") > 1) {
//                        String temp = "/xiaozu/topic/";
//                        int topicID = Integer.parseInt(model1.Url.substring(model1.Url.indexOf(temp) + temp.length()));
//
//                        Intent intent = new Intent(mContext, PostDetailActivity.class);
//                        intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                        mContext.startActivity(intent);
//                    } else if (model1.Url.indexOf("/xiaozu/act/") > 1) {
//                        String temp = "/xiaozu/act/";
//                        int actID = Integer.parseInt(model1.Url.substring(model1.Url.indexOf(temp) + temp.length()));
//
//                        Intent intent = new Intent(mContext, PostDetailActivity.class);
//                        intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                        mContext.startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(mContext, ItemPreview1Activity.class);
//                        intent.putExtra("name", "拼货预告");
//                        intent.putExtra("url", model1.Url);
//                        mContext.startActivity(intent);
//                    }
//                }

//            } else {
//                ViewUtil.gotoChangci(mContext, model1);
//            }
        //   }
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

        StallsAllListBean.ShopListBean bean = mdata.get(position);
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
        } else {
            holder.tv_right.setVisibility(View.GONE);
        }
        convertView.setTag(R.id.item, bean);
        convertView.setOnClickListener(this);

        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_pic;
        TextView tv_title, tv_content, tv_right;
    }

}
