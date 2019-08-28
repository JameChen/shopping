package com.nahuo.quicksale.common;

/**
 * Created by 诚 on 2015/9/24.
 */
public class NotificationHelper {
//
//    private static NotificationCompat.Builder mBuilder;
//
//
//    private static void CreateNotification(Context context, String contentTitle, String text, String showtitle,  PendingIntent contentIntent) {
//        //初始化
//        initNotify(context);
//
//
//
//        mBuilder.setContentTitle(contentTitle)
//                .setContentText(text).setContentIntent(contentIntent)
//                .setTicker(showtitle);
//
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(100, mBuilder.build());
//    }
//
//
//    /**
//     *
//     * 通知
//     * @param context
//     * @param contentTitle  通知标题
//     * @param text  通知内容
//     * @param tickerText  滚动显示
//     */
//     public  static  void CreateNotification(Context context, String contentTitle, String text, String tickerText)
//     {
//
//
//         Intent notifyIntent = new Intent(context, StartActivity.class);
//         // Sets the Activity to start in a new, empty task
//         notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                 | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//         // Creates the PendingIntent
//         PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                 notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//
//         CreateNotification(context,contentTitle,text,tickerText,contentIntent);
//     }
//
//
//
//
//    /**
//     * 初始化通知栏
//     */
//    private static void initNotify(Context context) {
//        mBuilder = new NotificationCompat.Builder(context);
//        mBuilder
//                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示
//                .setPriority(Notification.PRIORITY_DEFAULT)//设置该通知优先级
//             	.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
//
//                .setSmallIcon(R.mipmap.app_logo);
//    }

}
