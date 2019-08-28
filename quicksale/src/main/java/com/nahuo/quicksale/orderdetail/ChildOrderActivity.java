package com.nahuo.quicksale.orderdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.AgentOrderAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.RequestMethod.OrderDetailMethod;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.orderdetail.model.ChildOrderModel;

import de.greenrobot.event.EventBus;

/****
 * 子订单详细 created by 陈智勇 2015-4-24 上午10:43:41
 */
public class ChildOrderActivity extends BaseOrderActivity implements OnClickListener {

    private TextView          orderCodeTxt, orderCastTxt, orderWayTxt, orderShouldPayTxt;
    FlowLayout operateBtnParent;
    int                       orderID;
    private TextView          statusTxt, orderCreateTxt;
    private ListView          itemListView;

    private TextView          txtShop, txtPhone, txtQQ;

    private ChildOrderModel   childOrderModel;
    private AgentOrderAdapter mAdapter;

    private TextView tvWeipuAccount;
    private EventBus mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单详细");
        setContentView(R.layout.activity_get_child_order);
        orderID = getIntent().getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0);
        // orderID = 11884 ;
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setCancelable(true) ;
        mEventBus.register(this);
        initView();
        initData();
    }

    private void initData() {
        mLoadingDialog.start();
        HttpRequest request = mRequestHelper.getRequest(getApplicationContext(), OrderDetailMethod.GET_CHILD_ORDER,
                ChildOrderActivity.this);
        request.setConvert2Class(ChildOrderModel.class);
        request.addParam("agentOrderId", String.valueOf(orderID));
        request.addParam("type", "2");
        request.doPost();
    }

    void initView() {
        super.initView() ;
        statusTxt = (TextView)findViewById(R.id.txt_order_info_state);
        orderCreateTxt = (TextView)findViewById(R.id.txt_order_info_time);

        orderCodeTxt = (TextView)findViewById(R.id.txt_order_info_bill);
        orderCastTxt = (TextView)findViewById(R.id.txt_order_info_money);
        orderWayTxt = (TextView)findViewById(R.id.txt_order_info_buyway);
        orderShouldPayTxt = (TextView)findViewById(R.id.txt_order_info_should_pay);

        itemListView = (ListView)findViewById(R.id.lst_order_info_item);
        itemListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, mAdapter.getItem(position).getAgentItemID());
                parent.getContext().startActivity(intent);
            }
        });
        mAdapter = new AgentOrderAdapter();
        itemListView.setAdapter(mAdapter);
        operateBtnParent = (FlowLayout)findViewById(R.id.ll_order_info_btn_parent);

        txtShop = (TextView)findViewById(R.id.txt_child_order_shop);
        tvWeipuAccount=(TextView)findViewById(R.id.txt_child_order_account);
        txtPhone = (TextView)findViewById(R.id.txt_child_order_phone);
        txtQQ = (TextView)findViewById(R.id.txt_child_order_qq);
        txtShop.setOnClickListener(this);

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        childOrderModel = (ChildOrderModel)object;
        viewBindData();
    }
    void toWeiXun(int userId , String nickName){
        if(childOrderModel!=null){
            if(childOrderModel.Seller != null){
                super.toWeiXun(childOrderModel.Seller.UserID, childOrderModel.Seller.UserName) ;
            }
        }
    }
    void viewBindData() {
        super.viewBindData() ; 
        statusTxt.setText(getString(R.string.order_state, childOrderModel.getStatu()));
        orderCreateTxt.setText(childOrderModel.getCreateDate());
        orderCodeTxt.setText("订单编号:" + childOrderModel.getCode());
        orderCastTxt.setText(getString(R.string.order_get_money2, childOrderModel.getAmount()));
        orderWayTxt.setText(getString(R.string.sell_money, childOrderModel.RetailAmount));
        orderShouldPayTxt.setText(getString(R.string.rmb_2, childOrderModel.Gain)+"(不包含运费)");
        mAdapter.refresh(childOrderModel.getItems());

        if (childOrderModel.Seller != null) {
            ((View)tvWeipuAccount.getParent()).setVisibility(View.VISIBLE);
            String shopName = getString(R.string.shop_name, childOrderModel.Seller.Shop.getName());
            ViewHub.highlightTextView(getApplicationContext(), txtShop, shopName, R.color.light_blue, 3, shopName.length());
            tvWeipuAccount.setText(getString(R.string.weipu_account,childOrderModel.Seller.Shop.getUserName()));
            txtPhone.setText(getString(R.string.phone_number, childOrderModel.Seller.Shop.getMobile()));
            txtQQ.setText(getString(R.string.qq_number, childOrderModel.Seller.Shop.getQQ()));
        }
        else{
            ((View)tvWeipuAccount.getParent()).setVisibility(View.GONE);
        }
        BaseOrderDetailActivity.addOrderDetailButton(operateBtnParent, childOrderModel.Buttons, new OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = (String)v.getTag();
                BaseOrderDetailActivity.removeCommentRedBall(v.getContext(), v, action);
                if (childOrderModel.Seller != null) {
                    ShopInfoModel shop = childOrderModel.Seller.Shop;
                    if (shop != null) {
                        BaseOrderDetailActivity.isBaseButtonClick(v.getContext(), action, childOrderModel.getOrderID(),
                                shop.getUserID(), shop.getUserName(), childOrderModel.getMemo());
                    }
                }
            }
        }, childOrderModel.getMemo(), childOrderModel.UnreadTalkingCount);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.ADD_MEMO:
            case EventBusId.CHANGE_NUMBER:
                initData();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_child_order_shop:
                if (childOrderModel.Seller != null) {
//                    Intent userIntent = new Intent(v.getContext(), UserInfoActivity.class);
//                    userIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, childOrderModel.Seller.UserID);
//                    v.getContext().startActivity(userIntent);
                }
                break;
        }
    }
}
