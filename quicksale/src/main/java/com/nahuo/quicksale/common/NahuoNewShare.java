package com.nahuo.quicksale.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.nahuo.Dialog.ShareNewPopWindow;
import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.SharePopWindow;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.ShopItemModel;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author ZZB
 * @description 自定义分享
 * @created 2014-11-24 下午6:02:50
 */
public class NahuoNewShare {

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

    public NahuoNewShare(Context context, ShopItemListModel item) {
        this(context, new ShareEntity(item));
    }

    public NahuoNewShare(Context context, ShopItemModel item) {
        this(context, new ShareEntity(item));
    }

    public NahuoNewShare(Context context, ShareEntity shareData) {
        mContext = context;
        mShareData = shareData;

    }

    /**
     * @param platforms @see {@link NahuoNewShare#PLATFORM_QQ_FRIEND} 等
     * @description 若只要指定个数的平台，在这里设置
     * @created 2014-11-24 下午6:03:20
     * @author ZZB
     */
    public NahuoNewShare addPlatforms(int... platforms) {
        mPlatforms.clear();
        for (Integer platform : platforms) {
            mPlatforms.add(platform);
        }
        return this;
    }

    public NahuoNewShare showCopyLink(boolean show) {
        mShowCopyLink = show;
        return this;
    }

    public NahuoNewShare setSharePromptTitle(CharSequence title) {
        this.mPromptTitle = title;
        return this;
    }

    public NahuoNewShare setSharePromptText(CharSequence text) {
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
            Intent intent = new Intent(mContext, ShareNewPopWindow.class);
            intent.putIntegerArrayListExtra(SharePopWindow.EXTRA_PLATFORMS, mPlatforms);
            intent.putExtra(SharePopWindow.EXTRA_SHARE_DATA, mShareData);
            intent.putExtra(SharePopWindow.EXTRA_SHARE_TYPE, mShareType);
            intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TITLE, mPromptTitle);
            intent.putExtra(SharePopWindow.EXTRA_SHARE_PROMPT_TEXT, mPromptText);
            mContext.startActivity(intent);

        }

    }

    public int getShareType() {
        return mShareType;
    }

    public NahuoNewShare setShareType(int shareType) {
        mShareType = shareType;
        return this;
    }
}
