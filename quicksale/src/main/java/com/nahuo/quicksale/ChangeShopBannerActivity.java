//package com.nahuo.quicksale;
//
//import android.app.AlertDialog.Builder;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.mobstat.StatService;
//import com.nahuo.library.controls.LightAlertDialog;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.library.helper.SDCardHelper;
//import com.nahuo.quicksale.api.ApiHelper;
//import com.nahuo.quicksale.api.ShopSetAPI;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.FileUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.exceptions.CatchedException;
//import com.nahuo.quicksale.oldermodel.ImageViewModel;
//import com.nahuo.quicksale.oldermodel.PublicData;
//import com.squareup.picasso.Picasso;
//import com.tencent.bugly.crashreport.CrashReport;
//
//import java.io.File;
//import java.util.ArrayList;
//
///**
// * @author PJ
// * @description 修改店招页面
// */
//public class ChangeShopBannerActivity extends BaseSlideBackActivity implements OnClickListener {
//
//    private ChangeShopBannerActivity vThis = this;
//    private static String TAG = "ChangeShopBannerActivity";
//    private View cameraBtn, take_pictureBtn;
//    private ImageView bannerImg;
//    private static final int REQUESTCODE_TAKEPHOTO = 1;// 拍照
//    private static final int REQUESTCODE_FROMALBUM = 2;// 从手机相册选择
//    private static final int REQUESTCODE_CUT = 3;// 裁剪
//    private Uri mPhotoUri;
//    private LoadingDialog loadingDialog;
//    private boolean mBannerChanged;// 店招是否修改
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//        setContentView(R.layout.activity_change_shop_banner);
//       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
//        initView();
//
//        String url = ImageUrlExtends.getImageUrl(SpManager.getShopBanner(vThis), Const.HEADER_BG_SIZE);
//        if (url.length() > 0) {
//            Picasso.with(vThis).load(url).placeholder(R.drawable.empty_photo).into(bannerImg);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mBannerChanged) {
//            Builder builder = LightAlertDialog.Builder.create(this);
//            builder.setTitle("提示").setMessage("店招已经修改，是否保存？")
//                    .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    }).setPositiveButton("保存", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    save();
//                }
//            });
//            builder.show();
//        } else {
//            super.onBackPressed();
//        }
//
//    }
//
//    private void initView() {
//        initTitleBar();
//        loadingDialog = new LoadingDialog(vThis);
//        cameraBtn = findViewById(R.id.change_shop_banner_camare);
//        take_pictureBtn = findViewById(R.id.change_shop_banner_take_picture);
//        bannerImg = (ImageView) findViewById(R.id.change_shop_banner_banner);
//        cameraBtn.setOnClickListener(this);
//        take_pictureBtn.setOnClickListener(this);
//    }
//
//    private void initTitleBar() {
//        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
//        tvTitle.setText("更换店招");
//
//        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
//        btnLeft.setText(R.string.titlebar_btnBack);
//        btnLeft.setVisibility(View.VISIBLE);
//        btnLeft.setOnClickListener(this);
//
//        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
//        btnRight.setText("保存");
//        btnRight.setVisibility(View.VISIBLE);
//        btnRight.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.titlebar_btnLeft:
//                onBackPressed();
//                break;
//            case R.id.titlebar_btnRight:
//                save();
//                break;
//
//            case R.id.change_shop_banner_camare:
//                takePhoto();
//                break;
//            case R.id.change_shop_banner_take_picture:
//                fromAblum();
//                break;
//        }
//    }
//
//    /**
//     * 调用系统照相机拍照
//     */
//    private void takePhoto() {
//        // 文件名
//        String fileName = "logo" + System.currentTimeMillis() + ".jpg";
//
//        // 封装Uri
//        String imgDirPath = SDCardHelper.getDCIMDirectory() + File.separator
//                + "weipu";
//        SDCardHelper.createDirectory(imgDirPath);
//        File file = new File(imgDirPath, fileName);
//        Uri imageUri = Uri.fromFile(file);
//        mPhotoUri = imageUri;
//        // 调用系统照相机
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
//        startActivityForResult(intent, REQUESTCODE_TAKEPHOTO);
//    }
//
//    /**
//     * 从相册选择照片并裁剪
//     */
//    private void fromAblum() {
//
//        Intent intent = new Intent(vThis, AlbumActivity.class);
//        intent.putExtra(AlbumActivity.EXTRA_MAX_PIC_COUNT, 1);
//        startActivityForResult(intent, REQUESTCODE_FROMALBUM);
//    }
//
////    /**
////     * 裁剪图片方法实现
////     *
////     * @param uri
////     *            照片的uri
////     * @param aspectX
////     *            宽度相对高度的比例
////     * @param aspectY
////     *            高度相对宽度的比例
////     * @param size
////     *            最大的高度、宽度
////     */
////    private void gotoCut(Uri uri, int aspectX, int aspectY, int size) {
////        Intent intent = new Intent("com.android.camera.action.CROP");
////        intent.setDataAndType(uri, "image/*");
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
////        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
////        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
////        intent.putExtra("crop", "true");
////        // 输出高宽比例
////        intent.putExtra("aspectX", 2);
////        intent.putExtra("aspectY", 1);
////        // 根据高宽比列计算高、宽
////        int width = aspectX > aspectY ? size : (size / aspectY) * aspectX;
////        int height = aspectX < aspectY ? size : (size / aspectX) * aspectY;
////        intent.putExtra("outputX", width);
////        intent.putExtra("outputY", height);
////        intent.putExtra("scale", true);//黑边
////        intent.putExtra("scaleUpIfNeeded", true);//黑边
////        intent.putExtra("return-data", false);// 若为false则表示不返回数据
////        startActivityForResult(intent, REQUESTCODE_CUT);
////    }
//
//
//    private void cropPic(Uri uri) {
//        final Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");//裁剪功能
//        //裁剪後輸出圖片的尺寸大小
//        intent.putExtra("outputX", 640);//這會限定圖片為
//        intent.putExtra("outputY", 320);
//        intent.putExtra("aspectX", 2);
//        intent.putExtra("aspectY", 1);
//        //切大照片,有可能因為超過傳回內存的容量16MB,會有問題,(Bitmap預設是ARGB_8888，1920x1080x4=8294400=8MB)
//        //原因是因為Android允許你使用return-data拿資料回來,再用(Bitmap)extras.getParcelable("data")拿到圖片
//        //檔案太大的解決辦法:不要讓Intent帶檔案回來,自創建檔案,使用uri方法去連結它
//        intent.putExtra("return-data", true);//要帶檔案回來
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        this.startActivityForResult(intent, REQUESTCODE_CUT);
//    }
//
//    /**
//     * 接收从其他界面返回的数据
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUESTCODE_TAKEPHOTO:// 拍照
//                if (resultCode == RESULT_OK) {
//                    try {
//                        if (!SDCardHelper.checkFileExists(mPhotoUri.getPath())) {
//                            Toast.makeText(vThis, "未找到图片：" + mPhotoUri.getPath(),
//                                    Toast.LENGTH_SHORT).show();
//                            return;
//                        }
////                    int aspectX = 2;// 宽度相对高度的比例
////                    int aspectY = 1;// 高度相对宽度的比例
////                    int size = 640;// 高度、宽度的最大像素值
//                        // 进入裁剪界面
////                    gotoCut(mPhotoUri, aspectX, aspectY, size);
//
//                        cropPic(Uri.fromFile(new File(mPhotoUri.getPath())));
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                break;
//            case REQUESTCODE_FROMALBUM:// 从手机相册选择
//                if (resultCode == RESULT_OK) {
//
//                    @SuppressWarnings("unchecked")
//                    ArrayList<ImageViewModel> imgs = (ArrayList<ImageViewModel>) data
//                            .getSerializableExtra(AlbumActivity.EXTRA_SELECTED_PIC_MODEL);
//                    if (imgs.size() > 0) {
//                        String picturePath = imgs.get(0).getOriginalUrl();
////                    int aspectX = 2;// 宽度相对高度的比例
////                    int aspectY = 1;// 高度相对宽度的比例
////                    int size = 640;// 高度、宽度的最大像素值
//
//                        try {
////                        String imgDirPath = SDCardHelper.getSDCardRootDirectory()+"/weipu/upload_tmp/banner_temp";
////                      SDCardHelper.createDirectory(imgDirPath);
////                        String filePath = imgDirPath +"/"+ System.currentTimeMillis() + ".jpg";
////                        //拷贝一份选择的文件，进行裁切
////                        SDCardHelper.copyFile(picturePath, filePath);
////
////                        mPhotoUri = Uri.parse("file://"+filePath);
////                        // 进入裁剪界面
////                        gotoCut(mPhotoUri, aspectX, aspectY, size);
//                            cropPic(Uri.fromFile(new File(picturePath)));
//
//
//                        } catch (Exception e) {
//                            Toast.makeText(vThis, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Toast.makeText(vThis, "未选择图片", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//            case REQUESTCODE_CUT:// 裁剪
//                if (data != null)//要檢查,因為裁剪時按cancel回來也會來此,但result == null
//                {
//                    //取得裁剪後的照片
//                    Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
//                    String imgDirPath = SDCardHelper.getSDCardRootDirectory() + "/weipu/upload_tmp/banner_temp";
//                    SDCardHelper.createDirectory(imgDirPath);
//                    String filePath = imgDirPath + "/" + System.currentTimeMillis() + ".jpg";
//                    try {
//                        FileUtils.saveBitmap(filePath, cameraBitmap);
//                        displayImage(filePath, cameraBitmap);
//                    } catch (Exception e) {
//                        ViewHub.showLongToast(getApplicationContext(), "保存图片发生异常...");
//                        Log.e(getClass().getSimpleName(), "保存图片发生异常 Exception:" + e.toString());
//                        CrashReport.postCatchedException(new CatchedException("保存图片发生异常 Exception:" + e.toString()));
//                    }
//                } else {
//                    Log.w(getClass().getSimpleName(), "裁剪图片 return data is null");
//                    CrashReport.postCatchedException(new CatchedException("crop image return data is null:user cancel"));
//                }
//
////                if (resultCode == RESULT_OK) {
////                    try {
////                        if (!SDCardHelper.checkFileExists(mPhotoUri.getPath())) {
////                            Toast.makeText(vThis, "未找到图片：" + mPhotoUri.getPath(),
////                                    Toast.LENGTH_SHORT).show();
////                            return;
////                        }
////                        // 展示图片
////                        String srcPath = mPhotoUri.toString();
////                        displayImage(srcPath);
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                }
//                break;
//        }
//    }
//
//    /**
//     * 保存店招
//     */
//    private void save() {
//        if (mPhotoUri != null) {
//            SaveDataTask saveDataTask = new SaveDataTask(mPhotoUri.getPath());
//            saveDataTask.execute((Void) null);
//        } else {
//            ViewHub.showShortToast(getApplicationContext(), "请先选择一张图片...");
//        }
//    }
//
//    /**
//     * 展示图片
//     */
////    private void displayImage(String srcPath) {
////        mBannerChanged = true;
////        mPhotoUri = Uri.parse(srcPath);
////        if (srcPath.length() > 0)
////        {
////            Picasso.with(vThis).load(srcPath).placeholder(R.drawable.empty_photo).into(bannerImg);
////        }
////    }
//    private void displayImage(String srcPath, Bitmap bitmap) {
//        mBannerChanged = true;
//        // 展示图片
//        mPhotoUri = Uri.parse(srcPath);
////        Picasso.with(vThis).load(srcPath).placeholder(R.drawable.empty_photo).into(mIvShopLogo);
//        bannerImg.setImageBitmap(bitmap);
//    }
//
//
//    /**
//     * 保存店招
//     */
//    private class SaveDataTask extends AsyncTask<Void, Void, String> {
//
//        private String mLogo = "";
//
//        public SaveDataTask(String logo) {
//            mLogo = logo;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                // 店铺ID
//                String shopId = String
//                        .valueOf(SpManager.getShopId(vThis));
//                // 保存店标设置
//                if (!TextUtils.isEmpty(mLogo)) {
//                    // 本地文件名
//                    if (mLogo.startsWith("file://")) {
//                        mLogo = mLogo.substring(7);// 去除file://
//                    }
//                    // 服务器文件名
//                    String fileName = "shopbanner" + System.currentTimeMillis() + ".jpg";
//                    // 上传店标
//                    String serverPath = ShopSetAPI.getInstance().uploadImage(
//                            shopId, fileName, mLogo);
//                    if (TextUtils.isEmpty(serverPath))
//                        throw new Exception("更新店招失败，操作无法完成");
//
//                    // 保存店铺资料
//                    String cookie = PublicData.getCookie(vThis);
//                    boolean success = ShopSetAPI.getInstance().updateBanner(
//                            serverPath, cookie);
//
//                    if (success) {
//                        // 更新成功时，更新banner路径
//                        SpManager.setShopBanner(vThis, serverPath);
//                        return "OK";
//                    } else {
//                        throw new Exception("店招更新失败，操作无法完成");
//                    }
//                }
//                return "OK";
//            } catch (Exception ex) {
//                Log.e(TAG, "无法更新店招");
//                ex.printStackTrace();
//                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadingDialog.start(getString(R.string.me_loading));
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            loadingDialog.stop();
//
//            if (result.equals("OK")) {
////                mBannerChanged = false;
////                Toast.makeText(vThis, "店招保存成功", Toast.LENGTH_LONG).show();
////                // 通知myitem更新店招
////                Intent changeBannerIntent = new Intent();
////                changeBannerIntent
////                        .setAction(MyItemsActivity.MyItemsActivityChangeBannerBroadcaseName);
////                sendBroadcast(changeBannerIntent);
//            } else {
//                // 验证result
//                if (result.toString().startsWith("401")
//                        || result.toString().startsWith("not_registered")) {
//                    Toast.makeText(vThis, result.toString(), Toast.LENGTH_LONG)
//                            .show();
//                    ApiHelper.checkResult(result, vThis);
//                } else {
//                    Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        StatService.onPause(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        StatService.onResume(this);
//    }
//}
