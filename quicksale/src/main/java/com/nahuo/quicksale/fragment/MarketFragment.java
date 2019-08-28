package com.nahuo.quicksale.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nahuo.bean.StallsAllListBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.BaseFragment;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ItemPreview1Activity;
import com.nahuo.quicksale.activity.MarketActivity;
import com.nahuo.quicksale.adapter.ImageBannerAdapter;
import com.nahuo.quicksale.adapter.MarketsAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.MarketApi;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.SimpleHttpRequestListener;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Constant;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout;
import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.customview.ViewFlow;
import com.nahuo.quicksale.customview.ViewFlowCircleIndicator;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.service.autoupdate.AppUpdate;
import com.nahuo.service.autoupdate.AppUpdateService;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 广告栏以及倒计时
 *
 * @author James Chen
 * @create time in 2017/3/23 17:58
 */
public class MarketFragment extends BaseFragment implements HttpRequestListener, IRefreshLayout.RefreshCallBack {

    private ListView mListView;
    private MarketsAdapter mAdapter;
    private static final String EXTRA_LIST_DATA = "EXTRA_LIST_DATA";
    private static final String EXTRA_AD_LIST_DATA = "EXTRA_AD_LIST_DATA";
    private static final String EXTRA_LIST_CATID = "EXTRA_LIST_CATID";
    private static final String EXTRA_MID = "EXTRA_MID";
    private static final String EXTRA_FID = "EXTRA_FID";
    private ArrayList<StallsAllListBean.ShopListBean> mListData = new ArrayList<>();
    private SwipeRefreshLayoutEx mRefreshLayout;
    //private FlowIndicator mLayoutIndicator;
    private List<BannerAdModel> adList = new ArrayList<>();
    // private BannerAdAdapter mPagerAdapter;
    private MyCountDownTimer mCountDownTimer;
    private View headerView;
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private int curCategoryID;
    private ViewFlow mViewFlow;
    private ViewFlowCircleIndicator mViewFlowIndic;
    private ImageBannerAdapter bAdapter;
    private CountDownTask mCountDownTask;
    private PinHuoNewResultModel newResult;
    private AppUpdate mAppUpdate;
    // private View empty_view;
    private int fid, mid;
    public LoadingDialog mLoadingDialog;
    public View empty_view;
    public static MarketFragment getInstance(ArrayList<StallsAllListBean.ShopListBean> dataList, int mid, int fid) {
        MarketFragment fragment = new MarketFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_LIST_DATA, dataList);
        args.putSerializable(EXTRA_MID, mid);
        args.putInt(EXTRA_FID, fid);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_pin_huo, container, false);
        EventBus.getDefault().registerSticky(this);
        initViews();
        initData();
        return mContentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mListData = (ArrayList<StallsAllListBean.ShopListBean>) getArguments().getSerializable(EXTRA_LIST_DATA);
        // adList = (List<BannerAdModel>) getArguments().getSerializable(EXTRA_AD_LIST_DATA);
        fid = getArguments().getInt(EXTRA_FID);
        mid = getArguments().getInt(EXTRA_MID);
