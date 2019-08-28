package com.nahuo.quicksale.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopInfoResultModel;
import com.nahuo.quicksale.oldermodel.ShopItemListResultModel;
import com.nahuo.quicksale.mvp.MvpBasePresenter;
import com.nahuo.quicksale.mvp.view.SearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by JorsonWong on 2015/8/18.
 */
public class SearchPresenter extends MvpBasePresenter<SearchView> implements HttpRequestListener {

    private static final String TAG = "SearchPresenter";

    private static final String PREF_KEY_HOT_SHOPS = "pref_key_hot_shops";//热店
    public static final String PREF_KEY_HOT_GOODS = "pref_key_hot_keywords";//热门商品
    private static final String PREF_KEY_SUPPLIER_HOT_GOODS = "pref_key_supplier_hot_keywords";//店铺热门商品

//    public static final String PREF_KEY_SHOP_SEARCH_LOG = "pref_key_account_search_log";//微铺号搜索记录
    public static final String PREF_KEY_GOODS_SEARCH_LOG = "pref_key_goods_search_log";//商品搜索记录

    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();

    private boolean mShowSearching;
    private boolean mHadInitHotShops;

    private int mSupplierId = 0;

    private final static int PAGE_SIZE = 20;

    public SearchPresenter(Context context) {
        super(context);

    }

    public void getHotGoods() {
        mShowSearching = false;
        try {
            String prefHotWordsStr = SpManager.getString(mContextRef.get(), PREF_KEY_HOT_GOODS, "");

            JSONObject jo = new JSONObject((String) prefHotWordsStr);
            JSONArray ja1 = jo.getJSONArray("HotCategroys");
            JSONArray ja2 = jo.getJSONArray("HotBrands");
            JSONArray ja3=jo.getJSONArray("HotKeyWords");
            String[] hotWords = new String[ja1.length()];
            for (int i = 0; i < ja1.length(); i++) {
                hotWords[i] = ja1.getString(i);
            }
            String[] hotBrandWords = new String[ja2.length()];
            for (int i = 0; i < ja2.length(); i++) {
                hotBrandWords[i] = ja2.getString(i);
            }
            String[] hotKeyWords = new String[ja3.length()];
            for (int i = 0; i < ja3.length(); i++) {
                hotKeyWords[i] = ja3.getString(i);
            }
            if (isViewAttached()){
                getView().onGetHotKeyWordsSuccess(hotKeyWords);
            }
            if (isViewAttached() && hotWords != null && hotBrandWords != null) {
                mHadInitHotShops = true;
                getView().onGetHotWordSuccess(hotWords,hotBrandWords);
            }
        }
        catch (Exception ex) {

        }

        HttpRequestHelper.HttpRequest httpRequest = mRequestHelper.getRequest(mContextRef.get().getApplicationContext(),
                RequestMethod.SearchMethod.GET_ITEM_HOT_KEYWORD, this);
        httpRequest.addParam("fromType", "3");
        httpRequest.addParam("userids", "-1");
        httpRequest.addParam("version", "2");
        httpRequest.setConvert2Token(new TypeToken<String>() {
        });
        httpRequest.doGet();
    }


    public void getGoodsSearchLogs() {
        String logs = SpManager.getString(mContextRef.get(), PREF_KEY_GOODS_SEARCH_LOG, "");
        if (!TextUtils.isEmpty(logs)) {
            String[] logss = logs.split(";");
            if (logss != null && logss.length > 0) {
                List list = Arrays.asList(logss);
                List arrayList = new ArrayList(list);
                getView().onSearchLogsLoaded(arrayList);
            }
        }
    }

    public void cleanGoodsSearchLogs() {
        cleanSearchLogs(PREF_KEY_GOODS_SEARCH_LOG);
    }

    public void getItemInfo(int id) {
        mShowSearching = true;
        HttpRequestHelper.HttpRequest httpRequest = mRequestHelper.getRequest(mContextRef.get(), RequestMethod.ShopMethod.SHOP_GET_ITEM_BASE_INFO, this);
        httpRequest.addParam("id", id + "");
        httpRequest.setConvert2Class(GoodBaseInfo.class);
        httpRequest.doPost();
    }

