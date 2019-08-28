package com.nahuo.quicksale.oldermodel;

import java.util.ArrayList;
import java.util.Iterator;


public class ImageDirModel {

    private String DirName = "";// 目录名
    private String FirstImgPath = "";// 第一个图片路径
    private ArrayList<String> Files = new ArrayList<String>();// 所有子图片路径
    
	public String getDirName() {
		return DirName;
	}
	public void setDirName(String dirName) {
		DirName = dirName;
	} 
	public String getFirstImgPath() {
		return FirstImgPath;
	}
	public void setFirstImgPath(String firstImgPath) {
		FirstImgPath = firstImgPath;
	}
	public ArrayList<String> getFiles() {
		return Files;
	}
	/**
	 * @description 根据文件类型过滤文件
	 * @created 2014-9-28 上午11:08:49
	 * @author ZZB
	 */
	public ArrayList<String> excludeFiles(String ...excludeFileTypes){
	    if(Files != null && Files.size() > 0){
	        Iterator<String> it = Files.iterator();
	        while(it.hasNext()){
	            String file = it.next();
	            if(file!=null && file.length()>0)
	            {
	            for(String type : excludeFileTypes){
	                if(file.endsWith("." + type)){
	                    it.remove();
	                    break;
	                }
	            }
	            }
	        }
	        return Files;
	    }else{
	        return Files;
	    }
        
	}
	public void setFiles(ArrayList<String> files) {
		Files = files;
	}

}
