package com.nahuo.quicksale.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.FollowsBean;
import com.nahuo.bean.SearchPanelBean;
import com.nahuo.bean.SortMenusBean;
import com.nahuo.constant.IDsConstant;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.Bookends1;
import com.nahuo.quicksale.adapter.PinHuoDetailAdapter;
import com.nahuo.quicksale.adapter.PinHuoShowAdapter;
import com.nahuo.quicksale.adapter.SortFilterAdpater;
import com.nahuo.quicksale.adapter.SortMenusAdpater;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.SearchItemModel;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.util.RecyclerViewLoadMoreUtil;
import com.nahuo.quicksale.util.RxUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nahuo.quicksale.activity.SortReasonActivity.EXTRA_RID;
import static com.nahuo.quicksale.activity.SortReasonActivity.EXTRA_VALUEIDS;

public class CommonSearchItemActivity extends BaseAppCompatActivity implements PinHuoShowAdapter.TitleSortMenusLister, OnClickListener, HttpRequestListener {
    public static final String EXTRA_SEARCH_KEY = "EXTRA_SEARCH_KEY";
    private Context mContext = this;
    private static final String TAG = CommonSearchItemActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;

    private PinHuoShowAdapter mNewItemAdapter;
    private Bookends1<PinHuoDetailAdapter> mNewItemBookends1;
    protected LoadingDialog mLoadingDialog;

    private List<SearchItemModel> newitemdata;
    private List<SearchItemModel> passitemdata;

    private int displayMode = 0;
    private String searchKey = "";
    private SwipeRefreshLayout mRefreshLayout;
    public int newCount = 0;
    public int oldeCount = 0;
    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean mIsRefresh = true;
    public List<ShopItemListModel> firstPassItems = new ArrayList<>();
    private ProgressBar mLoadMorePB;
    private TextView mLoadMoreTxt;
    private RecommendModel mRecommendModel;
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private View mEmptyView;
    private List<FollowsBean> mListData = new ArrayList<>();
    // private ListView mListView;
    //  private FollowsAdapter mAdapter;
    private LinearLayout ll_bottom;
    private int currentMenuID;
    private TextView[] mTvs;
    private int mCrrentPos;
    private int sortBy = -1;
    private DrawerLayout mDrawerLayout;
    private View drawer_content;
    private TextView tv_draw_layout_ok, tv_draw_layout_clear;
    private RecyclerView recycler_drawer;
    private LinearLayoutManager linearLayoutManager;
    private SortFilterAdpater sortFilterAdpater;
    private int lastPosition = 0;
    private int lastOffset = 0;
    private boolean first_Post_Panels = true;
    private int ID = -1;
    private String filterValues = "";//筛选数据
    private int rid;
    private String valueIDS = "";
    private SearchPanelBean searchPanelBean = null;
    private List<SearchPanelBean.PanelsBeanX> Panels = new ArrayList<>();
    //private ObservableScrollView scrollView;
    private Drawable nav_choose_sel, nav_choose;
    private boolean is_First_Sort = true;
    private ImageView mVScrollToTop;
    private RadioButton headNewRb, headOlderRb;
    private View radio_group_head;
    private int displaystatuid = 0;
    private String Part1Title = "", Part2Title = "";
    boolean isGbHide = false;
    private TextView look_old_data, look_new_data;
    private boolean isHas_NewAndPass = false;
    private RecyclerViewLoadMoreUtil recyclerViewLoadMoreUtil;
    private List<ShopItemListModel> mData = new ArrayList<>();
    private List<SortMenusBean> sortMenus = new ArrayList<>();
    private boolean isLoadedPassItem = false;
    private boolean isCheckPassTitle = false;
    private boolean isFirstLoadTitle = true;
    private RecyclerView recyclerView_sort_menu;
    private SortMenusAdpater sortMenusAdpater;
    private boolean sortType_isNew = true;
    private boolean isScrollPassSort;
    private int sortPos = -1;
    private boolean oneUP = true, oneDown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_common_search_item);
        searchKey = getIntent().getStringExtra(EXTRA_SEARCH_KEY);
        rid = getIntent().getIntExtra(EXTRA_RID, -1);
        if (rid <= -1)
            rid = 0;
        valueIDS = getIntent().getStringExtra(EXTRA_VALUEIDS);
        initView();
        initData();
        // displayMode = 1;

    }

    private void initView() {
        initDrawView();
        radio_group_head = findViewById(R.id.radio_group);
        headNewRb = (RadioButton) findViewById(R.id.rb_new);
        headOlderRb = (RadioButton) findViewById(R.id.rb_older);
        headNewRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckPassTitle = false;
                sortType_isNew = true;
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                mNewItemAdapter.clear();
                displaystatuid = 0;
                displayMode = 0;
                loadData(true, true);
            }
        });
        headOlderRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckPassTitle = true;
                sortType_isNew = false;
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                mNewItemAdapter.clear();
                displaystatuid = 1;
                displayMode = 2;
                loadData(true, true);
            }
        });
        mEmptyView = findViewById(R.id.empty_view);
//        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
//        scrollView.setHeight(ScreenUtils.getScreenHeight(this));
//        scrollView.setImgeViewOnClickListener((ImageView) findViewById(R.id.iv_scroll_to_top));
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        recyclerView_sort_menu = (RecyclerView) findViewById(R.id.recyclerView_sort_menu);
        sortMenusAdpater = new SortMenusAdpater(mContext);
        sortMenusAdpater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isCheckPassTitle) {
                    sortType_isNew = false;
                } else {
                    if (isScrollPassSort) {
                        sortType_isNew = false;
                    } else {
                        sortType_isNew = true;
                    }
                }
                goToSortMenu((SortMenusBean) adapter.getData().get(position), position, sortType_isNew);
            }
        });
        mVScrollToTop = (ImageView) findViewById(R.id.iv_scroll_to_top);
//        mListView = (ListView) findViewById(R.id.listview);
//        mAdapter = new FollowsAdapter(this);
//        mListView.setAdapter(mAdapter);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("天天拼货团");//全局商品搜索
        findViewById(R.id.iv_left).setOnClickListener(this);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        final GridLayoutManager gridManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridManager);
        //mRefreshLayout.setCallBack(this);
        mNewItemAdapter = new PinHuoShowAdapter(this);
        mNewItemAdapter.setMenusLister(this);
