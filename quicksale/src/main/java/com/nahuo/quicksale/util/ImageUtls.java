package com.nahuo.quicksale.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by jame on 2017/8/2.
 */

public class ImageUtls {
    public static final String BUYTOOL_ROOT_DIRECTORY = "/mnt/sdcard/water_pinhuo_save";
    public static  int Max_Compress_Kb = 300;

    /**
     * 将Bitmap转换成文件
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String path, String fileName) throws IOException {
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        Log.d("yu", bm.getByteCount() + "");
        File myCaptureFile = new File(path, fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        if (bm != null)
            bm.recycle();
        return myCaptureFile;
    }

    public static File commPressNoWater(String fileName, String picName) throws Exception {
        File file = null;
        file = ImageUtls.getSmallBitmap(fileName, picName);
//        file = saveFile(bitmap, ImageUtls.BUYTOOL_ROOT_DIRECTORY, picName);
//        if (bitmap != null)
//            bitmap.recycle();
        return file;
    }

    //删除文件
    public static void delFile(String fileName) {
        File file = new File(BUYTOOL_ROOT_DIRECTORY + "/" + fileName);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    /**
     * 压缩256kb
     *
     * @param bitmap
     * @return
     */
    public static Bitmap commpressPic(Bitmap bitmap) throws Exception {
        Bitmap bitmap2 = null;
        // bitmap2 = compressAndGenImage(bitmap,"",32768);
        bitmap2 = Bytes2Bimap(bitmap2Bytes(bitmap, 204800));
        return bitmap2;
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getMyBitmap(String filePath) throws Exception {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int h = 0;
        int w = 0;
        if (width > 1100) {
            h = height * 1100 / width;
            w = 1100;
        } else {
            h = height;
            w = width;
        }
        bitmap=matrixScale(bitmap, 0, 0, w, h);
        return getCompressImage(bitmap);

    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static File getSmallBitmap(String filePath, String picName) throws Exception {
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, options);
//        int height = options.outHeight;
//        int width = options.outWidth;
//        int h= height * 1100/width;
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, 1100, h);
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeFile(filePath, options);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int h = 0;
        int w = 0;
        if (width > 1100) {
            h = height * 1100 / width;
            w = 1100;

        } else {
            h = height;
            w = width;
        }
        bitmap=matrixScale(bitmap, 0, 0, w, h);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
//            Log.d("yu",bitmap.getAllocationByteCount()+"");
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
//            Log.d("yu",bitmap.getByteCount()+"");
//        }
        return compressImage(bitmap, picName);

    }

    /**
     * @param bitmap  原图路径
     * @param offsetX 截取开始点在X轴偏移量
     * @param offsetY 截取开始点在Y轴偏移量
     * @param targetW 截取多宽（像素）
     * @param targetH 截取多高（像素）
     * @return
     */
    public static Bitmap matrixScale(Bitmap bitmap, int offsetX, int offsetY, int targetW, int targetH) {
        // 构建原始位图
        // Bitmap bitmap = BitmapFactory.decodeFile(path);
        // 获取原始宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算宽高缩放比例，targetW，targetH即期待缩放完成后位图的宽高
        float scaleW = (float) targetW / width;
        float scaleH = (float) targetH / height;
        // 将缩放比例放进矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);
        // 这个方法作用非常多，详细解释一下各个参数的意义！
        // bitmap：原始位图
        // 第二到第五个参数，即截取原图哪一部分构建新位图，
        // offsetX和offsetY代表在X轴和Y轴上的像素偏移量，即从哪个位置开始截取
        // width和height代表截取多少个像素，但是要注意，offsetX+width应该小于等于原图的宽度
        // offsetY+height小于等于原图高度，要不然会报错，因为截到原图外面去了
        // 像下面这样填写，就代表截取整个原图，
        // Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        // 如果填写100,100,200,200，就代表
        // 从原图左上角往右和下各偏移100像素，然后往后和往下各截取200构建新位图
        // matrix：缩放矩阵
        // 最后一个参数表示如果矩阵里面还存放了过滤条件，是否按条件过滤（如果matrix里面只放了平移数据），最后一个参数设置成什么都不会生效
        bitmap = Bitmap.createBitmap(bitmap, offsetX, offsetY, width, height, matrix, false);
        return bitmap;
    }
//    /**
//     * 压缩
//     *
//     * @param srcPath
//     * @return
//     */
//    public static Bitmap getCommpressPic(Bitmap srcPath) throws Exception {
//        Bitmap bitmap2 = null;
//        bitmap2 = getImage(srcPath);
//        return bitmap2;
//    }

    //    public static Bitmap getImage(Bitmap image) throws Exception {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//            baos.reset();//重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
//        newOpts.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        newOpts.inJustDecodeBounds = false;
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//        float hh = 800f;//这里设置高度为800f
//        float ww = 480f;//这里设置宽度为480f
//        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//        int be = 1;//be=1表示不缩放
//        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / ww);
//        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / hh);
//        }
//        if (be <= 0)
//            be = 1;
//        newOpts.inSampleSize = be;//设置缩放比例
//        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        isBm = new ByteArrayInputStream(baos.toByteArray());
//        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
//    }
    public static Bitmap getCompressImage(Bitmap image) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > Max_Compress_Kb) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        if (image != null && !image.isRecycled())
            image.recycle();
        is.close();
        return bitmap;
    }

    public static File compressImage(Bitmap image, String picName) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > Max_Compress_Kb) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream is = new ByteArrayInputStream(baos.toByteArray());
        // Bitmap bitmap = BitmapFactory.decodeStream(is);
        if (image != null && !image.isRecycled())
            image.recycle();
        File dirFile = new File(BUYTOOL_ROOT_DIRECTORY);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(BUYTOOL_ROOT_DIRECTORY, picName);
        FileOutputStream fos = new FileOutputStream(myCaptureFile);
        // Put data in your baos
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        is.close();
        return myCaptureFile;
    }

    public static Bitmap compressAndGenImage(Bitmap image, String outPath, int maxSize)
            throws Exception {
        Bitmap bitmap;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        image.compress(Bitmap.CompressFormat.JPEG, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // 每次质量下降10
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, os);
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is);
        // Generate compressed image file
