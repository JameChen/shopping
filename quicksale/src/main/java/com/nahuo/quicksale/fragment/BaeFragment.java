package com.nahuo.quicksale.fragment;

import android.support.v4.app.Fragment;

import com.baidu.mobstat.StatService;

/**
 * Created by jame on 2017/6/16.
 */

public class BaeFragment extends Fragment{
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
