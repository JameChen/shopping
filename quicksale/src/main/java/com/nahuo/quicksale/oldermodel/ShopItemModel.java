package com.nahuo.quicksale.oldermodel;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.nahuo.bean.ColorPicsBean;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.SystemGroupId;
import com.nahuo.quicksale.common.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 微铺款式实体类
 */
public class ShopItemModel implements Serializable {

    private static final long serialVersionUID = -3718423961923385896L;
    //totalSalecount

    @Expose
    private String RelatedGoodsText = "";
    /**
     * OriPrice : 80.00
     * Discount : 1.3折
     */
    @Expose
    @SerializedName("OriPrice")
    private String OriPrice = "";
    @Expose
    @SerializedName("Discount")
    private String Discount = "";
    /**
     * BuyerShopID : 673192
     * BuyerShopName :
     */
    @Expose
    @SerializedName("BuyerShopID")
    private long BuyerShopID;
    @Expose
    @SerializedName("BuyerShopName")
    private String BuyerShopName = "";

    public int getMyUserId() {
        return MyUserId;
    }

    public void setMyUserId(int myUserId) {
        MyUserId = myUserId;
    }

    private int MyUserId = 0;

    public String getRelatedGoodsText() {
        return RelatedGoodsText;
    }

    public void setRelatedGoodsText(String relatedGoodsText) {
        RelatedGoodsText = relatedGoodsText;
    }

    @Expose
    private int StallID;
    /**
     * ButtomSmallButtons : [{"title":"分享","action":"分享","isPoint":false,"isEnable":true,"type":"button"},{"title":"收藏","action":"收藏","isPoint":false,"isEnable":true,"type":"button"},{"title":"拿货车","action":"拿货车","isPoint":false,"isEnable":true,"type":"button"}]
     * ButtomBigButtons : [{"title":"我要拼单","action":"拼单","isPoint":true,"isEnable":true,"type":"button"}]
     * IsShowShareBtn : true
     */
    @Expose
    @SerializedName("IsShowShareBtn")
    private boolean IsShowShareBtn;
    @Expose
    @SerializedName("ButtomSmallButtons")
    private List<OrderButton> ButtomSmallButtons;
    @Expose
    @SerializedName("ButtomBigButtons")
    private List<OrderButton> ButtomBigButtons;

    public int getStallID() {
        return StallID;
    }

    public void setStallID(int stallID) {
        StallID = stallID;
    }

    @Expose
    private String MainColorPic = "";

    public String getMainColorPic() {
        return MainColorPic;
    }

    public void setMainColorPic(String mainColorPic) {
        MainColorPic = mainColorPic;
    }

    @Expose
    public boolean CanDownLoadPicAndVideo;
    public int ID;
    @SerializedName("UserID")
    @Expose
    private int userId;
    @Expose
    private int UploadID;// 数据库中上传的队列ID
    @Expose
    private int ParentID;// 父ID
    @Expose
    private int ItemID;// 商品款式ID
    @Expose
    private int AgentItemID;// 商品款式ID
    @Expose
    private String Name;// 商品名称，截取商品描述的前16个汉字作为"商品名称"
    @Expose
    private String Description;// 商品描述

    public String getDescriptionHead() {
        return DescriptionHead;
    }

    public void setDescriptionHead(String descriptionHead) {
        DescriptionHead = descriptionHead;
    }

    @Expose
    String DescriptionHead;//商品描述
    @Expose
    String DescriptionFoot;

    public String getDescriptionFoot() {
        return DescriptionFoot;
    }

    public void setDescriptionFoot(String descriptionFoot) {
        DescriptionFoot = descriptionFoot;
    }

    @SerializedName("IsFavorite")
    @Expose
    private boolean IsFavorite;                                  // 是否收藏
    @Expose
    private String Cat;// 商品分类
    //    @Expose
//    private int Style;// 分类
    @Expose
    private String Cover;// 主图
    @Expose
    private String CreateDate;// 简历日期
    @Expose
    private double Weight;// 重量
    @Expose
    private String[] Images;// 附图列表

    public String[] getHeadImages() {
        return HeadImages;
    }

    public void setHeadImages(String[] headImages) {
        HeadImages = headImages;
    }

    @Expose
    private String[] HeadImages;// 附图列表
    @Expose
    private double OrgPrice;
    @Expose
    private double Price;// 单价
    @Expose
    private String ItemStatu;