//        FileOutputStream fos = new FileOutputStream(outPath);
//        fos.write(os.toByteArray());
//        fos.flush();
//        fos.close();
        is.close();
        return bitmap;
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
        if (bitmap != null || !bitmap.isRecycled())
            bitmap.recycle();
        return output.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 获取网络图片
     */
    public static Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            int length = http.getContentLength();
            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }


//    /**
//     * 设置水印图片在左上角
//     *
//     * @param src
//     * @param watermark
//     * @param paddingLeft
//     * @param paddingTop
//     * @return
//     */
//    public static Bitmap createWaterMaskLeftTop(
//            Context context, Bitmap src, Bitmap watermark,
//            int paddingLeft, int paddingTop) throws Exception {
//
//        return createWaterMaskBitmap(src, watermark,
//                dp2px(context, paddingLeft), dp2px(context, paddingTop));
//    }

    private static File createWaterMaskBitmap(Bitmap src, Bitmap watermark,
                                              int paddingLeft, int paddingTop, String picName) throws Exception {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        Bitmap watermark2 = resizeBitmap(watermark, width, height);
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark2, 0, 0, null);
        // 保存
        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        if (src != null)
            src.recycle();
        if (watermark2 != null)
            watermark2.recycle();
        return compressImage(newb, picName);
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) throws Exception {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWight = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight, scaleHeight);
            Bitmap res = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return res;

        } else {
            return null;
        }
    }

//    /**
//     * 设置水印图片在右下角
//     *
//     * @param src
//     * @param watermark
//     * @param paddingRight
//     * @param paddingBottom
//     * @return
//     */
//    public static Bitmap createWaterMaskRightBottom(
//            Context context, Bitmap src, Bitmap watermark,
//            int paddingRight, int paddingBottom) throws Exception {
//        return createWaterMaskBitmap(src, watermark,
//                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
//                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
//    }
//
//    /**
//     * 设置水印图片到右上角
//     *
//     * @param src
//     * @param watermark
//     * @param paddingRight
//     * @param paddingTop
//     * @return
//     */
//    public static Bitmap createWaterMaskRightTop(
//            Context context, Bitmap src, Bitmap watermark,
//            int paddingRight, int paddingTop) throws Exception {
//        return createWaterMaskBitmap(src, watermark,
//                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
//                dp2px(context, paddingTop));
//    }
//
//    /**
//     * 设置水印图片到左下角
//     *
//     * @param src
//     * @param watermark
//     * @param paddingLeft
//     * @param paddingBottom
//     * @return
//     */
//    public static Bitmap createWaterMaskLeftBottom(
//            Context context, Bitmap src, Bitmap watermark,
//            int paddingLeft, int paddingBottom) throws Exception {
//        return createWaterMaskBitmap(src, watermark, dp2px(context, paddingLeft),
//                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
//    }
//

    /**
     * 设置水印图片到中间
     *
     * @param src
     * @param watermark
     * @return
     */
    public static File createWaterMaskCenter(Bitmap src, Bitmap watermark, String picName) throws Exception {
        int paddingLeft = 0;
        int paddingTop = 0;
        paddingLeft = Math.abs(src.getWidth() - watermark.getWidth()) / 2;
        paddingTop = Math.abs(src.getHeight() - watermark.getHeight()) / 2;
        return createWaterMaskBitmap(src, watermark,
                paddingLeft,
                paddingTop, picName);
    }

    /**
     * 给图片添加文字到左上角
     *
     * @param context
     * @param bitmap
     * @param text
     * @return
     */
    public static Bitmap drawTextToLeftTop(Context context, Bitmap bitmap, String text,
                                           int size, int color, int paddingLeft, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 绘制文字到右下角
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToRightBottom(Context context, Bitmap bitmap, String text,
                                               int size, int color, int paddingRight, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 绘制文字到右上方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingRight
     * @param paddingTop
     * @return
     */
    public static Bitmap drawTextToRightTop(Context context, Bitmap bitmap, String text,
                                            int size, int color, int paddingRight, int paddingTop) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                bitmap.getWidth() - bounds.width() - dp2px(context, paddingRight),
                dp2px(context, paddingTop) + bounds.height());
    }

    /**
     * 绘制文字到左下方
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @param paddingLeft
     * @param paddingBottom
     * @return
     */
    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
                                              int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }

    /**
     * 绘制文字到中间
     *
     * @param context
     * @param bitmap
     * @param text
     * @param size
     * @param color
     * @return
     */
    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text,
                                          int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 2,
                (bitmap.getHeight() + bounds.height()) / 2);
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;
    }

    /**
     * 缩放图片
     *
     * @param src
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }

    /**
     * dip转pix
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
