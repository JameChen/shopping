package com.nahuo.quicksale.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;

import com.google.zxing.BarcodeFormat;
import com.nahuo.library.helper.ImageTools;
import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.quicksale.exceptions.CatchedException;
import com.nahuo.quicksale.qrcode.Contents;
import com.nahuo.quicksale.qrcode.QRCodeEncoder;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;

public class QrCodeHelper {

    public static String downloadDirPath = SDCardHelper.getSDCardRootDirectory()
            + "/weipu/share_download";


    public static Uri genItemQrcode(Context context, String genUrl, @DrawableRes int imgResId, String tipsText) {
        Bitmap logoBitmap=BitmapFactory.decodeResource(context.getResources(), imgResId);
        Uri uri = genItemQrcode(context, genUrl, logoBitmap, tipsText);
        return uri;
    }

    /**
     * @description 生成二维码
     * @author pj
     */
    public static Uri genItemQrcode(Context context, String genUrl, String logoPath, String tipsText) {
        //商品图
        Bitmap logoBitmap = BitmapFactory.decodeFile(logoPath);
        Uri uri = genItemQrcode(context, genUrl, logoBitmap, tipsText);
        return uri;
    }

    private static Uri genItemQrcode(Context context, String genUrl, Bitmap logoBitmap, String tipsText) {
        int maxWidth = 500;
        int qrCodeDimention = 180;
        int widthSpac = 10;
        try {
            float qrcodeBeginY = 0;
            float scaleWidth = (float) 500.00 / logoBitmap.getWidth();
            qrcodeBeginY = scaleWidth * logoBitmap.getHeight();

            //根据商品图的高度决定整个图层高度，整个背景
            Bitmap newbmp = Bitmap.createBitmap(maxWidth, (int) qrcodeBeginY + widthSpac + qrCodeDimention, android.graphics.Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newbmp);
            canvas.drawColor(Color.WHITE);

            //写商品图
            float scaleHeight = qrcodeBeginY / logoBitmap.getHeight();
            Matrix mtx = new Matrix();
            mtx.postScale(scaleWidth, scaleHeight);
            canvas.drawBitmap(logoBitmap, mtx, null);

            //二维码
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(genUrl, null,
                    Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                    qrCodeDimention);
            Bitmap mQrBitmap = qrCodeEncoder.encodeAsBitmap(null);
            canvas.drawBitmap(mQrBitmap, widthSpac, qrcodeBeginY + widthSpac, null);
            //文字
            if (tipsText.length() > 0) {

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.DKGRAY);
                float scale = context.getResources().getDisplayMetrics().density;
                paint.setTextSize(20.0f);//DisplayUtil.sp2px(6, scale));
                paint.setDither(true);
                paint.setFilterBitmap(true);
                Rect bounds = new Rect();
                paint.getTextBounds(tipsText, 0, tipsText.length(), bounds);
                canvas.drawText(tipsText, qrCodeDimention + widthSpac * 2, qrcodeBeginY + widthSpac + 30, paint);

                paint.setColor(Color.LTGRAY);
            }
            String qrCodeSavePath = downloadDirPath;
            String qrCodeSaveFileName = SystemClock.currentThreadTimeMillis() + "_item_qrcode.jpg";
//            if (logoFile.exists()) {
//                qrCodeSaveFileName = logoFile.getName() + "qrcode.jpg";
//            } else {
            qrCodeSaveFileName = SystemClock.currentThreadTimeMillis() + "_item_qrcode.jpg";
//            }
            qrCodeSavePath += "/" + qrCodeSaveFileName;
            FileUtils.saveBitmap(qrCodeSavePath, newbmp);
            String bucket = "share_download";
            int bucket_id = 111111;
            ImageTools.saveImageExternal(context, qrCodeSavePath, 100, qrCodeSaveFileName,
                    bucket, bucket_id, 0, 0);
            return Uri.fromFile(new File(qrCodeSavePath));
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            CrashReport.postCatchedException(new CatchedException(e));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
