package com.nahuo.quicksale.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.PinHuoNewAdapter2;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.base.BaseActivty;
import com.nahuo.quicksale.controls.SelectSizeColorMenu;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.ResultData;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class FocusActivity extends BaseActivty implements HttpRequestListener, PullToRefreshListView.OnRefreshListener, PullToRefreshListView.OnLoadMoreListener {

    private FocusActivity vThis = this;
    private PullToRefreshListViewEx mListView;
    private PinHuoNewAdapter2 mAdapter;

    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean isTasking = false;
    private boolean mIsRefresh = true;
    private List<PinHuoModel> mListData = new ArrayList<>();

    public Activity parentActivity;
    private View mContentView;
    private LoadingDialog mLoadingDialog;

    private boolean isshow = false;
    private String mKeyword = "";
    private boolean mNeedToClearKeyword;
    private CountDownTask mCountDownTask;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);
        EventBus.getDefault().registerSticky(this);
        initViews();
    }

    public void onEventMainThread(BusEvent event) {

    }

    private void startCountDown() {
        mCountDownTask = CountDownTask.create();
        mAdapter.setCountDownTask(mCountDownTask);
    }

    private void cancelCountDown() {
        mAdapter.setCountDownTask(null);
        mCountDownTask.cancel();
    }


    @Override
    public void onResume() {
        super.onResume();
        startCountDown();
        if (isshow) {
            onRefresh();
        }

        isshow = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelCountDown();
    }

    private void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("我的关注");
        findViewById(R.id.titlebar_btnLeft).setVisibility(View.VISIBLE);
        findViewById(R.id.titlebar_btnLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoadingDialog = new LoadingDialog(vThis);
        mListView = (PullToRefreshListViewEx) findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyViewText("");
        mListView.mEmptyView = null;
        mAdapter = new PinHuoNewAdapter2(vThis);
        mListView.pull2RefreshManually();
        mListView.setAdapter(mAdapter);
        mAdapter.setListener(new PinHuoNewAdapter2.PinHuoNewListener() {
            @Override
            public void focusStatChanged() {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd));
                loadData();
            }
        });
        findViewById(R.id.btn_pin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd));
                finish();
            }
        });

    }


    private void showMenu(final GoodBaseInfo info) {
        SelectSizeColorMenu menu = new SelectSizeColorMenu(vThis, info);
        menu.show();

    }

    private void getItemInfo(int id) {
        HttpRequestHelper.HttpRequest httpRequest = mRequestHelper.getRequest(vThis,
                RequestMethod.ShopMethod.SHOP_GET_ITEM_BASE_INFO, this);
        httpRequest.addParam("id", id + "");
        httpRequest.setConvert2Class(GoodBaseInfo.class);
        httpRequest.doPost();
    }

    private void loadData() {
        isTasking = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
        QuickSaleApi.getMyFocusList(vThis, mRequestHelper, this, PAGE_INDEX, PAGE_SIZE);
    }

    @Override
    public void onLoadMore() {
        if (!isTasking) {
            mIsRefresh = false;
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        if (!isTasking) {
            if (mNeedToClearKeyword) {
                mKeyword = "";
            }
            mNeedToClearKeyword = true;
            mIsRefresh = true;
            loadData();
        }
    }

    @Override
    public void onRequestStart(String method) {
        if (RequestMethod.QuickSaleMethod.GET_FOCUS_LIST.equals(method)) {
//            mLoadingDialog.setMessage("正在读取关注信息....");
//            mLoadingDialog.show();
        }
        if (RequestMethod.QuickSaleMethod.SAVE_FOCUS.equals(method)) {
            mLoadingDialog.setMessage("正在保存....");
            mLoadingDialog.show();
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        if (RequestMethod.QuickSaleMethod.GET_FOCUS_LIST.equals(method)) {
            onDataLoaded(object);
        }
        if (RequestMethod.QuickSaleMethod.SAVE_FOCUS.equals(method)) {
            loadData();
        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {

    }

    private void onDataLoaded(Object obj) {
        try {
            mListData = GsonHelper.jsonToObject(obj.toString(),
                    new TypeToken<List<PinHuoModel>>() {
                    });
            for (PinHuoModel p : mListData) {
                p.isFocus = true;
            }
            mAdapter.setData(mListData);
            mAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
        }
        loadFinished();
    }

    private void loadFinished() {
        isTasking = false;
        mListView.onRefreshComplete();
        mListView.onLoadMoreComplete();

        if (mListData.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_view).setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.GONE);
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        }
    }


    public void search(String keyword) {
        mKeyword = keyword;
        mNeedToClearKeyword = false;
        mListView.pull2RefreshManually();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
