package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.LightPopDialog.PopDialogListener;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.ChangePriceAgentDialog;
import com.nahuo.quicksale.ChangePriceDialog1;
import com.nahuo.quicksale.DialogChooseExpress;
import com.nahuo.quicksale.DialogOrderRecordDetailFragment;
import com.nahuo.quicksale.DialogSureGetGoods;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.OrderMutablePayActivity;
import com.nahuo.quicksale.OrderPayActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.RefundByBuyerActivity;
import com.nahuo.quicksale.RefundBySellerActivity;
import com.nahuo.quicksale.RefundBySupperActivity;
import com.nahuo.quicksale.TradeDialogFragment;
import com.nahuo.quicksale.TradingDetailsActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.CustomerServiceActivity;
import com.nahuo.quicksale.activity.PackageListActivity;
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
import com.nahuo.quicksale.common.Const.OrderType;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.exceptions.CatchedException;
import com.nahuo.quicksale.oldermodel.OrderButton;
import com.nahuo.quicksale.oldermodel.OrderItemRecordDetailModel;
import com.nahuo.quicksale.oldermodel.OrderModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ReFundModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShipperRefund;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.orderdetail.BaseOrderDetailActivity;
import com.nahuo.quicksale.orderdetail.ShipActivity;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.PickingBillModel;
import com.nahuo.quicksale.orderdetail.model.Refund;
import com.nahuo.quicksale.orderdetail.model.SellerOrderModel;
import com.nahuo.quicksale.orderdetail.model.SendGoodsModel;
import com.nahuo.quicksale.orderdetail.model.TransferModel;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author ZZB
 * @description 订单管理
 * @created 2015-4-3 下午4:59:14
 */
public class OrderAdapter extends BaseExpandableListAdapter implements View.OnClickListener, HttpRequestListener {
    private static final String TAG = OrderAdapter.class.getSimpleName();
    private OrderAdapter vThis = this;
    private BaseAppCompatActivity mContext;
    private LayoutInflater mInflater;
    private List<OrderModel> mData;
    private ButtonOnClickListener mBtnOnClickListener = new ButtonOnClickListener();
    private ChildViewOnClickListener mChildViewOnClickListener = new ChildViewOnClickListener();
    private GroupViewOnClickListener mGroupViewOnClickListener = new GroupViewOnClickListener();
    private ShopcartAdapter.TotalPriceChangedListener mTotalPriceChangedListener;
    private LoadingDialog mLoadingDialog;
    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();
    private DecimalFormat mDecimalFormat = new DecimalFormat("#0.00");
    public int mCurOrderType = Const.OrderType.ALL;    // 订单类型
    public int mCurOrderStatu = Const.OrderStatus.DONE;
    public int qsid;
    public String title;        // 订单状态
    public List<String> prePayList = new ArrayList<>();

    public OrderAdapter(BaseAppCompatActivity context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mLoadingDialog = new LoadingDialog(mContext);
    }

    public void setData(List<OrderModel> data) {
        this.mData = data;
    }

    public List<OrderModel> getData() {
        return mData;
    }

    public void addData(List<OrderModel> data) {
        mData.addAll(data);
    }

    @Override
    public int getGroupCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = 0;
        OrderModel groupItem = mData.get(groupPosition);

