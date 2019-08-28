package com.nahuo.quicksale.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.nahuo.live.xiaozhibo.common.TCLiveRoomMgr;
import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.model.LiveDetailBean;
import com.nahuo.live.xiaozhibo.model.LiveListBean;
import com.nahuo.live.xiaozhibo.play.TCLivePlayerActivity;
import com.nahuo.live.xiaozhibo.play.TCVodPlayerActivity;
import com.nahuo.live.xiaozhibo.push.camera.TCLivePublisherActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.util.RxUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.nahuo.live.xiaozhibo.common.utils.TCConstants.PUSHER_ID;

/**
 * Created by jame on 2018/5/2.
 */

public class BaseNewFragment extends Fragment {
    protected CompositeDisposable mCompositeDisposable;
    protected Activity activity;
    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
    /**
     * 获取直播间
     *
     * @author James Chen
     * @create time in 2018/5/11 16:34
     */
    public void goLiveItem(String id) throws Exception{
        if (TextUtils.isEmpty(id)) {
            ViewHub.showShortToast(activity, "直播场次id为空");
            return;
        }
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi("goLiveItem"
        ).getLiveItem(Integer.parseInt(id))
                .compose(RxUtil.<PinHuoResponse<LiveDetailBean>>rxSchedulerHelper())
                .compose(RxUtil.<LiveDetailBean>handleResult())
                .subscribeWith(new CommonSubscriber<LiveDetailBean>(activity, true, R.string.loading) {
                    @Override
                    public void onNext(LiveDetailBean bean) {
                        super.onNext(bean);
                        if (bean == null) {
                            ViewHub.showShortToast(activity, "直播场次详细为空");
                            return;
                        }
                        String AnchorUserName = bean.getAnchorUserName();
                        int AnchorUserID = bean.getAnchorUserID();
                        String RoomID = bean.getRoomID();
                        String Title = bean.getTitle();
                        int WatchCount = bean.getWatchCount();
                        String LiveUrl = bean.getLiveUrl();
                        String liveUrl = bean.getLiveUrl();
                        switch (bean.getStatuID()) {
                            case LiveListBean.Type_ProcessLiveList:
                                TCLiveRoomMgr.getLiveRoom().setRoomList(bean);
                                if (Utils.isHasTxIdentifier(activity)) {
                                    Intent intent = new Intent(activity, TCLivePlayerActivity.class);
                                    intent.putExtra(TCConstants.PLAY_URL, liveUrl);
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(PUSHER_ID, AnchorUserID + "");
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
                                    ViewHub.showShortToast(activity, "直播账号为空");
                                }
                                break;
                            case LiveListBean.Type_PreviewLiveList:
                                if (bean.isCanWatch()) {
                                    TCLiveRoomMgr.getLiveRoom().setRoomList(bean);
                                    Intent intent = new Intent(activity, TCLivePublisherActivity.class);
                                    intent.putExtra(TCConstants.ROOM_TITLE,
                                            "");
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(TCConstants.GROUP_ID, RoomID);
                                    intent.putExtra(TCConstants.USER_ID, SpManager.getIdentifier(activity));
                                    intent.putExtra(TCConstants.USER_NICK, AnchorUserName);
                                    intent.putExtra(TCConstants.USER_HEADPIC, Const.getShopLogo(AnchorUserID));
                                    intent.putExtra(TCConstants.COVER_PIC, Const.getShopLogo(AnchorUserID));
//                        intent.putExtra(TCConstants.SCR_ORIENTATION, mOrientation);
                                    intent.putExtra(TCConstants.BITRATE, TCConstants.BITRATE_NORMAL);
                                    intent.putExtra(TCConstants.USER_LOC,
                                            "");
                                    //intent.putExtra(TCConstants.SHARE_PLATFORM, mShare_meidia);
                                    startActivity(intent);
                                } else {
                                    ViewHub.showLongToast(activity, bean.getMsg());
                                }
                                break;
                            case LiveListBean.Type_OverLiveList:
                                if (Utils.isHasTxIdentifier(activity)) {
                                    Intent intent = new Intent(activity, TCVodPlayerActivity.class);
                                    // Intent intent = new Intent(Vthis, TCLivePlayerActivity.class);
                                    // intent.putExtra(TCConstants.PLAY_URL, TextUtils.isEmpty(item.hlsPlayUrl) ? item.playurl : item.hlsPlayUrl);
                                    intent.putExtra(TCConstants.PLAY_URL, LiveUrl);
                                    intent.putExtra(TCConstants.LIVE_ITEM, bean);
                                    intent.putExtra(PUSHER_ID, SpManager.getIdentifier(activity));
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
                                    ViewHub.showLongToast(activity, "直播账号为空");
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
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
//        if (mCompositeDisposable == null) {
//            mCompositeDisposable = new CompositeDisposable();
//        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       unSubscribe();
    }
}
