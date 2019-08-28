package com.nahuo.quicksale.model.http.api;

import com.nahuo.bean.AdBean;
import com.nahuo.bean.BalanceBean;
import com.nahuo.bean.CollectBean;
import com.nahuo.bean.DiscountBean;
import com.nahuo.bean.InvitatioCodeBean;
import com.nahuo.bean.LoginBean;
import com.nahuo.bean.MenuRedPointBean;
import com.nahuo.bean.NoticeBean;
import com.nahuo.bean.OrderMeBean;
import com.nahuo.bean.PackageReceivedBean;
import com.nahuo.bean.PageBean;
import com.nahuo.bean.PayTimeBean;
import com.nahuo.bean.ReFundBean;
import com.nahuo.bean.SaveApplyInfo;
import com.nahuo.bean.SearchPanelBean;
import com.nahuo.bean.ShopBaseConfig;
import com.nahuo.bean.ShopCartBean;
import com.nahuo.bean.SortBean;
import com.nahuo.bean.TradeBean;
import com.nahuo.bean.TxUserInfo;
import com.nahuo.live.xiaozhibo.model.GoodsBean;
import com.nahuo.live.xiaozhibo.model.LiveDetailBean;
import com.nahuo.live.xiaozhibo.model.LiveListBean;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.ActivityFreightBillModel;
import com.nahuo.quicksale.oldermodel.AgentGroup;
import com.nahuo.quicksale.oldermodel.ApplyListModel;
import com.nahuo.quicksale.oldermodel.AuthInfoModel;
import com.nahuo.quicksale.oldermodel.BalanceModel;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.oldermodel.ContactResultModel;
import com.nahuo.quicksale.oldermodel.CouponModel;
import com.nahuo.quicksale.oldermodel.GoodsCount;
import com.nahuo.quicksale.oldermodel.NewsListModel;
import com.nahuo.quicksale.oldermodel.PHQDModel;
import com.nahuo.quicksale.oldermodel.PostsListModel;
import com.nahuo.quicksale.oldermodel.ScoreModel;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.SubmitOrderResult;
import com.nahuo.quicksale.oldermodel.TempOrderV2;
import com.nahuo.quicksale.oldermodel.TopicDetailModel;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;
import com.nahuo.quicksale.oldermodel.TradeLogItem;
import com.nahuo.quicksale.oldermodel.UserModel;
import com.nahuo.quicksale.oldermodel.json.JAddress;
import com.nahuo.quicksale.oldermodel.json.JPayUser;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.orderdetail.model.TransferModel;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static com.nahuo.quicksale.api.HttpUtils.SERVERURL_Https_V4;
import static com.nahuo.quicksale.api.XiaoZuAPI.XIAOZU_GBZ;

/**
 * Created by jame on 2018/4/27.
 */

public interface PinHuoApi {
    String HOST = HttpUtils.BASE_Https_URL + HttpUtils.VERSION;
    String HTTP_V4 = SERVERURL_Https_V4;

    /**
     * 拼货列表
     *
     * @param cid 类目id
     */
    @GET("pinhuoitem/GetHomeActivityList2/")
    Flowable<PinHuoResponse<PinHuoNewResultModel>> getPinHuoNewList(
            @Query("cid") int cid, @Query("debug") String debug,
            @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize
    );

    /**
     * 获取栏目广告
     *
     * @param valueID
     * @param AreaTypeID 1:主页广告 2：弹窗广告 3：市场广告
     *                   1、from就是来源id，小程序之前已经加过了
     *                   2、verid=2是为了区别3.4版之前的广告还是3.5版之后的广告，因为新的这个幻灯片明显不能用于旧版本的，高度高了很多，中间有兼容过程
     */
    @GET("ads/GetBannersFormTypeV2/")
    Flowable<PinHuoResponse<AdBean>> getBannersFormTypeV2(
            @Query("AreaTypeID") int AreaTypeID, @Query("valueID") int valueID
            , @Query("from") int from, @Query("verid") int verid
    );

