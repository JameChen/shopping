package com.nahuo.quicksale.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.PostsListModel;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.TopicDetailModel;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 诚 on 2015/9/21.
 */
public class TopicAPI {

    /**
     * 获取我收藏的帖子
     *
     * @param cookie
     *            当前登录用户cookie值
     * */
    public static List<TopicInfoModel> getMyCollegeTopics(Context context,
                                                          int pageIndex, int pageSize, File saveFile) throws Exception {
        String cookie = SpManager.getCookie(context);

        Map<String, String> params = new HashMap<String, String>();
        params.put("pageindex", pageIndex + "");
        params.put("pagesize", pageSize + "");



        String json = HttpUtils.httpPost("xiaozu/my/collect/topics/", params, PublicData.getCookie(context));

        List<TopicInfoModel> topicInfoList = GsonHelper.jsonToObject(json,
                new TypeToken<List<TopicInfoModel>>() {
                });
        if (saveFile != null) {
            CacheDirUtil.saveString(saveFile, json);
        }
        return topicInfoList;
    }

    /**
     * 添加帖子
     *
     * @param gid
     *            小组ID
     * @param topic
     *            数据
     * @param cookie
     *            cookie值
     * */
    public static boolean add(Context context, String gid, TopicInfoModel topic)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("gid", gid);
        params.put("title", topic.getTitle());
        params.put("content", topic.getContent());
        params.put("images", topic.getImagesJsonStr());

        HttpUtils.httpPost("/xiaozu/topic/add", params, PublicData.getCookie(context));
        return true;
    }

    /**
     * 收藏
     *
     * @param tid
     *            topic id
     * @param cookie
     *            cookie值
     * @throws Exception
     * */
    public static String collection(Context context, int pid, Const.PostType type)
            throws Exception {

        String url = "/xiaozu/topic/collect/";
        switch (type) {
            case ACTIVITY:
                // tvTitle.setText("活动");
                url = "/xiaozu/activity/collect/";
                break;
            case TOPIC:
                // tvTitle.setText("帖子");
                url = "/xiaozu/topic/collect/";
                break;
            default:
                break;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", pid + "");

        String json = HttpUtils.httpPost(url, params, PublicData.getCookie(context));
        return json;

    }

    /**
     * 获取评论列表信息
     *
     * @param cookie
     *            当前登录用户cookie值
     * */
    public static List<PostsListModel> getPostsListInfo(Context context,
                                                        int pageIndex, int pageSize, int tid, File saveFile)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        // params.put("pageindex", String.valueOf(pageIndex));
        // params.put("pagesize", String.valueOf(pageSize));
        // params.put("tid", tid);


        String json = HttpUtils.httpPost("xiaozu/topic/posts/list/" + tid
                + "/" + pageIndex + "/" + pageSize, params, PublicData.getCookie(context));


        List<PostsListModel> mPostsListModels = GsonHelper.jsonToObject(json,
                new TypeToken<List<PostsListModel>>() {
                });
        if (saveFile != null && !mPostsListModels.isEmpty()) {
            CacheDirUtil.saveString(saveFile, json);
        }
        return mPostsListModels;
    }

    /**
     * 赞帖子
     *
     * @param tid
     *            topic id
     * @param cookie
     *            cookie值
     * */
    public static void like(Context context, int tid) throws Exception {
        Map<String, String> params = new HashMap<String, String>();

        String json = HttpUtils.httpPost("/xiaozu/topic/like/" + tid, params, PublicData.getCookie(context));
    }

    /**
     * 获取活动详情页信息
     *
     * @param cookie
     *            当前登录用户cookie值
     * */
    public static TopicDetailModel getTopicDetailInfo(Context context,
                                                      int activityID, File saveFile) throws Exception {

        Map<String, String> params = new HashMap<String, String>();

        String json = HttpUtils.httpPost( "xiaozu/topic/" + activityID, params, PublicData.getCookie(context));

        json = ImageUrlExtends.replaceImgSrc(context, json);
        TopicDetailModel mTopicDetailModel = GsonHelper.jsonToObject(json,
                TopicDetailModel.class);
        if (mTopicDetailModel != null) {
            CacheDirUtil.saveString(saveFile, json);
        }
        return mTopicDetailModel;
    }

    /**
     * 获取帖子信息
     *
     * @param cookie
     *            当前登录用户cookie值
     * */
    public static List<TopicInfoModel> getTopicInfo(Context context, int gid,
                                                    int pageIndex, int pageSize, File cacheFile) throws Exception {
        String cookie = SpManager.getCookie(context);


        Map<String, String> params = new HashMap<String, String>();
//        String json = HttpsUtils.httpPost("xiaozu/topic/list/" + gid + "/"
//                + pageIndex + "/" + pageSize, params, PublicData.getCookie(context));
        String json = HttpUtils.httpPost("xiaozu/topic/list_v2/" + gid + "/"
                + pageIndex + "/" + pageSize, params, PublicData.getCookie(context));
        List<TopicInfoModel> topicInfoList = GsonHelper.jsonToObject(json,
                new TypeToken<List<TopicInfoModel>>() {
                });
        if (cacheFile != null && topicInfoList != null) {
            CacheDirUtil.saveString(cacheFile, json);
        }
        return topicInfoList;
    }
}
