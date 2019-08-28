package com.nahuo.quicksale.orderdetail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.ChangePriceDialog1;
import com.nahuo.quicksale.DialogChooseExpress;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.RefundBySellerActivity;
import com.nahuo.quicksale.RefundBySupperActivity;
import com.nahuo.quicksale.adapter.SellerOrderAdapter;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.orderdetail.model.AgentOrders;
import com.nahuo.quicksale.orderdetail.model.Buyer;
import com.nahuo.quicksale.orderdetail.model.Contact;
import com.nahuo.quicksale.orderdetail.model.OrderItemModel;
import com.nahuo.quicksale.orderdetail.model.SendGoodsModel;

import java.text.DecimalFormat;
import java.util.List;

import de.greenrobot.event.EventBus;

/****
 * 发货单详细 created by 陈智勇 2015-4-24 上午10:43:41
 */
public class SendGoodsActivity extends BaseOrderActivity implements View.OnClickListener {

    private TextView           orderCodeTxt, orderCastTxt, orderWayTxt;
    private TextView           postCompanyTxt, postNoTxt, postTimeTxt;
    FlowLayout operateBtnParent;
    int                        orderID;
    private TextView           statusTxt, orderCreateTxt;
    private ListView           itemListView;

    private TextView           txtBuyerName, txtBuyerPostName, txtBuyerPhone, txtBuyerAddr;

    private SendGoodsModel     sendGoodsModel;
    private SellerOrderAdapter mAdapter;

    private TextView           postTxt;

