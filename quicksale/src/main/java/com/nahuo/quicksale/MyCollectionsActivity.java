package com.nahuo.quicksale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.adapter.MyCollectionsAdapter;
import com.nahuo.quicksale.adapter.MyCollectionsAdapter.IBuyClickListener;
import com.nahuo.quicksale.adapter.MyCollectionsAdapter.Listener;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.RequestMethod.QuickSaleMethod;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.controls.SelectSizeColorMenu;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.ShopItemListResultModel;
import com.nahuo.quicksale.oldermodel.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏
 * Created by ZZB on 2015/9/16.
 */
public class MyCollectionsActivity extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private PullToRefreshListViewEx mListView;
    private MyCollectionsAdapter mAdapter;

    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean isTasking = false;
    private boolean mIsRefresh = true;
    private ShopItemListModel mRemovedItem;
    private Map<Integer, GoodBaseInfo> mGoodBaseInfos = new HashMap<Integer, GoodBaseInfo>();

    private  boolean isshow=false;

    private View mContentView;
    private LoadingDialog mLoadingDialog;
    private String mKeyword = "";
    private boolean mNeedToClearKeyword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_my_follows, container,
                false);

        initViews();
        mListView.pull2RefreshManually();
        return mContentView;
    }

    private void initViews() {
        mLoadingDialog = new LoadingDialog(getActivity());
        mListView = (PullToRefreshListViewEx) mContentView.findViewById(R.id.listview);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyViewText("未找到收藏商品或者拼货已结束");
        mAdapter = new MyCollectionsAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mAdapter.setListener(new Listener() {
            @Override
            public void onItemClick(ShopItemListModel item) {
                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                startActivity(intent);
            }

            @Override
            public void onRemoveCollectionsClick(ShopItemListModel item) {
                mRemovedItem = item;
                QuickSaleApi.removeFavorite(getActivity(), mRequestHelper, MyCollectionsActivity.this, item.getID());
            }
        });
        mAdapter.setIBuyClickListener(new IBuyClickListener() {
            @Override
            public void buyOnClickListener(ShopItemListModel model) {
                if (mGoodBaseInfos.containsKey(model.getID())) {
                    showMenu(mGoodBaseInfos.get(model.getID()));
                } else {
                    getItemInfo(model.getID());
                }
            }
        });
    }

    private void showMenu(final GoodBaseInfo info) {
        SelectSizeColorMenu menu = new SelectSizeColorMenu(getActivity(), info);
        menu.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(isshow){
            onRefresh();

            Log.d("red_count_bg", "onResume ");
        }
        Log.d("red_count_bg", "onResume ");

        isshow=true;
    }



    private void getItemInfo(int id) {
        HttpRequest httpRequest = mRequestHelper.getRequest(getActivity(),
                ShopMethod.SHOP_GET_ITEM_BASE_INFO, this);
        httpRequest.addParam("id", id + "");
        httpRequest.setConvert2Class(GoodBaseInfo.class);
        httpRequest.doPost();
    }

    private void loadData() {
        isTasking = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
        QuickSaleApi.getFavorites(getActivity(), mRequestHelper, this, PAGE_INDEX, PAGE_SIZE, mKeyword);
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
            if(mNeedToClearKeyword){
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
        if (ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
//            mLoadingDialog.setMessage("正在读取商品信息....");
//            mLoadingDialog.show();
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        if (QuickSaleMethod.GET_FAVORITES.equals(method)) {
            onDataLoaded(object);
        } else if (QuickSaleMethod.REMOVE_FAVORITE.equals(method)) {
//            mAdapter
            mAdapter.remove(mRemovedItem);
            ViewHub.showLongToast(getActivity(), "取消收藏成功");
        } else if (ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
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
        List<ShopItemListModel> data = null;
        if(obj instanceof ShopItemListResultModel){
            ShopItemListResultModel root = (ShopItemListResultModel) obj;
            data = root.getDatas();
        }else{
            data = new ArrayList<>();
        }

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

    public void search(String keyword){
        mKeyword = keyword;
        mNeedToClearKeyword = false;
        mListView.pull2RefreshManually();
    }
}
