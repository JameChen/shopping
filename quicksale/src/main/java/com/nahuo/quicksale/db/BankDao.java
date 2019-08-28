package com.nahuo.quicksale.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.nahuo.quicksale.oldermodel.Bank;
import com.nahuo.quicksale.upyun.api.utils.TimeCounter;

public class BankDao {
    
    
    private static final String TAG = BankDao.class.getSimpleName();
    private SQLiteOpenHelper mDBHelper;
    private SQLiteDatabase mReadDB;
    
    public BankDao(Context context){
        mDBHelper = new BankAndAreaDBHelper(context);
        mReadDB = mDBHelper.getReadableDatabase();
    }
    
    public List<Bank> getBanks(int parentId){
        TimeCounter counter = new TimeCounter("查询银行");
        List<Bank> banks = new ArrayList<Bank>();
        Cursor cursor = mReadDB.query(DBColumns.Bank.TABLE_NAME, new String[]{DBColumns.Bank.ID, DBColumns.Bank.NAME}, DBColumns.Bank.PARENT_ID + " = ?", new String[]{String.valueOf(parentId)}, null, null, null);
        while(cursor.moveToNext()){
            Bank bank = new Bank();
            bank.setName(DBHelper.getString(cursor, DBColumns.Bank.NAME));
            bank.setId(DBHelper.getInt(cursor, DBColumns.Bank.ID));
            banks.add(bank);
        }
        counter.end();
        return banks;
    }
    /**
     * @description 根据银行名称获取银行
     * @created 2014-9-16 上午10:06:09
     * @author ZZB
     */
    public Bank getBankByName(String name){
        if(TextUtils.isEmpty(name)){
            return null;
        }
        TimeCounter counter = new TimeCounter("查询银行");
        Bank bank = null;
        Cursor cursor = mReadDB.query(DBColumns.Bank.TABLE_NAME, new String[]{DBColumns.Bank.ID, DBColumns.Bank.NAME}, DBColumns.Bank.NAME + " = ?", new String[]{name}, null, null, null);
        if(cursor.moveToNext()){
            bank = new Bank();
            bank.setName(DBHelper.getString(cursor, DBColumns.Bank.NAME));
            bank.setId(DBHelper.getInt(cursor, DBColumns.Bank.ID));
        }else{
            Log.e(TAG, "查找不到银行：" +  name);
        }
        counter.end();
        return bank;
    }
    
}
