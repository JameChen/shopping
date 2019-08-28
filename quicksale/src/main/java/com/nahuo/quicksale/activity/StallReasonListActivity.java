package com.nahuo.quicksale.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.Dialog.DomDialog;
import com.nahuo.bean.FollowsBean;
import com.nahuo.bean.SearchPanelBean;
import com.nahuo.bean.SortMenusBean;
import com.nahuo.constant.IDsConstant;
import com.nahuo.library.controls.BottomMenu;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.CommonSearchActivity;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.Bookends1;
import com.nahuo.quicksale.adapter.PinHuoShowAdapter;
import com.nahuo.quicksale.adapter.SortFilterAdpater;
import com.nahuo.quicksale.adapter.SortMenusAdpater;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseSeasonActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.customview.ItemJCVideoPlayerStandard;
import com.nahuo.quicksale.customview.RGridLayoutManager;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayer;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.ItemShopCategory;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.CircleCarTxtUtl;
import com.nahuo.quicksale.util.FocusSave;
import com.nahuo.quicksale.util.RecyclerViewLoadMoreUtil;
import com.nahuo.quicksale.util.RxUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.width;
import static com.nahuo.quicksale.api.RequestMethod.QuickSaleMethod.GET_ITEM_4STALLID;

/**
 * 商品详情点击档口进去的场次列表
 * Created by ZZB on 201510/15.
 */
public class StallReasonListActivity extends BaseSeasonActivity implements PinHuoShowAdapter.TitleSortMenusLister, View.OnClickListener {
    private static final String TAG = StallReasonListActivity.class.getSimpleName();
    public static final String EXTRA_STALL_RID = "EXTRA_RID";
    private static final String EXTRA_PIN_HUO_OVERED = "EXTRA_PIN_HUO_OVERED";
    private static final String EXTRA_USERID = "EXTRA_USERID";
    private CircleTextView circleTextView;
    public TextView tvTitleCenter;
    private RelativeLayout rl_wai;
    public StallReasonListActivity activity;
    private TextView tvTLeft, tvTRight;
    private ImageView img_Tsearch;

    private static int PAGE_INDEX = 0;
    private static final int PAGE_SIZE = 20;
    private boolean isTasking = false, mIsRefresh = true;
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private RecommendModel mRecommendModel;
    //  private PinHuoDetailAdapter mNewItemAdapter;
    //  private BookStall<PinHuoDetailAdapter> mNewItemBookends;
    protected LoadingDialog mLoadingDialog;
    //private PinHuoModel mPinHuoModel;
    private ImageView mIvCover, iv_cover_yg, iv_cover_ended, mIvNewItemIcon, iv_cover_pining;
    private View mOveredTips, mNextActivitysView;
    private TextView tv_stall_name, tv_shop_intro, txt_next_yg, mTvShopName, mTvShopDesc, mTvHH, mTvH, mTvMM, mTvM, mTvSS, mTvS, mTvF, mTvDay, mTvDayDesc, mTvOveredTips, btn_focus;//mTvTitle, mTvContent,
    private WebView mTvNextTips, tv_overed_webview;
    private TextView mTvFHH, mTvFH, mTvFMM, mTvFM, mTvFSS, mTvFS, mTvFF, mTvFDay, mTvFDayDesc, mTvfOveredTips;
    private CircleImageView shopIcon;
    private MyCountDownTimer mCountDownTimer;
    private ProgressBar mLoadMorePB;
    private TextView mLoadMoreTxt, mTvNextActivity;
    private View mFloatHeadView, mVScrollToTop;
    private ItemJCVideoPlayerStandard video_player;
    private View layout_need_hide;
    private int mLastPos;
    private int n;
    private boolean mNeedToClearKeyword;
    private TextView tvDealSort, tvShopCat, tvCollectSort, tvMustDealSort;
    private List<ItemShopCategory> mItemShopCategories;
    //    private TextView tvDealSort1,tvCreateTimeSort1,tvCollectSort1,tvMustDealSort1;
    private Const.SortIndex mSortIndex = Const.SortIndex.DefaultDesc;
    private int mColorBlue = Color.parseColor("#38A8FE");
    private int mColorGray = Color.parseColor("#717171");
    private int mShopCatId = -1;
    private int mCurrentPs = 0;
    //private boolean isOvered = false;
    private Context mContext;
    private int displayMode = 0;
    private List<ShopItemListModel> PassItems = new ArrayList<>();
    public LinearLayout ll_bottom;
    // private HorizontalScrollView hs_bottomx;
    public int newCount = 0;
    public int oldeCount = 0;
    public List<ShopItemListModel> firstPassItems = new ArrayList<>();
    private String currentMenu = "";
    private int currentMenuID;
    private int sortBy = -1;
    private boolean isFocus = false;
    public static String FIRST_FOCUS_DETAI_PRE = "FIRST_FOCUS_DETAI_PRE";
    private int ID = -1, QID = -1, mRId = -1;
    private LinearLayout lladd;
    private long countdownDuration = 0;
    //static public PinHuoDetailListActivity act;
    private View layout_out_head, layout_name;
    private int head_height;
    private DrawerLayout mDrawerLayout;
    private View drawer_content;
    private TextView tv_draw_layout_ok, tv_draw_layout_clear;
    private RecyclerView recycler_drawer;
    private SortFilterAdpater sortFilterAdpater;
    private List<SearchPanelBean.PanelsBeanX> Panels = new ArrayList<>();
    private SearchPanelBean searchPanelBean = null;
    private boolean first_Post_Panels = true;
    private int lastPosition = 0;
    private int lastOffset = 0;
    private LinearLayoutManager linearLayoutManager;
    private String filterValues = "";//筛选数据
    private TextView[] mTvs, mTvs2;
    private int mCrrentPos;
    private Drawable nav_choose_sel, nav_choose;
    private boolean is_First_Sort = true;
    private boolean has_Sort = false;
    private boolean has_ICon = false;
    private List<ShopItemListModel> mData = new ArrayList<>();
    private PinHuoShowAdapter mNewItemAdapter;
    private RecyclerViewLoadMoreUtil recyclerViewLoadMoreUtil;
    private View radio_group_head, radio_group;
    private RadioButton headNewRb, headOlderRb, newRb, olderRb;
    private int displaystatuid = 0;
    private boolean isOnly_Pass = false;
    private TextView look_old_data, look_new_data;
    private boolean isHas_NewAndPass = false;
    private List<SortMenusBean> sortMenus = new ArrayList<>();
    private boolean isLoadedPassItem = false;
    private boolean isCheckPassTitle = false;
    private boolean isFirstLoadTitle = true;
    private boolean isFirstLoadIcon = true;
    private RecyclerView recyclerView_sort_menu, recyclerView_sort_menu_head;
    private SortMenusAdpater sortMenusAdpater;
    private boolean sortType_isNew = true;
    private boolean isScrollPassSort;
    private int sortPos = -1;
    private boolean isGbHide = false;
    private  boolean oneUP=true,oneDown=true;

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.SEARCH_PIN_HUO_DETAIL:
//                String keyword = event.data.toString();
//                search(keyword);
                break;
            case EventBusId.WEIXUN_NEW_MSG:
                ChatUtl.judeChatNums(circleTextView, event);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_pin_huo_stall_detail_list);
            init();
            initData();
            initViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mContext = this;
        BWApplication.addActivity(this);
        EventBus.getDefault().registerSticky(this);
        findViewById(R.id.iv_chat_txt).setOnClickListener(this);
        tvTLeft = (TextView) findViewById(R.id.tvTLeft);
        tvTRight = (TextView) findViewById(R.id.tvTRight);
        tvTRight.setVisibility(View.GONE);
        findViewById(R.id.iv_all_search).setOnClickListener(this);
        circleTextView = (CircleTextView) findViewById(R.id.circle_car_text);
        CircleCarTxtUtl.setColor(circleTextView);
        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
        img_Tsearch = (ImageView) findViewById(R.id.img_Tsearch);
        img_Tsearch.setVisibility(View.GONE);
        tvTRight.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.toolbar_cart, 0);
        tvTRight.setOnClickListener(this);
        tvTLeft.setOnClickListener(this);
        img_Tsearch.setOnClickListener(this);
        //isOvered = getIntent().getBooleanExtra(EXTRA_PIN_HUO_OVERED, false);
        mRId = getIntent().getIntExtra(EXTRA_STALL_RID, -1);
        onAttach(this);
    }

    private void initData() {
        //  mRefreshLayout.manualRefresh();
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mActivity);
        }
    }

    private void bindData() {
//        if (mRecommendModel.getInfo().getActivityType().equals("拼货")) {
//            lladd.setVisibility(View.VISIBLE);
//            if (mRecommendModel.getInfo().getOpenStatu() != null && mRecommendModel.getInfo().getOpenStatu().equals("开拼中")) {
//                mIvNewItemIcon.setVisibility(View.VISIBLE);
//            } else {
//                mIvNewItemIcon.setVisibility(View.GONE);
//            }
//        } else {
//            lladd.setVisibility(View.GONE);
//            mIvNewItemIcon.setVisibility(View.GONE);
//        }
        // mNewItemAdapter.setChengTuanCount(0);
        //  mNewItemAdapter.part2title = mRecommendModel.getInfo().getPart2Title();
        // countdownDuration = mRecommendModel.getInfo().getEndMillis() - System.currentTimeMillis();
        if (false) {
            if (btn_focus != null)
                btn_focus.setVisibility(View.VISIBLE);
        } else {
            if (btn_focus != null)
                btn_focus.setVisibility(View.GONE);
        }
        String url = "";
//        if (mNewItemAdapter != null)
//            mNewItemAdapter.setShowCoinPayIcon(mRecommendModel.isShowCoinPayIcon());
       // mRecommendModel.setCover("https://inews.gtimg.com/newsapp_bt/0/8405167103/1000");
        if (isFirstLoadIcon) {
            if (TextUtils.isEmpty(mRecommendModel.getCover())) {
                has_ICon = false;
            } else {
                has_ICon = true;
            }
            if (has_ICon) {
                if (mNewItemAdapter != null) {
                    //  mNewItemAdapter.removeHeaderView(headView);
                    mNewItemAdapter.addHeaderView(headView);
                }
            } else {
                if (mNewItemAdapter != null) {
                    mNewItemAdapter.removeHeaderView(headView);
                }
            }
            isFirstLoadIcon = false;
        }
        if (TextUtils.isEmpty(mRecommendModel.getCover())) {
            url = "";
        } else {
            url = ImageUrlExtends.getImageUrl(mRecommendModel.getCover(), 20);
        }

        if (tvTitleCenter != null) {
            tvTitleCenter.setText(mRecommendModel.getTitle() + "");
        }
       // mRecommendModel.getInfo().setVideo("http://nahuo-video-server.b0.upaiyun.com/0/171016/131525960265864128.mp4");
//        if (TextUtils.isEmpty(mRecommendModel.getInfo().getVideo())) {
//            if (video_player != null)
//                video_player.setVisibility(View.GONE);
//        } else {
//            if (video_player != null) {
//                video_player.setVisibility(View.VISIBLE);
//                video_player.setUp(mRecommendModel.getInfo().getVideo()
//                        , JCVideoPlayer.SCREEN_LAYOUT_LIST
//                        , mRecommendModel.getInfo().getName(), mRecommendModel, layout_need_hide);
//                Picasso.with(mAppContext).load(url).placeholder(R.drawable.empty_photo).into(video_player.thumbImageView);
//            }
//        }
//        if (mRecommendModel.getInfo() != null) {
//            if (tvTitleCenter != null) {
//                tvTitleCenter.setText(mRecommendModel.getInfo().getName() + "");
//            }
//            if (!TextUtils.isEmpty(mRecommendModel.getInfo().getStallName())) {
//                tv_stall_name.setText(mRecommendModel.getInfo().getStallName());
//                tv_stall_name.setVisibility(View.VISIBLE);
//            } else {
//                tv_stall_name.setVisibility(View.GONE);
//            }
//            if (!TextUtils.isEmpty(mRecommendModel.getInfo().getSignature())) {
//                tv_shop_intro.setText(mRecommendModel.getInfo().getSignature());
//                tv_shop_intro.setVisibility(View.GONE);
//            } else {
//                tv_shop_intro.setVisibility(View.GONE);
//            }
//        }
        // mNewItemAdapter.addHeaderView(sortHeadView);
//        if (is_First_Sort) {
//            if (TextUtils.isEmpty(url)) {
//                if (mNewItemAdapter != null)
//                    mNewItemAdapter.removeHeaderView(headView);
//            } else {
//                if (mNewItemAdapter != null) {
//                    mNewItemAdapter.removeHeaderView(headView);
//                    mNewItemAdapter.addHeaderView(headView);
//                }
//            }
//        } else {
//            if (has_ICon) {
//                if (mNewItemAdapter != null) {
//                    mNewItemAdapter.removeHeaderView(headView);
//                    mNewItemAdapter.addHeaderView(headView);
//                }
//            } else {
//                if (mNewItemAdapter != null) {
//                    mNewItemAdapter.removeHeaderView(headView);
//                }
//            }
//        }
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(mAppContext).load(url).placeholder(R.drawable.empty_photo).into(mIvCover);
        } else {
            mIvCover.setImageResource(R.drawable.empty_photo);
        }
        if (!TextUtils.isEmpty(mRecommendModel.getSummary())) {
            mTvShopDesc.setVisibility(View.VISIBLE);
            mTvShopDesc.setText(mRecommendModel.getSummary());
        } else {
            mTvShopDesc.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mRecommendModel.getName())) {
            if (layout_name != null)
                layout_name.setVisibility(View.GONE);
        } else {
            if (layout_name != null)
                layout_name.setVisibility(View.VISIBLE);
        }
        mTvShopName.setText(mRecommendModel.getName());
        //String shopLogo = Const.getShopLogo(mRecommendModel.getInfo().getShopUserID());
        //Picasso.with(mAppContext).load(shopLogo).placeholder(R.drawable.empty_photo).into(shopIcon);