//        mNewItemAdapter.setType(PinHuoDetailAdapter.TYPE_SEARCH);
//        mNewItemAdapter.setChangShi(true);
        mNewItemBookends1 = new Bookends1(mNewItemAdapter, gridManager);
        // mNewItemBookends1.addHeader(headView);
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_pin_huo_detail_passitem, null);
        mLoadMorePB = (ProgressBar) footerView.findViewById(R.id.progressbar);
        mLoadMoreTxt = (TextView) footerView.findViewById(R.id.load_more_txt);
        look_old_data = (TextView) footerView.findViewById(R.id.look_old_data);
        look_new_data = (TextView) footerView.findViewById(R.id.look_new_data);
        look_old_data.setOnClickListener(this);
        look_new_data.setOnClickListener(this);
        mNewItemAdapter.addFooterView(footerView);
        mRecyclerView.setAdapter(mNewItemAdapter);
        recyclerViewLoadMoreUtil = new RecyclerViewLoadMoreUtil();
        recyclerViewLoadMoreUtil.init(mContext, mVScrollToTop, mRefreshLayout, mRecyclerView, mNewItemAdapter, new RecyclerViewLoadMoreUtil.RefreshDataListener() {
            @Override
            public void onRefresh() {
                onMyRefresh();
            }

            @Override
            public boolean loadMore() {
                onMyLoadMore();
                return false;
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mScrollThreshold = ScreenUtils.px2dip(CommonSearchItemActivity.this, 30);
                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                int pos = manager.findFirstVisibleItemPosition();
                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
                if (!recyclerView.canScrollVertically(-1) && !isGbHide)
                    radio_group_head.setVisibility(View.VISIBLE);
                if (isSignificantDelta) {
                    if (dy > 0) {
                        radio_group_head.setVisibility(View.GONE);
                    } else {
                        if (!isGbHide)
                            radio_group_head.setVisibility(View.VISIBLE);
                    }
                }
                int sPos = mNewItemAdapter.getPassSortPos();
                if (sPos >= 0) {
                    sortPos = sPos;
                }
                if (pos >= sortPos && sortPos != -1) {
                    isScrollPassSort = true;
                } else {
                    isScrollPassSort = false;
                }
                if (isCheckPassTitle) {
                    headOlderRb.setChecked(true);
                } else {
                    if (isScrollPassSort) {
                        headOlderRb.setChecked(true);
                    } else {
                        headNewRb.setChecked(true);
                    }
                }
                //Log.e("yu", "dy===" + dy + "--pos===" + pos + "--sortPos==" + sortPos);
//                if (mVScrollToTop != null) {
//                    if (pos > 5 && mVScrollToTop.getVisibility() != View.VISIBLE) {
//                        mVScrollToTop.setVisibility(View.VISIBLE);
//                    } else if (pos <= 5 && mVScrollToTop.getVisibility() != View.GONE) {
//                        mVScrollToTop.setVisibility(View.GONE);
//                    }
//                }
            }
        });
//        mVScrollToTop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mRecyclerView != null)
//                    mRecyclerView.scrollToPosition(0);
//            }
//        });
        mNewItemAdapter.setmListener(new PinHuoShowAdapter.PinHuoListener() {
            @Override
            public void onRemoveFollowLongClick(FollowsBean item) {

            }

            @Override
            public void onNewOlderItemClick(ShopItemListModel item) {
                Intent intent = new Intent(CommonSearchItemActivity.this, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, 0);
                startActivity(intent);
            }
        });
        recyclerViewLoadMoreUtil.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        recyclerViewLoadMoreUtil.autoRefreshing();//第一次自动加载一次
//        mNewItemAdapter.setListener(new PinHuoDetailAdapter.Listener() {
//            @Override
//            public void onItemClick(ShopItemListModel item) {
//                Intent intent = new Intent(CommonSearchItemActivity.this, ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
//                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, 0);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public void OnClickSortMenus(SortMenusBean item, int pos, boolean sortType_isNew) {
        goToSortMenu(item, pos, sortType_isNew);
    }

    @Override
    public void onClickTitleMenus(boolean isCheckPassTitle) {
        this.isCheckPassTitle = isCheckPassTitle;
        if (isCheckPassTitle) {
            if (headOlderRb != null)
                headOlderRb.setChecked(true);
            //   olderRb.setChecked(true);
            mNewItemAdapter.clear();
            displaystatuid = 1;
            displayMode = 2;
            loadData(true, true);
        } else {
            if (headNewRb != null)
                headNewRb.setChecked(true);
            //  newRb.setChecked(true);
            mNewItemAdapter.clear();
            displayMode = 1;
            displaystatuid = 0;
            loadData(true, true);
        }
    }

    private void judeNewSortType() {
        if (this.sortType_isNew) {
            isCheckPassTitle = false;
            if (headNewRb != null)
                headNewRb.setChecked(true);
            //   olderRb.setChecked(true);
            displaystatuid = 0;
            displayMode = 0;
        } else {
            isCheckPassTitle = true;
            if (headOlderRb != null)
                headOlderRb.setChecked(true);
            //   olderRb.setChecked(true);
            displaystatuid = 1;
            displayMode = 2;
        }
    }

    private void setCheck(int value) {
        if (value != 20 && !ListUtils.isEmpty(sortMenus)) {
            for (SortMenusBean menusBean : sortMenus) {
                if (menusBean.getValue() == value) {
                    menusBean.isCheck = true;
                } else {
                    if (menusBean.getValue() != 20)
                        menusBean.isCheck = false;
                }
            }
        }
    }

    private void goToSortMenu(SortMenusBean item, int pos, boolean sortType_isNew) {
        this.sortType_isNew = sortType_isNew;
        if (item != null) {
            mCrrentPos = pos;
            int value = item.getValue();
            setCheck(value);
            if (value != 20)
                judeNewSortType();
            if (value == 20) {
                mDrawerLayout.openDrawer(drawer_content);
                if (first_Post_Panels) {
                    getSearchPanel();
                }
            } else if (value == 4) {
                sortBy = 5;
                sortMenus.get(pos).setValue(sortBy);
                mNewItemAdapter.clear();
                // displayMode = 0;
                loadData(true, true);
            } else if (value == 5) {
                sortBy = 4;
                sortMenus.get(pos).setValue(sortBy);
                mNewItemAdapter.clear();
                // displayMode = 0;
                loadData(true, true);
            } else {
                sortBy = value;
                mNewItemAdapter.clear();
                // displayMode = 0;
                loadData(true, true);
            }
            if (sortMenusAdpater != null)
                sortMenusAdpater.notifyDataSetChanged();
        }
    }

    private void initDrawView() {
        tv_draw_layout_ok = (TextView) findViewById(R.id.tv_draw_layout_ok);
        tv_draw_layout_ok.setOnClickListener(this);
        tv_draw_layout_clear = (TextView) findViewById(R.id.tv_draw_layout_clear);
        tv_draw_layout_clear.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        recycler_drawer = (RecyclerView) findViewById(R.id.recycler_drawer);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);
        recycler_drawer.setLayoutManager(linearLayoutManager);
        sortFilterAdpater = new SortFilterAdpater(this);
        sortFilterAdpater.setDrawRecyclerView(recycler_drawer);
        recycler_drawer.setAdapter(sortFilterAdpater);
        recycler_drawer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // getPositionAndOffset();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getLayoutManager() != null) {
                    getPositionAndOffset();
                }
            }
        });

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                scrollToPosition();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                FunctionHelper.hideSoftInput(CommonSearchItemActivity.this);
                if (recycler_drawer != null)
                    recycler_drawer.setVisibility(View.INVISIBLE);
                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        drawer_content = findViewById(R.id.drawer_content);
        drawer_content.setClickable(true);
    }


    /**
     * 让RecyclerView滚动到指定位置
     */
    private void scrollToPosition() {
        if (recycler_drawer != null)
            if (recycler_drawer.getLayoutManager() != null && lastPosition >= 0) {
                recycler_drawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffset);
                        recycler_drawer.setVisibility(View.VISIBLE);
                    }
                }, 100);
            }
    }

    /**
     * 记录RecyclerView当前位置
     */
    private void getPositionAndOffset() {
        //获取可视的第一个view
        View topView = linearLayoutManager.getChildAt(0);
        if (topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.getTop();
            //得到该View的数组位置
            lastPosition = linearLayoutManager.getPosition(topView);
        }
    }

    private void initData() {

        //mRefreshLayout.manualRefresh();
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        //  mLoadingDialog.start(getString(R.string.items_loadData_loading));
        // new Task().execute((Void) null);
    }


