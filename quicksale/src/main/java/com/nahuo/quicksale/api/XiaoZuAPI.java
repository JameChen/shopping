package com.nahuo.quicksale.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.NewsListModel;
import com.nahuo.quicksale.oldermodel.NewsModel;
import com.nahuo.quicksale.oldermodel.PublicData;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @description 小组相关api
 * @created 2014-12-17 下午5:47:57
 * @author ZZB
 */
public class XiaoZuAPI {
	public static final int XIAOZU_GBZ = 60090;
	public static final int XIAOZU_TC = 60051;
    
    /**
     * @description 吐槽建议
     * @created 2014-12-17 下午5:46:57
     * @author ZZB
     */
    public static void suggestions(Context context, String content) throws Exception{
        Map<String, String> params = new HashMap<String, String>();
        params.put("content", content);
        params.put("gid", XIAOZU_TC + "");
        HttpUtils.httpPost("xiaozu/tweet/add", params, PublicData.getCookie(context));
    }

    /**
     * @description 获取广播站帖子的列表
     * @author PJ
     */
    public static List<NewsListModel> getNewsList(Context context,int pageSize,int pageIndex) throws Exception{
        List<NewsListModel> newsList = null;
		try {
	        String cookie = PublicData.getCookie(context);
	        String json = HttpUtils.httpPost("xiaozu/topic/list/"+XIAOZU_GBZ+"/"+pageIndex+"/"+pageSize, new HashMap<String, String>(),
	                cookie);
	        Log.d("XiaoZuAPI:获取帖子列表", json);

			// 解析数据
	        newsList = GsonHelper
					.jsonToObject(json,
							new TypeToken<List<NewsListModel>>() {
							});
		} catch (Exception ex) {
			Log.e("XiaoZuAPI", MessageFormat.format("{0}->{1}方法发生异常：{2}", "XiaoZuAPI",
					"getNewsList", ex.getMessage()));
			ex.printStackTrace();
			throw ex;
		}
		return newsList;
    }

    /**
     * @description 获取广播站帖子详细信息
     * @author PJ
     */
    public static NewsModel getNewsDetail(Context context,int id) throws Exception{
    	NewsModel newsItem = null;
		try {
	        String cookie = PublicData.getCookie(context);
	        String json = HttpUtils.httpPost("xiaozu/topic/"+id, new HashMap<String, String>(),
	                cookie);
	        Log.d("XiaoZuAPI:获取帖子", json);

			// 解析数据
	        newsItem = GsonHelper
					.jsonToObject(json,
							new TypeToken<NewsModel>() {
							});
	        	SpManager.setLastNewsIDs(context, newsItem.getID());
		} catch (Exception ex) {
			Log.e("XiaoZuAPI", MessageFormat.format("{0}->{1}方法发生异常：{2}", "XiaoZuAPI",
					"getNewsList", ex.getMessage()));
			ex.printStackTrace();
			throw ex;
		}
		return newsItem;
    }

    /**
     * @description 获取是否有新的广播帖子
     * @author PJ
     */
    public static boolean getHasNews(Context context) throws Exception{
		try {
	        String cookie = PublicData.getCookie(context);
	        String json = HttpUtils.httpPost("xiaozu/topic/latest/"+XIAOZU_GBZ, new HashMap<String, String>(),
	                cookie);

	        JSONObject repObj = new JSONObject(json);
	        int lastNewsID = repObj.getInt("ID");
	        int saveLastNewsID = SpManager.getLastNewsIDs(context);
	        if (lastNewsID == saveLastNewsID)
	        {
	        	return false;
	        }
	        else
	        {
	        	return true;
	        }
		} catch (Exception ex) {
			Log.e("XiaoZuAPI", MessageFormat.format("{0}->{1}方法发生异常：{2}", "XiaoZuAPI",
					"getNewsList", ex.getMessage()));
			ex.printStackTrace();
			throw ex;
		}
    }
}
