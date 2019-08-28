package com.nahuo.quicksale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.internal.LinkedTreeMap;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nahuo.Dialog.PraiseDomDialog;
import com.nahuo.bean.MsgRed;
import com.nahuo.library.controls.BottomMenuList;
import com.nahuo.library.controls.LightPopDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.activity.SettingsActivity;
import com.nahuo.quicksale.api.ApiHelper;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.FileUtils;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.customview.CancelDialog;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.exceptions.CatchedException;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.oldermodel.Address;
import com.nahuo.quicksale.oldermodel.Area;
import com.nahuo.quicksale.oldermodel.ContactModel;
import com.nahuo.quicksale.oldermodel.ContactResultModel;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.json.JAddress;
import com.nahuo.quicksale.provider.ContactInfoProvider;
import com.nahuo.quicksale.provider.UserInfoProvider;
import com.nahuo.quicksale.task.CheckUpdateTask;
import com.nahuo.quicksale.util.GlideUtls;
import com.nahuo.quicksale.util.JsonKit;
import com.nahuo.quicksale.util.RxUtil;
import com.nahuo.quicksale.util.UMengTestUtls;
import com.nahuo.quicksale.yft.YFTActivity;
import com.nahuo.service.autoupdate.AppUpdate;
import com.nahuo.service.autoupdate.AppUpdateService;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * @description 我的账号
 * @created 2014-8-15 上午11:01:44
 */
public class MeSettingActivity extends BaseAppCompatActivity implements OnClickListener {

    private static final int REQUEST_ADDRESS = 1;

    private Context mContext = this;
    private static final String TAG = MeSettingActivity.class.getSimpleName();
    private static final int REQUESTCODE_TAKEPHOTO = 45;                             // 拍照
    private static final int REQUESTCODE_FROMALBUM = 2;                              // 从手机相册选择
    private static final int REQUESTCODE_CUT = 3;                              // 裁剪
    private static final int REQUESTCODE_SIGNATURE = 4;                              // 修改签名
    private static final int REQUEST_7DAYS_CREDIT = 5;                              // 七天无理由退换货
    // private PopupWindowEx
    // pwAddPhoto;
    private MeSettingActivity vThis = this;
    private ArrayList<Address> mAddresses;
    private AppUpdate mAppUpdate;
    private int phoneImg = R.drawable.contact_phone_gray;
    private int qqImg = R.drawable.contact_qq_gray;
    private int wxImg = R.drawable.contact_weixin_gray;
    private int emailImg = R.drawable.contact_email_gray;
    private int addressImg = R.drawable.contact_address_gray;
    private String address;
    private Uri mPhotoUri;                                              // 拍照图片文件Uri
    private String mLogoPath = "";                             // 店标图片文件路径
    private ImageView mIvShopLogo;
    private LoadingDialog loadingDialog;
    private TextView mTvNickName;                              // 用户名

    private TextView mTvAddress;

    private TextView userTv;

    private View userRelative;

    private ClipboardManager clipboard;

    private List<ContactModel> datas = new ArrayList<ContactModel>();

