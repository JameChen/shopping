package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.bean.TradeBean;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.adapter.TransRecordAdapter;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.TradeLogItem;
import com.nahuo.quicksale.util.RxUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Description:交易记录页面 2014-7-23 上午11:40:54
 *
 * @author ZZB
 */
public class TradeLogActivity extends BaseAppCompatActivity implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener {
    public static String TAG = TradeLogActivity.class.getSimpleName();
    private PullToRefreshListViewEx mListView;
    private TransRecordAdapter mAdapter;
    private Context mContext;
    //    private LoadingDialog mDialog;
    private boolean mIsLoading, mIsRefresh = true;
    private static int PAGE_INDEX = 1;
    private static final int PAGE_SIZE = 20;
    private TradeLogItem data = null;

    private String begin = "";
    private String end = "";
    private String type = "";

    public static final String TO_TRADDETALIL_BUNDLE = "TO_TRADDETALIL_BUNDLE";//bundle名字
    public static final String EXTRA_TRADE_LIST = "EXTRA_TRADE_LIST";//bundle名字
    public static final String EXTRA_TRADE_NAME = "EXTRA_TRADE_NAME";//bundle名字
    private List<TradeBean.ButtonsBean.ValueBean> values;
    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_trade_log);
        // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        mContext = this;
        if (getIntent() != null) {
            values = (List<TradeBean.ButtonsBean.ValueBean>) getIntent().getSerializableExtra(EXTRA_TRADE_LIST);
            name = getIntent().getStringExtra(EXTRA_TRADE_NAME);
        }
        initView();
        mListView.pull2RefreshManually();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    private void initView() {
//        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        initListView();
    }

    private void initListView() {
        mListView = (PullToRefreshListViewEx) findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyViewText("没有交易记录哦");
        mListView.setCanLoadMore(true);
        mListView.setCanRefresh(true);
        initHeaderView();
        mAdapter = new TransRecordAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /* TradeLogItem.TradeList item = (TradeLogItem.TradeList) mAdapter.getItem(position - 1);
                final TextDlgFragment f = TextDlgFragment.newInstance(genContent(item));
                f.setNegativeListener("关闭", null);
                f.show(getSupportFragmentManager(), "TextDlgFragment");
                Intent intent=new Intent(mContext,TradingDetailsActivity.class);
                startActivity(intent);*/
                toTradeDetailsActivity(TO_TRADDETALIL_BUNDLE, (TradeLogItem.TradeList) mAdapter.getItem(position - 1));
            }
        });
