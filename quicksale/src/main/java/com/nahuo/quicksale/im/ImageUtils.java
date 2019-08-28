package com.nahuo.quicksale.im;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;

import java.io.IOException;

public class ImageUtils {
	// public static String getThumbnailImagePath(String imagePath) {
	// String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
	// path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1,
	// imagePath.length());
	// EMLog.d("msg", "original image path:" + imagePath);
	// EMLog.d("msg", "thum image path:" + path);
	// return path;
	// }

	public static String getImagePath(String remoteUrl) {
		String imageName = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1,
				remoteUrl.length());
		String path = PathUtil.getInstance().getImagePath() + "/" + imageName;
		EMLog.d("msg", "image path:" + path);
		return path;

	}
	public static BitmapFactory.Options getBitmapOptions(String var0) {
		BitmapFactory.Options var1 = new BitmapFactory.Options();
		var1.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(var0, var1);
		return var1;
	}
	public static int calculateInSampleSize(BitmapFactory.Options var0, int var1, int var2) {
		int var3 = var0.outHeight;
		int var4 = var0.outWidth;
		int var5 = 1;
		if(var3 > var2 || var4 > var1) {
			int var6 = Math.round((float)var3 / (float)var2);
			int var7 = Math.round((float)var4 / (float)var1);
			var5 = var6 > var7?var6:var7;
		}

		return var5;
	}
	public static int readPictureDegree(String var0) {
		short var1 = 0;

		try {
			ExifInterface var2 = new ExifInterface(var0);
			int var3 = var2.getAttributeInt("Orientation", 1);
			switch(var3) {
				case 3:
					var1 = 180;
					break;
				case 6:
					var1 = 90;
					break;
				case 8:
					var1 = 270;
			}
		} catch (IOException var4) {
			var4.printStackTrace();
		}

		return var1;
	}
	public static Bitmap rotateImageView(int var0, Bitmap var1) {
		Matrix var2 = new Matrix();
		var2.postRotate((float)var0);
		Bitmap var3 = Bitmap.createBitmap(var1, 0, 0, var1.getWidth(), var1.getHeight(), var2, true);
		return var3;
	}

	public static Bitmap decodeScaleImage(String var0, int var1, int var2) {
		BitmapFactory.Options var3 = getBitmapOptions(var0);
		int var4 = calculateInSampleSize(var3, var1, var2);
		EMLog.d("img", "original wid" + var3.outWidth + " original height:" + var3.outHeight + " sample:" + var4);
		var3.inSampleSize = var4;
		var3.inJustDecodeBounds = false;
		Bitmap var5 = BitmapFactory.decodeFile(var0, var3);
		int var6 = readPictureDegree(var0);
		Bitmap var7 = null;
		if(var5 != null && var6 != 0) {
			var7 = rotateImageView(var6, var5);
			var5.recycle();
			var5 = null;
			return var7;
		} else {
			return var5;
		}
	}

	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName = thumbRemoteUrl.substring(
				thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path = PathUtil.getInstance().getImagePath() + "/" + "th"
				+ thumbImageName;
		EMLog.d("msg", "thum image path:" + path);
		return path;
	}

}
