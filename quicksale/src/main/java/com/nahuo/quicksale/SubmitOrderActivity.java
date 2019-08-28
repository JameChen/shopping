package com.nahuo.quicksale;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.Dialog.CDialog2;
import com.nahuo.Dialog.DistributionModeDialog;
import com.nahuo.Dialog.DomDialog;
import com.nahuo.Dialog.SingleDomDialog;
import com.nahuo.Dialog.SingleDomNoTitleDialog;
import com.nahuo.PopupWindow.CouponBottmomMenu;
import com.nahuo.bean.SaveApplyInfo;
import com.nahuo.constant.Constant;
import com.nahuo.library.controls.LightPopDialog.PopDialogListener;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.activity.ShopCartNewActivity;
import com.nahuo.quicksale.adapter.SubmitNewOrderAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.RequestMethod.ShopCartMethod;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.controls.NFHSZDialog;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.exceptions.CatchedException;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.Address;
import com.nahuo.quicksale.oldermodel.ApplyListModel;
import com.nahuo.quicksale.oldermodel.Area;
import com.nahuo.quicksale.oldermodel.CouponModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShipSettingModel;
import com.nahuo.quicksale.oldermodel.SubmitOrderResult;
import com.nahuo.quicksale.oldermodel.TempOrder;
import com.nahuo.quicksale.oldermodel.TempOrderV2;
import com.nahuo.quicksale.oldermodel.json.JAddress;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.util.AKUtil;
import com.nahuo.quicksale.util.RxUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import ch.ielse.view.SwitchView;
import de.greenrobot.event.EventBus;

public class SubmitOrderActivity extends BaseAppCompatActivity implements DistributionModeDialog.PopDialogListener, OnClickListener, HttpRequestListener {

    private static final String TAG = SubmitOrderActivity.class.getSimpleName();
    private SubmitOrderActivity vThis = this;
    private ListView mPullExListView;
    private View mListHeader;
    private TextView mTvName, mTvPhone, mTvAddress, mTvShipType, mTvShipTypeTips;
    private TextView discountCoupon, tvSelectShipTypeTips, tv_delivery, tv_choose_SelectShip, tv_fee_left;
    private SubmitNewOrderAdapter mAdapter;
    private TextView mTvCountPrice, mTvCountPriceDetail, tv_choose_delivery;
    private TempOrderV2 mTempOrders;
    // private ExpandableListView mExpListView;
    private View layout_distrbution, layout_delivery;
    public static final String INTENT_ORDER = "intent_order";
    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();
    private boolean mReload;
    private ShipSettingModel selectShipSetting;
    private LoadingDialog mLoadingDialog;
    private CouponBottmomMenu bottomMenu;
    private View relativeLayout;
    private List<CouponModel> list;

    private CouponModel selectCoupon;

    private TextView canUseNum, noUse;
    private ImageView rightBack;
    private int AddressID;
    private String orderStr;
    private LinearLayout ll_add;
    private String ShipNotice = "";
    private String CouponsNotice = "";
    private String ShipSettingNotice = "";
    private String ShipApplyNotice = "";
    private String CoinNotice = "";
    private List<ShipSettingModel> shipSettingModelList = new ArrayList<>();
    private int workWareHouseID;
    private String WareHouseIDS;
    private String Desc = "";
    private TempOrderV2.ShipApplyBean shipApplyBean;
    private ApplyListModel workApplyData;
    private boolean AutoUseCoupon;
    private View layout_totalproductamount, layout_discountamount, layout_couponamount, layout_expand, layout_fee;
    private TextView tv_totalproductamount, tv_discountamount, tv_couponamount, tv_fee, tv_fee_weight;
    private String DiscountAmount = "";
    private String CouponAmount = "";
    private String TotalProductAmount = "";
    private ImageView iv_up_icon, iv_down_icon;
    private boolean is_expand = true;
    private String TotalPostFeeAmount = "", TotalPayableAmount = "", TotalOriPostFeeAmount = "", Total_Weight = "", ProductDiscount = "", PostfeeDiscount = "";
    private int head_height;
    private View layout_coin_pop, coin_left;
    private SwitchView switch_coin;
    private TextView tv_coin_des_top, tv_coin_des_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_submit_order);
        BWApplication.addActivity(this);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        mLoadingDialog = new LoadingDialog(this);
        setTitle("订单结算");
        layout_totalproductamount = findViewById(R.id.layout_totalproductamount);
        layout_discountamount = findViewById(R.id.layout_discountamount);
        layout_couponamount = findViewById(R.id.layout_couponamount);
        layout_fee = findViewById(R.id.layout_fee);
        layout_expand = findViewById(R.id.layout_expand);
        iv_up_icon = (ImageView) findViewById(R.id.iv_up_icon);
        iv_down_icon = (ImageView) findViewById(R.id.iv_down_icon);
        iv_down_icon.setOnClickListener(this);
        iv_up_icon.setOnClickListener(this);
        tv_totalproductamount = (TextView) findViewById(R.id.tv_totalproductamount);
        tv_discountamount = (TextView) findViewById(R.id.tv_discountamount);
        tv_couponamount = (TextView) findViewById(R.id.tv_couponamount);
        tv_fee = (TextView) findViewById(R.id.tv_fee);
        tv_fee_weight = (TextView) findViewById(R.id.tv_fee_weight);
        initHeadView();
        mTempOrders = (TempOrderV2) getIntent().getSerializableExtra(INTENT_ORDER);
        if (mTempOrders != null) {
            AutoUseCoupon = mTempOrders.AutoUseCoupon;
            // String coin = mTempOrders.getCoinBalance();
            WareHouseIDS = mTempOrders.getWareHouseIDS();
            if (!mTempOrders.isShowCoinTag()) {
                layout_coin_pop.setVisibility(View.GONE);
            } else {
                layout_coin_pop.setVisibility(View.VISIBLE);
                tv_coin_des_bottom.setText(mTempOrders.getCoinSmallSummary());
                tv_coin_des_top.setText(mTempOrders.getCoinSummary());
            }
//            if (TextUtils.isEmpty(coin) || coin.equals("0") || coin.equals("0.0") || coin.equals("0.00")) {
//                layout_coin_pop.setVisibility(View.GONE);
//            } else {
//                layout_coin_pop.setVisibility(View.VISIBLE);
//                tv_coin_des_bottom.setText(mTempOrders.getCoinSmallSummary());
//                tv_coin_des_top.setText(mTempOrders.getCoinSummary());
//            }
        }
        orderStr = this.getIntent().getStringExtra(ShopCartNewActivity.SHOPCARTORDER);
        initViews();
        init();
        //judeExpand();
        if (mTempOrders == null) {
            ViewHub.showShortToast(getApplicationContext(), getString(R.string.no_order));
            finish();
            return;
        } else {
            list = mTempOrders.getCoupons();
//            CouponsNotice = tempOrders.getCouponsNotice();
//            ShipSettingNotice = tempOrders.getShipSettingNotice();
//            ShipApplyNotice = tempOrders.getShipApplyNotice();
            ShipNotice = mTempOrders.getShipNotice();
            shipApplyBean = mTempOrders.getShipApply();
            if (shipApplyBean != null) {
                Desc = shipApplyBean.getDesc();
                tv_choose_delivery.setText(Desc);
                workWareHouseID = shipApplyBean.getWareHouseID();
            }
            initCouponView();
        }

    }

    private void judeExpand() {
        if (mTempOrders != null) {
            DiscountAmount = mTempOrders.getDiscountAmount();
            CouponAmount = mTempOrders.getCouponAmount();
            TotalProductAmount = mTempOrders.getTotalProductAmountX();
            TotalPayableAmount = mTempOrders.getTotalPayableAmount();
            TotalPostFeeAmount = mTempOrders.getTotalPostFeeAmount();
            TotalOriPostFeeAmount = mTempOrders.getTotalOriPostFeeAmount();
            ProductDiscount = mTempOrders.getProductDiscount();
            PostfeeDiscount = mTempOrders.getPostfeeDiscount();
            Total_Weight = mTempOrders.getTotalWeight();
            if (TextUtils.isEmpty(Total_Weight)) {
                tv_fee_weight.setVisibility(View.GONE);
            } else {
                tv_fee_weight.setVisibility(View.VISIBLE);
                tv_fee_weight.setText(Total_Weight);
            }
            if (TextUtils.isEmpty(ProductDiscount)) {
                layout_fee.setVisibility(View.GONE);
            } else {
                layout_fee.setVisibility(View.VISIBLE);
            }
            tv_fee.setText(ProductDiscount + "");
            mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, TotalPayableAmount)));
            mTvCountPriceDetail.setText("" + TotalOriPostFeeAmount);
