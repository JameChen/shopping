package com.nahuo.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.quicksale.oldermodel.BannerAdModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jame on 2018/1/24.
 */

public class AdBean implements Serializable {
    private static final long serialVersionUID = 4349574270954315056L;
    @SerializedName("ADList")
    @Expose
    ArrayList<BannerAdModel> bannerAdModelList;

    public ArrayList<BannerAdModel> getBannerAdModelList() {
        return bannerAdModelList;
    }

    public void setBannerAdModelList(ArrayList<BannerAdModel> bannerAdModelList) {
        this.bannerAdModelList = bannerAdModelList;
    }

    /**
     * CustomHtml : <p>

     <input type="button" value="充值" onclick="javascript:GoToRecharge();" />
     <input type="button" value="商品详细" onclick="javascript:GoToItemDetail('7007061','27379');" />
     <input type="button" value="场次列表" onclick="javascript:GoToQsList('27379');" />
     </p>
     */
    @Expose
    @SerializedName("CustomHtml")
    private String CustomHtml="";

    public String getCustomHtml() {
        return CustomHtml;
    }

    public void setCustomHtml(String CustomHtml) {
        this.CustomHtml = CustomHtml;
    }
}
