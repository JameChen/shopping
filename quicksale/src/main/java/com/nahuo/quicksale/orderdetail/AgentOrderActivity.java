package com.nahuo.quicksale.orderdetail;

import android.app.Activity;
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
import com.nahuo.quicksale.AddressActivity2;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.AgentOrderAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.RequestMethod.OrderDetailMethod;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.Address;
import com.nahuo.quicksale.orderdetail.model.AgentOrderModel;
import com.nahuo.quicksale.orderdetail.model.Buyer;
import com.nahuo.quicksale.orderdetail.model.Contact;
import com.nahuo.quicksale.orderdetail.model.MyAgentOrderContact;

import de.greenrobot.event.EventBus;

/****
 * 代理单详细
 * created by 陈智勇   2015-4-24  上午10:43:41
 */
public class AgentOrderActivity extends BaseOrderActivity implements OnClickListener{
    
    private TextView        orderCodeTxt, orderCastTxt, orderWayTxt,
        orderShouldPayTxt;
    FlowLayout operateBtnParent ;
    int orderID ;
    private TextView        statusTxt, orderCreateTxt;
    private ListView itemListView; 

    private TextView txtBuyerName , txtBuyerPostName , txtBuyerPhone , txtBuyerAddr ;
    
    private TextView txtBuyerPostName1 , txtBuyerPostPhone , txtBuyerPostAddr ;  
    private AgentOrderModel agentModel; 
    private AgentOrderAdapter mAdapter ; 
    private View editUpAddress ; 
    private EventBus mEventBus = EventBus.getDefault();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("订单详细") ; 
        setContentView(R.layout.activity_get_agent_order);
        orderID = getIntent().getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0) ; 
        mLoadingDialog = new LoadingDialog(this) ; 
//        orderID = 11883 ; 
        mEventBus.register(this) ; 
        initView() ; 
        initData() ; 
    }

    private void initData() {
        mLoadingDialog.start() ;
        HttpRequest request = mRequestHelper.getRequest(getApplicationContext()
                , OrderDetailMethod.GET_AGENT_ORDER, AgentOrderActivity.this );
        request.setConvert2Class(AgentOrderModel.class) ; 
        request.addParam("agentOrderId", String.valueOf(orderID));
        request.addParam("type", "2");
        request.doPost() ; 
    }

    void initView() {
        super.initView() ; 
        statusTxt = (TextView)findViewById(R.id.txt_order_info_state);
        orderCreateTxt = (TextView)findViewById(R.id.txt_order_info_time);
        
        orderCodeTxt = (TextView)findViewById(R.id.txt_order_info_bill);
        orderCastTxt = (TextView)findViewById(R.id.txt_order_info_money);
        orderWayTxt = (TextView)findViewById(R.id.txt_order_info_buyway);
        orderShouldPayTxt = (TextView)findViewById(R.id.txt_order_info_should_pay);
        
        itemListView = (ListView)findViewById(R.id.lst_order_info_item) ; 
        itemListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(parent.getContext() , ItemDetailsActivity.class) ; 
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, mAdapter.getItem(position).getAgentItemID()) ;
                parent.getContext().startActivity(intent) ; 
            }}) ; 
        mAdapter = new AgentOrderAdapter() ; 
        itemListView.setAdapter(mAdapter) ; 
        operateBtnParent = (FlowLayout) findViewById(R.id.ll_order_info_btn_parent);
        
        txtBuyerName = (TextView)findViewById(R.id.txt_buyer_name);
        txtBuyerPostName = (TextView)findViewById(R.id.txt_order_post_name);
        txtBuyerPhone = (TextView)findViewById(R.id.txt_order_post_phone);
        txtBuyerAddr = (TextView)findViewById(R.id.txt_order_post_address);
        
        txtBuyerPostName1 = (TextView)findViewById(R.id.txt_order_post_name1);
        txtBuyerPostPhone = (TextView)findViewById(R.id.txt_order_post_phone1);
        txtBuyerPostAddr = (TextView)findViewById(R.id.txt_order_post_address1);
        
        editUpAddress = findViewById(R.id.btn_edit) ; 
        editUpAddress.setOnClickListener(this) ; 
        findViewById(R.id.ll_agent).setOnClickListener(this);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            Address address = (Address)data.getSerializableExtra(AddressActivity2.INTENT_SELECTED_ADDRESS);
            if(address!=null){
                OrderAPI.setUpToSuperAddress(this, mRequestHelper, agentModel.getOrderID(), address.getId()) ; 
            }
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        if(object instanceof AgentOrderModel){
            agentModel = (AgentOrderModel)object ; 
            viewBindData() ; 
        }