//            if (TextUtils.isEmpty(DiscountAmount) && TextUtils.isEmpty(CouponAmount) && TextUtils.isEmpty(TotalProductAmount)) {
//                iv_up_icon.setVisibility(View.GONE);
//                layout_expand.setVisibility(View.GONE);
//            } else {
            if (TextUtils.isEmpty(PostfeeDiscount)) {
                layout_discountamount.setVisibility(View.GONE);
            } else {
                layout_discountamount.setVisibility(View.VISIBLE);
                tv_discountamount.setText(PostfeeDiscount);
            }
//            if (TextUtils.isEmpty(CouponAmount)) {
//                layout_couponamount.setVisibility(View.GONE);
//            } else {
//                layout_couponamount.setVisibility(View.VISIBLE);
//                tv_couponamount.setText(CouponAmount);
//            }
            if (TextUtils.isEmpty(TotalProductAmount)) {
                layout_totalproductamount.setVisibility(View.GONE);
            } else {
                layout_totalproductamount.setVisibility(View.VISIBLE);
                tv_totalproductamount.setText(TotalProductAmount);
            }
            if (is_expand) {
                iv_up_icon.setVisibility(View.GONE);
                showExpandAnimation(true);
            } else {
                iv_up_icon.setVisibility(View.VISIBLE);
                showExpandAnimation(false);
            }
            // }
        } else {
            iv_up_icon.setVisibility(View.GONE);
            layout_expand.setVisibility(View.GONE);
        }
    }

    View foot;

    private void showExpandAnimation(boolean isShow) {
        if (isShow) {
            if (layout_expand.getVisibility() != View.VISIBLE) {
                layout_expand.setVisibility(View.VISIBLE);
                iv_up_icon.setVisibility(View.GONE);
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.bottom_menu_appear);
                layout_expand.startAnimation(anim);
                ViewTreeObserver vto = layout_expand.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        layout_expand.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        head_height = layout_expand.getHeight();
                        if (foot != null) {
                            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, head_height);
                            foot.setLayoutParams(params);
                            mPullExListView.addFooterView(foot);
                        }
                        //layout_out_head.getWidth();
                    }
                });

            }
        } else {
            if (layout_expand.getVisibility() == View.VISIBLE) {
                if (foot != null)
                    mPullExListView.removeFooterView(foot);
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.botttom_menu_disppearx);
                layout_expand.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        layout_expand.setVisibility(View.GONE);
                        iv_up_icon.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }

    }

    private void initCouponView() {
        if (!ListUtils.isEmpty(list)) {
            bottomMenu = new CouponBottmomMenu(SubmitOrderActivity.this, list);
            bottomMenu.setTouchable(true);
            canUseNum.setText(list.size() + "张可用");
            canUseNum.setVisibility(View.VISIBLE);
            noUse.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);
            if (AutoUseCoupon)
                selectCoupon = list.get(0);
            if (selectCoupon != null)
                noUse.setText(selectCoupon.getType() + ":" + formatDiscount(selectCoupon.getDiscount()));
            //initOrder();
        } else {
            noUse.setText("无可用");
            relativeLayout.setVisibility(View.GONE);
            noUse.setVisibility(View.VISIBLE);
            rightBack.setVisibility(View.INVISIBLE);
        }
    }


    private String formatDiscount(String s) {
        if (AKUtil.hasDigit(s)) {
            return "-" + s;
        } else {
            return s;
        }
    }

    private void setTitle(String text) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(text);
    }

    private void initViews() {

        mPullExListView = (ListView) findViewById(R.id.lv_order);
        foot = LayoutInflater.from(this).inflate(R.layout.sub_foot, null);
        // mExpListView = mPullExListView.getRefreshableView();
        // mExpListView.setFocusable(false);
        mListHeader.findViewById(R.id.rl_receive_info).setOnClickListener(this);
        tv_fee_left = (TextView) findViewById(R.id.tv_fee_left);
        tv_fee_left.setOnClickListener(this);
        discountCoupon = (TextView) mListHeader.findViewById(R.id.discountCoupon);
        layout_distrbution = mListHeader.findViewById(R.id.layout_distrbution);
        layout_delivery = mListHeader.findViewById(R.id.layout_delivery);
        layout_delivery.setOnClickListener(this);
        layout_distrbution.setOnClickListener(this);
        tvSelectShipTypeTips = (TextView) mListHeader.findViewById(R.id.tvSelectShipTypeTips);
        tv_delivery = (TextView) mListHeader.findViewById(R.id.tv_delivery);
        tv_choose_SelectShip = (TextView) mListHeader.findViewById(R.id.tv_choose_SelectShip);
        tv_choose_delivery = (TextView) mListHeader.findViewById(R.id.tv_choose_delivery);
        discountCoupon.setOnClickListener(this);
        tvSelectShipTypeTips.setOnClickListener(this);
        tv_delivery.setOnClickListener(this);
        mTvName = (TextView) mListHeader.findViewById(R.id.tvName);
        mTvPhone = (TextView) mListHeader.findViewById(R.id.tvPhone);
        mTvAddress = (TextView) mListHeader.findViewById(R.id.tvAddress);
        mTvShipType = (TextView) mListHeader.findViewById(R.id.tvSelectShipType);
        mTvShipTypeTips = (TextView) mListHeader.findViewById(R.id.tvSelectShipTypeDescription);
        mTvShipType.setOnClickListener(this);
        relativeLayout = mListHeader.findViewById(R.id.canuse_coupon_pop);
        relativeLayout.setOnClickListener(this);
//        for (int i = 0; i < mTempOrders.getShipSetting().size(); i++) {
//            if (mTempOrders.getShipSetting().get(i).isDefault()) {//默认选择
//                selectShipSetting = mTempOrders.getShipSetting().get(i);
//                break;
//            }
//        }
//        if (selectShipSetting != null) {
//            mTvShipType.setText(selectShipSetting.getName());
//            mTvShipTypeTips.setText(selectShipSetting.getDescription());
//        }
        mPullExListView.addHeaderView(mListHeader, null, false);
//        mPullExListView.setOnGroupClickListener(new OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                return true;
//            }
//        });
        mTvCountPrice = (TextView) findViewById(android.R.id.text1);
        mTvCountPriceDetail = (TextView) findViewById(android.R.id.text2);

        findViewById(R.id.tv_gotoYunFei).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(vThis, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 93595);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                vThis.startActivity(intent);
            }
        });

    }

    private void initHeadView() {
        mListHeader = LayoutInflater.from(this).inflate(R.layout.layout_submit_order_list_header, null);
        ll_add = (LinearLayout) mListHeader.findViewById(R.id.ll_add);
        canUseNum = (TextView) mListHeader.findViewById(R.id.CountSomePieces);
        noUse = (TextView) mListHeader.findViewById(R.id.countMoney);
        rightBack = (ImageView) mListHeader.findViewById(android.R.id.icon);
        layout_coin_pop = mListHeader.findViewById(R.id.layout_coin_pop);
        coin_left = mListHeader.findViewById(R.id.coin_left);
        coin_left.setOnClickListener(this);
        switch_coin = (SwitchView) mListHeader.findViewById(R.id.switch_coin);
        tv_coin_des_top = (TextView) mListHeader.findViewById(R.id.tv_coin_des_top);
        tv_coin_des_bottom = (TextView) mListHeader.findViewById(R.id.tv_coin_des_bottom);
        switch_coin.setOpened(false);
        switch_coin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initOrder();
            }
        });
