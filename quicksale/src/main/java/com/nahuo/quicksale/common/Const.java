package com.nahuo.quicksale.common;

import android.content.Context;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.app.BWApplication;

import java.util.HashMap;
import java.util.Map;

public class Const {

    public static final int RequestCode_Live = 1;
    public static final int YEPIN_GROUPID = 60033;
    public static final String IMAGES_CASH_PATH = "weipu/weipu_save";
    public static final String TOP_LIST = "Top_List";
    public static final String TAG_TEST = "test";
    public static final boolean DEBUG = Debug.CONST_DEBUG;            // BuildConfig.DEBUG;
    public static final String PIC_SEPERATOR = "85231";
    public static final int UPLOAD_MAX_COUNTER = 3;
    private static Map<String, String> startParam = new HashMap<String, String>();
    private static String startChildHost = "";
    private static String startNormalHost = "//main";
    public static boolean IS_NETWORK_AVAILABLE = true;                         // 网络是否可用默认是true
    private static int qqFaceWidth = 0;
    public static int doubleClickTime = 1000;
    public static final String ITEM_NAHUO = "http://item.nahuo.com/item/";

    public static final String UPYUN_BUCKET_ITEM = "nahuo-img-server";

    public static final String UPYUN_API_KEY_ITEM = "ueRRYuadbyhmJnM/shyyNrj8Wm4=";


    public static final String UPYUN_BUCKET = "banwo-img-server";


    public static final String UPYUN_API_KEY = "3bCOkeK030b7wFIgF7nnqB/a6/s=";

    public static final String WECHAT_MINIAPP_IDS = "gh_dbb20c98c942";//分享微信小程序id
    public static final String WECHAT_MINIAPP_NAHUOCOIN_IDS = "gh_7c9aa28c3ea7";//拿货币微信小程序id


    public static final long UPYUN_EXPIRATION = System.currentTimeMillis()
            / 1000 + 1000 * 5 * 10;


    /**
     * @author ZZB
     * @description 帖子类型
     * @created 2015-2-9 下午5:52:40
     */
    public static enum PostType {
        ACTIVITY, TOPIC
    }

    /**
     * 商品复制类型
     *
     * @author ZZB
     *         created at 2015/8/5 14:19
     */
    public static class ShopItemCopyType {
        //自己创建的商品
        public static final int SELF_UPLOAD = 1;
        //自己转发的商品 = 2,
        public static final int SELF_SHARE = 2;
        //自己复制的商品 = 3,
        public static final int SELF_COPY = 3;
        //        未转发商品 = 4,
        public static final int NOT_SHARE = 4;
        //        已转发的商品=5,
        public static final int ALREADY_SHARED = 5;
        //        已复制的商品=6
        public static final int ALREADY_COPIED = 6;

    }

    /**
     * @author ZZB
     * @description 腾讯Bug监控
     * @created 2015-5-22 下午5:14:07
     */
    public static class Bugly {
        public static final String APP_ID = "900009060";
        public static final String APP_KEY = "bKZqxOlXo0jWjO28";
    }

    /**
     * @description 商品地址
     * @created 2015-5-6 下午5:19:41
     * @author ZZB
     */
//    public static String getItemUrl(int itemId) {
//        return "http://item.weipushop.com/wap/wpitem/" + itemId;
//    }

    public static int getQQFaceWidth(Context context) {
        if (qqFaceWidth == 0) {
            qqFaceWidth = FunctionHelper.dip2px(context.getResources(), 17);
        }
        return qqFaceWidth;
    }

    /**
     * 应用logo地址
     */
    public static final String APP_LOGO_URL = ImageUrlExtends.HTTP_BANWO_FILES + "/img/ttpht.png";

    /**
     * @author ZZB
     * @description 订单状态
     * @created 2015-4-28 下午5:20:31
     */
    public static class OrderStatus {
        /**
         * 已关闭
         */
        public static final int CLOSE_PAY = 6;
        /**
         * 全部
         */
        public static final int ALL_PAY = 0;
        /**
         * 待支付
         */
        public static final int WAIT_PAY = 1;
        /**
         * 待发货
         */
        public static final int WAIT_SHIP = 2;
        /**
         * 已收货
         */
        public static final int SHIPED = 3;
        /**
         * 已完成
         */
        public static final int DONE = 4;
        /**
         * 已取消
         */
        public static final int CANCEL = 5;
        /**
         * 退款售后
         */
        public static final int REFUND = -1;
    }

    public enum SortIndex {
        DealCountDesc,
        CreateTimeDesc,
        CollectCountDesc,
        MustDealDesc,
        DefaultDesc,
        Default
    }

