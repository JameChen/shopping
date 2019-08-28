package com.nahuo.quicksale.adapter;

import java.io.File;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nahuo.library.controls.RecyclingImageView;
import com.nahuo.library.dynamicgrid.BaseDynamicGridAdapter;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.squareup.picasso.Picasso;


/**
 * @description 上传商品时，图片的adapter
 * @created 2014-12-8 上午10:46:49
 * @author ZZB
 */
public class UploadItemPicGridAdapter extends BaseDynamicGridAdapter<ImageViewModel>{

    private AbsListView.LayoutParams mLayoutParams;
    private static int IMG_WIDTH;
    private static int IMG_HEIGHT;

    public UploadItemPicGridAdapter(Context context, int columnCount) {
        super(context, columnCount);
        IMG_WIDTH = DisplayUtil.dip2px(getContext(), 60);
        IMG_HEIGHT = DisplayUtil.dip2px(getContext(), 60);
    }

    @Override
    public int getCount(){
        //这里加1是因为有一个添加图片的按钮
        return super.getCount() + 1;
    }
    /**实际图片的数目*/
    public int getPicCount(){
        return super.getCount();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            mLayoutParams = new AbsListView.LayoutParams(IMG_WIDTH, IMG_HEIGHT);
            imageView = new RecyclingImageView(getContext());
            imageView.setLayoutParams(mLayoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }
        if(position < getPicCount()){
            ImageViewModel item = (ImageViewModel) getItem(position);
            File f = new File(item.getUrl());
            if (f.exists()) {
                Picasso.with(getContext()).load(f).resize(100, 100).placeholder(R.drawable.empty_photo).into(imageView);
            }else{
                String url = ImageUrlExtends.getImageUrl(item.getUrl(), Const.LIST_ITEM_SIZE);
                Picasso.with(getContext()).load(url).resize(100, 100).placeholder(R.drawable.empty_photo).into(imageView);
            }
            imageView.setTag(R.id.tag_is_add_btn, false);
            imageView.setTag(item);
        }else{
            imageView.setImageResource(R.drawable.add);
            imageView.setTag(R.id.tag_is_add_btn, true);
        }
        return imageView;
    }

}
