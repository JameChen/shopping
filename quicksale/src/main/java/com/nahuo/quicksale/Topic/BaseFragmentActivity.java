package com.nahuo.quicksale.Topic;

import android.os.Bundle;

import com.nahuo.quicksale.R;

public class BaseFragmentActivity extends com.nahuo.quicksale.base.BaseFragmentActivity {


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out) ;
        super.onCreate(arg0);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition( R.anim.slide_left_in ,R.anim.slide_right_out) ;
    }

}