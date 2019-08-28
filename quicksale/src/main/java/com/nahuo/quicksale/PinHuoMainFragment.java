package com.nahuo.quicksale;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import com.nahuo.library.controls.CircleTextView;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.oldermodel.quicksale.PinHuoResultModel;
import com.nahuo.quicksale.mvp.view.PinHuoView;
import com.nahuo.quicksale.util.LoadGoodsTask;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZB on 2015/10/23.
 */
public class PinHuoMainFragment extends BaseFragment implements PinHuoView, OnClickListener {
    private static final String TAG=PinHuoMainFragment.class.getSimpleName();
    private static final String EXTRA_PIN_HUO_RESULT = "EXTRA_PIN_HUO_RESULT";
    private TextView mTvTitle, mTvForecase;
    private View mLayoutSwitchBar;
    private ViewPager mViewPager;
    private FragmentStatePagerAdapter mPagerAdapter;
    private List<Fragment> mFragments = new ArrayList<>();
    private RadioButton mRdPining, mRdPinOvered;
    private ArrayList<PinHuoModel> mForecastList;
    private CircleTextView carCountTv;
    private int goodsCount=-1;//购物车中商品数量
    public static PinHuoMainFragment getInstance(PinHuoResultModel model) {
        PinHuoMainFragment fragment = new PinHuoMainFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PIN_HUO_RESULT, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_pin_huo_main, container, false);
        initViews();
        initData();
        return mContentView;
    }
    private void initData() {
        PinHuoResultModel result = (PinHuoResultModel) getArguments().getSerializable(EXTRA_PIN_HUO_RESULT);
        for (PinHuoModel pm : result.ActivityList) {
            pm.IsStart = true;
        }
        onAllDataLoaded(result.ActivityList, result.ReadyList, result.OverList);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG,"start car goods");
        new LoadGoodsTask(getActivity(),carCountTv).execute();
    }

    private void initViews() {
        carCountTv=$(R.id.circle_car_text);
        mRdPining = $(R.id.rd_pin_huo);
        mRdPinOvered = $(R.id.rd_pin_huo_overed);
        mRdPining.setOnClickListener(this);
        mRdPinOvered.setOnClickListener(this);
        $(R.id.iv_back).setOnClickListener(this);
        mTvTitle = $(R.id.tv_title);
        mTvForecase = $(R.id.tv_forecase);
        mTvForecase.setOnClickListener(this);
        mLayoutSwitchBar = $(R.id.layout_switch_bar);
        mViewPager = $(R.id.view_pager_pin_huo);
        mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return ListUtils.getSize(mFragments);
            }

        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mRdPining.setChecked(position == 0);
                mRdPinOvered.setChecked(position == 1);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        $(R.id.iv_shopping_cart).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                mActivity.finish();
                break;
            case R.id.rd_pin_huo:// 正在开拼
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rd_pin_huo_overed:// 拼货预告
                mViewPager.setCurrentItem(1);
                break;
            case R.id.iv_shopping_cart:
                Utils.gotoShopcart(mActivity);
                break;
            case R.id.tv_forecase:
                Intent it = new Intent(mActivity, PinHuoForecaseActivity.class);
                it.putExtra(PinHuoForecaseActivity.EXTRA_LIST_DATA, mForecastList);
                mActivity.startActivity(it);
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onLoadPinAndForecastFailed() {

    }

    private void showOnlyForecast(boolean show) {
//        mTvTitle.setVisibility(show ? View.VISIBLE : View.GONE);
        mLayoutSwitchBar.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAllDataLoaded(ArrayList<PinHuoModel> pinList,
                                ArrayList<PinHuoModel> forecastList,
                                ArrayList<PinHuoModel> overList) {
        mForecastList = forecastList;
        if (ListUtils.isEmpty(pinList)) {
            //only show forecast
//            showOnlyForecast(true);
            mTvTitle.setVisibility(View.VISIBLE);
            mLayoutSwitchBar.setVisibility(View.INVISIBLE);
//            mFragments.add(EmptyOveredFragment.newInstance());
        } else {//隐藏正在拼货
            showOnlyForecast(false);
            mFragments.add(PinHuoFragment.getInstance(pinList));
        }
        if (ListUtils.isEmpty(overList)) {
            //show empty overed
            mFragments.add(EmptyOveredFragment.newInstance());
        } else {//拼货预告
            mFragments.add(PinHuoOveredFragment.getInstance(overList));
        }

        mPagerAdapter.notifyDataSetChanged();
    }

    private void clear() {
        mFragments.clear();
        mViewPager.removeAllViewsInLayout();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