//    private void dataLoaded() {
//
//
////        if (newitemdata.size() > 0 || passitemdata.size() > 0) {
////            final GridLayoutManager gridManager = new GridLayoutManager(mContext, 2);
////            mRecyclerView.setLayoutManager(gridManager);
////            mNewItemAdapter = new CommonSearchItemAdapter();
////            mNewItemAdapter.setData(newitemdata);
////            if (passitemdata.size() > 0) {
////                mNewItemAdapter.setPassItemPosition();
////                mNewItemAdapter.addPassItem(passitemdata);
////            }
////            mNewItemBookends1 = new Bookends11(mNewItemAdapter, gridManager);
////
////            mRecyclerView.setAdapter(mNewItemBookends1);
//////            mNewItemAdapter.setListener(new CommonSearchItemAdapter.Listener() {
//////                @Override
//////                public void onItemClick(SearchItemModel item) {
//////                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
//////                    intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
//////                    intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, 0);
//////                    startActivity(intent);
//////                }
//////            });
////
////            mNewItemBookends1.notifyDataSetChanged();
////
////        } else {
////            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
////        }
//    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(drawer_content)) {
                mDrawerLayout.closeDrawer(drawer_content);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.look_old_data:
                isCheckPassTitle = true;
                sortType_isNew = false;
                if (headOlderRb != null)
                    headOlderRb.setChecked(true);
                //   olderRb.setChecked(true);
                mNewItemAdapter.clear();
                displaystatuid = 1;
                displayMode = 2;
                loadData(true, true);
                break;
            case R.id.look_new_data:
                isCheckPassTitle = false;
                sortType_isNew = true;
                if (headNewRb != null)
                    headNewRb.setChecked(true);
                //  newRb.setChecked(true);
                mNewItemAdapter.clear();
                displayMode = 0;
                displaystatuid = 0;
                loadData(true, true);
                break;
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_draw_layout_ok:
                mDrawerLayout.closeDrawer(drawer_content);
                setSortData();
                break;
            case R.id.tv_draw_layout_clear:
                if (sortFilterAdpater != null)
                    sortFilterAdpater.setClear();
                break;

        }
    }

    public void onMyRefresh() {
        mNewItemAdapter.clear();
        //displayMode = 0;
        loadData(true, false);
    }

    public void onMyLoadMore() {
        //mRefreshLayout.setCanLoadMore(true);
        loadData(false, false);
    }

    private void loadData(boolean isRefresh, boolean showLoading) {
        if (!isCheckPassTitle && isRefresh && displayMode == 2) {
            displayMode = 0;
        }
        recyclerViewLoadMoreUtil.setPullUpRefreshEnable(true);
        if (mLoadingDialog == null && mContext != null)
            mLoadingDialog = new LoadingDialog(mContext);
        mNewItemBookends1.setShowFooter(true);
        mLoadMorePB.setVisibility(View.VISIBLE);
        mLoadMoreTxt.setText("正在加载更多");
        //showEmptyView(false);
        mIsRefresh = isRefresh;
        PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
//        QuickSaleApi.getCommonSearchItem(this,
//                mRequestHelper,
//                this,
//                PAGE_INDEX,
//                PAGE_SIZE,
//                searchKey,
//                displayMode, rid, valueIDS, filterValues, sortBy);

        getSearchV2(rid, PAGE_INDEX, PAGE_SIZE, searchKey, sortBy == -1 ? null : sortBy + "",
                valueIDS, displayMode, filterValues);
    }

    private void getSearchV2(int rid, int pageIndex, int pageSize, String keyword, String sort, String valueIDS, int displayMode, String filterValues) {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getSearchV2(rid, pageIndex, pageSize, keyword, sort, valueIDS, displayMode, filterValues)
                .compose(RxUtil.<PinHuoResponse<RecommendModel>>rxSchedulerHelper())
                .compose(RxUtil.<RecommendModel>handleResult())
                .subscribeWith(new CommonSubscriber<RecommendModel>(mContext, true, R.string.loading) {
                    @Override
                    public void onNext(RecommendModel recommendModel) {
                        super.onNext(recommendModel);
                        onDataLoaded(recommendModel);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        loadFinished();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loadFinished();
                    }
                }));
    }

    private void loadFinished() {
        if (mLoadingDialog != null) {
            mLoadingDialog.stop();
        }
        //isTasking = false;
        recyclerViewLoadMoreUtil.endRefreshing();
        recyclerViewLoadMoreUtil.endLoading();
    }

    public void onLoadPinAndForecastFailed() {
        loadFinished();
        ViewHub.showShortToast(mContext, "加载失败，请稍候再试");
    }

    @Override
    public void onRequestStart(String method) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setMessage("正在获取数据...");
            mLoadingDialog.show();
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (mLoadingDialog != null) {
            mLoadingDialog.stop();
        }
        switch (method) {
            case RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_COMMONSEARCHITEM:
                onDataLoaded(object);
                break;
            case RequestMethod.IteMizeMethod.Get_Search_Panel:
                // Log.d("yu", object.toString());
                gotoSerchPabel((SearchPanelBean) object);
                break;
        }
    }

    private void gotoSerchPabel(SearchPanelBean object) {
        SearchPanelBean xsearchPanelBean = object;
        if (ListUtils.isEmpty(Panels)) {
            if (xsearchPanelBean != null) {
                if (!ListUtils.isEmpty(xsearchPanelBean.getPanels())) {
                    first_Post_Panels = false;
                    searchPanelBean = xsearchPanelBean;
                    Panels.addAll(xsearchPanelBean.getPanels());
                    if (sortFilterAdpater != null) {
                        sortFilterAdpater.setPanels(searchPanelBean);
                        sortFilterAdpater.notifyDataSetChanged();
                    }
                }
            }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null) {
            mLoadingDialog.stop();
        }
    }

    private void showEmptyView(boolean show) {
//        mRefreshLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        mEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 价格高 价格低 同时有
     */
    private boolean hasPriceValue(List<SortMenusBean> sortMenus) {
      /*  {
            "Title": "价格高",
                "Value": 4
        },
        {
            "Title": "价格低",
                "Value": 5
        },*/
        boolean flag = false;
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < sortMenus.size(); i++) {
            int value = sortMenus.get(i).getValue();
            if (value == 5 || value == 4) {
                values.add(value);
            }
        }
        if (values.size() == 2) {
            flag = true;
        }
        return flag;
    }

    private void initbottommenu() {
        // ID = mRecommendModel.getInfo().getID();
        if (mRecommendModel != null) {
            boolean hasValue;
            currentMenuID = mRecommendModel.getCurrentMenuID();
            List<SortMenusBean> sortMenus1 = mRecommendModel.getSortMenus();
            if (mNewItemAdapter != null)
                mNewItemAdapter.setCurrentMenuID(currentMenuID);
            if (is_First_Sort) {
                List<SortMenusBean> sortMenus2 = new ArrayList<>();
                if (!ListUtils.isEmpty(sortMenus1)) {
                    hasValue = hasPriceValue(sortMenus1);
                    if (hasValue) {
                        for (SortMenusBean sortMenusBean : sortMenus1) {
                            int value = sortMenusBean.getValue();
                            if (value != 5) {
                                if (currentMenuID == 4 || currentMenuID == 5) {
                                    if (value == 4) {
                                        sortMenusBean.setValue(currentMenuID);
                                        sortMenusBean.setTitle("价格");
                                    }
                                }
                                if (value == 4) {
                                    sortMenusBean.setTitle("价格");
                                }
                                sortMenus2.add(sortMenusBean);
                            }
                        }
                    } else {
                        sortMenus2.addAll(sortMenus1);
                    }
                }
                if (!ListUtils.isEmpty(sortMenus2)) {
                    for (SortMenusBean sortMenusBean : sortMenus2) {
                        if (sortMenusBean.getValue() == currentMenuID) {
                            sortMenusBean.isCheck = true;
                            break;
                        }
                    }
                }
                sortMenus.clear();
                sortMenus.addAll(sortMenus2);
                is_First_Sort = false;
            }
        }
        if (ListUtils.isEmpty(sortMenus)) {
            ll_bottom.setVisibility(View.GONE);
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            sortMenusAdpater.setCurrentMenuID(currentMenuID);
            sortMenusAdpater.setNewData(sortMenus);
            GridLayoutManager manager = new GridLayoutManager(mContext, sortMenus.size());
            //manager.setAutoMeasureEnabled(true);
            recyclerView_sort_menu.setLayoutManager(manager);
            recyclerView_sort_menu.setAdapter(sortMenusAdpater);
        }
    }

