package com.nahuo.quicksale.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.ActivityCategoryModel;
import com.nahuo.quicksale.oldermodel.ActivityInfoModel;
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
public class ActivityAPI {
    /**
     * 获取活动详情页信息
     *
     * @param cookie 当前登录用户cookie值
     */
    public static TopicDetailModel getActivityDetailInfo(Context context,
                                                         int activityID, File cache) throws Exception {
        String cookie = SpManager.getCookie(context);
        HashMap<String, String> parms = new HashMap<String, String>();
        String json = HttpUtils.httpPost(
        "xiaozu/activity/" + activityID, parms, PublicData.getCookie(context));

        json = ImageUrlExtends.replaceImgSrc(context, json);
        TopicDetailModel mTopicDetailModel = GsonHelper.jsonToObject(json,
                TopicDetailModel.class);

        if (mTopicDetailModel != null) {
            CacheDirUtil.saveString(cache, json);
        }
        return mTopicDetailModel;
    }

    /**
     * 获取我的收藏的活动
     *
     * @param cookie 当前登录用户cookie值
     */
    public static List<TopicInfoModel> getMyCollectionActivitys(Context context,
                                                                int pageIndex, int pageSize, File saveFile) throws Exception {
        String cookie = SpManager.getCookie(context);

        HashMap<String, String> parms = new HashMap<String, String>();
        parms.put("pageindex", pageIndex + "");
        parms.put("pagesize", pageSize + "");


        String json = HttpUtils.httpPost("xiaozu/my/collect/activitys/", parms, PublicData.getCookie(context));
        List<TopicInfoModel> list = GsonHelper.jsonToObject(json,
                new TypeToken<List<TopicInfoModel>>() {
                });
        if (saveFile != null) {
            CacheDirUtil.saveString(saveFile, json);
        }
        return list;
    }

    /**
     * 获取评论列表信息
     *
     * @param cookie 当前登录用户cookie值
     */
    public static List<PostsListModel> getPostsListInfo(Context context,
                                                        int pageIndex, int pageSize, int tid, File saveFile)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        // params.put("pageindex", String.valueOf(pageIndex));
        // params.put("pagesize", String.valueOf(pageSize));
        // params.put("tid", tid);
        String cookie = SpManager.getCookie(context);

        String json = HttpUtils.httpPost("xiaozu/activity/posts/list/"
                + tid + "/" + pageIndex + "/" + pageSize, params, PublicData.getCookie(context));

        List<PostsListModel> mPostsListModels = GsonHelper.jsonToObject(json,
                new TypeToken<List<PostsListModel>>() {
                });
        if (saveFile != null && mPostsListModels != null
                && mPostsListModels.size() > 0) {
            CacheDirUtil.saveString(saveFile, json);
        }
        return mPostsListModels;
    }
    /**
     * 添加活动
     *
     * @param gid
     *            小组ID
     * @param activity
     *            数据
     * @param cookie
     *            cookie值
     * */
    public static boolean add(Context context, String gid,
                              ActivityInfoModel activity) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("gid", gid);
        params.put("title", activity.getTitle());
        params.put("content", activity.getContent());
        params.put("images", activity.getImagesJsonStr());
        params.put("cid", activity.getType());
        String cookie = SpManager.getCookie(context);
      //  String json = APIHelper.post(context, "/xiaozu/activity/add", params,
            //    cookie);
        String json = HttpUtils.httpPost(  "/xiaozu/activity/add", params, PublicData.getCookie(context));
        return true;
    }


    /**
     * @description 获取招代理页活动
     * @created 2015-2-6 下午6:10:28
     * @author ZZB
     */
    public static List<ActivityCategoryModel> getActivityCategorys(
            Context context, File saveFile) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String cookie = SpManager.getCookie(context);


        String json = HttpUtils.httpPost( "xiaozu/activity/categorys", params, PublicData.getCookie(context));


        List<ActivityCategoryModel> posts = GsonHelper.jsonToObject(json,
                new TypeToken<List<ActivityCategoryModel>>() {
                });
        if (posts != null && posts.size() > 0) {
            CacheDirUtil.saveString(saveFile, json);
        }
        return posts;
    }
}
