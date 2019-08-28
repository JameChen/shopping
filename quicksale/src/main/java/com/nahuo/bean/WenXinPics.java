package com.nahuo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2017/6/12.
 */

public class WenXinPics implements  Serializable {
    private static final long serialVersionUID = -7699744381820636797L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    String title="";
    List<String> images;
    int Type;
}