//    private void setItemOnclick(TextView[] mTvs, int pos, List<SortMenusBean> sortMenus, Drawable nav_nomal, Drawable nav_choose, Drawable nav_up, Drawable nav_dowm) {
//        for (int j = 0; j < mTvs.length; j++) {
//            if (j == pos) {
//                int value = sortMenus.get(pos).getValue();
//                if (value == 20) {
//                    mDrawerLayout.openDrawer(drawer_content);
//                    if (first_Post_Panels) {
//                        getSearchPanel();
//                    }
//                } else if (value == 4) {
//                    sortBy = 5;
//                    mTvs[j].setCompoundDrawables(null, null, nav_up, null);
//                    sortMenus.get(j).setValue(sortBy);
//                    mNewItemAdapter.clear();
//                    //displayMode = 0;
//                    loadData(true, true);
//                } else if (value == 5) {
//                    sortBy = 4;
//                    mTvs[j].setCompoundDrawables(null, null, nav_dowm, null);
//                    sortMenus.get(j).setValue(sortBy);
//                    mNewItemAdapter.clear();
//                    // displayMode = 0;
//                    loadData(true, true);
//                } else {
//                    sortBy = value;
//                    mTvs[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                    mNewItemAdapter.clear();
//                    // displayMode = 0;
//                    loadData(true, true);
//                }
//            } else {
//                int value = sortMenus.get(pos).getValue();
//                if (value != 20) {
//                    int sValue = sortMenus.get(j).getValue();
//                    if (sValue != 20) {
////                        if (sValue == 5) {
////                            mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
////                        } else if (sValue == 4) {
////                            mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
////                        }
//                        if (sValue == 5 || sValue == 4) {
//                            mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
//                            sortMenus.get(j).setValue(4);
//                        }
//                        mTvs[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
//                    }
//                }
//            }
//        }
//    }

    //筛选
    private void setSortData() {
        try {
            List<Boolean> price_is_selects = new ArrayList<>();
            if (searchPanelBean != null) {
                if (!ListUtils.isEmpty(searchPanelBean.getPanels())) {
                    JSONObject jObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (SearchPanelBean.PanelsBeanX panelsBeanX : searchPanelBean.getPanels()) {
                        if (panelsBeanX.getTypeID() == IDsConstant.TYPEID_PRICE) {
                            if (!ListUtils.isEmpty(panelsBeanX.getPanels())) {
                                for (SearchPanelBean.PanelsBeanX.PanelsBean panelsBean : panelsBeanX.getPanels()) {
                                    if (panelsBean.isSelect) {
                                        price_is_selects.add(true);
                                    }
                                }
                            }
                        }
                        JSONObject jsonObject = new JSONObject();
                        List<Integer> integerList = new ArrayList<>();
                        if (!ListUtils.isEmpty(panelsBeanX.getPanels())) {
                            for (int i = 0; i < panelsBeanX.getPanels().size(); i++) {
                                if (panelsBeanX.getPanels().get(i).isSelect) {
                                    integerList.add(panelsBeanX.getPanels().get(i).getID());
                                }
                            }
                        }
                        if (!ListUtils.isEmpty(integerList)) {
                            String values = integerList.toString().substring(1, integerList.toString().length() - 1);
                            jsonObject.put("TypeID", panelsBeanX.getTypeID());
                            jsonObject.put("Values", values);
                            jsonArray.put(jsonObject);
                        }
                    }
                    if (jsonArray.length() > 0) {
                        jObject.put("Params", jsonArray);
                    }
                    // if (price_is_selects.size() <= 0) {

                    if (searchPanelBean.getMinPrice() > -1)
                        jObject.put("MinPrice", searchPanelBean.getMinPrice());
                    if (searchPanelBean.getMaxPrice() > -1)
                        jObject.put("MaxPrice", searchPanelBean.getMaxPrice());
                    // }

                    if (jObject.length() <= 0) {
                        filterValues = "";
                        if (!ListUtils.isEmpty(sortMenus))
                            sortMenus.get(mCrrentPos).isCheck = false;
                        //                      mTvs[mCrrentPos].setCompoundDrawables(null, null, nav_choose, null);
//                        mTvs[mCrrentPos].setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
                    } else {
                        filterValues = jObject.toString();
                        if (!ListUtils.isEmpty(sortMenus))
                            sortMenus.get(mCrrentPos).isCheck = true;
//                        mTvs[mCrrentPos].setCompoundDrawables(null, null, nav_choose_sel, null);
//                        mTvs[mCrrentPos].setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
                    }
                    mNewItemAdapter.clear();
                    //  displayMode = 0;
                    judeNewSortType();
                    loadData(true, true);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onDataLoaded(Object obj) {


        try {
            //mRefreshLayout.setCanLoadMore(true);
            loadFinished();
            mRecommendModel = (RecommendModel) obj;
            // Bookends1.part2title = "往期好货";
            if (mRecommendModel != null) {
                mNewItemAdapter.setShowCoinPayIcon(mRecommendModel.isShowCoinPayIcon());
                Part1Title = mRecommendModel.getPart1Title();
                Part2Title = mRecommendModel.getPart2Title();
                headNewRb.setText(Part1Title);
                headOlderRb.setText(mRecommendModel.getPart2Title());
//                if (!TextUtils.isEmpty(mRecommendModel.getPart2Title()))
//                    Bookends1.part2title = mRecommendModel.getPart2Title();
//                if (!ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    if (mRecommendModel.NewItems.size() % 2 != 0) {
//                        mNewItemAdapter.setHasEmtyId(true);
//                    }
//                }
            }
            // displayMode 0 旧款新款 1新 2 旧
            if (mIsRefresh) {
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                look_old_data.setVisibility(View.GONE);
                look_new_data.setVisibility(View.GONE);
                initbottommenu();
//                if (ListUtils.isEmpty(mRecommendModel.ShopList)) {
//                    //mListView.setVisibility(View.GONE);
//                } else {
//                    //  mListView.setVisibility(View.VISIBLE);
//                    showEmptyView(false);
////                    mListData.clear();
////                    mListData.addAll(mRecommendModel.ShopList);
////                    mAdapter.setData(mListData);
////                    mAdapter.notifyDataSetChanged();
////                    AKUtil.setListViewHeightBasedOnChildren(mListView);
//                    for (FollowsBean bean : mRecommendModel.ShopList) {
//                        ShopItemListModel model = new ShopItemListModel();
//                        model.setItem_layout_type(ShopItemListModel.TYPE_SHOP);
//                        model.setFollowsBean(bean);
//                        mData.add(model);
//                    }
//                }
                newCount = 0;
                oldeCount = 0;
                isLoadedPassItem = false;
                // mNewItemAdapter.setData(mRecommendModel.NewItems);
                firstPassItems = mRecommendModel.PassItems;
                if (!ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    isHas_NewAndPass = true;
                } else {
                    isHas_NewAndPass = false;
                }
                if (!ListUtils.isEmpty(mRecommendModel.NewItems))
                    newCount = mRecommendModel.NewItems.size();
                if (!ListUtils.isEmpty(mRecommendModel.PassItems))
                    oldeCount = mRecommendModel.PassItems.size();
                showEmptyView(false);
                if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    //没数据
                    if (isFirstLoadTitle) {
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.GONE);
                        isGbHide = true;
                        isFirstLoadTitle = false;
                    }
//                    displayMode = 0;
//                    displaystatuid = 0;
                    showEmptyView(true);
                    // isCheckPassTitle = false;

                } else if (ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    //只有新款
                    if (isFirstLoadTitle) {
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.GONE);
                        isGbHide = true;
                        isFirstLoadTitle = false;
                    }
                    // addItemSortMenus();
                    if (!isCheckPassTitle) {
                        addNewData(mRecommendModel.NewItems, false);
                        if (newCount < 20) {
                            setNoMore();
                        } else {
                            displayMode = 1;
                            displaystatuid = 0;
                        }
                    }
                } else if (!ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    //只有旧款
                    if (displayMode==0||displayMode==1){
                        isCheckPassTitle=true;
                    }
                    if (isFirstLoadTitle) {
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.VISIBLE);
                        isGbHide = false;
                        headOlderRb.setVisibility(View.VISIBLE);
                        headOlderRb.setChecked(true);
                        headNewRb.setVisibility(View.GONE);
                        isFirstLoadTitle = false;
                    }

//                    addItemTitle(mRecommendModel, ShopItemListModel.Show_Older_Title);
//                    addItemSortMenus();
                    isLoadedPassItem = true;
                    if (isCheckPassTitle) {
                        addOlderData(mRecommendModel.PassItems, false);
                        if (oldeCount < 20) {
                            setNoMore();
                        } else {
                            displayMode = 2;
                            displaystatuid = 1;
                        }
                    }
                } else if (!ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    //有新款有旧款
                    if (isFirstLoadTitle) {
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.VISIBLE);
                        isGbHide = false;
                        isFirstLoadTitle = false;
                    }
                    // addItemTitle(mRecommendModel, ShopItemListModel.Show_All_Title);
                    // addItemSortMenus();
                    if (displayMode == 1 || displayMode == 0) {
                        isCheckPassTitle = false;
                        addNewData(mRecommendModel.NewItems, true);
                        if (newCount < 20) {
                            addItemTitle(mRecommendModel, ShopItemListModel.Show_Older_Title);
                            addItemSortMenus(false);
                            isLoadedPassItem = true;
                            addOlderData(mRecommendModel.PassItems, false);
                            if (mRecommendModel.PassItems.size() < 20) {
                                setNoMore();
                            } else {
                                displayMode = 2;
                                displaystatuid = 1;
                            }
                        } else {
                            displayMode = 1;
                            displaystatuid = 0;
                        }
                    } else {
                        isCheckPassTitle = true;
                        //刷新旧款
                        isLoadedPassItem = true;
                        addOlderData(mRecommendModel.PassItems, false);
                        if (oldeCount < 20) {
                            setNoMore();
                        } else {
                            displayMode = 2;
                            displaystatuid = 1;
                        }
                    }
                    if (radio_group_head != null)
                        radio_group_head.setVisibility(View.VISIBLE);
                    isGbHide = false;
                    headOlderRb.setVisibility(View.VISIBLE);
                    headNewRb.setVisibility(View.VISIBLE);
                    if (isCheckPassTitle) {
                        headOlderRb.setChecked(true);
                    } else {
                        headNewRb.setChecked(true);
                    }
                }
               /* if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    if (radio_group_head != null)
                        radio_group_head.setVisibility(View.GONE);
                    isGbHide = true;
                } else if (ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    if (radio_group_head != null)
                        radio_group_head.setVisibility(View.GONE);
                    isGbHide = true;
                } else if (!ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    if (radio_group_head != null)
                        radio_group_head.setVisibility(View.VISIBLE);
                    isGbHide = false;
//                    olderRb.setVisibility(View.VISIBLE);
//                    olderRb.setChecked(true);
//                    newRb.setVisibility(View.GONE);
                    headOlderRb.setVisibility(View.VISIBLE);
                    headOlderRb.setChecked(true);
                    headNewRb.setVisibility(View.GONE);
                } else if (!ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    if (radio_group_head != null)
                        radio_group_head.setVisibility(View.VISIBLE);
                    isGbHide = false;
                }*/
//                if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//
//                } else {
//                    if (displayMode == 0 || displayMode == 1) {
//                        Bookends1.passPosindex = -100;
//                        if (mRecommendModel.NewItems.size() < 20) {
//                            addNewData(mRecommendModel.NewItems, true);
//                            if (ListUtils.isEmpty(firstPassItems)) {
//
//                                addItemSortMenus(mRecommendModel);
//                                setNoMore();
//                            } else {
//                                addItemTitle(mRecommendModel, ShopItemListModel.Show_All_Title);
//                                addOlderData(firstPassItems, false);
//                                if (firstPassItems.size() < 20) {
//                                    setNoMore();
//                                } else {
//
//                                }
//                            }
//                            // setNoMore();
//                        } else {
//                            addNewData(mRecommendModel.NewItems, false);
//                        }
//                    } else if (displayMode == 2) {
//                        Bookends1.passPosindex = -100;
//                        addOlderData(firstPassItems, false);
//                        if (mRecommendModel.PassItems.size() < 20) {
//                            setNoMore();
//                        }
//                    }
//                }

//
//                if (!ListUtils.isEmpty(firstPassItems))
//                    oldeCount = firstPassItems.size();
//                if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    Bookends1.passPosindex = -100;
//                    if (ListUtils.isEmpty(mRecommendModel.ShopList)) {
//                        showEmptyView(true);
//                    }
//                } else if (!ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    showEmptyView(false);
//                    if (mRecommendModel.NewItems.size() < 20) {
//                        //新款加载完成去加载旧款
//                        newCount = mRecommendModel.NewItems.size();
//                        addNewData(mRecommendModel.NewItems, true);
//                        if (!ListUtils.isEmpty(firstPassItems)) {
//                            if (firstPassItems.size() + newCount < 20) {
//                                Bookends1.passPosindex = newCount;
////                                mNewItemAdapter.setPassItemPosition1();
////                                mNewItemAdapter.addPassItem(firstPassItems);
//                                mLoadMorePB.setVisibility(View.GONE);
//                                mLoadMoreTxt.setText("没有更多款式了");
//                                addItemTitle(mRecommendModel);
//                                addOlderData(firstPassItems, true);
//                               // load_more_no_data_txt.setVisibility(View.VISIBLE);
//                                ;
//                                // mRefreshLayout.setLoadMoreEnable(false);
//                                recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
//                            } else {
//                                Bookends1.passPosindex = newCount;
////                                mNewItemAdapter.setPassItemPosition1();
////                                mNewItemAdapter.addPassItem(firstPassItems);
//                                addItemTitle(mRecommendModel);
//                                addOlderData(firstPassItems, false);
//                                displayMode = 2;
//                            }
//
//                        } else {
//                            //没就旧款表示加载完
//                            Bookends1.passPosindex = -100;
//                            mLoadMorePB.setVisibility(View.GONE);
//                            mLoadMoreTxt.setText("没有更多款式了");
//                           // load_more_no_data_txt.setVisibility(View.VISIBLE);
//                            //mRefreshLayout.setLoadMoreEnable(false);
//                            recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
//                        }
//                    } else {
//                        //加载新款
//                        Bookends1.passPosindex = -100;
//                        newCount = mRecommendModel.NewItems.size();
//                        addNewData(mRecommendModel.NewItems, false);
//                        displayMode = 1;
//                    }
//                } else {
//                    //只有旧款时候
//                    if (!ListUtils.isEmpty(firstPassItems)) {
//                        showEmptyView(false);
//                        if (firstPassItems.size() < 20) {
//                            //加载完了
//                            Bookends1.passPosindex = 0;
////                            mNewItemAdapter.setPassItemPosition1();
////                            mNewItemAdapter.addPassItem(firstPassItems);
//                            mLoadMorePB.setVisibility(View.GONE);
//                            mLoadMoreTxt.setText("没有更多款式了");
//                            addItemTitle(mRecommendModel);
//                            addOlderData(firstPassItems, true);
//                           // load_more_no_data_txt.setVisibility(View.VISIBLE);
//                            // mRefreshLayout.setLoadMoreEnable(false);
//                            recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
//
//                        } else {
//                            //继续去加载旧款
//                            Bookends1.passPosindex = 0;
////                            mNewItemAdapter.setPassItemPosition1();
////                            mNewItemAdapter.addPassItem(firstPassItems);
//                            addItemTitle(mRecommendModel);
//                            addOlderData(firstPassItems, false);
//                            displayMode = 2;
//                        }
//                    } else {
//                        Bookends1.passPosindex = -100;
//                    }
//                }
                // mNewItemAdapter.notifyDataSetChanged();
                if (mNewItemAdapter != null)
                    mNewItemAdapter.isCheckPassTitle = this.isCheckPassTitle;
            } else {
                //加载更多
                //继续加载新款
                if (displayMode == 1 || displayMode == 0) {
                    if (ListUtils.isEmpty(mRecommendModel.NewItems)) {
                        if (ListUtils.isEmpty(firstPassItems)) {
                            setNoMore();
                        } else {
                            if (isLoadedPassItem) {
                                addOlderData(firstPassItems, false);
                            } else {
                                addItemTitle(mRecommendModel, ShopItemListModel.Show_Older_Title);
                                addItemSortMenus(false);
                                isLoadedPassItem = true;
                                addOlderData(firstPassItems, false);
                            }
                            displayMode = 2;
                            displaystatuid = 1;
                            PAGE_INDEX = 1;
                        }
                    } else {
                        if (mRecommendModel.NewItems.size() < 20) {
                            addMoreNewData(mRecommendModel.NewItems, true, newCount);
                            if (ListUtils.isEmpty(firstPassItems)) {
                                setNoMore();
                            } else {
                                if (isLoadedPassItem) {
                                    addOlderData(firstPassItems, false);
                                } else {
                                    addItemTitle(mRecommendModel, ShopItemListModel.Show_Older_Title);
                                    addItemSortMenus(false);
                                    isLoadedPassItem = true;
                                    addOlderData(firstPassItems, false);
                                }
                                displayMode = 2;
                                displaystatuid = 1;
                                PAGE_INDEX = 1;
                            }

                        } else {
                            addMoreNewData(mRecommendModel.NewItems, false, newCount);
                        }
                        newCount = newCount + mRecommendModel.NewItems.size();
                    }
                } else if (displayMode == 2) {
                    if (ListUtils.isEmpty(mRecommendModel.PassItems)) {
                        setNoMore();
                    } else {
                        if (isLoadedPassItem) {
                            addMoreOlderData(mRecommendModel.PassItems, false, oldeCount);
                        } else {
                            addItemTitle(mRecommendModel, ShopItemListModel.Show_Older_Title);
                            addItemSortMenus(false);
                            addMoreOlderData(mRecommendModel.PassItems, false, oldeCount);
                        }
                        oldeCount = oldeCount + mRecommendModel.PassItems.size();
                    }
                }

//                if (displayMode == 1 || displayMode == 0) {
//                    addMoreNewData(mRecommendModel.NewItems, false, 0);
//                    if (mRecommendModel.NewItems.size() < 20) {
//                        //新款加载完成去加载旧款
//                        setNoMore();
//                    }
//                } else if (displayMode == 2) {
//                    addMoreOlderData(mRecommendModel.PassItems, false, 0);
//                    if (mRecommendModel.PassItems.size() < 20) {
//                        //新款加载完成去加载旧款
//                        setNoMore();
//                    }
//                }


//                if ((displayMode == 1) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    if (mRecommendModel.NewItems.size() < 20) {
//                        //新款加载完成去加载旧款
//                        addMoreNewData(mRecommendModel.NewItems, true, newCount);
//                        // mNewItemAdapter.addDataToTail(mRecommendModel.NewItems);
//                        newCount = mRecommendModel.NewItems.size() + newCount;
//                        if (!ListUtils.isEmpty(firstPassItems)) {
//                            //加载第一页旧款
//                            Bookends1.passPosindex = newCount;
////                            mNewItemAdapter.setPassItemPosition1();
////                            mNewItemAdapter.addPassItem(firstPassItems);
//                            addItemTitle(mRecommendModel);
//                            addOlderData(firstPassItems, false);
//                            displayMode = 2;
//                            PAGE_INDEX = 1;
//                        } else {
//                            //新款加载完了没有旧款
//                            Bookends1.passPosindex = -100;
//                            mLoadMorePB.setVisibility(View.GONE);
//                            mLoadMoreTxt.setText("没有更多款式了");
//                            //   load_more_no_data_txt.setVisibility(View.VISIBLE);
//                            //mRefreshLayout.setLoadMoreEnable(false);
//                            recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
//
//                        }
//
//                        //loadData(false, false);
//                    } else {
//                        //加载新款
//                        addMoreNewData(mRecommendModel.NewItems, false, newCount);
//                        newCount = mRecommendModel.NewItems.size() + newCount;
//                        //mNewItemAdapter.addDataToTail(mRecommendModel.NewItems);
//                        displayMode = 1;
//                        //loadData(false, false);
//                    }
//                } else if ((displayMode == 1) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    //新款没了加载第一页旧款
//                    if (!ListUtils.isEmpty(firstPassItems)) {
//                        Bookends1.passPosindex = newCount;
////                        mNewItemAdapter.setPassItemPosition1();
////                        mNewItemAdapter.addPassItem(firstPassItems);
//                        addItemTitle(mRecommendModel);
//                        addOlderData(firstPassItems, false);
//                        displayMode = 2;
//                        PAGE_INDEX = 1;
//                        //loadData(false, false);
//                    } else {
//                        //加载完了
//                        Bookends1.passPosindex = -100;
//                        mLoadMorePB.setVisibility(View.GONE);
//                        mLoadMoreTxt.setText("没有更多款式了");
//                        //  load_more_no_data_txt.setVisibility(View.VISIBLE);
//                        //mRefreshLayout.setLoadMoreEnable(false);
//                        recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
//
//                    }
//                }
//                //继续加载旧款
//                else if (displayMode == 2) {
//                    if (!ListUtils.isEmpty(mRecommendModel.PassItems)) {
//                        addMoreOlderData(mRecommendModel.PassItems, false, oldeCount);
//                        oldeCount = +mRecommendModel.PassItems.size();
//                        // mNewItemAdapter.addPassItem(mRecommendModel.PassItems);
//                        displayMode = 2;
//                        //loadData(false, false);
//                    } else {
//                        //加载完了
//                        mLoadMorePB.setVisibility(View.GONE);
//                        mLoadMoreTxt.setText("没有更多款式了");
//                        //  load_more_no_data_txt.setVisibility(View.VISIBLE);
//                        // mRefreshLayout.setLoadMoreEnable(false);
//                        recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
//
//                    }
//                } else if (displayMode == 0) {
//                    //没有新款也没有旧款
//                    mLoadMorePB.setVisibility(View.GONE);
//                    mLoadMoreTxt.setText("没有更多款式了");
//                    // load_more_no_data_txt.setVisibility(View.VISIBLE);
//                    //mRefreshLayout.setLoadMoreEnable(false);
//                    recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
//                }
            }
            if (mNewItemAdapter != null) {
                mNewItemAdapter.setmData(mData);
                mNewItemAdapter.notifyDataSetChanged();
                //   mAdapter.notifyDataSetChanged();
            }
            // mNewItemBookends.notifyDataSetChanged();
//        if (mLoadingDialog.isShowing()) {
//            mLoadingDialog.stop();
//            mRefreshLayout.scrollTo(0, 0);
//        }
//        if (mRecommendModel.getNextAcvivity() != null &&
//                mRecommendModel.getNextAcvivity().getID() > 0) {
//            mNextActivitysView.setVisibility(View.VISIBLE);
//            long startMillis = TimeUtils.timeStampToMillis(mRecommendModel.getNextAcvivity().getStartTime());
//            Date startDate = TimeUtils.timeStampToDate(mRecommendModel.getNextAcvivity().getStartTime(), "yyyy-MM-dd HH:mm:ss");
//            Calendar startCal = Calendar.getInstance();
//            startCal.setTime(startDate);
//            int startYear = startCal.get(Calendar.YEAR);
//            int startMonth = startCal.get(Calendar.MONTH);
//            int startDay = startCal.get(Calendar.DATE);
//            int startHour = startCal.get(Calendar.HOUR);
//
//            Calendar cal = Calendar.getInstance();
//            int nowYear = cal.get(Calendar.YEAR);
//            int nowMonth = cal.get(Calendar.MONTH);
//            int nowDay = cal.get(Calendar.DATE);
//            int nowHour = cal.get(Calendar.HOUR);
//
//            if (startYear == nowYear && startMonth == nowMonth) {
//                if (startDay > nowDay) {
//                    if (startDay - nowDay < 1) {//今天
//                        mTvNextActivity.setText("下一场今天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "后开拼");
//                        return;
//                    } else if (startDay - nowDay < 2) {//明天
//                        mTvNextActivity.setText("下一场明天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "开拼");
//                        return;
//                    }
//                }
//            }
//            mTvNextActivity.setText("下一场" + TimeUtils.millisToTimestamp(startMillis, "MM月dd日HH点") + "开拼");
//        } else {
//            mNextActivitysView.setVisibility(View.GONE);
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setNoMore() {
        if (isHas_NewAndPass) {
            look_old_data.setText("浏览" + Part2Title);
            look_new_data.setText("浏览" + Part1Title);
            if (!isCheckPassTitle) {
                look_old_data.setVisibility(View.GONE);
                look_new_data.setVisibility(View.GONE);
            } else {
                look_old_data.setVisibility(View.GONE);
                look_new_data.setVisibility(View.VISIBLE);
            }
        } else {
            look_old_data.setVisibility(View.GONE);
            look_new_data.setVisibility(View.GONE);
        }
        mLoadMorePB.setVisibility(View.GONE);
        mLoadMoreTxt.setText(R.string.loading_no_more);
        recyclerViewLoadMoreUtil.setPullUpRefreshEnable(false);
    }

    /**
     * 刷新加载新款数据
     */
    private void addNewData(List<ShopItemListModel> NewItems, boolean isAddEmpty) {
        //isAddEmpty是否判断加空格数据
        if (!ListUtils.isEmpty(NewItems)) {
            for (int i = 0; i < NewItems.size(); i++) {
                ShopItemListModel model = NewItems.get(i);
                model.setItem_layout_type(ShopItemListModel.TYPE_NEW_OLDER_ITEM);
                if (i % 2 == 0) {
                    model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_LEFT);
                } else {
                    model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_RIGHT);
                }
                mData.add(model);
            }
        }
        if (NewItems.size() % 2 != 0 && isAddEmpty) {
            ShopItemListModel model = new ShopItemListModel();
            model.setID(-1);
            mData.add(model);
        }
    }

    /**
     * 加载更多新款数据
     */
    private void addMoreNewData(List<ShopItemListModel> NewItems, boolean isAddEmpty, int newCount) {
        //isAddEmpty是否判断加空格数据
        if (!ListUtils.isEmpty(NewItems)) {
            for (int i = 0; i < NewItems.size(); i++) {
                ShopItemListModel model = NewItems.get(i);
                model.setItem_layout_type(ShopItemListModel.TYPE_NEW_OLDER_ITEM);
                if (newCount % 2 == 0) {
                    if (i % 2 == 0) {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_LEFT);
                    } else {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_RIGHT);
                    }
                } else {
                    if (i % 2 == 0) {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_RIGHT);
                    } else {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_LEFT);
                    }
                }

                mData.add(model);
            }
        }
        if (isAddEmpty) {
            if (newCount % 2 == 0) {
                if (NewItems.size() % 2 != 0) {
                    ShopItemListModel model = new ShopItemListModel();
                    model.setID(-1);
                    mData.add(model);
                }
            } else {
                if (NewItems.size() % 2 == 0) {
                    ShopItemListModel model = new ShopItemListModel();
                    model.setID(-1);
                    mData.add(model);
                }
            }
        }

    }

    /**
     * 刷新加载旧款数据
     */
    private void addOlderData(List<ShopItemListModel> OdlerItems, boolean isAddEmpty) {
        //isAddEmpty是否判断加空格数据
        if (!ListUtils.isEmpty(OdlerItems)) {
            for (int i = 0; i < OdlerItems.size(); i++) {
                ShopItemListModel model = OdlerItems.get(i);
                model.isPassItem = true;
                model.setItem_layout_type(ShopItemListModel.TYPE_NEW_OLDER_ITEM);
                if (i % 2 == 0) {
                    model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_LEFT);
                } else {
                    model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_RIGHT);
                }
                mData.add(model);
            }
        }
        if (OdlerItems.size() % 2 != 0 && isAddEmpty) {
            ShopItemListModel model = new ShopItemListModel();
            model.setID(-1);
            mData.add(model);
        }
    }

    /**
     * 加载更多旧款数据
     */
    private void addMoreOlderData(List<ShopItemListModel> NewItems, boolean isAddEmpty, int newCount) {
        //isAddEmpty是否判断加空格数据
        if (!ListUtils.isEmpty(NewItems)) {
            for (int i = 0; i < NewItems.size(); i++) {
                ShopItemListModel model = NewItems.get(i);
                model.setItem_layout_type(ShopItemListModel.TYPE_NEW_OLDER_ITEM);
                if (newCount % 2 == 0) {
                    if (i % 2 == 0) {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_LEFT);
                    } else {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_RIGHT);
                    }
                } else {
                    if (i % 2 == 0) {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_RIGHT);
                    } else {
                        model.setItem_layout_direction(ShopItemListModel.LAYOUT_DIRECTION_LEFT);
                    }
                }

                mData.add(model);
            }
        }
        if (isAddEmpty) {
            if (newCount % 2 == 0) {
                if (NewItems.size() % 2 != 0) {
                    ShopItemListModel model = new ShopItemListModel();
                    model.setID(-1);
                    mData.add(model);
                }
            } else {
                if (NewItems.size() % 2 == 0) {
                    ShopItemListModel model = new ShopItemListModel();
                    model.setID(-1);
                    mData.add(model);
                }
            }
        }

    }

    /**
     * 加载标题
     */
    public void addItemTitle(RecommendModel mRecommendModel, int type) {
        if (mRecommendModel != null) {
            if (type == ShopItemListModel.Show_Older_Title) {
                ShopItemListModel margin = new ShopItemListModel();
                margin.setItem_layout_type(ShopItemListModel.TYPE_MARIN_VIEW);
                mData.add(margin);
            }
            String title2 = mRecommendModel.getPart2Title();
            String title1 = mRecommendModel.getPart1Title();
            ShopItemListModel model = new ShopItemListModel();
            model.setItem_layout_type(ShopItemListModel.TYPE_TITLE);
            model.setPart2title(title2);
            model.setPart1title(title1);
            model.setShowTitleType(type);
            mData.add(model);
        }
    }

    /**
     * 加载筛选
     */
    public void addItemSortMenus(boolean sortType_isNew) {
        ShopItemListModel model = new ShopItemListModel();
        model.setItem_layout_type(ShopItemListModel.TYPE_SORT_MENUS);
        model.setSortMenus(sortMenus);
        model.sortType_isNew = false;
        if (!ListUtils.isEmpty(sortMenus))
            mData.add(model);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        onLoadPinAndForecastFailed();
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        onLoadPinAndForecastFailed();
    }

