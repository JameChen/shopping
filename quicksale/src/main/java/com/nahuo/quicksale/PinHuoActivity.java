package com.nahuo.quicksale;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.SimpleHttpRequestListener;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoResultModel;
import com.nahuo.quicksale.mvp.view.PinHuoView;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * 正在开拼
 * Created by ZZB on 2015/10/13.
 */
public class PinHuoActivity extends BaseActivity implements PinHuoView {
private static final String TAG=PinHuoActivity.class.getSimpleName();
    private LoadingDialog mLoadingDialog;

    private boolean mIsLoading;

    //debug
//    private int loadCount;
//    private String[] fakeData = new String[]{
//            "{\"StartList\":[{\"AppCover\":\"http:\\/\\/comm-img.b0.upaiyun.com\\/common\\/201510\\/130899806391727548.jpg\",\"Description\":\"aaaaaaaaaaaaaaa\",\"GroupDealCount\":5,\"ID\":43,\"Name\":\"aaaaaaaaaaaaaaa\",\"PCCover\":\"http:\\/\\/comm-img.b0.upaiyun.com\\/common\\/201510\\/130899806329508315.jpg\",\"StartTime\":\"2015-10-22 00:00:00\",\"ToTime\":\"2015-10-23 18:00:00\",\"Url\":\"www.baidu.com\"}],\"ReadyList\":[]}",
//            "{\"StartList\":[],\"ReadyList\":[]}",
//            "{\"StartList\":[{\"AppCover\":\"http:\\/\\/comm-img.b0.upaiyun.com\\/common\\/201510\\/130899806391727548.jpg\",\"Description\":\"aaaaaaaaaaaaaaa\",\"GroupDealCount\":5,\"ID\":43,\"Name\":\"aaaaaaaaaaaaaaa\",\"PCCover\":\"http:\\/\\/comm-img.b0.upaiyun.com\\/common\\/201510\\/130899806329508315.jpg\",\"StartTime\":\"2015-10-22 00:00:00\",\"ToTime\":\"2015-10-23 18:00:00\",\"Url\":\"www.baidu.com\"}],\"ReadyList\":[{\"AppCover\":\"http:\\/\\/comm-img.b0.upaiyun.com\\/common\\/201510\\/130899807016559029.jpg\",\"Description\":\"bbbbbb\",\"GroupDealCount\":5,\"ID\":47,\"Name\":\"bbbbbb\",\"PCCover\":\"http:\\/\\/comm-img.b0.upaiyun.com\\/common\\/201510\\/130899806955550411.jpg\",\"StartTime\":\"2015-10-23 09:00:00\",\"ToTime\":\"2015-10-23 18:00:00\",\"Url\":\"www.baidu.com\"}]}"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pin_huo);
        EventBus.getDefault().registerSticky(this);
        initViews();
        loadData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"start "+TAG);
    }

    private void replaceFragment(PinHuoResultModel result) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, PinHuoMainFragment.getInstance(result)).commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initViews() {

        mLoadingDialog = new LoadingDialog(this);
    }
    protected <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    private void loadData(boolean showLoading) {
        if (mIsLoading) {
            return;
        }
//        loadCount ++;
        mIsLoading = true;
        if (showLoading) {
            showLoading();
        }
        QuickSaleApi.getPinAndForecastList(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {

            @Override
            public void onRequestSuccess(String method, Object object) {
                loadFinished();
                PinHuoResultModel result = (PinHuoResultModel) object;
//                PinHuoResultModel result = GsonHelper.jsonToObject(fakeData[loadCount % 3], PinHuoResultModel.class);
                replaceFragment(result);
//                onAllDataLoaded(result.StartList, result.ReadyList);
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                loadFinished();
                onLoadPinAndForecastFailed();
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                loadFinished();
                onLoadPinAndForecastFailed();
            }
        }), 1, 100);
    }


    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.start();
        }
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.stop();
        }
    }

    private void loadFinished() {
        hideLoading();
        mIsLoading = false;
        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_FINISHED));
    }

    @Override
    public void onLoadPinAndForecastFailed() {
        loadFinished();
        ViewHub.showShortToast(this, "加载失败，请稍候再试");
    }


    @Override
    public void onAllDataLoaded(ArrayList<PinHuoModel> pinList,
                                ArrayList<PinHuoModel> forecastList,
                                ArrayList<PinHuoModel> historyList) {

    }


    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFRESH_ALL_ITEMS:// 刷新
                loadData(true);
                break;
            case EventBusId.REFRESH_PIN_HUO:
                loadData(false);
                break;
        }
    }
}