    /**
     * @author ZZB
     * @description 订单按钮值
     * @created 2015-4-28 下午4:46:04
     */
    public static class OrderAction {
        public static final String ACTION_XIAJIA = "下架";
        public static final String ACTION_BUDAN = "补单";
        public static final String ACTION_PINDAN = "拼单";

        public static final String ACTION_STALL = "档口";
        public static final String ACTION_COLLECT = "收藏";
        public static final String ACTION_SHARE = "分享";
        public static final String ACTION_NAHUO = "拿货车";

        public static final String NOTISSUEINVOICES = "不放发货单";
        public static final String DELIVERY = "发货方式";
        public static final String CONTACT_CUSTOMER = "联系客服";
        public static final String PICS_ONE_DOWNLOAD = "一键下图";
        public static final String SALE_AFTER = "售后单";
        public static final String MONEY_BACK = "已退款订单";
        public static final String ONE_KEY_JOINS_THE_TRUCK = "一键加入购物车";
        public static final String GOTO_CHANGCI = "场次";
        public static final String SHOW_TRANSFER = "已结算订单";
        public static final String BUYER_APPLY_SETTLE_REFUND = "买家结算退款";
        public static final String AGENT_REFUND = "代理退款单";
        public static final String SUPPLIERS_CHANGE_PRICE = "供货商改价";
        public static final String SELLER_CHANGE_PRICE = "卖家改价";
        public static final String SELLER_CANCEL = "卖家取消";
        public static final String BUYER_CANCEL = "买家取消";
        public static final String BUYER_PAY = "买家支付";
        public static final String SUPPLIER_SHIP = "供货商发货";
        public static final String BUYER_CONFIRM_RECEIPT = "买家确认收货";
        //        public static final String BUYER_LOGISTICS        = "买家物流";
        public static final String MEMO = "备注";
        public static final String LEAVE_MSG = "留言";
        public static final String BUYER_RETURN = "买家申请退款";
        //        public static final String BUYER_GET_GOOD         = "买家确认收货";
        public static final String BUYER_EXPRESS = "买家物流";
        public static final String BUYER_RETURN_BILL = "买家退款单";
        public static final String BUYER_FOUND_BILL = "买家维权单";
        //        public static final String SUPPLIERS_SEND_GOOD    = "供货商发货";
        public static final String SELLER_EXPRESS = "卖家物流";
        public static final String SELLER_RETURN_BILL = "卖家退款单";
        public static final String SELLER_FOUND_BILL = "卖家维权单";
        public static final String SELLER_EXPRESS_BILL = "卖家发货单";
        public static final String SUPPLIERS_RETUNR_BILL = "供货商退款单";
        public static final String SUPPLIERS_FOUND_BILL = "供货商维权单";
        public static final String BUHUO = "补货";
    }

    /**
     * @author ZZB
     * @description 订单类型
     * @created 2015-4-14 上午11:19:49
     */
    public static class OrderType {
        /**
         * 所有订单
         */
        public static final int ALL = -1;
        /**
         * 拿货单
         */
        public static final int BUY = 1;
        /**
         * 售货单
         */
        public static final int SELL = 2;
        /**
         * 代理单
         */
        public static final int AGENT = 3;
        /**
         * 发货单
         */
        public static final int SHIP = 4;
    }

    public static void clearStartParam() {
        setStartModel("");
        setStartParam(new HashMap<String, String>());
    }

    public static String getStartNormalHost() {
        return startNormalHost;
    }

    public static void setStartModel(String model) {
        startChildHost = model;
    }

    public static void setStartParam(Map<String, String> param) {
        startParam = param;
    }

    public static String getStartModel() {
        return startChildHost;
    }

    public static Map<String, String> getStartParam() {
        return startParam;
    }

    public static String getStallLogo(int uid) {
//        return "http://api2.nahuo.com/v3/shop/logo/uid/" + uid;
        return HttpUtils.SERVERURL + "shop/logo/sid/" + uid;
    }

    public static String getShopLogo(int uid) {
//        return "http://api2.nahuo.com/v3/shop/logo/uid/" + uid;
        return HttpUtils.SERVERURL + "/shop/logo/uid/" + uid;
    }

    public static String getShopLogo(long uid) {
//        return "http://api2.nahuo.com/v3/shop/logo/uid/" + uid;
        return HttpUtils.SERVERURL + "/shop/logo/uid/" + uid;
    }

    public static String getShopLogo(String uid) {
//        return "http://api2.nahuo.com/v3/shop/logo/uid/" + uid;
        return HttpUtils.SERVERURL + "/shop/logo/uid/" + uid;
    }

    /**
     * @author ZZB
     * @description 商品上传状态
     * @created 2015-4-7 下午5:22:57
     */
    public static class UploadStatus {
        /**
         * 上传中
         */
        public static final String UPLOADING = "上传中";
        /**
         * 等待上传
         */
        public static final String UPLOAD_WAIT = "等待";
        /**
         * 上传失败
         */
        public static final String UPLOAD_FAILED = "点击重传";
        /**
         * 无网络
         */
        public static final String NO_NETWORK = "无网络";
    }

