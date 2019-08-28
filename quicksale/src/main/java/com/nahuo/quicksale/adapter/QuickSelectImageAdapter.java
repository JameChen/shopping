package com.nahuo.quicksale.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.nahuo.library.helper.DisplayUtil;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.oldermodel.MediaStoreImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZZB on 2015/7/9 16:43
 */
public class QuickSelectImageAdapter extends BaseRecyclerAdapter<MediaStoreImage> implements View.OnClickListener {

    private List<String> mSelectedPics = new ArrayList<>();
    private float mColHeight, mMinColWidth;
    private int mMaxSelectableNum = 9;
    private List<String> mNotInRecentButSelectedPicUrls = new ArrayList<>();//不在最近照片，却选择了的照片的数目
    @Override
    protected int getItemViewLayoutId() {
        return R.layout.rvitem_quick_select_imgs;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder holder, int position) {
        View itemView = holder.getItemView();
        initColHeight(holder.mContext);
        final ImageView iv = $(itemView, R.id.iv_img);
        final CheckBox cb = $(itemView, R.id.cb_select);


        final MediaStoreImage img = mData.get(position);

        cb.setChecked(img.isChecked);

        final String path = img.path;
        float orgH = img.height;
        float orgW = img.width;
        float scale = mColHeight / orgH;
        float newW = orgW * scale;
        newW = newW < mMinColWidth ? mMinColWidth : newW;//如果小于最小宽度，就使用最小宽度
        cb.setTag(img);
        itemView.setTag(img);
        Picasso.with(holder.mContext).load(new File(path)).resize((int) newW, (int) mColHeight).placeholder(R.drawable.empty_photo).into(iv);
        cb.setOnClickListener(this);
        itemView.setOnClickListener(this);

    }

    private void initColHeight(Context context) {
        if (mColHeight == 0) {
            int padding = 2 * DisplayUtil.dip2px(context, 4);
            mColHeight = DisplayUtil.dip2px(context, 180) - padding;
            mMinColWidth = DisplayUtil.dip2px(context, 50);//最小宽度50dp
        }
    }

    public List<String> getSelectedPics() {
        return mSelectedPics;
    }
    /**
     *@author ZZB
     *@desc 获取已选择的图片，包括传进来的，不在最近列表的图片
     */
    public List<String> getSelectedPicWithInit(){
        mNotInRecentButSelectedPicUrls.addAll(mSelectedPics);
        return mNotInRecentButSelectedPicUrls;
    }
    @Override
    public void onClick(View v) {
        CheckBox cb = null;
        switch (v.getId()) {
            case R.id.cb_select:
                cb = (CheckBox) v;
                break;
            default:
                cb = $(v, R.id.cb_select);
                break;
        }
        MediaStoreImage img = (MediaStoreImage) v.getTag();
        String path = img.path;
        boolean isChecked = !img.isChecked;
        if (getTotalSelectedNum() >= mMaxSelectableNum && mListener != null && isChecked) {
            mListener.onImagesExceedMaxNum();
            cb.setChecked(false);
            return;
        }
        img.isChecked = isChecked;
        cb.setChecked(img.isChecked);
        if (isChecked) {
            mSelectedPics.add(path);
        } else {
            mSelectedPics.remove(path);
        }
        if (mListener != null) {
            mListener.onImageChecked(getTotalSelectedNum());
        }
    }
    /**
     *@author ZZB
     *@desc 获取已经选择的图片数目，包括不在recent pics里面的
     */
    private int getTotalSelectedNum(){
        return mSelectedPics.size() + mNotInRecentButSelectedPicUrls.size();
    }
    public static interface Listener {
        public void onImageChecked(int checkedNum);

        public void onImagesExceedMaxNum();
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void addSelectedPicUrls(List<String> picUrls) {
        if(ListUtils.isEmpty(picUrls)){
            return;
        }
        Map<String, MediaStoreImage> map = new HashMap<>();
        for (MediaStoreImage img : mData) {
            map.put(img.path, img);
        }
        for (String url : picUrls) {
            MediaStoreImage img = map.get(url);
            if(img != null){
                img.isChecked = true;
                mSelectedPics.add(url);
            }else{
                mNotInRecentButSelectedPicUrls.add(url);
            }
        }
        mListener.onImageChecked(getTotalSelectedNum());
        notifyDataSetChanged();
    }


}