    public String getItemStatu() {
        return ItemStatu;
    }

    public void setItemStatu(String itemStatu) {
        ItemStatu = itemStatu;
    }

    @Expose
    private double RetailPrice;// 零售价
    @Expose
    private boolean SupplierIsUnifiedRetailPrice;//供货商是否统一零售价
    @Expose
    private List<ProductModel> Products;// 商品详细属性（颜色尺码规格）
    @Expose
    private List<Group> Groups;
    @Expose
    private List<ItemTagModel> Tags;
    private List<ColorPicsBean> colorPicsBeanList = new ArrayList<>();

    public List<ColorPicsBean> getColorPicsBeanList() {
        return colorPicsBeanList;
    }

    public void setColorPicsBeanList(List<ColorPicsBean> colorPicsBeanList) {
        this.colorPicsBeanList = colorPicsBeanList;
    }

    @Expose
    public List<OrderButton> Buttons;
    @Expose
    private boolean IsOnly4Agent;//false为所有人可见，true为指定代理可见
    @Expose
    private int ItemSourceType;//1是自己的，2是转发的，3是复制的
    @Expose
    private int SourceID;//复制商品的原ID

    public String getReplenishmentRemark() {
        return ReplenishmentRemark;
    }

    public void setReplenishmentRemark(String replenishmentRemark) {
        ReplenishmentRemark = replenishmentRemark;
    }

    @Expose
    private int DisplayStatuID;
    @Expose
    private String DisplayStatu = "新款";


    public String getMiniUrl() {
        return "pages/pinhuo/itemdetail?id=" + String.valueOf(ItemID) + "&key=" + String.valueOf(userId) + "&tag=android";
    }


    public int getDisplayStatuID() {
        return DisplayStatuID;
    }

    public void setDisplayStatuID(int displayStatuID) {
        DisplayStatuID = displayStatuID;
    }

    public String getDisplayStatu() {
        return DisplayStatu;
    }

    public void setDisplayStatu(String displayStatu) {
        DisplayStatu = displayStatu;
    }

    @Expose
    private String ReplenishmentRemark;//补货描述

    @Expose
    private int ApplyStatuID;// 申请状态,申请中：1，拒绝：2，接受：3
    @Expose
    @SerializedName("Intro")
    private String intro;//商品简介
    private String groupIds;

    private String OldImages = "";// 修改状态下，不需要修改的图片原地址
    private boolean IsAdd;// 是否是新增商品

    private int uploadCounter;//上传的次数
    private boolean hasNotified;//是否已经刷新页面
    @Expose
    @SerializedName("MyItemCopyType")
    private int itemCopyType;
    @Expose
    @SerializedName("Cat2")
    private ItemCategory4PC itemCat4PC;
    @Expose
    @SerializedName("Styles2")
    private List<CustomModel> itemStyle4PC;
    @Expose
    @SerializedName("Attrs")
    private List<CustomModel> itemAttrs;//属性物价商品等
    @Expose
    @SerializedName("ShopCats")
    private List<CustomModel> shopCats;//店铺分类
    @Expose
    @SerializedName("IsTop")
    private boolean isTop;//是否置顶
    @Expose
    @SerializedName("Videos")
    private List<String> Videos;
    @Expose
    @SerializedName("Propertys")
    private List<PropertysBeanX> Propertys;

    public List<PropertysBeanX> getPropertys() {
        return Propertys;
    }

    public void setPropertys(List<PropertysBeanX> Propertys) {
        this.Propertys = Propertys;
    }

    @Expose
    @SerializedName("RelatedGoods")
    private List<RelatedGoodsBean> RelatedGoods;

    public List<RelatedGoodsBean> getRelatedGoods() {
        return RelatedGoods;
    }

    public void setRelatedGoods(List<RelatedGoodsBean> RelatedGoods) {
        this.RelatedGoods = RelatedGoods;
    }

    public boolean isIsShowShareBtn() {
        return IsShowShareBtn;
    }

    public void setIsShowShareBtn(boolean IsShowShareBtn) {
        this.IsShowShareBtn = IsShowShareBtn;
    }

    public List<OrderButton> getButtomSmallButtons() {
        return ButtomSmallButtons;
    }

