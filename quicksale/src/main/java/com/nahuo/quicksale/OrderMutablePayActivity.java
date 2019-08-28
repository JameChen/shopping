package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListViewEx;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.adapter.OrderAdapter;
import com.nahuo.quicksale.adapter.ShopcartAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.RequestMethod.OrderMethod;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const.OrderStatus;
import com.nahuo.quicksale.common.Const.OrderType;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.OrderModel;
import com.nahuo.quicksale.oldermodel.ResultData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @description 订单管理
 * @created 2015-4-3 上午10:40:18
 * @author ZZB
 */
public class OrderMutablePayActivity extends BaseAppCompatActivity implements HttpRequestListener, View.OnClickListener,
        OnRefreshListener, OnLoadMoreListener, OnGroupClickListener,ShopcartAdapter.TotalPriceChangedListener {

    private Context                   mContext           = this;
    private PullToRefExpandListViewEx mListView;
    private OrderAdapter              mAdapter;
    private static final int          PAGE_SIZE          = 20;
    private static int                PAGE_INDEX         = 1;
    private boolean                   mIsLoading;
    private boolean                   mIsRefresh;
    private HttpRequestHelper         mHttpRequestHelper = new HttpRequestHelper();
    private EventBus                  mEventBus          = EventBus.getDefault();
    private TextView               mAllAmount;
    public static final String EXTRA_QSID = "EXTRA_QSID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    private int qsid;
    private String title;
    private TextView                  mETTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_mutable_pay);
        mEventBus.register(this);
        Intent intent=getIntent();
        qsid = intent.getIntExtra(EXTRA_QSID, 0);
        title = intent.getStringExtra(EXTRA_TITLE);
        initView();
        mListView.pull2RefreshManually();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEventBus.unregister(this);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.SEND_GOOD:// 发货
            case EventBusId.SURE_GET_GOOD:// 确认收货
            case EventBusId.CHANGE_PRICE:// 改价
            case EventBusId.REFUND_BUYER_AGRESS:// 买家退货
            case EventBusId.REFUND_SELLER_AGRESS:// 卖家退货
            case EventBusId.REFUND_SUPP_AGRESS:// 供货商退货
            case EventBusId.CANCEL_ORDER:// 取消订单
                mListView.pull2RefreshManually();
                break;
            case EventBusId.REFRESH_ORDER_MANAGER:  //jpush收到新订单，刷新列表
                mListView.pull2RefreshManually();
                break ;
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();

    }

    private void loadData(boolean isRefresh) {
        if (!mIsLoading) {
//            mListView.setCanLoadMore(true);
            mIsRefresh = isRefresh;
            PAGE_INDEX = isRefresh ? 1 : ++PAGE_INDEX;
            OrderAPI.getOrders(this, mHttpRequestHelper, this, "", OrderStatus.WAIT_PAY, PAGE_INDEX, PAGE_SIZE);
        }
    }

    private void onDataLoaded(Object object) {
        JSONObject jo;
        List<OrderModel> items = new ArrayList<OrderModel>();
        try {
            jo = new JSONObject((String) object);
            items = GsonHelper.jsonToObject(jo.get("OrderList").toString(), new TypeToken<List<OrderModel>>() {
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mIsRefresh) {
            mAdapter.setData(items);
            mListView.setCanLoadMore(false);//禁用下一页，后台说一次性读出来
        } else {
            if (items.size() < PAGE_SIZE) {
                mListView.setCanLoadMore(false);
            }
            mAdapter.addData(items);
        }

        mAdapter.notifyDataSetChanged();
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
    }

    @Override
    public void onRequestStart(String method) {
        if (ShopMethod.ORDER_LIST.equals(method)) {
            mIsLoading = true;
        }

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (ShopMethod.ORDER_LIST.equals(method)) {// 订单加载完成
            finishLoading();
            onDataLoaded(object);
        } else if (OrderMethod.GET_REFUND_COUNT.equals(method)) {
//            OrderRefundCount count = (OrderRefundCount)object;
//            int totalCount = count.getTotalCount();
//            boolean hasRefund = totalCount > 0;

        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        if (ShopMethod.ORDER_LIST.equals(method)) {// 订单加载完成
            finishLoading();
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (ShopMethod.ORDER_LIST.equals(method)) {// 订单加载完成
            finishLoading();
        }
    }

    @Override
    public void shopcartItemCountChanged(int groupPs, int childPs, int num, int preCount) {
    }

    @Override
    public void totalPriceChanged(double totalPrice) {
        double money = 0.00;
        List<OrderModel> datas = mAdapter.getData();
        for (OrderModel item : datas)
        {
            if (item.isSelect)
            {
                money += item.getPrice();
            }
        }
        mAllAmount.setText(String.format("¥ %.2f",money));
    }

    private void initView() {
        mETTitle = (TextView)findViewById(R.id.title_name);
        mETTitle.setText(title);
        mAllAmount = (TextView)findViewById(R.id.all_amount);
        findViewById(R.id.batch_pay_btn).setOnClickListener(this);
        findViewById(R.id.checkbox_allcheck).setOnClickListener(this);

        mListView = (PullToRefExpandListViewEx)findViewById(R.id.lv_pull_refresh);
        mListView.setEmptyViewText("暂时没有相关订单哦~");
        mListView.setOnGroupClickListener(this);
        mListView.setCanLoadMore(false);//禁用下一页，后台说一次性读出来
        mListView.setCanRefresh(true);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mAdapter = new OrderAdapter(this);
        mAdapter.mCurOrderStatu = OrderStatus.WAIT_PAY;
        mAdapter.setTotalPriceChangedListener(this);
        mListView.setAdapter(mAdapter);

    }

    private void finishLoading() {
        mListView.onRefreshComplete();
        mIsLoading = false;
    }

    private void search() {
        ViewHub.hideKeyboard(this);
        mListView.pull2RefreshManually();
    }

    public static String listToString(List<String> stringList){
        if (stringList==null) {
            return null;
        }
        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.checkbox_allcheck://全选]
                CheckBox cb = (CheckBox)findViewById(R.id.checkbox_allcheck);
                mAdapter.setCheckAll(cb.isChecked());
                break;
            case R.id.batch_pay_btn://批量支付
                String orderids = "";
                double money = 0.00;
                for (OrderModel item : mAdapter.getData())
                {
                    if (item.isSelect)
                    {
                        orderids += "," + item.getOrderId();
                        money += item.getPrice();
                    }
                }
                if (orderids.length()>0)
                {
                    orderids = orderids.substring(1);
                }
                else
                {
                    ViewHub.showLongToast(mContext,"请选择批量付款的订单");
                    return;
                }
                List<String> orderidsArr = Arrays.asList(orderids.split(","));
                Comparator comp = new Comparator() {
                    public int compare(Object o1, Object o2) {
                        int p1 = Integer.valueOf((String) o1);
                        int p2 = Integer.valueOf((String) o2);
                        if (p1 < p2)
                            return -1;
                        else if (p1 == p2)
                            return 0;
                        else if (p1 > p2)
                            return 1;
                        return 0;
                    }
                };
                Collections.sort(orderidsArr, comp);
                Intent it = new Intent(mContext, OrderPayActivity.class);
                it.putExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, listToString(orderidsArr));
                it.putExtra(OrderPayActivity.INTENT_PAY_MONEY, money);
                mContext.startActivity(it);
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    public void reload() {
        //解决数据没清空，代理单子订单有可能出现重复的bug
        mAdapter.setData(new ArrayList<OrderModel>());
        mAdapter.notifyDataSetChanged();
        mAdapter.mCurOrderType = OrderType.BUY;
        mAdapter.mCurOrderStatu = OrderStatus.WAIT_PAY;
        mAdapter.qsid = qsid;
        mAdapter.title = title;
        mListView.showLoadingView();
        mListView.pull2RefreshManually();
        findViewById(R.id.inputArea).setVisibility(View.GONE);

    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void onLoadMore() {
        loadData(false);

    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}