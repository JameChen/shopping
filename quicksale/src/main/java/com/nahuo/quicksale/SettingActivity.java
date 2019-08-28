package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.nahuo.library.controls.LightPopDialog.PopDialogListener;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.service.autoupdate.AppUpdate;
import com.nahuo.service.autoupdate.AppUpdateService;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.FileUtils;
import com.nahuo.quicksale.common.VersionUtils;
import com.nahuo.quicksale.controls.WidgetSettingItem;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.task.CheckUpdateTask;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class SettingActivity extends BaseActivity2 implements OnClickListener {

    private Context           mContext = this;
    private WidgetSettingItem mWsiUpgrade;
    private AppUpdate         mAppUpdate;
    private boolean           mUploadingErrorLog;
    private LoadingDialog     loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("设置");
        initView();
        loadingDialog = new LoadingDialog(this);

        mWsiUpgrade.setRightText("当前版本：" + VersionUtils.getDisplayVersion());
        mAppUpdate = AppUpdateService.getAppUpdate(this);

        findViewById(R.id.wsi_exit).setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (!mUploadingErrorLog) {
                    new Task().execute();
                }

                return true;
            }
        });

    }

    private void initView() {
        mWsiUpgrade = (WidgetSettingItem)findViewById(R.id.wsi_upgrade);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wsi_login:
                Intent pswIntent = new Intent(getApplicationContext(), ChangePswActivity.class);
                pswIntent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.LOGIN);
                startActivity(pswIntent);
                break;
            case R.id.wsi_exit:
                ViewHub.showLightPopDialog(this, getString(R.string.dialog_title),
                        getString(R.string.shopset_exit_confirm), "取消", "退出登录", new PopDialogListener() {
                            @Override
                            public void onPopDialogButtonClick(int which) {
                                UserInfoProvider.exitApp(mContext);
                                finish();
                            }
                        });
                break;
            case R.id.wsi_clear:
                new ClearTask().execute();
                break;
            case R.id.wsi_upgrade:
                new CheckUpdateTask(this, mAppUpdate, true, true).execute();
                break;
            default:
                break;
        }
    }

    private class ClearTask extends AsyncTask<Object, Void, String> {
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ViewHub.showLongToast(getApplicationContext(), "清除缓存中，请等待...");
        }

        @Override
        protected String doInBackground(Object... params) {

            long gcCountBefore = 0;
            long gcCountEnd = 0;
            //清除cachedir
            File cacheFile = getApplicationContext().getCacheDir();
             gcCountBefore = getFileSize(cacheFile, 0);
            delFileSize(cacheFile);
             gcCountEnd = getFileSize(cacheFile, 0);
            //清除external cache
            cacheFile = getApplicationContext().getExternalCacheDir();
            gcCountBefore += getFileSize(cacheFile, 0);
            delFileSize(cacheFile);
            gcCountEnd += getFileSize(cacheFile, 0);
            //清除sd cache
            File cacheFile1 = new File(SDCardHelper.getSDCardRootDirectory()+"/weipu/qrcode_cache");
            File cacheFile2 = new File(SDCardHelper.getSDCardRootDirectory()+"/weipu/share");
            File cacheFile3 = new File(SDCardHelper.getSDCardRootDirectory()+"/weipu/share_download");
            File cacheFile4 = new File(SDCardHelper.getSDCardRootDirectory()+"/weipu/tackPhoto_tmp");
            File cacheFile5 = new File(SDCardHelper.getSDCardRootDirectory()+"/weipu/upload_tmp");
            File cacheFile6=new File(SDCardHelper.getSDCardRootDirectory()+"/pinhuo/pihuo_save");
            gcCountBefore += getFileSize(cacheFile1, 0);
            gcCountBefore += getFileSize(cacheFile2, 0);
            gcCountBefore += getFileSize(cacheFile3, 0);
            gcCountBefore += getFileSize(cacheFile4, 0);
            gcCountBefore += getFileSize(cacheFile5, 0);
            gcCountBefore+=getFileSize(cacheFile6,0);
            delFileSize(cacheFile1);delImg2Media(cacheFile1);
            delFileSize(cacheFile2);delImg2Media(cacheFile2);
            delFileSize(cacheFile3);delImg2Media(cacheFile3);
            delFileSize(cacheFile4);delImg2Media(cacheFile4);
            delFileSize(cacheFile5);delImg2Media(cacheFile5);
            delFileSize(cacheFile6);delImg2Media(cacheFile6);
            gcCountEnd += getFileSize(cacheFile1, 0);
            gcCountEnd += getFileSize(cacheFile2, 0);
            gcCountEnd += getFileSize(cacheFile3, 0);
            gcCountEnd += getFileSize(cacheFile4, 0);
            gcCountEnd += getFileSize(cacheFile5, 0);
            gcCountEnd+=getFileSize(cacheFile6,0);
            //toast
            String showText = Formatter.formatFileSize(mContext, gcCountBefore - gcCountEnd);
            return showText;
        }

        @Override
        protected void onPostExecute(String result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            ViewHub.showLongToast(getApplicationContext(), "已释放" + result + "缓存");
        }
    }

    
    private class Task extends AsyncTask<Object, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ViewHub.showLongToast(getApplicationContext(), "上传日志...");
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                uploadErrorLog();
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            mUploadingErrorLog = false;
            if (result instanceof String && ((String)result).startsWith("error:")) {
                ViewHub.showLongToast(getApplicationContext(), ((String)result).replace("error:", ""));
            } else {
                ViewHub.showLongToast(getApplicationContext(), "上传成功");
            }
        }
    }

    public boolean uploadErrorLog() throws Exception {
        mUploadingErrorLog = true;
        long curTimeMills = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        String date = "";
        String filePath = "";
        boolean hasError = false;
        File file = null;
        for (int i = 0; i < 7; i++) {
            date = sdf.format(new Date(curTimeMills - i * TimeUtils.DAY_MILLIS));
            // 只上传最近七天中
            filePath = FileUtils.ERROR_LOG_PATH + "/" + date + "-log.txt";
            file = new File(filePath);
            if (file.exists()) {
                hasError = true;
                break;
            }
        }
        if (hasError) {
            String error = FileUtils.readFile(file);
            if (!TextUtils.isEmpty(error)) {
                AccountAPI.uploadErrorLog(this, error);
            }
        }
        return hasError;

    }

    private long getFileSize(File f, long allFileSize) {
        if (f == null) {
            return 0;
        }
        if (f.isDirectory() && f.listFiles() != null) {
            for (File item : f.listFiles()) {
                allFileSize = getFileSize(item, allFileSize);
            }
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(f);
                allFileSize += fis.available();
            } catch (Exception e) {
            }
        }
        return allFileSize;
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
    
    private void delImg2Media(File f)
    {   
        mContext.getContentResolver().delete(Media.EXTERNAL_CONTENT_URI, "_data like '%"+f.getAbsolutePath()+"%'", null);
        Log.e("asd", "asdasd");
    }
}
