package com.nahuo.quicksale;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.activity.OrderDetailsActivity;
import com.nahuo.quicksale.api.TradeAPI;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.TradeLogItem;
import com.nahuo.quicksale.util.AKUtil;
import com.nahuo.quicksale.util.RxUtil;

/**
 * Created by ALAN on 2017/4/5 0005.
 * 交易详情
 */
public class TradingDetailsActivity extends BaseSlideBackActivity implements View.OnClickListener {
    private static final String TAG = TradingDetailsActivity.class.getSimpleName();
    private TextView tvTitle;
    private Button btnLeft;
    private TradingDetailsActivity vthis = this;
    private TextView typeTv, typeDescTv, OrderNumber, OrderNumberItem, explainTv, content, createTime, createTimeItem,tv_orderList_desc;
    private TradeLogItem.TradeList item;
    private TextView money;
    private ImageView image;
    private LoadingDialog mLoadingDialog;
    public static final String ORDERID = "ORDERID";
    public static final String SETTLEAMOUNT = "SETTLEAMOUNT";
    public static final String ORDERID_PIN_HUO_ID = "ORDERID_PIN_HUO_ID";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_NEWDETAIL = 1;
    private String name = "";
    private WebView orderListView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.layout_trade_detail);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        if (getIntent() != null) {
            name = getIntent().getStringExtra(TradeLogActivity.EXTRA_TRADE_NAME);
        }
        initView();
    }

    private void initView() {
        mLoadingDialog = new LoadingDialog(vthis);
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        if (TextUtils.isEmpty(name))
            tvTitle.setText("交易详情");
        else
            tvTitle.setText(name + "详情");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        money = (TextView) findViewById(R.id.trade_details_money);
        image = (ImageView) findViewById(R.id.trade_details_image);
        //orderListView=findViewById(R.id.trade_include5);
        orderListView=findViewById(R.id.webView);
        orderListView.setDrawingCacheEnabled(false);
        orderListView.getSettings().setLoadWithOverviewMode(true);
        orderListView.getSettings().setJavaScriptEnabled(true);
        orderListView.setBackgroundColor(Color.parseColor("#00000000"));
        orderListView.setBackgroundResource(R.drawable.white_left);

        initIncludeView(findViewById(R.id.trade_include1),
                findViewById(R.id.trade_include2),
                findViewById(R.id.trade_include3),
                findViewById(R.id.trade_include4),null);
    }

    private void initIncludeView(View include1, View include2, View include3, View include4,View include5) {
        typeTv = (TextView) include1.findViewById(R.id.trade_type_tv);
        typeDescTv = (TextView) include1.findViewById(R.id.trade_type_tv2);

        OrderNumber = (TextView) include2.findViewById(R.id.trade_type_tv);
        OrderNumberItem = (TextView) include2.findViewById(R.id.trade_type_tv2);
        OrderNumberItem.setTextColor(ContextCompat.getColor(this,R.color.bule_overlay));
        OrderNumberItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TradingDetailsActivity.this, OrderDetailsActivity.class);
                intent.putExtra(OrderDetailsActivity.EXTRA_ORDER_CODE, orderNo);
                startActivity(intent);
            }
        });
        explainTv = (TextView) include3.findViewById(R.id.trade_type_tv);
        content = (TextView) include3.findViewById(R.id.trade_type_tv2);
        //content.setGravity(Gravity.LEFT);
        createTime = (TextView) include4.findViewById(R.id.trade_type_tv);
        createTimeItem = (TextView) include4.findViewById(R.id.trade_type_tv2);
        tv_orderList_desc=findViewById(R.id.trade_desc_tv);
        tv_orderList_desc.setVisibility(View.GONE);
