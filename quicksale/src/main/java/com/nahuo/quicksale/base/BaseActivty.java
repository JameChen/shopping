package com.nahuo.quicksale.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.baidu.mobstat.StatService;
import com.nahuo.live.xiaozhibo.mainui.LiveListActivity;
import com.nahuo.live.xiaozhibo.play.TCLivePlayerActivity;
import com.nahuo.live.xiaozhibo.play.TCVodPlayerActivity;
import com.nahuo.live.xiaozhibo.push.camera.TCLivePublisherActivity;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.GoodsCount;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by jame on 2017/3/31.
 */

public class BaseActivty extends Activity {
    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.getClass().getSimpleName().equals(MainNewActivity.class.getSimpleName())) {
            BWApplication.addActivity(this);
        }
        if (!this.getClass().getSimpleName().equals(MainNewActivity.class.getSimpleName())
                & !this.getClass().getSimpleName().equals(LiveListActivity.class.getSimpleName())
                & !this.getClass().getSimpleName().equals(TCVodPlayerActivity.class.getSimpleName())
                & !this.getClass().getSimpleName().equals(TCLivePublisherActivity.class.getSimpleName()
        ) & !this.getClass().getSimpleName().equals(TCLivePlayerActivity.class.getSimpleName())) {
            BWApplication.addVActivity(this);
        }
    }

    public  interface  GoodsTotalQtyCall{
         void getQty(int qty);
    }
    public void getGoodsTotalQty(String TAG, Activity Vthis, final GoodsTotalQtyCall call) {
        if (Vthis==null)
            return;
        if (Vthis.isFinishing())
            return;
        if (SpManager.getIs_Login(Vthis)) {
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG
            ).GetTotalQty()
                    .compose(RxUtil.<PinHuoResponse<GoodsCount>>rxSchedulerHelper())
                    .compose(RxUtil.<GoodsCount>handleResult())
                    .subscribeWith(new CommonSubscriber<GoodsCount>(Vthis) {
                        @Override
                        public void onNext(GoodsCount bean) {
                            super.onNext(bean);
                            int count = 0;
                            if (bean != null)
                                count = bean.TotalQty;
                            if (call!=null)
                                call.getQty(count);
                        }
                    }));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
        JPushInterface.onPause(this);
        UMengTestUtls.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
        JPushInterface.onResume(this);
        UMengTestUtls.onResume(this);
    }
}
