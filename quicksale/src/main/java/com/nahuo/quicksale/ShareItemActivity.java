//package com.nahuo.quicksale;
//
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.text.Html;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.NahuoShare;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.common.WXHelper;
//import com.nahuo.quicksale.oldermodel.Share2WPItem;
//import com.squareup.picasso.Picasso;
//
///**
// * @description 分享商品
// * @created 2015-5-6 下午2:56:12
// * @author ZZB
// */
//public class ShareItemActivity extends BaseActivity2 implements OnClickListener {
//
//    public static final String  EXTRA_SHARED_ITEM = "EXTRA_SHARED_ITEM";
//    private static final String WX_MOMENTS        = "WechatMoments";
//    private static final String WX_FRIENDS        = "Wechat";
//    private static final String SINA              = "sina";
//    private static final String QQ                = "qq";
//    private static final String QZONE             = "zone";
//    private Share2WPItem        mShare2wpItem;
//    private TextView            mTvTitle, mTvAgentPrice, mTvRetailPrice, mTvShareTips;
//    private ImageView           mIvCover;
//    private String              mImgUrl;
//    private int mItemId;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_share_item);
//        setTitle("分享赚钱");
//        initExtras();
//        initView();
//
//    }
//
//    @Override
//    public void onBackPressed() {
////        Intent intent = new Intent(this, MainActivity.class);
////        startActivity(intent);
//        finish();
//    }
//    private void initExtras() {
//        mShare2wpItem = (Share2WPItem)getIntent().getSerializableExtra(EXTRA_SHARED_ITEM);
//        mImgUrl = ImageUrlExtends.getImageUrl(mShare2wpItem.imgUrls[0], Const.LIST_ITEM_SIZE);
//        mItemId = mShare2wpItem.myItemId;
//    }
//
//    private void initView() {
//        DecimalFormat df = new DecimalFormat("0.##");
//        double supplyPrice = Double.parseDouble(mShare2wpItem.supplyPrice);
//        double agentPrice = Double.parseDouble(mShare2wpItem.agentPrice);
//        double retailPrice = Double.parseDouble(mShare2wpItem.retailPrice);
//        double agentEarn = agentPrice - supplyPrice;
//        double retailEarn = retailPrice - supplyPrice;
//        mTvTitle = (TextView)findViewById(R.id.tv_title);
//        mTvAgentPrice = (TextView)findViewById(R.id.tv_agent_price);
//        mTvRetailPrice = (TextView)findViewById(R.id.tv_retail_price);
//        mTvShareTips = (TextView)findViewById(R.id.tv_share_tips);
//        mIvCover = (ImageView)findViewById(R.id.iv_cover);
//        mImgUrl = TextUtils.isEmpty(mImgUrl) ? Const.APP_LOGO_URL : mImgUrl;
//        Picasso.with(this).load(mImgUrl)
//                .placeholder(R.drawable.empty_photo).into(mIvCover);
//        mTvTitle.setText(mShare2wpItem.getIntro());
//        mTvAgentPrice.setText(getString(R.string.agent_price, agentPrice));
//        mTvRetailPrice.setText(getString(R.string.retail_price, retailPrice));
//
//        String tips = getString(R.string.share_earn_money_text, df.format(agentEarn), df.format(retailEarn));
//        mTvShareTips.setText(Html.fromHtml(tips));
//    }
//
//    @Override
//    public void onBackClick(View v) {
//        onBackPressed();
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_one_key_share:// 一键分享
//                oneKeyShare();
//                break;
//            case R.id.tv_share_to_friend_circle:// 分享到朋友圈
//                shareItem(WX_MOMENTS);
//                break;
//            case R.id.tv_share_to_wx_friend:// 分享给微信好友
//                shareItem(WX_FRIENDS);
//                break;
//            case R.id.tv_share_to_sina_circle:// 新浪微博
//                shareItem(SINA);
//                break;
//            case R.id.tv_share_to_qq_circle:// qq
//                shareItem(QQ);
//                break;
//            case R.id.tv_share_to_qzone_circle:// QQ空间
//                shareItem(QZONE);
//                break;
//        }
//    }
//
//    private void oneKeyShare() {
//        List<String> imgUrls = new ArrayList<String>();
//        for(String url : mShare2wpItem.imgUrls) {
//            url = ImageUrlExtends.getImageUrl(url, Const.DOWNLOAD_ITEM_SIZE);
//            imgUrls.add(url);
//        }
//
//        WXHelper wxHelper = new WXHelper();
//        String content = mShare2wpItem.name;
//        wxHelper.ShareToWXTimeLine(this, content, imgUrls, Const.getItemUrl(mItemId),true);
//    }
//
//    private void shareItem(String platform) {
//        String shareMsg = mShare2wpItem.getIntro();
//
//        ShareEntity mShareData = new ShareEntity();
//        mShareData.setTitle(mShare2wpItem.name);
//        mShareData.setSummary(shareMsg);
//        mShareData.setTargetUrl(Const.getItemUrl(mItemId));
//        if (TextUtils.isEmpty(mImgUrl)) {
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
//            mShareData.setThumData(Utils.bitmapToByteArray(bitmap, true));
//        } else {
//            mShareData.setImgUrl(mImgUrl);
//        }
//        NahuoShare share = new NahuoShare(this, mShareData);
//        share.showCopyLink(false);
//        share.setShareType(NahuoShare.SHARE_TYPE_WEBPAGE);
//        if (platform.equals(WX_MOMENTS)) {
//            mShareData.setTitle(shareMsg);
//            share.addPlatforms(NahuoShare.PLATFORM_WX_CIRCLE);
//        } else if (platform.equals(WX_FRIENDS)) {
//            share.addPlatforms(NahuoShare.PLATFORM_WX_FRIEND);
//        } else if (platform.equals(SINA)) {
//            share.addPlatforms(NahuoShare.PLATFORM_SINA_WEIBO);
//        } else if (platform.equals(QQ)) {
//            share.addPlatforms(NahuoShare.PLATFORM_QQ_FRIEND);
//        } else if (platform.equals(QZONE)) {
//            share.addPlatforms(NahuoShare.PLATFORM_QZONE);
//        }
//        share.show();
//
//    }
//}
