//package com.nahuo.quicksale.im;
//
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
//import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
//import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
//import com.nahuo.quicksale.BaseSlideBackActivity;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.ViewHub;
//import com.nahuo.quicksale.adapter.MoreRecordAdapter;
//import com.nahuo.quicksale.api.OtherAPI;
//import com.nahuo.quicksale.oldermodel.MoreRecordModel;
//import com.nahuo.quicksale.oldermodel.PublicData;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MoreRecordActivity extends BaseSlideBackActivity implements OnClickListener, OnLoadMoreListener,
//        OnRefreshListener {
//    private PullToRefreshListView pullRefreshListView;
//    private MoreRecordAdapter     mAdapter;
//    private int                   mPageIndex    = 1;
//    private final int             PAGE_SIZE     = 20;
//    private MoreRecordActivity    vThis         = this;
//    private String                userid;
//    private LoadingDialog         loadingDialog = null;
//    private View                  emptyView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
//        setContentView(R.layout.activity_morerecord);
//       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局
//        userid = getIntent().getStringExtra("userid");
//        if (savedInstanceState != null) {
//
//        }
//        initTitleBar();
//        initview();
//    }
//
//    // 初始化视图
//    private void initview() {
//        pullRefreshListView = (PullToRefreshListView)findViewById(R.id.chat_pull_refresh_listview_items);
//        pullRefreshListView.setCanLoadMore(true);
//        pullRefreshListView.setCanRefresh(true);
//
//        pullRefreshListView.setMoveToFirstItemAfterRefresh(true);
//        pullRefreshListView.setOnRefreshListener(this);
//        pullRefreshListView.setOnLoadListener(this);
//        loadingDialog = new LoadingDialog(this);
//        emptyView = findViewById(R.id.home_layout_empty);
//        initItemAdapter();
//        loadData();
//    }
//
//    private void initItemAdapter() {
//        mAdapter = new MoreRecordAdapter(vThis, Integer.parseInt(userid));
//
//        pullRefreshListView.setAdapter(mAdapter);
//    }
//
//    private void loadData() {
//        new LoadDataTask(true).execute();
//    }
//
//
//
//    //加载任务
//    public class LoadDataTask extends AsyncTask<Void, Void, Object> {
//
//        private boolean mIsRefresh = false;
//
//        public LoadDataTask(boolean isRefresh) {
//            mIsRefresh = isRefresh;
//            loadingDialog.start("加载数据");
//        }
//
//        @Override
//        protected Object doInBackground(Void... params) {
//            // TODO Auto-generated method stub
//            try {
//                List<MoreRecordModel> result = OtherAPI.gethisrecord(mPageIndex, PAGE_SIZE, userid,
//                        PublicData.getCookie(vThis));
//
//                return result;
//            } catch (Exception ex) {
//
//                ex.printStackTrace();
//                return "error:" + (ex.getMessage() == null ? "未知异常" : ex.getMessage());
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//
//            if (loadingDialog.isShowing()) {
//                loadingDialog.dismiss();
//            }
//            List<MoreRecordModel> data = new ArrayList<MoreRecordModel>();
//
//            if (mIsRefresh) {
//                pullRefreshListView.onRefreshComplete();
//            } else {
//                pullRefreshListView.onLoadMoreComplete();
//            }
//            if (result instanceof String && ((String)result).startsWith("error:")) {
//                ViewHub.showLongToast(vThis, ((String)result).replace("error:", ""));
//            } else {
//                @SuppressWarnings("unchecked")
//                List<MoreRecordModel> adddata = (List<MoreRecordModel>)result;
//
//                if (mIsRefresh) {
//                    data = adddata;
//                } else {
//                    data = mAdapter.getData();
//                    data.addAll(adddata);
//                }
//
//                mAdapter.setData(data);
//                mAdapter.notifyDataSetChanged();
//
//
//                if (data.size() == 0) {
//                    showEmptyView(true, "没有上新数据");
//
//                } else {
//
//                    showEmptyView(false, "");
//                }
//            }
//
//        }
//    }
//
//    // 初始化
//    private void initTitleBar() {
//        TextView title = (TextView)findViewById(R.id.titlebar_tvTitle);
//        title.setText("聊天记录");
//        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
//        btnLeft.setText(R.string.titlebar_btnBack);
//        btnLeft.setVisibility(View.VISIBLE);
//        btnLeft.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        switch (v.getId()) {
//            case R.id.titlebar_btnLeft:
//                finish();
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        // TODO Auto-generated method stub
//        bindItemData(true);
//        if (mAdapter.getCount() == 0) {
//            showEmptyView(true, "没有上新数据");
//
//        } else {
//            showEmptyView(false, "");
//
//        }
//    }
//
//    /**
//     * 显示空数据视图
//     * */
//    private void showEmptyView(boolean show, String msg) {
//        pullRefreshListView.setVisibility(show ? View.GONE : View.VISIBLE);
//        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
//
//    /**
//     * /** 绑定款式列表
//     * */
//    private void bindItemData(boolean isRefresh) {
//        if (isRefresh) {
//            showEmptyView(false, ""); // 开始执行刷新操作时，不显示空数据视图
//
//            mPageIndex = 1;
//            new LoadDataTask(isRefresh).execute();
//        } else {
//            mPageIndex++;
//            new LoadDataTask(isRefresh).execute();
//        }
//
//    }
//
//    @Override
//    public void onLoadMore() {
//        // TODO Auto-generated method stub
//        bindItemData(false);
//    }
//}
