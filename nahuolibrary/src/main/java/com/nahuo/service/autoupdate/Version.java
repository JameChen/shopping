package com.nahuo.service.autoupdate;

import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Version {

    /**
     * 版本号 e.g: 13
     */
    public int code;

    /**
     * 版本名 e.g: 1.0.9
     */
    public String name;

    /**
     * 此版本特性 e.g: Fixed bugs
     */
    public String feature;

    /***
     * 必须升级的版本号
     */
    public String destroyVersions;

    /**
     * 此版本APK下载地址
     */
    public String targetUrl;
    public String md5;

    public Version() {
        super();
    }

    public Version(int code, String name, String feature, String targetUrl, String destroyVersions) {
        this.code = code;
        this.name = name;
        this.feature = feature;
        this.targetUrl = targetUrl;
        this.destroyVersions = destroyVersions;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer("VERSION -> ");
        buffer.append("Code:").append(code).append(", ");
        buffer.append("Name:").append(name).append(", ");
        buffer.append("Feature:").append(feature).append(", ");
        buffer.append("TargetUrl:").append(targetUrl).append(", ");
        buffer.append("DestroyVersions:").append(destroyVersions);

        return buffer.toString();
    }

    public static Version parserVersion(String versionXML) {
        Version version = new Version();
        XmlPullParser parser;
        if (TextUtils.isEmpty(versionXML)) {
            return version;
        }

        try {
            parser = XmlPullParserFactory.newInstance()
                    .newPullParser();
            InputStream is = new ByteArrayInputStream(versionXML.getBytes());
            parser.setInput(is, "utf-8");
            // parser.setInput(new StringReader(versionXML));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("Code")) {
                            eventType = parser.next();
                            try {
                                version.code = Integer.valueOf(parser.getText());
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        } else if (parser.getName().equals("NewVersion")) {
                            eventType = parser.next();
                            version.name = parser.getText();
                        } else if (parser.getName().equals("Feature")) {
                            eventType = parser.next();
                            version.feature = parser.getText();
                        } else if (parser.getName().equals("Url")) {
                            eventType = parser.next();
                            version.targetUrl = parser.getText();
                        } else if (parser.getName().equals("MD5")) {
                            eventType = parser.next();
                            version.md5 = parser.getText();
                        }else if (parser.getName().equals("DestroyVersions")) {
                            eventType = parser.next();
                            version.destroyVersions = parser.getText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        parser = null;
        return version;
    }
}
