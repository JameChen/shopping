package com.nahuo.quicksale.mvp.view;

import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.mvp.MvpView;

import java.util.ArrayList;

/**
 * Created by ZZB on 2015/10/13.
 */
public interface PinHuoView extends MvpView{

    void showLoading();
    void hideLoading();
    void onLoadPinAndForecastFailed();
    void onAllDataLoaded(ArrayList<PinHuoModel> pinList,
                         ArrayList<PinHuoModel> forecastList,
                         ArrayList<PinHuoModel> historyList);

}
