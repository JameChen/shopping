package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.constant.UpYunIcon;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.TuanPiUtil;
import com.nahuo.quicksale.controls.RectangleTextSpan;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.util.GlideUtls;
import com.nahuo.quicksale.util.ReasonUtls;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ZZB on 2015/10/16.
 */
public class PinHuoDetailAdapter extends BaseRecyclerAdapter<ShopItemListModel> implements View.OnClickListener {
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");
    private static int IMAGE_WIDTH;
    private Context mContext;
    private Listener mListener;
    private int mChengTuanCount; //场次的成团数
    private boolean isOvered;
    private int mQsChengTuanCount;
    public String part2title = "往期好货：";
    public boolean hasHead = false;
    public boolean ShowCoinPayIcon;
    public boolean isChangShi = false;
    public static int TYPE_SEARCH = 1;
    public int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setChangShi(boolean changShi) {
        isChangShi = changShi;
    }

    public void setShowCoinPayIcon(boolean showCoinPayIcon) {
        ShowCoinPayIcon = showCoinPayIcon;
    }

    public boolean hasEmtyId = false;

    public void setHasEmtyId(boolean hasEmtyId) {
        this.hasEmtyId = hasEmtyId;
    }

    public void setHasHead(boolean hasHead) {
        this.hasHead = hasHead;
    }

    //int pos=-1;
    public interface Listener {
        void onItemClick(ShopItemListModel item);
    }

    public PinHuoDetailAdapter() {
        IMAGE_WIDTH = (DisplayUtil.getScreenWidth() - ScreenUtils.dip2px(BWApplication.getInstance(), DisplayUtil.getResDimen(BWApplication.getInstance(), R.dimen.rv_horizontal_spacing))) / 2;
    }

