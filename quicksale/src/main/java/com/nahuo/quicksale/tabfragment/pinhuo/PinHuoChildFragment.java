package com.nahuo.quicksale.tabfragment.pinhuo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.AdBean;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.BaseFragment;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ItemPreview1Activity;
import com.nahuo.quicksale.activity.PinHuoDetailListActivity;
import com.nahuo.quicksale.activity.SortReasonActivity;
import com.nahuo.quicksale.adapter.ImageBannerAdapter;
import com.nahuo.quicksale.adapter.PinHuoMainAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.SimpleHttpRequestListener;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.customview.ViewFlowCircleIndicator;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.loader.GlideImageLoader;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel_Map;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * 广告栏以及倒计时
 *
 * @author James Chen
 * @create time in 2017/3/23 17:58
 */
public class PinHuoChildFragment extends BaseFragment implements HttpRequestListener, OnRefreshListener, SwipeRefreshLayout.OnRefreshListener {
    public static String TAG = PinHuoChildFragment.class.getSimpleName();
    // private ListView mListView;
    //  private PinHuoNewAdapter2 mAdapter;
    private PinHuoMainAdapter adapter;
    private static final String EXTRA_LIST_DATA = "EXTRA_LIST_DATA";
    private static final String EXTRA_AD_LIST_DATA = "EXTRA_AD_LIST_DATA";
    private static final String EXTRA_LIST_CATID = "EXTRA_LIST_CATID";
    private static final String EXTRA_POS = "EXTRA_POS";
    public ArrayList<PinHuoModel> mListData;
    //  private SwipeRefreshLayout mRefreshLayout;
    //private FlowIndicator mLayoutIndicator;
    public List<BannerAdModel> adList;
    public String CustomHtml = "";
    // private BannerAdAdapter mPagerAdapter;
    private MyCountDownTimer mCountDownTimer;
    private View headerView, webHeadView, line;
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private int curCategoryID, pos;
    //private ViewFlow mViewFlow;
    private ViewFlowCircleIndicator mViewFlowIndic;
    private ImageBannerAdapter bAdapter;
    private CountDownTask mCountDownTask;
    private PinHuoNewResultModel newResult;
    // private AppUpdate mAppUpdate;
    private View empty_view, pager_view;
    private ImageView iv_scroll_to_top;
    private TextView tv_empty;
    private int firstVisible, visibleCount, totalCount;
    private AdBean adBean;
    private WebView webView;
    private LoadingDialog mLoadingDialog;
    private Activity activity;
    private static final String EXTR_CID = "EXTR_CID";
    private RecyclerView recyclerView;
    private PinHuoTabFragment pinHuoTabFragment;
    private Banner banner;
    private List<String> images = new ArrayList();
    private RefreshLayout swipeToLoadLayout;
    private BezierCircleHeader mRefreshHeader;

