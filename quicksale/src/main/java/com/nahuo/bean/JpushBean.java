package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jame on 2017/10/20.
 */

public class JpushBean {

    /**
     * content : 37475
     * typeid : 3大师的三
     */
    @Expose
    @SerializedName("content")
    private String content="";
    @Expose
    @SerializedName("typeid")
    private String typeid="";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