    public void setButtomSmallButtons(List<OrderButton> ButtomSmallButtons) {
        this.ButtomSmallButtons = ButtomSmallButtons;
    }

    public List<OrderButton> getButtomBigButtons() {
        return ButtomBigButtons;
    }

    public void setButtomBigButtons(List<OrderButton> ButtomBigButtons) {
        this.ButtomBigButtons = ButtomBigButtons;
    }

    public String getOriPrice() {
        return OriPrice;
    }

    public void setOriPrice(String OriPrice) {
        this.OriPrice = OriPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String Discount) {
        this.Discount = Discount;
    }

    public long getBuyerShopID() {
        return BuyerShopID;
    }

    public void setBuyerShopID(long BuyerShopID) {
        this.BuyerShopID = BuyerShopID;
    }

    public String getBuyerShopName() {
        return BuyerShopName;
    }

    public void setBuyerShopName(String BuyerShopName) {
        this.BuyerShopName = BuyerShopName;
    }

    public static class RelatedGoodsBean implements Serializable {
        private static final long serialVersionUID = 4516760675859545793L;
        /**
         * ID : 1014224
         * QsID : 0
         * Name : dkkffkfkfkfkfkfkfk
         * Cover : upyun:nahuo-img-server://33306/item/1505444482.jpg
         * Price : 5.23
         */
        @Expose
        @SerializedName("ID")
        private int ID;
        @Expose
        @SerializedName("QsID")
        private int QsID;
        @Expose
        @SerializedName("Name")
        private String Name;
        @Expose
        @SerializedName("Cover")
        private String Cover;
        @Expose
        @SerializedName("Price")
        private double Price;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getQsID() {
            return QsID;
        }

        public void setQsID(int QsID) {
            this.QsID = QsID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String Cover) {
            this.Cover = Cover;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }
    }

    public static class PropertysBeanX implements Serializable {
        private static final long serialVersionUID = 1276651597954356359L;
        /**
         * Name : 材质
         * SelectedID : 3
         * Propertys : [{"ID":3,"Name":"棉麻"},{"ID":2,"Name":"雪纺"},{"ID":4,"Name":"丝绸"}]
         */
        @Expose
        @SerializedName("Name")
        private String Name;
        @Expose
        @SerializedName("SelectedID")
        private int SelectedID;
        @Expose
        @SerializedName("Propertys")
        private List<PropertysBean> Propertys;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getSelectedID() {
            return SelectedID;
        }

        public void setSelectedID(int SelectedID) {
            this.SelectedID = SelectedID;
        }

        public List<PropertysBean> getPropertys() {
            return Propertys;
        }

        public void setPropertys(List<PropertysBean> Propertys) {
            this.Propertys = Propertys;
        }

