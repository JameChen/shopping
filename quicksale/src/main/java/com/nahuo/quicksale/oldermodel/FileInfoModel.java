package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

public class FileInfoModel {

    @Expose
    private String FileName;
    @Expose
    private String FileUrl;

    public String getFileName() {
	return FileName;
    }

    public void setFileName(String fileName) {
	FileName = fileName;
    }

    public String getFileUrl() {
	return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
	FileUrl = fileUrl;
    }
}