        int orderType = groupItem.getOrderType();
        if (orderType == OrderType.AGENT || orderType == OrderType.SELL) {// 代理单或者拿货单
            count = ListUtils.isEmpty(groupItem.getAgentOrderItems()) ? 0 : groupItem.getAgentOrderItems().size();
        } else if (orderType == OrderType.SHIP) {// 发货单
            count = ListUtils.isEmpty(groupItem.getPickingOrders()) ? 0 : groupItem.getPickingOrders().size();
        } else {
            count = 0;// 拿货单一定没有子订单
        }
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData == null ? null : mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        OrderModel parent = mData.get(groupPosition);
        int orderType = parent.getOrderType();
        // 代理单与售货单
        if (orderType == Const.OrderType.AGENT || orderType == Const.OrderType.SELL) {
            List<OrderModel> items = parent.getAgentOrderItems();
            boolean isEmpty = ListUtils.isEmpty(items);
            return isEmpty ? null : items.get(childPosition);
        } else if (orderType == OrderType.SHIP) {// 发货单
            List<PickingBillModel> items = parent.getPickingOrders();
            return ListUtils.isEmpty(items) ? null : items.get(childPosition);
        } else {// 拿货单1没有子订单
            return null;
        }

    }

    @Override
    public long getGroupId(int groupPosition) {
        OrderModel item = mData.get(groupPosition);
        return item == null ? 0 : item.getOrderId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        OrderModel item = (OrderModel) getChild(groupPosition, childPosition);
        return item == null ? 0 : item.getOrderId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void setCheckAll(boolean checked) {
        for (OrderModel item : mData) {
            item.isSelect = checked;
        }
        notifyDataSetChanged();
        if (mTotalPriceChangedListener != null) {
            mTotalPriceChangedListener.totalPriceChanged(0);
        }
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = mInflater.inflate(R.layout.lvitem_order_parent, parent, false);
            holder.rootView = convertView;
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.tvCreateDate = (TextView) convertView.findViewById(R.id.tv_create_date);
            holder.tvSummary = (TextView) convertView.findViewById(R.id.tv_summary);
            holder.tvItemCount = (TextView) convertView.findViewById(R.id.tv_item_count);
            holder.tvItemTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
            holder.tvOrderNum = (TextView) convertView.findViewById(R.id.tv_order_num);
            holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tv_pay_status);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.btnLeft = (Button) convertView.findViewById(R.id.btn_left);
            holder.btnCenter = (Button) convertView.findViewById(R.id.btn_center);
            holder.btnRight = (Button) convertView.findViewById(R.id.btn_right);
            holder.ivThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
            holder.line1 = (View) convertView.findViewById(R.id.line1);
            holder.recordView = (View) convertView.findViewById(R.id.btn_record_icon);
            holder.tvRecordText = (TextView) convertView.findViewById(R.id.btn_record_text);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        emptyGroupHolder(holder);
        final OrderModel topOrder = mData.get(groupPosition);
        holder.checkbox.setTag(topOrder.getOrderId());
        if (mCurOrderStatu == Const.OrderStatus.WAIT_PAY) {
            holder.checkbox.setVisibility(View.VISIBLE);

            holder.checkbox.setChecked(topOrder.isSelect);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
//                    int orderid = (int)cb.getTag();
//                    for (OrderModel item : mData) {
//                        if (orderid == item.getOrderId()) {
//                            item.isSelect = cb.isChecked();
//                            break;
//                        }
//                    }
                    topOrder.isSelect = cb.isChecked();
                    notifyDataSetChanged();
                    if (mTotalPriceChangedListener != null) {
                        mTotalPriceChangedListener.totalPriceChanged(0);
                    }
                }
            });
        } else {
            holder.checkbox.setVisibility(View.GONE);
            holder.checkbox.setChecked(false);
        }
        if (topOrder.getSummary().length() > 0) {
            holder.tvSummary.setVisibility(View.VISIBLE);
        } else {
            holder.tvSummary.setVisibility(View.GONE);
        }
        holder.tvSummary.setText(topOrder.getSummary());
        holder.tvCreateDate.setText(topOrder.getCreateDate());
        holder.tvOrderStatus.setText(topOrder.getOrderStatu());
        if (topOrder.getPostage() <= 0) {
            holder.tvPrice.setText("¥" + mDecimalFormat.format(topOrder.getPrice()));
        } else {
            holder.tvPrice.setText("¥" + mDecimalFormat.format(topOrder.getPrice()) +
                    "(含运费" + mDecimalFormat.format(topOrder.getPostage()) + ")");
        }
        holder.tvOrderStatus.setText(topOrder.getOrderStatu());
        holder.tvItemCount.setText(getItemCountStr(topOrder.getItemCount()));

        holder.rootView.setOnClickListener(mGroupViewOnClickListener);
        holder.rootView.setTag(R.id.order_parent_item, topOrder);

        buyTicket(holder, topOrder);
        //点击查看备注消息
        if (topOrder.getOrderItems().size() > 0) {
            holder.recordView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderAPI.getRecordDetail(mContext, mHttpRequestHelper,
                            OrderAdapter.this, topOrder.getOrderItems().get(0).getAgentItemID(), topOrder.getQSID());
                    OrderAPI.cleanRecordDetail(mContext, mHttpRequestHelper,
                            OrderAdapter.this, topOrder.getOrderItems().get(0).getAgentItemID(), topOrder.getQSID());

                    if (topOrder.getOrderItems().size() > 0) {
                        topOrder.getOrderItems().get(0).setRecordQty(0);
                        vThis.notifyDataSetChanged();
                    }
                }
            });
        }

