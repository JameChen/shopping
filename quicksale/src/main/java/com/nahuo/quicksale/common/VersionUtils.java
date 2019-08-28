package com.nahuo.quicksale.common;

import com.nahuo.quicksale.BuildConfig;

/**
 * Created by ZZB on 2015/7/31.
 */
public class VersionUtils {

    /**
     * 获取显示的版本名，最多只有两个小数点，即1.2.3 后面的版本不再显示
     *@author ZZB
     *created at 2015/7/31 18:01
     */
    public static String getDisplayVersion(){
        String realVersion = BuildConfig.VERSION_NAME;
        String[] versions = realVersion.split("\\.");
        if(versions.length < 3){
            return realVersion;
        }else{
            StringBuffer displayVersion = new StringBuffer();
            for(int i=0; i< 3; i++){
                displayVersion.append(versions[i]);
                displayVersion.append(i == 2 ? "" : ".");
            }
            return displayVersion.toString();
        }
    }

}
