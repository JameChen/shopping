package com.nahuo.quicksale.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.baidu.mobstat.StatService;
import com.nahuo.Dialog.ReFundOderDialog;
import com.nahuo.bean.ReFundBean;
import com.nahuo.live.xiaozhibo.common.TCLiveRoomMgr;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.mainui.LiveListActivity;
import com.nahuo.live.xiaozhibo.model.LiveDetailBean;
import com.nahuo.live.xiaozhibo.model.LiveListBean;
import com.nahuo.live.xiaozhibo.play.TCLivePlayerActivity;
import com.nahuo.live.xiaozhibo.play.TCVodPlayerActivity;
import com.nahuo.live.xiaozhibo.push.camera.TCLivePublisherActivity;
import com.nahuo.quicksale.CheckPermissionsActivity;
import com.nahuo.quicksale.GuideActivity;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.StartActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.hyphenate.EaseUI;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import de.greenrobot.event.EventBus;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by jame on 2018/3/28.
 */

public class BaseAppCompatActivity extends CheckPermissionsActivity {
    protected CompositeDisposable mCompositeDisposable;
    protected Activity activity;
    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    public void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        if (!this.getClass().getSimpleName().equals(MainNewActivity.class.getSimpleName())) {
            BWApplication.addActivity(this);
        }
        if (this.getClass().getSimpleName().equals(MainNewActivity.class.getSimpleName()) ||
                this.getClass().getSimpleName().equals(ItemDetailsActivity.class.getSimpleName())
                || this.getClass().getSimpleName().equals(StartActivity.class.getSimpleName())
                || this.getClass().getSimpleName().equals(GuideActivity.class.getSimpleName())
                || this.getClass().getSimpleName().equals(WXEntryActivity.class.getSimpleName())){
            transparencyBar(this);
        }
        if (!this.getClass().getSimpleName().equals(MainNewActivity.class.getSimpleName())
                & !this.getClass().getSimpleName().equals(LiveListActivity.class.getSimpleName())
                & !this.getClass().getSimpleName().equals(TCVodPlayerActivity.class.getSimpleName())
                & !this.getClass().getSimpleName().equals(TCLivePublisherActivity.class.getSimpleName()
        ) & !this.getClass().getSimpleName().equals(TCLivePlayerActivity.class.getSimpleName())) {
            BWApplication.addVActivity(this);
        }
    }

    public void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            setSystemBarTint(activity);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setSystemBarTint(activity);
        }
    }

    public void setSystemBarTint(Activity activity) {

//        SystemBarTintManager tintManager = new SystemBarTintManager(this);
//// 激活状态栏设置
//        tintManager.setStatusBarTintEnabled(true);
//// 激活导航栏设置
//        tintManager.setNavigationBarTintEnabled(true);
//// 设置一个颜色给系统栏
//        tintManager.setTintColor(ContextCompat.getColor(activity,R.color.my_colorPrimary));
    }

    public void setBarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            ToolbarUtil.createStatusBarView(this,R.color.transparent,1);
