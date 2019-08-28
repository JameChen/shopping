package com.nahuo.quicksale.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.Dialog.CDialog;
import com.nahuo.Dialog.CDialog2;
import com.nahuo.Dialog.CDialog3;
import com.nahuo.Dialog.PdMenu;
import com.nahuo.bean.OrderDetailBean;
import com.nahuo.bean.OrderMultiParent;
import com.nahuo.bean.OrderMultiSub;
import com.nahuo.bean.PackageListParent;
import com.nahuo.bean.SaveApplyInfo;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.OrderPayActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.TradeDialogFragment;
import com.nahuo.quicksale.TradingDetailsActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.OrderDetailAdapter;
import com.nahuo.quicksale.adapter.OrderPackageAdapter;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.OrderAction;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.controls.NFHSZDialog;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.ApplyListModel;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ReFundModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;
import com.nahuo.quicksale.orderdetail.model.TransferModel;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.PicOrVideoDownloadService;
import com.nahuo.quicksale.util.RxUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.AddItem_From_Order;
import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.CANCEL_NEW_ORDER;
import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.Comfirm_Receipt;

public class OrderDetailsActivity extends BaseAppCompatActivity implements OrderDetailAdapter.Listener, View.OnClickListener, HttpRequestListener {
    TextView tvTitleCenter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    OrderDetailAdapter adapter;
    HttpRequestHelper httpRequestHelper = new HttpRequestHelper();
    OrderDetailsActivity vThis;
    int orderID;
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDERID";
    public static final String EXTRA_ORDER_CODE = "EXTRA_ORDER_CODE";
    private String Dcim_Path;
    private LoadingDialog mLoadingDialog;
    private EventBus mEventBus = EventBus.getDefault();
    private int RefundTradeID;
    private double settleAmount;
    private String orderCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
        initData();
        getHttpOderData();
    }

    private void initData() {
        if (getIntent() != null) {
            orderID = getIntent().getIntExtra(EXTRA_ORDER_ID, 0);
            orderCode = getIntent().getStringExtra(EXTRA_ORDER_CODE);
        }
        //orderID=1740;
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFUND_BUYER_AGRESS:
            case EventBusId.SURE_GET_GOOD:
            case EventBusId.ADD_MEMO:
            case EventBusId.CHANGE_NUMBER:
            case EventBusId.REFUND_SUPP_AGRESS:
            case EventBusId.REFUND_SELLER_AGRESS:
                getHttpOderData();
                break;
        }
    }

    private void getHttpOderData() {
        HttpRequestHelper.HttpRequest request = httpRequestHelper.getRequest(getApplicationContext(),
                RequestMethod.OrderDetailMethod.GET_ORDER_DETAILS, vThis);
        request.setConvert2Class(OrderDetailBean.class);
        request.addParam("orderId", String.valueOf(orderID));
        request.addParam("orderCode", orderCode);
//        request.addParam("type", "2");
        request.doPost();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventBus != null)
            mEventBus.unregister(this);
    }

    private void initView() {
        vThis = this;
        mEventBus.register(this);
        Dcim_Path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        mLoadingDialog = new LoadingDialog(this);
        BWApplication.getInstance().registerActivity(this);
        findViewById(R.id.tvTLeft).setOnClickListener(this);
        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
        tvTitleCenter.setText(R.string.order_detail_tittle);
        tvTitleCenter.setOnClickListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        initRefreshLayout();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void initAdapter() {
        adapter = new OrderDetailAdapter(null, this);
        adapter.setOrderID(orderID);
        adapter.setmListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        mAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
    }

    private void onRefreshData() {
        getHttpOderData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTLeft:
                finish();
                break;
            case R.id.tvTitleCenter:
                if (FunctionHelper.isDoubleFastClick()) {
                    if (mRecyclerView != null)
                        mRecyclerView.scrollToPosition(0);
                }
                break;
            case R.id.tv_look_refund:
                Intent intent = new Intent(vThis, TradingDetailsActivity.class);
                intent.putExtra(TradingDetailsActivity.ORDERID, RefundTradeID);
                intent.putExtra(TradingDetailsActivity.SETTLEAMOUNT, settleAmount);
                intent.putExtra(TradingDetailsActivity.EXTRA_TYPE, TradingDetailsActivity.TYPE_NEWDETAIL);
                vThis.startActivity(intent);
                //getRefundDetails(vThis, RefundTradeID);
                break;
        }
    }

    /**
     * 获取订单退款信息
     *
     * @author James Chen
     * @create time in 2018/5/9 14:31
     */
    public void getRefundDetails(final Context context, final int oid) {
        if (oid < 0) {
            ViewHub.showShortToast(context, "没有此订单");
        } else {
            addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi("getOrderItemForRefund")
                    .GetTransferTradeInfo(oid).compose(RxUtil.<PinHuoResponse<TransferModel>>rxSchedulerHelper())
                    .compose(RxUtil.<TransferModel>handleResult()).subscribeWith(new CommonSubscriber<TransferModel>(context, true, R.string.loading) {
                        @Override
                        public void onNext(final TransferModel order) {
                            super.onNext(order);
                            if (order != null) {
                                ReFundModel item = new ReFundModel();
                                item.setType(order.TypeName);
                                item.setMoney(order.RefundAmount);
                                item.setTime(order.CreateDate);
                                item.setPerson(order.RefundUserName);
                                item.setState(order.Statu);
                                item.setContent(order.Desc);
                                final TradeDialogFragment f = TradeDialogFragment.newInstance(item);
                                f.show(((BaseOrderDetailActivity) context).getSupportFragmentManager(), "TradeDialogFragment");
                            }
                        }
                    }));
        }
    }

    protected boolean isDialogShowing() {
        return (mLoadingDialog != null && mLoadingDialog.isShowing());
    }

    protected void hideDialog() {
        if (isDialogShowing()) {
            mLoadingDialog.stop();
        }
    }

    @Override
    public void onRequestStart(String method) {
        if (RequestMethod.OrderDetailMethod.GET_ORDER_DETAILS.equals(method)) {
            if (!isDialogShowing())
                mLoadingDialog.start("获取订单详情信息...");
        } else if (method.equals(AddItem_From_Order)) {
            mLoadingDialog.start("正在加入中...");
        } else if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单
            mLoadingDialog.start("取消订单中...");
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        hideDialog();
        if (RequestMethod.OrderDetailMethod.GET_ORDER_DETAILS.equals(method)) {
            mSwipeRefreshLayout.setRefreshing(false);
            try {
                OrderDetailBean bean = (OrderDetailBean) object;
                adapter.setNewData(null);
                adapter.setOrderDetailBean(bean);
                adapter.removeAllHeaderView();
                adapter.removeAllFooterView();
                if (bean != null) {
                    addConsigneeInfoHeadView(bean);
                    if (!ListUtils.isEmpty(bean.getPackageList()))
                        addPackageHeadView(bean);
                    addCarriageSettle(bean);
                    List<OrderDetailBean.SenderListBean> senderList = bean.getSenderList();
                    if (!ListUtils.isEmpty(senderList)) {
                        ArrayList<MultiItemEntity> res = new ArrayList<>();
                        for (int i = 0; i < senderList.size(); i++) {
                            OrderDetailBean.SenderListBean senderListBean = senderList.get(i);
                            OrderMultiParent parent = new OrderMultiParent();
                            parent.setName(senderListBean.getName());
                            if (i == 0) {
                                parent.isShowTop = false;
                            } else {
                                parent.isShowTop = true;
                            }
                            if (!ListUtils.isEmpty(senderListBean.getChildOrders())) {

                                List<OrderMultiSub> subs = new ArrayList<>();
                                for (int j = 0; j < senderListBean.getChildOrders().size(); j++) {
                                    OrderDetailBean.SenderListBean.ChildOrdersBean childOrdersBean = senderListBean.getChildOrders().get(j);
                                    if (childOrdersBean != null) {
                                        OrderMultiSub orderMultiSub = new OrderMultiSub();
                                        if (j == 0) {
                                            orderMultiSub.isShowTop = false;
                                        } else {
                                            orderMultiSub.isShowTop = true;
                                        }
                                        orderMultiSub.setCoinSummary(childOrdersBean.getCoinSummary());
                                        orderMultiSub.setTotalQty(childOrdersBean.getTotalQty());
                                        orderMultiSub.setCode(childOrdersBean.getCode());
                                        orderMultiSub.setCover(childOrdersBean.getCover());
                                        orderMultiSub.setItemID(childOrdersBean.getItemID());
                                        orderMultiSub.setOrderID(childOrdersBean.getOrderID());
                                        orderMultiSub.setPrice(childOrdersBean.getPrice());
                                        orderMultiSub.setSummary(childOrdersBean.getSummary());
                                        orderMultiSub.setTitle(childOrdersBean.getTitle());
                                        orderMultiSub.setTransferID(childOrdersBean.getTransferID());
                                        orderMultiSub.setButtons(childOrdersBean.getButtons());
                                        orderMultiSub.setProducts(childOrdersBean.getProducts());
                                        orderMultiSub.setBuyerShopID(childOrdersBean.getBuyerShopID());
                                        orderMultiSub.setBuyerShopName(childOrdersBean.getBuyerShopName());
                                        subs.add(orderMultiSub);
                                    }
                                }
                                parent.setSubItems(subs);
                            }
                            res.add(parent);
                        }
                        adapter.setNewData(res);
                        adapter.expandAll();
                    }
                    addBottomFootView(bean);
                }
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单成功
            ViewHub.showShortToast(vThis, "取消订单成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CHANGE_NUMBER));
        } else if (Comfirm_Receipt.equals(method)) {
            ViewHub.showShortToast(vThis, "签收成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CHANGE_NUMBER));
        } else if (method.equals(AddItem_From_Order)) {
            //   ViewHub.showShortToast(vThis, "添加成功");
            //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
//            ViewHub.showLightPopDialog((Activity) vThis, "提示", "此笔订单的商品已经全部还原到拿货车了。您可以在拿货车对商品编辑后重新下单！", "关闭", "进入拿物车", new LightPopDialog.PopDialogListener() {
//                @Override
//                public void onPopDialogButtonClick(int which) {
//                    vThis.startActivity(new Intent(vThis, ShopCartNewActivity.class));
//                }
//            });
            CDialog dialog = new CDialog(this);
            dialog.setHasTittle(true).setTitle("已经添加成功啦！").setMessage("此笔订单的商品已经全部添加到拿货车了。您可以在拿货车对商品进行重新编辑并下单！").setPositive("进入拿物车", new CDialog.PopDialogListener() {
                @Override
                public void onPopDialogButtonClick(int which) {
                    vThis.startActivity(new Intent(vThis, ShopCartNewActivity.class));
                }
            }).setNegative("关闭", null).show();
        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        hideDialog();
        if (RequestMethod.OrderDetailMethod.GET_ORDER_DETAILS.equals(method)) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单
            ViewHub.showShortToast(vThis, "" + msg);
        } else if (method.equals(AddItem_From_Order)) {
            //加入失败
            ViewHub.showShortToast(vThis, "" + msg);
        } else if (method.equals(Comfirm_Receipt)) {
            //签收失败
            ViewHub.showShortToast(vThis, "" + msg);
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        hideDialog();
        if (RequestMethod.OrderDetailMethod.GET_ORDER_DETAILS.equals(method)) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单
            ViewHub.showShortToast(vThis, "" + msg);
        } else if (method.equals(AddItem_From_Order)) {
            //加入失败
            ViewHub.showShortToast(vThis, "" + msg);
        } else if (method.equals(Comfirm_Receipt)) {
            //签收失败
            ViewHub.showShortToast(vThis, "" + msg);
        }
    }

    TextView tv_user_name, tv_phone, tv_address;
    TextView tv_order_code, tv_copy_code, tv_status, tv_create_time;
    LinearLayout layout_address_buttons;
    private ButtonOnClickListener mBtnOnClickListener;

    /**
     * 添加頭部
     *
     * @author James Chen
     * @create time in 2018/3/7 11:04
     */
    private void addConsigneeInfoHeadView(final OrderDetailBean orderDetailBean) {
        View headView = getLayoutInflater().inflate(R.layout.order_detail_head_address, (ViewGroup) mRecyclerView.getParent(), false);
        tv_user_name = (TextView) headView.findViewById(R.id.tv_user_name);
        tv_phone = (TextView) headView.findViewById(R.id.tv_phone);
        tv_address = (TextView) headView.findViewById(R.id.tv_address);
        tv_order_code = (TextView) headView.findViewById(R.id.tv_order_code);
        tv_copy_code = (TextView) headView.findViewById(R.id.tv_copy_code);
        tv_status = (TextView) headView.findViewById(R.id.tv_status);
        tv_create_time = (TextView) headView.findViewById(R.id.tv_create_time);
        layout_address_buttons = (LinearLayout) headView.findViewById(R.id.layout_address_buttons);
        tv_copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderDetailBean != null) {
                    if (TextUtils.isEmpty(orderDetailBean.getCode()))
                        ViewHub.showShortToast(vThis, "单号为空");
                    else {
                        Utils.addNewToClipboard(vThis, orderDetailBean.getCode());
                    }
                } else {
                    ViewHub.showShortToast(vThis, "单号为空");
                }

            }
        });
        if (orderDetailBean != null) {
            if (TextUtils.isEmpty(orderDetailBean.getCode()))
                tv_order_code.setText("");
            else
                tv_order_code.setText("拿货单：" + orderDetailBean.getCode());
            tv_status.setText(orderDetailBean.getStatu());
            tv_create_time.setText("创建时间：" + orderDetailBean.getCreateTime());
            OrderDetailBean.ConsigneeInfoBean consigneeInfoBean = orderDetailBean.getConsigneeInfo();
            if (consigneeInfoBean != null) {
                if (TextUtils.isEmpty(consigneeInfoBean.getRecipient())) {
                    tv_user_name.setText("");
                } else {
                    tv_user_name.setText("收货人：" + consigneeInfoBean.getRecipient());
                }
                if (TextUtils.isEmpty(consigneeInfoBean.getAddress()))
                    tv_address.setText("");
                else
                    tv_address.setText("收货地址：" + consigneeInfoBean.getAddress());

                if (FunctionHelper.isMobileNum(consigneeInfoBean.getMobile())) {
                    String phonenum = consigneeInfoBean.getMobile();
                    if (!TextUtils.isEmpty(phonenum) && phonenum.length() > 6) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < phonenum.length(); i++) {
                            char c = phonenum.charAt(i);
                            if (i >= 3 && i <= 6) {
                                sb.append('*');
                            } else {
                                sb.append(c);
                            }
                        }
                        tv_phone.setText(sb.toString());
                    } else {
                        tv_phone.setText(consigneeInfoBean.getMobile());
                    }
                } else {
                    tv_phone.setText(consigneeInfoBean.getMobile());
                }
            }
            layout_address_buttons.removeAllViews();
            if (!ListUtils.isEmpty(orderDetailBean.getButtons())) {
                addOrderDetailButton(layout_address_buttons, orderDetailBean.getButtons(), mBtnOnClickListener, orderDetailBean);
            }
        }
        adapter.addHeaderView(headView);
    }

    /**
     * 添加頭部
     *
     * @author James Chen
     * @create time in 2018/3/7 11:04
     */
    private TextView tv_fee, tv_amount, tv_c_status, tv_look_refund;

    private void addCarriageSettle(final OrderDetailBean orderDetailBean) {
        View headView = getLayoutInflater().inflate(R.layout.order_detail_head_carriagesettle, (ViewGroup) mRecyclerView.getParent(), false);
        tv_fee = (TextView) headView.findViewById(R.id.tv_fee);
        tv_amount = (TextView) headView.findViewById(R.id.tv_amount);
        tv_c_status = (TextView) headView.findViewById(R.id.tv_c_status);
        tv_look_refund = (TextView) headView.findViewById(R.id.tv_look_refund);
        tv_look_refund.setOnClickListener(this);

        if (orderDetailBean != null) {
            OrderDetailBean.CarriageSettleBean carriageSettleBean = orderDetailBean.getCarriageSettle();
            if (carriageSettleBean != null) {
                RefundTradeID = carriageSettleBean.getRefundTradeID();
                if (RefundTradeID > 0) {
                    tv_look_refund.setVisibility(View.VISIBLE);
                } else {
                    tv_look_refund.setVisibility(View.GONE);
                }
                tv_c_status.setText(carriageSettleBean.getStatuX());
                tv_fee.setText("本单总共预付运费：¥" + carriageSettleBean.getPrepayAmount()
                        + ",实际运费：¥" + carriageSettleBean.getActualAmount());
                settleAmount = carriageSettleBean.getAmount();
                tv_amount.setText("结余：¥" + carriageSettleBean.getAmount());
                if (carriageSettleBean.isIsShow()) {
                    adapter.addHeaderView(headView);
                }
            }
        }
    }

    /**
     * 添加包裹頭部
     *
     * @author James Chen
     * @create time in 2018/3/7 11:04
     */
    RecyclerView packageView;
    OrderPackageAdapter packageAdapter;

    private void addPackageHeadView(OrderDetailBean orderDetailBean) {
        View headView = getLayoutInflater().inflate(R.layout.order_detail_head_package, (ViewGroup) mRecyclerView.getParent(), false);
        packageView = (RecyclerView) headView.findViewById(R.id.pack_list);
        packageView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<MultiItemEntity> res = new ArrayList<>();
        PackageListParent parent = new PackageListParent();
        parent.setName("我的包裹");
        for (int i = 0; i < orderDetailBean.getPackageList().size(); i++) {
            if (i == 0) {
                orderDetailBean.getPackageList().get(i).isShowTop = false;
            } else {
                orderDetailBean.getPackageList().get(i).isShowTop = true;
            }
            orderDetailBean.getPackageList().get(i).setPosStr("单号" + (i + 1) + "：");
        }
        parent.setSubItems(orderDetailBean.getPackageList());
        res.add(parent);
        packageAdapter = new OrderPackageAdapter(res, vThis);
        packageAdapter.setOrderId(orderID);
        packageView.setAdapter(packageAdapter);
        packageAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        packageAdapter.expandAll();
        adapter.addHeaderView(headView);
    }

    /**
     * 添加底部
     *
     * @author James Chen
     * @create time in 2018/3/7 11:04
     */
    TextView txt_order_info_money, txt_order_info_buyway, txt_order_info_coupon, txt_order_info_buyway_weight,
            tv_bottom_line1, tv_bottom_line2, tv_bottom_line3, tv_bottom_line4,
            tv_bottom_line5, tv_left_fee;

    private void addBottomFootView(OrderDetailBean orderDetailBean) {
        View headView = getLayoutInflater().inflate(R.layout.order_detail_foot, (ViewGroup) mRecyclerView.getParent(), false);
        txt_order_info_money = (TextView) headView.findViewById(R.id.txt_order_info_money);
        txt_order_info_buyway = (TextView) headView.findViewById(R.id.txt_order_info_buyway);
        txt_order_info_coupon = (TextView) headView.findViewById(R.id.txt_order_info_coupon);
        txt_order_info_buyway_weight = (TextView) headView.findViewById(R.id.txt_order_info_buyway_weight);
        tv_left_fee = (TextView) headView.findViewById(R.id.tv_left_fee);
        tv_bottom_line1 = (TextView) headView.findViewById(R.id.tv_bottom_line1);
        tv_bottom_line2 = (TextView) headView.findViewById(R.id.tv_bottom_line2);
        tv_bottom_line3 = (TextView) headView.findViewById(R.id.tv_bottom_line3);
        tv_bottom_line4 = (TextView) headView.findViewById(R.id.tv_bottom_line4);
        tv_bottom_line5 = (TextView) headView.findViewById(R.id.tv_bottom_line5);
        tv_left_fee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vThis, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 93595);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                startActivity(intent);
            }
        });
        if (orderDetailBean != null) {
            if (TextUtils.isEmpty(orderDetailBean.getCode()))
                tv_order_code.setText("");
            else
                tv_order_code.setText("拿货单：" + orderDetailBean.getCode());
            txt_order_info_money.setText("¥" + orderDetailBean.getProductAmount());
            txt_order_info_buyway.setText("¥" + orderDetailBean.getPostFee());
            txt_order_info_coupon.setText("¥" + orderDetailBean.getDiscountAmount());
            txt_order_info_buyway_weight.setText(orderDetailBean.getWeight());
            OrderDetailBean.BottomInfoBean bottomInfo = orderDetailBean.getBottomInfo();
            if (bottomInfo != null) {
                if (TextUtils.isEmpty(bottomInfo.getButtomLine1())) {
                    tv_bottom_line1.setVisibility(View.GONE);
                } else {
                    tv_bottom_line1.setVisibility(View.VISIBLE);
                    tv_bottom_line1.setText(bottomInfo.getButtomLine1());
                }
                if (TextUtils.isEmpty(bottomInfo.getButtomLine2())) {
                    tv_bottom_line2.setVisibility(View.GONE);
                } else {
                    tv_bottom_line2.setVisibility(View.VISIBLE);
                    tv_bottom_line2.setText(bottomInfo.getButtomLine2());
                }
                if (TextUtils.isEmpty(bottomInfo.getButtomLine3())) {
                    tv_bottom_line3.setVisibility(View.GONE);
                } else {
                    tv_bottom_line3.setVisibility(View.VISIBLE);
                    tv_bottom_line3.setText(bottomInfo.getButtomLine3());
                }
                if (TextUtils.isEmpty(bottomInfo.getButtomLine4())) {
                    tv_bottom_line4.setVisibility(View.GONE);
                } else {
                    tv_bottom_line4.setVisibility(View.VISIBLE);
                    tv_bottom_line4.setText(bottomInfo.getButtomLine4());
                }
                if (TextUtils.isEmpty(bottomInfo.getButtomLine5())) {
                    tv_bottom_line5.setVisibility(View.GONE);
                } else {
                    tv_bottom_line5.setVisibility(View.VISIBLE);
                    tv_bottom_line5.setText(bottomInfo.getButtomLine5());
                }

            }

        }
        adapter.addFooterView(headView);
    }

    public void addOrderDetailButton(LinearLayout parent, List<OrderButton> buttons, ButtonOnClickListener l
            , OrderDetailBean bean) {
        l = new ButtonOnClickListener();
        parent.removeAllViews();
        l.setBean(bean);
        if (buttons != null) {
            int margin = ScreenUtils.dip2px(vThis, 10);
            int top_margin = ScreenUtils.dip2px(vThis, 6);
            int top_pad = ScreenUtils.dip2px(vThis, 4);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = top_margin;
            for (OrderButton model : buttons) {
//                if (!model.isEnable())
//                    continue;
                TextView child = new TextView(parent.getContext());
                child.setPadding(margin, top_pad, margin, top_pad);
                child.setEllipsize(TextUtils.TruncateAt.END);
                child.setSingleLine(true);
                child.setTextSize(15);
                child.setText(model.getTitle());
                child.setGravity(Gravity.CENTER_VERTICAL);
                child.setClickable(model.isEnable());
                // child.getPaint().measureText(text, start, end)
                highlightButton(child, model.isPoint(), model.getType().equals("text"));
                if (model.isEnable()) {
                    child.setTag(model);
                    child.setOnClickListener(l);
                    child.setClickable(true);
                    child.setEnabled(true);
                } else {
                    child.setClickable(false);
                    child.setEnabled(false);
                }
                parent.addView(child, params);
            }
        }
    }

    String workWareHouseID = "", workWareHouseIDs = "";

    @Override
    public void downloadPicandVideo(OrderMultiSub bean) {
        //一件下图
        if (bean != null) {
            mDetailId = bean.getItemID();
            new Task().execute();
        }
    }

    private String fileName = "", vfileName = "";
    private ShopItemModel mShopItem;
    private int mDetailId, mQId;
    private List<String> nPicsList = new ArrayList<>();
    private List<String> nVideosList = new ArrayList<>();

    //获取详情图片和视频
    private class Task extends AsyncTask<Object, Void, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            //拼货商品信息
            try {
                mShopItem = BuyOnlineAPI.getInstance().getPiHuoItemInfo(mDetailId, mQId, PublicData.getCookie(vThis));
                return mShopItem;
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            mLoadingDialog.start("正在获取数据中");

        }

        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(vThis, ((String) result).replace("error:", ""));
            } else {
                try {
                    ShopItemModel bean = (ShopItemModel) result;
                    if (bean != null) {
                        List<String> picList = new ArrayList<>();
                        List<String> videoList = new ArrayList<>();
                        List<String> videos = bean.getVideos();
                        String[] picArray = bean.getImages();
                        if (videos != null) {
                            if (videos.size() > 0) {
                                videoList.clear();
                                videoList.addAll(videos);
                            }
                        }
                        if (picArray != null) {
                            if (picArray.length > 0) {
                                for (String url : picArray) {
                                    if (!TextUtils.isEmpty(url)) {
                                        picList.add(ImageUrlExtends.getImageUrl(url, 21));
                                    }
                                }
                            }
                        }
                        if (ListUtils.isEmpty(videoList) && ListUtils.isEmpty(picList)) {
                            ViewHub.showShortToast(vThis, "没有图片和视频资源");
                        } else {
                            if (!ListUtils.isEmpty(picList)) {
                                File downloadDirectory = new File(Dcim_Path + "/" + Const.IMAGES_CASH_PATH);
                                if (!downloadDirectory.exists()) {
                                    downloadDirectory.mkdirs();
                                }
                                nPicsList.clear();
                                for (String url : picList) {
                                    if (!TextUtils.isEmpty(url)) {
                                        try {
                                            fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf("!"));
                                        } catch (Exception e) {
                                            fileName = "/" + System.currentTimeMillis() + ".jpg";
                                        }
                                        File cacheFile = new File(Dcim_Path + "/" + Const.IMAGES_CASH_PATH + fileName);
                                        if (!cacheFile.exists()) {
                                            nPicsList.add(url);
                                        }
                                    }
                                }

                            }
                            if (!ListUtils.isEmpty(videoList)) {
                                File downloadDirectory = new File(Dcim_Path + VideoActivity1.PINHUP_ROOT_DIRECTORY);
                                if (!downloadDirectory.exists()) {
                                    downloadDirectory.mkdirs();
                                }
                                nVideosList.clear();
                                for (String url : videoList) {
                                    if (!TextUtils.isEmpty(url)) {
                                        try {
                                            vfileName = url.substring(url.lastIndexOf("/"), url.length());
                                        } catch (Exception e) {
                                            vfileName = "/" + System.currentTimeMillis() + ".mp4";
                                        }
                                        File cacheFile = new File(Dcim_Path + VideoActivity1.PINHUP_ROOT_DIRECTORY + vfileName);
                                        if (!cacheFile.exists()) {
                                            nVideosList.add(url);
                                        }
                                    }
                                }
                            }
                            if (ListUtils.isEmpty(nPicsList) && ListUtils.isEmpty(nVideosList)) {
                                ViewHub.showLongToast(vThis, "图片保存在：DCIM/weipu/weipu_save" + "\n" + "视频保存在：DCIM/pinhuo/pinhuo_video_save");
                            } else {
                                isFinish = false;
                                PicOrVideoDownloadService.getInstace(vThis, Dcim_Path, nPicsList, nVideosList, new PicOrVideoDownloadService.DownloadStateListener() {
                                    @Override
                                    public void onFinish() {
                                        isFinish = true;


                                    }

                                    @Override
                                    public void onFailed() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ViewHub.showShortToast(vThis, "下载失败");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onUpdate(int pSize, int pTotalCount, int vSize, int vTotalCount, int type) {
                                        Message msg = handler.obtainMessage();
                                        /**
                                         * 将Message对象发送到目标对象
                                         * 所谓的目标对象，就是生成该msg对象的handler对象
                                         */
                                        Bundle b = new Bundle();
                                        b.putInt("type", type);
                                        b.putInt("pSize", pSize);
                                        b.putInt("pTotalCount", pTotalCount);
                                        b.putInt("vSize", vSize);
                                        b.putInt("vTotalCount", vTotalCount);
                                        msg.setData(b);
                                        msg.sendToTarget();
                                    }
                                }).startDownload();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    boolean isFinish = false;
    PdMenu pdMenu = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Bundle b = msg.getData();
                int type = b.getInt("type");
                int pSize = b.getInt("pSize");
                int pTotalCount = b.getInt("pTotalCount");
                int vSize = b.getInt("vSize");
                int vTotalCount = b.getInt("vTotalCount");
                pdMenu = PdMenu.getInstance(vThis);
                pdMenu.dShow("正在下载图片" + pSize + "/" + pTotalCount + "\n" + "正在下载视频" + vSize + "/" + vTotalCount);
                if (type == PicOrVideoDownloadService.TYPE_PIC_VIDEO || type == PicOrVideoDownloadService.TYPE_VIDEO) {
                    if (vSize >= vTotalCount) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ViewHub.showShortToast(vThis, "下载完成");
                                if (pdMenu != null) {
                                    pdMenu.dismiss();
                                    pdMenu = null;
                                }
                            }
                        }, 1000);

                    }
                } else if (type == PicOrVideoDownloadService.TYPE_PIC) {
                    if (pSize >= pTotalCount) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ViewHub.showShortToast(vThis, "下载完成");
                                if (pdMenu != null) {
                                    pdMenu.dismiss();
                                    pdMenu = null;
                                }
                            }
                        }, 1000);

                    }
                }
            }

        }
    };

    private class ButtonOnClickListener implements View.OnClickListener {
        OrderDetailBean bean;

        public void setBean(OrderDetailBean bean) {
            this.bean = bean;
        }

        @Override
        public void onClick(final View v) {
            final OrderButton btn = (OrderButton) v.getTag();
            String action = btn.getAction();
            if (OrderAction.GOTO_CHANGCI.equals(action)) {// 供货商修改价格
                try {
                    if (btn.getData() instanceof  Double) {
                        int defectiveID = new Double(Double.parseDouble(btn.getData().toString())).intValue();
                        QuickSaleApi.getRecommendShopItems(vThis,
                                httpRequestHelper,
                                vThis,
                                defectiveID,
                                0,
                                20,
                                "",
                                Const.SortIndex.DefaultDesc,
                                -1,
                                0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (OrderAction.SALE_AFTER.equals(action)) {
                try {
                    if (btn.getData() instanceof  Double) {
                        int defectiveID = new Double(Double.parseDouble(btn.getData().toString())).intValue();
                        if (defectiveID < 0) {
                            Intent vendorIntent = new Intent(vThis, PackageListActivity.class);
                            vendorIntent.putExtra(PackageListActivity.EXTRA_ORDERID, orderID);
                            vThis.startActivity(vendorIntent);
                        } else {

                            Intent intent = new Intent(vThis, CustomerServiceActivity.class);
                            intent.putExtra(CustomerServiceActivity.EXTRA_TYPE, CustomerServiceActivity.TYPE_AFTER_SALES_APPLY_DETAIL);
                            intent.putExtra(CustomerServiceActivity.EXTRA_APPLYID, defectiveID);
                            vThis.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (OrderAction.SUPPLIERS_CHANGE_PRICE.equals(action)) {// 供货商修改价格
                // OrderAPI.getSendGoodOrderDetail(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getShipID());
            } else if (OrderAction.SELLER_CHANGE_PRICE.equals(action)) {// 卖家改价
                // OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                OrderAPI.getSellerOrderDetail(vThis, httpRequestHelper, vThis, orderID);
            } else if (OrderAction.SELLER_CANCEL.equals(action)) {// 卖家取消
                cancelOrder(bean, btn);
            } else if (OrderAction.BUYER_PAY.equals(action)) {// 买家支付

                Intent it = new Intent(vThis, OrderPayActivity.class);
                it.putExtra(OrderPayActivity.INTENT_PAY_ORDER, bean.getCode() + "");
                it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, bean.getOrderIDS() + "");
                it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, Double.parseDouble(bean.getPayableAmount()));
                vThis.startActivity(it);

            } else if (OrderAction.BUYER_CANCEL.equals(action)) {// 买家取消
                cancelOrder(bean, btn);
            } else if (OrderAction.SUPPLIER_SHIP.equals(action)) {// 供货商发货
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                int shipId = order.getShipID();
//                if (shipId == 0 && order.getShipOrder() != null) {
//                    shipId = order.getShipOrder().ID;
//                }
//                new DialogChooseExpress(v.getContext(), shipId).show();
            } else if (OrderAction.BUYER_CONFIRM_RECEIPT.equals(action)) {// 买家确认收货
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                new DialogSureGetGoods(vThis, order.getOrderId()).show();
//                ViewHub.showLightPopDialog((Activity) vThis, "提示", "确认已收齐发货的商品了，确认后状态将变成“已完结”", "关闭", "确认签收", new LightPopDialog.PopDialogListener() {
//                    @Override
//                    public void onPopDialogButtonClick(int which) {
//                        if (bean != null) {
//                            OrderAPI.comfirmReceipt(vThis, httpRequestHelper, vThis, orderID);
//                        } else {
//                            ViewHub.showShortToast(vThis, "没有订单");
//                        }
//                    }
//                });
                CDialog3 dialog = new CDialog3(vThis);
                dialog.setHasTittle(false).setTitle("").setMessage("确认已收齐发货的商品了，确认后状态将变成“已完结”").setPositive("关闭", new CDialog3.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                    }
                }).setNegative("确认签收", new CDialog3.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        if (bean != null) {
                            OrderAPI.comfirmReceipt(vThis, httpRequestHelper, vThis, orderID);
                        } else {
                            ViewHub.showShortToast(vThis, "没有订单");
                        }
                    }
                }).show();
            } else if (OrderAction.BUYER_RETURN.equals(action)) {// 买家申请退款
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                RefundApplyDialog.getInstance(mContext, action, 0, order.getOrderId()).show();
            } else if (OrderAction.SELLER_RETURN_BILL.equals(action) || OrderAction.SELLER_FOUND_BILL.equals(action)) {
                // 卖家维权) {// 卖家退款
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                Refund refund = order.getRefund();
//                if (refund != null) {// 进入卖家退款详细页
//                    Intent retIntent = new Intent(mContext, RefundBySellerActivity.class);
//                    retIntent.putExtra("ID", refund.getRefundID());
//                    mContext.startActivity(retIntent);
//                } else {
//                    ViewHub.showShortToast(mContext, "refund is null");
//                }
            } else if (OrderAction.BUYER_RETURN_BILL.equals(action) || OrderAction.BUYER_FOUND_BILL.equals(action)) {
                // 买家维权// 买家退款
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                Refund refund = order.getRefund();
//                if (refund != null) {
//                    Intent retIntent = new Intent(mContext, RefundByBuyerActivity.class);
//                    retIntent.putExtra("ID", refund.getRefundID());
//                    mContext.startActivity(retIntent);
//                } else {
//                    ViewHub.showShortToast(mContext, "refund is null");
//                }
            } else if (OrderAction.BUYER_EXPRESS.equals(action)) {// 买家物流
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                Intent shipIntent = new Intent(mContext, ShipActivity.class);
//                shipIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, order.getOrderId());
//                mContext.startActivity(shipIntent);
            } else if (OrderAction.SELLER_EXPRESS.equals(action)) {// 卖家物流
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                Intent shipIntent = new Intent(mContext, ShipActivity.class);
//                shipIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, order.getOrderId());
//                mContext.startActivity(shipIntent);
            } else if (Const.OrderAction.SUPPLIERS_RETUNR_BILL.equals(action) || OrderAction.SUPPLIERS_FOUND_BILL.equals(action)) {
                //供货商退款、供货商维权
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                ShipperRefund refund = order.getShipperRefund();
//                if (refund != null) {
//                    Intent retIntent = new Intent(mContext, RefundBySupperActivity.class);
//                    retIntent.putExtra("ID", refund.ShipperRefundID);
//                    mContext.startActivity(retIntent);
//                }
            } else if (OrderAction.SHOW_TRANSFER.equals(action)) {
                //展示退款信息
                //  showTransfer(v, btn);
            } else if (OrderAction.BUHUO.equals(action)) {// 我要补货

//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
////                String msg = order.getReplenishmentRemark();
////                new DialogReplenishment(v.getContext(), msg, itemID).show();
//                //修改不传去掉qid
//                Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, order.getOrderItems().get(0).getAgentItemID());
                //intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, order.getQSID());
                //    v.getContext().startActivity(intent);
            } else if (OrderAction.MONEY_BACK.equals(action)) {// 已退
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                Intent intent = new Intent(v.getContext(), TradingDetailsActivity.class);
//                intent.putExtra(TradingDetailsActivity.ORDERID, order.getOrderId());
//                v.getContext().startActivity(intent);
            } else if (OrderAction.ONE_KEY_JOINS_THE_TRUCK.equals(action)) {
                //一键加入拿货车
                if (orderID > 0) {
                    OrderAPI.addItemFromOrder(vThis, httpRequestHelper, vThis, orderID);
                } else {
                    ViewHub.showShortToast(vThis, "没有订单");
                }
            } else if (OrderAction.CONTACT_CUSTOMER.equals(action)) {
                //联系客服
                ChatUtl.goToChatActivity(vThis);
            } else if (OrderAction.DELIVERY.equals(action)) {
                //发货方式
                try {
                    setDivily();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (OrderAction.NOTISSUEINVOICES.equals(action)) {
                if (vThis != null)
                    vThis.saveOrderShipConfig(vThis, orderID);
            }

        }

        private void setDivily() {
            if (!TextUtils.isEmpty(bean.getWareHouseIDS())) {
                if (bean.getWareHouseIDS().contains(",")) {
                    String[] ids = bean.getWareHouseIDS().split(",");
                    workWareHouseID = ids[0];
                } else {
                    workWareHouseID = bean.getWareHouseIDS();
                }
            }
            workWareHouseIDs = bean.getWareHouseIDS();
            new AsyncTask<Void, Void, Object>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (mLoadingDialog != null)
                        mLoadingDialog.start("加载中...");
                }

                @Override
                protected Object doInBackground(Void... params) {
                    try {
                        return QuickSaleApi.getApplyList2(vThis, workWareHouseID);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "error:" + e.getMessage();
                    }
                }

                @Override
                protected void onPostExecute(final Object result) {
                    super.onPostExecute(result);
                    if (mLoadingDialog != null)
                        mLoadingDialog.stop();
                    if (result instanceof String && ((String) result).startsWith("error:")) {
                        ViewHub.showShortToast(vThis, ((String) result).replace("error:", ""));
                    } else {
                        ApplyListModel workApplyData = (ApplyListModel) result;
                        final NFHSZDialog dialog = NFHSZDialog.newInstance(workApplyData);
                        dialog.mContext = vThis;
                        final NFHSZDialog ddd = dialog;
                        dialog.setDialogListener(new NFHSZDialog.FHSZDialogListener() {
                            @Override
                            public void FHSZResult(final int typeid, final int value) {
                                dialog.dismiss();
//                                    if (typeid == 0 && workApplyData.getApplyInfo().getTypeName().equals("仓库安排")) {
//                                        ViewHub.showLongToast(mContext, "默认仓库安排，无需申请");
//                                        return;
//                                    }
                                new AsyncTask<Void, Void, Object>() {

                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        mLoadingDialog.start("保存中");
                                    }

                                    @Override
                                    protected Object doInBackground(Void... params) {
                                        try {
                                            return QuickSaleApi.setSubmitApply2(vThis, workWareHouseIDs, typeid, value);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return "error:" + e.getMessage();
                                        }
                                    }

                                    @Override
                                    protected void onPostExecute(Object result) {
                                        super.onPostExecute(result);
                                        mLoadingDialog.stop();
                                        if (result instanceof String && ((String) result).startsWith("error:")) {
                                            ViewHub.showLongToast(vThis, ((String) result).replace("error:", ""));
                                            return;
                                        } else if (result instanceof SaveApplyInfo) {
                                            SaveApplyInfo info = (SaveApplyInfo) result;
                                            if (info != null) {
//                                                    if (info.getApplyInfo() != null) {
//                                                        if (tv_choose_delivery != null)
//                                                            tv_choose_delivery.setText(info.getApplyInfo().getDesc());
//                                                    }
                                                ViewHub.showLongToast(vThis, info.getMessage() + "");
                                            }

                                        }

                                    }

                                }.execute();
                            }
                        });
                        dialog.show(((Activity) vThis).getFragmentManager(), "FHSZDialog");
                    }
                }

            }.execute();
        }

    }

    /**
     * @description 取消订单
     * @created 2015-4-28 下午3:01:56
     * @author ZZB
     */
    private void cancelOrder(final OrderDetailBean bean, final OrderButton btn) {
//        ViewHub.showLightPopDialog((Activity) vThis, "确定要取消该订单吗？", "取消订单后，您可以进入“已取消”列表，一键将商品重新添加到拿货车哦！", "我再想想", "确定", new LightPopDialog.PopDialogListener() {
//            @Override
//            public void onPopDialogButtonClick(int which) {
//                if (orderID > 0) {
//                    OrderAPI.cancelNewOrder(vThis, httpRequestHelper, vThis, orderID);
//                } else {
//                    ViewHub.showShortToast(vThis, "没有订单");
//                }
//
//            }
//        });
        CDialog2 dialog = new CDialog2(vThis);
        dialog.setHasTittle(true).setTitle("确定要取消该订单吗？").setMessage("取消订单后，您可以进入“已取消”列表，一键将商品重新添加到拿货车哦！").setPositive("确定取消", new CDialog2.PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                if (bean != null) {
                    OrderAPI.cancelNewOrder(vThis, httpRequestHelper, vThis, orderID);
                } else {
                    ViewHub.showShortToast(vThis, "没有订单");
                }
            }
        }).setNegative("我再想想", null).show();
    }

    private void highlightButton(TextView btn, boolean highlight, boolean isText) {

        if (isText) {
            btn.setBackgroundColor(getMyColor(R.color.btn_bg_gray));
            btn.setTextColor(getMyColor(R.color.lightblack));
        } else {
            btn.setBackgroundResource(highlight ? R.drawable.order_button_red_bg : R.drawable.order_button_white_gray_bg);
            btn.setTextColor(highlight ? vThis.getResources().getColor(R.color.white) : vThis.getResources().getColor(R.color.txt_gray));
        }
    }

    int getMyColor(int colorId) {
        return vThis.getResources().getColor(colorId);
    }

}
