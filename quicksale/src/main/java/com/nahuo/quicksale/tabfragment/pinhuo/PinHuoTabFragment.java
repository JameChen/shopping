package com.nahuo.quicksale.tabfragment.pinhuo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.AdBean;
import com.nahuo.constant.UmengClick;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.CommonSearchActivity;
import com.nahuo.quicksale.PinHuoActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.XBaseFragment;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.adapter.ImageBannerAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.SimpleHttpRequestListener;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.loader.GlideImageLoader;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.mvp.view.PinHuoView;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.oldermodel.PinHuoCategroyModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoNewResultModel_Map;
import com.nahuo.quicksale.tab.Constant;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.CircleCarTxtUtl;
import com.nahuo.quicksale.util.ListDataSave;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 拼货滑动子目录
 *
 * @author James Chen
 * @create time in 2017/3/24 16:23
 */
public class PinHuoTabFragment extends XBaseFragment implements OnClickListener, PinHuoView, OnRefreshListener {
    private static final String TAG = PinHuoTabFragment.class.getSimpleName();
    private static final String EXTRA_PIN_HUO_RESULT = "EXTRA_PIN_HUO_RESULT";
    private TextView mTvTitle;
    private RadioGroup mRadioGroup;
    public ViewPager mViewPager;
    private int curCategoryID;
    private FragmentStatePagerAdapter mPagerAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<PinHuoNewResultModel> datas;
    private List<Integer> catIDs;
    private CircleTextView carCountTv;
    private Map<String, AdBean> ads = new HashMap<>();
    public ListDataSave save;
    public List<String> recordIDs = new ArrayList<>();
    public List<Boolean> flags = new ArrayList<>();
    public List<String> typeIDs = new ArrayList<>();
    public List<BannerAdModel> bData = new ArrayList<>();
    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
    int mCurrentCheckedRadioLeft;
    private View layout_explain;
    public static View empty_view;
    public LoadingDialog mLoadingDialog;
    private Toolbar toolbar;
    private AppBarLayout appbar;
    private View headerView, line;
    private WebView webHeadView;
    // private ViewFlow mViewFlow;
    //private ViewFlowCircleIndicator mViewFlowIndic;
    private ImageBannerAdapter bAdapter;
    private List<BannerAdModel> adList;
    private String CustomHtml = "";
//    private RefreshLayout swipeToLoadLayout;
//    private BezierCircleHeader mRefreshHeader;
    private Banner banner;
    private List<String> images = new ArrayList();

