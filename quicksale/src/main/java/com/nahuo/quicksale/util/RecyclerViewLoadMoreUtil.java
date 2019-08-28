package com.nahuo.quicksale.util;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.nahuo.quicksale.adapter.PinHuoShowAdapter;
import com.nahuo.quicksale.customview.RGridLayoutManager;

/**
 * Created by jame on 2018/1/30.
 */

public class RecyclerViewLoadMoreUtil {
    protected Context mContext;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RefreshDataListener mRefreshDataListener;
    private boolean mIsRefresh = true;//是否禁止下拉刷新，默认是不禁止下拉刷新
    private boolean mIsLoadMore = true;//是否禁止上拉刷新，默认是不禁止上拉刷新
    private boolean isSlidingToScreenBottom = false;//用来标记是否正在向最后一个滑动，既是否向右滑动或向下滑动

    public RecyclerViewLoadMoreUtil() {

    }

    public void init(final Context context, final ImageView mVScrollToTop, final SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, final RecyclerView.Adapter adapter, final RefreshDataListener refreshDataListener) {
        initConfig(context, swipeRefreshLayout, recyclerView, adapter, refreshDataListener);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                boolean isRefreshing = swipeRefreshLayout.isRefreshing();
                if (isRefreshing) {
                    refreshDataListener.onRefresh();
                }
            }
        });
        final RGridLayoutManager gridLayoutManager = new RGridLayoutManager(mContext,
                2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int type = mAdapter.getItemViewType(position);
                if (type == PinHuoShowAdapter.Type.TYPE_SHOP.ordinal()) {
                    return gridLayoutManager.getSpanCount();
                } else if (type == PinHuoShowAdapter.Type.TYPE_TITLE.ordinal()) {
                    return gridLayoutManager.getSpanCount();
                } else if (type == PinHuoShowAdapter.Type.TYPE_NEW_OLDER_ITEM.ordinal()) {
                    return 1;
                }else if (type>=PinHuoShowAdapter.BASE_ITEM_TYPE_HEADER||type==PinHuoShowAdapter.Type.TYPE_FOOT.ordinal()){
                    return gridLayoutManager.getSpanCount();
                }
                return gridLayoutManager.getSpanCount();
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                RGridLayoutManager manager = (RGridLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                boolean idleFlag = newState == RecyclerView.SCROLL_STATE_IDLE;
                if (idleFlag) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = mAdapter.getItemCount();

                    // 判断是否滚动到屏幕底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToScreenBottom) {
                        if (mScrolledBottomListener != null) {
                            mScrolledBottomListener.ScrolledBottom(recyclerView);
                        }

                        if (mIsLoadMore) {//可以加载更多
                            mSwipeRefreshLayout.setEnabled(false);//禁止使用下拉刷新
                            mRefreshDataListener.loadMore();
                            //Log.d("TAG", "howes right=" + manager.findLastCompletelyVisibleItemPosition());
//                                Toast.makeText(mContext, "加载更多", Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(mContext, "不能加载更多", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //   Log.d("TAG", "dy=" + dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //当列表项的总高度小于屏幕，即不会滚动列表时，dx、dy都等于0
                if (dy > 0) {
                    //大于0表示，正在向右滚动，向上滚动
                    isSlidingToScreenBottom = true;
                } else {
                    //小于等于0 表示停止或向左滚动，向下滚动
                    isSlidingToScreenBottom = false;
                }
                if (mScrollingListener != null) {
                    mScrollingListener.scroll(isSlidingToScreenBottom);
                }
                RGridLayoutManager manager = (RGridLayoutManager) recyclerView.getLayoutManager();
                int pos = manager.findFirstVisibleItemPosition();
                if (mVScrollToTop != null) {
                    if (pos > 5 && mVScrollToTop.getVisibility() != View.VISIBLE) {
                        mVScrollToTop.setVisibility(View.VISIBLE);
                    } else if (pos <= 5 && mVScrollToTop.getVisibility() != View.GONE) {
                        mVScrollToTop.setVisibility(View.GONE);
                    }
                }
            }
        });
        if (mVScrollToTop!=null)
        mVScrollToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecyclerView!=null)
                    mRecyclerView.scrollToPosition(0);
            }
        });
    }

    private void initConfig(Context context, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, RecyclerView.Adapter adapter, RefreshDataListener refreshDataListener) {
        this.mContext = context;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        this.mRecyclerView = recyclerView;
        this.mAdapter = adapter;
        this.mRefreshDataListener = refreshDataListener;
    }

    /**
     * 首次进入页面可以自动加载，要使用必须在{@link #init(Context, SwipeRefreshLayout, RecyclerView, RecyclerView.Adapter, RefreshDataListener)}
     * 之后调用
     */
    public void autoRefreshing() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mRefreshDataListener.onRefresh();
            }
        });
    }

    /**
     * 要使用必须在{@link #init(Context, SwipeRefreshLayout, RecyclerView, RecyclerView.Adapter, RefreshDataListener)}
     * 之后调用
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(@ColorRes int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);
    }

    public void endRefreshing() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void endLoading() {
        if (mIsRefresh) {//如果之前已经设置过禁止下拉刷新，此时不应该在设置成可下拉刷新
            setPullDownRefreshEnable(true);//可以使用下拉刷新
        } else {//如果之前已经设置过禁止下拉刷新，此时不应该在设置成可下拉刷新
            setPullDownRefreshEnable(false);//不可以使用下拉刷新
        }
    }

    /**
     * 设置列表是否禁止下拉刷新，是否显示下拉刷新动画只能通过该方法设置
     *
     * @param isRefresh true 可以使用下拉刷新；false 不可以使用下拉刷新
     */
    public void setPullDownRefreshEnable(boolean isRefresh) {
        mIsRefresh = isRefresh;
        mSwipeRefreshLayout.setEnabled(isRefresh);
    }

    /**
     * 设置列表是否禁止上拉刷新
     *
     * @param isLoadMore true 可以使用上拉刷新；false 不可以使用上拉刷新
     */
    public void setPullUpRefreshEnable(boolean isLoadMore) {
        mIsLoadMore = isLoadMore;
    }

    public interface RefreshDataListener {
        void onRefresh();

        boolean loadMore();
    }

    private ScrollingListener mScrollingListener;

    public interface ScrollingListener {
        /**
         * true 向上滚动；false 向下滚动
         *
         * @param scrollState
         */
        void scroll(boolean scrollState);
    }

    public void setScrollingListener(ScrollingListener listener) {
        mScrollingListener = listener;
    }

    private ScrolledBottomListener mScrolledBottomListener;

    public interface ScrolledBottomListener {
        /**
         * 列表滚动到屏幕底部时调用
         *
         * @param recyclerView
         */
        void ScrolledBottom(RecyclerView recyclerView);
    }

    public void setScrollingListener(ScrolledBottomListener listener) {
        mScrolledBottomListener = listener;
    }

}
