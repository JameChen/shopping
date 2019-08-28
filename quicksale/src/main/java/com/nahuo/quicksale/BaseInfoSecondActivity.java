package com.nahuo.quicksale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nahuo.library.controls.BottomMenuList;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.ImageTools;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.oldermodel.AuthInfoModel;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class BaseInfoSecondActivity extends BaseActivity implements OnClickListener {

    private Context mContext = this;
    public static String EXTRA_DATA = "EXTRA_DATA";
    private ImageView ivStore, ivLicense;
    private TextView tvRight;
    private static final int REQUESTCODE_TAKEPHOTO = 0;// 拍照
    private static final int REQUESTCODE_FROMALBUM = 1; // 从手机相册选择
    private AuthInfoModel data;// 第一页传过来的值
    private int step;//0商铺
    private Uri mPhotoUri;
    private String storeUrl = "";// 返回的商铺照片路径
    private String licenseUrl = "";// 返回的执照照片路径
    private String storeFile = "";// 准备上传的商铺图片路径
    private String licenseFile = "";// 准备上传的执照图片路径
    //    private EventBus mEventBus = EventBus.getDefault();
    private RelativeLayout rlTip;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base_info);
        if (loadingDialog == null)
            loadingDialog = new LoadingDialog(this);
        initViews();
        initDatas();
    }

    private void initDatas() {
        loadingDialog.start();
        if (loadingDialog.isShowing()) {
            loadingDialog.stop();
        }
        data = (AuthInfoModel) getIntent().getSerializableExtra(BaseInfoSecondActivity.EXTRA_DATA);
    }

    private void initViews() {
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("2/2上传资料");
        tvRight = (TextView) findViewById(R.id.tv_right);
        this.findViewById(R.id.llPanel1).setVisibility(View.GONE);
        this.findViewById(R.id.llPanel2).setVisibility(View.VISIBLE);
        ivLicense = (ImageView) this.findViewById(R.id.ivLicense);
        ivStore = (ImageView) this.findViewById(R.id.ivStore);
        rlTip = (RelativeLayout) this.findViewById(R.id.rlTip);
        rlTip.setVisibility(View.GONE);
        tvRight.setOnClickListener(this);
        ivLicense.setOnClickListener(this);
        ivStore.setOnClickListener(this);
        findViewById(R.id.iv_left).setOnClickListener(this);
        rlTip.findViewById(R.id.ic_close).setOnClickListener(this);
        tvRight.setText("提交");
        tvRight.setEnabled(true);
        tvRight.setClickable(true);
    }

    private void togglePopupWindow() {
        BottomMenuList menu = new BottomMenuList(this);
        menu.setItems(getResources().getStringArray(R.array.menu_upload_image_texts))
                .setOnMenuItemClickListener(new AdapterView.OnItemClickListener() {

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
    }

    private void takePhoto() {
        // 文件名
        String fileName = "renzheng" + System.currentTimeMillis() + ".jpg";
        // 封装Uri
        String imgDirPath = SDCardHelper.getDCIMDirectory() + File.separator + "take_photo";
        SDCardHelper.createDirectory(imgDirPath);
        File file = new File(imgDirPath, fileName);
        Uri imageUri;
        String authority = getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            imageUri = FileProvider.getUriForFile(this, authority, file);
        } else {
            imageUri = Uri.fromFile(file);

        }
        mPhotoUri = imageUri;
        // 调用系统照相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(intent, REQUESTCODE_TAKEPHOTO);
    }

    /**
     * 从相册选择照片
     */
    private void fromAblum() {
        Intent intent = new Intent(this, AlbumActivity.class);
        intent.putExtra(AlbumActivity.EXTRA_MAX_PIC_COUNT, 1);
        startActivityForResult(intent, REQUESTCODE_FROMALBUM);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent dataIntent) {
        switch (requestCode) {
            case REQUESTCODE_TAKEPHOTO:// 拍照
                try {
                    if (resultCode == RESULT_OK) {
                        if (!SDCardHelper.checkFileExists(mPhotoUri.getPath())) {
                            ViewHub.showShortToast(getApplicationContext(), "未找到图片：" + mPhotoUri.getPath());
                            return;
                        }
                        String url = mPhotoUri.getPath();

                        url = ImageTools.createThumb(url, 2000, 2000, 100, true, 500);//最大500K
                        ImageTools.checkImgRotaing(url, true, 0, false);
                        if (url.startsWith("file://")) {
                            url = url.replace("file://", "");
                        }
                        showImg(url);
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "保存图片发生异常 Exception:" + e.toString());
                }
                break;
            case REQUESTCODE_FROMALBUM:// 从手机相册选择
                try {
                    if (resultCode == Activity.RESULT_OK) {
                        ArrayList<ImageViewModel> list = (ArrayList<ImageViewModel>) dataIntent.getSerializableExtra(AlbumActivity.EXTRA_SELECTED_PIC_MODEL);
                        if (list != null && list.size() > 0) {
                            ImageViewModel item = list.get(0);
                            String url = item.getUrl();
                            url = ImageTools.createThumb(url, 2000, 2000, 100, true, 500);//最大500K
                            ImageTools.checkImgRotaing(url, true, 0, false);
                            if (url.startsWith("file://")) {
                                url = url.replace("file://", "");
                            }
                            showImg(url);
                        }
                    }
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "保存图片发生异常 Exception:" + e.toString());
                }
                break;
        }
    }

    private void showImg(String url) {
        switch (step) {
            case 1: {
                licenseFile = url;
                if (url.startsWith("http:")) {
                    Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivLicense);
                } else {
                    Picasso.with(mContext).load(new File(url)).placeholder(R.drawable.empty_photo).into(ivLicense);
                }
                break;
            }
            case 0: {
                storeFile = url;
                if (url.startsWith("http:")) {
                    Picasso.with(mContext).load(url).placeholder(R.drawable.empty_photo).into(ivStore);
                } else {
                    Picasso.with(mContext).load(new File(url)).placeholder(R.drawable.empty_photo).into(ivStore);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
            case R.id.tv_right: {
                if (TextUtils.isEmpty(licenseFile) || TextUtils.isEmpty(storeFile)) {
                    Toast.makeText(this, "请选择商铺/执照图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                new UploadImgTask().execute();
                break;
            }
            case R.id.ivStore: {
                step = 0;
                togglePopupWindow();
                break;
            }
            case R.id.ivLicense: {
                step = 1;
                togglePopupWindow();
                break;
            }
            case R.id.ic_close: {
                finish();
                break;
            }
        }

    }

    private void commitData() {
        if (data.getAuthInfo().getImages() == null) {
            data.getAuthInfo().setImages(new ArrayList<AuthInfoModel.Images>());
        }
        data.getAuthInfo().getImages().clear();

        AuthInfoModel.Images img1 = data.new Images();
        img1.setTypeID(1);
        img1.setUrl(storeUrl);
        data.getAuthInfo().getImages().add(img1);

        AuthInfoModel.Images img2 = data.new Images();
        img2.setTypeID(2);
        img2.setUrl(licenseUrl);
        data.getAuthInfo().getImages().add(img2);

        new Task().execute((Void) null);
    }

    public class Task extends AsyncTask<Void, Void, String> {
        public Task() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start("保存数据中,请稍后...");
        }

        @Override
        protected void onPostExecute(String result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result.equals("ok")) {
                EventBus.getDefault().postSticky(BusEvent.getEvent(EventBusId.RENZHENG_SELECT_RB));
                rlTip.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseInfoSecondActivity.this.finish();
                    }
                }, 3000);
            } else {
                ViewHub.showLongToast(mContext, result);
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                ShopSetAPI.getInstance().SaveAuthInfo(data, PublicData.getCookie(mContext));
                return "ok";
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex.getMessage();
            }
        }

    }


    public class UploadImgTask extends AsyncTask<Void, Void, String> {
        public UploadImgTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog.start("准备图片中...");
        }

        @Override
        protected void onPostExecute(String result) {
            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result.equals("ok")) {
                if (storeUrl.length() > 0 && storeUrl.length() > 0) {
                    commitData();
                }
            } else {
                ViewHub.showLongToast(mContext, result);
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                if (storeUrl.isEmpty()) {
                    loadingDialog.start("上传店铺图片...");
                    String result = ShopSetAPI.getInstance().PHShopAuthImage(new File(storeFile),
                            SpManager.getUserId(mContext),
                            SpManager.getCookie(mContext));
                    if (result.startsWith("error:")) {
                        throw new Exception(result);
                    }
                    JSONObject jo = new JSONObject(result);
                    String url = jo.getString("FileUrl");
                    storeUrl = url;
                    if (!storeUrl.startsWith("http://")) {
                        storeUrl = "";
                        throw new Exception("上传失败");
                    }
                }

                if (licenseUrl.isEmpty()) {
                    loadingDialog.start("上传营业执照...");
                    String result1 = ShopSetAPI.getInstance().PHShopAuthImage(new File(licenseFile),
                            SpManager.getUserId(mContext),
                            SpManager.getCookie(mContext));
                    if (result1.startsWith("error:")) {
                        throw new Exception(result1);
                    }
                    JSONObject jo = new JSONObject(result1);
                    String url = jo.getString("FileUrl");
                    licenseUrl = url;
                    if (!licenseUrl.startsWith("http://")) {
                        licenseUrl = "";
                        throw new Exception("上传失败");
                    }
                }

                loadingDialog.start("图片准备完毕...");
                return "ok";
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex.getMessage();
            }
        }

    }

}
