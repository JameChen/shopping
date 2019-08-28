package com.nahuo.quicksale.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.SharePopWindow;
import com.nahuo.quicksale.activity.DetailsChangSharePopWindow;
import com.nahuo.quicksale.activity.DetailsSharePopWindow;
import com.nahuo.quicksale.activity.DetailsSingleSharePopWindow;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author ZZB
 * @description 商品详情分享
 * @created 2014-11-24 下午6:02:50
 */
public class NahuoDetailsShare {

    public static final int PLATFORM_WX_FRIEND = 1;
    public static final int PLATFORM_WX_CIRCLE = 2;
    /**
     * QQ好友
     */
    public static final int PLATFORM_QQ_FRIEND = 3;
    public static final int PLATFORM_QZONE = 4;
    public static final int PLATFORM_SINA_WEIBO = 5;
    public static final int PLATFORM_COPY_LINK = 6;

    /* 分享形式：网页，图片 */
    public static final int SHARE_TYPE_WEBPAGE = 1;
    public static final int SHARE_TYPE_IMG = 2;

    private Context mContext;
    private ShareEntity mShareData;
    /**
     * 分享类型，默认是网页
     */
    private int mShareType = SHARE_TYPE_WEBPAGE;
    /**
     * 分享平台
     */
    private ArrayList<Integer> mPlatforms = new ArrayList<Integer>(Arrays.asList(new Integer[]{
            PLATFORM_WX_FRIEND, PLATFORM_WX_CIRCLE, PLATFORM_QQ_FRIEND, PLATFORM_QZONE}));
//    private ArrayList<Integer> mPlatforms          = new ArrayList<Integer>(Arrays.asList(new Integer[] {
//            PLATFORM_WX_FRIEND, PLATFORM_WX_CIRCLE, PLATFORM_QQ_FRIEND, PLATFORM_QZONE, PLATFORM_SINA_WEIBO}));
    /**
     * 显示"复制链接"
     */
    private boolean mShowCopyLink = true;
    private CharSequence mPromptTitle;
    private CharSequence mPromptText;
    private int Type;
    public static int TYPE_SHARE = 1;
    public static int TYPE_CHANGSHI_SHARE = 2;

    public void setType(int type) {
        Type = type;
    }

    public NahuoDetailsShare(Context context, ShopItemListModel item) {
        this(context, new ShareEntity(item));
    }

    public NahuoDetailsShare(Context context, ShopItemModel item) {
        this(context, new ShareEntity(item));
    }

    public NahuoDetailsShare(Context context, RecommendModel item) {
        this(context, new ShareEntity(item));
    }

    public NahuoDetailsShare(Context context, ShareEntity shareData) {
        mContext = context;
        mShareData = shareData;

    }

    /**
     * @param platforms @see {@link NahuoDetailsShare#PLATFORM_QQ_FRIEND} 等
     * @description 若只要指定个数的平台，在这里设置
     * @created 2014-11-24 下午6:03:20
     * @author ZZB
     */
    public NahuoDetailsShare addPlatforms(int... platforms) {
        mPlatforms.clear();
        for (Integer platform : platforms) {
            mPlatforms.add(platform);
        }
        return this;
    }

    public NahuoDetailsShare showCopyLink(boolean show) {
        mShowCopyLink = show;
        return this;
    }

    public NahuoDetailsShare setSharePromptTitle(CharSequence title) {
        this.mPromptTitle = title;
        return this;
    }

    public NahuoDetailsShare setSharePromptText(CharSequence text) {
        this.mPromptText = text;
        return this;
    }

    public void show() {
        if (mShowCopyLink) {
            mPlatforms.add(PLATFORM_COPY_LINK);
        }
        if (mPlatforms.size() == 1) {// 如果只有一个平台，不弹窗，直接分享
            int platform = mPlatforms.get(0);
            ShareHelper helper = new ShareHelper((Activity) mContext);
            helper.setShareType(mShareType);
            helper.share(platform, mShareData);
        } else {
            if (Type == TYPE_SHARE) {
                Intent intent = new Intent(mContext, DetailsSingleSharePopWindow.class);
                intent.putIntegerArrayListExtra(SharePopWindow.EXTRA_PLATFORMS, mPlatforms);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_DATA, mShareData);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_TYPE, mShareType);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TITLE, mPromptTitle);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TEXT, mPromptText);
                mContext.startActivity(intent);
            } else if (Type ==TYPE_CHANGSHI_SHARE) {
                Intent intent = new Intent(mContext, DetailsChangSharePopWindow.class);
                intent.putIntegerArrayListExtra(SharePopWindow.EXTRA_PLATFORMS, mPlatforms);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_DATA, mShareData);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_TYPE, mShareType);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TITLE, mPromptTitle);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TEXT, mPromptText);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, DetailsSharePopWindow.class);
                intent.putIntegerArrayListExtra(SharePopWindow.EXTRA_PLATFORMS, mPlatforms);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_DATA, mShareData);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_TYPE, mShareType);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TITLE, mPromptTitle);
                intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TEXT, mPromptText);
                mContext.startActivity(intent);
            }

        }

    }

    public int getShareType() {
        return mShareType;
    }

    public NahuoDetailsShare setShareType(int shareType) {
        mShareType = shareType;
        return this;
    }
}
