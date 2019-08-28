package com.nahuo.quicksale;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.NoScrollListView;
import com.nahuo.quicksale.adapter.DeliverGoodsAdapter;
import com.nahuo.quicksale.adapter.FreightListAdapter;
import com.nahuo.quicksale.adapter.OrderAPI;
import com.nahuo.quicksale.adapter.PlaceAnOrderAdapter;
import com.nahuo.quicksale.oldermodel.OrdersModel;
import com.nahuo.quicksale.util.AKUtil;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by ALAN on 2017/4/1 0001.
 * 下单记录
 */
public class BillDetailActivity extends BaseSlideBackActivity implements View.OnClickListener{
    private static final String TAG=BillDetailActivity.class.getSimpleName();
    private NoScrollListView listView,listView2;
    private BillDetailActivity vthis=this;
    private TextView tvTitle;
    private Button btnLeft;
    private String BillId;
    private List<OrdersModel.OrdersItem> orderList;
    private List<OrdersModel.ShipedsItem> shipedsList;
    private DeliverGoodsAdapter orderAdapter;
    private PlaceAnOrderAdapter placeAdapter;
    private TextView accountTime, freightPaid1,ReallyPaid,freightPaid2,freightPaid3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_bill_details);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
         initView();
    }
    private void initView(){
        listView=(NoScrollListView)findViewById(R.id.bill_details_list1);
        listView2=(NoScrollListView)findViewById(R.id.bill_details_list2);
        // 标题栏
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        tvTitle.setText("账单详情");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        BillId=vthis.getIntent().getStringExtra(FreightListAdapter.FREIGHTLISTADAPTER_BILLID);
        accountTime=(TextView) findViewById(R.id.bill_desc_account_time);
        initTitle(findViewById(R.id.include_head1),findViewById(R.id.include_head2));
        initAdapter();

        initData();
    }
    private void initTitle(View include1,View include2){
        freightPaid1=(TextView) include1.findViewById(R.id.include_freight_paid_tv);
        ReallyPaid=(TextView) include1.findViewById(R.id.include_really_paid_tv);
        freightPaid2=(TextView) include2.findViewById(R.id.include_freight_paid_tv);
        freightPaid3=(TextView) include2.findViewById(R.id.include_really_paid_tv);
    }
    private void initAdapter(){
        //请记住两个集合反了
        orderList=new ArrayList<OrdersModel.OrdersItem>();
        shipedsList=new ArrayList<OrdersModel.ShipedsItem>();
        orderAdapter=new DeliverGoodsAdapter(shipedsList,vthis);
        listView.setAdapter(orderAdapter);
        placeAdapter=new PlaceAnOrderAdapter(orderList,vthis);
        listView2.setAdapter(placeAdapter);
    }
    private void initData(){
        new PlaceOrderTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                vthis.finish();
                break;
        }
    }

    //下单task
    private class PlaceOrderTask extends AsyncTask<Void,Void,String>{

        OrdersModel order;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                order=OrderAPI.getInstance().getOrder(vthis,BillId);
                orderList=order.getOrders();
                shipedsList=order.getShipeds();
                Log.v(TAG,"orderList:size"+orderList.size()+",,shipedsList：size"+shipedsList.size());
                return "OK";
            }catch (Exception ex){
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.equals("OK")) {
                // 验证result
                if (result.startsWith("401")
                        || result.startsWith("not_registered")) {
                    Toast.makeText(vthis, result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(vthis, result, Toast.LENGTH_LONG).show();
                }
            }else{
                if(orderList!=null&&orderList.size()>0){
                    placeAdapter.mList=orderList;
                    placeAdapter.notifyDataSetChanged();
                }
                if(shipedsList!=null&&shipedsList.size()>0){
                     orderAdapter.mList=shipedsList;
                     orderAdapter.notifyDataSetChanged();
                }
                if(order!=null){
                   accountTime.setText("结算时间:"+order.getFromTime()+"-"+order.getToTime());
                   freightPaid1.setText("已付运费:"+order.getPayablePostFee());
                   ReallyPaid.setText("实际运费:"+order.getActualPostFee());
                   freightPaid2.setText(Html.fromHtml("<font>运费结余:</font><font color='"+  ContextCompat.getColor(BillDetailActivity.this,AKUtil.returnchangeTextColor(order.getBalancePostFee()))+"'>"+order.getBalancePostFee()+"</font>"));
                   freightPaid3.setText(Html.fromHtml("<font>结算状态:</font><font color='#C60A1E'>"+order.getStatu()+"</font>"));
                }
            }


        }
    }

}
