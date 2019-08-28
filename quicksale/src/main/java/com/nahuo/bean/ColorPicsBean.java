package com.nahuo.bean;

import java.io.Serializable;

/**
 * Created by jame on 2018/1/4.
 */

public class ColorPicsBean implements Serializable {
    private static final long serialVersionUID = -7944050404478599081L;
    /**
     * Color :空为主色卡
     * Url : upyun:banwo-img:/shop/121/43433480d.jpg
     */
    private boolean is_upload=true;

    public boolean is_upload() {
        return is_upload;
    }

    public void setIs_upload(boolean is_upload) {
        this.is_upload = is_upload;
    }

    private String Color="";
    private String Url="";

    public String getColor() {
        return Color;
    }

    public void setColor(String Color) {
        this.Color = Color;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }
}
