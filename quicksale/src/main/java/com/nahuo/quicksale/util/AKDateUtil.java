package com.nahuo.quicksale.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alan on 2017/3/29 0029.
 */
public class AKDateUtil {
    public static String getWeek(String dateString){
        final String dayNames[] = { "周日", "周一", "周二", "周三", "周四", "周五",
                "周六" };
        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        try {
            date = sdfInput.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        if(dayOfWeek<0)dayOfWeek=0;
        return dayNames[dayOfWeek];
    }
}
