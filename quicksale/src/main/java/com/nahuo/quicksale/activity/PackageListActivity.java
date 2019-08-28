package com.nahuo.quicksale.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.bean.PackageNewListBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.BaseActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.TradeLogSearchActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.PackageListAdapter;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.common.ListUtils;

import java.util.List;

public class PackageListActivity extends BaseActivity implements OnClickListener, PullToRefreshListView.OnRefreshListener, PullToRefreshListView.OnLoadMoreListener {

    private PullToRefreshListViewEx mListView;
    private PackageListAdapter mAdapter;
    private Context mContext;
        private LoadingDialog mDialog;
    private boolean mIsLoading, mIsRefresh = true;
    private List<PackageNewListBean.PackageListBean> data;
    private PackageNewListBean bean=null;
    private String Msg="";
    private static int                 PAGE_INDEX           = 0;
    private static final int           PAGE_SIZE            = 20;

    //订单id
    public static final String EXTRA_ORDERID="EXTRA_ORDERID";
    private int orderID;
    private View layout_msg;
    private TextView tv_msg_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bgqd);
        mContext = this;
        initView();

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
        if (mListView!=null)
        mListView.pull2RefreshManually();
    }

    private void initView() {
        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        initListView();
    }

    private void initListView() {
        orderID=this.getIntent().getIntExtra(EXTRA_ORDERID,0);
        mListView = (PullToRefreshListViewEx) findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setRefreshing(false);
        mListView.setCanLoadMore(false);
        mListView.setEmptyViewText("没有数据");
        mAdapter = new PackageListAdapter(mContext);
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
        new AsyncTask<Void, Void, Object>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mDialog.start("加载包裹数据");
            }

            @Override
            protected Object doInBackground(Void... params) {

                try {
                    bean=QuickSaleApi.getPackageList(mContext,orderID);
                    data = bean.getPackageList();
                    Msg=bean.getMsg();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error:" + e.getMessage();
                }
                return data;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                loadFinished();
                if (result instanceof String && ((String) result).startsWith("error:")) {
                    ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
                } else {
                    if (mIsRefresh) {
                        mAdapter.setData(data);
                        judeMsg();
                    } else {
                        mListView.setCanLoadMore(!ListUtils.isEmpty(data));
                        mAdapter.addDataToTail(data);
                    }
//                    mAdapter.setData(items);
                    mAdapter.notifyDataSetChanged();
                }
//                if (mDialog.isShowing()) {
//                    mDialog.stop();
//                }

            }

        }.execute();
    }

    private void judeMsg() {
        if (TextUtils.isEmpty(Msg)){
            layout_msg.setVisibility(View.GONE);
        }else {
            layout_msg.setVisibility(View.VISIBLE);
            tv_msg_content.setText(Msg+"");
        }
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);

        tvTitle.setText("售后包裹选择");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setText("说明");
        btnRight.setVisibility(View.GONE);
        btnRight.setOnClickListener(this);
        layout_msg=findViewById(R.id.layout_msg);
        tv_msg_content=(TextView) findViewById(R.id.tv_msg_content);

    }
    private void loadFinished() {
        mIsLoading = false;
        if (mDialog.isShowing()) {
            mDialog.stop();
        }
        mListView.onRefreshComplete();
        mListView.onLoadMoreComplete();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnRight:
//                Intent intent = new Intent(mContext, PostDetailActivity.class);
//                intent.putExtra(PostDetailActivity.EXTRA_TID, 103888);
//                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                mContext.startActivity(intent);
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
