package com.nahuo.quicksale;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.adapter.WithdrawLogAdapter;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.WithdrawItem;

import java.util.List;

/**
 * Description:提现记录
 * 2014-7-24 下午5:44:42
 * 
 * @author ZZB
 */
public class WithdrawLogActivity extends BaseSlideBackActivity implements OnClickListener, OnRefreshListener, OnLoadMoreListener  {

    private PullToRefreshListViewEx mListView;
    private WithdrawLogAdapter mAdapter;
    private Context mContext;
//    private LoadingDialog mDialog;
    private boolean mIsLoading, mIsRefresh = true;
    private static int                 PAGE_INDEX           = 0;
    private static final int           PAGE_SIZE            = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_withdraw_log);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        mContext = this;
        initView();
        mListView.pull2RefreshManually();
    }

    private void initView() {
//        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        initListView();
    }

    private void initListView() {
        mListView = (PullToRefreshListViewEx) findViewById(R.id.listview);
        mListView.setEmptyViewText("没有提现记录哦");
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        initHeaderView();
        mAdapter = new WithdrawLogAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WithdrawItem item = (WithdrawItem) mAdapter.getItem(position - 1);
                final TextDlgFragment f = TextDlgFragment.newInstance(genContent(item));
                f.setNegativeListener("关闭", null);
                f.show(getSupportFragmentManager(), "TextDlgFragment");
                
            }
        });
        loadData();
    }

    private void initHeaderView() {
        View headerView = findViewById(R.id.header_view);
        TextView tradeMoney = (TextView) headerView.findViewById(R.id.trade_money);
        tradeMoney.setText(Html.fromHtml("<B>交易金额</B>"));
        TextView tradeTime = (TextView) headerView.findViewById(R.id.trade_time);
        tradeTime.setText(Html.fromHtml("<B>交易时间</B>"));
        TextView balance = (TextView) headerView.findViewById(R.id.trade_state);
        balance.setText(Html.fromHtml("<B>状态</B>"));
        View rightIcon = headerView.findViewById(R.id.right_icon);
        rightIcon.setVisibility(View.GONE);
    }

    private void loadData() {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                mDialog.start();
            }

            @Override
            protected Object doInBackground(Void... params) {
                List<WithdrawItem> items = null;
                try {
                    items = PaymentAPI.getWithdrawLog(mContext, PAGE_INDEX, PAGE_SIZE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error:" + e.getMessage();
                }
                return items;
            }

            @Override
            protected void onPostExecute(Object result) {
                loadFinished();
                if (result instanceof String && ((String) result).startsWith("error:")) {
                    ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
                } else {
                    @SuppressWarnings("unchecked")
                    List<WithdrawItem> items = (List<WithdrawItem>) result;
                    if (mIsRefresh) {
                        mAdapter.setData(items);
                    } else {
                        mListView.setCanLoadMore(!ListUtils.isEmpty(items));
                        mAdapter.addDataToTail(items);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);

        tvTitle.setText("提现记录");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnLeft:
            finish();
            break;
        }
    }


    private String genContent(WithdrawItem item) {
        StringBuilder content = new StringBuilder();
        content.append("<font color='black'>提现号 : </font>").append(item.getWithdrawNumber()).append("<br><br>");
        content.append("<font color='black'>提现金额 : </font>").append(item.getTradeMoney())
                .append("<br><br>");
        content.append("<font color='black'>提现结果 : </font>").append(item.getTradeState())
                .append("<br><br>");
        content.append("<font color='black'>提现时间 : </font>").append(item.getTradeTime())
                .append("<br><br>");
        content.append("<font color='black'>审核时间 : </font>").append(item.getFinishTime()).append("<br><br>");
        content.append("<font color='black'>审核结果 : </font>").append(item.getCheckResult())
                .append("<br><br>");
        return content.toString();
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
    private void loadFinished() {
        mIsLoading = false;
        mListView.onRefreshComplete();
        mListView.onLoadMoreComplete();
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
}
