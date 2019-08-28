package com.nahuo.quicksale;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nahuo.quicksale.adapter.PinHuoAdapter;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout.RefreshCallBack;
import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.PinHuoModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 正在拼货
 * Created by ZZB on 2015/10/13.
 */
public class PinHuoFragment extends BaseFragment implements RefreshCallBack {

    private ListView mListView;
    private PinHuoAdapter mAdapter;
    private static final String EXTRA_LIST_DATA = "EXTRA_LIST_DATA";
    private ArrayList<PinHuoModel> mListData;
    private SwipeRefreshLayoutEx mRefreshLayout;

    public static PinHuoFragment getInstance(ArrayList<PinHuoModel> dataList) {
        PinHuoFragment fragment = new PinHuoFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LIST_DATA, dataList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_pin_huo, container, false);
        initViews();
        initData();
        return mContentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mListData = (ArrayList<PinHuoModel>) getArguments().getSerializable(EXTRA_LIST_DATA);
        if (!ListUtils.isEmpty(mListData)) {
            mAdapter.setData(mListData);
            mAdapter.notifyDataSetChanged();
            //关注列表
            mAdapter.getFocusStat();
        }
    }

    private void initViews() {
        mRefreshLayout = $(R.id.refresh_layout);
        mRefreshLayout.setCallBack(this);
        mListView = $(R.id.listview);
        mAdapter = new PinHuoAdapter(mActivity);
        mListView.setAdapter(mAdapter);
    }
    public void onEventMainThread(BusEvent event){
        switch (event.id){
            case EventBusId.LOAD_PIN_HUO_FINISHED:
                mRefreshLayout.completeRefresh();
                break;
        }
    }
    @Override
    public void onRefresh() {
        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
    }

    @Override
    public void onLoadMore() {

    }

}
