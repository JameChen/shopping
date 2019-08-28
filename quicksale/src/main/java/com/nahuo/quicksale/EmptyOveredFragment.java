package com.nahuo.quicksale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.common.Const.PostType;
import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout.RefreshCallBack;
import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;

import de.greenrobot.event.EventBus;


public class EmptyOveredFragment extends BaseFragment {

    public static EmptyOveredFragment newInstance() {
        EmptyOveredFragment fragment = new EmptyOveredFragment();
        return fragment;
    }

    public EmptyOveredFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_empty_overed, container, false);
        initViews();
        return mContentView;
    }

    private void initViews() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
