package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */

public class ImgViewAdapter extends BaseAdapter {

    private Context mContext;
    public List<String> dataList;

    public ImgViewAdapter(Context c, List<String> dataList) {

        mContext = c;
        this.dataList = dataList;
        if (this.dataList==null)
        { this.dataList = new ArrayList<String>(); }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 存放列表项控件句柄
     */
    private class ViewHolder {
        public ImageView imageView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.img_imageview, parent, false);
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.image_view);
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
//                    Intent intent = new Intent(mContext,
//                    		PicGalleryActivity.class);
//                    intent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, position) ;
//                    intent.putExtra(PicGalleryActivity.EXTRA_URLS, (ArrayList<String>)dataList);
//                    mContext.startActivity(intent);
                    ((PostDetailActivity)mContext).toPicGallery(null, position) ;
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.imageView.setImageBitmap(null);
        }
        viewHolder.imageView.setTag(dataList.get(position));
        Picasso.with(mContext).load(dataList.get(position)).placeholder(R.drawable.empty_photo).into(viewHolder.imageView);

        return convertView;
    }

}
