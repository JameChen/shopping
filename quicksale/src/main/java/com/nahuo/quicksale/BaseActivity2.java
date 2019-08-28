package com.nahuo.quicksale;


/**
 * @description 有标题Activity:requestFeature(Window.FEATURE_CUSTOM_TITLE);
 * @created 2015-5-5 下午2:14:26
 */
public class BaseActivity2 extends BaseActivity1{

    @Override
    protected AbstractActivity getAbsActivity() {
        return new AbstractActivity(false);
    }
    
}