    public static PinHuoChildFragment getInstance(ArrayList<PinHuoModel> dataList, int catId, AdBean bean, int pos) {
        //fragmentManager.findFragmentByTag(catId+"");
        PinHuoChildFragment fragment = new PinHuoChildFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LIST_DATA, dataList);
        args.putSerializable(EXTRA_AD_LIST_DATA, bean);
        args.putInt(EXTRA_LIST_CATID, catId);
        args.putInt(EXTRA_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            onDataLoaded(object);
        }
    }

    Bundle savedState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_pin_huo, container, false);
        savedState = savedInstanceState;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        initViews();
        initData();
        return mContentView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTR_CID, curCategoryID);
        outState.putInt(EXTRA_POS, pos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    String name = "";

    private void initData() {
        adBean = (AdBean) getArguments().getSerializable(EXTRA_AD_LIST_DATA);
        if (adBean != null) {
            adList = adBean.getBannerAdModelList();
        }
        mListData = (ArrayList<PinHuoModel>) getArguments().getSerializable(EXTRA_LIST_DATA);

        //   adList = (List<BannerAdModel>) getArguments().getSerializable(EXTRA_AD_LIST_DATA);
        curCategoryID = getArguments().getInt(EXTRA_LIST_CATID);
        pos = getArguments().getInt(EXTRA_POS);
        if (curCategoryID <= 0 && savedState != null) {
            curCategoryID = savedState.getInt(EXTR_CID);
            pos = savedState.getInt(EXTRA_POS);
        }
        if (!ListUtils.isEmpty(mListData)) {
//            if (!TextUtils.isEmpty(mListData.get(0).getVideo()))
//                mListData.get(0).VideoPlayer_Is_Hide = false;
//            mAdapter.setData(mListData);
//            mAdapter.notifyDataSetChanged();
//            //关注列表
//            mAdapter.getFocusStat();
            if (adapter != null) {
                adapter.setData(mListData);
                adapter.notifyDataSetChanged();
                //关注列表
                adapter.getFocusStat();
            }
        }
        if (pos == 0) {
            getAdList();
            judeEmpty(mListData);
        }
        // flushAdView();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (refreshLayout != null)
            getRefleshData();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
            return true;
            // return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            setEmptyView(false);
            // html加载完成之后，添加监听图片的点击js函数
//                    wv.loadUrl("<script>" +
//                "    function GoToRecharge() {" +
//                "        window.WebViewJavascriptBridge.GoToRecharge()" +
//                "    }" +
//                "    function GoToItemDetail(str, str1) {" +
//                "        window.WebViewJavascriptBridge.GoToItemDetail(str, str1)" +
//                "    }" +
//                "    function GoToQsList(str) {" +
//                "        window.WebViewJavascriptBridge.GoToQsList(str)" +
//                "    }" +
//                "</script>");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initViews() {
        mLoadingDialog = new LoadingDialog(activity);
        //    mAppUpdate = AppUpdateService.getAppUpdate(activity);
//        mRefreshLayout = $(refresh_layout);
//        mRefreshLayout.setEnabled(false);
//        mRefreshLayout.setRefreshing(false);
//        // 设置下拉进度的背景颜色，默认就是白色的
//        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
//        // 设置下拉进度的主题颜色
//        // refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
//        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
//        mRefreshLayout.setOnRefreshListener(this);
//        mListView = $(R.id.listview);
//        mListView.setVisibility(View.GONE);
        swipeToLoadLayout = $(R.id.swipeToLoadLayout);
        mRefreshHeader = $(R.id.header);
        swipeToLoadLayout.setPrimaryColorsId(R.color.my_colorPrimary, android.R.color.white);
        //mRefreshHeader.setEnableHorizontalDrag(true);
        swipeToLoadLayout.setEnableHeaderTranslationContent(true);
        swipeToLoadLayout.setOnRefreshListener(this);
        //swipeToLoadLayout.autoRefresh(100);
        swipeToLoadLayout.setEnableLoadMore(false);
        recyclerView = $(R.id.recyclerView);
        //recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setVisibility(View.VISIBLE);
        // empty_view = $(R.id.empty_view);
        tv_empty = $(R.id.tv_empty);
        iv_scroll_to_top = $(R.id.iv_scroll_to_top);
        iv_scroll_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mListView != null) {
//                    mListView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
//                    mListView.setSelection(0);
//                }
                if (recyclerView != null) {
                    recyclerView.scrollToPosition(0);
                }
            }
        });

        LayoutInflater lif = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = lif.inflate(R.layout.layout_pinhuo_head_ad, null);
        // headerView = lif.inflate(R.layout.layout_pinhuo_head_ad, null);
        webHeadView = lif.inflate(R.layout.layout_pinhuo_ad_webview, null);
        line = lif.inflate(R.layout.ad_line, null);
        empty_view = lif.inflate(R.layout.public_empty_view, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(mActivity) * 2 / 3);
        empty_view.setLayoutParams(layoutParams);
        TextView btn_reload = (TextView) empty_view.findViewById(R.id.btn_reload);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
                onRefresh();
            }
        });
        // mListView.addHeaderView(headerView);

//        View pagerView = headerView.findViewById(R.id.pager_view);
        // ViewPager mPager = (ViewPager) headerView.findViewById(R.id.pager);
        //   mLayoutIndicator = (FlowIndicator) headerView.findViewById(R.id.layout_indicator);
        //pager_view = headerView.findViewById(R.id.pager_view);
        //  mViewFlow = (ViewFlow) headerView.findViewById(R.id.viewflow);
        banner = (Banner) headerView.findViewById(R.id.banner);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(ScreenUtils.getScreenWidth(getActivity())));
        banner.setLayoutParams(params);
        banner.setBannerAnimation(AccordionTransformer.class);
        webView = (WebView) webHeadView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        // webView.getSettings().setBuiltInZoomControls(true);
        //webView.getSettings().setSupportZoom(true);
        webView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webView.setWebViewClient(new MyWebViewClient());
        webView.addJavascriptInterface(new JavascriptInterface(), "wst");
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                float downX=0,downY=0;
//                //在触发时回去到起始坐标
//                float x = event.getX();
//                float y = event.getY();
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        //将按下时的坐标存储
//                        downX = x;
//                        downY = y;
//                        //mListView.requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                       // mListView.requestDisallowInterceptTouchEvent(true);
//                        //获取到距离差
////                        float dx = x - downX;
////                        float dy = y - downY;
//                        //防止是按下也判断
////                        if (Math.abs(dx) > 8 && Math.abs(dy) > 8) {
////                            //通过距离差判断方向
////                            int orientation = getOrientation(dx, dy);
////                            switch (orientation) {
////                                case 'r':
////                                    action = "右";
////                                    listView.requestDisallowInterceptTouchEvent(true);
////                                    break;
////                                case 'l':
////                                    action = "左";
////                                    listView.requestDisallowInterceptTouchEvent(true);
////                                    break;
////                                case 't':
////                                    action = "上";
////
////                                    break;
////                                case 'b':
////                                    action = "下";
////
////                                    break;
////                            }
//                        //}
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        //mListView.requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
                // mListView.requestFocus();
                return false;
            }
        });
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(ScreenUtils.getScreenWidth(getActivity())));
//        mViewFlow.setLayoutParams(params);
        mViewFlowIndic = (ViewFlowCircleIndicator) headerView.findViewById(R.id.viewflowindic);
        //mViewFlow.setParentView(PinHuoTabFragment.mViewPager, mRefreshLayout, mListView);
