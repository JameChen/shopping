package com.nahuo.quicksale.util;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by yu on 2017/5/6.
 */
public class VideoThumbLoader {
    private ImageView imgView;
    private String path;
    //创建cache
    private LruCache<String, Bitmap> lruCache;

    public VideoThumbLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
        int maxSize = maxMemory / 4;//拿到缓存的内存大小 35         lruCache = new LruCache<String, Bitmap>(maxSize){
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //这个方法会在每次存入缓存的时候调用
                return value.getByteCount();

            }
        };
    }

    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        try {
            if (getVideoThumbToCache(path) == null) {
                //当前地址没有缓存时，就添加
                if (TextUtils.isEmpty(path) || bitmap == null)
                    return;
                lruCache.put(path, bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getVideoThumbToCache(String path) {
        if (TextUtils.isEmpty(path)){
            return null;
        }else {
            return lruCache.get(path);
        }

    }

    public void showThumbByAsynctack(String path, ImageView imgview) {
        try {
            if (!TextUtils.isEmpty(path)) {
                if (getVideoThumbToCache(path) == null) {
                    //异步加载
                    new MyBobAsynctack(imgview, path).execute(path);
                } else {
                    imgview.setImageBitmap(getVideoThumbToCache(path));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyBobAsynctack extends AsyncTask<String, Void, Bitmap> {
        private ImageView imgView;
        private String path;

        public MyBobAsynctack(ImageView imageView, String path) {
            this.imgView = imageView;
            this.path = path;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //这里的创建缩略图方法是调用VideoUtil类的方法，也是通过 android中提供的 ThumbnailUtils.createVideoThumbnail(vidioPath, kind);
            Bitmap bitmap = createVideoThumbnail(params[0], 50, 50);

            //加入缓存中
            if (getVideoThumbToCache(params[0]) == null) {
                addVideoThumbToCache(path, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                if (imgView.getTag().equals(path)) {//通过 Tag可以绑定 图片地址和 imageView，这是解决Listview加载图片错位的解决办法之一
                    imgView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

}