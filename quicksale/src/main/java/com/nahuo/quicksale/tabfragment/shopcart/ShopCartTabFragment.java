package com.nahuo.quicksale.tabfragment.shopcart;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.internal.LinkedTreeMap;
import com.luck.picture.lib.dialog.PictureDialog;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.bean.ColorPicsBean;
import com.nahuo.bean.DiscountBean;
import com.nahuo.bean.Level0Item;
import com.nahuo.bean.Level1Item;
import com.nahuo.bean.Level2Item;
import com.nahuo.bean.NoticeBean;
import com.nahuo.bean.ShopCartBean;
import com.nahuo.bean.ShopCartItemBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListView;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.BuildConfig;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.SubmitOrderActivity;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ItemPreview1Activity;
import com.nahuo.quicksale.activity.MainNewActivity;
import com.nahuo.quicksale.adapter.ShopcartAdapter;
import com.nahuo.quicksale.adapter.ShopcartAdapter.IGoodItemOnClickListener;
import com.nahuo.quicksale.adapter.ShopcartAdapter.TotalPriceChangedListener;
import com.nahuo.quicksale.adapter.ShopcartNewAdapter;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.OrderAPI;
import com.nahuo.quicksale.api.RequestMethod.ShopCartMethod;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.TabFragment;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.controls.ShopCartSelectSizeColorMenu;
import com.nahuo.quicksale.countdownutils.CountDownTask;
import com.nahuo.quicksale.customview.CancelDialog;
import com.nahuo.quicksale.customview.MarqueeTextView;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.GoodBaseInfo;
import com.nahuo.quicksale.oldermodel.ProductModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopCart.ShopcartItem;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.oldermodel.TempOrderV2;
import com.nahuo.quicksale.tab.Constant;
import com.nahuo.quicksale.util.ActivityUtil;
import com.nahuo.quicksale.util.JsonKit;
import com.nahuo.quicksale.util.RxUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.R.id.iv_scroll_top;
import static com.nahuo.quicksale.api.RequestMethod.ShopCartMethod.GET_ITEMS_FOR_ALL_SHOP3;

/**
 * @author JorsonWong
 * @description 购物车Activity
 * @created 2015年4月1日 上午11:03:38
 */
public class ShopCartTabFragment extends TabFragment implements OnClickListener, OnGroupClickListener,
        OnChildClickListener, ShopCartSelectSizeColorMenu.Listener, TotalPriceChangedListener, IGoodItemOnClickListener, HttpRequestListener, ShopcartNewAdapter.ShopCartOnClickListener {
    protected static final String TAG = ShopCartTabFragment.class.getSimpleName();
    public static final String ETRA_LEFT_BTN_ISHOW = "ETRA_LEFT_BTN_ISHOW";
    // private PullToRefreshExpandableListView mPullExListView;
    private CheckBox mCbSelectAll;
    private TextView mTvTotalPrice;
    private ShopcartAdapter mAdapter;
    private ShopcartNewAdapter adapter;
    private RecyclerView mExpListView;
    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();
    private View mEmptyView;
    private TextView mTvEmpty;
    private View mBottomView;
    private View nBottomLine;
    private ImageView mIvScroll2Top;
    private EventBus mEventBus = EventBus.getDefault();

    private int mChangeCountGroupPosition = -1;

    private int mChangeCountChildPosition = -1;

    private int mPreChangeCount = 1;

    private Activity vthis;

    private TextView btnRight;
    private CountDownTask mCountDownTask;
    private Context mContext;
    //添加一个空接口（解决框架的bug）
    private PullToRefExpandListView.EmptyInterface emptyInterface;

    private static final int DELETE_SELECT = 1;//删除选中商品

    private static final int CLEAR_ALL = 2;//清空所有失效商品

    private MarqueeTextView tv_marquee;

    private List<NoticeBean> list = new ArrayList<>();

    private ShopCartTabFragment Vthis = this;

    private List<String> slist = new ArrayList<>();

    private RelativeLayout shopcart_rela;
    protected LoadingDialog mLoadingDialog;
    private String url = "";
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
    private TextView tvTitleCenter, tvTLeft, invalid_shop_clear;
    private View layout_manage;
    private boolean is_open_manage = false;
    private Animation mShowAction, mHiddenAction;
    private View ll_notifi;
    private TextView tv_look, tv_cancel, tv_price_botom;
    private DiscountBean discountBean;
    private String Url = "";
    public boolean is_show_left_btn = false;
    protected PictureDialog dialog;
    private SwipeRefreshLayout refresh_layout;
    private Button btn_buyer;

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        dismissDialog();
        dialog = new PictureDialog(vthis);
        dialog.show();
    }


    View mContentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vthis = getActivity();
    }

    private PinHuoApi pinHuoApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.activity_new_shopcart, container, false);
        initTitleBar();
        initViews();
        init();
        load();
        return mContentView;
    }

    private void startCountDown() {
        mCountDownTask = CountDownTask.create();
        adapter.setCountDownTask(mCountDownTask);
    }

    private void cancelCountDown() {
        adapter.setCountDownTask(null);
        mCountDownTask.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        MainNewActivity.currFragTag = Constant.FRAGMENT_FLAG_SHOP_CART;
        startCountDown();

    }

    @Override
    public void onPause() {
        super.onPause();
        cancelCountDown();
    }

    private void showNotifyAnimation(boolean isShow) {
        if (isShow) {
            if (ll_notifi.getVisibility() != View.VISIBLE) {
                ll_notifi.setVisibility(View.VISIBLE);
                Animation anim = AnimationUtils.loadAnimation(vthis, R.anim.bottom_menu_appear);
                ll_notifi.startAnimation(anim);
            }
        } else {
         //   if (ll_notifi.getVisibility() == View.VISIBLE) {
                Animation anim = AnimationUtils.loadAnimation(vthis, R.anim.botttom_menu_disppearx);
                ll_notifi.startAnimation(anim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ll_notifi.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
           // }
        }

    }

    GridLayoutManager manager;

    private void initViews() {
        // BWApplication.addActivity(this);
        mLoadingDialog = new LoadingDialog(vthis);
        url = Const.getShopLogo(SpManager.getUserId(BWApplication.getInstance()));
        mEmptyView = mContentView.findViewById(R.id.empty_view);
        mContentView.findViewById(android.R.id.button2).setOnClickListener(this);
        // mEmptyView.setOnClickListener(this);
        btn_buyer = (Button) mContentView.findViewById(android.R.id.button1);
        btn_buyer.setOnClickListener(this);
        refresh_layout = (SwipeRefreshLayout) mContentView.findViewById(R.id.refresh_layout);
        refresh_layout.setRefreshing(true);
        // 设置下拉进度的背景颜色，默认就是白色的
        refresh_layout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // 设置下拉进度的主题颜色
        // refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshData();
            }
        });
        mExpListView = (RecyclerView) mContentView.findViewById(R.id.lv_shopcart);
        //   mExpListView.setEmptyView(mEmptyView);
        mTvEmpty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
        ll_notifi = mContentView.findViewById(R.id.ll_notifi);
        ll_notifi.setOnClickListener(this);
        tv_look = (TextView) mContentView.findViewById(R.id.tv_look);
        tv_cancel = (TextView) mContentView.findViewById(R.id.tv_cancel);
        tv_price_botom = (TextView) mContentView.findViewById(R.id.tv_price_botom);
        tv_cancel.setOnClickListener(this);
        //mTvEmpty.setText(Html.fromHtml(getString(R.string.shopcart_empty_text)));
        tv_marquee = (MarqueeTextView) mContentView.findViewById(R.id.gotoYunFei);
        //tv_marquee.setOnClickListener(this);
        tv_marquee.setVerticalSwitchSpeed(1000);
        tv_marquee.setHorizontalScrollSpeed(200);
        tv_marquee.setOnItemOnClickListener(new MarqueeTextView.OnItemClickListener() {
            @Override
            public void onItemclick(int index) {
                // Log.v(TAG, "index" + index);
                NoticeBean bean = list.get(index);
                String target = bean.getTarget();
                if (!TextUtils.isEmpty(target)) {
//                    Intent intent = new Intent(vthis, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
//                    intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
//                            url);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
//                    if (target.equals("activity")) {
//                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
//                                Const.PostType.ACTIVITY);
//                    }
//                    if (target.equals("topic")) {
//                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
//                                Const.PostType.TOPIC);
//                    }
//                    Vthis.startActivity(intent);
                    switch (target){
                        case "activity":
                            Intent intent = new Intent(vthis, PostDetailActivity.class);
                            intent.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                            intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                    url);
                            intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                            intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                    Const.PostType.ACTIVITY);
                            Vthis.startActivity(intent);
                            break;
                        case "topic":
                            Intent intent1 = new Intent(vthis, PostDetailActivity.class);
                            intent1.putExtra(PostDetailActivity.EXTRA_TID, bean.getTargetID());
                            intent1.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                                    url);
                            intent1.putExtra(PostDetailActivity.EXTRA_POST_TITLE, bean.getTitle());
                            intent1.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                                    Const.PostType.TOPIC);
                            Vthis.startActivity(intent1);
                            break;
                        case "schedule":
                            ActivityUtil.goToChangCiActivity(vthis,bean.getTargetID());
                            break;
                        case "goods":
                            ActivityUtil.goToItemDtailActivity(vthis,bean.getTargetID());
                            break;
                    }
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusBarHeight(vthis));
            mContentView.findViewById(R.id.layout_tittle_status).setLayoutParams(params);
            mContentView.findViewById(R.id.layout_tittle_status).setVisibility(View.VISIBLE);
        }
        /*List<String> all=new ArrayList<String>();
        all.add("\t\t运费怎么算的？");
        all.add("\t\t怎么付款了没货发？");
        tv_marquee.setContentList(all);*/
        //tv_marquee.startScroll();
        mTvEmpty.setOnClickListener(this);
