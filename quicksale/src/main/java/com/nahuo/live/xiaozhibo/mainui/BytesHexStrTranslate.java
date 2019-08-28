package com.nahuo.live.xiaozhibo.mainui;

/**
 * Created by jame on 2019/4/23.
 */

public class BytesHexStrTranslate {
    public static String printHexBinary(byte[] bt_ary) throws Exception {
        StringBuilder sb = new StringBuilder();

        if (bt_ary != null)

            for (byte b : bt_ary) {

                sb.append(String.format("%02X", b));

            }

        return sb.toString();
    }


}
