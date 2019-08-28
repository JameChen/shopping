package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.CollectBean;
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.ViewHolder;
import com.nahuo.quicksale.common.TuanPiUtil;
import com.nahuo.quicksale.util.GlideUtls;
import com.nahuo.quicksale.util.ReasonUtls;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jame on 2017/6/1.
 */

public class CollectionAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private Activity activity;
    private int mGridViewWidth;
    private CollectionAdapter.Listener mListener;
    private CollectionAdapter.IBuyClickListener mIBuyClickListener;
    private Map<Integer, String> mSignatureMap;
    private int img_size = 100;
    private FragmentActivity mContext;
    private LinearLayout.LayoutParams mLayoutParams = null;
    private Html.ImageGetter imageGetter;
    List<CollectBean> mdata = new ArrayList<>();
    private static int IMAGE_WIDTH;
    private int mChengTuanCount; //场次的成团数
    private int mQsChengTuanCount;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");

    public void setData(List<CollectBean> data) {
        this.mdata = data;
    }

    public void addDataToTail(List<CollectBean> data) {
        this.mdata.addAll(data);
    }

    public CollectionAdapter(Context context) {
        this.context = context;
        this.activity= (Activity) context;
        IMAGE_WIDTH = (DisplayUtil.getScreenWidth() - ScreenUtils.dip2px(BWApplication.getInstance(),DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.rv_horizontal_spacing))) / 2;
        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                try {
                    Drawable drawable = null;
                    int rId = Integer.parseInt(source);
                    drawable = mContext.getResources().getDrawable(rId);
                    drawable.setBounds(0, 0, 30, 30);
                    return drawable;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(context, parent, R.layout.item_collection);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try {
            View itemView = holder.getConvertView();
            TextView tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            TextView tvSaleCount = (TextView) itemView.findViewById(R.id.tv_saleCount);
            ImageView ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            RelativeLayout rl_iv=(RelativeLayout)itemView.findViewById(R.id.rl_iv);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(IMAGE_WIDTH, ScreenUtils.getProportionHeight(IMAGE_WIDTH));
            ivCover.setLayoutParams(lp);
            rl_iv.setLayoutParams(new LinearLayout.LayoutParams(IMAGE_WIDTH, ScreenUtils.getProportionHeight(IMAGE_WIDTH)));
            TextView tvPinStatus = (TextView) itemView.findViewById(R.id.tv_pin_status);
            ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            ImageView pinStatusIcon = (ImageView) itemView.findViewById(R.id.tv_pin_status_icon);
            TextView Iv_Collection=holder.getView(R.id.Iv_Collection);
            ImageView iv_down_over=(ImageView)itemView.findViewById(R.id.iv_down_over);
            View iv_sale_out=itemView.findViewById(R.id.iv_sale_out);
            CollectBean item = mdata.get(position);
            ReasonUtls.setItemMargin(position, itemView);
            Iv_Collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHub.showLightPopDialog(activity, "温馨提示", "您确定要取消收藏吗？", activity.getString(android.R.string.cancel),
                            activity.getString(android.R.string.ok), new LightPopDialog.PopDialogListener() {
                                @Override
                                public void onPopDialogButtonClick(int which) {
                                    mListener.onRemoveCollectionsClick(mdata.get(position));

                                }
                            });
                }
            });
            String picsUrl = item.getCover();
            if (picsUrl != null && picsUrl.length() > 0) {
                String url = ImageUrlExtends.getImageUrl(picsUrl, 15);
//                if (!TextUtils.isEmpty(url)) {
//                    Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivCover);
//                }
                GlideUtls.glideChang(context,url,ivCover);
            }
            int dealCount = item.getDealCount();
            //判断到底是读场次还是商品的成团数量
            mChengTuanCount = item.getChengTuanCount() > 0 ? item.getChengTuanCount() : mQsChengTuanCount;
            progressBar.setMax(mChengTuanCount);
            progressBar.setProgress(dealCount);
            ivCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            pinStatusIcon.setVisibility(View.VISIBLE);
            if (item.getDisplayStatuID()==2) {
                pinStatusIcon.setVisibility(View.GONE);
                tvPinStatus.setText("等待发起补拼");
            } else {
                if (item.getDisplayStatuID() == 0) {
                    pinStatusIcon.setImageResource(R.drawable.new_icon);
                } else if (item.getDisplayStatuID()==1){
                    pinStatusIcon.setImageResource(R.drawable.bu_icom);
                }
                pinStatusIcon.setVisibility(View.VISIBLE);
                tvPinStatus.setText(TuanPiUtil.getChengTuanTips(false, mChengTuanCount, dealCount));
            }
            if (item!=null){
                if (item.IsShowStatuIcon) {
                    if (item.getDisplayStatuID() == 2) {
                        pinStatusIcon.setVisibility(View.GONE);
                    }else {
                        pinStatusIcon.setVisibility(View.VISIBLE);
                    }
                } else {
                    pinStatusIcon.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(item.getStatu())){
                    if (item.getStatu().equals("已下架")){
                        iv_down_over.setVisibility(View.VISIBLE);
                        tvPinStatus.setText("已下架");
                    }else {
                        if (item.isSaleOut()){
                            iv_sale_out.setVisibility(View.VISIBLE);
                        }else {
                            iv_sale_out.setVisibility(View.GONE);
                        }
                        iv_down_over.setVisibility(View.GONE);
                    }
                }
            }else {
                pinStatusIcon.setVisibility(View.GONE);
            }
           // tvPrice.setText("¥" +item.getPrice());
            FunctionHelper.formatAgentPrice(context, tvPrice, item.getPrice());
            if (item.getTotalQty() > 0) {
                tvSaleCount.setText("总销量" + item.getTotalQty() + "件");
            } else {
                tvSaleCount.setText("");
            }

            tvContent.setText(item.getTitle());
            itemView.setOnClickListener(new OnItemClickListener(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
  /*
        TextView mTvBuy = holder.getView(R.id.tv_buy);
//            holder.mTvBuy.setOnClickListener(this);
        // holder.mTvChat = (TextView) convertView.findViewById(R.id.tv_talk);
        //holder.mTvChat.setOnClickListener(this);
        Button mBtnRemoveCollect = holder.getView(R.id.btn_remove_collection);
        // holder.mGvPics = (GridView) convertView.findViewById(R.id.gv_pics);
        TextView mTvUserName = holder.getView(R.id.tv_username);
        ImageView mIvAvatar = holder.getView(R.id.iv_icon);
        ProgressBar mBar = holder.getView(R.id.chengtuan_progress);
        // holder.mTVcancle = (TextView) convertView.findViewById(R.id.txt_cancle);
        TextView mIvCollection = holder.getView(R.id.Iv_Collection);
        TextView mTvSign = holder.getView(R.id.tv_signature);
        TextView mPrice = holder.getView(R.id.tv_price);
        TextView mBarName = holder.getView(R.id.bar_name);
        TextView mYuepin = holder.getView(R.id.with_ping);
        TextView mTilte = holder.getView(R.id.txt_title);
        View img_views = holder.getView(R.id.img_views);
        ImageView img1 = holder.getView(R.id.img_1);
        ImageView img2 = holder.getView(R.id.img_2);
        ImageView img3 = holder.getView(R.id.img_3);
        TextView mTime = holder.getView(R.id.tv_time);

  *//*      img1.setLayoutParams(mLayoutParams);
        img2.setLayoutParams(mLayoutParams);
        img3.setLayoutParams(mLayoutParams);*//*
//        holder.img1.setOnClickListener(new OnItemClickListener(position));
//        holder.img2.setOnClickListener(new OnItemClickListener(position));
//        holder.img3.setOnClickListener(new OnItemClickListener(position));
        holder.getConvertView().setOnClickListener(new OnItemClickListener(position));
        final ShopItemListModel listItem = mdata.get(position);
//        String userName = listItem.getUserName();
        String shopName = listItem.ShopName;
        String[] picsUrl = listItem.getImages();
        mTvUserName.setText(shopName);


        String shopLogo = Const.getShopLogo(listItem.getUserid());
        String iconUrl = ImageUrlExtends.getImageUrl(shopLogo, Const.LIST_COVER_SIZE);
        Picasso.with(mContext).load(iconUrl).placeholder(R.drawable.empty_photo).into(mIvAvatar);

        if (picsUrl.length > 0) {
            String imgUrl1 = ImageUrlExtends.getImageUrl(picsUrl[0], img_size);
            Picasso.with(mContext).load(imgUrl1).placeholder(R.drawable.empty_photo).into(img1);
        }
        if (picsUrl.length > 1) {
            img2.setVisibility(View.VISIBLE);
            String imgUrl2 = ImageUrlExtends.getImageUrl(picsUrl[1], img_size);
            Picasso.with(mContext).load(imgUrl2).placeholder(R.drawable.empty_photo).into(img2);
        } else {
            img2.setVisibility(View.INVISIBLE);
        }
        if (picsUrl.length > 2) {
            img3.setVisibility(View.VISIBLE);
            String imgUrl3 = ImageUrlExtends.getImageUrl(picsUrl[2], img_size);
            Picasso.with(mContext).load(imgUrl3).placeholder(R.drawable.empty_photo).into(img3);
        } else {
            img3.setVisibility(View.INVISIBLE);
        }

        mYuepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NahuoShare toShare = new NahuoShare(mContext, listItem);
                toShare.show();
            }
        });


        //先默认给了3个 imageview
        //     populateGridView(holder.mGvPics, stockArr);
        mPrice.setText("¥" + new java.text.DecimalFormat("#.00").format(listItem.getAgentPrice()));
        mBar.setProgress(listItem.getDealCount());
        //mBtnRemoveCollect.setOnClickListener(this);
        mBtnRemoveCollect.setTag(R.id.Tag_Position, position);
        //mIvCollection.setOnClickListener(this);
        mIvCollection.setTag(R.id.Tag_Position, position);
        String userid = listItem.getUserid() + "";
        mTvBuy.setTag(R.id.Tag_Position, position);
        boolean isStart = listItem.isStart();
        //mTvBuy.setOnClickListener(isStart ? this : null);
        mTvBuy.setBackgroundResource(isStart ? R.drawable.btn_red : R.color.btn_bg_gray);
        String intro = listItem.getIntroOrName();
        Spanned introHtml = listItem.getTextHtml(intro, mContext, imageGetter);
        mTime.setText(listItem.getShowTime());
        mTilte.setText(listItem.getName());

        mBarName.setText(TuanPiUtil.getChengTuanTips(listItem.isStart(), listItem.getChengTuanCount(), listItem.getDealCount()));
        mBar.setMax(listItem.getChengTuanCount());
        if (mSignatureMap != null) {
            mTvSign.setText(mSignatureMap.get(listItem.getUserid()));
        }*/
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setSignatures(Map<Integer, String> map) {
        mSignatureMap = map;
        notifyDataSetChanged();
    }

    public void setListener(CollectionAdapter.Listener listener) {
        mListener = listener;
    }

    public void setIBuyClickListener(CollectionAdapter.IBuyClickListener listener) {
        this.mIBuyClickListener = listener;
    }

    public void remove(CollectBean obj) {
        if (obj == null) {
            return;
        }
        Iterator<CollectBean> it = mdata.iterator();
        while (it.hasNext()) {
            CollectBean item = it.next();
            if (item.getID() == obj.getID()) {
                it.remove();
                notifyDataSetChanged();
                break;
            }
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPos;

        public OnItemClickListener(int pos) {
            mPos = pos;
        }

        @Override
        public void onClick(View v) {
            CollectBean item = mdata.get(mPos);
            mListener.onItemClick(item);
        }
    }

    public interface Listener {
        void onItemClick(CollectBean item);
        void onRemoveCollectionsClick(CollectBean item);
    }

    public interface IBuyClickListener {
        public void buyOnClickListener(CollectBean model);
    }
}