    /**
     * 首页弹窗广告
     *
     * @param version    版本号
     * @param AreaTypeID 2：弹窗广告
     */
    @GET("ads/GetBannersFormType/")
    Flowable<PinHuoResponse<List<BannerAdModel>>> getBannersFormType(
            @Query("AreaTypeID") int AreaTypeID, @Query("ver") int version
    );

    /**
     * 分类
     */
    @GET("pinhuoitem/activity/GetRecommendList")
    Flowable<PinHuoResponse<SortBean>> getRecommendList(

    );

    /**
     * 购物车列表
     */
    @GET("pinhuocart/GetItems3/")
    Flowable<PinHuoResponse<ShopCartBean>> getItems3(
            @Query("debug") @Nullable String debug
    );

    /**
     * 获取拿货车计算价格
     *
     * @param version 2计算折扣 ,3结算
     */
    @FormUrlEncoded
    @POST("pinhuocart/GetItemDiscount/")
    Flowable<PinHuoResponse<DiscountBean>> getItemDiscount(
            @Field("itemInfos") String itemInfos, @Field("version") int version
    );

    /**
     * 获取拿货车结算
     *
     * @param version 2计算折扣 ,3结算
     */
    @FormUrlEncoded
    @POST("pinhuo/order/CreateTempOrder/")
    Flowable<PinHuoResponse<TempOrderV2>> createTempOrder(
            @Field("itemInfos") String itemInfos, @Field("version") int version
            , @Field("coin") int coin);

    /**
     * 获取拿货车结算
     *1
     * @param version 2计算折扣 ,3结算
     */
    @FormUrlEncoded
    @POST("pinhuo/order/CreateTempOrder/")
    Flowable<PinHuoResponse<TempOrderV2>> createTempOrder(
            @Field("itemInfos") String itemInfos, @Field("version") int version, @Nullable @Field("AddressID") int AddressID
            , @Field("CouponID") int CouponID, @Field("coin") int coin
    );

    /**
     * 提交订单
     */
    @POST("pinhuo/order/submit/")
    Flowable<PinHuoResponse<SubmitOrderResult>> submitTempOrder(
            @Query("addressId") int addressId, @Query("buyerRemark") String buyerRemark,
            @Query("CouponID") int CouponID
            , @Query("shipTypeID") int shipTypeID
            , @Query("shipRemark") String shipRemark
            , @Query("from") String from, @Query("coin") int coin

    );

    /**
     * 获取公告通知
     *
     * @param areaID 1 拿货车里公告
     */
    @GET("pinhuoitem/getcurrentnotice/")
    Flowable<PinHuoResponse<List<NoticeBean>>> getCurrentNotice(
            @Query("areaID") int areaID
    );

    /**
     * 拼货场次列表
     *
     * @param filterValues string    筛选json字符串。。和场次商品接口结构一致
     */
    @GET("pinhuoitem/GetItemsV2/")
    Flowable<PinHuoResponse<RecommendModel>> getItemsV2(
            @Query("qsid") int qsid,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("keyword") String keyword,
            @Query("sort") @Nullable String sort,
            @Query("itemCat") int itemCat,
            @Query("displayMode") int displayMode,
            @Query("filterValues") String filterValues);


    /**
     * 拼货商品详情
     */
    @GET("pinhuoitem/detail2/")
    Flowable<PinHuoResponse<Object>> getPinhuoDetail(
            @Query("id") int id, @Query("qsid") int qsid);

    /**
     * 拿货车修改
     */
    @POST("pinhuocart/update/")
    Flowable<PinHuoResponse<Object>> updatePinHuocart(
            @Body RequestBody requestBody);

    /**
     * 拿货车添加
     */
    @POST("pinhuocart/add/")
    Flowable<PinHuoResponse<Object>> addPinHuocart(
            @Query("data") String data);

//    /**
//     * 订单退款信息
//     */
//    @GET("shop/agent/refund/GetOrderItemForRefund/")
//    Flowable<PinHuoResponse<ReFundBean>> getOrderItemForRefund(
//            @Query("oid") int id);

