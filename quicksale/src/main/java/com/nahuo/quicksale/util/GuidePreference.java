package com.nahuo.quicksale.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jame on 2017/6/6.
 */

public class GuidePreference {
    public static final String PREFERENCE_NAME = "news_guide_save";
    private static SharedPreferences mSharedPreferences;
    private static GuidePreference mPreferencemManager;
    private static SharedPreferences.Editor editor;
    private static String SAVE_SQ_ME_COL= "SAVE_SQ_ME_COL";
    private static String SAVE_PINHUO_MAIN= "SAVE_PINHUO_MAIN";
    private static String SAVE_SHOP_CART_DETAIL="SAVE_SHOP_CART_DETAIL";
    @SuppressLint("CommitPrefEdits")
    private GuidePreference(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static synchronized void init(Context cxt){
        if(mPreferencemManager == null){
            mPreferencemManager = new GuidePreference(cxt);
        }
    }

    /**
     * get instance of PreferenceManager
     *
     * @param
     * @return
     */
    public synchronized static GuidePreference getInstance() {
        if (mPreferencemManager == null) {
            throw new RuntimeException("please init first!");
        }

        return mPreferencemManager;
    }
    public void setSqMe_Cole(String id,int first){
        editor.putInt(SAVE_SQ_ME_COL+id, first);
        editor.apply();
    }
    public int geSqMe_Cole(String id) {
        return mSharedPreferences.getInt(SAVE_SQ_ME_COL+id, -1);
    }
    public void setPinhuoMain(String id,int first){
        editor.putInt(SAVE_PINHUO_MAIN+id, first);
        editor.apply();
    }
    public int getPinhuoMain(String id) {
        return mSharedPreferences.getInt(SAVE_PINHUO_MAIN+id, -1);
    }
    public void setSaveShopCartDetail(int id,int first){
        editor.putInt(SAVE_SHOP_CART_DETAIL+id, first);
        editor.apply();
    }
    public  int getSaveShopCartDeatail(int id){
        return  mSharedPreferences.getInt(SAVE_SHOP_CART_DETAIL+id,-1);
    }


}