//        if(topOrder.isHasBuyerMsg()){// 留言备注按钮
//            holder.recordView.setVisibility(View.VISIBLE);
//        }else{
//            holder.recordView.setVisibility(View.GONE);
//        }
        return convertView;
    }

    public void setTotalPriceChangedListener(ShopcartAdapter.TotalPriceChangedListener listener) {
        this.mTotalPriceChangedListener = listener;
    }

    /**
     * @description 清空GroupHolder的值, 不然复用有些值还会存在
     * @created 2015-4-29 下午2:09:51
     * @author ZZB
     */
    private void emptyGroupHolder(GroupHolder holder) {
        hideButtons(holder);
        holder.tvCreateDate.setText("");
        holder.tvItemCount.setText("");
        holder.tvItemTitle.setText("");
        holder.tvOrderNum.setText("");
        holder.tvOrderStatus.setText("");
        holder.tvPrice.setText("");
        holder.tvUserName.setText("");
    }

    /**
     * @description 清空ChildHolder的值
     * @created 2015-4-29 下午2:12:43
     * @author ZZB
     */
    private void emptyChildHolder(ChildHolder holder) {
        holder.rootView.setTag(R.id.order_sub_item_id, 0);
        holder.rootView.setTag(R.id.order_is_picking_order, false);
        holder.tvItemCount.setText("");
        holder.tvItemTitle.setText("");
        holder.tvPrice.setText("");
        holder.tvUserName.setText("");
        holder.tvOrderName.setText("");
        holder.tvPayStatus.setText("");
    }

    /**
     * @description 发货单4
     * @created 2015-4-14 下午2:28:28
     * @author ZZB
     */
    private void shipTicket(GroupHolder holder, OrderModel topOrder) {
        try {
            holder.rootView.setTag(R.id.order_parent_item_type, BaseOrderDetailActivity.GET_SHIP_ORDER);
            holder.tvOrderNum.setText("发货单: " + topOrder.getCode());
            holder.tvUserName.setText("买家: " + topOrder.getBuyerName());
            holder.tvOrderStatus.setText(topOrder.getShipStatu());
            OrderItemModel item = null;
            if ("Agent".equals(topOrder.getType())) {// 代理的货
                item = topOrder.getAgentOrderItems().get(0).getOrderItems().get(0);
            } else {// 自己的货
                item = topOrder.getOrderItems().get(0);
            }
            loadMidImg(holder.ivThumb, item.getCover());
            holder.tvItemTitle.setText(item.getName());
            List<OrderButton> buttons = topOrder.getButtons();
            if (!ListUtils.isEmpty(buttons)) {
                // 修改运费 或者 发货
                OrderButton btnChangePrice = buttons.get(0);
                holder.btnLeft.setTag(R.id.order_parent_item, topOrder);
                populateButton(holder.btnLeft, btnChangePrice);
            } else {
                hideButtons(holder);
            }
        } catch (Exception e) {
            CrashReport.postCatchedException(new CatchedException(e));
        }


    }

    /**
     * @description 代理单 3
     * @created 2015-4-14 下午2:28:10
     * @author ZZB
     */
    private void agentTicket(GroupHolder holder, OrderModel topOrder) {
        holder.rootView.setTag(R.id.order_parent_item_type, BaseOrderDetailActivity.GET_AGENT_ORDER);
        holder.tvOrderNum.setText("代理单: " + topOrder.getCode());
        holder.tvUserName.setText("买家: " + topOrder.getBuyerName());
        holder.tvPrice.setText("¥" + mDecimalFormat.format(topOrder.getAmount()));
        OrderItemModel item = topOrder.getOrderItems().get(0);
        holder.tvItemTitle.setText(item.getName());
        loadMidImg(holder.ivThumb, item.getCover());
        List<OrderButton> buttons = topOrder.getButtons();
        if (!ListUtils.isEmpty(buttons)) {
            // 代理退款单
            OrderButton btnCancel = buttons.get(0);
            holder.btnLeft.setTag(R.id.order_parent_item, topOrder);
            populateButton(holder.btnLeft, btnCancel);
            if (buttons.size() > 1) {
                OrderButton btnChangePrice = buttons.get(1);
                holder.btnRight.setTag(R.id.order_parent_item, topOrder);
                populateButton(holder.btnRight, btnChangePrice);
            }
        }
    }

    /**
     * @description 售货单 2
     * @created 2015-4-14 上午11:52:46
     * @author ZZB
     */
    private void sellTicket(GroupHolder holder, OrderModel topOrder) {
        holder.rootView.setTag(R.id.order_parent_item_type, BaseOrderDetailActivity.GET_SELLER_ORDER);
        holder.tvOrderNum.setText("售货单: " + topOrder.getCode());
        holder.tvUserName.setText("买家: " + topOrder.getBuyerName());
        // List<OrderModel.Item> items = topOrder.getOrderItems();
        if (!ListUtils.isEmpty(topOrder.getOrderItems())) {
            OrderItemModel item = topOrder.getOrderItems().get(0);
            holder.tvItemTitle.setText(item.getName());
            loadMidImg(holder.ivThumb, item.getCover());
        } else {
            holder.tvItemTitle.setText("这张单好像出问题了哦~");
        }

        List<OrderButton> buttons = topOrder.getButtons();
        if (!ListUtils.isEmpty(buttons)) {
            // 卖家取消
            OrderButton btnCancel = buttons.get(0);
            holder.btnLeft.setTag(R.id.order_parent_item, topOrder);
            populateButton(holder.btnLeft, btnCancel);
            if (buttons.size() > 1) {
                // 卖家改价
                OrderButton btnChangePrice = buttons.get(1);
                holder.btnRight.setTag(R.id.order_parent_item, topOrder);
                populateButton(holder.btnRight, btnChangePrice);
            }

        } else {
            hideButtons(holder);
        }
    }

    /**
     * @description 拿货单 1
     * @created 2015-4-14 上午11:54:37
     * @author ZZB
     */
    private void buyTicket(GroupHolder holder, OrderModel topOrder) {
        holder.rootView.setTag(R.id.order_parent_item_type, BaseOrderDetailActivity.GET_BUY_ORDER);
        holder.tvOrderNum.setText("拿货单: " + topOrder.getCode());
        ShopInfoModel shop = topOrder.getShop();
        OrderItemModel item = topOrder.getOrderItems().get(0);
        holder.tvUserName.setText("卖家: " + shop.getName());
        holder.tvItemTitle.setText(item.getName());
        loadMidImg(holder.ivThumb, item.getCover());
        List<OrderButton> buttons = topOrder.getButtons();
        boolean isShowLine = false;
        if (!ListUtils.isEmpty(buttons)) {
            isShowLine = true;
            holder.line1.setVisibility(View.VISIBLE);
            // 支付改价 、确认收货、退款
            OrderButton btnChangePrice = buttons.get(0);
            holder.btnLeft.setTag(R.id.order_parent_item, topOrder);
            populateButton(holder.btnLeft, btnChangePrice);
            if (buttons.size() > 1) {
                holder.btnCenter.setTag(R.id.order_parent_item, topOrder);
                OrderButton btnCancel = buttons.get(1);
                populateButton(holder.btnCenter, btnCancel);
            }
            if (buttons.size() > 2) {
                // 取消、物流
                holder.btnRight.setTag(R.id.order_parent_item, topOrder);
                OrderButton btnCancel = buttons.get(2);
                populateButton(holder.btnRight, btnCancel);
            }

        } else {
            hideButtons(holder);
        }
        holder.line1.setVisibility(View.VISIBLE);
        //显示有新消息
        if (topOrder.getOrderItems().size() > 0) {
            if (topOrder.getOrderItems().get(0).getRecordCountQty() > 0) {
                holder.recordView.setVisibility(View.VISIBLE);
            } else {
                holder.recordView.setVisibility(View.GONE);
            }
            if (topOrder.getOrderItems().get(0).getRecordQty() > 0) {
                holder.tvRecordText.setVisibility(View.VISIBLE);
                holder.tvRecordText.setText(topOrder.getOrderItems().get(0).getRecordQty() + "");
            } else {
                holder.tvRecordText.setVisibility(View.GONE);
                holder.tvRecordText.setText("");
            }
        }

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        ChildHolder holder;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = mInflater.inflate(R.layout.lvitem_order_child, parent, false);
            holder.rootView = convertView;
            holder.dashLine = convertView.findViewById(R.id.dash_line);
            holder.whiteBlock = convertView.findViewById(R.id.white_block);
            holder.tvOrderName = (TextView) convertView.findViewById(R.id.tv_order_name);
            holder.tvItemCount = (TextView) convertView.findViewById(R.id.tv_item_count);
            holder.tvItemTitle = (TextView) convertView.findViewById(R.id.tv_item_title);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            holder.ivThumb = (ImageView) convertView.findViewById(R.id.iv_thumb);
            holder.tvPayStatus = (TextView) convertView.findViewById(R.id.tv_arrow_right);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        emptyChildHolder(holder);
        holder.dashLine.setVisibility(isLastChild ? View.GONE : View.VISIBLE);
        holder.whiteBlock.setVisibility(isLastChild ? View.VISIBLE : View.GONE);
        OrderModel parentItem = mData.get(groupPosition);

        switch (parentItem.getOrderType()) {// 拿货单1没有子订单
            case OrderType.SELL:// 售货单2
                OrderModel childItem = parentItem.getAgentOrderItems().get(childPosition);
                sellSubTicket(holder, childItem, childPosition);
                break;
            case OrderType.AGENT:// 代理单3
                OrderModel childItem1 = parentItem.getAgentOrderItems().get(childPosition);
                agentSubTicket(holder, childItem1, childPosition);
                break;
            case OrderType.SHIP:// 发货单4
                PickingBillModel pickingOrder = parentItem.getPickingOrders().get(childPosition);
                shipSubTicket(holder, pickingOrder, childPosition);
                // shipSubTicket(holder, childItem, childPosition);
                break;
        }
        // holder.tvItemCount.setText(getItemCountStr(childItem.getItemCount()));
        // holder.rootView.setOnClickListener(mChildViewOnClickListener);
        // holder.rootView.setTag(R.id.order_sub_item, childItem);
        return convertView;
    }

    /**
     * @description 发货单子订单4，PickingOrders(拣货单):复制的商品
     * @created 2015-4-14 下午3:40:18
     * @author ZZB
     */
    private void shipSubTicket(ChildHolder holder, PickingBillModel pickingOrder, int pos) {
        OrderItemModel orderItem = pickingOrder.Items.get(0);
        ShopInfoModel shop = pickingOrder.Shop;
        holder.tvUserName.setText(shop.getName());
        holder.tvPayStatus.setText("");
        holder.tvOrderName.setText("拣货单" + (pos + 1) + ": ");
        holder.tvItemCount.setText(getItemCountStr(pickingOrder.ItemCount));
        holder.tvPrice.setText("¥" + mDecimalFormat.format(pickingOrder.Amount));
        holder.rootView.setOnClickListener(mChildViewOnClickListener);
        holder.rootView.setTag(R.id.order_sub_item_id, pickingOrder.PickingID);
        holder.rootView.setTag(R.id.order_is_picking_order, true);
        loadSmallImg(holder.ivThumb, orderItem.getCover());
        holder.tvItemTitle.setText(orderItem.getName());
    }

    /**
     * @description 代理子订单3
     * @created 2015-4-14 下午3:40:24
     * @author ZZB
     */
    private void agentSubTicket(ChildHolder holder, OrderModel topOrder, int pos) {
        holder.tvItemCount.setText(getItemCountStr(topOrder.getItemCount()));
        holder.rootView.setOnClickListener(mChildViewOnClickListener);
        holder.rootView.setTag(R.id.order_sub_item_id, topOrder.getId());
        OrderItemModel item = topOrder.getOrderItems().get(0);// "Items:[{}]"
        loadSmallImg(holder.ivThumb, item.getCover());
        holder.tvOrderName.setText("上家订单" + (pos + 1) + ": ");
        OrderModel.Seller seller = topOrder.getSeller();
        ShopInfoModel shop = seller.shop;
        holder.tvUserName.setText(shop.getName());
        holder.tvPrice.setText("¥" + mDecimalFormat.format(topOrder.getAmount()));
        holder.tvPayStatus.setText(topOrder.getOrderStatu());
        holder.tvItemTitle.setText(item.getName());
    }

    /**
     * @description 售货单2
     * @created 2015-4-14 下午3:36:26
     * @author ZZB
     */
    private void sellSubTicket(ChildHolder holder, OrderModel topOrder, int pos) {
        holder.tvItemCount.setText(getItemCountStr(topOrder.getItemCount()));
        holder.rootView.setOnClickListener(mChildViewOnClickListener);
        holder.rootView.setTag(R.id.order_sub_item_id, topOrder.getId());
        OrderItemModel item = topOrder.getOrderItems().get(0);
        loadSmallImg(holder.ivThumb, item.getCover());
        holder.tvOrderName.setText("上家订单" + (pos + 1) + ": ");
        holder.tvUserName.setText(topOrder.getShop().getName());
        holder.tvPrice.setText("¥" + mDecimalFormat.format(topOrder.getAmount()));
        holder.tvPayStatus.setText(topOrder.getOrderStatu());
        holder.tvItemTitle.setText(item.getName());
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private static class GroupHolder {
        private TextView tvRecordText, tvItemTitle, tvOrderNum, tvOrderStatus, tvUserName, tvCreateDate, tvItemCount, tvPrice, tvSummary;
        private ImageView ivThumb;
        private Button btnLeft, btnRight, btnCenter;
        private View rootView;
        private CheckBox checkbox;
        private View line1, recordView;
    }

    private static class ChildHolder {
        private TextView tvOrderName, tvUserName, tvItemTitle, tvItemCount, tvPrice, tvPayStatus;
        private ImageView ivThumb;
        private View rootView, dashLine, whiteBlock;
    }

    private void loadMidImg(ImageView iv, String url) {
        url = ImageUrlExtends.getImageUrl(url, Const.LIST_ITEM_SIZE);
        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(iv);
    }

    private void loadSmallImg(ImageView iv, String url) {
        url = ImageUrlExtends.getImageUrl(url, Const.LIST_ITEM_SIZE);
        Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(iv);
    }

    private String getItemCountStr(int count) {
        return String.format(mContext.getString(R.string.order_total_item_count), count);
    }

    private void populateButton(Button btn, OrderButton orderBtn) {
        btn.setVisibility(View.VISIBLE);
        btn.setTag(orderBtn);
        btn.setOnClickListener(mBtnOnClickListener);
        btn.setText(orderBtn.getTitle());
        highlightButton(btn, orderBtn.isPoint(), "text".equals(orderBtn.getType()));
    }

    /**
     * @description 高亮按钮
     * @created 2015-4-28 下午3:05:14
     * @author ZZB
     */
    private void highlightButton(Button btn, boolean highlight, boolean isText) {

        if (isText) {
            btn.setBackgroundColor(getColor(R.color.btn_bg_gray));
            btn.setTextColor(getColor(R.color.lightblack));
        } else {
            btn.setBackgroundResource(highlight ? R.drawable.btn_blue : R.drawable.bg_rect_gray_corner);
            btn.setTextColor(highlight ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.lightblack));
        }
    }

    private int getColor(int colorId) {
        return mContext.getResources().getColor(colorId);
    }

    private void hideButtons(GroupHolder holder) {
        holder.line1.setVisibility(View.GONE);
        holder.btnLeft.setVisibility(View.GONE);
        holder.btnCenter.setVisibility(View.GONE);
        holder.btnRight.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    // 点击整个view事件
    private class GroupViewOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            OrderModel item = (OrderModel) v.getTag(R.id.order_parent_item);
            int type = (Integer) v.getTag(R.id.order_parent_item_type);
            Log.v(TAG, "type" + type);
            int id = item.getOrderId();
            int transferID = -1;
            switch (type) {
                case BaseOrderDetailActivity.GET_AGENT_ORDER:
                    id = item.getAgentOrderID();
                    break;
                case BaseOrderDetailActivity.Get_PICK_ORDER:
                    List<PickingBillModel> pickingOrders = item.getPickingOrders();
                    if (!ListUtils.isEmpty(pickingOrders)) {
                        id = item.getPickingOrders().get(0).PickingID;
                    }
                    break;
                case BaseOrderDetailActivity.GET_SHIP_ORDER:
                    id = item.getShipID();
                    break;
                case BaseOrderDetailActivity.GET_BUY_ORDER:
                    transferID = item.getTransferID();
                    break;
            }
            BaseOrderDetailActivity.toOrderDetail2(mContext, id, type, transferID);
        }

    }

    private class ChildViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            boolean isPickingOrder = (Boolean) v.getTag(R.id.order_is_picking_order);
            int orderId = (Integer) v.getTag(R.id.order_sub_item_id);
            if (isPickingOrder) {
                BaseOrderDetailActivity.toOrderDetail(mContext, orderId, BaseOrderDetailActivity.Get_PICK_ORDER);
            } else {
                BaseOrderDetailActivity.toOrderDetail(mContext, orderId, BaseOrderDetailActivity.GET_CHILD_ORDER);
            }

        }
    }

    private class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View v) {
            final OrderButton btn = (OrderButton) v.getTag();
            String action = btn.getAction();
            if (OrderAction.GOTO_CHANGCI.equals(action)) {// 供货商修改价格
                try {
                    OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                    if (btn.getData() instanceof  Double) {
                        int defectiveID = new Double(Double.parseDouble(btn.getData().toString())).intValue();
                        QuickSaleApi.getRecommendShopItems(mContext,
                                mHttpRequestHelper,
                                OrderAdapter.this,
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
//                PinHuoDetailListActivity.launch(mContext, item, !item.IsStart);
//                ViewUtil.gotoChangci(mContext,model);
            }else if (OrderAction.SALE_AFTER.equals(action)){
                try {
                   // boolean redirectToList=FunctionHelper.getOrderButtonRedirectToList(btn.getData());
                    if (btn.getData() instanceof  Double) {
                        int defectiveID = new Double(Double.parseDouble(btn.getData().toString())).intValue();

                        if (defectiveID < 0) {
                            OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                            Intent vendorIntent = new Intent(mContext, PackageListActivity.class);
                            vendorIntent.putExtra(PackageListActivity.EXTRA_ORDERID, order.getOrderId());
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
            }
            else if (OrderAction.SUPPLIERS_CHANGE_PRICE.equals(action)) {// 供货商修改价格
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                OrderAPI.getSendGoodOrderDetail(mContext, mHttpRequestHelper, OrderAdapter.this, order.getShipID());
            } else if (OrderAction.SELLER_CHANGE_PRICE.equals(action)) {// 卖家改价
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                OrderAPI.getSellerOrderDetail(mContext, mHttpRequestHelper, OrderAdapter.this, order.getOrderId());
            } else if (OrderAction.SELLER_CANCEL.equals(action)) {// 卖家取消
                cancelOrder(v, btn);
            } else if (OrderAction.BUYER_PAY.equals(action)) {// 买家支付
//                if (mCurOrderStatu == Const.OrderStatus.WAIT_PAY) {
//                    OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                    Intent it = new Intent(mContext, OrderPayActivity.class);
//                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, order.getOrderId() + "");
//                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, order.getPrice());
//                    mContext.startActivity(it);
//                } else {
                if (isPrePay()) {
                    ViewHub.showTextDialog(mContext, "提示",
                            "是否批量支付？",
                            "批量支付", "只付此单",
                            new ViewHub.EditDialogListener() {
                                @Override
                                public void onOkClick(DialogInterface dialog, EditText editText) {

                                }

                                @Override
                                public void onOkClick(EditText editText) {
                                    //批量
                                    Intent it = new Intent(mContext, OrderMutablePayActivity.class);
                                    it.putExtra(OrderMutablePayActivity.EXTRA_QSID, qsid);
                                    it.putExtra(OrderMutablePayActivity.EXTRA_TITLE, "批量支付");
                                    mContext.startActivity(it);
                                }

                                @Override
                                public void onNegativecClick() {//不需要
                                    OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                                    Intent it = new Intent(mContext, OrderPayActivity.class);
                                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, order.getOrderId() + "");
                                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, order.getPrice());
                                    mContext.startActivity(it);
                                }
                            });
                } else {
                    OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                    Intent it = new Intent(mContext, OrderPayActivity.class);
                    it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, order.getOrderId() + "");
                    it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, order.getPrice());
                    mContext.startActivity(it);
                }
                //}
            } else if (OrderAction.BUYER_CANCEL.equals(action)) {// 买家取消
                cancelOrder(v, btn);
            } else if (OrderAction.SUPPLIER_SHIP.equals(action)) {// 供货商发货
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                int shipId = order.getShipID();
                if (shipId == 0 && order.getShipOrder() != null) {
                    shipId = order.getShipOrder().ID;
                }
                new DialogChooseExpress(v.getContext(), shipId).show();
            } else if (OrderAction.BUYER_CONFIRM_RECEIPT.equals(action)) {// 买家确认收货
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                new DialogSureGetGoods(mContext, order.getOrderId()).show();
            } else if (OrderAction.BUYER_RETURN.equals(action)) {// 买家申请退款
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                //RefundApplyDialog.getInstance(mContext, action, 0, order.getOrderId()).show();
                if (mContext!=null)
                    ((BaseAppCompatActivity)mContext).getOrderItemForRefund(mContext,order.getOrderId());
            } else if (OrderAction.SELLER_RETURN_BILL.equals(action) || OrderAction.SELLER_FOUND_BILL.equals(action)) {
                // 卖家维权) {// 卖家退款
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                Refund refund = order.getRefund();
                if (refund != null) {// 进入卖家退款详细页
                    Intent retIntent = new Intent(mContext, RefundBySellerActivity.class);
                    retIntent.putExtra("ID", refund.getRefundID());
                    mContext.startActivity(retIntent);
                } else {
                    ViewHub.showShortToast(mContext, "refund is null");
                }
            } else if (OrderAction.BUYER_RETURN_BILL.equals(action) || OrderAction.BUYER_FOUND_BILL.equals(action)) {
                // 买家维权// 买家退款
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                Refund refund = order.getRefund();
                if (refund != null) {
                    Intent retIntent = new Intent(mContext, RefundByBuyerActivity.class);
                    retIntent.putExtra("ID", refund.getRefundID());
                    mContext.startActivity(retIntent);
                } else {
                    ViewHub.showShortToast(mContext, "refund is null");
                }
            } else if (OrderAction.BUYER_EXPRESS.equals(action)) {// 买家物流
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                Intent shipIntent = new Intent(mContext, ShipActivity.class);
                shipIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, order.getOrderId());
                mContext.startActivity(shipIntent);
            } else if (OrderAction.SELLER_EXPRESS.equals(action)) {// 卖家物流
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                Intent shipIntent = new Intent(mContext, ShipActivity.class);
                shipIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, order.getOrderId());
                mContext.startActivity(shipIntent);
            } else if (Const.OrderAction.SUPPLIERS_RETUNR_BILL.equals(action) || OrderAction.SUPPLIERS_FOUND_BILL.equals(action)) {
                //供货商退款、供货商维权
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                ShipperRefund refund = order.getShipperRefund();
                if (refund != null) {
                    Intent retIntent = new Intent(mContext, RefundBySupperActivity.class);
                    retIntent.putExtra("ID", refund.ShipperRefundID);
                    mContext.startActivity(retIntent);
                }
            } else if (OrderAction.SHOW_TRANSFER.equals(action)) {
                //展示退款信息
                showTransfer(v, btn);
            } else if (OrderAction.BUHUO.equals(action)) {// 我要补货
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
//                String msg = order.getReplenishmentRemark();
//                new DialogReplenishment(v.getContext(), msg, itemID).show();
                //修改不传去掉qid
                Intent intent = new Intent(v.getContext(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, order.getOrderItems().get(0).getAgentItemID());
                //intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, order.getQSID());
                v.getContext().startActivity(intent);
            } else if (OrderAction.MONEY_BACK.equals(action)) {// 已退
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                Intent intent = new Intent(v.getContext(), TradingDetailsActivity.class);
                intent.putExtra(TradingDetailsActivity.ORDERID, order.getOrderId());
                v.getContext().startActivity(intent);
            }
        }

    }

    //  代付款判断
    private boolean isPrePay() {
        boolean flag = false;
        prePayList.clear();
        if (mData != null) {
            for (int i = 0; i < mData.size(); i++) {
                String orderstatu = mData.get(i).getOrderStatu();
                if (orderstatu.equals("待支付")) {
                    prePayList.add(orderstatu);
                    if (prePayList.size() > 1) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    private void showTransfer(final View v, final OrderButton btn) {
        OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
        if (order != null) {
            OrderAPI.getTransfer(mContext, mHttpRequestHelper, OrderAdapter.this, order.getTransferID());
        } else {
            ViewHub.showShortToast(mContext, "没有订单");
        }
    }

    /**
     * @description 取消订单
     * @created 2015-4-28 下午3:01:56
     * @author ZZB
     */
    private void cancelOrder(final View v, final OrderButton btn) {
        ViewHub.showLightPopDialog((Activity) mContext, "提示", "是否取消订单", "取消", "确定", new PopDialogListener() {
            @Override
            public void onPopDialogButtonClick(int which) {
                OrderModel order = (OrderModel) v.getTag(R.id.order_parent_item);
                if (order != null) {
                    OrderAPI.cancelOrder(mContext, mHttpRequestHelper, OrderAdapter.this, order.getOrderId());
                } else {
                    ViewHub.showShortToast(mContext, "没有订单");
                }

            }
        });
    }

    @Override
    public void onRequestStart(String method) {
        if (OrderMethod.CANCEL_ORDER.equals(method)) {// 取消订单
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
            Toast.makeText(mContext, "已成功提交", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        mLoadingDialog.stop();
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        mLoadingDialog.stop();
    }

    //展示备注层
    private void showRecordView(List<OrderItemRecordDetailModel> recordModels) {
        DialogOrderRecordDetailFragment dialog = DialogOrderRecordDetailFragment.newInstance(recordModels);

        dialog.show(mContext.getSupportFragmentManager(), "DialogOrderRecordDetailFragment");

    }
}
