package com.nahuo.quicksale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

/**
 * @description 单Fragment的基类
 * @created 2014-8-20 下午3:43:58
 * @author ZZB
 */
public abstract class SingleFragmentActivity extends BaseSlideBackActivity{

    private static final String TAG = SingleFragmentActivity.class.getSimpleName();

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }else{
            Log.d(TAG, fragment.getClass().getSimpleName());
        }
    }
    
    
}
