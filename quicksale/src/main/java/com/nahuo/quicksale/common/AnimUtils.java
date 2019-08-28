package com.nahuo.quicksale.common;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by ZZB on 2015/8/7.
 */
public class AnimUtils {

    /**
     * 左右摆View
     *@author ZZB
     *created at 2015/8/7 11:16
     */
    public static void shake(View view){
        float tLeft = -10;
        float tRight = 10;
        ObjectAnimator.ofFloat(view, "translationX", 0, tRight, tLeft, tRight, tLeft, tRight, tLeft, tRight, tLeft, 0).setDuration(800).start();
    }
}