//        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                mLayoutIndicator.setSelectedPos(position);
//            }
//        });
//        mPagerAdapter = new BannerAdAdapter(mActivity);
//        mPager.setAdapter(mPagerAdapter);

//        mPagerAdapter.setOnItemClickLitener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                BannerAdModel selectAd = adList.get((int) v.getTag());
//                gotoBannerJump(selectAd);
//            }
//        });
        adList = null;
//        mCountDownTimer = new MyCountDownTimer();
//        mCountDownTimer.start();
        //  mAdapter = new PinHuoNewAdapter2(mActivity);
        //mListView.setAdapter(mAdapter);
        adapter = new PinHuoMainAdapter(activity, R.layout.lvitem_pin_huo_pining_new, null);
        //adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
//        layoutManager.setSmoothScrollbarEnabled(true);
//        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView != null) {
                    int scollPos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    if (scollPos > 4) {
                        iv_scroll_to_top.setVisibility(View.VISIBLE);
                    } else {
                        iv_scroll_to_top.setVisibility(View.GONE);
                    }
//                    if (dy<0&&scollPos==0){
//                        recyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
//                    }else {
//                        recyclerView.setNestedScrollingEnabled(true);
//                    }
                }
            }
        });
//        mListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                //mRefreshLayout.setEnabled(false);
//                return false;
//            }
//        });
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
////                boolean enable = false;
////                if (mListView != null && mListView.getChildCount() > 0) {
////                    // check if the first item of the list is visible
////                    boolean firstItemVisible = mListView.getFirstVisiblePosition() == 0;
////                    // check if the top of the first item is visible
////                    boolean topOfFirstItemVisible = mListView.getChildAt(0).getTop() == 0;
////                    // enabling or disabling the refresh layout
////                    enable = firstItemVisible && topOfFirstItemVisible;
////                    //   Log.e("yu", "enable=" + enable);
////                }
////                mRefreshLayout.setEnabled(enable);
////                //mRefreshLayout.invalidate();
////                switch (scrollState) {
////
////                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
////                        Log.e("videoTest", "SCROLL_STATE_FLING");
////                        break;
////
////                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
////                        Log.e("videoTest", "SCROLL_STATE_IDLE");
////                        autoPlayVideo(view);
////                        break;
////
////                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
////                        Log.e("videoTest", "SCROLL_STATE_TOUCH_SCROLL");
////                        break;
////
////                    default:
////                        break;
////                }
//            }
//
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//// firstVisibleItem   当前第一个可见的item
//                // visibleItemCount   当前可见的item个数
//                firstVisible = firstVisibleItem;
//                visibleCount = visibleItemCount;
//                totalCount = totalItemCount;
//                if (firstVisibleItem == 0) { // 只要第一个item有一小部分可见都会满足条件
//                    View firstView = view.getChildAt(firstVisibleItem);
//                    if (firstView != null) {
//                        if (firstView.getTop() == 0) { // 判断第一个item到顶部的距离
//                            iv_scroll_to_top.setVisibility(View.GONE);
//                        }
//                    }
//                }
//                if (mListView != null) {
//                    int scollPos = mListView.getFirstVisiblePosition();
//                    //Log.e("videoTest", "scollPos=="+scollPos);
//                    if (scollPos > 4) {
//                        iv_scroll_to_top.setVisibility(View.VISIBLE);
//                    } else {
//                        iv_scroll_to_top.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
//        mViewFlow.setTimeSpan(3000);
//        mViewFlow.setFlowIndicator(mViewFlowIndic);
//        int size = adList == null ? 0 : adList.size();
//        mViewFlow.setSelection(size * 1000);
//        bAdapter = new ImageBannerAdapter(mActivity,adList);
//        mViewFlow.setAdapter(bAdapter);
//        mViewFlow.startAutoFlowTimer();
//        bAdapter.setOnItemClickLitener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BannerAdModel selectAd = adList.get((int) v.getTag());
//                gotoBannerJump(selectAd);
//            }
//        });
    }

    private void setRefreshFalse() {
//        if (mRefreshLayout != null)
//            mRefreshLayout.setRefreshing(false);
        if (swipeToLoadLayout != null)
            swipeToLoadLayout.finishRefresh();
    }

    /**
     * 根据距离差判断 滑动方向
     *
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        if (Math.abs(dx) > Math.abs(dy)) {
            //X轴移动
            return dx > 0 ? 'r' : 'l';
        } else {
            //Y轴移动
            return dy > 0 ? 'b' : 't';
        }
    }

    private void autoPlayVideo(AbsListView view) {
//        Log.e("yu", "====" + "firstVisible = " + firstVisible +
//                "visibleCount =" + visibleCount +
//                "totalCount =" + totalCount);
//        if (mAdapter != null)
//            mAdapter.autoStartVideo(firstVisible);
//        for (int i = 0; i < visibleCount; i++) {
//            if (view!=null&&view.getChildAt(i)!=null&&view.getChildAt(i).findViewById(R.id.video_player) != null) {
//                ItemJCVideoPlayerStandard videoPlayerStandard1 = (ItemJCVideoPlayerStandard) view.getChildAt(i).findViewById(R.id.video_player);
//                Rect rect = new Rect();
//                videoPlayerStandard1.getLocalVisibleRect(rect);
//                int videoheight3 = videoPlayerStandard1.getHeight();
//                Log.e("videoTest","i="+i+"==="+"videoheight3:"+videoheight3+"==="+"rect.top:"+rect.top+"==="+"rect.bottom:"+rect.bottom);
//                if (rect.top==0&&rect.bottom==videoheight3)
//                {
//                    videoPlayerStandard1.startVideo();
//
//                }
//
//            }
//        }
    }

    private void startCountDown() {
        mCountDownTask = CountDownTask.create();
        //  mAdapter.setCountDownTask(mCountDownTask);
        if (adapter != null)
            adapter.setCountDownTask(mCountDownTask);
    }

    private void cancelCountDown() {
        //mAdapter.setCountDownTask(null);
        if (adapter != null)
            adapter.setCountDownTask(null);
        mCountDownTask.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelCountDown();
//        try {
//            mAppUpdate.callOnPause();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void setEmptyView(boolean isShow) {

        if (isShow) {
//            if (PinHuoTabFragment.empty_view.getVisibility() != View.VISIBLE) {
//                if (adapter != null) {
//                    adapter.removeAllFooterView();
//                    adapter.addFooterView(empty_view);
//                }
//                if (empty_view != null)
//                    empty_view.setVisibility(View.VISIBLE);
//            }
            if (adapter != null) {
                adapter.removeAllFooterView();
                adapter.addFooterView(empty_view);
            }
            if (empty_view != null)
                empty_view.setVisibility(View.VISIBLE);
        } else {
            if (adapter != null)
                adapter.removeAllFooterView();
            if (empty_view != null)
                empty_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCountDown();
//         mListView.requestDisallowInterceptTouchEvent(true);
//         mListView.setEnabled(true);
//        mListView.requestFocus();
//        webView.requestFocusFromTouch() ;
//        webView.setClickable(true);
//        webView.setEnabled(true);
//        try {
//            mAppUpdate.callOnResume();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //new LoadGoodsTask(getActivity(),(CircleTextView) $(R.id.iv_shopping_cart)).execute();
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            //主页弹窗广告接收
            case EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED:
//                List<BannerAdModel> data = (List<BannerAdModel>) event.data;
//                if (data != null) {
//                    PopADMenu menu = PopADMenu.getInstance(getActivity(), this, data, mAppUpdate);
//                    menu.show();
//                }
                break;
            //轮播广告接收
            case EventBusId.LOAD_PIN_HUO_AD_FINISHED:
                PinHuoNewResultModel_Map data2 = (PinHuoNewResultModel_Map) event.data;
                if (data2 == null) {
                    adList = null;
                    //CustomHtml = "<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>";
                    CustomHtml = "";
                    flushAdView();
                } else {
                    if (data2.CurrCategoryID == curCategoryID) {
                        adList = data2.adList;
                        //CustomHtml = "<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>";
                        CustomHtml = data2.getCustomHtml();
                        flushAdView();
                    }
                }
                break;
            case EventBusId.LOAD_PIN_HUO_FINISHED:
                try {
                    PinHuoNewResultModel data1 = (PinHuoNewResultModel) event.data;
                    if (data1 != null) {
//                        if (data1.ActivityList == null || data1.ActivityList.size() == 0) {
//                            setEmptyView(true);
//                        } else {
//                            setEmptyView(false);
//                        }
                        judeEmpty(data1.ActivityList);
                        if (data1.CurrCategoryID == curCategoryID) {
                            mListData = data1.ActivityList;
                            flushListView();
                        }
                    } else {
                        setEmptyView(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EventBusId.REFRESH_COMPLETEd:
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_CURRENT_TAB));
                getRefleshData();
                break;
//            case EventBusId.REFRESH_COMPLETEd1:
//                getRefleshData();
//                break;
//            case EventBusId.REFRESH_PINHUO_CHILD_DATA:
//                int id= (int) event.data;
//                if (id>0){
//                    curCategoryID=id;
//                    getRefleshData();
//                }

            //      break;

        }
    }

    private void flushListView() {
        //   Log.e("yu", "flushListView");
//        mAdapter.setData(mListData);
//        mAdapter.notifyDataSetChanged();
//        //关注列表
//        mAdapter.getFocusStat();
        if (adapter != null) {
            adapter.setData(mListData);
            adapter.notifyDataSetChanged();
            //关注列表
            adapter.getFocusStat();
        }
        //  ListviewUtls.setListViewHeightBasedOnChildren(mListView);
    }

    private void flushAdView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (ListUtils.isEmpty(adList)) {
            params.topMargin = 0;
        } else {
            params.topMargin = ScreenUtils.getPinHuoProportionHeight(ScreenUtils.getScreenWidth(getActivity()));
        }
        tv_empty.setLayoutParams(params);
        //((PinHuoTabFragment) this.getParentFragment()).flushAdView(adList, CustomHtml);
        if (ListUtils.isEmpty(adList) && TextUtils.isEmpty(CustomHtml)) {
//            mListView.removeHeaderView(headerView);
//            mListView.removeHeaderView(line);
//            mListView.removeHeaderView(webHeadView);
            adapter.removeAllHeaderView();
        } else {
//            mListView.removeHeaderView(headerView);
//            mListView.removeHeaderView(line);
//            mListView.removeHeaderView(webHeadView);
            adapter.removeAllHeaderView();
            if (!ListUtils.isEmpty(adList) && !TextUtils.isEmpty(CustomHtml)) {
                //mListView.addHeaderView(headerView);
                adapter.addHeaderView(headerView);
               /* if (line != null)
                    adapter.addHeaderView(line);
                // mListView.addHeaderView(line);
                line.setBackgroundResource(R.color.white);*/
                //mListView.addHeaderView(webHeadView);
                adapter.addHeaderView(webHeadView);
                setViewFlow();
                setWebView();
            } else if (!ListUtils.isEmpty(adList) && TextUtils.isEmpty(CustomHtml)) {
//                mListView.addHeaderView(headerView);
//                mListView.addHeaderView(line);
                adapter.addHeaderView(headerView);
                adapter.addHeaderView(line);
                if (line != null)
                    line.setBackgroundResource(R.color.ad_line_bg);
                setViewFlow();
            } else if (ListUtils.isEmpty(adList) && !TextUtils.isEmpty(CustomHtml)) {
                //mListView.addHeaderView(line);
               /* adapter.addHeaderView(line);
                if (line != null)
                    line.setBackgroundResource(R.color.white);*/
                //  mListView.addHeaderView(webHeadView);
                adapter.addHeaderView(webHeadView);
                setWebView();
            }
        }
        adapter.notifyDataSetChanged();
