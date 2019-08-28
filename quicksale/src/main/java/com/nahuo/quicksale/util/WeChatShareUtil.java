package com.nahuo.quicksale.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.nahuo.quicksale.ShareEntity;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ALAN on 2017/5/4 0004.
 */
public class WeChatShareUtil {
    //分享微信小程序
    public static SendMessageToWX.Req shareMiNiAppToWX(String defaultUrl, String miniAppId, String appPath, String title, String desc, ShareEntity share) throws Exception {
        WXMiniProgramObject miniProgramObject = new WXMiniProgramObject();
        miniProgramObject.webpageUrl = defaultUrl;
        miniProgramObject.userName = miniAppId;
        miniProgramObject.path = appPath;
        miniProgramObject.withShareTicket = true;
        WXMediaMessage msg = new WXMediaMessage(miniProgramObject);
        msg.description = desc;
        msg.title = title;
        Bitmap bmp = null;
        bmp = BitmapFactory.decodeStream(new URL(share.getImgUrl()).openStream());
        // 获得图片的宽高
//        int width = bmp.getWidth();
//        int height = bmp.getHeight();
//        float targetW=width;
//        float targetH=targetW*3/5;
//        float scaleW = (float) targetW / width;
//        float scaleH=targetH/height;
//        // 取得想要缩放的matrix参数
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleW,scaleH);
//        // 得到新的图片
//        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, false);
        // Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 300, 200, true);
//        WXImageObject wxImageObject=new WXImageObject(bmp);
        //      msg.mediaObject=wxImageObject;
        msg.thumbData = bitmap2Bytes(bmp, 32768);

        //  msg.thumbData = bmpToByteArray(thumbBmp, true);
        //  msg.thumbData=getImageFromURL(share.getImgUrl());
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        return req;
    }

    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            if (options == 10) {
                options -= 5;
            } else if (options == 5) {
                options -= 1;
            } else {
                options -= 10;
            }
        }
        return output.toByteArray();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                // F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }


    /**
     * 根据图片网络地址获取图片的byte[]类型数据
     *
     * @param urlPath 图片网络地址
     * @return 图片数据
     */

    //Java代码
    public static byte[] getImageFromURL(String urlPath) {
        byte[] data = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            // conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                data = readInputStream(is);
            } else {
                data = null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return data;
    }


    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = baos.toByteArray();
        try {
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
