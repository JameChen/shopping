package com.nahuo.quicksale.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nahuo.quicksale.oldermodel.Area;
import com.nahuo.quicksale.upyun.api.utils.TimeCounter;

/**
 * @description 省市区相关
 * @created 2014-8-26 上午11:25:01
 * @author ZZB
 */
public class AreaDao {

    private SQLiteOpenHelper mDBHelper;
    private SQLiteDatabase mReadDB;
    private static final String TAG = AreaDao.class.getSimpleName();

    public AreaDao(Context context) {
        mDBHelper = new BankAndAreaDBHelper(context);
        mReadDB = mDBHelper.getReadableDatabase();
    }

    /**
     * @description 根据父ID获取地区
     * @created 2014-8-22 下午6:15:32
     * @author ZZB
     */
    public List<Area> getAreas(int parentId) {
        TimeCounter counter = new TimeCounter("查询地区");
        List<Area> areas = new ArrayList<Area>();
        Cursor cursor = mReadDB.query(DBColumns.Area.TABLE_NAME, new String[] { DBColumns.Area.ID,
                DBColumns.Area.NAME }, DBColumns.Area.PARENT_ID + " = ?",
                new String[] { String.valueOf(parentId) }, null, null, null);
        while (cursor.moveToNext()) {
            Area area = new Area();
            area.setName(DBHelper.getString(cursor, DBColumns.Area.NAME));
            area.setId(DBHelper.getInt(cursor, DBColumns.Area.ID));
            areas.add(area);
        }
        counter.end();
        return areas;
    }

    /**
     * @description 根据地区id获取地区
     * @created 2014-9-11 下午12:01:02
     * @author ZZB
     */
    public Area getArea(int id) {

        TimeCounter counter = new TimeCounter("查询地区");
        Area area = null;
        Cursor cursor = mReadDB.query(DBColumns.Area.TABLE_NAME, new String[] { DBColumns.Area.ID,
                DBColumns.Area.NAME, DBColumns.Area.PARENT_ID }, DBColumns.Area.ID + " = ?",
                new String[] { String.valueOf(id) }, null, null, null);
        if (cursor.moveToNext()) {
            area = new Area();
            area.setName(DBHelper.getString(cursor, DBColumns.Area.NAME));
            area.setId(DBHelper.getInt(cursor, DBColumns.Area.ID));
            area.setParentId(DBHelper.getInt(cursor, DBColumns.Area.PARENT_ID));
        }
        counter.end();
        return area;
    }

}
