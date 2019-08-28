package com.nahuo.quicksale.Topic;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListView;
import com.nahuo.library.controls.pulltorefresh.PullToRefreshListViewEx;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.adapter.HotActivityAdapter;
import com.nahuo.quicksale.api.ActivityAPI;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;

import java.io.File;
import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */
public class CollectionActivityTabFragment extends Fragment implements
        PullToRefreshListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener, AdapterView.OnItemClickListener {


    public static final String TAG = "ActivityTabFragment";
    private PullToRefreshListViewEx mListView;
    private List<TopicInfoModel> listDatas;
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private HotActivityAdapter mAdapter;
    private View mContentView;
    private boolean mIsLoadingActivity;
    private int lookTopicIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mContentView == null) {
            mContentView = inflater.inflate(R.layout.layout_mynote_list,
                    container, false);
            initListView();
            // 获取数据
            firstLoadData();
        }
        if (mContentView.getParent() != null) {
            ((ViewGroup) mContentView.getParent()).removeView(mContentView);
        }
        return mContentView;
    }

    private void initListView() {
        mListView = (PullToRefreshListViewEx) mContentView
                .findViewById(R.id.recommend_listview);
        View emptyView = LayoutInflater.from(mContentView.getContext()).inflate(
                R.layout.layout_empty_view, null);
        ((TextView)emptyView.findViewById(R.id.empty_txt)).setText("您木有收藏任何活动哟~");
        mListView.setEmptyView(emptyView,new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                firstLoadData();
            }
        });
        mListView.setCanLoadMore(true);
        mListView.setCanRefresh(true);
        mListView.setMoveToFirstItemAfterRefresh(true);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);

        mAdapter = new HotActivityAdapter(mContentView.getContext());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this) ;
        mListView.setAutoLoadMore(true) ;
    }


    /**
     * @description 首次加载数据
     * @created 2015-2-11 上午10:45:33
     * @author ZZB
     */
    private void firstLoadData() {
        new LoadActivityInfoTask(true).execute();
    }

    /**
     * /** 绑定款式列表
     * */
    private void bindItemData(boolean isRefresh) {
        if (mIsLoadingActivity) {
            return;
        }
        if (isRefresh) {
            mPageIndex = 1;
            new LoadActivityInfoTask(isRefresh).execute();
        } else {
            mPageIndex++;
            new LoadActivityInfoTask(isRefresh).execute();
        }

    }

    @Override
    public void onLoadMore() {
        bindItemData(false);
    }

    @Override
    public void onRefresh() {
        bindItemData(true);
        mListView.setCanLoadMore(true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        lookTopicIndex = position - 1;
        if (lookTopicIndex < 0)
            lookTopicIndex = 0;
        if (lookTopicIndex >= mAdapter.getCount()) {
            lookTopicIndex = mAdapter.getCount() - 1;
        }
        TopicInfoModel model = mAdapter.getItem(lookTopicIndex);
        if (model == null) {

            return;
        }
        Intent intent = new Intent(parent.getContext(),
                PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_TID, model.getID());
        intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL,
                Const.getShopLogo(model.getUserID()));
        intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, model.getTitle());
        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE,
                Const.PostType.ACTIVITY);
        startActivityForResult(intent , 0);
    }

    /**
     * 加载活动信息
     *
     * @author nahuo9
     *
     */
    private class LoadActivityInfoTask extends AsyncTask<Void, Void, String> {
        private boolean mIsRefresh = false;

        public LoadActivityInfoTask(boolean isRefresh) {
            mIsRefresh = isRefresh;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mAdapter.setData(listDatas);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(Void... params) {
            File cacheFile = null;
            if (mAdapter.getCount() == 0) {
                cacheFile = CacheDirUtil.getCache(BWApplication.getInstance(),
                        "collectionactivityTab_" + mPageIndex);
                if (cacheFile.exists()) {
                    List<TopicInfoModel> list = GsonHelper.jsonToObject(
                            CacheDirUtil.readString(cacheFile),
                            new TypeToken<List<TopicInfoModel>>() {
                            });
                    if (!list.isEmpty()) {
                        listDatas = list;
                        publishProgress();
                    }
                }
            }
            try {
                List<TopicInfoModel> result = (List<TopicInfoModel>) ActivityAPI
                        .getMyCollectionActivitys(BWApplication.getInstance(), mPageIndex,
                                mPageSize, cacheFile);
                if (mIsRefresh) {
                    listDatas = result;
                } else {
                    listDatas.addAll(result);
                }

                return "OK";
            } catch (Exception ex) {
                Log.e(TAG, "加载帖子信息发生异常");
                ex.printStackTrace();
                return ex.getMessage() == null ? "操作异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.equals("OK")) {
                mListView.showErrorView();
            } else {
                if (listDatas.size() % mPageSize != 0) {
                    mListView.setCanLoadMore(false);
                }
                mAdapter.setData(listDatas);
                mAdapter.notifyDataSetChanged();
                if (listDatas.size()==0)
                { mListView.showEmptyView(); }
            }
            if (mIsRefresh) {
                mListView.onRefreshComplete();
            } else {
                mListView.onLoadMoreComplete();
            }
        }
    }

}
