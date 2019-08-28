package com.nahuo.quicksale.base;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nahuo.quicksale.tab.Constant;
import com.nahuo.quicksale.tabfragment.me.MeTabFragment;
import com.nahuo.quicksale.tabfragment.pinhuo.PinHuoTabFragment;
import com.nahuo.quicksale.tabfragment.shopcart.ShopCartTabFragment;
import com.nahuo.quicksale.tabfragment.sort.SortTabFragment;
import com.nahuo.quicksale.tabfragment.yepin.YePinTabFragment;

import de.greenrobot.event.EventBus;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class TabFragment extends Fragment {
    private static final String TAG = "TabFragment";
    protected FragmentManager mFragmentManager = null;
    protected FragmentTransaction mFragmentTransaction = null;
    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

            if (!hidden) {
                setTabStatusBar();
            }
    }

    private void setTabStatusBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        if (this.getClass().getSimpleName().equals(PinHuoTabFragment.class.getSimpleName())) {
//            Window window = getActivity().getWindow();
//            window.setStatusBarColor(Color.WHITE);
//            window.setNavigationBarColor(Color.WHITE);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        } else  if (this.getClass().getSimpleName().equals(MeTabFragment.class.getSimpleName())) {
//            Window window = getActivity().getWindow();
//            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.transparent));
//            window.setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.transparent));
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        }else {
//            Window window = getActivity().getWindow();
//            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.my_colorPrimary));
//            window.setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.my_colorPrimary));
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        }
//    }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        Log.i(TAG, "onAttach...");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate...");
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        setTabStatusBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onCreateView...");
//		View v = inflater.inflate(R.layout.messages_layout, container, false);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onActivityCreated...");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onStart...");
        super.onStart();

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onResume...");
        super.onResume();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onPause...");
        super.onPause();
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onStop...");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onDestroyView...");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onDestroy...");
        super.onDestroy();
        unSubscribe();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    public static TabFragment newInstance(Context context, String tag) {
        TabFragment baseFragment = null;
        if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_PIN_HUO)) {
            baseFragment = new PinHuoTabFragment();
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_SORT)) {
            baseFragment = new SortTabFragment();
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_YUE_PIN)) {
            baseFragment = new YePinTabFragment();
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_SHOP_CART)) {
            baseFragment = new ShopCartTabFragment();
        } else if (TextUtils.equals(tag, Constant.FRAGMENT_FLAG_ME)) {
            baseFragment = new MeTabFragment();
        }

        return baseFragment;

    }

}
