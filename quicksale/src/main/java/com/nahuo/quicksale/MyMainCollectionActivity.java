package com.nahuo.quicksale;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nahuo.quicksale.CommonSearchActivity.SearchType;
import com.nahuo.quicksale.Topic.BaseFragmentActivity;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.fragment.FootPrintFragment;
import com.nahuo.quicksale.oldermodel.PageraModel.TabModel;
import com.nahuo.quicksale.pageindicator.TabPageIndicatorBackIcon;
import com.nahuo.quicksale.pageradapter.BasePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by 诚 on 2015/9/28.
 */
public class MyMainCollectionActivity extends BaseFragmentActivity implements
        View.OnClickListener {
    private GoodsFrament mAlreadyPinFragment;
    private MyFollowsFrament mMyFollowsFragment;
    private MyCollectionsActivity mCollectionsFragment;
    private CollectionsFragment collectionsFragment;
    private FootPrintFragment footPrintFragment;
    private int mCurPos;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  List<Fragment> fList=new ArrayList<>();
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_mycollect);
        initTitleBar();
        initView();
        //initTabs();
        EventBus.getDefault().registerSticky(this);

    }

    private void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        tabLayout= (TabLayout) findViewById(R.id.collect_tabs);
        viewPager= (ViewPager) findViewById(R.id.collect_view_page);
        tv_title.setText("我的关注");
        mMyFollowsFragment=new MyFollowsFrament();
        collectionsFragment=new CollectionsFragment();
        mAlreadyPinFragment=new GoodsFrament();
        mCollectionsFragment=new MyCollectionsActivity();
        footPrintFragment=new FootPrintFragment();
        fList.add(mMyFollowsFragment);
        fList.add(collectionsFragment);
        fList.add(footPrintFragment);
      //  fList.add(mAlreadyPinFragment);
        viewPager.setAdapter(new CollectAdapter(getSupportFragmentManager(),this));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        tv_title.setText("我的关注");
                        break;
                    case 1:
                        tv_title.setText("我收藏的款式");
                        break;
                    case 2:
                        tv_title.setText("我的足迹");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

    }
    private class CollectAdapter extends FragmentPagerAdapter {

        private String titles [] = {"关注","收藏","足迹"};


        public CollectAdapter(FragmentManager supportFragmentManager, Context context) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fList.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
    private void initTitleBar() {
        findViewById(R.id.top_left_back).setOnClickListener(this);
        findViewById(R.id.top_right).setOnClickListener(this);
    }


    private void initTabs() {

        BasePagerAdapter pagerAdapter = new BasePagerAdapter(
                getSupportFragmentManager());
        List<TabModel> tabs = new ArrayList<TabModel>();
        TabModel followerTab = new TabModel();
        TabModel agentTab = new TabModel();
        mMyFollowsFragment = new MyFollowsFrament();
        mMyFollowsFragment.parentActivity = this;
        followerTab.setFragment(mMyFollowsFragment);
        followerTab.setPageTitle("关注的团");
        followerTab.setIconResId(R.drawable.tab_circle_rect_left);
        mCollectionsFragment = new MyCollectionsActivity();
        agentTab.setFragment(mCollectionsFragment);
        agentTab.setPageTitle("  收藏款  ");
        agentTab.setIconResId(R.drawable.tab_circle_rect_middle);
    //    agentTab.setIconResId(R.drawable.selector_cb_left);
        TabModel hotspotTab = new TabModel();
//        mCollectionsFragment = new MyCollectionsActivity();
//        hotspotTab.setFragment(mCollectionsFragment);
//        hotspotTab.setPageTitle("  商品  ");
        mAlreadyPinFragment = new GoodsFrament();
        hotspotTab.setFragment(mAlreadyPinFragment);
        hotspotTab.setPageTitle("  已拼款  ");
        hotspotTab.setIconResId(R.drawable.tab_circle_rect_right);
//        hotspotTab.setIconResId(R.drawable.selector_cb_right);
        tabs.add(followerTab);
        tabs.add(agentTab);
        tabs.add(hotspotTab);

        pagerAdapter.setTabs(tabs);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(2);
        // pager.setOnPageChangeListener(null);//需要indicator设置，否则无效
        TabPageIndicatorBackIcon indicator = (TabPageIndicatorBackIcon) findViewById(R.id.indicator);
        indicator.setStyleAttrId(R.attr.my_tabpage_style);
        indicator.setViewPager(pager);
        pager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurPos = position;
//                if (mCurPos==0) {
//                    findViewById(R.id.top_right).setVisibility(View.GONE);
//                } else {
//                    findViewById(R.id.top_right).setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyFollowsFragment = null;
        mAlreadyPinFragment = null;
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.SEARCH_PIN_HUO_ALREADY:
                mAlreadyPinFragment.search(event.data.toString());
                break;
            case EventBusId.SEARCH_PIN_HUO_FOLLOWS:
                mMyFollowsFragment.search(event.data.toString());
                break;
            case EventBusId.SEARCH_PIN_HUO_COLLECTIONS:
                mCollectionsFragment.search(event.data.toString());
                break;
        }
    }

    /**
     * 切换页面
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_left_back:
                finish();
                break;
            case R.id.top_right:
                SearchType type = null;
                switch (mCurPos) {
                    case 0:
                        type = SearchType.PIN_HUO_Follows;
                        break;
                    case 1:
                        type = SearchType.PIN_HUO_COLLECTIONS;
                        break;
                    case 2:
                        type = SearchType.PIN_HUO_ALREADY;
                        break;
                }
                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                break;

        }
    }

}