    /**
     * 保存用户发货配置
     */
    @GET("pinhuobuyer/SaveOrderShipConfig/")
    Flowable<PinHuoResponse<Object>> saveOrderShipConfig(
            @Query("phOrderID") int phOrderID);
    /**
     * 订单退款信息
     */
    @GET("pinhuobuyer/OrderV2/GetOrderRefundInfo/")
    Flowable<PinHuoResponse<ReFundBean>> getOrderItemForRefund(
            @Query("orderID") int id);
    /**
     * 获取订单退款交易信息
     */
    @GET("pay/Funds/GetTransferTradeInfo/")
    Flowable<PinHuoResponse<TransferModel>> GetTransferTradeInfo(
            @Query("transferID") int id);

    /**
     * 获取订单退款交易信息
     */
    @GET("pay/GetPostFeeTradeFlow/")
    Flowable<PinHuoResponse<TradeLogItem.TradeList>> GetTradeInfo4PostFee(
            @Query("transferID") int id, @Query("settleAmount") double settleAmount);

//    /**
//     * 订单退款
//     */
//    @POST("shop/agent/refund/BuyerApplyRefund/")
//    Flowable<PinHuoResponse<Object>> buyerApplyRefund(
//            @QueryMap Map<String,Object> map);

    /**
     * 订单退款
     */
    @POST("pinhuobuyer/orderv2/BuyerApplyRefund/")
    Flowable<PinHuoResponse<Object>> buyerApplyRefund(
            @QueryMap Map<String, Object> map);

    /**
     * 订单退款买家结算退款
     */
    @POST("pinhuobuyer/orderv2/BuyerApplySettleRefund/")
    Flowable<PinHuoResponse<Object>> buyerApplySettleRefund(
            @QueryMap Map<String, Object> map);

    /**
     * 获取地址
     */
    @GET("shop/address/GetAddresses")
    Flowable<PinHuoResponse<List<JAddress>>> getAddresses(
    );

    /**
     * 获取订单红点数目
     */
    @GET("pinhuobuyer/GetOrderStatuCount")
    Flowable<PinHuoResponse<OrderMeBean>> getOrderStatuCount(
    );

    /**
     * 获取用户认证状态
     */
    @GET("pinhuobuyer/GetAuthInfoStatu")
    Flowable<PinHuoResponse<AuthInfoModel>> getAuthInfoStatu(
    );

    /**
     * 获取用户信息，包括用户名，邮箱，积分等等
     */
    @GET(HTTP_V4 + "user/user/GetMyUserInfo")
    Flowable<PinHuoResponse<ScoreModel>> getMyUserInfo(
    );

    /**
     * 获取用户信息，包括用户名，邮箱，积分等等
     */
    @GET(HTTP_V4 + "user/user/GetMyUserInfo")
    Flowable<PinHuoResponse<UserModel>> getUserInfo(
    );

    /**
     * 商品访问记录
     */
    @GET("pinhuoitem/SetItemVisitRecord")
    Flowable<PinHuoResponse<Object>> setItemVisitRecord(
            @Query("id") int id);

    /**
     * 账号登录
     */
    @FormUrlEncoded
    @POST(HTTP_V4 + "user/user/login")
    Flowable<PinHuoResponse<LoginBean>> getLoginData(@Field("account") String account,
                                                     @Field("password") String password,
                                                     @Field("isEncode") boolean isEncode,
                                                     @Field("loginFrom") int loginFrom,
                                                     @Field("IMEI") String IMEI,
                                                     @Field("PhoneName") String PhoneName,
                                                     @Field("OS") String OS,
                                                     @Field("Network") String Network
            , @Field("deviceID") String deviceID
            , @Field("channelCode") String channelCode
    );

    /**
     * 短信登录
     */
    @FormUrlEncoded
    @POST(HTTP_V4 + "user/user/login")
    Flowable<PinHuoResponse<LoginBean>> getSMSLoginData(@Field("account") String account,
                                                        @Field("mobileCode") String mobileCode,
                                                        @Field("isEncode") boolean isEncode,
                                                        @Field("loginFrom") int loginFrom,
                                                        @Field("IMEI") String IMEI,
                                                        @Field("PhoneName") String PhoneName,
                                                        @Field("OS") String OS,
                                                        @Field("Network") String Network
            , @Field("deviceID") String deviceID
            , @Field("channelCode") String channelCode,@Field("invitationCode") String invitationCode
    );