//        if (SpManager.getBALANCE_COIN(vThis)>0){
//            layout_coin_pop.setVisibility(View.VISIBLE);
//        }else {
//            layout_coin_pop.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void onResume() {

        if (mReload) {
            setReceiverInfo();
        }
        initOrder();
        super.onResume();
    }

    private void init() {
        mAdapter = new SubmitNewOrderAdapter(this);
        mAdapter.setData(mTempOrders.getOrders());
        mPullExListView.setAdapter(mAdapter);
        int count = 0;
        double money = 0;
        double post = 0;
//        for (int i = 0; i < mTempOrders.getOrders().size(); i++) {
//           // mPullExListView.expandGroup(i);
//            money = money + mTempOrders.getOrders().get(i).PayableAmount;
//            post = post + mTempOrders.getOrders().get(i).PostFee;
//            if (mTempOrders.getOrders().get(i).getOrderItems() != null) {
//                for (TempOrder.OrderItem item : mTempOrders.getOrders().get(i).getOrderItems()) {
//                    count = count + item.Qty;
//                }
//            }
//        }
//        mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(money))));
//        mTvCountPriceDetail.setText(String.format("含预收运费 ¥%.2f", post));
//        Address addr = SpManager.getLastSelectAddress(getApplicationContext(),
//                SpManager.getUserId(getApplicationContext()));
        loadAddress();
//        if (addr == null)
//            loadAddress();
//        else {
//            mTvName.setText(addr.getUserName());
//            mTvPhone.setText(addr.getPhone());
//            Area city = addr.getCity();
//            Area area = addr.getArea();
//            mTvAddress.setText(addr.getProvince() + " " + (city == null ? "" : city) + " " + (area == null ? "" : area) + "\n"
//                    + addr.getDetailAddress());
//        }
        //配送方式
        initDistributionmode();

    }

    //配送方式
    private void initDistributionmode() {
        final List<ShipSettingModel> xlist = mTempOrders.getShipSetting();

        if (!ListUtils.isEmpty(xlist)) {
            shipSettingModelList.clear();
            shipSettingModelList.addAll(xlist);
            List<Boolean> hasChecks = new ArrayList<>();
            for (ShipSettingModel itemModel : xlist) {
                if (itemModel.isDefault()) {
                    hasChecks.add(itemModel.isDefault());
                    selectShipSetting = itemModel;
                }
            }
            for (int i = 0; i < shipSettingModelList.size(); i++) {
                if (hasChecks.size() <= 0 && i == 0) {
                    selectShipSetting = shipSettingModelList.get(i);
                    break;
                }
            }
            if (selectShipSetting != null && tv_choose_SelectShip != null)
                tv_choose_SelectShip.setText(selectShipSetting.getName());
        }
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.leftMargin = DisplayUtil.dip2px(this, 10);
//        params.rightMargin = DisplayUtil.dip2px(this, 10);
//        if (xlist != null) {
//            final TextView[] mTvs = new TextView[xlist.size()];
//            ll_add.removeAllViews();
//            for (int i = 0; i < xlist.size(); i++) {
//                TextView tv = new TextView(this);
//                tv.setText(xlist.get(i).getName());
//                tv.setGravity(Gravity.CENTER);
//                tv.setTextSize(13);
//                if (i == 0) {
//                    //默认第一个
//                    tv.setTextColor(Color.WHITE);
//                    tv.setBackgroundResource(R.drawable.bg_sumit_tv);
//                    String descri = xlist.get(i).getDescription();
//                    selectShipSetting = xlist.get(i);
//                    if (!TextUtils.isEmpty(descri)) {
//                        mTvShipTypeTips.setText(descri);
//                    }
//                } else {
//                    tv.setTextColor(Color.BLACK);
//                    tv.setBackgroundResource(R.drawable.bg_sumit_tv_gray);
//                }
//                tv.setPadding(20, 20, 20, 20);
//                tv.setLayoutParams(params);
//                mTvs[i] = tv;
//                ll_add.addView(tv);
//                final int pos = i;
//                tv.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        for (int j = 0; j < mTvs.length; j++) {
//                            if (j == pos) {
//                                mTvs[j].setTextColor(Color.WHITE);
//                                mTvs[j].setBackgroundResource(R.drawable.bg_sumit_tv);
//                                String descri = xlist.get(j).getDescription();
//                                selectShipSetting = xlist.get(j);
//                                if (!TextUtils.isEmpty(descri)) {
//                                    mTvShipTypeTips.setText(descri);
//                                }
//                                // Toast.makeText(SubmitOrderActivity.this, xlist.get(j).getDescription() + "", Toast.LENGTH_SHORT).show();
//                            } else {
//                                mTvs[j].setTextColor(Color.BLACK);
//                                mTvs[j].setBackgroundResource(R.drawable.bg_sumit_tv_gray);
//
//                            }
//                        }
//
//                    }
//                });
//            }
        //  }
    }

    private void loadAddress() {
//        HttpRequestHelper r = new HttpRequestHelper();
//        r.getRequest(this, ShopMethod.SHOP_ADDRESS_GET_ADDRESSES, this)
//                .setConvert2Token(new TypeToken<List<JAddress>>() {
//                }).doPost();
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getAddresses()
                .compose(RxUtil.<PinHuoResponse<List<JAddress>>>rxSchedulerHelper())
                .compose(RxUtil.<List<JAddress>>handleResult())
                .subscribeWith(new CommonSubscriber<List<JAddress>>(vThis) {
                    @Override
                    public void onNext(List<JAddress> jAddresses) {
                        super.onNext(jAddresses);
                        List<JAddress> jList = jAddresses;
                        if (jList != null && !jList.isEmpty()) {
                            boolean hasDefault = false;
                            for (JAddress ja : jList) {
                                if (ja.isIsDefault()) {
                                    setLastAddress(ja);
                                    break;
                                }
                            }
                            if (!hasDefault) {// 没有设置默认地址
                                JAddress ja = jList.get(0);
                                setLastAddress(ja);
                            }
                        }
                        setReceiverInfo();
                    }
                }));


    }

    private void setReceiverInfo() {
        Address mAddress = SpManager.getLastSelectAddress(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        if (mAddress != null) {
            AddressID = mAddress.getId();
            mTvName.setText(mAddress.getUserName());
            mTvPhone.setText(mAddress.getPhone());
            Area city = mAddress.getCity();
            Area area = mAddress.getArea();
            mTvAddress.setText(mAddress.getProvince() + " " + (city == null ? "" : city) + " " + (area == null ? "" : area) + "\n"
                    + mAddress.getDetailAddress());
            if (BWApplication.addisEdit) {
                if (TextUtils.isEmpty(ShipNotice))
                    return;
                try {
                    DomDialog dialog = new DomDialog(this);
                    dialog.setTitle("提示").setMessage(ShipNotice).setPositive("ok", new DomDialog.PopDialogListener() {
                        @Override
                        public void onPopDialogButtonClick(int which) {

                        }
                    }).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    CrashReport.postCatchedException(new CatchedException(e));
                }
            }
        } else {
            mTvName.setText("选择/添加收货地址");
            mTvPhone.setText("");
            mTvAddress.setText("");
            ViewHub.showLightPopDialog(this, "提示", "您还没有设置收货地址，现在去添加？", getString(android.R.string.cancel),
                    getString(android.R.string.ok), new PopDialogListener() {
                        @Override
                        public void onPopDialogButtonClick(int which) {
                            Intent it = new Intent(getApplicationContext(), AddressActivity2.class);
                            mReload = true;
                            startActivity(it);
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fee_left:
                Intent intent = new Intent(vThis, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 93595);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                vThis.startActivity(intent);
                break;
            case R.id.iv_up_icon:
                showExpandAnimation(true);
                break;
            case R.id.iv_down_icon:
                showExpandAnimation(false);
                break;
            case R.id.layout_delivery:
                //发货方式
                if (FunctionHelper.isDoubleFastClick())
                    return;
                setFaHuoMethor();
                break;
            case R.id.layout_distrbution:
                //配送方式
                DistributionModeDialog.getInstance(this).setPositive(this).setList(shipSettingModelList).showDialog();
                break;
            case R.id.discountCoupon:
                SingleDomDialog dialog = new SingleDomDialog(this);
                dialog.setTitle("如何获得更多优惠券").setMessage(CouponsNotice).setPositive("我知道了", new SingleDomDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {

                    }
                }).show();
                break;
            case R.id.tvSelectShipTypeTips:
                SingleDomNoTitleDialog dialog2 = new SingleDomNoTitleDialog(this);
                dialog2.setTitle("").setMessage(ShipSettingNotice).setPositive("我知道了", new SingleDomNoTitleDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {

                    }
                }).show();
                break;
            case R.id.tv_delivery:
                SingleDomNoTitleDialog dialog3 = new SingleDomNoTitleDialog(this);
                dialog3.setTitle("").setMessage(ShipApplyNotice).setPositive("我知道了", new SingleDomNoTitleDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {

                    }
                }).show();
                break;
            case R.id.coin_left:
                SingleDomNoTitleDialog dialog4 = new SingleDomNoTitleDialog(this);
                dialog4.setTitle("").setMessage(CoinNotice).setPositive("我知道了", new SingleDomNoTitleDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {

                    }
                }).show();
                break;
            case R.id.titlebar_btnLeft:
                goBack();
                break;
            case R.id.tvSelectShipType: {
                int selectedIndex = 0;
                final String items[] = new String[mTempOrders.getShipSetting().size()];
                for (int i = 0; i < mTempOrders.getShipSetting().size(); i++) {
                    items[i] = mTempOrders.getShipSetting().get(i).getName();
//                            +
//                            "["+mTempOrders.getShipSetting().get(i).getDescription()+"]";
                    if (selectShipSetting == null) {
                        if (mTempOrders.getShipSetting().get(i).isDefault()) {
                            selectedIndex = i;
                        }
                    } else if (selectShipSetting.getName().equals(items[i])) {
                        selectedIndex = i;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择配送方式");
                builder.setSingleChoiceItems(items, selectedIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectShipSettingWithName(items[which]);
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (which >= 0 && which <= items.length) {
                            selectShipSettingWithName(items[which]);
                        }
                    }
                });
                builder.create().show();
                break;
            }
            case R.id.rl_receive_info:
                BWApplication.addisEdit = false;
                Intent it = new Intent(getApplicationContext(), AddressActivity2.class);
                mReload = true;
                startActivity(it);
                break;
            case android.R.id.button1:
//                if (!SpManager.getIS_AGREE_SUBMIT_ORDER(getApplicationContext())) {
//                    ViewHub.showTextDialog(this, "重要提示",
//                            "亲，专场结束后，已付款订单不可取消哦！因为买手向档口报单后如果你不要货，买手无法向档口退货，请务必考虑好再下单！",
//                            "知道了，我同意", "不接受",
//                            new ViewHub.EditDialogListener() {
//                                @Override
//                                public void onOkClick(DialogInterface dialog, EditText editText) {
//                                    SpManager.setIS_AGREE_SUBMIT_ORDER(getApplicationContext(), true);
//                                    submitOrderFunction();
//                                }
//
//                                @Override
//                                public void onOkClick(EditText editText) {
//                                    SpManager.setIS_AGREE_SUBMIT_ORDER(getApplicationContext(), true);
//                                    submitOrderFunction();
//                                }
//
//                                @Override
//                                public void onNegativecClick() {
//                                }
//                            });
//                } else {
//                    submitOrderFunction();
//                }
                submitOrderFunction();
                break;
            case R.id.canuse_coupon_pop:
                if (!ListUtils.isEmpty(list)) {
                    bottomMenu.show((View) v.getParent().getParent());
                   /* if(selectCoupon==null){
                        bottomMenu.setDefault(list.get(0));
                    }else{
                        bottomMenu.setDefault(selectCoupon);
                    }*/
                    if (AutoUseCoupon) {
                        bottomMenu.setDefault(selectCoupon);
                    }
                    bottomMenu.setOnCouponClickItem(new CouponBottmomMenu.onCouponClickItem() {
                        @Override
                        public void onCouponClick(CouponModel coupon) {
                            selectCoupon = coupon;
                            if (selectCoupon == null) {
                                if (!TextUtils.isEmpty(orderStr)) {
                                    initOrder();
                                }
                            } else {
                                //noUse.setText("-" + selectCoupon.getDiscount());
                                noUse.setText(selectCoupon.getType() + ":" + formatDiscount(selectCoupon.getDiscount()));
                                if (!TextUtils.isEmpty(orderStr)) {
                                    initOrder();
                                }
                            }

                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
        return;
    }

    private void goBack() {
        CDialog2 dialog = new CDialog2(vThis);
        dialog.setHasTittle(true).setTitle("提示").setMessage("新款好货不等人，请三思而行").setPositive("去意已决", new CDialog2.PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                finish();
            }
        }).setNegative("我再想想", null).show();

    }

    private void setFaHuoMethor() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getApplyList(workWareHouseID + "")
                .compose(RxUtil.<PinHuoResponse<ApplyListModel>>rxSchedulerHelper())
                .compose(RxUtil.<ApplyListModel>handleResult())
                .subscribeWith(new CommonSubscriber<ApplyListModel>(vThis, true, R.string.loading) {
                    @Override
                    public void onNext(ApplyListModel applyListModel) {
                        super.onNext(applyListModel);
                        final NFHSZDialog dialog = NFHSZDialog.newInstance(applyListModel);
                        dialog.mContext = vThis;
                        dialog.setDialogListener(new NFHSZDialog.FHSZDialogListener() {
                            @Override
                            public void FHSZResult(final int typeid, final int value) {
                                dialog.dismiss();
                                addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).saveApplyInfo4Order(WareHouseIDS, typeid, value)
                                        .compose(RxUtil.<PinHuoResponse<SaveApplyInfo>>rxSchedulerHelper())
                                        .compose(RxUtil.<SaveApplyInfo>handleResult())
                                        .subscribeWith(new CommonSubscriber<SaveApplyInfo>(vThis, true, R.string.loading_work_warehouse) {
                                            @Override
                                            public void onNext(SaveApplyInfo info) {
                                                super.onNext(info);
                                                if (info != null) {
                                                    if (info.getApplyInfo() != null) {
                                                        if (tv_choose_delivery != null)
                                                            tv_choose_delivery.setText(info.getApplyInfo().getDesc());
                                                    }
                                                    ViewHub.showLongToast(vThis, info.getMessage() + "");
                                                }
                                            }
                                        }));
                            }
                        });
                        dialog.show(vThis.getFragmentManager(), "FHSZDialog");
                    }
                }));