//    public class Task extends AsyncTask<Void, Void, String> {
//        public Task() {
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingDialog.start("加载数据中,请稍后...");
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result.equals("ok")) {
//                dataLoaded();
//            } else {
//                ViewHub.showLongToast(mContext, result);
//            }
//            if (mLoadingDialog.isShowing()) {
//                mLoadingDialog.stop();
//            }
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            try {
//                String json = ShopSetAPI.getInstance().CommonSearchItem(searchKey, displayMode,
//                        PublicData.getCookie(mContext));
//                JSONObject jo = new JSONObject(json);
//                newitemdata = GsonHelper.jsonToObject(jo.get("NewItems").toString(),
//                        new TypeToken<List<SearchItemModel>>() {
//                        });
//                passitemdata = GsonHelper.jsonToObject(jo.get("PassItems").toString(),
//                        new TypeToken<List<SearchItemModel>>() {
//                        });
//                return "ok";
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                return "error:" + ex.getMessage();
//            }
//        }
//
//    }

    //筛选更多接口
    private void getSearchPanel() {
        try {
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("ID", rid);
            jsonobject.put("Keyword", searchKey);
            jsonobject.put("Values", valueIDS);
            //QuickSaleApi.getSearchPanel(this, mRequestHelper, this, IDsConstant.AREAID_SEARCH + "", jsonobject.toString());
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                    .getSearchPanel(IDsConstant.AREAID_SEARCH, jsonobject.toString(), displaystatuid)
                    .compose(RxUtil.<PinHuoResponse<SearchPanelBean>>rxSchedulerHelper())
                    .compose(RxUtil.<SearchPanelBean>handleResult())
                    .subscribeWith(new CommonSubscriber<SearchPanelBean>(mContext) {
                        @Override
                        public void onNext(SearchPanelBean searchPanelBean) {
                            super.onNext(searchPanelBean);
                            gotoSerchPabel(searchPanelBean);
                        }
                    }));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
