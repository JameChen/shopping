package com.nahuo.quicksale.common;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.nahuo.library.helper.ImageTools;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ShareEntity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.exceptions.CatchedException;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.util.WeChatShareUtil;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class ShareHelper {

    /* 腾讯 */
    private static final String TENCENT_APP_ID = Const.TecentOpen.APP_ID;
    // private static final String TENCENT_APP_KEY = "ztQchMNkrB8re2hd";
    /* 新浪微博 */
    public static final String SINA_APP_KEY = Const.DEBUG ? "2614881502" : "951413893";
    public static final String SINA_REDIRECT_URL = "http://www.nahuo.com/college/wp";
    /* 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope */
    // private static final String SINA_SCOPE = "";
    /* 微信 */
    private static  String WECHAT_APP_ID = BWApplication.getShareWXAPPId();

    //微信小程序分享id

    private static  String WECHAT_MINIAPP_ID = BWApplication.getWXTTMiniAppID();
    // private static final String WECHAT_APP_SECRET =
    // "169a398ac2979759143e6f2387d0c280";
    private IWeiboShareAPI mWeiboShareAPI;
    private static Tencent mTencent;
    private static IWXAPI mWxAPI;
    private Activity mContext;
    private int mShareType;
    private static final int WX_TIMELINE_SUPPORT_API = com.tencent.mm.opensdk.constants.Build.TIMELINE_SUPPORTED_SDK_INT;
    private final static String hddDirectory = SDCardHelper.getSDCardRootDirectory() + "/weipu/share";

    private static enum Step {
        WECHAT_FRIEND, WECHAT_CIRCLE, WEIBO
    }

    public ShareHelper(Activity context) {
        mContext = context;
    }

    public void share(int platform, ShareEntity shareData) {
        switch (platform) {
            case NahuoShare.PLATFORM_WX_FRIEND:// 微信朋友
                wechatFriendShare(shareData);
                break;
            case NahuoShare.PLATFORM_WX_CIRCLE:// 微信朋友圈
                wechatFriendCircleShare(shareData);
                break;
            case NahuoShare.PLATFORM_SINA_WEIBO:// 新浪微博
                weiboShare(shareData);
                break;
            case NahuoShare.PLATFORM_QQ_FRIEND:// QQ
                qqFriendShare(shareData);
                break;
            case NahuoShare.PLATFORM_QZONE:// QQ空间
                qzoneShare(shareData);
                break;
            case NahuoShare.PLATFORM_COPY_LINK:
                copyLink(shareData);
                break;
            default:
                throw new RuntimeException("分享平台匹配出错");
        }
    }

    /**
     * @description 复制链接
     * @created 2015-1-29 下午2:18:03
     * @author ZZB
     */
    private void copyLink(ShareEntity shareData) {
        Utils.addToClipboard(mContext, shareData.getTargetUrl());
        ViewHub.showShortToast(mContext, "链接已复制到剪切板");
        mContext.finish();
    }

    /**
     * @description qq空间分享
     * @created 2014-12-3 下午3:30:20
     * @author ZZB
     */
    private void qzoneShare(ShareEntity shareData) {
        try {
            validateQzoneShare(shareData);
            Bundle params = new Bundle();
            params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            params.putString(QzoneShare.SHARE_TO_QQ_TITLE, shareData.getTitle());
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, shareData.getSummary());
            // app分享不支持传目标链接
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareData.getTargetUrl());
            // params.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL,
            // shareData.getImgUrl());//这个用调不出来客户端
            ArrayList<String> imageUrls = new ArrayList<String>();
            String imgURL = shareData.getImgUrl();
            if (TextUtils.isEmpty(imgURL))
                imgURL = "http://nahuo.com";
            imageUrls.add(imgURL);
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);// 分享多张图片
            if (mTencent == null) {
                mTencent = Tencent.createInstance(TENCENT_APP_ID, mContext);
            }
            mTencent.shareToQzone(mContext, params, null);
        } catch (Exception e) {
            e.printStackTrace();
            ViewHub.showShortToast(mContext, "QQ空间分享出错：" + e.getMessage());
            BaiduStats.log(mContext, BaiduStats.EventId.CUSTOM_SHARE_FAILED, "QQ空间分享出错" + e.getMessage());
        }
    }

    /**
     * @description 分享到微信朋友
     * @created 2014-11-24 下午4:41:23
     * @author ZZB
     */
    public void wechatFriendShare(ShopItemListModel item) {
        wechatShare(new ShareEntity(item), false);
    }

    /**
     * @description 分享到微信朋友
     * @created 2014-11-24 下午4:41:23
     * @author ZZB
     */
    public void wechatFriendShare(final ShareEntity share) {
        Log.v("ShareHelper", share.getMiniAppUrl());
        Log.v("ShareHelper1", share.getImgUrl());
        if (!TextUtils.isEmpty(share.getMiniAppUrl())) {
            if (mWxAPI == null) {
                mWxAPI = WXAPIFactory.createWXAPI(mContext, WECHAT_APP_ID);
                mWxAPI.registerApp(WECHAT_APP_ID);
            }
            new AsyncTask<Void, Void, SendMessageToWX.Req>() {
                SendMessageToWX.Req req;

                @Override
                protected void onPostExecute(SendMessageToWX.Req req) {
                    super.onPostExecute(req);
                    if (req != null) {
                        mWxAPI.sendReq(req);
                    }
                }

                @Override
                protected SendMessageToWX.Req doInBackground(Void... params) {
                    try {
                        return WeChatShareUtil.shareMiNiAppToWX(share.getTargetUrl(), WECHAT_MINIAPP_ID, share.getMiniAppUrl(), share.getTitle(), share.getSummary(), share);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                }
            }.execute();

        } else {
            wechatShare(share, false);
        }
    }

    /**
     * @description 分享到微信朋友圈
     * @created 2014-11-24 下午4:41:45
     * @author ZZB
     */
    public void wechatFriendCircleShare(ShareEntity share) {
        wechatShare(share, true);
    }

    /**
     * @description 分享到微信朋友圈
     * @created 2014-11-24 下午4:41:45
     * @author ZZB
     */
    public void wechatFriendCircleShare(ShopItemListModel item) {
        wechatShare(new ShareEntity(item), true);
    }

    /**
     * @param toWxTimeLine true:分享到朋友圈 false:分享给好友
     * @description 微信分享
     * @created 2014-11-21 下午6:03:55
     * @author ZZB
     */
    private void wechatShare(final ShareEntity share, final boolean toWxTimeLine) {
        validateWeChatShare(share);
        if (mWxAPI == null) {
            mWxAPI = WXAPIFactory.createWXAPI(mContext, WECHAT_APP_ID);
            mWxAPI.registerApp(WECHAT_APP_ID);
        }
        boolean isWxSupport = mWxAPI.isWXAppInstalled();
        int supportApi = mWxAPI.getWXAppSupportAPI();
        if (!isWxSupport) {
            ViewHub.showLongToast(mContext, "您未安装微信或者微信版本过低");
            return;
        } else if (supportApi < WX_TIMELINE_SUPPORT_API && toWxTimeLine) {
            ViewHub.showLongToast(mContext, "您的微信版本不支持分享到朋友圈");
            return;
        }
        shareToWeChat(share, toWxTimeLine);
//        if (share.getThumData() != null) {// 如果有bitmap图片了，不用加载网络图片
//            shareToWeChat(share, toWxTimeLine);
//        } else {
//            new LoadBitmapTask(toWxTimeLine ? Step.WECHAT_CIRCLE : Step.WECHAT_FRIEND, share).execute();
//        }
    }

    /**
     * @description 分享到微信
     * @created 2014-11-27 下午2:17:20
     * @author ZZB
     */
    private void shareToWeChat(final ShareEntity share, final boolean toWxTimeLine) {

//        try {
//            SendMessageToWX.Req req = mShareType == NahuoShare.SHARE_TYPE_WEBPAGE ? getWechatWebReq(share, toWxTimeLine)
//                    : getWechatImgReq(share, toWxTimeLine);
//            mWxAPI.sendReq(req);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ViewHub.showLongToast(mContext, "微信分享出错" + e.getMessage());
//            BaiduStats.log(mContext, BaiduStats.EventId.CUSTOM_SHARE_FAILED, "微信分享出错" + e.getMessage());
//        }
        new AsyncTask<Void, Void, SendMessageToWX.Req>() {
            SendMessageToWX.Req req;

            @Override
            protected void onPostExecute(SendMessageToWX.Req req) {
                super.onPostExecute(req);
                try {
                    if (req != null) {
                        mWxAPI.sendReq(req);
                    } else {
                        ViewHub.showLongToast(mContext, "微信分享失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ViewHub.showLongToast(mContext, "微信分享出错" + e.getMessage());
                    BaiduStats.log(mContext, BaiduStats.EventId.CUSTOM_SHARE_FAILED, "微信分享出错" + e.getMessage());
                }
            }

            @Override
            protected SendMessageToWX.Req doInBackground(Void... params) {
                try {
                    SendMessageToWX.Req req = mShareType == NahuoShare.SHARE_TYPE_WEBPAGE ? getWechatWebReq(share, toWxTimeLine)
                            : getWechatImgReq(share, toWxTimeLine);
                    //  mWxAPI.sendReq(req);
                    return req;
                } catch (Exception e) {
                    e.printStackTrace();
                    BaiduStats.log(mContext, BaiduStats.EventId.CUSTOM_SHARE_FAILED, "微信分享出错" + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
        }.execute();

    }

    private SendMessageToWX.Req getWechatImgReq(ShareEntity share, boolean toWxTimeLine) {
        WXImageObject imgObj = new WXImageObject(share.getThumData());
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        msg.thumbData = share.getThumData(); // 设置缩略图
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = toWxTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        return req;
    }

    private SendMessageToWX.Req getWechatWebReq(ShareEntity share, boolean toWxTimeLine) throws Exception {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = share.getTargetUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share.getTitle();
        msg.description = share.getSummary();
        //  byte[] thumbData = share.getThumData();
        Bitmap bmp = null;
        if (!TextUtils.isEmpty(share.getImgUrl())) {
            bmp = BitmapFactory.decodeStream(new URL(share.getImgUrl()).openStream());
            msg.thumbData = WeChatShareUtil.bitmap2Bytes(bmp, 32768);
        }else {
            bmp = BitmapFactory.decodeResource(BWApplication.getInstance().getResources(), R.drawable.ttpht);
            msg.thumbData = WeChatShareUtil.bitmap2Bytes(bmp, 32768);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = toWxTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        return req;
    }

    // *---------------------------------------新浪微博分享(开始)----------------------------------------------------*/

    /**
     * @description 新浪微博分享
     * @created 2014-11-21 下午5:36:37
     * @author ZZB
     */
    public void weiboShare(final ShareEntity share) {
        // 微博分享只能分享140字以内
        share.setSummary(StringUtils.substring(share.getSummary(), 0, 139));
        if (mWeiboShareAPI == null) {
            mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, ShareHelper.SINA_APP_KEY);
            mWeiboShareAPI.registerApp();
        }
        if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
            if (share.getThumData() != null) {// 有bitmap不加载网络图片
                shareToWeibo(share);
            } else {
                new LoadBitmapTask(Step.WEIBO, share).execute();
            }
        } else {
            ViewHub.showLongToast(mContext, "您未安装微博客户端或者是非官方版本微博客户端，不能使用该功能");
        }
    }

    /**
     * @description 分享到新浪微博
     * @created 2014-11-27 下午2:18:12
     * @author ZZB
     */
    private void shareToWeibo(final ShareEntity share) {
        try {
            WeiboMessage weiboMessage = new WeiboMessage();
            weiboMessage.mediaObject = mShareType == NahuoShare.SHARE_TYPE_WEBPAGE ? getWeiboWebpageObj(share)
                    : getWeiBoImgObject(share);
            SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
            // transaction 唯一标识
            request.transaction = String.valueOf(System.currentTimeMillis());
            request.message = weiboMessage;
            mWeiboShareAPI.sendRequest(mContext, request);
        } catch (Exception e) {
            e.printStackTrace();
            ViewHub.showLongToast(mContext, "新浪微博分享出错" + e.getMessage());
            BaiduStats.log(mContext, BaiduStats.EventId.CUSTOM_SHARE_FAILED, "新浪微博分享出错" + e.getMessage());
        }

    }

    private static ImageObject getWeiBoImgObject(ShareEntity share) {
        ImageObject imageObject = new ImageObject();
        byte[] thumbData = share.getThumData();
        imageObject.setImageObject(BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length));
        return imageObject;
    }

    private static WebpageObject getWeiboWebpageObj(ShareEntity share) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = share.getTitle();
        mediaObject.description = share.getSummary();
        mediaObject.thumbData = share.getThumData();
        mediaObject.actionUrl = share.getTargetUrl();
        mediaObject.defaultText = share.getExtra();// "Webpage 默认文案";
        return mediaObject;
    }

    // *---------------------------------------新浪微博分享(结束)----------------------------------------------------*/
    // *---------------------------------------QQ分享(开始)----------------------------------------------------*/

    /**
     * @description QQ朋友分享
     * @created 2014-11-21 下午5:36:45
     * @author ZZB
     */
    public void qqFriendShare(ShareEntity share) {
        String savePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            savePath = Environment.getExternalStorageDirectory() + "/app_logo.jpg";
        final File iconFile = new File(savePath);
        if (!iconFile.exists()) {
            try {
                FileUtils.saveBitmap(savePath, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_logo));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        try {
            validateQQShare(share);
            final Bundle params = new Bundle();
            params.putString(QQShare.SHARE_TO_QQ_TITLE, share.getTitle());
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share.getTargetUrl());
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share.getSummary());
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share.getImgUrl());
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share.getAppName());
            if (TextUtils.isEmpty(share.getImgUrl())) {
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, savePath);
            }
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
                    mShareType == NahuoShare.SHARE_TYPE_WEBPAGE ? QQShare.SHARE_TO_QQ_TYPE_DEFAULT
                            : QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            // params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, share.getExtra());
            if (mTencent == null) {
                mTencent = Tencent.createInstance(TENCENT_APP_ID, mContext);
            }
            mTencent.shareToQQ(mContext, params, null);
        } catch (Exception e) {
            e.printStackTrace();
            ViewHub.showShortToast(mContext, "QQ分享出错：" + e.getMessage());
            BaiduStats.log(mContext, BaiduStats.EventId.CUSTOM_SHARE_FAILED, "QQ分享出错" + e.getMessage());
        }
    }

    private void validateWeChatShare(ShareEntity shareData) {
        if (shareData == null) {
            throwIllegalArgumentException("ShareEntity为空");
        }
        if (shareData.getSummary().length() > 1024) {
            String content = shareData.getSummary();
            String content1 = content.substring(0, 100);
            shareData.setSummary(content1);
//            throwIllegalArgumentException("ShareEntity#summary的长度超过1024");
        }
    }

    /**
     * @description 校验qq分享
     * @created 2015-1-29 上午10:41:55
     * @author ZZB
     */
    private void validateQQShare(ShareEntity shareData) {
        if (shareData == null) {
            throwIllegalArgumentException("ShareEntity为空");
        }
        if (TextUtils.isEmpty(shareData.getTitle())) {
            throwIllegalArgumentException("ShareEntity.getTitle为空");
        }
    }

    /**
     * @description 校验qzone分享
     * @created 2015-1-29 上午10:37:15
     * @author ZZB
     */
    private void validateQzoneShare(ShareEntity shareData) {
        if (shareData == null) {
            throwIllegalArgumentException("ShareEntity为空");
        }
    }

    private void throwIllegalArgumentException(String msg) {
        throw new IllegalArgumentException(msg);
    }

    // *---------------------------------------QQ分享(结束)----------------------------------------------------*/

    private class LoadBitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private Step mStep;
        private ShareEntity mShareData;

        public LoadBitmapTask(Step step, ShareEntity shareData) {
            mStep = step;
            mShareData = shareData;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case WECHAT_CIRCLE:
                case WECHAT_FRIEND:
                case WEIBO:
                    ViewHub.showShortToast(mContext, "正在分享...");
                    break;
            }
        }

        @Override
        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new URL(mShareData.getImgUrl()).openStream());
            } catch (Exception e) {
                e.printStackTrace();
                BaiduStats.log(mContext, BaiduStats.EventId.CUSTOM_SHARE_FAILED, "url转为bitmap失败:" + e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (!SDCardHelper.createDirectory(hddDirectory)) {
                Toast.makeText(mContext, "无法在存储卡上建立目录，请检查原因", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean loadBitmapSuccess = true;
            String filename = hddDirectory + "/" + TimeUtils.dateToTimeStamp(new Date(), "yyyyMMddHHmmssSSS") + ".jpg";
            ;
            FileOutputStream fileOutputStream = null;
            File file = new File(filename);
            BufferedOutputStream bos = null;
            try {
                bos = new BufferedOutputStream(new FileOutputStream(filename));
                result.compress(CompressFormat.JPEG, 100, bos);
                bos.close();
                result.recycle();
                filename = ImageTools.createThumb(filename, 100, 100, 50, false, 25);
            } catch (OutOfMemoryError ex) {
                CrashReport.postCatchedException(new CatchedException(ex));
                file.delete();
                ex.printStackTrace();
                loadBitmapSuccess = false;
            } catch (Exception ex) {
                file.delete();
                ex.printStackTrace();
                loadBitmapSuccess = false;
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (loadBitmapSuccess) {
                mShareData.setThumData(ImageTools.getBitmapWithFileName(filename.replace("file://", ""), 100, 100));
            } else {
                ViewHub.showShortToast(mContext, "加载原图片失败，使用默认图片");
                mShareData.setImgUrl(Const.APP_LOGO_URL);
            }
            switch (mStep) {
                case WECHAT_CIRCLE:
                    shareToWeChat(mShareData, true);
                    break;
                case WECHAT_FRIEND:
                    shareToWeChat(mShareData, false);
                    break;
                case WEIBO:
                    shareToWeibo(mShareData);
                    break;

            }

        }
    }

    public void setWeiboShareAPI(IWeiboShareAPI weiboShareAPI) {
        mWeiboShareAPI = weiboShareAPI;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public int getShareType() {
        return mShareType;
    }

    public void setShareType(int shareType) {
        mShareType = shareType;
    }

}
