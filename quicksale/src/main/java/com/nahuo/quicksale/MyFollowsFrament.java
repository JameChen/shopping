package com.nahuo.quicksale;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.nahuo.bean.FollowsBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.adapter.FollowsAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.controls.SelectSizeColorMenu;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ResultData;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by 诚 on 2015/9/29.
 */
public class MyFollowsFrament extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, FollowsAdapter.RemoveListener {

    private MyFollowsFrament vThis = this;
    private PullToRefreshListViewEx mListView;
    private FollowsAdapter mAdapter;

    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean isTasking = false;
    private boolean mIsRefresh = true;
    private List<FollowsBean> mListData = new ArrayList<>();

    public Activity parentActivity;
    private View mContentView;
    private LoadingDialog mLoadingDialog;

    private boolean isshow = false;
    private String mKeyword = "";
    private boolean mNeedToClearKeyword;
    private CountDownTask mCountDownTask;
    private EventBus mEventBus = EventBus.getDefault();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_my_follows, container,
                false);
        mEventBus.registerSticky(this);
        initViews();
        mListView.pull2RefreshManually();

        return mContentView;
    }
    public void onEventMainThread(BusEvent event) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEventBus!=null)
            mEventBus.unregister(this);
    }

    private void startCountDown() {
        mCountDownTask = CountDownTask.create();
        //   mAdapter.setCountDownTask(mCountDownTask);
    }

    private void cancelCountDown() {
        // mAdapter.setCountDownTask(null);
        mCountDownTask.cancel();
    }


    @Override
    public void onResume() {
        super.onResume();
        // startCountDown();
        if (isshow) {
            onRefresh();
        }

        isshow = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        // cancelCountDown();
    }

    private void initViews() {
        parentActivity = getActivity();
        mLoadingDialog = new LoadingDialog(getActivity());
        mListView = (PullToRefreshListViewEx) mContentView.findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyViewText("");
        mListView.mEmptyView = null;
        mAdapter = new FollowsAdapter(getActivity());
        mAdapter.setListener(this);
//        mAdapter.setListener(new PinHuoNewAdapter2.PinHuoNewListener() {
//            @Override
//            public void focusStatChanged() {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd));
//                loadData();
//            }
//        });
        mListView.setAdapter(mAdapter);
        mContentView.findViewById(R.id.btn_pin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_CHANGE_SHOPCAT));
                parentActivity.finish();
            }
        });

    }


    private void showMenu(final GoodBaseInfo info) {
        SelectSizeColorMenu menu = new SelectSizeColorMenu(getActivity(), info);
        menu.show();

    }

    private void getItemInfo(int id) {
        HttpRequestHelper.HttpRequest httpRequest = mRequestHelper.getRequest(getActivity(),
                RequestMethod.ShopMethod.SHOP_GET_ITEM_BASE_INFO, this);
        httpRequest.addParam("id", id + "");
        httpRequest.setConvert2Class(GoodBaseInfo.class);
        httpRequest.doPost();
    }

    private void loadData() {
        isTasking = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
        //QuickSaleApi.getMyFocusList(getActivity(), mRequestHelper, this, PAGE_INDEX, PAGE_SIZE);
        QuickSaleApi.getMyNewFocusList(getActivity(), mRequestHelper, this, PAGE_INDEX, PAGE_SIZE);

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
            mListView.setCanLoadMore(true);
            loadData();
        }
    }

    @Override
    public void onRequestStart(String method) {
        super.onRequestStart(method);
        if (RequestMethod.QuickSaleMethod.GET_NEW_FOCUS_LIST.equals(method)) {
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
        if (RequestMethod.QuickSaleMethod.GET_NEW_FOCUS_LIST.equals(method)) {
            onDataLoaded(object);
        }
        if (RequestMethod.QuickSaleMethod.SAVE_FOCUS.equals(method)) {
            loadData();
        }
    }

    private void onDataLoaded(Object obj) {
        try {
            List<FollowsBean> data = GsonHelper.jsonToObject(obj.toString(),
                    new TypeToken<List<FollowsBean>>() {
                    });
            if (mIsRefresh) {
                mListData.clear();
                mListData.addAll(data);
                if (mListData.size() > 0) {
                    mListView.setVisibility(View.VISIBLE);
                    mContentView.findViewById(R.id.empty_view).setVisibility(View.GONE);
                } else {
                    mListView.setVisibility(View.GONE);
                    mContentView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                }
            } else {
                if (ListUtils.isEmpty(data)) {
                    mListView.setCanLoadMore(false);
                } else {
                    mListData.addAll(data);
                }

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
    }


    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        super.onRequestExp(method, msg, data);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg);
    }

    public void search(String keyword) {
        mKeyword = keyword;
        mNeedToClearKeyword = false;
        mListView.pull2RefreshManually();
    }

    @Override
    public void onRemoveFollowLongClick(final FollowsBean item) {
        if (item == null)
            return;
        QuickSaleApi.saveFocus(mActivity, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {

                mLoadingDialog.start("取消关注中...");

            }

            @Override
            public void onRequestSuccess(String method, Object object) {
                mLoadingDialog.stop();
                if (mAdapter!=null)
                    mAdapter.removeFollow(item);
                if (mListView!=null)
                    mListView.onRefreshComplete();
                if (!ListUtils.isEmpty(mListData)) {
                    mListView.setVisibility(View.VISIBLE);
                    mContentView.findViewById(R.id.empty_view).setVisibility(View.GONE);
                } else {
                    mListView.setVisibility(View.GONE);
                    mContentView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
                }
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
                ViewHub.showLongToast(mActivity, "已取消关注");
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                mLoadingDialog.stop();
                ViewHub.showShortToast(mActivity, msg);
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                mLoadingDialog.stop();
                ViewHub.showShortToast(mActivity, msg);
            }
        }, item.getShopID());

    }
}