//        mExpListView.setOnRefreshListener(this);
//        // mExpListView = (ExpandableListView)mPullExListView.getRefreshableView();
//        mExpListView.setOnGroupClickListener(this);
//        mExpListView.setOnChildClickListener(this);
        mCbSelectAll = (CheckBox) mContentView.findViewById(android.R.id.checkbox);
        mCbSelectAll.setOnClickListener(this);
        mTvTotalPrice = (TextView) mContentView.findViewById(android.R.id.text1);
        mBottomView = mContentView.findViewById(android.R.id.inputArea);
        nBottomLine = mContentView.findViewById(R.id.line);
        shopcart_rela = (RelativeLayout) mContentView.findViewById(R.id.shopcart_rela);
        mBottomView.setVisibility(View.INVISIBLE);
        mIvScroll2Top = (ImageView) mContentView.findViewById(R.id.iv_scroll_top);
        manager = new GridLayoutManager(vthis, 1);
        mExpListView.setLayoutManager(manager);
        mExpListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int pos = manager.findFirstVisibleItemPosition();
//                if (pos > 2 && mIvScroll2Top.getVisibility() != View.VISIBLE) {
//                    mIvScroll2Top.setVisibility(View.VISIBLE);
//                } else if (pos <= 2 && mIvScroll2Top.getVisibility() != View.GONE) {
//                    mIvScroll2Top.setVisibility(View.GONE);
//                }
            }
        });
//        mExpListView.setOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                ((PullToRefExpandListView) mExpListView).setEmptyInterface(new PullToRefExpandListView.EmptyInterface() {
//                    @Override
//                    public void OnEmpty() {
//                        //空方法
//                    }
//                });
//                mIvScroll2Top.setVisibility(firstVisibleItem > 0 ? View.VISIBLE : View.GONE);
//            }
//        });
//        mExpListView.showLoadingView();

    }

    private void setRefreshFalse() {
        if (refresh_layout != null)
            refresh_layout.setRefreshing(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mEventBus.registerSticky(this);
    }

    private void init() {

        mAdapter = new ShopcartAdapter(vthis);
        adapter = new ShopcartNewAdapter(null);
        adapter.setmListener(this);
        mAdapter.setTextView(invalid_shop_clear);

        // mExpListView.setAdapter(mAdapter);
        mTvTotalPrice.setText(getString(R.string.rmb_x, Utils.moneyFormat(0)));
        mAdapter.setTotalPriceChangedListener(this);
        mAdapter.setIGoodItemOnClickListener(this);
    }

    public void showManageLayout() {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout_manage.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mHiddenAction.setDuration(500);
        if (is_open_manage) {
            layout_manage.setVisibility(View.VISIBLE);
            layout_manage.setAnimation(mShowAction);

        } else {
            layout_manage.setAnimation(mHiddenAction);

        }
    }

    List<Level2Item> selecteDelIds = new ArrayList<>();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_notifi:
                if (!TextUtils.isEmpty(Url)) {
                    Intent intent = new Intent(vthis, ItemPreview1Activity.class);
                    intent.putExtra("name", "活动");
                    intent.putExtra("url", Url);
                    startActivity(intent);
                }
                break;
            case R.id.tv_cancel:
                showNotifyAnimation(false);
                break;
            case R.id.tvTLeft:
                // finish();
                break;
            /*case R.id.gotoYunFei: {
                Intent intent = new Intent(this, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, 93255);
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                this.startActivity(intent);
                break;
            }*/
            case android.R.id.button1:// 结算
                //List<ShopcartItem> selecteds = mAdapter.getSelectedShopcartItems();
                List<Level2Item> selecteds = adapter.getSelectedShopcartItems();
                if (!ListUtils.isEmpty(selecteds)) {
                    creatTempOrder(selecteds);
                } else if (!adapter.hasSelectedIsNotAvailableItems()) {
                    ViewHub.showShortToast(vthis, getString(R.string.select_nothing));
                } else {
                    new CancelDialog(vthis, vthis.getResources().getString(R.string.shopcart_dialog)).show();
                }
                break;
            case android.R.id.button2:// 删除
                //  final List<ShopcartItem> selecteIds = mAdapter.getSelectedShopcartItems();
                List<Level2Item> selecteIds = adapter.getSelectedShopcartItems();
                selecteDelIds.clear();
                selecteDelIds.addAll(selecteIds);
                if (!ListUtils.isEmpty(selecteDelIds)) {
                    ViewHub.showOkDialog(vthis, "提示", "您确定要删除所选商品吗？", getString(android.R.string.ok),
                            getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //removeItems(selecteIds);
                                    deleteShopCart(selecteDelIds, DELETE_SELECT);
                                }
                            });
                } else {
                    ViewHub.showShortToast(vthis, getString(R.string.select_nothing));
                }
                break;
            case android.R.id.checkbox:
                //  mAdapter.setSelectAll(mCbSelectAll.isChecked());
                if (adapter != null)
                    adapter.setSelectAll(mCbSelectAll.isChecked());
                break;
            case R.id.tv_empty:
                EventBus.getDefault().postSticky(
                        BusEvent.getEvent(EventBusId.MAIN_CHANGE_SHOPCAT));
              //  EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.ALL_ITEM_CHANGE_CURRENT_TAB, 0));