    /**
     * 注册账号
     */
    @FormUrlEncoded
    @POST(HTTP_V4 + "user/user/register2")
    Flowable<PinHuoResponse<LoginBean>> getRegister2(
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("isEncode") boolean isEncode,
            @Field("code") String code,
            @Field("regFrom") int regFrom,
            @Field("deviceID") String deviceID,
            @Field("channelCode") String channelCode
            ,@Field("invitationCode") String invitationCode
    );

    /**
     * 注册店铺
     */
    @POST("shop/shop/register/")
    Flowable<PinHuoResponse<ShopInfoModel>> registerShop(
            @Query("name") String name, @Query("mobile") String mobile);

    /**
     * 注册店铺
     */
    @POST("shop/shop/register/")
    Flowable<PinHuoResponse<ShopInfoModel>> registerShop(
            @Query("name") String name, @Query("mobile") String mobile
            , @Query("qq") String qq, @Query("qq_name") String qq_name, @Query("wx") String wx, @Query("wx_nme") String wx_nme);

    /**
     * 获取店铺
     */
    @GET("shop/shop/getshopinfo")
    Flowable<PinHuoResponse<ShopInfoModel>> getShopInfo(
    );

    /**
     * 获取基本设置-零售价修改开关
     */
    @GET("shop/agent/GetMyShopBaseConfig")
    Flowable<PinHuoResponse<ShopBaseConfig>> getMyShopBaseConfig(
    );

    /**
     * 获取支付用户信息
     */
    @POST("pay/Account/GetAccountBaseInfo")
    Flowable<PinHuoResponse<JPayUser>> getAccountBaseInfo(
            @QueryMap Map<String, String> map
    );
//    @Query("partner") String partner, @Query("user_name") String user_name,
//    @Query("time_stamp") String time_stamp, @Query("nonce") String nonce,
//    @Query("sign") String sign

    /**
     * 获取余额
     */
    @GET("pay/Account/GetBalance4PinHuo")
    Flowable<PinHuoResponse<BalanceModel>> getBalance4PinHuo(
            @QueryMap Map<String, String> map
    );

    /**
     * 存储设备设置号，每次启动的时候进行调用
     *
     * @author Chiva Liang
     */
    @POST(HTTP_V4 + "user/user/SaveDeviceInfo")
    Flowable<PinHuoResponse<Object>> saveDeviceInfo(
            @Query("from") int from, @Query("userID") int userID
            , @Query("deviceID") String deviceID, @Query("channelCode") String channelCode
    );

    /**
     * 获取ECC数据
     *
     * @author Chiva Liang
     */
    @GET("user/eccbuyer/assigneccbuyer")
    Flowable<PinHuoResponse<Object>> getAssigneccbuyer(

    );

    /**
     * 分类列表
     *
     * @param filterValues string    筛选json字符串。。和场次商品接口结构一致
     */
    @GET("pinhuoitem/SearchV2/")
    Flowable<PinHuoResponse<RecommendModel>> getSearchV2(
            @Query("rid") int rid,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("keyword") String keyword,
            @Query("sort") @Nullable String sort,
            @Query("valueIDS") String valueIDS,
            @Query("displayMode") int displayMode,
            @Query("filterValues") String filterValues);

    /**
     * 筛选列表
     */
    @POST("pinhuoitem/GetSearchPanel/")
    Flowable<PinHuoResponse<SearchPanelBean>> getSearchPanel(
            @Query("AreaID") int AreaID, @Query("Datas") String Datas
    );

    /**
     * 筛选列表
     */
    @POST("pinhuoitem/GetSearchPanel/")
    Flowable<PinHuoResponse<SearchPanelBean>> getSearchPanel(
            @Query("AreaID") int AreaID, @Query("Datas") String Datas,
            @Query("displaystatuid ") int displaystatuid
    );

