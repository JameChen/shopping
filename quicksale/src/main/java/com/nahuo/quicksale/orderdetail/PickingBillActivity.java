package com.nahuo.quicksale.orderdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.PickingBillAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.RequestMethod.OrderDetailMethod;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.orderdetail.model.PickingBillModel;

import java.text.DecimalFormat;

import de.greenrobot.event.EventBus;

/****
 * 拣货单详细 created by 陈智勇 2015-4-25 下午5:34:29
 */
public class PickingBillActivity extends BaseOrderActivity {

    private TextView           orderCastTxt, orderWayTxt, orderShouldPayTxt;
    FlowLayout operateBtnParent;
    int                        orderID;
    private TextView           statusTxt;
    private ListView           itemListView;

    private TextView           txtShop, txtPhone, txtQQ;

    private PickingBillModel   pickingBillModel;
    private PickingBillAdapter mAdapter;
    private EventBus mEventBus = EventBus.getDefault();
    private TextView tvWeipuAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单详细");
        setContentView(R.layout.activity_get_pciking_order);
        orderID = getIntent().getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0);
        // orderID = 10114 ;
        mLoadingDialog = new LoadingDialog(this);
        mEventBus.register(this);
        initView();
        initData();
    }

    private void initData() {
        HttpRequest request = mRequestHelper.getRequest(getApplicationContext(), OrderDetailMethod.GET_PICKING_ORDER,
                PickingBillActivity.this);
        request.setConvert2Class(PickingBillModel.class);
        request.addParam("pickingId", String.valueOf(orderID));
        request.addParam("type", "2");
        request.doPost();
        mLoadingDialog.start();
    }

    void initView() {
        super.initView() ;
        statusTxt = (TextView)findViewById(R.id.txt_order_info_state);
        orderCastTxt = (TextView)findViewById(R.id.txt_order_info_money);
        orderWayTxt = (TextView)findViewById(R.id.txt_order_info_sell);
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
        mAdapter = new PickingBillAdapter();
        itemListView.setAdapter(mAdapter);
        operateBtnParent = (FlowLayout)findViewById(R.id.ll_order_info_btn_parent);

        txtShop = (TextView)findViewById(R.id.txt_child_order_shop);
        tvWeipuAccount = (TextView) findViewById(R.id.txt_child_order_account);
        txtPhone = (TextView)findViewById(R.id.txt_child_order_phone);
        txtQQ = (TextView)findViewById(R.id.txt_child_order_qq);
        findViewById(R.id.txt_child_order_shop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pickingBillModel.Shop != null) {
//                    Intent userIntent = new Intent(v.getContext(), UserInfoActivity.class);
//                    userIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, pickingBillModel.Shop.getUserID());
//                    v.getContext().startActivity(userIntent);
                }
            }
        });
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        pickingBillModel = (PickingBillModel)object;
        viewBindData();
    }
    void toWeiXun(int userId , String nickName){
        if(pickingBillModel!=null && pickingBillModel.Shop!=null){
            super.toWeiXun(pickingBillModel.Shop.getUserID(), pickingBillModel.Shop.getUserName()) ;
        }
    }
    void viewBindData() {
        super.viewBindData() ; 
        statusTxt.setText("单号:" + pickingBillModel.Code);
        orderCastTxt.setText(getString(R.string.order_money, pickingBillModel.RetailAmount));
        orderWayTxt.setText("拿货金额:" + pickingBillModel.Amount);
        DecimalFormat df = new DecimalFormat("0.00");
        orderShouldPayTxt.setText(df.format(pickingBillModel.Gain));
        mAdapter.refresh(pickingBillModel.Items);
        if (pickingBillModel.Shop != null) {
            ((View)txtShop.getParent()).setVisibility(View.VISIBLE);
            txtShop.setText(getString(R.string.shop_name, pickingBillModel.Shop.getName()));
            tvWeipuAccount.setText(getString(R.string.weipu_account,pickingBillModel.Shop.getUserName()));
            txtPhone.setText(getString(R.string.phone_number, pickingBillModel.Shop.getMobile()));
            txtQQ.setText(getString(R.string.qq_number, pickingBillModel.Shop.getQQ()));
        }
        else{
            ((View)txtShop.getParent()).setVisibility(View.GONE);
        }
        BaseOrderDetailActivity.addOrderDetailButton(operateBtnParent, pickingBillModel.Buttons, new OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = (String)v.getTag();
                BaseOrderDetailActivity.removeCommentRedBall(v.getContext(), v, action);
                int id = 0;
                String name = "";
                if (pickingBillModel.Shop != null) {
                    id = pickingBillModel.Shop.getUserID();
                    name = pickingBillModel.Shop.getUserName();
                }
                BaseOrderDetailActivity.isBaseButtonClick(v.getContext(), action, pickingBillModel.OrderID, id, name,
                        pickingBillModel.Memo);
            }
        }, pickingBillModel.Memo);
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
}