//        if (mIsRefresh) {
//            if (mRecommendModel.getInfo().isStart()) {//开始状态 // && mRecommendModel.getInfo().isHasNewItems()
//                if (mRecommendModel.getInfo().getActivityType().equals("拼货")) {
//                    //普通场
//                    if (mRecommendModel.getInfo().getOpenStatu().equals("预告")) {
////                        mTvOveredTips.setVisibility(View.VISIBLE);
////                        .setText("预计将于今晚"+mRecommendModel.getInfo().getTimes()+"点开拼");
//                        //中部html
//                        mTvNextTips.setVisibility(View.VISIBLE);
//                        StringBuilder html = new StringBuilder();
//                        html.append("<html>"
//                                + "<head>"
//                                + "<style type=\"text/css\">"
//                                + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                                + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
//                                + "<div class=wp-item-detail-format>");
//                        html.append(mRecommendModel.getInfo().getCenterContent());
//
//                        html.append("</div>" + "</body>" + "</html>");
//                        mTvNextTips.getSettings().setDefaultTextEncodingName("UTF -8");
//                        mTvNextTips.loadData(html.toString(), "text/html; charset=UTF-8", null);
//                        mTvNextTips.setVerticalScrollBarEnabled(false);
//                        //右上角绿色图标
//                        iv_cover_yg.setVisibility(View.VISIBLE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                        //加载往期好货的数据
//                    } else if (mRecommendModel.getInfo().getOpenStatu().equals("开拼中")) {
//                        //右上角新款图标
//                        mIvNewItemIcon.setVisibility(View.VISIBLE);
//                        //倒计时
//                        mCountDownTimer = new MyCountDownTimer(countdownDuration);
//                        mCountDownTimer.start();
//                        mOveredTips.setVisibility(View.GONE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.tv_overed_tips).setVisibility(View.GONE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.VISIBLE);
//                        if (countdownDuration > 0) {
//                            if (lladd != null)
//                                lladd.setVisibility(View.VISIBLE);
//                            mCountDownTimer = new MyCountDownTimer(countdownDuration);
//                            mCountDownTimer.start();
//                        } else {
//                            if (lladd != null)
//                                lladd.setVisibility(View.GONE);
//                        }
//                    } else if (mRecommendModel.getInfo().getOpenStatu().equals("已结束")) {
//                        //中部html
//                        mTvNextTips.setVisibility(View.VISIBLE);
//                        StringBuilder html = new StringBuilder();
//                        html.append("<html>"
//                                + "<head>"
//                                + "<style type=\"text/css\">"
//                                + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                                + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
//                                + "<div class=wp-item-detail-format>");
//                        html.append(mRecommendModel.getInfo().getCenterContent());
//                        html.append("</div>" + "</body>" + "</html>");
//                        mTvNextTips.getSettings().setDefaultTextEncodingName("UTF -8");
//                        mTvNextTips.loadData(html.toString(), "text/html; charset=UTF-8", null);
//                        mTvNextTips.setVerticalScrollBarEnabled(false);
//                        //右上角结束图标  大邮戳没有， 只有小图
//                        iv_cover_ended.setVisibility(View.VISIBLE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                    }
//                } else {//聚合场，按照目前逻辑不变
//                    mIvNewItemIcon.setVisibility(View.GONE);
//                    if (lladd != null) {
//                        lladd.setVisibility(View.GONE);
//                    }
//                }
//            } else {//结束状态
//                //本场已结束
//                mTvOveredTips.setVisibility(View.VISIBLE);
//                mTvfOveredTips.setText("本场已结束");
//                mTvOveredTips.setText("本场已结束");
//                tv_overed_webview.setVisibility(View.VISIBLE);
//                StringBuilder html = new StringBuilder();
//                html.append("<html>"
//                        + "<head>"
//                        + "<style type=\"text/css\">"
//                        + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                        + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
//                        + "<div class=wp-item-detail-format>");
//                html.append(mRecommendModel.getInfo().getCenterContent());
//                html.append("</div>" + "</body>" + "</html>");
//                tv_overed_webview.getSettings().setDefaultTextEncodingName("UTF -8");
//                tv_overed_webview.loadData(html.toString(), "text/html; charset=UTF-8", null);
//                tv_overed_webview.setVerticalScrollBarEnabled(false);
//                mOveredTips.setVisibility(View.VISIBLE);
//                mTvOveredTips.setText("本场已结束");
//                headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                //右上角结束大邮戳  没这玩意
//                //此处加载本场结束的数据
//            }
//            txt_next_yg.setVisibility(mRecommendModel.getInfo().getTimes() > 0 ? View.VISIBLE : View.GONE);
//            txt_next_yg.setText("第" + mRecommendModel.getInfo().getTimes() + "期");
//        }
        ViewTreeObserver vto = layout_out_head.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout_out_head.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                head_height = layout_out_head.getHeight();
                //layout_out_head.getWidth();
            }
        });
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


    //新款排序
    private void initbottommenu() {
        if (mRecommendModel != null) {
            if (isGbHide) {
                if (mNewItemAdapter != null) {
                    mNewItemAdapter.removeHeaderView(gbHeadView);
                }
            } else {
                if (mNewItemAdapter != null) {
                    mNewItemAdapter.removeHeaderView(gbHeadView);
                    mNewItemAdapter.addHeaderView(gbHeadView);
                }
            }
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
                if (ListUtils.isEmpty(sortMenus)) {
                    //  ll_bottom.setVisibility(View.GONE);
                    if (mNewItemAdapter != null) {
                        mNewItemAdapter.removeHeaderView(sortHeadView);
                    }
                } else {
                    if (mNewItemAdapter != null) {
                        mNewItemAdapter.addHeaderView(sortHeadView);
                    }
                    //  ll_bottom.setVisibility(View.VISIBLE);
                    sortMenusAdpater.setCurrentMenuID(currentMenuID);
                    sortMenusAdpater.setNewData(sortMenus);
                    GridLayoutManager manager = new GridLayoutManager(mContext, sortMenus.size());
                    GridLayoutManager manager2 = new GridLayoutManager(mContext, sortMenus.size());
                    //manager.setAutoMeasureEnabled(true);
                    recyclerView_sort_menu.setLayoutManager(manager);
                    recyclerView_sort_menu.setAdapter(sortMenusAdpater);
                    if (recyclerView_sort_menu_head != null) {
                        recyclerView_sort_menu_head.setLayoutManager(manager2);
                        recyclerView_sort_menu_head.setAdapter(sortMenusAdpater);
                    }
                }
            }
        }


       /* currentMenuID = mRecommendModel.getCurrentMenuID();
        final List<SortMenusBean> sortMenus1 = mRecommendModel.getSortMenus();
        boolean hasValue;
        if (is_First_Sort) {
            if (!ListUtils.isEmpty(sortMenus1)) {
                has_Sort = true;
                if (mNewItemAdapter != null) {
                    mNewItemAdapter.removeHeaderView(sortHeadView);
                    mNewItemAdapter.addHeaderView(sortHeadView);
                }
                is_First_Sort = false;
                ll_bottomx.removeAllViews();
                ll_bottomx2.removeAllViews();
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams vparams = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 1), LinearLayout.LayoutParams.MATCH_PARENT);
                vparams.topMargin = DisplayUtil.dip2px(mContext, 10);
                vparams.bottomMargin = DisplayUtil.dip2px(mContext, 10);
                int drawpadding = DisplayUtil.dip2px(mContext, 8);
                int paddingRight = DisplayUtil.dip2px(mContext, 0);
                final Drawable nav_nomal = getResources().getDrawable(R.drawable.todefaut);
                nav_nomal.setBounds(0, 0, nav_nomal.getMinimumWidth(), nav_nomal.getMinimumHeight());
                final Drawable nav_up = getResources().getDrawable(R.drawable.toup);
                nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
                final Drawable nav_dowm = getResources().getDrawable(R.drawable.todown);
                nav_dowm.setBounds(0, 0, nav_dowm.getMinimumWidth(), nav_dowm.getMinimumHeight());
                nav_choose = getResources().getDrawable(R.drawable.screen_choose);
                nav_choose.setBounds(0, 0, nav_choose.getMinimumWidth(), nav_choose.getMinimumHeight());
                nav_choose_sel = getResources().getDrawable(R.drawable.screen_choose_click);
                nav_choose_sel.setBounds(0, 0, nav_choose_sel.getMinimumWidth(), nav_choose_sel.getMinimumHeight());
                ll_bottomx.setVisibility(View.VISIBLE);
                ll_bottomx2.setVisibility(View.VISIBLE);
                final List<SortMenusBean> sortMenus = new ArrayList<>();
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
                            sortMenus.add(sortMenusBean);
                        }
                    }
                } else {
                    sortMenus.addAll(sortMenus1);
                }
                mTvs = new TextView[sortMenus.size()];
                mTvs2 = new TextView[sortMenus.size()];
                for (int i = 0; i < sortMenus.size(); i++) {
                    LinearLayout l1 = new LinearLayout(mContext);
                    l1.setLayoutParams(lparams);
                    l1.setGravity(Gravity.CENTER);
                    l1.setOrientation(LinearLayout.HORIZONTAL);
                    TextView tv = new TextView(mContext);
                    View view = new View(mContext);
                    view.setLayoutParams(vparams);
                    view.setBackgroundColor(Color.parseColor("#D9D9D9"));
                    int value = sortMenus.get(i).getValue();
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    //tv.setPadding(30, 20, 30, 20);
                    tv.setLayoutParams(params);
                    tv.setText(sortMenus.get(i).getTitle());

                    LinearLayout l2 = new LinearLayout(mContext);
                    l2.setLayoutParams(lparams);
                    l2.setGravity(Gravity.CENTER);
                    l2.setOrientation(LinearLayout.HORIZONTAL);
                    TextView tv2 = new TextView(mContext);
                    View view2 = new View(mContext);
                    view2.setLayoutParams(vparams);
                    view2.setBackgroundColor(Color.parseColor("#D9D9D9"));
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setTextSize(16);
                    tv2.setLayoutParams(params);
                    tv2.setText(sortMenus.get(i).getTitle());

                    if (value == 4 || value == 5 || value == 20) {
                        tv.setPadding(0, 0, paddingRight, 0);
                        tv.setCompoundDrawablePadding(drawpadding);
                        tv2.setPadding(0, 0, paddingRight, 0);
                        tv2.setCompoundDrawablePadding(drawpadding);
                    }
                    if (value == currentMenuID) {
                        if (value == 5) {
                            tv.setCompoundDrawables(null, null, nav_up, null);
                            tv2.setCompoundDrawables(null, null, nav_up, null);
                        } else if (value == 4) {
                            tv.setCompoundDrawables(null, null, nav_dowm, null);
                            tv2.setCompoundDrawables(null, null, nav_dowm, null);
                        } else if (value == 20) {
                            tv.setCompoundDrawables(null, null, nav_choose_sel, null);
                            tv2.setCompoundDrawables(null, null, nav_choose_sel, null);
                        }
                        //tv.setCompoundDrawables(null, null, nav_up1, null);
                        tv.setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
                        tv2.setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
                    } else {
                        if (value == 5 || value == 4) {
                            tv.setCompoundDrawables(null, null, nav_nomal, null);
                            tv2.setCompoundDrawables(null, null, nav_nomal, null);
                        } else if (value == 20) {
                            tv.setCompoundDrawables(null, null, nav_choose, null);
                            tv2.setCompoundDrawables(null, null, nav_choose, null);
                        }
                        tv.setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
                        tv2.setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
                        //tv.setCompoundDrawables(null, null, nav_up, null);
                    }
                    mTvs[i] = tv;
                    mTvs2[i] = tv2;
                    l1.addView(tv);
                    l2.addView(tv2);
                    ll_bottomx.addView(l1);
                    ll_bottomx2.addView(l2);
                    if (i < sortMenus.size() - 1) {
                        ll_bottomx.addView(view);
                        ll_bottomx2.addView(view2);
                    }
                    final int pos = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCrrentPos = pos;
                            setItemOnclick(mTvs, mTvs2, pos, sortMenus, nav_nomal, nav_choose, nav_up, nav_dowm);
                        }
                    });
                    tv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCrrentPos = pos;
                            setItemOnclick(mTvs, mTvs2, pos, sortMenus, nav_nomal, nav_choose, nav_up, nav_dowm);

                        }
                    });

                }


            } else {
                //  ll_bottomx.setVisibility(View.GONE);
                if (mNewItemAdapter != null) {
                    mNewItemAdapter.removeHeaderView(sortHeadView);
                }
            }
        } else {
            if (mNewItemAdapter != null) {
                mNewItemAdapter.removeHeaderView(sortHeadView);
                if (has_Sort)
                    mNewItemAdapter.addHeaderView(sortHeadView);
            }
        }*/
    }