//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
 //               ActivityUtil.goToMainActivity(vthis);
                break;
            case iv_scroll_top:
                mExpListView.scrollToPosition(0);
                break;
            case R.id.tvTRight:
                if (is_open_manage) {
                    is_open_manage = false;
                    btnRight.setText("管理");
                } else {
                    btnRight.setText("取消");
                    is_open_manage = true;
                }
                showManageLayout();
                break;
            case R.id.invalid_shop_clear:
                ViewHub.showOkDialog(vthis, "提示",
                        "确定要清空已失效的商品吗？", getString(android.R.string.ok),
                        getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //List<ShopcartItem> deleteAllLostItem = mAdapter.getLoseShopCart();
                                if (adapter != null) {
                                    List<Level2Item> deleteAllLostItem = adapter.getLoseShopCart();
                                    if (!ListUtils.isEmpty(deleteAllLostItem)) {
                                        deleteShopCart(deleteAllLostItem, CLEAR_ALL);
                                    }
                                }
                            }
                        });
                break;
            default:
                break;
        }

    }

    private void initTitleBar() {
        is_show_left_btn = true;
        layout_manage = mContentView.findViewById(R.id.layout_manage);
        tvTLeft = (TextView) mContentView.findViewById(R.id.tvTLeft);
        tvTLeft.setOnClickListener(this);
        if (is_show_left_btn) {
            tvTLeft.setVisibility(View.INVISIBLE);
        } else {
            tvTLeft.setVisibility(View.VISIBLE);

        }
        invalid_shop_clear = (TextView) mContentView.findViewById(R.id.invalid_shop_clear);
        invalid_shop_clear.setOnClickListener(this);
        tvTitleCenter = (TextView) mContentView.findViewById(R.id.tvTitleCenter);
        tvTitleCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FunctionHelper.isDoubleFastClick()) {
                    if (mExpListView != null)
                        mExpListView.scrollToPosition(0);
                }
            }
        });
        tvTitleCenter.setText(R.string.shopping_cart);
        btnRight = (TextView) mContentView.findViewById(R.id.tvTRight);
        btnRight.setText(getString(R.string.manage_shop_car));
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);
    }

    private void removeItems(List<ShopcartItem> items) {
        StringBuilder itemIds = new StringBuilder();
        StringBuilder tags = new StringBuilder();
        for (ShopcartItem item : items) {
            itemIds.append(item.AgentItemID).append(",");
            tags.append(item.Tag).append(",");
        }
        HttpRequest request = mRequestHelper.getRequest(vthis, ShopCartMethod.REMOVE_ITEMS, this);
        request.addParam("itemIds", StringUtils.deleteEndStr(itemIds.toString(), ","));
        request.addParam("tags", StringUtils.deleteEndStr(tags.toString(), ","));
        request.doPost();

    }

    //拼接删除订单
    private void deleteShopCart(List<Level2Item> items, int type) {

        try {
            List<Integer> ids = new ArrayList<>();
            if (!ListUtils.isEmpty(items)) {
                for (Level2Item level2 : items) {
                    ids.add(level2.getAgentItemID());
                }
              //  new DeleteShopCartTask(ids, type).execute();
                deleteOrClearShopCart(ids,type);
            }

//            final JSONArray ja = new JSONArray();
//            for (ShopcartItem om : items) {
//                if (items.size() > 0) {
//                    JSONObject jo = new JSONObject();
//                    jo.put("AgentItemID", om.AgentItemID);
//                    jo.put("Color", om.Color);
//                    jo.put("Size", om.Size);
//                    jo.put("Qty", om.Qty);
//                    ja.put(jo);
//                }
//            }
//            if (ja != null && ja.length() > 0) {
//                new DeleteShopCartTask(ja, type).execute();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void deleteOrClearShopCart(List<Integer> ids, final int type){
        String idsStr = "";
        idsStr = ids.toString().substring(1, ids.toString().length() - 1);
        addSubscribe(HttpManager.getInstance().getPinHuoNetCacheApi(TAG)
        .deleteShopCart(idsStr).compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
        .compose(RxUtil.handleResult())
        .subscribeWith(new CommonSubscriber<Object>(vthis,true,R.string.loading_shop_cart_del){
            @Override
            public void onNext(Object o) {
                super.onNext(o);
                isHideClearTitle();
                switch (type) {
                    case DELETE_SELECT:
                        //mAdapter.deleteSelectedItems();
                        adapter.deleteSelectedItems();
                        mCbSelectAll.setChecked(false);
                        mBottomView.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        nBottomLine.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case CLEAR_ALL:
                        // mAdapter.deleteLoseItems();
                        adapter.deleteLoseItems();
                        mBottomView.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        nBottomLine.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        isHideClearTitle();

                        break;
                    default:
                }
                judeEmpty();
            }
        }));
    }
    protected boolean isDialogShowing() {
        return (mLoadingDialog != null && mLoadingDialog.isShowing());
    }

    protected void hideDialog() {
        if (isDialogShowing()) {
            mLoadingDialog.stop();
        }
    }

    int mPos;

    /**
     * adpter
     *
     * @author James Chen
     * @create time in 2018/2/24 11:37
     */
    @Override
    public void OnEditClick(Level2Item level2Item, int pos) {
        mPos = pos;
        if (FunctionHelper.isDoubleFastClick())
            return;
        //new ItemDetailTask(level2Item).execute();
        getItemDetailData(level2Item);
    }

    private void getItemDetailData(final Level2Item level2Item) {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getPinhuoDetail(level2Item.getAgentItemID(), 0)
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult())
                .subscribeWith(new CommonSubscriber<Object>(vthis, true, R.string.loading) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        try {
                            LinkedTreeMap map = (LinkedTreeMap) object;
                            String json = JsonKit.mapToJson(map, null).toString();
                            ShopItemModel bean = getDetailResult(json);
                            initColorSizeMap(bean.getProducts(), level2Item);
                            showBuyPopup(bean, level2Item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    private ShopItemModel getDetailResult(String json) throws JSONException {
        ShopItemModel result = null;
        List<ProductModel> list = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        List<ColorPicsBean> colorPicsList = new ArrayList<>();
        if (list != null)
            list.clear();
        JSONArray jsonArray = jsonObject.optJSONArray("Products");
        double price = 0.0;
        String sPrice = jsonObject.optString("Price");
        String MainColorPic = "";
        MainColorPic = jsonObject.optString("MainColorPic");
        if (!TextUtils.isEmpty(MainColorPic)) {
            ColorPicsBean colorPicsBean = new ColorPicsBean();
            colorPicsBean.setUrl(MainColorPic);
            colorPicsBean.setColor("");
            colorPicsList.add(colorPicsBean);
        }
        if (!TextUtils.isEmpty(sPrice))
            price = Double.parseDouble(sPrice);
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject1 = new JSONObject(jsonArray.get(i).toString());
            String color = jsonObject1.optString("Color");
            String colorPic = jsonObject1.optString("ColorPic");
            if (!TextUtils.isEmpty(colorPic)) {
                ColorPicsBean colorPicsBean = new ColorPicsBean();
                colorPicsBean.setColor(color);
                colorPicsBean.setUrl(colorPic);
                colorPicsList.add(colorPicsBean);
            }
            JSONObject jsonObject2 = new JSONObject(jsonObject1.optString("Sizes").toString());
            Iterator it = jsonObject2.keys();
            while (it.hasNext()) {
                ProductModel productModel = new ProductModel();
                productModel.setColor(color);
                productModel.setColorPic(colorPic);
                String key = (String) it.next();
                if (key != null) {
                    productModel.setSize(key.toString());
                    int value = jsonObject2.optInt(key.toString());
                    productModel.setStock(value);
                }
                list.add(productModel);
            }

        }
        result = GsonHelper.jsonToObject(json, ShopItemModel.class);
        result.setProducts(list);
        result.setRetailPrice(price);
        result.setColorPicsBeanList(colorPicsList);
        return result;
    }

    @Override
    public void OnChangeClick(boolean IsAvailable) {
        changData(IsAvailable);

    }

    private void changData(boolean IsAvailable) {
        try {
            isHideClearTitle();
            int total = 0;
            if (adapter != null) {
                List<MultiItemEntity> data = adapter.getListData();
                if (!ListUtils.isEmpty(data)) {
                    boolean isAllCheck = true;
                    JSONArray jsArray = new JSONArray();
                    for (MultiItemEntity entity : data) {
                        if (entity instanceof Level2Item) {
                            Level2Item level2Item = (Level2Item) entity;
                            if (level2Item.isSelect && level2Item.isAvailable()) {
                                JSONObject jsonObj = new JSONObject();
                                jsonObj.put("AgentItemID", level2Item.getAgentItemID());
                                jsArray.put(jsonObj);
                                total += level2Item.getTotalQty();
                            }
//                            if (!level2Item.isSelect && level2Item.isAvailable())
//                                isAllCheck = false;
                            if (!level2Item.isSelect)
                                isAllCheck = false;
                        }

                    }
                    if (isAllCheck) {
                        mCbSelectAll.setChecked(true);
                    } else {
                        mCbSelectAll.setChecked(false);
                    }
                    if (jsArray.length() > 0) {
                        if (IsAvailable) {
//                            HttpRequest request = mHttpRequestHelper.getRequest(vthis, ShopCartMethod.GET_ITEM_DISCOUNT_FOR_ALL_SHOP,
//                                    this);
//                            request.addParam("itemInfos", jsArray.toString());
//                            request.addParam("version", "2");
//                            request.setConvert2Token(new TypeToken<DiscountBean>() {
//                            });
//                            request.doPost();
                            postDiscount(jsArray.toString(), com.nahuo.constant.Constant.ShopCartVersion.Version_2);
                        }
                    } else {
                        showNotifyAnimation(false);
                        mTvTotalPrice.setText("¥0.00");
                        tv_price_botom.setText(R.string.freight_not_included);
                    }

                } else {
                    showNotifyAnimation(false);
                    mTvTotalPrice.setText("¥0.00");
                    tv_price_botom.setText(R.string.freight_not_included);
                }

            }
            if (total > 0) {
                btn_buyer.setText("结算(" + total + ")");
            } else {
                btn_buyer.setText("结算");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<String> mColors = new LinkedHashSet<String>();
    private Set<String> mSizes = new LinkedHashSet<String>();
    private Map<String, String> mSizeColorMap = new HashMap<String, String>();
    private Map<String, String> mColorSizeMap = new HashMap<String, String>();
    private ShopItemModel mShopItem;

    @Override
    public void editSucess(List<Level2Item.ProductsBean> Products, int total) {
        if (adapter != null)
            adapter.setEditItem(mPos, Products, total);
        changData(true);
    }

    private class ItemDetailTask extends AsyncTask<Object, Void, Object> {
        Level2Item level2Item;

        public ItemDetailTask(Level2Item level2Item) {
            super();
            this.level2Item = level2Item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mLoadingDialog == null)
                mLoadingDialog = new LoadingDialog(vthis);
            if (!isDialogShowing()) {
                mLoadingDialog.setMessage("获取详情数据...");
                mLoadingDialog.show();
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                mShopItem = BuyOnlineAPI.getInstance().getPiHuoItemDetailNew(level2Item.getAgentItemID(), 0, PublicData.getCookie(mContext));
                return mShopItem;
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }


        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            try {
                if (isDialogShowing()) {
                    mLoadingDialog.stop();
                }
                if (result instanceof String && ((String) result).startsWith("error:")) {
                    ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
                }
                ShopItemModel bean = (ShopItemModel) result;
                initColorSizeMap(bean.getProducts(), level2Item);
                showBuyPopup(bean, level2Item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @description 检查商品规格
     * @created 2014-10-23 下午9:14:19
     * @author ZZB
     */
    private void showBuyPopup(ShopItemModel mShopItemModel, Level2Item level2Item) {
        // Log.i("ItemDetail", "ApplyId:" + mShopItemModel.getApplyStatuID());
        GoodBaseInfo baseInfo = new GoodBaseInfo(mShopItemModel.getItemID(), mShopItemModel.getName(),
                mShopItemModel.getIntro(), mShopItemModel.getMainColorPic(), mShopItemModel.getRetailPrice(),
                mShopItemModel.getPrice(), mShopItemModel.getApplyStatuID());
        Iterator<String> it1 = mSizes.iterator();
        while (it1.hasNext()) {
            String str = it1.next();
            Log.i(getClass().getSimpleName(), "size:" + str);
            baseInfo.addSize(str);
        }
        Iterator<String> it = mColors.iterator();
        while (it.hasNext()) {
            String str = it.next();
            Log.i(getClass().getSimpleName(), "color:" + str);
            baseInfo.addColor(str);
        }

        for (ProductModel pm : mShopItemModel.getProducts()) {
            if (pm.getStock() > 0) {
                baseInfo.addProduct(pm);
            }
        }
        baseInfo.setColorPicsBeanList(mShopItemModel.getColorPicsBeanList());

        ShopCartSelectSizeColorMenu menu = new ShopCartSelectSizeColorMenu(vthis, baseInfo);
        menu.setLevel2Item(level2Item);
        menu.setmListener(this);
        menu.setSelectMenuDismissListener(new ShopCartSelectSizeColorMenu.SelectMenuDismissListener() {
            @Override
            public void dismissStart(long duration) {
                ScaleAnimation animation = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);//ScaleAnimation缩放动画
                animation.setFillAfter(true);
                animation.setDuration(duration);
                //  mRootView.startAnimation(animation);
            }

            @Override
            public void dismissEnd() {
            }
        });
        menu.show();
//
//        ScaleAnimation animation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setFillAfter(true);
//        animation.setDuration(300);


    }

    private void initColorSizeMap(List<ProductModel> specs, Level2Item level2Item) {
        mColors.clear();
        mSizes.clear();
        for (ProductModel pm : specs) {
            if (level2Item != null) {
                if (!ListUtils.isEmpty(level2Item.getProducts())) {
                    for (int j = 0; j < level2Item.getProducts().size(); j++) {
                        Level2Item.ProductsBean prm = level2Item.getProducts().get(j);
                        if (pm.getColor().equals(prm.getColor()) && pm.getSize().equals(prm.getSize())) {
                            pm.setQty(prm.getQty());
                        }
                    }
                }
            }
            if (pm.getStock() > 0) {
                String color = pm.getColor();
                String size = pm.getSize();
                mColors.add(color);
                mSizes.add(size);

                // color对应的尺码
                String sizes = mColorSizeMap.get(color);
                sizes += size + ",";
                mColorSizeMap.put(color, sizes);

                // 尺码对应的颜色
                String colors = mSizeColorMap.get(size);
                colors += color + ",";
                mSizeColorMap.put(size, colors);
            }
        }
    }

    private class DeleteShopCartTask extends AsyncTask<Void, Void, String> {
        private List<Integer> ids;
        //type 类型两种1(删除所选商品),2(删除所有商品)
        private int type = -1;

        public DeleteShopCartTask(List<Integer> ids, int type) {
            this.ids = ids;
            this.type = type;
        }

        private String json;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (!isDialogShowing()) {
                mLoadingDialog.setMessage("正在删除...");
                mLoadingDialog.show();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                // json = HttpsUtils.httpPost(ShopCartMethod.DELETE_ITEMS, ids.toString(), PublicData.getCookie(ShopCartNewActivity.this));
                return BuyOnlineAPI.getInstance().deleteShopCartItem(ids, vthis);
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (isDialogShowing()) {
                mLoadingDialog.stop();
            }
            if (result.equals("error")) {
                // 验证result
                if (result.startsWith("401") || result.startsWith("not_registered")) {
                    Toast.makeText(vthis, result, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(vthis, result, Toast.LENGTH_LONG).show();
                }
            } else {
                //解析result通知更新
                isHideClearTitle();
                switch (type) {
                    case DELETE_SELECT:
                        //mAdapter.deleteSelectedItems();
                        adapter.deleteSelectedItems();
                        mCbSelectAll.setChecked(false);
                        mBottomView.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        nBottomLine.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        break;
                    case CLEAR_ALL:
                        // mAdapter.deleteLoseItems();
                        adapter.deleteLoseItems();
                        mBottomView.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        nBottomLine.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
                        isHideClearTitle();

                        break;
                    default:
                }
                judeEmpty();
            }
        }
    }

    private void load() {
//        HttpRequest request = mRequestHelper.getRequest(vthis, GET_ITEMS_FOR_ALL_SHOP3, this);
//        if (Const.DEBUG) {
//            request.addParam("debug", "1");
//        }
//        request.setConvert2Class(ShopCartBean.class);
//        request.doPost();
        getItems3();
        getCurrentNotice();
        // new AdsTask().execute();

    }

    private void getItems3() {
        String debug = "";
        if (BuildConfig.DEBUG) {
            debug = "1";
        } else {
            debug = null;
        }
        pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getItems3(debug).
                compose(RxUtil.<PinHuoResponse<ShopCartBean>>rxSchedulerHelper())
                .compose(RxUtil.<ShopCartBean>handleResult()).subscribeWith(new CommonSubscriber<ShopCartBean>(vthis) {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (!isHidden())
                            showPleaseDialog();
//                        if (mLoadingDialog == null)
//                            mLoadingDialog = new LoadingDialog(vthis);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissDialog();
                        setRefreshFalse();
                        hideDialog();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissDialog();
                        setRefreshFalse();
                        hideDialog();
                    }

                    @Override
                    public void onNext(ShopCartBean shopCartBean) {
                        super.onNext(shopCartBean);
                        setRefreshFalse();
                        hideDialog();
                        dismissDialog();
                        onDataLoaded(shopCartBean);
                        isHideClearTitle();
                    }
                }));
    }

    private void getCurrentNotice() {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getCurrentNotice(com.nahuo.constant.Constant.NotifyAreaID.NotifyAreaID_ShopCart)
                .compose(RxUtil.<PinHuoResponse<List<NoticeBean>>>rxSchedulerHelper())
                .compose(RxUtil.<List<NoticeBean>>handleResult())
                .subscribeWith(new CommonSubscriber<List<NoticeBean>>(vthis) {
                    @Override
                    public void onNext(List<NoticeBean> noticeBeen) {
                        super.onNext(noticeBeen);
                        // Animation animation = null;
                        list.clear();
                        List<NoticeBean> data = noticeBeen;
                        if (!ListUtils.isEmpty(data)) {
                            slist.clear();
                        }
                        if (data != null && data.size() > 0) {
                            //  animation = AnimationUtils.loadAnimation(vthis, R.anim.slide_bottom_in);
                            if (shopcart_rela != null)
                                shopcart_rela.setVisibility(View.VISIBLE);
                            // ll_notifi.startAnimation(animation);
                            for (int i = 0; i < data.size(); i++) {
                                NoticeBean bean = data.get(i);
                                bean.setNums_content(i + 1 + ": " + bean.getTitle());
                                list.add(bean);
                                slist.add(i + 1 + ": " + bean.getTitle());
                            }
                            if (tv_marquee != null)
                                tv_marquee.setContentList(slist);
                            //tv_notifi_auto.startFlipping();
                            // marqueeFactory.resetData(list);
                        } else {
                            //  animation = AnimationUtils.loadAnimation(vthis, R.anim.slide_bottom_out);
                            // ll_notifi.startAnimation(animation);
                            shopcart_rela.setVisibility(View.GONE);
                        }
                    }
                }));
    }

    private String postJsonChuan = "";

    private void creatTempOrder(List<Level2Item> items) {
        try {
//            StringBuilder postJson = new StringBuilder();
//            postJson.append("[");
            JSONArray jsonArray = new JSONArray();
            for (Level2Item level2Item : items) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("AgentItemID", level2Item.getAgentItemID());
                jsonArray.put(jsonObject);
            }
//            for (ShopcartItem item : items) {
//                if (item.IsAvailable == true) {
//                    postJson.append("{");
//                    postJson.append("AgentItemID:'" + item.AgentItemID + "',");
//                    postJson.append("Color:'" + item.Color + "',");
//                    postJson.append("Size:'" + item.Size + "',");
//                    postJson.append("Qty:'" + item.Qty + "'");
//                    postJson.append("},");
//                }
//            }
            if (jsonArray.length() > 0) {
                // postJson = new StringBuilder(postJson.substring(0, postJson.length() - 1));
                String postJsonStr = jsonArray.toString();
//                HttpRequest request = mHttpRequestHelper.getRequest(vthis, ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP,
//                        this);
//                request.addParam("itemInfos", postJsonStr);
//                request.addParam("version", "3");
//                request.setConvert2Token(new TypeToken<TempOrderV2>() {
//                });
//                request.doPost();
                postOrderTemp(postJsonStr, com.nahuo.constant.Constant.ShopCartVersion.Version_3);
                postJsonChuan = postJsonStr;
            } else {
                new CancelDialog(vthis, vthis.getResources().getString(R.string.shopcart_dialog)).show();

            }
            // postJson.append("]");

            //判断是否全是失效商品
//            String postJsonStr = postJson.toString();
//            if (items.size() > 0 && ((!postJsonStr.equals("]")) && (!postJsonStr.equals("[")) && (!postJsonStr.equals("[]")))) {
//                //只结算有效商品
//                HttpRequest request = mHttpRequestHelper.getRequest(this, ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP,
//                        this);
//                request.addParam("itemInfos", postJson.toString());
//                request.addParam("version", "2");
//                request.setConvert2Token(new TypeToken<TempOrderV2>() {
//                });
//                request.doPost();
//                postJsonChuan = postJson.toString();
//            } else if (items.size() > 0 && (postJsonStr.equals("]") || postJsonStr.equals("[") || postJsonStr.equals("[]"))) {
//                //全是失效商品
//                new CancelDialog(vthis, vthis.getResources().getString(R.string.shopcart_dialog)).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postOrderTemp(String itemInfos, int version) {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.createTempOrder(itemInfos, version, com.nahuo.constant.Constant.CionType.CIONTYPE_DEFAUT)
                .compose(RxUtil.<PinHuoResponse<TempOrderV2>>rxSchedulerHelper())
                .compose(RxUtil.<TempOrderV2>handleResult())
                .subscribeWith(new CommonSubscriber<TempOrderV2>(vthis, true, R.string.create_temp_loading) {
                    @Override
                    public void onNext(TempOrderV2 tempOrderV2) {
                        super.onNext(tempOrderV2);
                        createdOrder(tempOrderV2);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));


    }

    private void getItemDiscount(List<ShopcartItem> items) {
        try {
            StringBuilder postJson = new StringBuilder();
            postJson.append("[");
            for (ShopcartItem item : items) {
                if (item.IsAvailable == true) {
                    postJson.append("{");
                    postJson.append("AgentItemID:'" + item.AgentItemID + "',");
                    postJson.append("Color:'" + item.Color + "',");
                    postJson.append("Size:'" + item.Size + "',");
                    postJson.append("Qty:'" + item.Qty + "'");
                    postJson.append("},");
                }
            }
            if (postJson.length() > 0) {
                postJson = new StringBuilder(postJson.substring(0, postJson.length() - 1));
            }
            postJson.append("]");

            //判断是否全是失效商品
            String postJsonStr = postJson.toString();
            if (items.size() > 0 && ((!postJsonStr.equals("]")) && (!postJsonStr.equals("[")) && (!postJsonStr.equals("[]")))) {
                //只结算有效商品
//                HttpRequest request = mHttpRequestHelper.getRequest(vthis, ShopCartMethod.GET_ITEM_DISCOUNT_FOR_ALL_SHOP,
//                        this);
//                request.addParam("itemInfos", postJson.toString());
//                request.addParam("version", "2");
//                request.setConvert2Token(new TypeToken<DiscountBean>() {
//                });
//                request.doPost();
                postDiscount(postJson.toString(), com.nahuo.constant.Constant.ShopCartVersion.Version_2);
                postJsonChuan = postJson.toString();
            } else {
                showNotifyAnimation(false);
                mTvTotalPrice.setText("¥0.00");
                tv_price_botom.setText(R.string.freight_not_included);
            }
//            else if (items.size() > 0 && (postJsonStr.equals("]") || postJsonStr.equals("[") || postJsonStr.equals("[]"))) {
//                //全是失效商品
//                //new CancelDialog(vthis, vthis.getResources().getString(R.string.shopcart_dialog)).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postDiscount(String postJson, int version) {
        PinHuoApi pinHuoApi = HttpManager.getInstance().getPinHuoNoCacheApi(TAG);
        addSubscribe(pinHuoApi.getItemDiscount(postJson, version)
                .compose(RxUtil.<PinHuoResponse<DiscountBean>>rxSchedulerHelper())
                .compose(RxUtil.<DiscountBean>handleResult())
                .subscribeWith(new CommonSubscriber<DiscountBean>(vthis) {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (!isHidden())
                            showPleaseDialog();
                    }

                    @Override
                    public void onNext(DiscountBean discountBean) {
                        super.onNext(discountBean);
                        dismissDialog();
                        initDiscountData(discountBean);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        dismissDialog();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        dismissDialog();
                    }
                }));
    }

    public void onRefreshData() {
        mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_SHOPCART_RED_IS_SHOW));
        load();
    }

    // @Override
    // public void onLoadMore() {}

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Log.i(getClass().getSimpleName(), "onChildClick g: " + groupPosition + " c:" + childPosition);
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

        return true;
    }

    @Override
    public void totalPriceChanged(double totalPrice) {
        //Log.e(TAG, "====iii");
        // mTvTotalPrice.setText(getString(R.string.rmb_x, Utils.moneyFormat(totalPrice)));
        mCbSelectAll.setChecked(mAdapter.isSelectAll());
        //获取商品折扣
        List<ShopcartItem> selecteds = mAdapter.getSelectedShopcartItems();
        if (!selecteds.isEmpty()) {
            getItemDiscount(selecteds);
        } else {
            showNotifyAnimation(false);
            mTvTotalPrice.setText("¥0.00");
            tv_price_botom.setText(R.string.freight_not_included);
            // tv_price_botom.setVisibility(View.GONE);
        }

    }

    @Override
    public void onRequestStart(String method) {
        // if (ShopCartMethod.GET_ITEMS_FOR_ALL_SHOP.equals(method)) {
        // //
        // // if (mLoadingDialog != null && !isDialogShowing()) {
        // // mLoadingDialog.setMessage(getString(R.string.loading));
        // // mLoadingDialog.show();
        // // }
        // } else
        if (mLoadingDialog == null)
            mLoadingDialog = new LoadingDialog(vthis);
        if (ShopCartMethod.GET_ITEM_DISCOUNT_FOR_ALL_SHOP.equals(method)) {
            if (!isHidden())
                showPleaseDialog();
        } else if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
            if (!isDialogShowing()) {
                mLoadingDialog.setMessage("正在创建订单，请耐心等待...");
                mLoadingDialog.show();
            }
        } else if (ShopCartMethod.REMOVE_ITEMS.equals(method)) {
            if (!isDialogShowing()) {
                mLoadingDialog.setMessage("正在删除...");
                mLoadingDialog.show();
            }
        }
    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        //   mExpListView.onRefreshComplete();
        setRefreshFalse();
        hideDialog();
        dismissDialog();
        if (GET_ITEMS_FOR_ALL_SHOP3.equals(method)) {
            onDataLoaded(object);
            isHideClearTitle();
        } else if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
            createdOrder(object);
        } else if (ShopCartMethod.GET_ITEM_DISCOUNT_FOR_ALL_SHOP.equals(method)) {
            // Log.d("yu", object.toString());
            initDiscountData((DiscountBean) object);

        } else if (ShopCartMethod.REMOVE_ITEMS2.equals(method)) {
            mAdapter.deleteSelectedItems();
            mCbSelectAll.setChecked(false);
            mBottomView.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
            nBottomLine.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void initDiscountData(DiscountBean object) {
        try {
            discountBean = object;
            if (discountBean != null) {
                String content = discountBean.getContent();
                Url = discountBean.getUrl();
                // Log.e("yyt", content + "==");
                if (TextUtils.isEmpty(content.toString().trim())) {
                    showNotifyAnimation(false);
                } else {
                    tv_look.setText(content + "");
                    showNotifyAnimation(true);
                }
                mTvTotalPrice.setText("¥" + discountBean.getPayableAmount());
                if (discountBean.getTotalAmount().equals(discountBean.getPayableAmount())) {
                    //tv_price_botom.setVisibility(View.GONE);
                    tv_price_botom.setText(R.string.freight_not_included);
                } else {
                    tv_price_botom.setVisibility(View.VISIBLE);
                    String mess = "总额：" + discountBean.getTotalAmount() + "  ";
                    if (discountBean.getDiscount().equals("0.00") || discountBean.getDiscount().equals("0") || TextUtils.isEmpty(discountBean.getDiscount())) {
                        mess += "  (不含运费)";
                    } else {
                        mess += "立减：" + discountBean.getDiscount() + "  (不含运费)";
                    }
                    tv_price_botom.setText(mess);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        if (ShopCartMethod.SET_QTY.equals(method)) {// 更新失败恢复原来的数量
            resetCountWhenUpdateFail();
        }
        dismissDialog();
        hideDialog();
        //  mExpListView.onRefreshComplete();
        setRefreshFalse();

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        if (ShopCartMethod.SET_QTY.equals(method)) {// 更新失败恢复原来的数量
            resetCountWhenUpdateFail();
        } else if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
            ViewHub.showShortToast(vthis, msg);
        }
        dismissDialog();
        // mExpListView.onRefreshComplete();
        setRefreshFalse();
        hideDialog();
    }

    @Override
    public void shopcartItemCountChanged(int groupPs, int childPs, int num, int preCount) {
        ShopcartItem item = mAdapter.getChild(groupPs, childPs);
        mChangeCountGroupPosition = groupPs;
        mChangeCountChildPosition = childPs;
        mPreChangeCount = preCount;
        setQtyNun(item.AgentItemID, num, item.Color, item.Size);
        // load();

    }

    public static final String SHOPCARTORDER = "SHOPCARTORDER";

    private void createdOrder(Object object) {
        // Log.i(getClass().getSimpleName(), "createdOrder object : " + object.toString());
        hideDialog();
        TempOrderV2 tempOrders;
        if (object != null && (tempOrders = (TempOrderV2) object) != null && tempOrders.getOrders().size() > 0) {
            Intent it = new Intent(vthis, SubmitOrderActivity.class);
            it.putExtra(SubmitOrderActivity.INTENT_ORDER, (Serializable) tempOrders);
            it.putExtra(SHOPCARTORDER, postJsonChuan);
            startActivity(it);
        }
    }

    private void onDataLoaded(Object object) {
        // mExpListView.onRefreshComplete();
        setRefreshFalse();
        hideDialog();
        try {
            ShopCartBean shopCart = (ShopCartBean) object;
            // Log.v(TAG, "商品数量:" + shopCart.TotalCount + "");
            if (shopCart != null) {
                ArrayList<MultiItemEntity> res = new ArrayList<>();
                if (!ListUtils.isEmpty(shopCart.getItems())) {
                    List<ShopCartItemBean> list = shopCart.getItems();
                    for (ShopCartItemBean bean : list) {
                        Level0Item level0Item = new Level0Item();
                        level0Item.setWareHouseName(bean.getWareHouseName());
                        level0Item.setAvailable(true);
                        level0Item.isSelect = bean.isSelect;
                        if (!ListUtils.isEmpty(bean.getTimeList())) {
                            List<ShopCartItemBean.TimeListBean> timeList = bean.getTimeList();
                            for (ShopCartItemBean.TimeListBean bean1 : timeList) {
                                Level1Item level1Item = new Level1Item();
                                level1Item.setAvailable(bean1.isIsAvailable());
                                level1Item.setStart(bean1.isIsStart());
                                level1Item.setEndMillis(bean1.getEndMillis());
                                level1Item.setToTime(bean1.getToTime());
                                level0Item.addSubItem(level1Item);
                                if (!ListUtils.isEmpty(bean1.getItems())) {
                                    List<ShopCartItemBean.TimeListBean.ItemsBean> itemsList = bean1.getItems();
                                    for (int i = 0; i < itemsList.size(); i++) {
                                        ShopCartItemBean.TimeListBean.ItemsBean bean2 = itemsList.get(i);
                                        if (i == 0) {
                                            bean2.isShowTopLine = false;
                                        } else {
                                            bean2.isShowTopLine = true;
                                        }
                                        Level2Item level2Item = new Level2Item();
                                        level2Item.setCover(bean2.getCover());
                                        level2Item.setName(bean2.getName());
                                        level2Item.setAvailable(bean1.isIsAvailable());
                                        level2Item.setPrice(bean2.getPrice());
                                        level2Item.setTotalQty(bean2.getTotalQty());
                                        level2Item.setAgentItemID(bean2.getAgentItemID());
                                        level2Item.isShowTopLine = bean2.isShowTopLine;
                                        if (!ListUtils.isEmpty(bean2.getProducts())) {
                                            List<Level2Item.ProductsBean> productsBeanList = new ArrayList<>();
                                            for (ShopCartItemBean.TimeListBean.ItemsBean.ProductsBean productsBean : bean2.getProducts()) {
                                                Level2Item.ProductsBean pBean = new Level2Item.ProductsBean();
                                                pBean.setColor(productsBean.getColor());
                                                pBean.setQty(productsBean.getQty());
                                                pBean.setSize(productsBean.getSize());
                                                productsBeanList.add(pBean);
                                            }
                                            level2Item.setProducts(productsBeanList);
                                        }
                                        level1Item.addSubItem(level2Item);
                                    }
                                }
                            }
                        }
                        res.add(level0Item);
                    }
                }
                if (!ListUtils.isEmpty(shopCart.getDisableItems())) {
                    List<ShopCartItemBean> list = shopCart.getDisableItems();
                    Level0Item level0Item = new Level0Item();
                    level0Item.setAvailable(false);
                    level0Item.setWareHouseName("失效商品");
                    level0Item.isSelect = true;
                    Level1Item level1Item = new Level1Item();
                    level1Item.setAvailable(false);
                    level0Item.addSubItem(level1Item);
                    boolean first_Top = true;
                    for (ShopCartItemBean bean : list) {
                        if (!ListUtils.isEmpty(bean.getTimeList())) {
                            List<ShopCartItemBean.TimeListBean> timeList = bean.getTimeList();
                            for (ShopCartItemBean.TimeListBean bean1 : timeList) {
//                                Level1Item level1Item = new Level1Item();
//                                level1Item.setAvailable(bean1.isIsAvailable());
//                                level1Item.setStart(bean1.isIsStart());
//                                level1Item.setEndMillis(bean1.getEndMillis());
//                                level1Item.setToTime(bean1.getToTime());
//                                level0Item.addSubItem(level1Item);
                                if (!ListUtils.isEmpty(bean1.getItems())) {
                                    List<ShopCartItemBean.TimeListBean.ItemsBean> itemsList = bean1.getItems();
                                    for (int i = 0; i < itemsList.size(); i++) {
                                        ShopCartItemBean.TimeListBean.ItemsBean bean2 = itemsList.get(i);
                                        if (i == 0 && first_Top) {
                                            bean2.isShowTopLine = false;
                                            first_Top = false;
                                        } else {
                                            bean2.isShowTopLine = true;
                                        }
                                        Level2Item level2Item = new Level2Item();
                                        level2Item.setCover(bean2.getCover());
                                        level2Item.setName(bean2.getName());
                                        level2Item.setAvailable(bean1.isIsAvailable());
                                        level2Item.setPrice(bean2.getPrice());
                                        level2Item.setTotalQty(bean2.getTotalQty());
                                        level2Item.setAgentItemID(bean2.getAgentItemID());
                                        level2Item.isShowTopLine = bean2.isShowTopLine;
                                        if (!ListUtils.isEmpty(bean2.getProducts())) {
                                            List<Level2Item.ProductsBean> productsBeanList = new ArrayList<>();
                                            for (ShopCartItemBean.TimeListBean.ItemsBean.ProductsBean productsBean : bean2.getProducts()) {
                                                Level2Item.ProductsBean pBean = new Level2Item.ProductsBean();
                                                pBean.setColor(productsBean.getColor());
                                                pBean.setQty(productsBean.getQty());
                                                pBean.setSize(productsBean.getSize());
                                                productsBeanList.add(pBean);
                                            }
                                            level2Item.setProducts(productsBeanList);
                                        }
                                        level1Item.addSubItem(level2Item);
                                    }
                                }
                            }
                        }
                    }
                    res.add(level1Item);
                }
                if (ListUtils.isEmpty(res)) {
                    showNotifyAnimation(false);
                }
                adapter.setListData(res);
                mExpListView.setAdapter(adapter);
                adapter.expandAll();
                adapter.setSelectAll(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mBottomView.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
        nBottomLine.setVisibility(!ListUtils.isEmpty(adapter.getData()) ? View.VISIBLE : View.INVISIBLE);
        judeEmpty();
        // mCbSelectAll.setChecked(true);
        // mAdapter.setSelectAll(true);
    }

    private void judeEmpty() {
        if (ListUtils.isEmpty(adapter.getData())) {
            mEmptyView.setVisibility(View.VISIBLE);
            showNotifyAnimation(false);
            is_open_manage = false;
            btnRight.setText("管理");
            showManageLayout();
        } else {
            mEmptyView.setVisibility(View.GONE);
        }

    }

    private void setQtyNun(long itemId, int count, String color, String size) {
        HttpRequest request = mHttpRequestHelper.getRequest(vthis, ShopCartMethod.SET_QTY, this);
        request.addParam("itemId", itemId + "");
        request.addParam("color", color);
        request.addParam("size", size);
        request.addParam("qty", count + "");
        request.doPost();
    }

    @Override
    public void goodOnClick(ShopcartItem item) {
        Intent it = new Intent(vthis, ItemDetailsActivity.class);
        it.putExtra(ItemDetailsActivity.EXTRA_ID, item.AgentItemID);
        startActivity(it);
    }

    @Override
    public void onDestroy() {
        //  mEventBus.unregister(this);
        super.onDestroy();
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFRESH_SHOP_CART:
                if (this != null && vthis != null)
                    onRefreshData();
                break;
        }
    }

    private void resetCountWhenUpdateFail() {

        if (mChangeCountGroupPosition >= 0 && mChangeCountChildPosition >= 0
                && mAdapter.getGroupCount() > mChangeCountGroupPosition
                && mAdapter.getChildrenCount(mChangeCountGroupPosition) > mChangeCountChildPosition) {
            mAdapter.getChild(mChangeCountGroupPosition, mChangeCountChildPosition).Qty = mPreChangeCount;
            mAdapter.notifyDataSetChanged();
        }
    }

    private void isHideClearTitle() {
        // List<ShopcartItem> loseList = mAdapter.getLoseShopCart();
        if (adapter != null) {
            List<Level2Item> loseList = adapter.getLoseShopCart();
            invalid_shop_clear.setVisibility(ListUtils.isEmpty(loseList) ? View.GONE : View.VISIBLE);
        }
        //ll_notifi.setVisibility(ListUtils.isEmpty(loseList) ? View.GONE : View.VISIBLE);
    }

    //获取拿货车公告
    private class AdsTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                return OrderAPI.getcurrentnotice(vthis, 1);
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (result.getClass() == String.class && result.toString().startsWith("error:")) {
                ViewHub.showLongToast(vthis, result.toString());
                return;
            }

            Animation animation = null;
            list.clear();
            List<NoticeBean> data = (List<NoticeBean>) result;
            if (!ListUtils.isEmpty(data)) {
                slist.clear();
            }
            if (data != null && data.size() > 0) {
                animation = AnimationUtils.loadAnimation(vthis, R.anim.slide_bottom_in);
                if (shopcart_rela != null)
                    shopcart_rela.setVisibility(View.VISIBLE);
                // ll_notifi.startAnimation(animation);
                for (int i = 0; i < data.size(); i++) {
                    NoticeBean bean = data.get(i);
                    bean.setNums_content(i + 1 + ": " + bean.getTitle());
                    list.add(bean);
                    slist.add(i + 1 + ": " + bean.getTitle());
                }
                if (tv_marquee != null)
                    tv_marquee.setContentList(slist);
                //tv_notifi_auto.startFlipping();
                // marqueeFactory.resetData(list);
            } else {
                animation = AnimationUtils.loadAnimation(vthis, R.anim.slide_bottom_out);
                // ll_notifi.startAnimation(animation);
                shopcart_rela.setVisibility(View.GONE);
            }
        }
    }
}
