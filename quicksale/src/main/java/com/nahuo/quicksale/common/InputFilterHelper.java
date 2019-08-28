package com.nahuo.quicksale.common;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Created by ZZB on 2015/8/10.
 */
public class InputFilterHelper {

    /**
     * 过滤空格
     *@author ZZB
     *created at 2015/8/10 14:30
     */
    public static InputFilter noWhiteSpaceFilter(){
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String str = source.toString();
                str = str.replace(" ", "");//替换空格
                return str;
            }
        };
        return filter;
    }
    /**
     * 最大长度过滤器
     *@author ZZB
     *created at 2015/8/10 14:24
     */
    public static InputFilter maxLengthFilter(int maxLength){
        return new InputFilter.LengthFilter(maxLength);
    }

}