//    private void setItemOnclick(TextView[] mTvs, TextView[] mTvs2, int pos, List<SortMenusBean> sortMenus, Drawable nav_nomal, Drawable nav_choose, Drawable nav_up, Drawable nav_dowm) {
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
//                    mTvs2[j].setCompoundDrawables(null, null, nav_up, null);
//                    sortMenus.get(j).setValue(sortBy);
//                    mNewItemAdapter.clear();
//                    // displayMode = 0;
//                    loadData(true, true);
//                } else if (value == 5) {
//                    sortBy = 4;
//                    mTvs[j].setCompoundDrawables(null, null, nav_dowm, null);
//                    mTvs2[j].setCompoundDrawables(null, null, nav_dowm, null);
//                    sortMenus.get(j).setValue(sortBy);
//                    mNewItemAdapter.clear();
//                    //  displayMode = 0;
//                    loadData(true, true);
//                } else {
//                    sortBy = value;
//                    mTvs[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                    mTvs2[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                    mNewItemAdapter.clear();
//                    //  displayMode = 0;
//                    loadData(true, true);
//                }
//            } else {
//                int value = sortMenus.get(pos).getValue();
//                if (value != 20) {
//
//                    int sValue = sortMenus.get(j).getValue();
//                    if (sValue != 20) {
////                        if (sValue == 5) {
////                            mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
////                            mTvs2[j].setCompoundDrawables(null, null, nav_nomal, null);
////
////                        } else if (sValue == 4) {
////                            mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
////                            mTvs2[j].setCompoundDrawables(null, null, nav_nomal, null);
////                        }
//                        if (sValue == 5 || sValue == 4) {
//                            mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
//                            mTvs2[j].setCompoundDrawables(null, null, nav_nomal, null);
//                            sortMenus.get(j).setValue(4);
//                        }
//                        mTvs[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
//                        mTvs2[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
//                    }
//               /*     else if (sValue == 20) {
//                        mTvs[j].setCompoundDrawables(null, null, nav_choose, null);
//                        mTvs2[j].setCompoundDrawables(null, null, nav_choose, null);
//                    } */
//                }
//            }
//        }
//    }

    View footerView;

    //筛选更多接口
    private void getSearchPanel() {
        try {
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("ID", mRId);
            jsonobject.put("Keyword", "");
            jsonobject.put("Values", "");
            //QuickSaleApi.getSearchPanel(mAppContext, mRequestHelper, this, IDsConstant.AREAID_STALL + "", jsonobject.toString());
            addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG)
                    .getSearchPanel(IDsConstant.AREAID_STALL, jsonobject.toString(), displaystatuid)
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

    private void initViews() {
        initDrawView();
        radio_group = $(R.id.radio_group);
        radio_group.setVisibility(View.GONE);
        newRb = $(R.id.rb_new);
        olderRb = $(R.id.rb_older);
        mVScrollToTop = $(R.id.iv_scroll_to_top);
        tv_draw_layout_ok = $(R.id.tv_draw_layout_ok);
        tv_draw_layout_ok.setOnClickListener(this);
        tv_draw_layout_clear = $(R.id.tv_draw_layout_clear);
        tv_draw_layout_clear.setOnClickListener(this);
        mVScrollToTop.setOnClickListener(this);
        ll_bottom = $(R.id.ll_bottom);
        ll_bottom.setVisibility(View.GONE);
        //hs_bottomx = $(R.id.hs_bottomx);
        mNextActivitysView = $(R.id.ll_next_activity);
        mTvNextActivity = $(R.id.ll_next_activity_time);
        $(R.id.btn_next_activity).setOnClickListener(this);
        tvDealSort = $(R.id.tv_deal);
        tvDealSort.setOnClickListener(this);
        tvShopCat = $(R.id.tv_shopcat);
        tvShopCat.setOnClickListener(this);
        tvCollectSort = $(R.id.tv_collect);
        tvCollectSort.setOnClickListener(this);
        tvMustDealSort = $(R.id.tv_mustdeal);
        tvMustDealSort.setOnClickListener(this);
        tvMustDealSort.setVisibility(View.GONE);
        initHeadView();
        initFloatHeadView();
        mRefreshLayout = $(R.id.refresh_layout);
        // mRefreshLayout.setCallBack(this);
//        TextView txt_empty = $(R.id.empty_txt);
//        txt_empty.setText("没有找到相关款式");
        mRecyclerView = $(R.id.recycler_view);
        final GridLayoutManager gridManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(gridManager);
        //  mNewItemAdapter = new PinHuoDetailAdapter();
//        mNewItemAdapter.setHasHead(true);
//        mNewItemBookends = new BookStall<>(mNewItemAdapter, gridManager);
        mNewItemAdapter = new PinHuoShowAdapter(this);
        //  mNewItemBookends.addHeader(headView);
        //   mRecyclerView.setAdapter(mNewItemBookends);
//        mNewItemAdapter.setListener(new PinHuoDetailAdapter.Listener() {
//            @Override
//            public void onItemClick(ShopItemListModel item) {
//                Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
//                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
//                startActivity(intent);
//            }
//        });
        footerView = LayoutInflater.from(mAppContext).inflate(R.layout.layout_pin_huo_detail_passitem, null);
        mLoadMorePB = (ProgressBar) footerView.findViewById(R.id.progressbar);
        mLoadMoreTxt = (TextView) footerView.findViewById(R.id.load_more_txt);
        look_old_data = (TextView) footerView.findViewById(R.id.look_old_data);
        look_new_data = (TextView) footerView.findViewById(R.id.look_new_data);
        look_old_data.setOnClickListener(this);
        look_new_data.setOnClickListener(this);
        //  mNewItemBookends.addFooter(footerView);
        mNewItemAdapter.addFooterView(footerView);
        mRecyclerView.setAdapter(mNewItemAdapter);
        mNewItemAdapter.setEmptyLister(new PinHuoShowAdapter.EmptyLister() {
            @Override
            public void onClickEmpty() {
                mRefreshLayout.setRefreshing(true);
                onMyRefresh();
            }
        });
        mNewItemAdapter.setmListener(new PinHuoShowAdapter.PinHuoListener() {
            @Override
            public void onRemoveFollowLongClick(FollowsBean item) {

            }

            @Override
            public void onNewOlderItemClick(ShopItemListModel item) {
                Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, item.getQsID());
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mScrollThreshold = ScreenUtils.px2dip(mContext, 30);

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //   Log.e("yu","onScrolled=====>");
                try {
                    RGridLayoutManager manager = (RGridLayoutManager) recyclerView.getLayoutManager();
                    int pos = manager.findFirstVisibleItemPosition();
                    //Log.e("yu", "pos=====>" + pos);
                    if (pos >= 1) {
                        JCVideoPlayer.releaseAllVideos();
                    }
                    int scroll_height = -5;
                    int scroll_down_height = 5;
                    if (mNewItemAdapter != null) {
                        int headCount=mNewItemAdapter.getHeadCount();
                        int index = mNewItemAdapter.getSortItemIndex(sortHeadView);
                        int index3 = mNewItemAdapter.getSortItemIndex(gbHeadView);
                        //boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
                        if (index >= 0 && index3 >= 0) {
                            //有gb和sort
                            if (pos >= 0) {
                                if (pos >= index3) {
                                    //  Log.e("yu", "yesssss");
                                    ll_bottom.setVisibility(View.VISIBLE);
                                    if (pos == index3) {
                                        radio_group.setVisibility(View.VISIBLE);
                                    } else {

                                        if (dy < scroll_height) {
                                            radio_group.setVisibility(View.VISIBLE);
                                        } else {
                                            if (dy > scroll_down_height)
                                                radio_group.setVisibility(View.GONE);
                                        }
                                    }
                                    // radio_group.setVisibility(View.VISIBLE);
                                } else {
                                    radio_group.setVisibility(View.GONE);
                                    ll_bottom.setVisibility(View.GONE);
                                    //  Log.e("yu", "nonnojnononoo");
                                }
                            }
                        } else if (index >= 0 && index3 < 0) {
                            //没gb有sort
                            radio_group.setVisibility(View.GONE);
                            if (pos >= 0) {
                                if (pos >= index) {
                                    //  Log.e("yu", "yesssss");
                                    ll_bottom.setVisibility(View.VISIBLE);
                                } else {
                                    ll_bottom.setVisibility(View.GONE);
                                    //  Log.e("yu", "nonnojnononoo");
                                }
                            }
                        } else if (index < 0 && index3 >= 0) {
                            //有gb没sort
                            ll_bottom.setVisibility(View.GONE);
                            if (pos >= 0) {
                                if (pos >= index3) {
                                    //  Log.e("yu", "yesssss");
                                    if (pos == index3) {
                                        radio_group.setVisibility(View.VISIBLE);
                                    } else {

                                        if (dy < scroll_height) {
                                            radio_group.setVisibility(View.VISIBLE);
                                        } else {
                                            if (dy > scroll_down_height)
                                                radio_group.setVisibility(View.GONE);
                                        }
                                    }
//                                    if (!isOnly_Pass && dy < scroll_height) {
//                                        radio_group.setVisibility(View.VISIBLE);
//                                    } else {
//                                        if (dy > scroll_down_height)
//                                            radio_group.setVisibility(View.GONE);
//                                    }
                                    //  radio_group.setVisibility(View.VISIBLE);
                                } else {
                                    radio_group.setVisibility(View.GONE);
                                    //  Log.e("yu", "nonnojnononoo");
                                }
                            }
                        } else {
                            radio_group.setVisibility(View.GONE);
                            ll_bottom.setVisibility(View.GONE);
                        }
//                        if (pos == 0)
//                            ll_bottomx2.setVisibility(View.GONE);
                        int sPos=mNewItemAdapter.getPassSortPos();
                        if (sPos>=0) {
                            sortPos=sPos+headCount;
                        }
                        if (pos >= sortPos && sortPos != -1) {
                            isScrollPassSort = true;
                        } else {
                            isScrollPassSort = false;
                        }
                        if (isCheckPassTitle) {
                            if (headOlderRb != null)
                                headOlderRb.setChecked(true);
                            if (olderRb != null)
                                olderRb.setChecked(true);
                        } else {
                            if (isScrollPassSort) {
                                if (headOlderRb != null)
                                    headOlderRb.setChecked(true);
                                if (olderRb != null)
                                    olderRb.setChecked(true);
                            } else {
                                if (headNewRb != null)
                                    headNewRb.setChecked(true);
                                if (newRb != null)
                                    newRb.setChecked(true);
                            }
                        }
                       // Log.e("yu", "pos===" + pos + "--sortPos=="+sortPos+"---index==" + index + "---index3==" + index3+"--headcount="+headCount+"--dy="+dy+"--scroll_height="+scroll_height+"--scroll_down_height="+scroll_down_height);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
//                mVScrollToTop.setVisibility(pos > 5 ? View.VISIBLE : View.GONE);
            }
        });
        recyclerViewLoadMoreUtil = new RecyclerViewLoadMoreUtil();
        recyclerViewLoadMoreUtil.init(mContext, (ImageView) findViewById(R.id.iv_scroll_to_top), mRefreshLayout, mRecyclerView, mNewItemAdapter, new RecyclerViewLoadMoreUtil.RefreshDataListener() {
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
        recyclerViewLoadMoreUtil.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);

        recyclerViewLoadMoreUtil.autoRefreshing();//第一次自动加载一次

    }

    private void recyclerViewScollTop() {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(0);
        }
    }

    public void onMyRefresh() {
        mNeedToClearKeyword = true;
        if (n == 1) {
            if (mSortIndex == Const.SortIndex.DefaultDesc) {
                mSortIndex = Const.SortIndex.Default;
            }
        }
        mNewItemAdapter.clear();
        //  displayMode = 0;
        loadData(true, false);
    }

    public void onMyLoadMore() {
        //mRefreshLayout.setCanLoadMore(true);
        loadData(false, false);
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

    private void initDrawView() {
        mDrawerLayout = $(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        recycler_drawer = $(R.id.recycler_drawer);
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
                FunctionHelper.hideSoftInput(StallReasonListActivity.this);
                if (recycler_drawer != null)
                    recycler_drawer.setVisibility(View.INVISIBLE);
                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        drawer_content = $(R.id.drawer_content);
        drawer_content.setClickable(true);
    }

    public int getScollYDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    private View headView, sortHeadView, gbHeadView;

    @NonNull
    private View initHeadView() {

        gbHeadView = LayoutInflater.from(mActivity).inflate(R.layout.chang_shi_item_gb, null);
        headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_stall_list_head_view, null);
        sortHeadView = LayoutInflater.from(mActivity).inflate(R.layout.stall_head_sort_item, null);
        recyclerView_sort_menu = (RecyclerView) findViewById(R.id.recyclerView_sort_menu);
        recyclerView_sort_menu_head = (RecyclerView) sortHeadView.findViewById(R.id.recyclerView_sort_menu);
        recyclerView_sort_menu_head.setNestedScrollingEnabled(false);
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
        radio_group_head = gbHeadView.findViewById(R.id.radio_group);
        radio_group_head.setVisibility(View.VISIBLE);
        headNewRb = (RadioButton) gbHeadView.findViewById(R.id.rb_new);
        headOlderRb = (RadioButton) gbHeadView.findViewById(R.id.rb_older);
        newRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                if (newRb != null)
                    newRb.setChecked(true);
                isCheckPassTitle = false;
                sortType_isNew = true;
                if (headNewRb != null)
                    headNewRb.setChecked(true);
                //  newRb.setChecked(true);
                mNewItemAdapter.clear();
                displayMode = 0;
                displaystatuid = 0;
                loadData(true, true);
//                if (mLoadMoreTxt != null)
//                    mLoadMoreTxt.setText(R.string.list_more_msg);
//                if (headNewRb != null)
//                    headNewRb.setChecked(true);
//                mNewItemAdapter.removeAllHeadsView();
//                mNewItemAdapter.clear();
//                displayMode = 1;
//                displaystatuid = 0;
//                loadData(true, true);
                // recyclerViewScollTop();
            }
        });

        olderRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                if (olderRb != null)
                    olderRb.setChecked(true);
                isCheckPassTitle = true;
                sortType_isNew = false;
                if (headOlderRb != null)
                    headOlderRb.setChecked(true);
                if (olderRb != null)
                    olderRb.setChecked(true);
                //   olderRb.setChecked(true);
                mNewItemAdapter.clear();
                displaystatuid = 1;
                displayMode = 2;
                loadData(true, true);
            }
        });
        headNewRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                if (newRb != null)
                    newRb.setChecked(true);
                isCheckPassTitle = false;
                sortType_isNew = true;
                if (headNewRb != null)
                    headNewRb.setChecked(true);
                //  newRb.setChecked(true);
                mNewItemAdapter.clear();
                displayMode = 0;
                displaystatuid = 0;
                loadData(true, true);