    public void setChengTuanCount(int chengTuanCount) {
        mQsChengTuanCount = chengTuanCount;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setOvered(boolean overed) {
        isOvered = overed;
    }

    int passItemMinIndex = 10000;

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder holder, final int position) {
        View itemView = holder.itemView;
        // this.pos=position;
        mContext = itemView.getContext();
        FrameLayout layout = (FrameLayout) itemView.findViewById(R.id.layout);
        TextView tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        TextView tvSaleCount = (TextView) itemView.findViewById(R.id.tv_saleCount);
        TextView tv_ori_price = (TextView) itemView.findViewById(R.id.tv_ori_price);
        tv_ori_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        ImageView ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
        TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        RelativeLayout rl_content = (RelativeLayout) itemView.findViewById(R.id.rl_content);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(IMAGE_WIDTH, ScreenUtils.getProportionHeight(IMAGE_WIDTH));
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(IMAGE_WIDTH, ScreenUtils.getProportionHeight(IMAGE_WIDTH));
        ivCover.setLayoutParams(lp);
        rl_content.setLayoutParams(llp);
        TextView tvPinStatus = (TextView) itemView.findViewById(R.id.tv_pin_status);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
        ImageView pinStatusIcon = (ImageView) itemView.findViewById(R.id.tv_pin_status_icon);
        ImageView video_icon = (ImageView) itemView.findViewById(R.id.video_icon);
        ImageView iv_down_over = (ImageView) itemView.findViewById(R.id.iv_down_over);
        ImageView iv_coin_pay_icon = (ImageView) itemView.findViewById(R.id.iv_coin_pay_icon);
        View iv_sale_out = itemView.findViewById(R.id.iv_sale_out);

        ShopItemListModel item = mData.get(position);
        if (type == TYPE_SEARCH) {
            if (item.isShowCoinPayIcon()) {
                iv_coin_pay_icon.setVisibility(View.VISIBLE);
                GlideUtls.glidePic(mContext, UpYunIcon.ICON_LIST, iv_coin_pay_icon);
            } else {
                iv_coin_pay_icon.setVisibility(View.GONE);
            }
        } else {
            if (ShowCoinPayIcon) {
                iv_coin_pay_icon.setVisibility(View.VISIBLE);
                GlideUtls.glidePic(mContext, UpYunIcon.ICON_LIST, iv_coin_pay_icon);
            } else {
                iv_coin_pay_icon.setVisibility(View.GONE);
            }
        }
        if (item != null) {
            // hasEmtyId = judeDataHasEmtpyId();
//            Log.e("uuu", "item.isPassItem=" + item.isPassItem +
//                    "content=" + item.Title + "pos=" + position + "numsIsEr=" + hasEmtyId);
            if (isChangShi) {
                ReasonUtls.setReasonChangShiItemMargin(position, itemView);
            } else {
                ReasonUtls.setReasonItemMargin(position, itemView, item.isPassItem, hasEmtyId);
            }
            ivCover.setVisibility(View.VISIBLE);
            if (item.HasVideo) {
                video_icon.setVisibility(View.VISIBLE);
            } else {
                video_icon.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getStatu())) {
                if (item.getStatu().equals("已下架")) {
                    iv_down_over.setVisibility(View.VISIBLE);
                } else {
                    if (item.isSaleOut()) {
                        iv_sale_out.setVisibility(View.VISIBLE);
                    } else {
                        iv_sale_out.setVisibility(View.GONE);
                    }
                    iv_down_over.setVisibility(View.GONE);
                }
            }
        }
        if (item.getID() == -1) {
            tvPrice.setText("");
            tvSaleCount.setText("");
            tvContent.setText("");
            tvPinStatus.setText("");
            ivCover.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            pinStatusIcon.setVisibility(View.INVISIBLE);
            iv_coin_pay_icon.setVisibility(View.GONE);
            itemView.setEnabled(false);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        pinStatusIcon.setVisibility(View.VISIBLE);

//        String[] picsUrl = item.getImages();
        String picsUrl = item.getCover();
        if (picsUrl != null && picsUrl.length() > 0) {
            String url = ImageUrlExtends.getImageUrl(picsUrl, 15);
//            if (!TextUtils.isEmpty(url)) {
//
//                Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivCover);
//            }
            GlideUtls.glideChang(mContext, url, ivCover);
        }

        ivCover.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        pinStatusIcon.setVisibility(View.VISIBLE);
        //itemView.findViewById(R.id.recycler_view1_txt).setVisibility(View.GONE);
        if (passItemMinIndex == position) {
//            if (passItemMinIndex%2 == 1) {
//                tvPrice.setText("");
//                tvSaleCount.setText("");
//                tvContent.setText("");
//                tvPinStatus.setText("");
//                ivCover.setVisibility(View.GONE);
//                progressBar.setVisibility(View.GONE);
//                pinStatusIcon.setVisibility(View.GONE);
//
//                passItemMinIndex++;
//            } else {

//            itemView.findViewById(R.id.recycler_view1_txt).setVisibility(View.VISIBLE);
//            ((TextView) itemView.findViewById(R.id.recycler_view1_txt)).setText(part2title);


//            }
        }
        if (passItemMinIndex + 1 == position) {
            //itemView.findViewById(R.id.recycler_view1_txt).setVisibility(View.VISIBLE);
            //((TextView) itemView.findViewById(R.id.recycler_view1_txt)).setText("");
        }
        int dealCount = item.getDealCount();
        //判断到底是读场次还是商品的成团数量
        mChengTuanCount = item.getChengTuanCount() > 0 ? item.getChengTuanCount() : mQsChengTuanCount;
        progressBar.setMax(mChengTuanCount);
        progressBar.setProgress(dealCount);
        ivCover.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        pinStatusIcon.setVisibility(View.VISIBLE);
        if (item.getDisplayStatuID() == 2) {
            pinStatusIcon.setVisibility(View.GONE);
            tvPinStatus.setText("等待发起补拼");
        } else {
            if (item.getDisplayStatuID() == 0) {
                pinStatusIcon.setImageResource(R.drawable.new_icon);
            } else if (item.getDisplayStatuID() == 1) {
                pinStatusIcon.setImageResource(R.drawable.bu_icom);
            }
            pinStatusIcon.setVisibility(View.VISIBLE);
            tvPinStatus.setText(TuanPiUtil.getChengTuanTips(false, mChengTuanCount, dealCount));
        }
        if (item != null) {
            if (item.IsShowStatuIcon) {
                if (item.getDisplayStatuID() == 2) {
                    pinStatusIcon.setVisibility(View.GONE);
                }else {
                    pinStatusIcon.setVisibility(View.VISIBLE);
                }
            } else {
                pinStatusIcon.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getStatu())) {
                if (item.getStatu().equals("已下架")) {
                    iv_down_over.setVisibility(View.VISIBLE);
                    tvPinStatus.setText("已下架");
                } else {
                    if (item.isSaleOut()) {
                        iv_sale_out.setVisibility(View.VISIBLE);
                    } else {
                        iv_sale_out.setVisibility(View.GONE);
                    }
                    iv_down_over.setVisibility(View.GONE);
                }
            }
        } else {
            pinStatusIcon.setVisibility(View.GONE);
        }
       FunctionHelper.formatAgentPrice(mContext, tvPrice, item.getPrice());
        if (TextUtils.isEmpty(item.getDiscount())) {
            tv_ori_price.setVisibility(View.GONE);
            tvSaleCount.setVisibility(View.VISIBLE);
        } else {
            tv_ori_price.setVisibility(View.VISIBLE);
            tvSaleCount.setVisibility(View.GONE);
        }
        FunctionHelper.formatRightPrice(mContext,tv_ori_price,item.getOriPrice());
        if (TextUtils.isEmpty(item.getDiscount())) {
            tvContent.setText(item.Title);
        } else {
            formatTitle(mContext, tvContent, item.Title, item.getDiscount());
        }
        if (item.getTotalQty() > 0) {
            tvSaleCount.setText("总销量" + item.getTotalQty() + "件");
        } else {
            tvSaleCount.setText("");
        }

//        if (item.isPassItem) {
//
//            pinStatusIcon.setVisibility(View.GONE);
//            progressBar.setVisibility(View.GONE);
//            tvPinStatus.setVisibility(View.GONE);
//        } else {
//            pinStatusIcon.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.VISIBLE);
//            tvPinStatus.setVisibility(View.VISIBLE);
////        int chengtuanCount = item.getChengTuanCount();
//            int dealCount = item.getDealCount();
//            //判断到底是读场次还是商品的成团数量
//            mChengTuanCount = item.getChengTuanCount() > 0 ? item.getChengTuanCount() : mQsChengTuanCount;
//
//            progressBar.setMax(mChengTuanCount);
//            progressBar.setProgress(dealCount);
//            tvPinStatus.setText(TuanPiUtil.getChengTuanTips(false, mChengTuanCount, dealCount));
//        }
//        if (item.getDisplayStatuID() == 0) {
//            pinStatusIcon.setImageResource(R.drawable.new_icon);
//        } else {
//            pinStatusIcon.setImageResource(R.drawable.bu_icom);
//        }
//        tvPrice.setText("¥" + PRICE_FORMAT.format(item.getPrice()));
//        if (item.getTotalQty() > 0) {
//            tvSaleCount.setText("总销量" + item.getTotalQty() + "件");
//        } else {
//            tvSaleCount.setText("");
//        }
//        tvContent.setText(item.Title);
        itemView.setOnClickListener(this);
        itemView.setTag(item);

    }

    private void formatAgentPrice(Context context, TextView tvPrice, double retailPrice) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String retailPriceStr = "¥" + df.format(retailPrice);
        //int dotPos = retailPriceStr.indexOf(".");
        SpannableString spRetailPrice = new SpannableString(retailPriceStr);
        // 设置字体大小（相对值,单位：像素） 参数表示为默认字体大小的多少倍 //0.5f表示默认字体大小的一半
        spRetailPrice.setSpan(new RelativeSizeSpan(1.7f), 1, retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置字体前景色
        spRetailPrice.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.pin_huo_red)), 0,
                retailPriceStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvPrice.setText(spRetailPrice);
    }

    private void formatTitle(Context context, TextView tvTittle, String name, String discount) {
        SpannableString spannableString = new SpannableString(" " + name);
        RectangleTextSpan topSpan = new RectangleTextSpan(context, R.color.pin_huo_red, R.color.pin_huo_red, discount);
        spannableString.setSpan(topSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTittle.setText(spannableString);
    }

    List<Integer> ids = new ArrayList<>();

    public void setPassItemPosition1() {
        ids.clear();
        if (this.getData().size() > 0) {
            for (int i = 0; i < getData().size(); i++) {
                ids.add(getData().get(i).getID());
            }
            if (!ids.contains(-1)) {
                ShopItemListModel emptyModel = new ShopItemListModel();
                emptyModel.setID(-1);
                List<ShopItemListModel> l = new ArrayList<>();
                l.add(emptyModel);
                this.addDataToTailx(l);
            }
        } else {
            ShopItemListModel emptyModel = new ShopItemListModel();
            emptyModel.setID(-1);
            List<ShopItemListModel> l = new ArrayList<>();
            l.add(emptyModel);
            this.addDataToTailx(l);
        }
        passItemMinIndex = this.getData().size();

    }

    public void setPassItemPosition() {
        ShopItemListModel emptyModel = new ShopItemListModel();
        emptyModel.setID(-1);
        List<ShopItemListModel> l = new ArrayList<>();
        l.add(emptyModel);
        this.addDataToTail(l);
//        if (this.getData().size() % 2 == 1) {
//            ShopItemListModel emptyModel = new ShopItemListModel();
//            emptyModel.setID(-1);
//            List<ShopItemListModel> l = new ArrayList<>();
//            l.add(emptyModel);
//            this.addDataToTail(l);
//        }
        passItemMinIndex = this.getData().size();

    }

    public void addFirstPassItem(List<ShopItemListModel> data) {
        List<ShopItemListModel> list = new ArrayList<>();
        ShopItemListModel emptyModel = new ShopItemListModel();
        emptyModel.setID(-1);
        list.add(emptyModel);
        for (ShopItemListModel d : data) {
            d.isPassItem = true;
            list.add(d);
        }
        if (this.getData().size() == 0) {
            this.setData(list);
        } else {
            this.addDataToTail(list);
        }
        this.notifyDataSetChanged();
    }

    public void addPassItem(List<ShopItemListModel> data) {
        for (ShopItemListModel d : data) {
            d.isPassItem = true;
        }
        if (this.getData().size() == 0) {
            this.setData(data);
        } else {
            this.addDataToTail(data);
        }
        this.notifyDataSetChanged();
    }

    List<ShopItemListModel> vlist = new ArrayList<>();

    public int getNewItemCount() {
        vlist.clear();
        if (this.getData().size() > 0) {
            for (int i = 0; i < getData().size(); i++) {
                if (getData().get(i).isPassItem = false) {
                    vlist.add(getData().get(i));
                }
            }

        }
        return vlist.size();
    }

    @Override
    protected int getItemViewLayoutId() {
        return R.layout.lvitem_pin_huo_detail1;
    }


    @Override
    public void onClick(View v) {
        ShopItemListModel item = (ShopItemListModel) v.getTag();
        mListener.onItemClick(item);
    }

}