    final Html.ImageGetter imageGetter = new Html.ImageGetter() {

        @Override
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            int rId = Integer.parseInt(source);
            drawable = getResources().getDrawable(rId);
            drawable.setBounds(0, 0, 40, 40);
            return drawable;
        }

    };

    private enum Step {
        LOAD_CONTACT_INFO, LOAD_DEFAULT_ADDRESS, CHANGE_SHOP_NAME
    }

    private TextView tvTitleCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        //setTitle("我的设置");
        tvTitleCenter = (TextView) findViewById(R.id.tvTitleCenter);
        tvTitleCenter.setText("我的设置");
        findViewById(R.id.tvTLeft).setOnClickListener(this);
        mAppUpdate = AppUpdateService.getAppUpdate(this);
        initView();
        initData();


    }

    private List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                if (resultCode == RESULT_OK) {
                    selectList = PictureSelector.obtainMultipleResult(data);
                    mLogoPath = selectList.get(0).getCompressPath();
                    GlideUtls.glidePic(this, mLogoPath, mIvShopLogo);
                    new SaveDataTask(mLogoPath).execute();
                }
                break;
            case REQUEST_7DAYS_CREDIT:// 七天无理由退换货
                //     setItemRightText(R.id.item_7days_commitment, SpManager.get7DaysCreditStatu(mContext));
                break;
            case REQUEST_ADDRESS:// 更新默认地址
                // Address add = SpManager.getLastSelectAddress(getApplicationContext(), SpManager.getUserId(mContext));
                // mTvAddress.setText(add == null ? "未设置" : add.getDetailAddress() );
                mTvAddress.setText(UserInfoProvider.getDefaultAddress(mContext, SpManager.getUserId(mContext)));
                break;
            case ContactActivity.CONTACT_RELOAD:
                //new Task(Step.LOAD_CONTACT_INFO).execute();
                load_contact_and_adress_info(0);
                break;
            case REQUESTCODE_TAKEPHOTO:// 拍照
                if (resultCode == RESULT_OK) {
                    try {
                        if (!SDCardHelper.checkFileExists(mPhotoUri.getPath())) {
                            ViewHub.showShortToast(getApplicationContext(), "未找到图片：" + mPhotoUri.getPath());
                            return;
                        }
//                        int aspectX = 1;// 宽度相对高度的比例
//                        int aspectY = 1;// 高度相对宽度的比例
//                        int size = 500;// 高度、宽度的最大像素值
                        // 进入裁剪界面
//                        gotoCut(mPhotoUri, aspectX, aspectY, size);

                        cropPic(Uri.fromFile(new File(mPhotoUri.getPath())));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case REQUESTCODE_FROMALBUM:// 从手机相册选择
                if (resultCode == Activity.RESULT_OK) {
                    // try {
                    // String srcPath = mPhotoUri.toString();
                    // if (data.getData() != null) {
                    // srcPath = data.getData().toString();
                    // }
                    // if (srcPath.startsWith("file://")) {
                    // if (!SDCardHelper.checkFileExists(srcPath.replace("file://", ""))) {
                    // Toast.makeText(vThis, "未找到图片：" + srcPath, Toast.LENGTH_SHORT).show();
                    // return;
                    // }
                    // displayImage(srcPath);
                    // } else if (srcPath.startsWith("content:")) {
                    // Uri selectedImage = data.getData();
                    // String picturePath = ImageTools.getFilePath4Uri(vThis,selectedImage);
                    // if (picturePath.startsWith("/storage"))
                    // {
                    // picturePath = "file://"+picturePath;
                    // }
                    // displayImage(picturePath);
                    // } else {
                    // if (!SDCardHelper.checkFileExists(srcPath)) {
                    // Toast.makeText(vThis, "未找到图片：" + srcPath, Toast.LENGTH_SHORT).show();
                    // return;
                    // }
                    // displayImage(srcPath);
                    // }
                    // } catch (Exception e) {
                    // e.printStackTrace();
                    // }
                    ArrayList<ImageViewModel> imgs = (ArrayList<ImageViewModel>) data
                            .getSerializableExtra(AlbumActivity.EXTRA_SELECTED_PIC_MODEL);
                    if (imgs.size() > 0) {
                        String picturePath = imgs.get(0).getOriginalUrl();
//                        int aspectX = 1;// 宽度相对高度的比例
//                        int aspectY = 1;// 高度相对宽度的比例
//                        int size = 500;// 高度、宽度的最大像素值

                        try {
//                            String imgDirPath = SDCardHelper.getSDCardRootDirectory() + "/weipu/upload_tmp/logo_temp";
//                            SDCardHelper.createDirectory(imgDirPath);
//                            String filePath = imgDirPath + "/" + System.currentTimeMillis() + ".jpg";
//                            // 拷贝一份选择的文件，进行裁切
//                            SDCardHelper.copyFile(picturePath, filePath);
//                            File file = new File(filePath);
////                            mPhotoUri = Uri.parse("file://" + (file.exists() && file.length() > 0 ? filePath : picturePath));
//                            mPhotoUri = Uri.fromFile(file.exists() && file.length() > 0 ? file : new File(picturePath));
//                            // 进入裁剪界面
//                            gotoCut(mPhotoUri, aspectX, aspectY, size);
                            cropPic(Uri.fromFile(new File(picturePath)));

                        } catch (Exception e) {
//                            Toast.makeText(vThis, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(vThis, "未选择图片", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case REQUESTCODE_CUT:// 裁剪
                if (data != null)//要檢查,因為裁剪時按cancel回來也會來此,但result == null
                {
                    //取得裁剪後的照片
                    Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                    String imgDirPath = SDCardHelper.getSDCardRootDirectory() + "/weipu/upload_tmp/logo_temp";
                    SDCardHelper.createDirectory(imgDirPath);
                    String filePath = imgDirPath + "/" + System.currentTimeMillis() + ".jpg";
                    try {
                        FileUtils.saveBitmap(filePath, cameraBitmap);
                        displayImage(filePath, cameraBitmap);
                    } catch (Exception e) {
                        ViewHub.showLongToast(getApplicationContext(), "保存图片发生异常...");
                        Log.e(getClass().getSimpleName(), "保存图片发生异常 Exception:" + e.toString());
                        CrashReport.postCatchedException(new CatchedException("保存图片发生异常 Exception:" + e.toString()));
                    }
                } else {
                    Log.w(getClass().getSimpleName(), "crop image get data is null");
                    CrashReport.postCatchedException(new CatchedException("裁剪图片 return data is null"));
                }

//                if (resultCode == RESULT_OK) {//
//                try {
//                    if (!SDCardHelper.checkFileExists(mPhotoUri.getPath())) {
//                        ViewHub.showShortToast(getApplicationContext(), "未找到图片：" + mPhotoUri.getPath());
//                        return;
//                    }
//                    // 展示图片
//                    String srcPath = mPhotoUri.toString();
//                    displayImage(srcPath);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                }
                break;
            case REQUESTCODE_SIGNATURE:// 签名
                if (resultCode == RESULT_OK) {
                    //     setItemRightText(R.id.item_signature, SpManager.getSignature(this));
                }
                break;
            /*case 10000:
                if(resultCode== ChatActivity.RESULT_CODE_COPY){
                    if(!TextUtils.isEmpty(userTv.getText().toString())){
                        clipboard.setText(userTv.getText().toString());
                    }

                }
                break;*/
        }

    }

    private void load_contact_and_adress_info(int typeID) {
        Map<String, Object> params = new HashMap<>();
        switch (typeID) {
            case 1:
                params.put("typeCode", "Mobile");
                break;
            case 2:
                params.put("typeCode", "QQ");
                break;
            case 3:
                params.put("typeCode", "WeiXin");
                break;
            case 4:
                params.put("typeCode", "Email");
                break;
            default:
                break;
        }
        params.put("isEnabled", "1");
        params.put("pageIndex", "1");
        params.put("pageSize", "0");//读取全部数据，为0表示所有
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getContactInfoList(params)
                .compose(RxUtil.<PinHuoResponse<ContactResultModel>>rxSchedulerHelper())
                .compose(RxUtil.<ContactResultModel>handleResult()).subscribeWith(new CommonSubscriber<ContactResultModel>(vThis) {
                    @Override
                    public void onNext(ContactResultModel contactResultModel) {
                        super.onNext(contactResultModel);
                        if (contactResultModel != null) {
                            datas = contactResultModel.getDatas();
                            ContactInfoProvider.saveContactInfo(vThis, datas);
                            check_conact();
                        }
                    }
                }));
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getShopInfo2()
                .compose(RxUtil.<PinHuoResponse<Object>>rxSchedulerHelper())
                .compose(RxUtil.<Object>handleResult()).subscribeWith(new CommonSubscriber<Object>(vThis) {
                    @Override
                    public void onNext(Object object) {
                        super.onNext(object);
                        try {
                            LinkedTreeMap map = (LinkedTreeMap) object;
                            String json = JsonKit.mapToJson(map, null).toString();
                            JSONObject jo = new JSONObject(json);
                            address = jo.getString("Address");
                            ContactInfoProvider.saveAddressInfo(vThis, address);
                            check_adress();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    private void initData() {
        String signature = SpManager.getSignature(this);
        //  setItemRightText(R.id.item_signature, TextUtils.isEmpty(signature) ? "点击进行签名" : signature);
        // new Task(Step.LOAD_CONTACT_INFO).execute();
        load_contact_and_adress_info(0);
        // new Task(Step.LOAD_DEFAULT_ADDRESS).execute();
        getAdress();
    }

    private void getAdress() {
        addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(TAG).getAddresses().compose(RxUtil.<PinHuoResponse<List<JAddress>>>rxSchedulerHelper())
                .compose(RxUtil.<List<JAddress>>handleResult())
                .subscribeWith(new CommonSubscriber<List<JAddress>>(vThis) {
                    @Override
                    public void onNext(List<JAddress> jList) {
                        super.onNext(jList);
                        ArrayList<Address> addresses = new ArrayList<Address>();
                        if (!ListUtils.isEmpty(jList)) {
                            for (JAddress ja : jList) {
                                Address add = new Address();
                                add.setId(ja.getID());
                                add.setUserName(ja.getRealName());
                                add.setPostCode(ja.getPostCode());
                                add.setPhone(ja.getMobile());
                                add.setDetailAddress(ja.getAddress());
                                add.setDefault(ja.isIsDefault());
                                if (ja.isIsDefault()) {
                                    UserInfoProvider.setDefaultAddress(vThis, SpManager.getUserId(vThis), ja.getAddress());
                                }
                                String area_name = "", city_name = "", province_name = "";
                                if (!TextUtils.isEmpty(ja.getArea())) {
                                    String[] mRegions = ja.getArea().split("\\s+");
                                    if (mRegions.length >= 3) {
                                        province_name = mRegions[0];
                                        city_name = mRegions[1];
                                        area_name = mRegions[2];
                                    } else if (mRegions.length == 2) {
                                        province_name = mRegions[0];
                                        city_name = mRegions[1];
                                    } else {
                                        province_name = mRegions[0];
                                    }
                                }
                                Area area = new Area();
                                area.setName(area_name);
                                area.setId(ja.getAreaID());
                                area.setParentId(ja.getCityID());
                                Area city = new Area();
                                city.setId(ja.getCityID());
                                city.setParentId(ja.getProvinceID());
                                city.setName(city_name);
                                Area province = new Area();
                                province.setName(province_name);
                                province.setId(ja.getProvinceID());
                                add.setArea(area);
                                add.setCity(city);
                                add.setProvince(province);
                                addresses.add(add);
                            }
                        }
                        mAddresses=addresses;
                        if (mAddresses == null) {
                            mAddresses = new ArrayList<Address>();
                        }
                        if (!ListUtils.isEmpty(mAddresses)) {
                            for (Address add : mAddresses) {
                                if (add.isDefault()) {
                                    mTvAddress.setText(add.getDetailAddress());
                                    return;
                                }
                            }
                        }
                        mTvAddress.setText("未设置");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
    }

    // 初始化数据
    private void initView() {
        loadingDialog = new LoadingDialog(this);
//        initItem(R.id.item_signature, "签名");
//        initItem(R.id.item_edit_shop_name, "修改店名");
//        initItem(R.id.item_edit_shop_banner, "更换店招");
//        initItem(R.id.item_contacts, "联系方式");
        //        initItem(R.id.item_postage, "运费设置");
        initItem(R.id.item_nickname, "用户名");
        initItem(R.id.item_address, "收件地址");
        //  initItem(R.id.item_agent_setting, "代理设置");
        initItem(R.id.item_yft, "在线结算");
        initItem(R.id.item_weixun_setting, "微询设置");

        initItem(R.id.me_about, "关于我们");
        initItem(R.id.me_give_praise, "给我好评");
        initItem(R.id.me_clear_cache, "清除缓存");
        initItem(R.id.me_look_images_casch, "图片保存路径");
        setItemRightText(R.id.me_look_images_casch, Environment.DIRECTORY_DCIM + "/" + Const.IMAGES_CASH_PATH);
        initItem(R.id.me_login_psw, "修改密码");
        initItem(R.id.item_app_update, "版本更新");
        setItemRightText(R.id.item_app_update, "当前版本：" + FunctionHelper.GetAppVersion(vThis));
        initItem(R.id.me_checkout, "退出");
//        initItem(R.id.item_7days_commitment, "七天无理由退换货承诺");
//        setItemRightText(R.id.item_7days_commitment,
//                SpManager.isJoin7DaysDelivery(this) ? "已加入" : SpManager.get7DaysCreditStatu(mContext));
        mTvNickName = (TextView) findViewById(R.id.item_nickname).findViewById(R.id.tv_right_text);
        mTvNickName.setText(SpManager.getUserName(vThis));
        ((ImageView) findViewById(R.id.item_nickname).findViewById(R.id.iv_right_icon)).setVisibility(View.INVISIBLE);
        userTv = ((TextView) findViewById(R.id.item_nickname).findViewById(R.id.tv_right_text));
        userRelative = findViewById(R.id.item_nickname);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        userRelative.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                /*startActivityForResult((new Intent(vThis, MyContextMenu.class)).putExtra("type",
                        EMMessage.Type.TXT.ordinal()).putExtra("VISIBLE","VISIBLE"),10000);*/
                clipboard.setText(mTvNickName.getText().toString());
                CancelDialog dialog = new CancelDialog(mContext, "用户名已复制", R.drawable.ic_right);
                dialog.setShowTime(1000);
                dialog.show();
                return true;
            }
        });

        mTvAddress = (TextView) findViewById(R.id.item_address).findViewById(R.id.tv_right_text);
        // setItemRightText(R.id.item_edit_shop_name, SpManager.getShopName(this));
        setItemRightText(R.id.item_yft, UserInfoProvider.hasOpenedYFT(this, SpManager.getUserId(mContext)) ? "已开通"
                : "未开通");
        mTvAddress.setText("加载中");

        final String sText = "<img src=\"" + phoneImg + "\" />&nbsp;&nbsp;<img src=\"" + qqImg
                + "\" />&nbsp;&nbsp;<img src=\"" + wxImg + "\" />&nbsp;&nbsp;<img src=\"" + emailImg
                + "\" />&nbsp;&nbsp;<img src=\"" + addressImg + "\" />";
        //   setItemRightText(R.id.item_contacts, Html.fromHtml(sText, imageGetter, null));

        // Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        // TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        // tvTitle.setText(R.string.title_activity_meAccount);
        //
        // btnLeft.setText(R.string.titlebar_btnBack);
        // btnLeft.setVisibility(View.VISIBLE);
        // btnLeft.setOnClickListener(this);

        mIvShopLogo = (ImageView) findViewById(R.id.me_imgShopLogo);
        String logo = SpManager.getShopLogo(vThis);
        // 店标
        if (!TextUtils.isEmpty(logo)) {
            String url = ImageUrlExtends.getImageUrl(logo, Const.LIST_COVER_SIZE);
            Picasso.with(vThis).load(url).skipMemoryCache().placeholder(R.drawable.empty_photo).into(mIvShopLogo);
        }
        // View addPhotoPwView = LayoutInflater.from(vThis).inflate(R.layout.layout_pw_addphoto, null);
        // pwAddPhoto = new PopupWindowEx(addPhotoPwView, R.id.uploaditem_pw_addphoto, LayoutParams.MATCH_PARENT,
        // LayoutParams.WRAP_CONTENT, true);
        // pwAddPhoto.setAnimationStyle(R.style.PopupBottomAnimation);
        // View btnTakePhoto = addPhotoPwView.findViewById(R.id.uploaditem_pw_addphoto_btnTakePhoto);
        // View btnFromAlbum = addPhotoPwView.findViewById(R.id.uploaditem_pw_addphoto_btnFromAlbum);
        // View btnCancel = addPhotoPwView.findViewById(R.id.uploaditem_pw_addphoto_btnCancel);
        // btnTakePhoto.setOnClickListener(this);
        // btnFromAlbum.setOnClickListener(this);
        // btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTLeft:
                finish();
                break;
            case R.id.me_give_praise:
                // 提示好评
                PraiseDomDialog dialog = new PraiseDomDialog(this);
                dialog.setTitle("评价").setMessage("您对我们APP的使用还满意吗？").setPositive("给好评", new PraiseDomDialog.PopDialogListener() {
                    @Override
                    public void onPopDialogButtonClick(int which) {
                        openApplicationMarket(getPackageName());
                    }
                }).show();

                break;
//            case R.id.item_7days_commitment:// 7天无理由退换货
//                Intent intent = new Intent(this, SetPayPswFragment.class);
//                startActivityForResult(intent, REQUEST_7DAYS_CREDIT);
//                break;
//            case R.id.btn_shop_preview:// 预览店铺
//                Intent previewIntent = new Intent(vThis, ItemPreviewActivity.class);
//                previewIntent.putExtra("url", "http://" + SpManager.getShopId(vThis) + ".weipushop.com");
//                previewIntent.putExtra("name", SpManager.getShopName(vThis));
//                startActivity(previewIntent);
//                break;
            case R.id.me_look_images_casch:
                File parentFlie = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + Const.IMAGES_CASH_PATH);
                //获取父目录
                //   File parentFlie = new File(file.getParent());
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri imageUri;
                String authority = getPackageName() + ".provider";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //通过FileProvider创建一个content类型的Uri
                    imageUri = FileProvider.getUriForFile(mContext, authority, parentFlie);
                } else {
                    imageUri = Uri.fromFile(parentFlie);
                }
                intent.setDataAndType(imageUri, "*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivity(intent);
                break;
            case R.id.item_yft:// 在线结算
                Intent yftIntent = new Intent(mContext, YFTActivity.class);
                startActivity(yftIntent);
                break;
//            case R.id.item_postage:// 运费设置
//                Intent postIntent = new Intent(mContext, SetExpressFeeActivity.class);
//                startActivity(postIntent);
//                break;
//            case R.id.item_edit_shop_name:// 修改店铺名
//                changeShopName(v);
//                break;
//            case R.id.item_signature:// 签名
//                Intent signIntent = new Intent(this, ChangeSignatureActivity.class);
//                startActivityForResult(signIntent, REQUESTCODE_SIGNATURE);
//                break;
            case R.id.item_shop_icon:// 修改店标
                PictureSelector.create(vThis)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(4)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(
                                PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(false)// 是否可预览视频
                        .enablePreviewAudio(false) // 是否可播放音频
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(true)// 是否裁剪
                        .compress(true)// 是否压缩
                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .compressGrade(Luban.THIRD_GEAR)
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        // .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        // .withAspectRatio(4, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(true)// 是否开启点击声音
                        //.selectionMedia(null)// 是否传入已选图片
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .compressMaxKB(200)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                        // .compressWH(1100, 1100) // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        .rotateEnabled(true) // 裁剪是否可旋转图片
                        .scaleEnabled(true)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                //  togglePopupWindow();
                break;
//            case R.id.item_edit_shop_banner:// 修改店招
//                Intent bannerIntent = new Intent(mContext, ChangeShopBannerActivity.class);
//                startActivity(bannerIntent);
//                break;

            case R.id.titlebar_btnLeft:// 返回
                finish();
                break;
//            case R.id.item_agent_setting:// 微铺设置
//                toOtherActivity(BaseSetActivity.class);
//                break;
//            case R.id.item_contacts: // 联系方式
//                ContactActivity.backToMain = false;
//                Intent contactIntent = new Intent(vThis, ContactActivity.class);
//                startActivityForResult(contactIntent, ContactActivity.CONTACT_RELOAD);
//                break;
            case R.id.item_address: // 收件地址
                Intent addIntent = new Intent(mContext, AddressActivity2.class);
                addIntent.putExtra(AddressActivity2.INTENT_IS_SELECT_DEFAULT, true);
                startActivityForResult(addIntent, REQUEST_ADDRESS);
                break;
            case R.id.item_weixun_setting:// 微询设置
                toOtherActivity(SettingsActivity.class);
                break;
//            case R.id.wsi_qrcode:
//                toOtherActivity(QRCodeActivity.class);
//                break;
            case R.id.me_about:
                //关于我们
                Intent knowIntent = new Intent(mContext, PostDetailActivity.class);
                knowIntent.putExtra(PostDetailActivity.EXTRA_TID, 102139);
                knowIntent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                startActivity(knowIntent);
                break;
            case R.id.me_clear_cache: // 清除缓存
                new ClearTask().execute();
                break;
            case R.id.me_login_psw: // 修改密码
                Intent pswIntent = new Intent(getApplicationContext(), ChangePswActivity.class);
                pswIntent.putExtra(Const.PasswordExtra.EXTRA_PSW_TYPE, Const.PasswordType.LOGIN);
                startActivity(pswIntent);
                break;
            case R.id.me_checkout:// 退出登录
                ViewHub.showLightPopDialog(this, getString(R.string.dialog_title),
                        getString(R.string.shopset_exit_confirm), "取消", "退出登录", new LightPopDialog.PopDialogListener() {
                            @Override
                            public void onPopDialogButtonClick(int which) {
                                //友盟统计账号
                                UMengTestUtls.onProfileSignOff();
                                UserInfoProvider.exitApp(mContext);
                                MsgRed msgRed = new MsgRed();
                                msgRed.setCount(0);
                                msgRed.setIs_Show(false);
                                EventBus.getDefault().post(BusEvent.getEvent(EventBusId.PINHUO_ME_RED_IS_SHOW, msgRed));

                                finish();
                            }
                        });
                break;
            case R.id.item_app_update:// 版本更新
                new CheckUpdateTask(this, mAppUpdate, true, true).execute();
                break;
            default:
                break;
        }
    }

    /**
     * 通过包名 在应用商店打开应用
     *
     * @param packageName 包名
     */
    private void openApplicationMarket(String packageName) {
        try {
            String str = "market://details?id=" + packageName;
            Intent localIntent = new Intent(Intent.ACTION_VIEW);
            localIntent.setData(Uri.parse(str));
            startActivity(localIntent);
        } catch (Exception e) {
            // 打开应用商店失败 可能是没有手机没有安装应用市场
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "打开应用商店失败", Toast.LENGTH_SHORT).show();
            // 调用系统浏览器进入商城
            String url = "http://app.mi.com/detail/163525?ref=search";
            openLinkBySystem(url);
        }
    }

    /**
     * 调用系统浏览器打开网页
     *
     * @param url 地址
     */
    private void openLinkBySystem(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        super.finish();
    }

    private void toOtherActivity(Class<?> cls) {
        Intent intent = new Intent(vThis, cls);
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
        mAppUpdate.callOnPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
        mAppUpdate.callOnResume();
    }

    /**
     * 获取联系方式数据
     */
    private class Task extends AsyncTask<Object, Void, String> {
        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mStep) {
                case CHANGE_SHOP_NAME:
                    loadingDialog.start("修改店铺名中...");
                    break;
                default:
                    datas = ContactInfoProvider.getContactInfo(vThis);
                    address = ContactInfoProvider.getContactAddressInfo(vThis);
                    checkContactStatus();
                    initContactStatus();
                    break;
            }

        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                switch (mStep) {
                    case LOAD_CONTACT_INFO:
                        datas = ShopSetAPI.getInstance().GetContactInfoList(0, PublicData.getCookie(vThis));

                        // 获取地址
                        String json = ShopSetAPI.getInstance().GetAgentShopInfo(PublicData.getCookie(vThis));
                        JSONObject jo = new JSONObject(json);
                        address = jo.getString("Address");

                        // 本地缓存起来
                        ContactInfoProvider.saveContactInfo(vThis, datas, address);
                        // 获取页面联系方式状态
                        checkContactStatus();
                        break;
                    case LOAD_DEFAULT_ADDRESS:
                        mAddresses = ShopSetAPI.getAddresses(mContext);
                        break;
                    case CHANGE_SHOP_NAME:
                        String shopName = params[0].toString();
                        ShopSetAPI.updateShopName(vThis, shopName);
                        return shopName;
                }

                return null;
            } catch (Exception ex) {
                Log.e(TAG, "无法获取数据");
                ex.printStackTrace();
                return ex.getMessage() == null ? "error:未知异常" : "error:" + ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case CHANGE_SHOP_NAME:
                        //  SpManager.setShopName(mContext, result);
                        //    setItemRightText(R.id.item_edit_shop_name, result);
                        break;
                    case LOAD_CONTACT_INFO:
                        initContactStatus();
                        break;
                    case LOAD_DEFAULT_ADDRESS:
                        if (mAddresses == null) {
                            mAddresses = new ArrayList<Address>();
                        }
                        for (Address add : mAddresses) {
                            if (add.isDefault()) {
                                mTvAddress.setText(add.getDetailAddress());
                                return;
                            }
                        }
                        mTvAddress.setText("未设置");
                        break;
                }
            }

        }

        private void checkContactStatus() {
            check_conact();
            check_adress();
        }

        private void initContactStatus() {
            String sText = "<img src=\"" + phoneImg + "\" />&nbsp;&nbsp;<img src=\"" + qqImg
                    + "\" />&nbsp;&nbsp;<img src=\"" + wxImg + "\" />&nbsp;&nbsp;<img src=\"" + emailImg
                    + "\" />&nbsp;&nbsp;<img src=\"" + addressImg + "\" />";

            //  setItemRightText(R.id.item_contacts, Html.fromHtml(sText, imageGetter, null));
        }
    }

    private void check_adress() {
        if (address.length() > 0) {
            addressImg = R.drawable.contact_address;
        } else {
            addressImg = R.drawable.contact_address_gray;
        }
    }

    private void check_conact() {
        if (datas == null) {
            datas = new ArrayList<ContactModel>();
        }
        phoneImg = R.drawable.contact_phone_gray;
        qqImg = R.drawable.contact_qq_gray;
        wxImg = R.drawable.contact_weixin_gray;
        phoneImg = R.drawable.contact_email_gray;
        if (!ListUtils.isEmpty(datas)) {
            for (ContactModel contactModel : datas) {
                switch (contactModel.getTypeID()) {
                    case 1:// Mobile
                        phoneImg = R.drawable.contact_phone;
                        break;
                    case 2:// QQ
                        qqImg = R.drawable.contact_qq;
                        break;
                    case 3:// WeiXin
                        wxImg = R.drawable.contact_weixin;
                        break;
                    case 4:// Email
                        emailImg = R.drawable.contact_email;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void togglePopupWindow() {
        BottomMenuList menu = new BottomMenuList(this);
        menu.setItems(getResources().getStringArray(R.array.menu_upload_image_texts))
                .setOnMenuItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                fromAblum();
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
        // if (pwAddPhoto.isShowing()) {
        // pwAddPhoto.dismiss();
        // } else {
        // pwAddPhoto.showAtLocation(findViewById(R.id.me_imgShopLogo), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
        // 0);
        // }
    }

    /**
     * 调用系统照相机拍照
     */
    private void takePhoto() {
        // 文件名
        String fileName = "logo" + System.currentTimeMillis() + ".jpg";

        // 封装Uri
        String imgDirPath = SDCardHelper.getDCIMDirectory() + File.separator + "weipu";
        SDCardHelper.createDirectory(imgDirPath);
        File file = new File(imgDirPath, fileName);
        Uri imageUri = Uri.fromFile(file);
        mPhotoUri = imageUri;
        // 调用系统照相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(intent, REQUESTCODE_TAKEPHOTO);
    }

    /**
     * 从相册选择照片并裁剪
     */
    private void fromAblum() {
        // // 文件名
        // String fileName = "";
        // // 宽度相对高度的比例
        // int aspectX = 1;
        // // 高度相对宽度的比例
        // int aspectY = 1;
        // // 高度、宽度的最大像素值
        // int size = 120;
        //
        // fileName = "logo" + System.currentTimeMillis() + ".jpg";
        // aspectX = 1;
        // aspectY = 1;
        // size = 640;
        //
        // // 封装Uri
        // String imgDirPath = SDCardHelper.getDCIMDirectory() + File.separator + "weipu";
        // SDCardHelper.createDirectory(imgDirPath);
        // File file = new File(imgDirPath, fileName);
        // Uri imageUri = Uri.fromFile(file);
        // mPhotoUri = imageUri;
        //
        // // 调用系统相册的相关设置
        // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.setType("image/*");
        // // 输出Uri
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        // intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        // intent.putExtra("crop", "true");
        // // 输出高宽比例
        // intent.putExtra("aspectX", aspectX);
        // intent.putExtra("aspectY", aspectY);
        // // 根据高宽比列计算高、宽
        // int width = aspectX > aspectY ? size : (size / aspectY) * aspectX;
        // int height = aspectX < aspectY ? size : (size / aspectX) * aspectY;
        // intent.putExtra("outputX", width);
        // intent.putExtra("outputY", height);
        // intent.putExtra("return-data", false);
        // startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUESTCODE_FROMALBUM);

        Intent intent = new Intent(vThis, AlbumActivity.class);
        intent.putExtra(AlbumActivity.EXTRA_MAX_PIC_COUNT, 1);
        startActivityForResult(intent, REQUESTCODE_FROMALBUM);
    }

//    /**
//     * 裁剪图片方法实现
//     *
//     * @param uri     照片的uri
//     * @param aspectX 宽度相对高度的比例
//     * @param aspectY 高度相对宽度的比例
//     * @param size    最大的高度、宽度
//     */
//    private void gotoCut(Uri uri, int aspectX, int aspectY, int size) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
////        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
//        intent.putExtra("scale", true);// 黑边
//        intent.putExtra("scaleUpIfNeeded", true);// 黑边
//        intent.putExtra("return-data", true);// 若为false则表示不返回数据
//        startActivityForResult(intent, REQUESTCODE_CUT);
//    }


    private void cropPic(Uri uri) {
        final Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//裁剪功能
        //裁剪後輸出圖片的尺寸大小
        intent.putExtra("outputX", 300);//這會限定圖片為
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("circleCrop", true);
        //切大照片,有可能因為超過傳回內存的容量16MB,會有問題,(Bitmap預設是ARGB_8888，1920x1080x4=8294400=8MB)
        //原因是因為Android允許你使用return-data拿資料回來,再用(Bitmap)extras.getParcelable("data")拿到圖片
        //檔案太大的解決辦法:不要讓Intent帶檔案回來,自創建檔案,使用uri方法去連結它
        intent.putExtra("return-data", true);//要帶檔案回來
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        this.startActivityForResult(intent, REQUESTCODE_CUT);
    }

    /**
     * 展示图片
     */
    private void displayImage(String srcPath, Bitmap bitmap) {
        // 展示图片

        mLogoPath = srcPath;
//        Picasso.with(vThis).load(srcPath).placeholder(R.drawable.empty_photo).into(mIvShopLogo);
        mIvShopLogo.setImageBitmap(bitmap);
        new SaveDataTask(mLogoPath).execute();
    }

    /**
     * 保存个性设置
     */
    private class SaveDataTask extends AsyncTask<Void, Void, String> {

        private String mLogo = "";

        public SaveDataTask(String logo) {
            mLogo = logo;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // 店铺ID
                String shopId = String.valueOf(SpManager.getShopId(vThis));
                // 保存店标设置
                if (!TextUtils.isEmpty(mLogo)) {
                    // 本地文件名
                    if (mLogo.startsWith("file://")) {
                        mLogo = mLogo.substring(7);// 去除file://
                    }
                    // 服务器文件名
                    String fileName = "shoplogo" + System.currentTimeMillis() + ".jpg";
                    // 上传店标
                    String serverPath = ShopSetAPI.getInstance().uploadImage(shopId, fileName, mLogo);
                    if (TextUtils.isEmpty(serverPath))
                        throw new Exception("更新店标失败，操作无法完成");

                    // 保存店铺资料
                    String serverPath_logo = serverPath;//"upyun:" + PublicData.UPYUN_BUCKET + ":" + serverPath;
                    String cookie = PublicData.getCookie(vThis);
                    boolean success = ShopSetAPI.getInstance().updateLogo(serverPath_logo, cookie);

                    if (success) {
                        // 店标更新成功时，更新logo路径
                        SpManager.setShopLogo(vThis, serverPath_logo);
                        return "OK";
                    } else {
                        throw new Exception("店标更新失败，操作无法完成");
                    }
                }
                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "无法更新店标");
                ex.printStackTrace();
                return ex.getMessage() == null ? "未知异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start(getString(R.string.me_loading));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingDialog.stop();

            if (result.equals("OK")) {
                ViewHub.showShortToast(getApplicationContext(), "店标保存成功");

                // 通知myitem更新店标
                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.SHOP_LOGO_UPDATED));
                // Intent changeLogoIntent = new Intent();
                // changeLogoIntent.setAction(MyItemsActivity.MyItemsActivityChangeLogoBroadcaseName);
                // sendBroadcast(changeLogoIntent);
            } else {
                // 验证result
                if (result.toString().startsWith("401") || result.toString().startsWith("not_registered")) {
                    ViewHub.showShortToast(getApplicationContext(), result.toString());
                    ApiHelper.checkResult(result, vThis);
                } else {
                    ViewHub.showShortToast(getApplicationContext(), result);
                }
            }
        }
    }

    private void initItem(int viewId, String text) {
        View v = findViewById(viewId);
        v.setOnClickListener(this);
        TextView tv = (TextView) v.findViewById(R.id.tv_left_text);
        ImageView ivLeftIcon = (ImageView) v.findViewById(R.id.iv_left_icon);

        tv.setText(text);
        ivLeftIcon.setVisibility(View.GONE);

    }

    private void setItemRightText(int viewId, String text) {
        View v = findViewById(viewId);
        TextView tv = (TextView) v.findViewById(R.id.tv_right_text);
        tv.setText(text);
    }

    private void setItemRightText(int viewId, Spanned spanned) {
        View v = findViewById(viewId);
        TextView tv = (TextView) v.findViewById(R.id.tv_right_text);
        tv.setText(spanned);
    }

    /**
     * @description 修改店铺名
     * @created 2014-11-25 下午5:29:30
     * @author ZZB
     */
    private void changeShopName(final View v) {
//        Builder builder = LightAlertDialog.Builder.create(vThis);
//        final EditText et = new EditText(vThis);
//        et.setSingleLine(true);
//        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
//        et.setBackgroundResource(R.drawable.single_bg);
//        String shopName = ((TextView) v.findViewById(R.id.tv_right_text)).getText().toString();
//        et.setText(shopName);
//        ViewHub.editTextRequestKeyboard(vThis, et);
//        builder.setTitle("修改店铺名").setView(et).setNegativeButton("取消", null)
//                .setPositiveButton("修改", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // 后面换成通知更新的方式
//                        String newShopName = et.getText().toString();
//                        newShopName = newShopName.replaceAll("\n", "").replaceAll("\r", "");
//                        if (TextUtils.isEmpty(newShopName)) {
//                            ViewHub.showShortToast(vThis, "店铺名不能为空");
//                        } else if (newShopName.length() > 30) {
//                            ViewHub.showShortToast(vThis, "店铺名不能超过30个字符");
//                        } else {
//                            new Task(Step.CHANGE_SHOP_NAME).execute(newShopName);
//                        }
//
//                    }
//                });
//        builder.show();
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
            File cacheFile1 = new File(SDCardHelper.getSDCardRootDirectory() + "/weipu/qrcode_cache");
            File cacheFile2 = new File(SDCardHelper.getSDCardRootDirectory() + "/weipu/share");
            File cacheFile3 = new File(SDCardHelper.getSDCardRootDirectory() + "/weipu/share_download");
            File cacheFile4 = new File(SDCardHelper.getSDCardRootDirectory() + "/weipu/tackPhoto_tmp");
            File cacheFile5 = new File(SDCardHelper.getSDCardRootDirectory() + "/weipu/upload_tmp");
            File cacheFile6 = new File(SDCardHelper.getSDCardRootDirectory() + "/pinhuo/pihuo_save");
            File cacheFile7 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/pinhuo/pinhuo_video_save");
            gcCountBefore += getFileSize(cacheFile1, 0);
            gcCountBefore += getFileSize(cacheFile2, 0);
            gcCountBefore += getFileSize(cacheFile3, 0);
            gcCountBefore += getFileSize(cacheFile4, 0);
            gcCountBefore += getFileSize(cacheFile5, 0);
            gcCountBefore += getFileSize(cacheFile6, 0);
            gcCountBefore += getFileSize(cacheFile7, 0);
            delFileSize(cacheFile1);
            delImg2Media(cacheFile1);
            delFileSize(cacheFile2);
            delImg2Media(cacheFile2);
            delFileSize(cacheFile3);
            delImg2Media(cacheFile3);
            delFileSize(cacheFile4);
            delImg2Media(cacheFile4);
            delFileSize(cacheFile5);
            delImg2Media(cacheFile5);
            delFileSize(cacheFile6);
            delImg2Media(cacheFile6);
            delFileSize(cacheFile7);
            delImg2Media(cacheFile7);
            gcCountEnd += getFileSize(cacheFile1, 0);
            gcCountEnd += getFileSize(cacheFile2, 0);
            gcCountEnd += getFileSize(cacheFile3, 0);
            gcCountEnd += getFileSize(cacheFile4, 0);
            gcCountEnd += getFileSize(cacheFile5, 0);
            gcCountEnd += getFileSize(cacheFile6, 0);
            gcCountEnd += getFileSize(cacheFile7, 0);
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

    private void delImg2Media(File f) {
        mContext.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_data like '%" + f.getAbsolutePath() + "%'", null);
    }
}
