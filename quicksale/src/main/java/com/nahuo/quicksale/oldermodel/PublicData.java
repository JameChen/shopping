package com.nahuo.quicksale.oldermodel;

import android.content.Context;
import android.text.TextUtils;

import com.nahuo.quicksale.common.SpManager;

public class PublicData {

    /**
     * cookie值
     * */
    private static String      app_cookie         = "";

    /**
     * 店铺实体
     * */
    // @Deprecated
    // public static ShopInfoModel mShopInfo = new ShopInfoModel();
    /**
     * 店铺实体
     * */
    @Deprecated
    public static UserModel    mUserInfo          = new UserModel();

    /**
     * 又拍云空间名，用于存储商品图片
     * */
    public static final String UPYUN_BUCKET_ITEM  = "item-img";//"nahuo-img-server";
    /**
     * 又拍云表单api验证密钥，用于上传与商品有关的图片
     * */
    public static final String UPYUN_API_KEY_ITEM = "x0zSWZ16yenc7xViIWiolAXTvgg=";//"ueRRYuadbyhmJnM/shyyNrj8Wm4=";

    /**
     * 又拍云空间名，用于存储非商品图片
     * */
    public static final String UPYUN_BUCKET       = "banwo-img-server";

    /**
     * 又拍云表单api验证密钥，用于上传非商品图片
     * */
    public static final String UPYUN_API_KEY      = "3bCOkeK030b7wFIgF7nnqB/a6/s=";

    // /**
    // * 过期时间，必须大于当前时间
    // * */
    // public static final long UPYUN_EXPIRATION = System.currentTimeMillis()
    // / 1000 + 1000 * 5 * 10;

    public static long getUpYunExpiration() {
        return System.currentTimeMillis() / 1000 + 1000 * 5 * 10;
    }

    /**
     * 用于存取微铺背景图的键
     * */
    public static final String SHOP_COVER_KEY = "shopCover";

    /**
     * 店铺背景图路径
     * */
    public static String       shop_bg        = "";

    /**
     * 获取app服务器版本的Url
     * */
    public static final String UPDATE_URL     = "http://wp-app.service.nahuo.com/wp_app_version.asmx/getAndroidVersion";

    /**
     * 相册列表
     * */
    // public static ArrayList<ImageDirModel> deviceImgDic = null;

    public static String getCookie(Context context) {
        if (TextUtils.isEmpty(app_cookie)) {
            // BaiduStats.log(context, BaiduStats.EventId.COOKIE_IS_NULL);
            app_cookie = SpManager.getCookie(context);
        }
        return app_cookie;
    }

    public static void setCookie(Context context, String cookie) {
        app_cookie = cookie;
        SpManager.setCookie(context, cookie);
    }

    /**
     * @description 只在注册或者登录时设置cookie才可调用
     * @created 2014-8-26 下午6:31:49
     * @author ZZB
     */
    public static void setCookieOnlyAtInit(String cookie) {
        app_cookie = cookie;
    }
}
