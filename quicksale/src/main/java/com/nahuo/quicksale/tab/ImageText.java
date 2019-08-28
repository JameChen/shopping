package com.nahuo.quicksale.tab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.quicksale.R;

/**
 * Created by jame on 2018/4/11.
 */

public class ImageText extends RelativeLayout {
    private Context mContext;
    private ImageView mImageView = null;
    private TextView mTextView = null;
    private TextView mTextViewRed=null;

    public TextView getmTextViewRed() {
        return mTextViewRed;
    }
    private LinearLayout ll_tab;
    public ImageText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parentView = inflater.inflate(R.layout.tab_main_image_text, this, true);
        ll_tab=(LinearLayout) parentView.findViewById(R.id.ll_tab);
        mImageView= (ImageView) parentView.findViewById(R.id.tab_icon);
        mTextView=(TextView) parentView.findViewById(R.id.tab_content);
        mTextViewRed=(TextView) parentView.findViewById(R.id.tab_red);
    }
    public ImageText(Context context) {
        super(context);


    }
    public void setImage(int id){
        if(mImageView != null){
            mImageView.setImageResource(id);
        }
    }

    public void setText(String s){
        if(mTextView != null){
            mTextView.setText(s);
        }
    }
    public void setTextRed(int s){
        if(mTextViewRed != null){
            if (s>0) {
                mTextViewRed.setVisibility(VISIBLE);
                mTextViewRed.setText(s+"");
            }else {
                mTextViewRed.setVisibility(GONE);
            }
        }
    }
    public void setCheckedNoAnim(int itemID){
        if(mTextView != null){
            mTextView.setTextColor(ContextCompat.getColor(mContext,R.color.tab_text_check_color));
        }
        int checkDrawableId = -1;
        switch (itemID){
            case Constant.BTN_FLAG_PIN_HUO:
                checkDrawableId = R.drawable.tab_quick_select;
                break;
            case Constant.BTN_FLAG_YUE_PIN:
                checkDrawableId = R.drawable.tab_yuepin_select;
                break;
            case Constant.BTN_FLAG_SORT:
                checkDrawableId = R.drawable.tab_sort_sel;
                break;
            case Constant.BTN_FLAG_ME:
                checkDrawableId = R.drawable.tab_me_select;
                break;
            case Constant.BTN_FLAG_SHOP_CART:
                checkDrawableId = R.drawable.icon_shopcart_sel;
                break;
            default:break;
        }

        if(mImageView != null){
            mImageView.setImageResource(checkDrawableId);
        }
    }
    public void setChecked(int itemID){
        if(mTextView != null){
            mTextView.setTextColor(ContextCompat.getColor(mContext,R.color.tab_text_check_color));
        }
        int checkDrawableId = -1;
        switch (itemID){
            case Constant.BTN_FLAG_PIN_HUO:
                checkDrawableId = R.drawable.tab_quick_select;
                break;
            case Constant.BTN_FLAG_YUE_PIN:
                checkDrawableId = R.drawable.tab_yuepin_select;
                break;
            case Constant.BTN_FLAG_SORT:
                checkDrawableId = R.drawable.tab_sort_sel;
                break;
            case Constant.BTN_FLAG_ME:
                checkDrawableId = R.drawable.tab_me_select;
                break;
            case Constant.BTN_FLAG_SHOP_CART:
                checkDrawableId = R.drawable.icon_shopcart_sel;
                break;
            default:break;
        }

        if(mImageView != null){
            // 将一个TextView沿垂直方向先从原大小（1f）放大到5倍大小（5f），然后再变回原大小。
//            ObjectAnimator anim = ObjectAnimator.ofFloat(this, "scaleX", 1f, 2f, 1f);
//
//            anim.setDuration(1000);
//
//            // 回调监听，可以有也可以无。
//            // 根据情况，如果需要监听动画执行到何种“进度”，那么就监听之。
////            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
////
////                @Override
////                public void onAnimationUpdate(ValueAnimator animation) {
////                    float value = (Float) animation.getAnimatedValue();
////                  //  Log.d("zhangphil", value + "");
////                }
////            });
//            anim.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animator) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animator) {
//
//                }
//            });
//            // 正式开始启动执行动画
//            anim.start();
            CircleAnimateUtils.handleAnimate(mImageView);
           mImageView.setImageResource(checkDrawableId);
        }
    }
    public void setUnChecked(int itemID){
        if(mTextView != null){
            mTextView.setTextColor(ContextCompat.getColor(mContext,R.color.tab_text_un_check_color));
        }
        int checkDrawableId = -1;
        LayoutParams params=new LayoutParams(ScreenUtils.dip2px(mContext,10),ScreenUtils.dip2px(mContext,10));
        params.addRule(RelativeLayout.ALIGN_TOP|RelativeLayout.RIGHT_OF,R.id.ll_tab);
        params.topMargin=ScreenUtils.dip2px(mContext,5);
        params.leftMargin=ScreenUtils.dip2px(mContext,-10);

        switch (itemID){
            case Constant.BTN_FLAG_PIN_HUO:
                checkDrawableId = R.drawable.tab_quick_normal;
                break;
            case Constant.BTN_FLAG_YUE_PIN:
                checkDrawableId = R.drawable.tab_yuepin_normal;
                mTextViewRed.setLayoutParams(params);
                break;
            case Constant.BTN_FLAG_SORT:
                checkDrawableId = R.drawable.tab_sort_defaut;
                break;
            case Constant.BTN_FLAG_ME:
                checkDrawableId = R.drawable.tab_me_normal;
                mTextViewRed.setLayoutParams(params);
                break;
            case Constant.BTN_FLAG_SHOP_CART:
                checkDrawableId = R.drawable.icon_shopcart_defaut;
                break;
            default:break;
        }
        if(mImageView != null){
            mImageView.setImageResource(checkDrawableId);
        }
    }
}
