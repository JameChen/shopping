package com.nahuo.quicksale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.activity.CommonSearchItemActivity;
import com.nahuo.quicksale.adapter.BaseSearchAdapter;
import com.nahuo.quicksale.adapter.FilterAdapter;
import com.nahuo.quicksale.adapter.SearchLogAdapter;
import com.nahuo.quicksale.adapter.ShopSearchAdapter;
import com.nahuo.quicksale.base.NoTitleAppCompatActivity;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.mvp.presenter.SearchPresenter;
import com.nahuo.quicksale.mvp.view.SearchView;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;

import java.util.LinkedList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.activity.SortReasonActivity.EXTRA_RID;
import static com.nahuo.quicksale.activity.SortReasonActivity.EXTRA_VALUEIDS;

/**
 * Created by JorsonWong on 2015/8/18.
 */
public class CommonSearchActivity extends NoTitleAppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, SearchView
        , View.OnTouchListener, PullToRefreshListView.OnLoadMoreListener {

    private static final String TAG = "CommonSearchActivity";

    private EditText mEtSearch;
    private AutoCompleteTextView autoCompleteTextView;
    private FlowLayout mHotWordsContainer;
    private LinearLayout mSearchResultNullContainer;
    //    private PullToRefreshListViewEx mLvSearchResult;
    private TextView mTvSearchResultNulTip, mTvSearchRecommend, mTvHotTitle;
    private ListView mLvHistory;
    private View mLlSearch, mLlSearchResult;
    private TextView tv_search_btn;
    private FlowLayout ll_history;
    private String[] mHotWords;
    private String[] mHotBrandWords;
    private String[] hotKeyWords;
    private SearchLogAdapter mLogAdapter;
    private BaseSearchAdapter mGoodsAdapter;
    private ShopSearchAdapter mShopAdapter;
    private int mCurKeywordPageIndex = 0;
    private LoadingDialog mLoadingDialog;
    private SearchPresenter mPresenter;
    private int mPageIndex = 1;
    private int supplierId;
    private View layout_history;
    private int hotWordRN = 0;
    private RadioGroup mRadioGroup;
    private int mKeywordPageSize = 100;
    /**
     * 搜索类型
     */
    public final static String EXTRA_SEARCH_TYPE = "extra_search_type";

    /**
     * 供应商id:搜索某个供应商商品时必须要传入
     */
    public final static String EXTRA_SUPPLIER_ID = "extra_supplier_id";

    private ScaleAnimation animShow = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

    private SearchType mSearchType;
    private LinkedList<String> linkedList = new LinkedList<>();
    private int rid;
    private String valueIDS;
    private boolean isShowSearch = false;

    public enum SearchType {
        ALL_ITEM_SEARCH,//全局商品搜索
        PIN_HUO_DETAIL,//拼货详情列表
        PIN_HUO_COLLECTIONS,//收藏
        PIN_HUO_ALREADY,//已拼
        PIN_HUO_Follows,//已拼
        SHOP,//搜索微铺号
        ITEMS, //搜索货品更新
        SUPPLIER_ITEMS//搜索供应商商品
    }

    public static void launch(Context context, SearchType searchType) {
        Intent intent = new Intent(context, CommonSearchActivity.class);
        intent.putExtra(EXTRA_SEARCH_TYPE, searchType);
        context.startActivity(intent);
    }

    int padding;
    int paddingTB;
    FlowLayout.LayoutParams paramsx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = new SearchPresenter(this);
        mPresenters.add(mPresenter);
        animShow.setDuration(400);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mSearchType = (SearchType) getIntent().getSerializableExtra(EXTRA_SEARCH_TYPE);
        rid = getIntent().getIntExtra(EXTRA_RID, -1);
        valueIDS = getIntent().getStringExtra(EXTRA_VALUEIDS);
        padding = DisplayUtil.dip2px(this, 16);
        paddingTB = DisplayUtil.dip2px(this, 4);
        paramsx = new FlowLayout.LayoutParams(padding, padding);
        initView();
        initData();
        //setListViewHeightBasedOnChildren(mLvHistory);
    }

    private FilterAdapter<String> arrayAdapter;

    private void initData() {
        mLogAdapter = new SearchLogAdapter(this);
        // mLvHistory.setAdapter(mLogAdapter);
        switch (mSearchType) {
            case PIN_HUO_COLLECTIONS:
            case PIN_HUO_DETAIL:
            case PIN_HUO_ALREADY:
            case ALL_ITEM_SEARCH:
                mPresenter.getHotGoods();
                mPresenter.getGoodsSearchLogs();
                mTvHotTitle.setText("热点档口/品牌");
                mEtSearch.setHint(R.string.input_good_name);
//                String prefHotWordsStr = SpManager.getString(this, SearchPresenter.PREF_KEY_HOT_GOODS, "");
//                try {
//                    JSONObject jo = new JSONObject((String) prefHotWordsStr);
//                    JSONArray ja = jo.getJSONArray("SearchKeywords");
//                    if (ja.length()>0){
//                        //final String[] search = new String[ja.length()];
//                        final ArrayList<String> search=new ArrayList<>();
//                        for(int i=0;i<ja.length();i++){
//                          //  search[i] = ja.getString(i);
//                            search.add(ja.getString(i));
//                        }
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                break;
        }

    }

    private void initView() {
        tv_search_btn = (TextView) findViewById(R.id.tv_search_btn);
        tv_search_btn.setText(R.string.search_off);
        tv_search_btn.setOnClickListener(this);
        mLlSearch = findViewById(R.id.ll_search);
        mLlSearchResult = findViewById(R.id.ll_search_result);
        mTvHotTitle = (TextView) findViewById(R.id.tv_hot_words_title);
        layout_history = findViewById(R.id.layout_history);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_search);
        findViewById(R.id.tv_clear).setOnClickListener(this);
