package com.nahuo.library.controls.pulltorefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nahuo.library.R;
import com.nahuo.library.event.OnListViewItemClickListener;

/**
 * ListView下拉刷新和加载更多
 * <p/>
 * <p/>
 * <strong>变更说明:</strong>
 * <p/>
 * 默认如果设置了OnRefreshListener接口和OnLoadMoreListener接口，<br>
 * 并且不为null，则打开这两个功能了。
 * <p/>
 * 剩余三个Flag： <br>
 * mIsAutoLoadMore(是否自动加载更多) <br>
 * mIsMoveToFirstItemAfterRefresh(下拉刷新后是否显示第一条Item) <br>
 * mIsDoRefreshOnWindowFocused(当该ListView所在的控件显示到屏幕上时，是否直接显示正在刷新...)
 * <p/>
 * <p/>
 * <strong>有改进意见，请发送到俺的邮箱哈~ 多谢各位小伙伴了！^_^</strong>
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

    /**
     * 显示格式化日期模板
     */
    private final static String DATE_FORMAT_STR = "yyyy年MM月dd日 HH:mm";

    /**
     * 实际的padding的距离与界面上偏移距离的比例
     */
    private final static int RATIO = 3;
    // ===========================以下4个常量为
    // 下拉刷新的状态标识===============================
    /**
     * 松开刷新
     */
    private final static int RELEASE_TO_REFRESH = 0;
    /**
     * 下拉刷新
     */
    private final static int PULL_TO_REFRESH = 1;
    /**
     * 正在刷新
     */
    private final static int REFRESHING = 2;
    /**
     * 刷新完成 or 什么都没做，恢复原状态。
     */
    private final static int DONE = 3;
    // ===========================以下3个常量为
    // 加载更多的状态标识===============================
    /**
     * 加载中
     */
    private final static int ENDINT_LOADING = 1;
    /**
     * 手动完成刷新
     */
    private final static int ENDINT_MANUAL_LOAD_DONE = 2;
    /**
     * 自动完成刷新
     */
    private final static int ENDINT_AUTO_LOAD_DONE = 3;

    /**
     * <strong>下拉刷新HeadView的实时状态flag</strong>
     * <p/>
     * <p/>
     * 0 : RELEASE_TO_REFRESH;
     * <p/>
     * 1 : PULL_To_REFRESH;
     * <p/>
     * 2 : REFRESHING;
     * <p/>
     * 3 : DONE;
     */
    private int mHeadState;
    /**
     * <strong>加载更多FootView（EndView）的实时状态flag</strong>
     * <p/>
     * <p/>
     * 0 : 完成/等待刷新 ;
     * <p/>
     * 1 : 加载中
     */
    private int mEndState;

    // ================================= 功能设置Flag
    // ================================

    /**
     * 可以加载更多？
     */
    private boolean mCanLoadMore = true;
    /**
     * 可以下拉刷新？
     */
    private boolean mCanRefresh = true;
    /**
     * 可以自动加载更多吗？（注意，先判断是否有加载更多，如果没有，这个flag也没有意义）
     */
    private boolean mIsAutoLoadMore = true;
    /**
     * 下拉刷新后是否显示第一条Item
     */
    private boolean mIsMoveToFirstItemAfterRefresh = false;
    /**
     * 当该ListView所在的控件显示到屏幕上时，是否直接显示正在刷新...
     */
    private boolean mIsDoRefreshOnUIChanged = false;

    public boolean isRefreshing() {
        return mHeadState == DONE ? false : true;
    }

    public boolean isCanLoadMore() {
        return mCanLoadMore;
    }

    public void setCanLoadMore(boolean pCanLoadMore) {
        mCanLoadMore = pCanLoadMore;
        // if (mCanLoadMore && getFooterViewsCount() == 0) {
        // addFooterView();
        // }
    }

    public boolean isCanRefresh() {
        return mCanRefresh;
    }

    public void setCanRefresh(boolean pCanRefresh) {
        mCanRefresh = pCanRefresh;
    }

    public boolean isAutoLoadMore() {
        return mIsAutoLoadMore;
    }

    public void setAutoLoadMore(boolean pIsAutoLoadMore) {
        mIsAutoLoadMore = pIsAutoLoadMore;
    }

    public boolean isMoveToFirstItemAfterRefresh() {
        return mIsMoveToFirstItemAfterRefresh;
    }

    public void setMoveToFirstItemAfterRefresh(boolean pIsMoveToFirstItemAfterRefresh) {
        mIsMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
    }

    public boolean isDoRefreshOnUIChanged() {
        return mIsDoRefreshOnUIChanged;
    }

    public void setDoRefreshOnUIChanged(boolean pIsDoRefreshOnWindowFocused) {
        mIsDoRefreshOnUIChanged = pIsDoRefreshOnWindowFocused;
    }

    // =====用于滑动删除===================================================
    private Boolean mIsHorizontal;

    private View mPreItemView;

    private View mCurrentItemView;

    private float mFirstX;

    private float mFirstY;

    private int mRightViewWidth;

    // private boolean mIsInAnimation = false;
    private final int mDuration = 300;

    private final int mDurationStep = 10;

    private boolean mIsShown;
    // 是否允许左右滑动
    private boolean mCanSwipe = false;

    private boolean isCanSwipe() {
        return mCanSwipe;
    }

    public View getCurrentItemView() {
        return mCurrentItemView;
    }

    /**
     * 设置是否支持滑动
     */
    private void setCanSwipe(boolean isCanSwipe) {
        mCanSwipe = isCanSwipe;
    }

    // ============================================================================

    private LayoutInflater mInflater;

    private LinearLayout mHeadRootView;