        public static class PropertysBean implements Serializable {
            private static final long serialVersionUID = 1809630458830170495L;
            /**
             * ID : 3
             * Name : 棉麻
             */
            @Expose
            @SerializedName("ID")
            private int ID;
            @Expose
            @SerializedName("Name")
            private String Name;

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }
        }
    }

    public List<String> getVideos() {
        return Videos;
    }

    public void setVideos(List<String> Videos) {
        this.Videos = Videos;
    }

    public List<OrderButton> getButtons() {
        return Buttons;
    }

    public void setButtons(List<OrderButton> buttons) {
        Buttons = buttons;
    }

    public QsScheduleModel getSchedule() {
        return schedule;
    }

    public void setSchedule(QsScheduleModel schedule) {
        this.schedule = schedule;
    }

    @Expose
    private QsScheduleModel schedule;

    private String userName;

    private String uploadStatus = Const.UploadStatus.UPLOAD_WAIT;//上传状态
    private int uploadedNum;
    private String uploadFailedMsg;
    private String uniqueTag;//本地唯一tag:userid+time millis

    public void updateUploadProgress() {
        uploadedNum++;
    }

    public float getUploadProgress() {
        float totalNum = Images.length + 1;
        return uploadedNum / totalNum;
    }

    public String getUploadProgressStr() {
        float progress = getUploadProgress();
        DecimalFormat df = new DecimalFormat("#0");
        String str = df.format(progress * 100);
        return str;
    }

    public ArrayList<ItemShopCategory> getItemShopCats() {
        if (shopCats == null) {
            return null;
        } else {
            ArrayList<ItemShopCategory> cats = new ArrayList<ItemShopCategory>();
            for (CustomModel cat : shopCats) {
                ItemShopCategory c = new ItemShopCategory();
                c.setId(cat.getId());
                c.setName(cat.getName());
                cats.add(c);
            }
            return cats;
        }
    }

    public List<CustomModel> getShopCats() {
        return shopCats;
    }

    public void setShopCats(List<CustomModel> shopCats) {
        this.shopCats = shopCats;
    }

    public void setShopCatsByItemShopCategory(List<ItemShopCategory> shopCats) {
        if (shopCats == null) {
            return;
        }
        this.shopCats = new ArrayList<CustomModel>();
        for (ItemShopCategory cat : shopCats) {
            this.shopCats.add(new CustomModel(cat.getId(), cat.getName()));
        }
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }


    public List<CustomModel> getItemAttrs() {
        return itemAttrs;
    }

    public void setItemAttrs(List<CustomModel> itemAttrs) {
        this.itemAttrs = itemAttrs;
    }

    /**
     * @description 是否是特价商品
     * @created 2015-3-19 下午6:12:47
     * @author ZZB
     */
    public boolean isOnSale() {
        String attrsStr = getItemAttrsStr();
        return StringUtils.contains(attrsStr, "1", ",");
    }

    public String getShopCatsStr() {
        if (shopCats == null) {
            return "";
        } else {
            String str = "";
            for (CustomModel cat : shopCats) {
                str = StringUtils.append(str, cat.getId() + "", ",");
            }
            return str;
        }
    }

    public String getItemAttrsStr() {
        if (itemAttrs == null) {
            return "";
        } else {
            String str = "";
            for (CustomModel attr : itemAttrs) {
                str = StringUtils.append(str, attr.getId() + "", ",");
            }
            return str;
        }
    }

    public static class Group implements Serializable {
        private static final long serialVersionUID = -5541170849258449853L;
        @Expose
        private int ID;
        @Expose
        private String Name;

        public int getID() {
            return ID;
        }

        public void setID(int iD) {
            ID = iD;
        }

        public String getName() {
            return Name.replace("'", "");
        }

        public void setName(String name) {
            Name = name;
        }

    }

    public static class ItemTagModel implements Serializable {
        private static final long serialVersionUID = -5541170849258449854L;
        @Expose
        private int ID;
        @Expose
        private String Name;

        public int getID() {
            return ID;
        }

        public void setID(int iD) {
            ID = iD;
        }

        public String getName() {
            return Name.replace("'", "");
        }

        public void setName(String name) {
            Name = name;
        }

    }


    public List<CustomModel> getItemStyle4PC() {
        return itemStyle4PC;
    }

    public void setItemStyle4PC(List<CustomModel> itemStyle4PC) {
        this.itemStyle4PC = itemStyle4PC;
    }

    public ItemCategory4PC getItemCat4PC() {
        return itemCat4PC;
    }

    public void setItemCat4PC(ItemCategory4PC itemCat4PC) {
        this.itemCat4PC = itemCat4PC;
    }

    public int getItemSourceType() {
        return ItemSourceType;
    }

    public void setItemSourceType(int itemSourceType) {
        ItemSourceType = itemSourceType;
    }

    public int getSourceID() {
        return SourceID;
    }

    public void setSourceID(int sourceID) {
        SourceID = sourceID;
    }

    /**
     * Description:获取分组名称字符串
     * 2014-7-21下午6:20:48
     *
     * @author ZZB
     */
    public String getGroupNamesFromGroups() {
        if (!IsOnly4Agent) {
            return "公开";
        } else {
            if (Groups != null && Groups.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Group g : Groups) {
                    sb.append(g.getName()).append(",");
                }
                return StringUtils.deleteEndStr(sb.toString(), ",");
            } else {
                return "所有代理";
            }
        }

    }

    /**
     * Description:获取分组id字符串
     * 2014-7-21下午6:20:38
     *
     * @author ZZB
     */
    public String getGroupIdsFromGropus() {
        if (!IsOnly4Agent) {
            return SystemGroupId.ALL_PPL + "";
        }
        if (Groups != null && Groups.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Group g : Groups) {
                sb.append(g.getID()).append(",");
            }
            return StringUtils.deleteEndStr(sb.toString(), ",");
        } else {
            return SystemGroupId.ALL_AGENT + "";
        }
    }

    public List<Group> getGroups() {
        return Groups;
    }

    public void setGroups(List<Group> groups) {
        Groups = groups;
    }