//        mLvSearchResult = (PullToRefreshListViewEx) findViewById(R.id.lv_search_result);
//        mLvSearchResult.setCanRefresh(false);
//        mLvSearchResult.setCanLoadMore(true);
//        mLvSearchResult.setOnLoadListener(this);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = mEtSearch.getText().toString().trim();
                    if (!TextUtils.isEmpty(text)) {
                        mPageIndex = 1;
                        search(text, mPageIndex);
                    } else {
                        ViewHub.showShortToast(getApplicationContext(), "请输入关键字搜索");
                    }
                }
                return false;
            }
        });
        mEtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mLlSearch.isShown()) {
                    mLlSearch.setVisibility(View.VISIBLE);
                    mLlSearchResult.setVisibility(View.GONE);
                }
                return false;
            }
        });
        autoCompleteTextView.setHint(SpManager.getSearchTip(this));
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = autoCompleteTextView.getText().toString().trim();
                    if (!TextUtils.isEmpty(text)) {
                        mPageIndex = 1;
                        search(text, mPageIndex);
                    } else {
                        ViewHub.showShortToast(getApplicationContext(), "请输入关键字搜索");
                    }
                }
                return false;
            }
        });
        autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (autoCompleteTextView.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;

                //触摸点位置判断
                if (event.getX() > autoCompleteTextView.getWidth() - autoCompleteTextView.getPaddingRight() - getResources().getDimension(R.dimen.autocompletetextview_pd)) {
                    autoCompleteTextView.setText("");
                }
                return false;
            }
        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    isShowSearch = false;
                    tv_search_btn.setText(R.string.search_off);
                    autoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.search_left_gray), null, null, null);
                } else {
                    isShowSearch = true;
                    tv_search_btn.setText(R.string.search_on);
                    autoCompleteTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.search_left_gray), null, getResources().getDrawable(R.drawable.txt_clear_bg), null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mHotWordsContainer = (FlowLayout) findViewById(R.id.ll_hot_word);
        ll_history = (FlowLayout) findViewById(R.id.ll_history);
        ll_history.setOnTouchListener(this);
        mHotWordsContainer.setOnTouchListener(this);
        mSearchResultNullContainer = (LinearLayout) findViewById(R.id.ll_search_null);
        mTvSearchResultNulTip = (TextView) findViewById(R.id.tv_result_null_tip);
        mTvSearchRecommend = (TextView) findViewById(R.id.tv_recommend);
        mTvSearchRecommend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        mTvSearchRecommend.setOnClickListener(this);
        mLvHistory = (ListView) findViewById(R.id.lv_history);

