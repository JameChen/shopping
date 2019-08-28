package com.nahuo.quicksale.api;

import android.content.Context;

import com.nahuo.quicksale.oldermodel.PublicData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 诚 on 2015/9/21.
 */
public class ReplyAPI {

    /**
     * 保存活动信息
     * @param aid
     * @param content
     * @param rootid
     * @param pid
     * @param cookie
     * @return
     * @throws Exception
     */
    public static String saveActivityPost(Context context,int tid, String content,int rootid,int pid)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("aid", Integer.toString(tid));
        params.put("content", content);
        params.put("rootid", rootid == 0 ? "null" : (rootid + ""));
        params.put("pid", Integer.toString(pid));




        String json = HttpUtils.httpPost("xiaozu/activity/savepost", params, PublicData.getCookie(context));

        return json;
    }

    /**
     * 保存帖子信息
     * @param tid
     * @param content
     * @param rootid
     * @param pid
     * @param cookie
     * @return
     * @throws Exception
     */
    public static String saveTopicPost(Context context,int tid, String content,Object rootid,int pid)
            throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tid", Integer.toString(tid));
        params.put("content", content);
        params.put("rootid", rootid.toString());
        params.put("pid", Integer.toString(pid));



        String json = HttpUtils.httpPost("xiaozu/topic/savepost", params, PublicData.getCookie(context));

        return json;
    }



}