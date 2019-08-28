package com.nahuo.quicksale;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.adapter.ShipLogAdapter;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.oldermodel.ShipInfoModel;

import java.util.List;

public class ShipLogActivity extends BaseActivity implements OnClickListener {

    private ListView mListView;
    private ShipLogAdapter mAdapter;
    private Context mContext;
        private LoadingDialog mDialog;
    private boolean mIsLoading, mIsRefresh = true;
    private List<ShipInfoModel> data;
    private String expressCode;
    private String expressName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_ship_info_list);

        expressName = getIntent().getStringExtra("name");
        expressCode = getIntent().getStringExtra("code");

       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        mContext = this;
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    private void initView() {
        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        initListView();
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new ShipLogAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        loadData();
    }

    private void loadData() {
        mIsLoading = true;
        new AsyncTask<Void, Void, Object>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mDialog.start("加载数据...,");
            }

            @Override
            protected Object doInBackground(Void... params) {

                try {
                    data = QuickSaleApi.getShipInfo(mContext, expressName, expressCode);
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error:" + e.getMessage();
                }
                return data;
            }

            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);
                loadFinished();
                if (result instanceof String && ((String) result).startsWith("error:")) {
                    ViewHub.showShortToast(mContext, ((String) result).replace("error:", ""));
                } else {
                    mAdapter.setData(data);
                    mAdapter.notifyDataSetChanged();
                }

            }

        }.execute();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);

        tvTitle.setText("物流数据");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

    }
    private void loadFinished() {
        mIsLoading = false;
        if (mDialog.isShowing()) {
            mDialog.stop();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
        }
    }
}
