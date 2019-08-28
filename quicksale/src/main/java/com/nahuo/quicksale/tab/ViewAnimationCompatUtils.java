package com.nahuo.quicksale.tab;

import android.animation.Animator;
import android.view.View;

/**
 * Created by jame on 2018/4/13.
 */

public class ViewAnimationCompatUtils {   private ViewAnimationCompatUtils(){}

    public static Animator createCircularReveal(final View view,
                                                final int centerX, final int centerY, final float startRadius, final float endRadius) {

        Animator animator = CircularRevealLayout.Builder.on(view)
                .centerX(centerX)
                .centerY(centerY)
                .startRadius(startRadius)
                .endRadius(endRadius)
                .create();

        return animator;

    }


}
