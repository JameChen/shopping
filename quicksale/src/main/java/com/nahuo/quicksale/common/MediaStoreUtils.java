package com.nahuo.quicksale.common;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.nahuo.quicksale.oldermodel.MediaStoreImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZB on 2015/7/9 16:23
 */
public class MediaStoreUtils {

    /**
     * @author ZZB
     * @desc 获取最近的图片
     */
    public static List<MediaStoreImage> getRecentImages(Context context, int count) {
        List<MediaStoreImage> imgs = new ArrayList<>();
        Cursor cursor = null;
        int counter = 0;
        try {
            String[] projections = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT};
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projections, null, null, MediaStore.Images.Media.DATE_ADDED + " desc");
            int pathIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int wIndex = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
            int hIndex = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
            while (cursor.moveToNext()) {
                if (counter < count) {
                    String data = cursor.getString(pathIndex);
                    MediaStoreImage img = new MediaStoreImage(cursor.getString(pathIndex), cursor.getInt(wIndex), cursor.getInt(hIndex));
                    imgs.add(img);
                } else {
                    break;
                }
                counter++;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return imgs;
    }

    /**
     * @author ZZB
     * @desc 把图片文件加入到系统数据库，使类似于相册等应用可以看到
     */
    public static void scanImageFile(Context context, String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.getApplicationContext().sendBroadcast(scanFileIntent);
    }

    public static void scanImageFile(Context context, Uri uri) {
        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    uriToFile(uri,context).getAbsolutePath(), uriToFile(uri,context).getName(), null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.getApplicationContext().sendBroadcast(scanFileIntent);
       // MediaScannerConnection.scanFile(context, new String[]{uriToFile(uri,context).getAbsolutePath()}, null, null);
    }
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
                MediaStore.Images.Media._ID
        }, MediaStore.Images.Media.DATA + "=? ", new String[] {
                filePath
        }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] {
                        MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA
                }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {} else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }
}
