package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.adapter.PinHuoForecastAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.SimpleHttpRequestListener;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout;
import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel;
import com.nahuo.quicksale.util.LoadGoodsTask;

import java.util.Calendar;
import java.util.Date;

import de.greenrobot.event.EventBus;

/**
 * 往期拼货页 2016-4-10新版本
 */
public class PinHuoNewOveredActivity extends FragmentActivity implements View.OnClickListener, IRefreshLayout.RefreshCallBack {
    private static final String TAG = PinHuoNewOveredActivity.class.getSimpleName();
    private LoadingDialog mLoadingDialog;
    private ListView mListView;
    private PinHuoForecastAdapter mAdapter;
    private SwipeRefreshLayoutEx mRefreshLayout;

    private boolean mIsLoading;
    private int cid, qsid;
    private String cName;
    private PinHuoNewResultModel data;
    private View headerView, mNextActivitysView;
    private TextView mTvNextActivity;
    private Context mContext = this;

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_Name = "EXTRA_Name";
    public static final String EXTRA_QSID = "EXTRA_QSID";
    private CircleTextView circleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "往期回顾页");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pin_huo_new_overed);
        Intent intent = getIntent();
        cid = intent.getIntExtra(EXTRA_ID, 0);
        qsid = intent.getIntExtra(EXTRA_QSID, 0);
        cName = intent.getStringExtra(EXTRA_Name);
        initViews();
        loadData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {
        mLoadingDialog = new LoadingDialog(this);
        circleTextView = (CircleTextView) findViewById(R.id.circle_car_text);
        $(R.id.iv_back).setOnClickListener(this);
        ((TextView) $(R.id.iv_title)).setText(cName);
        $(R.id.iv_shopping_cart).setOnClickListener(this);

        mNextActivitysView = $(R.id.ll_next_activity);
        mTvNextActivity = $(R.id.ll_next_activity_time);
        $(R.id.btn_next_activity).setOnClickListener(this);

        //列表
        mRefreshLayout = $(R.id.refresh_layout);
        mRefreshLayout.setCallBack(this);
        mListView = $(R.id.listview);
        mAdapter = new PinHuoForecastAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadGoodsTask(this, circleTextView).execute();
    }

    protected <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    private void loadData(boolean showLoading) {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (showLoading) {
            showLoading();
        }
        QuickSaleApi.getPinHuoNewOveredList(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {

            @Override
            public void onRequestSuccess(String method, Object object) {
                data = (PinHuoNewResultModel) object;
                dataLoaded();
                loadFinished();
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                loadFinished();
                onLoadPinAndForecastFailed();
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                loadFinished();
                onLoadPinAndForecastFailed();
            }
        }), cid,qsid, 1, 100);
    }


    public void showLoading() {
        mLoadingDialog.start();
    }

    public void hideLoading() {
        mLoadingDialog.stop();
    }

    private void loadFinished() {
        hideLoading();
        mIsLoading = false;
        mRefreshLayout.completeRefresh();
    }

    private void dataLoaded() {
        for (PinHuoModel pm : data.Activitys) {
            pm.isOvered = !pm.IsStart;
        }
        //列表
        mAdapter.setData(data.Activitys);
        mAdapter.notifyDataSetChanged();

        if (data.NextAcvivity != null &&
                data.NextAcvivity.getID() > 0) {
            mNextActivitysView.setVisibility(View.VISIBLE);
            long startMillis = TimeUtils.timeStampToMillis(data.NextAcvivity.getStartTime());
            Date startDate = TimeUtils.timeStampToDate(data.NextAcvivity.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startDate);
            int startYear = startCal.get(Calendar.YEAR);
            int startMonth = startCal.get(Calendar.MONTH);
            int startDay = startCal.get(Calendar.DATE);
            int startHour = startCal.get(Calendar.HOUR);

            Calendar cal = Calendar.getInstance();
            int nowYear = cal.get(Calendar.YEAR);
            int nowMonth = cal.get(Calendar.MONTH);
            int nowDay = cal.get(Calendar.DATE);
            int nowHour = cal.get(Calendar.HOUR);

            if (startYear == nowYear && startMonth == nowMonth) {
                if (startDay > nowDay) {
                    if (startDay - nowDay < 1) {//今天
                        mTvNextActivity.setText("今天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "后开拼");
                        return;
                    } else if (startDay - nowDay < 2) {//明天
                        mTvNextActivity.setText("明天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "开拼");
                        return;
                    }
                }
            }
            mTvNextActivity.setText(TimeUtils.millisToTimestamp(startMillis, "MM月dd日HH点") + "开拼");
        } else {
            mNextActivitysView.setVisibility(View.GONE);
        }
    }

    public void onLoadPinAndForecastFailed() {
        loadFinished();
        ViewHub.showShortToast(this, "加载失败，请稍候再试");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_activity:// 查看拼货预告
                String url = data.NextAcvivity.getUrl();
                if (url.indexOf("/xiaozu/topic/") > 1) {
                    String temp = "/xiaozu/topic/";
                    int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));

                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                    mContext.startActivity(intent);
                } else if (url.indexOf("/xiaozu/act/") > 1) {
                    String temp = "/xiaozu/act/";
                    int actID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));

                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ItemPreviewActivity.class);
                    intent.putExtra("name", "拼货预告");
                    intent.putExtra("url", url);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_shopping_cart:
                Utils.gotoShopcart(this);
                break;
        }
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    @Override
    public void onLoadMore() {

    }
}