    /**
     * 获取ECC是否注册
     *
     * @author Chiva Liang
     */
    @GET("user/ImUser/GetImUserinfo")
    Flowable<PinHuoResponse<Object>> getImUserinfo(@Query("userid") String userid
    );

    /**
     * 分类列表
     *
     * @param filterValues string    筛选json字符串。。和场次商品接口结构一致
     */
    @GET("pinhuoitem/GetItem4StallID/")
    Flowable<PinHuoResponse<RecommendModel>> getItem4StallID(
            @Query("sid") int sid,
            @Query("pageIndex") int pageIndex,
            @Query("pageSize") int pageSize,
            @Query("sort") @Nullable String sort,
            @Query("displayMode") int displayMode,
            @Query("filterValues") String filterValues);

    /**
     * 获取发货方式
     */
    @GET("pinhuobuyer/GetApplyList/")
    Flowable<PinHuoResponse<ApplyListModel>> getApplyList(
            @Query("WareHouseID") String WareHouseID
    );

    /**
     * 获取发货方式保存
     */
    @GET("pinhuobuyer/SaveApplyInfo4Order/")
    Flowable<PinHuoResponse<SaveApplyInfo>> saveApplyInfo4Order(
            @Query("wareHouseIDS") String wareHouseIDS, @Query("typeID") int typeID
            , @Query("value") int value
    );

    /**
     * 拿货车移除商品
     */
    @POST("pinhuocart/delete/")
    Flowable<PinHuoResponse<Object>> deleteShopCart(
            @Query("ids") String ids
    );

    /**
     * 获取联系人
     */
    @POST("shop/shop/GetContactInfoList/")
    Flowable<PinHuoResponse<ContactResultModel>> getContactInfoList(
            @QueryMap Map<String, Object> map
    );

    /**
     * 获取店铺
     */
    @POST("shop/agent/GetShopInfo/")
    Flowable<PinHuoResponse<Object>> getShopInfo2(
    );

    /**
     * 获取运费信息
     */
    @POST("pinhuobuyer/PostFeeBillList/")
    Flowable<PinHuoResponse<ActivityFreightBillModel>> getPostFeeBillList(
            @QueryMap Map<String, Object> map
    );

    /**
     * 获取包裹信息
     */
    @POST("pinhuobuyer/GetPackageList/")
    Flowable<PinHuoResponse<PageBean>> getPackageList(
            @QueryMap Map<String, Object> map
    );

    /**
     * 获取包裹信息
     */
    @POST("xiaozu/topic/list/" + XIAOZU_GBZ + "/{pageIndex}/{pageSize}")
    Flowable<PinHuoResponse<List<NewsListModel>>> getXiaoZuTopicList(
            @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize
    );
    /**
     * 获取小组评论
     */
    @POST("xiaozu/topic/posts/list" + "/{tid}/{pageIndex}/{pageSize}")
    Flowable<PinHuoResponse<List<PostsListModel>>> getTopicPostsListInfo(
            @Path("tid") int tid,  @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize
    );
    /**
     * 获取活动评论
     */
    @POST("xiaozu/activity/posts/list" + "/{tid}/{pageIndex}/{pageSize}")
    Flowable<PinHuoResponse<List<PostsListModel>>> getActivityPostsListInfo(
            @Path("tid") int tid,  @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize
    );
    /**
     * 获取活动收藏
     */
    @POST("xiaozu/activity/collect/")
    Flowable<PinHuoResponse<Object>> collecXiaoZutActivity(
            @Query("id") int id
    );
    /**
     * 获取小组收藏
     */
    @POST("xiaozu/topic/collect/")
    Flowable<PinHuoResponse<Object>> collecXiaoZutTopic(
            @Query("id") int id
    );
    /**
     * 获取优惠券信息
     */
    @POST("pinhuobuyer/GetCouponList/")
    Flowable<PinHuoResponse<List<CouponModel>>> getCouponList(
            @QueryMap Map<String, Object> map
    );

    /**
     * 获取配货清单
     */
    @POST("pinhuobuyer/GetAllotList")
    Flowable<PinHuoResponse<List<PHQDModel>>> getAllotList(
    );