//        loadData();
    }

    private void initInputArea() {
        findViewById(R.id.inputArea).setVisibility(View.VISIBLE);
        if (data != null) {
            if (data.getSummary().length() == 0 && PAGE_INDEX == 1) {
                findViewById(R.id.inputArea).setVisibility(View.GONE);
            }
            if (PAGE_INDEX == 1) {
                ((TextView) findViewById(R.id.txt1)).setText(Html.fromHtml(data.getSummary()));
            }
        }
    }

    private void initHeaderView() {
        View headerView = findViewById(R.id.header_view);
        TextView tradeMoney = (TextView) headerView.findViewById(R.id.trade_money);
        tradeMoney.setText(Html.fromHtml("<B>交易金额</B>"));
        TextView tradeTime = (TextView) headerView.findViewById(R.id.trade_time);
        tradeTime.setText(Html.fromHtml("<B>交易时间</B>"));
        TextView balance = (TextView) headerView.findViewById(R.id.tv_trade_type);
        balance.setText(Html.fromHtml("<B>交易类型</B>"));
        View rightIcon = headerView.findViewById(R.id.right_icon);
        rightIcon.setVisibility(View.GONE);
        headerView.setVisibility(View.GONE);
    }

    private void getTradeList4PinHuo() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageIndex", PAGE_INDEX);
        params.put("pageSize", PAGE_SIZE);
        if (begin.length() > 0) {
            params.put("fromTime", begin);
        }
        if (end.length() > 0) {
            params.put("toTime", end);
        }
        if (!ListUtils.isEmpty(values)) {
            for (TradeBean.ButtonsBean.ValueBean valueBean : values) {
                if (valueBean != null) {
                    params.put(valueBean.getKey(), valueBean.getValue());
                }
            }
        }
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getTradeList4PinHuo(params)
                .compose(RxUtil.<PinHuoResponse<TradeLogItem>>rxSchedulerHelper())
                .compose(RxUtil.<TradeLogItem>handleResult())
                .subscribeWith(new CommonSubscriber<TradeLogItem>(this, true, R.string.loading) {
                    @Override
                    public void onNext(TradeLogItem bean) {
                        super.onNext(bean);
                        data = bean;
                        loadFinished();
                        if (data != null) {
                            if (mIsRefresh) {
                                mAdapter.setData(data.getTradeList());
                            } else {
                                mAdapter.addDataToTail(data.getTradeList());
                            }
                            mListView.setCanLoadMore(!ListUtils.isEmpty(data.getTradeList()));
                            mAdapter.notifyDataSetChanged();
                            if (mIsRefresh) {
                                mListView.onRefreshComplete();
                            } else {
                                mListView.onLoadMoreComplete();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loadFinished();
                    }
                }));
    }

    private void loadData() {
        mIsLoading = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
        getTradeList4PinHuo();
//        new AsyncTask<Void, Void, Object>() {
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
////                mDialog.start();
//            }
//
//            @Override
//            protected Object doInBackground(Void... params) {
//
//                try {
//                    data = PaymentAPI.getTradeLog(mContext, begin, end, type, PAGE_INDEX, PAGE_SIZE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return "error:" + e.getMessage();
//                }
//                return data;
//            }
//
//            @Override
//            protected void onPostExecute(Object result) {
//                super.onPostExecute(result);
//                loadFinished();
//                if (result instanceof String && ((String) result).startsWith("error:")) {
//                    ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
//                } else {
//                    if (mIsRefresh) {
//                        mAdapter.setData(data.getTradeList());
//                    } else {
//                        mAdapter.addDataToTail(data.getTradeList());
//                    }
//                    mListView.setCanLoadMore(!ListUtils.isEmpty(data.getTradeList()));
////                    mAdapter.setData(items);
//                    mAdapter.notifyDataSetChanged();
//                    if (mIsRefresh) {
//                        mListView.onRefreshComplete();
//                    } else {
//                        mListView.onLoadMoreComplete();
//                    }
//                }
////                if (mDialog.isShowing()) {
////                    mDialog.stop();
////                }
//
//            }
//        }.execute();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        ImageView btnRight = (ImageView) findViewById(R.id.titlebar_icon_right);

        tvTitle.setText(name);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        btnRight.setVisibility(View.VISIBLE);
        btnRight.setImageResource(R.drawable.pn_message_left_white);
        btnRight.setOnClickListener(this);

    }

    private void loadFinished() {
        mIsLoading = false;
        mListView.onRefreshComplete();
        mListView.onLoadMoreComplete();
        initInputArea();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_icon_right:
                Intent intent = new Intent(mContext, TradeLogSearchActivity.class);
                intent.putExtra(EXTRA_TRADE_NAME, name);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    begin = data.getStringExtra("begin");
                    end = data.getStringExtra("end");
                    type = data.getStringExtra("type");
                    mListView.pull2RefreshManually();

                }

                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoadMore() {
        if (!mIsLoading) {
            mIsRefresh = false;
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        if (!mIsLoading) {
            mIsRefresh = true;
            loadData();
        }

    }

    private String genContent(TradeLogItem.TradeList item) {
        StringBuilder content = new StringBuilder();
        content.append("<font color='black'>订单类型 : </font>").append(item.getTradeTypeName()).append("<br><br>");
        String orderNo = item.getOrderCode();
        if (!TextUtils.isEmpty(orderNo)) {
            content.append("<font color='black'>订单号 : </font>").append(orderNo).append("<br><br>");
        }
        content.append("<font color='black'>交易金额 : </font>").append(item.getAmount()).append("<br><br>");
        content.append("<font color='black'>支付时间 : </font>").append(item.getDate()).append("<br><br>");
        content.append("<font color='black'>支付方 : </font>").append(item.getUserName()).append("<br><br>");
        content.append("<font color='black'>收款方 : </font>").append(item.getOtherUserName()).append("<br>");
        if (item.getTradeName().length() > 0) {
            content.append("<font color='black'>详情 : </font>").append(item.getTradeDesc()).append("<br>");
        }
        return content.toString();
    }

    private void toTradeDetailsActivity(String bundleName, TradeLogItem.TradeList item) {
        Intent intent = new Intent(mContext, TradingDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TRADE_NAME,name);
        bundle.putSerializable(bundleName, item);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}