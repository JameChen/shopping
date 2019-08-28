package com.nahuo.quicksale.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.PicGalleryActivity;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 诚 on 2015/9/21.
 */
public class URLImageGetter implements Html.ImageGetter {
    TextView textView;
    Context context;

    public URLImageGetter(Context contxt, TextView textView) {
        this.context = contxt;
        this.textView = textView;
    }
    public void setTextView(TextView textView){
        this.textView = textView ;
    }
    @Override
    public Drawable getDrawable(String paramString) {
        if(paramString.startsWith("/bbs/upload")){
            paramString = "http://www.nahuo.com/"+paramString ;
        }
        else{
            paramString = ImageUrlExtends.getImageUrl(paramString);
        }
        final String source = paramString ;
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PicGalleryActivity.class);
                ArrayList<String> urls = new ArrayList<String>() ;
                urls.add(source) ;
                intent.putExtra(PicGalleryActivity.EXTRA_URLS, urls);
                intent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, 0);
                v.getContext().startActivity(intent);
            }
        });
        URLDrawable urlDrawable = new URLDrawable(context, paramString);
        ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
        getterTask.execute(paramString);
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, String, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                urlDrawable.drawable = result;
                URLImageGetter.this.textView.requestLayout();
            }
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        public Drawable fetchDrawable(String url) {
            Drawable drawable = null;
            URL Url;
            try {
                Url = new URL(url);
                drawable = Drawable.createFromStream(Url.openStream(), "");
            } catch (Exception e) {
                e.printStackTrace() ;
                return null;
            }
            // 按比例缩放图片
            // Rect bounds = getDefaultImageBounds(context);
            // int newwidth = bounds.width();
            // int newheight = bounds.height();
            // double factor = 0.3;
            // double fx = (double)drawable.getIntrinsicWidth() /
            // (double)newwidth;
            // double fy = (double)drawable.getIntrinsicHeight() /
            // (double)newheight;
            // factor = fx > fy ? fx : fy;
            // if (factor < 0.3) factor = 0.3;
            // newwidth = (int)(drawable.getIntrinsicWidth() / factor);
            // newheight = (int)(drawable.getIntrinsicHeight() / factor);
            //
            // drawable.setBounds(0, 0, newwidth, newheight);

            if (url.contains(".gif")) {
                if(drawable!=null)
                    drawable.setBounds(0, 0, 30, 30);
            } else {
                if (drawable != null){
                    int width = drawable.getIntrinsicWidth() ;
                    width = width>500?500 :width ;
                    int height = width*drawable.getIntrinsicHeight()/drawable.getIntrinsicWidth() ;
                    drawable.setBounds(0, 0, width, height);
                }
            }
            return drawable;
        }
    }

    // //预定图片宽高比例为 4:3
    // @SuppressWarnings("deprecation")
    // public Rect getDefaultImageBounds(Context context) {
    // Display display =
    // ((Activity)context).getWindowManager().getDefaultDisplay();
    // int width = display.getWidth();
    // int height = (int) (width * 3 / 4);
    //
    // Rect bounds = new Rect(0, 0, width, height);
    // return bounds;
    // }
}
