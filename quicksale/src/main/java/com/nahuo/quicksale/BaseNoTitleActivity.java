package com.nahuo.quicksale;


/**
 * @description 没标题activity:requestFeature(Window.FEATURE_NO_TITLE);
 * @created 2015-5-5 下午2:13:25
 * @author ZZB
 */
public class BaseNoTitleActivity extends BaseActivity1{

    @Override
    protected AbstractActivity getAbsActivity() {
        return new AbstractActivity(true);
    }

}
