package com.nahuo.quicksale;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.quicksale.adapter.CouponAdapter;
import com.nahuo.quicksale.api.CouponAPI;
import com.nahuo.quicksale.base.BaseNewFragment;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.CouponModel;
import com.nahuo.quicksale.oldermodel.ShortTimeModel;
import com.nahuo.quicksale.util.RxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nahuo.quicksale.R.string.items_loadData_loading;

/**
 * Created by ALAN on 2017/4/24 0024.
 */
public class CouponFragment extends BaseNewFragment implements PullToRefreshListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener {
    private static final String TAG = CouponFragment.class.getSimpleName();
    private Context mContext, vthis;
    private PullToRefreshListViewEx pullRefreshListView;
    private View emptyView;

    private List<CouponModel> modelList = new ArrayList<CouponModel>();

    private CouponAdapter adapter;

    private static final String COUPON_BUNDLE = "COUPON_BUNDLE";

    private LoadingDialog mloadingDialog;

    private int type = -1;

    private int mPageIndex = 1;
    private int mPageSize = 20;

    private Task task;

    private TextView tvEmptyMessage;


    public static CouponFragment getInstance(List<CouponModel> list) {
        CouponFragment fragment = new CouponFragment();
        Bundle args = new Bundle();
        ShortTimeModel item = new ShortTimeModel(list);
        args.putSerializable(COUPON_BUNDLE, item);
        fragment.setArguments(args);
        return fragment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        vthis = mContext;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_coupon_list, container, false);
        initView(v);
        return v;
    }

    private void initView(View contentView) {
        mloadingDialog = new LoadingDialog(vthis);
        pullRefreshListView = (PullToRefreshListViewEx) contentView.findViewById(R.id.coupon_pull_refresh_listview_items);
        pullRefreshListView.setEmptyViewText("");
        pullRefreshListView.setCanLoadMore(true);
        pullRefreshListView.setCanRefresh(true);
        pullRefreshListView.setMoveToFirstItemAfterRefresh(true);
        pullRefreshListView.setOnRefreshListener(this);
        pullRefreshListView.setOnLoadListener(this);
        emptyView = contentView.findViewById(R.id.coupon_bill_empty);
        ImageView icon = (ImageView) emptyView.findViewById(R.id.empty_icon);
        icon.setImageResource(R.drawable.ic_no_coupon);
        tvEmptyMessage = (TextView) emptyView
                .findViewById(R.id.layout_empty_tvMessage);

        // 刷新数据
        showEmptyView(false, "");

        emptyView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pullRefreshListView.pull2RefreshManually();

                if (pullRefreshListView != null) {
                    if (pullRefreshListView.isCanRefresh())
                        pullRefreshListView.onRefreshComplete();
                    if (pullRefreshListView.isCanLoadMore())
                        pullRefreshListView.onLoadMoreComplete();
                }

            }
        });

        adapter = new CouponAdapter(mContext, modelList);
        pullRefreshListView.setAdapter(adapter);
        //ShortTimeModel  item=(ShortTimeModel)getArguments().getSerializable(COUPON_BUNDLE);
        type = getType();
        if (type != -1) {
            adapter.setType(type);
            initData();
        }
    }

    private void initData() {
//        task = new Task(type, true);
//        task.execute((Void) null);
        getCouponList(type, true);
    }

    @Override
    public void onLoadMore() {
        bindItemData(false);
    }

    @Override
    public void onRefresh() {

        bindItemData(true);
        if (modelList.size() == 0) {
            showEmptyView(false, "暂时没有相关优惠券哦");
        } else {

        }
    }

    private void bindItemData(boolean isRefresh) {
        if (isRefresh) {
            showEmptyView(false, ""); // 开始执行刷新操作时，不显示空数据视图
            mPageIndex = 1;
        } else {
            mPageIndex++;
        }
//        task = new Task(type, isRefresh);
//        task.execute((Void) null);
        getCouponList(type, isRefresh);
    }

    /**
     * 显示空数据视图
     */
    private void showEmptyView(boolean show, String msg) {
        pullRefreshListView.setVisibility(show ? View.GONE : View.VISIBLE);
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(msg)) {
            if (getActivity() != null)
                tvEmptyMessage.setText(R.string.layout_empty_message);
        } else {
            tvEmptyMessage.setText(msg);
        }
    }

    private void initAdapter(List<CouponModel> modelList) {
        adapter.setList(modelList);
    }

    private void getCouponList(int type, final boolean mIsRefresh) {
        Map<String, Object> params = new HashMap<>();
        params.put("pageindex", mPageIndex);
        params.put("pagesize", mPageSize);
        params.put("statuid", type);
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                .getCouponList(params)
                .compose(RxUtil.<PinHuoResponse<List<CouponModel>>>rxSchedulerHelper())
                .compose(RxUtil.<List<CouponModel>>handleResult())
                .subscribeWith(new CommonSubscriber<List<CouponModel>>(vthis, true, R.string.items_loadData_loading) {
                    @Override
                    public void onNext(List<CouponModel> couponModels) {
                        super.onNext(couponModels);
                        if (mIsRefresh) {
                            pullRefreshListView.onRefreshComplete();
                        } else {
                            pullRefreshListView.onLoadMoreComplete();
                        }
                        if (!ListUtils.isEmpty(couponModels)) {
                            //数据添加进去
                            if (mIsRefresh) {
                                modelList = couponModels;
                            } else {
                                modelList.addAll(couponModels);
                            }
                        }
                        pullRefreshListView.setCanLoadMore(!ListUtils.isEmpty(couponModels));
                        if (!ListUtils.isEmpty(modelList)) {
                            showEmptyView(false, "");
                        } else {
                            showEmptyView(true, "暂时没有相关优惠券哦");
                        }

                        initAdapter(modelList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mIsRefresh) {
                            pullRefreshListView.onRefreshComplete();
                        } else {
                            pullRefreshListView.onLoadMoreComplete();
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (mIsRefresh) {
                            pullRefreshListView.onRefreshComplete();
                        } else {
                            pullRefreshListView.onLoadMoreComplete();
                        }
                    }
                }));
    }

    //获取我的优惠券
    private class Task extends AsyncTask<Void, Void, String> {

        private int type = -1;//(0:未使用,1:已使用,2:已过期)

        private boolean mIsRefresh = false;

        List<CouponModel> list;

        public Task(int type, boolean isRefresh) {
            this.type = type;
            mIsRefresh = isRefresh;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mloadingDialog.start(getString(items_loadData_loading));
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                list = CouponAPI.getInstance().getCouponList(getContext(), mPageSize, mPageIndex, type);
                if (!ListUtils.isEmpty(list)) {
                    //数据添加进去
                    if (mIsRefresh) {
                        modelList = list;
                    } else {
                        modelList.addAll(list);
                    }
                }
                return "OK";
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mloadingDialog.stop();

            pullRefreshListView.setCanLoadMore(!ListUtils.isEmpty(list));
            if (!ListUtils.isEmpty(modelList)) {
                showEmptyView(false, "");
            } else {
                showEmptyView(true, "暂时没有相关优惠券哦");
            }

            initAdapter(modelList);
            if (mIsRefresh) {
                pullRefreshListView.onRefreshComplete();
            } else {
                pullRefreshListView.onLoadMoreComplete();
            }
        }
    }
}
