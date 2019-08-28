package com.nahuo.quicksale.oldermodel;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nahuo.bean.FollowsBean;
import com.nahuo.bean.SortMenusBean;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.utils.TimeUtils;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.ShopItemModel.Group;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 微店商品管理款式实体类
 */
public class ShopItemListModel implements Serializable {
    /**
     * "CreateDate": "2017-06-01 07:28:29",
     * "ID": 6593706,
     * "ItemID": 2824846,
     * "Name": "CA 【炫丽】819 新款棉麻套装女夏收腰无袖不规则上衣 不规则短裤女两件套潮（按上衣分色） PL06.01",
     * "Cover": "upyun:nahuo-img-server://357075/item/1496099656.jpg",
     * "Intro": "CA 【炫丽】819 新款棉麻套装女夏收腰无袖不规则上衣+不规则短裤女两件套潮（按上衣分色） PL06.01",
     * "UserID": 1009824,
     * "ShopName": "跑量优选",
     * "UserName": "跑量优选",
     * "IsSource": true,
     * "AgentPrice": "65.49",
     * "Images": [
     * "upyun:nahuo-img-server://357075/item/1496099656.jpg",
     * "upyun:nahuo-img-server://357075/item/1496099657.jpg",
     * "upyun:nahuo-img-server://357075/item/1496099658.jpg",
     * "upyun:nahuo-img-server://357075/item/1496099659.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057160.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057161.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057162.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057163.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057234.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057235.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057236.jpg",
     * "upyun:nahuo-img-server://357075/item/1496057237.jpg"
     * ],
     * "ChengTuanCount": 1,
     * "DealCount": 24,
     * "IsStart": true,
     * "QsID": 11980
     */
    public final static int TYPE_SHOP = 1, TYPE_TITLE = 2, TYPE_NEW_OLDER_ITEM = 3,TYPE_SORT_MENUS=4,TYPE_EMPTY_MID=5,TYPE_MARIN_VIEW=6;
    public final static int LAYOUT_DIRECTION_LEFT = 1, LAYOUT_DIRECTION_RIGHT = 2;
    private int item_layout_type;
    private int item_layout_direction;
    @SerializedName("ShowCoinPayIcon")
    @Expose
    public boolean ShowCoinPayIcon;
    @Expose
    @SerializedName("IsShowStatuIcon")
    public boolean IsShowStatuIcon=true;
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
    public boolean sortType_isNew;
    public String getOriPrice() {
        return OriPrice;
    }
    private List<SortMenusBean> SortMenus;

    public List<SortMenusBean> getSortMenus() {
        return SortMenus;
    }

    public void setSortMenus(List<SortMenusBean> sortMenus) {
        SortMenus = sortMenus;
    }

