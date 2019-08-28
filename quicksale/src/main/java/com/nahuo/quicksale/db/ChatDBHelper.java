package com.nahuo.quicksale.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static ChatDBHelper instance;

	private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
			+ DBColumns.ChatUser.TABLE_NAME + " ("
			+ DBColumns.ChatUser.COLUMN_NAME_NICK + " TEXT, "
			+ DBColumns.ChatUser.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

	private static final String CONVERSION_TABLE_CREATE = "CREATE TABLE "
			+ DBColumns.ConversionUser.TABLE_NAME + " ("
			+ DBColumns.ConversionUser.COLUMN_NAME_NICK + " TEXT, "
			+ DBColumns.ConversionUser.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

	private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
			+ InviteMessgeDao.TABLE_NAME + " ("
			+ InviteMessgeDao.COLUMN_NAME_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, "
			+ InviteMessgeDao.COLUMN_NICK_FROM + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_GROUP_ID + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_GROUP_Name + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_REASON + " TEXT, "
			+ InviteMessgeDao.COLUMN_NAME_STATUS + " INTEGER, "
			+ InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
			+ InviteMessgeDao.COLUMN_NAME_TIME + " TEXT); ";

	// 构造
	public ChatDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	// 构造
	public ChatDBHelper(Context context) {
		this(context, "chat_user.db", null, DATABASE_VERSION);
	}

	public static ChatDBHelper getInstance(Context context) {
		if (instance == null) {
			instance = new ChatDBHelper(context.getApplicationContext());
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USERNAME_TABLE_CREATE);
		db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
		db.execSQL(CONVERSION_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
	}
	


	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}

}