    /**
     * 获取换货币
     */
    @GET("pay/coin/GetBalance/")
    Flowable<PinHuoResponse<BalanceBean>> getBalance(
    );

    /**
     * 获取菜单栏红点提醒
     */
    @GET("pinhuocart/GetMenuRedPoint/")
    Flowable<PinHuoResponse<MenuRedPointBean>> getMenuRedPoint(
    );

    /**
     * 检查身份验证接口
     */
    @GET(HTTP_V4 + "user/user/CheckIDCard/")
    Flowable<PinHuoResponse<Object>> checkIDCard(
            @Query("idcode") String idcode
    );

    /**
     * 获取验证码
     */
    @POST(HTTP_V4 + "user/user/getmobileverifycode2/")
    Flowable<PinHuoResponse<Object>> getMobileVerifyCode2(
            @QueryMap Map<String, Object> map
    );

    /**
     * 检查手机和验证码
     */
    @POST(HTTP_V4 + "user/user/checkmobileverifycode/")
    Flowable<PinHuoResponse<Object>> checkMobileVerifyCode(
            @QueryMap Map<String, Object> map
    );

    /**
     * 重置支付密码
     */
    @POST("pay/account/ReSetPassword/")
    Flowable<PinHuoResponse<Object>> reSetPayPassword(
            @QueryMap Map<String, String> map
    );

    /**
     * 设置支付密码
     */
    @POST("pay/account/SetPassword/")
    Flowable<PinHuoResponse<Object>> setPayPassword(
            @Body RequestBody requestBody);

    /**
     * 开通衣付通
     */
    @POST("pay/Account/SetSettlement/")
    Flowable<PinHuoResponse<Object>> setSettlement(
            @QueryMap Map<String, String> map
    );

    /**
     * 开通衣付通
     */
    @POST("pay/common/GetButtom4APPMyBag/")
    Flowable<PinHuoResponse<TradeBean>> getButtom4APPMyBag(

    );

    /**
     * 获取交易记录
     */
    @POST("pay/trade/GetTradeList4PinHuoV2/")
    Flowable<PinHuoResponse<TradeLogItem>> getTradeList4PinHuo(
            @QueryMap Map<String, Object> map
    );

    /**
     * 我的收藏
     */
    @POST("pinhuobuyer/item/GetFavoriteItem/")
    Flowable<PinHuoResponse<List<CollectBean>>> GetFavoriteItem(
            @QueryMap Map<String, Object> map
    );

    /**
     * 约拼获取帖子信息
     */
    @GET("xiaozu/topic/list_v2/{gid}/{pageIndex}/{pageSize}")
    Flowable<PinHuoResponse<List<TopicInfoModel>>> getList_v2(
            @Path("gid") int gid, @Path("pageIndex") int pageIndex, @Path("pageSize") int pageSize
    );
    /**
     * 约拼获取小组信息
     */
    @GET("xiaozu/group/{gid}")
    Flowable<PinHuoResponse<AgentGroup>> getGroup(
            @Path("gid") int gid
    );
    /**
     * 约拼加入小组
     */
    @POST("xiaozu/group/join")
    Flowable<PinHuoResponse<Object>> joinGroup(
            @QueryMap Map<String, Object> map
    );
    /**
     * 小组活动
     */
    @GET("xiaozu/activity/{activityID}")
    Flowable<PinHuoResponse<TopicDetailModel>> getActivityDetailInfo(
            @Path("activityID") int activityID
    );
    /**
     * 小组帖子
     */
    @GET("xiaozu/topic/{activityID}")
    Flowable<PinHuoResponse<TopicDetailModel>> getTopicDetailInfo(
            @Path("activityID") int activityID
    );
    /**
     * 获取客服问题
     */
    @GET("pinhuobuyer/CS/GetTopicList")
    Flowable<PinHuoResponse<Object>> GetTopicList(

    );
    /**
     * 获取ECC数据
     */
    @GET("user/eccbuyer/getsendext")
    Flowable<PinHuoResponse<Object>> getSendext(
    );
    /**
     * 上传ECC数据
     */
    @POST("user/eccbuyer/SaveImMessage")
    Flowable<PinHuoResponse<Object>> saveImMessage(
            @Query("msg")String msg
    );
    /**
     * 点收货物商品列表
     */
    @GET("pinhuobuyer/GetPackageProductList")
    Flowable<PinHuoResponse<PackageReceivedBean>> GetPackageProductList(
            @Query("shipID") int  shipID
    );
    /**
     * 点货确认接口
     */
    @POST("pinhuobuyer/ReceivedProduct")
    Flowable<PinHuoResponse<Object>> goToReceivedProduct(
            @Query("shipID")int  shipID, @Query("orderProductID")String  orderProductID
    );
    /**
     * 获取支付倒计时
     */
    @GET("pinhuo/order/GetPayViewInfo")
    Flowable<PinHuoResponse<PayTimeBean>> getPayViewInfo(
            @Query("ordercode")String ordercode
    );
    /*-----------------------------------------*/
    /**
     * 拼货列表
     *
     * @param
     */
    @GET("pinhuoitem/live/getlivelist/")
    Flowable<PinHuoResponse<LiveListBean>> getLiveList(
    );
    /**
     * TX账号
     *
     * @param
     */
    @GET(HTTP_V4+"user/ImUser/GetTXUserInfo/")
    Flowable<PinHuoResponse<TxUserInfo>> getTXUserInfo(
            @QueryMap Map<String, Object> map
    );

