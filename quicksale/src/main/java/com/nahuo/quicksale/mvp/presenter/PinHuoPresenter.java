package com.nahuo.quicksale.mvp.presenter;

import android.content.Context;

import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.SimpleHttpRequestListener;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoResultModel;
import com.nahuo.quicksale.mvp.MvpBasePresenter;
import com.nahuo.quicksale.mvp.view.PinHuoView;

import java.util.List;

/**
 * Created by ZZB on 2015/10/13.
 */
public class PinHuoPresenter extends MvpBasePresenter<PinHuoView> {

    public PinHuoPresenter(Context context){
        super(context);
    }
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private List<PinHuoModel> mPinList;
    private List<PinHuoModel> mPinForecastList;

    public void loadPinList(){
        HttpRequestListener listener = new SimpleHttpRequestListener() {

            @Override
            public void onRequestSuccess(String method, Object object) {
                loadFinished();
                PinHuoResultModel result = (PinHuoResultModel) object;
                if(isViewAttached()){
                    getView().onAllDataLoaded(result.ActivityList, result.ReadyList, result.OverList);
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                loadFinished();
                if(isViewAttached()){
                    getView().onLoadPinAndForecastFailed();
                }
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                loadFinished();
                if(isViewAttached()){
                    getView().onLoadPinAndForecastFailed();
                }
            }
        };
        getView().showLoading();
        QuickSaleApi.getPinAndForecastList(new RequestEntity(mAppContext, mRequestHelper, listener), 0, 50);
    }

    private void loadFinished() {
        if(isViewAttached()){
            getView().hideLoading();
        }
    }


}