//    private TextView           mTipsTextView;
//    private TextView           mLastUpdatedTextView;
//    private ImageView          mArrowImageView;
//    private ProgressBar        mProgressBar;

    private View mEndRootView;
    private ProgressBar mEndLoadProgressBar;
    private TextView mEndLoadTipsTextView;

    /** headView动画 */
//    private RotateAnimation    mArrowAnim;
    /** headView反转动画 */
//    private RotateAnimation    mArrowReverseAnim;

    /**
     * 用于保证startY的值在一个完整的touch事件中只被记录一次
     */
    private boolean mIsRecored;

    private int mHeadViewWidth;
    private int mHeadViewHeight;

    private int mStartY;
    private boolean mIsBack;

    public int mFirstItemIndex;
    public int mLastItemIndex;
    private int mCount;
    private boolean mEnoughCount;        // 足够数量充满屏幕？

    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

    private String mLabel;

    private TextView mTvText;
    private ImageView mIvProgress;
    private CircleLoadingDrawable mCircumlarProgressDrawable;

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String pLabel) {
        mLabel = pLabel;
    }

    public PullToRefreshListView(Context pContext) {
        super(pContext);
        init(pContext, null);
    }

    public PullToRefreshListView(Context pContext, AttributeSet pAttrs) {
        super(pContext, pAttrs);
        init(pContext, pAttrs);
    }

    public PullToRefreshListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
        super(pContext, pAttrs, pDefStyle);
        init(pContext, pAttrs);
    }

    /**
     * 初始化操作
     *
     * @param pContext
     */
    private void init(Context pContext, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray mTypedArray = pContext.obtainStyledAttributes(attrs, R.styleable.swipelistviewstyle);
            if (mTypedArray != null) {
                // 获取自定义属性和默认值
                mRightViewWidth = (int) mTypedArray.getDimension(R.styleable.swipelistviewstyle_right_width, 200);
                mCanSwipe = mTypedArray.getBoolean(R.styleable.swipelistviewstyle_isCanSwipe, false);
                mTypedArray.recycle();
            }
        }

        setCacheColorHint(Color.TRANSPARENT);
        setOnLongClickListener(null);
        mInflater = LayoutInflater.from(pContext);

        addHeadView(pContext);

        setOnScrollListener(this);

        initPullImageAnimation(0);

    }

    /**
     * 添加下拉刷新的HeadView
     */
    private void addHeadView(Context context) {
        mHeadRootView = (LinearLayout) mInflater.inflate(R.layout.pull_to_refresh_circle, null);
        mTvText = (TextView) mHeadRootView.findViewById(R.id.tv_text);
        mIvProgress = (ImageView) mHeadRootView.findViewById(R.id.iv_progress);
        mCircumlarProgressDrawable = new CircleLoadingDrawable(getContext());
        mIvProgress.setImageDrawable(mCircumlarProgressDrawable);

//        mArrowImageView = (ImageView)mHeadRootView.findViewById(R.id.head_arrowImageView);
//        mArrowImageView.setMinimumWidth(70);
//        mArrowImageView.setMinimumHeight(50);
//        mProgressBar = (ProgressBar)mHeadRootView.findViewById(R.id.head_progressBar);
//        mTipsTextView = (TextView)mHeadRootView.findViewById(R.id.head_tipsTextView);
//        mLastUpdatedTextView = (TextView)mHeadRootView.findViewById(R.id.head_lastUpdatedTextView);

        measureView(mHeadRootView);
        mHeadViewHeight = mHeadRootView.getMeasuredHeight();
        mHeadViewWidth = mHeadRootView.getMeasuredWidth();

        mHeadRootView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
        mHeadRootView.invalidate();

        addHeaderView(mHeadRootView, null, false);

        mHeadState = DONE;
        changeHeadViewByState();
    }

    public View getHeaderView() {
        return mHeadRootView;
    }

    /**
     * 添加加载更多FootView
     */
    private void addFooterView(Context context) {

        mEndRootView = mInflater.inflate(R.layout.pull_to_refresh_load_more, null);
        mEndRootView.setVisibility(View.VISIBLE);
        mEndLoadProgressBar = (ProgressBar) mEndRootView.findViewById(R.id.pull_to_refresh_footer_progress);
        mEndLoadTipsTextView = (TextView) mEndRootView.findViewById(R.id.load_more);
        mEndRootView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCanLoadMore) {
                    if (mCanRefresh) {
                        // 当可以下拉刷新时，如果FootView没有正在加载，并且HeadView没有正在刷新，才可以点击加载更多。
                        if (mEndState != ENDINT_LOADING && mHeadState != REFRESHING) {
                            mEndState = ENDINT_LOADING;
                            onLoadMore();
                        }
                    } else if (mEndState != ENDINT_LOADING) {
                        // 当不能下拉刷新时，FootView不正在加载时，才可以点击加载更多。
                        mEndState = ENDINT_LOADING;
                        onLoadMore();
                    }
                }
            }
        });

        addFooterView(mEndRootView);

        if (mIsAutoLoadMore) {
            mEndState = ENDINT_AUTO_LOAD_DONE;
        } else {
            mEndState = ENDINT_MANUAL_LOAD_DONE;
        }
    }

    /**
     * 实例化下拉刷新的箭头的动画效果
     *
     * @param pAnimDuration 动画运行时长
     * @date 2013-11-20 上午11:53:22
     * @change JohnWatson
     * @version 1.0
     */
    private void initPullImageAnimation(final int pAnimDuration) {

        int _Duration;

        if (pAnimDuration > 0) {
            _Duration = pAnimDuration;
        } else {
            _Duration = 250;
        }
        // Interpolator _Interpolator;
        // switch (pAnimType) {
        // case 0:
        // _Interpolator = new AccelerateDecelerateInterpolator();
        // break;
        // case 1:
        // _Interpolator = new AccelerateInterpolator();
        // break;
        // case 2:
        // _Interpolator = new AnticipateInterpolator();
        // break;
        // case 3:
        // _Interpolator = new AnticipateOvershootInterpolator();
        // break;
        // case 4:
        // _Interpolator = new BounceInterpolator();
        // break;
        // case 5:
        // _Interpolator = new CycleInterpolator(1f);
        // break;
        // case 6:
        // _Interpolator = new DecelerateInterpolator();
        // break;
        // case 7:
        // _Interpolator = new OvershootInterpolator();
        // break;
        // default:
        // _Interpolator = new LinearInterpolator();
        // break;
        // }

//        Interpolator _Interpolator = new LinearInterpolator();

//        mArrowAnim = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        mArrowAnim.setInterpolator(_Interpolator);
//        mArrowAnim.setDuration(_Duration);
//        mArrowAnim.setFillAfter(true);
//
//        mArrowReverseAnim = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//        mArrowReverseAnim.setInterpolator(_Interpolator);
//        mArrowReverseAnim.setDuration(_Duration);
//        mArrowReverseAnim.setFillAfter(true);
    }

    /**
     * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
     *
     * @param pChild
     * @date 2013-11-20 下午4:12:07
     * @change JohnWatson
     * @version 1.0
     */
    private void measureView(View pChild) {
        ViewGroup.LayoutParams p = pChild.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;

        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        pChild.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 为了判断滑动到ListView底部没
     */
    @Override
    public void onScroll(AbsListView pView, int pFirstVisibleItem, int pVisibleItemCount, int pTotalItemCount) {
        if (mOnScrollListener != null)
            mOnScrollListener.onScroll(pView, pFirstVisibleItem, pVisibleItemCount, pTotalItemCount);
        // System.out.println("onScroll . pFirstVisibleItem = "+pFirstVisibleItem);
        mFirstItemIndex = pFirstVisibleItem;
        mLastItemIndex = pFirstVisibleItem + pVisibleItemCount - 1;
        mCount = pTotalItemCount - 1;
        if (pTotalItemCount > pVisibleItemCount) {
            mEnoughCount = true;
        } else {
            mEnoughCount = false;
        }

    }

    /**
     * 这个方法，可能有点乱，大家多读几遍就明白了。
     */
    @Override
    public void onScrollStateChanged(AbsListView pView, int pScrollState) {
        if (mOnScrollListener != null)
            mOnScrollListener.onScrollStateChanged(pView, pScrollState);
        if (mCanLoadMore) {// 存在加载更多功能

            if (getFooterViewsCount() == 0 && mEnoughCount) {
                addFooterView(pView.getContext());
            }

            // 数据铺满屏幕时才有加载更多
            if (mLastItemIndex == mCount && mEnoughCount == true && pScrollState == SCROLL_STATE_IDLE) {
                // SCROLL_STATE_IDLE=0，滑动停止
                if (mEndState != ENDINT_LOADING) {
                    if (mIsAutoLoadMore) {// 自动加载更多，我们让FootView显示 “更 多”
                        if (mCanRefresh) {
                            // 存在下拉刷新并且HeadView没有正在刷新时，FootView可以自动加载更多。
                            if (mHeadState != REFRESHING) {
                                // FootView显示 : 更 多 ---> 加载中...
                                mEndState = ENDINT_LOADING;
                                onLoadMore();
                                changeEndViewByState();
                            }
                        } else {// 没有下拉刷新，我们直接进行加载更多。
                            // FootView显示 : 更 多 ---> 加载中...
                            mEndState = ENDINT_LOADING;
                            onLoadMore();
                            changeEndViewByState();
                        }
                    } else {// 不是自动加载更多，我们让FootView显示 “点击加载”
                        // FootView显示 : 点击加载 ---> 加载中...
                        mEndState = ENDINT_MANUAL_LOAD_DONE;
                        changeEndViewByState();
                    }
                }
            }
        } else if (mEndRootView != null && mEndRootView.getVisibility() == VISIBLE) {
            // 突然关闭加载更多功能之后，我们要移除FootView。
            System.out.println("this.removeFooterView(endRootView);...");
            mEndRootView.setVisibility(View.GONE);
            this.removeFooterView(mEndRootView);
        }
    }

    /**
     * 改变加载更多状态
     *
     * @date 2013-11-11 下午10:05:27
     * @change JohnWatson
     * @version 1.0
     */
    private void changeEndViewByState() {
        if (mCanLoadMore) {
            // 允许加载更多
            switch (mEndState) {
                case ENDINT_LOADING:// 刷新中

                    // 加载中...
                    String endTips = mEndLoadTipsTextView.getText().toString();
                    String loading = getContext().getString(R.string.pull_to_refresh_footer_refreshing_label);
                    if (endTips.equals(loading)) {
                        break;
                    }
                    mEndLoadTipsTextView.setText(R.string.pull_to_refresh_footer_refreshing_label);
                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
                    mEndLoadProgressBar.setVisibility(View.VISIBLE);
//
//                    mEndRootView.setRefreshing(true);

                    break;
                case ENDINT_MANUAL_LOAD_DONE:// 手动刷新完成

                    // 点击加载
                    mEndLoadTipsTextView.setText(R.string.pull_to_refresh_footer_pull_label);
                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
                    mEndLoadProgressBar.setVisibility(View.GONE);
//                    mEndRootView.setRefreshing(false);

                    mEndRootView.setVisibility(View.VISIBLE);
                    break;
                case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成
                    // 更 多
                    mEndLoadTipsTextView.setText(R.string.pull_to_refresh_footer_pull_label);
                    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
                    mEndLoadProgressBar.setVisibility(View.GONE);
//                    mEndRootView.setRefreshing(false);

                    mEndRootView.setVisibility(View.VISIBLE);
                    break;
                default:
                    // 原来的代码是为了： 当所有item的高度小于ListView本身的高度时，
                    // 要隐藏掉FootView，大家自己去原作者的代码参考。

                    // if (mEnoughCount) {
                    // mEndRootView.setVisibility(View.VISIBLE);
                    // } else {
                    // mEndRootView.setVisibility(View.GONE);
                    // }
                    break;
            }
        }
    }

    /**
     * *****五星注意事项： 此方法不适用于ViewPager中，因为viewpager默认实例化相邻的item的View 建议： 不嵌套的时候，可以放在这个方法里使用，效果就是：进入界面直接刷新。具体刷新的控制条件，你自己决定。
     * 方法为：直接调用pull2RefreshManually();
     */
    @Override
    public void onWindowFocusChanged(boolean pHasWindowFocus) {
        super.onWindowFocusChanged(pHasWindowFocus);

        if (mIsDoRefreshOnUIChanged) {
            if (pHasWindowFocus) {
                pull2RefreshManually();
            }
        }
    }

    /**
     * 当该ListView所在的控件显示到屏幕上时，直接显示正在刷新...
     */
    public void pull2RefreshManually() {
        mHeadState = REFRESHING;
        changeHeadViewByState();
        onRefresh();
        setSelection(0);
        mIsRecored = false;
        mIsBack = false;
    }

    /**
     * 原作者的，我没改动，请读者自行优化。
     */
    public boolean onTouchEvent(MotionEvent event) {

        if (mCanRefresh) {
            if (mCanLoadMore && mEndState == ENDINT_LOADING) {
                // 如果存在加载更多功能，并且当前正在加载中，默认不允许下拉刷新，必须加载完毕后下拉刷新才能使用。
                return super.onTouchEvent(event);
            }

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (mListener != null) {
                        mListener.OnItemDown(event.getX(), event.getY());
                    }

                    if (mFirstItemIndex == 0 && !mIsRecored) {
                        mIsRecored = true;
                        mStartY = (int) event.getY();
                        // MyLogger.showLogWithLineNum(5,
                        // "mFirstItemIndex == 0 && !mIsRecored mStartY = "+mStartY);
                    } else if (mFirstItemIndex == 0 && mIsRecored) {
                        // 说明上次的Touch事件只执行了Down动作，然后直接被父类拦截了。
                        // 那么就要重新给mStartY赋值啦。
                        // MyLogger.showLogWithLineNum(5,
                        // "mFirstItemIndex = "+mFirstItemIndex+"__!mIsRecored = "+!mIsRecored);
                        mStartY = (int) event.getY();
                    }

                    break;

                case MotionEvent.ACTION_UP:

                    if (mListener != null) {
                        mListener.OnItemUp(event.getX(), event.getY());
                    }

                    if (mHeadState != REFRESHING) {

                        if (mHeadState == DONE) {

                        }
                        if (mHeadState == PULL_TO_REFRESH) {
                            // 在松手的时候，如果HeadView显示下拉刷新，那就恢复原状态。
                            mHeadState = DONE;
                            changeHeadViewByState();
                        }
                        if (mHeadState == RELEASE_TO_REFRESH) {
                            // 在松手的时候，如果HeadView显示松开刷新，那就显示正在刷新。
                            mHeadState = REFRESHING;
                            changeHeadViewByState();
                            onRefresh();
                        }
                    }

                    mIsRecored = false;
                    mIsBack = false;

                    break;

                case MotionEvent.ACTION_MOVE:
                    int _TempY = (int) event.getY();
                    int delta = (_TempY - mStartY) / RATIO;
                    setProgress(-(float) delta / 100);
                    if (!mIsRecored && mFirstItemIndex == 0) {
                        mIsRecored = true;
                        mStartY = _TempY;
                        // MyLogger.showLogWithLineNum(4,
                        // "!mIsRecored && mFirstItemIndex == 0 and __mStartY = "+mStartY);
                    }

                    if (mHeadState != REFRESHING && mIsRecored) {
                        // 保证在设置padding的过程中，当前的位置一直是在head，
                        // 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
                        // 可以松手去刷新了
                        if (mHeadState == RELEASE_TO_REFRESH) {
                            setSelection(0);
                            // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                            if (((_TempY - mStartY) / RATIO < mHeadViewHeight) && (_TempY - mStartY) > 0) {
                                mHeadState = PULL_TO_REFRESH;
                                changeHeadViewByState();
                            }
                            // 一下子推到顶了
                            else if (_TempY - mStartY <= 0) {
                                mHeadState = DONE;
                                changeHeadViewByState();
                            }
                            // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (mHeadState == PULL_TO_REFRESH) {
                            setSelection(0);
                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((_TempY - mStartY) / RATIO >= mHeadViewHeight) {
                                mHeadState = RELEASE_TO_REFRESH;
                                mIsBack = true;
                                changeHeadViewByState();
                            } else if (_TempY - mStartY <= 0) {
                                // System.out.println("mHeadState == PULL_TO_REFRESH _TempY = "+_TempY+"__mStartY = "+mStartY);
                                mHeadState = DONE;
                                changeHeadViewByState();
                            }
                        }

                        if (mHeadState == DONE) {
                            if (_TempY - mStartY > 0) {
                                // System.out.println("mHeadState == DONE ... _TempY - mStartY = "+(_TempY
                                // - mStartY));
                                mHeadState = PULL_TO_REFRESH;
                                changeHeadViewByState();
                            }
                        }

                        if (mHeadState == PULL_TO_REFRESH) {
                            mHeadRootView.setPadding(0, -1 * mHeadViewHeight + (_TempY - mStartY) / RATIO, 0, 0);

                        }

                        if (mHeadState == RELEASE_TO_REFRESH) {
                            mHeadRootView.setPadding(0, (_TempY - mStartY) / RATIO - mHeadViewHeight, 0, 0);
                        }
                    }
                    break;
            }
        }

        if (mCanSwipe) {
            float lastX = event.getX();
            float lastY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("---->ACTION_DOWN");

                    if (mIsShown && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                        System.out.println("1---> hiddenRight");
                        /**
                         * 情况一：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                         */
                        hiddenRight(mPreItemView);
                    }

                    mFirstX = lastX;
                    mFirstY = lastY;
                    int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);

                    if (motionPosition >= 0) {
                        View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
                        mPreItemView = mCurrentItemView;
                        mCurrentItemView = currentItemView;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    float dx = lastX - mFirstX;
                    float dy = lastY - mFirstY;

                    // confirm is scroll direction
                    if (mIsHorizontal == null) {
                        if (!judgeScrollDirection(dx, dy)) {
                            break;
                        }
                    }

                    if (mIsHorizontal) {
                        if (mPreItemView == null || mCurrentItemView == null)
                            return false;

                        if (mIsShown && mPreItemView != mCurrentItemView) {
                            System.out.println("2---> hiddenRight");
                            /**
                             * 情况二：
                             * <p>
                             * 一个Item的右边布局已经显示，
                             * <p>
                             * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
                             * <p>
                             * 向左滑动只触发该情况，向右滑动还会触发情况五
                             */
                            hiddenRight(mPreItemView);
                        }

                        if (mIsShown && mPreItemView == mCurrentItemView) {
                            dx = dx - mRightViewWidth;
                            System.out.println("======dx " + dx);
                        }

                        // can't move beyond boundary
                        if (dx < 0 && dx > -mRightViewWidth) {
                            mCurrentItemView.scrollTo((int) (-dx), 0);
                        }

                        clearPressedState();
                        return true;
                    } else {
                        if (mPreItemView == null || mCurrentItemView == null)
                            return false;
                        if (mIsShown) {
                            System.out.println("3---> hiddenRight");
                            /**
                             * 情况三：
                             * <p>
                             * 一个Item的右边布局已经显示，
                             * <p>
                             * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                             */
                            hiddenRight(mPreItemView);
                        }
                    }
                    clearPressedState();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    System.out.println("============ACTION_UP");
                    clearPressedState();
                    if (mPreItemView == null || mCurrentItemView == null)
                        return false;
                    if (mIsShown) {
                        System.out.println("4---> hiddenRight");
                        /**
                         * 情况四：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                         */
                        hiddenRight(mPreItemView);
                    }

                    if (mIsHorizontal != null && mIsHorizontal) {
                        if (mFirstX - lastX > mRightViewWidth / 2) {
                            showRight(mCurrentItemView);
                        } else {
                            System.out.println("5---> hiddenRight");
                            /**
                             * 情况五：
                             * <p>
                             * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                             */
                            hiddenRight(mCurrentItemView);
                        }

                        return true;
                    }

                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 单击事件监听器
     */
    private OnListViewItemClickListener mListener = null;

    private OnScrollListener mOnScrollListener;

    public void setOnListViewItemClickListener(OnListViewItemClickListener listener) {
        mListener = listener;
    }

    /**
     * 当HeadView状态改变时候，调用该方法，以更新界面
     */
    private void changeHeadViewByState() {
        switch (mHeadState) {
            case RELEASE_TO_REFRESH:
//                mArrowImageView.setVisibility(View.VISIBLE);
//                mProgressBar.setVisibility(View.GONE);
//                mTipsTextView.setVisibility(View.VISIBLE);
//                mLastUpdatedTextView.setVisibility(View.VISIBLE);
//
//                mArrowImageView.clearAnimation();
//                mArrowImageView.startAnimation(mArrowAnim);
//                // 松开刷新
//                mTipsTextView.setText(R.string.pull_to_refresh_release_label);
                break;
            case PULL_TO_REFRESH:
                setRefreshing(false);

//                mProgressBar.setVisibility(View.GONE);
//                mTipsTextView.setVisibility(View.VISIBLE);
//                mLastUpdatedTextView.setVisibility(View.VISIBLE);
//                mArrowImageView.clearAnimation();
//                mArrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (mIsBack) {
                    mIsBack = false;
//                    mArrowImageView.clearAnimation();
//                    mArrowImageView.startAnimation(mArrowReverseAnim);
//                    // 下拉刷新
//                    mTipsTextView.setText(R.string.pull_to_refresh_pull_label);
                } else {
                    // 下拉刷新
//                    mTipsTextView.setText(R.string.pull_to_refresh_pull_label);
                }
                break;

            case REFRESHING:

                changeHeaderViewRefreshState();
                break;
            case DONE:

                mHeadRootView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
//                setProgress(0);
                setRefreshing(false);

//                mProgressBar.setVisibility(View.GONE);
//                mArrowImageView.clearAnimation();
//                mArrowImageView.setImageResource(R.drawable.pulltorefresh_down_arrow);
//                // 下拉刷新
//                mTipsTextView.setText(R.string.pull_to_refresh_pull_label);
//                mLastUpdatedTextView.setVisibility(View.VISIBLE);

                break;
        }
    }

    /**
     * 改变HeadView在刷新状态下的显示
     */
    private void changeHeaderViewRefreshState() {
        mHeadRootView.setPadding(0, 0, 0, 0);

        // 华生的建议： 实际上这个的setPadding可以用动画来代替。我没有试，但是我见过。其实有的人也用Scroller可以实现这个效果，
        // 我没时间研究了，后期再扩展，这个工作交给小伙伴你们啦~ 如果改进了记得发到我邮箱噢~

//        mProgressBar.setVisibility(View.VISIBLE);
//        mArrowImageView.clearAnimation();
//        mArrowImageView.setVisibility(View.GONE);
//        // 正在刷新...
//        mTipsTextView.setText(R.string.pull_to_refresh_refreshing_label);
//        mLastUpdatedTextView.setVisibility(View.VISIBLE);

        setRefreshing(true);

    }

    /**
     * 下拉刷新监听接口
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    /**
     * 加载更多监听接口
     */
    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
        if (pRefreshListener != null) {
            mRefreshListener = pRefreshListener;
            mCanRefresh = true;
        }
    }

    public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
        if (pLoadMoreListener != null) {
            mLoadMoreListener = pLoadMoreListener;
            mCanLoadMore = true;
            // if (mCanLoadMore && getFooterViewsCount() == 0) {
            // addFooterView();
            // }
        }
    }

    /**
     * 正在下拉刷新
     */
    protected void onRefresh() {
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    /**
     * 下拉刷新完成
     */
    public void onRefreshComplete() {

        mHeadState = DONE;
        // 最近更新: Time
//        mLastUpdatedTextView.setText(getResources().getString(R.string.pull_to_refresh_lasttime)
//                + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
        changeHeadViewByState();

        // 下拉刷新后是否显示第一条Item
        if (mIsMoveToFirstItemAfterRefresh && getCount() > 0) {
            mFirstItemIndex = 0;
            setSelection(0);
        }

        if (mCanLoadMore && getFooterViewsCount() > 0) {
            this.removeFooterView(mEndRootView);
        }
    }

    /**
     * 正在加载更多，FootView显示 ： 加载中...
     */
    private void onLoadMore() {
        if (mLoadMoreListener != null) {
            // 加载中...
            mEndLoadTipsTextView.setText(R.string.pull_to_refresh_footer_refreshing_label);
            mEndLoadTipsTextView.setVisibility(View.VISIBLE);
            mEndLoadProgressBar.setVisibility(View.VISIBLE);

//            mEndRootView.setRefreshing(true);

            mLoadMoreListener.onLoadMore();
        }
    }

    /**
     * 加载更多完成
     */
    public void onLoadMoreComplete() {
        if (mIsAutoLoadMore) {
            mEndState = ENDINT_AUTO_LOAD_DONE;
        } else {
            mEndState = ENDINT_MANUAL_LOAD_DONE;
        }
        if (mEndRootView != null) {
            this.removeFooterView(mEndRootView);
            changeEndViewByState();
        }
    }

    /**
     * 主要更新一下刷新时间啦
     */
    public void setAdapter(BaseAdapter adapter) {
        // 最近更新: Time
//        mLastUpdatedTextView.setText(getResources().getString(R.string.pull_to_refresh_lasttime)
//                + new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
        super.setAdapter(adapter);
    }

    /**
     * return true, deliver to listView. return false, deliver to child. if move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mCanSwipe == false)
            return super.onInterceptTouchEvent(ev);
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
                System.out.println("onInterceptTouchEvent----->ACTION_DOWN");
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);

                if (motionPosition >= 0) {
                    View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;

                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                System.out.println("onInterceptTouchEvent----->ACTION_UP");
                if (mIsShown && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                    System.out.println("1---> hiddenRight");
                    /**
                     * 情况一：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }

    /**
     * @param dx
     * @param dy
     * @return judge if can judge scroll direction
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;

        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
            System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
            System.out.println("mIsHorizontal---->" + mIsHorizontal);
        } else {
            canJudge = false;
        }

        return canJudge;
    }

    private void clearPressedState() {
        // TODO current item is still has background, issue
        if (mCurrentItemView == null)
            return;
        mCurrentItemView.setPressed(false);
        setPressed(false);
        refreshDrawableState();
        // invalidate();
    }

    private void showRight(View view) {
        System.out.println("=========showRight");

        Message msg = new MoveHandler().obtainMessage();
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = mRightViewWidth;
        msg.sendToTarget();

        mIsShown = true;
    }

    public void hiddenRight(View view) {
        if (view == null)
            return;
        System.out.println("=========hiddenRight");
        if (mCurrentItemView == null) {
            return;
        }
        Message msg = new MoveHandler().obtainMessage();//
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = 0;

        msg.sendToTarget();

        mIsShown = false;
    }

    /**
     * show or hide right layout animation
     */
    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;

        int fromX;

        int toX;

        View view;

        private boolean mIsInAnimation = false;

        private void animatioOver() {
            mIsInAnimation = false;
            stepX = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View) msg.obj;
                fromX = msg.arg1;
                toX = msg.arg2;
                stepX = (int) ((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
                    animatioOver();
                    return;
                }
            }

            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX) || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }

            view.scrollTo(fromX, 0);
            invalidate();

            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }
    }

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }

    public void setOnScrollListenter(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }


    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            mCircumlarProgressDrawable.start();
        } else {
            mCircumlarProgressDrawable.stop();
        }
    }


    /**
     * -1到1
     */
    public void setProgress(float progress) {
        int rotation = (int) ((360 * progress) % 360);
        mCircumlarProgressDrawable.setPercent(rotation);
    }

//    public void setTextColor(int color) {
//        mTvText.setTextColor(color);
//    }
//
//    public void setText(CharSequence charSequence) {
//        mTvText.setText(charSequence);
//    }
}
