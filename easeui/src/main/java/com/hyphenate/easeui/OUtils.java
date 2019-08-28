package com.hyphenate.easeui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.view.WindowManager;

/**
 * Created by jame on 2018/10/26.
 */

public class OUtils {
    public static void initEaseNotifier(NotificationManager notificationManager , long[] VIBRATION_PATTERN ,String CHANNEL_ID){
     if (Build.VERSION.SDK_INT >= 26) {
         // Create the notification channel for Android 8.0
         NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                 "hyphenate chatuidemo message default channel.", NotificationManager.IMPORTANCE_DEFAULT);
         channel.setVibrationPattern(VIBRATION_PATTERN);
         notificationManager.createNotificationChannel(channel);
     }
    }
  public static int getWindowType(){
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
      } else {
          return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
      }
  }
}
