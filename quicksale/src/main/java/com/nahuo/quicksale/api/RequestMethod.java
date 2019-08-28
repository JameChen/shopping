package com.nahuo.quicksale.api;

/**
 * @author JorsonWong
 * @description Http 请求method
 * @created 2015年4月2日 上午10:51:40
 */
public class RequestMethod {
    //分类
    public static class IteMizeMethod {
        //筛选更多接口
        public static final String Get_Search_Panel = "pinhuoitem/GetSearchPanel";
    }

    public static class SaleAfterMethod {
        public static final String GET_DETAIL_FOR_ADD = "pinhuobuyer/Defective/DetailForAdd";
        public static final String GET_DETAIL_APPLY = "pinhuobuyer/Defective/Apply";
        public static final String GET_APPY_SUCCESSDETAIL = "pinhuobuyer/Defective/Detail";
        public static final String GET_APPY_UPDATE = "pinhuobuyer/Defective/Update";
        public static final String UPDATE_RETURN_EXPRESS = "pinhuobuyer/Defective/UpdateReturnExpress";
    }

    public static class QuickSaleMethod {
        //拼货列表与预告列表
        public static final String GET_PIN_AND_FORECAST_LIST = "pinhuoitem/GetAllActivitys";
        public static final String GET_PIN_HUO_NEW_LIST = "pinhuoitem/GetHomeActivityList2";
        public static final String GET_PIN_HUO_NEW_AD = "ads/GetBannersFormType";
        public static final String GET_PIN_HUO_V2_NEW_AD = "ads/GetBannersFormTypeV2";
        public static final String CLICK_BANNER = "ads/ClickBanner";
        public static final String GET_PIN_HUO_NEW_BY_CID_LIST = "pinhuoitem/GetActListByCid";
        //收藏列表
        //public static final String GET_FAVORITES = "shop/agent/GetMyFavoriteForQuickSale";
        public static final String GET_FAVORITES = "pinhuobuyer/item/GetFavoriteItem";

        //取消收藏
        public static final String REMOVE_FAVORITE = "shop/agent/RemoveItemFromMyFavorite";
        //收藏
        public static final String ADD_FAVORITE = "shop/agent/AddItemToMyFavorite";
        //闪批列表
        public static final String RECOMMEND_SHOP_ITEMS = "pinhuoitem/GetItemsV2";
        //商品访问档口列表
        public static final String GET_ITEM_4STALLID = "pinhuoitem/GetItem4StallID";
        //分页搜索
        public final static String RECOMMEND_SHOP_COMMONSEARCHITEM = "pinhuoitem/SearchV2";
        //用户签名
        public static String USERS_SIGNATURE = "user/user/getsignaturelist";
        //获取已拼
        public static String GET_QSORDER = "shop/agent/order/GetMyQSOrderList";
        //单中商品列表 参数
        //typeID    0 即将成团   1已拼成团
        public static String GETBUYERITEMLIST = "pinhuobuyer/item/GetBuyerItemList";
        //足迹
        public static String GETITEMVISITLIST = "pinhuoitem/item/GetItemVisitList";
        //关注团
        public static final String SAVE_FOCUS = "pinhuoitem/user/SaveFocusActivity";
        /**
         * 获取关注列表
         */
        public static final String GET_FOCUS_LIST = "pinhuoitem/user/GetMyFocusActivity";
        public static final String GET_NEW_FOCUS_LIST = "pinhuoitem/user/GetMyFocusActivity2";
        public static final String GET_FOCUS_STAT_LIST = "pinhuoitem/user/GetUserFocusActivity";
    }

    public static class UserMethod {

        private static final String USER_BASE = "user/";

        //扫描登录
        public static String SCAN_LOGIN = "user/user/ScanLogin";
        /**
         * 注册
         */
        public static String USER_REGISTER = USER_BASE + "user/register";

        /**
         * 注册新接口(后台生成用户名)
         */
        public static String USER_REGISTER2 = USER_BASE + "user/register2";
        /**
         * 登录
         */
        public static String USER_LOGIN = USER_BASE + "user/login";
        /**
         * 连接登录
         */
        public static String USER_CONNECT_LOGIN = USER_BASE + "connect/login";
        /**
         * 获取注册验证码
         */
        public static final String GET_SIGN_UP_VERIFY_CODE = "user/user/getmobileverifycode";

        /**
         * 获取注册验证码
         */
        public static final String NEW_GET_SIGN_UP_VERIFY_CODE = "user/user/getmobileverifycode2";

