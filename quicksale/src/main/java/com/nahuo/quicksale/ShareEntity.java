package com.nahuo.quicksale;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.oldermodel.quicksale.RecommendModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.nahuo.library.helper.ImageUrlExtends.getImageUrl;

/**
 * @author ZZB
 * @description 分享实体
 * @created 2014-11-21 下午5:08:00
 */
public class ShareEntity implements Serializable {

    private static final long serialVersionUID = 3530661148678110264L;
    private String title="";
    private String summary="";
    private String targetUrl="";
    private String imgUrl="";
    private String appName="";
    private String extra="";
    private byte[] thumData;
    private List<String> share_images = new ArrayList<>();
    private String miniAppUrl="";

    public String getTitle_des() {
        return title_des;
    }

    public void setTitle_des(String title_des) {
        this.title_des = title_des;
    }

    private String title_des="";
    public String getMiniAppUrl() {
        return miniAppUrl;
    }

    public void setMiniAppUrl(String miniAppUrl) {
        this.miniAppUrl = miniAppUrl;
    }

    public ShareEntity() {
        setMiniAppUrl("");
    }

    public List<String> getImages() {
        return share_images;
    }

    public void setImages(String[] images) {
        share_images.clear();
        int url_index = 0;
        if (images != null && images.length > 0) {
            for (String url : images) {
                if (!TextUtils.isEmpty(url)) {
                    String xurl = ImageUrlExtends.getImageUrl(url, 21);
                    if (xurl.contains("!")) {
                        String fileName = xurl.substring(0, xurl.lastIndexOf("!"));
                        share_images.add(fileName);
                    }else {
                        share_images.add(url);
                    }
                    url_index++;
//                    if (url_index > 8) {
//                        break;
//                    }
                }
            }
        }

    }

    //微信小程序160
    public ShareEntity(ShopItemModel item) {
        setTitle(item.getName());
        setSummary("亲，这款不错，档口畅销热款，一件也是档口价，一起拼不？");
        String tmpImgUrl = item.getImages()[0];
        String imageUrl = getImageUrl(tmpImgUrl,15);
        setImgUrl(imageUrl);
        setTargetUrl(item.getTuanPiItemUrl());
        setMiniAppUrl(item.getMiniUrl());
        setImages(item.getImages());
    }

    public ShareEntity(ShopItemListModel item) {
        setTitle(item.getName());
        setSummary("亲，这款不错，档口畅销热款，一件也是档口价，一起拼不？");
        String tmpImgUrl = item.getImages()[0];
        String imageUrl = getImageUrl(tmpImgUrl,15);
        setImgUrl(imageUrl);
        setTargetUrl(item.getTuanPiItemUrl());
        setMiniAppUrl(item.getMiniUrl());


    }
    public ShareEntity(RecommendModel item) {
        if (item!=null) {

            String tmpImgUrl="",tuanPiItemUrl="";
          //  setSummary("亲，这款不错，档口畅销热款，一件也是档口价，一起拼不？");
            if (item.getInfo()!=null){
                setTitle(item.getInfo().getName());
                setSummary(item.getInfo().getDescription());
                tmpImgUrl=item.getInfo().getCover();
                tuanPiItemUrl="item.nahuo.com/items/"+item.getInfo().getID();
                setMiniAppUrl("pages/pinhuo/pinhuodetail?ShareUserID="+ BWApplication.getUserID()+"&qsid="+item.getInfo().getID()+"&key="+item.getMyUserId()+"&tag=android");
            }else {
                setTitle("亲，这款不错，档口畅销热款，一件也是档口价，一起拼不？");
                setSummary("亲，这款不错，档口畅销热款，一件也是档口价，一起拼不？");
                setMiniAppUrl("pages/pinhuo/pinhuodetail?ShareUserID="+ BWApplication.getUserID()+"&qsid=0"+"&key="+item.getMyUserId()+"&tag=android");
                tuanPiItemUrl="item.nahuo.com/items/"+0;
            }
            String imageUrl = getImageUrl(tmpImgUrl, 15);
            setImgUrl(imageUrl);
            setTargetUrl(tuanPiItemUrl);

        }
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public byte[] getThumData() {
        return thumData;
    }

    public void setThumData(byte[] thumData) {
        this.thumData = thumData;
    }

    public void setThumData(Bitmap bitmap) {
        try {
            this.thumData = Utils.bitmapToByteArray(bitmap, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