//        mListView.requestDisallowInterceptTouchEvent(true);
//        mListView.setEnabled(true);
        // mListView.requestFocus();

    }

    private void setViewFlow() {
//        mViewFlow.stopAutoFlowTimer();
//        mViewFlow.setTimeSpan(Constant.ADVERTISE_TIME);
//        mViewFlow.setFlowIndicator(mViewFlowIndic);
//        int size = adList == null ? 0 : adList.size();
//        mViewFlow.setSelection(size * 1000);
//        mViewFlow.setSideBuffer(size);
//        bAdapter = new ImageBannerAdapter(mActivity, adList);
//        mViewFlow.setAdapter(bAdapter);
//        mViewFlow.startAutoFlowTimer();
//        bAdapter.setOnItemClickListener(new ImageBannerAdapter.BannerOnclickListener() {
//            @Override
//            public void onItemClick(View v, int positon) {
//                // Toast.makeText(mActivity, positon + "", Toast.LENGTH_SHORT).show();
//                gotoAd(positon);
//            }
//        });
        if (!ListUtils.isEmpty(adList)) {
            images.clear();
            for (BannerAdModel adModel : adList) {
                images.add(adModel.getImageUrl());
            }
        }
        banner.releaseBanner();
        // banner.stopAutoPlay();
        banner.setImageLoader(new GlideImageLoader());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                // Log.e("yu","==>"+position+"");
                gotoAd(position);
            }
        });
        banner.setImages(images);
        banner.start();
    }

    private void setWebView() {
        StringBuffer html = new StringBuffer();
        html.append("<html>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">"
                + "<head>"
                + "<style type=\"text/css\">"
                + ".wp-item-detail-format.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
                + "*{margin:0px; padding:0px;word-break:break-all;}"
                + "</style>" + "</head>" + "<body>"
                + "<div class=wp-item-detail-format>");
        html.append(CustomHtml);
        html.append("</div>");
        html.append("<script>" +
                "    function GoToRecharge() {" +
                "        window.wst.GoToRecharge()" +
                "    }" +
                "    function GoToItemDetail(str, str1) {" +
                "        window.wst.GoToItemDetail(str, str1)" +
                "    }" +
                "    function GoToQsList(str) {" +
                "        window.wst.GoToQsList(str)" +
                "    }"
                +
                "    function GoToGroupDetail(str, str1) {" +
                "        window.wst.GoToGroupDetail(str, str1)" +
                "    }"
                +
                "    function GoToUrl(str, str1) {" +
                "        window.wst.GoToUrl(str, str1)" +
                "    }"
                +
                "    function GoToLogin() {" +
                "        window.wst.GoToLogin()" +
                "    }"
                +
                "    function GoToRegister() {" +
                "        window.wst.GoToRegister()" +
                "    }"
                +
                "    function GoToShopAuth() {" +
                "        window.wst.GoToShopAuth()" +
                "    }"
                +
                "    function GoToItemCat(str, str1,str2) {" +
                "        window.wst.GoToItemCat(str, str1,str2)" +
                "    }" +

                        "    function GoToLiveList() {" +
                        "        window.wst.GoToLiveList()" +
                        "    }"+
                "    function GoToLiveItem(str) {" +
                "        window.wst.GoToLiveItem(str)" +
                "    }"+

                "</script>");
        html.append("</body></html>");
        webView.loadDataWithBaseURL(null, html.toString(), "text/html",
                "UTF-8", null);
    }

    private void gotoAd(int positon) {
        if (positon >= 0) {
            name = BWApplication.PINHUPTITLE;
            BannerAdModel selectAd = adList.get(positon);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("type", name + "-" + (positon + 1));
            // hashMap.put("index", (positon + 1) + "");
            UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click27, hashMap);
            QuickSaleApi.clickBanner(new RequestEntity(getActivity(), new HttpRequestHelper(), new SimpleHttpRequestListener() {
                @Override
                public void onRequestSuccess(String method, Object object) {

                }
            }), selectAd.getRecordID());
            gotoBannerJump(selectAd);
        }
    }

    @Override
    public void onRefresh() {
        // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
        getRefleshData();
        //mListView.requestFocus();
    }

    private boolean isShowDialog = false;

    public void getRefleshData() {
        getAdList();
//        QuickSaleApi.getPinHuoV2NewAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                try {
//                    AdBean adBean = (AdBean) object;
//                    if (adBean == null) {
//                        adList = null;
//                        CustomHtml = "";
//                    } else {
//                        CustomHtml = adBean.getCustomHtml();
//                        adList = adBean.getBannerAdModelList();
//                    }
//                    flushAdView();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//        }), curCategoryID);
        if (curCategoryID <= 0)
            return;
        if (mLoadingDialog != null && !isHidden())
            isShowDialog = true;
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNetCacheApi(TAG);
        addSubscribe(pinHuoApi.getPinHuoNewList(curCategoryID, "1", 1, 100).
                compose(RxUtil.<PinHuoResponse<PinHuoNewResultModel>>rxSchedulerHelper())
                .compose(RxUtil.<PinHuoNewResultModel>handleResult())
                .subscribeWith(new CommonSubscriber<PinHuoNewResultModel>(activity, isShowDialog, R.string.loading) {
                    @Override
                    public void onNext(PinHuoNewResultModel object) {
                        super.onNext(object);
                        try {
                            setEmty_txt(R.string.pin_huo_no_data);
                            // loadDialogFinish();
                            newResult = object;
                            if (newResult!=null){
                                BWApplication.Share_WXAPPID=newResult.getWXShareAppID();
                                SpManager.setShareWXAppID(mActivity,newResult.getWXShareAppID());
                                BWApplication.PAY_WXAPPID=newResult.getWXPayAppID();
                                SpManager.setPayWXAppID(mActivity,newResult.getWXPayAppID());
                            }
                            mListData = newResult.ActivityList;
                            judeEmpty(mListData);
                            flushListView();
                      /*      mRefreshLayout.completeRefresh();*/
                            setRefreshFalse();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        //mRefreshLayout.completeRefresh();
                        setRefreshFalse();
                        setEmty_txt(R.string.pin_huo_net_error);
                        //loadDialogFinish();
                        judeEmpty(mListData);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        //mRefreshLayout.completeRefresh();
                        setRefreshFalse();
                        //setEmty_txt(R.string.pin_huo_net_error);
                        // loadDialogFinish();
                        judeEmpty(mListData);
                    }
                }));
//        QuickSaleApi.getPinHuoNewList(new RequestEntity(getActivity(), new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestStart(String method) {
//                super.onRequestStart(method);
//                if (mLoadingDialog != null&&!isHidden())
//                    mLoadingDialog.start("加载数据...");
//            }
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                try {
//                    setEmty_txt(R.string.pin_huo_no_data);
//                    loadDialogFinish();
//                    newResult = (PinHuoNewResultModel) object;
//                    mListData = newResult.ActivityList;
//                    judeEmpty(mListData);
//                    flushListView();
//                    mRefreshLayout.completeRefresh();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                mRefreshLayout.completeRefresh();
//                setEmty_txt(R.string.pin_huo_net_error);
//                loadDialogFinish();
//                judeEmpty(mListData);
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                mRefreshLayout.completeRefresh();
//                setEmty_txt(R.string.pin_huo_net_error);
//                loadDialogFinish();
//                judeEmpty(mListData);
//            }
//        }), curCategoryID, 1, 100);
    }

    public void getAdList() {
        if (curCategoryID <= 0)
            return;
        PinHuoApi pinHuoApi1 = HttpManager.getInstance().getPinHuoNetCacheApi(TAG);
        addSubscribe(pinHuoApi1.getBannersFormTypeV2(com.nahuo.constant.Constant.AreaTypeID.AREATYPEID_AD, curCategoryID
                , com.nahuo.constant.Constant.LoginRegisterFrom.LOGIN_REGISTER_FROM_ANDROID, 3)
                .compose(RxUtil.<PinHuoResponse<AdBean>>rxSchedulerHelper())
                .compose(RxUtil.<AdBean>handleResult())
                .subscribeWith(new CommonSubscriber<AdBean>(activity) {
                    @Override
                    public void onNext(AdBean object) {
                        super.onNext(object);
                        try {
                            AdBean adBean = object;
                            if (adBean == null) {
                                adList = null;
                                CustomHtml = "";
                            } else {
                                CustomHtml = adBean.getCustomHtml();
                                adList = adBean.getBannerAdModelList();
                            }
                            flushAdView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }

    private void setEmty_txt(int string) {
        if (tv_empty != null)
            tv_empty.setText(string);
    }

    private void setEmty_txt(String string) {
        if (tv_empty != null)
            tv_empty.setText(string);
    }

    private void loadDialogFinish() {
        if (mLoadingDialog != null)
            mLoadingDialog.stop();
    }

    private void judeEmpty(ArrayList<PinHuoModel> mListData) {
        if (ListUtils.isEmpty(mListData) && TextUtils.isEmpty(CustomHtml)) {
            // tv_empty.setText("亲！没数据了，再刷新看看");
            setEmptyView(true);
        } else {
            setEmptyView(false);
        }
    }

//    @Override
//    public void onLoadMore() {
//
//    }

    /**
     * 1.网页
     * 2.小组帖子
     * 3.拼货场次
     * 4.拼货场次类目
     * 5.安装地址
     *
     * @param selectAd
     */
    public void gotoBannerJump(BannerAdModel selectAd) {
        switch (selectAd.TypeID) {
            case 9:
                try {
                    ActivityUtil.goToLiveListActivity(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    goLiveItem(selectAd.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1://网页
            case 5: {//网页
                //Intent intent = new Intent(mActivity, ItemPreviewActivity.class);
                Intent intent = new Intent(mActivity, ItemPreview1Activity.class);
                intent.putExtra("name", "");
                intent.putExtra("url", selectAd.Content);
                mActivity.startActivity(intent);
                break;
            }
            case 6: { // 打开商品详情
                String content = selectAd.Content;
                String itemID;
                if (content.contains("http://")) {
                    itemID = content.replace("http://item.weipushop.com/wap/wpitem/", "");
                } else {
                    itemID = content;
                }
                int id;
                try {
                    id = Integer.valueOf(itemID);
                } catch (NumberFormatException e) {
                    ViewHub.showShortToast(mActivity, "无法识别该商品");
                    return;
                }
                Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
                startActivity(intent);
                break;
            }
            case 2: {//小组帖子
                if (selectAd.Content.indexOf("/xiaozu/topic/") > 1) {
                    String temp = "/xiaozu/topic/";
                    int topicID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(mActivity, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                    mActivity.startActivity(intent);
                } else if (selectAd.Content.indexOf("/xiaozu/act/") > 1) {
                    String temp = "/xiaozu/act/";
                    int actID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(mActivity, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                    mActivity.startActivity(intent);
                }
                break;
            }
            case 3: {// 进入场次
//                QuickSaleApi.getRecommendShopItems(mAppContext,
//                        mRequestHelper,
//                        this,
//                        Utils.parseInt(selectAd.Content),
//                        0,
//                        20,
//                        "",
//                        Const.SortIndex.DefaultDesc,
//                        -1,
//                        0);
                getItemsV2(Utils.parseInt(selectAd.Content),
                        0,
                        20,
                        "",
                        Const.SortIndex.DefaultDesc + "",
                        -1,
                        -1,
                        "");
//                PinHuoModel item = new PinHuoModel();
//                item.ID = Integer.parseInt(selectAd.Content);
//                PinHuoDetailListActivity.launch(mActivity, item,true);
                break;
            }
            case 4: {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PIN_HUO_SELECT_RB, Integer.parseInt(selectAd.Content)));
//                int id =  Integer.parseInt(selectAd.Content);
//
//                int selectIndex = -1;
//                int index = 0;
//                for (PinHuoCategroyModel pcm : newResult.CategoryList) {
//                    if (id == pcm.getCid()) {
//                        selectIndex = index;
//                    }
//                    index++;
//                }
//                if (selectIndex >= 0) {
//                    RadioButton rb = (RadioButton) PinHuoFragment.mRadioGroup.getChildAt(selectIndex);
//                    rb.setChecked(true);
//                    curCategoryID = (int) rb.getTag();
//                    getRefleshData();
//                }
                break;
            }
        }
    }

    private void getItemsV2(int qsid, int pageIndex, int pageSize, String keyword, String sort, int itemCat, int displayMode, String filterValues) {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getItemsV2(qsid, pageIndex, pageSize, keyword, sort, itemCat, displayMode, filterValues)
                .compose(RxUtil.<PinHuoResponse<RecommendModel>>rxSchedulerHelper())
                .compose(RxUtil.<RecommendModel>handleResult())
                .subscribeWith(new CommonSubscriber<RecommendModel>(getActivity()) {
                    @Override
                    public void onNext(RecommendModel recommendModel) {
                        super.onNext(recommendModel);
                        onDataLoaded(recommendModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }

    private RecommendModel mRecommendModel;

    private void onDataLoaded(Object object) {
        mRecommendModel = (RecommendModel) object;
        RecommendModel.InfoEntity ie = mRecommendModel.getInfo();
        PinHuoModel item = new PinHuoModel();
        item.setUrl(ie.getUrl());
        item.setID(ie.getID());
        item.IsStart = ie.isIsStart();
        item.setAppCover(ie.getAppCover());
        item.setPicAd(false);
        item.setDescription(ie.getDescription());
        item.setGroupDealCount(ie.getChengTuanCount());
        item.setName(ie.getName());
        item.setPCCover(ie.getPCCover());
        long l = ie.getToTime();
        item.setStartMillis(ie.getStartTime());
        long ll = ie.getEndMillis();
        item.setEndMillis(ie.getToTime());
        item.setLimitPoint(ie.getLimitPoint());
        item.setLimitShopAuth(ie.isLimitShopAuth());
        item.setVisitResult(ie.getVisitResult());
        item.setActivityType(ie.getActivityType());
        item.setHasNewItems(mRecommendModel.NewItems.size() > 0 ? true : false);
        ViewUtil.gotoChangci(getActivity(), item);
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture) {
            super(millisInFuture, 100);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFinish() {
            try {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
            } catch (Exception e) {

            }
        }
    }

    public static final int REQUEST_RECHARGE = 1;

    // js通信接口
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void GoToLogin() {
            Utils.gotoLoginActivity(getActivity());
        }

        @android.webkit.JavascriptInterface
        public void GoToRegister() {
            Utils.gotoRegisterActivity(getActivity());
        }

        @android.webkit.JavascriptInterface
        public void GoToShopAuth() {
            Utils.gotoShopAuthActivity(getActivity());
        }

        @android.webkit.JavascriptInterface
        public void GoToRecharge() {
            //充值
         /*   Intent intent = new Intent(getActivity(), WXPayEntryActivity.class);
            intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
            startActivityForResult(intent, REQUEST_RECHARGE);*/
            Utils.gotoPayEntryActivity(getActivity());
        }

        @android.webkit.JavascriptInterface
        public void GoToItemDetail(String str, String str1) {
            //商品详情
            try {
                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.parseInt(str));
                //intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, Integer.parseInt(str1));
                startActivity(intent);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToQsList(String str) {
            try {
                PinHuoModel model1 = new PinHuoModel();
                model1.ID = Integer.parseInt(str);
                PinHuoDetailListActivity.launch(getActivity(), model1, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToGroupDetail(String str, String str1) {
            try {
                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, Integer.parseInt(str));

                //帖子
                if (str1.equals("activity")) {
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                } else {
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                }
                getActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @android.webkit.JavascriptInterface
        public void GoToUrl(String str, String str1) {
            //如果为0表示App打开，1表示由系统浏览器打开
            try {
                if (!TextUtils.isEmpty(str)) {
                    if (str1.equals("0")) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(str);
                        intent.setData(content_url);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), ItemPreview1Activity.class);
                        intent.putExtra("name", "天天拼货");
                        intent.putExtra("url", str);
                        getActivity().startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @android.webkit.JavascriptInterface
        public void GoToItemCat(String str, String str1, String str2) {
            //分类
            try {
                Intent intent = new Intent(getActivity(), SortReasonActivity.class);
                intent.putExtra(SortReasonActivity.EXTRA_RID, Integer.parseInt(str));
                intent.putExtra(SortReasonActivity.EXTRA_VALUEIDS, str1);
                intent.putExtra(SortReasonActivity.EXTRA_TITLE, str2);
                getActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @android.webkit.JavascriptInterface
        public void GoToLiveList() {
            //分类
            try {
                ActivityUtil.goToLiveListActivity(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        @android.webkit.JavascriptInterface
        public void GoToLiveItem(String str) {
            //分类
            try {
                goLiveItem(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
