package com.nahuo.live.xiaozhibo.common.utils;

import android.app.FragmentManager;

import com.nahuo.live.demo.liveroom.LiveRoom;
import com.nahuo.live.xiaozhibo.common.widget.beautysetting.BeautyDialogFragment;


/**
 * Created by kuenzhang on 08/03/2018.
 */

public class TCBeautyHelper implements BeautyDialogFragment.OnBeautyParamsChangeListener {
    private BeautyDialogFragment mBeautyDialogFragment;
    private BeautyDialogFragment.BeautyParams   mBeautyParams   = new BeautyDialogFragment.BeautyParams();
    private LiveRoom mLiveRoom;

    public TCBeautyHelper(LiveRoom liveRoom) {
        mLiveRoom = liveRoom;
        mBeautyDialogFragment = new BeautyDialogFragment();
        mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams,this);
        //设置默认的滤镜参数
        mLiveRoom.setSpecialRatio(0.3f);
    }

    public void show(FragmentManager manager, String tag) {
        mBeautyDialogFragment.show(manager, tag);
    }

    public void dismiss() {
        mBeautyDialogFragment.dismiss();
    }

    public boolean isAdded() {
        return mBeautyDialogFragment.isAdded();
    }

    public BeautyDialogFragment.BeautyParams getParams() {
        return mBeautyParams;
    }
    @Override
    public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
        switch (key){
            case BeautyDialogFragment.BEAUTYPARAM_BEAUTY:
            case BeautyDialogFragment.BEAUTYPARAM_WHITE:
                if (mLiveRoom != null) {
                    mLiveRoom.setBeautyFilter(params.mBeautyStyle, params.mBeautyProgress, params.mWhiteProgress, params.mRuddyProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FACE_LIFT:
                if (mLiveRoom != null) {
                    mLiveRoom.setFaceSlimLevel(params.mFaceLiftProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_BIG_EYE:
                if (mLiveRoom != null) {
                    mLiveRoom.setEyeScaleLevel(params.mBigEyeProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                if (mLiveRoom != null) {
                    mLiveRoom.setFilter(TCUtils.getFilterBitmap(mBeautyDialogFragment.getResources(), params.mFilterIdx));
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_MOTION_TMPL:
                if (mLiveRoom != null){
                    mLiveRoom.setMotionTmpl(params.mMotionTmplPath);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_GREEN:
                if (mLiveRoom != null){
                    mLiveRoom.setGreenScreenFile(TCUtils.getGreenFileName(params.mGreenIdx));
                }
                break;
            default:
                break;
        }
    }
}