        public static final String VALIDATE_SIGN_UP_VERIFY_CODE = "user/user/checkmobileverifycode";

    }

    /**
     * @author JorsonWong
     * @description 购物车
     * @created 2015年4月2日 上午10:52:06
     */
    public static class ShopCartMethod {
        // private static final String CART_BASE = "shop/Cart/";

        private static final String SHOP_AGENT_CART = "shop/agent/Cart/";

        /** 加入购物车 */
//        public static final String  SHOP_AGENT_CART_ADD_ITEM       = SHOP_AGENT_CART + "AddItem";

        /**
         * 加入购物车
         */
        public static final String SHOP_CART_COUNT = "pinhuocart/GetTotalQty";

        /**
         * 购物车数量
         */
        public static final String SHOP_AGENT_CART_ADD_ITEMS = "pinhuocart/add";
        /**
         * 购物车数量
         */
        public static final String SHOP_AGENT_CART_UPDATE_ITEMS = "pinhuocart/update";

        /**
         * 设置商品购物车数量
         */
        public static final String SET_QTY = SHOP_AGENT_CART + "SetQty";

        /**
         * 获取购物车商品数量
         */
        public static final String CART_GET_ITEM_COUNT = SHOP_AGENT_CART + "GetItemCount";

        /**
         * 获取购物车列表
         */
        public static final String GET_ITEMS_FOR_ALL_SHOP = "pinhuocart/GetItems";

        /**
         * 获取购物车列表（购物车新接口,包含失效商品）
         */
        public static final String GET_ITEMS_FOR_ALL_SHOP2 = "pinhuocart/GetItems2";
        public static final String GET_ITEMS_FOR_ALL_SHOP3= "pinhuocart/GetItems3";

        /**
         * 创建临时订单
         */
        public static final String CREATE_TEMP_ORDER_FOR_ALL_SHOP = "pinhuo/order/CreateTempOrder";
        /**
         * 获取商品折扣
         */
        public static final String GET_ITEM_DISCOUNT_FOR_ALL_SHOP = "pinhuocart/GetItemDiscount";
        /**
         * 提交订单
         */
        public static final String SUBMIT_ORDER_LIST = "pinhuo/order/submit";

        /**
         * 批量删除购物车商品
         */
        public static final String REMOVE_ITEMS = SHOP_AGENT_CART + "RemoveItems";

        /**
         * 批量删除购物车商品(2.3新接口)
         */
        public static final String REMOVE_ITEMS2 = SHOP_AGENT_CART + "RemoveProductV2";
    }

    public static class ShopMethod {

        /**
         * 注册店铺
         */
        public static final String REGISTER_SHOP = "shop/shop/register";
        /**
         * 某供货商商品
         */
        public static final String GET_SHOP_ITEMS = "shop/agent/GetShopItems2";
        /**
         * 获取订单
         */
        public static final String GET_ORDER_RECORD = "buyertool/buyer/GetRecordQty2";
        public static final String GET_ORDER_RECORD_DETAIL = "buyertool/buyer/getrecordlistbyid";
        public static final String CLEAR_ORDER_RECORD_DETAIL = "buyertool/buyer/clearrecord";
        //public static final String ORDER_LIST = "pinhuobuyer/GetOrderListV3";
      //  public static final String ORDER_LIST = "pinhuoBuyer/GetOrderListV4";
        public static final String ORDER_LIST = "pinhuoBuyer/GetOrderListV5";
        //售后单
        public static final String GETCSORDERLIST = "pinhuobuyer/GetCSOrderList";
        public static final String ORDER_ACTIVITY_LIST = "pinhuoBuyer/GetActivityList";
        public static final String ORDER_GETORDERFOOTINFO = "pinhuobuyer/GetOrderFootInfo";
        /**
         * 获取地址列表
         */
        public static final String SHOP_ADDRESS_GET_ADDRESSES = "shop/address/GetAddresses";
        /**
         * 删除地址
         */
        public static final String SHOP_ADDRESS_DELETE = "shop/address/delete";

        /**
         * 增加收货地址
         */
        public static final String SHOP_ADDRESS_ADD = "shop/address/add";

        /**
         * 修改收货地址
         */
        public static final String SHOP_ADDRESS_UPDATE = "shop/address/update";
        /**
         * 获取商品基本信息
         */
        public static final String SHOP_GET_ITEM_BASE_INFO = "shop/agent/getitembaseinfo";

