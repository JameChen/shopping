package com.nahuo.quicksale.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.bean.WenXinPics;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.SharePopGridAdapter;
import com.nahuo.quicksale.base.BaseActivty;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.ShareHelper;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * @author ZZB
 * @description 商品详情分享弹窗
 * @created 2014-11-21 下午6:15:48
 */
public class DetailsSharePopWindow extends BaseActivty implements OnItemClickListener, IWeiboHandler.Response, OnClickListener {

    private ShareEntity mShareData;
    private Context mContext = this;
    private GridView mGridView;
    private ArrayList<Integer> mPlatforms;
    private int mShareType;
    private ShareHelper mShareHelper;
    // 分享平台
    public static final String EXTRA_PLATFORMS = "EXTRA_PLATFORMS";
    // 分享数据
    public static final String EXTRA_SHARE_DATA = "EXTRA_SHARE_DATA";
    // 分享类型
    public static final String EXTRA_SHARE_TYPE = "EXTRA_SHARE_TYPE";
    /**
     * 分享提示标题
     */
    public static final String EXTRA_SHARE_PROMPT_TITLE = "extra_share_prompt_title";
    /**
     * 分享提示内容
     */
    public static final String EXTRA_SHARE_PROMPT_TEXT = "extra_share_prompt_text";

    private SsoHandler mSsoHandler;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private IWeiboShareAPI mWeiboShareAPI = null;
    private View mPromptView;
    private TextView mTvPromptText, tv_share_link, tv_share_pics, tv_bottom;
    private TextView mTvPromptTitle;
    private String mSharePromptText;
    private String mSharePromptTitle;
    private int Share_Type = 1;
    private View ll_bom;
    private EventBus mEventBus = EventBus.getDefault();

    // private WeiboAuth mWeiboAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_share1);
        mEventBus.register(this);
        setFinishOnTouchOutside(true);
        initData();
        if (mPlatforms == null || mPlatforms.size() == 0) {
            finish();
            return;
        }
        initView();
//        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, ShareHelper.SINA_APP_KEY);
//        mWeiboShareAPI.registerApp();
        if (savedInstanceState != null && mWeiboShareAPI != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
    }

