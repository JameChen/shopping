package com.nahuo.library.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

public class SDCardHelper {

    private static final String TAG = "SDCardHelper";

    /**
     * 拷贝文件
     * 
     * @return boolean true-存在，false-不存在
     */
    public static void copyFile(String oldPath, String newPath) {   
        try {   
            int bytesum = 0;   
            int byteread = 0;   
            File oldfile = new File(oldPath);   
            if (oldfile.exists()) { //文件存在时   
                InputStream inStream = new FileInputStream(oldPath); //读入原文件   
                FileOutputStream fs = new FileOutputStream(newPath);   
                byte[] buffer = new byte[1444];   
                int length;   
                while ( (byteread = inStream.read(buffer)) != -1) {   
                    bytesum += byteread; //字节数 文件大小   
                    System.out.println(bytesum);   
                    fs.write(buffer, 0, byteread);   
                }   
                inStream.close();   
            }   
        }   
        catch (Exception e) {   
            System.out.println("复制单个文件操作出错");   
            e.printStackTrace();   
   
        }   
   
    }   
    
    /**
     * 验证SDCard是否存在
     * 
     * @return boolean true-存在，false-不存在
     */
    public static boolean IsSDCardExists() {
	String state = Environment.getExternalStorageState();
	if (state.equals(Environment.MEDIA_MOUNTED))
	    return true;
	return false;
    }

    /**
     * 获取SDCard根目录
     */
    public static String getSDCardRootDirectory() {
	String sdcard = null;
	if (IsSDCardExists()) {
	    sdcard = Environment.getExternalStorageDirectory().toString();
	    if (!Environment.getExternalStorageDirectory().canWrite()) {
		try {
		    Runtime.getRuntime().exec("chmod 777 " + sdcard);
		} catch (IOException ex) {
		    Log.e(TAG, "权限修改失败");
		    ex.printStackTrace();
		}
	    }
	}
	return sdcard;
    }

    /**
     * 获取DCIM文件目录
     * */
    public static String getDCIMDirectory() {
	String path = Environment.getExternalStoragePublicDirectory(
		Environment.DIRECTORY_DCIM).getPath();
	return path;
    }

    /**
     * 判断文件是否存在
     * 
     * @param filePath
     *            文件路径
     * @return boolean true-成功，false-失败
     */
    public static boolean checkFileExists(String filePath) {
	File file = new File(filePath);
	return file.exists();
    }

    /**
     * 在SD卡上创建目录
     * 
     * @param filePath
     *            文件夹路径
     * @return boolean true-成功，false-失败
     */
    public static boolean createDirectory(String filePath) {
	if (checkFileExists(filePath)) {
	    return true;
	}
	File dir = new File(filePath);
	boolean result = dir.mkdirs();
	return result;
    }

    /**
     * 在SD卡上创建文件
     * 
     * @param filePath
     *            文件路径
     * @return boolean true-成功，false-失败
     */
    public static boolean createFile(String filePath) throws IOException {
	if (checkFileExists(filePath)) {
	    return true;
	}
	File file = new File(filePath);
	return file.createNewFile();
    }

    /**
     * 读取文件，返回字节数组
     * 
     * @param filePath
     *            文件路径
     * */
    public static byte[] readFile(String filePath) throws Exception {
	byte[] bytes;
	FileInputStream inputStream = null;
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	try {
	    inputStream = new FileInputStream(filePath);
	    byte[] buffer = new byte[1024];
	    int length = 0;
	    while ((length = inputStream.read(buffer)) != -1) {
		outputStream.write(buffer, 0, length);
	    }
	    bytes = outputStream.toByteArray();
	} catch (Exception ex) {
	    throw ex;
	} finally {
	    if (inputStream != null) {
		inputStream.close();
	    }
	    outputStream.close();
	}
	return bytes;
    }

    /**
     * 删除文件目录
     * 
     * @param file
     *            文件路径或者文件夹路径
     * @return 返回处理结果：1001.文件目录不存在 1000.删除成功
     * */
    public static String deleteFolder(File file) throws Exception {
	String result = "";
	try {
	    if (!file.exists()) {
		result = "1001";
		return result;
	    } else {
		if (file.isFile()) {
		    file.delete();
		} else if (file.isDirectory()) {
		    File[] childFile = file.listFiles();
		    if (childFile == null || childFile.length == 0) {
			file.delete();
		    } else {
			for (File f : childFile) {
			    deleteFolder(f);
			}
			file.delete();
		    }
		}
	    }
	    result = "1000";
	} catch (Exception ex) {
	    throw ex;
	}
	return result;
    }
}
