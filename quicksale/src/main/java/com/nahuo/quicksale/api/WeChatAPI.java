package com.nahuo.quicksale.api;

import android.content.Context;

import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.WeChatAccessToken;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.FileUtils;
import com.nahuo.quicksale.oldermodel.WeChatUserInfo;

/**
 * @description 微信相关api
 * @created 2014-12-19 下午2:29:54
 * @author ZZB
 */
public class WeChatAPI {

    /**
     * @description 获取第三方登录access token
     * @return
     *         正确返回:{
     *         "access_token":"ACCESS_TOKEN",
     *         "expires_in":7200,
     *         "refresh_token":"REFRESH_TOKEN",
     *         "openid":"OPENID",
     *         "scope":"SCOPE"
     *         }<br/>
     *         错误返回:{
     *         "errcode":40029,"errmsg":"invalid code"
     *         }
     * @created 2014-12-19 下午3:15:49
     * @author ZZB
     */
    public static WeChatAccessToken getLoginAccessToken(Context context, String code) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append("https://api.weixin.qq.com/sns/oauth2/access_token?").append("appid=" + Const.WeChatOpen.APP_ID_1)
            .append("&secret=" + Const.WeChatOpen.APP_SECRET_1).append("&code=" + code).append("&grant_type=authorization_code");
        String result = HttpUtils.get(url.toString());
        WeChatAccessToken token = GsonHelper.jsonToObject(result, WeChatAccessToken.class);
        //第一次获取的token有效期只有2小时，所以要刷新延长有效期(30天)
        WeChatAccessToken refreshedToken = refreshAccessToken(context, token.getRefreshToken());
        return refreshedToken;
    }
    /**
     * @description 刷新access token 有较长的有效期（30天）
     * @created 2014-12-19 下午4:55:41
     * @author ZZB
     * @throws Exception 
     */
    private static WeChatAccessToken refreshAccessToken(Context context, String refreshToken) throws Exception{
        StringBuilder url = new StringBuilder();
        url.append("https://api.weixin.qq.com/sns/oauth2/refresh_token?").append("appid=" + Const.WeChatOpen.APP_ID_1)
            .append("&grant_type=refresh_token").append("&refresh_token=" + refreshToken);
        FileUtils.writeFile("refreshAccessToken:" + url.toString());
        String result = HttpUtils.get(url.toString());
        WeChatAccessToken token = GsonHelper.jsonToObject(result, WeChatAccessToken.class);
        return token;
    }
    
    /**
     * @description 获取微信用户信息
     * @created 2014-12-19 下午5:03:45
     * @author ZZB
     */
    public static WeChatUserInfo getUserInfo(Context context, WeChatAccessToken token) throws Exception{
        StringBuilder url = new StringBuilder();
        url.append("https://api.weixin.qq.com/sns/userinfo?").append("openid=" + token.getOpenId())
            .append("&access_token=" + token.getAccessToken());
        String result = HttpUtils.get(url.toString());
        WeChatUserInfo userinfo = GsonHelper.jsonToObject(result, WeChatUserInfo.class);
        return userinfo;
        
    }
}