//                mNewItemAdapter.removeAllHeadsView();
//                mNewItemAdapter.clear();
//                displaystatuid = 0;
//                displayMode = 1;
//                loadData(true, true);
                //recyclerViewScollTop();
            }
        });
        headOlderRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mLoadMoreTxt != null)
//                    mLoadMoreTxt.setText(R.string.list_more_msg);
//                if (olderRb != null)
//                    olderRb.setChecked(true);
//                mNewItemAdapter.removeAllHeadsView();
//                mNewItemAdapter.clear();
//                displaystatuid = 1;
//                displayMode = 2;
//                loadData(true, true);
                // recyclerViewScollTop();
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                if (olderRb != null)
                    olderRb.setChecked(true);
                isCheckPassTitle = true;
                sortType_isNew = false;
                if (headOlderRb != null)
                    headOlderRb.setChecked(true);
                if (olderRb != null)
                    olderRb.setChecked(true);
                //   olderRb.setChecked(true);
                mNewItemAdapter.clear();
                displaystatuid = 1;
                displayMode = 2;
                loadData(true, true);
            }
        });
//        mEmptyView = LayoutInflater.from(mActivity).inflate(R.layout.stall_empty, null);
//        mEmptyView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRefreshLayout.setRefreshing(true);
//                onMyRefresh();
//            }
//        });
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = mRecommendModel.getInfo().getUrl();
//                if (url.indexOf("/xiaozu/topic/") > 1) {
//                    String temp = "/xiaozu/topic/";
//                    int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                    mContext.startActivity(intent);
//                } else if (url.indexOf("/xiaozu/act/") > 1) {
//                    String temp = "/xiaozu/act/";
//                    int actID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                    mContext.startActivity(intent);
//                } else {
//                    Intent intent = new Intent(mContext, ItemPreview1Activity.class);
//                    intent.putExtra("name", "拼货预告");
//                    intent.putExtra("url", url);
//                    mContext.startActivity(intent);
//                }
            }
        });
        rl_wai = (RelativeLayout) headView.findViewById(R.id.rl_wai);
        LinearLayout.LayoutParams wai_layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(width));
        //RelativeLayout.LayoutParams li_layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getPinHuoProportionHeight(width));
        rl_wai.setLayoutParams(wai_layoutParams);
        layout_out_head = headView.findViewById(R.id.layout_out_head);
        layout_name = headView.findViewById(R.id.layout_name);
        video_player = (ItemJCVideoPlayerStandard) headView.findViewById(R.id.video_player);

       // ll_bottomx = (LinearLayout) sortHeadView.findViewById(R.id.ll_bottomx);

        layout_need_hide = headView.findViewById(R.id.layout_need_hide);
        tv_shop_intro = (TextView) headView.findViewById(R.id.tv_shop_intro);
        tv_stall_name = (TextView) headView.findViewById(R.id.tv_stall_name);
        tv_stall_name.setVisibility(View.GONE);
        mIvCover = (ImageView) headView.findViewById(R.id.iv_cover);
        iv_cover_yg = (ImageView) headView.findViewById(R.id.iv_cover_yg);
        iv_cover_ended = (ImageView) headView.findViewById(R.id.iv_cover_ended);
        txt_next_yg = (TextView) headView.findViewById(R.id.txt_next_yg);
        mIvNewItemIcon = (ImageView) headView.findViewById(R.id.iv_newitem_icon);