    /**
     * @author ZZB
     * @description 诚保状态id
     * @created 2015-3-5 上午10:39:34
     */
    public static class CreditJoinStatuId {
        /**
         * 未加入
         */
        public static final int NOT_APPLY = -1;
        /**
         * 审核未通过
         */
        public static final int CHECK_NOT_PASSED = 2;
        /**
         * 审核通过，已加入
         */
        public static final int CHECK_PASSED = 3;
        /**
         * 已退出
         */
        public static final int QUIT = 7;

    }

    /**
     * @author ZZB
     * @description 诚保服务id
     * @created 2015-3-4 下午4:46:26
     */
    public static class CreditItemId {
        /**
         * 诚信保证金计划
         */
        public static final int CREDIT_MONEY = 1;
        /**
         * 24小时无理由退换货
         */
        public static final int _24HR_RETURN = 2;
        /**
         * 一件拿样
         */
        public static final int ONE_SAMPLE = 3;
        /**
         * 5件起混批
         */
        public static final int _5_MIXED = 4;
        /**
         * 7天寄售
         */
        public static final int _7_DAYS_DELIEVERY = 5;
        /**
         * 微货源
         */
        public static final int MICRO_SOURCES = 6;
    }

    /**
     * @author ZZB
     * @description 从哪里登录
     * @created 2015-1-6 上午11:14:17
     */
    public static enum LoginFrom {
        QQ, WECHAT;
    }

    /**
     * @author ZZB
     * @description 申请代理的状态
     * @created 2014-11-18 上午10:33:24
     */
    public static final class ApplyAgentStatu {
        /**
         * 未申请
         */
        public static final int NOT_APPLY = 0;
        /**
         * 申请中
         */
        public static final int APPLYING = 1;
        /**
         * 拒绝
         */
        public static final int REJECT = 2;
        /**
         * 接受
         */
        public static final int ACCEPT = 3;
    }

    /**
     * @author ZZB
     * @description 运费类型
     * @created 2014-9-24 上午11:26:36
     */
    public static final class PostFeeType {
        /**
         * 供货商运费之和
         */
        public static final int VENDOR_TOTAL = 1;
        /**
         * 统一运费
         */
        public static final int UNIFICATION = 2;
        /**
         * 卖家运费
         */
        public static final int VENDOR_SINGLE = 3;
    }

    /**
     * @author ZZB
     * @description 银行状态
     * @created 2014-9-19 下午5:18:36
     */
    public static final class BankState {
        /**
         * 全部
         */
        public static final String ALL = "全部";
        /**
         * 未提交
         */
        public static final String NOT_COMMIT = "未提交";
        /**
         * 未审核
         */
        public static final String CHECKING = "审核中";
        /**
         * 已审核
         */
        public static final String AUTH_PASSED = "已审核";
        /**
         * 驳回
         */
        public static final String REJECT = "驳回";
        /**
         * 重审
         */
        public static final String RECHECK = "重审";
        /**
         * 未审核
         */
        public static final String UNCHECK = "未审核";
    }

    /**
     * @author ZZB
     * @description 身份验证状态
     * @created 2014-9-15 下午2:06:04
     */
    public static final class IDAuthState {
        // 全部 = -1, 未提交 = 0, 审核中 = 1, 已审核 = 2,驳回 = 3, 重审 = 4,
        /**
         * 全部
         */
        public static final String ALL = "全部";
        /**
         * 未提交
         */
        public static final String NOT_COMMIT = "未提交";
        /**
         * 未审核
         */
        public static final String CHECKING = "审核中";
        /**
         * 已审核
         */
        public static final String AUTH_PASSED = "已审核";
        /**
         * 驳回
         */
        public static final String REJECT = "驳回";
        /**
         * 重审
         */
        public static final String RECHECK = "重审";
    }

    /**
     * @author ZZB
     * @description 腾讯开放平台相关
     * @created 2014-12-18 上午10:56:44
     */
    public static final class TecentOpen {
        public static final String APP_ID = "1104862442";
    }

    /**
     * @author ZZB
     * @description 微信开放平台相关
     * @time 2014-8-4 上午10:40:44
     */
    public static final class WeChatOpen {

