package com.nahuo.quicksale.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.*;

import com.nahuo.quicksale.oldermodel.ItemShopInfo;
import com.nahuo.quicksale.oldermodel.ShopItemListModel;
import com.nahuo.quicksale.upyun.api.utils.TimeCounter;

public class ItemListsDBHelper {

    private static final String TAG = "ItemListsDBHelper";
    private DBManager dbManager;

    private static ItemListsDBHelper instance = null;

    public static ItemListsDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ItemListsDBHelper(context);
        }
        return instance;
    }

    private ItemListsDBHelper(Context context) {
        dbManager = new DBManager(context);
    }

    /**
     * 把当次数据记录保存到数据库中
     * */
    public boolean AddAllItems(List<ShopItemListModel> lists, int pageIndex) {
        try {
            TimeCounter tc = new TimeCounter("上新数据库操作");
            for (ShopItemListModel shopItemListModel : lists) {
                ContentValues cv = this.GetAllItemContentValues(shopItemListModel, pageIndex);
                dbManager.Insert("all_item_list", cv);
            }
            tc.end();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取出数据库中的记录，如没有数据返回为null
     * */
    public List<ShopItemListModel> GetAllItems(int pageIndex) {
        List<ShopItemListModel> itmelist = new ArrayList<ShopItemListModel>();
        Cursor cursor = null;
        try {
            cursor = dbManager.Select("all_item_list", null, null, null,
                    null,
                    null, "create_date DESC");
            while (cursor.moveToNext()) {
                ShopItemListModel item = new ShopItemListModel();
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setIsSource(cursor.getInt(cursor.getColumnIndex("is_source")) == 0 ? true
                        : false);
                item.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                item.setUserName(cursor.getString(cursor.getColumnIndex("username")));
                item.setCreateDate(cursor.getString(cursor.getColumnIndex("create_date")));
                item.setCover(cursor.getString(cursor.getColumnIndex("cover")));
                item.setImagesJson(cursor.getString(cursor.getColumnIndex("images")));
                item.setIsUploaded(true);
                item.setID(cursor.getInt(cursor.getColumnIndex("item_id")));
                item.setItemID(cursor.getInt(cursor.getColumnIndex("item_item_id")));
                item.setMyID(cursor.getInt(cursor.getColumnIndex("my_id")));
                item.setUserid(DBHelper.getInt(cursor, "user_id"));
                item.setRetailPrice(DBHelper.getDouble(cursor, "retail_price"));
                item.setApplyStatuID(DBHelper.getInt(cursor, "apply_statu_id"));
                ItemShopInfo info = new ItemShopInfo();
                info.setUserId(DBHelper.getInt(cursor, "org_user_id"));
                info.setUserName(DBHelper.getString(cursor, "username"));
                info.setAddress(DBHelper.getString(cursor, "address"));
                info.setBackupAddress(DBHelper.getString(cursor, "backup_address"));
                info.setMobile(DBHelper.getString(cursor, "mobile"));
                info.setWxName(DBHelper.getString(cursor, "wx_name"));
                info.setWxNum(DBHelper.getString(cursor, "wx_num"));
                item.setItemShopInfo(new ItemShopInfo[]{info});
                
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
     * 删除指定pageindex的数据，用于重新赋值
     * */
    public boolean DeleteAllItem(int pageIndex) {
        try {
            TimeCounter tc = new TimeCounter("删除all_item_list");
            dbManager.Delete("all_item_list", "page_index=?", new String[] { String.valueOf(pageIndex) });
            tc.end();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除所有allitem数据
     * */
    public boolean DeleteAllItem() {
        try {
            dbManager.Delete("all_item_list", "",
                    new String[] {});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 填充数据到ContentValues，并返回ContentValues
     */
    private ContentValues GetAllItemContentValues(ShopItemListModel cim, int pageIndex) {
        ContentValues cv = new ContentValues();
        cv.put("create_date", cim.getCreateDateStr());
        cv.put("name", cim.getName());
        cv.put("username", cim.getUserName());
        cv.put("cover", cim.getCover());
        cv.put("price", cim.getPrice());
        cv.put("is_source", cim.GetIsSource() ? 0 : 1);
        cv.put("images", cim.getImagesJsonStr());
        cv.put("page_index", pageIndex);
        cv.put("item_id", cim.getID());
        cv.put("item_item_id", cim.getItemID());
        cv.put("my_id", cim.getMyID());
        cv.put("user_id", cim.getUserid());
        cv.put("retail_price", cim.getRetailPrice());
        cv.put("intro", cim.getIntro());
        cv.put("apply_statu_id", cim.getApplyStatuID());
        if(cim.getItemShopInfo() != null){
            ItemShopInfo info = cim.getItemShopInfo()[0];
            cv.put("address", info.getAddress());
            cv.put("backup_address", info.getBackupAddress());
            cv.put("mobile", info.getMobile());
            cv.put("org_user_id", info.getUserId());
            cv.put("wx_name", info.getWxName());
            cv.put("wx_num", info.getWxNum());
        }
        
        return cv;
    }

    /**
     * 把当次数据记录保存到数据库中
     * */
    public boolean AddMyItems(List<ShopItemListModel> lists, int pageIndex) {
        try {
            for (ShopItemListModel shopItemListModel : lists) {
                ContentValues cv = this.GetMyItemContentValues(shopItemListModel, pageIndex);
                dbManager.Insert("my_item_list", cv);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 取出数据库中的记录，如没有数据返回为null
     * */
    public List<ShopItemListModel> GetMyItems(int pageIndex) {
        List<ShopItemListModel> itmelist = new ArrayList<ShopItemListModel>();
        Cursor cursor = null;
        try {
            cursor = dbManager.Select("my_item_list", null, "page_index = " + pageIndex, null,
                    null,
                    null, "create_date DESC");
            while (cursor.moveToNext()) {
                ShopItemListModel item = new ShopItemListModel();
                item.setName(cursor.getString(cursor.getColumnIndex("name")));
                item.setIntro(DBHelper.getString(cursor, "intro"));
                item.setIsSource(DBHelper.getInt(cursor, "is_source") == 0 ? true : false);
                item.setPrice(DBHelper.getDouble(cursor, "price"));
                item.setUserName(DBHelper.getString(cursor, "username"));
                item.setCreateDate(DBHelper.getString(cursor, "create_date"));
                item.setCover(DBHelper.getString(cursor, "cover"));
                item.setImagesJson(DBHelper.getString(cursor, "images"));
                item.setGroupIdsJson(DBHelper.getString(cursor, "group_ids"));
                item.setGroupNamesJson(DBHelper.getString(cursor, "group_names"));
                item.setIsUploaded(true);
                item.setID(DBHelper.getInt(cursor, "item_id"));
                item.setItemID(DBHelper.getInt(cursor, "item_item_id"));
                item.setMyID(DBHelper.getInt(cursor, "my_id"));
                item.setUserid(DBHelper.getInt(cursor, "user_id"));
                item.setRetailPrice(DBHelper.getDouble(cursor, "retail_price"));
                
                ItemShopInfo info = new ItemShopInfo();
                info.setUserId(DBHelper.getInt(cursor, "org_user_id"));
                info.setAddress(DBHelper.getString(cursor, "address"));
                info.setBackupAddress(DBHelper.getString(cursor, "backup_address"));
                info.setMobile(DBHelper.getString(cursor, "mobile"));
                info.setWxName(DBHelper.getString(cursor, "wx_name"));
                info.setWxNum(DBHelper.getString(cursor, "wx_num"));
                info.setUserName(DBHelper.getString(cursor, "org_user_name"));
                item.setItemShopInfo(new ItemShopInfo[]{info});
                
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
     * 删除指定pageindex的数据，用于重新赋值
     * */
    public boolean DeleteMyItem(int pageIndex) {
        try {
            dbManager.Delete("my_item_list", "page_index=?",
                    new String[] { String.valueOf(pageIndex) });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除所有myitem数据
     * */
    public boolean DeleteMyItem() {
        try {
            dbManager.Delete("my_item_list", "",
                    new String[] {});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 填充数据到ContentValues，并返回ContentValues
     */
    private ContentValues GetMyItemContentValues(ShopItemListModel cim, int pageIndex) {
        ContentValues cv = new ContentValues();
        cv.put("create_date", cim.getCreateDateStr());
        cv.put("name", cim.getName());
        cv.put("username", cim.getUserName());
        cv.put("cover", cim.getCover());
        cv.put("price", cim.getPrice());
        cv.put("is_source", cim.GetIsSource() ? 0 : 1);
        cv.put("images", cim.getImagesJsonStr());
        cv.put("page_index", pageIndex);
        cv.put("group_ids", cim.getGroupIdsFromGroups());
        cv.put("group_names", cim.getGroupNamesFromGroups());
        cv.put("item_id", cim.getID());
        cv.put("item_item_id", cim.getItemID());
        cv.put("my_id", cim.getMyID());
        cv.put("user_id", cim.getUserid());
        cv.put("retail_price", cim.getRetailPrice());
        cv.put("intro", cim.getIntro());
        
        if(cim.getItemShopInfo() != null){
            ItemShopInfo info = cim.getItemShopInfo()[0];
            cv.put("address", info.getAddress());
            cv.put("backup_address", info.getBackupAddress());
            cv.put("mobile", info.getMobile());
            cv.put("org_user_id", info.getUserId());
            cv.put("wx_name", info.getWxName());
            cv.put("wx_num", info.getWxNum());
            cv.put("org_user_name", info.getUserName());
        }
        return cv;
    }

}
