package com.nahuo.quicksale.common;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import com.nahuo.quicksale.ItemImageViewActivity;

import org.xml.sax.XMLReader;

/**
 * Created by 诚 on 2015/9/21.
 */
public class URLTagHandler implements Html.TagHandler {
    private Context mContext;

    public URLTagHandler(Context context) {
        mContext = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output,
                          XMLReader xmlReader) {
        if (tag.toLowerCase().equals("img")) {
            if (opening) {
                startImg(tag, output, xmlReader);
            } else {
                endImg(tag, output, xmlReader);
            }
        }
    }

    public void startImg(String tag, Editable output, XMLReader xmlReader) {
    }

    public void endImg(String tag, Editable output, XMLReader xmlReader) {
        Object[] imgUrl = output.getSpans(0, output.length(), ImageSpan.class);
        if (imgUrl.length > 0) {
            output.setSpan(new ImgSpan((ImageSpan)imgUrl[0]), output.getSpanStart(imgUrl[0]),
                    output.getSpanEnd(imgUrl[0]), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ImgSpan extends ClickableSpan implements View.OnClickListener {
        String mUrl = "";

        public ImgSpan(ImageSpan is) {
            mUrl = is.getSource();
        }

        @Override
        public void onClick(View v) {
            if (mUrl.length() > 0) {
                // 查看大图
                Intent intent = new Intent(mContext,
                        ItemImageViewActivity.class);
                intent.putExtra("com.nahuo.application.SubmitActivityAndTopicActivity.image_url", mUrl);
                mContext.startActivity(intent);
            }
        }
    }
}