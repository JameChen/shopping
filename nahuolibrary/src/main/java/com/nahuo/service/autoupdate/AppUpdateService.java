package com.nahuo.service.autoupdate;

import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.nahuo.library.R;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.service.autoupdate.internal.FoundVersionDialog;
import com.nahuo.service.autoupdate.internal.NetworkUtil;
import com.nahuo.service.autoupdate.internal.ResponseCallback;
import com.nahuo.service.autoupdate.internal.VerifyTask;
import com.nahuo.service.autoupdate.internal.VersionDialogListener;
import com.nahuo.service.autoupdate.internal.VersionPersistent;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.RetryPolicy;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class AppUpdateService {

    private Context context;
    private NetworkStateReceiver networkReceiver;
    private boolean updateDirectly = false;

    private boolean isRegistered = false;

    private long downloadTaskId = -12306;
    private static AutoUpgradeDelegate updateDelegate;
    private static final int FAIL_TIME = 2;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    class AutoUpgradeDelegate implements AppUpdate, ResponseCallback,
            VersionDialogListener {

        private Displayer customShowingDelegate;
        private Version latestVersion;
        private boolean installAuto;

        @Override
        public void checkLatestVersion(String url, ResponseParser parser) {
            checkVersion(url, parser, false);
        }

        @Override
        public void checkAndUpdateDirectly(String url, ResponseParser parser) {
            checkVersion(url, parser, true);
        }

        void checkVersion(String url, ResponseParser parser,
                          boolean isUpdateDirectly) {

            updateDirectly = isUpdateDirectly;
            if (isNetworkActive()) {
                VerifyTask task = new VerifyTask(context, parser, this);
                task.execute(url);
            }
        }

        private int failCount = 0;

        @Override
        public void downloadAndInstallCurrent() {
            downloadAndInstall(latestVersion);
        }

        @Override
        public void downloadAndInstall(final Version latestVersion) {
            downloadAndInstall(latestVersion, true);
        }

        private void delFileSize(File f) {
            if (f == null)
                return;
            if (f.isDirectory() && f.listFiles() != null) {
                for (File item : f.listFiles()) {
                    delFileSize(item);
                }
            } else {
                f.delete();
            }
            return;
        }

        public void downloadAndInstall(final Version latestVersion, boolean autoInstall) {
            installAuto = autoInstall;
            if (latestVersion == null || !isNetworkActive() || failCount > FAIL_TIME) {
                failCount = 0;
                return;
            }
            // 下载之前删除之前下载的同名文件，然后在进行下载，主要是下载不会覆盖之前的文件，导致更新会失败
            File dir = getUpdateFile(context, latestVersion.targetUrl);
            if (dir.exists()) {
                String md5 = getFileMD5(dir);
                if (md5 != null && md5.equals(latestVersion.md5)) {
                    if (installAuto) {
                        install(dir.getAbsolutePath());
                    }
                    return;
                } else
                    dir.delete();
            }
            File cacheFile = context.getCacheDir();
            delFileSize(cacheFile);
            //清除external cache
            cacheFile = context.getExternalCacheDir();
            delFileSize(cacheFile);
            ThinDownloadManager downloadManager = new ThinDownloadManager(10);
            RetryPolicy retryPolicy = new DefaultRetryPolicy();

            Uri downloadUri = Uri.parse(latestVersion.targetUrl);

            final Uri destinationUri;
            String authority = context.getPackageName() + ".provider";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //通过FileProvider创建一个content类型的Uri
                destinationUri = FileProvider.getUriForFile(context, authority, dir);
            } else {
                destinationUri = Uri.fromFile(dir);
            }
            final DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.LOW)
                    .setRetryPolicy(retryPolicy)
                    .setDownloadListener(new DownloadStatusListener() {
                        long lastUpdateProgress;

                        @Override
                        public void onDownloadComplete(int id) {
                            File apkFile = new File(destinationUri.getPath());
                            String md5 = getFileMD5(apkFile);
                            if (md5 == null) {
                                if (installAuto) {
                                    downloadCompleted(destinationUri);
                                }
                                failCount = 0;
                            } else if (TextUtils.isEmpty(latestVersion.md5) || md5.equals(latestVersion.md5)) {
                                if (installAuto) {
                                    downloadCompleted(destinationUri);
                                }
                                failCount = 0;
                            } else {
                                failCount++;
                                apkFile.delete();
                                downloadAndInstall(latestVersion, installAuto);
                            }
                        }

                        @Override
                        public void onDownloadFailed(int id, int errorCode, String errorMessage) {
                            if (isNetworkActive()) {
                                failCount++;
                                if (failCount > FAIL_TIME) {
                                    if (installAuto) {
                                        Builder builder = LightAlertDialog.Builder.create(context);
                                        builder.setTitle("提示").setMessage("下载失败,通过浏览器下载？")
                                                .setNegativeButton("取消", null)
                                                .setPositiveButton("好", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Uri uri = Uri.parse(latestVersion.targetUrl);
                                                        Intent intent = new
                                                                Intent(Intent.ACTION_VIEW, uri);
                                                        context.startActivity(intent);
                                                    }
                                                });
                                        builder.show();
                                    }
                                } else {
                                    File apkFile = new File(destinationUri.getPath());
                                    apkFile.delete();
                                    downloadAndInstall(latestVersion, installAuto);
                                }
                            }
                        }

                        @Override
                        public void onProgress(int id, long totalBytes, long downloadedBytes, int progress) {
                            if (installAuto) {
                                long time = System.currentTimeMillis();
                                if (time - lastUpdateProgress > 1000) {
                                    lastUpdateProgress = time;
                                    updateProgress(progress);
                                }
                            }
                        }
                    });
            downloadManager.add(downloadRequest);
        }

        protected void downloadCompleted(Uri destinationUri) {
            mBuilder.setContentText("更新下载完成!").setProgress(0, 0, false);
            Intent installAPKIntent = new Intent(Intent.ACTION_VIEW);

            installAPKIntent.setDataAndType(destinationUri, "application/vnd.android.package-archive");
            installAPKIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                installAPKIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                installAPKIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(context
                    , 0, installAPKIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            Notification noti = mBuilder.build();
            noti.flags = android.app.Notification.FLAG_NO_CLEAR;
            mNotifyManager.notify(0, noti);
            install(destinationUri.getPath());
        }

        boolean isNetworkActive() {
            return NetworkUtil.getNetworkType(context) != NetworkUtil.NOCONNECTION;
        }

        private void updateProgress(int progress) {
            //"正在下载:" + progress + "%"
            mBuilder.setContentText(context.getString(R.string.download_progress, progress)).setProgress(100, progress, false);
            //setContentInent如果不设置在4.0+上没有问题，在4.0以下会报异常
            PendingIntent pendingintent = PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            mBuilder.setContentIntent(pendingintent);
            mNotifyManager.notify(0, mBuilder.build());
        }

        @Override
        public void onFoundLatestVersion(Version version) {
            this.latestVersion = version;

            if (updateDirectly) {
                downloadAndInstall(latestVersion);
                String versionTipFormat = context.getResources().getString(
                        R.string.update_latest_version_title);
                Toast.makeText(context,
                        String.format(versionTipFormat, latestVersion.name),
                        Toast.LENGTH_LONG).show();
                return;
            }

            if (customShowingDelegate != null) {
                customShowingDelegate.showFoundLatestVersion(latestVersion);
            } else {
                FoundVersionDialog dialog = new FoundVersionDialog(context,
                        latestVersion, this);
                dialog.show();
            }
        }

        @Override
        public void onCurrentIsLatest() {
            if (customShowingDelegate != null) {
                customShowingDelegate.showIsLatestVersion();
            } else {
                Toast.makeText(context, R.string.is_latest_version_label,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void callOnResume() {
//            if (isRegistered)
//                return;
//            isRegistered = true;
//            context.registerReceiver(networkReceiver, new IntentFilter(
//                    ConnectivityManager.CONNECTIVITY_ACTION));
        }

        @Override
        public void callOnPause() {
//            if (!isRegistered)
//                return;
//            isRegistered = false;
//            context.unregisterReceiver(networkReceiver);
        }

        @Override
        public void setCustomDisplayer(Displayer delegate) {
            customShowingDelegate = delegate;
        }

        @Override
        public Version getLatestVersion() {
            return latestVersion;
        }

        @Override
        public void doUpdate(boolean laterOnWifi) {
            if (!laterOnWifi) {
                downloadAndInstall(latestVersion);
            } else {
                new VersionPersistent(context).save(latestVersion);
            }
        }

        @Override
        public void doIgnore() {

        }

    }

    private void install(String path) {
        // 下载任务已经完成，清除
        new VersionPersistent(context).clear();
        File apkFile = new File(path);
        if (!apkFile.exists()){
            Toast.makeText(context,apkFile.getPath(),Toast.LENGTH_SHORT).show();
        }
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String authority = context.getPackageName() + ".provider";
        Uri destinationUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            destinationUri = FileProvider.getUriForFile(context, authority, apkFile);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            installIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            destinationUri = Uri.fromFile(apkFile);
        }
        installIntent.setDataAndType(destinationUri,
                "application/vnd.android.package-archive");
        context.startActivity(installIntent);
    }

    class NetworkStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                if (NetworkUtil.getNetworkType(context) == NetworkUtil.WIFI) {
                    Version versionTask = new VersionPersistent(context).load();
                    if (versionTask != null) {
                        Toast.makeText(context, R.string.later_update_tip,
                                Toast.LENGTH_SHORT).show();
                        updateDelegate.downloadAndInstall(versionTask);
                    }
                }
            }
        }
    }

    private AppUpdate getAppUpdate() {
        if (updateDelegate == null) {
            updateDelegate = new AutoUpgradeDelegate();
        }
        return updateDelegate;
    }

    public static AppUpdateService updateServiceInstance = null;

    public static AppUpdate getAppUpdate(Context context) {
        if (null == updateServiceInstance) {
            updateServiceInstance = new AppUpdateService(context);
        }
        return updateServiceInstance.getAppUpdate();
    }

    private AppUpdateService(Context context) {
        this.context = context;
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        String appName = context.getString(context.getApplicationInfo().labelRes);
        int icon = context.getApplicationInfo().icon;
        mBuilder.setContentTitle(appName).setSmallIcon(icon);
//        networkReceiver = new NetworkStateReceiver();
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static File getUpdateFile(Context context, String url) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (dir == null) {
            dir = context.getCacheDir();
        }
        dir = new File(dir, extractName(url));
        return dir;
    }

    private static String extractName(String path) {
        String tempFileName = "_temp@" + path.hashCode();
        if (path != null) {
            boolean fileNameExist = path.substring(path.length() - 5,
                    path.length()).contains(".");
            if (fileNameExist) {
                tempFileName = path.substring(path
                        .lastIndexOf(File.separator) + 1);
            }
        }
        return tempFileName;
    }
}
