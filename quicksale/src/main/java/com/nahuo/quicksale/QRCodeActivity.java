package com.nahuo.quicksale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.zxing.BarcodeFormat;
import com.nahuo.library.controls.BottomMenuList;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageTools;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.FileUtils;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.qrcode.Contents;
import com.nahuo.quicksale.qrcode.QRCodeEncoder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Description:二维码、微铺号页面 2014-7-9上午11:18:44
 */
public class QRCodeActivity extends BaseSlideBackActivity implements View.OnClickListener {

    private Context             mContext      = this;
    private Bitmap              mQrBitmap;
    private static final String QR_CACHED_DIR = Environment.getExternalStorageDirectory().getPath()
                                                      + "/weipu/qrcode_cache";
    private String              mLogoUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_qr_code);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);

        initView();
    }

    private void initView() {
        initTitleBar();
        TextView wpName = (TextView)findViewById(R.id.user_name);
        wpName.setText("用户名:" + SpManager.getUserName(this));
        TextView shopName = (TextView)findViewById(R.id.shop_name);
        shopName.setText(SpManager.getShopName(this));
        ImageView logoImg = (ImageView)findViewById(R.id.icon);
        String logo = SpManager.getShopLogo(this);
        logo = TextUtils.isEmpty(logo) ? SpManager.getShopLogo(this) : logo;
        // 店标
        if (!TextUtils.isEmpty(logo)) {
            mLogoUrl = ImageUrlExtends.getImageUrl(logo, Const.LIST_COVER_SIZE);
            Picasso.with(this).load(mLogoUrl).skipMemoryCache().placeholder(R.drawable.shop_logo_normal).into(logoImg);
        }
        initQRCode();

    }

    private void initQRCode() {
        final ImageView mIvQrCode = (ImageView)findViewById(R.id.qr_code);
        final String qrcodeCachePath = genFilePath(mLogoUrl);
        File f = new File(qrcodeCachePath);
//        if (f.exists()) {
//            mQrBitmap = BitmapFactory.decodeFile(f.getPath());
//            mIvQrCode.setImageBitmap(mQrBitmap);
//        } else {
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ViewHub.showLongToast(mContext, "生成二维码中...");
                }

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap logobitmap;
                    try {
                        logobitmap = BitmapFactory.decodeStream(new URL(mLogoUrl).openStream());

                        return logobitmap;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    genQrCode(mIvQrCode, result);
                }

            }.execute();
//        }

        mIvQrCode.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                return saveQrCode();
            }
        });

    }

    private void initTitleBar() {
        TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        Button btnRight = (Button)findViewById(R.id.titlebar_btnRight);

        tvTitle.setText(R.string.title_wp_qrcode);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);

        int width30 = FunctionHelper.dip2px(mContext.getResources(), 30);

        btnRight.setBackgroundResource(R.drawable.ic_menu_moreoverflow_normal_holo_dark);
        btnRight.setText("");
        int marginRight = FunctionHelper.dip2px(mContext.getResources(), 10);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width30, width30);
        lp.setMargins(0, 0, marginRight, 0);
        btnRight.setLayoutParams(lp);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

    }

    /**
     * @description 生成二维码
     * @created 2014-11-19 下午5:56:30
     * @author ZZB
     */
    private void genQrCode(final ImageView mIvQrCode, Bitmap logoBitmap) {
        String qrData = "http://" + SpManager.getShopId(mContext) + ".weipushop.com/";
        int qrCodeDimention = 500;
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null, Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(), qrCodeDimention);
        try {
            mQrBitmap = qrCodeEncoder.encodeAsBitmap(logoBitmap);
            mIvQrCode.setImageBitmap(mQrBitmap);
            FileUtils.saveBitmap(genFilePath(mLogoUrl), mQrBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * @description 生成二维码md5唯一缓存地址
     * @created 2014-11-19 下午5:56:46
     * @author ZZB
     */
    private String genFilePath(String url) {
        String cacheUrl = QR_CACHED_DIR + "/" + MD5Utils.encrypt16bit(url) + ".png";
        return cacheUrl;
    }

    /**
     * @description 保存二维码到本地
     * @created 2014-12-15
     */
    private boolean saveQrCode() {
        if (SDCardHelper.IsSDCardExists()) {
            if (mQrBitmap != null) {
                try {

                    String bucket = "weipu";
                    int bucket_id = 111113;
                    String imgDirPah = SDCardHelper.getDCIMDirectory() + File.separator + bucket + File.separator;
                    SDCardHelper.createDirectory(imgDirPah);
                    String imgFileName = SpManager.getShopName(mContext) + "二维码.png";
                    String imgPath = imgDirPah + imgFileName;
                    // String url = MediaStore.Images.Media.insertImage(
                    // cr, QRBitmap, imgPath, "");

                    FileOutputStream fileOutputStream = null;
                    File file = new File(imgPath);
                    try {
                        SDCardHelper.createFile(imgPath);
                        fileOutputStream = new FileOutputStream(file);
                        boolean result = mQrBitmap.compress(CompressFormat.JPEG, 100, fileOutputStream);
                    } catch (Exception ex) {
                        file.delete();
                        ex.printStackTrace();
                        Toast.makeText(mContext, "下载失败! 请重新尝试", Toast.LENGTH_SHORT).show();
                        return false;
                    } finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    ImageTools.saveImageExternal(mContext, imgPath, mQrBitmap.getRowBytes() * mQrBitmap.getHeight(),
                            imgFileName, bucket, bucket_id, 0, 0);

                    Toast.makeText(mContext, "下载成功!路径为:" + imgPath, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, "下载失败!  没有图片", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else {
            Toast.makeText(mContext, "下载失败!  请插入存储卡", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_btnRight:
                BottomMenuList menu = new BottomMenuList(this);
                menu.setItems(getResources().getStringArray(R.array.menu_qrcode_texts))
                        .setOnMenuItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switch (position) {
                                    case 0:
                                        saveQrCode();
                                        break;
                                    case 1:
                                        shareShop();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }).show();
                // if (menuPop.isShowing()) {
                // menuPop.dismiss();
                // } else {
                // menuPop.showAtLocation(findViewById(R.id.qr_code_view), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                // 0, 0);
                // }
                break;
        }
    }

    /**
     * @description 分享店铺
     * @created 2014-12-15
     */
    private void shareShop() {
        String shopUrl = "http://" + SpManager.getShopId(mContext) + ".weipushop.com/";
        String shopName = SpManager.getShopName(mContext);
        String userName = SpManager.getUserName(mContext);
        String imageUrl_upyun = SpManager.getShopLogo(mContext);
        String imageUrl = ImageUrlExtends.getImageUrl(imageUrl_upyun, Const.LIST_COVER_SIZE);

        ShareEntity mShareData = new ShareEntity();
        mShareData.setTitle(shopName);
        mShareData.setSummary("用户名：" + userName + "\n" + SpManager.getSignature(mContext));
        mShareData.setTargetUrl(shopUrl);
        if (TextUtils.isEmpty(imageUrl)) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_logo);
            mShareData.setThumData(Utils.bitmapToByteArray(bitmap, true));
        } else {
            mShareData.setImgUrl(imageUrl);
        }

        NahuoShare share = new NahuoShare(mContext, mShareData);
        share.show();

    }

    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
