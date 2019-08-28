//package com.nahuo.quicksale;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.baidu.mobstat.StatService;
//import com.google.gson.reflect.TypeToken;
//import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
//import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
//import com.nahuo.library.helper.GsonHelper;
//import com.nahuo.quicksale.adapter.OrderActivityAdapter;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.HttpRequestListener;
//import com.nahuo.quicksale.api.OrderAPI;
//import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.model.OrderActivityModel;
//import com.nahuo.quicksale.model.ResultData;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//
///**
// * @description 订单管理
// * @created 2015-4-3 上午10:40:18
// * @author ZZB
// */
//public class OrderActivityListActivity extends BaseSlideBackActivity implements HttpRequestListener, View.OnClickListener,
//        PullToRefreshListView.OnRefreshListener, PullToRefreshListView.OnLoadMoreListener {
//
//    private Context                   mContext           = this;
//    private PullToRefreshListViewEx mListView;
//    private OrderActivityAdapter              mAdapter;
//    private static final int          PAGE_SIZE          = 20;
//    private static int                PAGE_INDEX         = 1;
//    private boolean                   mIsLoading;
//    private boolean                   mIsRefresh;
//    private HttpRequestHelper         mHttpRequestHelper = new HttpRequestHelper();
//    private EventBus                  mEventBus          = EventBus.getDefault();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_order_activity_list);
//        mEventBus.register(this);
//        initView();
//        mListView.pull2RefreshManually();
//        OrderAPI.getActivityOrders(this, mHttpRequestHelper, this, PAGE_INDEX, PAGE_SIZE);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mEventBus.unregister(this);
//    }
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//           default:
//                break ;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//            super.onBackPressed();
//
//    }
//
//    private void loadData(boolean isRefresh) {
//        if (!mIsLoading) {
//            mListView.setCanLoadMore(true);
//            mIsRefresh = isRefresh;
//            PAGE_INDEX = isRefresh ? 1 : ++PAGE_INDEX;
//            OrderAPI.getActivityOrders(this, mHttpRequestHelper, this, PAGE_INDEX, PAGE_SIZE);
//        }
//    }
//
//    private void onDataLoaded(Object object) {
//        List<OrderActivityModel> items = GsonHelper.jsonToObject(String.valueOf(object), new TypeToken<List<OrderActivityModel>>() {});// new
//                                                                                                          // ArrayList<OrderActivityModel>();//
//        if (mIsRefresh) {
//            mAdapter.mList = items;
//        } else {
//            if (items.size() < PAGE_SIZE) {
//                mListView.setCanLoadMore(false);
//            }
//            mAdapter.mList.addAll(items);
//        }
//
//        mAdapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onRequestStart(String method) {
//        if (ShopMethod.ORDER_ACTIVITY_LIST.equals(method)) {
//            mIsLoading = true;
//        }
//
//    }
//
//    @Override
//    public void onRequestSuccess(String method, Object object) {
//        if (ShopMethod.ORDER_ACTIVITY_LIST.equals(method)) {// 订单加载完成
//            finishLoading();
//            onDataLoaded(object);
//        }
//    }
//
//    @Override
//    public void onRequestFail(String method, int statusCode, String msg) {
//        if (ShopMethod.ORDER_ACTIVITY_LIST.equals(method)) {// 订单加载完成
//            finishLoading();
//        }
//    }
//
//    @Override
//    public void onRequestExp(String method, String msg, ResultData data) {
//        if (ShopMethod.ORDER_ACTIVITY_LIST.equals(method)) {// 订单加载完成
//            finishLoading();
//        }
//    }
//
//    private void initView() {
//
//        ((TextView) findViewById(R.id.tv_title)).setText("订单管理");
//        Button backBtn = (Button) findViewById(R.id.titlebar_btnLeft);
//        backBtn.setVisibility(View.VISIBLE);
//        backBtn.setOnClickListener(this);
//
//        mListView = (PullToRefreshListViewEx)findViewById(R.id.lv_pull_refresh);
//        mListView.setCanLoadMore(true);
//        mListView.setCanRefresh(true);
//        mListView.setMoveToFirstItemAfterRefresh(true);
//        mListView.setOnRefreshListener(this);
//        mListView.setOnLoadListener(this);
//        mListView.setEmptyViewText("未找到订单购买记录");
//        List<OrderActivityModel> items = new ArrayList<OrderActivityModel>();
//        mAdapter = new OrderActivityAdapter(mContext,items);
//        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    position = 1;
//                }
//                    OrderActivityModel model = mAdapter.getItem(position-1);
//                    Intent intent = new Intent(mContext, OrderManageActivity.class);
//                    intent.putExtra(OrderManageActivity.EXTRA_QSID, model.getQSID());
//                    intent.putExtra(OrderManageActivity.EXTRA_TITLE, model.getName());
//                    startActivity(intent);
//            }
//        });
//
//    }
//
//    private void finishLoading() {
//        mListView.onRefreshComplete();
//        mIsLoading = false;
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.titlebar_btnLeft:
//                onBackPressed();
//                break;
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        loadData(true);
//    }
//
//    @Override
//    public void onLoadMore() {
//        loadData(false);
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        StatService.onPause(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        StatService.onResume(this);
//    }
//}
