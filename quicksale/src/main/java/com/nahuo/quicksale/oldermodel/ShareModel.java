package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class ShareModel implements Serializable {

    private static final long serialVersionUID = -3718423961923385898L;

    @Expose
    private String url;
    @Expose
    private String name;
    @Expose
    private String price;
    @Expose
    private String img;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
    
    
    
    
}
