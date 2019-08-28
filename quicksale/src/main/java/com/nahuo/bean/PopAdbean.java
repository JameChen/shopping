package com.nahuo.bean;

import com.nahuo.quicksale.oldermodel.BannerAdModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jame on 2018/5/2.
 */

public class PopAdbean implements Serializable {
    private static final long serialVersionUID = 3022910825199677454L;
    List<BannerAdModel> bannerAdModelList;

    public List<BannerAdModel> getBannerAdModelList() {
        return bannerAdModelList;
    }
}
