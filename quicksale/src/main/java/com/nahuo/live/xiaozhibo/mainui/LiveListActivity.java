package com.nahuo.live.xiaozhibo.mainui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.live.xiaozhibo.adpater.LiveListAdapter;
import com.nahuo.live.xiaozhibo.common.TCLiveRoomMgr;
import com.nahuo.live.xiaozhibo.common.utils.GlideUtls;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.common.widget.like.PLHeartLayout;
import com.nahuo.live.xiaozhibo.model.LiveDetailBean;
import com.nahuo.live.xiaozhibo.model.LiveListBean;
import com.nahuo.live.xiaozhibo.model.ParLiveListBean;
import com.nahuo.live.xiaozhibo.play.TCLivePlayerActivity;
import com.nahuo.live.xiaozhibo.play.TCVodPlayerActivity;
import com.nahuo.live.xiaozhibo.push.camera.TCLivePublisherActivity;
import com.nahuo.quicksale.CommonSearchActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.CircleCarTxtUtl;
import com.nahuo.quicksale.util.RxUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class LiveListActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView tvTitle,tvTRight;
    public String TAG = LiveListActivity.class.getSimpleName();
    private LiveListActivity Vthis = this;
    private SmartRefreshLayout refresh_layout;
    private RecyclerView recyclerView;
    private LiveListAdapter adapter;
    private int mBitrateType = TCConstants.BITRATE_NORMAL;
    private ImageView bg_iv, iv_likes;
    private TextView tv_head_title, tv_head_watch_count, tv_time_title;
    private PLHeartLayout heart_layout;
    private View layout_empty;
    private LiveListBean.SubLiveListBean mCurrentLiveItem;
    private View headView;
    private CircleTextView circleTextView;
    private ImageView img_Tsearch;
    protected EventBus mEventBus = EventBus.getDefault();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        if (mEventBus != null) {
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        } else {
            mEventBus = EventBus.getDefault();
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        }
        Vthis = this;