//        iv_cover_pining = (ImageView) headView.findViewById(R.id.iv_cover_pining);
        mOveredTips = (View) headView.findViewById(R.id.iv_over_tips);
        lladd = (LinearLayout) headView.findViewById(R.id.lladd);
        lladd.setVisibility(View.GONE);
        tv_overed_webview = (WebView) headView.findViewById(R.id.tv_overed_webview);

        mTvShopDesc = (TextView) headView.findViewById(R.id.tv_shop_desc);
        mTvShopName = (TextView) headView.findViewById(R.id.tv_shop_name);
        shopIcon = (CircleImageView) headView.findViewById(R.id.tv_shop_icon);
        shopIcon.setVisibility(View.GONE);
        mTvHH = (TextView) headView.findViewById(R.id.tv_hh);
        mTvH = (TextView) headView.findViewById(R.id.tv_h);
        mTvMM = (TextView) headView.findViewById(R.id.tv_mm);
        mTvM = (TextView) headView.findViewById(R.id.tv_m);
        mTvSS = (TextView) headView.findViewById(R.id.tv_ss);
        mTvS = (TextView) headView.findViewById(R.id.tv_s);
        mTvF = (TextView) headView.findViewById(R.id.tv_f);
        mTvDay = (TextView) headView.findViewById(R.id.tv_day);
        mTvDayDesc = (TextView) headView.findViewById(R.id.tv_day_desc);
//        mTvOveredTips = (TextView) headView.findViewById(R.id.tv_overed_tips);
        mTvOveredTips = (TextView) lladd.findViewById(R.id.tv_overed_tips);
        mTvNextTips = (WebView) lladd.findViewById(R.id.tv_next_tips);
        btn_focus = (TextView) headView.findViewById(R.id.btn_focus);
        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int notice;
                notice = R.string.fucous_notice;
