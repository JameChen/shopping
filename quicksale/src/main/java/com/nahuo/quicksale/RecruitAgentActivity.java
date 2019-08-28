package com.nahuo.quicksale;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;

/**
 * @description 招募代理
 */
public class RecruitAgentActivity extends BaseSlideBackActivity implements OnClickListener {

	private RecruitAgentActivity vThis = this;
	private static final String WX_MOMENTS = "WechatMoments";
	private static final String WX_FRIENDS = "Wechat";
	private static final String SINA = "sina";
	private static final String QQ = "qq";
	private static final String QZONE = "zone";
	private EditText recruit_agent_text;
	private String raText;
    private String mOrgMsg = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_recruit_agent);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//				R.layout.layout_titlebar_default);

		raText = SpManager.getShopRecruitDesc(vThis);
		mOrgMsg = raText;
		initView();
	}

	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    if(!mOrgMsg.equals(recruit_agent_text.getText().toString())){
	        new SaveDataTask().execute();
	    }
	}
	private void initView() {
		initTitleBar();
		recruit_agent_text = (EditText) findViewById(R.id.recruit_agent_text);
		if (raText.length() == 0) {
			raText = "亲，点击“我要代理”，等我审核通过后即可做我的代理了，超级简单～";
		}
		recruit_agent_text.setText(raText);
		recruit_agent_text.setSelection(raText.length());
	}

	private void initTitleBar() {
		TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
		tvTitle.setText("招代理");

		Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
		btnLeft.setText(R.string.titlebar_btnBack);
		btnLeft.setVisibility(View.VISIBLE);
		btnLeft.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		    case R.id.tv_send_install_url://分享安装链接
		        String installUrl = "http://www.nahuo.com/service/DownLoadWeiPuApp";
		        ShareEntity shareData = new ShareEntity();
		        shareData.setTitle("微信批发，代理神器！");
		        shareData.setSummary("微信批发，代理神器！");
		        shareData.setTargetUrl(installUrl);
		        shareData.setImgUrl(Const.APP_LOGO_URL);
		        NahuoShare share = new NahuoShare(vThis, shareData);
		        share.show();
		        break;
		case R.id.tv_share_to_friend_circle:// 分享到朋友圈
			shareShop(WX_MOMENTS);
			break;
		case R.id.tv_share_to_wx_friend:// 分享给微信好友
			shareShop(WX_FRIENDS);
			break;
		case R.id.tv_share_to_sina_circle:// 新浪微博
			shareShop(SINA);
			break;
		case R.id.tv_share_to_qq_circle:// qq
			shareShop(QQ);
			break;
		case R.id.tv_share_to_qzone_circle:// QQ空间
			shareShop(QZONE);
			break;
		case R.id.titlebar_btnLeft:
//			finish();
		    onBackPressed();
			break;
		case R.id.titlebar_btnRight:
			SaveDataTask sdt = new SaveDataTask();
			sdt.execute((Void) null);
			break;
		case R.id.tv_share_to_copy_circle:
			try {
				ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				String shopUrl = "http://"+SpManager.getShopId(vThis)+".weipushop.com/agent";
				ClipData clip = ClipData.newPlainText("label", shopUrl);
				cmb.setPrimaryClip(clip);

				Toast.makeText(vThis, "您的店铺地址已复制，长按粘贴即可", Toast.LENGTH_LONG)
						.show();
			} catch (Exception ex) {
				Log.i("tag", "复制到剪贴板失败");
			}
			break;
		}
	}

	// 保存招募方案
	public class SaveDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				AccountAPI.setShopRecruitDesc(vThis, recruit_agent_text.getText().toString());
				SpManager.setShopRecruitDesc(vThis, recruit_agent_text.getText().toString());
				return "OK";
			} catch (Exception ex) {
				ex.printStackTrace();
				return ex.getMessage() == null ? "未知异常" : ex.getMessage();
			}
		}


	}

	/**
	 * @description 分享店铺
	 * @created 2014-10-30 下午3:51:48
	 * @author ZZB
	 */
	private void shareShop(String platform) {
	    raText = recruit_agent_text.getText().toString();
		String shopUrl = "http://"+SpManager.getShopId(vThis)+".weipushop.com/agent";
		String shopName = SpManager.getShopName(vThis);
		String imageUrl_upyun = SpManager.getShopLogo(vThis);
		String imageUrl = ImageUrlExtends.getImageUrl(imageUrl_upyun, Const.LIST_COVER_SIZE);

		ShareEntity mShareData = new ShareEntity();
		if (platform.equals(WX_MOMENTS) || platform.equals(SINA))
		{//比较特殊，看不到content的，所以这里处理一下
			mShareData.setTitle(raText);
			}
		else
		{
		mShareData.setTitle(shopName);
		}
		mShareData.setSummary(raText);
		mShareData.setTargetUrl(shopUrl);
		if (TextUtils.isEmpty(imageUrl)) {
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
					R.mipmap.app_logo);
			mShareData.setThumData(Utils.bitmapToByteArray(bitmap, true));
		} else {
			mShareData.setImgUrl(imageUrl);
		}
		NahuoShare share = new NahuoShare(vThis, mShareData);
		share.showCopyLink(false);
		share.setShareType(NahuoShare.SHARE_TYPE_WEBPAGE);
		if (platform.equals(WX_MOMENTS)) {
			share.addPlatforms(NahuoShare.PLATFORM_WX_CIRCLE);
		} else if (platform.equals(WX_FRIENDS)) {
			share.addPlatforms(NahuoShare.PLATFORM_WX_FRIEND);
		}else if (platform.equals(SINA)) {
			share.addPlatforms(NahuoShare.PLATFORM_SINA_WEIBO);
		}else if (platform.equals(QQ)) {
			share.addPlatforms(NahuoShare.PLATFORM_QQ_FRIEND);
		}else if (platform.equals(QZONE)) {
			share.addPlatforms(NahuoShare.PLATFORM_QZONE);
		}
		share.show();
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
