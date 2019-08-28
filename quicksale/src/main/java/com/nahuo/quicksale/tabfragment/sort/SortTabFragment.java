package com.nahuo.quicksale.tabfragment.sort;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.SortBean;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.CommonSearchActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.adapter.GridSpacingItemDecoration;
import com.nahuo.quicksale.adapter.SortAdpter;
import com.nahuo.quicksale.adapter.SubSortAdpter;
import com.nahuo.quicksale.base.TabFragment;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.tab.Constant;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.CircleCarTxtUtl;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 *
 * @author James Chen
 * @create time in 2017/12/11 10:55
 */
public class SortTabFragment extends TabFragment implements View.OnClickListener {
    public static String Tag = SortTabFragment.class.getSimpleName();
    private TextView tv_title;
    private ImageView iv_all_search, iv_shopping_cart;
    private CircleTextView carCountTv;
    private Activity Vthis;
    public LoadingDialog mLoadingDialog;
    public SortAdpter mSortAdapter;
    private SubSortAdpter mSubSortAdpter;
    private RecyclerView recycler_main, recycler_sub;
    private SortBean mSortBean;
    private List<SortBean.ListBean> mData = new ArrayList<>();
    private List<SortBean.ListBean.DatasBean> mSubData = new ArrayList<>();
    private int mSelectPos;
    private SmartRefreshLayout refresh_layout;
    private BezierCircleHeader mRefreshHeader;
    private View empty_view, line;
    private TextView tv_empty,btn_reload;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sort, null);
        initTitle();
        initView();
        initData();
        return view;
    }

    private void initView() {
        refresh_layout = (SmartRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRefreshHeader = (BezierCircleHeader) view.findViewById(R.id.header);
        refresh_layout.setPrimaryColorsId(R.color.my_colorPrimary, android.R.color.white);
        //mRefreshHeader.setEnableHorizontalDrag(true);
        refresh_layout.setEnableHeaderTranslationContent(true);
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
        //swipeToLoadLayout.autoRefresh(100);
        refresh_layout.setEnableLoadMore(false);
//        refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
//        refresh_layout.setRefreshing(true);
//        // 设置下拉进度的背景颜色，默认就是白色的
//        refresh_layout.setProgressBackgroundColorSchemeResource(android.R.color.white);
//        // 设置下拉进度的主题颜色
//        // refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
//        refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
//
        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
//        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                // 开始刷新，设置当前为刷新状态
//                //swipeRefreshLayout.setRefreshing(true);
//                // TODO 获取数据
//                initData();
//                // 这个不能写在外边，不然会直接收起来
//                //swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        recycler_sub = (RecyclerView) view.findViewById(R.id.recycler_sub);
        mSubSortAdpter = new SubSortAdpter(Vthis);
        GridLayoutManager gridManager = new GridLayoutManager(Vthis, 3);
        recycler_sub.setLayoutManager(gridManager);
        recycler_sub.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.padding_search_bar), getResources().getDimensionPixelSize(R.dimen.padding_search_bar), getResources().getDimensionPixelSize(R.dimen.padding_search_bar_top), getResources().getDimensionPixelSize(R.dimen.padding_search_bar_bottom), true));
        recycler_sub.setAdapter(mSubSortAdpter);
        recycler_main = (RecyclerView) view.findViewById(R.id.recycler_main);
        mSortAdapter = new SortAdpter(Vthis);
        mSortAdapter.setListener(new SortAdpter.Listener() {
            @Override
            public void onItemClick(SortBean.ListBean item) {
                if (mSubSortAdpter != null) {
                    mSubSortAdpter.setData(item.getDatas());
                    mSubSortAdpter.notifyDataSetChanged();
                }
            }
        });
        LinearLayoutManager linearManager = new LinearLayoutManager(Vthis);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_main.setLayoutManager(linearManager);
        recycler_main.setAdapter(mSortAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusBarHeight(Vthis));
            view.findViewById(R.id.layout_tittle_status).setLayoutParams(params);
            view.findViewById(R.id.layout_tittle_status).setVisibility(View.VISIBLE);
        }


    }

    private void initData() {
        boolean isShowDialog = false;
        if (!isHidden() && mLoadingDialog != null)
            isShowDialog = true;
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNetCacheApi(Tag);
        addSubscribe(pinHuoApi.getRecommendList()
                .compose(RxUtil.<PinHuoResponse<SortBean>>rxSchedulerHelper())
                .compose(RxUtil.<SortBean>handleResult())
                .subscribeWith(new CommonSubscriber<SortBean>(Vthis, isShowDialog, R.string.loading) {
                    @Override
                    public void onNext(SortBean object) {
                        super.onNext(object);
                        recycler_main.setBackgroundColor(ContextCompat.getColor(Vthis,R.color.gray_f1));
                        setRefreshFalse();
                        //    closeDialog(mLoadingDialog);
                        mSortBean = object;
                        if (mSortBean != null) {
                            mData = mSortBean.getList();
                            mSubData = mData.get(mSelectPos).getDatas();
                            mData.get(mSelectPos).isSelect = true;
                            mSortAdapter.setData(mData);
                            mSubSortAdpter.setData(mSubData);
                            mSortAdapter.notifyDataSetChanged();
                            mSubSortAdpter.notifyDataSetChanged();
                        }
                        judeEmpyView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        setRefreshFalse();
                        judeEmpyView();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        setRefreshFalse();
                        judeEmpyView();
                    }
                }));
//        SortApi.getRecommendList(new RequestEntity(Vthis, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                setRefreshFalse();
//                closeDialog(mLoadingDialog);
//                mSortBean = (SortBean) object;
//                if (mSortBean != null) {
//                    mData = mSortBean.getList();
//                    mSubData = mData.get(mSelectPos).getDatas();
//                    mData.get(mSelectPos).isSelect = true;
//                    mSortAdapter.setData(mData);
//                    mSubSortAdpter.setData(mSubData);
//                    mSortAdapter.notifyDataSetChanged();
//                    mSubSortAdpter.notifyDataSetChanged();
//                }
//                judeEmpyView();
//                Log.d(Tag, object.toString());
//            }
//
//            @Override
//            public void onRequestStart(String method) {
//                super.onRequestStart(method);
//                if (!isHidden()&&mLoadingDialog!=null)
//                showDialog(mLoadingDialog, "正在加载数据");
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                super.onRequestExp(method, msg, data);
//                closeDialog(mLoadingDialog);
//                setRefreshFalse();
//                judeEmpyView();
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                super.onRequestFail(method, statusCode, msg);
//                closeDialog(mLoadingDialog);
//                setRefreshFalse();
//                judeEmpyView();
//            }
//        }));
    }

    private void judeEmpyView() {
        if (ListUtils.isEmpty(mData)) {
            empty_view.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
        } else {
            empty_view.setVisibility(View.GONE);
            line.setVisibility(View.VISIBLE);
        }
    }

    private void setRefreshFalse() {
        if (refresh_layout != null)
            refresh_layout.finishRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainNewActivity.currFragTag = Constant.FRAGMENT_FLAG_SORT;
        ChatUtl.setChatBroad(Vthis);
        // new LoadGoodsTask(this, carCountTv).execute();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //   EventBus.getDefault().registerSticky(this);
    }

    private void initTitle() {

        Vthis = getActivity();
        mLoadingDialog = new LoadingDialog(Vthis);
        empty_view = view.findViewById(R.id.empty_view);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        btn_reload = (TextView) view.findViewById(R.id.btn_reload);
        tv_empty.setText(R.string.pin_huo_net_error);
        line = view.findViewById(R.id.line);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (refresh_layout != null)
                    refresh_layout.autoRefresh();
                initData();
            }
        });
        carCountTv = (CircleTextView) view.findViewById(R.id.circle_car_text);
        CircleCarTxtUtl.setColor(carCountTv);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        iv_all_search = (ImageView) view.findViewById(R.id.iv_all_search);
        iv_shopping_cart = (ImageView) view.findViewById(R.id.iv_shopping_cart);
        view.findViewById(R.id.iv_chat_txt).setOnClickListener(this);
        iv_all_search.setOnClickListener(this);
        tv_title.setText(R.string.app_main_sort);

    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.WEIXUN_NEW_MSG:
                if (carCountTv != null)
                    ChatUtl.judeChatNums(carCountTv, event);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chat_txt:
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click31);
                ChatUtl.goToChatMainActivity(Vthis, true);
//                Intent intent = new Intent(this, ChatMainActivity.class);
//                intent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
//                startActivity(intent);
                break;
            case R.id.iv_all_search:
                UMengTestUtls.UmengOnClickEvent(Vthis, UmengClick.Click30);
                CommonSearchActivity.launch(Vthis, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                break;
            case R.id.iv_shopping_cart:
                Utils.gotoShopcart(Vthis);
                break;
        }
    }
}