//        Button footer = new Button(this);
//        footer.setText("清空搜索记录");
//        footer.setBackgroundResource(R.drawable.listview_item_bg);
//        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
//        footer.setLayoutParams(params);
//        footer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = LightAlertDialog.Builder.create(CommonSearchActivity.this);
//                builder.setTitle("提示").setMessage("您确定要清空搜索记录吗？")
//                        .setNegativeButton(android.R.string.cancel, null)
//                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                mPresenter.cleanGoodsSearchLogs();
//                            }
//                        }).show();
//            }
//        });
//        mLvHistory.addFooterView(footer);
//        mLvHistory.setOnItemClickListener(this);

        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void reloadRadioBtn() {
        mCurKeywordPageIndex = 1;
        ((RadioButton) mRadioGroup.getChildAt(hotWordRN)).setChecked(true);
        switch (hotWordRN) {
            case 0:
                //档口品牌
                addWords(mHotBrandWords);
                break;
            case 1:
                //话题
                addWords(hotKeyWords);
//                //类目
//                addWords(mHotWords);
                break;
            case 2:
                //类目
                addWords(mHotWords);
                break;
        }
    }

    private void createHotWords(String[] words) {
        if (words != null && words.length > 0) {
            int padding = DisplayUtil.dip2px(this, 16);
            int paddingTB = DisplayUtil.dip2px(this, 4);

            FlowLayout.LayoutParams params2 = new FlowLayout.LayoutParams(padding, padding);

            for (String word : words) {
                TextView textView = new TextView(this);
                textView.setSingleLine(true);
                textView.setText(word);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(this);
                textView.setOnTouchListener(this);
                textView.setPadding(padding, paddingTB, padding, paddingTB);
                textView.setBackgroundResource(R.drawable.selector_hot_word);
                mHotWordsContainer.addView(textView, params2);
                textView.startAnimation(animShow);
            }
        }
        startHotWordChanger();
    }


    private void addWords(String[] strs) {
        if (strs != null) {
            mTvHotTitle.setVisibility(View.VISIBLE);
            mHotWordsContainer.removeAllViews();
//            int inBegin = mCurKeywordPageIndex * mKeywordPageSize;
//            int inEnd = (inBegin + mKeywordPageSize >= strs.length) ? strs.length : inBegin + mKeywordPageSize;
//            String[] words = new String[inEnd - inBegin];
//            int index = 0;
//            for (int i = inBegin; i < inEnd; i++) {
//                words[index] = strs[i];
//                index++;
//            }
            createHotWords(strs);//words
        } else {
            mTvHotTitle.setVisibility(View.GONE);
            mHotWordsContainer.removeAllViews();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear:
                AlertDialog.Builder builder = LightAlertDialog.Builder.create(CommonSearchActivity.this);
                builder.setTitle("提示").setMessage("您确定要清空搜索记录吗？")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.cleanGoodsSearchLogs();
                            }
                        }).show();
                break;
            case R.id.rb1:
                mEtSearch.setHint(R.string.input_good_name);
                autoCompleteTextView.setHint(SpManager.getSearchTip(this));
                mTvHotTitle.setText("热点档口/品牌：");
                hotWordRN = 0;
                reloadRadioBtn();
                break;
            case R.id.rb2:
                mEtSearch.setHint(R.string.input_good_name);
                autoCompleteTextView.setHint(SpManager.getSearchTip(this));
                mTvHotTitle.setText("热门类目：");
                hotWordRN = 2;
                reloadRadioBtn();

                break;
            case R.id.rb_mid:
                mEtSearch.setHint(R.string.input_good_name);
                autoCompleteTextView.setHint(SpManager.getSearchTip(this));
                mTvHotTitle.setText("话题搜索：");
                hotWordRN = 1;
                reloadRadioBtn();
                break;
            case R.id.iv_back:
                if (back2SearchPage()) {
                    return;
                }
                finish();
                break;
            case R.id.tv_search_btn:
                if (isShowSearch) {
                    String text = autoCompleteTextView.getText().toString().trim();
                    if (!TextUtils.isEmpty(text)) {
                        mPageIndex = 1;
                        search(text, mPageIndex);
                    } else {
                        ViewHub.showShortToast(getApplicationContext(), "请输入关键字搜索");
                    }
                } else {
                    if (back2SearchPage()) {
                        return;
                    }
                    FunctionHelper.hideSoftInput(this);
                    finish();
                }
                break;
            case R.id.tv_recommend:
                Intent it = new Intent(getApplicationContext(), ItemPreviewActivity.class);
                it.putExtra("url", "http://www.nahuo.com/product/v3/?keyword=" + mEtSearch.getText());
                it.putExtra("title", mEtSearch.getText());
                startActivity(it);
                break;
            default:
                if (v instanceof TextView && v.getParent() == mHotWordsContainer) {
                    String word = ((TextView) v).getText().toString();
                    mPageIndex = 1;
                    search(word, mPageIndex);
                }
                if (v instanceof TextView && v.getParent() == ll_history) {
                    String word = ((TextView) v).getText().toString();
                    mPageIndex = 1;
                    search(word, mPageIndex);
                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        if (back2SearchPage()) {
            return;
        }
        super.onBackPressed();
    }

    private boolean back2SearchPage() {
        if (!mLlSearch.isShown()) {
            mLlSearch.setVisibility(View.VISIBLE);
            mLlSearchResult.setVisibility(View.GONE);
            // findViewById(R.id.tv_search_log).setVisibility(mLogAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
            // mLvHistory.setVisibility(mLogAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
            if (!ListUtils.isEmpty(linkedList)) {
                findViewById(R.id.tv_search_log).setVisibility(View.VISIBLE);
                layout_history.setVisibility(View.VISIBLE);
            } else {
                layout_history.setVisibility(View.GONE);
                findViewById(R.id.tv_search_log).setVisibility(View.GONE);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                stopHotWordChanger();
                break;
            case MotionEvent.ACTION_UP:
                startHotWordChanger();
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getAdapter().getItem(position).toString();
        mPageIndex = 1;
        search(text, mPageIndex);
    }

    @Override
    public void onResume() {
        startHotWordChanger();
        super.onResume();
    }

    @Override
    public void onPause() {
        stopHotWordChanger();
        super.onPause();
    }

    private void search(String search, int index) {
        int id = 0;
        switch (mSearchType) {
            case PIN_HUO_DETAIL:
                id = EventBusId.SEARCH_PIN_HUO_DETAIL;
                break;
//            case PIN_HUO_Follows:
//                id = EventBusId.SEARCH_PIN_HUO_FOLLOWS;
            case PIN_HUO_ALREADY:
                id = EventBusId.SEARCH_PIN_HUO_ALREADY;
                break;
            case PIN_HUO_COLLECTIONS:
                id = EventBusId.SEARCH_PIN_HUO_COLLECTIONS;
                break;
            case ALL_ITEM_SEARCH:
                Intent intent = new Intent(this, CommonSearchItemActivity.class);
                intent.putExtra(CommonSearchItemActivity.EXTRA_SEARCH_KEY, search);
                intent.putExtra(EXTRA_RID, rid);
                intent.putExtra(EXTRA_VALUEIDS, valueIDS);
                startActivity(intent);
                break;
        }
        EventBus.getDefault().post(BusEvent.getEvent(id, search));
        mPresenter.addGoodsSearchLogs(search);
        FunctionHelper.hideSoftInput(this);
        if (mSearchType != SearchType.ALL_ITEM_SEARCH) {
            finish();
        }
//        mEtSearch.setText(search);
//        if (mSearchType == SearchType.SHOP) {
//            mPresenter.searchShop(search, index);
//        } else if (mSearchType == SearchType.ITEMS) {
//            mPresenter.searchGoodsByKeyWord(search, index);
//        } else if (mSearchType == SearchType.SUPPLIER_ITEMS) {
//            mPresenter.searchSupplierGoodsByKeyWord(supplierId, search, index);
//        }
    }

    private void startHotWordChanger() {
        if (mHotWords != null && mHotWords.length > mKeywordPageSize) {//大于一页才会轮播
            stopHotWordChanger();
            mHandler.postDelayed(mHotWordTimerTask, 5000);
        }
    }

    private void stopHotWordChanger() {
        mHandler.removeCallbacks(mHotWordTimerTask);
    }

    @Override
    public void showLoading(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        mLoadingDialog.start(msg);
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null)
            mLoadingDialog.stop();
    }

    @Override
    public void onLoadMoreItems(List<ShopItemListModel> items) {
//        mLvSearchResult.onLoadMoreComplete();
//        if (!ListUtils.isEmpty(items)) {
//            mGoodsAdapter.addDataToTail(items);
//            mGoodsAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onGetHotWordSuccess(String[] words, String[] brands) {
        mHotWords = words;
        mHotBrandWords = brands;
        //addWords(mHotWords);
        addWords(mHotBrandWords);
    }

    @Override
    public void onGetHotKeyWordsSuccess(String[] hotKeyWords) {
        this.hotKeyWords = hotKeyWords;
        addWords(hotKeyWords);
    }

    @Override
    public void onGetSearchSuccess(List<String> search) {
        arrayAdapter = new FilterAdapter<>(this, search);
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPageIndex = 1;
                if (position >= 0) {
                    String item = (String) parent.getItemAtPosition(position);
                    search(item, mPageIndex);
                }

            }
        });
    }

    @Override
    public void onSearchItemSuccess(List<ShopItemListModel> items) {
//        if (mSearchType == SearchType.SUPPLIER_ITEMS) {
//            mGoodsAdapter = new SearchShopItemsAdapter(this);
//        } else {
//            mGoodsAdapter = new SearchAllItemsAdapter(this);
//        }
//        mGoodsAdapter.setIBuyClickListener(new BaseSearchAdapter.IBuyClickListener() {
//            @Override
//            public void buyOnClickListener(ShopItemListModel model) {
//                mPresenter.getItemInfo(model.getID());
//            }
//        });
//
//        mLlSearch.setVisibility(View.GONE);
//        mLlSearchResult.setVisibility(View.VISIBLE);
//        mLvSearchResult.onLoadMoreComplete();
//        mLvSearchResult.setAdapter(mGoodsAdapter);
//        if (ListUtils.isEmpty(items)) {
//            Log.i(TAG, "search result null!");
//            mSearchResultNullContainer.setVisibility(View.VISIBLE);
//            mLvSearchResult.setVisibility(View.GONE);
//            mGoodsAdapter.clear();
//            mGoodsAdapter.notifyDataSetChanged();
//            mTvSearchResultNulTip.setText(getString(R.string.search_result_null, mEtSearch.getText()));
//            mTvSearchRecommend.setText(getString(R.string.nahuo_search, mEtSearch.getText()));
//        } else {
//            mSearchResultNullContainer.setVisibility(View.GONE);
//            mLvSearchResult.setVisibility(View.VISIBLE);
//            mGoodsAdapter.clear();
//            mGoodsAdapter.setData(items);
//            mGoodsAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onSearchFailed(String error) {
        ViewHub.showShortToast(getApplicationContext(), error);
    }

    @Override
    public void onSearchAccount(List<ShopInfoModel> shopInfoModels) {
//        mShopAdapter = new ShopSearchAdapter(this);
//
//        mLlSearch.setVisibility(View.GONE);
//        mLlSearchResult.setVisibility(View.VISIBLE);
//        mLvSearchResult.onLoadMoreComplete();
//        mLvSearchResult.setAdapter(mShopAdapter);
//
//        if (ListUtils.isEmpty(shopInfoModels)) {
//            Log.i(TAG, "search result null!");
//            mSearchResultNullContainer.setVisibility(View.VISIBLE);
//            mLvSearchResult.setVisibility(View.GONE);
//            mShopAdapter.getData().clear();
//            mShopAdapter.notifyDataSetChanged();
//            mTvSearchResultNulTip.setText(getString(R.string.search_result_null, mEtSearch.getText()));
//            mTvSearchRecommend.setText(getString(R.string.nahuo_search, mEtSearch.getText()));
//        } else {
//            mSearchResultNullContainer.setVisibility(View.GONE);
//            mLvSearchResult.setVisibility(View.VISIBLE);
//            mShopAdapter.getData().clear();
//            mShopAdapter.setData(shopInfoModels);
//            mShopAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onSearchLogsLoaded(List<String> logs) {
        if (!ListUtils.isEmpty(logs)) {
            layout_history.setVisibility(View.VISIBLE);
            findViewById(R.id.tv_search_log).setVisibility(View.VISIBLE);
//            mLogAdapter.setData(logs);
//            mLogAdapter.notifyDataSetChanged();
            // findViewById(R.id.tv_search_log).setVisibility(mLogAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
            // mLvHistory.setVisibility(mLogAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
            linkedList.clear();
            linkedList.addAll(logs);
            ll_history.removeAllViews();
            for (String ss : logs) {
                TextView textView = new TextView(this);
                textView.setSingleLine(true);
                textView.setText(ss);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(this);
                textView.setOnTouchListener(this);
                textView.setPadding(padding, paddingTB, padding, paddingTB);
                textView.setBackgroundResource(R.drawable.selector_hot_word);
                ll_history.addView(textView, paramsx);
                // textView.startAnimation(animShow);
            }
        } else {
            findViewById(R.id.tv_search_log).setVisibility(View.GONE);
            layout_history.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAddSearchWord(String word) {
//        if (mLogAdapter != null && !TextUtils.isEmpty(word)) {
//            mLogAdapter.getLogs().add(0, word);
//            mLogAdapter.notifyDataSetChanged();
//        }
        if (ll_history != null && !TextUtils.isEmpty(word)) {
            linkedList.addFirst(word);
            ll_history.removeAllViews();
            for (String ss : linkedList) {
                TextView textView = new TextView(this);
                textView.setSingleLine(true);
                textView.setText(ss);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(this);
                textView.setOnTouchListener(this);
                textView.setPadding(padding, paddingTB, padding, paddingTB);
                textView.setBackgroundResource(R.drawable.selector_hot_word);
                ll_history.addView(textView, paramsx);
                // textView.startAnimation(animShow);
            }
        }

    }

    @Override
    public void onCleanSearchLogs() {
//        if (mLogAdapter != null) {
//            mLogAdapter.getLogs().clear();
//            mLogAdapter.notifyDataSetChanged();
//            findViewById(R.id.tv_search_log).setVisibility(mLogAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
//            mLvHistory.setVisibility(mLogAdapter.getCount() > 0 ? View.VISIBLE : View.GONE);
//        }
        if (ll_history != null) {
            ll_history.removeAllViews();
            linkedList.clear();
            if (!ListUtils.isEmpty(linkedList)) {
                findViewById(R.id.tv_search_log).setVisibility(View.VISIBLE);
                layout_history.setVisibility(View.VISIBLE);
            } else {
                layout_history.setVisibility(View.GONE);
                findViewById(R.id.tv_search_log).setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onShowBuy(GoodBaseInfo goodBaseInfo) {
//        showMenu(goodBaseInfo);
    }

    @Override
    public void onLoadMore() {
//        mPageIndex++;
//        search(mEtSearch.getText().toString(), mPageIndex);
    }

//    private void showMenu(final GoodBaseInfo info) {
//        SelectSizeColorMenu menu = new SelectSizeColorMenu(this, info);
//        menu.setSelectMenuDismissListener(new SelectSizeColorMenu.SelectMenuDismissListener() {
//            @Override
//            public void dismissStart(long duration) {
//                ScaleAnimation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
//                        Animation.RELATIVE_TO_SELF, 0.5f);
//                animation.setFillAfter(true);
//                animation.setDuration(duration);
//                findViewById(R.id.rootView).startAnimation(animation);
//            }
//
//            @Override
//            public void dismissEnd() {
//            }
//        });
//        menu.show();
//        ScaleAnimation animation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setFillAfter(true);
//        animation.setDuration(400);
//        findViewById(R.id.rootView).startAnimation(animation);
//    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (hotWordRN) {
                case 0:
                    //档口品牌
                    addWords(mHotBrandWords);
                    break;
                case 1:
                    //话题
                    addWords(hotKeyWords);
                    // addWords(mHotWords);
                    break;
                case 2:
                    //类目
                    addWords(mHotWords);
                    break;
            }

            startHotWordChanger();
        }
    };

    private Runnable mHotWordTimerTask = new Runnable() {
        @Override
        public void run() {
            int pages = mHotWords.length / mKeywordPageSize;
            if (mHotWords.length % mKeywordPageSize > 0) {
                pages++;
            }
            if ((++mCurKeywordPageIndex) >= pages) {
                mCurKeywordPageIndex = 0;
            }
            mHandler.sendEmptyMessage(0);
        }
    };


}
