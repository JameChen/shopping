package com.nahuo.quicksale.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.quicksale.R;

/**
 * Created by jame on 2018/1/16.
 */

public class GlideUtls {
    public static void glideChang(Context mContext, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.drawable.empty_photo_chang)
                .error(R.drawable.empty_photo_chang)
                .fallback(R.drawable.empty_photo_chang)
                //.skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(imageView);
    }

    public static void glidePic(Context mContext, String path, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
                .fallback(R.drawable.empty_photo)
                //.override(256, 320)
                // .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(imageView);

    }
    public static void glidePic(Context mContext, String path, ImageView imageView,int empty_photo) {
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(empty_photo)
                .error(empty_photo)
                .fallback(empty_photo);
                //.override(256, 320)
                // .skipMemoryCache(true)
               // .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(imageView);

    }
    public static void glideColorPic(Context mContext, String path, ImageView imageView, int color) {
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(color)
                .error(color)
                .fallback(color)
                //.override(256, 320)
                // .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(imageView);

    }

    public static void glideCirclePic(Context mContext, String path, ImageView imageView) {
        //设置图片圆角角度
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .circleCropTransform()
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
                .fallback(R.drawable.empty_photo)
                //.override(256, 320)
                // .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(imageView);

    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(final Context context, final String imageUrl, final ImageView imageView, final int photo) {
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(photo)
                .error(photo)
                .fallback(photo)

                //.override(256, 320)
                // .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
//                        if (imageView != null) {
//                            imageView.setImageResource(R.drawable.empty_photo);
//                        }
                    }


                    @Override
                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int newHeight=height;
                        int newWidth=width;
                        if (width> ScreenUtils.getScreenWidth(context)){
                            newWidth=ScreenUtils.getScreenWidth(context);
                            newHeight=ScreenUtils.getScreenWidth(context)*height/newWidth;
                        }
                        if (imageView != null) {
                            if (TextUtils.equals(imageUrl,(String)imageView.getTag())) {
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                params.height = newHeight;
                                params.width = newWidth;
                                imageView.setLayoutParams(params);
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    }
                });
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        if (imageView == null) {
//                            imageView.setImageResource(photo);
//                        }
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        int height = resource.getBounds().width();
//                        int width = resource.getBounds().height();
//                        if (imageView == null) {
//                            return false;
//                        }
////                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
////                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
////                        }
//                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
////                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
////                        //float scale = (float) vw / (float) resource.getIntrinsicWidth();
////                        int vh = (int) ((float)vw/(float) 1.78);
////                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
//                        params.height = height;
//                        params.width = width;
//                        imageView.setLayoutParams(params);
//                        imageView.setImageDrawable(resource);
//
//                        return false;
//                    }
//                }).into(imageView);
    }
}
