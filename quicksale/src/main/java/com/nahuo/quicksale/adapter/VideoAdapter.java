package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.util.VideoThumbLoader;
import com.squareup.picasso.Picasso;

import java.util.List;


public class VideoAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> videoList;
    private ListView listView;
    private VideoThumbLoader mVideoThumbLoader;
    // LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(
    // (int) (Runtime.getRuntime().maxMemory() / 16)) {
    // @Override
    // protected int sizeOf(String key, Bitmap value) {
    // return value.getRowBytes() * value.getHeight();
    // }
    // };

    public VideoAdapter(Context context, List<String> videoList,
                        ListView listView) {
        this.mContext = context;
        this.videoList = videoList;
        this.listView = listView;
        mVideoThumbLoader=new VideoThumbLoader();
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public String getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.video_item, null);
            //convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 100)));
            holder.videoImage = (ImageView) convertView
                    .findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final String videoEntity = getItem(position);
        String icon= SpManager.getString(mContext,SpManager.VIDEO_SHOP_FIRST_ICON);
        if (TextUtils.isEmpty(icon)){
            holder.videoImage.setImageResource(R.drawable.black_corner);
        }else {
            Picasso.with(mContext).load(icon).placeholder(R.drawable.empty_photo).into(holder.videoImage);
        }
        //holder.videoImage.setImageResource(R.drawable.black_corner);
        holder.videoImage.setTag(videoEntity);
       // asyncImageLoader.loadImage(position, videoEntity, imageLoadListener);
       mVideoThumbLoader.showThumbByAsynctack(videoEntity,holder.videoImage);

        return convertView;
    }

    class ViewHolder {
        ImageView videoImage;
    }

}
