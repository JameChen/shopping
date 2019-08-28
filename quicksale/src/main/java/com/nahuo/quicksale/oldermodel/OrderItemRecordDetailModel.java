package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class OrderItemRecordDetailModel implements Serializable {

    private static final long serialVersionUID = -2888648509672857565L;
    
    @Expose
    private String content;
    @Expose
    private String createtime;
    @Expose
    private String id;
    @Expose
    private String userid;
    @Expose
    private String username;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
