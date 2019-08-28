package com.nahuo.quicksale.db;

import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.BaiduStats;

public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = DBHelper.class.getSimpleName();
	private SQLiteDatabase mDefaultWritableDatabase = null;
	private static final String DATABASE_NAME = "wp.db";
	private static final int DATABASE_VERSION = 22;
	private Context mContext;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mContext = context;
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mContext = context;
	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		final SQLiteDatabase db;
		if (mDefaultWritableDatabase != null) {
			db = mDefaultWritableDatabase;
		} else {
			db = super.getWritableDatabase();
		}
		return db;
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;
		String strSQL = "";

		InputStream in = null;

		// 读取文件
		try {
			in = mContext.getResources().openRawResource(R.raw.sql);
			// 将文件读入字节数组
			byte[] reader = new byte[in.available()];
			while (in.read(reader) != -1) {
			}
			strSQL = new String(reader, "utf-8");

			String[] strTables = strSQL.split(";");
			// db.beginTransaction();
			for (String strTalbe : strTables) {
				db.execSQL(strTalbe.replace("\r\n", "") + ";");
			}
			// db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			BaiduStats.log(mContext, BaiduStats.EventId.CREATE_DB_FAILED,
					e.getMessage());
		}
	}

	// 版本2数据库更新
	public void version2(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;

		try {
			db.beginTransaction();
			db.execSQL("alter table upload_tasklist add old_image char(500) null;");
			db.execSQL("alter table upload_tasklist add is_add INTEGER null;");
			db.execSQL("alter table upload_tasklist add item_id INTEGER null;");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	// 版本3数据库更新
	public void version3(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;

		try {
			db.beginTransaction();
			db.execSQL("alter table upload_tasklist add username char(200) null;");
			db.execSQL("alter table shop_item_list add username char(200) null;");
			db.execSQL("alter table my_item_list add username char(200) null;");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Description:upload_tasklist 增加一个字段 group_ids 2014-7-15上午11:25:00
	 */
	public void version4(SQLiteDatabase db) {

		this.mDefaultWritableDatabase = db;
		try {
			db.beginTransaction();
			db.execSQL("alter table upload_tasklist add group_ids char(500) null;");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Description:my_item_list增加一个字段group_ids 2014-7-22 上午10:55:42
	 * 
	 * @author ZZB
	 */
	public void version5(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;
		try {
			db.beginTransaction();
			db.execSQL("alter table my_item_list add group_ids char(500) null;");
			db.execSQL("alter table my_item_list add group_names char(500) null;");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	/**
	 * Description:my_item_list 增加一个字段 is_only_4_agent 默认值为0，即false 2014-7-23
	 * 下午8:53:25
	 * 
	 * @author ZZB
	 */
	public void version6(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;
		try {
			db.beginTransaction();
			db.execSQL("alter table my_item_list add is_only_4_agent integer;");
			db.execSQL("alter table upload_tasklist add is_only_4_agent integer;");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Description:upload_tasklist 增加一个字段 retail_price 2014-8-27 下午2:34:25
	 * 
	 * @author PJ
	 */
	public void version7(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;
		try {
			db.beginTransaction();
			db.execSQL("alter table upload_tasklist add column retail_price DEC(10,2);");
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * Description:all_item_list,shop_item_list,my_item_list 增加一个字段
	 * ID,itemID,myid,记录下来 2014-9-12 下午2:34:25
	 * 
	 * @author PJ
	 */
	public void version9(SQLiteDatabase db) {
		this.mDefaultWritableDatabase = db;
		try {
			db.beginTransaction();
			db.execSQL("alter table all_item_list add column item_id INTEGER;");
			db.execSQL("alter table all_item_list add column item_item_id INTEGER;");
			db.execSQL("alter table all_item_list add column my_id INTEGER;");

			db.execSQL("alter table shop_item_list add column item_id INTEGER;");
			db.execSQL("alter table shop_item_list add column item_item_id INTEGER;");
			db.execSQL("alter table shop_item_list add column my_id INTEGER;");

			db.execSQL("alter table my_item_list add column item_id INTEGER;");
			db.execSQL("alter table my_item_list add column item_item_id INTEGER;");
			db.execSQL("alter table my_item_list add column my_id INTEGER;");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @description
	 * @created 2014-9-30 下午4:28:24
	 * @author ZZB
	 */
	public void version10(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			// 上传次数
			db.execSQL("alter table upload_tasklist add column upload_counter INTEGER default 0;");
			// 是否通知刷新界面，避免多次刷新
			db.execSQL("alter table upload_tasklist add column notified INTEGER default 0;");
			// all_item_list : user_id, retail_price
			db.execSQL("alter table all_item_list add column user_id INTEGER;");
			db.execSQL("alter table all_item_list add column retail_price DEC(10,2);");
			// shop_item_list : retail_price
			db.execSQL("alter table shop_item_list add column retail_price DEC(10,2);");
			// my_item_list : user_id, retail_price
			db.execSQL("alter table my_item_list add column user_id INTEGER;");
			db.execSQL("alter table my_item_list add column retail_price DEC(10,2);");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @description 空出来，之前3.3版本占用了version11，为了兼容更新，这里不对11做处理，留空
	 * @created 2014-11-16
	 * @author PJ
	 */
	public void version11(SQLiteDatabase db) {
		// 空出来，之前3.3版本占用了version11，为了兼容更新，这里不对11做处理，留空
	}

	/**
	 * @description 上传队列增加intro字段, 商品简介
	 * @created 2014-10-25
	 * @author PJ
	 */
	public void version12(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("alter table upload_tasklist add column intro varchar(5000);");
			db.execSQL("alter table my_item_list add column intro varchar(5000);");
			db.execSQL("alter table all_item_list add column intro varchar(5000);");
			db.execSQL("alter table shop_item_list add column intro varchar(5000);");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @description all_item_list 加字段
	 * @created 2014-11-11 下午7:56:53
	 * @author ZZB
	 */
	public void version13(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("alter table all_item_list add column org_user_id integer;");
			db.execSQL("alter table all_item_list add column address varchar(500);");
			db.execSQL("alter table all_item_list add column backup_address varchar(500);");
			db.execSQL("alter table all_item_list add column mobile varchar(500);");
			db.execSQL("alter table all_item_list add column wx_name varchar(500);");
			db.execSQL("alter table all_item_list add column wx_num varchar(500);");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * @description my_item_list 加字段
	 * @created 2014-11-12 下午3:39:42
	 * @author ZZB
	 */
	public void version14(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("alter table my_item_list add column org_user_id integer;");
			db.execSQL("alter table my_item_list add column address varchar(500);");
			db.execSQL("alter table my_item_list add column backup_address varchar(500);");
			db.execSQL("alter table my_item_list add column mobile varchar(500);");
			db.execSQL("alter table my_item_list add column wx_name varchar(500);");
			db.execSQL("alter table my_item_list add column wx_num varchar(500);");
			db.execSQL("alter table my_item_list add column org_user_name varchar(500);");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	public void version15(SQLiteDatabase db) {
		try {
			db.beginTransaction();
			db.execSQL("alter table all_item_list add column apply_statu_id integer;");
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	/**
	 * @description 上传商品添加分类
	 * @created 2015-3-3 下午2:06:58
	 * @author ZZB
	 */
	public void version16(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("alter table upload_tasklist add column category varchar(100);");
            db.execSQL("alter table upload_tasklist add column styles varchar(100);");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
	/**
	 * @description 上传商品添加店铺分类
	 * @created 2015-3-19 下午3:31:45
	 * @author ZZB
	 */
	private void version17(SQLiteDatabase db){
	    try {
            db.beginTransaction();
            db.execSQL("alter table upload_tasklist add column shop_cats varchar(100);");
            db.execSQL("alter table upload_tasklist add column attrs varchar(50);");
            db.execSQL("alter table upload_tasklist add column is_top integer;");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
	}
	/**
	 * @description upload_tasklist 添加user_id
	 * @created 2015-3-31 下午4:09:22
	 * @author ZZB
	 */
	private void version18(SQLiteDatabase db){
        try {
            db.beginTransaction();
            db.execSQL("alter table upload_tasklist add column user_id integer;");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
	/**
	 * @description upload_tasklist增加两个字段
	 * @created 2015-3-31 下午4:24:19
	 * @author ZZB
	 */
    private void version19(SQLiteDatabase db){
        try {
            db.beginTransaction();
            db.execSQL("alter table upload_tasklist add column item_source_type integer;");
            db.execSQL("alter table upload_tasklist add column user_name varchar(200);");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
    /**
     * @description upload_tasklist增加字段
     * @created 2015-3-31 下午4:37:40
     * @author ZZB
     */
    private void version20(SQLiteDatabase db){
        execSql(db, "alter table upload_tasklist add column source_id integer;");
    }
    /**
     * @description upload_tasklist增加字段
     * @created 2015-4-10 下午2:43:13
     * @author ZZB
     */
    private void version21(SQLiteDatabase db){
        execSql(db, "alter table upload_tasklist add column login_user_id integer;");
    }
    /**
     * @description upload_tasklist增加字段
     * @created 2015-4-24 下午1:55:19
     * @author ZZB
     */
    private void version22(SQLiteDatabase db){
        execSql(db, "alter table upload_tasklist add column unique_tag varchar(200);");
    }
	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			this.mDefaultWritableDatabase = db;
			switch (oldVersion) {
			case 1:
				version2(db);
			case 2:
				version3(db);
			case 3:
				version4(db);
			case 4:
				version5(db);
			case 5:
				version6(db);
			case 6:
			case 7:
				version7(db);
			case 8:
				version9(db);
			case 9:
				version10(db);
			case 10:
				version11(db);
			case 11:
				version12(db);
			case 12:
				version13(db);
			case 13:
				version14(db);
			case 14:
				version15(db);
			case 15:
			    version16(db);
			case 16:
			    version17(db);
			case 17:
			    version18(db);
			case 18:
			    version19(db);
			case 19:
			    version20(db);
			case 20:
			    version21(db);
			case 21:
			    version22(db);
				break;
			}// 注意，最新版本之前的case不能break, 也即break应该出现在最后一个case!!!!!!!
		} catch (Exception e) {
			e.printStackTrace();
			BaiduStats.log(mContext, BaiduStats.EventId.UPDATE_DB_FAILED,
					oldVersion + "->" + newVersion + ":" + e.getMessage());
		}

	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.mDefaultWritableDatabase = db;
	}

	private void execSql(SQLiteDatabase db, String... sqls){
        try {
            db.beginTransaction();
            for(String sql : sqls){
                db.execSQL(sql);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
	public static String getString(Cursor cursor, String column) {
		return cursor.getString(cursor.getColumnIndex(column));
	}

	public static int getInt(Cursor cursor, String column) {
		return cursor.getInt(cursor.getColumnIndex(column));
	}

	public static double getDouble(Cursor cursor, String column) {
		return cursor.getDouble(cursor.getColumnIndex(column));
	}
	public static boolean getBoolean(Cursor cursor, String column){
	    return getInt(cursor, column) == 0 ? false : true;
	}

}
