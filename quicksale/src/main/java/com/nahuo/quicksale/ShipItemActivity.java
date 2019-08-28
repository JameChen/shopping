package com.nahuo.quicksale;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.adapter.ShipItemAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.RequestMethod.OrderMethod;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.ShipItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/***
 * @description 配货单
 * @created 2015年5月6日 下午6:06:22
 * @author JorsonWong
 */
public class ShipItemActivity extends BaseActivity2 implements OnClickListener {

    private LinearLayout        mLlTipContent;
    private TextView            mTvTipText;
    private TextView            mTvEmpty;
    private TextView            mTvWhatIsPicking;
    private ListView            mListView;

    private HttpRequestHelper   mHttpRequestHelper = new HttpRequestHelper();
    private ShipItemAdapter     mAdapter;
    private List<ShipItem>      mPickingOrders;
    private TextView            mTvTime;

    private static final String TAG                = "ShipItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ship_item);

        mPickingOrders = SpManager.getShipItems(getApplicationContext(), SpManager.getUserId(getApplicationContext()));
        setTitle(R.string.ship_order);
        setRightText(R.string.refresh);
        initView();
        init();
    }

    private void init() {
        mAdapter = new ShipItemAdapter(this);
        mListView.setAdapter(mAdapter);
        if (mPickingOrders != null && !mPickingOrders.isEmpty()) {
            mAdapter.setPickOrders(mPickingOrders);
            mAdapter.notifyDataSetChanged();
            
            mLlTipContent.setVisibility(View.GONE);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            long time = SpManager
                    .getShipItemTime(getApplicationContext(), SpManager.getUserId(getApplicationContext()));
            Date date = new Date(time);
            String str = format.format(date);
            mTvTime.setText(str + "  数据");
            mTvTime.setVisibility(View.VISIBLE);
        } else {
            mTvTime.setVisibility(View.GONE);
            mLlTipContent.setVisibility(View.VISIBLE);
            mTvEmpty.setVisibility(View.GONE);
            mTvTipText.setVisibility(View.VISIBLE);
            mTvWhatIsPicking.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        mLlTipContent = (LinearLayout)findViewById(R.id.ll_tip);
        mTvTipText = (TextView)findViewById(R.id.tv_tip);
        mTvEmpty = (TextView)findViewById(R.id.tv_empty);
        mTvWhatIsPicking = (TextView)findViewById(R.id.tv_what_picking);
        mTvTime = (TextView)findViewById(R.id.tv_time);
        mTvWhatIsPicking.setOnClickListener(this);
        mListView = (ListView)findViewById(android.R.id.list);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_what_picking:
                // Intent newIntent = new Intent(getApplicationContext(), NewsActivity.class);
                // startActivity(newIntent);
                Intent newIntent = new Intent(getApplicationContext(), NewsDetailActivity.class);
                newIntent.putExtra("ID", 84288);
                startActivity(newIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRightClick(View v) {
        refresh();
        super.onRightClick(v);
    }

    private void refresh() {
        HttpRequest r = mHttpRequestHelper.getRequest(getApplicationContext(), OrderMethod.GET_SHIP_ITEMS, this);
        r.setConvert2Token(new TypeToken<List<ShipItem>>() {});
        r.doPost();
    }

    @Override
    public void onRequestStart(String method) {
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.start();
        super.onRequestStart(method);
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (OrderMethod.GET_SHIP_ITEMS.equals(method)) {
            try {
                mPickingOrders = (List<ShipItem>)object;
            } catch (Exception e) {
                e.printStackTrace();
            }
            SpManager.setShipItems(getApplicationContext(), mPickingOrders,
                    SpManager.getUserId(getApplicationContext()));
            if (mPickingOrders != null && !mPickingOrders.isEmpty()) {
                mAdapter.setPickOrders(mPickingOrders);
                mAdapter.notifyDataSetChanged();
                mLlTipContent.setVisibility(View.GONE);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                String str = format.format(new Date());
                mTvTime.setText(str + "  数据");
                mTvTime.setVisibility(View.VISIBLE);
            } else {
                mTvTime.setVisibility(View.GONE);
                mLlTipContent.setVisibility(View.VISIBLE);
                mTvEmpty.setVisibility(View.VISIBLE);
                mTvTipText.setVisibility(View.GONE);
                mTvWhatIsPicking.setVisibility(View.GONE);
            }
        }
        super.onRequestSuccess(method, object);
    }
}
