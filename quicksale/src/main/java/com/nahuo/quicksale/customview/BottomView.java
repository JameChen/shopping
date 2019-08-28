package com.nahuo.quicksale.customview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Constant;

/**
 * 底部控件
 * Created by jame on 2017/11/9.
 */

public class BottomView extends RelativeLayout{
    private Context mContext = null;
    private ImageView mImageView = null;
    private TextView mTextView = null,mRed=null;
    private final static int DEFAULT_IMAGE_WIDTH = 64;
    private final static int DEFAULT_IMAGE_HEIGHT = 64;
    private int CHECKED_COLOR = Color.rgb(199, 40, 45);
    private int UNCHECKED_COLOR = Color.rgb(69, 69, 69);
    public BottomView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.tab_main_indicator, this, true);
        mImageView = (ImageView)findViewById(R.id.tab_icon);
        mTextView = (TextView)findViewById(R.id.tab_title);
        mRed=(TextView)findViewById(R.id.tab_new);
    }
    public void setImage(int id){
        if(mImageView != null){
            mImageView.setImageResource(id);
            setImageSize(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        }
    }
    public void setRed(int count){
        if (mRed!=null){
            if (count>0) {
                mRed.setVisibility(VISIBLE);
                mRed.setText(count + "");
            }else {
                mRed.setVisibility(GONE);
            }
        }
    }

    public void setText(String s){
        if(mTextView != null){
            mTextView.setText(s);
            mTextView.setTextColor(UNCHECKED_COLOR);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return true;
    }
    private void setImageSize(int w, int h){
        if(mImageView != null){
            ViewGroup.LayoutParams params = mImageView.getLayoutParams();
            params.width = w;
            params.height = h;
            //mImageView.setLayoutParams(params);
        }
    }
    public void setChecked(int itemID){
        if(mTextView != null){
            mTextView.setTextColor(CHECKED_COLOR);
        }
        int checkDrawableId = -1;
        switch (itemID){
            case Constant.TAB_FLAG_PIN_HUO:
                checkDrawableId = R.drawable.tab_quick_select;
                break;
            case Constant.TAB_FLAG_YE_PIN:
                checkDrawableId = R.drawable.tab_yuepin_select;
                break;
            case Constant.TAB_FLAG_MARKET:
                checkDrawableId = R.drawable.icon_market_sel;
                break;
            case Constant.TAB_FLAG_CHAT:
                checkDrawableId = R.drawable.tab_chat_select;
                break;
            case Constant.TAB_FLAG_ME:
                checkDrawableId=R.drawable.tab_me_select;
                break;
            default:break;
        }
        if(mImageView != null){
            mImageView.setImageResource(checkDrawableId);
        }
    }
}
