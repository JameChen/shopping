package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.Dialog.CDialog;
import com.nahuo.Dialog.CDialog2;
import com.nahuo.bean.OrderDetailBean;
import com.nahuo.bean.OrderMultiParent;
import com.nahuo.bean.OrderMultiSub;
import com.nahuo.bean.ProductsBean;
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.ChangePriceAgentDialog;
import com.nahuo.quicksale.ChangePriceDialog1;
import com.nahuo.quicksale.DialogOrderRecordDetailFragment;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.OrderPayActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.TradeDialogFragment;
import com.nahuo.quicksale.TradingDetailsActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.CustomerServiceActivity;
import com.nahuo.quicksale.activity.PackageListActivity;
import com.nahuo.quicksale.activity.ShopCartNewActivity;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.RequestMethod.OrderDetailMethod;
import com.nahuo.quicksale.api.RequestMethod.OrderMethod;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.OrderAction;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.OrderItemRecordDetailModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ReFundModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.orderdetail.model.SellerOrderModel;
import com.nahuo.quicksale.orderdetail.model.SendGoodsModel;
import com.nahuo.quicksale.orderdetail.model.TransferModel;
import com.nahuo.quicksale.util.GlideUtls;

import java.util.List;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.AddItem_From_Order;
import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.CANCEL_NEW_ORDER;
import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.CANCEL_NEW_SUB_ORDER;
import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.Comfirm_Receipt;

/**
 * Created by jame on 2018/3/6.
 */

public class OrderDetailAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> implements HttpRequestListener {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public AppCompatActivity mContext;
    private LoadingDialog mLoadingDialog;
    private HttpRequestHelper httpRequestHelper = new HttpRequestHelper();
    private OrderDetailBean orderDetailBean;
    int orderID;
    public Listener mListener;

    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setOrderDetailBean(OrderDetailBean orderDetailBean) {
        this.orderDetailBean = orderDetailBean;
    }

