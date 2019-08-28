package com.nahuo.quicksale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nahuo.library.controls.BottomMenuList;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageTools;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.oldermodel.ImageViewModel.UploadStatus;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FastCameraActivity extends BaseSlideBackActivity implements OnClickListener, SurfaceHolder.Callback {

    private static final int          REQUESTCODE_viewImg = 1;                                            // 大图预览
    public static final int           RESULTCODE_OK       = 101;
    public static final String        IMAGE_URL           = "com.nahuo.bw.b.FastCameraActivity.image_url";
    private FastCameraActivity        vThis               = this;
    private String                    TAG                 = "FastCameraActivity";
    private Camera                    camera              = null;
    private SurfaceHolder             mSurfaceHolder;
    private boolean                   bPreviewing         = false;
    private int                       intScreenWidth      = 0;
    private int                       intScreenHeight     = 0;
    private String                    nowFlashModel       = Parameters.FLASH_MODE_AUTO;                   // 当前闪光灯状态
    private int                       cameraPosition      = 1;                                            // 0代表前置摄像头，1代表后置摄像头
    private int                       cameraID            = -1;                                           // 当前使用摄像头ID

    private Button                    mCloseBtn, mEnterBtn, mCutCameraBtn, mFlashlightBtn, mCheeseBtn;
    // private Button btnLookup, btnDeletePhoto, btnCancel_menu;
    private LinearLayout              mTakedPhotoLL;
    private SurfaceView               mSurfaceView;
    private ImageView                 mCurrentImageView;                                                  // 当前操作的拍照图对象
                                                                                                           // private
                                                                                                           // PopupWindowEx
                                                                                                           // pwPhotoMenu;
    private int                       mImageView_width    = 0;                                            // 下方快拍图片展示区域单张图片宽度
    private int                       mImageView_height   = 0;                                            // 下方快拍图片展示区域单张图片高度
    private boolean                   isTaking            = false;                                        // 表示当前正在拍照处理中，不允许再次拍照
    private ImageView                 mFocusImg;
    private int                       mFocusImgSize;
    private Animation                 mFocusAnimation;
    private AnimationSet              animationSet;

    private int                       photoMax            = 9;

    public final static String        strTakedPhotoDir    = SDCardHelper.getSDCardRootDirectory()
                                                                  + "/weipu/tackPhoto_Tmp/";
    private String                    bucket              = "tackPhoto_Tmp";
    private int                       bucket_id           = 111112;
    private ArrayList<ImageViewModel> mTackedPhotos       = new ArrayList<ImageViewModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不要标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fast_camera);

        // 获取还能拍多少张图片
        Intent intent = getIntent();
        photoMax = intent.getIntExtra("hasCount", 9);

        mImageView_width = FunctionHelper.dip2px(getResources(), 55);
        mImageView_height = mImageView_width;

        // getDisplayMetrics();
        findViews();
        getSurfaceHolder();
    }

    // 加载摄像头设备ID
    private void loadCameraID() {
        int cameraCount = 0;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
            if (cameraPosition == 1) {
                // 现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    cameraID = i;
                    if (cameraCount == 1) {
                        // 如果只有一个摄像头，那么就不用切换了，直接隐藏掉切换功能
                        mCutCameraBtn.setVisibility(View.INVISIBLE);
                    }
                    return;
                }
            } else {
                // 现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    cameraID = i;
                    if (cameraCount == 1) {
                        // 如果只有一个摄像头，那么就不用切换了，直接隐藏掉切换功能
                        mCutCameraBtn.setVisibility(View.INVISIBLE);
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void onPause() {
        // 被交换到后台时执行
        // camera.release();
        // camera = null;
        bPreviewing = false;
        super.onPause();
    }

    @Override
    public void onStop() {
        resetCamera();
        super.onStop();
    }

    @Override
    public void onResume() {

        try {
            initCamera();
        } catch (IOException e) {
            Log.e(TAG, "initCamera() in Resume() erorr!");
        }
        super.onResume();
    }

    // @Override
    // protected void onDestroy() {
    // super.onDestroy();
    //
    // resetCamera();
    // }
    /*
     * function: 非preview时：实例化Camera,开始preview 非preview时and相机打开时：再设置一次preview preview时：不动作
     */
    private void initCamera() throws IOException {
        if (!bPreviewing) {
            /* 若相机非在预览模式，则打开相机 */
            try {
                if (cameraID == -1) {
                    loadCameraID();
                }
                camera = Camera.open(cameraID);
            } catch (Exception ex) {
                Toast.makeText(FastCameraActivity.this, "摄像设备不存在或不可用，请调整后再试", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            camera.setDisplayOrientation(90);
        }
        // 非预览时and相机打开时，开启preview
        if (camera != null && !bPreviewing) {
            Log.i(TAG, "inside the camera");
            /* 创建Camera.Parameters对象 */
            Camera.Parameters parameters = camera.getParameters();

            // 在后置摄像头下，才使用闪光灯
            if (cameraPosition == 1) {
                parameters.setFlashMode(nowFlashModel);
                mFlashlightBtn.setVisibility(View.VISIBLE);
            } else {
                mFlashlightBtn.setVisibility(View.INVISIBLE);
            }
            // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
            /* 设置相片格式为JPEG */
            parameters.setPictureFormat(PixelFormat.JPEG);
            // parameters.setPreviewFormat(PixelFormat.YCbCr_422_SP);

            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
            // 这里加try是有可能从getSupportedPreviewSizes取到的尺寸，在真实的摄像头驱动中没这个尺寸，这里做一个预备方案，如果取最大的那个尺寸设置会崩溃，那么取一次第二大的尺寸再次设置，如果还崩溃，就祈祷吧
            try {
                if (intScreenWidth <= 10) {
                    // 选择合适的预览尺寸
                    for (Camera.Size size : sizeList) {
                        // Log.i("aaaaaaaa",size.width+"|"+size.height);
                        if (size.height < size.width) {// android设备的允许高宽和实际是上使用的高宽是反的
                            if (intScreenWidth < size.width && intScreenHeight < size.height) {
                                intScreenWidth = size.width;
                                intScreenHeight = size.height;
                            }
                        }
                    }
                }

                if (intScreenWidth > 0) {
                    /* 指定preview的屏幕大小 */
                    parameters.setPreviewSize(intScreenWidth, intScreenHeight);
                    /* 设置图片分辨率大小 */
                    parameters.setPictureSize(intScreenWidth, intScreenHeight);
                }
                camera.setParameters(parameters);
            } catch (Exception e) {
                int secendWidth = 0;
                int secendHeight = 0;
                // 选择合适的预览尺寸
                for (Camera.Size size : sizeList) {
                    // Log.i("aaaaaaaa",size.width+"|"+size.height);
                    if (size.height < size.width) {// android设备的允许高宽和实际是上使用的高宽是反的
                        if (secendWidth < size.width && secendHeight < size.height && size.width < intScreenWidth
                                && size.height < intScreenHeight) {
                            secendWidth = size.width;
                            secendHeight = size.height;
                        }
                    }
                }

                if (secendWidth > 0) {
                    /* 指定preview的屏幕大小 */
                    parameters.setPreviewSize(secendWidth, secendHeight);
                    /* 设置图片分辨率大小 */
                    parameters.setPictureSize(secendWidth, secendHeight);
                }
                camera.setParameters(parameters);
                // WindowManager manager = (WindowManager)getSystemService(vThis.WINDOW_SERVICE);
                // Display display = manager.getDefaultDisplay();
                // parameters.setPreviewSize(display.getWidth(),display.getHeight());
                // parameters.setPictureSize(display.getWidth(),display.getHeight());
                // camera.setParameters(parameters);
            }
            /* setPreviewDisplay唯一的参数为SurfaceHolder */
            camera.setPreviewDisplay(mSurfaceHolder);
            /* 立即运行Preview */
            camera.startPreview();
            camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
            // 实现自动对焦
            camera.autoFocus(new AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        camera.cancelAutoFocus();// 只有加上了这一句，才会自动对焦。
                    }
                }

            });
            bPreviewing = true;
        }
    }

    /* func:停止preview,释放Camera对象 */
    private void resetCamera() {
        if (camera != null && bPreviewing) {
            camera.stopPreview();
            /* 释放Camera对象 */
            camera.release();
            camera = null;
            bPreviewing = false;
        }
    }

    /* func:停止preview */
    private void stopPreview() {
        if (camera != null && bPreviewing) {
            Log.v(TAG, "stopPreview");
            camera.stopPreview();
        }
    }

    private void takeAPicture() {
        if (camera != null && bPreviewing) {
            if (mTackedPhotos.size() < photoMax) {
                if (!isTaking) {
                    /* 调用takePicture()方法拍照 */
                    camera.takePicture(shutterCallback, rawCallback, jpegCallback);// 调用PictureCallback
                                                                                   // interface的对象作为参数
                    isTaking = true;
                }
            } else {
                Toast.makeText(FastCameraActivity.this, "已经拍满9张照片了哦，删除一些再拍吧", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* func:获取屏幕分辨率 */
    private void getDisplayMetrics() {
        // DisplayMetrics dm = new DisplayMetrics();
        // getWindowManager().getDefaultDisplay().getMetrics(dm);
        // intScreenWidth = dm.widthPixels;
        // intScreenHeight = dm.heightPixels;
        // Log.i(TAG, Integer.toString(intScreenWidth));
        // WindowManager wm = (WindowManager)
        // getSystemService(this.WINDOW_SERVICE);
        // Display display = wm.getDefaultDisplay();
        // intScreenWidth = display.getWidth();
        // intScreenHeight = display.getHeight();
    }

    private ShutterCallback shutterCallback = new ShutterCallback() {
                                                public void onShutter() {
                                                    // Shutter has closed
                                                }
                                            };

    private PictureCallback rawCallback     = new PictureCallback() {
                                                public void onPictureTaken(byte[] _data, Camera _camera) {
                                                    // TODO Handle RAW image data
                                                }
                                            };

    private PictureCallback jpegCallback    = new PictureCallback() {
                                                public void onPictureTaken(byte[] _data, Camera _camera) {
                                                    // 保存
                                                    Log.v("asdasdasd", String.valueOf(_data.length));
                                                    BitmapFactory.Options options = new BitmapFactory.Options();
                                                    options.inJustDecodeBounds = true;
                                                    // 首先设置.inJustDecodeBounds为true
                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(_data, 0,
                                                            _data.length, options);
                                                    // 得到图片有宽和高的options对象后，设置.inJustDecodeBounds为false。
                                                    int be = (int)(options.outHeight / (float)1000);
                                                    if (be <= 0)
                                                        be = 1;
                                                    // 计算得到图片缩小倍数
                                                    options.inSampleSize = be;
                                                    options.inJustDecodeBounds = false;
                                                    options.inPurgeable = true;
                                                    options.inInputShareable = true;
                                                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                                                    // 获取真正的图片对象（缩略图）
                                                    bitmap = BitmapFactory.decodeByteArray(_data, 0, _data.length,
                                                            options);

                                                    // Bitmap bitmap = BitmapFactory.decodeByteArray(_data, 0,
                                                    // _data.length);
                                                    if (bitmap != null) {
                                                        if (!SDCardHelper.createDirectory(strTakedPhotoDir)) {
                                                            Toast.makeText(FastCameraActivity.this,
                                                                    "无法在存储卡上建立目录，请检查原因", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }
                                                        // 组合一个保存到sd卡中的路径，临时存储图片，并把图片路径保存下来
                                                        String fileName = TimeUtils.dateToTimeStamp(new Date(),
                                                                "yyyyMMddHHmmssSSS") + ".jpg";
                                                        String filePath = strTakedPhotoDir + fileName;
                                                        File myCaptureFile = new File(filePath);

                                                        BufferedOutputStream bos = null;
                                                        try {
                                                            bos = new BufferedOutputStream(new FileOutputStream(
                                                                    myCaptureFile));
                                                            bitmap.compress(CompressFormat.JPEG, 100, bos);
                                                            bos.close();
                                                            bitmap.recycle();

                                                            // 校正旋转,这种连拍情况下，image会旋转，这里根据调整为正确状态
                                                            if (cameraPosition == 1) {
                                                                ImageTools.checkImgRotaing(filePath, false, 90, false);
                                                            } else {
                                                                ImageTools.checkImgRotaing(filePath, false, 270, true);
                                                            }

                                                            // 生成缩略图，并返回缩略图路径
                                                            String imgUrl = ImageTools.createThumb(filePath, 480, 640,
                                                                    50, false);
                                                            // 将图片添加到图片展示区
                                                            ImageViewModel imageViewModel = new ImageViewModel();
                                                            imageViewModel.setCanRemove(false);
                                                            imageViewModel.setLoading(false);
                                                            imageViewModel.setNewAdd(true);
                                                            imageViewModel.setUploadStatus(UploadStatus.NONE);
                                                            imageViewModel.setUrl(imgUrl);
                                                            imageViewModel.setOriginalUrl(filePath);
                                                            // 保存起来图片路径，方便返回
                                                            mTackedPhotos.add(imageViewModel);

                                                            // 在tackedphoto层展示出一张图片，显示出刚才拍照的结果，方便查看和删除
                                                            addImgViewToTakedPhto(imageViewModel);

                                                            mEnterBtn.setText("确定" + mTackedPhotos.size() + "/"
                                                                    + photoMax);

                                                            ImageTools.saveImageExternal(vThis, filePath, _data.length,
                                                                    fileName, bucket, bucket_id, 0, 0);
                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        bitmap.recycle();
                                                        bitmap = null;
                                                    }
                                                    // 重启摄像头
                                                    resetCamera();
                                                    try {
                                                        initCamera();
                                                    } catch (Exception e) {
                                                        Log.e(TAG, "initCamera Error after snapping");
                                                    }
                                                    isTaking = false;
                                                }
                                            };

    private void addImgViewToTakedPhto(ImageViewModel imageViewModel) {
        ImageView imgItem = new ImageView(vThis);
        Picasso.with(vThis).load(imageViewModel.getUrl()).placeholder(R.drawable.empty_photo).into(imgItem);
        // 设置控件的高、宽、左间距、右间距
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)imgItem.getLayoutParams();
        if (params == null)
            params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.MATCH_PARENT);
        params.topMargin = 3;
        params.width = mImageView_width;
        params.height = mImageView_height;
        imgItem.setLayoutParams(params);
        imgItem.setTag(imageViewModel.getOriginalUrl());
        imgItem.setOnClickListener(new OnClickListener() {

            // 单击弹出菜单
            @Override
            public void onClick(View v) {
                // 记录当前图片项
                mCurrentImageView = (ImageView)v;
                // 弹出操作菜单
                togglePopupWindow(v);
            }
        });
        mTakedPhotoLL.addView(imgItem);

    }

    /**
     * 弹出或关闭PopupWindow
     * 
     * @param view 当前被点击的控件
     * */
    private void togglePopupWindow(View view) {
        // 隐藏软键盘
        FunctionHelper.hideSoftInput(view.getWindowToken(), vThis);
        BottomMenuList menu = new BottomMenuList(this);
        menu.setItems(getResources().getStringArray(R.array.photo_menu1_texts))
                .setOnMenuItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                lookup(mCurrentImageView);
                                break;
                            case 1:
                                removeImageItem(mCurrentImageView);
                                break;
                            default:
                                break;
                        }
                    }
                }).show();

        // if (pwPhotoMenu.isShowing()) {
        // pwPhotoMenu.dismiss();
        // } else {
        // pwPhotoMenu
        // .showAtLocation(findViewById(R.id.fast_camera), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        // }
    }

    /* get a fully initialized SurfaceHolder */
    private void getSurfaceHolder() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    // 从xml加载控件对象
    private void findViews() {
        // View photoMenuPwView = LayoutInflater.from(vThis).inflate(R.layout.layout_pw_photo_menu1, null);
        // pwPhotoMenu = new PopupWindowEx(photoMenuPwView, R.id.camera_pw_photo_menu1, LayoutParams.MATCH_PARENT,
        // LayoutParams.WRAP_CONTENT, true);
        // pwPhotoMenu.setAnimationStyle(R.style.PopupBottomAnimation);
        mTakedPhotoLL = (LinearLayout)findViewById(R.id.camera_takedPhoto);
        mCloseBtn = (Button)findViewById(R.id.camera_close);
        mCutCameraBtn = (Button)findViewById(R.id.camera_cutCamera);
        mFlashlightBtn = (Button)findViewById(R.id.camera_flashlight);
        mCheeseBtn = (Button)findViewById(R.id.camera_cheese);
        mEnterBtn = (Button)findViewById(R.id.camera_enter);
        mFocusImg = (ImageView)findViewById(R.id.camera_focus);
        mEnterBtn.setText("确定" + mTackedPhotos.size() + "/" + photoMax);
        mSurfaceView = (SurfaceView)findViewById(R.id.camera_mSurfaceView);
        // mSurfaceView.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // camera.autoFocus(null);
        // }
        // });
        mSurfaceView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mFocusAnimation == null) {
                    // 对焦变量和动画
                    mFocusImgSize = FunctionHelper.dip2px(vThis.getResources(), 100);// 对焦框的默认大小
                    mFocusAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    mFocusAnimation.setRepeatCount(0);
                    mFocusAnimation.setAnimationListener(new AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            mFocusImg.setVisibility(View.GONE);
                        }
                    });
                    mFocusAnimation.setDuration(500);
                    animationSet = new AnimationSet(true);
                    animationSet.addAnimation(mFocusAnimation);
                }
                // 展示一个对焦动画
                if (mFocusImg.getVisibility() == View.VISIBLE) {
                    mFocusImg.setVisibility(View.GONE);
                }
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mFocusImg.getLayoutParams();
                if (params == null)
                    params = new RelativeLayout.LayoutParams(mFocusImgSize, mFocusImgSize);
                mFocusImg.setLayoutParams(params);
                params.setMargins((int)(event.getX() - mFocusImgSize / 2), (int)(event.getY() - mFocusImgSize / 2), 0,
                        0);// 原大小
                mFocusImg.setVisibility(View.VISIBLE);
                mFocusImg.startAnimation(animationSet);

                camera.autoFocus(null);
                return false;
            }
        });
        mCloseBtn.setTag(1);
        mCloseBtn.setOnClickListener(this);
        mCutCameraBtn.setTag(2);
        mCutCameraBtn.setOnClickListener(this);
        mFlashlightBtn.setTag(3);
        mFlashlightBtn.setOnClickListener(this);
        mCheeseBtn.setTag(4);
        mCheeseBtn.setOnClickListener(this);
        mEnterBtn.setTag(8);
        mEnterBtn.setOnClickListener(this);

        // btnLookup = (Button)photoMenuPwView.findViewById(R.id.camera_pw_photo_menu_btnLookup);
        // btnDeletePhoto = (Button)photoMenuPwView.findViewById(R.id.camera_pw_photo_menu_btnDelete);
        // btnCancel_menu = (Button)photoMenuPwView.findViewById(R.id.camera_pw_photo_menu_btnCancel);
        // btnLookup.setTag(5);
        // btnLookup.setOnClickListener(this);
        // btnDeletePhoto.setTag(6);
        // btnDeletePhoto.setOnClickListener(this);
        // btnCancel_menu.setTag(7);
        // btnCancel_menu.setOnClickListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!bPreviewing) {
            try {
                camera = Camera.open();
            } catch (Exception ex) {
                Toast.makeText(FastCameraActivity.this, "摄像设备不存在或不可用，请调整后再试", Toast.LENGTH_SHORT).show();
                finish();
            }
            camera.setDisplayOrientation(90);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (bPreviewing) {
            camera.stopPreview();
        }

        Camera.Parameters params = camera.getParameters();
        // Log.v("width",String.valueOf( params.getPreviewSize().width));
        // Log.v("heigth",String.valueOf(params.getPreviewSize().height));
        // Log.v("width",String.valueOf( width));
        // Log.v("heigth",String.valueOf(height));
        // params.setPreviewSize(width,height);
        camera.setParameters(params);
        try {
            camera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
        camera.autoFocus(null);// 对焦一次
        bPreviewing = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            bPreviewing = false;
            camera.release();
            mSurfaceHolder = null;
        }
    }

    @Override
    public void onClick(View v) {
        int tag = Integer.parseInt(String.valueOf(v.getTag()));
        switch (tag) {
            case 1:
                finish();
                break;
            case 2:
                // 切换前后摄像头
                changeCamera();
                break;
            case 3:
                // 设置闪光灯
                changeFlashModel();
                break;
            case 4:
                takeAPicture();
                break;
            case 5:// 操作菜单-查看大图
                togglePopupWindow(v);
                lookup(mCurrentImageView);
                break;
            case 6:// 操作菜单-删除
                togglePopupWindow(v);
                removeImageItem(mCurrentImageView);
                break;
            case 7:// 操作菜单-取消
                mCurrentImageView = null;
                togglePopupWindow(v);
                break;
            case 8:
                Bundle bundle = new Bundle();
                bundle.putSerializable("imgs", (Serializable)mTackedPhotos);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULTCODE_OK, intent);
                finish();
                break;
        }
    }

    /**
     * 切换闪光灯模式
     * */
    private void changeFlashModel() {
        resetCamera();

        // 切换3种状态闪光灯：自动(1)，打开(2)，关闭(3)
        if (nowFlashModel == Parameters.FLASH_MODE_AUTO) {
            mFlashlightBtn.setBackgroundResource(R.drawable.camera_flashlight_open_bg);
            nowFlashModel = Parameters.FLASH_MODE_ON;
        } else if (nowFlashModel == Parameters.FLASH_MODE_ON) {
            mFlashlightBtn.setBackgroundResource(R.drawable.camera_flashlight_close_bg);
            nowFlashModel = Parameters.FLASH_MODE_OFF;
        } else {
            mFlashlightBtn.setBackgroundResource(R.drawable.camera_flashlight_auto_bg);
            nowFlashModel = Parameters.FLASH_MODE_AUTO;
        }

        // 重启刷新摄像头
        try {
            initCamera();
        } catch (IOException e) {
            Log.e(TAG, "initCamera() in Resume() erorr!");
        }
    }

    private void changeCamera() {
        resetCamera();

        if (cameraPosition == 1) {
            cameraPosition = 0;
        } else {
            cameraPosition = 1;
        }
        loadCameraID();

        // 重启刷新摄像头
        try {
            initCamera();
        } catch (IOException e) {
            Log.e(TAG, "initCamera() in Resume() erorr!");
        }
    }

    /**
     * 查看大图
     * */
    private void lookup(View view) {
        if (view == null)
            return;
        // 判断图片是否是新添加的，新添加的图片读取原图路径，否则读取网络路径
        String imgUrl = "";
        ImageView imgItem = (ImageView)view;
        imgUrl = "file://" + imgItem.getTag();

        resetCamera();

        // 查看大图
//        Intent intent = new Intent(vThis, ItemImageViewActivity.class);
//        intent.putExtra(UploadItemActivity.IMAGE_URL, imgUrl);
//        startActivityForResult(intent, REQUESTCODE_viewImg);

        mCurrentImageView = null;
    }

    /**
     * 删除照片
     * */
    private void removeImageItem(View view) {
        if (view == null)
            return;
        if (view instanceof ImageView) {
            mTackedPhotos.remove(view.getTag());
            mCurrentImageView = null;
            mTakedPhotoLL.removeView(view);

            mEnterBtn.setText("确定" + mTackedPhotos.size() + "/" + photoMax);
        }
    }

    /**
     * 接收从其他界面返回的数据
     * */
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_viewImg) {
            // 重启刷新摄像头
            try {
                initCamera();
            } catch (IOException e) {
                Log.e(TAG, "initCamera() in Resume() erorr!");
            }
        }
    }
}
