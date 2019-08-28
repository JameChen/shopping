//package com.nahuo.quicksale;
//
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//
//import com.baidu.mobstat.StatService;
//import com.nahuo.quicksale.common.LastActivitys;
//import com.nahuo.quicksale.common.Utils;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//
///**
// * @author ZZB
// * @description 供货商
// * @created 2015-4-20 上午11:15:29
// */
//public class AllItemsActivity2 extends FragmentActivity implements View.OnClickListener {
//
//    private ViewPager mViewPager;
//    private FragmentPagerAdapter mPagerAdapter;
//    private List<Fragment> mFragments = new ArrayList<Fragment>();
//    private EventBus mEventBus = EventBus.getDefault();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_items2);
//        initView();
//        mEventBus.registerSticky(this);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (getParent() != null) {
//            getParent().onBackPressed();
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    private void initView() {
//        initFragments();
//    }
//
//    private void initFragments() {
//        mViewPager = (ViewPager) findViewById(R.id.view_pager);
////        Fragment allItemFragment = new QuickSellFragment();
////        mFragments.add(allItemFragment);
//        mFragments.add(new PinHuoDetailFragment());
//        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public int getCount() {
//                return mFragments.size();
//            }
//
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments.get(position);
//            }
//        };
//
//        mViewPager.setAdapter(mPagerAdapter);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.iv_shopping_cart:
//                Utils.gotoShopcart(this);
//                break;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        StatService.onPause(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        StatService.onResume(this);
//        LastActivitys.getInstance().clear();
//        LastActivitys.getInstance().addView(getWindow().getDecorView());
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
//            case EventBusId.ALL_ITEM_CHANGE_CURRENT_TAB:
//                try {
//                    int item = Integer.valueOf(event.data.toString()).intValue();
//                    mViewPager.setCurrentItem(item);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
//}