    public OrderDetailAdapter(List<MultiItemEntity> data, AppCompatActivity context) {
        super(data);
        this.mContext = context;
        mLoadingDialog = new LoadingDialog(mContext);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0_order_detail);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1_order_detail);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                final OrderMultiParent parent = (OrderMultiParent) item;
                if (parent != null) {
                    helper.setText(R.id.tv_tittle, parent.getName());
                    if (parent.isShowTop) {
                        helper.setGone(R.id.line_top, true);
                    } else {
                        helper.setGone(R.id.line_top, false);
                    }
                }
                if (parent.isExpanded()) {
                    helper.setImageResource(R.id.icon_expand, R.drawable.oder_shang);
                } else {
                    helper.setImageResource(R.id.icon_expand, R.drawable.order_xia);
                }
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (parent.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;
            case TYPE_LEVEL_1:
                final OrderMultiSub sub = (OrderMultiSub) item;
                if (sub != null) {
                    if (sub.isShowTop) {
                        helper.setGone(R.id.line_top, true);
                    } else {
                        helper.setGone(R.id.line_top, false);
                    }
                    if (sub.getBuyerShopID() > 0) {
                        helper.setGone(R.id.layout_buyer_shop, true);
                    } else {
                        helper.setGone(R.id.layout_buyer_shop, false);
                    }
                    String shopLogo = Const.getShopLogo(sub.getBuyerShopID());
                    ImageView iv_buyer_shop = helper.getView(R.id.iv_buyer_shop);
                    GlideUtls.glideCirclePic(mContext, shopLogo, iv_buyer_shop);
                    helper.setText(R.id.tv_buyer_shop_name, sub.getBuyerShopName());
                    helper.setText(R.id.tv_order_code, "商品单号：" + sub.getCode());
                    helper.getView(R.id.tv_copy_code).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.addNewToClipboard(mContext, sub.getCode());
                        }
                    });
                    ImageView imageView = helper.getView(R.id.iv_icon);
                    String path = ImageUrlExtends.getImageUrl(sub.getCover(), Const.LIST_COVER_SIZE);
                    GlideUtls.glidePic(mContext, path, imageView);
                    helper.setText(R.id.tv_order_title, sub.getTitle());
                    helper.setText(R.id.tv_price, "¥" + sub.getPrice());
                    helper.setText(R.id.tv_count, "×" + sub.getTotalQty() + "");
                    if (TextUtils.isEmpty(sub.getSummary())) {
                        helper.setVisible(R.id.tv_summary, false);
                    } else {
                        helper.setVisible(R.id.tv_summary, true);
                    }
                    if (TextUtils.isEmpty(sub.getCoinSummary())) {
                        helper.setGone(R.id.tv_coin_summary, false);
                    } else {
                        helper.setGone(R.id.tv_coin_summary, true);
                    }
                    helper.setText(R.id.tv_coin_summary, sub.getCoinSummary());
                    helper.setText(R.id.tv_summary, sub.getSummary());
                    if (!ListUtils.isEmpty(sub.getProducts())) {
                        helper.setVisible(R.id.layout_color_size, true);
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        for (int i = 0; i < sub.getProducts().size(); i++) {
                            ProductsBean productsBean = sub.getProducts().get(i);
                            if (i == 0) {
                                sb.append("<font color=\"#424242\">" + productsBean.getColor() + "/" + productsBean.getSize() + "/"
                                        + productsBean.getQty() + "  </font>" + "<font color=\"#9c9c9c\">" + productsBean.getSummary() + "</font>");
                            } else {
                                sb.append("<br>" + "<font color=\"#424242\">" + productsBean.getColor() + "/" + productsBean.getSize() + "/"
                                        + productsBean.getQty() + "  </font>" + "<font color=\"#9c9c9c\">" + productsBean.getSummary() + "</font>");
                            }
                        }
                        helper.setText(R.id.tv_color_size, Html.fromHtml(sb.toString()));

                    } else {
                        helper.setGone(R.id.layout_color_size, false);
                    }
                    LinearLayout layout_address_buttons = helper.getView(R.id.layout_address_buttons);
                    if (ListUtils.isEmpty(sub.getButtons())) {
                        helper.setGone(R.id.layout_buttons, false);
                    } else {
                        helper.setGone(R.id.layout_buttons, true);
                        addOrderDetailButton(layout_address_buttons, sub.getButtons(), sub);
                    }
                    helper.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(mContext, ItemDetailsActivity.class);
                                intent.putExtra(ItemDetailsActivity.EXTRA_ID, sub.getItemID());
                                // intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                                mContext.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }

    }

    public void addOrderDetailButton(LinearLayout parent, List<OrderButton> buttons
            , OrderMultiSub bean) {
        ButtonOnClickListener l = new ButtonOnClickListener();
        parent.removeAllViews();
        l.setBean(bean);
        if (buttons != null) {
            int margin = ScreenUtils.dip2px(mContext, 10);
            int top_margin = ScreenUtils.dip2px(mContext, 6);
            int top_pad = ScreenUtils.dip2px(mContext, 4);
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

    @Override
    public void onRequestStart(String method) {
        if (method.equals(AddItem_From_Order)) {
            mLoadingDialog.start("正在加入中...");
        } else if (OrderMethod.CANCEL_NEW_SUB_ORDER.equals(method)) {// 取消订单
            mLoadingDialog.start("取消订单中...");
        } else if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            mLoadingDialog.start("获取场次信息...");
        } else if (OrderMethod.GET_TRANSFER.equals(method)) {
            mLoadingDialog.start("获取转账详情中...");
        } else if (OrderDetailMethod.GET_SLLER_ORDER.equals(method)) {// 获取售货单详情
            mLoadingDialog.start("获取订单详情中...");
        } else if (OrderDetailMethod.GET_SEND_GOODS_ORDER.equals(method)) {// 获取 发货单详情
            mLoadingDialog.start("获取订单详情中...");
        } else if (RequestMethod.ShopMethod.GET_ORDER_RECORD_DETAIL.equals(method)) {//获取备注
            mLoadingDialog.start("获取备注中...");
        } else if (RequestMethod.ShopMethod.CLEAR_ORDER_RECORD_DETAIL.equals(method)) {//清除备注

        } else if (RequestMethod.OrderDetailMethod.SaveReplenishmentRecord.equals(method)) {//补货
            mLoadingDialog.start("补货中...");
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (mLoadingDialog != null)
            mLoadingDialog.stop();
        if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            RecommendModel mRecommendModel = (RecommendModel) object;
            RecommendModel.InfoEntity ie = mRecommendModel.getInfo();
            PinHuoModel item = new PinHuoModel();
            item.setID(ie.getID());
            item.IsStart = ie.isIsStart();
            item.setAppCover(ie.getAppCover());
            item.setPicAd(false);
            item.setDescription(ie.getDescription());
            item.setGroupDealCount(ie.getChengTuanCount());
            item.setName(ie.getName());
            item.setPCCover(ie.getPCCover());
            long l = ie.getToTime();
            item.setStartMillis(ie.getStartTime());
            long ll = ie.getEndMillis();
            item.setEndMillis(ie.getToTime());
            item.setLimitPoint(ie.getLimitPoint());
            item.setLimitShopAuth(ie.isLimitShopAuth());
            item.setVisitResult(ie.getVisitResult());
            item.setActivityType(ie.getActivityType());
            item.setHasNewItems(mRecommendModel.NewItems.size() > 0 ? true : false);
            ViewUtil.gotoChangci(mContext, item);
        } else if (OrderDetailMethod.GET_SLLER_ORDER.equals(method)) {// 获取售货单详情
            SellerOrderModel order = (SellerOrderModel) object;
            new ChangePriceAgentDialog(mContext, order).show();
        } else if (OrderMethod.GET_TRANSFER.equals(method)) {
            TransferModel order = (TransferModel) object;

            /*
            订单类型：拼货订单退款
            订单编号：109324-99404934F
            退款金额：¥1133.00
            退款时间：2016-03-02 00:33:22
            退款方：拼货平台
            退款说明：
            * */
            /*StringBuilder content = new StringBuilder();
            content.append("<font color='black'>订单类型： </font><font color='gray'>").append(order.TypeName).append("</font><br><br>");
//                content.append("<font color='black'>订单编号： </font>").append(order.ID).append("<br><br>");
            content.append("<font color='black'>退款金额： </font><font color='gray'>").append(order.RefundAmount).append("</font><br><br>");
            content.append("<font color='black'>退款时间： </font><font color='gray'>").append(order.CreateDate).append("</font><br><br>");
            content.append("<font color='black'>退款方： </font><font color='gray'>").append(order.RefundUserName).append("</font><br><br>");
            content.append("<font color='black'>退款说明： </font><br><font color='red' size='3'>不含运费，运费单独结算退款</font><br><font color='gray'>").append(order.Desc).append("</font><br>");

            final TextDlgFragment f = TextDlgFragment.newInstance(content.toString());
            f.setNegativeListener("关闭", null);
            f.show(mContext.getSupportFragmentManager(), "TextDlgFragment");*/

            //更换为新界面
            ReFundModel item = new ReFundModel();
            item.setType(order.TypeName);
            item.setMoney(order.RefundAmount);
            item.setTime(order.CreateDate);
            item.setPerson(order.RefundUserName);
            item.setState(order.Statu);
            item.setContent(order.Desc);
            final TradeDialogFragment f = TradeDialogFragment.newInstance(item);
            f.show(mContext.getSupportFragmentManager(), "TradeDialogFragment");

        } else if (OrderDetailMethod.GET_SEND_GOODS_ORDER.equals(method)) {// 获取发货单详情
            SendGoodsModel order = (SendGoodsModel) object;
            new ChangePriceDialog1(mContext, order.ShipID, order.Amount, order.PostFee, order.IsFreePost).show();
        } else if (OrderMethod.CANCEL_ORDER.equals(method)) {// 取消订单成功
            ViewHub.showShortToast(mContext, "取消订单成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
        } else if (RequestMethod.ShopMethod.GET_ORDER_RECORD_DETAIL.equals(method)) {//获取备注
            showRecordView((List<OrderItemRecordDetailModel>) object);
        } else if (RequestMethod.ShopMethod.CLEAR_ORDER_RECORD_DETAIL.equals(method)) {//获取备注

        } else if (RequestMethod.OrderDetailMethod.SaveReplenishmentRecord.equals(method)) {//补货
            ViewHub.showShortToast(mContext, "已成功提交");
        } else if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单成功
            ViewHub.showShortToast(mContext, "取消订单成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CHANGE_NUMBER));
        }
        if (CANCEL_NEW_SUB_ORDER.equals(method)) {
            ViewHub.showShortToast(mContext, "取消订单成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CHANGE_NUMBER));
        } else if (Comfirm_Receipt.equals(method)) {
            ViewHub.showShortToast(mContext, "签收成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CHANGE_NUMBER));
        } else if (method.equals(AddItem_From_Order)) {
            //ViewHub.showShortToast(mContext, "添加成功");
            //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
//            ViewHub.showLightPopDialog((Activity) mContext, "提示", "此笔订单的商品已经全部还原到拿货车了。您可以在拿货车对商品编辑后重新下单！", "关闭", "进入拿物车", new LightPopDialog.PopDialogListener() {
//                @Override
//                public void onPopDialogButtonClick(int which) {
//                    mContext.startActivity(new Intent(mContext, ShopCartNewActivity.class));
//                }
//            });
            CDialog dialog = new CDialog(mContext);
            dialog.setHasTittle(true).setTitle("已经添加成功啦！").setMessage("此笔订单的商品已经全部添加到拿货车了。您可以在拿货车对商品进行重新编辑并下单！").setPositive("进入拿物车", new CDialog.PopDialogListener() {
                @Override
                public void onPopDialogButtonClick(int which) {
                    mContext.startActivity(new Intent(mContext, ShopCartNewActivity.class));
                }
            }).setNegative("关闭", null).show();
        }
    }

    //展示备注层
    private void showRecordView(List<OrderItemRecordDetailModel> recordModels) {
        DialogOrderRecordDetailFragment dialog = DialogOrderRecordDetailFragment.newInstance(recordModels);

        dialog.show(mContext.getSupportFragmentManager(), "DialogOrderRecordDetailFragment");

    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        if (mLoadingDialog != null)
            mLoadingDialog.stop();
        if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单
            ViewHub.showShortToast(mContext,   msg);
        } else if (method.equals(AddItem_From_Order)) {
            //"加入失败"
            ViewHub.showShortToast(mContext,  msg);
        } else if (method.equals(Comfirm_Receipt)) {
            //"签收失败"
            ViewHub.showShortToast(mContext,  msg);
        } else if (CANCEL_NEW_SUB_ORDER.equals(method)) {//取消订单失败
            ViewHub.showShortToast(mContext, "" + msg);
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (mLoadingDialog != null)
            mLoadingDialog.stop();
        if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单
            ViewHub.showShortToast(mContext, "" + msg);
        } else if (method.equals(AddItem_From_Order)) {
            //加入失败
            ViewHub.showShortToast(mContext, "" + msg);
        } else if (method.equals(Comfirm_Receipt)) {
            //签收失败
            ViewHub.showShortToast(mContext, "" + msg);
        } else if (CANCEL_NEW_SUB_ORDER.equals(method)) {
            //取消订单失败
            ViewHub.showShortToast(mContext, "" + msg);
        }
    }

    private class ButtonOnClickListener implements View.OnClickListener {
        OrderMultiSub bean;

        public void setBean(OrderMultiSub bean) {
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
                        QuickSaleApi.getRecommendShopItems(mContext,
                                httpRequestHelper,
                                OrderDetailAdapter.this,
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
                            Intent vendorIntent = new Intent(mContext, PackageListActivity.class);
                            vendorIntent.putExtra(PackageListActivity.EXTRA_ORDERID, bean.getOrderID());
                            mContext.startActivity(vendorIntent);
                        } else {

                            Intent intent = new Intent(mContext, CustomerServiceActivity.class);
                            intent.putExtra(CustomerServiceActivity.EXTRA_TYPE, CustomerServiceActivity.TYPE_AFTER_SALES_APPLY_DETAIL);
                            intent.putExtra(CustomerServiceActivity.EXTRA_APPLYID, defectiveID);
                            mContext.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (OrderAction.SUPPLIERS_CHANGE_PRICE.equals(action)) {// 供货商修改价格
                // OrderAPI.getSendGoodOrderDetail(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getShipID());
            } else if (OrderAction.SELLER_CHANGE_PRICE.equals(action)) {// 卖家改价
                // OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                OrderAPI.getSellerOrderDetail(mContext, httpRequestHelper, OrderDetailAdapter.this, bean.getOrderID());
            } else if (OrderAction.SELLER_CANCEL.equals(action)) {// 卖家取消
                cancelOrder(bean, btn);
            } else if (OrderAction.BUYER_PAY.equals(action)) {// 买家支付

                Intent it = new Intent(mContext, OrderPayActivity.class);
                it.putExtra(OrderPayActivity.INTENT_PAY_ORDER, orderDetailBean.getCode() + "");
                it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, orderDetailBean.getOrderIDS() + "");
                it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, Double.parseDouble(orderDetailBean.getPayableAmount()));
                mContext.startActivity(it);
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
//                new DialogSureGetGoods(mContext, order.getOrderId()).show();
                ViewHub.showLightPopDialog((Activity) mContext, "提示", "确认已收齐发货的商品了，确认后状态将变成“已完结”", "关闭", "确认签收", new LightPopDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        if (bean != null) {
                            OrderAPI.comfirmReceipt(mContext, httpRequestHelper, OrderDetailAdapter.this, bean.getOrderID());
                        } else {
                            ViewHub.showShortToast(mContext, "没有订单");
                        }
                    }
                });
            } else if (OrderAction.BUYER_RETURN.equals(action)) {// 买家申请退款
                // OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                //  RefundApplyDialog.getInstance(mContext, action, 0, bean.getOrderID()).show();
                if (mContext != null)
                    ((BaseAppCompatActivity) mContext).getOrderItemForRefund(mContext, bean.getOrderID());
            } else if (OrderAction.BUYER_APPLY_SETTLE_REFUND.equals(action)) {
                if (mContext != null)
                    ((BaseAppCompatActivity) mContext).getRefundAndBuyerApplySettleRefund(mContext, bean.getOrderID());
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
                showTransfer(v, bean);
            } else if (OrderAction.BUHUO.equals(action)) {// 我要补货
                try {
                    Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
                    intent.putExtra(ItemDetailsActivity.EXTRA_ID, bean.getItemID());
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (OrderAction.MONEY_BACK.equals(action)) {// 已退
//                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                if (bean != null) {
                    Intent intent = new Intent(v.getContext(), TradingDetailsActivity.class);
                    intent.putExtra(TradingDetailsActivity.ORDERID, bean.getOrderID());
                    v.getContext().startActivity(intent);
                }
            } else if (OrderAction.ONE_KEY_JOINS_THE_TRUCK.equals(action)) {
                //一键加入拿货车
                if (bean != null) {
                    OrderAPI.addItemFromOrder(mContext, httpRequestHelper, OrderDetailAdapter.this, bean.getOrderID());
                } else {
                    ViewHub.showShortToast(mContext, "没有订单");
                }
            } else if (OrderAction.PICS_ONE_DOWNLOAD.equals(action)) {
                //一键下图
                if (mListener != null)
                    mListener.downloadPicandVideo(bean);
            }else if (OrderAction.NOTISSUEINVOICES.equals(action)) {
                if (mContext != null)
                    ((BaseAppCompatActivity) mContext).saveOrderShipConfig(mContext, bean.getOrderID());
            }

        }

    }

    /**
     * 展示退款信息
     * "已结算订单"
     *
     * @author James Chen
     * @create time in 2018/3/5 17:53
     */
    private void showTransfer(final View v, final OrderMultiSub bean) {
        ///  OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
        if (bean != null) {
            OrderAPI.getTransfer(mContext, httpRequestHelper, OrderDetailAdapter.this, bean.getTransferID());
        } else {
            ViewHub.showShortToast(mContext, "没有订单");
        }
    }

    /**
     * @description 取消订单
     * @created 2015-4-28 下午3:01:56
     * @author ZZB
     */
    private void cancelOrder(final OrderMultiSub bean, final OrderButton btn) {
//        ViewHub.showLightPopDialog((Activity) mContext, "提示", "确定取消不购买此款商品了吗？", "我再想想", "确定", new LightPopDialog.PopDialogListener() {
//            @Override
//            public void onPopDialogButtonClick(int which) {
//                if (bean != null) {
//                    OrderAPI.cancel_new_sub_order(mContext, httpRequestHelper, OrderDetailAdapter.this, bean.getOrderID());
//                } else {
//                    ViewHub.showShortToast(mContext, "没有订单");
//                }
//
//            }
//        });
        CDialog2 dialog = new CDialog2(mContext);
        dialog.setHasTittle(false).setTitle("").setMessage("确定不购买此款商品了吗？").setPositive("确定取消", new CDialog2.PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                if (bean != null) {
                    OrderAPI.cancel_new_sub_order(mContext, httpRequestHelper, OrderDetailAdapter.this, bean.getOrderID());
                } else {
                    ViewHub.showShortToast(mContext, "没有订单");
                }
            }
        }).setNegative("我再想想", null).show();
//        ViewHub.showLightPopDialog((Activity) mContext, "提示", "是否取消订单", "取消", "确定", new LightPopDialog.PopDialogListener() {
//            @Override
//            public void onPopDialogButtonClick(int which) {
//                if (bean != null) {
//                    OrderAPI.cancelNewOrder(mContext, httpRequestHelper, OrderDetailAdapter.this, bean.getOrderID());
//                } else {
//                    ViewHub.showShortToast(mContext, "没有订单");
//                }
//
//            }
//        });
    }

    private void highlightButton(TextView btn, boolean highlight, boolean isText) {

        if (isText) {
            btn.setBackgroundColor(getMyColor(R.color.btn_bg_gray));
            btn.setTextColor(getMyColor(R.color.lightblack));
        } else {
            btn.setBackgroundResource(highlight ? R.drawable.order_button_red_bg : R.drawable.order_button_white_gray_bg);
            btn.setTextColor(highlight ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.txt_gray));
        }
    }

    int getMyColor(int colorId) {
        return mContext.getResources().getColor(colorId);
    }

    public interface Listener {
        void downloadPicandVideo(OrderMultiSub bean);
    }
}
