package com.nahuo.quicksale.common;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

/**
 * Description:调用系统相机拍照
 * 2014-7-28 上午10:12:21
 * @author ZZB
 */
public class SystemCameraHelper {

    private static final String TAG = SystemCameraHelper.class.getSimpleName();
    /** 大照片 */
    public static final int ACTION_TAKE_PHOTO_B = 1;
    /** 小照片 */
    public static final int ACTION_TAKE_PHOTO_S = 2;
    public static final int ACTION_TAKE_VIDEO = 3;
    

    private static final String BITMAP_STORATE_KEY = "view_bitmap";
    private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageview_visibility";

    private Context mContext;
    private ImageView mImageView;
    private Bitmap mImageBitmap;

    private static final String VIDEO_STORAGE_KEY = "view_video";
    private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY =
            "videoview_visibility";
    private VideoView mVideoView;
    private Uri mVideoUri;

    private String mCurrentPhotoPath;
    private String mAlbumName;

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    public SystemCameraHelper(Context context, String albumName){
        this.mContext = context;
        this.mAlbumName = albumName;
    }

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = getAlbumStorageDir(mAlbumName);
            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d(TAG, "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(TAG, "External storage is not mounted READ/WRITE.");
        }
        return storageDir;
    }

    private File getAlbumStorageDir(String albumName) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO ? new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName)
                : new File(Environment.getExternalStorageDirectory() + "/dcim/" + albumName);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {
        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();
        return f;
    }

    private void setPic() {
        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;// 这里是true,下面decode出来的Bitmap为null,避免OOM。只是把图片的宽高放Options里
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;// 这里是false,通过上面计算出来的inSampleSize，避免OOM,
                                             // 下面decode出来的Bitmap是可用的，
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        /* Associate the Bitmap to the ImageView */
        mImageView.setImageBitmap(bitmap);
        // mVideoUri = null;
        // mImageView.setVisibility(View.VISIBLE);
        // mVideoView.setVisibility(View.INVISIBLE);
    }

    /*
     * invoke the system's media scanner to add your photo to the Media
     * Provider's database, making it available in the Android Gallery
     * application and to other apps.
     */
    private void addPicToGallery() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        switch (actionCode) {
        case ACTION_TAKE_PHOTO_B:
            File f = null;

            try {
                f = setUpPhotoFile();
                mCurrentPhotoPath = f.getAbsolutePath();
                // The Android Camera application saves a full-size photo if you
                // give it a file to save into
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                mCurrentPhotoPath = null;
            }
            break;

        default:
            break;
        } // switch
        ((Activity) mContext).startActivityForResult(takePictureIntent, actionCode);
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        ((Activity) mContext).startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
    }

    /**
     * Description:onActivityResult时调用，显示拍完的照片
     * 2014-7-28 上午11:32:12
     * @author ZZB
     */
    public void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        mImageBitmap = (Bitmap) extras.get("data");
        mImageView.setImageBitmap(mImageBitmap);
        // mVideoUri = null;
        // mImageView.setVisibility(View.VISIBLE);
        // mVideoView.setVisibility(View.INVISIBLE);
    }
    /**
     * Description:onActivityResult时调用，显示拍完的照片
     * 2014-7-28 上午11:32:12
     * @author ZZB
     */
    public String handleBigCameraPhoto() {
        String tmpPath = mCurrentPhotoPath;
        if (mCurrentPhotoPath != null) {
            setPic();
            addPicToGallery();
            mCurrentPhotoPath = null;
        }
        return tmpPath;
    }
    /**
     * Description:onActivityResult时调用，显示拍完的照片
     * 2014-7-28 上午11:32:12
     * @author ZZB
     */
    public void handleCameraVideo(Intent intent) {
        mVideoUri = intent.getData();
        mVideoView.setVideoURI(mVideoUri);
        mImageBitmap = null;
        mVideoView.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
    }

    public void takeBigPic(ImageView imageView) {
        mImageView = imageView;
        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
    }

    public void takeSmallPic(ImageView imageView) {
        mImageView = imageView;
        dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
    }

    public void takeVideo(VideoView videoView) {
        mVideoView = videoView;
        dispatchTakeVideoIntent();
    }
    
    public static void displayImageView(ImageView imageView, String imageFilePath) {
        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        /* Get the size of the image */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;// 这里是true,下面decode出来的Bitmap为null,避免OOM。只是把图片的宽高放Options里
        BitmapFactory.decodeFile(imageFilePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        /* Set bitmap options to scale the image decode target */
        bmOptions.inJustDecodeBounds = false;// 这里是false,通过上面计算出来的inSampleSize，避免OOM,
                                             // 下面decode出来的Bitmap是可用的，
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);

        /* Associate the Bitmap to the ImageView */
        imageView.setImageBitmap(bitmap);
        // mVideoUri = null;
        // mImageView.setVisibility(View.VISIBLE);
        // mVideoView.setVisibility(View.INVISIBLE);
    }
}
