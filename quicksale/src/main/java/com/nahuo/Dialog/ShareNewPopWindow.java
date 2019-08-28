package com.nahuo.Dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.adapter.SharePopGridAdapter;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.ShareHelper;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import java.util.ArrayList;

/**
 * @author ZZB
 * @description 分享弹窗
 * @created 2014-11-21 下午6:15:48
 */
public class ShareNewPopWindow extends Activity implements OnItemClickListener, IWeiboHandler.Response {

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
    private TextView mTvPromptText, tv_tittle;
    private TextView mTvPromptTitle;
    private String mSharePromptText;
    private String mSharePromptTitle;

    // private WeiboAuth mWeiboAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_new_share);

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
        if (mShareData != null) {
            if (TextUtils.isEmpty(mShareData.getTitle_des())) {
                tv_tittle.setVisibility(View.GONE);
            } else {
                tv_tittle.setVisibility(View.VISIBLE);
                tv_tittle.setText(mShareData.getTitle_des());
            }
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
        tv_tittle = (TextView) findViewById(R.id.tv_tittle);
        mGridView = (GridView) findViewById(R.id.gridview);
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
                mShareHelper.share(view.getId(), mShareData);
                break;
        }
        // finish();
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
}
