package com.nahuo.quicksale.Topic;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.oldermodel.PageraModel.TabModel;
import com.nahuo.quicksale.pageindicator.TabPageIndicatorBackIcon;
import com.nahuo.quicksale.pageradapter.BasePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */
public class MyTopicAndActivityActivity  extends BaseFragmentActivity implements
        View.OnClickListener {
    private CollectionActivityTabFragment activityFragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycollect);
        initTitleBar();
        initTabs();

    }

    private void initTitleBar() {

        findViewById(R.id.top_left_back).setOnClickListener(this);
    }



    private void initTabs() {

        BasePagerAdapter pagerAdapter = new BasePagerAdapter(
                getSupportFragmentManager());
        List<TabModel> tabs = new ArrayList<TabModel>();

        TabModel agentTab = new TabModel();
        agentTab.setFragment(new  CollectionNoteTabFragment());
        agentTab.setPageTitle("话题");
        agentTab.setIconResId(R.drawable.tab_circle_rect_left) ;
        TabModel hotspotTab = new TabModel();
        activityFragment =new  CollectionActivityTabFragment() ;
        hotspotTab.setFragment(activityFragment);
        hotspotTab.setPageTitle("活动");
        hotspotTab.setIconResId(R.drawable.tab_circle_rect_right);
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

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityFragment = null ;
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
            default:
                break;
        }
    }

}