    //添加商品搜索记录
    public void addGoodsSearchLogs(String word){
        addWord2SearchLogs(PREF_KEY_GOODS_SEARCH_LOG, word);
    }
    private void addWord2SearchLogs(String prefKey, String word) {
        String logs = SpManager.getString(mContextRef.get(), prefKey, "");
        if (!logs.contains(word + ";")) {
            logs = word + ";" + logs;
            SpManager.setString(mContextRef.get(), prefKey, logs);
            if (isViewAttached()) {
                getView().onAddSearchWord(word);
            }
        }
    }

    private void cleanSearchLogs(String prefKey) {
        SpManager.setString(mContextRef.get(), prefKey, "");
        getView().onCleanSearchLogs();
    }


    @Override
    public void onRequestStart(String method) {
        if (isViewAttached() & mShowSearching) {
            String loadingStr = "搜索中...";
            if (RequestMethod.ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
                loadingStr = "获取商品信息中...";
            }
            getView().showLoading(loadingStr);
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (isViewAttached()) {
            if (RequestMethod.SearchMethod.GET_SHOP_INFO_LIST.equals(method)) {//搜索店铺结果
                ShopInfoResultModel model = (ShopInfoResultModel) object;
                if (model != null) {
                    getView().onSearchAccount(model.getDatas());
                }
            } else if (RequestMethod.SearchMethod.GET_AGENT_ITEMS.equals(method)
                    || RequestMethod.SearchMethod.GET_SHOP_ITEM2.equals(method)) {//搜索商品结果
                ShopItemListResultModel model = (ShopItemListResultModel) object;
                if (model != null) {
                    if (model.getPageIndex() == 1) {
                        getView().onSearchItemSuccess(model.getDatas());
                    } else {
                        getView().onLoadMoreItems(model.getDatas());
                    }
                }
            } else if (RequestMethod.SearchMethod.GET_ITEM_HOT_KEYWORD.equals(method)) {//热门商品
                try {
                    JSONObject jo = new JSONObject((String) object);
                    JSONArray ja1 = jo.getJSONArray("HotCategroys");
                    JSONArray ja2 = jo.getJSONArray("HotBrands");
                    JSONArray ja3=jo.getJSONArray("SearchKeywords");
                    if (ja3.length()>0){
                        ArrayList<String> search=new ArrayList<>();
                        for(int i=0;i<ja3.length();i++){
                            search.add(ja3.getString(i));
                        }
                        getView().onGetSearchSuccess(search);
                    }
                    String[] hotWords = new String[ja1.length()];
                    for(int i=0;i<ja1.length();i++){
                        hotWords[i] = ja1.getString(i);
                    }
                    String[] hotBrandWords = new String[ja2.length()];
                    for(int i=0;i<ja2.length();i++){
                        hotBrandWords[i] = ja2.getString(i);
                    }
                    SpManager.setString(mContextRef.get(), mSupplierId > 0 ? (PREF_KEY_SUPPLIER_HOT_GOODS + "_" + mSupplierId) : PREF_KEY_HOT_GOODS,
                            jo.toString());
                        if (!mHadInitHotShops) {
                            mHadInitHotShops = true;

                            getView().onGetHotWordSuccess(hotWords,hotBrandWords);
                        }
                }
                catch (Exception ex) {

                }
            } else if (RequestMethod.ShopMethod.SHOP_GET_ITEM_BASE_INFO.equals(method)) {
                GoodBaseInfo info = (GoodBaseInfo) object;
                if (info != null)
                    getView().onShowBuy(info);
            }
            getView().hideLoading();
        }

    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        if (isViewAttached()) {
            if (RequestMethod.SearchMethod.GET_AGENT_ITEMS.equals(method)
                    || RequestMethod.SearchMethod.GET_SHOP_INFO_LIST.equals(method)) {
                getView().onSearchFailed(msg);
            }
            getView().hideLoading();
        }
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (isViewAttached()) {
            if (RequestMethod.SearchMethod.GET_AGENT_ITEMS.equals(method)
                    || RequestMethod.SearchMethod.GET_SHOP_INFO_LIST.equals(method)) {
                getView().onSearchFailed(msg);
            }
            getView().hideLoading();
        }
    }


}
