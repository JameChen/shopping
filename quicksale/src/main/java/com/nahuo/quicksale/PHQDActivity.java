package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.adapter.PHQDAdapter;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.PHQDModel;
import com.nahuo.quicksale.util.RxUtil;

import java.util.List;
/*
* 配货清单(不进行分页)
* */
public class PHQDActivity extends BaseAppCompatActivity implements OnClickListener, PullToRefreshListView.OnRefreshListener, PullToRefreshListView.OnLoadMoreListener {
    private static final String TAG=PHQDActivity.class.getSimpleName();
    private PullToRefreshListViewEx mListView;
    private PHQDAdapter mAdapter;
    private Context mContext;
    private LoadingDialog mDialog;
    private boolean mIsRefresh = true;
    private List<PHQDModel> data;
    private static int                 PAGE_INDEX           = 1;
    private static final int           PAGE_SIZE            = 20;
     private LoadDataTask loadDataTask;
    private View emptyView;
    private TextView tvEmptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bgqd);
        mContext = this;
        initView();
       // mListView.pull2RefreshManually();
        mListView.setCanLoadMore(false);
        mListView.setCanRefresh(true);
        loadData();
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
        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        initListView();
    }

    private void initListView() {
        mListView = (PullToRefreshListViewEx) findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyViewText("您还没有配货信息哦");
        emptyView =findViewById(R.id.coupon_bill_empty);
        emptyView.setOnClickListener(this);
        tvEmptyMessage = (TextView) emptyView
                .findViewById(R.id.layout_empty_tvMessage);
        showEmptyView(false, "");
        mAdapter = new PHQDAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
//        loadData();
    }

    /**
     * 显示空数据视图
     * */
    private void showEmptyView(boolean show, String msg) {
        mListView.setVisibility(show ? View.GONE : View.VISIBLE);
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(msg)) {
            tvEmptyMessage.setText(getString(R.string.layout_empty_message));
        } else {
            tvEmptyMessage.setText(msg);
        }
    }

    private void loadData() {
//        loadDataTask=new LoadDataTask(true);
//        loadDataTask.execute((Void) null);
        getAllotList(true);
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        tvTitle.setText("配货清单");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setText("说明");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);
    }
    private void loadFinished(boolean mIsRefresh) {
        if(mIsRefresh){
            mListView.onRefreshComplete();
        }else{
            mListView.onLoadMoreComplete();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnRight:
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 103887);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                mContext.startActivity(intent);
                break;
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_icon_loading:
                startActivityForResult(new Intent(mContext, TradeLogSearchActivity.class),  1);
                break;
            //刷新
            case R.id.coupon_bill_empty:
                onRefresh();
                break;
        }
    }

    @Override
    public void onLoadMore() {
        bindItemData(false);
    }


    private void bindItemData(boolean isRefresh) {
        if (isRefresh) {
            PAGE_INDEX = 1;
//            loadDataTask = new LoadDataTask(isRefresh);
//            loadDataTask.execute((Void) null);
            getAllotList(isRefresh);
        } else {
            PAGE_INDEX++;
//            loadDataTask = new LoadDataTask(isRefresh);
//            loadDataTask.execute((Void) null);
            getAllotList(isRefresh);
        }
    }

    @Override
    public void onRefresh() {
        bindItemData(true);
    }

    private void getAllotList(final boolean mIsRefresh){
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
        .getAllotList().compose(RxUtil.<PinHuoResponse<List<PHQDModel>>>rxSchedulerHelper())
        .compose(RxUtil.<List<PHQDModel>>handleResult())
        .subscribeWith(new CommonSubscriber<List<PHQDModel>>(this,true,R.string.loading){
            @Override
            public void onNext(List<PHQDModel> phqdModels) {
                super.onNext(phqdModels);
                loadFinished(mIsRefresh);
                mListView.setCanRefresh(true);
                if (mIsRefresh) {
                    data=phqdModels;
                } else {
                    data.addAll(phqdModels);
                }
                mAdapter.setData(data);
                mAdapter.notifyDataSetChanged();
                if(!ListUtils.isEmpty(data))
                {
                    showEmptyView(false,"");
                }
                else
                {
                    showEmptyView(true,"您还没有配货信息哦");
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                loadFinished(mIsRefresh);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                loadFinished(mIsRefresh);
            }
        }));

    }
    private class LoadDataTask extends AsyncTask<Void,Void,Object>{
        private List<PHQDModel> returnData;
        private boolean mIsRefresh = false;

        public LoadDataTask(boolean mIsRefresh) {
            this.mIsRefresh = mIsRefresh;
            showEmptyView(false,"");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                mDialog.start();
                mDialog.setMessage("正在加载配货信息...");
        }

        @Override
        protected Object doInBackground(Void... params) {

            try {
                returnData = QuickSaleApi.getPHQD(mContext, PAGE_INDEX, PAGE_SIZE);
                Log.v(TAG,PAGE_INDEX+"");
                //mListView.setCanLoadMore(!ListUtils.isEmpty(returnData));
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return returnData;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loadFinished(mIsRefresh);
            mDialog.stop();
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
            } else {
                returnData=(List<PHQDModel>)result;
                mListView.setCanRefresh(true);
                if (mIsRefresh) {
                    data=returnData;
                } else {
                    data.addAll(returnData);
                }
                mAdapter.setData(data);
                mAdapter.notifyDataSetChanged();

                if(!ListUtils.isEmpty(data))
                {
                    showEmptyView(false,"");
                }
                else
                {
                    showEmptyView(true,"您还没有配货信息哦");
                }

            }
        }
    }
}
