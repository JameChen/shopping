package com.nahuo.quicksale.controls;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;


import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.QuickSelectImageAdapter;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.ImageViewModel;
import com.nahuo.quicksale.oldermodel.MediaStoreImage;
import com.nahuo.quicksale.mvp.presenter.QuickSelectImgPresenter;
import com.nahuo.quicksale.mvp.view.QuickSelectImgView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZB on 2015/7/9 10:45
 */
public class UploadItemPopMenu extends PopupWindow implements AdapterView.OnItemClickListener, View.OnClickListener, QuickSelectImgView {

    private Activity mActivity;
    private View                mRootView;
    private ListView mGridView;
    private AdapterView.OnItemClickListener mMenuItemClickListener;
    private LinearLayout mGridViewBg;
    private String[]            mItemTexts;
    private RecyclerView mRvRecentPics;
    private ProgressBar mProgressBar;
    private QuickSelectImageAdapter mQuickSelectAdapter;
    private QuickSelectImgPresenter mPresenter;
    private List<String> mSelectedPicUrls = new ArrayList<>();
    public UploadItemPopMenu(Activity activity) {
        super();
        this.mActivity = activity;
        mPresenter = new QuickSelectImgPresenter(activity);
        mPresenter.attachView(this);
        initViews();
        setItems(activity.getResources().getStringArray(R.array.menu_upload_image_texts));
        mPresenter.loadRecentImages(activity);
    }

    public UploadItemPopMenu(Activity activity, AttributeSet attr) {
        super();
        this.mActivity = activity;
        initViews();
    }

    private void initViews() {
        mRootView = mActivity.getLayoutInflater().inflate(R.layout.layout_upload_item_pop_menu, null);
        mRootView.setOnClickListener(this);
        mGridView = (ListView)mRootView.findViewById(android.R.id.list);
        mGridViewBg = (LinearLayout)mRootView.findViewById(android.R.id.content);
        mRootView.findViewById(android.R.id.button1).setOnClickListener(this);
        mGridView.setOnItemClickListener(this);
        mRvRecentPics = (RecyclerView) mRootView.findViewById(R.id.rv_recent_pics);

        mQuickSelectAdapter = new QuickSelectImageAdapter();
        mQuickSelectAdapter.setListener(new QuickSelectImageAdapter.Listener() {
            @Override
            public void onImageChecked(int checkedNum) {
                if(checkedNum == 0){
                    mItemTexts[0] = "拍照";
                }else{
                    mItemTexts[0] = "确定(已选" + checkedNum + "张)";
                }
                create();
            }

            @Override
            public void onImagesExceedMaxNum() {
                ViewHub.showShortToast(mActivity, "最多只能选9张图片");
            }
        });
        mRvRecentPics.setAdapter(mQuickSelectAdapter);

        LinearLayoutManager layoutManger = new LinearLayoutManager(mActivity);
        layoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvRecentPics.setLayoutManager(layoutManger);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);

    }

    public UploadItemPopMenu setItems(String... items) {
        this.mItemTexts = items;
        return this;
    }
    public UploadItemPopMenu setSelectedPicUrls(List<ImageViewModel> pics){
        for(ImageViewModel pic : pics){
            mSelectedPicUrls.add(pic.getUrl());
        }
        return this;
    }

    private UploadItemPopMenu create() {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < mItemTexts.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", mItemTexts[i]);
            maps.add(map);
        }
        String[] from = new String[] {"text"};
        int[] to = new int[] {android.R.id.title};
        SimpleAdapter adapter = new SimpleAdapter(mActivity, maps, com.nahuo.library.R.layout.bottom_menu_list_item, from, to);
        mGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return this;
    }

    /**
     * @description 显示菜单栏
     * @created 2015年3月20日 上午11:22:23
     * @author JorsonWong
     */
    public void show() {

        create();

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        this.setContentView(mRootView);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x50000000);
        this.setBackgroundDrawable(dw);

        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        mGridViewBg.setVisibility(View.VISIBLE);
        mGridViewBg.startAnimation(AnimationUtils.loadAnimation(mActivity, com.nahuo.library.R.anim.bottom_menu_appear));
        setAnimationStyle(com.nahuo.library.R.style.LightPopDialogAnim);

    }

    public int getStatusHeight() {
        Rect frame = new Rect();
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        return statusHeight;
    }

    /**
     * @description item click callback
     * @created 2015年3月20日 上午11:21:15
     * @author JorsonWong
     */
    public UploadItemPopMenu setOnMenuItemClickListener(AdapterView.OnItemClickListener listener) {
        this.mMenuItemClickListener = listener;
        return this;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mMenuItemClickListener != null) {
            mMenuItemClickListener.onItemClick(parent, view, position, id);
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void dismiss() {
        mPresenter.detachView(false);
        if (mGridViewBg.isShown()) {
            Animation anim = AnimationUtils.loadAnimation(mActivity, com.nahuo.library.R.anim.bottom_menu_disappear);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mGridViewBg.clearAnimation();
                    mGridViewBg.setVisibility(View.GONE);
                    dismiss();
                }
            }, anim.getDuration());
            mGridViewBg.startAnimation(anim);
        } else {
            super.dismiss();
        }
    }

    @Override
    public void showLoading(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRecentImgsLoaded(List<MediaStoreImage> imgs) {
        if(ListUtils.isEmpty(imgs)){
            mRvRecentPics.setVisibility(View.GONE);
        }else{
            mQuickSelectAdapter.setData(imgs);
            mQuickSelectAdapter.addSelectedPicUrls(mSelectedPicUrls);
            mQuickSelectAdapter.notifyDataSetChanged();
        }

    }
    /**
     *@author ZZB
     *@desc 是否选择了图片
     */
    public boolean hasSelectedImages(){
        return !ListUtils.isEmpty(mQuickSelectAdapter.getSelectedPics());
    }

    public List<String> getSelectedImgs(){
        return mQuickSelectAdapter.getSelectedPics();
    }
    /**
     *@author ZZB
     *@desc 获取已选择的图片，包括传进来的，不在最近列表的图片
     */
    public List<String> getSelectedImgsWithInit(){
        return mQuickSelectAdapter.getSelectedPicWithInit();
    }


}