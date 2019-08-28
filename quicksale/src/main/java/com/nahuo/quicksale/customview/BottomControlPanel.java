package com.nahuo.quicksale.customview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部控件
 * Created by jame on 2017/11/9.
 */

public class BottomControlPanel extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private BottomView mPinHuoBtn = null;
    private BottomView mYePinBtn = null;
    private BottomView mChatBtn = null;
    private BottomView mMeBtn = null;
    private BottomView mMarketBtn = null;
    private int DEFALUT_BACKGROUND_COLOR = Color.rgb(243, 243, 243); //Color.rgb(192, 192, 192)
    private BottomPanelCallback mBottomCallback = null;
    private List<BottomView> viewList = new ArrayList<>();
    public BottomControlPanel(Context context) {
        super(context);
    }



    public interface BottomPanelCallback {
        public void onBottomPanelClick(int itemId);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mPinHuoBtn = (BottomView) findViewById(R.id.btn_pin_huo);
        mYePinBtn = (BottomView) findViewById(R.id.btn_ye_pin);
        mMarketBtn = (BottomView) findViewById(R.id.btn_market);
        mChatBtn = (BottomView) findViewById(R.id.btn_chat);
        mMeBtn = (BottomView) findViewById(R.id.btn_me);
        setBackgroundColor(DEFALUT_BACKGROUND_COLOR);
        viewList.add(mPinHuoBtn);
        viewList.add(mYePinBtn);
        viewList.add(mMarketBtn);
        viewList.add(mChatBtn);
        viewList.add(mMeBtn);
    }
    public void initBottomPanel() {
        if ( mPinHuoBtn!= null) {
            mPinHuoBtn.setImage(R.drawable.tab_quick_normal);
            mPinHuoBtn.setText("拼货");
        }
        if (mYePinBtn != null) {
            mYePinBtn.setImage(R.drawable.tab_yuepin_normal);
            mYePinBtn.setText("约拼");
        }
        if (mMarketBtn != null) {
            mMarketBtn.setImage(R.drawable.icon_market_de);
            mMarketBtn.setText("市场");
        }
        if (mChatBtn != null) {
            mChatBtn.setImage(R.drawable.tab_chat_normal);
            mChatBtn.setText("客服");
        }
        if (mMeBtn != null) {
            mMeBtn.setImage(R.drawable.tab_me_normal);
            mMeBtn.setText("我的");
        }
        setBtnListener();

    }
    public void setItemRed( int index,int count){
        switch (index) {
            case Constant.TAB_FLAG_PIN_HUO:
                if (mPinHuoBtn!=null)
                    mPinHuoBtn.setRed(count);
                break;
            case Constant.TAB_FLAG_YE_PIN:
                if (mYePinBtn!=null)
                    mYePinBtn.setRed(count);
                break;
            case Constant.TAB_FLAG_MARKET:
                if (mMarketBtn!=null)
                    mMarketBtn.setRed(count);
                break;
            case Constant.TAB_FLAG_CHAT:
                if (mChatBtn!=null)
                    mChatBtn.setRed(count);
                break;
            case Constant.TAB_FLAG_ME:
                if (mMeBtn!=null)
                    mMeBtn.setRed(count);
                break;
        }

    }
    private void setBtnListener() {
        int num = this.getChildCount();
        for (int i = 0; i < num; i++) {
            View v = getChildAt(i);
            if (v != null) {
                v.setOnClickListener(this);
            }
        }
    }

    public void setBottomCallback(BottomPanelCallback bottomCallback) {
        mBottomCallback = bottomCallback;
    }
    @Override
    public void onClick(View v) {
        initBottomPanel();
        int index = -1;
        switch (v.getId()) {
            case R.id.btn_pin_huo:
                index = Constant.TAB_FLAG_PIN_HUO;
                mPinHuoBtn.setChecked(Constant.TAB_FLAG_PIN_HUO);
                break;
            case R.id.btn_ye_pin:
                index = Constant.TAB_FLAG_YE_PIN;
                mYePinBtn.setChecked(Constant.TAB_FLAG_YE_PIN);
                break;
            case R.id.btn_market:
                index = Constant.TAB_FLAG_MARKET;
                mMarketBtn.setChecked(Constant.TAB_FLAG_MARKET);
                break;
            case R.id.btn_chat:
                index = Constant.TAB_FLAG_CHAT;
                mChatBtn.setChecked(Constant.TAB_FLAG_CHAT);
                break;
            case R.id.btn_me:
                index = Constant.TAB_FLAG_ME;
                mMeBtn.setChecked(Constant.TAB_FLAG_ME);
                break;
            default:
                break;
        }
        if (mBottomCallback != null) {
            mBottomCallback.onBottomPanelClick(index);
        }
    }
    public void defaultBtnChecked() {
        if (mPinHuoBtn != null) {
            mPinHuoBtn.setChecked(Constant.TAB_FLAG_PIN_HUO);
        }
    }
}
