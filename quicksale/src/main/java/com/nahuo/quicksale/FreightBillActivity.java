package com.nahuo.quicksale;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.adapter.FreightListAdapter;
import com.nahuo.quicksale.api.FreightBillAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.ActivityFreightBillModel;
import com.nahuo.quicksale.util.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alan on 2017/4/1 0001.
 * 运费账单
 */
public class FreightBillActivity extends BaseAppCompatActivity implements View.OnClickListener, PullToRefreshListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener {
    private static final String TAG = FreightBillActivity.class.getSimpleName();
    private FreightBillActivity vThis = this;
    private PullToRefreshListView pullRefreshListView;
    private LoadDataTask loadDataTask;
    private LoadingDialog mloadingDialog;
    private TextView tvEmptyMessage;
    private FreightListAdapter adapter;
    private List<ActivityFreightBillModel.FreightBillItemModel> itemList = null;
    private View emptyView;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private TextView tvTitle;
    private Button btnLeft;
    private TextView sumMoney, startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.layout_freight_bill);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        initView();
    }

    private void initView() {
        emptyView = findViewById(R.id.freight_bill_empty);
        // 标题栏
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        tvTitle.setText("运费账单");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setText("说明");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

        itemList = new ArrayList<ActivityFreightBillModel.FreightBillItemModel>();
        // 初始化适配器
        tvEmptyMessage = (TextView) emptyView
                .findViewById(R.id.layout_empty_tvMessage);
        mloadingDialog = new LoadingDialog(vThis);
        pullRefreshListView = (PullToRefreshListView) findViewById(R.id.freight_bill_pull_refresh_listview_items);
        pullRefreshListView.setCanLoadMore(true);
        pullRefreshListView.setCanRefresh(true);
        pullRefreshListView.setMoveToFirstItemAfterRefresh(true);
        pullRefreshListView.setOnRefreshListener(this);
        pullRefreshListView.setOnLoadListener(this);
        // 头部view：
        View headerView = getLayoutInflater().inflate(R.layout.layout_freight_headview, null);
        sumMoney = (TextView) headerView.findViewById(R.id.id_fright_bill_titleMoney);
        startTime = (TextView) headerView.findViewById(R.id.id_fright_bill_titleTime);
        pullRefreshListView.addHeaderView(headerView);
        // 刷新数据
        showEmptyView(false, "");
        emptyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pullRefreshListView.pull2RefreshManually();

                if (pullRefreshListView != null) {
                    if (pullRefreshListView.isCanRefresh())
                        pullRefreshListView.onRefreshComplete();
                    if (pullRefreshListView.isCanLoadMore())
                        pullRefreshListView.onLoadMoreComplete();
                }

            }
        });
        initItemAdapter();
        loadData();
    }

    /**
     * 获取运费账单列表数据
     */
    private void loadData() {
        getFreightList(true);
//        loadDataTask = new LoadDataTask(true);
//        loadDataTask.execute((Void) null);
    }

    /**
     * 显示空数据视图
     */
    private void showEmptyView(boolean show, String msg) {
        pullRefreshListView.setVisibility(show ? View.GONE : View.VISIBLE);
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(msg)) {
            tvEmptyMessage.setText(getString(R.string.layout_empty_message));
        } else {
            tvEmptyMessage.setText(msg);
        }
    }

    // 初始化数据
    private void initItemAdapter() {
        if (itemList == null)
            itemList = new ArrayList<ActivityFreightBillModel.FreightBillItemModel>();
        adapter = new FreightListAdapter(itemList, vThis);
        pullRefreshListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_btnRight:
                Intent intent = new Intent(vThis, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 103997);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                vThis.startActivity(intent);
                break;
        }

    }

    @Override
    public void onLoadMore() {
        bindItemData(false);
    }

    @Override
    public void onRefresh() {

        bindItemData(true);
        if (itemList.size() == 0) {
            showEmptyView(false, "您还没有运费账单");
        } else {

        }
    }

    /**
     * /** 绑定运费账单列表
     */
    private void bindItemData(boolean isRefresh) {
        if (isRefresh) {
            showEmptyView(false, ""); // 开始执行刷新操作时，不显示空数据视图
            mPageIndex = 1;
            getFreightList(isRefresh);
//            loadDataTask = new LoadDataTask(isRefresh);
//            loadDataTask.execute((Void) null);
        } else {
            mPageIndex++;
            getFreightList(isRefresh);
//            loadDataTask = new LoadDataTask(isRefresh);
//            loadDataTask.execute((Void) null);
        }
    }

    String money = "";

    private void getFreightList(final boolean isRefresh) {

        Map<String, Object> params = new HashMap<>();
        params.put("pageindex", mPageIndex);
        params.put("pagesize", mPageSize);
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getPostFeeBillList(params)
                .compose(RxUtil.<PinHuoResponse<ActivityFreightBillModel>>rxSchedulerHelper())
                .compose(RxUtil.<ActivityFreightBillModel>handleResult())
                .subscribeWith(new CommonSubscriber<ActivityFreightBillModel>(vThis, true, R.string.items_loadData_loading) {
                    @Override
                    public void onNext(ActivityFreightBillModel activityFreightBillModel) {
                        super.onNext(activityFreightBillModel);
                        List<ActivityFreightBillModel.FreightBillItemModel> result = activityFreightBillModel.getBills();
                        pullRefreshListView.setCanLoadMore(!ListUtils.isEmpty(result));
                        //pullRefreshListView.setCanRefresh(true);
                        money = activityFreightBillModel.getPrepaidPostFee();
                        if (isRefresh) {
                            itemList = result;
                        } else {
                            itemList.addAll(result);
                        }
                        if (isRefresh) {
                            pullRefreshListView.onRefreshComplete();
                        } else {
                            pullRefreshListView.onLoadMoreComplete();
                        }
                        if (!TextUtils.isEmpty(money) && mPageIndex == 1) {
                            sumMoney.setText("¥" + money);
                        }
                        adapter.mList = itemList;
                        adapter.notifyDataSetChanged();
                        if (itemList.size() > 0) {
                            vThis.showEmptyView(false, "");
                        } else {
                            vThis.showEmptyView(true, "您还没有运费账单");
                        }
                        if (itemList.size() >= 1) {
                            startTime.setText(itemList.get(0).getToTime() + "至今");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isRefresh) {
                            pullRefreshListView.onRefreshComplete();
                        } else {
                            pullRefreshListView.onLoadMoreComplete();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (isRefresh) {
                            pullRefreshListView.onRefreshComplete();
                        } else {
                            pullRefreshListView.onLoadMoreComplete();
                        }
                    }
                }));
    }

    public class LoadDataTask extends AsyncTask<Void, Void, String> {

        private boolean mIsRefresh = false;
        String money;

        public LoadDataTask(boolean isRefresh) {
            mIsRefresh = isRefresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mloadingDialog.start(getString(R.string.items_loadData_loading));
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ActivityFreightBillModel json = FreightBillAPI.getFreightList(FreightBillActivity.this, mPageSize, mPageIndex);
                List<ActivityFreightBillModel.FreightBillItemModel> result = json.getBills();
                pullRefreshListView.setCanLoadMore(!ListUtils.isEmpty(result));
                //pullRefreshListView.setCanRefresh(true);
                money = json.getPrepaidPostFee();
                Log.v(TAG, result.size() + "");
                if (mIsRefresh) {
                    itemList = result;
                } else {
                    itemList.addAll(result);
                }
                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "获取运费账单列表发生异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v(TAG, result);
            mloadingDialog.stop();
            if (mIsRefresh) {
                pullRefreshListView.onRefreshComplete();
            } else {
                pullRefreshListView.onLoadMoreComplete();
            }
            if (!TextUtils.isEmpty(money) && mPageIndex == 1) {
                sumMoney.setText("¥" + money);
            }
            adapter.mList = itemList;
            adapter.notifyDataSetChanged();
            if (itemList.size() > 0) {
                vThis.showEmptyView(false, "");
            } else {
                vThis.showEmptyView(true, "您还没有运费账单");
            }
            if (itemList.size() >= 1) {
                startTime.setText(itemList.get(0).getToTime() + "至今");
            }

            if (!result.equals("OK")) {
                // 验证result
                if (result.startsWith("401")
                        || result.startsWith("not_registered")) {
                    Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
