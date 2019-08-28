package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.adapter.PinHuoForecastAdapter;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.mvp.BaseMVPActivity;

import java.util.ArrayList;

public class PinHuoForecaseActivity extends BaseMVPActivity implements View.OnClickListener {

    private Context mContext = this;
//    private SwipeRefreshLayoutEx mEmptyLayout;
    private ListView mListView;
    private PinHuoForecastAdapter mAdapter;
    public static final String EXTRA_LIST_DATA = "EXTRA_LIST_DATA";
    private ArrayList<PinHuoModel> mListData;
//    private SwipeRefreshLayoutEx mRefreshLayout;
    private View listLayout,emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_huo_forecase);
        initViews();
        initData();
    }

    private void initViews()
    {
        ((TextView) findViewById(R.id.tv_title)).setText("拼货预告");
        Button backBtn = (Button) findViewById(R.id.titlebar_btnLeft);
        backBtn.setVisibility(View.VISIBLE);
        backBtn.setOnClickListener(this);
        //空视图
        emptyLayout = $(R.id.empty_layout);
        $(R.id.btn_know_more).setOnClickListener(this);
        //list视图
        listLayout = $(R.id.list_layout);
        mListView = $(R.id.listview);
        mAdapter = new PinHuoForecastAdapter(mContext);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        mListData = (ArrayList<PinHuoModel>) getIntent().getSerializableExtra(EXTRA_LIST_DATA);
        if (mListData.size()>0) {
            if (!ListUtils.isEmpty(mListData)) {
                mAdapter.setData(mListData);
                mAdapter.notifyDataSetChanged();
            }
            emptyLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            emptyLayout.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.btn_know_more:
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 460);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                startActivity(intent);
                break;
        }
    }
}
