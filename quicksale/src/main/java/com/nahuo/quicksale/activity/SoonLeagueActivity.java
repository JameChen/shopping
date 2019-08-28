package com.nahuo.quicksale.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.nahuo.constant.UmengClick;
import com.nahuo.quicksale.CollectionsFragment;
import com.nahuo.quicksale.CommonSearchActivity;
import com.nahuo.quicksale.GoodsFrament;
import com.nahuo.quicksale.MyCollectionsActivity;
import com.nahuo.quicksale.MyFollowsFrament;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.BaseFragmentActivity;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.fragment.IntoGroupFragment;
import com.nahuo.quicksale.util.UMengTestUtls;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
/**
 * 拼货成团
 *@author  James Chen
 *@create time in 2017/6/21 10:25
 */
public class SoonLeagueActivity extends BaseFragmentActivity implements
        View.OnClickListener  {
    private GoodsFrament mAlreadyPinFragment;
    private MyFollowsFrament mMyFollowsFragment;
    private MyCollectionsActivity mCollectionsFragment;
    private CollectionsFragment collectionsFragment;
    private int mCurPos;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  List<Fragment> fList=new ArrayList<>();
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soon_league);
        initTitleBar();
        initView();
        EventBus.getDefault().registerSticky(this);
    }

    private void initView() {
        tv_title=(TextView)findViewById(R.id.tv_title);
        tabLayout= (TabLayout) findViewById(R.id.collect_tabs);
        viewPager= (ViewPager) findViewById(R.id.collect_view_page);
        tv_title.setText("即将成团的商品");
//        mMyFollowsFragment=new MyFollowsFrament();
//        collectionsFragment=new CollectionsFragment();

        mAlreadyPinFragment=new GoodsFrament();
        //mCollectionsFragment=new MyCollectionsActivity();
        //fList.add(mMyFollowsFragment);
       // fList.add(collectionsFragment);
       // fList.add(mAlreadyPinFragment);
        viewPager.setAdapter(new CollectAdapter(getSupportFragmentManager(),this));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(1);
        UMengTestUtls.UmengOnClickEvent(SoonLeagueActivity.this, UmengClick.Click17);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                UMengTestUtls.UmengOnClickEvent(SoonLeagueActivity.this, UmengClick.Click17);
                switch (tab.getPosition()) {
                    case 0:
                        tv_title.setText("拼单中的商品");
                        break;
                    case 1:
                        tv_title.setText("拼单中的商品");
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

        private String titles [] = {"即将成团","已拼成团"};

        public CollectAdapter(FragmentManager supportFragmentManager, Context context) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return IntoGroupFragment.newInstance(position);
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



    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

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
                CommonSearchActivity.SearchType type = null;
               switch (mCurPos) {
                    case 0:
                        type = CommonSearchActivity.SearchType.PIN_HUO_Follows;
                        break;
                    case 1:
                        type = CommonSearchActivity.SearchType.PIN_HUO_COLLECTIONS;
                        break;
                    case 2:
                        type = CommonSearchActivity.SearchType.PIN_HUO_ALREADY;
                        break;
                }
                CommonSearchActivity.launch(this, CommonSearchActivity.SearchType.ALL_ITEM_SEARCH);
                break;

        }
    }

}
