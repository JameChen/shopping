package com.nahuo.live.deal;

import android.content.res.Resources;

public class DensityUtil {
    /**
     * dp > px
     *
     * @param dpValue
     * @return
     */
    public static int dp2px(float dpValue) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * density + 0.5f);
    }
}
