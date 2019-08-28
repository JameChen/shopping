package com.nahuo.quicksale;

import android.os.Bundle;

import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.UserModel;

public class BaseActivity extends BaseSlideBackActivity {

    private static final String BUNDLE_LOGINUSER = "com.nahuo.bw.b.BaseActivity.bundle_loginUser";
    private static final String SHOPINFO = "com.nahuo.bw.b.BaseActivity.shopInfo";
    private static final String USERINFO = "com.nahuo.bw.b.BaseActivity.userInfo";
    private static final String COOKIE = "com.nahuo.bw.b.BaseActivity.cookie";
    private static final String SHOP_BG = "com.nahuo.bw.b.BaseActivity.shop_bg";

    /**
     * 还原已保存的数据
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	if (savedInstanceState == null)
	    return;
	// 还原已保存的数据
	Bundle bundle = savedInstanceState.getBundle(BUNDLE_LOGINUSER);
	if (bundle != null) {
	    // 店铺资料
	    ShopInfoModel entity = (ShopInfoModel) bundle
		    .getSerializable(SHOPINFO);
	    if (entity != null) {
//		PublicData.mShopInfo = entity;
	    }
	    // 个人资料
	    UserModel userEntity = (UserModel) bundle
		    .getSerializable(USERINFO);
	    if (userEntity != null) {
		PublicData.mUserInfo = userEntity;
	    }
	    // cookie值
	    String cookie = bundle.getString(COOKIE, "");
	    PublicData.setCookie(this, cookie);
	    // 店铺背景图路径
	    String shop_bg = bundle.getString(SHOP_BG, "");
	    PublicData.shop_bg = shop_bg;
	}
    }

    /**
     * 还原已保存的数据
     * */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	super.onRestoreInstanceState(savedInstanceState);

	if (savedInstanceState == null)
	    return;
	// 还原已保存的数据
	Bundle bundle = savedInstanceState.getBundle(BUNDLE_LOGINUSER);
	if (bundle != null) {
	    // 店铺资料
	    ShopInfoModel entity = (ShopInfoModel) bundle
		    .getSerializable(SHOPINFO);
	    if (entity != null) {
//		PublicData.mShopInfo = entity;
	    }
	    // 个人资料
	    UserModel userEntity = (UserModel) bundle
		    .getSerializable(USERINFO);
	    if (userEntity != null) {
		PublicData.mUserInfo = userEntity;
	    }
	    // cookie值
	    String cookie = bundle.getString(COOKIE, "");
	    PublicData.setCookie(this, cookie);
	    // 店铺背景图路径
	    String shop_bg = bundle.getString(SHOP_BG, "");
	    PublicData.shop_bg = shop_bg;
	}
    }

    @Override
    protected void onStart() {
	super.onStart();
    }

    @Override
    protected void onRestart() {
	super.onRestart();
    }

    @Override
	public void onResume() {
	super.onResume();
    }

    /**
     * 保存数据
     * */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);

//	ShopInfoModel entity = PublicData.mShopInfo;
	UserModel userEntity = PublicData.mUserInfo;
	Bundle saveBundle = new Bundle();
//	saveBundle.putSerializable(SHOPINFO, entity);
	saveBundle.putSerializable(USERINFO, userEntity);
	saveBundle.putString(COOKIE, PublicData.getCookie(this));
	saveBundle.putString(SHOP_BG, PublicData.shop_bg);
	outState.putBundle(BUNDLE_LOGINUSER, saveBundle);
    }

    @Override
	public void onPause() {
	super.onPause();
    }

    @Override
    protected void onStop() {
	super.onStop();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
    }

}
