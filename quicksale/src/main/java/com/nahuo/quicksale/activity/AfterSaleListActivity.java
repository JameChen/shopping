package com.nahuo.quicksale.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.fragment.AfterFragment;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class AfterSaleListActivity extends BaseAppCompatActivity implements View.OnClickListener {
    TextView mETTitle;
    Button tvTLeft;
    TabLayout tabLayout;
    ViewPager viewPager;
    EditText mEtSearch;
    private EventBus mEventBus = EventBus.getDefault();
    CollectAdapter adapter;
    List<AfterFragment> mList = new ArrayList<>();


    private boolean isEtSearchShowing() {
        return mEtSearch.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onBackPressed() {
        if (isEtSearchShowing()) {
            ViewHub.hideKeyboard(this);
            mEtSearch.setVisibility(View.GONE);
            mETTitle.setVisibility(View.VISIBLE);
            mEtSearch.setText("");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_sale_list);
        mEventBus.register(this);
        mETTitle = (TextView) findViewById(R.id.title_name);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chang();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return true;
            }
        });
        findViewById(R.id.iv_search).setOnClickListener(this);
        mETTitle.setText("售后单");
        tvTLeft = (Button) findViewById(R.id.iv_back);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.page);
        tvTLeft.setOnClickListener(this);
        adapter = new CollectAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                chang();
//                switch (tab.getPosition()) {
//                    case 0:
//                        tv_title.setText("拼单中的商品");
//                        break;
//                    case 1:
//                        tv_title.setText("拼单中的商品");
//                        break;
//                }
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
        for (int i = 0; i < 3; i++) {
            mList.add(AfterFragment.newInstance(i)
            );
        }
    }

    private void chang() {
        try {
            mList.get(viewPager.getCurrentItem()).chang(mEtSearch.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CollectAdapter extends FragmentPagerAdapter {

        private String titles[] = {"全部", "处理中", "已处理"};

        public CollectAdapter(FragmentManager supportFragmentManager, Context context) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_search:// 搜索
                if (isEtSearchShowing()) {// 如果搜索栏显示，直接搜索
                    if (!TextUtils.isEmpty(mEtSearch.getText().toString().trim()))
                        search();
                } else {
                    mEtSearch.setVisibility(View.VISIBLE);
                    mETTitle.setVisibility(View.GONE);
                    mEtSearch.requestFocus();
                    ViewHub.showKeyboard(this, mEtSearch);
                }

                break;
        }
    }

    public void onEventMainThread(BusEvent event) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEventBus != null)
            mEventBus.unregister(this);
    }


    private void search() {
       /* if (mLister != null)
            mLister.search();*/
        try {
            mList.get(viewPager.getCurrentItem()).search();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
