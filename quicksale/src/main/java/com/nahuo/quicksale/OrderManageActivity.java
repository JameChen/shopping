package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.mobstat.StatService;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nahuo.bean.NoticeBean;
import com.nahuo.bean.OrderBean;
import com.nahuo.constant.Constant;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListView.OnRefreshListener;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.activity.OrderDetailsActivity;
import com.nahuo.quicksale.adapter.OrderNewAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.OrderStatus;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.RxUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author ZZB
 * @description 订单管理
 * @created 2015-4-3 上午10:40:18
 */
public class OrderManageActivity extends BaseAppCompatActivity implements HttpRequestListener, View.OnClickListener,
        OnRefreshListener, OnLoadMoreListener, OnGroupClickListener {
    private Context mContext = this;
    //private PullToRefExpandListViewEx mListView;
    //private OrderAdapter mAdapter;

    private OrderNewAdapter adapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tv_order_notify, tv_empty_text;
    private LoadingDialog mLoadingDialog;


    private static final int PAGE_SIZE = 20;
    private static int PAGE_INDEX = 1;
    private boolean mIsLoading;
    private boolean mIsRefresh = true;
    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();
    private TextView mETTitle, txt1, txtTag;//, txt2, txt3, txt4, txt5, txt6;
    //    private View txtLine;
    private EventBus mEventBus = EventBus.getDefault();
    public static final String EXTRA_QSID = "EXTRA_QSID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_ORDER_TYPE = "EXTRA_ORDER_TYPE";
    private int mCurOrderStatu = OrderStatus.ALL_PAY;        // 订单状态
    private int qsid;
    private String title;
    private String FooterHTML;
    private String FooterTag;
    private EditText mEtSearch;
    private RadioGroup mRadioGroup;
    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private OrderManageActivity Vthis;
    private static  String TAG=OrderManageActivity.class.getSimpleName();
    private View layout_order_notify;
    private TextView tvOrderNotify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_manage);
        Vthis=this;
        mEventBus.register(this);
        BWApplication.getInstance().registerActivity(this);
        BWApplication.addActivity(this);
        Intent intent = getIntent();
        qsid = intent.getIntExtra(EXTRA_QSID, 0);
        title = intent.getStringExtra(EXTRA_TITLE);
        if (title == null) {
            title = "";
        }
        if (title.length() <= 0) {
            title = "订单管理";
        }
        mCurOrderStatu = intent.getIntExtra(EXTRA_ORDER_TYPE, OrderStatus.ALL_PAY);
        initView();
        // mListView.pull2RefreshManually();
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
            case EventBusId.CHANGE_NUMBER:
                //   mListView.pull2RefreshManually();
                onRefreshData();
                break;
            case EventBusId.REFRESH_ORDER_MANAGER:  //jpush收到新订单，刷新列表
                mCurOrderStatu = OrderStatus.WAIT_PAY;
                onRefreshData();
                //  mListView.pull2RefreshManually();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isEtSearchShowing()) {
            ViewHub.hideKeyboard(this);
            mEtSearch.setVisibility(View.GONE);
            mETTitle.setVisibility(View.VISIBLE);
            mEtSearch.setText("");
        } else {
            super.onBackPressed();
        }
    }

    private boolean isEtSearchShowing() {
        return mEtSearch.getVisibility() == View.VISIBLE;
    }

    private void loadData(boolean isRefresh) {
        if (!mIsLoading) {
            enableRadioButtons(false);
            // mListView.setCanLoadMore(true);
            mIsRefresh = isRefresh;
            PAGE_INDEX = isRefresh ? 1 : ++PAGE_INDEX;
            String keyword = mEtSearch.getText().toString();
            OrderAPI.getOrders(this, mHttpRequestHelper, this, keyword, mCurOrderStatu, PAGE_INDEX, PAGE_SIZE);
            if (mIsRefresh)
            getCurrentNotice();
            //OrderAPI.getOrderFootInfo(this, mHttpRequestHelper, this, mCurOrderStatu);
        }
    }

    /**
     * @description 订单状态分类是否可点
     * @created 2015-4-29 上午9:35:01
     * @author ZZB
     */
    public void enableRadioButtons(boolean clickable) {
        int count = mRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            mRadioGroup.getChildAt(i).setClickable(clickable);
        }
    }
    private void getCurrentNotice() {
        int id=com.nahuo.constant.Constant.NotifyAreaID.NotifyAreaID_OrderManager;
        switch (mCurOrderStatu) {
            case OrderStatus.ALL_PAY:
                id= Constant.NotifyAreaID.NotifyAreaID_OrderManager_All;
                break;
            case OrderStatus.WAIT_PAY:
                id= Constant.NotifyAreaID.NotifyAreaID_OrderManager_Wait_Pay;
                break;
            case OrderStatus.WAIT_SHIP:
                id= Constant.NotifyAreaID.NotifyAreaID_OrderManager_Wait_Send;
                break;
            case OrderStatus.SHIPED:
                id= Constant.NotifyAreaID.NotifyAreaID_OrderManager_Wait_Receive;
                break;
            case OrderStatus.DONE:
                id= Constant.NotifyAreaID.NotifyAreaID_OrderManager_Done;
                break;
            case OrderStatus.CANCEL:
                id= Constant.NotifyAreaID.NotifyAreaID_OrderManager_Close;
                break;
        }
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getCurrentNotice(id)
                .compose(RxUtil.<PinHuoResponse<List<NoticeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<NoticeBean>>handleResult())
                .subscribeWith(new CommonSubscriber<List<NoticeBean>>(Vthis) {
                    @Override
                    public void onNext(List<NoticeBean> noticeBeen) {
                        super.onNext(noticeBeen);
                        if (!ListUtils.isEmpty(noticeBeen)) {
                            final NoticeBean bean=noticeBeen.get(0);
                            layout_order_notify.setVisibility(View.VISIBLE);
                            tvOrderNotify.setText(bean.getTitle());
                            layout_order_notify.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String target = bean.getTarget();
                                    if (!TextUtils.isEmpty(target)) {
                                        String url = Const.getShopLogo(SpManager.getUserId(BWApplication.getInstance()));
                                        switch (target){
                                            case "activity":
                                                Intent intent = new Intent(Vthis, PostDetailActivity.class);
                                                intent.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                                                intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                                        url);
                                                intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                                                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                                        Const.PostType.ACTIVITY);
                                                Vthis.startActivity(intent);
                                                break;
                                            case "topic":
                                                Intent intent1 = new Intent(Vthis, PostDetailActivity.class);
                                                intent1.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                                                intent1.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                                        url);
                                                intent1.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                                                intent1.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                                        Const.PostType.TOPIC);
                                                Vthis.startActivity(intent1);
                                                break;
                                            case "schedule":
                                                ActivityUtil.goToChangCiActivity(Vthis,bean.getTargetID());
                                                break;
                                            case "goods":
                                                ActivityUtil.goToItemDtailActivity(Vthis,bean.getTargetID());
                                                break;
                                        }

                                    }
                                }
                            });
                        }else {
                            layout_order_notify.setVisibility(View.GONE);
                        }

                    }
                }));
    }
    private void onDataLoaded(Object object) {
        try {
            if (mIsRefresh) {
                adapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
            OrderBean orderBean = (OrderBean) object;
            if (orderBean != null) {
                final int size = ListUtils.isEmpty(orderBean.getOrderList()) ? 0 : orderBean.getOrderList().size();
                if (mIsRefresh) {
                    adapter.setNewData(null);
//                    adapter.removeAllHeaderView();
//                    if (!TextUtils.isEmpty(orderBean.getNotice())) {
//                        addHeadView(orderBean);
//                    }
                    if (ListUtils.isEmpty(orderBean.getOrderList())) {
                        tv_empty_text.setVisibility(View.VISIBLE);
                    } else {
                        tv_empty_text.setVisibility(View.GONE);
                        adapter.setNewData(orderBean.getOrderList());
                    }
                } else {
                    if (ListUtils.isEmpty(orderBean.getOrderList())) {

                    } else {
                        adapter.addData(orderBean.getOrderList());
                    }
                }
                if (size < PAGE_SIZE) {
                    //第一页如果不够一页就不显示没有更多数据布局
                    adapter.loadMoreEnd(mIsRefresh);
                } else {
                    adapter.loadMoreComplete();
                }
            } else {
                if (mIsRefresh) {
                    adapter.setNewData(null);
                }
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        JSONObject jo;
//        List<OrderModel> items = new ArrayList<OrderModel>();
//        try {
//            jo = new JSONObject((String) object);
//            items = GsonHelper.jsonToObject(jo.get("OrderList").toString(), new TypeToken<List<OrderModel>>() {
//            });
//
//            OrderAPI.getOrderRecord(this, mHttpRequestHelper, this, items);
//
//            if (mIsRefresh) {
////                if (jo.has("FooterStr")) {
////                    FooterHTML = jo.getString("FooterStr");
////                    FooterTag = jo.getString("Tag");
////                }
//                if (ListUtils.isEmpty(items)) {
//                    findViewById(R.id.inputArea).setVisibility(View.GONE);
//                } else {
//                    findViewById(R.id.inputArea).setVisibility(View.VISIBLE);
//                    if (FooterHTML != null && FooterHTML.length() > 0) {
//                        findViewById(R.id.inputArea).setVisibility(View.VISIBLE);
//                    } else {
//                        findViewById(R.id.inputArea).setVisibility(View.GONE);
//                    }
//                }
//                mAdapter.setData(items);
//            } else {
////            if (items.size() < PAGE_SIZE) {
////                mListView.setCanLoadMore(false);//禁用下一页，后台说一次性读出来
////            }
//                mAdapter.addData(items);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        mAdapter.notifyDataSetChanged();
//        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
//            mListView.expandGroup(i);
//        }
//        // initInputArea();
////        if (mAdapter.getGroupCount() <= 0) {
////            findViewById(R.id.inputArea).setVisibility(View.GONE);
////        } else {
////            findViewById(R.id.inputArea).setVisibility(View.VISIBLE);
////        }
//        if (mCurOrderStatu == OrderStatus.ALL_PAY)
//            findViewById(R.id.inputArea).setVisibility(View.GONE);
    }

//    private void onRecordLoaded(Object object) {
//        List<OrderItemRecordModel> records = (List<OrderItemRecordModel>) object;//GsonHelper.jsonToObject(String.valueOf(object), new TypeToken<List<OrderItemRecordModel>>() {});
//        Map<String, Integer> recordMap = new HashMap<String, Integer>();
//        Map<String, Integer> recordCountMap = new HashMap<String, Integer>();
//        for (OrderItemRecordModel record : records) {
//            recordCountMap.put(record.getAgentItemID() + "-" + record.getQsID(), record.getTotalRecordCount());
//            recordMap.put(record.getAgentItemID() + "-" + record.getQsID(), record.getQty());
//        }
//        List<OrderModel> orders = mAdapter.getData();
//        for (OrderModel om : orders) {
//            if (om.getOrderItems().size() > 0) {
//                om.getOrderItems().get(0).setRecordQty(
//                        recordMap.get(
//                                om.getOrderItems().get(0).getAgentItemID() + "-" + om.getQSID()
//                        )
//                );
//                om.getOrderItems().get(0).setRecordCountQty(
//                        recordCountMap.get(
//                                om.getOrderItems().get(0).getAgentItemID() + "-" + om.getQSID()
//                        )
//                );
//            }
//        }
//
//        mAdapter.setData(orders);
//        mAdapter.notifyDataSetChanged();
//    }

//    private void initInputArea() {
//        findViewById(R.id.inputArea).setVisibility(View.GONE);
//        if (mAdapter.getGroupCount() <= 0) {
//            findViewById(R.id.inputArea).setVisibility(View.GONE);
//        } else {
//            if (FooterHTML != null && FooterHTML.length() > 0) {
//                findViewById(R.id.inputArea).setVisibility(View.VISIBLE);
//            } else {
//                findViewById(R.id.inputArea).setVisibility(View.GONE);
//            }
//        }
//        txt1.setText(Html.fromHtml(FooterHTML));
//        txtTag.setVisibility(View.GONE);
//        if (FooterTag.equals("PostFeeLink")) {
//            txtTag.setText("运费账单");
//            txtTag.setVisibility(View.VISIBLE);
//        }
//        if (FooterTag.equals("AllotLink")) {
//            txtTag.setText(Html.fromHtml("配货清单" + "<br/><font color=\"#7d7d7e\">可设置发货方式</font>"));
//            txtTag.setVisibility(View.VISIBLE);
//        }
//        if (FooterTag.equals("PackageLink")) {
//            txtTag.setText("包裹清单");
//            txtTag.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public void onRequestStart(String method) {
        if (ShopMethod.ORDER_LIST.equals(method)) {
            mIsLoading = true;
            if (!isDialogShowing()) {
                mLoadingDialog.setMessage("正在获取订单列表数据...");
                mLoadingDialog.show();
            }
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        hideDialog();
        if (ShopMethod.ORDER_LIST.equals(method)) {// 订单加载完成
            finishLoading();
            onDataLoaded(object);
        }
//        else if (ShopMethod.GET_ORDER_RECORD.equals(method)) {// 订单留言
//           // onRecordLoaded(object);
//        } else if (OrderMethod.GET_REFUND_COUNT.equals(method)) {
////            OrderRefundCount count = (OrderRefundCount)object;
////            int totalCount = count.getTotalCount();
////            boolean hasRefund = totalCount > 0;
//
//        } else if (ShopMethod.ORDER_GETORDERFOOTINFO.equals(method)) {
//            Log.d("dd", object + "");
//            onFooterStr(object);
//        }
    }

//    public void onFooterStr(Object object) {
//        JSONObject jo;
//        try {
//            jo = new JSONObject((String) object);
//            if (jo.has("FooterStr")) {
//                FooterHTML = jo.getString("FooterStr");
//                FooterTag = jo.getString("Tag");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        if (mIsRefresh) {
//            initInputArea();
//        }
//
//    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        if (ShopMethod.ORDER_LIST.equals(method)) {// 订单加载完成
            hideDialog();
            finishLoadFail();
            finishLoading();
        }
    }

    private void finishLoadFail() {
        if (adapter != null) {
            if (mIsRefresh) {
                adapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                adapter.loadMoreFail();
            }
        }

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (ShopMethod.ORDER_LIST.equals(method)) {// 订单加载完成
            hideDialog();
            finishLoadFail();
            finishLoading();
        }
    }

    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
    int selectIndex = -1;

    private void initView() {
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        mETTitle = (TextView) findViewById(R.id.title_name);
        layout_order_notify=findViewById(R.id.layout_order_notify);
        tvOrderNotify= (TextView) findViewById(R.id.tv_order_notify);
        mETTitle.setText(title);
        mETTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FunctionHelper.isDoubleFastClick()) {
                    if (mRecyclerView != null)
                        mRecyclerView.scrollToPosition(0);
                }
            }
        });
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mEtSearch.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return true;
            }
        });
        txt1 = (TextView) findViewById(R.id.txt1);
        txtTag = (TextView) findViewById(R.id.txtTag);
        txtTag.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
