package com.nahuo.quicksale.util;

/**
 * Created by jame on 2019/1/24.
 */

public class ObjectUtils {
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}
