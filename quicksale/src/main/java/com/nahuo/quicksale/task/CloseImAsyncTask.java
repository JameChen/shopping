package com.nahuo.quicksale.task;

import android.content.Context;
import android.os.AsyncTask;

import com.nahuo.quicksale.api.ShopSetAPI;
import com.nahuo.quicksale.oldermodel.PublicData;

/**
 * Created by jame on 2018/4/19.
 */

public class CloseImAsyncTask extends AsyncTask<Object, Object, Object> {
   public Context context;
    public CloseImAsyncTask(Context context){
        this.context=context;
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            return ShopSetAPI.getCloseIm(PublicData.getCookie(context));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
