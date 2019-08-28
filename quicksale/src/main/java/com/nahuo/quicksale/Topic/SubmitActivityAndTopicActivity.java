package com.nahuo.quicksale.Topic;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.permissions.RxPermissions;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.PopupWindowEx;
import com.nahuo.library.dynamicgrid.DynamicGridView;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageTools;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.AlbumActivity;
import com.nahuo.quicksale.FastCameraActivity;
import com.nahuo.quicksale.ItemImageViewActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.ExpressionAdapter;
import com.nahuo.quicksale.adapter.UploadItemPicGridAdapter;
import com.nahuo.quicksale.api.ActivityAPI;
import com.nahuo.quicksale.api.TopicAPI;
import com.nahuo.quicksale.api.UploadItemAPI;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SmileUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.event.SimpleTextWatcher;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.ActivityCategoryModel;
import com.nahuo.quicksale.oldermodel.ActivityInfoModel;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static android.R.attr.path;

/**
 * Created by 诚 on 2015/9/21.
 */

public class SubmitActivityAndTopicActivity extends Activity implements
        View.OnClickListener {

    private static final String TAG = "SubmitActivityAndTopicActivity";
    public static final int RESULTCODE_OK = 110;
    public static final String COLOR_SIZE_LIST = "com.nahuo.application.SubmitActivityAndTopicActivity.colorSizeList";
    public static final String ITEMCAT_DATACHANGE = "com.nahuo.application.SubmitActivityAndTopicActivity.itemcat_datachange";
    public static final String ITEMCAT_CURRENT = "com.nahuo.application.SubmitActivityAndTopicActivity.itemcat_current";
    public static final String ITEMCATCHILDREN_CURRENT = "com.nahuo.application.SubmitActivityAndTopicActivity.itemcatchildren_current";
    public static final String ITEMSTYLE_CURRENT = "com.nahuo.application.SubmitActivityAndTopicActivity.itemstyle_current";
    public static final String IMAGE_URL = "com.nahuo.application.SubmitActivityAndTopicActivity.image_url";

    private static final int REQUESTCODE_TAKEPHOTO = 2;// 拍照
    private static final int REQUESTCODE_FROMALBUM = 3;// 从手机相册选择

    private SubmitActivityAndTopicActivity vThis = this;
    private LoadingDialog loadingDialog;
    private Button btnLeft, btnRight, btnAddImage, btnAtivityType;
    private DynamicGridView gvUploadImage;
    private TextView tvTitle, mTvEmothions, mTvViewText;
    private Button btnActivityVisible, btnTopicVisible, btnActivityGroup,
            btnTopicGroup;
    private Spinner btnActivityType;
    private View vActivityView, vTopicView;
    private EditText edtContent, edtTitle;
    private Button btnTakePhoto, btnFromAlbum, btnCancel;
    private Button btnLookup, btnSetCoverImg, btnDeletePhoto, btnCancel_menu;
    private PopupWindowEx pwAddPhoto, pwPhotoMenu;
    private Uri mPhotoUri;

    private View mRootView;
    private List<ActivityCategoryModel> categorys;
    private ActivityInfoModel activityModel;
    private TopicInfoModel topicModel;
    private int groupID;
    private boolean isEdit;
    private UploadItemPicGridAdapter mAdapter;
    private List<ImageViewModel> mPicModels = new ArrayList<ImageViewModel>();
    public static final String EXTRA_UPLOAD_PIC_MODELS = "EXTRA_UPLOAD_PIC_MODELS";
    private static final int MAX_IMG_COUNT = 9;
    private ImageViewModel mCurrentClickedViewModel;
    private boolean mIsKeyboardShowing;// , mKeepEmotionsBtn;
    private GridView mGvEmotions;
    private List<String> mEmotionResList;// 表情资源id
    private View mLayoutEmotions;

    private ImageView mBtnvoice;
    // private VoiceHelper voicehelper;
    RxPermissions rxPermissions;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_activity_and_topic);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.my_colorPrimaryDark));
        }
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mRootView = findViewById(R.id.uploaditem);
        mTvEmothions = (TextView) findViewById(R.id.tv_emotions);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        boolean isFocused = edtContent.isFocused();
                        int heightDiff = mRootView.getRootView().getHeight()
                                - mRootView.getHeight();
                        if (heightDiff > 200) {// 键盘显示
                            mIsKeyboardShowing = true;
                            if (mGvEmotions.getVisibility() != View.GONE)
                                mGvEmotions.setVisibility(View.GONE);
                        } else {// 键盘消失
                            mIsKeyboardShowing = false;
                            if (mGvEmotions.getVisibility() != View.VISIBLE) {// 如果表情不见了
                                mGvEmotions.setVisibility(View.GONE);
                            }
                        }
                    }
                });

        groupID = getIntent().getIntExtra("groupID", 0);
        isEdit = getIntent().getBooleanExtra("isedit", false);
        activityModel = (ActivityInfoModel) getIntent().getSerializableExtra(
                "activity");
        topicModel = (TopicInfoModel) getIntent().getSerializableExtra("topic");
        if (isEdit) {// 修改状态
            // 进入修改模式，绑定值之类的
        } else {// 新增

            mPicModels = (List<ImageViewModel>) getIntent()
                    .getSerializableExtra(EXTRA_UPLOAD_PIC_MODELS);
            if (mPicModels == null) {
                mPicModels = new ArrayList<ImageViewModel>();
            }
        }
        initView();
        showImageContainer();
        GetCategoryDataTask gcdt = new GetCategoryDataTask();
        gcdt.execute((Void) null);
    }

    @Override
    public void onBackPressed() {
        int emotionVisibility = mLayoutEmotions.getVisibility();
        if (emotionVisibility == View.VISIBLE) {
            mTvEmothions.setVisibility(View.GONE);
            mGvEmotions.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    // 初始化修改商品的状态
    private void initEditItem() {
        List<String> imgArr = new ArrayList<String>();
        if (activityModel != null) {// 活动
            edtContent.setText(activityModel.getContent());
            edtTitle.setText(activityModel.getTitle());
            btnActivityVisible.setText(activityModel.getVisible());
            btnActivityGroup.setText(activityModel.getIsGroupType());
            btnActivityType.setSelection(0);// (activityModel.getType());
            imgArr = activityModel.getImages();
        } else {// 话题
            edtContent.setText(topicModel.getContent());
            edtTitle.setText(topicModel.getTitle());
            btnActivityVisible.setText(topicModel.getVisible());
            btnActivityGroup.setText(topicModel.getIsGroupType());
            imgArr = topicModel.getImages();
        }

        List<ImageViewModel> imgItems = new ArrayList<ImageViewModel>();
        // 初始化图片
        for (String img : imgArr) {

            String fullUrl = ImageUrlExtends.getImageUrl(img);

            // 将图片添加到图片展示区
            ImageViewModel imageViewModel = new ImageViewModel();
            imageViewModel.setCanRemove(true);
            imageViewModel.setNewAdd(false);
            imageViewModel.setUrl(fullUrl);
            // imageViewModel.setWebsite(img);// 原图
            imgItems.add(imageViewModel);
        }
        mAdapter.set(imgItems);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        loadingDialog = new LoadingDialog(vThis);
        mLayoutEmotions = findViewById(R.id.layout_emotions);

        initGridView();
        // 标题栏
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnRight.setText(R.string.titlebar_btnComplete);
        btnLeft.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) btnRight
                .getLayoutParams();
        params.width = 50;
        params.height = 50;
        params.rightMargin = 30;
        btnRight.setText("");
        btnRight.setPadding(5, 5, 5, 5);
        btnRight.setLayoutParams(params);
        btnRight.setBackgroundResource(R.drawable.ic_submit_comment);
        btnRight.setVisibility(View.VISIBLE);

        // 界面布局
        gvUploadImage = (DynamicGridView) findViewById(R.id.submit_activity_and_topic_gvUploadImage);

        gvUploadImage.setLastDraggable(false);
        gvUploadImage.setScrollable(false);
        gvUploadImage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                boolean isAddBtn = (Boolean) view.getTag(R.id.tag_is_add_btn);
                if (!isAddBtn) {
                    Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                    vib.vibrate(50);
                    gvUploadImage.startEditMode(position);
                }
                return true;
            }
        });
        btnActivityGroup = (Button) findViewById(R.id.submit_activity_group);
        btnActivityGroup.setOnClickListener(this);
        btnActivityType = (Spinner) findViewById(R.id.submit_activity_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(vThis,
                android.R.layout.simple_spinner_item,
                new String[]{"正在加载活动类别"});
        adapter.setDropDownViewResource(R.layout.drop_down_item);
        btnActivityType.setAdapter(adapter);
        btnActivityVisible = (Button) findViewById(R.id.submit_activity_visible);
        btnActivityVisible.setOnClickListener(this);
        vActivityView = (View) findViewById(R.id.submit_activity_view);
        mTvViewText = (TextView) findViewById(R.id.submit_activity_view_text);
        btnTopicGroup = (Button) findViewById(R.id.submit_topic_group);
        btnTopicGroup.setOnClickListener(this);
        btnTopicVisible = (Button) findViewById(R.id.submit_topic_visible);
        btnTopicVisible.setOnClickListener(this);
        vTopicView = (View) findViewById(R.id.submit_topic_view);
        if (activityModel != null) {// 活动
            vActivityView.setVisibility(View.VISIBLE);
            vTopicView.setVisibility(View.GONE);
            mTvViewText.setVisibility(View.VISIBLE);
            tvTitle.setText("添加活动");
        } else {// 话题
            vActivityView.setVisibility(View.GONE);
            vTopicView.setVisibility(View.VISIBLE);
            mTvViewText.setVisibility(View.GONE);
            tvTitle.setText("添加话题");
        }
        edtContent = (EditText) findViewById(R.id.submit_activity_and_topic_content);
        edtTitle = (EditText) findViewById(R.id.submit_activity_and_topic_title);
        edtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if ((hasFocus && mIsKeyboardShowing) || !hasFocus) {
                    mGvEmotions.setVisibility(View.GONE);
                }
            }
        });
        edtTitle.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 30) {
                    int endPos = 30;
                    if (s.length() >= 100
                            && StringUtils.isEmojiCharacter(s.charAt(29))) {
                        endPos = 28;
                    }
                    String text = StringUtils.substring(s.toString(), 0, endPos);
                    edtTitle.setText(text);
                    edtTitle.setSelection(text.length());
                    ViewHub.showShortToast(vThis, "标题只允许输入30个字");
                }
            }
        });
        btnAddImage = (Button) findViewById(R.id.submit_activity_and_topic_btnAddImage);
        // 添加照片选择方式的弹出框
        View addPhotoPwView = LayoutInflater.from(vThis).inflate(
                R.layout.layout_pw_addphoto, null);
        btnTakePhoto = (Button) addPhotoPwView
                .findViewById(R.id.pw_addphoto_btnTakePhoto);
        btnFromAlbum = (Button) addPhotoPwView
                .findViewById(R.id.pw_addphoto_btnFromAlbum);
        btnCancel = (Button) addPhotoPwView
                .findViewById(R.id.pw_addphoto_btnCancel);
        btnTakePhoto.setOnClickListener(this);
        btnFromAlbum.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        pwAddPhoto = new PopupWindowEx(addPhotoPwView, R.id.pw_addphoto,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        pwAddPhoto.setAnimationStyle(R.style.PopupBottomAnimation);
        // mItemGroup.setLeftText(mItemGroupNames);
        // 点击照片，弹出操作菜单
        View photoMenuPwView = LayoutInflater.from(vThis).inflate(
                R.layout.layout_pw_photo_menu, null);
        btnLookup = (Button) photoMenuPwView
                .findViewById(R.id.pw_photo_menu_btnLookup);
        btnSetCoverImg = (Button) photoMenuPwView
                .findViewById(R.id.pw_photo_menu_btnSetCoverImg);
        btnDeletePhoto = (Button) photoMenuPwView
                .findViewById(R.id.pw_photo_menu_btnDelete);
        btnCancel_menu = (Button) photoMenuPwView
                .findViewById(R.id.pw_photo_menu_btnCancel);
        btnLookup.setOnClickListener(this);
        btnSetCoverImg.setOnClickListener(this);
        btnDeletePhoto.setOnClickListener(this);
        btnCancel_menu.setOnClickListener(this);
        pwPhotoMenu = new PopupWindowEx(photoMenuPwView, R.id.pw_photo_menu,
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        pwPhotoMenu.setAnimationStyle(R.style.PopupBottomAnimation);

        // 添加字符串
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnAddImage.setOnClickListener(this);

        // 初始化适配器
        // mImageItemAdapter = new UploadImageItemAdapter(mImageList);
        mAdapter = new UploadItemPicGridAdapter(this, getResources()
                .getInteger(R.integer.upload_item_pic_col_num));
        mAdapter.set(mPicModels);
        gvUploadImage.setAdapter(mAdapter);
        gvUploadImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                boolean isAddBtn = (Boolean) view.getTag(R.id.tag_is_add_btn);
                if (isAddBtn) {
                    onAddImgBtnClick(view);
                } else {
                    mCurrentClickedViewModel = (ImageViewModel) view.getTag();
                    togglePopupWindow(view, 2);
                }
            }
        });

        mBtnvoice = (ImageView) findViewById(R.id.btn_set_mode_voice);

        // 弹出麦克风
        mBtnvoice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //     voicehelper = new VoiceHelper(vThis, edtTitle);
                //    voicehelper.togglePopupWindow(findViewById(R.id.uploaditem));
                return false;
            }
        });

    }

    /**
     * @description 点击添加图片按钮
     * @created 2014-12-8 下午4:05:31
     * @author ZZB
     */
    private void onAddImgBtnClick(View v) {
        if (mAdapter.getPicCount() == MAX_IMG_COUNT) {
            ViewHub.showShortToast(vThis, "最多只能添加9张图片哦！");
            return;
        } else {
            togglePopupWindow(v, 1);
        }
    }

    @Override
    public void onClick(View v) {
        mGvEmotions.setVisibility(View.GONE);
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_btnRight:// 添加商品
                addItem();
                break;
            case R.id.submit_activity_and_topic_btnAddImage:
                togglePopupWindow(v, 1);
                break;
            case R.id.pw_addphoto_btnTakePhoto:// 拍照
                togglePopupWindow(v, 1);
                takePhoto();
                break;
            case R.id.pw_addphoto_btnFromAlbum:// 从手机相册选择
                togglePopupWindow(v, 1);
                fromAblum();
                break;
            case R.id.pw_addphoto_btnCancel:// 添加照片-取消
                togglePopupWindow(v, 1);
                break;
            case R.id.pw_photo_menu_btnLookup:// 操作菜单-查看大图
                togglePopupWindow(v, 2);
                lookup();
                break;
            case R.id.pw_photo_menu_btnDelete:// 操作菜单-删除
                togglePopupWindow(v, 2);
                mAdapter.remove(mCurrentClickedViewModel);
                showImageContainer();
                break;
            case R.id.pw_photo_menu_btnCancel:// 操作菜单-取消
                togglePopupWindow(v, 2);
                break;
            case R.id.submit_activity_visible:
            case R.id.submit_topic_visible:// 谁可看帖
                break;
            case R.id.submit_activity_group:
            case R.id.submit_topic_group:// 本组成员可看
                break;
            case R.id.submit_activity_type:// 活动分类
                break;
            case R.id.tv_emotions:// 点击表情
                // int visibility = mGvEmotions.getVisibility();
                boolean isShowing = (Boolean) mGvEmotions.getTag();
                if (isShowing) {
                    mGvEmotions.setTag(false);
                    mGvEmotions.setVisibility(View.GONE);
                    ViewHub.showKeyboard(vThis, edtContent);
                } else {
                    mGvEmotions.setTag(true);
                    ViewHub.hideKeyboard(vThis);
                    mGvEmotions.setVisibility(View.VISIBLE);
                    mTvEmothions.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.submit_activity_and_topic_content:
                if (mIsKeyboardShowing) {
                    mGvEmotions.setVisibility(View.GONE);
                }
                mTvEmothions.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 弹出或关闭PopupWindow
     *
     * @param view 当前被点击的控件
     * @param type 弹出框类型：1.选择照片菜单 2.操作照片菜单
     */
    private void togglePopupWindow(View view, int type) {
        // 隐藏软键盘
        FunctionHelper.hideSoftInput(view.getWindowToken(), vThis);
        PopupWindowEx pw = null;
        switch (type) {
            case 1:
                pw = pwAddPhoto;
                break;
            case 2:
                pw = pwPhotoMenu;
                break;
        }
        if (pw.isShowing()) {
            pw.dismiss();
        } else {
            pw.showAtLocation(findViewById(R.id.uploaditem), Gravity.BOTTOM
                    | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    /**
     * 调用系统照相机
     */
    private void takePhoto() {
        try {
            if (rxPermissions == null) {
                rxPermissions = new RxPermissions(this);
            }
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                if (!SDCardHelper.IsSDCardExists()) {
                                    Toast.makeText(vThis, "系统未检测到存储卡，不能使用此功能！", Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }

                                if (!SDCardHelper.checkFileExists(FastCameraActivity.strTakedPhotoDir)) {
                                    SDCardHelper.createDirectory(FastCameraActivity.strTakedPhotoDir);
                                }
                                String fileName = TimeUtils.dateToTimeStamp(new Date(),
                                        "yyyyMMddHHmmssSSS") + ".jpg";
                                File file = new File(FastCameraActivity.strTakedPhotoDir, fileName);
                                String authority = SubmitActivityAndTopicActivity.this.getPackageName() + ".provider";
                                Uri imageUri;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    //通过FileProvider创建一个content类型的Uri
                                    imageUri = FileProvider.getUriForFile(SubmitActivityAndTopicActivity.this, authority, file);
                                } else {
                                    imageUri = Uri.fromFile(file);
                                }
                                mPhotoUri = imageUri;
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                                startActivityForResult(cameraIntent, REQUESTCODE_TAKEPHOTO);
                            } else {
                                ViewHub.showShortToast(SubmitActivityAndTopicActivity.this, "获取权限失败或拒绝了权限");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            ViewHub.showShortToast(SubmitActivityAndTopicActivity.this, "获取权限失败");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 从相册选择照片
     */
    private void fromAblum() {

        Intent intent = new Intent(vThis, AlbumActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AlbumActivity.EXTRA_SELECTED_PIC_MODEL,
                mAdapter.getItems());
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUESTCODE_FROMALBUM);

    }

    /**
     * 查看大图
     */
    private void lookup() {

        String url = mCurrentClickedViewModel.getUrl();
        File f = new File(url);
        if (f.exists()) {
            url = "file://" + url;
        } else {
            url = ImageUrlExtends.getImageUrl(url);
        }
        // 查看大图
        Intent intent = new Intent(vThis, ItemImageViewActivity.class);
        intent.putExtra(ItemImageViewActivity.IMAGE_URL, url);
        startActivity(intent);

    }

    /**
     * 接收从其他界面返回的数据
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 拍照
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUESTCODE_TAKEPHOTO) {

                if (!SDCardHelper.checkFileExists(mPhotoUri.getPath())) {
                    Toast.makeText(vThis, "未找到图片：" + mPhotoUri.getPath(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                String srcPath = mPhotoUri.getPath();

                // 校正旋转,这种连拍情况下，image会旋转，这里根据调整为正确状态
                ImageTools.checkImgRotaing(srcPath, true, 0, false);
                //
                ImageViewModel photoModel = new ImageViewModel();
                photoModel.setCanRemove(false);
                photoModel.setLoading(false);
                photoModel.setNewAdd(true);
                photoModel.setUploadStatus(ImageViewModel.UploadStatus.NONE);
                photoModel.setUrl(srcPath);
                photoModel.setOriginalUrl(srcPath);
                mAdapter.add(photoModel);
                showImageContainer();

            }
            // 从手机相册选择
            else if (requestCode == REQUESTCODE_FROMALBUM) {
                handleImgsFromAlbumn(data);
            }
        }
    }

    private void handleImgsFromAlbumn(Intent data) {
        @SuppressWarnings("unchecked")
        ArrayList<ImageViewModel> imgs = (ArrayList<ImageViewModel>) data
                .getSerializableExtra(AlbumActivity.EXTRA_SELECTED_PIC_MODEL);
        mAdapter.set(imgs);
        showImageContainer();
    }

    /**
     * 是否显示图片列表
     */
    private void showImageContainer() {
        gvUploadImage.setVisibility(mAdapter.getPicCount() > 0 ? View.VISIBLE
                : View.GONE);
        btnAddImage.setVisibility(mAdapter.getPicCount() == 0 ? View.VISIBLE
                : View.GONE);
    }

    /**
     * 添加商品
     */
    private void addItem() {
        if (!validateInput())
            return;
        uploadImage();
    }

    /**
     * 上传图片到服务器
     */
    private void uploadImage() {
        // 执行异步任务
        SaveItemTask saveItemTask = new SaveItemTask();
        saveItemTask.execute();
    }

    /**
     * 验证用户录入
     */
    private boolean validateInput() {
        if (mAdapter.getPicCount() < 1) {
            Toast.makeText(vThis, "请添加图片", Toast.LENGTH_SHORT).show();
            return false;
        }

        String description = edtContent.getText().toString().trim();
        if (TextUtils.isEmpty(description)) {
            if (activityModel != null) {// 活动
                ViewHub.setEditError(edtContent, "活动内容不能为空");
            } else {// 话题
                ViewHub.setEditError(edtContent, "话题内容不能为空");
            }
            return false;
        } else {
            if (description.length() < 50) {
                ViewHub.setEditError(edtContent, "内容不少于50字");
                return false;
            }
        }
        if (TextUtils.isEmpty(edtTitle.getText().toString())) {
            ViewHub.setEditError(edtTitle, "标题不能为空");
            return false;
        }
        return true;
    }

    /**
     * 添加商品
     */
    private class SaveItemTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start("正在发布，请稍后...");
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String imgPrefix = "";
                if (activityModel != null) {
                    imgPrefix = "activity";
                } else {
                    imgPrefix = "topic";
                }

                int picNum = mAdapter.getPicCount();
                // 图片
                List<String> images = new ArrayList<String>();
                for (int i = 0; i < picNum; i++) {
                    String uploadFilePath = mAdapter.getItem(i).getUrl();
                    if (!(new File(uploadFilePath).exists())) {
                        continue;
                    }
                    // 压缩图片，将压缩后的图片重命名后保存到SDCard
                    String imgUrl = ImageTools.createThumb(uploadFilePath,
                            1000, 1000, 100, true, Const.UPLOAD_ITEM_MAX_SIZE);
                    if (imgUrl.startsWith("file://")) {
                        imgUrl = imgUrl.substring(7);// 去除file://
                    }
                    // 上传图片
                    String shopId = String.valueOf(SpManager.getShopId(vThis));
                    String fileName = "/xiaozu/" + imgPrefix
                            + System.currentTimeMillis() + ".jpg";

                    try {
                        UploadItemAPI.uploadImage(fileName, imgUrl);
                        String saveUrl = ImageUrlExtends.HTTP_NAHUO_IMG_SERVER
                                + "/" + fileName;
                        images.add(saveUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "上传图片失败，请重新保存";
                    }
                }

                String description = edtContent.getText().toString().trim();
                description = description.replace("<", "&lt;");
                description = description.replace(">", "&gt;");
                description = description.replace("\"", "&quot;");
                String title = edtTitle.getText().toString().trim();
                title = title.replace("<", "&lt;");
                title = title.replace(">", "&gt;");
                title = title.replace("\"", "&quot;");
                if (activityModel != null) {// 活动
                    String type = "";
                    if (btnActivityType.getSelectedItem() != null) {
                        type = btnActivityType.getSelectedItem().toString();
                    } else {
                        type = "招募代理";
                    }
                    boolean findedCategoryID = false;
                    for (ActivityCategoryModel model : categorys) {
                        if (model.getName().equals(type)) {
                            type = String.valueOf(model.getCategoryID());
                            findedCategoryID = true;
                            break;
                        }
                    }
                    if (!findedCategoryID) {
                        type = String.valueOf(categorys.get(0).getCategoryID());
                    }
                    String group = btnActivityGroup.getText().toString();
                    String visible = btnActivityVisible.getText().toString();
                    activityModel.setTitle(title);
                    activityModel.setContent(description);
                    activityModel.setType(type);
                    activityModel.setIsGroupType(group);
                    activityModel.setVisible(visible);
                    activityModel.setImages(images);

                    ActivityAPI.add(vThis, String.valueOf(groupID),
                            activityModel);
                } else {// 话题
                    String group = btnTopicGroup.getText().toString();
                    String visible = btnTopicVisible.getText().toString();
                    topicModel.setTitle(title);
                    topicModel.setContent(description);
                    topicModel.setIsGroupType(group);
                    topicModel.setVisible(visible);
                    topicModel.setImages(images);

                    TopicAPI.add(vThis, String.valueOf(groupID), topicModel);
                }

                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "发布话题/活动发生异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingDialog.stop();

            if (result.equals("OK")) {
                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_GROUP_DETAIL));
                ViewHub.showLongToast(vThis, "发布成功，请等待审核");
                finish();
            } else {
                ViewHub.showLongToast(vThis, result);
            }
        }
    }

    public class GetCategoryDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("OK")) {
                ViewHub.showLongToast(vThis, result);
            } else {
                loadCategorySpinner();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            loadCategorySpinner();
        }

        @Override
        protected String doInBackground(Void... params) {
            File cacheFile = CacheDirUtil.getCache(getApplicationContext(),
                    "submitAndTopic_cahce");
            if (cacheFile.exists()) {
                List<ActivityCategoryModel> posts = GsonHelper.jsonToObject(
                        CacheDirUtil.readString(cacheFile),
                        new TypeToken<List<ActivityCategoryModel>>() {
                        });
                if (posts.size() > 0) {
                    categorys = posts;
                    publishProgress();
                }
            }
            try {
                categorys = ActivityAPI.getActivityCategorys(vThis, cacheFile);
                return "OK";
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

    }

    private void loadCategorySpinner() {
        String[] names = new String[categorys.size()];
        for (int i = 0; i < categorys.size(); i++) {
            names[i] = categorys.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(vThis,
                android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(R.layout.drop_down_item);
        btnActivityType.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    /**
     * 获取表情的gridview的子view
     *
     * @return
     */
    private void initGridView() {
        mGvEmotions = (GridView) findViewById(R.id.gridview);
        mGvEmotions.setTag(false);
        mEmotionResList = getExpressionRes(100);
        mEmotionResList.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, mEmotionResList);
        mGvEmotions.setAdapter(expressionAdapter);
        expressionAdapter.setIMojiClickListner(new ExpressionAdapter.IMojiClickListener() {
            @Override
            public void mojiClick(String filename) {
                try {
                    if (filename != "delete_expression") { // 不是删除键，显示表情
                        String ico = SmileUtils.getSmailStr(filename, vThis);
                        edtContent.append(SmileUtils.getSmiledText(vThis, ico));
                    } else { // 删除文字或者表情
                        if (!TextUtils.isEmpty(edtContent.getText())) {
                            int selectionStart = edtContent.getSelectionStart();
                            if (selectionStart > 0) {
                                String body = edtContent.getText().toString();
                                String tempStr = body.substring(0,
                                        selectionStart);
                                int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                if (i != -1) {
                                    CharSequence cs = tempStr.substring(i,
                                            selectionStart);
                                    if (SmileUtils.containsKey(cs.toString()))
                                        edtContent.getEditableText().delete(i,
                                                selectionStart);
                                    else
                                        edtContent.getEditableText().delete(
                                                selectionStart - 1,
                                                selectionStart);
                                } else {
                                    edtContent.getEditableText().delete(
                                            selectionStart - 1, selectionStart);
                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<String>();
        for (int x = 0; x < getSum; x++) {
            String filename = "qq_" + x;
            reslist.add(filename);
        }
        return reslist;
    }
}