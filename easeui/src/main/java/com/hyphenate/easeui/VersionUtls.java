package com.hyphenate.easeui;

import android.os.Build;
import android.view.WindowManager;

/**
 * Created by jame on 2019/5/20.
 */

public class VersionUtls {
    public static int  getOCodes(){
        return Build.VERSION_CODES.O;
    }
    public static int  getTYPE_APPLICATION_OVERLAY(){
        return    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
    }


}
