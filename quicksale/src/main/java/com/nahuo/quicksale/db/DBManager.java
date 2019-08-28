package com.nahuo.quicksale.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public Cursor Select(String table, String[] columns, String selection,
                         String[] selectionArgs, String groupBy, String having,
                         String orderBy) {
        return db.query(table, columns, selection, selectionArgs, groupBy,
                having, orderBy);
    }

    /**
     * 执行SQL语句查询的方法
     *
     * @param sql           SQL语句
     * @param selectionArgs 填充占位符的数组
     * @return Cursor 返回Cursor对象
     */
    public Cursor Select(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    /**
     * 执行MAX、MIN、SUM、COUNT等返回首行首列查询的方法，并返回String对象
     *
     * @param sql           SQL语句
     * @param selectionArgs 填充占位符的数组
     * @return Cursor 返回String对象
     */
    public String ExecuteScalar(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        cursor.moveToFirst();
        String str = (cursor.getString(0) == null || cursor.getString(0) == null) ? "0"
                : cursor.getString(0);
        cursor.close();
        return str;
    }

	/**
	 * 执行增、删、改操作的方法
	 * 
	 * @param sql
	 *            SQL语句
	 * @return boolean 返回执行结果：true-成功，false-失败
	 */
	public boolean ExecuteSQL(String sql) {
		boolean result = false;
		try {
			db.execSQL(sql);
			result = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public boolean Insert(String table, ContentValues cv) {
		long id = db.insert(table, null, cv);
		return id != -1;
	}

    public void Update(String table, ContentValues cv, String whereClause,
                       String[] whereArgs) {
        db.update(table, cv, whereClause, whereArgs);
    }

    public void Delete(String table, String whereClause, String[] whereArgs) {
        db.delete(table, whereClause, whereArgs);
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    /**
     * 开启事务
     */
    public void beginTransaction() {
        db.beginTransaction();
    }

    /**
     * 提交事务
     */
    public void commitTransaction() {
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 回滚事务
     */
    public void rollbackTransaction() {
        db.endTransaction();
    }
}