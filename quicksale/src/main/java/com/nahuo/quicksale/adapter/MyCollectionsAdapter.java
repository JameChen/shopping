package com.nahuo.quicksale.adapter;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
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

import java.util.Iterator;
import java.util.Map;

/**
 * Created by ZZB on 2015/9/21.
 */
public class MyCollectionsAdapter extends MyBaseAdapter<ShopItemListModel> implements OnClickListener {
    //    private static int MAX_LINE;
//    private static final String TAG_EXPAND = "全文";
//    private static final String TAG_COLLAPSE = "收起";
    private int mGridViewWidth;
    private Listener mListener;
    private IBuyClickListener mIBuyClickListener;
    private Map<Integer, String> mSignatureMap;
    private int img_size = 100;
    private FragmentActivity mContext;
    private LinearLayout.LayoutParams mLayoutParams = null;
    private Html.ImageGetter imageGetter;

    public MyCollectionsAdapter(FragmentActivity context) {
        super(context);
//        MAX_LINE = context.getResources().getInteger(R.integer.content_max_line);
        mContext = context;

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


    public void setSignatures(Map<Integer, String> map) {
        mSignatureMap = map;
        notifyDataSetChanged();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void setIBuyClickListener(IBuyClickListener listener) {
        this.mIBuyClickListener = listener;
    }

    @Override
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lvitem_my_collection, parent, false);
            holder = new ViewHolder();
            holder.mTvBuy = (TextView) convertView.findViewById(R.id.tv_buy);
//            holder.mTvBuy.setOnClickListener(this);
            // holder.mTvChat = (TextView) convertView.findViewById(R.id.tv_talk);
            //holder.mTvChat.setOnClickListener(this);
            holder.mBtnRemoveCollect = (Button) convertView.findViewById(R.id.btn_remove_collection);
            // holder.mGvPics = (GridView) convertView.findViewById(R.id.gv_pics);
            holder.mTvUserName = (TextView) convertView.findViewById(R.id.tv_username);
            holder.mIvAvatar = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.mBar = (ProgressBar) convertView.findViewById(R.id.chengtuan_progress);
            // holder.mTVcancle = (TextView) convertView.findViewById(R.id.txt_cancle);
            holder.mIvCollection = (TextView) convertView.findViewById(R.id.Iv_Collection);
            holder.mTvSign = (TextView) convertView.findViewById(R.id.tv_signature);
            holder.mPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.mBarName = (TextView) convertView.findViewById(R.id.bar_name);
            holder.mYuepin = (TextView) convertView.findViewById(R.id.with_ping);
            holder.mTilte = (TextView) convertView.findViewById(R.id.txt_title);

            holder.img_views = (View) convertView.findViewById(R.id.img_views);
            holder.img1 = (ImageView) convertView.findViewById(R.id.img_1);
            holder.img2 = (ImageView) convertView.findViewById(R.id.img_2);
            holder.img3 = (ImageView) convertView.findViewById(R.id.img_3);


            holder.mTime = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mLayoutParams == null) {
//            mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                    ((WindowManager) mContext.getWindowManager()).getDefaultDisplay().getWidth() / 3);
            Display display = mContext.getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
            Point size = new Point();
            display.getSize(size);
            mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, size.x / 3);

            mLayoutParams.weight = 1;
        }


        holder.img1.setLayoutParams(mLayoutParams);
        holder.img2.setLayoutParams(mLayoutParams);
        holder.img3.setLayoutParams(mLayoutParams);
//        holder.img1.setOnClickListener(new OnItemClickListener(position));
//        holder.img2.setOnClickListener(new OnItemClickListener(position));
//        holder.img3.setOnClickListener(new OnItemClickListener(position));
        convertView.setOnClickListener(new OnItemClickListener(position));
        final ShopItemListModel listItem = mdata.get(position);
//        String userName = listItem.getUserName();
        String shopName = listItem.ShopName;
        String[] picsUrl = listItem.getImages();
        holder.mTvUserName.setText(shopName);


        String shopLogo = Const.getShopLogo(listItem.getUserid());
        String iconUrl = ImageUrlExtends.getImageUrl(shopLogo, Const.LIST_COVER_SIZE);
        Picasso.with(mContext).load(iconUrl).placeholder(R.drawable.empty_photo).into(holder.mIvAvatar);

        if (picsUrl.length > 0) {
            String imgUrl1 = ImageUrlExtends.getImageUrl(picsUrl[0], img_size);
            Picasso.with(mContext).load(imgUrl1).placeholder(R.drawable.empty_photo).into(holder.img1);
        }
        if (picsUrl.length > 1) {
            holder.img2.setVisibility(View.VISIBLE);
            String imgUrl2 = ImageUrlExtends.getImageUrl(picsUrl[1], img_size);
            Picasso.with(mContext).load(imgUrl2).placeholder(R.drawable.empty_photo).into(holder.img2);
        } else {
            holder.img2.setVisibility(View.INVISIBLE);
        }
        if (picsUrl.length > 2) {
            holder.img3.setVisibility(View.VISIBLE);
            String imgUrl3 = ImageUrlExtends.getImageUrl(picsUrl[2], img_size);
            Picasso.with(mContext).load(imgUrl3).placeholder(R.drawable.empty_photo).into(holder.img3);
        } else {
            holder.img3.setVisibility(View.INVISIBLE);
        }

