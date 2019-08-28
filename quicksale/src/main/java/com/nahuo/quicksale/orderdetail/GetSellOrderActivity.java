package com.nahuo.quicksale.orderdetail;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.AddressActivity2;
import com.nahuo.quicksale.ChangePriceAgentDialog;
import com.nahuo.quicksale.DialogChooseExpress;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.RefundBySellerActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.SellerOrderAdapter;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.Address;
import com.nahuo.quicksale.orderdetail.model.Consignee;
import com.nahuo.quicksale.orderdetail.model.MyAgentOrderContact;
import com.nahuo.quicksale.orderdetail.model.SellerOrderModel;

import java.text.DecimalFormat;

import de.greenrobot.event.EventBus;

/****
 * 售货单详细 created by 陈智勇 2015-4-24 上午10:31:30
 */
public class GetSellOrderActivity extends BaseOrderDetailActivity implements OnClickListener {

    private static final int CHANGE_UP_EXPRESS = 1 ; //修改给上家的报单信息
    private SellerOrderAdapter mAdapter;

    private TextView           buyerNameTxt, postNameTxt, postPhoneTxt, postAddrTxt;

    private TextView           gainTxt;

    private TextView           shipMoneyTxt;

    private TextView           getPriceTxt;

    private TextView           outPriceTxt;

    private TextView           txtBuyerPostName1, txtBuyerPostPhone, txtBuyerPostAddr;

