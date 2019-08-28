package com.nahuo.quicksale.oldermodel.quicksale;

import com.nahuo.quicksale.oldermodel.BannerAdModel;

import java.util.ArrayList;

public class PinHuoNewResultModel_Map{
    public PinHuoNewResultModel model;

    public int CurrCategoryID;

    public ArrayList<BannerAdModel> adList;
    private String CustomHtml="";

    public String getCustomHtml() {
        return CustomHtml;
    }

    public void setCustomHtml(String CustomHtml) {
        this.CustomHtml = CustomHtml;
    }

}
