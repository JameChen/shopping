package com.nahuo.library.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.nahuo.library.utils.TimeUtils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;

public class ImageTools {

	/**
	 * 生成缩略图，并保存到本地SDCard
	 * 
	 * @param srcPath
	 *            原图片文件路径
	 * @param width
	 *            生成缩略图的宽度
	 * @param height
	 *            生成缩略图的高度
	 * @param compressQuality
	 *            压缩率0-100
	 * @param rename
	 *            是否重命名，true时，生成的缩略图将按时间重命名
	 * @return 返回保存成功后的缩略图文件路径
	 * */
	public static String createThumb(String srcPath, int width, int height,
			int compressQuality, boolean rename) {
		if (!SDCardHelper.IsSDCardExists())
			return "";
		if (TextUtils.isEmpty(srcPath))
			return "";
		// 原图片文件目录、名称
		// String dirPath = srcPath.substring(0, srcPath.lastIndexOf("/"));
		String srcFileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
		// 创建缩略图文件目录、名称(若为指定缩略图名称，则缩略图名称与原图名称保持一致)
		String thumbDirPath = SDCardHelper.getSDCardRootDirectory()
				+ "/weipu/upload_tmp/thumb";
		String thumbFileName = "";
		if (rename) {
			thumbFileName = TimeUtils.dateToTimeStamp(new Date(), "yyyyMMddHHmmssSSS") + ".jpg";
		} else {
			thumbFileName = srcFileName;
		}

		if (!SDCardHelper.checkFileExists(thumbDirPath))
			SDCardHelper.createDirectory(thumbDirPath);

		String thumbFilePath = "";
		FileOutputStream fileOutputStream = null;
		File file = new File(thumbDirPath, thumbFileName);
		try {
			fileOutputStream = new FileOutputStream(file);
			boolean result = zoomFile(srcPath, width, height, fileOutputStream , 100);
			System.gc();
			if (result) {
				thumbFilePath = "file://" + file.getAbsolutePath();
			}
		} catch (Exception ex) {
			file.delete();
			ex.printStackTrace();
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

		return thumbFilePath;
	}

	/**
	 * 生成缩略图，并保存到本地SDCard
	 * 
	 * @param srcPath
	 *            原图片文件路径
	 * @param width
	 *            生成缩略图的宽度
	 * @param height
	 *            生成缩略图的高度
	 * @param compressQuality
	 *            压缩率0-100
	 * @param rename
	 *            是否重命名，true时，生成的缩略图将按时间重命名
	 * @param maxSize
	 *            压缩最大尺寸(KB)
	 * @return 返回保存成功后的缩略图文件路径
	 * */
	public static String createThumb(String srcPath, int width, int height,
			int compressQuality, boolean rename, int maxSize) {
		if (!SDCardHelper.IsSDCardExists())
			return "";
		if (TextUtils.isEmpty(srcPath))
			return "";
		// 原图片文件目录、名称
		// String dirPath = srcPath.substring(0, srcPath.lastIndexOf("/"));
		String srcFileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
		// 创建缩略图文件目录、名称(若为指定缩略图名称，则缩略图名称与原图名称保持一致)
		String thumbDirPath = SDCardHelper.getSDCardRootDirectory()
				+ "/weipu/upload_tmp/thumb";
		String thumbFileName = "";
		if (rename) {
			thumbFileName = TimeUtils.dateToTimeStamp(new Date(), "yyyyMMddHHmmssSSS") + ".jpg";
		} else {
			thumbFileName = srcFileName;
		}

		if (!SDCardHelper.checkFileExists(thumbDirPath))
			SDCardHelper.createDirectory(thumbDirPath);

		String thumbFilePath = "";
		FileOutputStream fileOutputStream = null;
		File file = new File(thumbDirPath, thumbFileName);
		try {
			fileOutputStream = new FileOutputStream(file);
			boolean result = zoomFile(srcPath, width, height, fileOutputStream, maxSize);
			System.gc();
			if (result) {
				thumbFilePath = "file://" + file.getAbsolutePath();
			}
		} catch (Exception ex) {
			file.delete();
			ex.printStackTrace();
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

		return thumbFilePath;
	}

//	/**
//	 * 生成缩略图，并保存到本地SDCard
//	 * 
//	 * @param srcPath
//	 *            原图片文件路径
//	 * @param width
//	 *            生成缩略图的宽度
//	 * @param height
//	 *            生成缩略图的高度
//	 * @param compressQuality
//	 *            压缩率0-100
//	 * @param rename
//	 *            是否重命名，true时，生成的缩略图将按时间重命名
//	 * @return 返回保存成功后的缩略图文件路径
//	 * */
//	public static String createThumb(String srcPath,String thumbPath, int width, int height,
//			int compressQuality, boolean rename) {
//		if (!SDCardHelper.IsSDCardExists())
//			return "";
//		if (TextUtils.isEmpty(srcPath))
//			return "";
//		// 原图片文件目录、名称
//		// String dirPath = srcPath.substring(0, srcPath.lastIndexOf("/"));
////		String srcFileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
//		String thumbFileName = "";
//		if (rename) {
//			thumbFileName = thumbPath;
//		} else {
//			thumbFileName = srcPath;
//		}
//
//		String thumbFilePath = "";
//		FileOutputStream fileOutputStream = null;
//		File file = new File(thumbPath);
//		try {
//			Bitmap bitmap = imageZoom(srcPath, width, height);
//			fileOutputStream = new FileOutputStream(file);
//			boolean result = bitmap.compress(CompressFormat.JPEG,
//					compressQuality, fileOutputStream);
//			bitmap.recycle();
//			System.gc();
//			if (result) {
//				thumbFilePath = "file://" + file.getAbsolutePath();
//			}
//		} catch (Exception ex) {
//			file.delete();
//			ex.printStackTrace();
//		} finally {
//			try {
//				if (fileOutputStream != null) {
//					fileOutputStream.flush();
//					fileOutputStream.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return thumbFilePath;
//	}
	/**
     * 压缩图片
     * 
     * @param filePath
     *            图片文件路径
     * @param _maxSize
     *            压缩最大尺寸(KB)
     * @return Bitmap对象
     * */
    public static boolean zoomFile(Bitmap bitmap, FileOutputStream fos, int _maxSize) {

        try {
            //当前质量
            int nowQuality = 100;
            //再压缩尺寸到指定以内(KB)
            double maxSize =_maxSize*1024.00;
            //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, nowQuality, baos);
            byte[] b = baos.toByteArray();
            while (b.length > maxSize) {
                nowQuality -= 1;
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, nowQuality, baos);
                b = baos.toByteArray();
            }
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            bitmap.compress(CompressFormat.JPEG, nowQuality, fos);

            bitmap.recycle();
//          //将字节换成KB
//          double mid = b.length/1024;
//          //判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
//          if (mid > maxSize) {
//              //获取bitmap大小 是允许最大大小的多少倍
//              double i = mid / maxSize * 2;
//              //开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
//              bitmap = zoomImageSize(bitmap, (int)(bitmap.getWidth() / Math.sqrt(i)),
//                      (int)(bitmap.getHeight() / Math.sqrt(i)));
//          }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 压缩图片
     * 
     * @param filePath
     *            图片文件路径
     * @return Bitmap对象
     * */
    public static Bitmap getBitmapWithFileName(String filePath, int width, int height) {

        Bitmap bitmap = null;
        try {
            //先压缩尺寸到指定高宽以内
            BitmapFactory.Options options = new BitmapFactory.Options();  
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            int inSampleSize = computeSampleSize(options, -1, width * height);
            if (inSampleSize < 1) {
                options.inSampleSize = 2;
            } else {
                options.inSampleSize = inSampleSize;
            }
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            FileInputStream inputStream = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeFileDescriptor(inputStream.getFD(),
                    null, options);
            inputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return bitmap;
    }
    
	/**
	 * 压缩图片
	 * 
	 * @param filePath
	 *            图片文件路径
	 * @param width
	 *            压缩后图片的宽度
	 * @param width
	 *            压缩后图片的高度
	 * @param _maxSize
	 *            压缩最大尺寸(KB)
	 * @return Bitmap对象
	 * */
	public static boolean zoomFile(String filePath, int width, int height, FileOutputStream fos, int _maxSize) {

		Bitmap bitmap = null;
		try {
			//先压缩尺寸到指定高宽以内
			BitmapFactory.Options options = new BitmapFactory.Options();  
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			int inSampleSize = computeSampleSize(options, -1, width * height);
			if (inSampleSize < 1) {
				options.inSampleSize = 2;
			} else {
				options.inSampleSize = inSampleSize;
			}
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			FileInputStream inputStream = new FileInputStream(filePath);
			bitmap = BitmapFactory.decodeFileDescriptor(inputStream.getFD(),
					null, options);
			inputStream.close();

//			zoomImageSize(bitmap, width, height);
			
			//当前质量
			int nowQuality = 100;
			//再压缩尺寸到指定以内(KB)
			double maxSize =_maxSize*1024.00;
			//将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, nowQuality, baos);
			byte[] b = baos.toByteArray();
			while (b.length > maxSize) {
			    b = null ; 
				nowQuality -= 8;
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, nowQuality, baos);
				b = baos.toByteArray();
			}
			bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
			bitmap.compress(CompressFormat.JPEG, nowQuality, fos);

			bitmap.recycle();
//			//将字节换成KB
//			double mid = b.length/1024;
//			//判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
//			if (mid > maxSize) {
//				//获取bitmap大小 是允许最大大小的多少倍
//				double i = mid / maxSize * 2;
//				//开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
//				bitmap = zoomImageSize(bitmap, (int)(bitmap.getWidth() / Math.sqrt(i)),
//						(int)(bitmap.getHeight() / Math.sqrt(i)));
//			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

//	public static Bitmap zoomImageSize(Bitmap bgimage, int newWidth,
//			int newHeight) {
//		Bitmap newBitmap = bgimage.copy(Bitmap.Config.ARGB_8888, true);
//		// 获取这个图片的宽和高
//		int width = newBitmap.getWidth();
//		int height = newBitmap.getHeight();
////		// 创建操作图片用的matrix对象
////		Matrix matrix = new Matrix();
////		// 计算宽高缩放率
////		float scaleWidth = ((float) newWidth) / width;
////		float scaleHeight = ((float) newHeight) / height;
////		// 缩放图片动作
////		matrix.postScale(scaleWidth, scaleHeight);
//		
//		Canvas canvas = new Canvas(newBitmap);
//		Rect rect = new Rect (0,0,width,height);
//		RectF rectf = new RectF(0,0,newWidth,newHeight);
//		canvas.drawBitmap(newBitmap,rect,rectf,null);  
//		
////		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
////				(int) height, matrix, true);
//		return newBitmap;
//	}


	/**
	 * 删除图片
	 * */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 检查图片的旋转角度，调整回来
	 * 
	 * @param filePath
	 *            图片绝对路径
	 * @param check
	 *            是否检查旋转角度，如果为false，则默认为左侧90°旋转
	 * */
	public static void checkImgRotaing(String filePath,boolean check,int defaultDegree,boolean isFilp) {
		if (!SDCardHelper.IsSDCardExists())
			return;
		if (TextUtils.isEmpty(filePath))
			return;

		if (!SDCardHelper.checkFileExists(filePath))
			return;

		FileOutputStream fileOutputStream = null;
		File file = new File(filePath);
		// 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
		int degree = 0;
		if (check){
			degree = ImageTools.readPictureDegree(file.getAbsolutePath());
		}else{
			degree = defaultDegree;
		}
		if (degree==0)
			return;
		try {
			Bitmap bitmap = null;

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			// 获得图片压缩比例
//			int inSampleSize = computeSampleSize(options, -1, options.outWidth/2
//					* options.outHeight/2);
			// 设置压缩比例
//			if (inSampleSize < 1) {
//				options.inSampleSize = 4;
//			} else {
//				options.inSampleSize = inSampleSize;
//			}
			options.inJustDecodeBounds = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			FileInputStream inputStream = new FileInputStream(filePath);
			// 压缩
			bitmap = BitmapFactory.decodeFileDescriptor(inputStream.getFD(),
					null, options);
			inputStream.close();
			// 校正旋转
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			if (isFilp){
				matrix.postScale(-1, 1);
			}
			Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					options.outWidth , options.outHeight, matrix, true);
			bitmap.recycle();

			fileOutputStream = new FileOutputStream(file);
			// 重新保存
			boolean result = newBitmap.compress(CompressFormat.JPEG, 100,
					fileOutputStream);
			bitmap.recycle();
			System.gc();
		} catch (Exception ex) {
			file.delete();
			ex.printStackTrace();
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
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
//		int degree = 0;
//		File file = new File(path);
//		Uri mImageCaptureUri = Uri.fromFile(file);
//	    ContentResolver cr = parent.getContentResolver();  
//	    Cursor cursor = cr.query(mImageCaptureUri, null, null, null, null);// 根据Uri从数据库中找  
//	    if (cursor != null) {  
//	        cursor.moveToFirst();// 把游标移动到首位，因为这里的Uri是包含ID的所以是唯一的不需要循环找指向第一个就是了  
//	        String orientation = cursor.getString(cursor  
//	                .getColumnIndex("orientation"));// 获取旋转的角度  
//	        degree = (int)Integer.valueOf(orientation);
//	        cursor.close();  
//	    }  
//	    return degree;
	    
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	

	/**
	 * 保存图片资料到系统元数据中
	 * 
	 * @param path
	 *            图片路径
	 * @param size
	 *            图片大小
	 * @param name
	 *            图片文件名
	 * @param bucket
	 *            父路径文件夹名
	 * @param bucket_id
	 *            父路径文件夹ID
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @return 
	 */
	public static void saveImageExternal(
			Context mContext,
			String path,
			int size,
			String name,
			String bucket,
			int bucket_id,
			int width,
			int height) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Media.DATA, path);
		contentValues.put(Media.SIZE, size);
		contentValues.put(Media.DISPLAY_NAME, name);
		contentValues.put(Media.MIME_TYPE, "image/jpeg");
		contentValues.put(Media.TITLE, name.substring(0, name.indexOf(".")));
		contentValues.put(Media.DATE_ADDED, System.currentTimeMillis());
		contentValues.put(Media.DATE_MODIFIED, System.currentTimeMillis());
		contentValues.put(Media.DATE_TAKEN, System.currentTimeMillis());
		contentValues.put(Media.ORIENTATION, "0");
		contentValues.put(Media.BUCKET_ID, bucket_id);
		contentValues.put(Media.BUCKET_DISPLAY_NAME, bucket);
		contentValues.put(Media.WIDTH, width);
		contentValues.put(Media.HEIGHT, height);
		
		mContext.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
	}
	
	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	@TargetApi(19)
    public static String getFilePath4Uri(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = MediaStore.Images.Media.DATA;
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static Bitmap blurBitmap(Context context, Bitmap bitmap){

		//Let's create an empty bitmap with the same size of the bitmap we want to blur
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

		//Instantiate a new Renderscript
		RenderScript rs = RenderScript.create(context);

		//Create an Intrinsic Blur Script using the Renderscript
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

		//Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
		Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

		//Set the radius of the blur
		blurScript.setRadius(25.f);

		//Perform the Renderscript
		blurScript.setInput(allIn);
		blurScript.forEach(allOut);

		//Copy the final bitmap created by the out Allocation to the outBitmap
		allOut.copyTo(outBitmap);

		//recycle the original bitmap
		bitmap.recycle();

		//After finishing everything, we destroy the Renderscript.
		rs.destroy();

		return outBitmap;


	}
}
