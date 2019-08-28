package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class VersionInfoModel {

    @Expose
    private int Code;// 版本号
    @Expose
    private String Feature;// 更新内容描述
    @Expose
    private String Version;// 版本名
    @Expose
    private String Url;// 下载链接

    public int getCode() {
	return Code;
    }

    public void setCode(int code) {
	Code = code;
    }

    public String getFeature() {
	return Feature;
    }

    public void setFeature(String feature) {
	Feature = feature;
    }

    public String getVersion() {
	return Version;
    }

    public void setVersion(String version) {
	Version = version;
    }

    public String getUrl() {
	return Url;
    }

    public void setUrl(String url) {
	Url = url;
    }
    
}