        /**
         * 获取所有待处理订单的数目
         */
        public static final String SHOP_AGENT_ORDER_GET_PENDING_ORDER_COUNT = "shop/agent/order/GetPendingOrderCount";
        /**
         * 获取店铺的商品分类
         */
        public static final String SHOP_AGENT_ITEMCAT_GETITEM_CATS = "shop/agent/itemcat/getitemcats";
        /**
         * 获取我的商品信息
         */
        public static final String SHOP_AGENT_GET_MY_ITEMS = "shop/agent/getmyitems";

        /**
         * 获取供货商商品
         */
        public static final String GET_AGENT_ITEMS = "shop/agent/getagentitems";
        /**
         * 删除商品
         */
        public static final String SHOP_AGENT_OFF_SHELF_ITEMS = "shop/agent/offShelfItems";
        /**
         * 获取商品详情
         */
        public static final String SHOP_AGENT_GET_ITEM_DETAIL = "shop/agent/getitemdetail";
        /**
         * 搜索供货商
         */
        public static final String SEARCH_MY_VENDORS = "shop/agent/GetMySuppliers";
        /**
         * 设置分享店铺方案
         */
        public static final String SET_SHOP_SHARE_DESC = "shop/agent/SetShopShareDesc";

    }

    public static class XiaoZuMethod {
        /**
         * 获取最新广播
         */
        public static final String XIAOZU_TOPIC_LATEST_BROADCAST = "xiaozu/topic/latest/60087";
    }

    public static class OrderDetailMethod {
        /**
         * 拿货单
         */
        public static final String GET_BUY_ORDER = "pinhuobuyer/OrderDetail";
        public static final String GET_ORDER_DETAILS = "pinhuobuyer/OrderDetailV2";
        /**
         *物流详情（new）
         */
        public static final String GET_PACKAGE_INFOV2 = "pinhuobuyer/GetPackageInfov2";
        /**
         * 代理单
         */
        public static final String GET_AGENT_ORDER = "shop/agent/order/GetAgentOrder4Seller";
        /**
         * 发货单
         */
        public static final String GET_SEND_GOODS_ORDER = "shop/agent/order/GetShipOrder";
        public static final String SaveReplenishmentRecord = "shop/agent/order/SaveReplenishmentRecord";

        /**
         * 售货单
         */
        public static final String GET_SLLER_ORDER = "shop/agent/order/GetSellOrder";
        /**
         * 子单
         */
        public static final String GET_CHILD_ORDER = "shop/agent/order/GetAgentOrder4Buyer";
        /**
         * 拣货单
         */
        public static final String GET_PICKING_ORDER = "shop/agent/order/GetPickingOrder";

    }

    public static class PayMethod {
        /**
         * 获取账号基本信息
         */
        public static final String GET_ACCOUNT_BASE_INFO = "pay/Account/GetAccountBaseInfo";
        /**
         * 设置支付密码
         */
        public static final String SET_PASSWORD = "pay/Account/SetPassword";
    }

    public static class OrderMethod {
        /**
         * 退款中数目
         */
        public static final String GET_REFUND_COUNT = "shop/agent/order/GetPendingRefundOrderCount";
        /**
         * 取消订单
         */
        public static final String CANCEL_ORDER = "shop/agent/order/CancelOrder";
        public static final String CANCEL_NEW_ORDER="pinhuobuyer/orderv2/Cancel";
        /**
         * 取消子订单
         */
        public static final String CANCEL_NEW_SUB_ORDER="pinhuobuyer/orderv2/CancelOrder";
        /**
         * 一键加入购物车接口（new）
         */
        public static final String AddItem_From_Order="pinhuocart/AddItemFromOrder";
        /**
         * 主订单确认收货接口（new）
         */
        public static final String Comfirm_Receipt="pinhuobuyer/orderv2/ComfirmReceipt";

        public static final String GET_TRANSFER = "pay/Funds/GetTransferTradeInfo";
        /**
         * 配货单
         */
        public static final String GET_SHIP_ITEMS = "shop/agent/order/getshipitems";
    }


    public static final class SearchMethod {

        public static final String GET_AGENT_ITEMS = "Shop/agent/GetAgentItems";
        public static final String GET_ITEM_HOT_KEYWORD = "Shop/agent/GetItemHotKeyword";

        public static final String GET_SHOP_INFO_LIST = "shop/shop/getShopInfoList";

        public static final String GET_HOT_SHOPS = "Shop/agent/GetItemHotUsers";

        public static final String GET_SHOP_ITEM2 = "shop/agent/GetShopItems2";


    }
}
