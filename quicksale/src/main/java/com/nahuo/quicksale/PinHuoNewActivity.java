//package com.nahuo.quicksale;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.view.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//
//import com.nahuo.library.controls.FlowLayout;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.quicksale.Topic.PostDetailActivity;
//import com.nahuo.quicksale.adapter.PinHuoNewAdapter;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.QuickSaleApi;
//import com.nahuo.quicksale.api.SimpleHttpRequestListener;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout;
//import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.model.BannerAdModel;
//import com.nahuo.quicksale.model.PinHuoCategroyModel;
//import com.nahuo.quicksale.model.PinHuoModel;
//import com.nahuo.quicksale.model.RequestEntity;
//import com.nahuo.quicksale.model.ResultData;
//import com.nahuo.quicksale.model.quicksale.PinHuoNewResultModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 拼货页 2016-4-10新版本
// */
//public class PinHuoNewActivity extends FragmentActivity implements View.OnClickListener,IRefreshLayout.RefreshCallBack {
//
//    private Context mContext = this;
//    private LoadingDialog mLoadingDialog;
//    //private View pagerView;
//    private ViewPager mPager;
//   // private BannerAdAdapter mPagerAdapter;
//    //private FlowIndicator mLayoutIndicator;
//    private ListView mListView;
//    private PinHuoNewAdapter mAdapter;
//    private SwipeRefreshLayoutEx mRefreshLayout;
//    private RadioGroup mRadioGroup;
//
//    private MyCountDownTimer mCountDownTimer;
//    private boolean mIsLoading;
//    private int curCategoryID;
//    private PinHuoNewResultModel data;
//    private List<BannerAdModel> adList;
//    private View headerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pin_huo_new);
//        EventBus.getDefault().registerSticky(this);
//        initViews();
//        loadData(true);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    private void initViews() {
//        mCountDownTimer = new MyCountDownTimer();
//        mCountDownTimer.start();
//        mLoadingDialog = new LoadingDialog(this);
//
//        $(R.id.tv_all).setOnClickListener(this);
//        $(R.id.iv_shopping_cart).setOnClickListener(this);
//
//        //分类
//        mRadioGroup = (RadioGroup)findViewById(R.id.radio_group);
//
//        //列表
//        mRefreshLayout = $(R.id.refresh_layout);
//        mRefreshLayout.setCallBack(this);
//        mListView = $(R.id.listview);
//
//        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        headerView = lif.inflate(R.layout.layout_pin_huo_new_header, null);
//        mListView.addHeaderView(headerView);
//        //轮播
//        //pagerView = headerView.findViewById(R.id.pager_view);
//        mPager = (ViewPager) headerView.findViewById(R.id.pager);
//        //mLayoutIndicator = (FlowIndicator) headerView.findViewById(R.id.layout_indicator);
//        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
////                int position_check = 0;
////                position_check %= mPagerAdapter.getViewCount();
////                if (position_check<0){
////                    position_check = mPagerAdapter.getViewCount()+position_check;
////                }
//        //        mLayoutIndicator.setSelectedPos(position);
//    //           handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
//
//            }
////            @Override
////            public void onPageScrollStateChanged(int arg0) {
////                switch (arg0) {
////                    case ViewPager.SCROLL_STATE_DRAGGING:
////                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
////                        break;
////                    case ViewPager.SCROLL_STATE_IDLE:
////                        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
////                        break;
////                    default:
////                        break;
////                }
////            }
//        });
//
//        mAdapter = new PinHuoNewAdapter(this);
//        mListView.setAdapter(mAdapter);
//    }
//    private void gotoBannerJump(BannerAdModel selectAd)
//    {
//        switch (selectAd.TypeID)
//        {
//            case 1:
//            case 5: {//网页
//                Intent intent = new Intent(mContext, ItemPreviewActivity.class);
//                intent.putExtra("name", "");
//                intent.putExtra("url", selectAd.Content);
//                mContext.startActivity(intent);
//                break;
//            }
//            case 2: {//小组帖子
//                if (selectAd.Content.indexOf("/xiaozu/topic/") > 1) {
//                    String temp = "/xiaozu/topic/";
//                    int topicID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                    mContext.startActivity(intent);
//                } else if (selectAd.Content.indexOf("/xiaozu/act/") > 1) {
//                    String temp = "/xiaozu/act/";
//                    int actID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                    mContext.startActivity(intent);
//                }
//                break;
//            }
//            case 3:
//            {
//                PinHuoModel item = new PinHuoModel();
//                item.ID = Integer.parseInt(selectAd.Content);
//                PinHuoDetailListActivity.launch(mContext, item,true);
//                break;
//            }
//            case 4:{
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PIN_HUO_SELECT_RB, Integer.parseInt(selectAd.Content)));
//                break;
//            }
//        }
//    }
//
//    private class MyCountDownTimer extends CountDownTimer {
//        public MyCountDownTimer() {
//            super(999999999, 1000);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            mAdapter.notifyDataSetChanged();
//        }
//
//        @Override
//        public void onFinish() {
//            try {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_PIN_HUO));
////                getActivity().finish();
//            }
//            catch(Exception ex){
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    protected <T extends View> T $(int id) {
//        return (T) findViewById(id);
//    }
//
//    private void loadData(boolean showLoading) {
//        if(mIsLoading){
//            return;
//        }
//        mIsLoading = true;
//        if(showLoading){
//            showLoading();
//        }
//        QuickSaleApi.getPinHuoNewList(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                data = (PinHuoNewResultModel) object;
//                dataLoaded();
//                loadFinished();
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                loadFinished();
//                onLoadPinAndForecastFailed();
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                loadFinished();
//                onLoadPinAndForecastFailed();
//            }
//        }), curCategoryID, 1, 100);
//    }
//
//
//    public void showLoading() {
//        mLoadingDialog.start();
//    }
//
//    public void hideLoading() {
//        mLoadingDialog.stop();
//    }
//
//    private void loadFinished() {
//        hideLoading();
//        mIsLoading = false;
//        mRefreshLayout.completeRefresh();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//            mCountDownTimer = new MyCountDownTimer();
//            mCountDownTimer.start();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mCountDownTimer!=null) {
//            mCountDownTimer.cancel();
//            mCountDownTimer = null;
//        }
//    }
//
//    private void dataLoaded() {
//
//        //分类
//        mRadioGroup.removeAllViews();
//        int selectIndex = 0;
//        int index = 0;
//        for(PinHuoCategroyModel pcm : data.CategoryList)
//        {
//            RadioButton tempButton = (RadioButton) getLayoutInflater().inflate(R.layout.radiobutton_pinhuo_new, null);
//            tempButton.setText(pcm.getName());
//            tempButton.setTag(pcm.getCid());
//            tempButton.setOnClickListener(this);
//            if (curCategoryID == pcm.getCid())
//            {
//                selectIndex = index;
//            }
//            mRadioGroup.addView(tempButton, FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.MATCH_PARENT);
//            index++;
//        }
//        ((RadioButton)mRadioGroup.getChildAt(selectIndex)).setChecked(true);
//
//        curCategoryID = data.CurrCategoryID;
//        QuickSaleApi.getPinHuoNewAd(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                adList = (List<BannerAdModel>) object;
//                adLoaded();
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                adList = null;
//                adLoaded();
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                adList = null;
//                adLoaded();
//            }
//        }), curCategoryID);
//
//                //列表
//        mAdapter.setData(data.ActivityList);
//        mAdapter.notifyDataSetChanged();
//    }
//
//    private void adLoaded() {
//        if (adList == null || adList.size() <= 0) {
//            mListView.removeHeaderView(headerView);
//        } else {
//            ArrayList<String> imgs = new ArrayList<>();
//            for (BannerAdModel adModel : adList)
//            {
//                imgs.add(adModel.ImageUrl);
//            }
//            if (mListView.getHeaderViewsCount()>0)
//            {
//                mListView.removeHeaderView(headerView);
//            }
//            mListView.addHeaderView(headerView);
////            mPagerAdapter = new BannerAdAdapter(mContext);
////            mPager.setAdapter(mPagerAdapter);
////            mPagerAdapter.setOnItemClickLitener(new View.OnClickListener() {
////
////                @Override
////                public void onClick(View v) {
////                    BannerAdModel selectAd = adList.get((int) v.getTag());
////
////                    gotoBannerJump(selectAd);
////                }
////            });
////            mPagerAdapter.setData(imgs);
////            mPagerAdapter.notifyDataSetChanged();
////            mLayoutIndicator.setMaxNum(imgs.size());
////            mPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界
////            //开始轮播效果
////            handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
//        }
//    }
//
//    public void onLoadPinAndForecastFailed() {
//        loadFinished();
//        ViewHub.showShortToast(this, "加载失败，请稍候再试");
//    }
//
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//            case EventBusId.REFRESH_ALL_ITEMS:// 刷新
//                loadData(true);
//                break;
//            case EventBusId.REFRESH_PIN_HUO:
//                loadData(false);
//                break;
//            case EventBusId.PIN_HUO_SELECT_RB:
//                int id = (int)event.data;
//
//                int selectIndex=-1;
//                int index = 0;
//                for(PinHuoCategroyModel pcm : data.CategoryList)
//                {
//                    if (id == pcm.getCid())
//                    {
//                        selectIndex = index;
//                    }
//                    index++;
//                }
//                if (selectIndex>=0)
//                {
//                    RadioButton rb = (RadioButton)mRadioGroup.getChildAt(selectIndex);
//                    rb.setChecked(true);
//                    curCategoryID = (int)rb.getTag();
//                    loadData(true);
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v.getClass().equals(RadioButton.class))
//        {
//            curCategoryID = (int)v.getTag();
//            loadData(true);
////            ((RadioButton) v).setChecked(true);
//        }
//        else {
//            switch (v.getId()) {
//                case R.id.tv_all:
//                    Intent it = new Intent(this, PinHuoActivity.class);
//                    startActivity(it);
//                    break;
//                case R.id.iv_shopping_cart:
//                    Utils.gotoShopcart(this);
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        loadData(false);
//    }
//
//    @Override
//    public void onLoadMore() {
//
//    }
//}
