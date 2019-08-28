package com.nahuo.quicksale.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.nahuo.quicksale.common.BaiduStats;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class BankAndAreaDBHelper extends SQLiteOpenHelper{
    
    private static final String TAG = BankAndAreaDBHelper.class.getSimpleName();
    private static final int VERSION = 2;
    
    //The Android's default system path of your application database.
    private static String DB_PATH;
 
    private static String DB_NAME = "BankAndArea.db";
 
    private SQLiteDatabase mDB; 
 
    private final Context mContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public BankAndAreaDBHelper(Context context) {
 
        super(context, DB_NAME, null, VERSION);
        this.mContext = context;
        DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + mContext.getPackageName() + "/databases/";
    }   
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     **/
    public void createDataBase(){
 
        boolean dbExist = checkDataBase();
 
        if(dbExist){
            //do nothing - database already exist
            Log.d(TAG, "数据库已经存在：" + DB_PATH + DB_NAME);
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, "Error copying database");
                e.printStackTrace();
            }
        }
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
        SQLiteDatabase checkDB = null;
 
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
            Log.d(TAG, "DATABASE DOESN'T EXIST YET");
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
        long start = System.currentTimeMillis();
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DB_NAME);
 
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
 
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
 
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
 
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        long costs = System.currentTimeMillis() - start;
        Log.d(TAG, "复制数据库耗时：" + costs);
        BaiduStats.log(mContext, BaiduStats.EventId.COPY_BANK_AREA_DB_TIME, costs + "", costs);
    }
 
    public void openDataBase() throws SQLException{
 
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        mDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
    public synchronized void close() {
 
            if(mDB != null)
                mDB.close();
 
            super.close();
 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
 
}