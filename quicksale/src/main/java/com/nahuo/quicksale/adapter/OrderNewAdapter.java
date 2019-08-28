package com.nahuo.quicksale.adapter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.Dialog.CDialog;
import com.nahuo.Dialog.CDialog2;
import com.nahuo.Dialog.CDialog3;
import com.nahuo.bean.OrderBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.ChangePriceAgentDialog;
import com.nahuo.quicksale.ChangePriceDialog1;
import com.nahuo.quicksale.DialogOrderRecordDetailFragment;
import com.nahuo.quicksale.OrderPayActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.TradeDialogFragment;
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
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.OrderAction;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.OrderItemRecordDetailModel;
import com.nahuo.quicksale.oldermodel.OrderModel;
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
import static com.nahuo.quicksale.api.RequestMethod.OrderMethod.Comfirm_Receipt;

/**
 * Created by jame on 2018/3/5.
 */

public class OrderNewAdapter extends BaseQuickAdapter<OrderBean.OrderListBean, BaseViewHolder> implements HttpRequestListener {

    private ButtonOnClickListener mBtnOnClickListener;
    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();
    private LoadingDialog mLoadingDialog;
    private AppCompatActivity mContext;

    public OrderNewAdapter(AppCompatActivity mContext) {
        super(R.layout.order_item);
        mLoadingDialog = new LoadingDialog(mContext);
        this.mContext = mContext;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean.OrderListBean item) {
        OrderBean.OrderListBean bean = item;
        if (bean != null) {
            if (getHeaderLayoutCount() > 0) {
                if (helper.getAdapterPosition() > getHeaderLayoutCount()) {
                    helper.setVisible(R.id.line, true);
                } else {
                    helper.setGone(R.id.line, false);
                }
            } else {
                if (helper.getAdapterPosition() > 0) {
                    helper.setVisible(R.id.line, true);
                } else {
                    helper.setGone(R.id.line, false);
                }
            }
            if (TextUtils.isEmpty(bean.getCode())) {
                helper.setText(R.id.tv_order_code, "");
            } else {
                helper.setText(R.id.tv_order_code, "拿货单   " + bean.getCode());
            }
            LinearLayout layout_order_buttons = helper.getView(R.id.layout_order_buttons);

            helper.setText(R.id.tv_order_status, bean.getStatu());
            if (TextUtils.isEmpty(bean.getSummary())) {
                helper.setGone(R.id.tv_order_summary, false);
            } else {
                helper.setGone(R.id.tv_order_summary, true);
                helper.setText(R.id.tv_order_summary, bean.getSummary());
            }
            if (TextUtils.isEmpty(bean.getRefundSummary())) {
                helper.setGone(R.id.tv_order_refund_summary, false);
            } else {
                helper.setGone(R.id.tv_order_refund_summary, true);
                helper.setText(R.id.tv_order_refund_summary, bean.getRefundSummary());
            }
            helper.setText(R.id.tv_order_title, bean.getTitle());
            ImageView iv_pic1 = helper.getView(R.id.iv_pic1);
            ImageView iv_pic2 = helper.getView(R.id.iv_pic2);
            ImageView iv_pic3 = helper.getView(R.id.iv_pic3);
            ImageView iv_simple_pic = helper.getView(R.id.iv_simple_pic);
            if (ListUtils.isEmpty(bean.getImages())) {
                helper.setGone(R.id.layout_multi_pic, false);
                helper.setVisible(R.id.layout_simple_pic, true);
            } else {
                int size = bean.getImages().size();
                if (size == 1) {
                    helper.setGone(R.id.layout_multi_pic, false);
                    helper.setVisible(R.id.layout_simple_pic, true);
                } else {
                    helper.setVisible(R.id.layout_multi_pic, true);
                    helper.setGone(R.id.layout_simple_pic, false);
                }
                String imageUrl1 = "", imageUrl2 = "", imageUrl3 = "";
                String cover1 = "", cover2 = "", cover3 = "";
                for (int i = 0; i < bean.getImages().size(); i++) {
                    if (i == 0) {
                        cover1 = bean.getImages().get(i);
                    } else if (i == 1) {
                        cover2 = bean.getImages().get(i);
                    } else if (i == 2) {
                        cover3 = bean.getImages().get(i);
                    } else {
                        break;
                    }
                }
                if (TextUtils.isEmpty(cover1)) {
                    iv_pic1.setVisibility(View.INVISIBLE);
                } else {
                    iv_pic1.setVisibility(View.VISIBLE);
                    imageUrl1 = ImageUrlExtends.getImageUrl(cover1, Const.LIST_COVER_SIZE);
                }
                if (TextUtils.isEmpty(cover2)) {
                    iv_pic2.setVisibility(View.INVISIBLE);
                } else {
                    iv_pic2.setVisibility(View.VISIBLE);
                    imageUrl2 = ImageUrlExtends.getImageUrl(cover2, Const.LIST_COVER_SIZE);
                }
                if (TextUtils.isEmpty(cover3)) {
                    iv_pic3.setVisibility(View.INVISIBLE);
                } else {
                    iv_pic3.setVisibility(View.VISIBLE);
                    imageUrl3 = ImageUrlExtends.getImageUrl(cover3, Const.LIST_COVER_SIZE);
                }
                GlideUtls.glidePic(mContext, imageUrl1, iv_pic1);
                GlideUtls.glidePic(mContext, imageUrl2, iv_pic2);
                GlideUtls.glidePic(mContext, imageUrl3, iv_pic3);
                GlideUtls.glidePic(mContext, imageUrl1, iv_simple_pic);
            }
            if (ListUtils.isEmpty(bean.getButtons())) {
                layout_order_buttons.removeAllViews();
                helper.setGone(R.id.layout_buttons, false);
            } else {
                helper.setGone(R.id.layout_buttons, true);
                layout_order_buttons.removeAllViews();
                addOrderDetailButton(layout_order_buttons, bean.getButtons(), mBtnOnClickListener, bean);
            }
        }

    }