    private Buyer              mBuyer;
    private EventBus           mEventBus = EventBus.getDefault();
    private int mUnreadMsgCount;
    private Button btnEditExpress ; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单详细");
        setContentView(R.layout.activity_get_order_sentgoods);
        orderID = getIntent().getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0);
        // orderID = 18574 ;
        mLoadingDialog = new LoadingDialog(this);
        initView();
        initData();
        mEventBus.register(this);
    }

    private void initData() {
        OrderAPI.getSendGoodOrderDetail(getApplicationContext(), mRequestHelper, this, orderID);
        mLoadingDialog.start();
    }

    void initView() {
        super.initView() ;
        statusTxt = (TextView)findViewById(R.id.txt_order_info_state);
        orderCreateTxt = (TextView)findViewById(R.id.txt_order_info_time);

        orderCodeTxt = (TextView)findViewById(R.id.txt_order_info_bill);
        orderCastTxt = (TextView)findViewById(R.id.txt_order_info_money);
        orderWayTxt = (TextView)findViewById(R.id.txt_order_info_buyway);

        itemListView = (ListView)findViewById(R.id.lst_order_info_item);
        itemListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, mAdapter.getItem(position).getAgentItemID());
                parent.getContext().startActivity(intent);
            }
        });
        mAdapter = new SellerOrderAdapter();
        itemListView.setAdapter(mAdapter);
        operateBtnParent = (FlowLayout)findViewById(R.id.ll_order_info_btn_parent);

        txtBuyerName = (TextView)findViewById(R.id.txt_buyer_name);
        txtBuyerPostName = (TextView)findViewById(R.id.txt_order_post_name);
        txtBuyerPhone = (TextView)findViewById(R.id.txt_order_post_phone);
        txtBuyerAddr = (TextView)findViewById(R.id.txt_order_post_address);

        postCompanyTxt = (TextView)findViewById(R.id.txt_send_post_company);
        postNoTxt = (TextView)findViewById(R.id.txt_send_post_code);
        postTimeTxt = (TextView)findViewById(R.id.txt_send_post_time);

        postTxt = (TextView)findViewById(R.id.txt_order_info_post);

        findViewById(R.id.ll_name).setOnClickListener(this);
        btnEditExpress = (Button)findViewById(R.id.btn_edit_express);
        btnEditExpress.setOnClickListener(this) ; 
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        if (object instanceof SendGoodsModel) {
            sendGoodsModel = (SendGoodsModel)object;
            viewBindData();
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        super.onRequestExp(method, msg, data);
    }
    void toWeiXun(int userId , String nickName){
        if(mBuyer!=null){
            super.toWeiXun(mBuyer.UserID, mBuyer.UserName) ;
        }
    }
    void viewBindData() {
        super.viewBindData() ; 
        statusTxt.setText(getString(R.string.order_state, sendGoodsModel.ShipStatu));
        orderCreateTxt.setText(sendGoodsModel.CreateDate);
        orderCodeTxt.setText("订单编号:" + sendGoodsModel.Code);

        DecimalFormat df = new DecimalFormat("0.00");
        String postStr = "快递运费:" + df.format(sendGoodsModel.PostFee);
        if(sendGoodsModel.IsFreePost){
            postStr = postStr +"(包邮)" ; 
        }
        if (sendGoodsModel.Type.equals("NoAgent")) {
            String needPay = "应付金额:" + getString(R.string.rmb_2, sendGoodsModel.ProductAmount);
            SpannableString msp = new SpannableString(needPay);
            msp.setSpan(new ForegroundColorSpan(Color.RED), 5, needPay.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            orderWayTxt.setText(msp);
            orderCastTxt.setText(getString(R.string.order_money, sendGoodsModel.PayableAmount));
            postTxt.setVisibility(View.VISIBLE);
            postTxt.setText(postStr);
        } else {
            orderCastTxt.setText(getString(R.string.order_money, sendGoodsModel.Amount));
            postTxt.setVisibility(View.GONE);
            orderWayTxt.setText(postStr);
        }
        List<OrderItemModel> items = sendGoodsModel.Items;
        if (items == null || items.size() == 0) {
            if (sendGoodsModel.AgentOrders != null && sendGoodsModel.AgentOrders.size() > 0) {
                AgentOrders agentOrders = sendGoodsModel.AgentOrders.get(0);
                mUnreadMsgCount = agentOrders.UnreadTalkingCount;
                mAdapter.refresh(agentOrders.Items);
                mBuyer = agentOrders.Buyer;
                if (mBuyer != null) {
                    txtBuyerName.setText(mBuyer.UserName);
                }
            }
        } else {
            mAdapter.refresh(sendGoodsModel.Items);
            mBuyer = sendGoodsModel.Buyer;
            mUnreadMsgCount = sendGoodsModel.UnreadTalkingCount;
            if (mBuyer != null) {
                txtBuyerName.setText(mBuyer.UserName);

            }
        }
        Contact consignee = sendGoodsModel.Consignee;
        if (consignee != null) {
            ((View)txtBuyerPostName.getParent()).setVisibility(View.VISIBLE);
            String postNameStr = getString(R.string.post_address, consignee.getRealName());
            txtBuyerPostName.setText(postNameStr);
            String address = consignee.getProvince() + " " + consignee.getCity() + " " + consignee.getArea() + " "
                    + consignee.getAddress();
            txtBuyerAddr.setText(getString(R.string.post_address, address));
            String contactStr = getString(R.string.contact, consignee.getMobile());
            txtBuyerPhone.setText(contactStr);
        }
        else{
            ((View)txtBuyerPostName.getParent()).setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(sendGoodsModel.ShipDate)) {
            ((View)postCompanyTxt.getParent()).setVisibility(View.VISIBLE);
            postCompanyTxt.setText(getString(R.string.post_company, sendGoodsModel.ExpressName));
            postNoTxt.setText(getString(R.string.post_number, sendGoodsModel.TrackingNo));
            postTimeTxt.setText(getString(R.string.send_goods_time, sendGoodsModel.ShipDate));
            
        }
        else{
            ((View)postCompanyTxt.getParent()).setVisibility(View.GONE);
        }
        if(sendGoodsModel.CanUpdateExpress){
            btnEditExpress.setVisibility(View.VISIBLE) ; 
        }
        else{
            btnEditExpress.setVisibility(View.INVISIBLE) ; 
        }

        BaseOrderDetailActivity.addOrderDetailButton(operateBtnParent, sendGoodsModel.Buttons, ol, sendGoodsModel.Memo,  mUnreadMsgCount);
    }

    private OnClickListener ol = new OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String action = (String)v.getTag();
                                       BaseOrderDetailActivity.removeCommentRedBall(v.getContext(), v, action);
                                       int userId = mBuyer.UserID;
                                       String userName = mBuyer.UserName;
                                       if (BaseOrderDetailActivity.isBaseButtonClick(v.getContext(), action,
                                               sendGoodsModel.OrderID, userId, userName, sendGoodsModel.Memo))
                                           return;
                                       do {
                                           if (Const.OrderAction.SUPPLIERS_CHANGE_PRICE.equals(action)) {
                                               new ChangePriceDialog1(v.getContext(), sendGoodsModel.ShipID,
                                                       sendGoodsModel.Amount, sendGoodsModel.PostFee,
                                                       sendGoodsModel.IsFreePost).show();
                                           }
                                           if (Const.OrderAction.SUPPLIER_SHIP.equals(action)) {
                                               new DialogChooseExpress(v.getContext(), sendGoodsModel.ShipID).show();
                                               break;
                                           }
                                           if (Const.OrderAction.SUPPLIERS_RETUNR_BILL.equals(action)||Const.OrderAction.SUPPLIERS_FOUND_BILL.equals(action)) {
                                               // 进入供货商退款详细页
                                               if (sendGoodsModel.ShipperRefund != null) {
                                                   Intent retIntent = new Intent(v.getContext(),
                                                           RefundBySupperActivity.class);
                                                   retIntent.putExtra("ID",
                                                           sendGoodsModel.ShipperRefund.ShipperRefundID);
                                                   v.getContext().startActivity(retIntent);
                                               }
                                               break;
                                           }
                                           if (Const.OrderAction.SELLER_RETURN_BILL.equals(action)||Const.OrderAction.SELLER_FOUND_BILL.equals(action)) {
                                               // 进入卖家退款详细页
                                               if (sendGoodsModel.Refund != null) {
                                                   Intent retIntent = new Intent(v.getContext(),
                                                           RefundBySellerActivity.class);
                                                   retIntent.putExtra("ID", sendGoodsModel.Refund.getRefundID());
                                                   v.getContext().startActivity(retIntent);
                                               }
                                               break;
                                           }
                                          
                                       } while (false);
                                   }
                               };

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.CHANGE_PRICE:
            case EventBusId.SEND_GOOD:
            case EventBusId.REFUND_SUPP_AGRESS:
            case EventBusId.ADD_MEMO:
            case EventBusId.CHANGE_NUMBER:
                initData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mEventBus.unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_buyer_name:// 点击用户名进入名片页
//                UserInfoActivity.toUserInfoActivity(this, mBuyer.UserID);
                break;
            case R.id.ll_name:
                if (sendGoodsModel.Buyer != null) {
//                    Intent userIntent = new Intent(v.getContext(), UserInfoActivity.class);
//                    userIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, sendGoodsModel.Buyer.UserID);
//                    v.getContext().startActivity(userIntent);
                }
                break ;
            case R.id.btn_edit_express:
                new DialogChooseExpress(v.getContext(), sendGoodsModel.ShipID, true).show() ; 
                break ; 
        }

    }
}
