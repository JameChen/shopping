package com.nahuo.quicksale.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.BaseFragment;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.IntoGroupAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout;
import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link IntoGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntoGroupFragment extends BaseFragment implements IRefreshLayout.RefreshCallBack {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String ARG_PARAM2 = "param2";
    private View mContentView, empty_view;
    private TextView tv_empty;
    private LoadingDialog mLoadingDialog;
    private String mKeyword = "";
    private boolean mNeedToClearKeyword;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayoutEx mRefreshLayout;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean isTasking = false;
    private boolean mIsRefresh = true;
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private Activity vThis ;
    private IntoGroupAdapter mAdapter;
    public IntoGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment IntoGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntoGroupFragment newInstance(int param1) {
        IntoGroupFragment fragment = new IntoGroupFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vThis= getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PAGE, -1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContentView = inflater.inflate(R.layout.fragment_into_group, container, false);
        initViews();
        return mContentView;
    }

    private void initViews() {
        mLoadingDialog = new LoadingDialog(getActivity());
        empty_view = mContentView.findViewById(R.id.empty_view);
        tv_empty = (TextView) mContentView.findViewById(R.id.tv_empty);
        empty_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        // mListView = (PullToRefreshListViewEx) mContentView.findViewById(R.id.listview);
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.recycler_view);
        mRefreshLayout = (SwipeRefreshLayoutEx) mContentView.findViewById(R.id.refresh_layout);
        GridLayoutManager gridManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridManager);
        mAdapter = new IntoGroupAdapter(vThis);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setCallBack(this);
        mRefreshLayout.setCanLoadMore(true);
        mRefreshLayout.manualRefresh();
        mAdapter.setListener(new IntoGroupAdapter.Listener() {
            @Override
            public void onItemClick(ShopItemListModel item) {
                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                startActivity(intent);
            }

        });

    }

    private void loadData() {
        isTasking = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
        //QuickSaleApi.getQsOrderList(getActivity(), mRequestHelper, this, PAGE_INDEX, PAGE_SIZE, mKeyword);
        QuickSaleApi.getBuyerItemList(getActivity(), mRequestHelper, this, PAGE_INDEX, PAGE_SIZE, mParam1);
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
    public void onLoadMore() {
        if (!isTasking) {
            mIsRefresh = false;
            loadData();
        }
    }

    private void loadFinished() {
        isTasking = false;
        mRefreshLayout.completeRefresh();
        mRefreshLayout.completeLoadMore();
        // mListView.onRefreshComplete();
        // mListView.onLoadMoreComplete();
    }

    @Override
    public void onRequestStart(String method) {
        super.onRequestStart(method);
        if (RequestMethod.QuickSaleMethod.GETBUYERITEMLIST.equals(method)) {
            mLoadingDialog.setMessage("正在读取商品信息....");
            mLoadingDialog.show();
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        if (RequestMethod.QuickSaleMethod.GETBUYERITEMLIST.equals(method)) {
            onDataLoaded(object);
        }
    }

    private void onDataLoaded(Object obj) {
        try {
            loadFinished();
            List<ShopItemListModel> data = (List<ShopItemListModel>) obj;
            if (mIsRefresh) {
                if (ListUtils.isEmpty(data)) {
                    empty_view.setVisibility(View.VISIBLE);
                    if (mParam1==0){
                        tv_empty.setText("无数据，如订单未成团，系统会在场次结束后，退款到您的天天账户余额，在“我的钱包”可查看。");
                    }else {
                        tv_empty.setText("无数据，如订单拼成团，场次结束后，买手会立即向档口报单采购。");
                    }
                } else {
                    empty_view.setVisibility(View.GONE);
                }
                mAdapter.setData(data);
            } else {
                //  mListView.setCanLoadMore(!ListUtils.isEmpty(data));
                mAdapter.addDataToTail(data);
            }
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg);
        ViewHub.showShortToast(vThis, msg);
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        super.onRequestExp(method, msg, data);
        ViewHub.showShortToast(vThis, msg);
    }
}