    @Override
    public void onRequestStart(String method) {
        if (method.equals(AddItem_From_Order)) {
            mLoadingDialog.start("正在加入中...");
        } else if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单
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
        } else if (OrderMethod.CANCEL_NEW_ORDER.equals(method)) {// 取消订单成功
            ViewHub.showShortToast(mContext, "取消订单成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
        } else if (Comfirm_Receipt.equals(method)) {
            ViewHub.showShortToast(mContext, "签收成功");
            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.CANCEL_ORDER));
        } else if (method.equals(AddItem_From_Order)) {
          //  ViewHub.showShortToast(mContext, "添加成功");
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
        } else if (RequestMethod.ShopMethod.GET_ORDER_RECORD_DETAIL.equals(method)) {//获取备注
            showRecordView((List<OrderItemRecordDetailModel>) object);
        } else if (RequestMethod.ShopMethod.CLEAR_ORDER_RECORD_DETAIL.equals(method)) {//获取备注

        } else if (RequestMethod.OrderDetailMethod.SaveReplenishmentRecord.equals(method)) {//补货
            ViewHub.showShortToast(mContext, "已成功提交");
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
            ViewHub.showShortToast(mContext, "" + msg);
        } else if (method.equals(AddItem_From_Order)) {
            //加入失败
            ViewHub.showShortToast(mContext, "" + msg);
        } else if (method.equals(Comfirm_Receipt)) {
            //签收失败
            ViewHub.showShortToast(mContext, "" + msg);
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (mLoadingDialog != null)
            mLoadingDialog.stop();
        if (CANCEL_NEW_ORDER.equals(method)) {// 取消订单
            ViewHub.showShortToast(mContext, "" + msg);
        } else if (method.equals(AddItem_From_Order)) {//加入失败
            ViewHub.showShortToast(mContext, "" + msg);
        } else if (method.equals(Comfirm_Receipt)) {//签收失败
            ViewHub.showShortToast(mContext, "" + msg);
        }
    }

    OrderBean.OrderListBean orderListBean;

    private class ButtonOnClickListener implements View.OnClickListener {
        OrderBean.OrderListBean bean;

        public void setBean(OrderBean.OrderListBean bean) {
            this.bean = bean;
        }

        @Override
        public void onClick(final View v) {
            orderListBean = this.bean;
            final OrderButton btn = (OrderButton) v.getTag();
            String action = btn.getAction();
            if (OrderAction.GOTO_CHANGCI.equals(action)) {// 供货商修改价格
                try {
                    if (btn.getData() instanceof  Double) {
                        int defectiveID = new Double(Double.parseDouble(btn.getData().toString())).intValue();
                        QuickSaleApi.getRecommendShopItems(mContext,
                                mHttpRequestHelper,
                                OrderNewAdapter.this,
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
                            vendorIntent.putExtra(PackageListActivity.EXTRA_ORDERID, bean.getID());
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
                OrderAPI.getSellerOrderDetail(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getID());
            } else if (OrderAction.SELLER_CANCEL.equals(action)) {// 卖家取消
                cancelOrder(bean, btn);
            } else if (OrderAction.BUYER_PAY.equals(action)) {// 买家支付
//                if (mCurOrderStatu == Const.OrderStatus.WAIT_PAY) {
//                    OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                    Intent it = new Intent(mContext, OrderPayActivity.class);
//                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, order.getOrderId() + "");
//                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, order.getPrice());
//                    mContext.startActivity(it);
//                } else {
                Intent it = new Intent(mContext, OrderPayActivity.class);
                it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, bean.getOrderIDS() + "");
                it.putExtra(OrderPayActivity.INTENT_PAY_ORDER, bean.getCode());
                it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, Double.parseDouble(bean.getPayableAmount()));
                mContext.startActivity(it);
//                if (isPrePay()) {
//                    ViewHub.showTextDialog(mContext, "提示",
//                            "是否批量支付？",
//                            "批量支付", "只付此单",
//                            new ViewHub.EditDialogListener() {
//                                @Override
//                                public void onOkClick(DialogInterface dialog, EditText editText) {
//
//                                }
//
//                                @Override
//                                public void onOkClick(EditText editText) {
//                                    //批量
//                                    Intent it = new Intent(mContext, OrderMutablePayActivity.class);
//                                    it.putExtra(OrderMutablePayActivity.EXTRA_QSID, qsid);
//                                    it.putExtra(OrderMutablePayActivity.EXTRA_TITLE, "批量支付");
//                                    mContext.startActivity(it);
//                                }
//
//                                @Override
//                                public void onNegativecClick() {//不需要
//                                    OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                                    Intent it = new Intent(mContext, OrderPayActivity.class);
//                                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, order.getOrderId() + "");
//                                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, order.getPrice());
//                                    mContext.startActivity(it);
//                                }
//                            });
//                } else {
//                    OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                    Intent it = new Intent(mContext, OrderPayActivity.class);
//                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, order.getOrderId() + "");
//                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, order.getPrice());
//                    mContext.startActivity(it);
//                }
                //}
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
//                ViewHub.showLightPopDialog((Activity) mContext, "提示", "确认已收齐发货的商品了，确认后状态将变成“已完结”", "关闭", "确认签收", new LightPopDialog.PopDialogListener() {
//                    @Override
//                    public void onPopDialogButtonClick(int which) {
//                        if (bean != null) {
//                            OrderAPI.comfirmReceipt(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getID());
//
//                        } else {
//                            ViewHub.showShortToast(mContext, "没有订单");
//                        }
//                    }
//                });
                CDialog3 dialog = new CDialog3(mContext);
                dialog.setHasTittle(false).setTitle("").setMessage("确认已收齐发货的商品了，确认后状态将变成“已完结”").setPositive("关闭", new CDialog3.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                    }
                }).setNegative("确认签收", new CDialog3.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        if (bean != null) {
                            OrderAPI.comfirmReceipt(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getID());

                        } else {
                            ViewHub.showShortToast(mContext, "没有订单");
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
                if (bean != null) {
                    OrderAPI.addItemFromOrder(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getID());

                } else {
                    ViewHub.showShortToast(mContext, "没有订单");
                }
            }

        }

    }

    /**
     * @description 取消订单
     * @created 2015-4-28 下午3:01:56
     * @author ZZB
     */
    private void cancelOrder(final OrderBean.OrderListBean bean, final OrderButton btn) {
//        ViewHub.showLightPopDialog((Activity) mContext, "确定要取消该订单吗？", "取消订单后，您可以进入“已取消”列表，一键将商品重新添加到拿货车哦！", "我再想想", "确定取消", new LightPopDialog.PopDialogListener() {
//            @Override
//            public void onPopDialogButtonClick(int which) {
//                if (bean != null) {
//                    OrderAPI.cancelNewOrder(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getID());
//                } else {
//                    ViewHub.showShortToast(mContext, "没有订单");
//                }
//
//            }
//        });
        CDialog2 dialog = new CDialog2(mContext);
        dialog.setHasTittle(true).setTitle("确定要取消该订单吗？").setMessage("取消订单后，您可以进入“已取消”列表，一键将商品重新添加到拿货车哦！").setPositive("确定取消", new CDialog2.PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                if (bean != null) {
                    OrderAPI.cancelNewOrder(mContext, mHttpRequestHelper, OrderNewAdapter.this, bean.getID());
                } else {
                    ViewHub.showShortToast(mContext, "没有订单");
                }
            }
        }).setNegative("我再想想", null).show();
    }

    /**
     * 展示退款信息
     *
     * @author James Chen
     * @create time in 2018/3/5 17:53
     */
    private void showTransfer(final View v, final OrderButton btn) {
        OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
        if (order != null) {
            OrderAPI.getTransfer(mContext, mHttpRequestHelper, OrderNewAdapter.this, order.getTransferID());
        } else {
            ViewHub.showShortToast(mContext, "没有订单");
        }
    }

    public void addOrderDetailButton(LinearLayout parent, List<OrderButton> buttons, ButtonOnClickListener l
            , OrderBean.OrderListBean bean) {
        l = new ButtonOnClickListener();
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
//                if (!TextUtils.isEmpty(memo) && Const.OrderAction.MEMO.equals(model.getAction())) {
//                    Drawable right = parent.getResources().getDrawable(R.drawable.icon_true);
//                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
//                    child.setCompoundDrawables(null, null, right, null);
//                } else if (leveMsgCount > 0 && Const.OrderAction.LEAVE_MSG.equals(model.getAction())) {//订单留言
//                    Drawable right = parent.getResources().getDrawable(R.drawable.red_ball);
//                    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
//                    child.setCompoundDrawables(null, null, right, null);
//                }
            }
        }
    }

    private void highlightButton(TextView btn, boolean highlight, boolean isText) {

        if (isText) {
            btn.setBackgroundColor(getColor(R.color.btn_bg_gray));
            btn.setTextColor(getColor(R.color.lightblack));
        } else {
            btn.setBackgroundResource(highlight ? R.drawable.order_button_red_bg : R.drawable.order_button_white_gray_bg);
            btn.setTextColor(highlight ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.txt_gray));
        }
    }

    private int getColor(int colorId) {
        return mContext.getResources().getColor(colorId);
    }

}