//        BaseRoom.mHttpRequest.destroyRoom("RoomID_47474_426188_3", SpManager.getIdentifier(Vthis), new HttpRequests.OnResponseCallback<HttpResponse>() {
//            @Override
//            public void onResponse(int retcode, @Nullable String retmsg, @Nullable HttpResponse data) {
//                Log.e("yu",retmsg+"---"+data.message);
//            }
//        });
      //  findViewById(R.id.line).setVisibility(View.GONE);
        TextView btnLeft = (TextView) findViewById(R.id.tvTLeft);
        tvTitle = (TextView) findViewById(R.id.tvTitleCenter);
        findViewById(R.id.iv_chat_txt).setOnClickListener(this);
        findViewById(R.id.iv_all_search).setOnClickListener(this);
        tvTRight= (TextView) findViewById(R.id.tvTRight);
        tvTRight.setVisibility(View.GONE);
        tvTitle.setText(R.string.title_activity_live_list);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        circleTextView = (CircleTextView) findViewById(R.id.circle_car_text);
        CircleCarTxtUtl.setColor(circleTextView);
        img_Tsearch = (ImageView) findViewById(R.id.img_Tsearch);
        img_Tsearch.setVisibility(View.GONE);
        findViewById(R.id.iv_all_search).setOnClickListener(this);
        //  tvTRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.toolbar_cart, 0);
        tvTRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.chat_pinhuo, 0);
        tvTRight.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(Vthis));
        refresh_layout = (SmartRefreshLayout) findViewById(R.id.refresh_layout);
        refresh_layout.setPrimaryColorsId(R.color.my_colorPrimary, android.R.color.white);
        //mRefreshHeader.setEnableHorizontalDrag(true);
        refresh_layout.setEnableHeaderTranslationContent(true);
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                onRefreshData();
            }
        });
        //swipeToLoadLayout.autoRefresh(100);
        refresh_layout.setEnableLoadMore(false);
        headView = getLayoutInflater().inflate(R.layout.layout_live_list_head, (ViewGroup) recyclerView.getParent(), false);

        adapter = new LiveListAdapter(Vthis);
        if (headView != null) {
            headView.setVisibility(View.GONE);
            heart_layout = (PLHeartLayout) headView.findViewById(R.id.heart_layout);
            headView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getLiveItem(mCurrentLiveItem);
                }
            });
            ViewGroup.LayoutParams params = headView.getLayoutParams();
            params.height = ScreenUtils.getScreenWidth(Vthis) - ScreenUtils.dip2px(Vthis, 20);
            bg_iv = (ImageView) headView.findViewById(R.id.bg_iv);
            iv_likes = (ImageView) headView.findViewById(R.id.iv_likes);
            tv_head_title = (TextView) headView.findViewById(R.id.tv_head_title);
            tv_head_watch_count = (TextView) headView.findViewById(R.id.tv_head_watch_count);
            tv_time_title = (TextView) headView.findViewById(R.id.tv_time_title);
            layout_empty = headView.findViewById(R.id.layout_empty);
            adapter.addHeaderView(headView);


        }
        adapter.setListener(new LiveListAdapter.LiveOnClickListener() {
            @Override
            public void onClick(LiveListBean.SubLiveListBean sub) {
                getLiveItem(sub);
            }
        });
        recyclerView.setAdapter(adapter);

        getLiveList();
    }
    @Override
    public void onResume() {
        super.onResume();
        //    new LoadGoodsTask(this, circleTextView).execute();
        ChatUtl.setChatBroad(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))
                mEventBus.unregister(this);
        }
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.WEIXUN_NEW_MSG:
                ChatUtl.judeChatNums(circleTextView, event);
                break;
        }
    }
    private void setRefreshFalse() {
        if (refresh_layout != null)
            refresh_layout.finishRefresh();
    }

    private void onRefreshData() {
        getLiveList();
    }

    /**
     * 获取列表
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    private void getLiveList() {

        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getLiveList()
                .compose(RxUtil.<PinHuoResponse<LiveListBean>>rxSchedulerHelper())
                .compose(RxUtil.<LiveListBean>handleResult())
                .subscribeWith(new CommonSubscriber<LiveListBean>(Vthis, true, R.string.loading) {
                    @Override
                    public void onNext(LiveListBean bean) {
                        super.onNext(bean);
//                        TCVideoListMgr.getInstance().fetchLiveList(new TCVideoListMgr.Listener() {
//                            @Override
//                            public void onVideoList(int retCode, ArrayList<TCVideoInfo> result, boolean refresh) {
//
//                            }
//                        });
                        setRefreshFalse();
                        ArrayList<MultiItemEntity> res = new ArrayList<>();
                        if (headView != null)
                            headView.setVisibility(View.VISIBLE);
                        if (bean != null) {
                            setHead(bean.getCurrentLiveItem());
                            if (!ListUtils.isEmpty(bean.getProcessLiveList())) {
                                ParLiveListBean parLiveListBean = new ParLiveListBean();
                                parLiveListBean.setTitle("开播中");
                                setSubLiveList(bean.getProcessLiveList(), LiveListBean.Type_ProcessLiveList);
                                parLiveListBean.setSubItems(bean.getProcessLiveList());
                                res.add(parLiveListBean);
                            }
                            if (!ListUtils.isEmpty(bean.getPreviewLiveList())) {
                                ParLiveListBean parLiveListBean = new ParLiveListBean();
                                parLiveListBean.setTitle("即将开始");
                                setSubLiveList(bean.getPreviewLiveList(), LiveListBean.Type_PreviewLiveList);
                                parLiveListBean.setSubItems(bean.getPreviewLiveList());
                                res.add(parLiveListBean);
                            }
                            if (!ListUtils.isEmpty(bean.getOverLiveList())) {
                                ParLiveListBean parLiveListBean = new ParLiveListBean();
                                parLiveListBean.setTitle("精彩回放");
                                setSubLiveList(bean.getOverLiveList(), LiveListBean.Type_OverLiveList);
                                parLiveListBean.setSubItems(bean.getOverLiveList());
                                res.add(parLiveListBean);
                            }
                        } else {
                            setHead(null);
                        }
                        if (adapter != null) {
                            adapter.setNewData(res);
                            adapter.expandAll();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        setRefreshFalse();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        setRefreshFalse();
                    }
                }));
    }

    private void setHead(LiveListBean.SubLiveListBean currentLiveItem) {
        mCurrentLiveItem = currentLiveItem;
        if (currentLiveItem != null) {
            mCurrentLiveItem.setType(LiveListBean.Type_ProcessLiveList);
            if (layout_empty != null)
                layout_empty.setVisibility(View.GONE);
            if (tv_head_title != null) {
                tv_head_title.setText(currentLiveItem.getTitle());
            }
            if (tv_head_watch_count != null) {
                tv_head_watch_count.setText(currentLiveItem.getWatchCount() + "人");
            }
            if (tv_time_title != null) {
                tv_time_title.setText(currentLiveItem.getTimeTitle());
            }
            GlideUtls.loadRoundedCorners(Vthis, R.drawable.live_bg_square, ImageUrlExtends.getImageUrl(currentLiveItem.getCover()), bg_iv, 15);
        } else {
            if (layout_empty != null)
                layout_empty.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取列表
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    private void getLiveItem(final LiveListBean.SubLiveListBean sub) {
        if (sub == null) {
            ViewHub.showShortToast(Vthis, "暂无直播");
            return;
        }
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
        ).getLiveItem(sub.getID())
                .compose(RxUtil.<PinHuoResponse<LiveDetailBean>>rxSchedulerHelper())
                .compose(RxUtil.<LiveDetailBean>handleResult())
                .subscribeWith(new CommonSubscriber<LiveDetailBean>(Vthis, true, R.string.loading) {
                    @Override
                    public void onNext(LiveDetailBean bean) {
                        super.onNext(bean);
                        if (bean == null) {
                            ViewHub.showShortToast(Vthis, "直播场次详细为空");
                            return;
                        }
                        String AnchorUserName = bean.getAnchorUserName();
                        int AnchorUserID = bean.getAnchorUserID();
                        String RoomID = bean.getRoomID();
                        String Title = bean.getTitle();
                        int WatchCount = bean.getWatchCount();
                        String LiveUrl = bean.getLiveUrl();
                        String liveUrl = bean.getLiveUrl();
                        switch (sub.getType()) {
                            case LiveListBean.Type_ProcessLiveList:
                                TCLiveRoomMgr.getLiveRoom().setRoomList(bean);
                                if (Utils.isHasTxIdentifier(Vthis)) {
                                    Intent intent = new Intent(Vthis, TCLivePlayerActivity.class);
                                    intent.putExtra(TCConstants.PLAY_URL, liveUrl);
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.PUSHER_ID, AnchorUserID + "");
                                    intent.putExtra(TCConstants.PUSHER_NAME, AnchorUserName);
                                    intent.putExtra(TCConstants.PUSHER_AVATAR, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.HEART_COUNT, "" + 0);
                                    intent.putExtra(TCConstants.MEMBER_COUNT, "" + WatchCount);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.PLAY_TYPE, true);
                                    intent.putExtra(TCConstants.FILE_ID, "");
                                    intent.putExtra(TCConstants.COVER_PIC, "");
                                    intent.putExtra(TCConstants.TIMESTAMP, System.currentTimeMillis());
                                    intent.putExtra(TCConstants.ROOM_TITLE, "");
                                  /*  intent.putExtra(TCConstants.ROOM_TITLE,
                                            "");
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.USER_ID, SpManager.getIdentifier(Vthis));
                                    intent.putExtra(TCConstants.USER_NICK, AnchorUserName);
                                    intent.putExtra(TCConstants.USER_HEADPIC, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.COVER_PIC, Const.getShopLogo(AnchorUserID));
//                        intent.putExtra(TCConstants.SCR_ORIENTATION, mOrientation);
                                    intent.putExtra(TCConstants.BITRATE, mBitrateType);
                                    intent.putExtra(TCConstants.USER_LOC,
                                            "");
                                    //intent.putExtra(TCConstants.SHARE_PLATFORM, mShare_meidia);*/
                                    startActivity(intent);
                                } else {
                                    ViewHub.showShortToast(Vthis, "直播账号为空");
                                }
                                break;
                            case LiveListBean.Type_PreviewLiveList:
                                if (sub.isCanWatch()) {
                                    TCLiveRoomMgr.getLiveRoom().setRoomList(bean);
                                    Intent intent = new Intent(Vthis, TCLivePublisherActivity.class);
                                    intent.putExtra(TCConstants.ROOM_TITLE,
                                            "");
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.USER_ID, SpManager.getIdentifier(Vthis));
                                    intent.putExtra(TCConstants.USER_NICK, AnchorUserName);
                                    intent.putExtra(TCConstants.USER_HEADPIC, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.COVER_PIC, Const.getShopLogo(AnchorUserID));
