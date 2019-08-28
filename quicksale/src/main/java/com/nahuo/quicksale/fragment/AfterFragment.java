package com.nahuo.quicksale.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.nahuo.bean.AfterBean;
import com.nahuo.bean.OrderListBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.AfterSaleListActivity;
import com.nahuo.quicksale.activity.CustomerServiceActivity;
import com.nahuo.quicksale.adapter.AfterAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.ResultData;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AfterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AfterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AfterFragment extends Fragment implements HttpRequestListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PAGE = "ARG_PAGE";
    private AfterSaleListActivity vThis;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HttpRequestHelper httpRequestHelper = new HttpRequestHelper();
    private AfterAdapter adapter;
    private boolean mIsRefresh = true;
    private static final int PAGE_SIZE = 20;
    private static int PAGE_INDEX = 1;
    private LoadingDialog mLoadingDialog;
    private int statuID = -1;
    public String keyword = "";
    private TextView tv_empty_text;

    public AfterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AfterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AfterFragment newInstance(int param1) {
        AfterFragment fragment = new AfterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PAGE, -1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (mParam1 == 0) {
            statuID = -1;
        } else {
            statuID = mParam1;
        }
    }

    protected boolean isDialogShowing() {
        return (mLoadingDialog != null && mLoadingDialog.isShowing());
    }

    protected void hideDialog() {
        if (isDialogShowing()) {
            mLoadingDialog.stop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_after, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        tv_empty_text = (TextView) view.findViewById(R.id.tv_empty_text);
        mLoadingDialog = new LoadingDialog(vThis);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(vThis));
        adapter = new AfterAdapter(null, vThis);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                onLoadDataMore();
            }
        });
//        adapter.setOrderID(orderID);
//        adapter.setmListener(this);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(final BaseQuickAdapter adapter, final View view, final int position) {
                try {
                    List<AfterBean> list = adapter.getData();
                    AfterBean bean = list.get(position);
                    Intent intent = new Intent(vThis, CustomerServiceActivity.class);
                    intent.putExtra(CustomerServiceActivity.EXTRA_TYPE, CustomerServiceActivity.TYPE_AFTER_SALES_APPLY_DETAIL);
                    intent.putExtra(CustomerServiceActivity.EXTRA_APPLYID, bean.getID());
                    vThis.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        loadData(true);
        return view;
    }

    private void onLoadDataMore() {
        loadData(false);
    }

    private void onRefreshData() {
        adapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        loadData(true);
    }

    private void loadData(boolean isRefresh) {
        mIsRefresh = isRefresh;
        PAGE_INDEX = isRefresh ? 1 : ++PAGE_INDEX;
        OrderAPI.getCsorderList(vThis, httpRequestHelper, this, keyword, statuID, PAGE_INDEX, PAGE_SIZE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        vThis = (AfterSaleListActivity) getActivity();
       // vThis.setmLister(this);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRequestStart(String method) {
        if (RequestMethod.ShopMethod.GETCSORDERLIST.equals(method)) {
            if (!isDialogShowing()) {
                mLoadingDialog.setMessage("正在获取列表数据...");
                mLoadingDialog.show();
            }
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (RequestMethod.ShopMethod.GETCSORDERLIST.equals(method)) {
            hideDialog();
            onDataLoaded(object);
        }
    }

    private void onDataLoaded(Object object) {
        try {
            if (mIsRefresh) {
                adapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
            OrderListBean bean = (OrderListBean) object;
            if (bean != null) {
                List<AfterBean> data = bean.getOrderList();
                final int size = ListUtils.isEmpty(data) ? 0 : data.size();
                if (mIsRefresh) {
                    adapter.setNewData(null);
                    adapter.removeAllHeaderView();
                    if (ListUtils.isEmpty(data)) {
                        tv_empty_text.setVisibility(View.VISIBLE);
                    } else {
                        tv_empty_text.setVisibility(View.GONE);
                        adapter.setNewData(data);
                    }
                } else {
                    if (ListUtils.isEmpty(data)) {

                    } else {
                        adapter.addData(data);
                    }
                }
                if (size < PAGE_SIZE) {
                    //第一页如果不够一页就不显示没有更多数据布局
                    adapter.loadMoreEnd(mIsRefresh);
                } else {
                    adapter.loadMoreComplete();
                }
            } else {
                if (mIsRefresh)
                    adapter.setNewData(null);
            }
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishLoadFail() {
        if (adapter != null) {
            if (mIsRefresh) {
                adapter.setEnableLoadMore(true);
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                adapter.loadMoreFail();
            }
        }

    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        if (RequestMethod.ShopMethod.GETCSORDERLIST.equals(method)) {
            hideDialog();
            finishLoadFail();
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (RequestMethod.ShopMethod.GETCSORDERLIST.equals(method)) {
            hideDialog();
            finishLoadFail();
        }
    }

    public void search() {
        onRefreshData();
    }

    public void chang(String msg) {
        keyword = msg;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
