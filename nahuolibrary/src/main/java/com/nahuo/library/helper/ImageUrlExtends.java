package com.nahuo.library.helper;

import android.content.Context;
import android.text.TextUtils;

import java.text.MessageFormat;

public class ImageUrlExtends {
    public static final String DOMAIN_BANWO_FILES="banwofiles-img.nahuo.com";
    public static final String DOMAIN_BANWO_IMG_SERVER="banwo-img.nahuo.com";
    public static final String DOMAIN_NAHUO_VIDEO_SERVER="video-img.nahuo.com";
    public static final String DOMAIN_PHITEM="phitem-img.nahuo.com";
    public static final String DOMAIN_NAHUO_IMG_SERVER="img4.nahuo.com";
    public static final String HTTP="http://";
    public static final String HTTP_BANWO_FILES=HTTP+DOMAIN_BANWO_FILES;
    public static final String HTTP_BANWO_IMG_SERVER=HTTP+DOMAIN_BANWO_IMG_SERVER;
    public static final String HTTP_NAHUO_VIDEO_SERVER=HTTP+DOMAIN_NAHUO_VIDEO_SERVER;
    public static final String HTTP_PHITEM=HTTP+DOMAIN_PHITEM;
    public static final String HTTP_NAHUO_IMG_SERVER=HTTP+DOMAIN_NAHUO_IMG_SERVER;
    /***
     * 替换 图片标签的src成缩略图的地址
     *	create by 陈智勇Mar 24, 2015-9:51:26 AM
     * @param html
     * @return
     */
    public static String replaceImgSrc(Context context, String html) {
        long time = System.currentTimeMillis();
        int start = 0;
        int end = 0;
        String startSplit = "src";
        String endSplit = "\"";
        String src = null;
        int type = getSuitImageType(context);
        String suitSrc = null;
        int count = 0;
        while (count++ < 100) {
            start = TextUtils.indexOf(html, startSplit, start);
            if (start >= 0) {
                start++;
                start = TextUtils.indexOf(html, endSplit, start);
                start++;
                end = TextUtils.indexOf(html, endSplit, start);
                end--;
                src = TextUtils.substring(html, start, end);
                suitSrc = ImageUrlExtends.getImageUrl(src, type);
                String tempStart = TextUtils.substring(html, 0, start);
                String tempEnd = TextUtils.substring(html, end, html.length());
                html = TextUtils.concat(tempStart, suitSrc, tempEnd).toString();
            } else {
                break;
            }
        }
        return html;
    }

    public static int getSuitImageType(Context context) {
        int type = 5;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        do {

            if (width < 200) {
                type = 6;
                break;
            }
            if (width < 220) {
                type = 8;
                break;
            }
            if (width < 320) {
                type = 10;
                break;
            }
            if (width < 500) {
                type = 14;
                break;
            }
            type = 18;
        } while (false);
        return type;
    }

    /**
     * 获取图片略缩图地址
     *
     * @param source 原图片路径
     * @param type   缩略图大小，从1-24
     */
    public static String getImageUrl(String source, int type) {

        if (TextUtils.isEmpty(source))
            return "";

        if (type < 1) {
            type = 1;
        } else if (type > 24) {
            type = 24;
        }

        if (source.contains("/Uploaded/")){
           source= source.replace("/Uploaded/","/Zoom/");
        }else if (source.contains("/Uploaded/")){
           source= source.replace("/uploaded/","/zoom/");
        }
        if (source.startsWith("/u")) {
            return MessageFormat.format("http://img4.nahuo.com{0}!{1}", source, convertImgRule(type, 0));
        } else if (source.contains("/shop/logo/uid/")) {
            return MessageFormat.format("{0}/{1}", source, convertImgRule(type, 4));
        } else if (source.startsWith("upyun:")) {
            String[] datas = source.split(":");
            if (datas == null || datas.length != 3)
                return source;
            if (datas[1].equals("nahuo-img-server")) {
                return MessageFormat.format("http://img4.nahuo.com{1}!{2}", datas[1], datas[2],
                        convertImgRule(type, 0));
            } else if (datas[1].equals("phitem")) {
                return MessageFormat.format("http://phitem-img.nahuo.com{1}!{2}", datas[1], datas[2],
                        convertImgRule(type, 1));
            } else if (datas[1].equals("banwo-files")) {
                return MessageFormat.format("http://banwofiles-img.nahuo.com{1}", datas[1], datas[2]
                        );
            } else if (datas[1].equals("banwo-img-server")) {
                return MessageFormat.format("http://banwo-img.nahuo.com{1}!{2}", datas[1], datas[2],
                        convertImgRule(type, 1));
            } else  if (datas[1].equals("item-img")){
                return MessageFormat.format("http://{0}.nahuo.com{1}!{2}", datas[1], datas[2],
                        convertImgRule(type, 1));
            }else  {
                return MessageFormat.format("http://{0}.nahuo.com{1}!{2}", datas[1], datas[2],
                        convertImgRule(type, 0));
            }
        } else if ((source.startsWith("http://nahuo-img-server.b0.upaiyun.com/") || source.startsWith("https://nahuo-img-server.b0.upaiyun.com/")) && source.indexOf("!thum.") < 0) {
            return MessageFormat.format("{0}!{1}", source, convertImgRule(type, 0));
        }else if ((source.startsWith("http://img4.nahuo.com/") || source.startsWith("https://img4.nahuo.com/")) && source.indexOf("!thum.") < 0) {
            return MessageFormat.format("{0}!{1}", source, convertImgRule(type, 0));
        }else if (source.startsWith("http://image10.nahuo.com") || source.startsWith("https://image10.nahuo.com")) {
            return MessageFormat.format("{0}/{1}.jpg?iscut=false", source, convertImgRule(type, 3));
        } else if (source.startsWith("http:") || source.startsWith("https:")) {
            return source;
        } else {
            return MessageFormat.format("http://img3.nahuo.com/{0}{1}.jpg", source, convertImgRule(type, 2));
        }
    }

