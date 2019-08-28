package com.nahuo.quicksale.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.StringUtils;
import com.nahuo.quicksale.oldermodel.ItemShopInfo;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.oldermodel.ShopItemModel;

import java.util.ArrayList;
import java.util.List;

public class UploadItemDBHelper {

    private static final String       TAG      = "UploadItemDBHelper";
    private DBManager                 dbManager;
    private static UploadItemDBHelper instance = null;

    public static UploadItemDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new UploadItemDBHelper(context);
        }
        return instance;
    }

    private UploadItemDBHelper(Context context) {
        dbManager = new DBManager(context);
    }

//    /**
//     * 增加一个调后台上传记录
//     * */
//    public boolean AddUploadItem(Context context, ShopItemModel itemModel) {
//        try {
//            // 新增单据
//            ContentValues cv = this.GetContentValues(context, itemModel);
//            dbManager.Insert("upload_tasklist", cv);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    /**
     * @description 获取等待上传的商品数目
     * @created 2015-4-8 下午7:41:26
     * @author ZZB
     */
    public int getWaitUploadNum(Context context) {
        Cursor cursor = null;
        try {
            int userId = SpManager.getUserId(context);
            cursor = dbManager.Select("select id from upload_tasklist where login_user_id = " + userId, null);
            int count = cursor.getCount();
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            if (cursor != null)
                cursor.close();
        }

    }

//    /**
//     * @description 获取上传列表所有的商品，包括上传失败的
//     * @created 2015-4-9 上午9:57:15
//     * @author ZZB
//     */
//    public List<ShopItemModel> getAllUploadItems(Context context) {
//        return getUploadItemsByUploadCounter(context, 100);
//    }

