package com.nahuo.quicksale.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nahuo.PopupWindow.MarketMenu;
import com.nahuo.bean.StallsAllListBean;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.library.controls.FlowLayout;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.CommonSearchActivity;
import com.nahuo.quicksale.ItemDetailsActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.ImageBannerAdapter;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.MarketApi;
import com.nahuo.quicksale.api.QuickSaleApi;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.SimpleHttpRequestListener;
import com.nahuo.quicksale.base.BaseFragmentActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Constant;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.common.ViewUtil;
import com.nahuo.quicksale.customview.ViewFlow;
import com.nahuo.quicksale.customview.ViewFlowCircleIndicator;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.flipshare.FlipShareView;
import com.nahuo.quicksale.flipshare.ShareItem;
import com.nahuo.quicksale.fragment.MarketFragment;
import com.nahuo.quicksale.oldermodel.BannerAdModel;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.RequestEntity;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;
import com.nahuo.quicksale.util.LoadGoodsTask;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 市场
 *
 * @author James Chen
 * @create time in 2017/5/31 10:19
 */
public class MarketActivity extends BaseFragmentActivity implements HttpRequestListener, View.OnClickListener, MarketMenu.OnMarketMenuClickItem {
    private CircleTextView carCountTv;
    private View empty_view;
    private ImageView iv_handle;
    private MarketActivity Vthis;
    private List<BannerAdModel> adList = new ArrayList<>();
    private ViewFlow mViewFlow;
    private ViewFlowCircleIndicator mViewFlowIndic;
    private ImageBannerAdapter bAdapter;

    protected <T extends View> T findView(int resId) {
        return (T) findViewById(resId);
    }

    public LoadingDialog mLoadingDialog;
    private List<StallsAllListBean.MarketListBean> data = new ArrayList<>();
    private List<StallsAllListBean.FloorListBean> fList = new ArrayList<>();
    private ArrayList<StallsAllListBean.ShopListBean> sList = new ArrayList<>();
    private int mid, fid;
    private TextView tv_title;