//                if (!isFocus) {
//                    notice = R.string.fucous_notice;
//                } else {
//                    notice = R.string.cancel_fucous_notice;
//                }
                QuickSaleApi.saveFocus(mContext, mRequestHelper, new HttpRequestListener() {
                    @Override
                    public void onRequestStart(String method) {
                        if (isFocus) {
                            mLoadingDialog.start("取消关注中...");
                        } else {
                            mLoadingDialog.start("关注中...");
                        }
                    }

                    @Override
                    public void onRequestSuccess(String method, Object object) {
                        mLoadingDialog.stop();
                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
                        if (isFocus) {
                            isFocus = false;
                            btn_focus.setText("关注本团");
                            btn_focus.setTextColor(getResources().getColor(R.color.text_red));
                            btn_focus.setBackgroundResource(R.drawable.bg_rectangle_red1);
                            ViewHub.showLongToast(mContext, "已取消关注");
                        } else {
                            isFocus = true;
                            btn_focus.setText("取消关注");
                            btn_focus.setTextColor(getResources().getColor(R.color.gray_92));
                            btn_focus.setBackgroundResource(R.drawable.bg_rectangle_grayx);
                            ViewHub.showLongToast(mContext, "已关注");
                        }
                    }

                    @Override
                    public void onRequestFail(String method, int statusCode, String msg) {
                        mLoadingDialog.stop();
                        ViewHub.showShortToast(mContext, msg);
                    }

                    @Override
                    public void onRequestExp(String method, String msg, ResultData data) {
                        mLoadingDialog.stop();
                        ViewHub.showShortToast(mContext, msg);
                    }
                }, ID);
                String use_id = SpManager.getUserId(mActivity) + "";
                FocusSave focusSave = new FocusSave(mContext, use_id);
                if (!focusSave.getFocus(FIRST_FOCUS_DETAI_PRE + use_id)) {
                    focusSave.setFocus(FIRST_FOCUS_DETAI_PRE + use_id, true);
                    DomDialog dialog = new DomDialog(mActivity);
                    dialog.setTitle("提示").setMessage(notice).setPositive("关闭", new DomDialog.PopDialogListener() {
                        @Override
                        public void onPopDialogButtonClick(int which) {

                        }
                    }).show();
                }

            }
        });
