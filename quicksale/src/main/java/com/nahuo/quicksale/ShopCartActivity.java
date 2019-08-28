//package com.nahuo.quicksale;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.text.Html;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.TranslateAnimation;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.CheckBox;
//import android.widget.ExpandableListView;
//import android.widget.ExpandableListView.OnChildClickListener;
//import android.widget.ExpandableListView.OnGroupClickListener;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.gson.reflect.TypeToken;
//import com.luck.picture.lib.dialog.PictureDialog;
//import com.nahuo.bean.DiscountBean;
//import com.nahuo.bean.NoticeBean;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListView;
//import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListView.OnRefreshListener;
//import com.nahuo.library.controls.pulltorefresh.PullToRefExpandListViewEx;
//import com.nahuo.quicksale.Topic.PostDetailActivity;
//import com.nahuo.quicksale.activity.ItemPreview1Activity;
//import com.nahuo.quicksale.activity.MainNewActivity;
//import com.nahuo.quicksale.adapter.ShopcartAdapter;
//import com.nahuo.quicksale.adapter.ShopcartAdapter.IGoodItemOnClickListener;
//import com.nahuo.quicksale.adapter.ShopcartAdapter.TotalPriceChangedListener;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
//import com.nahuo.quicksale.api.HttpRequestListener;
//import com.nahuo.quicksale.api.HttpUtils;
//import com.nahuo.quicksale.api.OrderAPI;
//import com.nahuo.quicksale.api.RequestMethod.ShopCartMethod;
//import com.nahuo.quicksale.app.BWApplication;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.ListUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.common.StringUtils;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.countdownutils.CountDownTask;
//import com.nahuo.quicksale.customview.CancelDialog;
//import com.nahuo.quicksale.customview.MarqueeTextView;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.oldermodel.PublicData;
//import com.nahuo.quicksale.oldermodel.ResultData;
//import com.nahuo.quicksale.oldermodel.ShopCart;
//import com.nahuo.quicksale.oldermodel.ShopCart.ShopcartItem;
//import com.nahuo.quicksale.oldermodel.TempOrderV2;
//import com.nahuo.quicksale.util.ActivityUtil;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//
//import static com.nahuo.quicksale.api.RequestMethod.ShopCartMethod.GET_ITEMS_FOR_ALL_SHOP2;
//
///**
// * @author JorsonWong
// * @description 购物车Activity
// * @created 2015年4月1日 上午11:03:38
// */
//public class ShopCartActivity extends AppCompatActivity implements OnClickListener, OnGroupClickListener,
//        OnChildClickListener, OnRefreshListener, TotalPriceChangedListener, IGoodItemOnClickListener, HttpRequestListener {
//    protected static final String TAG = "ShopCartNewActivity";
//    public static final String ETRA_LEFT_BTN_ISHOW = "ETRA_LEFT_BTN_ISHOW";
//    // private PullToRefreshExpandableListView mPullExListView;
//    private CheckBox mCbSelectAll;
//    private TextView mTvTotalPrice;
//    private ShopcartAdapter mAdapter;
//    private PullToRefExpandListViewEx mExpListView;
//    private HttpRequestHelper mHttpRequestHelper = new HttpRequestHelper();
//    private View mEmptyView;
//    private TextView mTvEmpty;
//    private View mBottomView;
//    private View nBottomLine;
//    private ImageView mIvScroll2Top;
//    private EventBus mEventBus = EventBus.getDefault();
//
//    private int mChangeCountGroupPosition = -1;
//
//    private int mChangeCountChildPosition = -1;
//
//    private int mPreChangeCount = 1;
//
//    private ShopCartActivity vthis = ShopCartActivity.this;
//
//    private TextView btnRight;
//    private CountDownTask mCountDownTask;
//    private Context mContext;
//    //添加一个空接口（解决框架的bug）
//    private PullToRefExpandListView.EmptyInterface emptyInterface;
//
//    private static final int DELETE_SELECT = 1;//删除选中商品
//
//    private static final int CLEAR_ALL = 2;//清空所有失效商品
//
//    private MarqueeTextView tv_marquee;
//
//    private List<NoticeBean> list = new ArrayList<>();
//
//    private ShopCartActivity Vthis = this;
//
//    private List<String> slist = new ArrayList<>();
//
//    private RelativeLayout shopcart_rela;
//    protected LoadingDialog mLoadingDialog;
//    private String url = "";
//    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
//    private TextView tvTitleCenter, tvTLeft, invalid_shop_clear;
//    private View layout_manage;
//    private boolean is_open_manage = false;
//    private Animation mShowAction, mHiddenAction;
//    private View ll_notifi;
//    private TextView tv_look, tv_cancel, tv_price_botom;
//    private DiscountBean discountBean;
//    private String Url = "";
//    public boolean is_show_left_btn = false;
//    protected PictureDialog dialog;
//    /**
//     * dismiss dialog
//     */
//    protected void dismissDialog() {
//        try {
//            if (dialog != null && dialog.isShowing()) {
//                dialog.dismiss();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    /**
//     * loading dialog
//     */
//    protected void showPleaseDialog() {
//        if (!isFinishing()) {
//            dismissDialog();
//            dialog = new PictureDialog(this);
//            dialog.show();
//        }
//    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shopcart);
//        // setTitle(R.string.shopping_cart);
//        initTitleBar();
//        initViews();
//        init();
//
//    }
//
//    private void startCountDown() {
//        mCountDownTask = CountDownTask.create();
//        mAdapter.setCountDownTask(mCountDownTask);
//    }
//
//    private void cancelCountDown() {
//        mAdapter.setCountDownTask(null);
//        mCountDownTask.cancel();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startCountDown();
//        load();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        cancelCountDown();
//    }
//
//    private void showNotifyAnimation(boolean isShow) {
//        if (isShow) {
//            if (ll_notifi.getVisibility() != View.VISIBLE) {
//                ll_notifi.setVisibility(View.VISIBLE);
//                Animation anim = AnimationUtils.loadAnimation(this, R.anim.bottom_menu_appear);
//                ll_notifi.startAnimation(anim);
//            }
//        } else {
//            if (ll_notifi.getVisibility() == View.VISIBLE) {
//                Animation anim = AnimationUtils.loadAnimation(this, R.anim.botttom_menu_disppearx);
//                ll_notifi.startAnimation(anim);
//                anim.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        ll_notifi.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//            }
//        }
//
//    }
//
//    private void initViews() {
//        mLoadingDialog = new LoadingDialog(this);
//        url = Const.getShopLogo(SpManager.getUserId(BWApplication.getInstance()));
//        mEmptyView = getLayoutInflater().inflate(R.layout.lv_shopcart_empty_view, null);
//        BWApplication.addActivity(this);
//        mExpListView = (PullToRefExpandListViewEx) findViewById(R.id.lv_shopcart);
//        mExpListView.setEmptyView(mEmptyView);
//        mTvEmpty = (TextView) mEmptyView.findViewById(R.id.tv_empty);
//        ll_notifi = findViewById(R.id.ll_notifi);
//        ll_notifi.setOnClickListener(this);
//        tv_look = (TextView) findViewById(R.id.tv_look);
//        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
//        tv_price_botom = (TextView) findViewById(R.id.tv_price_botom);
//        tv_cancel.setOnClickListener(this);
//        mTvEmpty.setText(Html.fromHtml(getString(R.string.shopcart_empty_text)));
//        tv_marquee = (MarqueeTextView) findViewById(R.id.gotoYunFei);
//        //tv_marquee.setOnClickListener(this);
//        tv_marquee.setVerticalSwitchSpeed(1000);
//        tv_marquee.setHorizontalScrollSpeed(200);
//        tv_marquee.setOnItemOnClickListener(new MarqueeTextView.OnItemClickListener() {
//            @Override
//            public void onItemclick(int index) {
//               // Log.v(TAG, "index" + index);
//                NoticeBean bean = list.get(index);
//                String target = bean.getTarget();
//                if (!TextUtils.isEmpty(target)) {
//                    Intent intent = new Intent(Vthis, PostDetailActivity.class);
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
//                }
//            }
//        });
//
//        /*List<String> all=new ArrayList<String>();
//        all.add("\t\t运费怎么算的？");
//        all.add("\t\t怎么付款了没货发？");
//        tv_marquee.setContentList(all);*/
//        //tv_marquee.startScroll();
//        mTvEmpty.setOnClickListener(this);
//        mExpListView.setOnRefreshListener(this);
//        // mExpListView = (ExpandableListView)mPullExListView.getRefreshableView();
//        mExpListView.setOnGroupClickListener(this);
//        mExpListView.setOnChildClickListener(this);
//        mCbSelectAll = (CheckBox) findViewById(android.R.id.checkbox);
//        mCbSelectAll.setOnClickListener(this);
//        mTvTotalPrice = (TextView) findViewById(android.R.id.text1);
//        mBottomView = findViewById(android.R.id.inputArea);
//        nBottomLine = findViewById(R.id.line);
//        shopcart_rela = (RelativeLayout) findViewById(R.id.shopcart_rela);
//        mBottomView.setVisibility(View.INVISIBLE);
//        mIvScroll2Top = (ImageView) findViewById(R.id.iv_scroll_top);
//
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
//
//    }
//
//    private void init() {
//        mEventBus.registerSticky(this);
//        mAdapter = new ShopcartAdapter(this);
//        mAdapter.setTextView(invalid_shop_clear);
//        mExpListView.setAdapter(mAdapter);
//        mTvTotalPrice.setText(getString(R.string.rmb_x, Utils.moneyFormat(0)));
//        mAdapter.setTotalPriceChangedListener(this);
//        mAdapter.setIGoodItemOnClickListener(this);
//    }
//
//    public void showManageLayout() {
//        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
//        mShowAction.setDuration(500);
//        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
//                0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//                0.0f);
//        mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                layout_manage.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        mHiddenAction.setDuration(500);
//        if (is_open_manage) {
//            layout_manage.setVisibility(View.VISIBLE);
//            layout_manage.setAnimation(mShowAction);
//
//        } else {
//            layout_manage.setAnimation(mHiddenAction);
//
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_notifi:
//                if (!TextUtils.isEmpty(Url)) {
//                    Intent intent = new Intent(this, ItemPreview1Activity.class);
//                    intent.putExtra("name", "活动");
//                    intent.putExtra("url", Url);
//                    startActivity(intent);
//                }
//                break;
//            case R.id.tv_cancel:
//                showNotifyAnimation(false);
//                break;
//            case R.id.tvTLeft:
//                finish();
//                break;
//            /*case R.id.gotoYunFei: {
//                Intent intent = new Intent(this, PostDetailActivity.class);
//                intent.putExtra(PostDetailActivity.EXTRA_TID, 93255);
//                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                this.startActivity(intent);
//                break;
//            }*/
//            case android.R.id.button1:// 结算
//                List<ShopcartItem> selecteds = mAdapter.getSelectedShopcartItems();
//                if (!selecteds.isEmpty()) {
//                    creatTempOrder(selecteds);
//                } else {
//                    ViewHub.showShortToast(getApplicationContext(), getString(R.string.select_nothing));
//                }
//                break;
//            case android.R.id.button2:// 删除
//                final List<ShopcartItem> selecteIds = mAdapter.getSelectedShopcartItems();
//                if (!selecteIds.isEmpty()) {
//                    ViewHub.showOkDialog(this, "提示", "您确定要删除所选商品吗？", getString(android.R.string.ok),
//                            getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //removeItems(selecteIds);
//                                    deleteShopCart(selecteIds, DELETE_SELECT);
//                                }
//                            });
//                } else {
//                    ViewHub.showShortToast(getApplicationContext(), getString(R.string.select_nothing));
//                }
//                break;
//            case android.R.id.checkbox:
//                mAdapter.setSelectAll(mCbSelectAll.isChecked());
//                break;
//            case R.id.tv_empty:
//                EventBus.getDefault().postSticky(
//                        BusEvent.getEvent(EventBusId.MAIN_CHANGE_CURRENT_TAB, MainNewActivity.TAG_ALLITEMS));
//                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.ALL_ITEM_CHANGE_CURRENT_TAB, 0));
//
////                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                startActivity(intent);
//                ActivityUtil.goToMainActivity(this);
//                break;
//            case R.id.iv_scroll_top:
//                mExpListView.setSelection(0);
//                break;
//            case R.id.tvTRight:
//                if (is_open_manage) {
//                    is_open_manage = false;
//                    btnRight.setText("管理");
//                } else {
//                    btnRight.setText("取消");
//                    is_open_manage = true;
//                }
//                showManageLayout();
//                break;
//            case R.id.invalid_shop_clear:
//                ViewHub.showOkDialog(this, "提示",
//                        "确定要清空已失效的商品吗？", getString(android.R.string.ok),
//                        getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                List<ShopcartItem> deleteAllLostItem = mAdapter.getLoseShopCart();
//                                if (deleteAllLostItem != null && deleteAllLostItem.size() > 0) {
//                                    deleteShopCart(deleteAllLostItem, CLEAR_ALL);
//                                }
//                            }
//                        });
//                break;
//            default:
//                break;
//        }
//
//    }
//
//    private void  initTitleBar() {
//        is_show_left_btn = getIntent().getBooleanExtra(ETRA_LEFT_BTN_ISHOW, false);
//        layout_manage = findViewById(R.id.layout_manage);
//        tvTLeft = (TextView) findViewById(R.id.tvTLeft);
//        tvTLeft.setOnClickListener(this);
//        if (is_show_left_btn) {
//            tvTLeft.setVisibility(View.INVISIBLE);
//        } else {
//            tvTLeft.setVisibility(View.VISIBLE);
//
//        }
//        invalid_shop_clear = (TextView) findViewById(R.id.invalid_shop_clear);
//        invalid_shop_clear.setOnClickListener(this);
//        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
//        tvTitleCenter.setText(R.string.shopping_cart);
//        btnRight = (TextView) findViewById(R.id.tvTRight);
//        btnRight.setText(getString(R.string.manage_shop_car));
//        btnRight.setVisibility(View.VISIBLE);
//        btnRight.setOnClickListener(this);
//    }
//
//    private void removeItems(List<ShopcartItem> items) {
//        StringBuilder itemIds = new StringBuilder();
//        StringBuilder tags = new StringBuilder();
//        for (ShopcartItem item : items) {
//            itemIds.append(item.AgentItemID).append(",");
//            tags.append(item.Tag).append(",");
//        }
//        HttpRequest request = mRequestHelper.getRequest(this, ShopCartMethod.REMOVE_ITEMS, this);
//        request.addParam("itemIds", StringUtils.deleteEndStr(itemIds.toString(), ","));
//        request.addParam("tags", StringUtils.deleteEndStr(tags.toString(), ","));
//        request.doPost();
//
//    }
//
//    //拼接删除订单
//    private void deleteShopCart(List<ShopcartItem> items, int type) {
//
//        try {
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
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected boolean isDialogShowing() {
//        return (mLoadingDialog != null && mLoadingDialog.isShowing());
//    }
//
//    protected void hideDialog() {
//        if (isDialogShowing()) {
//            mLoadingDialog.stop();
//        }
//    }
//
//    private class DeleteShopCartTask extends AsyncTask<Void, Void, String> {
//        private JSONArray ja;
//        //type 类型两种1(删除所选商品),2(删除所有商品)
//        private int type = -1;
//
//        public DeleteShopCartTask(JSONArray ja, int type) {
//            this.ja = ja;
//            this.type = type;
//        }
//
//        private String json;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            if (!isDialogShowing()) {
//                mLoadingDialog.setMessage("正在删除...");
//                mLoadingDialog.show();
//            }
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            try {
//                json = HttpUtils.httpPost(ShopCartMethod.REMOVE_ITEMS2, ja.toString(), PublicData.getCookie(ShopCartActivity.this));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return "OK";
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            if (isDialogShowing()) {
//                mLoadingDialog.stop();
//            }
//            if (!result.equals("OK")) {
//                // 验证result
//                if (result.startsWith("401") || result.startsWith("not_registered")) {
//                    Toast.makeText(vthis, result, Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(vthis, result, Toast.LENGTH_LONG).show();
//                }
//            } else {
//                //解析result通知更新
//                isHideClearTitle();
//                switch (type) {
//                    case DELETE_SELECT:
//                        mAdapter.deleteSelectedItems();
//                        mCbSelectAll.setChecked(false);
//                        mBottomView.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//                        nBottomLine.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//                        break;
//                    case CLEAR_ALL:
//                        mAdapter.deleteLoseItems();
//                        mBottomView.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//                        nBottomLine.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//                        isHideClearTitle();
//                        break;
//                    default:
//                }
//            }
//        }
//    }
//
//    private void load() {
//        HttpRequest request = mRequestHelper.getRequest(this, GET_ITEMS_FOR_ALL_SHOP2, this);
//        if (Const.DEBUG) {
//            request.addParam("debug", "1");
//        }
//        request.setConvert2Class(ShopCart.class);
//        request.doPost();
//
//        new AdsTask().execute();
//    }
//
//    private String postJsonChuan = "";
//
//    private void creatTempOrder(List<ShopcartItem> items) {
//        try {
//            StringBuilder postJson = new StringBuilder();
//            postJson.append("[");
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
//            if (postJson.length() > 0) {
//                postJson = new StringBuilder(postJson.substring(0, postJson.length() - 1));
//            }
//            postJson.append("]");
//
//            //判断是否全是失效商品
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
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getItemDiscount(List<ShopcartItem> items) {
//        try {
//            StringBuilder postJson = new StringBuilder();
//            postJson.append("[");
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
//            if (postJson.length() > 0) {
//                postJson = new StringBuilder(postJson.substring(0, postJson.length() - 1));
//            }
//            postJson.append("]");
//
//            //判断是否全是失效商品
//            String postJsonStr = postJson.toString();
//            if (items.size() > 0 && ((!postJsonStr.equals("]")) && (!postJsonStr.equals("[")) && (!postJsonStr.equals("[]")))) {
//                //只结算有效商品
//                HttpRequest request = mHttpRequestHelper.getRequest(this, ShopCartMethod.GET_ITEM_DISCOUNT_FOR_ALL_SHOP,
//                        this);
//                request.addParam("itemInfos", postJson.toString());
//                request.addParam("version", "2");
//                request.setConvert2Token(new TypeToken<DiscountBean>() {
//                });
//                request.doPost();
//                postJsonChuan = postJson.toString();
//            }else {
//                showNotifyAnimation(false);
//                mTvTotalPrice.setText("¥0.00" );
//                tv_price_botom.setText(R.string.freight_not_included);
//            }
////            else if (items.size() > 0 && (postJsonStr.equals("]") || postJsonStr.equals("[") || postJsonStr.equals("[]"))) {
////                //全是失效商品
////                //new CancelDialog(vthis, vthis.getResources().getString(R.string.shopcart_dialog)).show();
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onRefresh() {
//        mEventBus.post(BusEvent.getEvent(EventBusId.PINHUO_SHOPCART_RED_IS_SHOW));
//        load();
//    }
//
//    // @Override
//    // public void onLoadMore() {}
//
//    @Override
//    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//        Log.i(getClass().getSimpleName(), "onChildClick g: " + groupPosition + " c:" + childPosition);
//        return false;
//    }
//
//    @Override
//    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//
//        return true;
//    }
//
//    @Override
//    public void totalPriceChanged(double totalPrice) {
//        //Log.e(TAG, "====iii");
//        // mTvTotalPrice.setText(getString(R.string.rmb_x, Utils.moneyFormat(totalPrice)));
//        mCbSelectAll.setChecked(mAdapter.isSelectAll());
//        //获取商品折扣
//        List<ShopcartItem> selecteds = mAdapter.getSelectedShopcartItems();
//        if (!selecteds.isEmpty()) {
//            getItemDiscount(selecteds);
//        } else {
//            showNotifyAnimation(false);
//            mTvTotalPrice.setText("¥0.00");
//            tv_price_botom.setText(R.string.freight_not_included);
//            // tv_price_botom.setVisibility(View.GONE);
//        }
//
//    }
//
//    @Override
//    public void onRequestStart(String method) {
//        // if (ShopCartMethod.GET_ITEMS_FOR_ALL_SHOP.equals(method)) {
//        // //
//        // // if (mLoadingDialog != null && !isDialogShowing()) {
//        // // mLoadingDialog.setMessage(getString(R.string.loading));
//        // // mLoadingDialog.show();
//        // // }
//        // } else
//        if (mLoadingDialog == null)
//            mLoadingDialog = new LoadingDialog(this);
//        if (ShopCartMethod.GET_ITEM_DISCOUNT_FOR_ALL_SHOP.equals(method)){
//            showPleaseDialog();
//        }else
//        if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
//            if (!isDialogShowing()) {
//                mLoadingDialog.setMessage("正在创建订单，请耐心等待...");
//                mLoadingDialog.show();
//            }
//        } else if (ShopCartMethod.REMOVE_ITEMS.equals(method)) {
//            if (!isDialogShowing()) {
//                mLoadingDialog.setMessage("正在删除...");
//                mLoadingDialog.show();
//            }
//        }
//    }
//
//    @Override
//    public void onRequestSuccess(String method, Object object) {
//        mExpListView.onRefreshComplete();
//        hideDialog();
//        dismissDialog();
//       if (GET_ITEMS_FOR_ALL_SHOP2.equals(method)) {
//            onDataLoaded(object);
//            isHideClearTitle();
//        } else if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
//            createdOrder(object);
//        } else if (ShopCartMethod.GET_ITEM_DISCOUNT_FOR_ALL_SHOP.equals(method)) {
//            // Log.d("yu", object.toString());
//            discountBean = (DiscountBean) object;
//            if (discountBean != null) {
//                String content = discountBean.getContent();
//                Url = discountBean.getUrl();
//                Log.e("yyt",content+"==");
//                if (TextUtils.isEmpty(content.toString().trim())) {
//                    showNotifyAnimation(false);
//                } else {
//                    tv_look.setText(content + "");
//                    showNotifyAnimation(true);
//                }
//                mTvTotalPrice.setText("¥" + discountBean.getPayableAmount());
//                if (discountBean.getTotalAmount().equals(discountBean.getPayableAmount())) {
//                    //tv_price_botom.setVisibility(View.GONE);
//                    tv_price_botom.setText(R.string.freight_not_included);
//                } else {
//                    tv_price_botom.setVisibility(View.VISIBLE);
//                    String mess = "总额：" + discountBean.getTotalAmount() + "  ";
//                    if (discountBean.getDiscount().equals("0.00") || discountBean.getDiscount().equals("0") || TextUtils.isEmpty(discountBean.getDiscount())) {
//                        mess += "  (不含运费)";
//                    } else {
//                        mess += "立减：" + discountBean.getDiscount() + "  (不含运费)";
//                    }
//                    tv_price_botom.setText(mess);
//                }
//            }
//
//        } else if (ShopCartMethod.REMOVE_ITEMS2.equals(method)) {
//            mAdapter.deleteSelectedItems();
//            mCbSelectAll.setChecked(false);
//            mBottomView.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//            nBottomLine.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//        }
//    }
//
//    @Override
//    public void onRequestFail(String method, int statusCode, String msg) {
//        if (ShopCartMethod.SET_QTY.equals(method)) {// 更新失败恢复原来的数量
//            resetCountWhenUpdateFail();
//        }
//        dismissDialog();
//        hideDialog();
//        mExpListView.onRefreshComplete();
//
//    }
//
//    @Override
//    public void onRequestExp(String method, String msg, ResultData data) {
//        if (ShopCartMethod.SET_QTY.equals(method)) {// 更新失败恢复原来的数量
//            resetCountWhenUpdateFail();
//        } else if (ShopCartMethod.CREATE_TEMP_ORDER_FOR_ALL_SHOP.equals(method)) {
//            ViewHub.showShortToast(this, msg);
//        }
//        dismissDialog();
//        mExpListView.onRefreshComplete();
//        hideDialog();
//    }
//
//    @Override
//    public void shopcartItemCountChanged(int groupPs, int childPs, int num, int preCount) {
//        ShopcartItem item = mAdapter.getChild(groupPs, childPs);
//        mChangeCountGroupPosition = groupPs;
//        mChangeCountChildPosition = childPs;
//        mPreChangeCount = preCount;
//        setQtyNun(item.AgentItemID, num, item.Color, item.Size);
//        // load();
//
//    }
//
//    public static final String SHOPCARTORDER = "SHOPCARTORDER";
//
//    private void createdOrder(Object object) {
//        Log.i(getClass().getSimpleName(), "createdOrder object : " + object.toString());
//        hideDialog();
//        TempOrderV2 tempOrders;
//        if (object != null && (tempOrders = (TempOrderV2) object) != null && tempOrders.getOrders().size() > 0) {
//            Intent it = new Intent(getApplicationContext(), SubmitOrderActivity.class);
//            it.putExtra(SubmitOrderActivity.INTENT_ORDER, (Serializable) tempOrders);
//            it.putExtra(SHOPCARTORDER, postJsonChuan);
//            startActivity(it);
//        }
//    }
//
//    private void onDataLoaded(Object object) {
//        mExpListView.onRefreshComplete();
//        hideDialog();
//        try {
//            ShopCart shopCart = (ShopCart) object;
//            Log.v(TAG, "商品数量:" + shopCart.TotalCount + "");
//            if (shopCart != null) {
//                if (shopCart.Shops != null) {
//                    Log.i(getClass().getSimpleName(), "Shopcart shops not null");
//                    mAdapter.setShopCarts(shopCart.Shops);
//                    mAdapter.notifyDataSetChanged();
//                    for (int i = 0; i < mAdapter.getGroupCount(); i++) {
//                        mExpListView.expandGroup(i);
//                    }
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mBottomView.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//        nBottomLine.setVisibility(mAdapter.getGroupCount() > 0 ? View.VISIBLE : View.INVISIBLE);
//        mCbSelectAll.setChecked(true);
//        mAdapter.setSelectAll(true);
//    }
//
//    private void setQtyNun(long itemId, int count, String color, String size) {
//        HttpRequest request = mHttpRequestHelper.getRequest(this, ShopCartMethod.SET_QTY, this);
//        request.addParam("itemId", itemId + "");
//        request.addParam("color", color);
//        request.addParam("size", size);
//        request.addParam("qty", count + "");
//        request.doPost();
//    }
//
//    @Override
//    public void goodOnClick(ShopcartItem item) {
//        Intent it = new Intent(getApplicationContext(), ItemDetailsActivity.class);
//        it.putExtra(ItemDetailsActivity.EXTRA_ID, item.AgentItemID);
//        startActivity(it);
//    }
//
//    @Override
//    protected void onDestroy() {
//        mEventBus.unregister(this);
//        super.onDestroy();
//    }
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//            case EventBusId.REFRESH_SHOP_CART:
//                onRefresh();
//                break;
//        }
//    }
//
//    private void resetCountWhenUpdateFail() {
//
//        if (mChangeCountGroupPosition >= 0 && mChangeCountChildPosition >= 0
//                && mAdapter.getGroupCount() > mChangeCountGroupPosition
//                && mAdapter.getChildrenCount(mChangeCountGroupPosition) > mChangeCountChildPosition) {
//            mAdapter.getChild(mChangeCountGroupPosition, mChangeCountChildPosition).Qty = mPreChangeCount;
//            mAdapter.notifyDataSetChanged();
//        }
//    }
//
//    private void isHideClearTitle() {
//        List<ShopcartItem> loseList = mAdapter.getLoseShopCart();
//        invalid_shop_clear.setVisibility(ListUtils.isEmpty(loseList) ? View.GONE : View.VISIBLE);
//        //ll_notifi.setVisibility(ListUtils.isEmpty(loseList) ? View.GONE : View.VISIBLE);
//    }
//
//    //获取拿货车公告
//    private class AdsTask extends AsyncTask<Void, Void, Object> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Object doInBackground(Void... params) {
//            try {
//                return OrderAPI.getcurrentnotice(Vthis, 1);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "error:" + e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            if (result.getClass() == String.class && result.toString().startsWith("error:")) {
//                ViewHub.showLongToast(Vthis, result.toString());
//                return;
//            }
//
//            Animation animation = null;
//            list.clear();
//            List<NoticeBean> data = (List<NoticeBean>) result;
//            if (!ListUtils.isEmpty(data)) {
//                slist.clear();
//            }
//            if (data != null && data.size() > 0) {
//                animation = AnimationUtils.loadAnimation(Vthis, R.anim.slide_bottom_in);
//                shopcart_rela.setVisibility(View.VISIBLE);
//                // ll_notifi.startAnimation(animation);
//                for (int i = 0; i < data.size(); i++) {
//                    NoticeBean bean = data.get(i);
//                    bean.setNums_content(i + 1 + ": " + bean.getTitle());
//                    list.add(bean);
//                    slist.add(i + 1 + ": " + bean.getTitle());
//                }
//                tv_marquee.setContentList(slist);
//                //tv_notifi_auto.startFlipping();
//                // marqueeFactory.resetData(list);
//            } else {
//                animation = AnimationUtils.loadAnimation(Vthis, R.anim.slide_bottom_out);
//                // ll_notifi.startAnimation(animation);
//                shopcart_rela.setVisibility(View.GONE);
//            }
//        }
//    }
//}
