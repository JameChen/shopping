package com.nahuo.quicksale.mvp.view;


import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.mvp.MvpView;

import java.util.List;

/**
 * Created by JorsonWong on 2015/8/18.
 */
public interface SearchView extends MvpView {

    void showLoading(String msg);

    void hideLoading();

    void onSearchItemSuccess(List<ShopItemListModel> items);

    void onLoadMoreItems(List<ShopItemListModel> items);

    void onSearchFailed(String error);

    void onGetHotWordSuccess(String[] words,String[] brands);
    void onGetHotKeyWordsSuccess(String[] hotKeyWords);
    void onGetSearchSuccess(List<String> search);
    void onSearchAccount(List<ShopInfoModel> shopInfoModels);

    void onCleanSearchLogs();

    void onAddSearchWord(String word);

    void onSearchLogsLoaded(List<String> logs);

    void onShowBuy(GoodBaseInfo goodBaseInfo);

}
