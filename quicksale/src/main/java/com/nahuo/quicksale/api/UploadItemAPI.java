package com.nahuo.quicksale.api;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.Const.SystemGroupId;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.ColorModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.nahuo.quicksale.oldermodel.SizeModel;
import com.nahuo.quicksale.upyun.api.UpYunAPI;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadItemAPI {

    private static final String TAG = "UploadItemAPI";
    private static UploadItemAPI instance = null;

    /**
     * 单例
     * */
    public static UploadItemAPI getInstance() {
        if (instance == null) {
            instance = new UploadItemAPI();
        }
        return instance;
    }

    /**
     * 获取颜色列表
     * 
     * @author Chiva Liang
     * 
     * @param cookie
     *            cookie值
     * */
    public List<ColorModel> getColors(String cookie) throws Exception {
        List<ColorModel> colorList = null;
        try {
            String json = HttpUtils.httpPost("shop/shop/getcolors", new HashMap<String, String>(),
                    cookie);
            Log.i(TAG, "Json：" + json);
            colorList = GsonHelper.jsonToObject(json, new TypeToken<ArrayList<ColorModel>>() {
            });
        } catch (Exception ex) {
            Log.e(TAG,
                    MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getColors", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return colorList;
    }

    /**
     * 添加颜色
     * 
     * @author Chiva Liang
     * 
     * @param color
     *            颜色
     * @param cookie
     *            cookie值
     * @return ColorModel 颜色实体对象
     * */
    public ColorModel addColor(String color, String cookie) throws Exception {
        ColorModel cm = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("color", color);
            String json = HttpUtils.httpPost("shop/shop/addcolor", params, cookie);
            Log.i(TAG, "Json：" + json);
            cm = GsonHelper.jsonToObject(json, ColorModel.class);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "addColor", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return cm;
    }

    /**
     * 删除颜色
     * 
     * @author Chiva Liang
     * 
     * @param ids
     *            颜色ID，多ID情况下用英文逗号隔开
     * @param cookie
     *            cookie值
     * */
    public boolean deleteColors(String ids, String cookie) throws Exception {
        boolean result = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("ids", ids);
            String json = HttpUtils.httpPost("shop/shop/deletecolors", params, cookie);
            Log.i(TAG, "Json：" + json);
            result = true;
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "deleteColors",
                    ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

    /**
     * 获取尺码列表
     * 
     * @author Chiva Liang
     * 
     * @param cookie
     *            cookie值
     * */
    public List<SizeModel> getSizes(String cookie) throws Exception {
        List<SizeModel> sizeList = null;
        try {
            String json = HttpUtils.httpPost("shop/shop/getsizes", new HashMap<String, String>(),
                    cookie);
            Log.i(TAG, "Json：" + json);
            sizeList = GsonHelper.jsonToObject(json, new TypeToken<ArrayList<SizeModel>>() {
            });
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "getSizes", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return sizeList;
    }

    /**
     * 添加颜色
     * 
     * @author Chiva Liang
     * 
     * @param size
     *            尺码
     * @param cookie
     *            cookie值
     * @return SizeModel 尺码实体对象
     * */
    public SizeModel addSize(String size, String cookie) throws Exception {
        SizeModel cm = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("size", size);
            String json = HttpUtils.httpPost("shop/shop/addsize", params, cookie);
            Log.i(TAG, "Json：" + json);
            cm = GsonHelper.jsonToObject(json, SizeModel.class);
        } catch (Exception ex) {
            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "addSize", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return cm;
    }

    /**
     * 批量删除尺码
     * 
     * @author Chiva Liang
     * 
     * @param ids
     *            尺码ID，多ID情况下用英文逗号隔开
     * @param cookie
     *            cookie值
     * */
    public boolean deleteSizes(String ids, String cookie) throws Exception {
        boolean result = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("ids", ids);
            String json = HttpUtils.httpPost("shop/shop/deletesizes", params, cookie);
            Log.i(TAG, "Json：" + json);
            result = true;
        } catch (Exception ex) {
            Log.e(TAG,
                    MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "deleteSizes", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return result;
    }

   

    

    /**
     * 上传图片到服务器
     * 
     * @author Chiva Liang
     * 
     * @param shopId
     *            店铺ID
     * @param fileName
     *            文件名，上传到upyun的文件名
     * @param imgUrl
     *            本地图片文件路径
     * 
     * @return 返回图片在服务器上的路径
     * */
    public String uploadImage(String shopId, String fileName, String imgUrl) throws Exception {
        String serverPath = "";
        try {
            serverPath = UpYunAPI.uploadImage("item", shopId, fileName,
                    PublicData.UPYUN_BUCKET_ITEM, PublicData.UPYUN_API_KEY_ITEM, imgUrl);
        } catch (Exception ex) {
            Log.e(TAG,
                    MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "uploadImage", ex.getMessage()));
            ex.printStackTrace();
            throw ex;
        }
        return serverPath;
    }

//    /**
//     * 添加新商品款式资料
//     *
//     * @author Chiva Liang
//     *
//     * @param describ
//     *            描述，截取此描述的前16个汉字作为商品Title
//     * @param categoryId
//     *            分类ID
//     * @param price
//     *            单价
//     * @param coverImg
//     *            主图Url
//     * @param imgs
//     *            附图Url，多图情况下用英文逗号隔开
//     * @param products
//     *            商品详细属性（颜色尺码规格列表，Json格式）
//     * @param cookie
//     *            cookie值
//     * @return 返回商品ID
//     * */
//    public int addItem(ShopItemModel entity, String cookie) throws Exception {
//        int itemId = -1;
//        try {
//            if (entity.getImages() == null || entity.getImages().length == 0) {
//                throw new Exception("至少需要添加一张商品图片");
//            } else if (entity.getProducts() == null || entity.getProducts().size() == 0) {
//                throw new Exception("至少需要给商品设置一种规格");
//            }
//
//            // 商品图片Json
//            String images = "";
//            for (String imgUrl : entity.getImages()) {
//                images += "'" + imgUrl + "',";
//            }
//            if (images.length() > 0) {
//                images = images.substring(0, images.length() - 1);
//            }
//            images = "[" + images + "]";
//
//            // 商品规格Json
//            String products = "";
//            for (ProductModel product : entity.getProducts()) {
//                products += "{'Color':'" + product.getColor() + "',";
//                products += "'Size':'" + product.getSize() + "',";
//                products += "'Stock':" + product.getStock() + ",";
//                products += "'Price':" + product.getPrice() + "},";
//                // products += "'Cover':'" + product.getCover() + "'},";
//            }
//            if (products.length() > 0) {
//                products = products.substring(0, products.length() - 1);
//            }
//            products = "[" + products + "]";
//            // 分组json
//
//            // Styles
//            String styles = "[" + entity.getStyle() + "]";
//            //根据分组id判断是否只是代理可见
//            setIsOnly4Agent(entity);
//
//            String strSaveJson = "{";
//            strSaveJson += "'Tag':'" + entity.getUniqueTag() + "',";
//            strSaveJson += "'IsOnly4Agent':" + entity.getIsOnly4Agent() + ",";
//            strSaveJson += "'Name':'" + entity.getName().replace("'", "") + "',";// 对应db的 title
//            strSaveJson += "'intro':'" + entity.getIntro().replace("'", "")  + "',";//db的 intro
//            strSaveJson += "'Price':" + entity.getPrice() + ",";
//            strSaveJson += "'RetailPrice':" + entity.getRetailPrice() + ",";
//            strSaveJson += "'Cover':'" + entity.getCover() + "',";
//            strSaveJson += "'Cat':" + entity.getCat() + ",";
//            strSaveJson += "'Styles':" + styles + ",";
//            String imgHTMLStr = "";
//            // if (entity.getImages().length>5)
//            // {
//            imgHTMLStr = "<div id=\"beginAppImgInsertTag\" ></div>";
//
//            for (int i = 0; i < entity.getImages().length; i++) {
//                imgHTMLStr += "<img src=\"" + ImageUrlExtends.getImageUrl(entity.getImages()[i],24)
//                        + "\" />";
//                imgHTMLStr += "<br/>";
//            }
//            imgHTMLStr += "<div id=\"endAppImgInsertTag\" ></div>";
//            // }
//            strSaveJson += "'Description':'" + imgHTMLStr + "',";
//            strSaveJson += "'Images':" + images + ",";
//            strSaveJson += "'Groups':[" + entity.getGroupIds() + "],";
//            strSaveJson += "'ShopCats':[" + entity.getShopCatsStr() + "],";
//            strSaveJson += "'Attrs':[" + entity.getItemAttrsStr() + "],";
//            strSaveJson += "'IsTop':" + entity.isTop() + ",";
//            strSaveJson += "'Products':" + products + "}";
//            Log.d(TAG, "上传商品请求：" + strSaveJson);
//            String json = HttpsUtils.httpPost("shop/agent/additem", strSaveJson, cookie);
//            Log.i(TAG, "上传商品响应：" + json);
//            ShopItemModel shopItemModel = GsonHelper.jsonToObject(json, ShopItemModel.class);
//            itemId = shopItemModel.getAgentItemID();
//        } catch (Exception ex) {
//            Log.e(TAG, MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "addItem", ex.getMessage()));
//            ex.printStackTrace();
//            throw ex;
//        }
//        return itemId;
//    }

//    /**
//     * 修改商品款式资料
//     *
//     * @author pengjun
//     *
//     * @param entity
//     *            修改的商品属性
//     * @param id
//     *            修改的商品ID
//     * @param cookie
//     *            cookie值
//     * @return 返回商品ID
//     * */
//    public boolean updateItem(ShopItemModel entity, int id, String cookie) throws Exception {
//        boolean result = false;
//        try {
//            if (entity.getImages() == null || entity.getImages().length == 0) {
//                throw new Exception("至少需要添加一张商品图片");
//            } else if (entity.getProducts() == null || entity.getProducts().size() == 0) {
//                throw new Exception("至少需要给商品设置一种规格");
//            }
//
//            // 商品图片Json
//            String images = "";
//            for (String imgUrl : entity.getImages()) {
//                images += "'" + imgUrl + "',";
//            }
//            if (images.length() > 0) {
//                images = images.substring(0, images.length() - 1);
//            }
//            images = "[" + images + "]";
//
//            // 商品规格Json
//            String products = "";
//            for (ProductModel product : entity.getProducts()) {
//                products += "{'Color':'" + product.getColor() + "',";
//                products += "'Size':'" + product.getSize() + "',";
//                products += "'Stock':" + product.getStock() + ",";
//                products += "'Price':" + product.getPrice() + "},";
//                // products += "'Cover':'" + product.getCover() + "'},";
//            }
//            if (products.length() > 0) {
//                products = products.substring(0, products.length() - 1);
//            }
//
//            products = "[" + products + "]";
//
//            // Styles
//            String styles = "[" + entity.getStyle() + "]";
//            //根据group ids 判断是否只是代理可见
//            setIsOnly4Agent(entity);
//            String strSaveJson = "{";
//            strSaveJson += "'Tag':'" + entity.getUniqueTag() + "',";
//            strSaveJson += "'IsOnly4Agent':" + entity.getIsOnly4Agent() + ",";
//            strSaveJson += "'AgentItemID':" + String.valueOf(id) + ",";
//            strSaveJson += "'Name':'" + entity.getName() + "',";
//            strSaveJson += "'intro':'" + entity.getIntro() + "',";//db的 intro
//            strSaveJson += "'Price':" + entity.getPrice() + ",";
//            strSaveJson += "'RetailPrice':" + entity.getRetailPrice() + ",";
//            strSaveJson += "'Cover':'" + entity.getCover() + "',";
//            strSaveJson += "'Groups':[" + entity.getGroupIds() +"],";
//            strSaveJson += "'ShopCats':[" + entity.getShopCatsStr() + "],";
//            strSaveJson += "'Attrs':[" + entity.getItemAttrsStr() + "],";
//            strSaveJson += "'IsTop':" + entity.isTop() + ",";
//            strSaveJson += "'Cat':" + entity.getCat() + ",";
//            strSaveJson += "'Styles':" + styles + ",";
//            String imgHTMLStr = "";
//            // if (entity.getImages().length>5)
//            // {
//            imgHTMLStr = "<div id=\"beginAppImgInsertTag\" ></div>";
//
//            for (int i = 0; i < entity.getImages().length; i++) {
//                imgHTMLStr += "<img src=\"" + ImageUrlExtends.getImageUrl(entity.getImages()[i], 24)
//                        + "\" />";
//                imgHTMLStr += "<br/>";
//            }
//            imgHTMLStr += "<div id=\"endAppImgInsertTag\" ></div>";
//            // }
//            strSaveJson += "'Description':'" + imgHTMLStr + "',";
//            strSaveJson += "'Images':" + images + ",";
//            strSaveJson += "'Products':" + products + "}";
//            Log.d("API", "更新商品：" + strSaveJson);
//            String json = HttpsUtils.httpPost("shop/agent/updatemyitem", strSaveJson, cookie);
//            Log.i(TAG, "Json：" + json);
//            result = GsonHelper.jsonToObject(json, boolean.class);
//        } catch (Exception ex) {
//            Log.e(TAG,
//                    MessageFormat.format("{0}->{1}方法发生异常：{2}", TAG, "updateitem", ex.getMessage()));
//            ex.printStackTrace();
//            throw ex;
//        }
//        return result;
//    }
    /**
     * Description:根据group ids判断是否只是代理可看
     * 2014-7-24 上午9:16:25
     * @author ZZB
     */
    private void setIsOnly4Agent(ShopItemModel entity) {
        boolean isOnly4Agent = false;
        String groupIds = StringUtils.deleteEndStr(entity.getGroupIds(), ",");
        if(TextUtils.isEmpty(groupIds) || groupIds.equals(SystemGroupId.ALL_PPL + "")){
            isOnly4Agent = false;
            entity.setGroupIds("");
        }else{
            isOnly4Agent = true;
            if(groupIds.equals(SystemGroupId.ALL_AGENT + "")){
                entity.setGroupIds("");
            }
        }
        entity.setIsOnly4Agent(isOnly4Agent);
        
    }

    /**
     * 上传图片到服务器
     *
     * @author pj
     *
     * @param fileName
     *            文件名，上传到upyun的文件名
     * @param imgUrl
     *            本地图片文件路径
     *
     * @return 返回图片在服务器上的路径
     * */
    public static String uploadImage(String fileName, String imgUrl) throws Exception {
        String serverPath = "";
        serverPath = UpYunAPI.uploadImage(fileName,
                Const.UPYUN_BUCKET_ITEM, Const.UPYUN_API_KEY_ITEM, imgUrl);
        return serverPath;
    }
}