//        if (mPinHuoModel.getActivityType().equals("拼货")) {
//            lladd.setVisibility(View.VISIBLE);
//            if (mPinHuoModel.getOpenStatu() != null && mPinHuoModel.getOpenStatu().getStatu().equals("开拼中")) {
//                mIvNewItemIcon.setVisibility(View.VISIBLE);
//            } else {
//                mIvNewItemIcon.setVisibility(View.GONE);
//            }
//        } else {
//
//            lladd.setVisibility(View.GONE);
//            mIvNewItemIcon.setVisibility(View.GONE);
//        }

        return headView;
    }

    private void initFloatHeadView() {
        mFloatHeadView = findViewById(R.id.layout_float_head_view);
        mTvFHH = (TextView) mFloatHeadView.findViewById(R.id.tv_hh);
        mTvFH = (TextView) mFloatHeadView.findViewById(R.id.tv_h);
        mTvFMM = (TextView) mFloatHeadView.findViewById(R.id.tv_mm);
        mTvFM = (TextView) mFloatHeadView.findViewById(R.id.tv_m);
        mTvFSS = (TextView) mFloatHeadView.findViewById(R.id.tv_ss);
        mTvFS = (TextView) mFloatHeadView.findViewById(R.id.tv_s);
        mTvFF = (TextView) mFloatHeadView.findViewById(R.id.tv_f);
        mTvFDay = (TextView) mFloatHeadView.findViewById(R.id.tv_day);
        mTvFDayDesc = (TextView) mFloatHeadView.findViewById(R.id.tv_day_desc);
        mTvfOveredTips = (TextView) mFloatHeadView.findViewById(R.id.tv_overed_tips);
        mFloatHeadView.setVisibility(View.GONE);
        mFloatHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url = mPinHuoModel.getUrl();
//                if (url.indexOf("/xiaozu/topic/") > 1) {
//                    String temp = "/xiaozu/topic/";
//                    int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                    mContext.startActivity(intent);
//                } else if (url.indexOf("/xiaozu/act/") > 1) {
//                    String temp = "/xiaozu/act/";
//                    int actID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                    mContext.startActivity(intent);
//                } else {
//                    Intent intent = new Intent(mContext, ItemPreview1Activity.class);
//                    intent.putExtra("name", "拼货预告");
//                    intent.putExtra("url", url);
//                    mContext.startActivity(intent);
//                }
            }
        });
    }

    private void loadData(boolean isRefresh, boolean showLoading) {
        recyclerViewLoadMoreUtil.setPullUpRefreshEnable(true);
//        if (mPinHuoModel != null)
//            QID = mPinHuoModel.ID > 0 ? mPinHuoModel.ID : mPinHuoModel.QsID;
        if (isTasking) {
            return;
        } else {
            if (mLoadingDialog == null && mContext != null)
                mLoadingDialog = new LoadingDialog(mContext);

            // mNewItemBookends.setShowFooter(true);
            mLoadMorePB.setVisibility(View.VISIBLE);
            mLoadMoreTxt.setText("正在加载更多");
            //showEmptyView(false);
            mIsRefresh = isRefresh;
            isTasking = true;
            PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
            if (showLoading) {
                try {
                    mLoadingDialog.show();
                } catch (Exception e) {
                    e.toString();
                }
            }
//            QuickSaleApi.getItem4StallID(mAppContext,
//                    mRequestHelper,
//                    this,
//                    mRId,
//                    PAGE_INDEX,
//                    PAGE_SIZE,
//                    sortBy,
//                    displayMode, filterValues);
        }
        getItem4StallID(mRId, PAGE_INDEX, PAGE_SIZE, sortBy == -1 ? null : sortBy + "", displayMode, filterValues);

    }

    private void getItem4StallID(int sid, int pageIndex, int pageSize, String sort, int displayMode, String filterValues) {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getItem4StallID(sid, pageIndex, pageSize, sort, displayMode, filterValues)
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


//    @Override
//    public void onRefresh() {
//        //Log.v(TAG,"start onresume");
////        if (mNeedToClearKeyword) {//第一次之后需要清除搜索关键字
////            mPinHuoModel.keyword = "";
////        }
//        mNeedToClearKeyword = true;
//        if (n == 1) {
//            if (mSortIndex == Const.SortIndex.DefaultDesc) {
//                mSortIndex = Const.SortIndex.Default;
//            }
//        }
//        mNewItemAdapter.clear();
//        displayMode = 0;
//        loadData(true, false);
//        // displayMode = 1;
//
//        //new LoadGoodsTask(getActivity(),carCountTv).execute();
//
//    }
//
//    @Override
//    public void onLoadMore() {
//        // mRefreshLayout.setCanLoadMore(true);
//        loadData(false, false);
//    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        FunctionHelper.hideSoftInput(StallReasonListActivity.this);
        JCVideoPlayer.JC_TYPE = JCVideoPlayer.IS_PINHUO_MAIN_LIST_TYPE;
        // new LoadGoodsTask(this, circleTextView).execute();
        ChatUtl.setChatBroad(this);
//        if (!isOvered && mRecommendModel != null) {
//            long countdownDuration = mRecommendModel.getInfo().getEndMillis() - System.currentTimeMillis();
//            SpManager.setQuickSellEndTime(mAppContext, mRecommendModel.getInfo().getEndMillis());
//            mCountDownTimer = new MyCountDownTimer(countdownDuration);
//            mCountDownTimer.start();
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    public void onRequestStart(String method) {
        super.onRequestStart(method);
        switch (method) {
            case GET_ITEM_4STALLID:
                showDialog(mLoadingDialog, "正在加载档口数据");
                break;
        }

    }

    public void showDialog(LoadingDialog mLoadingDialog, String mess) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setMessage(mess);
            mLoadingDialog.show();
        }
    }

    public void closeDialog(LoadingDialog mLoadingDialog) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object);
        switch (method) {
            case GET_ITEM_4STALLID:
                closeDialog(mLoadingDialog);
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
    public void onRequestFail(String method, int statusCode, String msg) {
        switch (method) {
            case GET_ITEM_4STALLID:
                closeDialog(mLoadingDialog);
                loadFinished();
                onLoadPinAndForecastFailed();
                break;
        }

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        switch (method) {
            case GET_ITEM_4STALLID:
                closeDialog(mLoadingDialog);
                loadFinished();
                onLoadPinAndForecastFailed();
                break;
        }
    }

    public void onLoadPinAndForecastFailed() {
        loadFinished();
        ViewHub.showShortToast(mContext, "加载失败，请稍候再试");
    }

    private void loadFinished() {
        if (mLoadingDialog != null) {
            mLoadingDialog.stop();
        }
        isTasking = false;
//        mRefreshLayout.completeRefresh();
//        mRefreshLayout.completeLoadMore();
        recyclerViewLoadMoreUtil.endRefreshing();
        recyclerViewLoadMoreUtil.endLoading();
    }

    public void getFocusStat() {
        List<String> s = new ArrayList<>();
        ID = mRecommendModel.getInfo().getShopID();
        s.add("" + ID);
        QuickSaleApi.getFocusStatList(mContext, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
//                mLoadingDialog.start("读取关注数据中");
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
//                mLoadingDialog.stop();
                // btn_focus.setVisibility(View.VISIBLE);
                try {
                    List<Integer> shopids = GsonHelper.jsonToObject(object.toString(),
                            new TypeToken<List<Integer>>() {
                            });
                    if (shopids.indexOf(ID) > -1) {
                        isFocus = true;
                        btn_focus.setText("取消关注");
                        btn_focus.setTextColor(getResources().getColor(R.color.gray_92));
                        btn_focus.setBackgroundResource(R.drawable.bg_rectangle_grayx);
                    } else {
                        isFocus = false;
                        btn_focus.setText("关注本团");
                        btn_focus.setTextColor(getResources().getColor(R.color.text_red));
                        btn_focus.setBackgroundResource(R.drawable.bg_rectangle_red1);
                    }
                } catch (Exception ex) {
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
//                mLoadingDialog.stop();
                ViewHub.showShortToast(mContext, msg);
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
//                mLoadingDialog.stop();
                ViewHub.showShortToast(mContext, msg);
            }
        }, s);
    }

    String Part1Title = "", Part2Title = "";

    private void onDataLoaded(Object obj) {

        try {
            loadFinished();
            mRecommendModel = (RecommendModel) obj;
            Bookends1.part2title = "往期好货";
            // displayMode 0 旧款新款 1新 2 旧
            if (mIsRefresh) {
                if (mLoadMoreTxt != null)
                    mLoadMoreTxt.setText(R.string.list_more_msg);
                look_old_data.setVisibility(View.GONE);
                look_new_data.setVisibility(View.GONE);
                if (mRecommendModel != null) {
//                if (!TextUtils.isEmpty(mRecommendModel.getPart2Title()))
//                    Bookends1.part2title = mRecommendModel.getPart2Title();
                    Part1Title = mRecommendModel.getPart1Title();
                    Part2Title = mRecommendModel.getPart2Title();
                    if (olderRb != null)
                        olderRb.setText(mRecommendModel.getPart2Title());
                    if (headOlderRb != null)
                        headOlderRb.setText(mRecommendModel.getPart2Title());
                    if (headNewRb != null)
                        headNewRb.setText(Part1Title);
                    if (newRb != null)
                        newRb.setText(Part1Title);
                }
                bindData();
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
               // showEmptyView(false);
                if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    //没数据
                    if (isFirstLoadTitle) {
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.GONE);
                        if (radio_group != null)
                            radio_group.setVisibility(View.GONE);
                        isGbHide = true;
                        isFirstLoadTitle = false;
                        initbottommenu();
                    }
//                    displayMode = 0;
//                    displaystatuid = 0;
                    showEmptyView();
                    // isCheckPassTitle = false;

                } else if (ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    //只有新款
                    if (isFirstLoadTitle) {
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.GONE);
                        if (radio_group != null)
                            radio_group.setVisibility(View.GONE);
                        isGbHide = true;
                        isFirstLoadTitle = false;
                        initbottommenu();
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
                    if (displayMode == 0 || displayMode == 1) {
                        isCheckPassTitle = true;
                    }
                    if (isFirstLoadTitle) {
                        if (radio_group != null)
                            radio_group.setVisibility(View.VISIBLE);
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.VISIBLE);
                        isGbHide = false;
                        if (headOlderRb != null) {
                            headOlderRb.setVisibility(View.VISIBLE);
                            headOlderRb.setChecked(true);
                        }
                        if (headNewRb != null)
                            headNewRb.setVisibility(View.GONE);
                        if (olderRb != null) {
                            olderRb.setVisibility(View.VISIBLE);
                            olderRb.setChecked(true);
                        }
                        if (newRb != null)
                            newRb.setVisibility(View.GONE);
                        isFirstLoadTitle = false;
                        initbottommenu();
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
                        if (radio_group != null)
                            radio_group.setVisibility(View.VISIBLE);
                        if (radio_group_head != null)
                            radio_group_head.setVisibility(View.VISIBLE);
                        isGbHide = false;
                        isFirstLoadTitle = false;
                        initbottommenu();
                    }
                    if (radio_group_head != null)
                        radio_group_head.setVisibility(View.VISIBLE);
                    if (radio_group != null)
                        radio_group.setVisibility(View.VISIBLE);
                    isGbHide = false;
                    // addItemTitle(mRecommendModel, ShopItemListModel.Show_All_Title);
                    // addItemSortMenus();
                    if (olderRb != null)
                        olderRb.setVisibility(View.VISIBLE);
                    if (newRb != null)
                        newRb.setVisibility(View.VISIBLE);
                    if (headOlderRb != null)
                        headOlderRb.setVisibility(View.VISIBLE);
                    if (headNewRb != null)
                        headNewRb.setVisibility(View.VISIBLE);
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
                    if (isCheckPassTitle) {
                        if (headOlderRb != null)
                            headOlderRb.setChecked(true);
                        if (olderRb != null)
                            olderRb.setChecked(true);
                    } else {
                        if (headNewRb != null)
                            headNewRb.setChecked(true);
                        if (newRb != null)
                            newRb.setChecked(true);
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
       /*

                if (!ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    isHas_NewAndPass = true;
                } else {
                    isHas_NewAndPass = false;
                }
                if (!ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    displayMode = 2;
                    displaystatuid = 1;
                }
                if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    if (radio_group_head != null)
//                        radio_group_head.setVisibility(View.GONE);
                    if (mNewItemAdapter != null) {
                        mNewItemAdapter.removeHeaderView(gbHeadView);
                    }
                } else if (ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    if (mNewItemAdapter != null) {
                        mNewItemAdapter.removeHeaderView(gbHeadView);
                        //mNewItemAdapter.addHeaderView(headView);
                    }
//                    if (radio_group_head != null)
//                        radio_group_head.setVisibility(View.GONE);
                } else if (!ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    if (radio_group_head != null)
//                        radio_group_head.setVisibility(View.VISIBLE);
                    isOnly_Pass = true;
                    if (mNewItemAdapter != null) {
                        mNewItemAdapter.removeHeaderView(gbHeadView);
                        mNewItemAdapter.addHeaderView(gbHeadView);
                    }
                    olderRb.setVisibility(View.VISIBLE);
                    olderRb.setChecked(true);
                    newRb.setVisibility(View.GONE);
                    headOlderRb.setVisibility(View.VISIBLE);
                    headOlderRb.setChecked(true);
                    headNewRb.setVisibility(View.GONE);
                } else if (!ListUtils.isEmpty(mRecommendModel.PassItems) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    if (radio_group_head != null)
//                        radio_group_head.setVisibility(View.VISIBLE);
                    if (mNewItemAdapter != null) {
                        mNewItemAdapter.removeHeaderView(gbHeadView);
                        mNewItemAdapter.addHeaderView(gbHeadView);
                    }
                }
                initbottommenu();
                firstPassItems = mRecommendModel.PassItems;
                if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
                    Bookends1.passPosindex = -100;
                    showEmptyView(true);
                } else {
                    if (displayMode == 0 || displayMode == 1) {
                        Bookends1.passPosindex = -100;
                        addNewData(mRecommendModel.NewItems, false);
                        if (mRecommendModel.NewItems.size() < 20) {
                            setNoMore();
                        } else {
                            displayMode = 1;
                        }
                    } else if (displayMode == 2) {
                        Bookends1.passPosindex = -100;
                        addOlderData(firstPassItems, false);
                        if (mRecommendModel.PassItems.size() < 20) {
                            setNoMore();
                        }
                    }
                }

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
//                newCount = 0;
//                oldeCount = 0;
//                // mNewItemAdapter.setData(mRecommendModel.NewItems);
//                firstPassItems = mRecommendModel.PassItems;
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
//                                // load_more_no_data_txt.setVisibility(View.VISIBLE);
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
//                            // load_more_no_data_txt.setVisibility(View.VISIBLE);
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
//                            // load_more_no_data_txt.setVisibility(View.VISIBLE);
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
            } else {
                //加载更多
                //继续加载新款


                if (displayMode == 1 || displayMode == 0) {
                    addMoreNewData(mRecommendModel.NewItems, false, 0);
                    if (mRecommendModel.NewItems.size() < 20) {
                        //新款加载完成去加载旧款
                        setNoMore();
                    }
                } else if (displayMode == 2) {
                    addMoreOlderData(mRecommendModel.PassItems, false, 0);
                    if (mRecommendModel.PassItems.size() < 20) {
                        //新款加载完成去加载旧款
                        setNoMore();
                    }
                }*/


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
//            if (mIsRefresh){
//                if (mRecyclerView!=null) {
//                    mRecyclerView.setFocusableInTouchMode(true);
//                    mRecyclerView.scrollToPosition(0);
//                }
//            }
//            if (mLoadingDialog.isShowing()) {
//                mLoadingDialog.stop();
//                mRefreshLayout.scrollTo(0, 0);
//            }
//            if (mRecommendModel.getNextAcvivity() != null &&
//                    mRecommendModel.getNextAcvivity().getID() > 0) {
//                mNextActivitysView.setVisibility(View.VISIBLE);
//                long startMillis = TimeUtils.timeStampToMillis(mRecommendModel.getNextAcvivity().getStartTime());
//                Date startDate = TimeUtils.timeStampToDate(mRecommendModel.getNextAcvivity().getStartTime(), "yyyy-MM-dd HH:mm:ss");
//                Calendar startCal = Calendar.getInstance();
//                startCal.setTime(startDate);
//                int startYear = startCal.get(Calendar.YEAR);
//                int startMonth = startCal.get(Calendar.MONTH);
//                int startDay = startCal.get(Calendar.DATE);
//                int startHour = startCal.get(Calendar.HOUR);
//
//                Calendar cal = Calendar.getInstance();
//                int nowYear = cal.get(Calendar.YEAR);
//                int nowMonth = cal.get(Calendar.MONTH);
//                int nowDay = cal.get(Calendar.DATE);
//                int nowHour = cal.get(Calendar.HOUR);
//
//                if (startYear == nowYear && startMonth == nowMonth) {
//                    if (startDay > nowDay) {
//                        if (startDay - nowDay < 1) {//今天
//                            mTvNextActivity.setText("下一场今天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "后开拼");
//                            return;
//                        } else if (startDay - nowDay < 2) {//明天
//                            mTvNextActivity.setText("下一场明天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "开拼");
//                            return;
//                        }
//                    }
//                }
//                mTvNextActivity.setText("下一场" + TimeUtils.millisToTimestamp(startMillis, "MM月dd日HH点") + "开拼");
//            } else {
//                mNextActivitysView.setVisibility(View.GONE);
//            }

        } catch (Exception e) {
            e.printStackTrace();
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
                if (olderRb != null)
                    olderRb.setChecked(true);
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
                if (newRb != null)
                    newRb.setChecked(true);
                //  newRb.setChecked(true);
                mNewItemAdapter.clear();
                displayMode = 0;
                displaystatuid = 0;
                loadData(true, true);
                break;
            case R.id.iv_all_search:
                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                break;
            case R.id.iv_chat_txt:
//                Intent cIntent = new Intent(this, ChatMainActivity.class);
//                cIntent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
//                startActivity(cIntent);
                ChatUtl.goToChatMainActivity(this, true);
                break;
            case R.id.tvTLeft:
                finish();
                break;
            case R.id.img_Tsearch:
                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                break;
            case R.id.tvTRight:
                Utils.gotoShopcart(this);
                break;
            case R.id.tv_draw_layout_ok:
                mDrawerLayout.closeDrawer(drawer_content);
                setSortData();
                break;
            case R.id.tv_draw_layout_clear:
                if (sortFilterAdpater != null)
                    sortFilterAdpater.setClear();
                break;
            case R.id.btn_next_activity:// 查看拼货预告
                String url = mRecommendModel.getNextAcvivity().getUrl();
                if (url.indexOf("/xiaozu/topic/") > 1) {
                    String temp = "/xiaozu/topic/";
                    int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                    mContext.startActivity(intent);
                } else if (url.indexOf("/xiaozu/act/") > 1) {
                    String temp = "/xiaozu/act/";
                    int actID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));

                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, ItemPreview1Activity.class);
                    intent.putExtra("name", "拼货预告");
                    intent.putExtra("url", url);
                    mContext.startActivity(intent);
                }
                break;
            case R.id.iv_scroll_to_top:
                mRecyclerView.scrollToPosition(0);
                break;
            case R.id.tv_deal:
                setSortStatus(v.getId());
                mRecyclerView.scrollToPosition(0);// 防止刷新后，加载提示显示为无更多数据，其实是有数据，可以加载更多的
                break;
            case R.id.tv_shopcat:
                onFileterCategoryClicked((View) v.getParent());
                break;
            case R.id.tv_collect:
                setSortStatus(v.getId());
                mRecyclerView.scrollToPosition(0);// 防止刷新后，加载提示显示为无更多数据，其实是有数据，可以加载更多的
                break;
            case R.id.tv_mustdeal:
                setSortStatus(v.getId());
                mRecyclerView.scrollToPosition(0);//  防止刷新后，加载提示显示为无更多数据，其实是有数据，可以加载更多的
                break;
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
                    //  }

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

    /**
     * @description 过滤分类
     * @created 2015-3-19 上午10:16:10
     * @author ZZB
     */
    private void onFileterCategoryClicked(View anchorView) {
        if (mItemShopCategories == null) {
            ViewHub.showShortToast(mAppContext, "分类信息加载中，请稍等");
            return;
        }
        if (mItemShopCategories.size() == 0) {
            ViewHub.showShortToast(mAppContext, "暂无分类");
            return;
        }
        String[] items = new String[mItemShopCategories.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = mItemShopCategories.get(i).getName();
            if (mItemShopCategories.get(i).getId() == mShopCatId) {
                mCurrentPs = i;
            }
        }
        BottomMenu menu = new BottomMenu(this);
        menu.setSelectedItemPosition(mCurrentPs);
        menu.setItems(items).setOnMenuItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPs = position;
                filterCategory(mItemShopCategories.get(position).getId());
            }
        });
        menu.show(anchorView);
    }

    /**
     * @description 过滤分类
     * @created 2015-3-24 下午6:33:32
     * @author ZZB
     */
    private void filterCategory(int catId) {
        mShopCatId = catId;
        onMyRefresh();
    }

    //tag = 1 倒序         tag = 0 正序
    private void setSortStatus(int resId) {
        switch (resId) {
            case R.id.tv_deal:
                if (mSortIndex != Const.SortIndex.DealCountDesc) {
                    mSortIndex = Const.SortIndex.DealCountDesc;
                    mNewItemAdapter.clear();
                    //displayMode = 0;
                    resetSortView();
                    loadData(true, true);
                }
                break;
            case R.id.tv_collect:
                if (mSortIndex != Const.SortIndex.CollectCountDesc) {
                    mSortIndex = Const.SortIndex.CollectCountDesc;
                    mNewItemAdapter.clear();
                    // displayMode = 0;
                    resetSortView();
                    loadData(true, true);
                }
                break;
            case R.id.tv_mustdeal:
                if (mSortIndex != Const.SortIndex.MustDealDesc) {
                    mSortIndex = Const.SortIndex.MustDealDesc;
                    mNewItemAdapter.clear();
                    // displayMode = 0;
                    resetSortView();
                    loadData(true, true);
                }
                break;
        }
    }

    private void resetSortView() {
        switch (mSortIndex) {
            case DealCountDesc:
                tvDealSort.setTextColor(mColorBlue);
                tvCollectSort.setTextColor(mColorGray);
                tvMustDealSort.setTextColor(mColorGray);
                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                break;
            case CreateTimeDesc:
                tvDealSort.setTextColor(mColorGray);
                tvCollectSort.setTextColor(mColorGray);
                tvMustDealSort.setTextColor(mColorGray);
                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                break;
            case CollectCountDesc:
                tvDealSort.setTextColor(mColorGray);
                tvCollectSort.setTextColor(mColorBlue);
                tvMustDealSort.setTextColor(mColorGray);
                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                break;
            case MustDealDesc:
                tvDealSort.setTextColor(mColorGray);
                tvCollectSort.setTextColor(mColorGray);
                tvMustDealSort.setTextColor(mColorBlue);
                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                break;
        }
    }

    private class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long millisInFuture) {
            super(millisInFuture, 100);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millisUntilFinished_now = mRecommendModel.getInfo().getEndMillis() - System.currentTimeMillis();
            int hour = (int) (millisUntilFinished_now / TimeUtils.HOUR_MILLIS);
            int min = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
            int sec = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
            int milli = (int) (millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS - sec * TimeUtils.SECOND_MILLIS);
            updateCountDownTime(hour, min, sec, milli);
        }

        @Override
        public void onFinish() {
            try {
                mTvOveredTips.setVisibility(View.VISIBLE);
                mTvOveredTips.setText("本场已结束");
                headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
                mFloatHeadView.findViewById(R.id.tv_overed_tips).setVisibility(View.VISIBLE);
                mFloatHeadView.findViewById(R.id.ll_top1).setVisibility(View.GONE);
                ((TextView) mFloatHeadView.findViewById(R.id.tv_overed_tips)).setText("本场已结束");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateCountDownTime(int hour, int min, int sec, int milli) {
        setHourTv(mTvHH, mTvH, mTvDay, mTvDayDesc, hour);
//        setTv(mTvHH, mTvH, hour);
        setTv(mTvMM, mTvM, min);
        setTv(mTvSS, mTvS, sec);
        mTvF.setText(milli / 100 + "");

        setHourTv(mTvFHH, mTvFH, mTvFDay, mTvFDayDesc, hour);
//        setTv(mTvFHH, mTvFH, hour);
        setTv(mTvFMM, mTvFM, min);
        setTv(mTvFSS, mTvFS, sec);
        mTvFF.setText(milli / 100 + "");
    }

    private void setHourTv(TextView tvHH, TextView tvH, TextView tvDay, TextView tvDayDesc, int time) {
        int aa = time / 10;
        if (time < 24) {
            int a = time % 10;
            tvHH.setText(aa + "");
            tvH.setText(a + "");
            tvDay.setVisibility(View.GONE);
            tvDayDesc.setVisibility(View.GONE);
        } else {
            tvDay.setVisibility(View.VISIBLE);
            tvDayDesc.setVisibility(View.VISIBLE);
            int hours = time % 24;
            setTv(tvHH, tvH, hours);
            tvDay.setText((time / 24) + "");
        }

    }

    private void setTv(TextView tvAA, TextView tvA, int time) {
        int aa = time / 10;
        int a = time % 10;
        tvAA.setText(aa + "");
        tvA.setText(a + "");
    }

//    public void search(String keyword) {
//        //mPinHuoModel.keyword = keyword;
//        mNeedToClearKeyword = false;
//        mRefreshLayout.manualRefresh();
//    }


    // 根据userid获取首页数据
    private class GetShopCategoriesTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingDialog.start(getString(R.string.items_loadData_loading));
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                mItemShopCategories = ShopSetAPI
                        .getItemCatsByShopID(mAppContext, mRecommendModel.getInfo().getShopID());
                return "OK";
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mAppContext, ((String) result).replace("error:", ""));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
            onDetach();
            mSortIndex = Const.SortIndex.DefaultDesc;
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            }
        } catch (Exception e) {
            mLoadingDialog = null;
        }
    }

    // 返回键按下时会被调用
    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(drawer_content)) {
                mDrawerLayout.closeDrawer(drawer_content);
            } else {
                if (JCVideoPlayer.backPress()) {
                    return;
                }
                super.onBackPressed();

            }
        } else {
            if (JCVideoPlayer.backPress()) {
                return;
            }
            super.onBackPressed();
        }

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
        model.sortType_isNew = sortType_isNew;
        if (!ListUtils.isEmpty(sortMenus))
            mData.add(model);
    }
    private void showEmptyView() {
        ShopItemListModel model = new ShopItemListModel();
        model.setItem_layout_type(ShopItemListModel.TYPE_EMPTY_MID);
        model.setEmpty_txt("没有找到档口数据-。。-");
        mData.add(model);
        setNoMore();
        if (mLoadMoreTxt!=null)
        mLoadMoreTxt.setVisibility(View.GONE);
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

}

