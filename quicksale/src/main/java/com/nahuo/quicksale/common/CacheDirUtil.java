package com.nahuo.quicksale.common;


import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CacheDirUtil {

//	/****
//	 * 根据图片的地址获取webview缓存图片保存的地址
//	 *	create by 陈智勇2015-3-19下午1:34:02
//	 * @param url  图片的下载地址
//	 * @return
//	 */
//	public static String getWebViewCachePath(Context context , String url){
//		File webDBFile = context.getDatabasePath("webview.db") ;
//		Logger.e("web db2 = "+webDBFile.getAbsolutePath()) ;
//		if(!webDBFile.exists()){
//			webDBFile = context.getDir("webview", Context.MODE_PRIVATE) ;
//			webDBFile = new File(webDBFile , "Web Data");
//			try {
//				OutputStream out;
//				out = new FileOutputStream("/mnt/sdcard/db.db");
//				InputStream in = new FileInputStream(webDBFile) ;
//				byte []buf = new byte[in.available()] ;
//				in.read(buf) ;
//				in.close() ;
//				out.write(buf) ;
//				out.flush() ;
//				out.close() ;
//			} catch (FileNotFoundException e1) {
//				e1.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//
//			SQLiteDatabase db = null ;
//			try{
//				db = SQLiteDatabase.openDatabase(webDBFile.getAbsolutePath(), null	, 0) ;
//			}catch(SQLiteException e){
//				e.printStackTrace() ;
//			}
//			if(db!=null){
//				String path = getWebDBFilePath(db , url) ;
//				Logger.e("image path = "+path) ;
//			}
//		}
//		else{
//			SQLiteDatabase db = null ;
//			try{
//				db = SQLiteDatabase.openDatabase(webDBFile.getAbsolutePath(), null	, 0) ;
//			}catch(SQLiteException e){
//				e.printStackTrace() ;
//			}
//			if(db!=null){
//				String path = getWebDBFilePath(db , url) ;
//				Logger.e("image path = "+path) ;
//			}
//		}
//		return "" ;
//	}
//	private static String getWebDBFilePath(SQLiteDatabase db , String url){
//		String path = null ;
////		Cursor cursor = db.rawQuery("select filepath from cache.table where url = ?", new String[]{url}) ;
////		path = cursor.getString(0) ;
////		cursor.close() ;
//		return path ;
//	}
    /****
     * 返回制定目录名的缓冲目录
     *	create by 陈智勇2015-3-18上午9:20:47
     * @param context
     * @param name
     * @return
     */
    public static File getCacheDir(Context context , String name){
        File dir = null ;
        if(sdcardExist()){
            dir = context.getExternalFilesDir(name);

        }
        if(dir == null || !dir.exists()){
            dir = context.getDir(name, Context.MODE_PRIVATE) ;
        }
        return dir ;
    }
    /***
     * 获取app的缓存cache目录下的name文件
     * @param context
     * @param name
     * @return
     */
    public static File getCache(Context context , String name){
        File dir = null ;
        if(sdcardExist()){
            dir = context.getExternalFilesDir("cache");
        }
        if(dir == null || !dir.exists()){
            dir = context.getDir("cache", Context.MODE_PRIVATE) ;
        }
        return new File(dir , name ) ;
    }
    /***
     * 检查sdcard是否存在
     *	create by 陈智勇2015-3-16下午5:34:16
     * @return
     */
    public static boolean sdcardExist(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    /***
     * 缓存字符串到文件中
     *	create by 陈智勇2015-3-16下午5:37:06
     * @param saveFile
     * @param saveString
     */
    public static void saveString(File saveFile , String saveString){
        OutputStream out;



        try {
            out = new FileOutputStream(saveFile);

            out.write(saveString.getBytes());
            out.flush() ;
            out.close() ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /***
     * 从文件中读取字符串
     *	create by 陈智勇2015-3-16下午5:40:00
     * @param saveFile
     * @return
     */
    public static String readString(File saveFile){
        InputStream in;
        byte[] buf = null;
        try {
            in = new FileInputStream(saveFile);
            buf = new byte[in.available()] ;
            in.read(buf) ;
            in.close() ;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(buf == null){
            return "";
        }
        return new String(buf) ;
    }
}