    private RadioGroup mRadioGroup;
    static public ViewPager mViewPager;
    private FragmentStatePagerAdapter mPagerAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private List<Integer> catIDs = new ArrayList<>();
    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
    private float mCurrentCheckedRadioLeft;//当前被选中的RadioButton距离左侧的距离
    private View ad_layout;
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        initView();
        getData();
//        MarketApi.getAllStallsForSearch(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestStart(String method) {
//                super.onRequestStart(method);
//                showDialog(mLoadingDialog, "正在加载档口数据");
//            }
//
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//
//                closeDialog(mLoadingDialog);
//                List<StallsBean> list= (List<StallsBean>) object;
//                data.clear();
//                if (list != null) {
//                    data.addAll(list);
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                super.onRequestFail(method, statusCode, msg);
//                closeDialog(mLoadingDialog);
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                super.onRequestExp(method, msg, data);
//                closeDialog(mLoadingDialog);
//            }
//        }));
    }
    private void getAd1() {
        //读取广告
        adList.clear();
        QuickSaleApi.getMarketsNewAd(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {
            @Override
            public void onRequestSuccess(String method, Object object) {
                ArrayList<BannerAdModel> adList1 = (ArrayList<BannerAdModel>) object;
                adList.clear();
                adList.addAll(adList1);
                flushAdView();
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                //  EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
                flushAdView();
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
                flushAdView();
            }
        }), mid);

    }
    private void getAd() {
        //读取广告
        QuickSaleApi.getMarketsNewAd(new RequestEntity(this, new HttpRequestHelper(), new SimpleHttpRequestListener() {
            @Override
            public void onRequestSuccess(String method, Object object) {
                ArrayList<BannerAdModel> adList1 = (ArrayList<BannerAdModel>) object;
                adList.clear();
                adList.addAll(adList1);
                flushAdView();
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                //  EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
                flushAdView();
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
                flushAdView();
            }
        }), mid);

    }

    private void flushAdView() {

        if (adList == null || adList.size() <= 0) {
            ad_layout.setVisibility(View.GONE);
        } else {
            ad_layout.setVisibility(View.VISIBLE);
            ArrayList<String> imgs = new ArrayList<>();
            for (BannerAdModel adModel : adList) {
                imgs.add(adModel.ImageUrl);
            }
            mViewFlow.setTimeSpan(Constant.ADVERTISE_TIME);
            mViewFlow.setFlowIndicator(mViewFlowIndic);
            int size = adList == null ? 0 : adList.size();
            mViewFlow.setSelection(size * 1000);
            mViewFlow.setSideBuffer(size);
            bAdapter = new ImageBannerAdapter(this, adList);
            mViewFlow.setAdapter(bAdapter);
            mViewFlow.startAutoFlowTimer();
            bAdapter.setOnItemClickListener(new ImageBannerAdapter.BannerOnclickListener() {
                @Override
                public void onItemClick(View v, int positon) {
                    // Toast.makeText(mActivity, positon + "", Toast.LENGTH_SHORT).show();
                    if (positon >= 0) {
                        BannerAdModel selectAd = adList.get(positon);
                        gotoBannerJump(selectAd);
                    }
                }
            });
        }
    }

    /**
     * 1.网页
     * 2.小组帖子
     * 3.拼货场次
     * 4.拼货场次类目
     * 5.安装地址
     *
     * @param selectAd
     */
    public void gotoBannerJump(BannerAdModel selectAd) {
        switch (selectAd.TypeID) {
            case 1://网页
            case 5: {//网页
                //Intent intent = new Intent(mActivity, ItemPreviewActivity.class);
                Intent intent = new Intent(Vthis, ItemPreview1Activity.class);
                intent.putExtra("name", "");
                intent.putExtra("url", selectAd.Content);
                Vthis.startActivity(intent);
                break;
            }
            case 6: { // 打开商品详情
                String content = selectAd.Content;
                String itemID;
                if (content.contains("http://")) {
                    itemID = content.replace("http://item.weipushop.com/wap/wpitem/", "");
                } else {
                    itemID = content;
                }
                int id;
                try {
                    id = Integer.valueOf(itemID);
                } catch (NumberFormatException e) {
                    ViewHub.showShortToast(Vthis, "无法识别该商品");
                    return;
                }
                Intent intent = new Intent(Vthis, ItemDetailsActivity.class);
                intent.putExtra(ItemDetailsActivity.EXTRA_ID, id);
                startActivity(intent);
                break;
            }
            case 2: {//小组帖子
                if (selectAd.Content.indexOf("/xiaozu/topic/") > 1) {
                    String temp = "/xiaozu/topic/";
                    int topicID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(Vthis, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                    Vthis.startActivity(intent);
                } else if (selectAd.Content.indexOf("/xiaozu/act/") > 1) {
                    String temp = "/xiaozu/act/";
                    int actID = Integer.parseInt(selectAd.Content.substring(selectAd.Content.indexOf(temp) + temp.length()));
                    Intent intent = new Intent(Vthis, PostDetailActivity.class);
                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                    Vthis.startActivity(intent);
                }
                break;
            }
            case 3: {// 进入场次
                QuickSaleApi.getRecommendShopItems(Vthis,
                        mRequestHelper,
                        this,
                        Utils.parseInt(selectAd.Content),
                        0,
                        20,
                        "",
                        Const.SortIndex.DefaultDesc,
                        -1,
                        0);
//                PinHuoModel item = new PinHuoModel();
//                item.ID = Integer.parseInt(selectAd.Content);
//                PinHuoDetailListActivity.launch(mActivity, item,true);
                break;
            }
            case 4: {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PIN_HUO_SELECT_RB, Integer.parseInt(selectAd.Content)));
//                int id =  Integer.parseInt(selectAd.Content);
//
//                int selectIndex = -1;
//                int index = 0;
//                for (PinHuoCategroyModel pcm : newResult.CategoryList) {
//                    if (id == pcm.getCid()) {
//                        selectIndex = index;
//                    }
//                    index++;
//                }
//                if (selectIndex >= 0) {
//                    RadioButton rb = (RadioButton) PinHuoTabFragment.mRadioGroup.getChildAt(selectIndex);
//                    rb.setChecked(true);
//                    curCategoryID = (int) rb.getTag();
//                    getRefleshData();
//                }
                break;
            }
        }
    }

    private void initView() {
        EventBus.getDefault().registerSticky(this);
        Vthis = this;
        ad_layout = findViewById(R.id.ad_layout);
        mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
        mViewFlowIndic = (ViewFlowCircleIndicator) findViewById(R.id.viewflowindic);
        mViewFlow.setParentView(MarketActivity.mViewPager, null, null);
        mLoadingDialog = new LoadingDialog(MarketActivity.this);
        carCountTv = findView(R.id.circle_car_text);
        mHorizontalScrollView = findView(R.id.horizontalScrollView);
        empty_view = findView(R.id.empty_view);
        iv_handle = findView(R.id.iv_handle);
        tv_title = findView(R.id.tv_title);
        iv_handle.setOnClickListener(this);
        findView(R.id.iv_shopping_cart).setOnClickListener(this);
        mRadioGroup = findView(R.id.radio_group);
        mViewPager = findView(R.id.view_pager_pin_huo);
        mPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
                getSupportFragmentManager().beginTransaction().detach((Fragment) object);
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return ListUtils.getSize(mFragments);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container, position);
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                RadioButton btn = (RadioButton) mRadioGroup.getChildAt(position);
                btn.setChecked(true);
                fid = (int) btn.getTag();
                getItemData();
