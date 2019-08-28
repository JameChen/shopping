package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jame on 2017/3/24.
 */

public class ImageBannerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<BannerAdModel> imgBannerList;
    private int count = 0;
    private Context mContext;
    private BannerOnclickListener mItemClickListener;
    private int imge_type = 0;
    public static int IMGE_CENTER = 1;
    public static  int TYPE_FIRST_PAGE=1;
    public int type;

    public void setType(int type) {
        this.type = type;
    }

    public void setImge_type(int imge_type) {
        this.imge_type = imge_type;
    }

    public ImageBannerAdapter(Context context, List<BannerAdModel> imgBannerList) {
        init(context, imgBannerList);
    }

    public interface BannerOnclickListener {
        void onItemClick(View v, int positon);
    }

    public void setOnItemClickListener(BannerOnclickListener listener) {
        this.mItemClickListener = listener;
    }

    private void init(Context context, List<BannerAdModel> imgBannerList) {
        this.mContext = context;
        this.imgBannerList = imgBannerList;
        this.mInflater = LayoutInflater.from(this.mContext);
        if (imgBannerList != null)
            count = imgBannerList.size();
    }

    // 获得适配数据的 数量
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return imgBannerList.get(position % count);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            if (type==TYPE_FIRST_PAGE){
                view = mInflater.inflate(R.layout.imageview_item, arg2, false);
            }else {
                view = mInflater.inflate(R.layout.imgeview_defaut_item, arg2, false);
            }
            viewHolder.iv_ad = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BannerAdModel adList = imgBannerList.get(position % count);
        String pic_url = adList.ImageUrl;
        if (imge_type == IMGE_CENTER) {
            viewHolder.iv_ad.setScaleType(ImageView.ScaleType.CENTER);
        } else {
            viewHolder.iv_ad.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Picasso.with(mContext).load(pic_url).placeholder(R.drawable.empty_photo).into(viewHolder.iv_ad);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, position % count);
                }
            }
        });
//        if (viewHolder.iv_ad != null) {
//                if (!TextUtils.isEmpty(pic_url)) {
//                    viewHolder.iv_ad.setTag(pic_url);
//                }
//                Picasso.with(mContext).load(pic_url).placeholder(R.drawable.empty_photo).into(viewHolder.iv_ad);
//                if (pic_url.equals(viewHolder.iv_ad.getTag())) {
//                    viewHolder.iv_ad.setOnClickListener(mOnClickListener);
//                }
//            }
        return view;
    }

    public class ViewHolder {
        ImageView iv_ad;
    }
}