    private View               editUpAddress;
    private EventBus           mEventBus = EventBus.getDefault();
    private TextView txtExpressName , txtExpressNo , txtExpressTime ; 
    private Button btnEditExpress ; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单详细");
        setContentView(R.layout.activity_get_sell_order);
        orderID = getIntent().getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0);
        initView();
        initData();
        mEventBus.register(this);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.SEND_GOOD:
            case EventBusId.REFUND_SELLER_AGRESS:
            case EventBusId.CHANGE_PRICE:
            case EventBusId.ADD_MEMO:
            case EventBusId.CHANGE_NUMBER:
                initData();
                break;
        }
    }

    @Override
    void initView() {
        super.initView();
        mAdapter = new SellerOrderAdapter();
        itemListView.setAdapter(mAdapter);

        buyerNameTxt = (TextView)findViewById(R.id.txt_order_buyer);
        postNameTxt = (TextView)findViewById(R.id.txt_order_post_name);
        postPhoneTxt = (TextView)findViewById(R.id.txt_order_post_phone);
        postAddrTxt = (TextView)findViewById(R.id.txt_order_post_address);

        txtBuyerPostName1 = (TextView)findViewById(R.id.txt_order_post_name1);
        txtBuyerPostPhone = (TextView)findViewById(R.id.txt_order_post_phone1);
        txtBuyerPostAddr = (TextView)findViewById(R.id.txt_order_post_address1);

        editUpAddress = findViewById(R.id.btn_edit);
        editUpAddress.setOnClickListener(this);
        buyerNameTxt.setOnClickListener(this);
        
        txtExpressName = (TextView)findViewById(R.id.txt_express_name) ; 
        txtExpressNo = (TextView)findViewById(R.id.txt_express_no) ; 
        txtExpressTime = (TextView)findViewById(R.id.txt_express_time) ; 
        btnEditExpress = (Button)findViewById(R.id.btn_edit_express);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Address address = (Address)data.getSerializableExtra(AddressActivity2.INTENT_SELECTED_ADDRESS);
            if (address != null) {
                if(requestCode == CHANGE_UP_EXPRESS){ //给上家的报单信息
                    OrderAPI.setUpToSuperAddress(this, mRequestHelper, orderID, address.getId());
                }
            }
        }
    }

    @Override
    void initData() {
        super.initData();
        OrderAPI.getSellerOrderDetail(getApplicationContext(), mRequestHelper, this, orderID);
    }
    void toWeiXun(int userId , String nickName){
        SellerOrderModel sellerModel = (SellerOrderModel)mOrderInfoMode;
        if(sellerModel!=null && sellerModel.Buyer!=null){
            super.toWeiXun(sellerModel.Buyer.UserID, sellerModel.Buyer.UserName) ;
        }
    }
    @Override
    void viewBindData() {
        super.viewBindData();
        mAdapter.refresh(mOrderInfoMode.getItems());
        SellerOrderModel sellerModel = (SellerOrderModel)mOrderInfoMode;
        String buyerText = getString(R.string.buyer_name, sellerModel.Buyer.UserName); 
        ViewHub.highlightTextView(getApplicationContext(), buyerNameTxt, buyerText, R.color.light_blue, 5, buyerText.length());
        Consignee consignee = sellerModel.Consignee;
        postNameTxt.setText(getString(R.string.post_name1, consignee.getRealName()));
        postPhoneTxt.setText(getString(R.string.contact, consignee.getMobile()));
        postAddrTxt.setText(getString(R.string.post_address, consignee.getArea() + consignee.getStreet()));
        if (sellerModel.IsAgentOrderUseMyContact) {
            editUpAddress.setVisibility(View.VISIBLE);
        } else {
            editUpAddress.setVisibility(View.INVISIBLE);
        }

        if (!sellerModel.SellerIsOnlyShipper) {
            ViewGroup parent = (ViewGroup)operateBtnParent.getParent();
            int index = parent.indexOfChild(operateBtnParent);
            String paStr = "订单利润:";
            float gain = sellerModel.Gain;
            DecimalFormat df = new DecimalFormat("0.00");
            int len = paStr.length();
            paStr = paStr + df.format(gain);
            if (gainTxt == null) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                int leftMargin = getResources().getDimensionPixelSize(R.dimen.activity_margin);
                int topMargin = FunctionHelper.dip2px(getResources(), 4);
                params.leftMargin = leftMargin;
                params.topMargin = topMargin;
                gainTxt = new TextView(this);
                parent.addView(gainTxt, index, params);

                shipMoneyTxt = new TextView(this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.weight = 1;
                LinearLayout ll = new LinearLayout(this);
                outPriceTxt = new TextView(this);
                parent.addView(shipMoneyTxt, index, params);
                ll.addView(outPriceTxt, p);
                
                getPriceTxt = new TextView(this);
                getPriceTxt.setGravity(Gravity.RIGHT);
                getPriceTxt.setPadding(0, 0, leftMargin, 0);
                ll.addView(getPriceTxt, p);
                LinearLayout.LayoutParams llP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                llP.leftMargin = leftMargin;
                llP.topMargin = topMargin;

                parent.addView(ll, index, llP);
                View line = new View(this);
                line.setBackgroundResource(R.color.line_gray);
                LinearLayout.LayoutParams lineP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                lineP.topMargin = topMargin;
                parent.addView(line, index, lineP);
            }
            SpannableString msp = new SpannableString(paStr);
            if (gain < 0) {
                msp.setSpan(new ForegroundColorSpan(Color.RED), len, paStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green_price)), len, paStr.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            gainTxt.setText(msp);

            shipMoneyTxt.setText("拿货运费:" + df.format(sellerModel.AgentsShipAmount));

            getPriceTxt.setText("拿货金额:" + df.format(sellerModel.AgentsProductAmount));
            outPriceTxt.setText("拿货支出:" + df.format(sellerModel.AgentExpense));

        }

        MyAgentOrderContact agentContact = sellerModel.MyAgentOrderContact;
        if (agentContact != null) {
            ((View)txtBuyerPostName1.getParent()).setVisibility(View.VISIBLE);

            txtBuyerPostName1.setText(getString(R.string.post_name1, agentContact.RealName));
            txtBuyerPostPhone.setText(getString(R.string.contact, agentContact.Mobile));
            String address = agentContact.Province + " " + agentContact.City + " " + agentContact.Area + " "
                    + agentContact.Address;
            txtBuyerPostAddr.setText(getString(R.string.post_address, address));
            
        }
        else{
            ((View)txtBuyerPostName1.getParent()).setVisibility(View.GONE);
        }
        //增加物流信息
        View expressParent = (View)txtExpressNo.getParent() ; 
        if(sellerModel.MyExpress!=null&&sellerModel.MyExpress.ShipDate.length()>0){
            expressParent.setVisibility(View.VISIBLE) ; 
            txtExpressName.setText(getString(R.string.post_company , sellerModel.MyExpress.Name)) ; 
            txtExpressNo.setText(getString(R.string.post_number , sellerModel.MyExpress.TrackingNo)) ;
            txtExpressTime.setText(getString(R.string.send_goods_time , sellerModel.MyExpress.ShipDate)) ; 
            if(sellerModel.MyExpress.CanUpdateExpress){
                btnEditExpress.setOnClickListener(this) ;
                btnEditExpress.setVisibility(View.VISIBLE) ; 
            }
            else
                btnEditExpress.setVisibility(View.INVISIBLE) ; 
            
        }
        else if(expressParent.getVisibility()!=View.GONE){
            expressParent.setVisibility(View.GONE); 
        }
        
        addOrderDetailButton(operateBtnParent, mOrderInfoMode.Buttons, ol, sellerModel.getMemo(),
                sellerModel.UnreadTalkingCount);
    }

    private OnClickListener ol = new OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       String action = (String)v.getTag();
                                       BaseOrderDetailActivity.removeCommentRedBall(v.getContext(), v, action);
                                       SellerOrderModel sellerModel = (SellerOrderModel)mOrderInfoMode;
                                       int userID = 0;
                                       String name = "";
                                       if (sellerModel.Buyer != null) {
                                           userID = sellerModel.Buyer.UserID;
                                           name = sellerModel.Buyer.UserName;
                                       }
                                       if (BaseOrderDetailActivity.isBaseButtonClick(v.getContext(), action,
                                               sellerModel.getOrderID(), userID, name, mOrderInfoMode.getMemo()))
                                           return;
                                       do {
                                           if (Const.OrderAction.SELLER_CANCEL.equals(action)) {
                                               ViewHub.showOkDialog(v.getContext(), getString(R.string.prompt),
                                                       getString(R.string.cancel_order),
                                                       getString(R.string.titlebar_btnOK),
                                                       getString(R.string.titlebar_btnCancel),
                                                       new DialogInterface.OnClickListener() {
                                                           @Override
                                                           public void onClick(DialogInterface dialog, int which) {
                                                               mLoadingDialog.start("订单取消中...");
                                                               OrderAPI.cancelOrder(getApplicationContext(),
                                                                       mRequestHelper, GetSellOrderActivity.this,
                                                                       orderID);
                                                           }
                                                       });
                                               break;
                                           }
                                           if (Const.OrderAction.SELLER_CHANGE_PRICE.equals(action)) {
                                               new ChangePriceAgentDialog(v.getContext(), sellerModel).show();
                                               // new ChangePriceAgentDialog(v.getContext(), orderID,
                                               // sellerModel.getOrderPrice(), sellerModel.getPostFee(),
                                               // sellerModel.getPayableAmount(), sellerModel.AgentsProductAmount,
                                               // sellerModel.AgentsShipAmount, sellerModel.AgentExpense).show();
                                               break;
                                           }
                                           if (Const.OrderAction.SUPPLIER_SHIP.equals(action)) {
                                               if (sellerModel.ShipOrder != null)
                                                   new DialogChooseExpress(v.getContext(), sellerModel.ShipOrder.ID).show();
                                               break;
                                           }
                                           if (Const.OrderAction.SELLER_EXPRESS.equals(action)) {
                                               // 进入卖家物流详细页
                                               Intent shipIntent = new Intent(v.getContext(), ShipActivity.class);
                                               shipIntent.putExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, orderID);
                                               v.getContext().startActivity(shipIntent);
                                               break;
                                           }
                                           if (Const.OrderAction.SELLER_RETURN_BILL.equals(action)||Const.OrderAction.SELLER_FOUND_BILL.equals(action)) {
                                               // 进入卖家退款详细页
                                               if (sellerModel.Refund != null) {
                                                   Intent retIntent = new Intent(v.getContext(),
                                                           RefundBySellerActivity.class);
                                                   retIntent.putExtra("ID", sellerModel.Refund.getRefundID());
                                                   v.getContext().startActivity(retIntent);
                                               }
                                               break;
                                           }
                                           if (Const.OrderAction.SELLER_EXPRESS_BILL.equals(action)) {
                                               // 进入本人的发货单详细页
                                               if (sellerModel.ShipOrder != null) {
                                                   int shipID = sellerModel.ShipOrder.ID;
                                                   BaseOrderDetailActivity.toOrderDetail(v.getContext(), shipID,
                                                           BaseOrderDetailActivity.GET_SHIP_ORDER);
                                               }
                                               break;
                                           }
                                       } while (false);
                                   }
                               };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_express:
                SellerOrderModel model = (SellerOrderModel)mOrderInfoMode ; 
                if(model.ShipOrder!=null)
                    new DialogChooseExpress(v.getContext(), model.ShipOrder.ID, true).show() ; 
                break ; 
            case R.id.btn_edit:
                Intent intent = new Intent(v.getContext(), AddressActivity2.class);
                startActivityForResult(intent, CHANGE_UP_EXPRESS);
                break;
            case R.id.txt_order_buyer:
                SellerOrderModel sellModel = ((SellerOrderModel)mOrderInfoMode);
                if (sellModel.Buyer != null) {
//                    Intent userIntent = new Intent(v.getContext(), UserInfoActivity.class);
//                    userIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, sellModel.Buyer.UserID);
//                    v.getContext().startActivity(userIntent);
                }
                break;
        }
    }

}