//        if (!ListUtils.isEmpty(mListData)) {
//            mAdapter.setData(mListData);
//            mAdapter.notifyDataSetChanged();
//            //关注列表
//            // mAdapter.getFocusStat();
//        }
        getRefleshData();
        // flushAdView();
    }

    private void initViews() {
        mLoadingDialog = new LoadingDialog(getActivity());
        mAppUpdate = AppUpdateService.getAppUpdate(getActivity());
        mRefreshLayout = $(R.id.refresh_layout);
        mRefreshLayout.setCallBack(this);
        mListView = $(R.id.listview);
        empty_view = $(R.id.empty_view);
        LayoutInflater lif = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = lif.inflate(R.layout.layout_pin_huo_new_header, null);
       // mListView.addHeaderView(headerView);
        mViewFlow = (ViewFlow) headerView.findViewById(R.id.viewflow);
        mViewFlowIndic = (ViewFlowCircleIndicator) headerView.findViewById(R.id.viewflowindic);
        mViewFlow.setParentView(MarketActivity.mViewPager, mRefreshLayout, mListView);
        mAdapter = new MarketsAdapter(mActivity);
        mListView.setAdapter(mAdapter);
    }

    private void startCountDown() {
        mCountDownTask = CountDownTask.create();
        //   mAdapter.setCountDownTask(mCountDownTask);
    }

    private void cancelCountDown() {
        // mAdapter.setCountDownTask(null);
        mCountDownTask.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        //cancelCountDown();
        try {
            mAppUpdate.callOnPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // startCountDown();
        try {
            mAppUpdate.callOnResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //new LoadGoodsTask(getActivity(),(CircleTextView) $(R.id.iv_shopping_cart)).execute();
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFRESH_MARKET_SUB_ITEMS:// 刷新
//                int catID = (int) event.data;
//                if (catID > 0) {
//                    fid = catID;
//                }
                //  getRefleshData();
                break;
        }
//        switch (event.id) {
//            //主页弹窗广告接收
//            case EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED:
//                List<BannerAdModel> data = (List<BannerAdModel>) event.data;
//                if (data != null) {
////                    PopADMenu menu = PopADMenu.getInstance(getActivity(), this, data, mAppUpdate);
////                    menu.show();
//                }
//                break;
//            //轮播广告接收
//            case EventBusId.LOAD_PIN_HUO_AD_FINISHED:
//                PinHuoNewResultModel_Map data2 = (PinHuoNewResultModel_Map) event.data;
//                if (data2 == null) {
//                    adList = null;
//                    flushAdView();
//                } else {
//                    if (data2.CurrCategoryID == curCategoryID) {
//                        adList = data2.adList;
//                        flushAdView();
//                    }
//                }
//                break;
//            case EventBusId.LOAD_PIN_HUO_FINISHED:
//                try {
//                    PinHuoNewResultModel data1 = (PinHuoNewResultModel) event.data;
//                    if (data1 != null) {
////                        if (data1.ActivityList == null || data1.ActivityList.size() == 0) {
////                            empty_view.setVisibility(View.VISIBLE);
////                        } else {
////                            empty_view.setVisibility(View.GONE);
////                        }
//                        if (data1.CurrCategoryID == curCategoryID) {
//                            mListData = data1.ActivityList;
//                            flushListView();
//                        }
//                    } else {
//                       // empty_view.setVisibility(View.VISIBLE);
//                    }
//                    mRefreshLayout.completeRefresh();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case EventBusId.REFRESH_COMPLETEd:
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.MAIN_CURRENT_TAB));
//                getRefleshData();
//                break;
//
//        }
    }

    private void flushListView() {
        if (mAdapter != null) {
            mAdapter.setData(mListData);
            mAdapter.notifyDataSetChanged();
            //关注列表
            // mAdapter.getFocusStat();
        }
    }

    private void flushAdView() {

        if (adList == null || adList.size() <= 0) {
            mListView.removeHeaderView(headerView);
        } else {
            ArrayList<String> imgs = new ArrayList<>();
            for (BannerAdModel adModel : adList) {
                imgs.add(adModel.ImageUrl);
            }
            if (mListView.getHeaderViewsCount() > 0) {
                mListView.removeHeaderView(headerView);
            }
            mListView.addHeaderView(headerView);
//            mPagerAdapter.setData(imgs);
//            mPagerAdapter.notifyDataSetChanged();
            //     mLayoutIndicator.setMaxNum(imgs.size());
            mViewFlow.setTimeSpan(Constant.ADVERTISE_TIME);
            mViewFlow.setFlowIndicator(mViewFlowIndic);
            int size = adList == null ? 0 : adList.size();
            mViewFlow.setSelection(size * 1000);
            mViewFlow.setSideBuffer(size);
            bAdapter = new ImageBannerAdapter(mActivity, adList);
            mViewFlow.setAdapter(bAdapter);
            mViewFlow.startAutoFlowTimer();
            bAdapter.setOnItemClickListener(new ImageBannerAdapter.BannerOnclickListener() {
                @Override
                public void onItemClick(View v, int positon) {
                    // Toast.makeText(mActivity, positon + "", Toast.LENGTH_SHORT).show();
                    if (positon >= 0) {
                        BannerAdModel selectAd = adList.get(positon);
                        gotoBannerJump(selectAd);
                    }
                }
            });
        }
    }

    @Override
    public void onRefresh() {
//        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
        getRefleshData();
        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_MARKET_AD));
    }

    private void getRefleshData() {

        MarketApi.getStallList(new RequestEntity(getActivity(), new HttpRequestHelper(), new SimpleHttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
                super.onRequestStart(method);
                showDialog(mLoadingDialog, "正在加载数据");
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
                closeDialog(mLoadingDialog);
                mListData.clear();
                try {
                    StallsAllListBean bean = (StallsAllListBean) object;
                    List<StallsAllListBean.ShopListBean> shopList = bean.getShopList();
                    if (shopList != null) {
                        if (shopList.size()>0){
                            empty_view.setVisibility(View.GONE);
                        }else {
                            empty_view.setVisibility(View.VISIBLE);
                        }
                        if (mAdapter != null) {
                            mAdapter.setData(shopList);
                            mAdapter.notifyDataSetChanged();
                            //关注列表
                            // mAdapter.getFocusStat();
                        }
                    }else {
                        empty_view.setVisibility(View.VISIBLE);
                    }
                    // mListData.addAll(shopList);
                    // flushListView();
                    mRefreshLayout.completeRefresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                closeDialog(mLoadingDialog);
                mRefreshLayout.completeRefresh();
                ViewHub.showShortToast(getActivity(), "加载失败，请稍候再试");
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                closeDialog(mLoadingDialog);
                mRefreshLayout.completeRefresh();
                ViewHub.showShortToast(getActivity(), "加载失败，请稍候再试");
            }
        }), mid, fid);
      //  getAd();

    }

    private void getAd() {
        //读取广告
        QuickSaleApi.getMarketsNewAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
            @Override
            public void onRequestSuccess(String method, Object object) {
                ArrayList<BannerAdModel> adList1 = (ArrayList<BannerAdModel>) object;
                adList.clear();
                adList.addAll(adList1);
                flushAdView();
//                PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
//                _data.CurrCategoryID = curCategoryID;
//                _data.adList = adList;
                //  ads.put(curCategoryID + "", adList);
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                //  EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
                flushAdView();
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
                flushAdView();
            }
        }), mid);
    }

    @Override
    public void onLoadMore() {

    }

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
                QuickSaleApi.getRecommendShopItems(mAppContext,
                        mRequestHelper,
                        this,
                        Utils.parseInt(selectAd.Content),
                        0,
                        20,
                        "",
                        Const.SortIndex.DefaultDesc,
                        -1,
                        0);
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
//                    RadioButton rb = (RadioButton) PinHuoTabFragment.mRadioGroup.getChildAt(selectIndex);
//                    rb.setChecked(true);
//                    curCategoryID = (int) rb.getTag();
//                    getRefleshData();
//                }
                break;
            }
        }
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
}