    public void setOriPrice(String oriPrice) {
        OriPrice = oriPrice;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public boolean isShowCoinPayIcon() {
        return ShowCoinPayIcon;
    }

    public void setShowCoinPayIcon(boolean showCoinPayIcon) {
        ShowCoinPayIcon = showCoinPayIcon;
    }

    public int getItem_layout_direction() {
        return item_layout_direction;
    }

    public void setItem_layout_direction(int item_layout_direction) {
        this.item_layout_direction = item_layout_direction;
    }

    private String part2title="";
    private String part1title="";

    public int getShowTitleType() {
        return showTitleType;
    }

    public void setShowTitleType(int showTitleType) {
        this.showTitleType = showTitleType;
    }

    private int showTitleType;
    public static int Show_All_Title=0;
    public static int Show_Older_Title=1;
    public String getPart1title() {
        return part1title;
    }
    private String empty_txt="";

    public String getEmpty_txt() {
        return empty_txt;
    }

    public void setEmpty_txt(String empty_txt) {
        this.empty_txt = empty_txt;
    }

    public void setPart1title(String part1title) {
        this.part1title = part1title;
    }

    public String getPart2title() {
        return part2title;
    }

    public void setPart2title(String part2title) {
        this.part2title = part2title;
    }

    public FollowsBean getFollowsBean() {
        return followsBean;
    }

    public void setFollowsBean(FollowsBean followsBean) {
        this.followsBean = followsBean;
    }

    private FollowsBean followsBean=null;
    public int getItem_layout_type() {
        return item_layout_type;
    }

    public void setItem_layout_type(int item_layout_type) {
        this.item_layout_type = item_layout_type;
    }
    public boolean isPassItem = false;
    @Expose
    public boolean HasVideo;
    @Expose
    int AgentItemID;
    @Expose
    boolean IsSaleOut = false;

    public boolean isSaleOut() {
        return IsSaleOut;
    }

    public void setSaleOut(boolean saleOut) {
        IsSaleOut = saleOut;
    }

    public int getAgentItemID() {
        return AgentItemID;
    }

    public void setAgentItemID(int agentItemID) {
        AgentItemID = agentItemID;
    }

    @Expose
    public String Title;
    @Expose
    private boolean IsStart;
    @Expose
    public String ShopName;
    @Expose
    public String ShopUserName;
    private static final long serialVersionUID = 7427104386583994172L;
    @Expose
    private int ParentID;                               // 父ID
    @Expose
    private int QsID;                                     // 商品款式ID
    @Expose
    private int TotalQty;                                     // 商品总销量
    @Expose
    private int ID;                                     // 商品款式ID
    @Expose
    private int ItemID;                                 // 商品款式ID
    @Expose
    private String Name;                                   // 商品名称
    @Expose
    private double Price;                                  // 单价
    @Expose
    private double AgentPrice;                                  // 团批价格
    @Expose
    private boolean IsFavorite;                                  // 是否收藏
    @Expose
    private int DisplayStatuID;
    @Expose
    private String DisplayStatu;

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
    private double RetailPrice;                            // 零售价
    @Expose
    private double MyPrice;                                // 代理单价
    @Expose
    private double SupplierPrice;                          // 顶级供货商的批发价
    @Expose
    private double SupplierRetailPrice;                    // 顶级供货商的零售价
    @Expose
    private double MyRetailPrice;                          // 我的零售价格，没有则=-1
    @Expose
    private double MyRetailPriceDefaultRate;               // 我的零售价加价费率，没有则为-1
    @Expose
    private boolean IsSupplierIsUnifiedRetailPrice;         // 款式是否允许修改零售价
    @Expose
    private int MyID;                                   // 代理ID
    @Expose
    private String ItemUrl;                                // 分享URL
    @Expose
    private String CreateDate;                             // 创建时间
    @Expose
    private int StatuID;                                // 状态，新建=0，已上架=1，已下架=2，已删除=3
    @Expose
    private int MyStatuID;                              // 状态，未转发=-1，新建=0，已上架=1，已下架=2，已删除=3
    @Expose
    private String Statu;                                  // 状态
    @Expose
    private String Cover;
    @Expose
    private String UserName;
    @Expose
    private String[] Images;
    @Expose
    private int UserID;
    @Expose
    private boolean IsSource;
    @Expose
    private double OrgPrice;
    @Expose
    private boolean IsUploaded = true;                // 默认是网络数据，本地的数据需要手动置这个标志
    @Expose
    private int UploadID;                               // 数据库中上传的队列ID
    @Expose
    private boolean ShowLocalImg = false;               // 款式图片没有获取到的时候，是否显示本地图片的标志

    private boolean IsAdd;                                  // 是否是新增商品
    @Expose
    private List<Group> Groups;                                 // 商品分组

    private boolean isOnly4Agent;
    @Expose
    private int ApplyStatuID;                           // 申请状态,关注：0，申请中：1，拒绝：2，接受：3
    @Expose
    @SerializedName("Intro")
    private String intro;
    @Expose
    private int ItemSourceType;                         // 1是自己的，2是转发的，3是复制的
    @Expose
    private int SourceID;                               // 复制商品的原ID
    @Expose
    private int ChengTuanCount;                               // 团批成团数量

    @Expose
    private int DealCount;                               // 团批已成交数量


    @Expose
    @SerializedName("Shop")
    private ItemShopInfo[] itemShopInfo;

    private String groupIdsJson;
    private String groupNamesJson;
    private int uploadCounter;
    private boolean isUploading;                            // 是否正在上传中
    private boolean isCheck;

    //是否结束
    public boolean isStart() {
        return IsStart;
    }

    public void setIsStart(boolean isStart) {
        IsStart = isStart;
    }

    //是否复制
    @Expose
    public boolean IsCopy;

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

    public int getMyStatuID() {
        return MyStatuID;
    }

    public void setMyStatuID(int myStatuID) {
        MyStatuID = myStatuID;
    }

    public boolean isUploadFailed() {
        return uploadCounter >= Const.UPLOAD_MAX_COUNTER;
    }

    public void setUploadCounter(int uploadCounter) {
        this.uploadCounter = uploadCounter;
    }

    public double getSupplierPrice() {
        return SupplierPrice;
    }

    public void setSupplierPrice(double supplierPrice) {
        SupplierPrice = supplierPrice;
    }

    public double getSupplierRetailPrice() {
        return SupplierRetailPrice;
    }

    public void setSupplierRetailPrice(double supplierRetailPrice) {
        SupplierRetailPrice = supplierRetailPrice;
    }

    public double getMyRetailPrice() {
        return MyRetailPrice;
    }

    public void setMyRetailPrice(double myRetailPrice) {
        MyRetailPrice = myRetailPrice;
    }

    public double getMyRetailPriceDefaultRate() {
        return MyRetailPriceDefaultRate;
    }

    public void setMyRetailPriceDefaultRate(double myRetailPriceDefaultRate) {
        MyRetailPriceDefaultRate = myRetailPriceDefaultRate;
    }

    public boolean isIsSupplierIsUnifiedRetailPrice() {
        return IsSupplierIsUnifiedRetailPrice;
    }

    public void setIsSupplierIsUnifiedRetailPrice(boolean isSupplierIsUnifiedRetailPrice) {
        IsSupplierIsUnifiedRetailPrice = isSupplierIsUnifiedRetailPrice;
    }

    /**
     * Description:从json串返回的Groups拼groupIds,由逗号隔开 2014-7-22 上午11:03:01
     *
     * @author ZZB
     */
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

    /**
     * Description:从json串返回的Groups拼groupNames,由逗号隔开 2014-7-22 上午11:17:55
     *
     * @author ZZB
     */
    public String getGroupNamesFromGroups() {
        if (Groups == null || Groups.size() == 0) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Group g : Groups) {
                sb.append(g.getName()).append(",");
            }
            return StringUtils.deleteEndStr(sb.toString(), ",");
        }
    }

    /**
     * @description 如果intro为空返回name
     * @created 2014-11-10 下午6:05:10
     * @author ZZB
     */
    public String getIntroOrName() {
        return TextUtils.isEmpty(intro) ? Name : intro;
    }

    public List<Group> getGroups() {
        return Groups;
    }

    public void setGroups(List<Group> groups) {
        Groups = groups;
    }

    public boolean isIsAdd() {
        return IsAdd;
    }

    public void setIsAdd(boolean isAdd) {
        IsAdd = isAdd;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public boolean GetShowLocalImg() {
        return ShowLocalImg;
    }

    public void setShowLocalImg(boolean showLocalImg) {
        ShowLocalImg = showLocalImg;
    }

    public int getUploadID() {
        return UploadID;
    }

    public void setUploadID(int uploadID) {
        UploadID = uploadID;
    }

    public boolean GetIsUploaded() {
        return IsUploaded;
    }

    public void setIsUploaded(boolean isUploaded) {
        IsUploaded = isUploaded;
    }

//    public String getParentURL() {
//        return "http://item.weipushop.com/wap/wpitem/" + String.valueOf(ParentID);
//    }

    public int getParentID() {
        return ParentID;
    }

    public int getApplyStatuID() {
        return ApplyStatuID;
    }

    public void setApplyStatuID(int applyStatuID) {
        ApplyStatuID = applyStatuID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public double getOrgPrice() {
        return OrgPrice;
    }

    public void setOrgPrice(double orgPrice) {
        OrgPrice = orgPrice;
    }

    public double getRetailPrice() {
        return RetailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        RetailPrice = retailPrice;
    }

    public boolean GetIsSource() {
        return IsSource;
    }

    public void setIsSource(boolean isSource) {
        IsSource = isSource;
    }

    public double getMyPrice() {
        return MyPrice;
    }

    public void setMyPrice(double myPrice) {
        MyPrice = myPrice;
    }

    public int getUserid() {
        return UserID;
    }

    public void setUserid(int userid) {
        this.UserID = userid;
    }

    public String[] getImages() {
        return Images;
    }

    public void setImages(String[] images) {
        Images = images;
    }

    public void setImagesJson(String images) {
        Images = images.split(Const.PIC_SEPERATOR);
    }

    public String getImagesJsonStr() {
        String imgs = "";
        for (String img_Str : Images) {
            imgs += img_Str + Const.PIC_SEPERATOR;
        }
        return imgs;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getCover() {
        return Cover;
    }

    public void setCover(String cover) {
        Cover = cover;
    }

    @Expose
    private String UserLogo;

    public String getUserLogo() {
        return UserLogo;
    }

    public void setUserLogo(String userLogo) {
        UserLogo = userLogo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getName() {
        return Name.replace("'", "");
    }

    // private static final String QQ_FACE_ARRAY =
    // "微笑,撇嘴,色,发呆,得意,流泪,害羞,闭嘴,睡,大哭,尴尬,发怒,调皮,呲牙,惊讶,难过,酷,冷汗,抓狂,吐,偷笑,愉快,白眼,傲慢,饥饿,困,惊恐,流汗,憨笑,"+
    // "悠闲,奋斗,咒骂,疑问,嘘,晕,疯了,衰,骷髅,敲打,再见,擦汗,抠鼻,鼓掌,糗大了,坏笑,左哼哼,右哼哼,哈欠,鄙视,委屈,快哭了,阴险,亲亲,吓,可怜,菜刀,西瓜,啤酒,篮球,乒乓,咖啡,饭,猪头,玫瑰,凋谢,嘴唇,"+
    // "爱心,心碎,蛋糕,闪电,炸弹,刀,足球,瓢虫,便便,月亮,太阳,礼物,拥抱,强,弱,握手,胜利,抱拳,勾引,拳头,差劲,爱你,NO,OK,爱情,飞吻,跳跳,发抖,怄火,转圈,磕头,回头,跳绳,挥手,激动,街舞,献吻,"+
    // "左太极,右太极";
    private static final List<String> FACE_LIST = Arrays.asList(new String[]{"[微笑]", "[撇嘴]", "[色]", "[发呆]", "[得意]",
            "[流泪]", "[害羞]", "[闭嘴]", "[睡]", "[大哭]", "[尴尬]", "[发怒]", "[调皮]", "[呲牙]", "[惊讶]", "[难过]", "[酷]", "[冷汗]",
            "[抓狂]", "[吐]", "[偷笑]", "[愉快]", "[白眼]", "[傲慢]", "[饥饿]", "[困]", "[惊恐]", "[流汗]", "[憨笑]", "[悠闲]", "[奋斗]",
            "[咒骂]", "[疑问]", "[嘘]", "[晕]", "[疯了]", "[衰]", "[骷髅]", "[敲打]", "[再见]", "[擦汗]", "[抠鼻]", "[鼓掌]", "[糗大了]",
            "[坏笑]", "[左哼哼]", "[右哼哼]", "[哈欠]", "[鄙视]", "[委屈]", "[快哭了]", "[阴险]", "[亲亲]", "[吓]", "[可怜]", "[菜刀]", "[西瓜]",
            "[啤酒]", "[篮球]", "[乒乓]", "[咖啡]", "[饭]", "[猪头]", "[玫瑰]", "[凋谢]", "[嘴唇]", "[爱心]", "[心碎]", "[蛋糕]", "[闪电]",
            "[炸弹]", "[刀]", "[足球]", "[瓢虫]", "[便便]", "[月亮]", "[太阳]", "[礼物]", "[拥抱]", "[强]", "[弱]", "[握手]", "[胜利]",
            "[抱拳]", "[勾引]", "[拳头]", "[差劲]", "[爱你]", "[NO]", "[OK]", "[爱情]", "[飞吻]", "[跳跳]", "[发抖]", "[怄火]", "[转圈]",
            "[磕头]", "[回头]", "[跳绳]", "[挥手]", "[激动]", "[街舞]", "[献吻]", "[左太极]", "[右太极]"});

    // private static final List<String> FACE_LIST = Arrays.asList(QQ_FACE_ARRAY.split(","));
    public static Spanned getTextHtml(String normalText, Context context, Html.ImageGetter imageGetter) {
        if (TextUtils.isEmpty(normalText)) {
            return new SpannedString("");
        }
        // / 优化
        String formattedText = normalText;
        if (normalText.contains("[") && normalText.contains("]")) {
            for (String face : FACE_LIST) {
                if (formattedText.contains("[") && formattedText.contains("]")) {
                    if (formattedText.contains(face)) {
                        formattedText = formattedText.replace(
                                face,
                                "<img src=\""
                                        + context.getResources().getIdentifier(
                                        context.getPackageName() + ":drawable/qq_" + FACE_LIST.indexOf(face),
                                        null, null) + "\" />");
                    }
                } else {
                    break;
                }
            }
        }
        // 换行符转换
        formattedText = formattedText.replace("\n", "<br>");

        return Html.fromHtml(formattedText, imageGetter, null);
    }

    // char flagBegin = '[';
    // char flagEnd = ']';
    // private List<String> GetRichContextArray(String context) {
    //
    // char[] contextArray = context.toCharArray();
    // List<String> returnList = new ArrayList<String>();
    // returnList.add("");
    // int offset = 0;
    // String tempStr= "";
    // for (char c : contextArray) {
    // if (c == flagBegin)
    // {
    // returnList.add("");
    // }
    // if (returnList.size()>0)
    // {
    // tempStr = returnList.get(returnList.size()-1);
    // tempStr += c;
    // returnList.set(returnList.size()-1, tempStr);
    // }
    // if (c == flagEnd)
    // {
    // returnList.add("");
    // }
    // }
    // return returnList;
    // }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

//    public String getItemUrl() {
//        return "http://item.weipushop.com/wap/wpitem/" + String.valueOf(ID);
//    }

    public String getTuanPiItemUrl() {
        return Const.ITEM_NAHUO + String.valueOf(ID);
    }

    public String getMiniUrl() {
        return "pages/pinhuo/itemdetail?ShareUserID="+ BWApplication.getUserID()+"id=" + String.valueOf(ID)+"&key="+ SpManager.getUserId(BWApplication.getInstance())+"&tag=android";
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public String getCreateDateStr() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public int getStatuID() {
        return StatuID;
    }

    public void setStatuID(int statuID) {
        StatuID = statuID;
    }

    public String getStatu() {
        return Statu;
    }

    public void setStatu(String statu) {
        Statu = statu;
    }
    // public String getTimeTitle() {
    // 每次使用都创建SimpleDateFormat很耗性能，特别是在ListView的getView里，建议到外部解析，只创建一个SimpleDateFormat
    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // int day = 0, month = 0;
    // try {
    // java.util.Date olderDate = df.parse(CreateDate);
    //
    // day = olderDate.getDate();
    // month = olderDate.getMonth();
    //
    // } catch (ParseException e) {
    // e.printStackTrace();
    // }
    //
    // return month + "@" + day;
    // }

    public String getShowTime() {
        return FunctionHelper.getFriendlyTime(CreateDate);

    }

    public int getMyID() {
        return MyID;
    }

    public void setMyID(int myID) {
        MyID = myID;
    }

    public String getGroupIdsJson() {
        return groupIdsJson;
    }

    public void setGroupIdsJson(String groupIdsJson) {
        this.groupIdsJson = groupIdsJson;
    }

    public String getGroupNamesJson() {
        return groupNamesJson;
    }

    public void setGroupNamesJson(String groupNamesJson) {
        this.groupNamesJson = groupNamesJson;
    }

    public boolean isOnly4Agent() {
        return isOnly4Agent;
    }

    public void setOnly4Agent(boolean isOnly4Agent) {
        this.isOnly4Agent = isOnly4Agent;
    }

    public static UpdateItem toUpdateItem(ShopItemListModel item) {
        UpdateItem updateItem = new UpdateItem(item.getName(), item.getID(), item.getOrgPrice() + "",
                item.getRetailPrice() + "");
        updateItem.setIntro(item.getIntro());
        updateItem.mGroupIds = item.getGroupIdsFromGroups();
        updateItem.mGroupNames = item.getGroupNamesFromGroups();
        updateItem.agentPrice = item.getPrice() + "";
        updateItem.isOnly4Agent = item.isOnly4Agent();
        updateItem.myItemId = item.getMyID();
        return updateItem;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public ItemShopInfo[] getItemShopInfo() {
        return itemShopInfo;
    }

    public void setItemShopInfo(ItemShopInfo[] itemShopInfo) {
        this.itemShopInfo = itemShopInfo;
    }

    public boolean isUploading() {
        if (!IsUploaded) {// 自己上传
            isUploading = true;
        } else if (IsSource && IsAdd) {// 修改
            isUploading = true;
        } else {
            isUploading = false;
        }
        return isUploading;
    }

    public long getLongDate() {
        return TimeUtils.timeStampToMillis(CreateDate);
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public int getChengTuanCount() {
        return ChengTuanCount;
    }

    public void setChengTuanCount(int chengTuanCount) {
        ChengTuanCount = chengTuanCount;
    }

    public int getDealCount() {
        return DealCount;
    }

    public void setDealCount(int dealCount) {
        DealCount = dealCount;
    }

    public double getAgentPrice() {
        return AgentPrice;
    }

    public void setAgentPrice(double agentPrice) {
        AgentPrice = agentPrice;
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        IsFavorite = isFavorite;
    }

    public int getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(int totalQty) {
        TotalQty = totalQty;
    }

    public int getQsID() {
        return QsID;
    }

    public void setQsID(int qsID) {
        QsID = qsID;
    }

    public String getShopUserName() {
        return ShopUserName;
    }

    public void setShopUserName(String shopUserName) {
        ShopUserName = shopUserName;
    }

    @Expose
    private ShopItemListInfoModel Info;

    public class ShopItemListInfoModel {
        private String AppCover;

        private String Description;

        private int ChengTuanCount;

        private int ID;

        private boolean IsStart;

        private String Name;

        private String PCCover;

        private String StartTime;

        private String ToTime;

        private String Url;

        private int ShopID;

        private int ShopUserID;

        private int LimitPoint;

        private boolean LimitShopAuth;

        public void setAppCover(String AppCover) {
            this.AppCover = AppCover;
        }

        public String getAppCover() {
            return this.AppCover;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }

        public String getDescription() {
            return this.Description;
        }

        public void setChengTuanCount(int ChengTuanCount) {
            this.ChengTuanCount = ChengTuanCount;
        }

        public int getChengTuanCount() {
            return this.ChengTuanCount;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public int getID() {
            return this.ID;
        }

        public void setIsStart(boolean IsStart) {
            this.IsStart = IsStart;
        }

        public boolean getIsStart() {
            return this.IsStart;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getName() {
            return this.Name;
        }

        public void setPCCover(String PCCover) {
            this.PCCover = PCCover;
        }

        public String getPCCover() {
            return this.PCCover;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getStartTime() {
            return this.StartTime;
        }

        public void setToTime(String ToTime) {
            this.ToTime = ToTime;
        }

        public String getToTime() {
            return this.ToTime;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public String getUrl() {
            return this.Url;
        }

        public void setShopID(int ShopID) {
            this.ShopID = ShopID;
        }

        public int getShopID() {
            return this.ShopID;
        }

        public void setShopUserID(int ShopUserID) {
            this.ShopUserID = ShopUserID;
        }

        public int getShopUserID() {
            return this.ShopUserID;
        }

        public void setLimitPoint(int LimitPoint) {
            this.LimitPoint = LimitPoint;
        }

        public int getLimitPoint() {
            return this.LimitPoint;
        }

        public void setLimitShopAuth(boolean LimitShopAuth) {
            this.LimitShopAuth = LimitShopAuth;
        }

        public boolean getLimitShopAuth() {
            return this.LimitShopAuth;
        }

    }

}
