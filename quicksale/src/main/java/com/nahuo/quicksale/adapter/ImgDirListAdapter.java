package com.nahuo.quicksale.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.ImageDirModel;
import com.squareup.picasso.Picasso;

public class ImgDirListAdapter extends BaseAdapter {

    private Context             mContext;
    private List<ImageDirModel> mItemList;

    public ImgDirListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<ImageDirModel> data) {
        this.mItemList = data;
    }

    @Override
    public int getCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList == null ? null : mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (mItemList.size() > 0) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_image_dir, parent, false);

                holder = new ViewHolder();
                holder.img = (ImageView)view.findViewById(R.id.imgDir_img);
                holder.name = (TextView)view.findViewById(R.id.imgDir_name);
                holder.count = (TextView)view.findViewById(R.id.imgDir_count);
                view.setTag(holder);
            } else {
                holder = (ViewHolder)view.getTag();
            }

            ImageDirModel entity = mItemList.get(position);
            holder.model = entity;
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ViewHolder nowHolder = (ViewHolder)v.getTag();
                    mOnItemClickListener.onItemClick(nowHolder.model);
                }
            });

            Picasso.with(mContext).load(new File(entity.getFirstImgPath())).resize(200, 200).placeholder(R.drawable.empty_photo)
                    .into(holder.img);
            holder.name.setText(entity.getDirName());
            holder.count.setText(entity.excludeFiles("gif").size() + "张");
        }
        return view;
    }

    private class ViewHolder {
        ImageDirModel model;
        ImageView     img;
        TextView      name;
        TextView      count;
    }

    private OnImgDirClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnImgDirClickListener l) {
        mOnItemClickListener = l;
    }

    public interface OnImgDirClickListener {
        public void onItemClick(ImageDirModel model);
    }

}
