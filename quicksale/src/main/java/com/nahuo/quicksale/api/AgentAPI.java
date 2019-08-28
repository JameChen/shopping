package com.nahuo.quicksale.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.nahuo.quicksale.common.BaiduStats;
import com.nahuo.quicksale.oldermodel.Agent;
import com.nahuo.quicksale.oldermodel.AgentGroup;
import com.nahuo.quicksale.oldermodel.ApplyItem;
import com.nahuo.quicksale.oldermodel.PublicData;

/**
 * Description:我的代理相关API
 * 2014-7-10上午11:57:47
 */
public class AgentAPI {

    private static final String TAG = AgentAPI.class.getSimpleName();

    /**
     * @description 被拒绝后，下次可以申请代理的时间
     * @created 2015-2-3 下午5:04:58
     * @author ZZB
     * @throws Exception 
     */
    public static String getNextAgentApplyTime(Context context, int userID) throws Exception{
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", String.valueOf(userID));
        String json = HttpUtils.httpPost("shop/agent/GetNextAgentApplyTime", params, PublicData.getCookie(context));
        JSONObject repObj = new JSONObject(json);
        return repObj.getString("TimeTip");
    }
	/**
	 * 取消代理申请
	 * 
	 * @author pengjun
	 * @param userID userID
	 * @param cookie cookie值
	 * */
	public static void cancelAgent(Context context, int userID)
			throws Exception {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userid", String.valueOf(userID));
			String json = HttpUtils.httpPost("shop/agent/cancelAgent", params, PublicData.getCookie(context));
	}
	
    /**
     * @description 申请代理
     * @param message 申请代理的留言
     * @param agentUserId 代理的用户id
     * @created 2014-10-24 上午10:51:34
     * @author ZZB
     */
    public static void applyAgent(Context context, int agentUserId, String message) throws Exception{
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", agentUserId + "");
        params.put("message", message);
        HttpUtils.httpPost("shop/agent/applyagent", params,PublicData.getCookie(context));
    }
    
    /**
     * Description:获取代理分组
     * 2014-7-10下午2:22:54
     * 
     * @throws Exception
     */
    public static List<AgentGroup> getGroups(Context context) throws Exception {
        List<AgentGroup> groups = new ArrayList<AgentGroup>();
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageIndex", "1");
        params.put("pageSize", "99999");
        String result = HttpUtils.httpPost("shop/agent/GetGroups", params,
                cookie);
        Log.d(TAG + ":获取代理分组", result);
        JSONObject repObj = new JSONObject(result);

        JSONArray repArr = repObj.getJSONArray("Datas");
        for (int i = 0; i < repArr.length(); i++) {
            AgentGroup group = new AgentGroup();
            JSONObject groupObj = repArr.getJSONObject(i);
            group.setGroupId(groupObj.getInt("ID"));
            group.setName(groupObj.getString("Name"));
            group.setAgentNum(groupObj.getInt("UserCount"));
            groups.add(group);
        }

        return groups;
    }

    /**
     * Description:添加代理分组
     * 2014-7-10下午2:24:16
     * 
     * @return 如果不出错，返回boolean, 出错返回String
     * @throws Exception
     */
    public static int addGroup(Context context, String groupName) throws Exception {
        BaiduStats.log(context, BaiduStats.EventId.AGENT, "代理分组");
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", groupName);
        String result = HttpUtils.httpPost("shop/agent/AddGroup", params, cookie);
        
        Log.d(TAG + ":添加分组", result);
        JSONObject repObj = new JSONObject(result);
        return repObj.getInt("GroupID");
    }

    /**
     * Description:删除分组
     * 2014-7-10下午4:45:56
     */
    public static boolean deleteGroup(Context context, int groupId) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("groupid", groupId + "");
        String result = HttpUtils.httpPost("shop/agent/deletegroup", params, cookie);
        Log.d(TAG + ":删除分组", result);
        return true;
    }

    /**
     * Description:更新分组信息
     * 2014-7-10下午4:53:45
     */
    public static boolean updateGroup(Context context, int groupId, String name) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("groupid", groupId + "");
        params.put("name", name);
        String result = HttpUtils.httpPost("shop/agent/updategroup", params, cookie);
        Log.d(TAG + ":更新分组", result);
        return true;
    }

    /**
     * Description 获取申请代理用户数目
     * Jul 10, 2014 10:10:34 PM
     */
    public static int getApplyUserCount(Context context) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
