package com.nahuo.quicksale;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;

/**
 * @description 发送页面
 * @created 2014-11-6 下午1:53:50
 * @author ZZB
 */
public class ShareActivity extends BaseActivity2 implements OnClickListener {

	private ShareActivity vThis = this;
	private static final String WX_MOMENTS = "WechatMoments";
	private static final String WX_FRIENDS = "Wechat";
	private static final String SINA = "sina";
	private static final String QQ = "qq";
	private static final String QZONE = "zone";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_share2);
		initView();
	}

	@Override
    public void onBackPressed() {
        super.onBackPressed();
    }
	@Override
	public void onBackClick(View v) {
	    onBackPressed();
	}
	private void initView() {
	    setTitle("分享安装链接");
		String shareMsg = SpManager.getShareShopText(vThis);
		if(TextUtils.isEmpty(shareMsg)){
		    SpManager.setShareShopText(this, getString(R.string.shop_share_default));
		    shareMsg = SpManager.getShareShopText(this);
		}
	}

	@Override
	public void onRequestSuccess(String method, Object object) {
	    super.onRequestSuccess(method, object);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_share_to_friend_circle:// 分享到朋友圈
				shareShop(WX_MOMENTS);
				break;
			case R.id.tv_share_to_wx_friend:// 分享给微信好友
				shareShop(WX_FRIENDS);
				break;
//			case R.id.tv_share_to_sina_circle://新浪微博
//				shareShop(SINA);
//				break;
			case R.id.tv_share_to_qq_circle:// qq
				shareShop(QQ);
				break;
			case R.id.tv_share_to_qzone_circle://QQ空间
				shareShop(QZONE);
				break;
			case R.id.tv_share_to_copy_circle:

				Utils.addToClipboard(vThis, "http://www.nahuo.com/service/DownLoadpinhuoApp");
				ViewHub.showShortToast(vThis, "链接已复制到剪切板");
//				shareInstallUrl();
				break;
		}
	}

	/**
	 * @description 分享店铺
	 * @created 2014-10-30 下午3:51:48
	 * @author ZZB
	 */
	private void shareShop(String platform) {
		String shopUrl = "http://www.nahuo.com/service/DownLoadpinhuoApp";

		ShareEntity mShareData = new ShareEntity();
		mShareData.setTitle("天天拼货团-十三行快时尚女装批发平台，天天新款，轻松拿货！");
		mShareData.setSummary("天天拼货团-十三行快时尚女装批发平台，天天新款，轻松拿货！");
		mShareData.setTargetUrl(shopUrl);
			mShareData.setImgUrl(Const.APP_LOGO_URL);
		NahuoShare share = new NahuoShare(vThis, mShareData);
		share.showCopyLink(false);
		share.setShareType(NahuoShare.SHARE_TYPE_WEBPAGE);
		if (platform.equals(WX_MOMENTS)) {
			share.addPlatforms(NahuoShare.PLATFORM_WX_CIRCLE);
		} else if (platform.equals(WX_FRIENDS)) {
			share.addPlatforms(NahuoShare.PLATFORM_WX_FRIEND);
		}
//		else if (platform.equals(SINA)) {
//			share.addPlatforms(NahuoShare.PLATFORM_SINA_WEIBO);
//		}
		else if (platform.equals(QQ)) {
			share.addPlatforms(NahuoShare.PLATFORM_QQ_FRIEND);
		} else if (platform.equals(QZONE)) {
			share.addPlatforms(NahuoShare.PLATFORM_QZONE);
		}
		share.show();
	}

	/**
	 * @description 分享微铺安装地址
	 * @created 2014-10-30 下午2:00:27
	 * @author ZZB
	 */
	private void shareInstallUrl() {
		// 店铺网址
		String installUrl = "http://www.nahuo.com/service/DownLoadpinhuoApp";

		ShareEntity mShareData = new ShareEntity();
		mShareData.setTitle("天天拼货团-十三行快时尚女装批发平台，天天新款，轻松拿货！");
		mShareData.setSummary("天天拼货团-十三行快时尚女装批发平台，天天新款，轻松拿货！");
		mShareData.setTargetUrl(installUrl);
		mShareData.setImgUrl(Const.APP_LOGO_URL);
		NahuoShare share = new NahuoShare(vThis, mShareData);
		share.show();
	}

}
