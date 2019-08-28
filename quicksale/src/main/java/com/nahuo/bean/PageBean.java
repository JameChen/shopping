package com.nahuo.bean;

import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.oldermodel.BGQDModel;

import java.util.List;

/**
 * Created by jame on 2018/5/29.
 */

public class PageBean {

    @SerializedName("PackageList")
    private List<BGQDModel> PackageList;

    public List<BGQDModel> getPackageList() {
        return PackageList;
    }

    public void setPackageList(List<BGQDModel> PackageList) {
        this.PackageList = PackageList;
    }


}
