package com.nahuo.quicksale.oldermodel;

import java.util.List;

public class UploadItem {
    
    private String mGroupIds;
    private List<ColorSize> mColorSizes;
    private List<Pic> mPics;
    private String mPrice;
    private String mDesc;
    
    public static class ColorSize{
        
        public ColorSize(String color, String size, int qty) {
            super();
            mColor = color;
            mSize = size;
            mQty = qty;
        }
        public String mColor;
        public String mSize;
        public int mQty;
    }
    
    public static class Pic{
        public String mUrl;
        public boolean mIsCover;
    }

    public String getGroupIds() {
        return mGroupIds;
    }

    public void setGroupIds(String groupIds) {
        mGroupIds = groupIds;
    }

    public List<ColorSize> getColorSizes() {
        return mColorSizes;
    }

    public void setColorSizes(List<ColorSize> colorSizes) {
        mColorSizes = colorSizes;
    }

    public List<Pic> getPics() {
        return mPics;
    }

    public void setPics(List<Pic> pics) {
        mPics = pics;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    
    
}