    public static PinHuoTabFragment getInstance(PinHuoNewResultModel model) {
        PinHuoTabFragment fragment = new PinHuoTabFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PIN_HUO_RESULT, model);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_pin_huo_new2_main, container, false);
        initViews();
        initLoadData();
        // initLoadData();
        return mContentView;
    }

    private void init() {
        initData();
        readPopAdList();
        // readAdList();
    }

    //读取弹窗广告
    private void readPopAdList() {
//        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNetCacheApi(TAG);
//        addSubscribe(pinHuoApi.getBannersFormType(com.nahuo.constant.Constant.AreaTypeID.AREATYPEID_POP, FunctionHelper.GetAppVersionCode(mAppContext))
//                .compose(RxUtil.<PinHuoResponse<List<BannerAdModel>>>rxSchedulerHelper())
//                .compose(RxUtil.<List<BannerAdModel>>handleResult())
//                .subscribeWith(new ResourceSubscriber<List<BannerAdModel>>() {
//                    @Override
//                    public void onNext(List<BannerAdModel> object) {
//                        recordIDs.clear();
//                        typeIDs.clear();
//                        flags.clear();
//                        bData.clear();
//                        List<BannerAdModel> data = object;
//                        if (ListUtils.isEmpty(data))
//                            return;
//                        for (BannerAdModel bean : data) {
//                            typeIDs.add(bean.getTypeID() + "");
//                        }
//                        if (typeIDs.contains("5")) {
//                            //强制点击更新
//                            for (BannerAdModel bean : data) {
//                                bean.del_flag = true;
//                                bData.add(bean);
//                            }
//                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, bData));
//                            return;
//                        }
//                        String use_id = SpManager.getUserId(mActivity) + "";
//                        save = new ListDataSave(getActivity(), SpConstant.SP_PIHUO_AD + use_id);
//                        List<String> list = save.getDataList(SpConstant.PIHUO_AD_LIST + use_id);
//                        if (list == null || list.size() <= 0) {
//                            for (BannerAdModel bean : data) {
//                                recordIDs.add(bean.getRecordID() + "");
//                            }
//                            save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, recordIDs);
//                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, data));
//
//                        } else {
//                            if (data != null || data.size() > 0) {
//
//                                for (int i = 0; i < data.size(); i++) {
//                                    if (!list.contains(String.valueOf(data.get(i).getRecordID()))) {
//                                        list.add(String.valueOf(data.get(i).getRecordID()));
//                                        save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, list);
//                                        flags.add(true);
//                                    }
//                                }
//                                if (flags.contains(true))
//                                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, data));
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                }));
//        QuickSaleApi.getPinHuoPopupAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//
//                Log.d("yu", object.toString());
//                recordIDs.clear();
//                typeIDs.clear();
//                flags.clear();
//                bData.clear();
//                List<BannerAdModel> data = (List<BannerAdModel>) object;
//                if (data == null || data.size() <= 0)
//                    return;
//                for (BannerAdModel bean : data) {
//                    typeIDs.add(bean.getTypeID() + "");
//                }
//                if (typeIDs.contains("5")) {
//                    //强制点击更新
//                    for (BannerAdModel bean : data) {
//                        bean.del_flag = true;
//                        bData.add(bean);
//                    }
//                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, bData));
//                    return;
//                }
//                String use_id = SpManager.getUserId(mActivity) + "";
//                save = new ListDataSave(getActivity(), SpConstant.SP_PIHUO_AD + use_id);
//                List<String> list = save.getDataList(SpConstant.PIHUO_AD_LIST + use_id);
//                if (list == null || list.size() <= 0) {
//                    for (BannerAdModel bean : data) {
//                        recordIDs.add(bean.getRecordID() + "");
//                    }
//                    save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, recordIDs);
//                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, data));
//
//                } else {
//                    if (data != null || data.size() > 0) {
//
//                        for (int i = 0; i < data.size(); i++) {
//                            if (!list.contains(String.valueOf(data.get(i).getRecordID()))) {
//                                list.add(String.valueOf(data.get(i).getRecordID()));
//                                save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, list);
//                                flags.add(true);
//                            }
//                        }
//                        if (flags.contains(true))
//                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, data));
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, null));
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, null));
//            }
//        }), FunctionHelper.GetAppVersionCode(mAppContext));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //  EventBus.getDefault().unregister(this);
    }

    private void initLoadData() {
        boolean isShowdialog = false;
        if (mLoadingDialog != null && !isHidden())
            isShowdialog = true;
        curCategoryID = 0;
        if (FunctionHelper.IsNetworkOnline(mActivity)) {
            PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNetCacheApi(TAG);
            addSubscribe(pinHuoApi.getPinHuoNewList(curCategoryID, "1", 1, 100).
                    compose(RxUtil.<PinHuoResponse<PinHuoNewResultModel>>rxSchedulerHelper())
                    .compose(RxUtil.<PinHuoNewResultModel>handleResult())
                    .subscribeWith(new CommonSubscriber<PinHuoNewResultModel>(mActivity,CommonSubscriber.IS_RESET_DIALOG, isShowdialog, R.string.loading) {
                        @Override
                        public void onNext(PinHuoNewResultModel object) {
                            // loadFinished();
                            super.onNext(object);
                            PinHuoNewResultModel newResult = object;
                            if (newResult != null) {
                                BWApplication.Share_WXAPPID=newResult.getWXShareAppID();
                                SpManager.setShareWXAppID(mActivity,newResult.getWXShareAppID());
                                BWApplication.PAY_WXAPPID=newResult.getWXPayAppID();
                                SpManager.setPayWXAppID(mActivity,newResult.getWXPayAppID());
                                SpManager.setWXTTMiniAppID(mActivity,newResult.getWXTTMiniAppID());
                                SpManager.setWXKDBMiniAppID(mActivity,newResult.getWXKDBMiniAppID());

                                if (et_search!=null)
                                    et_search.setHint(newResult.getSearchTip());
                                SpManager.setSearchTip(mActivity,newResult.getSearchTip());
                                firstModel = newResult;
                                init();
                            }
                            setEmpty(newResult);
                            loadRefershFinished();
                        }

                        @Override
                        public void onError(Throwable t) {
                            // loadFinished();
                            //onLoadPinAndForecastFailed();
                            super.onError(t);
                            loadRefershFinished();
                            try {
                                setEmpty(firstModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onComplete() {
                            //  loadFinished();
                            super.onComplete();
                            loadRefershFinished();
                            try {
                                setEmpty(firstModel);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }));
        } else {
            ViewHub.showShortToast(mActivity, R.string.pin_huo_net_error);
            loadRefershFinished();
        }
//        QuickSaleApi.getPinHuoNewList(new RequestEntity(getActivity(), new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestStart(String method) {
//                super.onRequestStart(method);
//                if (mLoadingDialog != null&&!isHidden())
//                    mLoadingDialog.start("加载数据....");
//            }
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                try {
//                    loadFinished();
//                    PinHuoNewResultModel newResult = (PinHuoNewResultModel) object;
//                    if (newResult != null) {
//                        firstModel = newResult;
//                        //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_FINISHED, newResult));
//                        init();
//                    }
//                    setEmpty(newResult);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                loadFinished();
//                onLoadPinAndForecastFailed();
//                try {
//                    setEmpty(firstModel);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                loadFinished();
//                onLoadPinAndForecastFailed();
//                try {
//                    setEmpty(firstModel);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (data.getCode().equals("AccountError")) {
//                    ViewHub.showLongToast(getActivity(), "禁止登录");
//                    UserInfoProvider.exitApp(getContext());
//                }
//            }
//        }), curCategoryID, 1, 100);
    }

    private void loadRefershFinished() {
       // swipeToLoadLayout.finishRefresh();
        if (ListUtils.isEmpty(mFragments)){
            if (empty_view!=null)
            empty_view.setVisibility(View.VISIBLE);
        }else {
            if (empty_view!=null)
            empty_view.setVisibility(View.GONE);
        }
    }

    private void setEmpty(PinHuoNewResultModel newResult) {
//        if (newResult != null) {
//
//            if (newResult.ActivityList == null || newResult.ActivityList.size() == 0) {
//                // if (!isStartFragment)
//                empty_view.setVisibility(View.VISIBLE);
//            } else {
//                empty_view.setVisibility(View.GONE);
//
//            }
//        } else {
//            //if (!isStartFragment)
//            empty_view.setVisibility(View.VISIBLE);
//
//        }
    }

    private void loadFinished() {
        if (mLoadingDialog != null)
            mLoadingDialog.stop();
    }

    PinHuoNewResultModel firstModel;

    private void initData() {
        datas = new ArrayList<>();
        catIDs = new ArrayList<>();
        if (firstModel != null) {
            for (PinHuoCategroyModel pcm : firstModel.CategoryList) {
                catIDs.add(pcm.Cid);
                if (pcm.getCid() == firstModel.CurrCategoryID) {
                    curCategoryID = firstModel.CurrCategoryID;
                    datas.add(firstModel);
                } else {
                    datas.add(null);
                }
            }
            onAllDataLoaded();
        }

    }


    int useId, pinhuo_main_furst;
    EditText et_search;
    private void initViews() {
        mLoadingDialog = new LoadingDialog(getActivity());
        mHorizontalScrollView = $(R.id.horizontalScrollView);
        carCountTv = $(R.id.circle_car_text);
        CircleCarTxtUtl.setColor(carCountTv);
        mRadioGroup = $(R.id.radio_group);
        mTvTitle = $(R.id.tv_title);
        $(R.id.ivtopright).getBackground().setAlpha(200);
        mViewPager = $(R.id.view_pager_pin_huo);
        $(R.id.tv_all).setOnClickListener(this);
        $(R.id.iv_shopping_cart).setOnClickListener(this);
        $(R.id.iv_chat_txt).setOnClickListener(this);
        $(R.id.iv_all_search).setOnClickListener(this);
        $(R.id.iv_search).setOnClickListener(this);
        et_search=$(R.id.iv_all_search);
        empty_view = $(R.id.empty_view);
        $(R.id.btn_reload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initLoadData();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusBarHeight(mActivity));
            $(R.id.layout_tittle_status).setLayoutParams(params);
            $(R.id.layout_tittle_status).setVisibility(View.VISIBLE);
        }
    /*    layout_explain = $(R.id.layout_explain);
        toolbar = $(R.id.toolbar);
        appbar = $(R.id.appbar);
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset始终为0以下的负数
                // float percent = (Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange());
//                Log.e("yu","verticalOffset=>"+verticalOffset);
//                Log.e("yu","appBarLayout.getTotalScrollRange()=>"+appBarLayout.getTotalScrollRange());
//                Log.e("yu","getPinHuoProportionHeight=>"+ScreenUtils.getPinHuoProportionHeight(ScreenUtils.getScreenWidth(mActivity)));
                float percent = (Math.abs(verticalOffset * 1f) / appBarLayout.getTotalScrollRange());
                //Log.e("yu",percent+"");
                if (percent >= 0.7) {
                    final Drawable nav_nomal = getResources().getDrawable(R.drawable.search_left_black);
                    nav_nomal.setBounds(0, 0, nav_nomal.getIntrinsicWidth(), nav_nomal.getIntrinsicHeight());
                    ((EditText) $(iv_all_search)).setCompoundDrawables(nav_nomal, null, null, null);
                    ((ImageView) $(R.id.iv_chat_txt)).setImageResource(R.drawable.message_right_black);
                } else {
                    final Drawable nav_nomal = getResources().getDrawable(R.drawable.search_left_gray);
                    nav_nomal.setBounds(0, 0, nav_nomal.getIntrinsicWidth(), nav_nomal.getIntrinsicHeight());
                    ((EditText) $(iv_all_search)).setCompoundDrawables(nav_nomal, null, null, null);
                    ((ImageView) $(R.id.iv_chat_txt)).setImageResource(R.drawable.message_right_white);
                }
                float per=percent;
                if (per<=0.7){
                    per=0.7f;
                }
                toolbar.setBackgroundColor(changeAlpha(Color.WHITE, percent));
                et_search.setAlpha(per);
            }
        });
        headerView = $(R.id.layout_pinhuo_ad);
        webHeadView = $(R.id.webview);
        line = $(R.id.layout_ad_line);
        banner = $(R.id.banner);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(ScreenUtils.getScreenWidth(getActivity())));
        banner.setLayoutParams(params);
        banner.setBannerAnimation(AccordionTransformer.class);
        //banner.setType(Banner.Type_Out_ArcLayout);
        //mViewFlow = (ViewFlow) headerView.findViewById(R.id.viewflow);
        webHeadView.getSettings().setJavaScriptEnabled(true);
        // webView.getSettings().setBuiltInZoomControls(true);
        //webView.getSettings().setSupportZoom(true);
        webHeadView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webHeadView.setWebViewClient(new MyWebViewClient());
        webHeadView.addJavascriptInterface(new JavascriptInterface(), "wst");*/
        //   FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(ScreenUtils.getScreenWidth(getActivity())));
        //  mViewFlow.setLayoutParams(params);
        //mViewFlowIndic = (ViewFlowCircleIndicator) headerView.findViewById(R.id.viewflowindic);

//        swipeToLoadLayout = $(swipeToLoadLayout);
//        mRefreshHeader = $(R.id.header);
//        swipeToLoadLayout.setPrimaryColorsId(R.color.my_colorPrimary, android.R.color.white);
//        //mRefreshHeader.setEnableHorizontalDrag(true);
//        swipeToLoadLayout.setEnableHeaderTranslationContent(true);
//        swipeToLoadLayout.setOnRefreshListener(this);
//        swipeToLoadLayout.autoRefresh(100);
//        swipeToLoadLayout.setEnableLoadMore(false);

//        if (SpManager.getUserId(BWApplication.getInstance()) > 0)
//            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
//        useId = SpManager.getUserId(BWApplication.getInstance());
//        if (useId>0) {
//            GuidePreference.init(getActivity());
//            pinhuo_main_furst = GuidePreference.getInstance().getPinhuoMain(useId + "");
//            if (pinhuo_main_furst == 1) {
//                layout_explain.setVisibility(View.GONE);
//            } else {
//                layout_explain.setVisibility(View.VISIBLE);
//            }
//        }
//        layout_explain.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GuidePreference.getInstance().setPinhuoMain(useId+"",1);
//                layout_explain.setVisibility(View.GONE);
//                EventBus.getDefault().post( BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
//
//            }
//        });

    }




    public void flushAdView(List<BannerAdModel> adList1,
                            String CustomHtml1) {
//        adList = adList1;
//        CustomHtml = CustomHtml1;
//        if (ListUtils.isEmpty(adList) && TextUtils.isEmpty(CustomHtml)) {
//            headerView.setVisibility(View.GONE);
//            line.setVisibility(View.GONE);
//            webHeadView.setVisibility(View.GONE);
//        } else {
//            if (!ListUtils.isEmpty(adList) && !TextUtils.isEmpty(CustomHtml)) {
//                headerView.setVisibility(View.VISIBLE);
//                webHeadView.setVisibility(View.VISIBLE);
//                line.setVisibility(View.VISIBLE);
//                line.setBackgroundResource(R.color.white);
//                setViewFlow();
//                setWebView();
//            } else if (!ListUtils.isEmpty(adList) && TextUtils.isEmpty(CustomHtml)) {
//                headerView.setVisibility(View.VISIBLE);
//                line.setVisibility(View.VISIBLE);
//                webHeadView.setVisibility(View.GONE);
//                if (line != null)
//                    //  line.setBackgroundResource(R.color.ad_line_bg);
//                    line.setBackgroundResource(R.color.white);
//                setViewFlow();
//            } else if (ListUtils.isEmpty(adList) && !TextUtils.isEmpty(CustomHtml)) {
//                line.setVisibility(View.VISIBLE);
//                webHeadView.setVisibility(View.VISIBLE);
//                if (line != null)
//                    line.setBackgroundResource(R.color.white);
//                setWebView();
//            }
//        }
//        mListView.requestDisallowInterceptTouchEvent(true);
//        mListView.setEnabled(true);
//         mListView.requestFocus();
    }

    private void setViewFlow() {
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
//        mViewFlow.stopAutoFlowTimer();
//        mViewFlow.setTimeSpan(com.nahuo.quicksale.common.Constant.ADVERTISE_TIME);
//        mViewFlow.setFlowIndicator(mViewFlowIndic);
//        int size = adList == null ? 0 : adList.size();
//        mViewFlow.setSelection(size * 1000);
//        mViewFlow.setSideBuffer(size);
//        bAdapter = new ImageBannerAdapter(mActivity, adList);
//        bAdapter.setType(ImageBannerAdapter.TYPE_FIRST_PAGE);
//        mViewFlow.setAdapter(bAdapter);
//        mViewFlow.startAutoFlowTimer();
//        bAdapter.setOnItemClickListener(new ImageBannerAdapter.BannerOnclickListener() {
//            @Override
//            public void onItemClick(View v, int positon) {
//                // Toast.makeText(mActivity, positon + "", Toast.LENGTH_SHORT).show();
//                gotoAd(positon);
//            }
//        });
    }

    String name = "";

    private void gotoAd(int positon) {
        if (positon >= 0) {
            name = BWApplication.PINHUPTITLE;
            BannerAdModel selectAd = adList.get(positon);
           // Log.e("yu","getTypeID==>"+selectAd.getTypeID()+"getContent==>"+selectAd.getContent());
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("type", name + "-" + (positon + 1));
            // hashMap.put("index", (positon + 1) + "");
            UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click27, hashMap);
            QuickSaleApi.clickBanner(new RequestEntity(getActivity(), new HttpRequestHelper(), new SimpleHttpRequestListener() {
                @Override
                public void onRequestSuccess(String method, Object object) {

                }
            }), selectAd.getRecordID());
            ((MainNewActivity) getActivity()).gotoBannerJump(selectAd);
        }
    }

//    private void setWebView() {
//        StringBuffer html = new StringBuffer();
//        html.append("<html>"
//                + "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">"
//                + "<head>"
//                + "<style type=\"text/css\">"
//                + ".wp-item-detail-format.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                + "*{margin:0px; padding:0px;word-break:break-all;}"
//                + "</style>" + "</head>" + "<body>"
//                + "<div class=wp-item-detail-format>");
//        html.append(CustomHtml);
//        html.append("</div>");
//        html.append("<script>" +
//                "    function GoToRecharge() {" +
//                "        window.wst.GoToRecharge()" +
//                "    }" +
//                "    function GoToItemDetail(str, str1) {" +
//                "        window.wst.GoToItemDetail(str, str1)" +
//                "    }" +
//                "    function GoToQsList(str) {" +
//                "        window.wst.GoToQsList(str)" +
//                "    }"
//                +
//                "    function GoToGroupDetail(str, str1) {" +
//                "        window.wst.GoToGroupDetail(str, str1)" +
//                "    }"
//                +
//                "    function GoToUrl(str, str1) {" +
//                "        window.wst.GoToUrl(str, str1)" +
//                "    }"
//                +
//                "    function GoToItemCat(str, str1,str2) {" +
//                "        window.wst.GoToItemCat(str, str1,str2)" +
//                "    }" +
//                "</script>");
//        html.append("</body></html>");
//        webHeadView.loadDataWithBaseURL(null, html.toString(), "text/html",
//                "UTF-8", null);
//    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (refreshLayout != null)
            // refreshLayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            initLoadData();
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
            // setEmptyView(false);
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

//    // js通信接口
//    public class JavascriptInterface {
//        @android.webkit.JavascriptInterface
//        public void GoToRecharge() {
//            //充值
//         /*   Intent intent = new Intent(getActivity(), WXPayEntryActivity.class);
//            intent.putExtra(WXPayEntryActivity.EXTRA_TYPE, WXPayEntryActivity.Type.CHARGE);
//            startActivityForResult(intent, REQUEST_RECHARGE);*/
//            Utils.gotoPayEntryActivity(getActivity());
//        }
//
//        @android.webkit.JavascriptInterface
//        public void GoToItemDetail(String str, String str1) {
//            //商品详情
//            try {
//                Intent intent = new Intent(getActivity(), ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, Integer.parseInt(str));
//                //intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, Integer.parseInt(str1));
//                startActivity(intent);
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @android.webkit.JavascriptInterface
//        public void GoToQsList(String str) {
//            try {
//                PinHuoModel model1 = new PinHuoModel();
//                model1.ID = Integer.parseInt(str);
//                PinHuoDetailListActivity.launch(getActivity(), model1, false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @android.webkit.JavascriptInterface
//        public void GoToGroupDetail(String str, String str1) {
//            try {
//                Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//                intent.putExtra(PostDetailActivity.EXTRA_TID, Integer.parseInt(str));
//
//                //帖子
//                if (str1.equals("activity")) {
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                } else {
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                }
//                getActivity().startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @android.webkit.JavascriptInterface
//        public void GoToUrl(String str, String str1) {
//            //如果为0表示App打开，1表示由系统浏览器打开
//            try {
//                if (!TextUtils.isEmpty(str)) {
//                    if (str1.equals("0")) {
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url = Uri.parse(str);
//                        intent.setData(content_url);
//                        getActivity().startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(getActivity(), ItemPreview1Activity.class);
//                        intent.putExtra("name", "天天拼货");
//                        intent.putExtra("url", str);
//                        getActivity().startActivity(intent);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//
//        @android.webkit.JavascriptInterface
//        public void GoToItemCat(String str, String str1, String str2) {
//            //分类
//            try {
//                Intent intent = new Intent(getActivity(), SortReasonActivity.class);
//                intent.putExtra(SortReasonActivity.EXTRA_RID, Integer.parseInt(str));
//                intent.putExtra(SortReasonActivity.EXTRA_VALUEIDS, str1);
//                intent.putExtra(SortReasonActivity.EXTRA_TITLE, str2);
//                getActivity().startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

    /**
     * 根据百分比改变颜色透明度
     */
//    public int changeAlpha(int color, float fraction) {
//        int red = Color.red(color);
//        int green = Color.green(color);
//        int blue = Color.blue(color);
//        int alpha = (int) (Color.alpha(color) * fraction);
//        return Color.argb(alpha, red, green, blue);
//    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "start car goods");
        MainNewActivity.currFragTag = Constant.FRAGMENT_FLAG_PIN_HUO;
        ChatUtl.setChatBroad(getActivity());
    }
    @Override
    public void onStart() {
        super.onStart();
        if (banner!=null)
            banner.startAutoPlay();
    }
    @Override
    public void onStop() {
        super.onStop();
        if (banner!=null)
            banner.stopAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getClass().equals(AppCompatRadioButton.class)) {
            curCategoryID = (int) v.getTag();
            mViewPager.setCurrentItem(catIDs.indexOf(curCategoryID));
            //loadData();
//            if (datas.get(catIDs.indexOf(curCategoryID)) == null) {
            // EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));
//            }
//            else {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_FINISHED, datas.get(catIDs.indexOf(curCategoryID))));
//
//                PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
//                _data.CurrCategoryID = curCategoryID;
//                _data.adList = ads.get(curCategoryID+"");
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
//            }
        } else {
            switch (v.getId()) {
//                case R.id.my_collect:
//                    Intent tuan = new Intent(mActivity, MyMainCollectionActivity.class);
//                    startActivity(tuan);
//                    break;

                case R.id.tv_all:
//                    //正在开拼
                    Intent it = new Intent(mActivity, PinHuoActivity.class);
                    //   it.putExtra(CARGOODSCOUNT, carCountTv.getmCircleText().getText().toString());
                    startActivity(it);
                    //我的关注
                    //startActivity(new Intent(mActivity, FocusActivity.class));
                    break;
                case R.id.iv_shopping_cart:
                    Utils.gotoShopcart(mActivity);
                    break;
                case R.id.iv_chat_txt:
//                    Intent intent = new Intent(getActivity(), ChatMainActivity.class);
//                    intent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
//                    startActivity(intent);
                    UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click11);
                    ChatUtl.goToChatMainActivity(getActivity(), true);
                    break;
                case R.id.iv_all_search:
                    UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click1);
                    CommonSearchActivity.launch(mActivity, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                    break;
                case R.id.iv_search:
                    UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click1);
                    CommonSearchActivity.launch(mActivity, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                    break;

            }
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onLoadPinAndForecastFailed() {

    }

    @Override
    public void onAllDataLoaded(ArrayList<PinHuoModel> pinList,
                                ArrayList<PinHuoModel> forecastList,
                                ArrayList<PinHuoModel> historyList) {

    }

    int selectIndex;

    public void onAllDataLoaded() {
        int index = 0;
        PinHuoNewResultModel nowData = null;
        mFragments.clear();
        //List<Fragment> list = getChildFragmentManager().getFragments();
        if (true) {
            for (PinHuoNewResultModel pm : datas) {
                if (pm != null) {
                    nowData = pm;
                    mFragments.add(PinHuoChildFragment.getInstance(
                            pm.ActivityList,
                            pm.CurrCategoryID,
                            ads.get(curCategoryID + ""), index));
                } else {
                    mFragments.add(PinHuoChildFragment.getInstance(
                            new ArrayList<PinHuoModel>(),
                            catIDs.get(index),
                            null, index));
                }
                index++;
            }
        } else {
//            for (PinHuoNewResultModel pm : datas) {
//                if (pm != null) {
//                    nowData = pm;
//                }
//                index++;
//            }
//            for (int i = list.size() - 1; i >= 0; i--) {
//                mFragments.add(list.get(i));
//            }
        }

        mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return ListUtils.getSize(mFragments);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(mFragments.size() - 1 > 0 ? mFragments.size() - 1 : 1);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mViewPager.setTag(position);
                RadioButton btn = (RadioButton) mRadioGroup.getChildAt(position);
                btn.setChecked(true);
                changeRdadioButton();
                BWApplication.PINHUPTITLE = btn.getText() + "";
                // ViewHub.showShortToast(getActivity(), btn.getText() + "");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("type", btn.getText() + "");
                UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click10, hashMap);
                curCategoryID = (int) btn.getTag();
                PinHuoChildFragment childFragment = ((PinHuoChildFragment) mFragments.get(position));
                if (childFragment != null) {
                    if (ListUtils.isEmpty(childFragment.mListData)) {
                        childFragment.getRefleshData();
                    }
                    flushAdView(childFragment.adList, childFragment.CustomHtml);
                }
                // readAdList();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //加载分类
        if (nowData != null) {
            mRadioGroup.removeAllViews();
            selectIndex = 0;
            int index1 = 0;
            for (PinHuoCategroyModel pcm : nowData.CategoryList) {
                RadioButton tempButton = (RadioButton) mActivity.getLayoutInflater().inflate(R.layout.radiobutton_pinhuo_new, null);
                RadioGroup.LayoutParams layoutParams=new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.MATCH_PARENT);
                layoutParams.leftMargin=ScreenUtils.dip2px(mActivity, DisplayUtil.getResDimen(mActivity,R.dimen.rb_item_margin));
                layoutParams.rightMargin=ScreenUtils.dip2px(mActivity, DisplayUtil.getResDimen(mActivity,R.dimen.rb_item_margin));
                tempButton.setLayoutParams(layoutParams);
                tempButton.setText(pcm.getName());
               // tempButton.setMaxEms(4);
               // tempButton.setMaxLines(1);
                if (index1 == 0) {
                    // ViewHub.showShortToast(getActivity(),pcm.getName()+"");
                    BWApplication.PINHUPTITLE = pcm.getName();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("type", pcm.getName() + "");
                    UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click10, hashMap);
                }
                tempButton.setTag(pcm.getCid());
                tempButton.setOnClickListener(this);
                if (curCategoryID > 0 && curCategoryID == pcm.getCid()) {
                    selectIndex = index1;
                }
                mRadioGroup.addView(tempButton);
                index1++;
            }
            ((RadioButton) mRadioGroup.getChildAt(selectIndex)).setChecked(true);
            changeRdadioButton();
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mCurrentCheckedRadioLeft = ((RadioButton) mRadioGroup.getChildAt(selectIndex)).getLeft();
//                    mHorizontalScrollView.smoothScrollTo((int)mCurrentCheckedRadioLeft-(int)getResources().getDimension(R.dimen.rdo2), 0);
//                }
//            },10);
            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                    int radioButtonId = group.getCheckedRadioButtonId();
                    //根据ID获取RadioButton的实例
                    RadioButton rb = (RadioButton) getActivity().findViewById(radioButtonId);
                    mCurrentCheckedRadioLeft = rb.getLeft();//更新当前蓝色横条距离左边的距离
                    mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
                }
            });
        }
        mViewPager.setCurrentItem(0);
        mPagerAdapter.notifyDataSetChanged();

    }

    private void changeRdadioButton() {
        if (mRadioGroup != null) {
            for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
                RadioButton rb = (RadioButton) mRadioGroup.getChildAt(i);
                if (rb.isChecked()) {
                    rb.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelOffset(R.dimen.gd_txt_size_large));
                } else {
                    rb.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX,mActivity.getResources().getDimensionPixelOffset(R.dimen.gd_txt_size_small));
                }
            }
        }
    }

    private void readAdList() {
        //读取广告
//        QuickSaleApi.getPinHuoNewAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                ArrayList<BannerAdModel> adList = (ArrayList<BannerAdModel>) object;
//                PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
//                _data.CurrCategoryID = curCategoryID;
//                _data.adList = adList;
//                ads.put(curCategoryID + "", adList);
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//        }), curCategoryID);
//        QuickSaleApi.getPinHuoV2NewAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
////                Log.d("ee",object.toString());
////                Log.d("ee",object.toString());
//                AdBean adBean = (AdBean) object;
//                PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
//                _data.CurrCategoryID = curCategoryID;
//                if (adBean == null) {
//                    _data.adList = new ArrayList<BannerAdModel>();
//                    _data.setCustomHtml("");
//                    // _data.setCustomHtml("<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>");
//                } else {
//                    _data.adList = adBean.getBannerAdModelList();
//                    _data.setCustomHtml(adBean.getCustomHtml());
//                    //  _data.setCustomHtml("<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>");
//                }
//                ads.put(curCategoryID + "", adBean);
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//        }), curCategoryID);
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNetCacheApi(TAG);
        addSubscribe(pinHuoApi.getBannersFormTypeV2(com.nahuo.constant.Constant.AreaTypeID.AREATYPEID_AD, curCategoryID
                , com.nahuo.constant.Constant.LoginRegisterFrom.LOGIN_REGISTER_FROM_ANDROID, 2)
                .compose(RxUtil.<PinHuoResponse<AdBean>>rxSchedulerHelper())
                .compose(RxUtil.<AdBean>handleResult())
                .subscribeWith(new ResourceSubscriber<AdBean>() {
                    @Override
                    public void onNext(AdBean object) {
                        AdBean adBean = object;
                        PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
                        _data.CurrCategoryID = curCategoryID;
                        if (adBean == null) {
                            _data.adList = new ArrayList<>();
                            _data.setCustomHtml("");
                            // _data.setCustomHtml("<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>");
                        } else {
                            _data.adList = adBean.getBannerAdModelList();
                            _data.setCustomHtml(adBean.getCustomHtml());
                            //  _data.setCustomHtml("<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>");
                        }
                        ads.put(curCategoryID + "", adBean);
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
                    }

                    @Override
                    public void onError(Throwable t) {
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));

                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }

    private void clear() {
        mFragments.clear();
        mViewPager.removeAllViewsInLayout();
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFRESH_COMPLETEd1:
                //关注后刷新数据
//                if (mViewPager!=null){
//                    if (mPagerAdapter!=null) {
//                        Fragment fragment= mPagerAdapter.getItem(mViewPager.getCurrentItem());
//                            if (fragment instanceof PinHuoChildFragment){
//                                PinHuoChildFragment childFragment= (PinHuoChildFragment) fragment;
//                                childFragment.getRefleshData();
//                            }
//                    }
//                }

                break;
            case EventBusId.MAIN_CHANGE_SHOPCAT:
                if (mViewPager != null)
                    mViewPager.setCurrentItem(0);
                break;
            case EventBusId.WEIXUN_NEW_MSG:
                if (carCountTv == null) {
                    return;
                }
                String num = event.data.toString();
                if (TextUtils.isEmpty(num)) {
                    carCountTv.setVisibility(View.GONE);
                } else {
                    carCountTv.setVisibility(View.VISIBLE);
                    carCountTv.setText(event.data.toString());
                }
                break;
            case EventBusId.PIN_HUO_SELECT_RB:
                int id = (int) event.data;
                int selectIndex = -1;
                int index = 0;
                for (int _id : catIDs) {
                    if (id == _id) {
                        selectIndex = index;
                    }
                    index++;
                }
                if (selectIndex >= 0) {
                    RadioButton rb = (RadioButton) mRadioGroup.getChildAt(selectIndex);
                    rb.setChecked(true);
                    curCategoryID = (int) rb.getTag();
                    mViewPager.setCurrentItem(catIDs.indexOf(curCategoryID));
                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));
                    // loadData(true);
                }
                break;
            case EventBusId.PINHUO_AD_REFRESH:
                readPopAdList();
                break;
            case EventBusId.RERRESH_PINHUO_TAB_FRAGMENT:
                initLoadData();
                break;
//            case EventBusId.LOAD_PIN_HUO_FINISHED:
//                PinHuoNewResultModel data1 = (PinHuoNewResultModel)event.data;
//                if (data1!=null)
//                {
//                            for(int i = 0;i<catIDs.size();i++)
//                            {
//                                if (catIDs.get(i)==data1.CurrCategoryID)
//                                {
//                                    datas.set(i,data1);
//                                }
//                            }
//                }
//                break;
        }
    }


}