    /**
     * @see {@link Activity#onNewIntent}
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        if (mWeiboShareAPI != null)
            mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    public void onEventMainThread(BusEvent event) {

    }

    private void initData() {

        Intent intent = getIntent();
        mPlatforms = intent.getIntegerArrayListExtra(EXTRA_PLATFORMS);
        mShareData = (ShareEntity) intent.getSerializableExtra(EXTRA_SHARE_DATA);
        mShareType = intent.getIntExtra(EXTRA_SHARE_TYPE, NahuoShare.SHARE_TYPE_WEBPAGE);
        mSharePromptText = intent.getStringExtra(EXTRA_SHARE_PROMPT_TEXT);
        mSharePromptTitle = intent.getStringExtra(EXTRA_SHARE_PROMPT_TITLE);
        mShareHelper = new ShareHelper(this);
        mShareHelper.setShareType(mShareType);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void initView() {
        findViewById(R.id.blur).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_share_pics = (TextView) findViewById(R.id.tv_share_pics);
        tv_share_link = (TextView) findViewById(R.id.tv_share_link);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_share_link.setOnClickListener(this);
        tv_share_pics.setOnClickListener(this);
        findViewById(R.id.tv_weixun_fre).setOnClickListener(this);
        findViewById(R.id.tv_weixun_quan).setOnClickListener(this);
        findViewById(R.id.tv_weixun_quan_older).setOnClickListener(this);
        findViewById(R.id.tv_weixun_quan_new).setOnClickListener(this);
        mGridView = (GridView) findViewById(R.id.gridview);
        ll_bom = findViewById(R.id.ll_bom);
        mPromptView = findViewById(R.id.ll_prompt);
        mTvPromptText = (TextView) findViewById(R.id.tv_prompt_text);
        mTvPromptTitle = (TextView) findViewById(R.id.tv_prompt_title);

        mPromptView
                .setVisibility(TextUtils.isEmpty(mSharePromptText) && TextUtils.isEmpty(mSharePromptTitle) ? View.GONE
                        : View.VISIBLE);
        if (!TextUtils.isEmpty(mSharePromptText)) {
            mTvPromptText.setText(Html.fromHtml(mSharePromptText));
        }
        if (!TextUtils.isEmpty(mSharePromptTitle)) {
            mTvPromptTitle.setText(Html.fromHtml(mSharePromptTitle));
        }

        SharePopGridAdapter adapter = new SharePopGridAdapter(mContext);
        adapter.setData(mPlatforms);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(this);
    }

    public ShareEntity getShareData() {
        return mShareData;
    }

    public void setShareData(ShopItemListModel item) {
        mShareData = new ShareEntity(item);
    }

    public void setShareData(ShareEntity shareData) {
        mShareData = shareData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (view.getId()) {
            case NahuoShare.PLATFORM_SINA_WEIBO:// 新浪微博
                mShareHelper.setWeiboShareAPI(mWeiboShareAPI);
            case NahuoShare.PLATFORM_WX_FRIEND:// 微信朋友
            case NahuoShare.PLATFORM_WX_CIRCLE:// 微信朋友圈
            case NahuoShare.PLATFORM_QQ_FRIEND:// QQ
            case NahuoShare.PLATFORM_QZONE:// QQ空间
            case NahuoShare.PLATFORM_COPY_LINK:// 复制链接
                if (Share_Type == 1) {
                    mShareHelper.share(view.getId(), mShareData);
                } else {

                }
                break;
        }
        finish();
    }

    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(this, "分享取消", Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(this, "分享失败：Error Message: " + baseResp.errMsg, Toast.LENGTH_LONG).show();
                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_share_link:
                Share_Type = 1;
                tv_share_link.setTextColor(ContextCompat.getColor(DetailsSharePopWindow.this, R.color.base_red));
                tv_share_pics.setTextColor(ContextCompat.getColor(DetailsSharePopWindow.this, R.color.black));
                tv_share_link.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(DetailsSharePopWindow.this, R.drawable.icon_dl));
                tv_share_pics.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(DetailsSharePopWindow.this, R.drawable.icon_dlk));
                tv_bottom.setVisibility(View.INVISIBLE);
                mGridView.setVisibility(View.VISIBLE);
                ll_bom.setVisibility(View.GONE);
                break;
            case R.id.tv_share_pics:
                Share_Type = 2;
                tv_share_pics.setTextColor(ContextCompat.getColor(DetailsSharePopWindow.this, R.color.base_red));
                tv_share_link.setTextColor(ContextCompat.getColor(DetailsSharePopWindow.this, R.color.black));
                tv_bottom.setVisibility(View.VISIBLE);
                mGridView.setVisibility(View.GONE);
                ll_bom.setVisibility(View.VISIBLE);
                tv_share_link.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(DetailsSharePopWindow.this, R.drawable.icon_dlk));
                tv_share_pics.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(DetailsSharePopWindow.this, R.drawable.icon_dl));
                break;
            case R.id.tv_weixun_fre:
                if (!isInstallWeChart(this)) {
                    ViewHub.showShortToast(this, "您没有安装微信");
                    return;
                }
                if (mShareData.getImages() != null) {
                    WenXinPics bean=new WenXinPics();
                    bean.setImages(mShareData.getImages());
                    bean.setTitle(mShareData.getTitle());
                    bean.setType(1);
                    mEventBus.post(
                            BusEvent.getEvent(EventBusId.SHARE_WEIXIN_PICS, bean));
                }
                finish();
                break;
            case R.id.tv_weixun_quan:
                if (!isInstallWeChart(this)) {
                    ViewHub.showShortToast(this, "您没有安装微信");
                    return;
                }
                if (mShareData.getImages() != null) {
                    WenXinPics bean=new WenXinPics();
                    bean.setImages(mShareData.getImages());
                    bean.setTitle(mShareData.getTitle());
                    bean.setType(2);
                    mEventBus.post(
                            BusEvent.getEvent(EventBusId.SHARE_WEIXIN_PICS, bean));
                }
                finish();
                break;
            case R.id.tv_weixun_quan_older:
                if (!isInstallWeChart(this)) {
                    ViewHub.showShortToast(this, "您没有安装微信");
                    return;
                }
                if (mShareData.getImages() != null) {
                    WenXinPics bean=new WenXinPics();
                    bean.setImages(mShareData.getImages());
                    bean.setTitle(mShareData.getTitle());
                    bean.setType(3);
                    mEventBus.post(
                            BusEvent.getEvent(EventBusId.SHARE_WEIXIN_PICS, bean));
                }
                finish();
                break;
            case R.id.tv_weixun_quan_new:
                if (!isInstallWeChart(this)) {
                    ViewHub.showShortToast(this, "您没有安装微信");
                    return;
                }
                if (mShareData.getImages() != null) {
                    WenXinPics bean=new WenXinPics();
                    bean.setImages(mShareData.getImages());
                    bean.setTitle(mShareData.getTitle());
                    bean.setType(4);
                    mEventBus.post(
                            BusEvent.getEvent(EventBusId.SHARE_WEIXIN_PICS, bean));
                }
                finish();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    /**
     * 不实用微信sdk检查是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isInstallWeChart(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.tencent.mm", 0);
        } catch (Exception e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