//        mScrollView.onRefreshComplete() ; 
    }
    void toWeiXun(int userId , String nickName){
        if(agentModel!=null){
            Buyer buyer = agentModel.Buyer ; 
            if(buyer!=null){
                super.toWeiXun(buyer.UserID, buyer.UserName) ;
            }
        }
    }
    void viewBindData() {
        super.viewBindData() ; 
        statusTxt.setText(getString(R.string.order_state, agentModel.getStatu())) ; 
        orderCreateTxt.setText(agentModel.getCreateDate()) ;
        orderCodeTxt.setText("订单编号:"+agentModel.getCode()); 
        orderCastTxt.setText(getString(R.string.order_money, agentModel.getAmount())) ;
        orderWayTxt.setText(getString(R.string.order_get_money2 , agentModel.StockAmount));
        orderShouldPayTxt.setText(getString(R.string.rmb_2 , agentModel.Gain)+"(不包含运费)");
        mAdapter.refresh(agentModel.getItems()) ; 
        
        if(agentModel.IsAgentOrderUseMyContact){
            editUpAddress.setVisibility(View.VISIBLE) ; 
        }
        else{
            editUpAddress.setVisibility(View.INVISIBLE) ; 
        }
        Buyer buyer = agentModel.Buyer ; 
        if(buyer!=null){
            txtBuyerName.setText(buyer.UserName);
            Contact contact = buyer.Contact ; 
            String postNameStr = getString(R.string.post_name1 ,  contact.getRealName()) ; 
            txtBuyerPostName.setText(postNameStr);
            String address = contact.getProvince()+contact.getCity()+contact.getArea()+contact.getAddress()  ; 
            txtBuyerAddr.setText(getString(R.string.post_address , address)) ;
            String contactStr = getString(R.string.contact , contact.getMobile()) ; 
            txtBuyerPhone.setText(contactStr) ; 
        }
        
        MyAgentOrderContact agentContact = agentModel.MyAgentOrderContact ; 
        if(agentContact!=null){
            ((View)txtBuyerPostName1.getParent()).setVisibility(View.VISIBLE) ; 
            
            txtBuyerPostName1.setText(getString(R.string.post_name1 ,  agentContact.RealName)) ; 
            txtBuyerPostPhone.setText(getString(R.string.contact , agentContact.Mobile));
            String address = agentContact.Province+agentContact.City+agentContact.Area+agentContact.Address  ; 
            txtBuyerAddr.setText(getString(R.string.post_address , address)) ;
            txtBuyerPostAddr.setText(address) ;
        }
        else{
            ((View)txtBuyerPostName1.getParent()).setVisibility(View.GONE) ; 
        }
        BaseOrderDetailActivity.addOrderDetailButton(operateBtnParent, agentModel.Buttons, new OnClickListener() {
            @Override
            public void onClick(View v) {
                String action = (String)v.getTag() ;
                BaseOrderDetailActivity.removeCommentRedBall(v.getContext(), v, action);
                if(agentModel.Buyer!=null){
                    BaseOrderDetailActivity.isBaseButtonClick(v.getContext(), action, agentModel.getOrderID() 
                            , agentModel.Buyer.UserID , agentModel.Buyer.UserName , agentModel.getMemo()) ;
                }
            }
        } , agentModel.getMemo(),agentModel.UnreadTalkingCount) ; 
    }
    public void onEventMainThread(BusEvent event) {
        switch(event.id){
            case EventBusId.ADD_MEMO :
            case EventBusId.CHANGE_NUMBER:
            case EventBusId.UP_SUPER_ADDRESS:
                initData() ; 
                break ;
        }
    }
    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg);
//        mScrollView.onRefreshComplete() ; 
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this) ; 
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_edit:
                Intent intent = new Intent(v.getContext(), AddressActivity2.class) ; 
                startActivityForResult(intent, 1) ;
                break ;
            case R.id.ll_agent:
                if(agentModel.Buyer!=null){
//                    Intent userIntent = new Intent(v.getContext() , UserInfoActivity.class)  ;
//                    userIntent.putExtra(UserInfoActivity.EXTRA_USER_ID, agentModel.Buyer.UserID) ;
//                    v.getContext().startActivity(userIntent) ;
                }
                break ;
        }
    }
    
}
