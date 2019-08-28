//package com.nahuo.quicksale.activity;
//
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.nahuo.bean.SortBean;
//import com.nahuo.constant.UmengClick;
//import com.nahuo.library.controls.CircleTextView;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.quicksale.CommonSearchActivity;
//import com.nahuo.quicksale.R;
//import com.nahuo.quicksale.adapter.GridSpacingItemDecoration;
//import com.nahuo.quicksale.adapter.SortAdpter;
//import com.nahuo.quicksale.adapter.SubSortAdpter;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.SimpleHttpRequestListener;
//import com.nahuo.quicksale.api.SortApi;
//import com.nahuo.quicksale.base.BaseAppCompatActivity;
//import com.nahuo.quicksale.common.ListUtils;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.model.RequestEntity;
//import com.nahuo.quicksale.model.ResultData;
//import com.nahuo.quicksale.util.ChatUtl;
//import com.nahuo.quicksale.util.CircleCarTxtUtl;
//import com.nahuo.quicksale.util.UMengTestUtls;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 分类
// *
// * @author James Chen
// * @create time in 2017/12/11 10:55
// */
//public class SortActivity extends BaseAppCompatActivity implements View.OnClickListener {
//    public static String Tag = "SortActivity";
//    private TextView tv_title;
//    private ImageView iv_all_search, iv_shopping_cart;
//    private CircleTextView carCountTv;
//    private SortActivity Vthis;
//    public LoadingDialog mLoadingDialog;
//    public SortAdpter mSortAdapter;
//    private SubSortAdpter mSubSortAdpter;
//    private RecyclerView recycler_main, recycler_sub;
//    private SortBean mSortBean;
//    private List<SortBean.ListBean> mData = new ArrayList<>();
//    private List<SortBean.ListBean.DatasBean> mSubData = new ArrayList<>();
//    private int mSelectPos;
//    private SwipeRefreshLayout refresh_layout;
//    private View empty_view,line;
//    private TextView tv_empty;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sort);
//        initTitle();
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        refresh_layout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
//        refresh_layout.setRefreshing(true);
//        // 设置下拉进度的背景颜色，默认就是白色的
//        refresh_layout.setProgressBackgroundColorSchemeResource(android.R.color.white);
//        // 设置下拉进度的主题颜色
//        // refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
//        refresh_layout.setColorSchemeResources(R.color.colorAccent, R.color.lightcolorAccent, android.R.color.holo_blue_dark, android.R.color.holo_blue_light);
//
//        // 下拉时触发SwipeRefreshLayout的下拉动画，动画完毕之后就会回调这个方法
//        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                // 开始刷新，设置当前为刷新状态
//                //swipeRefreshLayout.setRefreshing(true);
//                // TODO 获取数据
//                initData();
//                // 这个不能写在外边，不然会直接收起来
//                //swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//        recycler_sub = (RecyclerView) findViewById(R.id.recycler_sub);
//        mSubSortAdpter = new SubSortAdpter(Vthis);
//        GridLayoutManager gridManager = new GridLayoutManager(Vthis, 3);
//        recycler_sub.setLayoutManager(gridManager);
//        recycler_sub.addItemDecoration(new GridSpacingItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.padding_search_bar), getResources().getDimensionPixelSize(R.dimen.padding_search_bar), getResources().getDimensionPixelSize(R.dimen.padding_search_bar_top), getResources().getDimensionPixelSize(R.dimen.padding_search_bar_bottom), true));
//        recycler_sub.setAdapter(mSubSortAdpter);
//        recycler_main = (RecyclerView) findViewById(R.id.recycler_main);
//        mSortAdapter = new SortAdpter(Vthis);
//        mSortAdapter.setListener(new SortAdpter.Listener() {
//            @Override
//            public void onItemClick(SortBean.ListBean item) {
//                if (mSubSortAdpter != null) {
//                    mSubSortAdpter.setData(item.getDatas());
//                    mSubSortAdpter.notifyDataSetChanged();
//                }
//            }
//        });
//        LinearLayoutManager linearManager = new LinearLayoutManager(Vthis);
//        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recycler_main.setLayoutManager(linearManager);
//        recycler_main.setAdapter(mSortAdapter);
//
//
//    }
//
//    private void initData() {
//        SortApi.getRecommendList(new RequestEntity(Vthis, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                setRefreshFalse();
//                setEmty_txt(R.string.pin_huo_no_data);
//                closeDialog(mLoadingDialog);
//                mSortBean = (SortBean) object;
//                if (mSortBean != null) {
//                    mData = mSortBean.getList();
//                    mSubData = mData.get(mSelectPos).getDatas();
//                    mData.get(mSelectPos).isSelect = true;
//                    mSortAdapter.setData(mData);
//                    mSubSortAdpter.setData(mSubData);
//                    mSortAdapter.notifyDataSetChanged();
//                    mSubSortAdpter.notifyDataSetChanged();
//                }
//                judeEmpyView();
//                Log.d(Tag, object.toString());
//            }
//
//            @Override
//            public void onRequestStart(String method) {
//                super.onRequestStart(method);
//                showDialog(mLoadingDialog, "正在加载数据");
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                super.onRequestExp(method, msg, data);
//                closeDialog(mLoadingDialog);
//                setEmty_txt(R.string.pin_huo_net_error);
//                setRefreshFalse();
//                judeEmpyView();
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                super.onRequestFail(method, statusCode, msg);
//                setEmty_txt(R.string.pin_huo_net_error);
//                closeDialog(mLoadingDialog);
//                setRefreshFalse();
//                judeEmpyView();
//            }
//        }));
//    }
//
//    private void judeEmpyView() {
//        if (ListUtils.isEmpty(mData)) {
//            empty_view.setVisibility(View.VISIBLE);
//            line.setVisibility(View.GONE);
//        } else {
//            empty_view.setVisibility(View.GONE);
//            line.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void setRefreshFalse() {
//        if (refresh_layout != null)
//            refresh_layout.setRefreshing(false);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        ChatUtl.setChatBroad(this);
//        // new LoadGoodsTask(this, carCountTv).execute();
//    }
//    private void setEmty_txt(int string) {
//        if (tv_empty != null)
//            tv_empty.setText(string);
//    }
//
//    private void setEmty_txt(String string) {
//        if (tv_empty != null)
//            tv_empty.setText(string);
//    }
//    private void initTitle() {
//        EventBus.getDefault().registerSticky(this);
//        Vthis = this;
//        mLoadingDialog = new LoadingDialog(Vthis);
//        empty_view = findViewById(R.id.empty_view);
//        tv_empty=(TextView)findViewById(R.id.tv_empty);
//        line= findViewById(R.id.line);
//        empty_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (refresh_layout != null)
//                    refresh_layout.setRefreshing(true);
//                initData();
//            }
//        });
//        carCountTv = (CircleTextView) findViewById(R.id.circle_car_text);
//        CircleCarTxtUtl.setColor(carCountTv);
//        tv_title = (TextView) findViewById(R.id.tv_title);
//        iv_all_search = (ImageView) findViewById(R.id.iv_all_search);
//        iv_shopping_cart = (ImageView) findViewById(R.id.iv_shopping_cart);
//        findViewById(R.id.iv_chat_txt).setOnClickListener(this);
//        iv_all_search.setOnClickListener(this);
//        tv_title.setText(R.string.app_main_sort);
//
//    }
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//            case EventBusId.WEIXUN_NEW_MSG:
//                ChatUtl.judeChatNums(carCountTv, event);
//                break;
//        }
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_chat_txt:
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click31);
//                ChatUtl.goToChatMainActivity(this,true);
////                Intent intent = new Intent(this, ChatMainActivity.class);
////                intent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
////                startActivity(intent);
//                break;
//            case R.id.iv_all_search:
//                UMengTestUtls.UmengOnClickEvent(this, UmengClick.Click30);
//                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
//                break;
//            case R.id.iv_shopping_cart:
//                Utils.gotoShopcart(this);
//                break;
//        }
//    }
//}
