package com.nahuo.quicksale.Topic;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.nahuo.quicksale.adapter.NewHotPostAdapter;
import com.nahuo.quicksale.api.TopicAPI;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;

import java.io.File;
import java.util.List;

/**
 * Created by 诚 on 2015/9/21.
 */
public class CollectionNoteTabFragment extends Fragment implements
        PullToRefreshListView.OnLoadMoreListener, PullToRefreshListView.OnRefreshListener, AdapterView.OnItemClickListener {

    private PullToRefreshListViewEx listview_note;
    private List<TopicInfoModel> datas; // 帖子数据
    private int mPageIndex = 1;
    private int mPageSize = 20;
    private NewHotPostAdapter mAdapter;
    private View mContentView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.layout_mynote_list, container,
                false);

        initListView();

        // 获取数据
        firstLoadData();

        return mContentView;
    }

    private void initListView() {
        listview_note = (PullToRefreshListViewEx) mContentView
                .findViewById(R.id.recommend_listview);
        View emptyView = LayoutInflater.from(mContentView.getContext()).inflate(
                R.layout.layout_empty_view, null);
        ((TextView) emptyView.findViewById(R.id.empty_txt)).setText("您木有收藏任何帖子哟~~");
        listview_note.setEmptyView(emptyView, new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                firstLoadData();
            }
        });
        listview_note.setCanLoadMore(true);
        listview_note.setCanRefresh(true);
        listview_note.setMoveToFirstItemAfterRefresh(true);
        listview_note.setOnRefreshListener(this);
        listview_note.setOnLoadListener(this);

        mAdapter = new NewHotPostAdapter(mContentView.getContext());
        listview_note.setAdapter(mAdapter);
        listview_note.setOnItemClickListener(this);
        listview_note.setAutoLoadMore(true);
    }

    /**
     * @description 首次加载数据
     * @created 2015-2-11 上午10:45:33
     * @author ZZB
     */
    private void firstLoadData() {
        new LoadTopicInfoTask(true).execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * /** 绑定款式列表
     */
    private void bindItemData(boolean isRefresh) {
        if (isRefresh) {
            mPageIndex = 1;
            new LoadTopicInfoTask(isRefresh).execute();
        } else {
            mPageIndex++;
            new LoadTopicInfoTask(isRefresh).execute();
        }

    }

    @Override
    public void onLoadMore() {
        bindItemData(false);
    }

    @Override
    public void onRefresh() {
        bindItemData(true);
        listview_note.setCanLoadMore(true);
    }

    /*
     * 加载帖子信息
     */
    private class LoadTopicInfoTask extends AsyncTask<Void, Void, String> {
        private boolean mIsRefresh = false;

        public LoadTopicInfoTask(boolean isRefresh) {
            mIsRefresh = isRefresh;
        }

        @Override
        protected String doInBackground(Void... params) {
            File cacheFile = null;
            if (mAdapter.getCount() == 0) {
                cacheFile = CacheDirUtil.getCache(BWApplication.getInstance(),
                        "notetab_cache");
                if (cacheFile.exists()) {
                    List<TopicInfoModel> topicInfoList = GsonHelper
                            .jsonToObject(CacheDirUtil.readString(cacheFile),
                                    new TypeToken<List<TopicInfoModel>>() {
                                    });
                    if (!topicInfoList.isEmpty()) {
                        datas = topicInfoList;
                        publishProgress();
                    }
                }
            }
            try {
                // 更换uri
                List<TopicInfoModel> result = (List<TopicInfoModel>) TopicAPI
                        .getMyCollegeTopics(BWApplication.getInstance(), mPageIndex,
                                mPageSize, cacheFile);

                if (mIsRefresh) {
                    datas = result;
                } else {
                    datas.addAll(result);
                }

                return "OK";
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex.getMessage() == null ? "操作异常" : ex.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            mAdapter.setData(datas);
            mAdapter.notifyDataSetChanged();
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.equals("OK")) {
                listview_note.showErrorView();
            } else {
                if (datas.size() % mPageSize != 0)
                    listview_note.setCanLoadMore(false);
                mAdapter.setData(datas);
                mAdapter.notifyDataSetChanged();
                if (datas.size() == 0) {
                    listview_note.showEmptyView();
                }
            }
            if (mIsRefresh) {
                listview_note.onRefreshComplete();
            } else {
                listview_note.onLoadMoreComplete();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
//			int postId = data.getIntExtra(PostDetailActivity.EXTRA_TID, 0);
            final TopicInfoModel model = datas.remove(lookTopicIndex);
            mAdapter.notifyDataSetChanged();
            new Thread() {
                public void run() {
                    try {
                        TopicAPI.collection(BWApplication.getInstance().getApplicationContext(), model.getID(), Const.PostType.TOPIC);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int lookTopicIndex;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // 进入活动详情
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


        // 进入活动详情
        Intent intent = new Intent(parent.getContext(), PostDetailActivity.class);
        intent.putExtra(PostDetailActivity.EXTRA_TID, model.getID());
        intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL, Const.getShopLogo(model.getUserID()));
        intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, model.getTitle());
        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, model.getType() == 0 ? Const.PostType.TOPIC
                : Const.PostType.ACTIVITY);
        startActivity(intent);
    }

}