//        txt2 = (TextView) findViewById(R.id.txt2);
//        txt3 = (TextView) findViewById(R.id.txt3);
//        txtLine = (View) findViewById(R.id.txtLine);
//        txt4 = (TextView) findViewById(R.id.txt4);
//        txt5 = (TextView) findViewById(R.id.txt5);
//        txt6 = (TextView) findViewById(R.id.txt6);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        switch (mCurOrderStatu) {
            case OrderStatus.ALL_PAY:
                selectIndex = 0;
                ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
                break;
            case OrderStatus.WAIT_PAY:
                selectIndex = 1;
                ((RadioButton) mRadioGroup.getChildAt(1)).setChecked(true);
                break;
            case OrderStatus.WAIT_SHIP:
                selectIndex = 2;
                ((RadioButton) mRadioGroup.getChildAt(2)).setChecked(true);
                break;
            case OrderStatus.SHIPED:
                selectIndex = 3;
                ((RadioButton) mRadioGroup.getChildAt(3)).setChecked(true);
                break;
            case OrderStatus.DONE:
                selectIndex = 4;
                ((RadioButton) mRadioGroup.getChildAt(4)).setChecked(true);
                break;
            case OrderStatus.CANCEL:
                selectIndex = 6;
                ((RadioButton) mRadioGroup.getChildAt(6)).setChecked(true);
                break;
            case OrderStatus.REFUND:
                selectIndex = 5;
                ((RadioButton) mRadioGroup.getChildAt(5)).setChecked(true);
                break;
            default:
                selectIndex = 0;
                ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCurrentCheckedRadioLeft = ((RadioButton) mRadioGroup.getChildAt(selectIndex)).getLeft();
                mHorizontalScrollView.scrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
            }
        }, 10);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                int radioButtonId = group.getCheckedRadioButtonId();
                //根据ID获取RadioButton的实例
                RadioButton rb = (RadioButton) findViewById(radioButtonId);
                mCurrentCheckedRadioLeft = rb.getLeft();//更新当前蓝色横条距离左边的距离
                mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
            }
        });
