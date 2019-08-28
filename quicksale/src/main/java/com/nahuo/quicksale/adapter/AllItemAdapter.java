package com.nahuo.quicksale.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.TuanPiUtil;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @author 诚
 */
public class AllItemAdapter extends MyBaseAdapter<ShopItemListModel> implements OnClickListener {

    private static int MAX_LINE;
    private static final String TAG_EXPAND = "全文";
    private static final String TAG_COLLAPSE = "收起";
    public FragmentActivity mContext;
    private Resources mResource;
    private DecimalFormat df = new DecimalFormat("#0.00");
    // private Share2WPDialogFragment mShare2WPDialogFragment;
    private Listener mListener;
    //    private int mGridViewWidth;
    private IBuyClickListener mIBuyClickListener;
    private Map<Integer, String> mSignatureMap;
    private Html.ImageGetter imageGetter;
    private int img_size = 100;
    private LinearLayout.LayoutParams mLayoutParams = null;


    public AllItemAdapter(FragmentActivity context) {
        super(context);
        mContext = context;
        mResource = mContext.getResources();
        img_size = mResource.getInteger(R.integer.grid_pic_width_small);
        MAX_LINE = mResource.getInteger(R.integer.content_max_line);

        imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                try {
                    Drawable drawable = null;
                    int rId = Integer.parseInt(source);
                    drawable = mResource.getDrawable(rId);
                    drawable.setBounds(0, 0, 30, 30);
                    return drawable;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }

        };
    }

    public void setSignatures(Map<Integer, String> map) {
        mSignatureMap = map;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        int count = getCount();
        if (count == 0) {
            return convertView;
        }
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_all_items, parent, false);
            holder = new ViewHolder();

            holder.tvBuy = (TextView) convertView.findViewById(R.id.tv_buy);
            holder.tvBuy.setOnClickListener(this);
            holder.tvShare = (TextView) convertView.findViewById(R.id.tv_share);
            holder.tvSignature = (TextView) convertView.findViewById(R.id.tv_signature);
            holder.tvCollect = (TextView) convertView.findViewById(R.id.tv_collect);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.mTvContents = (TextView) convertView.findViewById(R.id.tv_content);
            holder.mTvExpand = (TextView) convertView.findViewById(R.id.tv_expand_content);
//            holder.gridView = (GridView) convertView.findViewById(R.id.gv_pics);
            holder.img_views = (View) convertView.findViewById(R.id.img_views);
            holder.img1 = (ImageView) convertView.findViewById(R.id.img_1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.img_2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.img_3);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.chengtuan_progress);
            holder.tvChengTuanTips = (TextView) convertView.findViewById(R.id.chengtuan_tips);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_supply_price3);
            holder.userName = (TextView) convertView.findViewById(R.id.tv_username);
            holder.content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tvExpand = (TextView) convertView.findViewById(R.id.tv_expand_content);
            holder.supplyPrice = (TextView) convertView.findViewById(R.id.tv_supply_price);
            holder.retailPrice = (TextView) convertView.findViewById(R.id.tv_retail_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mLayoutParams == null) {
            mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    ((WindowManager) mContext.getWindowManager()).getDefaultDisplay().getWidth() / 3);
            mLayoutParams.weight = 1;
        }
        holder.img1.setLayoutParams(mLayoutParams);
        holder.img2.setLayoutParams(mLayoutParams);
        holder.img3.setLayoutParams(mLayoutParams);
        ShopItemListModel listItem = mdata.get(position);
        String intro = listItem.getIntroOrName();
        Spanned introHtml = listItem.getTextHtml(intro, mContext, imageGetter);
        holder.mTvContents.setText(introHtml);
        holder.mTvContents.setTag(intro);
        int chengtuanCount = listItem.getChengTuanCount();
        int dealCount = listItem.getDealCount();

        holder.progress.setMax(chengtuanCount);
        holder.progress.setProgress(dealCount);
        holder.tvChengTuanTips.setText(TuanPiUtil.getChengTuanTips(false, chengtuanCount, dealCount));

        String prettyTime = listItem.getShowTime();
        String d_imgcover = Const.getShopLogo(listItem.getUserid());
        String shopName = listItem.ShopName;
        String[] picsUrl = listItem.getImages();
        holder.userName.setText(shopName);
        holder.tvDate.setText(prettyTime);
        holder.tvPrice.setText(df.format(listItem.getAgentPrice()));

        String userid = listItem.getUserid() + "";
        holder.tvShare.setTag(position);
        holder.tvShare.setOnClickListener(this);
        if (mSignatureMap != null) {
            holder.tvSignature.setText(mSignatureMap.get(listItem.getUserid()));
        }

        holder.mTvContents.post(new Runnable() {
            @Override
            public void run() {
                int lineCount = holder.mTvContents.getLineCount();
                boolean show = lineCount > MAX_LINE;
                holder.mTvExpand.setVisibility(show ? View.VISIBLE : View.GONE);
                holder.mTvExpand.setText(TAG_EXPAND);
                holder.mTvContents.setMaxLines(MAX_LINE);
            }
        });
        holder.mTvExpand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = (String) holder.mTvExpand.getText().toString();
                boolean expand = tag.equals(TAG_EXPAND);
                holder.mTvContents.setMaxLines(expand ? 100 : MAX_LINE);
                holder.mTvExpand.setText(expand ? TAG_COLLAPSE : TAG_EXPAND);
            }
        });

        String iconUrl = ImageUrlExtends.getImageUrl(d_imgcover, Const.LIST_COVER_SIZE);
        Picasso.with(mContext).load(iconUrl).placeholder(R.drawable.empty_photo).into(holder.ivIcon);

        holder.mTvContents.setOnClickListener(new OnItemClickListener(position));
        holder.img1.setOnClickListener(new OnItemClickListener(position));
        holder.img2.setOnClickListener(new OnItemClickListener(position));
        holder.img3.setOnClickListener(new OnItemClickListener(position));