//        new AsyncTask<Void, Void, Object>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                //  mLoadingDialog.start("");
//            }
//
//            @Override
//            protected Object doInBackground(Void... params) {
//                try {
//                    return QuickSaleApi.getApplyList(vThis, workWareHouseID);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return "error:" + e.getMessage();
//                }
//            }
//
//            @Override
//            protected void onPostExecute(final Object result) {
//                super.onPostExecute(result);
////                        if (mLoadingDialog!=null)
////                        mLoadingDialog.stop();
//                if (result instanceof String && ((String) result).startsWith("error:")) {
//                    ViewHub.showShortToast(vThis, ((String) result).replace("error:", ""));
//                } else {
//                    workApplyData = (ApplyListModel) result;
//                    final NFHSZDialog dialog = NFHSZDialog.newInstance(workApplyData);
//                    dialog.mContext = vThis;
//                    final NFHSZDialog ddd = dialog;
//                    dialog.setDialogListener(new NFHSZDialog.FHSZDialogListener() {
//                        @Override
//                        public void FHSZResult(final int typeid, final int value) {
//                            dialog.dismiss();
////                                    if (typeid == 0 && workApplyData.getApplyInfo().getTypeName().equals("仓库安排")) {
////                                        ViewHub.showLongToast(mContext, "默认仓库安排，无需申请");
////                                        return;
////                                    }
//                            new AsyncTask<Void, Void, Object>() {
//
//                                @Override
//                                protected void onPreExecute() {
//                                    super.onPreExecute();
//                                    mLoadingDialog.start("保存中");
//                                }
//
//                                @Override
//                                protected Object doInBackground(Void... params) {
//                                    try {
//                                        return QuickSaleApi.setSubmitApply(vThis, workWareHouseID, typeid, value);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                        return "error:" + e.getMessage();
//                                    }
//                                }
//
//                                @Override
//                                protected void onPostExecute(Object result) {
//                                    super.onPostExecute(result);
//                                    mLoadingDialog.stop();
//                                    if (result instanceof String && ((String) result).startsWith("error:")) {
//                                        ViewHub.showLongToast(vThis, ((String) result).replace("error:", ""));
//                                        return;
//                                    } else if (result instanceof SaveApplyInfo) {
//                                        SaveApplyInfo info = (SaveApplyInfo) result;
//                                        if (info != null) {
//                                            if (info.getApplyInfo() != null) {
//                                                if (tv_choose_delivery != null)
//                                                    tv_choose_delivery.setText(info.getApplyInfo().getDesc());
//                                            }
//                                            ViewHub.showLongToast(vThis, info.getMessage() + "");
//                                        }
//
//                                    }
//
//                                }
//
//                            }.execute();
//                        }
//                    });
//                    dialog.show(((Activity) vThis).getFragmentManager(), "FHSZDialog");
//                }
//            }
//
//        }.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BWApplication.addisEdit = false;
    }

    private void initOrder() {
//        HttpRequest request = mHttpRequestHelper.getRequest(vThis, ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP, vThis);
//        request.addParam("itemInfos", orderStr);
//        request.addParam("AddressID", AddressID + "");
//        request.addParam("version", "3");
//        if (selectCoupon != null) {
//            request.addParam("CouponID", ((int) selectCoupon.getID()) + "");
//        }
//        request.setConvert2Token(new TypeToken<TempOrderV2>() {
//        });
//        request.doPost();
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        int coupId = -1;
        if (selectCoupon != null) {
            coupId = (int) selectCoupon.getID();
        }
        int coin = Constant.CionType.CIONTYPE_DEFAUT;
        if (switch_coin != null) {
            if (switch_coin.isOpened()) {
                coin = Constant.CionType.CIONTYPE_All;
            }
        }
        addSubscribe(pinHuoApi.createTempOrder(orderStr, Constant.ShopCartVersion.Version_3, AddressID
                , coupId, coin)
                .compose(RxUtil.<PinHuoResponse<TempOrderV2>>rxSchedulerHelper())
                .compose(RxUtil.<TempOrderV2>handleResult())
                .subscribeWith(new CommonSubscriber<TempOrderV2>(vThis, true, R.string.loading) {
                    @Override
                    public void onNext(TempOrderV2 tempOrderV2) {
                        super.onNext(tempOrderV2);
                        if (tempOrderV2 != null) {
                            tempOrders = tempOrderV2;
                            mTempOrders = tempOrderV2;
                            judeExpand();
                            if (tempOrders != null) {
                                CouponsNotice = tempOrders.getCouponsNotice();
                                ShipSettingNotice = tempOrders.getShipSettingNotice();
                                ShipApplyNotice = tempOrders.getShipApplyNotice();
                                CoinNotice = tempOrders.getCoinNotice();
                            }
                        }
                        if (tempOrderV2 != null && tempOrders != null && tempOrders.getOrders().size() > 0) {
                            mAdapter.setData(tempOrders.getOrders());
                            mAdapter.notifyDataSetChanged();
                            //double price = tempOrders.TotalPayableAmount;
                            //double couponPrice=tempOrders.TotalPostFeeAmount+tempOrders.TotalProductAmount-tempOrders.TotalPayableAmount;
                            if (selectCoupon != null) {
                                if (!AKUtil.hasDigit(selectCoupon.Discount)) {
                                    //  mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price)) + "(已优惠:" + selectCoupon.getDiscount() + ")"));
                                    //  mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price))));
                                    noUse.setVisibility(View.VISIBLE);
                                } else {
                                    double couponPrice = Double.parseDouble(selectCoupon.Discount.substring(1, selectCoupon.Discount.length()));
                                    // mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price)) + "(已优惠:¥" + Utils.moneyFormat(couponPrice) + ")"));
                                    // mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price))));
                                    noUse.setVisibility(View.VISIBLE);
                                }
                            } else {
                                //  mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price))));
                                noUse.setVisibility(View.GONE);
                            }
                            //  mTvCountPriceDetail.setText(String.format("预收运费 ¥%.2f", tempOrders.TotalPostFeeAmount));
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

    private void selectShipSettingWithName(String txt) {
        for (int i = 0; i < mTempOrders.getShipSetting().size(); i++) {
            if (txt.equals(mTempOrders.getShipSetting().get(i).getName()))
//                    +
//                    "[" + mTempOrders.getShipSetting().get(i).getDescription() + "]"))
            {
                selectShipSetting = mTempOrders.getShipSetting().get(i);
            }
        }
        if (selectShipSetting != null) {
            mTvShipType.setText(selectShipSetting.getName());
            mTvShipTypeTips.setText(selectShipSetting.getDescription());
        }
    }

    private void submitOrderFunction() {
        Address mAddress = SpManager.getLastSelectAddress(getApplicationContext(),
                SpManager.getUserId(getApplicationContext()));
        if (mAddress == null) {
            ViewHub.showShortToast(getApplicationContext(), "请选择收货信息后再提交");
        } else if (selectShipSetting == null) {
            ViewHub.showShortToast(getApplicationContext(), "请先填写收件地址");
        } else {
            submitOrder(mAddress.getId());
        }
    }

//    @Override
//    protected void onActivityResult(int arg0, int arg1, Intent data) {
//        if (arg1 == RESULT_OK) {
//            switch (arg0) {
//                case CODE_ADDR_RESULT:
//                    Address address = (Address)data.getSerializableExtra(AddressActivity2.INTENT_SELECTED_ADDRESS);
//                    if (address != null) {
//                        SpManager.setLastSelectAddress(getApplicationContext(),
//                                SpManager.getUserId(getApplicationContext()), address);
//                        setReceiverInfo();
//                    }
//
//                    Address delAddr = (Address)data.getSerializableExtra(AddressActivity2.INTENT_DELETED_ADDRESS);
//
//                    if (delAddr != null) {//
//                        Log.i(getClass().getSimpleName(), "address is deleted");
//                        setReceiverInfo();
//                    } else {
//                        Log.i(getClass().getSimpleName(), "address is deleted null");
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        super.onActivityResult(arg0, arg1, data);
//    }


    private void submitOrder(int addressId) {
        List<TempOrder> orders = mAdapter.getData();
//        HttpRequest r = mHttpRequestHelper.getRequest(this, ShopCartMethod.SUBMIT_ORDER_LIST, this);
//        r.addParam("addressId", addressId + "");
//        for (TempOrder order : orders) {
//            r.addParam("buyerRemark", order.LeaveMessage);
//            break;
//        }
//        if (selectCoupon != null) {
//            r.addParam("CouponID", (int) selectCoupon.getID() + "");
//        }
//        if (selectShipSetting != null)
//            r.addParam("shipTypeID", selectShipSetting.getID() + "");
//        r.addParam("shipRemark", "");
//        r.addParam("from", "ttaa");
//        r.setConvert2Class(SubmitOrderResult.class);
//        r.doPost();
        int couponId = -1;
        if (selectCoupon != null)
            couponId = (int) selectCoupon.getID();
        int coin = Constant.CionType.CIONTYPE_DEFAUT;
        if (switch_coin != null) {
            if (switch_coin.isOpened()) {
                coin = Constant.CionType.CIONTYPE_All;
            }
        }
        String message = "";
        if (!ListUtils.isEmpty(orders)) {
            for (TempOrder order : orders) {
                message = order.LeaveMessage;
                break;
            }
        }
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.submitTempOrder(addressId, message, couponId
                , couponId, "", "ttaa", coin)
                .compose(RxUtil.<PinHuoResponse<SubmitOrderResult>>rxSchedulerHelper())
                .compose(RxUtil.<SubmitOrderResult>handleResult())
                .subscribeWith(new CommonSubscriber<SubmitOrderResult>(vThis, true, R.string.loading) {
                    @Override
                    public void onNext(SubmitOrderResult object) {
                        super.onNext(object);
                        try {
                            SubmitOrderResult orderResult = object;
                            if (orderResult != null) {
                                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_SHOP_CART));
                                if (orderResult.OrderIds.length() > 0) {
                                    Intent it = new Intent(getApplicationContext(), OrderPayActivity.class);
                                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, orderResult.OrderIds);
                                    if (selectCoupon != null) {
                                        double price = 0.00;
                                        if (!TextUtils.isEmpty(tempOrders.getTotalPayableAmount()))
                                            price = FunctionHelper.DoubleTwoFormat(Double.parseDouble(tempOrders.getTotalPayableAmount()));
                                        it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, price);
                                    } else {
                                        it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, orderResult.PayableAmount);
                                    }
                                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER,orderResult.getOrderCode());
                                    it.putExtra(OrderPayActivity.INTENT_SHOW_SUBMIT_SUCCESS, true);
                                    startActivity(it);
                                    setResult(RESULT_OK);
                                } else {
                                    ViewHub.showShortToast(getApplicationContext(), "您的订单提交失败了，请重新提交");
                                }
                                //                if (successCount == 1 && failCount == 0) {// 只生成一张订单
                                //                    Intent it = new Intent(getApplicationContext(), OrderPayActivity.class);
                                //                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, orderResult.OrderList.get(0).OrderID);
                                //                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, orderResult.OrderList.get(0).PayableAmount);
                                //                    it.putExtra(OrderPayActivity.INTENT_SHOW_SUBMIT_SUCCESS, true);
                                //                    startActivity(it);
                                //                    setResult(RESULT_OK);
                                //                } else if (successCount + failCount > 1) {// 生成两张以上订单
                                //                    Intent it = new Intent(getApplicationContext(), SubmitOrderResultActivity.class);
                                //                    it.putExtra(SubmitOrderResultActivity.INTENT_SUBMIT_RESULT, orderResult);
                                //                    startActivity(it);
                                //                } else {
                                //                    ViewHub.showShortToast(getApplicationContext(), "您的订单提交失败了，请重新提交");
                                //                }
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    @Override
    public void onRequestStart(String method) {
        if (ShopCartMethod.SUBMIT_ORDER_LIST.equals(method)) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setMessage("正在提交订单，请耐心等待...");
            mLoadingDialog.show();
        } else if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setMessage("正在加载数据...");
            mLoadingDialog.show();
        }

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        hideDialog();
        if (ShopCartMethod.SUBMIT_ORDER_LIST.equals(method)) {
            try {
                SubmitOrderResult orderResult = (SubmitOrderResult) object;
                if (orderResult != null) {
                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_SHOP_CART));
                    if (orderResult.OrderIds.length() > 0) {
                        Intent it = new Intent(getApplicationContext(), OrderPayActivity.class);
                        it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, orderResult.OrderIds);
                        if (selectCoupon != null) {
                            double price = 0.00;
                            if (!TextUtils.isEmpty(tempOrders.getTotalPayableAmount()))
                                price = FunctionHelper.DoubleTwoFormat(Double.parseDouble(tempOrders.getTotalPayableAmount()));
                            it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, price);
                        } else {
                            it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, orderResult.PayableAmount);
                        }

                        it.putExtra(OrderPayActivity.INTENT_SHOW_SUBMIT_SUCCESS, true);
                        startActivity(it);
                        setResult(RESULT_OK);
                    } else {
                        ViewHub.showShortToast(getApplicationContext(), "您的订单提交失败了，请重新提交");
                    }
                    //                if (successCount == 1 && failCount == 0) {// 只生成一张订单
                    //                    Intent it = new Intent(getApplicationContext(), OrderPayActivity.class);
                    //                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, orderResult.OrderList.get(0).OrderID);
                    //                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, orderResult.OrderList.get(0).PayableAmount);
                    //                    it.putExtra(OrderPayActivity.INTENT_SHOW_SUBMIT_SUCCESS, true);
                    //                    startActivity(it);
                    //                    setResult(RESULT_OK);
                    //                } else if (successCount + failCount > 1) {// 生成两张以上订单
                    //                    Intent it = new Intent(getApplicationContext(), SubmitOrderResultActivity.class);
                    //                    it.putExtra(SubmitOrderResultActivity.INTENT_SUBMIT_RESULT, orderResult);
                    //                    startActivity(it);
                    //                } else {
                    //                    ViewHub.showShortToast(getApplicationContext(), "您的订单提交失败了，请重新提交");
                    //                }
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ShopMethod.SHOP_ADDRESS_GET_ADDRESSES.equals(method)) {
            List<JAddress> jList = (List<JAddress>) object;
            if (jList != null && !jList.isEmpty()) {
                boolean hasDefault = false;
                for (JAddress ja : jList) {
                    if (ja.isIsDefault()) {
                        setLastAddress(ja);
                        break;
                    }
                }
                if (!hasDefault) {// 没有设置默认地址
                    JAddress ja = jList.get(0);
                    setLastAddress(ja);
                }
            }
            setReceiverInfo();
        } else if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
            if (object != null) {
                tempOrders = (TempOrderV2) object;
                mTempOrders = (TempOrderV2) object;
                judeExpand();
                if (tempOrders != null) {
                    CouponsNotice = tempOrders.getCouponsNotice();
                    ShipSettingNotice = tempOrders.getShipSettingNotice();
                    ShipApplyNotice = tempOrders.getShipApplyNotice();
                    WareHouseIDS=tempOrders.getWareHouseIDS();
                }
            }
            if (object != null && (tempOrders = (TempOrderV2) object) != null && tempOrders.getOrders().size() > 0) {
                mAdapter.setData(tempOrders.getOrders());
                mAdapter.notifyDataSetChanged();
                //double price = tempOrders.TotalPayableAmount;
                //double couponPrice=tempOrders.TotalPostFeeAmount+tempOrders.TotalProductAmount-tempOrders.TotalPayableAmount;
                if (selectCoupon != null) {
                    if (!AKUtil.hasDigit(selectCoupon.Discount)) {
                        //  mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price)) + "(已优惠:" + selectCoupon.getDiscount() + ")"));
                        //  mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price))));
                        noUse.setVisibility(View.VISIBLE);
                    } else {
                        double couponPrice = Double.parseDouble(selectCoupon.Discount.substring(1, selectCoupon.Discount.length()));
                        // mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price)) + "(已优惠:¥" + Utils.moneyFormat(couponPrice) + ")"));
                        // mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price))));
                        noUse.setVisibility(View.VISIBLE);
                    }
                } else {
                    //  mTvCountPrice.setText(Html.fromHtml(getString(R.string.count_x_price_x, Utils.moneyFormat(price))));
                    noUse.setVisibility(View.GONE);
                }
                //  mTvCountPriceDetail.setText(String.format("预收运费 ¥%.2f", tempOrders.TotalPostFeeAmount));
            }
        }
    }

    private TempOrderV2 tempOrders;

    private void setLastAddress(JAddress ja) {
        Address add = new Address();
        add.setId(ja.getID());
        add.setUserName(ja.getRealName());
        add.setPostCode(ja.getPostCode());
        add.setPhone(ja.getMobile());
        add.setDetailAddress(ja.getAddress());
        add.setDefault(ja.isIsDefault());
        String area_name = "", city_name = "", province_name = "";
        if (!TextUtils.isEmpty(ja.getArea())) {
            String[] mRegions = ja.getArea().split("\\s+");
            if (mRegions.length >= 3) {
                province_name = mRegions[0];
                city_name = mRegions[1];
                area_name = mRegions[2];
            } else if (mRegions.length == 2) {
                province_name = mRegions[0];
                city_name = mRegions[1];
            } else {
                province_name = mRegions[0];
            }
        }
        //AreaDao dao = new AreaDao(getApplicationContext());
        // Area area = dao.getArea(ja.getAreaID());
        Area area = new Area();
        area.setName(area_name);
        area.setId(ja.getAreaID());
        area.setParentId(ja.getCityID());
        Area city = new Area();
        city.setId(ja.getCityID());
        city.setParentId(ja.getProvinceID());
        city.setName(city_name);
        Area province = new Area();
        province.setName(province_name);
        province.setId(ja.getProvinceID());
        add.setArea(area);
        add.setCity(city);
        add.setProvince(province);
//        if (area != null) {
//            Area city = dao.getArea(area.getParentId());
//            add.setCity(city);
//            if (city != null) {
//                Area province = dao.getArea(city.getParentId());
//                add.setProvince(province);
//            }
//        }
        UserInfoProvider.setDefaultAddress(getApplicationContext(), SpManager.getUserId(getApplicationContext()),
                add.getDetailAddress());
        SpManager.setLastSelectAddress(getApplicationContext(), SpManager.getUserId(getApplicationContext()), add);

    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        hideDialog();
        ViewHub.showLongToast(getApplicationContext(), msg);
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        hideDialog();
        ViewHub.showLongToast(getApplicationContext(), msg);
    }

    protected void hideDialog() {
        if (isDialogShowing()) {
            mLoadingDialog.stop();
        }
    }

    protected boolean isDialogShowing() {
        return (mLoadingDialog != null && mLoadingDialog.isShowing());
    }

    @Override
    public void onDistriDialogButtonClick(ShipSettingModel bean) {
        if (bean != null)
            selectShipSetting = bean;
        if (selectShipSetting != null && tv_choose_SelectShip != null)
            tv_choose_SelectShip.setText(selectShipSetting.getName());
    }
}
