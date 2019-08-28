package com.nahuo.quicksale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnLoadMoreListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView.OnRefreshListener;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.adapter.AllItemAdapter;
import com.nahuo.quicksale.adapter.AllItemAdapter.IBuyClickListener;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.RequestMethod.ShopMethod;
import com.nahuo.quicksale.common.Const.PostType;
import com.nahuo.quicksale.common.ListUtils;

import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.controls.SelectSizeColorMenu;
import com.nahuo.quicksale.controls.SelectSizeColorMenu.SelectMenuDismissListener;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.UserModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @description 代理
 * @created 2015-4-20 上午11:47:32
 * @author ZZB
 */
public class QuickSellFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener,
        View.OnClickListener {

    private static final String        TAG                     = QuickSellFragment.class.getSimpleName();
    public static final int            REQUEST_ITEM_DETAILS    = 1;
    public static final int            REQUEST_SHARE_TO_WP     = 4334;
    private HttpRequestHelper          mRequestHelper          = new HttpRequestHelper();
    private View mOnLineView, mOffLineView, mRootView;
    private PullToRefreshListViewEx    mListView;
    private AllItemAdapter             mAdapter;
    private static int                 PAGE_INDEX              = 0;
    private static final int           PAGE_SIZE               = 20;
    private boolean                    isTasking               = false;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, GoodBaseInfo> mGoodBaseInfos          = new HashMap<Integer, GoodBaseInfo>();
    private LoadingDialog              mLoadingDialog;
    private boolean                    mIsRefresh              = true;
    private EventBus                   mEventBus;
//    private View                       mEmptyView;
    private MyCountDownTimer mOnlineCountDownTimer, mOffLineCountDownTimer;
    private TextView mTvOnlineCountDown;
    private Calendar mOnlineStartCalendar, mOnlineEndCalendar, mCurrentCalendar;
    private static final int TYPE_ON_LINE = 1;
    private static final int TYPE_OFF_LINE = 2;
    private Context mContext;
    private RecommendModel mRecommendModel;
    private View mBtnKnowMore;
    private TextView mTvHour, mTvMin, mTvSec, mTvNextStartTime, mTvDay, mTvDayColon, mTvDayDesc, mTvDayDescColon;
    private ShopItemListModel mCollectItem;//收藏的商品

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mEventBus = EventBus.getDefault();
        mEventBus.registerSticky(this);
        mRootView = inflater.inflate(R.layout.frgm_quick_sell, container, false);
        initView();
        mListView.pull2RefreshManually();
        return mRootView;
    }


    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFRESH_ALL_ITEMS:// 刷新
                toRefresh();
                break;

        }
    }

    private void initActivityTime(long startMillis, long endMillis) {
        mTvNextStartTime.setText(TimeUtils.millisToTimestamp(startMillis, "yyyy-MM-dd HH:mm"));
        mCurrentCalendar = Calendar.getInstance();
        mOnlineStartCalendar = Calendar.getInstance();
        mOnlineStartCalendar.setTimeInMillis(startMillis);
        mOnlineEndCalendar = Calendar.getInstance();
        mOnlineEndCalendar.setTimeInMillis(endMillis);
//        boolean isDuringActivity = curMillis >= startMillis && curMillis <= endMillis;
        if( mRecommendModel.IsStart) {//活动开始
            Log.d("time", "活动开始：" + mRecommendModel.StartTime + ":" + mRecommendModel.EndTime);
            showOfflineView(false);
            showOnlineView(true);
            //换成远程推送
//            if (Math.abs(mRecommendModel.getStartMillis()-System.currentTimeMillis())<5000) {
//                NotificationHelper.CreateNotification(getActivity(), "拼货开始啦", "快来跟其他酷友一起参加拼货吧", "拼货开始啦");
//            }
        }else{//活动结束
            Log.d("time", "活动结束：" + mRecommendModel.NextStartTime + ":" + mRecommendModel.NextEndTime);
            showOfflineView(true);
            showOnlineView(false);
            //换成远程推送
//            if (Math.abs(mRecommendModel.getEndMillis()-System.currentTimeMillis())<5000) {
//                String shortTime = mRecommendModel.NextStartTime;
//                try
//                {
//                    shortTime = shortTime.substring(5);
//                }
//                catch(Exception ex){ ex.printStackTrace(); }
//                NotificationHelper.CreateNotification(getActivity(), "拼货结束了", "本场拼货已结束，"+shortTime+"再来", "拼货结束了");
//            }
        }

    }

    private void initView() {
        initOfflineView();//无闪批
        initOnlineView();//有闪批
    }

    private void initOfflineView() {
        mOffLineView = mRootView.findViewById(R.id.layout_hide_quick_sell);
        mBtnKnowMore = findOffViewById(R.id.btn_know_more);
        mBtnKnowMore.setOnClickListener(this);
        mTvHour = findOffViewById(R.id.tv_hour);
        mTvMin = findOffViewById(R.id.tv_min);
        mTvSec = findOffViewById(R.id.tv_sec);
        mTvNextStartTime = findOffViewById(R.id.tv_next_start_time);
        mTvDay = findOffViewById(R.id.tv_day);
        mTvDayDesc = findOffViewById(R.id.tv_day_desc);
        mTvDayColon = findOffViewById(R.id.tv_day_colon);
        mTvDayDescColon = findOffViewById(R.id.tv_day_desc_colon);
    }
    private void showDay(boolean show){
        mTvDay.setVisibility(show ? View.VISIBLE : View.GONE);
        mTvDayDesc.setVisibility(show ? View.VISIBLE : View.GONE);
        mTvDayColon.setVisibility(show ? View.VISIBLE : View.GONE);
        mTvDayDescColon.setVisibility(show ? View.VISIBLE : View.GONE);
    }
    private void initOnlineView() {
        mOnLineView = mRootView.findViewById(R.id.layout_quick_sell);
        mTvOnlineCountDown = (TextView) findOnViewById(R.id.tv_count_down);
        mListView = (PullToRefreshListViewEx) findOnViewById(R.id.lv_all_items);
        mListView.setCanLoadMore(true);
        mListView.setCanRefresh(true);
        mListView.setMoveToFirstItemAfterRefresh(true);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        mListView.setEmptyView(R.layout.layout_empty_quick_sell);

        mAdapter = new AllItemAdapter(getActivity());

        mAdapter.setListener(new AllItemAdapter.Listener() {
            @Override
            public void onItemClick(ShopItemListModel item) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                startActivityForResult(intent, REQUEST_ITEM_DETAILS);
            }
        });
        mAdapter.setIBuyClickListener(new IBuyClickListener() {
            @Override
            public void buyOnClickListener(ShopItemListModel model) {
                if (mGoodBaseInfos.containsKey(model.getID())) {
                    showMenu(mGoodBaseInfos.get(model.getID()));
                } else {
                    getItemInfo(model.getID());
                }
            }
        });
        mAdapter.setFavoriteListener(new AllItemAdapter.FavoriteListener() {
            @Override
            public void onFavoriteClick(ShopItemListModel item) {
                mCollectItem = item;
                QuickSaleApi.addFavorite(mContext, mRequestHelper, QuickSellFragment.this, item.getID());
            }
        });


        mListView.setAdapter(mAdapter);
    }


    @Override
    public void onRequestStart(String method) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
        }
        if (ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
            mLoadingDialog.setMessage("正在读取商品信息....");
            mLoadingDialog.show();
        } else if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            if(mOnLineView.getVisibility() != View.VISIBLE){
                mLoadingDialog.start("");
            }
        }else if(RequestMethod.QuickSaleMethod.ADD_FAVORITE.equals(method)){
            mLoadingDialog.start("收藏中...");
            mLoadingDialog.show();
        }

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        if (ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
            Log.i(TAG, "onRequestSuccess :" + object.toString());
            GoodBaseInfo info = (GoodBaseInfo)object;
            if (info != null) {
                mGoodBaseInfos.put(info.ItemID, info);
                showMenu(info);
            }
        } else if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            onDataLoaded(object);
        }else if(RequestMethod.QuickSaleMethod.USERS_SIGNATURE.equals(method)){
            onSignatureLoaded(object);
        }else if(RequestMethod.QuickSaleMethod.ADD_FAVORITE.equals(method)){//收藏
//            new CollectedPopupWindow(getActivity()).setShowDuration(4).show();
            ViewHub.showLongToast(getActivity(),"收藏成功");
            mAdapter.addCollectedItem(mCollectItem);
        }
    }

    private void onSignatureLoaded(Object object) {
        List<UserModel> users = (List<UserModel>) object;
        Map<Integer, String> map = new HashMap<>();
        for(UserModel user : users){
            map.put(user.getUserID(), user.getSignature());
        }
        mAdapter.setSignatures(map);

    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        } else if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            loadFinished();
            mListView.showErrorView();
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        } else if (ShopMethod.GET_AGENT_ITEMS.equals(method)) {
            loadFinished();
            mListView.showErrorView();
        }
    }

    private void onDataLoaded(Object obj) {
        loadFinished();
        mRecommendModel = (RecommendModel) obj;
//        long curMillis = System.currentTimeMillis();
//        mRecommendModel.IsStart = false;
//        mRecommendModel.StartTime = TimeUtils.millisToTimestamp(curMillis + 23*TimeUtils.HOUR_MILLIS, TimeUtils.DEFAULT_DATE_FORMAT);
//        mRecommendModel.EndTime = TimeUtils.millisToTimestamp(curMillis + 20*TimeUtils.DAY_MILLIS, TimeUtils.DEFAULT_DATE_FORMAT);
//        mRecommendModel.NextStartTime = TimeUtils.millisToTimestamp(curMillis + 30*TimeUtils.SECOND_MILLIS, TimeUtils.DEFAULT_DATE_FORMAT);
//        mRecommendModel.NextEndTime = TimeUtils.millisToTimestamp(curMillis + 40*TimeUtils.SECOND_MILLIS, TimeUtils.DEFAULT_DATE_FORMAT);
        List<ShopItemListModel> data = mRecommendModel.NewItems;
        if (mIsRefresh) {
            mAdapter.setData(data);
            if (mRecommendModel.getEndMillis()<System.currentTimeMillis()) {//今天的场次已结束，读取下一场的
                initActivityTime(mRecommendModel.getNextStartMillis(), mRecommendModel.getNextEndMillis());
            } else {
                initActivityTime(mRecommendModel.getStartMillis(), mRecommendModel.getEndMillis());

                SpManager.setQuickSellStartTime(mContext, mRecommendModel.getStartMillis());
                SpManager.setQuickSellEndTime(mContext, mRecommendModel.getEndMillis());
            }
        } else {
            mListView.setCanLoadMore(!ListUtils.isEmpty(data));
            mAdapter.addDataToTail(data);
        }
        mAdapter.notifyDataSetChanged();

        QuickSaleApi.getUserSignatures(mContext, mRequestHelper, this, data);

    }

    private void showMenu(final GoodBaseInfo info) {
        SelectSizeColorMenu menu = new SelectSizeColorMenu(getActivity(), info);
        menu.setSelectMenuDismissListener(new SelectMenuDismissListener() {
            @Override
            public void dismissStart(long duration) {
                ScaleAnimation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);
                animation.setDuration(duration);
                mOnLineView.startAnimation(animation);
            }

            @Override
            public void dismissEnd() {
            }
        });
        menu.show();
        ScaleAnimation animation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true);
        animation.setDuration(400);
        mOnLineView.startAnimation(animation);
    }

    private void loadData() {
        isTasking = true;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
//        QuickSaleApi.getRecommendShopItems(mContext, mRequestHelper, this, PAGE_INDEX, PAGE_SIZE);
    }

    private void getItemInfo(int id) {
        HttpRequest httpRequest = mRequestHelper.getRequest(getActivity().getApplicationContext(),
                ShopMethod.SHOP_GET_ITEM_BASE_INFO, this);
        httpRequest.addParam("id", id + "");
        httpRequest.setConvert2Class(GoodBaseInfo.class);
        httpRequest.doPost();
    }

    @Override
    public void onLoadMore() {
        Log.i(TAG, "onLoadMore");
        if (!isTasking) {
            mIsRefresh = false;
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh");
        if (!isTasking) {
            cancelTimer();
            mIsRefresh = true;
            loadData();
        }
    }

    private void cancelTimer() {
        if(mOffLineCountDownTimer != null){
            mOffLineCountDownTimer.cancel();
        }
        if(mOnlineCountDownTimer != null){
            mOnlineCountDownTimer.cancel();
        }
    }

    private View findOnViewById(int resId) {
        return mOnLineView.findViewById(resId);
    }
    private <T> T findOffViewById(int resId){
        return (T) mOffLineView.findViewById(resId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mEventBus.unregister(this);
    }

    private void loadFinished() {
        isTasking = false;
        mListView.onRefreshComplete();
        mListView.onLoadMoreComplete();
    }



    private void toRefresh() {
        mListView.setSelection(0);
        mListView.pull2RefreshManually();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_know_more://了解更多
                Intent intent = new Intent(mContext, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 460);
//                intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL, Const.getShopLogo(model.getUserID()));
//                intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, model.getTitle());
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, PostType.TOPIC);
                startActivity(intent);
                //91851
//                EventBus.getDefault().postSticky(
//                        BusEvent.getEvent(EventBusId.MAIN_CHANGE_CURRENT_TAB, MainActivity.TAG_MYITEMS));
                break;
        }
    }
    private void showOnlineView(boolean show){
        mOnLineView.setVisibility(show ? View.VISIBLE : View.GONE);
        if(show){
            long countdownDuration = mOnlineEndCalendar.getTimeInMillis() - mCurrentCalendar.getTimeInMillis() + 3000;
            mOnlineCountDownTimer = new MyCountDownTimer(countdownDuration, TYPE_ON_LINE);
            mOnlineCountDownTimer.start();
//            toRefresh();
        }else{
            if(mOnlineCountDownTimer != null){
                mOnlineCountDownTimer.cancel();
            }

        }
    }
    private void showOfflineView(boolean show){
        mOffLineView.setVisibility(show ? View.VISIBLE : View.GONE);
        if(show){
            long countdownDuration = mOnlineStartCalendar.getTimeInMillis() - mCurrentCalendar.getTimeInMillis() + 3000;
            mOffLineCountDownTimer = new MyCountDownTimer(countdownDuration, TYPE_OFF_LINE);
            mOffLineCountDownTimer.start();
        }else{
            if(mOffLineCountDownTimer != null){
                mOffLineCountDownTimer.cancel();
            }
        }
    }
    private class MyCountDownTimer extends CountDownTimer{

        private int mType;

        public MyCountDownTimer(long millisInFuture, int type) {
            super(millisInFuture, 1000);
            mType = type;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int hour = (int) (millisUntilFinished / TimeUtils.HOUR_MILLIS);
            int min = (int) ((millisUntilFinished - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
            int sec = (int) ((millisUntilFinished - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
            switch (mType){
                case TYPE_ON_LINE:
                    mTvOnlineCountDown.setText(hour + "小时" + min + "分" + sec + "秒");
                    break;
                case TYPE_OFF_LINE:
                    updateOffLineCounter(hour, min, sec);
                    break;
            }
        }

        @Override
        public void onFinish() {
//            if(mRecommendModel.getNextEndMillis() < System.currentTimeMillis()){
                Log.d("time", "下场活动也结束了：" + mRecommendModel.StartTime + ":" + mRecommendModel.EndTime);
                Log.d("time", "下场活动也结束了：" + mRecommendModel.NextStartTime + ":" + mRecommendModel.NextEndTime);
                mListView.pull2RefreshManually();
//            }else{
//                Log.d("time", "本场活动也结束：" + mRecommendModel.StartTime + ":" + mRecommendModel.EndTime);
//                Log.d("time", "下场活动也来了：" + mRecommendModel.NextStartTime + ":" + mRecommendModel.NextEndTime);
//                initActivityTime(mRecommendModel.getNextStartMillis(), mRecommendModel.getNextEndMillis());
//            }

        }
    }

    private void updateOffLineCounter(int hour, int min, int sec) {
        int day = 0;
        if(hour >= 24){
            showDay(true);
            day = hour / 24;
            hour = hour % 24;
            mTvDay.setText(day + "");
        }else{
            showDay(false);
        }
        mTvHour.setText(hour + "");
        mTvMin.setText(min + "");
        mTvSec.setText(sec + "");
    }
}