//        populateGridView(holder.gridView, picsUrl);
        if (picsUrl.length > 0) {
            String imgUrl1 = ImageUrlExtends.getImageUrl(picsUrl[0], img_size);
            Picasso.with(mContext).load(imgUrl1).placeholder(R.drawable.empty_photo).into(holder.img1);
        }
        if (picsUrl.length > 1) {
            holder.img2.setVisibility(View.VISIBLE);
            String imgUrl2 = ImageUrlExtends.getImageUrl(picsUrl[1], img_size);
            Picasso.with(mContext).load(imgUrl2).placeholder(R.drawable.empty_photo).into(holder.img2);
        }else{
            holder.img2.setVisibility(View.INVISIBLE);
        }
        if (picsUrl.length > 2) {
            holder.img3.setVisibility(View.VISIBLE);
            String imgUrl3 = ImageUrlExtends.getImageUrl(picsUrl[2], img_size);
            Picasso.with(mContext).load(imgUrl3).placeholder(R.drawable.empty_photo).into(holder.img3);
        }else{
            holder.img3.setVisibility(View.INVISIBLE);
        }

        holder.userName.setOnClickListener(this);
        holder.userName.setTag(R.id.Tag_Position, position);
        holder.ivIcon.setOnClickListener(this);
        holder.ivIcon.setTag(R.id.Tag_Position, position);
        int collectResId = listItem.isFavorite() ? R.drawable.ic_collected : R.drawable.ic_collect;
        holder.tvCollect.setCompoundDrawablesWithIntrinsicBounds(collectResId, 0, 0, 0);
        holder.tvCollect.setOnClickListener(this);
        holder.tvCollect.setTag(R.id.Tag_Position, position);
        holder.userName.setOnClickListener(this);
        holder.tvBuy.setTag(R.id.Tag_Position, position);
        return convertView;

    }

//    private void populateGridView(final GridView gridView, final String[] urls) {
//        final PicGridViewAdapter gridAdapter = new PicGridViewAdapter(mContext);
//
//        gridView.setAdapter(gridAdapter);
//        gridView.setNumColumns(urls.length < 3 ? urls.length : 3);
//        if (mGridViewWidth == 0) {
//            gridView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mGridViewWidth = gridView.getMeasuredWidth();
//                    gridAdapter.setGridViewWidth(gridView.getMeasuredWidth());
//                    gridAdapter.setData(urls);
//                    gridAdapter.notifyDataSetChanged();
//                }
//            });
//        } else {
//            gridAdapter.setGridViewWidth(mGridViewWidth);
//            gridAdapter.setData(urls);
//            gridAdapter.notifyDataSetChanged();
//        }
//
//    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_collect:
                ShopItemListModel favItem = mdata.get((Integer) v.getTag(R.id.Tag_Position));
                if (!favItem.isFavorite()) {
                    mFavListener.onFavoriteClick(favItem);
                }
                break;
            case R.id.tv_share://分享
                ShopItemListModel item = mdata.get((Integer) v.getTag());

                NahuoShare toShare = new NahuoShare(mContext, item);
                toShare.show();
                break;
            case R.id.tv_buy:
                int pos = (Integer) v.getTag(R.id.Tag_Position);
                if (mIBuyClickListener != null) {
                    mIBuyClickListener.buyOnClickListener(mdata.get(pos));
                }
                break;
        }
    }

    public void addCollectedItem(ShopItemListModel collectItem) {
        collectItem.setIsFavorite(true);
        notifyDataSetChanged();
    }


    // 转到wp
    private static class ViewHolder {
        private TextView mTvContents, mTvExpand, tvDate, tvBuy, tvShare, tvSignature, tvCollect, tvChengTuanTips, tvPrice;
        private ImageView ivIcon;
        //        private GridView gridView;
        private ImageView img1, img2, img3;
        private View img_views;
        private ProgressBar progress;

        TextView userName;
        TextView content;
        TextView tvExpand;
        TextView supplyPrice;
        TextView retailPrice;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void remove(int id) {
        for (ShopItemListModel item : mdata) {
            if (item.getID() == id) {
                mdata.remove(item);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void setIBuyClickListener(IBuyClickListener listener) {
        this.mIBuyClickListener = listener;
    }

    public static interface Listener {
        public void onItemClick(ShopItemListModel item);
    }

    private FavoriteListener mFavListener;

    public void setFavoriteListener(FavoriteListener listener) {
        mFavListener = listener;
    }

    public interface FavoriteListener {
        void onFavoriteClick(ShopItemListModel item);
    }

    public interface IBuyClickListener {
        public void buyOnClickListener(ShopItemListModel model);
    }

    private class OnItemClickListener implements OnClickListener {
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

}
