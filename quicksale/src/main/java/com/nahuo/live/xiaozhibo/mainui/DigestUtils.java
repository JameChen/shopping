package com.nahuo.live.xiaozhibo.mainui;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jame on 2019/4/24.
 */

public class DigestUtils {


    /**
     * sha256计算后进行16进制转换
     *
     * @param data 待计算的数据
     * @param encoding 编码
     * @return 计算结果
     */
    public static byte[] sha256X16(String data, String encoding) {
        byte[] bytes = sha256(data, encoding);
        StringBuilder sha256StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sha256StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            } else {
                sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        try {
            return sha256StrBuff.toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * sha256计算
     *
     * @param datas 待计算的数据
     * @param encoding  字符集编码
     * @return
     */
    public static byte[] sha256(String datas, String encoding) {
        try {
            return sha256(datas.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * sha256计算.
     * @return 计算结果
     */
    private static byte[] sha256(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }


    /**

     * 将加密后的字节数组转换成字符串

     *

     * @param b 字节数组

     * @return 字符串

     */

    private static String byteArrayToHexString(byte[] b) {

        StringBuilder hs = new StringBuilder();

        String stmp;

        for (int n = 0; b!=null && n < b.length; n++) {

            stmp = Integer.toHexString(b[n] & 0XFF);

            if (stmp.length() == 1)

                hs.append('0');

            hs.append(stmp);

        }

        return hs.toString().toLowerCase();

    }



    public static String sha256Hex(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = byteArrayToHexString(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String sha256Hex(byte[] bt) {
        MessageDigest md = null;
        String strDes = null;
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = byteArrayToHexString(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
