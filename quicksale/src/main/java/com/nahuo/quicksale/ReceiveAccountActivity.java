//package com.nahuo.quicksale;
//
//import java.io.File;
//
//import android.content.ClipData;
//import android.content.ClipboardManager;
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
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.mobstat.StatService;
//import com.nahuo.library.controls.BottomMenuList;
//import com.nahuo.library.controls.EditTextEx;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.library.helper.SDCardHelper;
//import com.nahuo.quicksale.api.ApiHelper;
//import com.nahuo.quicksale.api.ReceiveAccountAPI;
//import com.nahuo.quicksale.api.ShopSetAPI;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.NahuoShare;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.oldermodel.PublicData;
//import com.nahuo.quicksale.oldermodel.ReceiveAccountModel;
//import com.squareup.picasso.Picasso;
//
//public class ReceiveAccountActivity extends BaseActivity implements OnClickListener {
//
//    private static final String    TAG                   = "ReceiveAccountActivity";
//    private static final int       REQUESTCODE_TAKEPHOTO = 1;                       // 拍照
//    private static final int       REQUESTCODE_FROMALBUM = 2;                       // 从手机相册选择
//    private static final int       REQUESTCODE_CUT       = 3;                       // 裁剪
//    private ReceiveAccountActivity vThis                 = this;
//
//    private TextView               tvTitle;
//    private Button                 btnLeft, btnRight;
//    private Button                 btnedit, btnShareWX;
//    private ImageView              imgBg, imgBg1, imgPic;
//    private LinearLayout           llshareView;
//    private EditTextEx             txtReceiveaccountText;
//
//    private ReceiveAccountModel    data;
//    private int                    userID;
//    private String                 userName;
//    private LoadingDialog          loadingDialog;
//    // private PopupWindowEx pwAddPhoto;
//    // private PopupWindowEx pwEditPhoto;
//    // private Button btnTakePhoto, btnFromAlbum, btnCancel;
//    // private Button btnDelete, btnLookup, btnEditCancel;
//    private Uri                    mPicPath              = null;                    // 图片文件路径
//    private SaveDataTask           saveDataTask;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
//        setContentView(R.layout.activity_receive_account);
//       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局
//
//        loadingDialog = new LoadingDialog(vThis);
//
//        Intent intent = getIntent();
//        userID = intent.getIntExtra("userID", 0);
//
//        initView();
//        if (userID == 0) {
//            userID = SpManager.getUserId(vThis);
//            ReadReceiveAccountTask readReceiveAccountTask = new ReadReceiveAccountTask(userID);
//            readReceiveAccountTask.execute((Void)null);
//
//        } else {
//            userName = (String)intent.getStringExtra("userName");
//            data = (ReceiveAccountModel)intent.getSerializableExtra("data");
//            initData();
//        }
//
//    }
//
//    private void initView() {
//        // 标题栏
//        tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
//        btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
//        btnRight = (Button)findViewById(R.id.titlebar_btnRight);
//        tvTitle.setText(R.string.title_activity_receiveaccount);
//        btnLeft.setText(R.string.titlebar_btnBack);
//        btnRight.setText(R.string.receiveaccount_save);
//        btnLeft.setVisibility(View.VISIBLE);
//        btnLeft.setOnClickListener(this);
//        btnRight.setOnClickListener(this);
//        btnRight.setVisibility(View.VISIBLE);
//
//        // view
//        llshareView = (LinearLayout)findViewById(R.id.receiveaccount_shareView);
//        btnedit = (Button)findViewById(R.id.receiveaccount_edit);
//        btnShareWX = (Button)findViewById(R.id.receiveaccount_share_wx);
//        imgBg = (ImageView)findViewById(R.id.receiveaccount_imgPicBg);
//        imgBg1 = (ImageView)findViewById(R.id.receiveaccount_imgPicBg1);
//        imgPic = (ImageView)findViewById(R.id.receiveaccount_imgPic);
//        txtReceiveaccountText = (EditTextEx)findViewById(R.id.receiveaccount_text);
//        btnedit.setOnClickListener(this);
//        btnShareWX.setOnClickListener(this);
//        btnRight.setOnClickListener(this);
//        imgBg1.setOnClickListener(this);
//        imgPic.setOnClickListener(this);
//
//        // 添加照片选择方式的弹出框
////        View addPhotoPwView = LayoutInflater.from(vThis).inflate(R.layout.layout_pw_addphoto, null);
////        btnTakePhoto = (Button)addPhotoPwView.findViewById(R.id.uploaditem_pw_addphoto_btnTakePhoto);
////        btnFromAlbum = (Button)addPhotoPwView.findViewById(R.id.uploaditem_pw_addphoto_btnFromAlbum);
////        btnCancel = (Button)addPhotoPwView.findViewById(R.id.uploaditem_pw_addphoto_btnCancel);
////        btnTakePhoto.setOnClickListener(this);
////        btnFromAlbum.setOnClickListener(this);
////        btnCancel.setOnClickListener(this);
////        pwAddPhoto = new PopupWindowEx(addPhotoPwView, R.id.uploaditem_pw_addphoto, LayoutParams.MATCH_PARENT,
////                LayoutParams.WRAP_CONTENT, true);
////        pwAddPhoto.setAnimationStyle(R.style.PopupBottomAnimation);
//        // 修改照片选择方式弹出框
////        View editPhotoPwView = LayoutInflater.from(vThis).inflate(R.layout.layout_pw_receive_account_menu, null);
////        btnDelete = (Button)editPhotoPwView.findViewById(R.id.layout_pw_receive_account_menu_btnDelete);
////        btnLookup = (Button)editPhotoPwView.findViewById(R.id.layout_pw_receive_account_menu_btnLookup);
////        btnEditCancel = (Button)editPhotoPwView.findViewById(R.id.layout_pw_receive_account_menu_btnCancel);
////        btnDelete.setOnClickListener(this);
////        btnLookup.setOnClickListener(this);
////        btnEditCancel.setOnClickListener(this);
////        pwEditPhoto = new PopupWindowEx(editPhotoPwView, R.id.layout_receive_account_pw_editphoto,
////                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
////        pwEditPhoto.setAnimationStyle(R.style.PopupBottomAnimation);
//
//        txtReceiveaccountText.setEnabled(false);
//        llshareView.setVisibility(View.INVISIBLE);
//        btnRight.setVisibility(View.INVISIBLE);
//        imgPic.setVisibility(View.INVISIBLE);
//        imgBg1.setVisibility(View.INVISIBLE);
//        imgBg.setVisibility(View.INVISIBLE);
//    }
//
//    /**
//     * 初始化数据
//     * */
//    private void initData() {
//        if (data.getImages().length() > 0) {
//            // 店铺名称
//            txtReceiveaccountText.setText(data.getAccounts());
//            // 加载收款图片
//            String url = ImageUrlExtends.getImageUrl(data.getImages(), Const.HEADER_BG_SIZE);
//            Picasso.with(vThis).load(url).placeholder(R.drawable.empty_photo).into(imgPic);
//
//            if (userID == SpManager.getUserId(vThis)) {// 自己的收款信息
//                tvTitle.setText("我的收款信息");
//                txtReceiveaccountText.setEnabled(false);
//                // imgPic.setEnabled(false);
//                llshareView.setVisibility(View.VISIBLE);
//                btnRight.setVisibility(View.INVISIBLE);
//                imgPic.setVisibility(View.VISIBLE);
//                imgBg1.setVisibility(View.INVISIBLE);
//                imgBg.setVisibility(View.INVISIBLE);
//            } else {// 其他人的收款信息，不允许分享，保存操作
//                tvTitle.setText(userName + "的收款信息");
//                txtReceiveaccountText.setEnabled(false);
//                // imgPic.setEnabled(false);
//                llshareView.setVisibility(View.INVISIBLE);
//                btnRight.setVisibility(View.INVISIBLE);
//                imgPic.setVisibility(View.VISIBLE);
//                imgBg1.setVisibility(View.INVISIBLE);
//                imgBg.setVisibility(View.INVISIBLE);
//            }
//        } else {
//            tvTitle.setText(R.string.title_activity_receiveaccount);
//            txtReceiveaccountText.setEnabled(true);
//            llshareView.setVisibility(View.INVISIBLE);
//            btnRight.setVisibility(View.VISIBLE);
//            imgPic.setImageDrawable(null);
//            imgPic.setVisibility(View.INVISIBLE);
//            imgBg1.setVisibility(View.VISIBLE);
//            imgBg.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.titlebar_btnLeft:
//                finish();
//                break;
//            case R.id.titlebar_btnRight:
//                // 调用api保存收款账号和说明
//                save();
//                break;
//            case R.id.receiveaccount_edit:
//                llshareView.setVisibility(View.INVISIBLE);
//                btnRight.setVisibility(View.VISIBLE);
//                txtReceiveaccountText.setEnabled(true);
//                imgPic.setEnabled(true);
//                break;
//            case R.id.receiveaccount_share_wx:
//                receiveShare();
//                break;
//            case R.id.receiveaccount_imgPicBg:
//            case R.id.receiveaccount_imgPicBg1:
//                togglePopupWindow(true);
//                break;
//            case R.id.receiveaccount_imgPic:
//                if (txtReceiveaccountText.isEnabled()) {
//                    togglePopupWindow(false);
//                } else {
//                    lookup();
//                }
//                break;
//        // case R.id.uploaditem_pw_addphoto_btnTakePhoto:// 拍照
//        // togglePopupWindow(true);
//        // tackPhoto();
//        // break;
//        // case R.id.uploaditem_pw_addphoto_btnFromAlbum:// 从手机相册选择
//        // togglePopupWindow(true);
//        // fromAblum();
//        // break;
//        // case R.id.uploaditem_pw_addphoto_btnCancel:// 添加照片-取消
//        // togglePopupWindow(true);
//        // break;
//        // case R.id.layout_pw_receive_account_menu_btnDelete:// 删除
//        // togglePopupWindow(false);
//        // mPicPath = null;
//        // imgPic.setImageDrawable(null);
//        // imgPic.setVisibility(View.INVISIBLE);
//        // imgBg1.setVisibility(View.VISIBLE);
//        // imgBg.setVisibility(View.VISIBLE);
//        // break;
//        // case R.id.layout_pw_receive_account_menu_btnLookup:// 查看大图
//        // togglePopupWindow(false);
//        // lookup();
//        // break;
//        // case R.id.layout_pw_receive_account_menu_btnCancel:// 关闭选择
//        // togglePopupWindow(false);
//        // break;
//
//        }
//    }
//
//    /**
//     * 查看大图
//     * */
//    private void lookup() {
//        String lookupPath = "";
//        if (mPicPath != null) {
//            lookupPath = "file://" + mPicPath.getPath();
//        } else {
//            lookupPath = ImageUrlExtends.getImageUrl(data.getImages(),Const.HEADER_BG_SIZE);
//        }
//
//        // 查看大图
//        Intent intent = new Intent(vThis, ItemImageViewActivity.class);
//        intent.putExtra(ItemImageViewActivity.IMAGE_URL, lookupPath);
//        startActivity(intent);
//    }
//
//    /**
//     * 弹出或关闭PopupWindow
//     *
//     * @param view 当前被点击的控件
//     * */
//    private void togglePopupWindow(boolean add) {
//        if (add) {
//            BottomMenuList menu = new BottomMenuList(this);
//            menu.setItems(getResources().getStringArray(R.array.menu_upload_image_texts))
//                    .setOnMenuItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            switch (position) {
//                                case 0:
//                                    tackPhoto();
//                                    break;
//                                case 1:
//                                    fromAblum();
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }).show();
//            // if (pwAddPhoto.isShowing()) {
//            // pwAddPhoto.dismiss();
//            // } else {
//            // pwAddPhoto.showAtLocation(findViewById(R.id.receiveaccount_imgPicBg), Gravity.BOTTOM
//            // | Gravity.CENTER_HORIZONTAL, 0, 0);
//            // }
//        } else {
//            BottomMenuList menu = new BottomMenuList(this);
//            menu.setItems(getResources().getStringArray(R.array.menu_receive_account_texts))
//                    .setOnMenuItemClickListener(new OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            switch (position) {
//                                case 0:
//                                    mPicPath = null;
//                                    imgPic.setImageDrawable(null);
//                                    imgPic.setVisibility(View.INVISIBLE);
//                                    imgBg1.setVisibility(View.VISIBLE);
//                                    imgBg.setVisibility(View.VISIBLE);
//                                    break;
//                                case 1:
//                                    lookup();
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    }).show();
//
//            // if (pwEditPhoto.isShowing()) {
//            // pwEditPhoto.dismiss();
//            // } else {
//            // pwEditPhoto.showAtLocation(findViewById(R.id.receiveaccount_imgPic), Gravity.BOTTOM
//            // | Gravity.CENTER_HORIZONTAL, 0, 0);
//            // }
//        }
//    }
//
//    /**
//     * 调用系统照相机拍照
//     * */
//    private void tackPhoto() {
//        // 文件名
//        String fileName = "receive_account" + System.currentTimeMillis() + ".jpg";
//
//        // 封装Uri
//        String imgDirPath = SDCardHelper.getDCIMDirectory() + File.separator + "weipu";
//        SDCardHelper.createDirectory(imgDirPath);
//        File file = new File(imgDirPath, fileName);
//        Uri imageUri = Uri.fromFile(file);
//        mPicPath = imageUri;
//        // 调用系统照相机
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPicPath);
//        startActivityForResult(intent, REQUESTCODE_TAKEPHOTO);
//    }
//
//    /**
//     * 从相册选择照片并裁剪
//     * */
//    private void fromAblum() {
//        // 文件名
//        String fileName = "";
//        // 宽度相对高度的比例
//        int aspectX = 2;
//        // 高度相对宽度的比例
//        int aspectY = 1;
//        // 高度、宽度的最大像素值
//        int size = 640;
//
//        fileName = "receive_account" + System.currentTimeMillis() + ".jpg";
//
//        // 封装Uri
//        String imgDirPath = SDCardHelper.getDCIMDirectory() + File.separator + "weipu";
//        SDCardHelper.createDirectory(imgDirPath);
//        File file = new File(imgDirPath, fileName);
//        Uri imageUri = Uri.fromFile(file);
//        mPicPath = imageUri;
//
//        // 调用系统相册的相关设置
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        // 输出Uri
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPicPath);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // 输出高宽比例
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        // 根据高宽比列计算高、宽
//        int width = aspectX > aspectY ? size : (size / aspectY) * aspectX;
//        int height = aspectX < aspectY ? size : (size / aspectX) * aspectY;
//        intent.putExtra("outputX", width);
//        intent.putExtra("outputY", height);
//        intent.putExtra("return-data", false);
//        startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUESTCODE_FROMALBUM);
//    }
//
//    /**
//     * 裁剪图片方法实现
//     *
//     * @param uri 照片的uri
//     * @param aspectX 宽度相对高度的比例
//     * @param aspectY 高度相对宽度的比例
//     * @param size 最大的宽度,高度
//     */
//    private void gotoCut(Uri uri, int aspectX, int aspectY, int size) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // 输出高宽比例
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        // 根据高宽比列计算高、宽
//        int width = aspectX > aspectY ? size : (size / aspectY) * aspectX;
//        int height = aspectX < aspectY ? size : (size / aspectX) * aspectY;
//        intent.putExtra("outputX", width);
//        intent.putExtra("outputY", height);
//        intent.putExtra("return-data", false);// 若为false则表示不返回数据
//        startActivityForResult(intent, REQUESTCODE_CUT);
//    }
//
//    /**
//     * 接收从其他界面返回的数据
//     * */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // 拍照
//        if (requestCode == REQUESTCODE_TAKEPHOTO) {
//            if (resultCode == RESULT_OK) {
//                if(mPicPath == null) {
//                    ViewHub.showShortToast(vThis, "请选择图片");
//                    return;
//                }
//                if (!SDCardHelper.checkFileExists(mPicPath.getPath())) {
//                    Toast.makeText(vThis, "未找到图片：" + mPicPath.getPath(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // 宽度相对高度的比例
//                int aspectX = 2;
//                // 高度相对宽度的比例
//                int aspectY = 1;
//                // 进入裁剪界面
//                gotoCut(mPicPath, aspectX, aspectY, 640);
//            }
//        }// 从手机相册选择
//        else if (requestCode == REQUESTCODE_FROMALBUM) {
//            if (resultCode == RESULT_OK) {
//                // Uri newUri = data.getData();
//                // if (mPicPath==null || newUri.getPath() != mPicPath.getPath()) {
//                // mPicPath = newUri;
//                // }
//                if (!SDCardHelper.checkFileExists(mPicPath.getPath())) {
//                    Toast.makeText(vThis, "未找到图片：" + mPicPath.getPath(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // 展示图片
//                displayImage();
//            }
//        }
//        // 裁剪
//        else if (requestCode == REQUESTCODE_CUT) {
//            if (resultCode == RESULT_OK) {
//                if (!SDCardHelper.checkFileExists(mPicPath.getPath())) {
//                    Toast.makeText(vThis, "未找到图片：" + mPicPath.getPath(), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // 展示图片
//                displayImage();
//            }
//        }
//    }
//
//    /**
//     * 展示图片
//     * */
//    private void displayImage() {
//        // 展示图片
//        Picasso.with(vThis).load(new File(mPicPath.getPath())).placeholder(R.drawable.empty_photo).into(imgPic);
//
//        imgPic.setVisibility(View.VISIBLE);
//        imgBg1.setVisibility(View.INVISIBLE);
//        imgBg.setVisibility(View.INVISIBLE);
//    }
//
//    /**
//     * 保存个性化设置
//     * */
//    private void save() {
//        saveDataTask = new SaveDataTask(mPicPath == null ? "" : mPicPath.getPath());
//        saveDataTask.execute((Void)null);
//    }
//
//    /**
//     * 保存个性设置
//     * */
//    private class SaveDataTask extends AsyncTask<Void, Void, String> {
//
//        private String mPic = "";
//
//        public SaveDataTask(String picPath) {
//            mPic = picPath;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                String serverPath_Pic = "";
//                // 店铺ID
//                String shopId = String.valueOf(SpManager.getShopId(vThis));
//                // 保存店标设置
//                if (!TextUtils.isEmpty(mPic)) {
//                    // 本地文件名
//                    if (mPic.startsWith("file://")) {
//                        mPic = mPic.substring(7);// 去除file://
//                    }
//                    // 服务器文件名
//                    String fileName = "rc" + System.currentTimeMillis() + ".jpg";
//                    // 上传店标
//                    String serverPath = ShopSetAPI.getInstance().uploadImage(shopId, fileName, mPic);
//                    if (TextUtils.isEmpty(serverPath))
//                        throw new Exception("更新收款图片失败");
//
//                    serverPath_Pic = "upyun:" + PublicData.UPYUN_BUCKET + ":" + serverPath;
//                } else {
//                    serverPath_Pic = data.getImages();
//                }
//                // 保存店铺资料
//                boolean success = ReceiveAccountAPI.getInstance().setPaymentAccount(
//                        txtReceiveaccountText.getText().toString(), serverPath_Pic, PublicData.getCookie(vThis));
//                if (success) {
//                    // 刷新一下data数据，这样不用重新读取数据了
//                    data.setAccounts(txtReceiveaccountText.getText().toString());
//                    data.setImages(serverPath_Pic);
//                    mPicPath = null;
//
//                    return "OK";
//                } else {
//                    throw new Exception("收款信息保存失败");
//                }
//
//            } catch (Exception ex) {
//                Log.e(TAG, "无法更新收款图片");
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
//            saveDataTask = null;
//
//            if (result.equals("OK")) {
//                Toast.makeText(vThis, "保存成功", Toast.LENGTH_LONG).show();
//
//                llshareView.setVisibility(View.VISIBLE);
//                btnRight.setVisibility(View.INVISIBLE);
//                txtReceiveaccountText.setEnabled(false);
//                // imgPic.setEnabled(false);
//            } else {
//                // 验证result
//                if (result.toString().startsWith("401") || result.toString().startsWith("not_registered")) {
//                    Toast.makeText(vThis, result.toString(), Toast.LENGTH_LONG).show();
//                    ApiHelper.checkResult(result, vThis);
//                } else {
//                    Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }
//
//    private void receiveShare() {
//        // 复制文字到剪贴板
//        try {
//            ClipboardManager cmb = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("label", data.getAccounts());
//            cmb.setPrimaryClip(clip);
//
//            Toast.makeText(vThis, "文字已复制到剪贴板，分享给朋友后，手动发送文字消息", Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Log.i(TAG, "复制到剪贴板失败");
//        }
//        ShareEntity shareData = new ShareEntity();
//        String imageUrl = ImageUrlExtends.getImageUrl(data.getImages(), Const.HEADER_BG_SIZE);
//        shareData.setImgUrl(imageUrl);
//        shareData.setSummary(data.getAccounts());
//        NahuoShare share = new NahuoShare(this, shareData);
//        share.addPlatforms(NahuoShare.PLATFORM_WX_CIRCLE, NahuoShare.PLATFORM_WX_FRIEND);
//        share.setShareType(NahuoShare.SHARE_TYPE_IMG);
//        share.show();
//    }
//
//    // 读取指定店铺收款信息
//    private class ReadReceiveAccountTask extends AsyncTask<Void, Void, String> {
//
//        private int UserID;
//
//        public ReadReceiveAccountTask(int userID) {
//            UserID = userID;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            if (result.equals("OK")) {
//                initData();
//            } else {
//                Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
//            }
//
//            loadingDialog.stop();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadingDialog.start(getString(R.string.items_loadData_loading));
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                String cookie = PublicData.getCookie(vThis);
//
//                data = ReceiveAccountAPI.getInstance().getReceiveAccount(String.valueOf(UserID), cookie);
//
//                return "OK";
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                if (ex.getMessage().contains("未设置")) {
//                    data = new ReceiveAccountModel();
//                    return "OK";
//                } else {
//                    return ex.getMessage();
//                }
//            }
//        }
//
//    }
//
//    public void onResume() {
//        super.onResume();
//        StatService.onResume(this);
//    }
//
//    public void onPause() {
//        super.onPause();
//        StatService.onPause(this);
//    }
//}