//            //透明导航栏
//          //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            // 创建状态栏的管理实例
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//// 激活状态栏设置
//            tintManager.setStatusBarTintEnabled(true);
//// 激活导航栏设置
//            tintManager.setNavigationBarTintEnabled(true);
//// 设置一个颜色给系统栏
//            tintManager.setTintColor(Color.parseColor("#40C4FF"));
        }
    }

    @TargetApi(19)
    public void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup decorViewGroup = (ViewGroup) activity.getWindow().getDecorView();
            //获取自己布局的根视图
            View rootView = ((ViewGroup) (decorViewGroup.findViewById(android.R.id.content))).getChildAt(0);
            //预留状态栏位置
            rootView.setFitsSystemWindows(true);

            //添加状态栏高度的视图布局，并填充颜色
            View statusBarTintView = new View(activity);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    getInternalDimensionSize(activity.getResources(), "status_bar_height"));
            params.gravity = Gravity.TOP;
            statusBarTintView.setLayoutParams(params);
            statusBarTintView.setBackgroundColor(color);
            decorViewGroup.addView(statusBarTintView);
        }
    }
    /**
     * 获取直播间
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    public void goLiveItem(String id) throws Exception{
        if (TextUtils.isEmpty(id)) {
            ViewHub.showShortToast(activity, "直播场次id为空");
            return;
        }
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi("goLiveItem"
        ).getLiveItem(Integer.parseInt(id))
                .compose(RxUtil.<PinHuoResponse<LiveDetailBean>>rxSchedulerHelper())
                .compose(RxUtil.<LiveDetailBean>handleResult())
                .subscribeWith(new CommonSubscriber<LiveDetailBean>(activity, true, R.string.loading) {
                    @Override
                    public void onNext(LiveDetailBean bean) {
                        super.onNext(bean);
                        if (bean == null) {
                            ViewHub.showShortToast(activity, "直播场次详细为空");
                            return;
                        }
                        String AnchorUserName = bean.getAnchorUserName();
                        int AnchorUserID = bean.getAnchorUserID();
                        String RoomID = bean.getRoomID();
                        String Title = bean.getTitle();
                        int WatchCount = bean.getWatchCount();
                        String LiveUrl = bean.getLiveUrl();
                        String liveUrl = bean.getLiveUrl();
                        switch (bean.getStatuID()) {
                            case LiveListBean.Type_ProcessLiveList:
                                TCLiveRoomMgr.getLiveRoom().setRoomList(bean);
                                if (Utils.isHasTxIdentifier(activity)) {
                                    Intent intent = new Intent(activity, TCLivePlayerActivity.class);
                                    intent.putExtra(TCConstants.PLAY_URL, liveUrl);
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.PUSHER_ID, AnchorUserID + "");
                                    intent.putExtra(TCConstants.PUSHER_NAME, AnchorUserName);
                                    intent.putExtra(TCConstants.PUSHER_AVATAR, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.HEART_COUNT, "" + 0);
                                    intent.putExtra(TCConstants.MEMBER_COUNT, "" + WatchCount);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.PLAY_TYPE, true);
                                    intent.putExtra(TCConstants.FILE_ID, "");
                                    intent.putExtra(TCConstants.COVER_PIC, "");
                                    intent.putExtra(TCConstants.TIMESTAMP, System.currentTimeMillis());
                                    intent.putExtra(TCConstants.ROOM_TITLE, "");
                                  /*  intent.putExtra(TCConstants.ROOM_TITLE,
                                            "");
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.USER_ID, SpManager.getIdentifier(Vthis));
                                    intent.putExtra(TCConstants.USER_NICK, AnchorUserName);
                                    intent.putExtra(TCConstants.USER_HEADPIC, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.COVER_PIC, Const.getShopLogo(AnchorUserID));
//                        intent.putExtra(TCConstants.SCR_ORIENTATION, mOrientation);
                                    intent.putExtra(TCConstants.BITRATE, mBitrateType);
                                    intent.putExtra(TCConstants.USER_LOC,
                                            "");
                                    //intent.putExtra(TCConstants.SHARE_PLATFORM, mShare_meidia);*/
                                    startActivity(intent);
                                } else {
                                    ViewHub.showShortToast(activity, "直播账号为空");
                                }
                                break;
                            case LiveListBean.Type_PreviewLiveList:
                                if (bean.isCanWatch()) {
                                    TCLiveRoomMgr.getLiveRoom().setRoomList(bean);
                                    Intent intent = new Intent(activity, TCLivePublisherActivity.class);
                                    intent.putExtra(TCConstants.ROOM_TITLE,
                                            "");
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.USER_ID, SpManager.getIdentifier(activity));
                                    intent.putExtra(TCConstants.USER_NICK, AnchorUserName);
                                    intent.putExtra(TCConstants.USER_HEADPIC, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.COVER_PIC, Const.getShopLogo(AnchorUserID));
