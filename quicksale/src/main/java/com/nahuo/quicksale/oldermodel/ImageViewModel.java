package com.nahuo.quicksale.oldermodel;

import java.io.Serializable;

public class ImageViewModel  implements Serializable{
    private static final long serialVersionUID = -7620435178023928259L;  
	
    private boolean isAlbumPhoto=false;
    private boolean isNewAdd;
    private boolean isCanEdit;
    private boolean isCanRemove;
    private String website;// 网络原图路径
    private String url; // 拍照或从相册选择图片的情况下，此url为缩略图文件路径，并非原始图片文件路径，否则是网络路径，且为缩略图路径
    private String originalUrl; // 拍照或从相册选择图片的情况下，此url为原始图片文件路径，否则为空
    private boolean isLoading;
    private UploadStatus uploadStatus = UploadStatus.NONE;

    public ImageViewModel() {
    }



    public ImageViewModel(String url, String originalUrl, boolean isNewAdd) {
        this.url = url;
        this.originalUrl = originalUrl;
        this.isNewAdd = isNewAdd;
    }
    //    private boolean isCover;
    
    public boolean isAlbumPhoto() {
    	return isAlbumPhoto;
        }

        public void setIsAlbumPhoto(boolean value) {
    	this.isAlbumPhoto = value;
        }
        public boolean getIsAlbumPhoto() {
        	return isAlbumPhoto;
            }
        
    public boolean isNewAdd() {
	return isNewAdd;
    }

    public void setNewAdd(boolean isNewAdd) {
	this.isNewAdd = isNewAdd;
    }

    public boolean isCanEdit() {
	return isCanEdit;
    }

    public void setCanEdit(boolean isCanEdit) {
	this.isCanEdit = isCanEdit;
    }
    public boolean isCanRemove() {
	return isCanRemove;
    }
    public void setCanRemove(boolean isCanRemove) {
	this.isCanRemove = isCanRemove;
    }

    public String getWebsite() {
	return website;
    }

    public void setWebsite(String website) {
	this.website = website;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getOriginalUrl() {
	return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
	this.originalUrl = originalUrl;
    }

    public boolean isLoading() {
	return isLoading;
    }

    public void setLoading(boolean isLoading) {
	this.isLoading = isLoading;
    }

    public UploadStatus getUploadStatus() {
	return uploadStatus;
    }

    /**
     * 设置图片上传状态：SUCCESS-上传成功，FAIL-上传失败
     * */
    public void setUploadStatus(UploadStatus uploadStatus) {
	this.uploadStatus = uploadStatus;
    }


    public enum UploadStatus {
	NONE, SUCCESS, FAIL
    }


}
