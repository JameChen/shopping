package com.nahuo.quicksale.task;

import android.content.Context;
import android.os.AsyncTask;

import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.BuyOnlineAPI;

/**
 * @description 备注地址
 * @created 2014-11-12 下午5:26:30
 * @author ZZB
 */
public class BackupAddressTask extends AsyncTask<Object, Void, Object> {

    private Context mContext;
    private int mUserId;
    private String mAddress;
    private Listener mListener;
    
    public static interface Listener{
        
        public void onAddressSaved(String address);
    }

    public BackupAddressTask(Context context, int userId, String address) {
        mContext = context;
        mUserId = userId;
        mAddress = address;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {
            BuyOnlineAPI.itemAddressRemark(mContext, mAddress, mUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return "error:" + e.getMessage();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (result instanceof String && ((String) result).startsWith("error:")) {
            ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
        }else{
            if(mListener != null){
                mListener.onAddressSaved(mAddress);
            }
			
        }
    }

    public Listener getListener() {
        return mListener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

}
