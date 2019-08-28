package com.nahuo.quicksale.api;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.CacheDirUtil;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.AgentGroup;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.TopicInfoModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 诚 on 2015/9/21.
 */
public class GroupAPI {

    /**
     * 获取置顶小组
     *
     * @param cookie
     *            cookie值
     * */
    public static List<TopicInfoModel> getTopGroupInfo(Context context,
                                                       String gid, File cacheFile) throws Exception {
         Map<String, String> params = new HashMap<String, String>();
        String cookie = SpManager.getCookie(context);

        String json = HttpUtils.httpPost( "xiaozu/topic/toplist?gid=" + gid
                + "&top=3", params, PublicData.getCookie(context));
        List<TopicInfoModel> result = GsonHelper.jsonToObject(json,
                new TypeToken<List<TopicInfoModel>>() {
                });
        if (result.size() > 0) {
            CacheDirUtil.saveString(cacheFile, json);
        }
        return result;
    }

    /**
     * 获取某个小组的详细信息
     *
     * @param cookie
     *            cookie值
     * */
    public static AgentGroup getGroupInfo(Context context, String gid,
                                          File cacheFile) throws Exception {
         Map<String, String> params = new HashMap<String, String>();
        String cookie = SpManager.getCookie(context);


        String json = HttpUtils.httpPost("xiaozu/group/"+ gid, params, PublicData.getCookie(context));
        AgentGroup result = GsonHelper.jsonToObject(json,
                new TypeToken<AgentGroup>() {
                });
        if (result != null && result.getGroupID() != 0) {
            CacheDirUtil.saveString(cacheFile, json);
        }
        return result;
    }

    /**
     * 加入小组
     *
     * @param cookie
     *            cookie值
     * */
    public static void joinGroup(Context context, String gid, String content)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("gid", gid);
        params.put("content", content);
        HttpUtils.httpPost( "xiaozu/group/join", params, PublicData.getCookie(context));
    }

    /**
     * 退出小组
     *
     * @param cookie
     *            cookie值
     * */
    public static void exitGroup(Context context, String gid) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("gid", gid);
        HttpUtils.httpPost("xiaozu/group/exit", params, PublicData.getCookie(context));
    }

}
