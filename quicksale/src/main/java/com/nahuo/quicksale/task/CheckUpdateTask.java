package com.nahuo.quicksale.task;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.HttpUtils;
import com.nahuo.service.autoupdate.AppUpdate;
import com.nahuo.service.autoupdate.AppUpdateService;
import com.nahuo.service.autoupdate.Version;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author ZZB
 * @description 检查更新
 * @created 2014-8-15 上午11:18:17
 */
public class CheckUpdateTask extends AsyncTask<Void, Void, Object> {

    private static final String TAG = CheckUpdateTask.class.getSimpleName();
    private Context mContext;
    private LoadingDialog mLoadingDialog;
    private boolean mShowLoading;
    private boolean mVersionIsNewedShowTip;
    private AppUpdate mAppUpdate;
    private boolean mIsForce2Update = false;


    public CheckUpdateTask(Context context, AppUpdate appUpdate, boolean showLoading, boolean versionIsNewedShowTip) {
        this.mContext = context;
        mLoadingDialog = new LoadingDialog(context);
        mShowLoading = showLoading;
        mVersionIsNewedShowTip = versionIsNewedShowTip;
        mAppUpdate = appUpdate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mShowLoading) {
            try {
                mLoadingDialog.start(mContext.getString(R.string.me_check_app_update_loading));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected Object doInBackground(Void... params) {
        String updateXml = "";
        try {
            updateXml = HttpUtils.get(ImageUrlExtends.HTTP_BANWO_FILES+"/quick-sale/Config.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("red_count_bg", "update xml = " + updateXml);
        return Version.parserVersion(updateXml);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.stop();
        }
        if (result instanceof Version) {
            final Version version = (Version) result;
            int versionLocal = FunctionHelper.GetAppVersionCode(mContext);
            if (versionLocal < version.code) {
                while (version.feature.contains("\\n")) {
                    //replaceAll不行
                    version.feature = version.feature.replace("\\n", "\n");
                }
                final Activity aty = (Activity) mContext;
                if (aty == null || aty.isFinishing()) {
                    return;
                }
                // version.targetUrl="http://banwo-files.b0.upaiyun.com/test/quicksale.apk";
                String msg = MessageFormat.format(mContext.getString(R.string.update_msg), version.name, version.feature);
                String forcedUpdateText = getForcedUpdateText(version.destroyVersions);
                if (!TextUtils.isEmpty(forcedUpdateText)) {
                    mIsForce2Update = true;
                    msg = msg + "\n\n" + forcedUpdateText;
                }
                LightAlertDialog.Builder builder = LightAlertDialog.Builder.create(mContext);
                builder.setTitle(R.string.latest_version_title).setMessage(Html.fromHtml(msg.replace("\n", "<br>"))).setCancelable(false)
                        .setNegativeButton(R.string.ignore, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mIsForce2Update) {//强制更新，不更新不给用
                                    System.exit(0);
                                }
                                dialog.dismiss();
                                if (isWifiDataEnable(mContext)) {
                                    File apkFile = AppUpdateService.getUpdateFile(mContext, version.targetUrl);
                                    if (apkFile.exists()) {
                                        String md5 = AppUpdateService.getFileMD5(apkFile);
                                        if (md5 == null || !md5.equals(version.md5)) {
                                            mAppUpdate.downloadAndInstall(version, false);
                                        }
                                    } else {
                                        mAppUpdate.downloadAndInstall(version, false);
                                    }
                                }
                            }
                        }).setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(mContext, R.string.later_update_tip, Toast.LENGTH_LONG).show();
                        mAppUpdate.downloadAndInstall(version);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
//                Activity activity = (Activity) mContext;
//                if (activity.isFinishing()||activity==null)
//                    return;
//                final CDialog dialog = new CDialog(activity);
//                dialog.setHasTittle(true).setTitle(R.string.latest_version_title)
//                        .setMessage(Html.fromHtml(msg.replace("\n", "<br>")))
//                        .setPositive(R.string.update, new CDialog.PopDialogListener() {
//                            @Override
//                            public void onPopDialogButtonClick(int which) {
//                                dialog.dismiss();
//                                Toast.makeText(mContext, R.string.later_update_tip, Toast.LENGTH_LONG).show();
//                                mAppUpdate.downloadAndInstall(version);
//                            }
//                        }).setNegative(R.string.ignore, new CDialog.PopDialogListener() {
//                    @Override
//                    public void onPopDialogButtonClick(int which) {
//                        if (mIsForce2Update) {//强制更新，不更新不给用
//                            System.exit(0);
//                        }
//                        dialog.dismiss();
//                        if (isWifiDataEnable(mContext)) {
//                            File apkFile = AppUpdateService.getUpdateFile(mContext, version.targetUrl);
//                            if (apkFile.exists()) {
//                                String md5 = AppUpdateService.getFileMD5(apkFile);
//                                if (md5 == null || !md5.equals(version.md5)) {
//                                    mAppUpdate.downloadAndInstall(version, false);
//                                }
//                            } else {
//                                mAppUpdate.downloadAndInstall(version, false);
//                            }
//                        }
//                    }
//                }).show();
            } else {
                if (mVersionIsNewedShowTip) {
                    ViewHub.showOkDialog(mContext, getString(R.string.dialog_title_update),
                            getString(R.string.dialog_msg_no_update), "确定");
                }
            }
        } else {
            ViewHub.showLongToast(mContext, result.toString());
        }
    }

    /**
     * 判断wifi 是否可用
     *
     * @param context
     * @return
     * @throws Exception
     */
    private boolean isWifiDataEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }

    private String getString(int id) {
        return mContext.getString(id);
    }

    private String getForcedUpdateText(String destroyVersion) {
        String text = "";
        String versionName = FunctionHelper.getVersionName(mContext);
        Log.i(getClass().getSimpleName(), "versionName:" + versionName + " destroyVersion:" + destroyVersion);
        if (!TextUtils.isEmpty(versionName) && !TextUtils.isEmpty(destroyVersion)) {
            if (!destroyVersion.contains(",")) {
                if (destroyVersion.equals(versionName)) {
                    text = mContext.getString(R.string.forced_update_tip);
                }
            } else {
                String[] vNames = destroyVersion.split(",");
                for (String vName : vNames) {
                    if (versionName.equals(vName)) {
                        text = mContext.getString(R.string.forced_update_tip);
                        break;
                    }
                }
            }
        }
        return text;
    }
}
