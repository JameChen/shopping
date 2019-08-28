package com.nahuo.quicksale.eventbus;

/**
 * @author ZZB
 * @description EventBus 事件id
 * @created 2015-4-24 上午9:56:38
 */
public class EventBusId {
    public static final int BACKGROUND_ACTION = 67;
    public static final int LIVE_RESTART = 66;
    public static final int GOODSBROADCASTRECEIVER_ACTION = 65;
    public static final int PINHUO_YEPIN_RED_IS_SHOW = 64;
    /**
     * 主页切换TAB
     */
    //商品详情刷新
    public static final int ITEM_DETAIL_REFESH = 63;
    public static final int MAIN_CHANGE_SHOPCAT = 62;
    public static final int RERRESH_PINHUO_TAB_FRAGMENT = 61;
    public static final int REFRESH_MEFRAGMENT = 60;
    public static final int SECURITYSETTINGSACTIVITY_RESH = 59;
    /**
     * 主页数据刷新
     */
    public static final int MAIN_DATA_RESH = 58;
    public static final int REFRESH_GROUP_DETAIL_NEW = 57;
    public static final int PINHUO_AD_REFRESH = 56;
    public static final int PINHUO_SHOPCART_RED_IS_SHOW = 55;
    public static final int PINHUO_ME_RED_IS_SHOW = 54;
    public static final int JPUSH_PIN_HUO_GOTOBANNERJUMP = 53;
    //提示距离
    public static final int PINHUO_TAB_BOTTOM_SQME_LAYOUT_HEIGHT = 52;
    public static final int PINHUO_TAB_BOTTOM_LAYOUT = 51;
    //主页弹窗广告广播
    public static final int LOAD_PIN_HUO_POP_AD_FINISHED = 44;
    /**
     * 注册finish修改支付密码
     */
    public static final int FINISH_PAY_PWD = 43;
    /**
     * 切换主页
     */
    public static final int MAIN_CURRENT_TAB = 42;
    /**
     * 刷新购物车角标
     */
    public static final int REFRESH_GOODS_COUNT = 41;

    public static final int RENZHENG_SELECT_RB = 40;
    /************** 注意，所有新添加的id不能重复 ****************/
    public static final int PIN_HUO_SELECT_RB = 39;
    public static final int REFRESH_COMPLETEd = 38;
    public static final int REFRESH_COMPLETEd1 = 50;
    public static final int LOAD_PIN_HUO_AD_FINISHED = 37;
    public static final int REFRESH_GROUP_DETAIL = 36;
    /**
     * 拼货收藏
     */
    public static final int SEARCH_PIN_HUO_COLLECTIONS = 35;
    /**
     * 已拼货列表
     */
    public static final int SEARCH_PIN_HUO_ALREADY = 33;
    //    /**已关注货列表*/
    public static final int SEARCH_PIN_HUO_FOLLOWS = 45;
    /**
     * 搜索拼货详细列表
     */
    public static final int SEARCH_PIN_HUO_DETAIL = 32;
    public static final int LOAD_PIN_HUO_FINISHED = 31;
    public static final int REFRESH_PIN_HUO = 30;
    /***通知主界面管理消息数*/
    public static final int MANAGER_MSG_NUMBER = 29;
    /**
     * 上传商品
     */
    public static final int ON_UPLOAD_ITEM_CLICK = 28;
    /**
     * 分享到微铺成功
     */
    public static final int ON_SHARE_2_WP_SUCCESS = 27;
    /**
     * 微信多图片分享
     */
    public static final int SHARE_WEIXIN_PICS = 46;

    /**
     * 收到jpush新订单，刷新订单列表
     */
    public static final int REFRESH_ORDER_MANAGER = 26;
    /**
     * 网银支付支付成功
     */
    public static final int BANK_PAY_SUCCESS = 25;
    /**
     * 网银支付失败
     */
    public static final int BANK_PAY_FAIL = 24;

    /**
     * 微信支付成功
     */
    public static final int WECHAT_PAY_SUCCESS = 23;

    /**
     * 修改报给上家的地址信息
     */
    public static final int UP_SUPER_ADDRESS = 22;

    /**
     * 退出应用
     */
    public static final int ON_APP_EXIT = 21;
    /**
     * 店铺logo修改
     */
    public static final int SHOP_LOGO_UPDATED = 20;
    /**
     * 修改订单商品数量
     */
    public static final int CHANGE_NUMBER = 19;
    /**
     * 添加订单备注
     */
    public static final int ADD_MEMO = 18;
    /**
     * 订单支付成功
     */
    public static final int ORDER_PAY_SUCCESS = 17;

    /**
     * 取消订单
     */
    public static final int CANCEL_ORDER = 16;
    /**
     * 供货商 退货
     */
    public static final int REFUND_SUPP_AGRESS = 15;
    /**
     * 买家退货
     */
    public static final int REFUND_BUYER_AGRESS = 14;
    /**
     * 卖家退货
     */
    public static final int REFUND_SELLER_AGRESS = 13;

    /**
     * 发货
     */
    public static final int SEND_GOOD = 12;
    /**
     * 确认收货
     */
    public static final int SURE_GET_GOOD = 11;
    /**
     * 改价
     */
    public static final int CHANGE_PRICE = 10;
    /**
     * 刷新购物车
     */
    public static final int REFRESH_SHOP_CART = 9;

    /**
     * 供应商TAB切换
     */
    public static final int ALL_ITEM_CHANGE_CURRENT_TAB = 8;
    /**
     * 主页切换TAB
     */
    public static final int MAIN_CHANGE_CURRENT_TAB = 7;

    /**
     * 微询新消息
     */
    public static final int WEIXUN_NEW_MSG = 6;
    /**
     * 上传列表有商品被删除
     */
    public static final int UPLOAD_LIST_HAS_ITEM_DELETED = 5;
    /**
     * 关注供货商改变
     */
    public static final int FOLLOW_VENDORS_CHANGED = 1;
    /**
     * 代理与取消代理供货商
     */
    public static final int AGENT_VENDOR_CHANGED = 2;
    /**
     * 刷新all items
     */
    public static final int REFRESH_ALL_ITEMS = 3;
    /**
     * 刷新市场all items
     */
    public static final int REFRESH_MARKET_ALL_ITEMS = 47;
    /**
     * 刷新市场
     */
    public static final int REFRESH_MARKET_SUB_ITEMS = 48;
    /**
     * 刷新市场广告
     */
    public static final int REFRESH_MARKET_AD = 49;


}
