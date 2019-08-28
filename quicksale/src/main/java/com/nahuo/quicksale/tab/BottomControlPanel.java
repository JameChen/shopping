package com.nahuo.quicksale.tab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.constant.UmengClick;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.UMengTestUtls;

/**
 * Created by jame on 2018/4/11.
 */

public class BottomControlPanel extends LinearLayout implements View.OnClickListener {
    private ImageText mItPinHuo, mItSort, mItYePin, mItShopCart, mItMe;
    private BottomPanelCallback mBottomCallback = null;

    public void setmBottomCallback(BottomPanelCallback mBottomCallback) {
        this.mBottomCallback = mBottomCallback;
    }
    public BottomControlPanel (Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public BottomControlPanel(Context context) {
        super(context);

    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg));
        setOrientation(HORIZONTAL);
        mItPinHuo = (ImageText) findViewById(R.id.it_pin_huo);
        mItSort =  (ImageText)findViewById(R.id.it_sort);
        mItYePin =  (ImageText)findViewById(R.id.it_ye_pin);
        mItShopCart =  (ImageText)findViewById(R.id.it_shop_cart);
        mItMe = (ImageText) findViewById(R.id.it_me);
        initBottomPanel();
    }

    public void initBottomPanel() {
        setBtnListener();
        //setDefaultPanel();
    }
    public void  setInitTab(int index){
        mItPinHuo.setText(getResources().getString(R.string.tab_name_pin_huo));
        mItMe.setText(getResources().getString(R.string.tab_name_me));
        mItShopCart.setText(getResources().getString(R.string.tab_name_shop_cart));
        mItSort.setText(getResources().getString(R.string.tab_name_sort));
        mItYePin.setText(getResources().getString(R.string.tab_name_ye_pin));
        switch (index){
            case Constant.BTN_FLAG_PIN_HUO:
                mItPinHuo.setCheckedNoAnim(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_YUE_PIN:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setCheckedNoAnim(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_SORT:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setCheckedNoAnim(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_ME:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setCheckedNoAnim(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_SHOP_CART:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setCheckedNoAnim(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            default:break;
        }

    }
    public void setImagetTextRed(int index,int count){
        switch (index) {
            case Constant.BTN_FLAG_PIN_HUO:
                if (mItPinHuo!=null){
                    mItPinHuo.setTextRed(count);
                }
                break;
            case Constant.BTN_FLAG_SORT:
                if (mItSort!=null)
                    mItSort.setTextRed(count);
                break;
            case Constant.BTN_FLAG_YUE_PIN:
                if (mItYePin!=null)
                    mItYePin.setTextRed(count);
                break;
            case Constant.BTN_FLAG_SHOP_CART:
                if (mItShopCart!=null)
                    mItShopCart.setTextRed(count);
                break;
            case Constant.BTN_FLAG_ME:
                if (mItMe!=null)
                    mItMe.setTextRed(count);
                break;
        }
    }
    public TextView getImagetTextRed(int index){
        TextView red=null;
        switch (index) {
            case Constant.BTN_FLAG_PIN_HUO:
                if (mItPinHuo!=null){
                    red= mItPinHuo.getmTextViewRed();
                }
                break;
            case Constant.BTN_FLAG_SORT:
                if (mItSort!=null)
                   red= mItSort.getmTextViewRed();
               break;
            case Constant.BTN_FLAG_YUE_PIN:
                if (mItYePin!=null)
                  red=   mItYePin.getmTextViewRed();
                break;
            case Constant.BTN_FLAG_SHOP_CART:
                if (mItShopCart!=null)
                   red= mItShopCart.getmTextViewRed();
                break;
            case Constant.BTN_FLAG_ME:
                if (mItMe!=null)
                   red= mItMe.getmTextViewRed();
                break;
        }
        return red;
    }
    public void setDefaultPanel() {
        mItPinHuo.setCheckedNoAnim(Constant.BTN_FLAG_PIN_HUO);
        mItPinHuo.setText(getResources().getString(R.string.tab_name_pin_huo));
        mItMe.setUnChecked(Constant.BTN_FLAG_ME);
        mItMe.setText(getResources().getString(R.string.tab_name_me));
        mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
        mItShopCart.setText(getResources().getString(R.string.tab_name_shop_cart));
        mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
        mItSort.setText(getResources().getString(R.string.tab_name_sort));
        mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
        mItYePin.setText(getResources().getString(R.string.tab_name_ye_pin));
    }

    private void setBtnListener() {
        int num = this.getChildCount();
        for (int i = 0; i < num; i++) {
            View v = getChildAt(i);
            if (v != null)
                v.setOnClickListener(this);
        }
    }
    public interface BottomPanelCallback{
         void onBottomPanelClick(int itemId);
    }
    public void  setCurrentTab(int index){
        switch (index){
            case Constant.BTN_FLAG_PIN_HUO:
                mItPinHuo.setCheckedNoAnim(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_YUE_PIN:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setCheckedNoAnim(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_SORT:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setCheckedNoAnim(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_ME:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setCheckedNoAnim(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case Constant.BTN_FLAG_SHOP_CART:
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setCheckedNoAnim(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            default:break;
        }
        if(mBottomCallback != null){
            mBottomCallback.onBottomPanelClick(index);
        }
    }

    @Override
    public void onClick(View v) {
        int index = -1;
        boolean isLogin = SpManager.getIs_Login(getContext());
        switch(v.getId()) {
            case R.id.it_pin_huo:
                UMengTestUtls.UmengOnClickEvent(getContext(), UmengClick.Click2);
                index = Constant.BTN_FLAG_PIN_HUO;
                mItPinHuo.setChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case R.id.it_shop_cart:
                UMengTestUtls.UmengOnClickEvent(getContext(), UmengClick.Click5);
                if (isLogin) {
                    index = Constant.BTN_FLAG_SHOP_CART;
                    mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                    mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                    mItShopCart.setChecked(Constant.BTN_FLAG_SHOP_CART);
                    mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                    mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                }else {
                    ActivityUtil.goToLoginActivity(getContext());
                }
                break;
            case R.id.it_me:
                UMengTestUtls.UmengOnClickEvent(getContext(), UmengClick.Click6);
                if (isLogin) {
                    index = Constant.BTN_FLAG_ME;
                    mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                    mItMe.setChecked(Constant.BTN_FLAG_ME);
                    mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                    mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                    mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                }else {
                    ActivityUtil.goToLoginActivity(getContext());
                }
                break;
            case R.id.it_sort:
                UMengTestUtls.UmengOnClickEvent(getContext(), UmengClick.Click3);
                index=Constant.BTN_FLAG_SORT;
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setUnChecked(Constant.BTN_FLAG_YUE_PIN);
                break;
            case R.id.it_ye_pin:
                UMengTestUtls.UmengOnClickEvent(getContext(), UmengClick.Click4);
                index=Constant.BTN_FLAG_YUE_PIN;
                mItPinHuo.setUnChecked(Constant.BTN_FLAG_PIN_HUO);
                mItMe.setUnChecked(Constant.BTN_FLAG_ME);
                mItShopCart.setUnChecked(Constant.BTN_FLAG_SHOP_CART);
                mItSort.setUnChecked(Constant.BTN_FLAG_SORT);
                mItYePin.setChecked(Constant.BTN_FLAG_YUE_PIN);
                break;

        }
        if(mBottomCallback != null){
            if (isLogin) {
                mBottomCallback.onBottomPanelClick(index);
            }else if (index!=Constant.BTN_FLAG_ME&&index!=Constant.BTN_FLAG_SHOP_CART&&index!=-1){
                mBottomCallback.onBottomPanelClick(index);
            }
        }
    }
}