//    /**
//     * @description 获取需要上传的商品，不包括上传失败的
//     * @created 2015-4-9 上午10:00:04
//     * @author ZZB
//     */
//    public List<ShopItemModel> getValidUploadItems(Context context) {
//        return getUploadItemsByUploadCounter(context, Const.UPLOAD_MAX_COUNTER);
//    }
//
//    /**
//     * @description 获取uploadCounter小于指定数目的商品
//     * @created 2015-4-9 上午9:58:55
//     * @author ZZB
//     */
//    private List<ShopItemModel> getUploadItemsByUploadCounter(Context context, int uploadCounter) {
//        List<ShopItemModel> items = new ArrayList<ShopItemModel>();
//        Cursor cursor = null;
//        try {
//            cursor = dbManager.Select("upload_tasklist", null, "upload_counter <= ? and login_user_id=?", new String[] {
//                    String.valueOf(uploadCounter), String.valueOf(SpManager.getUserId(context))}, null, null,
//                    "create_time ASC");
//            while (cursor.moveToNext()) {
//                ShopItemModel item = new ShopItemModel();
//                item.setUploadID(DBHelper.getInt(cursor, "id"));
//                item.setIsAdd(DBHelper.getBoolean(cursor, "is_add"));
//                // item.setOldImages(cursor.getString(cursor.getColumnIndex("old_image")));
//                item.setItemID(cursor.getInt(cursor.getColumnIndex("item_id")));
//                item.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
//                item.setRetailPrice(cursor.getDouble(cursor.getColumnIndex("retail_price")));
//                item.setCover(cursor.getString(cursor.getColumnIndex("cover")));
//                item.setImagesJson(cursor.getString(cursor.getColumnIndex("images")));
//                item.setDescription(DBHelper.getString(cursor, "intro"));
//                item.setName(cursor.getString(cursor.getColumnIndex("title")));
//                item.setIntro(DBHelper.getString(cursor, "intro"));
//                item.setGroupIds(cursor.getString(cursor.getColumnIndex("group_ids")));
//                item.setCreateDate(cursor.getString(cursor.getColumnIndex("create_time")));
//                item.setItemSourceType(DBHelper.getInt(cursor, "item_source_type"));
//                item.setProductsJson(cursor.getString(cursor.getColumnIndex("products")));
//                item.setIsOnly4Agent(DBHelper.getBoolean(cursor, "is_only_4_agent"));
//                item.setUploadCounter(DBHelper.getInt(cursor, "upload_counter"));
//                item.setCat(DBHelper.getString(cursor, "category"));
//                item.setStyle(Integer.valueOf(DBHelper.getString(cursor, "styles")));
//                String shopCats = DBHelper.getString(cursor, "shop_cats");
//                if (!TextUtils.isEmpty(shopCats)) {
//                    item.setShopCats(GsonHelper.jsonToObject(shopCats, new TypeToken<ArrayList<CustomModel>>() {}));
//                }
//                String attrs = DBHelper.getString(cursor, "attrs");
//                if (!TextUtils.isEmpty(attrs)) {
//                    item.setItemAttrs(GsonHelper.jsonToObject(attrs, new TypeToken<ArrayList<CustomModel>>() {}));
//                }
//                item.setTop(DBHelper.getBoolean(cursor, "is_top"));
//                item.setUserId(DBHelper.getInt(cursor, "user_id"));
//                item.setUserName(DBHelper.getString(cursor, "user_name"));
//                item.setUniqueTag(DBHelper.getString(cursor, "unique_tag"));
//                items.add(item);
//            }
//            cursor.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Log.e(TAG, ex.getMessage());
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return items;
//    }

    /**
     * @description 根据创建时间获取上传的商品
     * @created 2015-1-27 下午2:48:39
     * @author ZZB
     */
    public ShopItemListModel getUploadItemByCreateTime(Context context, String createTime) {
        Cursor cursor = null;
        try {
            cursor = dbManager.Select("upload_tasklist", null, "create_time == ?", new String[] {createTime}, null,
                    null, "create_time DESC");
            while (cursor.moveToNext()) {
                ShopItemListModel item = cursorToShopItemListModel(cursor);
                // itmelist.add(item);
                return item;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @description 根据商品id获取上传商品
     * @created 2015-1-22 下午3:52:02
     * @author ZZB
     */
    public ShopItemListModel getUploadItemByItemId(int itemId) {
        Cursor cursor = null;
        try {
            cursor = dbManager.Select("upload_tasklist", null, "item_id == ?", new String[] {String.valueOf(itemId)},
                    null, null, "create_time DESC");
            while (cursor.moveToNext()) {
                ShopItemListModel item = cursorToShopItemListModel(cursor);
                // itmelist.add(item);
                return item;
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private ShopItemListModel cursorToShopItemListModel(Cursor cursor) {
        ShopItemListModel item = new ShopItemListModel();
        item.setUploadID(cursor.getInt(cursor.getColumnIndex("id")));
        item.setName(cursor.getString(cursor.getColumnIndex("title")));
        item.setIntro(DBHelper.getString(cursor, "intro"));
        item.setIsAdd(cursor.getInt(cursor.getColumnIndex("is_add")) == 0 ? true : false);
        // String oldImgString = cursor.getString(cursor.getColumnIndex("old_image"));
        String imgString = cursor.getString(cursor.getColumnIndex("images"));
        item.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
        item.setRetailPrice(cursor.getDouble(cursor.getColumnIndex("retail_price")));
        item.setItemID(cursor.getInt(cursor.getColumnIndex("item_id")));
        item.setCreateDate(cursor.getString(cursor.getColumnIndex("create_time")));
        item.setCover(cursor.getString(cursor.getColumnIndex("cover")));
        item.setImagesJson(imgString);
        item.setIsUploaded(false);
        item.setUploadCounter(DBHelper.getInt(cursor, "upload_counter"));
        item.setItemSourceType(DBHelper.getInt(cursor, "item_source_type"));
        item.setSourceID(DBHelper.getInt(cursor, "source_id"));
        ItemShopInfo info = new ItemShopInfo();
        info.setUserId(DBHelper.getInt(cursor, "user_id"));
        info.setUserName(DBHelper.getString(cursor, "user_name"));

        item.setItemShopInfo(new ItemShopInfo[] {info});
        return item;
    }

    /**
     * 读取出所有的后台上传记录
     * */
    @Deprecated
    public List<ShopItemListModel> GetUploadItems(Context context) {
        List<ShopItemListModel> itmelist = new ArrayList<ShopItemListModel>();
        Cursor cursor = null;
        try {
            cursor = dbManager.Select("upload_tasklist", null, "is_add == 1", null, null, null, "create_time DESC");
            while (cursor.moveToNext()) {
                ShopItemListModel item = cursorToShopItemListModel(cursor);
                itmelist.add(item);
            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, ex.getMessage());
            if (cursor != null) {
                cursor.close();
            }
        }
        return itmelist;
    }

    /**
     * 已上传完毕的情况下，删除这条后台上传记录
     * */
    public boolean DeleteUploadItem(int del_id) {
        try {
            dbManager.Delete("upload_tasklist", "id=?", new String[] {String.valueOf(del_id)});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUploadItem(String uploadTime) {
        dbManager.Delete("upload_tasklist", "create_time = ?", new String[] {uploadTime});
        return true;
    }

    /**
     * @description 更新商品的上传次数
     * @created 2014-9-30 下午4:53:50
     * @author ZZB
     */
    public void updateUploadCounter(String createTime, int counter) {
        Log.e(Const.TAG_TEST, "上传次数, " + createTime + ":" + counter);
        ContentValues cv = new ContentValues();
        cv.put("upload_counter", counter);
        dbManager.Update("upload_tasklist", cv, "create_time = ?", new String[] {createTime});
    }

//    /**
//     * 填充数据到ContentValues，并返回ContentValues
//     */
//    private ContentValues GetContentValues(Context context, ShopItemModel cim) {
//        ContentValues cv = new ContentValues();
//        cv.put("create_time", cim.getCreateDate());
//        cv.put("intro", cim.getIntro());
//        cv.put("title", cim.getName());
//        cv.put("price", cim.getPrice());
//        cv.put("retail_price", cim.getRetailPrice());
//        cv.put("cover", cim.getCover());
//        cv.put("products", cim.getProductsJsonStr());
//        cv.put("images", cim.getImagesJsonStr());
//        cv.put("is_add", cim.isIsAdd());
//        // cv.put("old_image", cim.getOldImages());
//        cv.put("item_id", cim.getItemID());
//        cv.put("group_ids", cim.getGroupIds());
//        cv.put("is_only_4_agent", cim.getIsOnly4Agent());
//        cv.put("upload_counter", cim.getUploadCounter());
//        cv.put("category", cim.getCat());
//        cv.put("styles", cim.getStyle() + "");
//        Gson gson = new Gson();
//        if (cim.getShopCats() != null) {
//            cv.put("shop_cats", gson.toJson(cim.getShopCats()));
//        }
//        if (cim.getItemAttrs() != null) {
//            cv.put("attrs", gson.toJson(cim.getItemAttrs()));
//        }
//        cv.put("is_top", cim.isTop());
//        cv.put("user_id", cim.getUserId());
//        cv.put("login_user_id", SpManager.getUserId(context));
//        cv.put("user_name", cim.getUserName());
//        cv.put("item_source_type", cim.getItemSourceType());
//        cv.put("source_id", cim.getSourceID());
//        cv.put("unique_tag", cim.getUniqueTag());
//        return cv;
   // }

    /**
     * Description:通过sp判断商品是否已经上传过 2014-7-24 下午8:41:21
     * 
     * @author ZZB
     */
    public static boolean isUploaded(Context context, ShopItemModel item) {
        // 只用数据库创建时间来判断这个商品的唯一性
        String time = item.getCreateDate();
        String uploadedItems = SpManager.getUploadItems(context);
        // 使用特定方法判断是否是上传过的商品
        if (StringUtils.contains(uploadedItems, time, ",")) {
            BaiduStats.log(context, BaiduStats.EventId.DUPLICATE_UPLOAD);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Description:把已经上传过的商品时间放到sp 2014-7-24 下午9:01:58
     * 
     * @author ZZB
     */
    public static void setUploadedItem(Context context, ShopItemModel item) {
        if (isUploaded(context, item)) {// 已经重复上传商品到服务器
            BaiduStats.log(context, BaiduStats.EventId.DUPLICATE_UPLOADED);
        }
        String uploadedItems = SpManager.getUploadItems(context);
        String newUploadedItems = uploadedItems + "," + item.getCreateDate();
        SpManager.setUploadedItems(context, newUploadedItems);
    }

    /**
     * Description:删除已经成功上传的商品 2014-7-24 下午9:04:22
     * 
     * @author ZZB
     */
    public static void removeUploadedItem(Context context, ShopItemModel item) {
        // TODO:暂时不删除已经上传的时间戳，已便查看是否数据库没有删除成功
        // String uploadedItems = SpManager.getUploadItems(context);
        // //删除已经上传的商品
        // uploadedItems = StringUtils.remove(uploadedItems, item.getCreateDate(), ",");
        // //重新赋值sp
        // SpManager.setUploadedItems(context, uploadedItems);
    }

}