//                        intent.putExtra(TCConstants.SCR_ORIENTATION, mOrientation);
                                    intent.putExtra(TCConstants.BITRATE, mBitrateType);
                                    intent.putExtra(TCConstants.USER_LOC,
                                            "");
                                    //intent.putExtra(TCConstants.SHARE_PLATFORM, mShare_meidia);
                                    startActivity(intent);
                                } else {
                                    ViewHub.showLongToast(Vthis, sub.getMsg());
                                }
                                break;
                            case LiveListBean.Type_OverLiveList:
                                if (Utils.isHasTxIdentifier(Vthis)) {
                                    Intent intent = new Intent(Vthis, TCVodPlayerActivity.class);
                                    // Intent intent = new Intent(Vthis, TCLivePlayerActivity.class);
                                    // intent.putExtra(TCConstants.PLAY_URL, TextUtils.isEmpty(item.hlsPlayUrl) ? item.playurl : item.hlsPlayUrl);
                                    intent.putExtra(TCConstants.PLAY_URL, LiveUrl);
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.PUSHER_ID, SpManager.getIdentifier(Vthis));
                                    intent.putExtra(TCConstants.PUSHER_NAME, AnchorUserName);
                                    intent.putExtra(TCConstants.PUSHER_AVATAR, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.HEART_COUNT, "" + 0);
                                    intent.putExtra(TCConstants.MEMBER_COUNT, "" + WatchCount);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.PLAY_TYPE, false);
                                    intent.putExtra(TCConstants.FILE_ID, "");
                                    intent.putExtra(TCConstants.COVER_PIC, "");
                                    intent.putExtra(TCConstants.TIMESTAMP, "");
                                    intent.putExtra(TCConstants.ROOM_TITLE, Title);
                                    //startActivityForResult(intent, START_LIVE_PLAY);
                                    startActivity(intent);
                                } else {
                                    ViewHub.showLongToast(Vthis, "直播账号为空");
                                }
                                break;
                        }
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

    private void setSubLiveList(List<LiveListBean.SubLiveListBean> liveList, int type) {
        if (!ListUtils.isEmpty(liveList)) {
            for (LiveListBean.SubLiveListBean bean : liveList) {
                bean.setType(type);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTLeft:
                finish();
                break;
            case R.id.iv_all_search:
                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                break;
            case R.id.iv_chat_txt:
//                Intent cIntent = new Intent(this, ChatMainActivity.class);
//                cIntent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
//                startActivity(cIntent);
                ChatUtl.goToChatMainActivity(this, true);
                break;
        }
    }
}
