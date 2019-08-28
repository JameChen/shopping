package com.nahuo.quicksale.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.nahuo.quicksale.common.MediaStoreUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by jame on 2018/8/13.
 */

public class FileUriUtil {
    public static Uri getFileProviderUri(Context context, File dir) {
        Uri destinationUri;
        String authority = context.getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            destinationUri = FileProvider.getUriForFile(context, authority, dir);
        } else {
            destinationUri = Uri.fromFile(dir);
        }
        return destinationUri;
    }

    public static Uri getWxFileProviderUri(Context context, File dir) {

        Uri destinationUri;
        //  String authority = context.getPackageName() + ".provider";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //通过FileProvider创建一个content类型的Uri
            try {
                destinationUri = MediaStoreUtils.getImageContentUri(context, dir);
               // destinationUri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(), dir.getAbsolutePath(), "bigbang.jpg", null));
            } catch (Exception e) {
                e.printStackTrace();
                destinationUri=null;
            }
        } else {
            destinationUri = Uri.fromFile(dir);
        }
        return destinationUri;
    }
}