//        tv_orderList_desc.setText("历史记录：");
       // tv_orderList_desc=include5.findViewById(R.id.trade_type_tv2);
        initData(include2);

    }

    private void initData(View include2) {
        Intent intent = this.getIntent();
        item = (TradeLogItem.TradeList) intent.getSerializableExtra(TradeLogActivity.TO_TRADDETALIL_BUNDLE);
        typeTv.setText("类型:");
        OrderNumber.setText("订单号:");
        explainTv.setText("描述说明:");
        createTime.setText("创建时间:");
        if (item != null) {
            initData(item, include2);
        } else {
            int type = vthis.getIntent().getIntExtra(TradingDetailsActivity.EXTRA_TYPE, 0);
            if (type == TradingDetailsActivity.TYPE_NEWDETAIL) {
                int h = vthis.getIntent().getIntExtra(ORDERID, 0);
                double settleamount = vthis.getIntent().getDoubleExtra(TradingDetailsActivity.SETTLEAMOUNT, 0);
                getRefundDetails(vthis, h, settleamount, include2);
            } else {
                new LoadDataTask(include2).execute();
            }
        }
    }

    public void getRefundDetails(final Context context, final int oid, double settleAmount, final View include2) {
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi("getOrderItemForRefund")
                .GetTradeInfo4PostFee(oid, settleAmount).compose(RxUtil.<PinHuoResponse<TradeLogItem.TradeList>>rxSchedulerHelper())
                .compose(RxUtil.<TradeLogItem.TradeList>handleResult()).subscribeWith(new CommonSubscriber<TradeLogItem.TradeList>(context, true, R.string.loading) {
                    @Override
                    public void onNext(final TradeLogItem.TradeList order) {
                        super.onNext(order);
                        if (order != null) {
                            initData(order, include2);
                        }
                    }
                }));
    }
    String orderNo="";
    private void initData(TradeLogItem.TradeList item, View include2) {
        if (item != null) {
                typeDescTv.setText(item.getTradeTypeName());
                 orderNo = item.getOrderCode();
                if (!TextUtils.isEmpty(orderNo)) {
                    include2.setVisibility(View.VISIBLE);
                    OrderNumberItem.setText(orderNo+"(点击查看详情)");
                } else {
                    include2.setVisibility(View.GONE);
                }
                content.setText(AKUtil.getTextToSpan(item.getTradeDesc()));
                createTimeItem.setText(AKUtil.getTextToSpan(item.getDate()));
                money.setText(AKUtil.getTextToSpan(item.getAmount() + ""));
                AKUtil.setTypeResource(item.getTradeTypeName(), item.getType(), image);
            if (!ListUtils.isEmpty(item.getcOrderLists())){
                tv_orderList_desc.setVisibility(View.VISIBLE);
                orderListView.setVisibility(View.VISIBLE);
                StringBuilder sb=new StringBuilder();
                sb.append("<!doctype html><html><body>" +
                        "<table border=\"1\">\n" +
                        "  <tr>\n" +
                        "    <th>商品</th>\n" +
                        "    <th>金额</th>\n" +
                        "  </tr>");
                for (TradeLogItem.TradeList.COrderList cOrderList:item.getcOrderLists()) {
                    sb.append("<tr><td align=\"center\">");
                    sb.append(cOrderList.getDesc());
                    sb.append("</td>");
                    sb.append("<td align=\"center\">&nbsp;&nbsp;");
                    sb.append(cOrderList.getAmount());
                    sb.append("&nbsp;&nbsp;</td></tr>");
                }
                    sb.append("</table> </body></html>");
                if (orderListView!=null) {
                    orderListView.loadData(sb.toString(), "text/html; charset=UTF-8", null);
                }
            }else {
                tv_orderList_desc.setVisibility(View.GONE);
                orderListView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orderListView!=null){
            orderListView.clearHistory();
            orderListView.clearCache(true);
            orderListView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            orderListView.freeMemory();
            orderListView.pauseTimers();
            orderListView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                vthis.finish();
                break;
        }
    }


    public class LoadDataTask extends AsyncTask<Void, Void, String> {
        private View include2;

        private TradeLogItem.TradeList record;

        public LoadDataTask(View include2) {
            this.include2 = include2;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog.start(getString(R.string.items_loadData_loading));
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingDialog.stop();
            if (!result.equals("OK")) {
                // 验证result
                if (result.startsWith("401")
                        || result.startsWith("not_registered")) {

                } else {
                    ViewHub.showOkDialog(vthis, "提示", result.toString(), "返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int which) {
                            finish();
                        }
                    });
                }
            } else {
                if (record != null) {
                    initData(record, include2);
                }
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            String json;
            try {
                int h = vthis.getIntent().getIntExtra(ORDERID, 0);
                record = TradeAPI.getInstance().getTradeDesc(vthis, h);
                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "获取交易详情异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }
    }
}
