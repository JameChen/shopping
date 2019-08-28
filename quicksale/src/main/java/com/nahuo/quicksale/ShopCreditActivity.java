package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.BuyerCreditModel;
import com.nahuo.quicksale.oldermodel.ShopCreditItem;
import com.nahuo.quicksale.oldermodel.ShopCreditModel;
import com.nahuo.quicksale.oldermodel.ShopCustomInfo;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * @description 店铺信誉
 * @created 2015-3-4 上午10:35:10
 * @author ZZB
 */
public class ShopCreditActivity extends BaseSlideBackActivity implements View.OnClickListener {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private Context            mContext      = this;
    private LoadingDialog      mLoadingDialog;
    private ImageView          mIvShopLogo;
    private TextView           mTvUserName, mTvShopSignature, mTvShopGoodRate, mTvBuyerGoodRate, mTvShopYears,
            mTvAgentNum, mTvMargin;
    private LinearLayout       mLayoutBuyerCredit, mLayoutShopCredit, mLayoutServiceCommitment;
    private int mUserId;
    private ShopCustomInfo mShopInfo;
    
    private static enum Step {
        LOAD_DATA
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_shop_credit);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mUserId = intent.getIntExtra(EXTRA_USER_ID, -1);
        new Task(Step.LOAD_DATA).execute();
    }

    private void initView() {
        initTitleBar();
        mLoadingDialog = new LoadingDialog(this);
        mIvShopLogo = (ImageView)findViewById(R.id.iv_shop_logo);
        mTvUserName = (TextView)findViewById(R.id.tv_user_name);
        mTvShopSignature = (TextView)findViewById(R.id.tv_signature);
        mTvShopGoodRate = (TextView)findViewById(R.id.tv_shop_good_rate);
        mTvBuyerGoodRate = (TextView)findViewById(R.id.tv_buyer_good_rate);
        mTvAgentNum = (TextView)findViewById(R.id.tv_agent_num);
        mTvMargin = (TextView)findViewById(R.id.tv_margin);
        mTvShopYears = (TextView)findViewById(R.id.tv_shop_years);
        mLayoutBuyerCredit = (LinearLayout)findViewById(R.id.layout_buyer_credit);
        mLayoutShopCredit = (LinearLayout)findViewById(R.id.layout_shop_credit);
        mLayoutServiceCommitment = (LinearLayout)findViewById(R.id.layout_service_commitment);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("商家信誉");

        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }
    /**
     * @description 店铺信息加载完成
     * @created 2015-3-4 上午11:51:36
     * @author ZZB
     */
    public void onShopInfoLoaded() {
        Picasso.with(mContext).load(Const.getShopLogo(mUserId)).into(mIvShopLogo);
        mTvUserName.setText(mShopInfo.getUserName());
        BuyerCreditModel buyerCredit = mShopInfo.getBuyerCredit();
        ShopCreditModel shopCredit = mShopInfo.getShopCredit();
        String buyerGoodRate = buyerCredit.getGoodRate();
        String shopGoodRate = shopCredit.getGoodRate();
        
        mTvBuyerGoodRate.setText(TextUtils.isEmpty(buyerGoodRate) ? "" : "好评率   " + buyerGoodRate);
        mTvShopGoodRate.setText(TextUtils.isEmpty(shopGoodRate) ? "" : "好评率   " + shopGoodRate);
        mTvAgentNum.setText(mShopInfo.getAgentUserCount() + "");
        mTvShopYears.setText(TimeUtils.timeStampToDaysElapsed("yyyy-MM-dd HH:mm:ss", mShopInfo.getCreateDate()));
        mTvShopSignature.setText(mShopInfo.getSignature());
        
        ViewHub.drawBuyerCreditLevel(mContext, mLayoutBuyerCredit, buyerCredit.getId());
        ViewHub.drawSellerCreditLevel(mContext, mLayoutShopCredit, shopCredit.getId());
        
        ShopCreditItem shopCreditItem = mShopInfo.getShopCreditItem();
        DecimalFormat df=new DecimalFormat("#0.00"); 
        mTvMargin.setText(df.format(shopCreditItem.getShopCreditMoney()) + "元");
        if(shopCreditItem.isJoin24HrReturn()){
            addServiceCommitmentBageView(R.drawable.bage_24);
        }
        if(shopCreditItem.isJoinCredit()){
            addServiceCommitmentBageView(R.drawable.bage_credit);
        }else{
            mTvMargin.setText("未加入诚信保障服务");
        }
        if(shopCreditItem.isJoinMicroSources()){
            addServiceCommitmentBageView(R.drawable.bage_micro_sources);
        }
        if(shopCreditItem.isJoinMixedBatch()){
            addServiceCommitmentBageView(R.drawable.bage_5);
        }
        if(shopCreditItem.isJoinOneSample()){
            addServiceCommitmentBageView(R.drawable.bage_1);
        }
        if(shopCreditItem.isJoinSevenDaysDelivery()){
            addServiceCommitmentBageView(R.drawable.bage_7);
        }
    }
    /**
     * @description 服务承诺
     * @created 2015-3-4 下午3:49:39
     * @author ZZB
     */
    private void addServiceCommitmentBageView(int resId){
        ImageView iv = new ImageView(this);
        iv.setImageResource(resId);
        int width = DisplayUtil.dip2px(mContext, 34);
        int height = DisplayUtil.dip2px(mContext, 30);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        mLayoutServiceCommitment.addView(iv, lp);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
        }
    }

    private class Task extends AsyncTask<Object, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case LOAD_DATA:
                    mLoadingDialog.start("加载中...");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOAD_DATA:
                        mShopInfo = ShopSetAPI.getShopCustomInfoByUserId(mContext, mUserId);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String)result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case LOAD_DATA:
                        onShopInfoLoaded();
                        break;
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
    

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