    /**
     * 根据图片指定后缀，转换成需求的缩略图后缀
     *
     * @param size a的大小，1-24
     * @param type 类型，0为thum模式，1为a模式 2为-width-height模式 3为width/height模式  4为size模式
     */
    public static String convertImgRule(int size, int type) {

        if (type == 0 || type == 2 || type == 3 || type == 4) {
            String pxSize = "50";
            switch (size) {
                case 1:
                    pxSize = "50";
                    break;
                case 2:
                    pxSize = "80";
                    break;
                case 3:
                    pxSize = "112";
                    break;
                case 4:
                    pxSize = "125";
                    break;
                case 5:
                    pxSize = "140";
                    break;
                case 6:
                    pxSize = "160";
                    break;
                case 7:
                    pxSize = "200";
                    break;
                case 8:
                    pxSize = "200";
                    break;
                case 9:
                    pxSize = "220";
                    break;
                case 10:
                    pxSize = "220";
                    break;
                case 11:
                    pxSize = "320";
                    break;
                case 12:
                    pxSize = "320";
                    break;
                case 13:
                    pxSize = "320";
                    break;
                case 14:
                    pxSize = "320";
                    break;
                case 15:
                    pxSize = "500";
                    break;
                case 16:
                    pxSize = "500";
                    break;
                case 17:
                    pxSize = "500";
                    break;
                case 18:
                    pxSize = "600";
                    break;
                case 19:
                    pxSize = "800";
                    break;
                case 20:
                    pxSize = "800";
                    break;
                case 21:
                    pxSize = "1000";
                    break;
                case 22:
                    pxSize = "1000";
                    break;
                case 23:
                    pxSize = "1000";
                    break;
                case 24:
                    pxSize = "1000";
                    break;
                default:
                    pxSize = "1000";
                    break;
            }
            if (type == 0) {
                return "thum." + pxSize;
            } else if (type == 3) {
                return pxSize + "/" + pxSize;
            } else if (type == 4) {
                return pxSize;
            } else {
                return "-" + pxSize + "-" + pxSize;
            }
        } else {
            return "a" + size;
        }
    }

    /**
     * 获取图片的原图地址
     *
     * @param source 原图片路径
     */
    public static String getImageUrl(String source) {

        if (TextUtils.isEmpty(source))
            return "";

        if (source.startsWith("http:") || source.startsWith("https:")) {
            return source;
        } else if (source.startsWith("/u")) {
            return MessageFormat.format("http://img4.nahuo.com{0}", source);
        } else if (source.startsWith("upyun:")) {
            String[] datas = source.split(":");
            if (datas == null || datas.length != 3)
                return source;
            if (datas[1].equals("nahuo-img-server")) {
                return MessageFormat.format("http://img4.nahuo.com{1}", datas[1], datas[2]
                        );
            } else if (datas[1].equals("phitem")) {
                return MessageFormat.format("http://phitem-img.nahuo.com{1}", datas[1], datas[2]
                       );
            } else if (datas[1].equals("banwo-files")) {
                return MessageFormat.format("http://banwofiles-img.nahuo.com{1}", datas[1], datas[2]
                );
            } else if (datas[1].equals("banwo-img-server")) {
                return MessageFormat.format("http://banwo-img.nahuo.com{1}", datas[1], datas[2]
                        );
            } else {
                return MessageFormat.format("http://{0}.nahuo.com{1}", datas[1], datas[2]
                        );
            }

        } else {
            return MessageFormat.format("http://img3.nahuo.com/{0}.jpg", source);
        }
    }

//    public static String getLocalOrImageUrl(String source, int type) {
//
//        if (TextUtils.isEmpty(source))
//            return "";
//        if (source.startsWith("upyun:")) {
//            String[] datas = source.split(":");
//            if (datas == null || datas.length != 3)
//                return source;
//            return MessageFormat.format("http://{0}.b0.upaiyun.com{1}", datas[1], datas[2], convertImgRule(type, 0));
//        } else {
//            return source;
//        }
//    }
}
