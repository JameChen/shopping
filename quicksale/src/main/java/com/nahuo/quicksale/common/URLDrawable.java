package com.nahuo.quicksale.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.nahuo.quicksale.R;

/**
 * Created by 诚 on 2015/9/21.
 */
public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

    @SuppressWarnings("deprecation")
    public URLDrawable(Context context, String img)
    {
        this.setBounds(getDefaultImageBounds(context,img));
        drawable = context.getResources().getDrawable(R.drawable.empty_photo);
        drawable.setBounds(getDefaultImageBounds(context,img));
    }

    @Override
    public void draw(Canvas canvas) {
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    @SuppressWarnings("deprecation")
    public Rect getDefaultImageBounds(Context context,String img) {
//	        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
//	        int width = display.getWidth();
//	        int height = (int) (width * 3 / 4);
//
//	        Rect bounds = new Rect(0, 0, width, height);
        Rect bounds;
        if(img.contains(".gif"))
        { bounds = new Rect(0, 0, 40, 40); }
        else
        { bounds = new Rect(0, 0, 500, 500); }
        return bounds;
    }

}