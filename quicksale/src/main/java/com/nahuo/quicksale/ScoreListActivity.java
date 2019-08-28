package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.adapter.ScoreAdapter;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ScoreModel;

import java.util.ArrayList;
import java.util.List;

public class ScoreListActivity extends BaseAppCompatActivity implements View.OnClickListener,PullToRefreshListView.OnRefreshListener, PullToRefreshListView.OnLoadMoreListener {

    private Context mContext = this;
    private TextView tvTitle;
    private TextView tv_right;
    private RelativeLayout rlDetail;
    private PullToRefreshListViewEx lvData;
    public static  String ETRA_POINT_NAME="ETRA_POINT_NAME";
    private String point_name="";
    private ScoreAdapter mAdapter;
    private List<ScoreModel> dataList = new ArrayList<ScoreModel>();
    private LoadingDialog loadingDialog;
    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean isTasking = false;
    private boolean mIsRefresh = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_score_list);
        if (loadingDialog == null)
            loadingDialog = new LoadingDialog(this);
        initViews();
        initData();

    }

    private void initViews() {
        Intent intent=getIntent();
        if (intent!=null)
            point_name=intent.getStringExtra(ETRA_POINT_NAME);
        loadingDialog.start("加载中...");
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(point_name);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("了解");
        rlDetail = (RelativeLayout) findViewById(R.id.rlDetail);
        lvData = (PullToRefreshListViewEx) findViewById(R.id.lvData);
        lvData.setOnRefreshListener(this);
        lvData.setOnLoadListener(this);
        lvData.setEmptyViewText("");
        findViewById(R.id.tv_right).setOnClickListener(this);
        findViewById(R.id.iv_left).setOnClickListener(this);
    }

    private void initData()
    {
        mAdapter = new ScoreAdapter(mContext, dataList);
        lvData.setAdapter(mAdapter);
        lvData.pull2RefreshManually();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
            case R.id.tv_right: {// 了解活跃积分
                String url = "http://www.nahuo.com/xiaozu/topic/102894";
//                String url = "http://www.nahuo.com/xiaozu/topic/" + SpManager.getUserId(this);
                String temp = "/xiaozu/topic/";
                int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
                Intent intent = new Intent(this, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                this.startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onRefresh() {

        if (!isTasking) {
            mIsRefresh = true;
            lvData.setCanLoadMore(true);
            loadData();
        }
    }
    private void loadData() {
        isTasking = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
        new Task().execute();
    }
    @Override
    public void onLoadMore() {
        if (!isTasking) {
            mIsRefresh = false;
            loadData();
        }
    }

    public class Task extends AsyncTask<Void, Void, Object> {
        public Task() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start("加载数据中,请稍后...");
        }

        @Override
        protected void onPostExecute(Object result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            try {
                List<ScoreModel> data= (List<ScoreModel>) result;
                if (mIsRefresh){
                    dataList.clear();
                    if (!ListUtils.isEmpty(data)) {
                        rlDetail.setVisibility(View.GONE);
                        dataList.addAll(data);
                        mAdapter.setData(dataList);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        rlDetail.setVisibility(View.VISIBLE);
                    }
                }else {
                    if (!ListUtils.isEmpty(data)) {
                        dataList.addAll(data);
                        mAdapter.setData(dataList);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        lvData.setCanLoadMore(false);
                    }
                }
                loadFinished();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            try {
                List<ScoreModel>  dataList = ShopSetAPI.getInstance().GetPointList(PAGE_INDEX,PAGE_SIZE,PublicData.getCookie(mContext));
                return dataList;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "error:"+ex.getMessage();
            }
        }

    }
    private void loadFinished() {
        isTasking = false;
        lvData.onRefreshComplete();
        lvData.onLoadMoreComplete();
    }
}