    /**
     * 拼货详情
     *
     * @param
     */
    @GET("pinhuoitem/live/getliveitem/")
    Flowable<PinHuoResponse<LiveDetailBean>> getLiveItem(
            @Query("id") int id
    );
//    /**
//     * 拼货详情
//     *
//     * @param
//     */
//    @GET("buyertool/BuyerV2/GetBaseInfo2/")
//    Flowable<PinHuoResponse<BaseInfoList>> GetBaseInfo2(
//            @QueryMap Map<String, Object> map
//    );

    /**
     * 拼货录制
     *
     * @param
     */
    @GET("buyertool/live/CreateLiveRecord/")
    Flowable<PinHuoResponse<Object>> createLiveRecord(
    );
    /**
     * 拼货结束录制
     *
     * @param
     */
    @GET("buyertool/live/StopLiveRecord/")
    Flowable<PinHuoResponse<Object>> stopLiveRecord(
    );
    /**
     * 设置是否显示商品
     *
     * @param
     */
    @POST("buyertool/live/SetIsShow/")
    Flowable<PinHuoResponse<Object>> setIsShow(
            @Query("id") int id,@Query("isShowItem") boolean isShowItem
    );
    /**
     * 设设置是否允许观众可以开始观看直播
     *
     * @param
     */
    @POST("buyertool/live/SetIsWatch/")
    Flowable<PinHuoResponse<Object>> setIsWatch(
            @Query("id") int id,@Query("isStartWatch") boolean isStartWatch
    );
    /**
     * 拼货详情
     *
     * @param
     */
    @GET("pinhuoitem/live/GetLiveItemAllGoods")
    Flowable<PinHuoResponse<GoodsBean>> getLiveItemAllGoods(
            @Query("id") int id
    );
    /**
     * 、设置哪个商品为试穿中
     *
     * @param
     */
    @POST("buyertool/live/SetOnTry/")
    Flowable<PinHuoResponse<Object>> setOnTry(
            @Query("id") int id,@Query("agentItemID") int agentItemID
    );
    /**
     *取消试穿中的商品
     *
     * @param
     */
    @POST("buyertool/live/CancelOnTry/")
    Flowable<PinHuoResponse<Object>> cancelOnTry(
            @Query("id") int id,@Query("agentItemID") int  agentItemID
    );
    /**
     * 购物车红点
     */
    @GET("pinhuocart/GetTotalQty/")
    Flowable<PinHuoResponse<GoodsCount>> GetTotalQty(
    );
    /**
     * 购物车红点
     */
    @GET(HTTP_V4+"user/config/index/")
    Flowable<PinHuoResponse<InvitatioCodeBean>> getConfigIndex(
    );
}
