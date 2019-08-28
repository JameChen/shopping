package com.nahuo.quicksale;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.adapter.MyGoodsAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.controls.SelectSizeColorMenu;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.UserModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 诚 on 2015/9/29.
 */
public class GoodsFrament extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    private GoodsFrament vThis = this;
    private PullToRefreshListViewEx mListView;
    private MyGoodsAdapter mAdapter;

    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean isTasking = false;
    private boolean mIsRefresh = true;
    private ShopItemListModel mRemovedItem;
    private Map<Integer, GoodBaseInfo> mGoodBaseInfos = new HashMap<Integer, GoodBaseInfo>();

    private View mContentView;
    private LoadingDialog mLoadingDialog;

    private boolean isshow = false;
    private String mKeyword = "";
    private boolean mNeedToClearKeyword;
    private MyCountDownTimer mCountDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_my_collections, container,
                false);

        initViews();
        mListView.pull2RefreshManually();

        return mContentView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isshow) {
            onRefresh();
        }

        isshow = true;
    }


    private void initViews() {
        mLoadingDialog = new LoadingDialog(getActivity());
        mListView = (PullToRefreshListViewEx) mContentView.findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyViewText("您还没有已拼商品");
        //mListView.mEmptyView = null;
        mAdapter = new MyGoodsAdapter(getActivity());
        mAdapter.setListener(new MyGoodsAdapter.Listener() {
            @Override
            public void onItemClick(ShopItemListModel item) {
                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                startActivity(intent);
            }

            public void onRemoveCollectionsClick(ShopItemListModel item) {
                mRemovedItem = item;
                QuickSaleApi.removeFavorite(getActivity(), mRequestHelper, GoodsFrament.this, item.getID());
            }
        });
        mAdapter.setIBuyClickListener(new MyGoodsAdapter.IBuyClickListener() {
            @Override
            public void buyOnClickListener(ShopItemListModel model) {
                if (mGoodBaseInfos.containsKey(model.getID())) {
                    showMenu(mGoodBaseInfos.get(model.getID()));
                } else {
                    getItemInfo(model.getID());
                }
            }
        });
        mListView.setAdapter(mAdapter);

        mCountDownTimer = new MyCountDownTimer();
        mCountDownTimer.start();
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer() {
            super(999999999, 1000);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFinish() {
        }
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
        QuickSaleApi.getQsOrderList(getActivity(), mRequestHelper, this, PAGE_INDEX, PAGE_SIZE, mKeyword);
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
        super.onRequestStart(method);
        if (RequestMethod.ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
            mLoadingDialog.setMessage("正在读取商品信息....");
            mLoadingDialog.show();
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        if (RequestMethod.QuickSaleMethod.GET_QSORDER.equals(method)) {
            onDataLoaded(object);
        } else if (RequestMethod.QuickSaleMethod.REMOVE_FAVORITE.equals(method)) {

            mAdapter.remove(mRemovedItem);
            ViewHub.showLongToast(getActivity(), "取消收藏成功");
        } else if (RequestMethod.ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
            GoodBaseInfo info = (GoodBaseInfo) object;
            if (info != null) {
                mGoodBaseInfos.put(info.ItemID, info);
                showMenu(info);
            }
        } else if (RequestMethod.QuickSaleMethod.USERS_SIGNATURE.equals(method)) {
            onSignatureLoaded(object);
        }
    }


    private void onSignatureLoaded(Object object) {
        List<UserModel> users = (List<UserModel>) object;
        Map<Integer, String> map = new HashMap<>();
        for (UserModel user : users) {
            map.put(user.getUserID(), user.getSignature());
        }
        mAdapter.setSignatures(map);

    }

    private void onDataLoaded(Object obj) {
        loadFinished();
        List<ShopItemListModel> data = (List<ShopItemListModel>) obj;
        // List<ShopItemListModel> data = root.getDatas();
        if (mIsRefresh) {
            mAdapter.setData(data);
        } else {
            mListView.setCanLoadMore(!ListUtils.isEmpty(data));
            mAdapter.addDataToTail(data);
        }
        mAdapter.notifyDataSetChanged();

        QuickSaleApi.getUserSignatures(getActivity(), mRequestHelper, this, data);
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
}

