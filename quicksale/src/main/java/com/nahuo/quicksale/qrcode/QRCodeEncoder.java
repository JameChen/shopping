package com.nahuo.quicksale.qrcode;
/*
 * Copyright (C) 2008 ZXing authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.provider.ContactsContract;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public final class QRCodeEncoder {
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private int dimension = Integer.MIN_VALUE;
    private String contents = null;
    private String displayContents = null;
    private String title = null;
    private BarcodeFormat format = null;
    private boolean encoded = false;

    public QRCodeEncoder(String data, Bundle bundle, String type, String format, int dimension) {
        this.dimension = dimension;
        encoded = encodeContents(data, bundle, type, format);
    }

    public String getContents() {
        return contents;
    }

    public String getDisplayContents() {
        return displayContents;
    }

    public String getTitle() {
        return title;
    }

    private boolean encodeContents(String data, Bundle bundle, String type, String formatString) {
        // Default to QR_CODE if no format given.
        format = null;
        if (formatString != null) {
            try {
                format = BarcodeFormat.valueOf(formatString);
            } catch (IllegalArgumentException iae) {
                // Ignore it then
                iae.printStackTrace();
            }
        }
        if (format == null || format == BarcodeFormat.QR_CODE) {
            this.format = BarcodeFormat.QR_CODE;
            encodeQRCodeContents(data, bundle, type);
        } else if (data != null && data.length() > 0) {
            contents = data;
            displayContents = data;
            title = "Text";
        }
        return contents != null && contents.length() > 0;
    }

    private void encodeQRCodeContents(String data, Bundle bundle, String type) {
        if (type.equals(Contents.Type.TEXT)) {
            if (data != null && data.length() > 0) {
                contents = data;
                displayContents = data;
                title = "Text";
            }
        } else if (type.equals(Contents.Type.EMAIL)) {
            data = trim(data);
            if (data != null) {
                contents = "mailto:" + data;
                displayContents = data;
                title = "E-Mail";
            }
        } else if (type.equals(Contents.Type.PHONE)) {
            data = trim(data);
            if (data != null) {
                contents = "tel:" + data;
                displayContents = PhoneNumberUtils.formatNumber(data);
                title = "Phone";
            }
        } else if (type.equals(Contents.Type.SMS)) {
            data = trim(data);
            if (data != null) {
                contents = "sms:" + data;
                displayContents = PhoneNumberUtils.formatNumber(data);
                title = "SMS";
            }
        } else if (type.equals(Contents.Type.CONTACT)) {
            if (bundle != null) {
                StringBuilder newContents = new StringBuilder(100);
                StringBuilder newDisplayContents = new StringBuilder(100);

                newContents.append("MECARD:");

                String name = trim(bundle.getString(ContactsContract.Intents.Insert.NAME));
                if (name != null) {
                    newContents.append("N:").append(escapeMECARD(name)).append(';');
                    newDisplayContents.append(name);
                }

                String address = trim(bundle.getString(ContactsContract.Intents.Insert.POSTAL));
                if (address != null) {
                    newContents.append("ADR:").append(escapeMECARD(address)).append(';');
                    newDisplayContents.append('\n').append(address);
                }

                Collection<String> uniquePhones = new HashSet<String>(Contents.PHONE_KEYS.length);
                for (int x = 0; x < Contents.PHONE_KEYS.length; x++) {
                    String phone = trim(bundle.getString(Contents.PHONE_KEYS[x]));
                    if (phone != null) {
                        uniquePhones.add(phone);
                    }
                }
                for (String phone : uniquePhones) {
                    newContents.append("TEL:").append(escapeMECARD(phone)).append(';');
                    newDisplayContents.append('\n').append(PhoneNumberUtils.formatNumber(phone));
                }

                Collection<String> uniqueEmails = new HashSet<String>(Contents.EMAIL_KEYS.length);
                for (int x = 0; x < Contents.EMAIL_KEYS.length; x++) {
                    String email = trim(bundle.getString(Contents.EMAIL_KEYS[x]));
                    if (email != null) {
                        uniqueEmails.add(email);
                    }
                }
                for (String email : uniqueEmails) {
                    newContents.append("EMAIL:").append(escapeMECARD(email)).append(';');
                    newDisplayContents.append('\n').append(email);
                }

                String url = trim(bundle.getString(Contents.URL_KEY));
                if (url != null) {
                    // escapeMECARD(url) -> wrong escape e.g. http\://zxing.google.com
                    newContents.append("URL:").append(url).append(';');
                    newDisplayContents.append('\n').append(url);
                }

                String note = trim(bundle.getString(Contents.NOTE_KEY));
                if (note != null) {
                    newContents.append("NOTE:").append(escapeMECARD(note)).append(';');
                    newDisplayContents.append('\n').append(note);
                }

                // Make sure we've encoded at least one field.
                if (newDisplayContents.length() > 0) {
                    newContents.append(';');
                    contents = newContents.toString();
                    displayContents = newDisplayContents.toString();
                    title = "Contact";
                } else {
                    contents = null;
                    displayContents = null;
                }

            }
        } else if (type.equals(Contents.Type.LOCATION)) {
            if (bundle != null) {
                // These must use Bundle.getFloat(), not getDouble(), it's part of the API.
                float latitude = bundle.getFloat("LAT", Float.MAX_VALUE);
                float longitude = bundle.getFloat("LONG", Float.MAX_VALUE);
                if (latitude != Float.MAX_VALUE && longitude != Float.MAX_VALUE) {
                    contents = "geo:" + latitude + ',' + longitude;
                    displayContents = latitude + "," + longitude;
                    title = "Location";
                }
            }
        }
    }

    public Bitmap encodeAsBitmap(Bitmap logoBitmap) throws WriterException {
        if (!encoded) return null;

        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
//            hints = ;
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, format, dimension, dimension, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        if(logoBitmap != null){
            setQRCodeLogo(bitmap, logoBitmap);
        }
        return bitmap;
    }
    private static Bitmap setQRCodeLogo(Bitmap qr_img, Bitmap logo) {

        int logoWidth = qr_img.getWidth() / 5; // 设置logo图片宽度为二维码图片的五分之一

        int logoHeight = qr_img.getHeight() / 5; // 设置logo图片高度为二维码图片的五分之一
        logo=resizeImage(logo, logoWidth, logoHeight);
        int logoX = (qr_img.getWidth() - logoWidth) / 2; // 设置logo图片的位置,这里令其居中

        int logoY = (qr_img.getHeight() - logoHeight) / 2; // 设置logo图片的位置,这里令其居中
        int right = logoX + logoWidth;  
        int bottom = logoY + logoHeight; 
        
        Rect mRect = new Rect(logoX, logoY, right, bottom);//位置
        
        Canvas canvas = new Canvas(qr_img);
        int lineWidth = 5;
        int left = logoX - lineWidth;
        int top = logoY - lineWidth;
        int rectRight = logoX + logoWidth + lineWidth, rectBottom = logoY + logoHeight + lineWidth;
        Rect innerRect = new Rect(left, top, rectRight, rectBottom);
        
        Paint paint = new Paint();
        paint.setStyle(Style.FILL);  

        paint.setColor(Color.WHITE);  
        canvas.drawRect(innerRect, paint);
        
        Rect mRectLogo = new Rect(0, 0, logoWidth, logoHeight);
        canvas.drawBitmap(logo, mRectLogo, mRect, null);
        
        return qr_img;
    }
 // -------------------------------------------------------------------
    //通过传入位图,新的宽.高比进行位图的缩放操作  
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {  
      
            Bitmap BitmapOrg = bitmap;  
            int width = BitmapOrg.getWidth();  
            int height = BitmapOrg.getHeight();  
            int newWidth = w;  
            int newHeight = h;  
      
             
            float scaleWidth = ((float) newWidth) / width;  
            float scaleHeight = ((float) newHeight) / height;  
      
            Matrix matrix = new Matrix();  
            matrix.postScale(scaleWidth, scaleHeight);  
      
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,  
                    height, matrix, true);  
      
            return resizedBitmap;  
      
        }  
    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) { return "UTF-8"; }
        }
        return null;
    }

    private static String trim(String s) {
        if (s == null) { return null; }
        String result = s.trim();
        return result.length() == 0 ? null : result;
    }

    private static String escapeMECARD(String input) {
        if (input == null || (input.indexOf(':') < 0 && input.indexOf(';') < 0)) { return input; }
        int length = input.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            if (c == ':' || c == ';') {
                result.append('\\');
            }
            result.append(c);
        }
        return result.toString();
    }
}