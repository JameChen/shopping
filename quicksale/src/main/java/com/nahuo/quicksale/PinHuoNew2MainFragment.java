//package com.nahuo.quicksale;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
//import android.support.v7.widget.AppCompatRadioButton;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.HorizontalScrollView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import com.nahuo.bean.AdBean;
//import com.nahuo.constant.SpConstant;
//import com.nahuo.constant.UmengClick;
//import com.nahuo.library.controls.CircleTextView;
//import com.nahuo.library.controls.FlowLayout;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.QuickSaleApi;
//import com.nahuo.quicksale.api.SimpleHttpRequestListener;
//import com.nahuo.quicksale.common.ListUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.model.BannerAdModel;
//import com.nahuo.quicksale.model.PinHuoCategroyModel;
//import com.nahuo.quicksale.model.PinHuoModel;
//import com.nahuo.quicksale.model.RequestEntity;
//import com.nahuo.quicksale.model.ResultData;
//import com.nahuo.quicksale.model.quicksale.PinHuoNewResultModel;
//import com.nahuo.quicksale.model.quicksale.PinHuoNewResultModel_Map;
//import com.nahuo.quicksale.mvp.view.PinHuoView;
//import com.nahuo.quicksale.util.ChatUtl;
//import com.nahuo.quicksale.util.CircleCarTxtUtl;
//import com.nahuo.quicksale.util.ListDataSave;
//import com.nahuo.quicksale.util.UMengTestUtls;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 拼货滑动子目录
// *
// * @author James Chen
// * @create time in 2017/3/24 16:23
// */
//public class PinHuoNew2MainFragment extends BaseFragment implements OnClickListener, PinHuoView {
//    private static final String TAG = PinHuoNew2MainFragment.class.getSimpleName();
//    private static final String EXTRA_PIN_HUO_RESULT = "EXTRA_PIN_HUO_RESULT";
//    private TextView mTvTitle, mTvForecase;
//    private RadioGroup mRadioGroup;
//    static public ViewPager mViewPager;
//    private int curCategoryID;
//    private FragmentStatePagerAdapter mPagerAdapter;
//    private List<Fragment> mFragments = new ArrayList<>();
//    private List<PinHuoNewResultModel> datas;
//    private List<Integer> catIDs;
//    private CircleTextView carCountTv;
//    private Map<String, AdBean> ads = new HashMap<>();
//    public static final String CARGOODSCOUNT = "CARGOODSCOUNT";
//    public ListDataSave save;
//    public List<String> recordIDs = new ArrayList<>();
//    public List<Boolean> flags = new ArrayList<>();
//    public List<String> typeIDs = new ArrayList<>();
//    public List<BannerAdModel> bData = new ArrayList<>();
//    private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
//    int mCurrentCheckedRadioLeft;
//    private View layout_explain;
//
//    private enum Step {
//        LOAD_GOODS_COUNT
//    }
//
//    public static PinHuoNew2MainFragment getInstance(PinHuoNewResultModel model) {
//        PinHuoNew2MainFragment fragment = new PinHuoNew2MainFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_PIN_HUO_RESULT, model);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mContentView = inflater.inflate(R.layout.frgm_pin_huo_new2_main, container, false);
//        EventBus.getDefault().registerSticky(this);
//        initViews();
//        initData();
//        readPopAdList();
//        readAdList();
//        return mContentView;
//    }
//
//    //读取弹窗广告
//    private void readPopAdList() {
//
//        QuickSaleApi.getPinHuoPopupAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//
//                Log.d("yu", object.toString());
//                recordIDs.clear();
//                typeIDs.clear();
//                flags.clear();
//                bData.clear();
//                List<BannerAdModel> data = (List<BannerAdModel>) object;
//                if (data == null || data.size() <= 0)
//                    return;
//                for (BannerAdModel bean : data) {
//                    typeIDs.add(bean.getTypeID() + "");
//                }
//                if (typeIDs.contains("5")) {
//                    //强制点击更新
//                    for (BannerAdModel bean : data) {
//                        bean.del_flag = true;
//                        bData.add(bean);
//                    }
//                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, bData));
//                    return;
//                }
//                String use_id = SpManager.getUserId(mActivity) + "";
//                save = new ListDataSave(getActivity(), SpConstant.SP_PIHUO_AD + use_id);
//                List<String> list = save.getDataList(SpConstant.PIHUO_AD_LIST + use_id);
//                if (list == null || list.size() <= 0) {
//                    for (BannerAdModel bean : data) {
//                        recordIDs.add(bean.getRecordID() + "");
//                    }
//                    save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, recordIDs);
//                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, data));
//
//                } else {
//                    if (data != null || data.size() > 0) {
//
//                        for (int i = 0; i < data.size(); i++) {
//                            if (!list.contains(String.valueOf(data.get(i).getRecordID()))) {
//                                list.add(String.valueOf(data.get(i).getRecordID()));
//                                save.setDataList(SpConstant.PIHUO_AD_LIST + use_id, list);
//                                flags.add(true);
//                            }
//                        }
//                        if (flags.contains(true))
//                            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, data));
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                //EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, null));
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                // EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_POP_AD_FINISHED, null));
//            }
//        }), FunctionHelper.GetAppVersionCode(mAppContext));
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//
//    private void initData() {
//        PinHuoNewResultModel firstModel = (PinHuoNewResultModel) getArguments().getSerializable(EXTRA_PIN_HUO_RESULT);
//        datas = new ArrayList<>();
//        catIDs = new ArrayList<>();
//        for (PinHuoCategroyModel pcm : firstModel.CategoryList) {
//            catIDs.add(pcm.Cid);
//            if (pcm.getCid() == firstModel.CurrCategoryID) {
//                curCategoryID = firstModel.CurrCategoryID;
//                datas.add(firstModel);
//            } else {
//                datas.add(null);
//            }
//        }
//        onAllDataLoaded();
//    }
//
//    int useId, pinhuo_main_furst;
//
//    private void initViews() {
//        //分类
//        layout_explain = $(R.id.layout_explain);
//        if (SpManager.getUserId(BWApplication.getInstance()) > 0)
//            EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
////        useId = SpManager.getUserId(BWApplication.getInstance());
////        if (useId>0) {
////            GuidePreference.init(getActivity());
////            pinhuo_main_furst = GuidePreference.getInstance().getPinhuoMain(useId + "");
////            if (pinhuo_main_furst == 1) {
////                layout_explain.setVisibility(View.GONE);
////            } else {
////                layout_explain.setVisibility(View.VISIBLE);
////            }
////        }
////        layout_explain.setOnClickListener(new OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                GuidePreference.getInstance().setPinhuoMain(useId+"",1);
////                layout_explain.setVisibility(View.GONE);
////                EventBus.getDefault().post( BusEvent.getEvent(EventBusId.PINHUO_TAB_BOTTOM_LAYOUT));
////
////            }
////        });
//        mHorizontalScrollView = $(R.id.horizontalScrollView);
//        carCountTv = $(R.id.circle_car_text);
//        CircleCarTxtUtl.setColor(carCountTv);
//        mRadioGroup = $(R.id.radio_group);
//        mTvTitle = $(R.id.tv_title);
//        $(R.id.ivtopright).getBackground().setAlpha(200);
//        mViewPager = $(R.id.view_pager_pin_huo);
//        mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return ListUtils.getSize(mFragments);
//            }
//
//        };
//        mViewPager.setAdapter(mPagerAdapter);
//        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                RadioButton btn = (RadioButton) mRadioGroup.getChildAt(position);
//                btn.setChecked(true);
//                BWApplication.PINHUPTITLE=btn.getText()+"";
//                // ViewHub.showShortToast(getActivity(), btn.getText() + "");
//                HashMap<String, String> hashMap = new HashMap<>();
//                hashMap.put("type", btn.getText() + "");
//                UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click10, hashMap);
//                curCategoryID = (int) btn.getTag();
//                if (datas.get(catIDs.indexOf(curCategoryID)) == null) {
//                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));
//                } else {
////                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_FINISHED, datas.get(catIDs.indexOf(curCategoryID))));
//                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));//也刷新
//                }
//                readAdList();
////                if (ads.get(curCategoryID + "") == null) {
////                    readAdList();
////                } else {
////                    PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
////                    _data.CurrCategoryID = curCategoryID;
////                    _data.adList = ads.get(curCategoryID + "").getBannerAdModelList();
////                    _data.setCustomHtml(ads.get(curCategoryID + "").getCustomHtml());
////                    EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
////                }
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//        $(R.id.tv_all).setOnClickListener(this);
//        $(R.id.iv_shopping_cart).setOnClickListener(this);
//        $(R.id.iv_chat_txt).setOnClickListener(this);
//        $(R.id.iv_all_search).setOnClickListener(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.v(TAG, "start car goods");
//        ChatUtl.setChatBroad(getActivity());
//        // new LoadGoodsTask(getActivity(), carCountTv).execute();
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        if (v.getClass().equals(AppCompatRadioButton.class)) {
//            curCategoryID = (int) v.getTag();
//            mViewPager.setCurrentItem(catIDs.indexOf(curCategoryID));
//
////            if (datas.get(catIDs.indexOf(curCategoryID)) == null) {
//            EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));
////            }
////            else {
////                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_FINISHED, datas.get(catIDs.indexOf(curCategoryID))));
////
////                PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
////                _data.CurrCategoryID = curCategoryID;
////                _data.adList = ads.get(curCategoryID+"");
////                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
////            }
//        } else {
//            switch (v.getId()) {
////                case R.id.my_collect:
////                    Intent tuan = new Intent(mActivity, MyMainCollectionActivity.class);
////                    startActivity(tuan);
////                    break;
//
//                case R.id.tv_all:
////                    //正在开拼
//                    Intent it = new Intent(mActivity, PinHuoActivity.class);
//                    //   it.putExtra(CARGOODSCOUNT, carCountTv.getmCircleText().getText().toString());
//                    startActivity(it);
//                    //我的关注
//                    //startActivity(new Intent(mActivity, FocusActivity.class));
//                    break;
//                case R.id.iv_shopping_cart:
//                    Utils.gotoShopcart(mActivity);
//                    break;
//                case R.id.iv_chat_txt:
////                    Intent intent = new Intent(getActivity(), ChatMainActivity.class);
////                    intent.putExtra(ChatMainActivity.ETRA_LEFT_BTN_ISHOW, true);
////                    startActivity(intent);
//                    UMengTestUtls.UmengOnClickEvent(getActivity(),UmengClick.Click11);
//                    ChatUtl.goToChatMainActivity(getActivity(), true);
//                    break;
//                case R.id.iv_all_search:
//                    UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click1);
//                    CommonSearchActivity.launch(mActivity, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
//                    break;
//            }
//        }
//    }
//
//
//    @Override
//    public void showLoading() {
//
//    }
//
//    @Override
//    public void hideLoading() {
//
//    }
//
//    @Override
//    public void onLoadPinAndForecastFailed() {
//
//    }
//
//    @Override
//    public void onAllDataLoaded(ArrayList<PinHuoModel> pinList,
//                                ArrayList<PinHuoModel> forecastList,
//                                ArrayList<PinHuoModel> historyList) {
//
//    }
//
//    int selectIndex;
//
//    public void onAllDataLoaded() {
//        int index = 0;
//        PinHuoNewResultModel nowData = null;
//        for (PinHuoNewResultModel pm : datas) {
//            if (pm != null) {
//                nowData = pm;
//                mFragments.add(PinhuoNew2Fragment.getInstance(
//                        pm.ActivityList,
//                        pm.CurrCategoryID,
//                        ads.get(curCategoryID + "")));
//            } else {
//                mFragments.add(PinhuoNew2Fragment.getInstance(
//                        new ArrayList<PinHuoModel>(),
//                        catIDs.get(index),
//                        null));
//            }
//            index++;
//        }
//        mPagerAdapter.notifyDataSetChanged();
//
//        //加载分类
//        if (nowData != null) {
//            mRadioGroup.removeAllViews();
//            selectIndex = 0;
//            int index1 = 0;
//            for (PinHuoCategroyModel pcm : nowData.CategoryList) {
//                RadioButton tempButton = (RadioButton) mActivity.getLayoutInflater().inflate(R.layout.radiobutton_pinhuo_new, null);
//                tempButton.setText(pcm.getName());
//                if (index1 == 0) {
//                    // ViewHub.showShortToast(getActivity(),pcm.getName()+"");
//                    BWApplication.PINHUPTITLE=pcm.getName();
//                    HashMap<String, String> hashMap = new HashMap<>();
//                    hashMap.put("type", pcm.getName() + "");
//                    UMengTestUtls.UmengOnClickEvent(getActivity(), UmengClick.Click10, hashMap);
//                }
//                tempButton.setTag(pcm.getCid());
//                tempButton.setOnClickListener(this);
//                if (curCategoryID > 0 && curCategoryID == pcm.getCid()) {
//                    selectIndex = index1;
//                }
//                mRadioGroup.addView(tempButton, FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.MATCH_PARENT);
//                index1++;
//            }
//            ((RadioButton) mRadioGroup.getChildAt(selectIndex)).setChecked(true);
////            new Handler().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    mCurrentCheckedRadioLeft = ((RadioButton) mRadioGroup.getChildAt(selectIndex)).getLeft();
////                    mHorizontalScrollView.smoothScrollTo((int)mCurrentCheckedRadioLeft-(int)getResources().getDimension(R.dimen.rdo2), 0);
////                }
////            },10);
//            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    //Map<String, Object> map = (Map<String, Object>) group.getChildAt(checkedId).getTag();
//                    int radioButtonId = group.getCheckedRadioButtonId();
//                    //根据ID获取RadioButton的实例
//                    RadioButton rb = (RadioButton) getActivity().findViewById(radioButtonId);
//                    mCurrentCheckedRadioLeft = rb.getLeft();//更新当前蓝色横条距离左边的距离
//                    mHorizontalScrollView.smoothScrollTo((int) mCurrentCheckedRadioLeft - (int) getResources().getDimension(R.dimen.rdo2), 0);
//                }
//            });
//        }
//    }
//
//    private void readAdList() {
//        //读取广告
////        QuickSaleApi.getPinHuoNewAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
////            @Override
////            public void onRequestSuccess(String method, Object object) {
////                ArrayList<BannerAdModel> adList = (ArrayList<BannerAdModel>) object;
////                PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
////                _data.CurrCategoryID = curCategoryID;
////                _data.adList = adList;
////                ads.put(curCategoryID + "", adList);
////                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
////            }
////
////            @Override
////            public void onRequestFail(String method, int statusCode, String msg) {
////                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
////            }
////
////            @Override
////            public void onRequestExp(String method, String msg, ResultData data) {
////                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
////            }
////        }), curCategoryID);
//        QuickSaleApi.getPinHuoV2NewAd(new RequestEntity(mActivity, new HttpRequestHelper(), new SimpleHttpRequestListener() {
//            @Override
//            public void onRequestSuccess(String method, Object object) {
////                Log.d("ee",object.toString());
////                Log.d("ee",object.toString());
//                AdBean adBean = (AdBean) object;
//                PinHuoNewResultModel_Map _data = new PinHuoNewResultModel_Map();
//                _data.CurrCategoryID = curCategoryID;
//                if (adBean == null) {
//                    _data.adList = new ArrayList<BannerAdModel>();
//                    _data.setCustomHtml("");
//                    // _data.setCustomHtml("<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>");
//                } else {
//                    _data.adList = adBean.getBannerAdModelList();
//                    _data.setCustomHtml(adBean.getCustomHtml());
//                    //  _data.setCustomHtml("<p>\\n<input type=\\\"button\\\" value=\\\"充值\\\" onclick=\\\"javascript:GoToRecharge();\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"商品详细\\\" onclick=\\\"javascript:GoToItemDetail('7007061','27379');\\\" \\/>\\n<input type=\\\"button\\\" value=\\\"场次列表\\\" onclick=\\\"javascript:GoToQsList('27379');\\\" \\/>\\n<\\/p>");
//                }
//                ads.put(curCategoryID + "", adBean);
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, _data));
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.LOAD_PIN_HUO_AD_FINISHED, null));
//            }
//        }), curCategoryID);
//
//    }
//
//    private void clear() {
//        mFragments.clear();
//        mViewPager.removeAllViewsInLayout();
//    }
//
//    public void onEventMainThread(BusEvent event) {
//        switch (event.id) {
//            case EventBusId.WEIXUN_NEW_MSG:
//                if (carCountTv == null) {
//                    return;
//                }
//                String num = event.data.toString();
//                if (TextUtils.isEmpty(num)) {
//                    carCountTv.setVisibility(View.GONE);
//                } else {
//                    carCountTv.setVisibility(View.VISIBLE);
//                    carCountTv.setText(event.data.toString());
//                }
//                break;
//            case EventBusId.PIN_HUO_SELECT_RB:
//                int id = (int) event.data;
//
//                int selectIndex = -1;
//                int index = 0;
//                for (int _id : catIDs) {
//                    if (id == _id) {
//                        selectIndex = index;
//                    }
//                    index++;
//                }
//                if (selectIndex >= 0) {
//                    RadioButton rb = (RadioButton) mRadioGroup.getChildAt(selectIndex);
//                    rb.setChecked(true);
//                    curCategoryID = (int) rb.getTag();
//                    mViewPager.setCurrentItem(catIDs.indexOf(curCategoryID));
//                    EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.REFRESH_ALL_ITEMS, curCategoryID));
//                    // loadData(true);
//                }
//                break;
//            case EventBusId.PINHUO_AD_REFRESH:
//                readPopAdList();
//                break;
////            case EventBusId.LOAD_PIN_HUO_FINISHED:
////                PinHuoNewResultModel data1 = (PinHuoNewResultModel)event.data;
////                if (data1!=null)
////                {
////                            for(int i = 0;i<catIDs.size();i++)
////                            {
////                                if (catIDs.get(i)==data1.CurrCategoryID)
////                                {
////                                    datas.set(i,data1);
////                                }
////                            }
////                }
////                break;
//        }
//    }
//
//
//}