//                if (datas.get(catIDs.indexOf(curCategoryID)) == null) {
//                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));
//                } else {
//                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));//也刷新
//                }
//
//                if (ads.get(curCategoryID + "") == null) {
//                    readAdList();
//                } else {
//                    PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
//                    _data.CurrCategoryID = curCategoryID;
//                    _data.adList = ads.get(curCategoryID + "");
//                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
//                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    int selectIndex;

    private void getData() {
        MarketApi.getStallList(new RequestEntity(Vthis, new HttpRequestHelper(), new SimpleHttpRequestListener() {
            @Override
            public void onRequestSuccess(String method, Object object) {
                closeDialog(mLoadingDialog);
                try {
                    StallsAllListBean bean = (StallsAllListBean) object;
                    data.clear();
                    fList.clear();
                    sList.clear();
                    List<StallsAllListBean.MarketListBean> list = bean.getMarketList();
                    List<StallsAllListBean.FloorListBean> floorListBeen = bean.getFloorList();
                    List<StallsAllListBean.ShopListBean> shopList = bean.getShopList();
                    if (shopList != null) {
                        sList.addAll(shopList);
                    }
                    if (bean != null) {
                        mid = bean.getMarketID();
                        fid = bean.getFloorID();
                        getAd();
                    }
                    if (list != null) {
                        data.addAll(list);
                        if (ListUtils.isEmpty(data)) {
                            iv_handle.setVisibility(View.GONE);
                        } else {
                            if (data.size() > 1) {
                                iv_handle.setVisibility(View.VISIBLE);
                            } else {
                                iv_handle.setVisibility(View.GONE);
                            }
                        }
                        for (StallsAllListBean.MarketListBean marketListBean : data) {
                            if (mid == marketListBean.getID()) {
                                String name = marketListBean.getName();
                                if (!TextUtils.isEmpty(name))
                                    tv_title.setText(name);
                            }
                        }

                    }
                    if (floorListBeen != null) {
                        fList.addAll(floorListBeen);
                        if (!ListUtils.isEmpty(fList)) {
                            mViewPager.setOffscreenPageLimit(fList.size() - 1);
                            clear();
                            catIDs.clear();
                            mRadioGroup.removeAllViews();
                            selectIndex = 0;
                            int index1 = 0;
                            for (StallsAllListBean.FloorListBean fBean : fList) {
                                catIDs.add(fBean.getID());
                                mFragments.add(MarketFragment.getInstance(new ArrayList<StallsAllListBean.ShopListBean>(), mid, fBean.getID()));
                                RadioButton tempButton = (RadioButton) Vthis.getLayoutInflater().inflate(R.layout.radiobutton_pinhuo_new, null);
                                tempButton.setText(fBean.getName());
                                tempButton.setTag(fBean.getID());
                                tempButton.setOnClickListener(Vthis);
                                if (fid > 0 && fid == fBean.getID()) {
                                    selectIndex = index1;
                                }
                                mRadioGroup.addView(tempButton, FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.MATCH_PARENT);
                                index1++;
                            }
                            mViewPager.setAdapter(mPagerAdapter);

                            ((RadioButton) mRadioGroup.getChildAt(selectIndex)).setChecked(true);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCurrentCheckedRadioLeft = ((RadioButton) mRadioGroup.getChildAt(selectIndex)).getLeft();
                                    mHorizontalScrollView.scrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
                                }
                            }, 10);
                            // mViewPager.setCurrentItem(catIDs.indexOf(fid));
                            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
                                    int radioButtonId = group.getCheckedRadioButtonId();
                                    //根据ID获取RadioButton的实例
                                    RadioButton rb = (RadioButton) findViewById(radioButtonId);
                                    //                               AnimationSet animationSet = new AnimationSet(true);
                                    //                               TranslateAnimation translateAnimation;
                                    //                               translateAnimation = new TranslateAnimation(mCurrentCheckedRadioLeft, rb.getLeft(), 0f, 0f);
                                    //                               animationSet.addAnimation(translateAnimation);
                                    //                               animationSet.setFillBefore(true);
                                    //                               animationSet.setFillAfter(true);
                                    //                               animationSet.setDuration(300);
                                    //                               mImageView.startAnimation(animationSet);//开始上面蓝色横条图片的动画切换
                                    // mViewPager.setCurrentItem(radioButtonId);//让下方ViewPager跟随上面的HorizontalScrollView切换
                                    mCurrentCheckedRadioLeft = rb.getLeft();//更新当前蓝色横条距离左边的距离
                                    mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
                                }
                            });
                            mPagerAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRequestStart(String method) {
                super.onRequestStart(method);
                showDialog(mLoadingDialog, "正在加载档口数据");
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                super.onRequestFail(method, statusCode, msg);
                closeDialog(mLoadingDialog);
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                super.onRequestExp(method, msg, data);
                closeDialog(mLoadingDialog);
            }
        }), mid, fid);
    }

    private void getItemData() {
        EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_MARKET_SUB_ITEMS, fid));

    }

    private void clear() {
        mFragments.clear();
        mViewPager.removeAllViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadGoodsTask(this, carCountTv).execute();
    }

    @Override
    public void onClick(final View v) {
        if (v.getClass().equals(RadioButton.class)) {
            fid = (int) v.getTag();
            mViewPager.setCurrentItem(catIDs.indexOf(fid));
            getItemData();
            //EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, fid));
        } else {
            switch (v.getId()) {
                case R.id.iv_handle:
                    try {
                        if (!ListUtils.isEmpty(data)) {
                            FlipShareView share;
                            FlipShareView.Builder builder = new FlipShareView.Builder(this, iv_handle);
                            for (StallsAllListBean.MarketListBean bean : data) {
                                builder.addItem(new ShareItem(bean.getID(), bean.getName(), Color.BLACK, Color.WHITE, BitmapFactory.decodeResource(getResources(), R.drawable.icon_market_sel)));
                            }
                            share = builder.setBackgroundColor(0x60000000).create();
                            share.setOnFlipClickListener(new FlipShareView.OnFlipClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    StallsAllListBean.MarketListBean stallsBean = data.get(position);
                                    if (!TextUtils.isEmpty(stallsBean.getName())) {
                                        tv_title.setText(stallsBean.getName());
                                    }
                                    mid = stallsBean.getID();
                                    fid = 0;
                                    getData();
                                    getAd1();
                                }

                                @Override
                                public void dismiss() {

                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    FlipShareView share = new FlipShareView.Builder(this, iv_handle)
//
//                            .addItem(new ShareItem("Facebookhttp://www.wangyuwei.me", Color.BLACK, Color.WHITE, BitmapFactory.decodeResource(getResources(), R.drawable.icon_market_sel)))
//                            .addItem(new ShareItem("Twitter", Color.BLACK, Color.WHITE, BitmapFactory.decodeResource(getResources(), R.drawable.icon_market_sel)))
//                            .addItem(new ShareItem("Google+", Color.BLACK, Color.WHITE, BitmapFactory.decodeResource(getResources(), R.drawable.icon_market_sel)))
//                            .addItem(new ShareItem("http://www.wangyuwei.me", Color.WHITE, Color.WHITE))
//                            .setBackgroundColor(0x60000000)
//                            .create();


//                    MarketMenu menu = MarketMenu.getInstance(this, data);
//                    menu.setListener(MarketActivity.this);
//                    menu.show();

                    break;
                case R.id.iv_shopping_cart:
                    Utils.gotoShopcart(this);
                    break;
                case R.id.iv_all_search:
                    CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                    break;
            }
        }
    }

    @Override
    public void onMarketMenuClick(StallsAllListBean.MarketListBean stallsBean) {
        if (!TextUtils.isEmpty(stallsBean.getName())) {
            tv_title.setText(stallsBean.getName());
        }
        mid = stallsBean.getID();
        fid = 0;
        getData();
        getAd();
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.REFRESH_MARKET_ALL_ITEMS:// 刷新
                int catID = (int) event.data;
                if (catID > 0) {
                    fid = catID;
                }
                getData();
                break;
            case EventBusId.REFRESH_MARKET_AD:
                getAd();
                break;
//            case EventBusId.REFRESH_PIN_HUO:
//                loadData(false);
//                break;
        }
    }

    @Override
    public void onRequestStart(String method) {

    }

    @Override
    public void onRequestSuccess(String method, Object object) {
        if (RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS.equals(method)) {
            onDataLoaded(object);
        }
    }

    private RecommendModel mRecommendModel;

    private void onDataLoaded(Object object) {
        mRecommendModel = (RecommendModel) object;
        RecommendModel.InfoEntity ie = mRecommendModel.getInfo();
        PinHuoModel item = new PinHuoModel();
        item.setUrl(ie.getUrl());
        item.setID(ie.getID());
        item.IsStart = ie.isIsStart();
        item.setAppCover(ie.getAppCover());
        item.setPicAd(false);
        item.setDescription(ie.getDescription());
        item.setGroupDealCount(ie.getChengTuanCount());
        item.setName(ie.getName());
        item.setPCCover(ie.getPCCover());
        long l = ie.getToTime();
        item.setStartMillis(ie.getStartTime());
        long ll = ie.getEndMillis();
        item.setEndMillis(ie.getToTime());
        item.setLimitPoint(ie.getLimitPoint());
        item.setLimitShopAuth(ie.isLimitShopAuth());
        item.setVisitResult(ie.getVisitResult());
        item.setActivityType(ie.getActivityType());
        item.setHasNewItems(mRecommendModel.NewItems.size() > 0 ? true : false);
        ViewUtil.gotoChangci(Vthis, item);
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {

    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {

    }
}