//        holder.mTvContent.setOnClickListener(new OnItemClickListener(position));
//        holder.mGvPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
//                ShopItemListModel item = mdata.get(position);
//                mListener.onItemClick(item);
//            }
//        });

        holder.mYuepin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NahuoShare toShare = new NahuoShare(mContext, listItem);
                toShare.show();
            }
        });


        //先默认给了3个 imageview
        //     populateGridView(holder.mGvPics, stockArr);
        holder.mPrice.setText("¥" + new java.text.DecimalFormat("#.00").format(listItem.getAgentPrice()));
        holder.mBar.setProgress(listItem.getDealCount());
        holder.mBtnRemoveCollect.setOnClickListener(this);
        holder.mBtnRemoveCollect.setTag(R.id.Tag_Position, position);
        holder.mIvCollection.setOnClickListener(this);
        holder.mIvCollection.setTag(R.id.Tag_Position, position);
        String userid = listItem.getUserid() + "";
        holder.mTvBuy.setTag(R.id.Tag_Position, position);
        boolean isStart = listItem.isStart();
        holder.mTvBuy.setOnClickListener(isStart ? this : null);
        holder.mTvBuy.setBackgroundResource(isStart ? R.drawable.btn_red : R.color.btn_bg_gray);
        String intro = listItem.getIntroOrName();
        Spanned introHtml = listItem.getTextHtml(intro, mContext, imageGetter);
        holder.mTime.setText(listItem.getShowTime());
        holder.mTilte.setText(listItem.getName());

        holder.mBarName.setText(TuanPiUtil.getChengTuanTips(listItem.isStart(), listItem.getChengTuanCount(), listItem.getDealCount()));
        holder.mBar.setMax(listItem.getChengTuanCount());
        if (mSignatureMap != null) {
            holder.mTvSign.setText(mSignatureMap.get(listItem.getUserid()));
        }
        return convertView;

    }

    private void populateGridView(final GridView gridView, final String[] urls) {
        if (urls == null) {
            return;
        }
        final PicGridViewAdapter gridAdapter = new PicGridViewAdapter(mContext);

        gridView.setAdapter(gridAdapter);
        gridView.setNumColumns(3);
        if (mGridViewWidth == 0) {
            gridView.post(new Runnable() {
                @Override
                public void run() {
                    mGridViewWidth = gridView.getMeasuredWidth();
                    gridAdapter.setGridViewWidth(gridView.getMeasuredWidth());
                    gridAdapter.setData(urls);
                    gridAdapter.notifyDataSetChanged();
                }
            });
        } else {
            gridAdapter.setGridViewWidth(mGridViewWidth);
            gridAdapter.setData(urls);
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Iv_Collection:
                int pos1 = (int) v.getTag(R.id.Tag_Position);
                mListener.onRemoveCollectionsClick(mdata.get(pos1));
                break;
            case R.id.btn_remove_collection://取消收藏
                int pos = (int) v.getTag(R.id.Tag_Position);
                mListener.onRemoveCollectionsClick(mdata.get(pos));
                break;
//            case R.id.tv_talk:// 微询聊天
//                String mUserID = v.getTag(R.id.Tag_Userid).toString();
//                String mUserName = v.getTag(R.id.Tag_Username).toString();
//                int wx_pos = (Integer) v.getTag();
//                ShopItemListModel wx_item = mdata.get(wx_pos);
//
//                //   ChatHelper.chat(mContext, Integer.valueOf(mUserID), mUserName, null, wx_item.getApplyStatuID());
//
//
//                ShopItemModel mShopItem = new ShopItemModel();
//                mShopItem.setIntro(wx_item.getIntro());
//                mShopItem.ID = wx_item.getID();
//                mShopItem.setCover(wx_item.getCover());
//
//                ChatHelper.chat(mContext, Integer.valueOf(mUserID), mUserName, mShopItem,
//                        wx_item.getApplyStatuID());
//                break;
            case R.id.tv_buy:
                int buyPos = (Integer) v.getTag(R.id.Tag_Position);
                if (mIBuyClickListener != null) {
                    mIBuyClickListener.buyOnClickListener(mdata.get(buyPos));
                }
                break;
        }
    }

    private static class ViewHolder {
        private TextView mTvUserName, mTvBuy;
        private ImageView mIvAvatar;
        private Button mBtnRemoveCollect;
        //   private GridView mGvPics;
        private ProgressBar mBar;
        private TextView mIvCollection;
        private TextView mTvSign;
        private TextView mPrice;
        private TextView mBarName, mYuepin;
        private TextView mTilte;
        private ImageView img1, img2, img3;
        private View img_views;
        private TextView mTime;
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

    public interface Listener {
        void onItemClick(ShopItemListModel item);

        void onRemoveCollectionsClick(ShopItemListModel item);
    }

    public interface IBuyClickListener {
        public void buyOnClickListener(ShopItemListModel model);
    }
}