        // public static final String APP_ID = DEBUG ? "wx3d1fe1d0ca7fda52" : "wxf9f70be4b1854e0e";
        public static final String APP_ID = "wx60fdde9efe3f6894";
        public static final String PARTNER_ID = "85419541";
        /* 登录相关的 */
        // public static final String APP_ID_1 = DEBUG ? "wx3d1fe1d0ca7fda52" : "wxf85afd6c7c1cdc7f";
        public static final String APP_ID_1 = BWApplication.getShareWXAPPId();
        public static final String APP_SECRET_1 = DEBUG ? "d0a912bf239e8e5481cdbc1eed94a6cc"
                : "6bf8aaeda97a61b09d9e626cf5337941";
    }

    public static final String WX_APP_KEY = "KYPinHuo";
    public static final String APP_ALIPAY = "KYAppAliPay";
    public static final String APP_PIN_HUO = "PinHuo";

    /**
     * Description:系统分组id 2014-7-15下午4:32:10
     */
    public static final class SystemGroupId {
        public static final int MIN_ID = -4;
        public static final int ALL_AGENT = -1;
        public static final int BLACK_LIST = -2;
        public static final int NEW_APPLY = -3;
        public static final int ALL_PPL = -5;

    }

    /**
     * Description:我的代理分组界面更新 2014-7-16下午5:46:16
     */
    public static final class AgentGroup {
        /**
         * 需要减少人数的分组id
         */
        public static final String DECREASE_GROUP_IDS = "DECREASE_GROUP_IDS";
        /**
         * 需要增加人数的分组id
         */
        public static final String INCREASE_GROUP_IDS = "INCREASE_GROUP_IDS";
        /**
         * 用户老的分组id
         */
        public static final String OLD_GROUP_IDS = "OLD_GROUP_IDS";
        public static final String GROUP_TYPE = "GROUP_TYPE";
        /**
         * 从一个分组移到另外一个分组的用户id，需要在旧分组中移除它
         */
        public static final String MOVED_USER_ID = "MOVED_USER_ID";
        public static final String USER_ID = "USER_ID";

    }

    /**
     * @author ZZB
     * @description 密码类型
     * @time 2014-7-31 下午2:08:16
     */
    public static enum PasswordType {
        /**
         * 支付
         */
        PAYMENT,
        /**
         * 登录
         */
        LOGIN, RESET_PAYMENT
    }

    /**
     * @author ZZB
     * @description 密码相关的，用intent传数据时的key
     * @time 2014-7-31 下午2:18:56
     */
    public static class PasswordExtra {
        public static final String EXTRA_PSW_TYPE = "EXTRA_PSW_TYPE";
    }

    /**
     * @description 图片picasso压缩质量
     * @author PJ
     */
    public static int IMAGE_QUANTITY = 90;

    /**
     * @description 图片上传最大大小
     * @author PJ
     */
    public static int UPLOAD_ITEM_MAX_SIZE = 160;

    /**
     * @description 图片下载缩略图配置
     * @author PJ
     */
    public static int DOWNLOAD_ITEM_SIZE = 24;

    /**
     * @description 列表位置头像的缩略图配置
     * @author PJ
     */
    public static int LIST_COVER_SIZE = 10;

    /**
     * @description 搜索列表商品小图的缩略图配置
     * @author PJ
     */
    public static int LIST_ITEM_SIZE = 3;
    /**
     * @description 列表位置HEADER头像的缩略图配置
     * @author PJ
     */
    public static int LIST_HEADER_COVER_SIZE = 4;
    /**
     * @description 列表位置HEADER背景的缩略图配置
     * @author PJ
     */
    public static int HEADER_BG_SIZE = 16;

    public static final int Alpha = 50;
    public static final int Red = 78;
    public static final int Green = 178;
    public static final int Blue = 255;

    public static final String LONGITUDE = "longitude";// 经度
    public static final String LATITUDE = "latitude";// 纬度

    public static class Actions {
        public static final String SaveAuthInfo = "http://api2.nahuo.com/v3/pinhuobuyer/SaveAuthInfo";// 认证
    }

    public static class Keys {
        public static final String Cookie = "Cookie";
        public static final String Address = "Address";
        public static final String Area = "Area";
        public static final String City = "City";
        public static final String Province = "Province";
        public static final String Street = "Street";
        public static final String Dimension = "Dimension";// 纬度
        public static final String Longitude = "Longitude";
        public static final String UserID = "UserID";
        public static final String RealName = "RealName";
        public static final String Mobile = "Mobile";
        public static final String ShopName = "ShopName";
        public static final String Images = "Images";
        public static final String TypeID = "TypeID";
        public static final String Url = "Url";
        public static final String AuthResult = "AuthResult";
        public static final String HouseNumber = "HouseNumber";
        public static final String Result = "Result";
        public static final String Message = "Message";
        public static final String Point = "Point";// 积分
        public static final String AuthInfo = "AuthInfo";
        public static final String StatuID = "StatuID";//未认证 = 0,审核中 = 1, 已认证 = 2, 认证失败 = 3,冻结 = 4
    }

}
