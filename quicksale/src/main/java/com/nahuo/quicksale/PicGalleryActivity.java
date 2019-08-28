package com.nahuo.quicksale;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.luck.picture.lib.dialog.CustomDialog;
import com.luck.picture.lib.dialog.PictureDialog;
import com.luck.picture.lib.tools.DebugUtil;
import com.luck.picture.lib.tools.ScreenUtils;
import com.nahuo.library.controls.FlowIndicator;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.adapter.SlidePicPagerAdapter;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.MediaStoreUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class PicGalleryActivity extends BaseSlideBackActivity {

    /** 所有的图片地址 */
    public static final String EXTRA_URLS = "EXTRA_URLS";
    /** 当前进来的图片url */
    public static final String EXTRA_CUR_POS = "EXTRA_CUR_POS";
    /**是否转化图片url*/
    public static final String EXTRA_TRANSFER_IMG_URL = "EXTRA_TRANSFER_IMG_URL";

    private Context mContext = this;
    private ViewPager mPager;
    private SlidePicPagerAdapter mPagerAdapter;
    private FlowIndicator mLayoutIndicator;
    private ArrayList<String> mUrls;
    private int mCurPos;
    private DownloadManager mDownloadManager;
    private boolean mTransferImgUrl = true;
    private String directory_path;
    private loadDataThread loadDataThread;
    String fileName = "";
    private String Dcim_Path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_gallery);
       // mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        Dcim_Path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        initExtras();
        initView();
    }

    private void initExtras() {
        Intent intent = getIntent();
        mUrls = intent.getStringArrayListExtra(EXTRA_URLS);
        mTransferImgUrl = intent.getBooleanExtra(EXTRA_TRANSFER_IMG_URL, true);
        mCurPos = intent.getIntExtra(EXTRA_CUR_POS, -1);
    }
    String savePath="";
    private void initView() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mLayoutIndicator = (FlowIndicator) findViewById(R.id.layout_indicator);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mLayoutIndicator.setSelectedPos(position);
            }
        });
        mPagerAdapter = new SlidePicPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setPicZoomable(true);
        mPagerAdapter.setPicScaleType(ScaleType.FIT_CENTER);
        mPager.setAdapter(mPagerAdapter);
        
        ArrayList<String> fullUrls = new ArrayList<String>();
        if(mTransferImgUrl){
            for (String url : mUrls) {
                fullUrls.add(ImageUrlExtends.getImageUrl(url, getResources().getInteger(R.integer.grid_pic_width_big)));
            } 
        }else{
            fullUrls.addAll(mUrls);
        }
        
        mPagerAdapter.setData(fullUrls);
        mPagerAdapter.notifyDataSetChanged();
        mLayoutIndicator.setMaxNum(fullUrls.size());
        mPager.setCurrentItem(mCurPos, true);
        mPagerAdapter.setOnViewTabListener(new OnViewTapListener() {
            
            @Override
            public void onViewTap(View view, float x, float y) {
                finish();
            }
        });
        mPagerAdapter.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                try {
                    String url = (String) v.getTag();
                    showDownLoadDialog(url);

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                Builder builder = LightAlertDialog.Builder.create(mContext);
//                builder.setPositiveButton("保存到手机", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String url = (String) v.getTag();
//                        DownloadManager.Request request = new DownloadManager.Request(Uri
//                                .parse(url));
//                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
//                                | DownloadManager.Request.NETWORK_WIFI);
//                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
//                        // request.setMimeType("image/jpeg");
//                        request.setVisibleInDownloadsUi(false);
//                        String folderName = Const.IMAGES_CASH_PATH;
//                        String fileName = "";
//                        try {
//                            fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf("!"));
//                        } catch (Exception e) {
//                            fileName = System.currentTimeMillis() + ".jpg";
//                        }
//                         savePath = Environment.DIRECTORY_DCIM + "/" + folderName + fileName;
//                        File tmpFile = new File(Environment.DIRECTORY_DCIM + "/" + folderName);
//                        if(!tmpFile.exists()) {
//                            tmpFile.mkdirs();
//                        }
////                      request.setDestinationInExternalPublicDir(folderName, fileName);
//                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM, folderName + fileName);
//                        try {
//                            long downloadId = mDownloadManager.enqueue(request);
//                            listener(downloadId);
//                        } catch (IllegalArgumentException e) {
//                            ViewHub.showLongToast(mContext, "图片下载失败");
//                          //  ViewHub.showOkDialog(mContext, "提示", "设备没开启下载功能，无法下载图片", "OK");
//                            return;
//                        }
////                        String savePath = new StringBuilder(Environment
////                                .getExternalStorageDirectory().getAbsolutePath())
////                                .append(File.separator).append(folderName).append(File.separator)
////                                .append(fileName).toString();
//                     //   MediaStoreUtils.scanImageFile(mContext, savePath);
//                       // ViewHub.showLongToast(mContext, "图片已保存至: " + savePath);
//
//                    }
//                });
//                builder.show();
                return false;
            }
        });
    }
    /**
     * 下载图片提示
     */
    private void showDownLoadDialog(final String path) {
        final CustomDialog dialog = new CustomDialog(PicGalleryActivity.this,
                ScreenUtils.getScreenWidth(PicGalleryActivity.this) * 3 / 4,
                ScreenUtils.getScreenHeight(PicGalleryActivity.this) / 4,
                R.layout.picture_wind_base_dialog_xml, R.style.Theme_dialog);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_commit = (Button) dialog.findViewById(R.id.btn_commit);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        tv_title.setText(getString(R.string.picture_prompt));
        tv_content.setText(getString(R.string.picture_prompt_content));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url=path;
                    if (TextUtils.isEmpty(url)){
                        ViewHub.showShortToast(PicGalleryActivity.this,"图片地址不能为空");
                    }else {
                        try {
                            fileName = url.substring(url.lastIndexOf("/"), url.lastIndexOf("!"));
                        } catch (Exception e) {
                            fileName = "/"+System.currentTimeMillis() + ".jpg";
                        }
                        File downloadDirectory = new File(Dcim_Path+"/"+Const.IMAGES_CASH_PATH);
                        if (!downloadDirectory.exists()) {
                            downloadDirectory.mkdirs();
                        }
                        File cacheFile = new File(Dcim_Path+"/"+Const.IMAGES_CASH_PATH+fileName);
                        if (cacheFile.exists()) {
                            ViewHub.showShortToast(PicGalleryActivity.this, "图片已经下载在本地"+Dcim_Path+"/"+Const.IMAGES_CASH_PATH);
                        } else {
                            showPleaseDialog();
                            loadDataThread = new loadDataThread(path);
                            loadDataThread.start();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                boolean isHttp = PictureMimeType.isHttp(path);
//                if (isHttp) {
//
//                } else {
//                    // 有可能本地图片
//                    try {
//                        String dirPath = PictureFileUtils.createDir(PicGalleryActivity.this,
//                                System.currentTimeMillis() + ".png", directory_path);
//                        PictureFileUtils.copyFile(path, dirPath);
//                        ViewHub.showShortToast(PicGalleryActivity.this,getString(R.string.picture_save_success) + "\n" + dirPath);
//                        dismissDialog();
//                    } catch (IOException e) {
//                        ViewHub.showShortToast(PicGalleryActivity.this,getString(R.string.picture_save_error) + "\n" + e.getMessage());
//                        dismissDialog();
//                        e.printStackTrace();
//                    }
//                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    // 进度条线程
    public class loadDataThread extends Thread {
        private String path;

        public loadDataThread(String path) {
            super();
            this.path = path;
        }

        public void run() {
            try {
                showLoadingImage(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static String createDir(Context context, String filename, String directory_path) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : context.getCacheDir();
        File path = null;
        if (!TextUtils.isEmpty(directory_path)) {
            // 自定义保存目录
            path = new File(rootDir.getAbsolutePath() + directory_path);
        } else {
            path = new File(rootDir.getAbsolutePath() + "/PictureSelector");
        }
        if (!path.exists())
            // 若不存在，创建目录，可以在应用启动的时候创建
            path.mkdirs();

        return path + "/" + filename;
    }


    // 下载图片保存至手机
    public void showLoadingImage(String urlPath) {

        try {
            URL u = new URL(urlPath);
            try {
                fileName = urlPath.substring(urlPath.lastIndexOf("/"), urlPath.lastIndexOf("!"));
            } catch (Exception e) {
                fileName = "/"+System.currentTimeMillis() + ".jpg";
            }
            String path=Dcim_Path+"/"+Const.IMAGES_CASH_PATH+fileName;
//            String path = createDir(PicGalleryActivity.this,
//                    System.currentTimeMillis() + ".png", directory_path);
            byte[] buffer = new byte[1024 * 8];
            int read;
            int ava = 0;
            long start = System.currentTimeMillis();
            BufferedInputStream bin;
            bin = new BufferedInputStream(u.openStream());
            BufferedOutputStream bout = new BufferedOutputStream(
                    new FileOutputStream(path));
            while ((read = bin.read(buffer)) > -1) {
                bout.write(buffer, 0, read);
                ava += read;
                long speed = ava / (System.currentTimeMillis() - start);
                DebugUtil.i("Download: " + ava + " byte(s)"
                        + "    avg speed: " + speed + "  (kb/s)");
            }
            bout.flush();
            bout.close();
            Message message = handler.obtainMessage();
            message.what = 200;
            message.obj = path;
            handler.sendMessage(message);
        } catch (IOException e) {
            ViewHub.showShortToast(PicGalleryActivity.this,getString(R.string.picture_save_error) + "\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    protected PictureDialog dialog;
    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        if (!isFinishing()) {
            dismissDialog();
            dialog = new PictureDialog(this);
            dialog.show();
        }
    }

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    try {
                        String path = (String) msg.obj;
                        MediaStoreUtils.scanImageFile(mContext, path);
                        ViewHub.showShortToast(PicGalleryActivity.this,getString(R.string.picture_save_success) + "\n" + path);
                        dismissDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    BroadcastReceiver broadcastReceiver;
    private void listener(final long Id) {

        // 注册广播监听系统的下载完成事件。
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long ID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (ID == Id) {
                    ViewHub.showLongToast(mContext, "图片已保存至: " + savePath);
                    MediaStoreUtils.scanImageFile(mContext, savePath);
                    //Toast.makeText(getApplicationContext(), "任务:" + Id + " 下载完成!", Toast.LENGTH_LONG).show();
                }
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (broadcastReceiver!=null)
//        unregisterReceiver(broadcastReceiver);
    }
}