//        mListView = (PullToRefExpandListViewEx) findViewById(R.id.lv_pull_refresh);
//        mListView.setEmptyViewText("暂时没有相关订单哦~");
//        mListView.setOnGroupClickListener(this);
//        mListView.setCanLoadMore(true);
//        mListView.setCanRefresh(true);
//        mListView.setOnRefreshListener(this);
//        mListView.setOnLoadListener(this);
//        mAdapter = new OrderAdapter(this);
//        mAdapter.qsid = qsid;
//        mListView.setAdapter(mAdapter);

        tv_empty_text = (TextView) findViewById(R.id.tv_empty_text);
        mLoadingDialog = new LoadingDialog(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        // addHeadView();
        initRefreshLayout();
        mSwipeRefreshLayout.setRefreshing(true);
        loadData(true);
        //onRefreshData();

    }

    protected boolean isDialogShowing() {
        return (mLoadingDialog != null && mLoadingDialog.isShowing());
    }

    protected void hideDialog() {
        if (isDialogShowing()) {
            mLoadingDialog.stop();
        }
    }

    private void initRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
    }

    private void onRefreshData() {
        adapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        loadData(true);
    }

    private void onLoadDataMore() {
        loadData(false);
    }

    private void initAdapter() {
        adapter = new OrderNewAdapter(this);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                onLoadDataMore();
            }
        });
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        mAdapter.setPreLoadNumber(3);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    List<OrderBean.OrderListBean> list = adapter.getData();
                    Intent intent = new Intent(OrderManageActivity.this, OrderDetailsActivity.class);
                    intent.putExtra(OrderDetailsActivity.EXTRA_ORDER_ID, list.get(position).getID());
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void addHeadView(final NoticeBean bean ) {
        View headView = getLayoutInflater().inflate(R.layout.order_head, (ViewGroup) mRecyclerView.getParent(), false);
        tv_order_notify = (TextView) headView.findViewById(R.id.tv_order_notify);
        tv_order_notify.setText(bean.getTitle());
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String target = bean.getTarget();
                if (!TextUtils.isEmpty(target)) {
                    String url = Const.getShopLogo(SpManager.getUserId(BWApplication.getInstance()));
                    switch (target){
                        case "activity":
                            Intent intent = new Intent(Vthis, PostDetailActivity.class);
                            intent.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                            intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                    url);
                            intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                            intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                    Const.PostType.ACTIVITY);
                            Vthis.startActivity(intent);
                            break;
                        case "topic":
                            Intent intent1 = new Intent(Vthis, PostDetailActivity.class);
                            intent1.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                            intent1.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                    url);
                            intent1.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                            intent1.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                    Const.PostType.TOPIC);
                            Vthis.startActivity(intent1);
                            break;
                        case "schedule":
                            ActivityUtil.goToChangCiActivity(Vthis,bean.getTargetID());
                            break;
                        case "goods":
                            ActivityUtil.goToItemDtailActivity(Vthis,bean.getTargetID());
                            break;
                    }

                }
            }
        });
        if (adapter != null)
            adapter.addHeaderView(headView);
    }

    private void finishLoading() {
        enableRadioButtons(true);
        // mListView.onRefreshComplete();
        mIsLoading = false;
    }

    private void search() {
        ViewHub.hideKeyboard(this);
        onRefreshData();
        //mListView.pull2RefreshManually();
    }

    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:// 搜索
                if (isEtSearchShowing()) {// 如果搜索栏显示，直接搜索
                    if (!TextUtils.isEmpty(mEtSearch.getText().toString().trim()))
                        search();
                } else {
                    mEtSearch.setVisibility(View.VISIBLE);
                    mETTitle.setVisibility(View.GONE);
                    mEtSearch.requestFocus();
                    ViewHub.showKeyboard(OrderManageActivity.this, mEtSearch);
                }

                break;
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.txtTag://进入运费
                if (FooterTag.equals("PostFeeLink")) {
                    Intent itemIntent = new Intent(mContext, FreightBillActivity.class);
                   /* itemIntent.putExtra("url", "http://pinhuobuyer.nahuo.com/PostfeeBillList");
                    itemIntent.putExtra("name", "运费账单");*/
                    startActivity(itemIntent);
                }
                if (FooterTag.equals("AllotLink")) {
                    Intent phqd = new Intent(mContext, PHQDActivity.class);
                    startActivity(phqd);
                }
                if (FooterTag.equals("PackageLink")) {
                    Intent phqd = new Intent(mContext, BGQDActivity.class);
                    startActivity(phqd);
                }
                break;
            case R.id.rb_done:// 已完结
                mCurOrderStatu = OrderStatus.DONE;
                reload();
                break;
            case R.id.rb_shiped:// 已发货
                mCurOrderStatu = OrderStatus.SHIPED;
                reload();
                break;
            case R.id.rb_cancel:// 已取消
                mCurOrderStatu = OrderStatus.CANCEL;
                reload();
                break;
            case R.id.rb_refund://退款/售后
                mCurOrderStatu = OrderStatus.REFUND;
                reload();
                break;
            case R.id.rb_wait_pay:// 待支付
                mCurOrderStatu = OrderStatus.WAIT_PAY;
                reload();
                break;
            case R.id.rb_wait_ship:// 待发货
                mCurOrderStatu = OrderStatus.WAIT_SHIP;
                reload();
                break;
            case R.id.rb_all://全部
                mCurOrderStatu = OrderStatus.ALL_PAY;
                reload();
                break;
            case R.id.rb_close:
                //关闭
                mCurOrderStatu = OrderStatus.CLOSE_PAY;
                reload();
                break;
        }
    }

    public void reload() {
        //解决数据没清空，代理单子订单有可能出现重复的bug
//        mAdapter.setData(new ArrayList<OrderModel>());
//        mAdapter.notifyDataSetChanged();
//        mAdapter.mCurOrderType = OrderType.BUY;
//        mAdapter.mCurOrderStatu = OrderStatus.DONE;
//        mAdapter.qsid = qsid;
//        mAdapter.title = title;
        adapter.setNewData(null);
        adapter.notifyDataSetChanged();
        onRefreshData();
//        mListView.showLoadingView();
//        mListView.pull2RefreshManually();
        //     findViewById(R.id.inputArea).setVisibility(View.GONE);
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
        Vthis=this;
        StatService.onResume(this);
    }
}
