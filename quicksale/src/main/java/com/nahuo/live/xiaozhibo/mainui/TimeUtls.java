package com.nahuo.live.xiaozhibo.mainui;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jame on 2019/4/22.
 */

public class TimeUtls {
    public static String getCurrentTime(){
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  df.format(day);
    }
}
