package com.nahuo.quicksale.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.graphics.Bitmap;

import com.nahuo.library.helper.SDCardHelper;
import com.nahuo.quicksale.BuildConfig;

public class FileUtils {

    public static final String ERROR_LOG_PATH = SDCardHelper.getSDCardRootDirectory() + "/quicksale/" + BuildConfig.APPLICATION_ID + "/logs";

    public static String readFile(File file){
        if(file == null || !file.exists()){
            return "";
        }
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String lineStr = "";
            // 一次读入一行，直到读入null为文件结束
            while ((lineStr = reader.readLine()) != null) {
                sb.append(lineStr).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }
    /**
     * @description 读取文件
     * @created 2015-4-2 下午2:29:56
     * @author ZZB
     */
    public static String readFile(String filePath) {
        File file = new File(filePath);
        return readFile(file);
    }

    /**
     * @description 写文件
     * @created 2015-1-5 上午9:24:43
     * @author ZZB
     */
    public static void writeFile(String content) {
        PrintWriter pw = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            String date = sdf.format(new Date());
            String folderPath = ERROR_LOG_PATH;
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String filePath = folderPath + "/" + date + "-log.txt";
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true),
                    "utf-8")), true);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = sdf1.format(new Date());
            pw.append("==================" + time + "==============================\r\n");
            pw.println(content);
            pw.flush();
            pw.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @description 把错误写到文件中
     * @created 2014-12-23 上午10:33:28
     * @author ZZB
     */
    public static void writeErrorToFile(Exception e) {
        PrintWriter pw = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            String date = sdf.format(new Date());
//            String folderPath = SDCardHelper.getSDCardRootDirectory() + "/weipu/logs";
            String folderPath = ERROR_LOG_PATH;
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            String filePath = folderPath + "/" + date + "-log.txt";
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true),
                    "utf-8")), true);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String time = sdf1.format(new Date());
            pw.append("==================" + time + "==============================\r\n");
            e.printStackTrace(pw);
            pw.flush();
            pw.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @description 保存bitmap
     * @created 2014-11-19 下午5:23:03
     * @author ZZB
     */
    public static void saveBitmap(String savePath, Bitmap bitmap) throws Exception {
        saveBitmap(savePath, bitmap, 100);
    }

    /**
     * @description 保存bitmap
     * @param quality 0-100, 100是压缩最好的质量
     * @created 2014-11-19 下午5:23:03
     * @author ZZB
     */
    public static void saveBitmap(String savePath, Bitmap bitmap, int quality) throws Exception {
        File f = new File(savePath);
        File parentFile = f.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fileOut = new FileOutputStream(f);
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, fileOut);
        fileOut.flush();
    }

}
