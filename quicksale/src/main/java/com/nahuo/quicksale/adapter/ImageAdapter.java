package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.List;

/**
 * Created by wangyunfa on 16/11/30.
 */

public class ImageAdapter extends BasePagerAdapter {
    private List<String> data;
    private Context context;
    private ImageOnClickListener mClickListener;

    public ImageAdapter(List<View> list, Context context, List<String> data) {
        super(list, context);
        this.data = data;
        this.context = context;
    }

    @Override
    protected void getFooterViewItem(View view) {

    }

    @Override
    protected void getItem(View view, final int position) {
        ImageView imageView = (ImageView) view.findViewById(R.id.iv);
        View vedio_icon = view.findViewById(R.id.vedio_icon);
         String mUrl = data.get(position);
        if (mUrl.contains(".mp4")) {
            String icon = SpManager.getString(context, SpManager.VIDEO_SHOP_FIRST_ICON);
            vedio_icon.setVisibility(View.VISIBLE);
            GlideUtls.glidePic(context, icon, imageView);

        } else {
            vedio_icon.setVisibility(View.GONE);
            //imageView.setScaleType(ScaleType.CENTER_CROP);
            GlideUtls.glidePic(context, ImageUrlExtends.getImageUrl(mUrl, 24), imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    String mUrl = data.get(position);
                    mClickListener.onClick(mUrl);
                }
            }
        });
    }

    public void setOnClickListener(ImageOnClickListener onClickListener) {
        mClickListener = onClickListener;
    }

    public interface ImageOnClickListener {
        void onClick(String mUrl);
    }
}
