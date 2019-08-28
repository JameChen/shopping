package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.bean.PageBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.adapter.BGQDLogAdapter;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.BGQDModel;
import com.nahuo.quicksale.util.RxUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BGQDActivity extends BaseAppCompatActivity implements View.OnClickListener, PullToRefreshListView.OnRefreshListener, PullToRefreshListView.OnLoadMoreListener {

    private PullToRefreshListViewEx mListView;
    private BGQDLogAdapter mAdapter;
    private Context mContext;
        private LoadingDialog mDialog;
    private boolean mIsLoading, mIsRefresh = true;
    private List<BGQDModel> data;
    private static int                 PAGE_INDEX           = 0;
    private static final int           PAGE_SIZE            = 20;

    //单条包裹清单信息
    public static final String EXPRESSCODE="EXPRESSCODE";
    public static final String TAG=BGQDActivity.class.getSimpleName();
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bgqd);

        mContext = this;
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
        loadData();
        StatService.onResume(this);
    }

    private void initView() {
//        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        initListView();
    }

    private void initListView() {
        code=this.getIntent().getStringExtra(EXPRESSCODE);
        mListView = (PullToRefreshListViewEx) findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyViewText("没有数据");
        mAdapter = new BGQDLogAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
//        loadData();
    }
    private void loadData() {
        mIsLoading = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
        Map<String, Object> params = new HashMap<>();
        params.put("PageIndex", PAGE_INDEX);
        params.put("PageSize", PAGE_SIZE);
        if (!TextUtils.isEmpty(code)) {
            params.put("code", code);
        }
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getPackageList(params)
        .compose(RxUtil.<PinHuoResponse<PageBean>>rxSchedulerHelper())
        .compose(RxUtil.<PageBean>handleResult())
        .subscribeWith(new CommonSubscriber<PageBean>(this){
            @Override
            public void onNext(PageBean o) {
                super.onNext(o);
                try {
                    loadFinished();
                    if (o!=null){
                        data=o.getPackageList();
                        if (mIsRefresh) {
                            mAdapter.setData(data);
                        } else {
                            mListView.setCanLoadMore(!ListUtils.isEmpty(data));
                            mAdapter.addDataToTail(data);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                loadFinished();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                loadFinished();
            }
        }));
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
//                    data = QuickSaleApi.getBGQD(mContext, PAGE_INDEX, PAGE_SIZE,code);
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
//                        mAdapter.setData(data);
//                    } else {
//                        mListView.setCanLoadMore(!ListUtils.isEmpty(data));
//                        mAdapter.addDataToTail(data);
//                    }
////                    mAdapter.setData(items);
//                    mAdapter.notifyDataSetChanged();
//                }
////                if (mDialog.isShowing()) {
////                    mDialog.stop();
////                }
//
//            }
//
//        }.execute();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);

        tvTitle.setText("包裹清单");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setText("说明");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

    }
    private void loadFinished() {
        mIsLoading = false;
        mListView.onRefreshComplete();
        mListView.onLoadMoreComplete();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnRight:
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 103888);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                mContext.startActivity(intent);
                break;
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_icon_loading:
                startActivityForResult(new Intent(mContext, TradeLogSearchActivity.class),  1);
                break;
        }
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
}