//    public String getOldImages() {
//        return OldImages;
//    }

    public void setOldImages(String oldImages) {
        OldImages = oldImages;
    }

    public boolean isIsAdd() {
        return IsAdd;
    }

    public void setIsAdd(boolean isAdd) {
        IsAdd = isAdd;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public double getWeight() {
        return Weight;
    }

    public void setWeight(double weight) {
        Weight = weight;
    }

    public int getUploadID() {
        return UploadID;
    }

    public void setUploadID(int uploadID) {
        UploadID = uploadID;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public int getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(int agentItemID) {
        AgentItemID = agentItemID;
    }

    public int getApplyStatuID() {
        return ApplyStatuID;
    }

    public void setApplyStatuID(int applyStatuID) {
        ApplyStatuID = applyStatuID;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public String getName() {
        return Name.replace("'", "");
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        String returnDescription = Description;
        // 从description中移除掉之前添加的imgs标签【等待修改商品功能开放之后取消注释即可】
//        if (returnDescription.contains("<div id=beginAppImgInsertTag ></div>")) {
//            String endStr = "<div id=endAppImgInsertTag ></div>";
//            returnDescription = returnDescription.substring(0,
//                    returnDescription.indexOf("<div id=beginAppImgInsertTag ></div>"))
//                    + returnDescription.substring(Description.indexOf(endStr) + endStr.length());
//        }
//        if (returnDescription.contains("<div id=\"beginAppImgInsertTag\" ></div>")) {
//            String endStr = "<div id=\"endAppImgInsertTag\" ></div>";
//            returnDescription = returnDescription.substring(0,
//                    returnDescription.indexOf("<div id=\"beginAppImgInsertTag\" ></div>"))
//                    + returnDescription.substring(Description.indexOf(endStr) + endStr.length());
//        }
//        if (returnDescription.contains("<div id=\"beginAppImgInsertTag />")) {
//            String endStr = "<div id=endAppImgInsertTag />";
//            returnDescription = returnDescription.substring(0,
//                    returnDescription.indexOf("<div id=beginAppImgInsertTag />"))
//                    + returnDescription.substring(Description.indexOf(endStr) + endStr.length());
//        }

        return returnDescription;
    }

    public String getDescriptionFull() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCat() {
        return Cat;
    }

    public void setCat(String cat) {
        Cat = cat;
    }


    public String getCover() {
        return Cover;
    }

    public void setCover(String coverImg) {
        Cover = coverImg;
    }

    public String[] getImages() {
        return Images;
    }

    public String getImagesJsonStr() {
        String imgs = "";
        for (String img_Str : Images) {
            imgs += img_Str + Const.PIC_SEPERATOR;
        }
        return imgs;
    }

    public void setImages(String[] images) {
        Images = images;
    }

    public void setImagesJson(String images) {
        Images = images.split(Const.PIC_SEPERATOR);
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        RetailPrice = retailPrice;
    }

    public List<ProductModel> getProducts() {
        return Products;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

    // 取出product的json字符串格式
    public String getProductsJsonStr() {
        // 商品规格Json
        String products = "";
        for (ProductModel product : Products) {
            products += "{'Color':'" + product.getColor() + "',";
            products += "'Size':'" + product.getSize() + "',";
            products += "'Stock':" + product.getStock() + ",";
            products += "'Price':" + product.getPrice() + ",";
            products += "'Cover':'" + product.getCover() + "'},";
        }
        if (products.length() > 0) {
            products = products.substring(0, products.length() - 1);
        }
        products = "[" + products + "]";

        return products;
    }

    public void setProducts(List<ProductModel> products) {
        Products = products;
    }

    public void setProductsJson(String products) {
        try {
            Products = GsonHelper.jsonToObject(products, new TypeToken<List<ProductModel>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            Products = new ArrayList<ProductModel>();
        }
    }

    public String getUrl() {
        return Const.ITEM_NAHUO  + ItemID;
    }

    public String getTuanPiItemUrl() {
        return Const.ITEM_NAHUO + ItemID;
    }

    public boolean getIsOnly4Agent() {
        return IsOnly4Agent;
    }

    public void setIsOnly4Agent(boolean isOnly4Agent) {
        IsOnly4Agent = isOnly4Agent;
    }

    public boolean getSupplierIsUnifiedRetailPrice() {
        return SupplierIsUnifiedRetailPrice;
    }

    public void setSupplierIsUnifiedRetailPrice(boolean supplierIsUnifiedRetailPrice) {
        SupplierIsUnifiedRetailPrice = supplierIsUnifiedRetailPrice;
    }

    public int getUploadCounter() {
        return uploadCounter;
    }

    public void setUploadCounter(int uploadCounter) {
        this.uploadCounter = uploadCounter;
    }

    public boolean isHasNotified() {
        return hasNotified;
    }

    public void setHasNotified(boolean hasNotified) {
        this.hasNotified = hasNotified;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    /**
     * @description 如果intro为空返回name
     * @created 2014-11-10 下午6:05:10
     * @author ZZB
     */
    public String getIntroOrName() {
        return TextUtils.isEmpty(intro) ? Name : intro;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemCopyType() {
        return itemCopyType;
    }

    public void setItemCopyType(int itemCopyType) {
        this.itemCopyType = itemCopyType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getUploadFailedMsg() {
        return uploadFailedMsg;
    }

    public void setUploadFailedMsg(String uploadFailedMsg) {
        this.uploadFailedMsg = uploadFailedMsg;
    }

    public String getUniqueTag() {
        return uniqueTag;
    }

    public void setUniqueTag(String uniqueTag) {
        this.uniqueTag = uniqueTag;
    }

    public double getOrgPrice() {
        return OrgPrice;
    }

    public void setOrgPrice(double orgPrice) {
        OrgPrice = orgPrice;
    }

    public String getGroupIdsFromGroups() {
        if (Groups == null || Groups.size() == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Group g : Groups) {
                sb.append(g.getID()).append(",");
            }
            return StringUtils.deleteEndStr(sb.toString(), ",");
        }
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.IsFavorite = isFavorite;
    }

    public List<ItemTagModel> getTags() {
        return Tags;
    }

    public void setTags(List<ItemTagModel> tags) {
        Tags = tags;
    }

    @SerializedName("Activity")
    @Expose
    private ActivityBean Activity;

    public ActivityBean getActivity() {
        return Activity;
    }

    public void setActivity(ActivityBean Activity) {
        this.Activity = Activity;
    }

    public static class ActivityBean implements Serializable {

        private static final long serialVersionUID = 2822959612353250895L;
        /**
         * StartTime : 2017-03-28 08:00:00
         * EndTime : 2017-03-29 07:00:00
         * IsStart : true
         * ChengTuanCount : 5
         * TransCount : 5
         */
        @SerializedName("ShowCoinPayIcon")
        @Expose
        private boolean ShowCoinPayIcon;

        public boolean isShowCoinPayIcon() {
            return ShowCoinPayIcon;
        }

        public void setShowCoinPayIcon(boolean showCoinPayIcon) {
            ShowCoinPayIcon = showCoinPayIcon;
        }

        @SerializedName("StartTime")
        @Expose
        private String StartTime;
        @SerializedName("EndTime")
        @Expose
        private String EndTime;
        @SerializedName("IsStart")
        @Expose
        private boolean IsStart;
        @SerializedName("ChengTuanCount")
        @Expose
        private int ChengTuanCount;
        @SerializedName("TransCount")
        @Expose
        private int TransCount;

        public int getTotalSaleCount() {
            return TotalSaleCount;
        }

        public void setTotalSaleCount(int totalSaleCount) {
            TotalSaleCount = totalSaleCount;
        }

        @SerializedName("TotalSaleCount")
        @Expose
        private int TotalSaleCount;

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public boolean isIsStart() {
            return IsStart;
        }

        public void setIsStart(boolean IsStart) {
            this.IsStart = IsStart;
        }

        public int getChengTuanCount() {
            return ChengTuanCount;
        }

        public void setChengTuanCount(int ChengTuanCount) {
            this.ChengTuanCount = ChengTuanCount;
        }

        public int getTransCount() {
            return TransCount;
        }

        public void setTransCount(int TransCount) {
            this.TransCount = TransCount;
        }
    }
}