//        return 1;
        String result = HttpUtils.httpPost("shop/agent/getapplyusercount", params, cookie);
        return Double.valueOf(result).intValue();
        
    }

    /**
     * Description:获取申请的用户数
     * 2014-7-11上午10:06:25
     */
    public static List<ApplyItem> getApplyUsers(Context context) throws Exception {
        List<ApplyItem> users = new ArrayList<ApplyItem>();
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageIndex", "1");
        params.put("pageSize", "99999");
        String result = HttpUtils.httpPost("shop/agent/getapplyusers", params, cookie);
        Log.d(TAG + ":获取申请代理的用户", result);
//        result = "{'Datas': [{'UserID': 129766,'UserName' : 'xxxxxxxx','Statu': '申请','CreateDate': '2014-07-10 11:34:33','Message': '就是要申请，不加不行' }]}";
        JSONObject repObj = new JSONObject(result);
        
        
        JSONArray dataArr = repObj.getJSONArray("Datas");
        for (int i = 0; i < dataArr.length(); i++) {
            JSONObject userObj = dataArr.getJSONObject(i);
            ApplyItem user = new ApplyItem();
            user.setUserId(userObj.getInt("UserID"));
            user.setHandleState(userObj.getString("Statu"));
            user.setCreateData(userObj.getString("CreateDate"));
            user.setMessage(userObj.getString("Message"));
            user.setName(userObj.getString("UserName"));
            users.add(user);
        }

        return users;
    }

    /**
     * Description:同意代理申请
     * 2014-7-11上午10:18:06
     */
    public static boolean agreeAgent(Context context, int userId) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userId + "");
        String result = HttpUtils.httpPost("shop/agent/agreeagent", params, cookie);
        Log.d(TAG + ":同意代理申请", result);
        return true;
    }

    /**
     * Description:拒绝代理申请
     * 2014-7-11上午10:19:21
     */
    public static boolean rejectAgent(Context context, int userId) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userId + "");
        String result = HttpUtils.httpPost("shop/agent/rejectagent", params, cookie);
        Log.d(TAG + ":拒绝代理申请", result);
        return true;
    }

    /**
     * Description:获取所有的代理用户数目
     * 2014-7-11上午11:37:33
     */
    public static int getAllAgentUserCount(String cookie) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        String result = HttpUtils.httpPost("shop/agent/getmyagentusercount", params, cookie);
        Log.d(TAG + ":获取所有的代理用户数目", result);
        return Double.valueOf(result).intValue();
    }

    /**
     * Description:获取所有代理
     * 2014-7-11上午11:50:38
     */
    public static List<Agent> getAllAgentUsers(Context context) throws Exception {
        return getGroupUsers(context, -1);
    }

    /**
     * Description:获取分组成员
     * 2014-7-11下午6:19:58
     */
    public static List<Agent> getGroupUsers(Context context, int groupId) throws Exception {
        return getAgents(context, null, groupId);
    }

    /**
     * Description:获取代理
     *2014-7-16上午10:09:52
     */
    private static List<Agent> getAgents(Context context, String keyword, int groupId) throws Exception{
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        if (groupId > 0) {
            params.put("groupId", groupId + "");
        }
        if(keyword != null){
            params.put("keyword", keyword);
        }
        params.put("pageIndex", "1");
        params.put("pageSize", "99999");

        String result = HttpUtils.httpPost("shop/agent/getmyagentusers", params, cookie);
        Log.d(TAG + ":获取分组用户", result);
        JSONObject repObj = new JSONObject(result);
        JSONArray repArr = repObj.getJSONArray("Datas");
        List<Agent> users = new ArrayList<Agent>();
        for (int i = 0; i < repArr.length(); i++) {
            JSONObject userObj = repArr.getJSONObject(i);
            Agent user = new Agent();
            user.setId(userObj.getInt("UserID"));
            user.setName(userObj.getString("UserName"));
            user.setCreateDate(userObj.getString("CreateDate"));
            ArrayList<AgentGroup> groups = new ArrayList<AgentGroup>();
            JSONArray groupArr = userObj.getJSONArray("Groups");
            for(int j=0; j<groupArr.length(); j++){
                JSONObject groupObj = groupArr.getJSONObject(j);
                AgentGroup group = new AgentGroup(groupObj.getInt("ID"), groupObj.getString("Name"));
                groups.add(group);
            }
            user.setGroups(groups);
            users.add(user);
        }
        return users;
    }
    
    /**
     * Description:获取黑名单
     * 2014-7-11下午2:28:07
     * 
     * @throws Exception
     */
    public static List<Agent> getBlackList(Context context) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageIndex", "1");
        params.put("pageSize", "99999");
        String result = HttpUtils.httpPost("shop/agent/getblacklistusers", params, cookie);
        Log.d(TAG + ":获取黑名单", result);
        JSONObject repObj = new JSONObject(result);
        JSONArray repArr = repObj.getJSONArray("Datas");
        List<Agent> users = new ArrayList<Agent>();
        for (int i = 0; i < repArr.length(); i++) {
            JSONObject userObj = repArr.getJSONObject(i);
            Agent user = new Agent();
            user.setId(userObj.getInt("UserID"));
            user.setName(userObj.getString("UserName"));
            user.setCreateDate(userObj.getString("CreateDate"));
            user.setMemo(userObj.getString("Memo"));
            users.add(user);
        }
        return users;
    }

    /**
     * Description:获取黑名单数目
     * 2014-7-11下午2:55:35
     */
    public static int getBlackListCount(Context context) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();
        String result = HttpUtils.httpPost("shop/agent/getblacklistusercount", params, cookie);
        Log.d(TAG + ":获取黑名单数目", result);
        return Double.valueOf(result).intValue();
    }

    /**
     * Description:批量添加黑名单
     * 2014-7-11下午4:36:11
     * 
     * @throws Exception
     */
    public static boolean addBlackList(Context context, int... userIds) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        for (Integer id : userIds) {
            sb.append(id).append(",");
        }
        params.put("userids", sb.toString());
        String result = HttpUtils.httpPost("shop/agent/addblacklistusers", params, cookie);
        Log.d(TAG + ":添加黑名单", result);
        return true;
    }

    /**
     * Description:移除黑名单
     * 2014-7-12 下午12:51:37
     */
    public static boolean removeBlackList(Context context, int... userIds) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        for (Integer id : userIds) {
            sb.append(id).append(",");
        }
        params.put("userids", sb.toString());
        String result = HttpUtils.httpPost("shop/agent/removeblacklistusers", params, cookie);
        Log.d(TAG + ":移除黑名单", result);
        return true;
    }

    /**
     * Description:分组
     * 2014-7-11下午4:29:02
     * 
     * @throws Exception
     */
    public static boolean addGroupUsers(Context context, int groupId, int... userIds) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        for (Integer id : userIds) {
            sb.append(id).append(",");
        }
        params.put("groupId", groupId + "");
        params.put("userids", sb.toString());
        String result = HttpUtils.httpPost("shop/agent/addgroupusers", params, cookie);
        Log.d(TAG + ":分组", result);
        return true;
    }

    /**
     * Description:批量移除组用户
     * 2014-7-11下午4:32:50
     * 
     * @throws Exception
     */
    public static boolean removeGroupUsers(Context context, int groupId, int... userIds) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();

        StringBuilder sb = new StringBuilder();
        for (Integer id : userIds) {
            sb.append(id).append(",");
        }
        params.put("groupId", groupId + "");
        params.put("userids", sb.toString());
        String result = HttpUtils.httpPost("shop/agent/removegroupusers", params, cookie);
        Log.d(TAG + ":批量移除组用户", result);
        return true;
    }

    /**
     * Description:为商品添加组
     * 2014-7-12 下午5:46:42
     * @throws Exception 
     */
    public static boolean addItemGroups(Context context, int itemId, String groupIds) throws Exception {
        String cookie = PublicData.getCookie(context);
        Map<String, String> params = new HashMap<String, String>();

        params.put("groupIds", groupIds);
        params.put("itemId", itemId + "");
        String result = HttpUtils.httpPost("shop/agent/additemgroups", params, cookie);
        Log.d(TAG + ":商品添加组可视范围", result);
        return true;
    }
    
    /**
     * Description:搜索代理
     *2014-7-16上午10:10:38
     */
    public static List<Agent> searchAgents(Context context, String keyword) throws Exception{
        return getAgents(context, keyword, -1);
    }
    
     
    
    

}
