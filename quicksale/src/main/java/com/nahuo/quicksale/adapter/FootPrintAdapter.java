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
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.ViewHolder;
import com.nahuo.quicksale.common.TuanPiUtil;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
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

public class FootPrintAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private Activity activity;
    private int mGridViewWidth;
    private FootPrintAdapter.Listener mListener;
    private FootPrintAdapter.IBuyClickListener mIBuyClickListener;
    private Map<Integer, String> mSignatureMap;
    private int img_size = 100;
    private FragmentActivity mContext;
    private LinearLayout.LayoutParams mLayoutParams = null;
    private Html.ImageGetter imageGetter;
    List<ShopItemListModel> mdata = new ArrayList<>();
    private static int IMAGE_WIDTH;
    private int mChengTuanCount; //场次的成团数
    private int mQsChengTuanCount;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");

    public void setData(List<ShopItemListModel> data) {
        this.mdata = data;
    }

    public void addDataToTail(List<ShopItemListModel> data) {
        this.mdata.addAll(data);
    }

    public FootPrintAdapter(Context context) {
        this.context = context;
        this.activity = (Activity) context;
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
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(IMAGE_WIDTH, ScreenUtils.getProportionHeight(IMAGE_WIDTH));
            ivCover.setLayoutParams(lp);
            TextView tvPinStatus = (TextView) itemView.findViewById(R.id.tv_pin_status);
            ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            ImageView pinStatusIcon = (ImageView) itemView.findViewById(R.id.tv_pin_status_icon);
            TextView Iv_Collection = holder.getView(R.id.Iv_Collection);
            ImageView iv_down_over=(ImageView)itemView.findViewById(R.id.iv_down_over);
            ShopItemListModel item = mdata.get(position);
            ReasonUtls.setItemMargin(position, itemView);
            Iv_Collection.setVisibility(View.GONE);
            Iv_Collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHub.showLightPopDialog(activity, "温馨提示", "您确定要取消收藏吗？", activity.getString(android.R.string.cancel),
                            activity.getString(android.R.string.ok), new LightPopDialog.PopDialogListener() {
                                @Override
                                public void onPopDialogButtonClick(int which) {
                                    //  mListener.onRemoveCollectionsClick(mdata.get(position));

                                }
                            });
                }
            });
            //判断到底是读场次还是商品的成团数量
            int dealCount = item.getDealCount();
            mChengTuanCount = item.getChengTuanCount() > 0 ? item.getChengTuanCount() : mQsChengTuanCount;
            progressBar.setMax(mChengTuanCount);
            progressBar.setProgress(dealCount);
            ivCover.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            pinStatusIcon.setVisibility(View.VISIBLE);
            String picsUrl = item.getCover();
            if (picsUrl != null && picsUrl.length() > 0) {
                String url = ImageUrlExtends.getImageUrl(picsUrl, 15);
//                if (!TextUtils.isEmpty(url)) {
//                    Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivCover);
//                }
                GlideUtls.glideChang(context,url,ivCover);
            }

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
                        iv_down_over.setVisibility(View.GONE);
                    }
                }
            }else {
               pinStatusIcon.setVisibility(View.GONE);
            }
            //tvPrice.setText("￥" +item.getPrice());
            FunctionHelper.formatAgentPrice(context, tvPrice, item.getPrice());
            if (item.getTotalQty() > 0) {
                tvSaleCount.setText("总销量" + item.getTotalQty() + "件");
            } else {
                tvSaleCount.setText("");
            }

            tvContent.setText(item.Title);
            itemView.setOnClickListener(new OnItemClickListener(position));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public void setSignatures(Map<Integer, String> map) {
        mSignatureMap = map;
        notifyDataSetChanged();
    }

    public void setListener(FootPrintAdapter.Listener listener) {
        mListener = listener;
    }

    public void setIBuyClickListener(FootPrintAdapter.IBuyClickListener listener) {
        this.mIBuyClickListener = listener;
    }

    public void remove(ShopItemListModel obj) {
        if (obj == null) {
            return;
        }
        Iterator<ShopItemListModel> it = mdata.iterator();
        while (it.hasNext()) {
            ShopItemListModel item = it.next();
            if (item.getID() == obj.getID() && item.getItemID() == obj.getItemID()) {
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
            ShopItemListModel item = mdata.get(mPos);
            mListener.onItemClick(item);
        }
    }

    public interface Listener {
        void onItemClick(ShopItemListModel item);
        //void onRemoveCollectionsClick(ShopItemListModel item);
    }

    public interface IBuyClickListener {
        public void buyOnClickListener(ShopItemListModel model);
    }
}
