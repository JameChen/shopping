package com.nahuo.quicksale.mvp.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.nahuo.quicksale.common.MediaStoreUtils;
import com.nahuo.quicksale.oldermodel.MediaStoreImage;
import com.nahuo.quicksale.mvp.MvpBasePresenter;
import com.nahuo.quicksale.mvp.view.QuickSelectImgView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZB on 2015/7/9 17:22
 */
public class QuickSelectImgPresenter extends MvpBasePresenter<QuickSelectImgView>{

    private Context mAppContext;

    public QuickSelectImgPresenter(Context context){
        mAppContext = context.getApplicationContext();
    }

    public void loadRecentImages(Context context){
        new AsyncTask<Void, Void, List<MediaStoreImage>>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().showLoading(true);
            }

            @Override
            protected List<MediaStoreImage> doInBackground(Void... params) {
                List<MediaStoreImage> imgs = new ArrayList<MediaStoreImage>();
                if(isViewAttached()){
                    imgs = MediaStoreUtils.getRecentImages(mAppContext, 20);
                }
                return imgs;
            }

            @Override
            protected void onPostExecute(List<MediaStoreImage> imgs) {
                super.onPostExecute(imgs);
                if(isViewAttached()){
                    getView().onRecentImgsLoaded(imgs);
                    getView().showLoading(false);
                }
            }
        }.execute();

    };

}
