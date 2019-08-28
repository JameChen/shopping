package com.nahuo.quicksale.orderdetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.BaseActivity2;
import com.nahuo.quicksale.DialogChooseExpress;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.OrderDetailItemAdapter;
import com.nahuo.quicksale.adapter.OrderDetailShipInfoAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.orderdetail.model.ExpressModel;
import com.nahuo.quicksale.orderdetail.model.OrderShipModel;
import com.nahuo.quicksale.orderdetail.model.OrderShipModel;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;

/***
 * 物流信息
 * created by 陈智勇   2015-4-30  下午3:48:43
 */
public class ShipActivity extends BaseActivity2{

    private int orderID ; 
    private OrderShipModel model ; 
    private ImageView icon;
    private TextView        name, info,money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("物流信息") ; 
        setContentView(R.layout.activity_ship_info) ; 
        mLoadingDialog = new LoadingDialog(this) ; 
        orderID = getIntent().getIntExtra(BaseOrderDetailActivity.EXTRA_ORDER_ID, 0) ; 
        initView() ; 
        initData()  ; 
        EventBus.getDefault().register(this) ;
    }

    private void initData() {
        mLoadingDialog.start() ; 
        HttpRequest request = mRequestHelper.getRequest(this, "pinhuoBuyer/GetShipInfo", this) ;
        request.setConvert2Class(OrderShipModel.class) ; 
        request.addParam("orderId", String.valueOf(orderID)).doPost() ; 
    }
    public void onEventMainThread(BusEvent event) {
        switch(event.id){
            case EventBusId.SEND_GOOD :
                initData() ; 
                break ;
        }
    }
    private void initView() {
        icon = (ImageView)findViewById(R.id.img_order_detail_icon);
        name = (TextView)findViewById(R.id.txt_order_detail_name);
        info = (TextView)findViewById(R.id.txt_order_detail_info);
        money = (TextView)findViewById(R.id.txt_order_detail_money);
        
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    void viewBindData(){
        String imageUrl = model.getCover() ;
        imageUrl = ImageUrlExtends.getImageUrl(imageUrl, Const.LIST_ITEM_SIZE);
        Picasso.with(this).load(imageUrl).placeholder(R.drawable.empty_photo).into(icon);
        name.setText(model.getItemName());
        info.setText("共"+ model.getTotalProdCount() + "件");
        money.setText(this.getResources().getString(R.string.rmb_x , model.getPayableAmount()));

        ListView listView = (ListView)findViewById(R.id.lst_order_info_expressinfo);
        final OrderDetailShipInfoAdapter adapter = new OrderDetailShipInfoAdapter() ;
        listView.setAdapter(adapter) ;
        adapter.setDatas(model.getShipDetails()) ;

    }
    @Override
    public void onRequestSuccess(String method, Object object) {
        model = (OrderShipModel)object ; 
        viewBindData() ; 
        super.onRequestSuccess(method, object);
    }

    
}
