package com.nahuo.quicksale.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;

/**
 * Created by 诚 on 2015/9/21.
 */
public class HotActivityAdapter extends BaseHotAdapter{

    public HotActivityAdapter(Context context)
    {
        mContext = context;
    }

    private int mGridViewWidth;
    @Override
    protected void doChildView(View commonView, TopicInfoModel model) {
        String []urls = null ;
        if(model.getImages()!=null){
            urls = new String[model.getImages().size()] ;
            GridView gridView = (GridView)commonView ;
            populateGridView(gridView , model.getImages().toArray(urls));
            gridView.setTag(model) ;
            if(gridView.getOnItemClickListener()== null){
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        TopicInfoModel model = (TopicInfoModel) parent.getTag() ;


                       Intent intent = new Intent(mContext, PostDetailActivity.class);
                       intent.putExtra(PostDetailActivity.EXTRA_TID, model.getID());
                       intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL, Const.getShopLogo(model.getUserID()));
                      intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, model.getTitle());
                     intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
                     ((Activity)mContext).startActivityForResult(intent , 1 );
                    }
                });
            }
        }
    }

    @Override
    protected View inflaterView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.item_hotactivity, null);
    }
    // 九宫格展示
    private void populateGridView(final GridView gridView, final String[] urls) {
        final PicGridViewAdapter gridAdapter = new PicGridViewAdapter(gridView.getContext());
        gridView.setAdapter(gridAdapter);
        gridView.setNumColumns(urls.length < 3 ? urls.length : 3);
        if (mGridViewWidth == 0) {
            gridView.post(new Runnable() {
                @Override
                public void run() {
                    mGridViewWidth = gridView.getMeasuredWidth();
                    gridAdapter.setGridViewWidth(gridView.getMeasuredWidth());
                    gridAdapter.setData(urls);
                    gridAdapter.notifyDataSetChanged();
                }
            });
        } else {
            gridAdapter.setGridViewWidth(mGridViewWidth);
            gridAdapter.setData(urls);
            gridAdapter.notifyDataSetChanged();
        }

    }
}
