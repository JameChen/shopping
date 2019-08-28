package com.nahuo.quicksale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.TreeMap;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.HttpRequestHelper.HttpRequest;
import com.nahuo.quicksale.common.NahuoShare;
import com.nahuo.quicksale.common.SafeUtils;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.PayForModel;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.zxing.encoding.EncodingHandler;

/***
 * 找人代付界面
 * created by 陈智勇   2015-5-21  上午11:26:12
 */
public class PayForMeActivity extends BaseActivity2 implements OnClickListener{

    private int orderID ; 
    private TextView txtProduct , txtBuyer , txtMoney , txtPhone ; 
    private ImageView imgPayQR ;  // 支付二维码
    private Bitmap payQR ; 
    private PayForModel model ;
    private int imgWidth; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("找人代付") ;
        setContentView(R.layout.activity_pay_for_me) ; 
        mLoadingDialog = new LoadingDialog(this) ; 
        orderID = getIntent().getIntExtra(OrderPayActivity.INTENT_PAY_ORDER_ID, 0);
        initView() ; 
        initData() ; 
    }
    private void initData() {
        HttpRequest request = mRequestHelper.getRequest(getApplicationContext(), "shop/agent/order/GetOrderReplacePayInfo"
                , this) ; 
//        request.addParam("orderID", String.valueOf(orderID)) ; 
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put("orderID", String.valueOf(orderID));
        SafeUtils.genCommonSinParams(getApplicationContext(), params);
        request.setParam(params);
        request.setConvert2Class(PayForModel.class) ; 
        request.doPost() ; 
    }
    private void initView() {
        txtProduct = (TextView)findViewById(R.id.txt_product) ; 
        txtBuyer = (TextView)findViewById(R.id.txt_buyer) ; 
        txtMoney = (TextView)findViewById(R.id.txt_pay) ; 
        txtPhone = (TextView)findViewById(R.id.txt_phone) ; 
        imgPayQR = (ImageView)findViewById(R.id.img_pay_qr) ; 
        imgPayQR.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(payQR!=null){
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                        File savePath = new File(Environment.getExternalStorageDirectory() , "天天拼货团_找人代付.jpg") ;
                        if(savePath.exists()){
                            savePath.delete() ; 
                        }
                        else{
                            insertContentResolver(savePath) ;
                        }
                        try {
                            FileOutputStream out = new FileOutputStream(savePath) ;
                            payQR.compress(CompressFormat.JPEG, 100, out) ;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ViewHub.showLongToast(getApplicationContext(), "二维码保存在 存储卡 根目录下。") ;
                    }
                    else{
                        ViewHub.showLongToast(getApplicationContext(), "没有存储卡，无法保存！") ;
                    }
                }
                return false;
            }
        }) ;
        imgWidth = getResources().getDisplayMetrics().widthPixels /2 ; 
        ViewGroup.LayoutParams params = imgPayQR.getLayoutParams() ; 
        params.width = imgWidth ; 
        params.height = imgWidth ; 
        imgPayQR.setLayoutParams(params) ;
        findViewById(R.id.txt_to_send).setOnClickListener(this) ; 
        
    }
    private void insertContentResolver(File file){
        ContentValues newValues = new ContentValues() ; 
        newValues.put(MediaStore.Images.Media.TITLE,file.getName().substring(0 , file.getName().lastIndexOf(".")));  
        newValues.put(MediaStore.Images.Media.DISPLAY_NAME,  
                file.getName());  
        newValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());  
        newValues.put(MediaStore.Images.Media.DATE_MODIFIED,  
                System.currentTimeMillis() / 1000);  
        newValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");  
        try{
            Uri uri = getContentResolver().insert(  
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, newValues); 
            Log.e("red_count_bg" , uri.getPath()) ;
        }catch(Exception e){
            e.printStackTrace() ; 
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(payQR!=null){
            payQR.recycle() ; 
            payQR =  null ; 
        }
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.txt_to_send:
                if(model!=null){
                    ShareEntity share = new ShareEntity() ; 
                    share.setAppName(getString(R.string.app_name)) ; 
                    share.setTitle("请求代付") ; 
                    share.setTargetUrl(model.payURL) ; 
                    share.setSummary("我在天天拼货团拍了一些宝贝，帮我代付一下吧~") ;
                    if(payQR!= null)
                        share.setThumData(Utils.bitmapToByteArray(payQR, false));
                    new NahuoShare(v.getContext(), share).show() ;
                }
                break ; 
        }
    }
    @Override
    public void onRequestSuccess(String method, Object object) {
        super.onRequestSuccess(method, object) ; 
        model = (PayForModel)object ; 
        if(model != null){
            model.payURL = model.payURL.replace("m.shop.weipushop.com/order/replacepay", "pay.nahuo.com/weipuorder/replacepay") ; 
            txtProduct.setText(getString(R.string.product_what , model.product.replace("\n", ""))) ; 
            if(TextUtils.isEmpty(model.buyer)){
                txtBuyer.setVisibility(View.GONE) ; 
            }
            else{
                txtBuyer.setText(getString(R.string.buyer_what , model.buyer)) ; 
            }
            String moneyStr = getString(R.string.pay_what , model.money) ; 
            SpannableString span = new SpannableString(moneyStr);
            span.setSpan(new ForegroundColorSpan(Color.RED), 4, moneyStr.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE) ;
            txtMoney.setText(span) ; 
            if(TextUtils.isEmpty(model.phone)){
                txtPhone.setVisibility(View.GONE) ; 
            }
            else
                txtPhone.setText(getString(R.string.phone_number , model.phone)) ; 
//            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(model.payURL, null, Contents.Type.TEXT,
//                    BarcodeFormat.QR_CODE.toString(), imgWidth);
            try {
                payQR = EncodingHandler.createQRCode(model.payURL, imgWidth);
                // payQR = qrCodeEncoder.encodeAsBitmap(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(payQR != null)
            {
                imgPayQR.setImageBitmap(payQR) ; 
            }
        }
    }
    @Override
    public void onRequestStart(String method) {
        super.onRequestStart(method) ; 
        mLoadingDialog.start("获取中...") ;
    }
    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        super.onRequestFail(method, statusCode, msg) ; 
        ViewHub.showLongToast(getApplicationContext(), msg) ;
    }
    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        super.onRequestExp(method, msg,data) ;
    }
}
