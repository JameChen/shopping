package com.nahuo.quicksale.mvp.view;

import com.nahuo.quicksale.oldermodel.MediaStoreImage;
import com.nahuo.quicksale.mvp.MvpView;

import java.util.List;

/**
 * Created by ZZB on 2015/7/9 17:18
 */
public interface QuickSelectImgView extends MvpView{

    public void showLoading(boolean show);

    public void onRecentImgsLoaded(List<MediaStoreImage> paths);


}