//                        intent.putExtra(TCConstants.SCR_ORIENTATION, mOrientation);
                                    intent.putExtra(TCConstants.BITRATE, TCConstants.BITRATE_NORMAL);
                                    intent.putExtra(TCConstants.USER_LOC,
                                            "");
                                    //intent.putExtra(TCConstants.SHARE_PLATFORM, mShare_meidia);
                                    startActivity(intent);
                                } else {
                                    ViewHub.showLongToast(activity, bean.getMsg());
                                }
                                break;
                            case LiveListBean.Type_OverLiveList:
                                if (Utils.isHasTxIdentifier(activity)) {
                                    Intent intent = new Intent(activity, TCVodPlayerActivity.class);
                                    // Intent intent = new Intent(Vthis, TCLivePlayerActivity.class);
                                    // intent.putExtra(TCConstants.PLAY_URL, TextUtils.isEmpty(item.hlsPlayUrl) ? item.playurl : item.hlsPlayUrl);
                                    intent.putExtra(TCConstants.PLAY_URL, LiveUrl);
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.PUSHER_ID, SpManager.getIdentifier(activity));
                                    intent.putExtra(TCConstants.PUSHER_NAME, AnchorUserName);
                                    intent.putExtra(TCConstants.PUSHER_AVATAR, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.HEART_COUNT, "" + 0);
                                    intent.putExtra(TCConstants.MEMBER_COUNT, "" + WatchCount);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.PLAY_TYPE, false);
                                    intent.putExtra(TCConstants.FILE_ID, "");
                                    intent.putExtra(TCConstants.COVER_PIC, "");
                                    intent.putExtra(TCConstants.TIMESTAMP, "");
                                    intent.putExtra(TCConstants.ROOM_TITLE, Title);
                                    //startActivityForResult(intent, START_LIVE_PLAY);
                                    startActivity(intent);
                                } else {
                                    ViewHub.showLongToast(activity, "直播账号为空");
                                }
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }
    public static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 获取订单退款信息
     *
     * @author James Chen
     * @create time in 2018/5/9 14:31
     */
    public void saveOrderShipConfig(final Context context, final int oid) {
        if (oid < 0)
            return;
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi("saveOrderShipConfig")
                .saveOrderShipConfig(oid).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult()).subscribeWith(new CommonSubscriber<Object>(context, true, R.string.loading) {
                    @Override
                    public void onNext(final Object reFundBean) {
                        super.onNext(reFundBean);
                        try {
                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_BUYER_AGRESS, "ok"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }
    /**
     * 获取订单退款信息
     *
     * @author James Chen
     * @create time in 2018/5/9 14:31
     */
    public void getOrderItemForRefund(final Context context, final int oid) {
        if (oid < 0)
            return;
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi("getOrderItemForRefund")
                .getOrderItemForRefund(oid).compose(RxUtil.<PinHuoResponse<ReFundBean>>rxSchedulerHelper())
                .compose(RxUtil.<ReFundBean>handleResult()).subscribeWith(new CommonSubscriber<ReFundBean>(context, true, R.string.loading) {
                    @Override
                    public void onNext(final ReFundBean reFundBean) {
                        super.onNext(reFundBean);
                        if (reFundBean != null) {
                            String styledText;
                            if (TextUtils.isEmpty(reFundBean.getCoin())) {
                                styledText = "已付商品货款：<font color='red'>¥" + reFundBean.getProductAmount() + "</font>，分摊运费：<font color='red'>¥"
                                        + reFundBean.getPostFee() + "</font>，总计可退：<font color='#09F709'>¥" + reFundBean.getTotalRefundAmount() + "</font>。";
                            } else {
                                styledText = "已付商品货款：<font color='red'>¥" + reFundBean.getProductAmount() + "</font>，换货币：<font color='red'>¥" + reFundBean.getCoin() + "</font>，分摊运费：<font color='red'>¥"
                                        + reFundBean.getPostFee() + "</font>，总计可退：<font color='#09F709'>¥" + reFundBean.getTotalRefundAmount() + "</font>。";
                            }
                            ReFundOderDialog dialog = new ReFundOderDialog((Activity) context);
                            dialog.setHasTittle(true).setMessage(Html.fromHtml(styledText))
                                    .setPositive("确认退款", new ReFundOderDialog.PopDialogListener() {
                                        @Override
                                        public void onPopDialogButtonClick(int which, ReFundOderDialog oderDialog) {
                                            buyerApplyRefund(context, oid, oderDialog);
                                        }
                                    }).setNegative("我再想想", null).show();
                        }
                    }
                }));
    }

    /**
     * 获取订单退款信息
     *
     * @author James Chen
     * @create time in 2018/5/9 14:31
     */
    public void getRefundAndBuyerApplySettleRefund(final Context context, final int oid) {
        if (oid < 0)
            return;
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi("getOrderItemForRefund")
                .getOrderItemForRefund(oid).compose(RxUtil.<PinHuoResponse<ReFundBean>>rxSchedulerHelper())
                .compose(RxUtil.<ReFundBean>handleResult()).subscribeWith(new CommonSubscriber<ReFundBean>(context, true, R.string.loading) {
                    @Override
                    public void onNext(final ReFundBean reFundBean) {
                        super.onNext(reFundBean);
                        if (reFundBean != null) {
                            String styledText;
                            if (TextUtils.isEmpty(reFundBean.getCoin())) {
                                styledText = "已付商品货款：<font color='red'>¥" + reFundBean.getProductAmount() + "</font>，分摊运费：<font color='red'>¥"
                                        + reFundBean.getPostFee() + "</font>，总计可退：<font color='#09F709'>¥" + reFundBean.getTotalRefundAmount() + "</font>。";
                            } else {
                                styledText = "已付商品货款：<font color='red'>¥" + reFundBean.getProductAmount() + "</font>，换货币：<font color='red'>¥" + reFundBean.getCoin() + "</font>，分摊运费：<font color='red'>¥"
                                        + reFundBean.getPostFee() + "</font>，总计可退：<font color='#09F709'>¥" + reFundBean.getTotalRefundAmount() + "</font>。";
                            }
                            ReFundOderDialog dialog = new ReFundOderDialog((Activity) context);
                            dialog.setHasTittle(true).setMessage(Html.fromHtml(styledText))
                                    .setPositive("确认退款", new ReFundOderDialog.PopDialogListener() {
                                        @Override
                                        public void onPopDialogButtonClick(int which, ReFundOderDialog oderDialog) {
                                            buyerApplySettleRefund(context, oid, oderDialog);
                                        }
                                    }).setNegative("我再想想", null).show();
                        }
                    }
                }));
    }

    /**
     * 订单退款
     *
     * @author James Chen
     * @create time in 2018/5/9 14:31
     */
    public void buyerApplySettleRefund(final Context context, int oid, final ReFundOderDialog oderDialog) {
        if (oid < 0)
            return;
        Map<String, Object> params = new HashMap<>();
        params.put("orderid", oid + "");
//        params.put("refundWithProduct", false);
//        params.put("refundType", 1);
//        params.put("refundAmount",0);
//        params.put("refundReason", "");
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi("buyerApplyRefund")
                .buyerApplySettleRefund(params).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult()).subscribeWith(new CommonSubscriber<Object>(context, true, R.string.loading_refund) {
                    @Override
                    public void onNext(Object reFundBean) {
                        super.onNext(reFundBean);
                        ViewHub.showShortToast(context, "退款成功！");
                        if (oderDialog != null)
                            oderDialog.dismiss();
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_BUYER_AGRESS, "ok"));
                    }
                }));
    }

    /**
     * 订单退款
     *
     * @author James Chen
     * @create time in 2018/5/9 14:31
     */
    public void buyerApplyRefund(final Context context, int oid, final ReFundOderDialog oderDialog) {
        if (oid < 0)
            return;
        Map<String, Object> params = new HashMap<>();
        params.put("orderid", oid + "");
//        params.put("refundWithProduct", false);
//        params.put("refundType", 1);
//        params.put("refundAmount",0);
//        params.put("refundReason", "");
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi("buyerApplyRefund")
                .buyerApplyRefund(params).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult()).subscribeWith(new CommonSubscriber<Object>(context, true, R.string.loading_refund) {
                    @Override
                    public void onNext(Object reFundBean) {
                        super.onNext(reFundBean);
                        ViewHub.showShortToast(context, "退款成功！");
                        if (oderDialog != null)
                            oderDialog.dismiss();
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFUND_BUYER_AGRESS, "ok"));
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
        JPushInterface.onPause(this);
        UMengTestUtls.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity=this;
        StatService.onResume(this);
        JPushInterface.onResume(this);
        UMengTestUtls.onResume(this);
        if (HttpUtils.ECC_OPEN) {
            EaseUI.getInstance().getNotifier().reset();
        }
    }
}
