package com.nahuo.quicksale;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nahuo.constant.UmengClick;
import com.nahuo.quicksale.util.UMengTestUtls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ALAN on 2017/4/24 0024.
 * 优惠券(Activity)
 */
public class CouponActivity extends FragmentActivity implements View.OnClickListener{
    TabLayout tabLayout;
    ViewPager viewPager;

    private TextView tvTitle;
    private Button btnLeft;

    private List<Fragment> mList=new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_group);
        View title=findViewById(R.id.include_coupon);
        initTitle(title);
        initView();
        initData();
    }

    private void initTitle(View title){
        // 标题栏
        tvTitle = (TextView) title.findViewById(R.id.titlebar_tvTitle);
        btnLeft = (Button) title.findViewById(R.id.titlebar_btnLeft);
        tvTitle.setText("优惠券");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
    }
    private void initView(){

        viewPager = (ViewPager) findViewById(R.id.coupon_vp_view);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(),getApplicationContext(),mList));

        tabLayout = (TabLayout) findViewById(R.id.coupon_tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //ViewHub.showShortToast(CouponActivity.this,tab.getPosition()+"");
                if (tab.getPosition()==1){
                    UMengTestUtls.UmengOnClickEvent(CouponActivity.this, UmengClick.Click14);
                }else  if (tab.getPosition()==2){
                    UMengTestUtls.UmengOnClickEvent(CouponActivity.this, UmengClick.Click15);
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

    private void initData(){

        for(int i=0;i<3;i++) {
            CouponFragment coupon = new CouponFragment();
            coupon.setType(i);
            mList.add(coupon);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                this.finish();
                break;
        }
    }


    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments [] = {"未使用","已使用","已过期"};

        private List<Fragment> mList;

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext,List<Fragment> mList) {
            super(supportFragmentManager);
            this.mList=mList;
        }

        @Override
        public Fragment getItem(int position) {
         return mList.get(position);
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }


}
